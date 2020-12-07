
scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5" % "provided"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.11.904"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.3"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.3"
resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"