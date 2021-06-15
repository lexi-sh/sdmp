package sdmp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.sql.DataFrameWriter
import sdmp.entities._

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

case class FileLogger(basePath: String) extends SdmpLogger {

  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  private val writer = mapper.writer().withDefaultPrettyPrinter()

  override def log(message: LoggedOutput): Unit = {

    val filePath = Paths.get(s"$basePath/sdmp.json")
    val backupFilePath = Paths.get(s"$basePath/sdmp_backup.json")
    Files.createDirectories(filePath.getParent)
    val messages = if (!Files.exists(filePath)) {
      Seq(message)
    } else {
      val fileStr = Files.readAllLines(filePath).toArray().mkString("")

      val currentMessages: List[LoggedOutput] = mapper.readValue(fileStr, classOf[List[LoggedOutput]])

      Files.deleteIfExists(backupFilePath)
      Files.copy(filePath, backupFilePath)
      Files.delete(filePath)

      currentMessages ++ Seq(message)
    }

    val jsonStr = writer.writeValueAsString(messages)

    Files.write(filePath, jsonStr.getBytes(StandardCharsets.UTF_8))
  }

  implicit class implicits[U](val writer: DataFrameWriter[U]) {
    def sdmpParquet(path: String, description: String = ""): Unit = {
      val stackTrace = StackTraceGenerator.getStackTraceForLogging()
      log(LoggedOutput(path, Parquet, description, InProgress, stackTrace))
      writer.parquet(path)
      log(LoggedOutput(path, Parquet, description, Completed, stackTrace))
    }
  }
}