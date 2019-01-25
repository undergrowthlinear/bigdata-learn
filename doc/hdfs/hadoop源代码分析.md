# 概念
- hadoop是分布式计算机系统框架
## HDFS----分布式文件系统(存储数据)
- 一次写入多次读取/高容错/大数据集/master slave设计架构
- 良好的平台移植性/很强的系统扩展性
### Code
- org.apache.hadoop.fs.FileSystem
  - org.apache.hadoop.hdfs.DistributedFileSystem
  - org.apache.hadoop.fs.LocalFileSystem
### NameNode----管理存储数据的元数据
- 一个NameNode,一个SecondaryNameNode
### DataNode----直接存储数据
- 若干个DataNode
## MapReduce----分布式计算框架(将计算放在数据节点)
- 分为Map任务和Reduce任务/ master(JobTracker) slave(TaskTracker)
## Hadoop Common----通用的工具集