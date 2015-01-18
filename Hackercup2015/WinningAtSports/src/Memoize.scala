/**
 * Simple memoization and memoization for recursive functions
 */
object Memoize {

  def memoize[X, Y](f: (X) => Y) = {
    val cache = collection.mutable.Map[X, Y]()
    (x: X) => cache.getOrElseUpdate(x, f(x))
  }

  def memoizeWithInternalRecursion[X, Y](f: (X, (X) => Y) => Y) = {
    lazy val ret: (X) => Y = memoize(f(_, ret))
    ret
  }

}

object MemoizeTest extends App{
  import Memoize._

  def log( s : => String ): Unit ={
    System.out.println( s )
  }

  var fibCounter = 0
  def fib(n:Int) : Int = {
    log( "fib:" + n )
    fibCounter += 1
    n match{
      case 0 => 0
      case 1 => 1
      case n => fib(n-1) + fib(n-2)
    }
  }

  var fibRecCounter = 0
  def fibRec( n:Int, recurseOn: (Int)=>Int ) = {
    log( "fibRec:" + n )
    fibRecCounter += 1
    n match {
      case 0 => 0
      case 1 => 1
      case n => recurseOn(n-1) + recurseOn(n-2)
    }
  }

  // Only the "external" calls to fib are memoized
  val fibonaci_notSoGood = memoize(fib)

  // All the calls, "internal" and "external" are memoized
  val fibonaci = memoizeWithInternalRecursion(fibRec)

  assert( fibonaci_notSoGood(20) == fibonaci(20) )
  log( "fibCounter:" + fibCounter )
  log( "fibRecCounter:" + fibRecCounter )
  assert( fibCounter == 21891 )
  assert( fibRecCounter == 21 )
}
