package contest

object ContestStubTest extends App with ContestStub{

  def extractOneTest(lineIterator: LineIterator) = Array(lineIterator.next)

  def solveOneTest(loadedTest: LoadedTest) = {
    log( s"solving test: ${loadedTest.mkString}")
    (loadedTest(0).toInt + 100).toString
  }

  solveAll()
}
