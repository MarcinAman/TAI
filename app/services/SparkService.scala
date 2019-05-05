package services

import javax.inject._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

@Singleton
class SparkService {
  val conf = new SparkConf().setMaster("local[*]").setAppName("Frameless repl").set("spark.ui.enabled", "false")
  implicit val spark = SparkSession.builder().config(conf).appName("TAI").getOrCreate()
  spark.sparkContext.setLogLevel("WARN")
}
