package test

//import org.apache.hadoop.hbase.client.{ConnectionFactory, Get, HTable, Scan}
//import org.apache.hadoop.hbase.spark.datasources.HBaseTableCatalog
//import org.apache.hadoop.hbase.util.Bytes
//import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, TableName}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil.convertScanToString
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes

object HbaseUtil {
  def createSparkSession(className: String): SparkSession = {
    val sparkConf = new SparkConf().setAppName(className)

    if (!sparkConf.contains("spark.master")) {
      sparkConf.setMaster("local[*]")
    }
    Console.out.println("spark.sql.autoBroadcastJoinThreshold" + sparkConf.get("spark.sql.autoBroadcastJoinThreshold"))
    Console.out.println("master: " + sparkConf.get("spark.master"))

    SparkSession.builder().config(sparkConf).getOrCreate()
  }

//  def hbaseClient(): Unit = {
//    import org.apache.hadoop.hbase.HBaseConfiguration
//    import org.apache.hadoop.hbase.client.ConnectionFactory
//    val conf = HBaseConfiguration.create
//    conf.set("hbase.zookeeper.quorum", "106.52.78.85")
//    conf.set("hbase.zookeeper.property.clientPort", "8081")
//    val conn = ConnectionFactory.createConnection(conf)
//    println(conn)
//    val tb = conn.getTable(TableName.valueOf("gps_geo")).asInstanceOf[HTable]
//    println(tb)
//    //创建查询的get对象
//    val get = new Get(Bytes.toBytes("0000000y"))
//    //指定列列族信息
//    // get.addColumn(Bytes.toBytes("info"), Bytes.toBytes("sex"));
//    get.addFamily(Bytes.toBytes("g"))
//    //执⾏行行查询
//    val res = tb.get(get)
//    println(res)
//    val cells = res.rawCells //获取改⾏行行的所有cell对象
//    for (cell <- cells) { //通过cell获取rowkey,cf,column,value
//      val cf = Bytes.toString(CellUtil.cloneFamily(cell))
//      val column = Bytes.toString(CellUtil.cloneQualifier(cell))
//      val value = Bytes.toString(CellUtil.cloneValue(cell))
//      val rowkey = Bytes.toString(CellUtil.cloneRow(cell))
//      println(rowkey + "----" + cf + "---" + column + "---" + value)
//    }
//    tb.close()
//    conn.close()
//  }

//  def scanhbaseTable(): Unit = {
//
//    val conf = HBaseConfiguration.create
//    conf.set("hbase.zookeeper.quorum", "10.0.0.10,10.0.0.4,10.0.0.14")
//    conf.set("hbase.zookeeper.property.clientPort", "2181")
//    val conn = ConnectionFactory.createConnection(conf)
//    val tb = conn.getTable(TableName.valueOf("gps_geo")).asInstanceOf[HTable]
//    val scan = new Scan();
//    scan.setStartRow("0000000y".getBytes());
//    scan.setStopRow("0000000y".getBytes());
//    val resultScanner = tb.getScanner(scan);
//    //    resultScanner.forEach(
//    //      result => {
//    //        val cells = result.rawCells()
//    //        for (cell <- cells) {
//    //          //通过cell获取rowkey,cf,column,value
//    //          val cf = Bytes.toString(CellUtil.cloneFamily(cell));
//    //          val column = Bytes.toString(CellUtil.cloneQualifier(cell));
//    //          val value = Bytes.toString(CellUtil.cloneValue(cell));
//    //          val rowkey = Bytes.toString(CellUtil.cloneRow(cell));
//    //          println(rowkey + "----" + cf + "--" + column + "---" + value);
//    //        }
//    //      }
//    //    )
//    tb.close();
//    conn.close()
//  }

  def sparkReadDataFromJdbcHive(): Unit = {
    val spark = createSparkSession("test")
    val jdbcDF = spark.read.format("jdbc")
      .option("url", "jdbc:hive2://106.52.78.85:8081/default?allowMultiQueries=true")
      .option("driver", "org.apache.hive.jdbc.HiveDriver")
      .option("dbtable", "gps_geo")
      .option("user", "hadoop").option("password", "Pswdforx123!")
      .load().rdd
      .collect().size
  }

  def sparkReadDataFromJdbcHBase(): Unit = {
    val spark = createSparkSession("test")
    val sql = spark.sqlContext
    val sparkConf = spark.sparkContext.getConf
    sparkConf.set("spark.sql.warehouse.dir", "/usr/hive/warehouse")
    val df = sql.read.format("org.apache.hadoop.hbase.spark")
      //      .option("hbase.columns.mapping",
      //        "name STRING :key, email STRING c:email, " + "birthDate DATE p:birthDate, height FLOAT p:height")
      .option("hbase.table", "gps_geo")
      .option("hbase.spark.use.hbasecontext", false)
      .load()
  }

//  def shc(): Unit ={
//    val spark = createSparkSession("test")
//    def catalog =
//      s"""{
//         |"table":{"namespace":"default", "name":"gps_geo"},
//         |"rowkey":"key",
//         |"columns":{
//         |"key":{"cf":"rowkey", "col":"geohash", "type":"string"},
//         |"lon":{"cf":"g", "col":"lon", "type":"string"},
//         |"lat":{"cf":"g", "col":"lat", "type":"string"},
//         |"partition_id":{"cf":"g", "col":"pid", "type":"string"},
//         |"town_id":{"cf":"g", "col":"town_id", "type":"string"}
//         |}
//         |}""".stripMargin
//    val sql = spark.sqlContext.read
//      .options(Map(HBaseTableCatalog.tableCatalog->catalog))
//      .format("org.apache.spark.sql.execution.datasources.hbase")
//      .load()
//  }

