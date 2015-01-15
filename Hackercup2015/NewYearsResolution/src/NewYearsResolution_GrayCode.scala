import java.io.File
import java.util.Scanner

import scala.annotation.tailrec

/**
 * Created by alvaro on 12/01/15.
 */
object NewYearsResolution_GrayCode extends App {

  implicit class Bits(val v: Int) extends AnyVal {
    def bit(index: Int) = (v & (1 << index)) != 0
    def setBit(index: Int): Int = v | (1 << index)
    def delBit(index: Int) = v & ~(1 << index)
    def setBit(index: Int, b: Boolean): Int = if (b) setBit(index) else delBit(index)
    def flipBit(index: Int) = setBit(index, !bit(index))
  }

  object GrayCodeIndex {

    private val _l2 = Math.log(2)

    @tailrec
    def bitPositionChangeForIndex(i: Int): Int = {
      assert( i > 0 )
      @inline def log2(i: Int) = (Math.log(i) / _l2).toInt
      val b = log2(i)
      val twoB = 1 << b
      if (i == twoB) b else bitPositionChangeForIndex(i - twoB)
    }

    def iterator(bits: Int) = Iterator.from(1).
      map(bitPositionChangeForIndex).
      takeWhile( _ < bits )
  }


  def measure[T](proc: => T) = {
    val ini = System.currentTimeMillis
    val ret = proc
    val end = System.currentTimeMillis
    System.err.println(s"${end - ini} ms")
    ret
  }


  def processFile(file: String) = measure {

    case class Food(p: Int, c: Int, f: Int){
      def +( food: Food ) = Food(p+food.p,c+food.c,f+food.f )
      def -( food: Food ) = Food(p-food.p,c-food.c,f-food.f )
    }


    val in = new Scanner(new File(file))
    val T = in.nextInt()
    for (t <- 1 to T) {
      val gFood = Food( in.nextInt(), in.nextInt(), in.nextInt() )
      val N = in.nextInt()
      val foods = (0 until N).map(_ => Food(in.nextInt(), in.nextInt(), in.nextInt()))

      val subsets = GrayCodeIndex.iterator(N).scanLeft( (0, Food(0,0,0)) ){ ( setAndFood: (Int, Food), index:Int ) =>
        val food = foods(index)
        val set = setAndFood._1
        val accum = if(set.bit(index)) setAndFood._2 - food else setAndFood._2 + food
        ( set.flipBit(index), accum )
      }

      val solution = subsets.find{ case (_,food) => food == gFood }

      val output = Map(true -> "yes", false -> "no")(solution.isDefined)
      println(s"Case #$t: $output")
    }
  }

  processFile("./NewYearsResolution/new_years_resolution.txt.in")
}
