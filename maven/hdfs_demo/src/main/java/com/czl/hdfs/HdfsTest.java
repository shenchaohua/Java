package com.czl.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HdfsTest {
    FileSystem fileSystem = null;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        Configuration configuration = new Configuration();
        fileSystem = FileSystem.get(new URI("hdfs://worker1:9000"), configuration, "root");
    }
    @After
    public void destroy() throws IOException {
        fileSystem.close();
    }

    @Test
    public void testMkdirs() throws URISyntaxException, IOException, InterruptedException {
        boolean mkdirs = fileSystem.mkdirs(new Path("/api_test"));
    }
    @Test
    public void copyFromLocalFile() throws IOException {
        fileSystem.copyFromLocalFile(new Path("D:\\workstate\\Java\\maven\\hdfs_demo\\pom.xml"),new Path("/pom.xml"));
    }
    @Test
    public void isFile() throws IOException {
        boolean file = fileSystem.isFile(new Path("/"));
        System.out.println(file);
    }

    @Test
    public void downloadFile() throws IOException {
        FSDataInputStream inputStream = fileSystem.open(new Path("/test.txt"));
        IOUtils.copyBytes(inputStream,System.out,1,true);
    }
}
