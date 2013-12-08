package contest

import java.util.Formatter
import java.math.BigInteger
import scala.collection.immutable.IndexedSeq

object CoinsGame extends App with ContestStub{

  def extractOneTest(lineIterator: LineIterator) = Array(lineIterator.next)

  def solveOneTest(loadedTest: LoadedTest) = {
    val Array(n,k,c) = loadedTest(0).split( """\s+""").map(_.toInt)

    object P{
      private val memo = collection.mutable.Map[(Int,Int,Int), Int]()
      private val INF = Int.MaxValue/2

      def apply(n: Int, k: Int, c: Int ): Int = {
        if( c <= 0 ) 0
        else if( n < 1 ) INF
        else if( k < n ) c + n - k
        else {
          val minNumberInJar:Int = k/n
          val safeCoins = n*minNumberInJar
          (safeCoins min c) + apply( n, k-safeCoins, c-safeCoins )
        }
      }
    }


    val sol1 = P(n,k,c)
    val sol2 = 1+P(n-1,k,c)
    val sol = sol1 min sol2
    log( s"sol:$sol   sol1:$sol1  sol2: $sol2")
    sol.toString
  }

  solveAll(".",false)
}