    def readHbaseByConnector(): Unit ={
      val spark = createSparkSession("test")
      val sql = spark.sqlContext
      val df = sql.read.format("org.apache.hadoop.hbase.spark")
        .option("hbase.columns.mapping",
          "geohash STRING :key, town_id STRING g:town_id,geohash STRING g:geohash" )
        .option("hbase.table", "gps_geo")
        .option("hbase.spark.use.hbasecontext", false)
        .load().collect()
    }

  //    def sparkHbaseConnectorByMavenIt(): Unit = {
  //  //    val sparkConf = new SparkConf().setAppName("Spark-HBase").setMaster("local[4]")
  //  //    sparkConf.set("spark.hbase.host", "10.0.0.10") //e.g. 192.168.1.1 or localhost or your hostanme
  //      val spark = createSparkSession("test")
  //      val sparkConf = spark.sparkContext.getConf
  //      sparkConf.set("hbase.zookeeper.quorum", "10.0.0.10,10.0.0.4,10.0.0.14")
  //      sparkConf.set("hbase.zookeeper.property.clientPort","2181")
  //      sparkConf.set("spark.hbase.host", "10.0.0.16")
  //      sparkConf.set("spark.hbase.port", "6000")
  //      val sc = spark.sparkContext
  //      val docRdd = sc.hbaseTable[(Option[String], Option[String])]("default:gps_geo")
  //        .select("geohash").inColumnFamily("g")
  //      println("Number of Records found : " + docRdd.count())
  //    }

    def readHbaseByInputFormat(): Unit ={
      val sparkConf = new SparkConf().setAppName("HBaseRead")
      sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      sparkConf.set("spark.kryoserializer.buffer.max.mb", "128");
      val sc = new SparkContext(sparkConf)
      val conf = HBaseConfiguration.create()
      val tableName = "gps_geo"
//      conf.set("hbase.master", "10.0.0.16:6005")
      conf.setInt("timeout", 120000)
      conf.set("hbase.zookeeper.quorum", "10.0.0.10,10.0.0.4,10.0.0.14")
      conf.set("hbase.zookeeper.property.clientPort", "2181")
      conf.set(TableInputFormat.INPUT_TABLE, tableName)
//      conf.setLong("hbase.client.scanner.max.result.size",2147483648L)
//      conf.setLong("hbase.server.scanner.max.result.size",2147483648L)
//      val scan = new Scan()
//      scan.addFamily(Bytes.toBytes("g"))
//      scan.setLimit(100)
//      conf.set(TableInputFormat.SCAN, convertScanToString(scan))
      val hBaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
//      .repartition(79*5)
      println(hBaseRDD.map(
        r => {
          val town_id = Bytes.toString(r._2.getValue(Bytes.toBytes("g"), Bytes.toBytes("town_id")))
          val geohash = Bytes.toString(r._1.get()).reverse
          (geohash, town_id)
        }
      ).filter(_._2!="0").count())
      sc.stop()
    }

//  def bulkLoad(): Unit ={
//    val sc = new SparkContext("local", "test")
//    val conf = new HBaseConfiguration()
//    conf.set("hbase.zookeeper.quorum", "10.0.0.10,10.0.0.4,10.0.0.14")
//    conf.setInt("zookeeper.recovery.retry", 0)
//    conf.setInt("hbase.client.retries.number", 0)
//
//    val hbaseContext = new HBaseContext(sc, conf)
//
//    val stagingFolder = new Path("/tmp/hbase/HFileOut")
//    val rdd = sc.parallelize(Array(
//      (Bytes.toBytes("1"),
//        (Bytes.toBytes("g"), Bytes.toBytes("geohash"), Bytes.toBytes("town_id")))))
//
//    rdd.hbaseBulkLoad(hbaseContext,TableName.valueOf("gps_geo"),
//      t => {
//        val rowKey = t._1
//        val family:Array[Byte] = t._2._1
//        val qualifier = t._2._2
//        val value = t._2._3
//
//        val keyFamilyQualifier= new KeyFamilyQualifier(rowKey, family, qualifier)
//        Seq((keyFamilyQualifier, value)).iterator
//      },
//      stagingFolder.toString)
//
//    val load = new LoadIncrementalHFiles(conf)
//    val conn: Connection = ConnectionFactory.createConnection(conf)
//    load.doBulkLoad(new Path(stagingFolder.toUri.getPath),
//      conn.getAdmin, conn.getTable(TableName.valueOf("gps_geo")), conn.getRegionLocator(TableName.valueOf("gps_geo")))
//  }

//  def read(): Unit ={
//    val sc = new SparkContext("local", "test")
//    val config = new HBaseConfiguration()
//    val hbaseContext = new HBaseContext(sc, config)
//
//    rdd.hbaseForeachPartition(hbaseContext, (it, conn) => {
//      val bufferedMutator = conn.getBufferedMutator(TableName.valueOf("gps_geo"))
//      it.foreach((putRecord) => {
//        . val put = new Put(putRecord._1)
//          . putRecord._2.foreach((putValue) => put.addColumn(putValue._1, putValue._2, putValue._3))
//          . bufferedMutator.mutate(put)
//      })
//      bufferedMutator.flush()
//      bufferedMutator.close()
//    })
//  }

  def main(args: Array[String]): Unit = {
    createSparkSession("test")
  }
}
