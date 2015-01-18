import java.io.{PrintStream, File, FileOutputStream}
import java.util
import java.util.Scanner

/**
 * Created by alvaro on 17/01/15.
 * https://www.facebook.com/hackercup/problems.php?pid=582396081891255&round=344496159068801
 *
 *
Your first-grade math teacher, Mr. Book, has just introduced you to an amazing new concept — primes! According to your notes, a prime is a positive integer greater than 1 that is divisible by only 1 and itself.

Primes seem fun, but without giving you and your 6-year-old colleagues time to consider their implications, he's promptly gone on to define another term: primacity. He explains that the primacity of an integer is the number of distinct primes which divide it. For example, the primacity of 12 is 2 (as it's divisible by primes 2 and 3), the primacity of 550 is 3 (as it's divisible by primes 2, 5, and 11), and the primacity of 7 is 1 (as the only prime it's divisible by is 7).

Following his lesson, Mr. Book has given you homework with some rather mean questions of the following form: Given 3 integers A, B, and K, how many integers in the inclusive range [A, B] have a primacity of exactly K?

Mr. Book probably expects his little homework assignment to take you and your classmates the rest of the year to complete, giving him time to slack off and nap during the remaining math classes. However, you want to learn more things from him instead! Can you use the skills you've learned in your first-grade computer science classes to finish Mr. Book's homework before tomorrow's math class?

Input
Input begins with an integer T, the number of homework questions. For each question, there is one line containing 3 space-separated integers: A, B, and K.

Output
For the ith question, print a line containing "Case #i: " followed by the number of integers in the inclusive range [A, B] with a primacity of K.

Constraints
1 ≤ T ≤ 100
2 ≤ A ≤ B ≤ 107
1 ≤ K ≤ 109

 */
object Homework extends App{

  def log( s : => String ) = {
    //System.err.println( s )
  }

  def measure[T](proc: => T) = {
    val ini = System.currentTimeMillis
    val ret = proc
    val end = System.currentTimeMillis
    System.err.println(s"${end - ini} ms")
    ret
  }


  val limit = 1e7.toInt + 1

  object isPrime extends (Int=>Boolean){
    lazy val notPrimes = measure {
      val a = new util.BitSet()
      for (step <- 2 to limit ; i <- step*2 to limit by step) {
        a.set(i)
      }
      System.err.println( s"Eratostenes computed")
      a
    }

    def apply( index : Int ) = !notPrimes.get(index)
  }

  val primes = measure {
    val primes = (2 to limit).filter(isPrime).toArray
    System.err.println( s"number of primes up to $limit:${primes.size}")
    primes
  }


  object primacity extends ((Int) => Int){
    lazy val primacities = {
      val primacities = new Array[Int](limit+1)
      for( p <- primes ; index <- p to limit by p ){
        primacities(index) += 1
      }
      primacities
    }
    override def apply(i: Int) = primacities(i)
  }


  def primacity_lammer( n : Int ) = {
    var remaining = n
    var ret = 0
    var d = 2
    while( d <= remaining ){
      if( remaining%d == 0 ){
        assert( isPrime(d) )
        ret += 1
        while( remaining%d == 0 ){
          remaining /= d
        }
      }
      d += 1
    }
    log( s"primacity $n: $ret ")
    ret
  }


  def solveCase( a: Int, b: Int, k: Int ) = {
    log( s"a:$a b:$b k:$k")
    (a to b).count( primacity(_) == k )
  }

  def solveFile( in: Scanner, out: PrintStream ) = {
    val T = in.nextInt
    for( t <- 1 to T ){
      val s = solveCase( in.nextInt, in.nextInt, in.nextInt )
      out.println( s"Case #$t: $s")
    }
  }

  def processFile( file: String ) = {
    log( s"Processing: $file" )
    val in = new Scanner( new File(file) )
    val out = new PrintStream( new FileOutputStream( file + ".out" ) )
    measure {
      solveFile(in, out)
    }
    in.close()
    out.close()
  }



  (new File(".")).list().filter( _.toLowerCase endsWith ".in").foreach(processFile)

}

