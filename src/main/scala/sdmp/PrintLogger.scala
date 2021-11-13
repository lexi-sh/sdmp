package sdmp

import org.apache.spark.sql.{DataFrame, DataFrameWriter}
import sdmp.entities._
import java.io.PrintWriter

case class PrintLogger(pw: PrintWriter) extends SdmpLogger {
  override def log(message: LoggedOutput): Unit = {
    pw.write(message.toString())
  }
}