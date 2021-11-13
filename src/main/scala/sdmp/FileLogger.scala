package sdmp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.sql.DataFrameWriter
import sdmp.entities._

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

case class FileLogger(basePath: String) extends SdmpLogger {

  override def log(message: LoggedOutput): Unit = {

    val filePath = Paths.get(s"$basePath/sdmp.json")
    val backupFilePath = Paths.get(s"$basePath/sdmp_backup.json")
    Files.createDirectories(filePath.getParent)
    val messages = if (!Files.exists(filePath)) {
      Seq(message)
    } else {
      val fileStr = Files.readAllLines(filePath).toArray().mkString("")

      val currentMessages: Seq[LoggedOutput] = mapper.readValue(fileStr, classOf[Array[LoggedOutput]])

      Files.deleteIfExists(backupFilePath)
      Files.copy(filePath, backupFilePath)
      Files.delete(filePath)

      currentMessages ++ Seq(message)
    }

    val jsonStr = writer.writeValueAsString(messages)

    Files.write(filePath, jsonStr.getBytes(StandardCharsets.UTF_8))
  }
}