package sdmp

import sdmp.entities.LoggedOutput
import com.amazonaws.services.s3.AmazonS3Client

trait SdmpLogger {
    def log(message: LoggedOutput): Unit
}



