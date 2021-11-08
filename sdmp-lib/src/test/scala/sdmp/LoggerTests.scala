package sdmp


import com.amazonaws.client.builder
import com.amazonaws.client.builder.{AwsClientBuilder, AwsSyncClientBuilder}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.{AmazonS3Client, AmazonS3ClientBuilder}
import org.scalatest.{BeforeAndAfter, FlatSpec}
import org.apache.spark.sql.SparkSession

import java.io.File
import java.net.URI
import java.nio.file.{Files, Path, Paths}
import scala.reflect.io.Directory

import sdmp.entities.LoggedOutput
import org.scalatest.Matchers
import com.fasterxml.jackson.module.scala.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class LoggerTests extends FlatSpec with BeforeAndAfter with Matchers {
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

    val s3Client = AmazonS3ClientBuilder
      .standard()
      .withEndpointConfiguration(
        new EndpointConfiguration("http://localhost:4566", "us-east-1"))
      .enablePathStyleAccess()
      .build()

    val bucket = "sdmp-test-bucket"
    val key = "my-key"

    s3Client.deleteObject(bucket, key)

    val s3Logger = S3Logger(s3Client, bucket, key)

    import s3Logger.implicits
    val df = Seq(TestData(1, "abc")).toDF()

    df.write.sdmpParquet("/tmp/testdata", "test data description")

    val fileStream = s3Client.getObject(bucket, s"$key/sdmp.json").getObjectContent
    val fileStr = scala.io.Source.fromInputStream(fileStream).mkString
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)

    val currentMessages: List[LoggedOutput] = mapper.readValue(fileStr, classOf[List[LoggedOutput]])
    currentMessages.length shouldBe 2
  }
}
