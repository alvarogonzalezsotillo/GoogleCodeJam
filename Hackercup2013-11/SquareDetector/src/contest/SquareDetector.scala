package contest

object SquareDetector extends App with ContestStub{

  def extractOneTest(lineIterator: LineIterator): LoadedTest = {
    val size = lineIterator.next.toInt
    lineIterator.take(size).toArray
  }


  def solveOneTest(t: LoadedTest): Solution = {

    implicit class PairToCoordinate( val p: (Int,Int) ){
      def row = p._1
      def col = p._2
      def value = t(row)(col)
      def to( pair: (Int,Int) ) = {
        for( i <- (p._1 to pair._1).view ; j <- (p._2 to pair._2).view ) yield (i,j)
      }
    }

    def rowColumnIterator( firstRows: Boolean, reverseRow: Boolean, reverseColumn: Boolean ) = {
      def iterator(reverse: Boolean) = {
        val ret = 0 until t.size
        if( reverse ) ret.reverse.view else ret.view
      }
      def rowColumn = for( row <- iterator(reverseRow) ; col <- iterator(reverseColumn) ) yield(row,col)
      def columnRow =  for( col <- iterator(reverseColumn) ; row <- iterator(reverseRow) ) yield(row,col)
      if( firstRows ) rowColumn else columnRow
    }

    def firstFilledCell( firstRows: Boolean, reverseRow: Boolean, reverseColumn: Boolean ) = {
      rowColumnIterator(firstRows, reverseRow, reverseColumn).find( _.value == '#' )
    }

    if( !((0,0) to (t.size-1,t.size-1)).exists( _.value == '#' ) ) return "NO"

    val topLeft1     = firstFilledCell(false,false,false)
    val topLeft2     = firstFilledCell(true, false,false)
    val topRight1    = firstFilledCell(false,false,true)
    val topRight2    = firstFilledCell(true, false,true)
    val BottomLeft1  = firstFilledCell(false,true, false)
    val BottomLeft2  = firstFilledCell(true, true, false)
    val BottomRight1 = firstFilledCell(false,true, true)
    val BottomRight2 = firstFilledCell(true, true, true)

    if( topLeft1 != topLeft2 ) return "NO"
    if( topRight1 != topRight2 ) return "NO"
    if( BottomLeft1 != BottomLeft2 ) return "NO"
    if( BottomRight1 != BottomRight2 ) return "NO"

    val topLeft = topLeft1.get
    val bottomRight = BottomRight1.get

    if( topLeft.row - bottomRight.row != topLeft.col - bottomRight.col) return "NO"
    if( (topLeft to bottomRight).exists( _.value != '#' ) ) return "NO"

    "YES"
  }



  solveAll()
}