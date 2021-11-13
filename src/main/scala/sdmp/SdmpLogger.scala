package sdmp

import sdmp.entities._
import com.amazonaws.services.s3.AmazonS3Client
import org.apache.spark.sql.DataFrameWriter
import com.fasterxml.jackson.module.scala.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

trait SdmpLogger {
    def log(message: LoggedOutput): Unit

    protected val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    protected val writer = mapper.writer().withDefaultPrettyPrinter()

  implicit class implicits[U](val writer: DataFrameWriter[U]) {

    private def action(path: String, description: String = "", action: String => Unit): Unit = {
      val stackTrace = StackTraceGenerator.getStackTraceForLogging()
      log(LoggedOutput(path, Parquet, description, InProgress, stackTrace))
      action(path)
      log(LoggedOutput(path, Parquet, description, Completed, stackTrace))
    }

    def sdmpParquet(path: String, description: String = ""): Unit = {
      action(path, description, writer.parquet _)
    }
  }
}



