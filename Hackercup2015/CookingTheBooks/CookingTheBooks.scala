package contest

import java.util.Scanner

object CookingTheBooks extends App with ContestStub{

  def extractOneTest(lineIterator: LineIterator) = {
    lineIterator.take(1).toArray
  }

  def computeMin( original: Array[Int] ) = {
    val max = original.max
    val min = (1 to 9).find( m => original.contains(m) ).get
    val indexOfFirstMax = original.indexOf(max)
	val indexOfLastMin = original.size - original.reverse.indexOf(min) - 1
	val ret = original.clone
    ret(indexOfFirstMax) = min
    ret(indexOfLastMin) = max
    ret
  }

  def solveOneTest(in: Scanner) = {
    val original = in.nextLine.trim.toArray.map( c => (c-'0').toInt )
	computeMin(original).mkString
  }

  solveAll(".",false)
}
