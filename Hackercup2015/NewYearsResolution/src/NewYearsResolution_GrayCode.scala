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

  class GrayCodeIndex {
    private var _index = 0;
    private val _l2 = Math.log(2)

    def nextBitPositionChange = {
      @tailrec
      def next(i: Int): Int = {
        @inline def log2(i: Int) = (Math.log(i) / _l2).toInt
        val b = log2(i)
        val twoB = 1 << b
        if (i == twoB) {
          b
        }
        else {
          next(i - twoB)
        }
      }
      _index += 1
      next(_index)
    }

    def index = _index
  }

  object GrayCodeIndex {
    def iterator(bits: Int) = new Iterator[Int] {
      private val _size = 1 << bits
      private val _grayCode = new GrayCodeIndex
      private def index = _grayCode.index
      override def hasNext = index < _size - 1
      override def next() = _grayCode.nextBitPositionChange
    }
  }


  def measure[T](proc: => T) = {
    val ini = System.currentTimeMillis
    val ret = proc
    val end = System.currentTimeMillis
    System.err.println(s"${end - ini} ms")
    ret
  }


  def processFile(file: String) = measure {

    case class Food(p: Int, c: Int, f: Int)


    val in = new Scanner(new File(file))
    val T = in.nextInt()
    for (t <- 1 to T) {
      val gp, gc, gf = in.nextInt()
      val N = in.nextInt()
      val foods = (0 until N).map(_ => Food(in.nextInt(), in.nextInt(), in.nextInt()))

      val subsets = GrayCodeIndex.iterator(N).scanLeft( (0, Food(0,0,0)) ){ ( setAndFood: (Int, Food), index:Int ) =>
        val food = foods(index)
        val set = setAndFood._1
        val include = if (set.bit(index)) -1 else 1
        val np = setAndFood._2.p + include*food.p
        val nc = setAndFood._2.c + include*food.c
        val nf = setAndFood._2.f + include*food.f

        ( set.flipBit(index), Food(np,nc,nf) )
      }

      val solution = subsets.find{ case (_,food) => food.p == gp && food.c==gc && food.f==gf }

      val output = Map(true -> "yes", false -> "no")(solution.isDefined)
      println(s"Case #$t: $output")
    }
  }

  processFile("./NewYearsResolution/new_years_resolution.txt.in")
}
