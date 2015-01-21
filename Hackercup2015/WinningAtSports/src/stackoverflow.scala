/**
 * Created by alvaro on 19/01/15.
 */
class stackoverflow1 {

  def uniquePathsMemoizationGenerator( seen: Array[Array[Int]] ) : (Int,Int,Int,Int) => Int = {

    def uniquePathsMemoization(n:Int, m:Int, row:Int, col:Int, seen:Array[Array[Int]]):Int = {
      if (row == m && col == n) 1
      else if (row > m || col > n) 0
      else {
        if (seen(row + 1)(col) == -1) seen(row + 1)(col) = uniquePathsMemoization(n, m, row + 1, col, seen)
        if (seen(row)(col + 1) == -1) seen(row)(col) = uniquePathsMemoization(n, m, row, col + 1, seen)

        seen(row + 1)(col) + seen(row)(col + 1)
      }
    }

    uniquePathsMemoization(_,_,_,_,seen)
  }

  val nRows = ???
  val nCols = ???
  val uniquePaths = uniquePathsMemoizationGenerator( Array.fill(nRows,nCols)(0))


}

class stackoverflow2 {

    def uniquePathsMemoizationGenerator( seen: Array[Array[Int]] ) : (Int,Int,Int,Int) => Int = {

      def uniquePathsMemoization(n:Int, m:Int, row:Int, col:Int, seen:Array[Array[Int]]):Int = (row,col) match{

        case (row,col) if row == m && col == n =>
          1
        case (row,col) if row > m || col > n =>
          0
        case (row,col) =>
          if (seen(row+1)(col) == -1) seen(row+1)(col) = uniquePathsMemoization(n, m, row + 1, col, seen)
          if (seen(row)(col + 1) == -1 ) seen(row)(col) = uniquePathsMemoization(n,m, row, col + 1, seen)

          seen(row+1)(col) + seen(row)(col + 1)
      }

      uniquePathsMemoization(_,_,_,_,seen)
    }

    val nRows = ???
    val nCols = ???
    val uniquePaths = uniquePathsMemoizationGenerator( Array.fill(nRows,nCols)(0))

    // Use uniquePaths from this point, instead of uniquePathsMemoization
}
