package contest

import java.util.Formatter
import java.math.BigInteger
import scala.collection.immutable.IndexedSeq

object LabelMaker extends App with ContestStub{

  def extractOneTest(lineIterator: LineIterator) = Array(lineIterator.next)

  type Numero = BigDecimal
  implicit class intOps( val i : Int ) extends AnyVal{
    def between( min: Int, max: Int ) = i >= min && i <= max
    def toNumero() = BigDecimal(i)
    def pow( e: Int ) = toNumero.pow(e)
  }
  implicit class charOps( val i : Char ) extends AnyVal{
    def between( min: Char, max: Char ) = i >= min && i <= max
  }
  implicit class stringOps( val s : String ) extends AnyVal{
    def toNumero() = BigDecimal(s)
  }
  implicit class numeroOps(val s: Numero ) extends AnyVal{
    def toString(base:Int) = {
      val bigint = new BigInteger(s.toString)
      bigint.toString(base)
    }
  }


  def solveOneTest(loadedTest: LoadedTest) = {
    object Combinations{
      def numberOfCombinations( symbols: Int, positions: Int ) : Numero = {
        symbols.pow(positions)
      }

      val cache = collection.mutable.Map[(Int,Int), Numero]()

      def numberOfAccumulatedCombinations_nomemo( symbols: Int, positions: Int ) : Numero=  {

        assert( positions >= 0 )
        if( positions == 0 ){
          0.toNumero
        }
        else{
          val noc = numberOfCombinations(symbols,positions)
          val noac = numberOfAccumulatedCombinations(symbols,positions-1)
          log( s"         noc:$noc  noac:$noac  ret:${noc+noac}")
          noc + noac
        }
      }

      def numberOfAccumulatedCombinations( symbols: Int, positions: Int ) : Numero=  {
        assert( positions >= 0 )
        if( positions == 0 ){
          0.toNumero
        }
        else{
          cache.get( (symbols,positions) ) match{
            case Some(ret) =>
              ret
            case None =>
              val noc = numberOfCombinations(symbols,positions)
              val noac = numberOfAccumulatedCombinations(symbols,positions-1)
              val ret = noc + noac
              cache += (symbols,positions) -> ret
              //log( s"         noc:$noc  noac:$noac  ret:${ret}" )
              ret
          }
        }
      }
    }

    val Array(digits, numberS) = loadedTest(0).split( """\s+""")
    val number = numberS.toNumero
    val orderedDigits = digits.sortBy(c=>c)
    val base = orderedDigits.size //ensuring( _ between( Character.MIN_RADIX, Character.MAX_RADIX ) )

    val solutionSize = {
      var ret = 1
      while( Combinations.numberOfAccumulatedCombinations(base,ret) < number ){
        ret += 1
      }
      ret
    }

    val previousCombinations = Combinations.numberOfAccumulatedCombinations(base, solutionSize-1)

    val numberOfCombinationsOfBiggerSize = number - previousCombinations - 1.toNumero

    val retNumber = numberOfCombinationsOfBiggerSize.toString(base)
    val filledRetNumber = retNumber.reverse.padTo(solutionSize,'0').reverse

    log( s"orderedDigits:$orderedDigits" )
    log( s"   over combinations:                ${Combinations.numberOfAccumulatedCombinations(base, solutionSize)}")
    log( s"   number:                           $number")
    log( s"   previousCombinations:             $previousCombinations" )
    log( s"   numberOfCombinationsOfBiggerSize: $numberOfCombinationsOfBiggerSize")
    log( s"   retNumber:                        $retNumber")
    log( s"   filledRetNumber:                  $filledRetNumber")

    val ret = filledRetNumber.map( c => if( c between('0','9') ) orderedDigits(c - '0') else orderedDigits(c - 'a' + 10) )
    log( s"   ret:$ret")
    ret
  }

  solveAll(".",false)
}
