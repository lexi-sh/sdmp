
scalaVersion := "2.11"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5" % "provided"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.11.904"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"