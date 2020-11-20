package sdmp.entities
case class LoggedOutput(uri: String, format: String, description: String, status: String, size: Double, stackTrace: String)

object LoggedOutput {
    def apply(uri: String, format: OutputFormat, description: String, status: OutputStatus, size: Double, stackTrace: String) {
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