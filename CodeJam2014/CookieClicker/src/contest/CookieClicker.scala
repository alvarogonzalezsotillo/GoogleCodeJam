package contest

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 12/04/14
 * Time: 1:37
 * To change this template use File | Settings | File Templates.
 */
object CookieClicker extends App with ContestStub{
  def extractOneTest(lineIterator: LineIterator): LoadedTest = lineIterator.take(1).toArray

  def solveOneTest(loadedTest: LoadedTest): Solution = {
    val Array(c,f,x) = loadedTest(0).split(" ").map( _.toDouble )


    log( s"************** c:$c  f:$f  x:$x")
    var s = 0.0
    var cookiesPerSecond = 2.0
    var cookies = 0.0
    while( cookies < x ){
      val secondsWithoutBuy = (x-cookies)/cookiesPerSecond
      val secondsToBuy = (c-cookies)/(cookiesPerSecond)
      val secondsBuying = secondsToBuy + (x-(cookies-c))/(cookiesPerSecond+f)
      val buy = secondsBuying < secondsWithoutBuy

      log( s"secondsWithoutBuy:$secondsWithoutBuy")
      log( s"secondsBuying:$secondsBuying")

      if( buy ){
        log( "Buy a farm")
        s += secondsToBuy
        cookies = 0
        cookiesPerSecond += f
      }
      else{
        log( "Do not buy a farm")
        s += secondsWithoutBuy
        cookies = x
      }
      log( s"s:$s  $cookies:cookies")
    }

    s.toString
  }

  solveAll(".",false)
}