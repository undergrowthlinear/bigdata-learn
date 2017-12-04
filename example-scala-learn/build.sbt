name := "example-scala-learn"

version := "0.0.1"

scalaVersion := "2.11.7"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.0" // % "provided"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.1.0" // % "provided"

libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.1.0" // % "provided"

libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "2.1.0" // % "provided"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.0" // % "provided"

libraryDependencies += "com.github.scopt" % "scopt_2.11" % "3.5.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.1.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-flume_2.11" % "2.1.0"




// libraryDependencies += "org.elasticsearch" % "elasticsearch-hadoop" % "5.2.0"
