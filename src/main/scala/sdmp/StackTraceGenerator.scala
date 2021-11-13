package sdmp

object StackTraceGenerator {
  def getStackTraceForLogging(): String = {
    val stackTrace = Thread.currentThread().getStackTrace.drop(4).mkString("\n")
    stackTrace
  }
}
