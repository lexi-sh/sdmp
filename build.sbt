
scalaVersion := "2.12.15"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.0" % "provided"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.11.904"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.5"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.5"
