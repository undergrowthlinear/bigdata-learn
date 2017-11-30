package spark

import java.net.URL

import org.apache.spark.Partitioner


/**
  * @description 自定义分区
  * @author zhangwu
  * @date 2017-11-09-23:23
  * @version 1.0.0
  */
class DomainNamePartitioner(numParts: Int) extends Partitioner {
  override def getPartition(key: Any): Int = {
    val domain = new URL(key.toString).getHost()
    val code = (domain.hashCode % numPartitions)
    if (code < 0) {
      code + numPartitions // 使其非负
    } else {
      code
    }
  }

  override def numPartitions: Int = numParts

  // 用来让Spark区分分区函数对象的Java equals方法
  override def equals(other: Any): Boolean = other match {
    case dnp: DomainNamePartitioner =>
      dnp.numPartitions == numPartitions
    case _ =>
      false
  }
}
