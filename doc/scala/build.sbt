name := "example-scala-learn"

version := "0.0.1"

scalaVersion := "2.11.7"

// additional libraries
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0" % "provided"
)

libraryDependencies += "org.elasticsearch" % "elasticsearch-hadoop" % "5.2.0"
