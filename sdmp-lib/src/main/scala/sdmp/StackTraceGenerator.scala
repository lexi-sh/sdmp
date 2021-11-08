package sdmp

object StackTraceGenerator {
  def getStackTraceForLogging(): String = {
    val stackTrace = Thread.currentThread().getStackTrace.drop(2).mkString("\n")
    stackTrace
  }
}
