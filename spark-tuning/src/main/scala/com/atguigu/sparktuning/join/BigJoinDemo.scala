package com.atguigu.sparktuning.join

import com.atguigu.sparktuning.utils.InitUtil
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object BigJoinDemo {

  def main( args: Array[String] ): Unit = {

    val sparkConf = new SparkConf().setAppName("BigJoinDemo")
      .set("spark.sql.shuffle.partitions", "36")
    val sparkSession: SparkSession = InitUtil.initSparkSession(sparkConf)
    useJoin(sparkSession)

  }


  def useJoin( sparkSession: SparkSession ) = {
    //查询出三张表 并进行join 插入到最终表中
    val saleCourse = sparkSession.sql("select *from default.sale_course")
    val coursePay = sparkSession.sql("select * from default.course_pay")
      .withColumnRenamed("discount", "pay_discount")
      .withColumnRenamed("createtime", "pay_createtime")
    val courseShoppingCart = sparkSession.sql("select * from default.course_shopping_cart")
      .drop("coursename")
      .withColumnRenamed("discount", "cart_discount")
      .withColumnRenamed("createtime", "cart_createtime")

    courseShoppingCart
      .join(coursePay, Seq("orderid"), "left")
      .join(saleCourse, Seq("courseid"), "right")
      .select("courseid", "coursename", "status", "pointlistid", "majorid", "chapterid", "chaptername", "edusubjectid"
        , "edusubjectname", "teacherid", "teachername", "coursemanager", "money", "orderid", "cart_discount", "sellmoney",
        "cart_createtime", "pay_discount", "paymoney", "pay_createtime", "default.sale_course.dt", "default.sale_course.dn")
      .write.mode(SaveMode.Overwrite).saveAsTable("default.salecourse_detail_1")

  }

}
