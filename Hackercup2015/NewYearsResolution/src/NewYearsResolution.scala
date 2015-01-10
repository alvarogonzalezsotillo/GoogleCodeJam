
import java.util.Scanner

/*
 * https://www.facebook.com/hackercup/problems.php?pid=1036037553088752&round=742632349177460
 * http://en.wikipedia.org/wiki/Subset_sum_problem
 */

object NewYearsResolution extends App with ContestStub {

  def extractOneTest(lineIterator: LineIterator) = {
    val first = lineIterator.next
    val second  = lineIterator.next
    val N = second.trim.toInt
    val foods = lineIterator.take(N).toArray
    Array( first, second ) ++ foods
  }

  var idgen = 0
  def nextid() = {
    idgen += 1
    idgen
  }
  
   case class Food(p:Int, c:Int, f:Int, id: Int = nextid() )

  
  // http://en.wikipedia.org/wiki/3SUM
  def sums( S: Seq[(Int,Food)], suma: Int ) : Set[Set[Food]] = {
    //println( s"$S" )
    if( S.map(p=>p._1).sum < suma ){
      Set()
    }
    else if( S.map(p=>p._1).min > suma ){
      Set()
    }
    else{
      val subsets = S.toSet.subsets.toSeq.filter{ ss =>
        val lasuma = ss.toSeq.map(p=>p._1).sum
        //println( s"   lasuma:$lasuma  ss:$ss" )
        lasuma == suma
      }.toSet
      //println( s"subsets:$subsets")
      subsets.map( _.map( p => p._2) ).toSet
    }
  }
  
  def solveOneTest(in: LoadedTest) = {
    
    
    val Array(gp, gc, gf) = in(0).split( """\s+""").map( _.toInt )
    
    val foods = in.drop(2).toIndexedSeq.map{ l =>
        val Array(p, c, f) = l.split( """\s+""").map( _.toInt )
        Food(p,c,f)
    }
 
    val p = foods.map( _.p ).zip(foods)
    val sumsP = sums( p , gp )
    println( s"gp:$gp  sumsP:${sumsP}" )
    
    val c = foods.map( _.c ).zip(foods)
    val sumsC = sums( c , gc )
    println( s"gc:$gc  sumsC:${sumsC}" )

    /*
    val f = foods.map( _.f ).zip(foods)
    val sumsF = sums( f , gf )
    println( s"gf:$gf  sumsF:${sumsF}" )
    
    
    */

    val intersect = sumsP.intersect(sumsC) 
    val f = intersect.exists( _.map(_.f).sum == gf )
    
    val ret = !intersect.isEmpty && f
    
    println( intersect.mkString(",") )
    
    ret.toString
  }

  solveAll(".",true)


}
