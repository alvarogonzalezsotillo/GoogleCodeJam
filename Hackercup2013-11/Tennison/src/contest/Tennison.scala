package contest

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 22/11/13
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
object Tennison extends App with ContestStub{

  def extractOneTest(lineIterator: Tennison.LineIterator): Tennison.LoadedTest = lineIterator.take(1).toArray

  def solveOneTest(t: Tennison.LoadedTest): Tennison.Solution = {
    val Array(k,ps,pr,pi,pu,pw,pd,pl) = t(0).split( """\s+""" ).map( _.toDouble )
    log( s"$k $ps $pr $pi $pu $pw $pd $pl" )
    "TO DO"
  }


  solveAll()
}
