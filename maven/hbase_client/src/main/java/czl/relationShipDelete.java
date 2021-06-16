package czl;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class relationShipDelete extends BaseRegionObserver {
    @Override
    public void preDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit, Durability durability) throws IOException {
        HTable table = (HTable)e.getEnvironment().getTable(TableName.valueOf("relationship"));
        byte[] rk = delete.getRow();
        List<Cell> cells = delete.getFamilyCellMap().get(Bytes.toBytes("friends"));
        Cell cell = cells.get(0);
        Delete otherUserDelete = new Delete(rk);
        otherUserDelete.addColumn(Bytes.toBytes("friends"),cell.getQualifierArray());
        table.delete(otherUserDelete);
        table.close();
    }
}
