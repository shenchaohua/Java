package com.atguigu.sparktuning.skew

import com.atguigu.sparktuning.bean.{CourseShoppingCart, SaleCourse}
import com.atguigu.sparktuning.utils.InitUtil
import org.apache.spark.SparkConf
import org.apache.spark.sql._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object SkewJoinNoTuning {
  def main( args: Array[String] ): Unit = {
    val sparkConf = new SparkConf().setAppName("SkewJoinTuning")
      .set("spark.sql.autoBroadcastJoinThreshold", "-1")
      .set("spark.sql.shuffle.partitions", "36")
//          .setMaster("local[*]")
    val sparkSession: SparkSession = InitUtil.initSparkSession(sparkConf)

    scatterBigAndExpansionSmall(sparkSession)

    //    while(true){}
  }


  /**
    * 打散大表  扩容小表 解决数据倾斜
    *
    * @param sparkSession
    */
  def scatterBigAndExpansionSmall( sparkSession: SparkSession ): Unit = {
    import sparkSession.implicits._

    val saleCourse = sparkSession.sql("select * from sale_course")
    val coursePay = sparkSession.sql("select * from course_pay")
      .withColumnRenamed("discount", "pay_discount")
      .withColumnRenamed("createtime", "pay_createtime")

    val courseShoppingCart = sparkSession.sql("select * from course_shopping_cart")
      .withColumnRenamed("discount", "cart_discount")
      .withColumnRenamed("createtime", "cart_createtime")

    val commonCourseShoppingCart: Dataset[Row] = courseShoppingCart.filter(item => item.getAs[Long]("courseid") != 101 && item.getAs[Long]("courseid") != 103)
    val skewCourseShoppingCart: Dataset[Row] = courseShoppingCart.filter(item => item.getAs[Long]("courseid") == 101 || item.getAs[Long]("courseid") == 103)

    saleCourse
      .join(skewCourseShoppingCart.drop("coursename"), Seq("courseid", "dt", "dn"), "right")
      .join(coursePay, Seq("orderid", "dt", "dn"), "left")
      .select("courseid", "coursename", "status", "pointlistid", "majorid", "chapterid", "chaptername", "edusubjectid"
        , "edusubjectname", "teacherid", "teachername", "coursemanager", "money", "orderid", "cart_discount", "sellmoney",
        "cart_createtime", "pay_discount", "paymoney", "pay_createtime", "dt", "dn")
      .write.mode(SaveMode.Overwrite).saveAsTable("salecourse_detail")

//    saleCourse
//      .join(courseShoppingCart.drop("coursename"), Seq("courseid", "dt", "dn"), "right")
//      .join(coursePay, Seq("orderid", "dt", "dn"), "left")
//      .select("courseid", "coursename", "status", "pointlistid", "majorid", "chapterid", "chaptername", "edusubjectid"
//        , "edusubjectname", "teacherid", "teachername", "coursemanager", "money", "orderid", "cart_discount", "sellmoney",
//        "cart_createtime", "pay_discount", "paymoney", "pay_createtime", "dt", "dn")
//      .write.mode(SaveMode.Overwrite).saveAsTable("salecourse_detail")
  }


}
