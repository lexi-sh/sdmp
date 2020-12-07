package sdmp

object StackTraceGenerator {
  def getStackTraceForLogging(): String = {
    val stackTrace = Thread.currentThread().getStackTrace.mkString("\n")
    stackTrace
  }
}
