package question01


import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Get, HBaseAdmin, HTable, Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object HbaseUtil {
  def createSparkSession(className: String): SparkSession = {
    val sparkConf = new SparkConf().setAppName(className)

    if (!sparkConf.contains("spark.master")) {
      sparkConf.setMaster("local[*]")
    }

    Console.out.println("master: " + sparkConf.get("spark.master"))

    SparkSession.builder().config(sparkConf).getOrCreate()
  }
  def hbaseClient(): Unit ={
    import org.apache.hadoop.hbase.HBaseConfiguration
    import org.apache.hadoop.hbase.client.ConnectionFactory
    val conf = HBaseConfiguration.create
    conf.set("hbase.zookeeper.quorum", "106.52.78.85")
    conf.set("hbase.zookeeper.property.clientPort", "8081")
    val conn = ConnectionFactory.createConnection(conf)
    println(conn)
    val tb = conn.getTable(TableName.valueOf("gps_geo")).asInstanceOf[HTable]
    println(tb)
    //创建查询的get对象
    val get = new Get(Bytes.toBytes("0000000y"))
    //指定列列族信息
    // get.addColumn(Bytes.toBytes("info"), Bytes.toBytes("sex"));
    get.addFamily(Bytes.toBytes("g"))
    //执⾏行行查询
    val res = tb.get(get)
    println(res)
    val cells = res.rawCells //获取改⾏行行的所有cell对象
    for (cell <- cells) { //通过cell获取rowkey,cf,column,value
      val cf = Bytes.toString(CellUtil.cloneFamily(cell))
      val column = Bytes.toString(CellUtil.cloneQualifier(cell))
      val value = Bytes.toString(CellUtil.cloneValue(cell))
      val rowkey = Bytes.toString(CellUtil.cloneRow(cell))
      println(rowkey + "----" + cf + "---" + column + "---" + value)
    }
    tb.close()
    conn.close()
  }

  def scanhbaseTable(): Unit ={

    val conf = HBaseConfiguration.create
    conf.set("hbase.zookeeper.quorum", "10.0.0.10,10.0.0.4,10.0.0.14")
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    val conn = ConnectionFactory.createConnection(conf)
    val tb = conn.getTable(TableName.valueOf("gps_geo")).asInstanceOf[HTable]
    val scan = new Scan();
    scan.setStartRow("0000000y".getBytes());
    scan.setStopRow("0000000y".getBytes());
    val resultScanner = tb.getScanner(scan);
    //    resultScanner.forEach(
    //      result => {
    //        val cells = result.rawCells()
    //        for (cell <- cells) {
    //          //通过cell获取rowkey,cf,column,value
    //          val cf = Bytes.toString(CellUtil.cloneFamily(cell));
    //          val column = Bytes.toString(CellUtil.cloneQualifier(cell));
    //          val value = Bytes.toString(CellUtil.cloneValue(cell));
    //          val rowkey = Bytes.toString(CellUtil.cloneRow(cell));
    //          println(rowkey + "----" + cf + "--" + column + "---" + value);
    //        }
    //      }
    //    )
    tb.close();
    conn.close()
  }

  def sparkReadDataFromJdbcHive(): Unit = {
    val spark = createSparkSession("test")
    val jdbcDF = spark.read.format("jdbc")
      .option("url", "jdbc:hive2://106.52.78.85:8081/default?allowMultiQueries=true")
      .option("driver", "org.apache.hive.jdbc.HiveDriver")
      .option("dbtable","gps_geo")
      .option("user", "hadoop").option("password", "Pswdforx123!")
      .load().rdd
      .collect().size
  }

  //  def sh(): Unit = {
  ////    val sparkConf = new SparkConf().setAppName("Spark-HBase").setMaster("local[4]")
  ////    sparkConf.set("spark.hbase.host", "10.0.0.10") //e.g. 192.168.1.1 or localhost or your hostanme
  //    val spark = createSparkSession("test")
  //    val sparkConf = spark.sparkContext.getConf
  //    sparkConf.set("hbase.zookeeper.quorum", "106.52.78.85")
  //    sparkConf.set("hbase.zookeeper.property.clientPort", "8081")
  //    val sc = spark.sparkContext
  //    val docRdd = sc.hbaseTable[(Option[String], Option[String])]("gps_geo")
  //      .select("geohash").inColumnFamily("g")
  //    println("Number of Records found : " + docRdd.count())
  //  }

  def readHbase(): Unit ={
    val sparkConf = new SparkConf().setAppName("HBaseRead").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val conf = HBaseConfiguration.create()
    val tableName = "gps_geo"

    conf.set("hbase.master", "10.0.0.16:6005")
    conf.setInt("timeout", 120000)
    conf.set("hbase.zookeeper.quorum", "10.0.0.10,10.0.0.4,10.0.0.14")
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    println("Number of Records found : " + hBaseRDD.count())
    sc.stop()
  }

  def main(args: Array[String]): Unit = {
    readHbase()
  }
}
