import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import java.util.Date;
import org.apache.spark.{SparkConf, SparkContext}

object groupByVsDistinct {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]").set("spark.port.maxRetries","1280")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val lines: RDD[String] = sc.textFile("src/main/resources/distinct.txt")
    val l = System.currentTimeMillis()
//    lines.flatMap(x => x.split(" ")).map((_, 1)).reduceByKey(_ + _).map(_._1).collect()
    println(System.currentTimeMillis()-l)
    val array = lines.flatMap(x => x.split(" ")).distinct()
    println(System.currentTimeMillis()-l)

    println(array.toDebugString)

  }
}
