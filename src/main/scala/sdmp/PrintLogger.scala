package sdmp

import org.apache.spark.sql.{DataFrame, DataFrameWriter}
import sdmp.entities._

case class PrintLogger() extends SdmpLogger {
  override def log(message: LoggedOutput): Unit = {
    println(message.toString())
  }
}