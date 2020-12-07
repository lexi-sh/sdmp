package sdmp

import org.apache.spark.sql.{DataFrame, DataFrameWriter}
import sdmp.entities._

case class PrintLogger() extends SdmpLogger {
  override def log(message: LoggedOutput): Unit = {
    println(message.toString())
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