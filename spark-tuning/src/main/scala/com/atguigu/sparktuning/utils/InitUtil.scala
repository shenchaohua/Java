package com.atguigu.sparktuning.utils

import java.util.Random

import com.atguigu.sparktuning.bean.{School, Student}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object InitUtil {


  def main( args: Array[String] ): Unit = {
    val sparkConf = new SparkConf().setAppName("InitData")
      .setMaster("local[*]") //TODO 要打包提交集群执行，注释掉
    val sparkSession: SparkSession = initSparkSession(sparkConf)
//    initHiveTable(sparkSession)
//    initBucketTable(sparkSession)
//    saveData(sparkSession)
    sparkSession.sql("drop table sale_course")
    sparkSession.sql("drop table course_shopping_cart")
    sparkSession.sql("drop table course_pay")
  }

  def initSparkSession( sparkConf: SparkConf ): SparkSession = {
    System.setProperty("HADOOP_USER_NAME", "hadoop")
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val ssc = sparkSession.sparkContext
    // TODO 改成自己的地址
    ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://10.0.0.16:4007")
    sparkSession
  }

  def initHiveTable( sparkSession: SparkSession ): Unit = {
    sparkSession.read.json("/test/coursepay.log")
      .write.partitionBy("dt", "dn")
      .format("parquet")
      .mode(SaveMode.Overwrite)
      .saveAsTable("default.course_pay")

    sparkSession.read.json("/test/salecourse.log")
      .write.partitionBy("dt", "dn")
      .format("parquet")
      .mode(SaveMode.Overwrite)
      .saveAsTable("default.sale_course")

    sparkSession.read.json("/test/courseshoppingcart.log")
      .write.partitionBy("dt", "dn")
      .format("parquet")
      .mode(SaveMode.Overwrite)
      .saveAsTable("default.course_shopping_cart")

  }

  def initBucketTable( sparkSession: SparkSession ): Unit = {
//    sparkSession.read.json("/test/coursepay.log")
//      .write.partitionBy("dt", "dn")
//      .format("parquet")
//      .bucketBy(5, "orderid")
//      .sortBy("orderid")
//      .mode(SaveMode.Overwrite)
//      .saveAsTable("default.course_pay_cluster")
    sparkSession.read.json("/test/courseshoppingcart.log")
      .write.partitionBy("dt", "dn")
      .bucketBy(10, "orderid")
      .format("parquet")
      .sortBy("orderid")
      .mode(SaveMode.Overwrite)
      .saveAsTable("default.course_shopping_cart_cluster2")
  }

  def saveData(sparkSession: SparkSession) = {
    import sparkSession.implicits._
    sparkSession.range(1000000).mapPartitions(partitions => {
      val random = new Random()
      partitions.map(item => Student(item, "name" + item, random.nextInt(100), random.nextInt(100)))
    }).write.partitionBy("partition")
      .mode(SaveMode.Append)
      .saveAsTable("sparktuning.test_student")

    sparkSession.range(1000000).mapPartitions(partitions => {
      val random = new Random()
      partitions.map(item => School(item, "school" + item, random.nextInt(100)))
    }).write.partitionBy("partition")
      .mode(SaveMode.Append)
      .saveAsTable("sparktuning.test_school")
  }


}
