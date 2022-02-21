package com.atguigu.sparktuning.rbo

import com.atguigu.sparktuning.utils.InitUtil
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object PredicateTuning {


  def main( args: Array[String] ): Unit = {
    val sparkConf = new SparkConf().setAppName("PredicateTunning")
      .setMaster("local[*]") //TODO 要打包提交集群执行，注释掉
    val sparkSession: SparkSession = InitUtil.initSparkSession(sparkConf)

    sparkSession.sql("use default;")


    println("=======================================Inner on 左表=======================================")
    val innerStr1 =
    """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |  and r.courseid<2
      """.stripMargin
    sparkSession.sql(innerStr1).show()
    sparkSession.sql(innerStr1).explain(mode = "extended")

    println("=======================================Inner where 左表=======================================")
    val innerStr2 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |where r.courseid<2
      """.stripMargin
    sparkSession.sql(innerStr2).show()
    sparkSession.sql(innerStr2).explain(mode = "extended")

    
    println("=======================================left on 左表=======================================")
    val leftStr1 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l left join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |  and l.courseid<2
      """.stripMargin
    sparkSession.sql(leftStr1).show()
    sparkSession.sql(leftStr1).explain(mode = "extended")

    println("=======================================left where 左表=======================================")
    val leftStr2 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l left join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |where l.courseid<2
      """.stripMargin
    sparkSession.sql(leftStr2).show()
    sparkSession.sql(leftStr2).explain(mode = "extended")


    println("=======================================left on 右表=======================================")
    val leftStr3 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l left join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |  and r.courseid<2
      """.stripMargin
    sparkSession.sql(leftStr3).show()
    sparkSession.sql(leftStr3).explain(mode = "extended")

    println("=======================================left where 右表=======================================")
    val leftStr4 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l left join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |where r.courseid<2 + 3
      """.stripMargin
    sparkSession.sql(leftStr4).show()
    sparkSession.sql(leftStr4).explain(mode = "extended")
    

    println("=======================================right on 左表=======================================")
    val leftStr5 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l right join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |  and l.courseid<2
      """.stripMargin
    sparkSession.sql(leftStr5).show()
    sparkSession.sql(leftStr5).explain(mode = "extended")

    println("=======================================right where 左表=======================================")
    val leftStr6 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l right join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |where l.courseid<2
      """.stripMargin
    sparkSession.sql(leftStr6).show()
    sparkSession.sql(leftStr6).explain(mode = "extended")


    println("=======================================right on 右表=======================================")
    val leftStr7 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l right join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |  and r.courseid<2
      """.stripMargin
    sparkSession.sql(leftStr7).show()
    sparkSession.sql(leftStr7).explain(mode = "extended")

    println("=======================================right where 右表=======================================")
    val leftStr8 =
      """
        |select
        |  l.courseid,
        |  l.coursename,
        |  r.courseid,
        |  r.coursename
        |from sale_course l right join course_shopping_cart r
        |  on l.courseid=r.courseid and l.dt=r.dt and l.dn=r.dn
        |where r.courseid<2 + 3
      """.stripMargin
    sparkSession.sql(leftStr8).show()
    sparkSession.sql(leftStr8).explain(mode = "extended")
  }
}
