package contest

object ContestStubTest extends App with ContestStub{
  type Solution = Int

  def readOneTest(lineIterator: LineIterator) = Array(lineIterator.next)

  def solveOneTest(loadedTest: LoadedTest) = {
    log( s"solving test: $loadedTest")
    loadedTest(0).toInt + 100
  }

  solveAll()
}
