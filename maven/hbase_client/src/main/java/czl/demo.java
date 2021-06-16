package czl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class demo {
    Configuration conf=null;
    Connection conn=null;
    HBaseAdmin admin =null;

    @Before
    public void init() throws IOException {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","worker1,worker2");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conn = ConnectionFactory.createConnection(conf);
    }
    //删除⼀一条数据
    @Test
    public void deleteData() throws IOException {
//需要获取⼀一个table对象
        final Table worker = conn.getTable(TableName.valueOf("relationship"));
//准备delete对象
        final Delete delete = new Delete(Bytes.toBytes("uid1"));
        delete.addColumn(Bytes.toBytes("friends"),Bytes.toBytes("uid3"));
        List<Cell> cells = delete.getFamilyCellMap().get(Bytes.toBytes("friends"));
        Cell cell = cells.get(0);
        System.out.println(Bytes.toString(CellUtil.cloneRow(cell)));
        System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
        System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));

        worker.close();
        System.out.println("删除数据成功！！");
    }


    public static void main(String[] args) throws IOException {
        new demo().init();
    }

}
