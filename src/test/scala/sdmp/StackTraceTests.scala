package sdmp

import org.scalatest.{FlatSpec, Matchers}

class StackTraceTests extends FlatSpec with Matchers {

  "StackTraceGenerator" should "not have StackTraceGenerator in its stack trace" in {
    val st = StackTraceGenerator.getStackTraceForLogging()
    st should not include "StackTraceGenerator"
    st should include("StackTraceTests.scala:8")
  }
}
