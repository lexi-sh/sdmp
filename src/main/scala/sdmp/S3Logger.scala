package sdmp

import com.amazonaws.services.s3.AmazonS3
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.sql.DataFrameWriter
import sdmp.entities.{Completed, InProgress, LoggedOutput, Parquet}

case class S3Logger(s3client: AmazonS3, bucket: String, key: String) extends SdmpLogger {
  override def log(message: LoggedOutput): Unit = {

    synchronized {
      val messages = if (!s3client.doesObjectExist(bucket, s"$key/sdmp.json")) {
        Seq(message)
      } else {
        val fileStream = s3client.getObject(bucket, s"$key/sdmp.json").getObjectContent
        val fileStr = scala.io.Source.fromInputStream(fileStream).mkString

        val currentMessages: List[LoggedOutput] = mapper.readValue(fileStr, classOf[List[LoggedOutput]])

        s3client.putObject(bucket, s"$key/sdmp_backup.json", fileStr)
        fileStream.close()

        currentMessages ++ Seq(message)
      }

      val jsonStr = writer.writeValueAsString(messages)

      s3client.putObject(bucket, s"$key/sdmp.json", jsonStr)
    }
  }
}
