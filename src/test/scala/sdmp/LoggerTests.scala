package sdmp


import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3Client
import org.scalatest.{BeforeAndAfter, FlatSpec}
import org.apache.spark.sql.SparkSession

import java.io.File
import java.net.URI
import java.nio.file.{Files, Path, Paths}
import scala.reflect.io.Directory

class LoggerTests extends FlatSpec with BeforeAndAfter {
  before {
    val directory = new Directory(new File("/tmp/testdata"))
    directory.deleteRecursively()
  }

  it should "run the print logger" in {
    val spark = SparkSession.builder().master("local").getOrCreate()
    import spark.implicits._

    val sdmpLogger = PrintLogger()

    import sdmpLogger.implicits
    val df = Seq(TestData(1, "abc")).toDF()

    df.write.sdmpParquet("/tmp/testdata", "test data description")
  }

  it should "run the file logger" in {
    val spark = SparkSession.builder().master("local").getOrCreate()
    import spark.implicits._

    val sdmpLogger = FileLogger("/tmp/logs/")

    import sdmpLogger.implicits
    val df = Seq(TestData(1, "abc")).toDF()

    df.write.sdmpParquet("/tmp/testdata", "test data description")

    val file = Files.readAllLines(Paths.get("/tmp/logs/sdmp.json"));
    val x = 4

  }

  it should "run the s3 logger across multiple threads without issue" in {
    val spark = SparkSession.builder().master("local").getOrCreate()
    import spark.implicits._

//    val s3Client = S3Client.builder().withEndpointConfiguration(new EndpointConfiguration("http://localhost:4566", "local"))
//      .build()

//    val s3Logger = S3Logger(s3Client, "my-bucket", "my-key")

  }
}
