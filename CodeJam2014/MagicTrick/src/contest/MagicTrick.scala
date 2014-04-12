package contest

import java.util.Formatter
import java.math.BigInteger
import scala.collection.immutable.IndexedSeq

object MagicTrick extends App with ContestStub{

  def extractOneTest(lineIterator: LineIterator) = {
    lineIterator.take( 2 + 2*4 ).toArray
  }


  def solveOneTest(loadedTest: LoadedTest) = {
    val firstRow = loadedTest(0).trim.toInt
    val firstArrangement = loadedTest.slice(1,5).map{ l =>
      l.split(" ").map( _.toInt )
    }.toList

    val secondRow = loadedTest(5).trim.toInt
    val secondArrangement = loadedTest.slice(6,10).map{ l =>
      l.split(" ").map( _.toInt )
    }.toList


    val firstPosibilities = firstArrangement(firstRow-1).toList
    val secondPosibilities = secondArrangement(secondRow-1).toList
    val solution = firstPosibilities.intersect( secondPosibilities )

    log( s"firstArrangement: $firstArrangement")
    log( s"firstRow: $firstRow  secondRow:$secondRow")
    log( s"firstPosibilities: $firstPosibilities")
    log( s"secondPosibilities: $secondPosibilities")
    log( s"solution: $solution")

    if( solution.size == 0 ){
      "Volunteer cheated!"
    }
    else if( solution.size > 1 ){
      "Bad magician!"
    }
    else{
      solution(0).toString
    }
  }

  solveAll(".",false)
}
