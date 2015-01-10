

trait ContestStub{
  import java.io._
  import java.util._

  type Solution = String
  type Solutions = scala.collection.GenSeq[Solution]
  type LoadedTest = Array[String]


  object log {
    val start = System.currentTimeMillis

    @scala.annotation.elidable(-1)
    def apply(st: => String) {
      val ct = Thread.currentThread
      val now = System.currentTimeMillis - start
      println( now + " - " + (ct.getName) + " - " + st)
    }
  }


  /**
   * GETS AN ITERATOR OF LINES FROM AN INPUT STREAM
   */
  type LineIterator = scala.collection.Iterator[String]

  private[this] def inputStream2LineInterator(in: InputStream): LineIterator = {
    val reader = new BufferedReader(new InputStreamReader(in))
    scala.collection.Iterator.continually(reader.readLine() )
  }


  private[this] def dumpSolution(solutions: Solutions)(dump: (String) => Unit = println) = {
    solutions.zipWithIndex.map {
      case (sol, cas) => dump("Case #" + (cas + 1) + ": " + sol)
    }
  }

  /**
   * MULTIPLEXES OUTPUT TO SEVERAL PRINT STREAMS
   */
  private[this] def printToFiles(writers: PrintStream*): (String) => Unit = {
    (s: String) => writers.map(_.println(s))
  }


  private[this] def readTests( in: LineIterator ) = {
    val numberOfTests = readHeader(in)
    for( t <- 1 to numberOfTests ) yield extractOneTest(in)
  }

  def toScanner( loadedTest: LoadedTest ) : Scanner = {
    val bais = new ByteArrayInputStream( loadedTest.mkString("\n").getBytes("UTF-8") )
    new Scanner(bais)
    
  }

  private[this] def solveTests( loadedTests: Seq[LoadedTest], parallel: Boolean ) : Solutions = {
    val tests = if( parallel ) loadedTests.par else loadedTests.seq
    tests.map( lt => solveOneTest(lt) ).seq
  }

  /**
   * OPENS A FILE, PROCESSES IT AND DUMPS THE SOLUTION TO A FILE AND STANDARD OUTPUT
   */
  private[this] def solveOneFile(parallel: Boolean)(file: String) = {

    log("FILE:" + new File(file).getAbsoluteFile )
    val initTime = System.currentTimeMillis

    val is = new FileInputStream(file)
    val in = inputStream2LineInterator(is)
    val tests = readTests(in)
    val solutions = solveTests(tests,parallel)

    val fileOut = if (file.endsWith(".in"))
      file.replaceAll("in$", "out")
    else
      file + ".out"

    val out = new PrintStream(new FileOutputStream(fileOut), true)
    dumpSolution(solutions)(printToFiles(out, System.out))
    out.close()
    is.close()

    log("Time for file " + file + ":" + (System.currentTimeMillis - initTime))
  }


  def extractOneTest( lineIterator: LineIterator ) : LoadedTest
  def solveOneTest( in: LoadedTest) : Solution

  def solver(parallel: Boolean = true) = solveOneFile(parallel) _

  def readHeader(in: LineIterator): Int = in.next().toInt

  def solveAll( path: String = ".", parallel: Boolean = true ) = {
    val dir = new File(path)
    dir.listFiles.map( _.toString).filter( _.endsWith(".in") ).map(solver(parallel))
  }
}
