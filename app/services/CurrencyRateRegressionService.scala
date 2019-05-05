package services


import javax.inject.Inject
import frameless._
import frameless.syntax._
import frameless.ml._
import frameless.ml.feature._
import frameless.ml.regression._
import models.{CurrencyPeriodData, DailyExchangeRates}
import org.apache.spark.ml.linalg.Vector
import org.joda.time.{DateTime, Duration}

class CurrencyRateRegressionService @Inject() (sparkService: SparkService){
  import sparkService._
  import spark.implicits._

  def calculateRegression(periodData: CurrencyPeriodData): CurrencyPeriodData = {
    case class Currency(date: Double, rate: Double)
    val ratesDS = TypedDataset.create(periodData.rates.map(r =>{
      val period = new Duration(r.date, periodData.currencyPeriod.to)
      val x = Currency(period.getStandardDays.toDouble, r.bid.toDouble)
      println(x)
      x
    }))

    case class Features(rate: Double)
    val assembler = TypedVectorAssembler[Features]

    case class CurrencyWithFeatures(date: Double, rate: Double, features: Vector)
    val dataWithFeatures = assembler.transform(ratesDS).as[CurrencyWithFeatures]

    case class LRInputs(date: Double, features: Vector)
    val rf = TypedLinearRegression[LRInputs]

    val model = rf.fit(dataWithFeatures).run()

    case class Result(date: Double, rate: Double, features: Vector, result: Double)
    val resultDS = model.transform(dataWithFeatures).as[Result]

    val result = resultDS.select(resultDS.col('date), resultDS.col('result)).collect.run()

    periodData.copy(rates = result.map(r =>
        DailyExchangeRates(
          periodData.currencyPeriod.to.minusDays(r._1.asInstanceOf[Double].toInt), BigDecimal(r._2.asInstanceOf[Double])
        )
      )
    )
  }
}
