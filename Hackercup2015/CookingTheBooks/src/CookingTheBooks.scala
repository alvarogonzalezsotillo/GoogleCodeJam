
import java.util.Scanner

/*
 * https://www.facebook.com/hackercup/problems.php?pid=582062045257424&round=742632349177460
 */

object CookingTheBooks extends App with ContestStub {

  def extractOneTest(lineIterator: LineIterator) = {
    lineIterator.take(1).toArray
  }

  def computeMin(original: Array[Int]) = {
    if (original.size == 1) {
      original
    } else {
      val permutations = for (
        i <- 0 until original.size;
        j <- 0 until original.size if original(j) != 0
      ) yield {
        val c = original.clone
        val temp = c(j)
        c(j) = c(i)
        c(i) = temp
        c
      }

      permutations.
        map(_.mkString).
        filter(_(0) != '0').
        map(_.toInt).
        min.
        toString.
        toArray.
        map(c => (c - '0').toInt)
    }
  }

  def computeMax(original: Array[Int]) = {
    if (original.size == 1) {
      original
    } else {
      val permutations = for (
        i <- 0 until original.size;
        j <- 0 until original.size if original(j) != 0
      ) yield {
        val c = original.clone
        val temp = c(j)
        c(j) = c(i)
        c(i) = temp
        c
      }

      permutations.
        map(_.mkString).
        filter(_(0) != '0').
        map(_.toInt).
        max.
        toString.
        toArray.
        map(c => (c - '0').toInt)
    }
  }

  def toDigits(i: Int) = i.toString.toArray.map(c => (c - '0').toInt)

  def solveOneTest(in: Scanner) = {
    val original = toDigits(in.nextInt)
    computeMin(original).mkString + " " + computeMax(original).mkString
  }

  solveAll(".",false)


}
