package sdmp

import sdmp.entities.LoggedOutput
import com.amazonaws.services.s3.AmazonS3Client


trait SdmpLogger {
    def log(message: LoggedOutput): Unit
}

case class S3Logger(s3client: AmazonS3Client, bucket: String, key: String) extends SdmpLogger {
    override def log(message: LoggedOutput): Unit = {
        println("s3 logged:" + message.toString())
    }
}

case class PrintLogger() extends SdmpLogger {
    override def log(message: LoggedOutput): Unit = {
        println(message.toString())
    }
}