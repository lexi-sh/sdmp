package sdmp

import sdmp.entities.LoggedOutput
import com.amazonaws.services.s3.AmazonS3Client

object SdmpGlobalSession {
    def logUsing(s3client: AmazonS3Client, bucket: String, key: String) {
        
    }
}

trait SdmpLogger {
    def log(message: LoggedOutput): Unit
}

case class S3Logger(s3client: AmazonS3Client, bucket: String, key: String) extends SdmpLogger {
    override def log(message: LoggedOutput): Unit = {

    }
}