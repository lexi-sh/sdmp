package sdmp

import sdmp.entities._
import com.amazonaws.services.s3.AmazonS3Client
import org.apache.spark.sql.DataFrameWriter
import com.fasterxml.jackson.module.scala.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.util.Try
import scala.util.Success
import scala.util.Failure

trait SdmpLogger {
    def log(message: LoggedOutput): Unit

    protected val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    protected val writer = mapper.writer().withDefaultPrettyPrinter()

  implicit class implicits[U](val writer: DataFrameWriter[U]) {

    private def action(path: String, description: String = "", action: String => Unit, format: OutputFormat): Unit = {
      val stackTrace = StackTraceGenerator.getStackTraceForLogging()
      log(LoggedOutput(path, format, description, InProgress, stackTrace))
      
      Try(action(path)) match {
          case Success(_) => log(LoggedOutput(path, format, description, Completed, stackTrace))
          case Failure(e) => 
            log(LoggedOutput(path, format, description, Failed, stackTrace))
            throw e
      }
       
      
    }

    def sdmpParquet(path: String, description: String = ""): Unit = {
      action(path, description, writer.parquet _, Parquet)
    }

    def sdmpCsv(path: String, description: String = ""): Unit = {
      action(path, description, writer.csv _, Csv)
    }

    def sdmpJson(path: String, description: String = ""): Unit = {
      action(path, description, writer.json _, Json)
    }
  }
}



