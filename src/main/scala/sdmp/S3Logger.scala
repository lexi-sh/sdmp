package sdmp

import com.amazonaws.services.s3.AmazonS3Client
import org.apache.spark.sql.DataFrameWriter
import sdmp.entities.{Completed, InProgress, LoggedOutput, Parquet}

case class S3Logger(s3client: AmazonS3Client, bucket: String, key: String) extends SdmpLogger {
  override def log(message: LoggedOutput): Unit = {
    println("s3 logged:" + message.toString())
  }

  implicit class implicits[U](val writer: DataFrameWriter[U]) {
    def sdmpParquet(path: String, description: String = ""): Unit = {
      val stackTrace = StackTraceGenerator.getStackTraceForLogging()
      log(LoggedOutput(path, Parquet, description, InProgress, 0D, stackTrace))
      writer.parquet(path)
      log(LoggedOutput(path, Parquet, description, Completed, 0D, stackTrace))
    }
  }
}
