package services


import javax.inject.Inject
import models.{CurrencyPeriodData, DailyExchangeRates}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.joda.time.Duration

class CurrencyRateRegressionService @Inject() (sparkService: SparkService){
  import sparkService._

  def calculateRegression(periodData: CurrencyPeriodData): CurrencyPeriodData = {
    // x -> date (features) y -> value (label)

    val ratesDS = spark.sparkContext.parallelize(periodData.rates.map(r =>{
      val period = new Duration(r.date, periodData.currencyPeriod.to)
      println(r.bid.toDouble, period.getStandardDays.toDouble)
      LabeledPoint(r.bid.toDouble, Vectors.dense(period.getStandardDays.toDouble))
    }))


    val numIterations = 10000
    val stepSize = 0.00000001
    val model = LinearRegressionWithSGD.train(ratesDS, numIterations, stepSize)

    //label (y or the independent variable, meaning the observed end result y)
    // and the prediction (the estimation based on the regression formula determined by the SDG algorithm)
    val valuesAndPreds = ratesDS.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    valuesAndPreds.foreach(result => println(s"predicted label: ${result._2}, actual label: ${result._1}"))

    val MSE = valuesAndPreds.map{ case(v, p) => math.pow( v - p, 2) }.mean()
    println("training Mean Squared Error = " + MSE)


    periodData.copy(rates = valuesAndPreds.map(r =>
      DailyExchangeRates(
        periodData.currencyPeriod.to.minusDays(r._1.toInt), BigDecimal(r._2)
      )
    ).collect()
    )
  }
}

