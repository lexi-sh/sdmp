package sdmp.entities
case class LoggedOutput(uri: String, format: String, description: String, status: String, size: Double, stackTrace: String) {
    override def toString: String = {
        s"uri: $uri\n" +
          s"format: $format\n" +
          s"description: $description\n" +
          s"status: $status\n" +
          s"size: $size\n" +
          s"stackTrace: $stackTrace"
    }
}

object LoggedOutput {
    def apply(uri: String, format: OutputFormat, description: String, status: OutputStatus, size: Double, stackTrace: String): LoggedOutput = {
        val formatStr = format match {
            case Csv => "csv"
            case Json => "json"
            case Parquet => "parquet"
        }

        val statusStr = status match {
            case Completed => "completed"
            case Empty => "empty"
            case InProgress => "in progress"
        }
        LoggedOutput(uri, formatStr, description, statusStr, size, stackTrace)
    }
}   