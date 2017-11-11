package com.undergrowth;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description hdfs本地
 * @date 2017-11-11-18:48
 */
public class HdfsLocalDemo {

  public static void main(String[] args) throws IOException, URISyntaxException {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);
    //
    //要上传的所在的本地路径
    Path src = new Path(HdfsLocalDemo.class.getClassLoader().getResource("people.txt").toURI());
    //要上传的hdfs的目标路径
    Path dst = new Path("D:/");
    fs.copyFromLocalFile(src, dst);
    //
    fs.copyToLocalFile(new Path("D:/people.txt"), new Path("D:/tmp"));
    fs.close();
  }

}
