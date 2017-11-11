# 参考
# 概念
## fs
## namenode
- 通过ssh与各个数据节点通信
## datanode
# 配置
## 伪分布式
- core-site.xml
```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```
- hdfs-site.xml
```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```
- hdfs namenode -format
- bin/start-dfs.sh----NameNode - http://localhost:50070/
- 操作
- stop-dfs.sh