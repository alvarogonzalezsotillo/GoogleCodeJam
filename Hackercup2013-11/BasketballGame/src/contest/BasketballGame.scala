package contest

/**
  * Created with IntelliJ IDEA.
  * User: alvaro
  * Date: 22/11/13
  * Time: 11:16
  * To change this template use File | Settings | File Templates.
  */
object BasketballGame extends App with ContestStub{


  solveAll()

  def extractOneTest(lineIterator: LineIterator): LoadedTest = {
    val header = lineIterator.next
    val Array(n,m,p) = header.split("""\s+""").map( _.toInt )
    val players = lineIterator.take(n).toList
    (header :: players).toArray
  }

  def solveOneTest(loadedTest: LoadedTest): Solution = {
  
    val Array(n,m,p) = loadedTest.head.split("""\s+""").map( _.toInt )
  
    class Player(val name:String, val shotP: Int, val height: Int){
      var draftNumber = 0
      var timePlayed = 0
      override def toString = name
    }
    
    
    val players = loadedTest.tail.map{ s =>
      val Array(name,shotP,height) = s.split("""\s+""")
      new Player(name, shotP.toInt, height.toInt)
    }
    
    val playersSortedByDraft = players.sortWith{ (p1,p2) =>
      if( p1.shotP != p2.shotP ) p1.shotP > p2.shotP
      else p1.height > p2.height
    }
    playersSortedByDraft.zipWithIndex.foreach{ case (player,index) => player.draftNumber = index+1 }
    
    val playersOnCourt = new collection.mutable.HashSet ++ playersSortedByDraft.take(p).toSet
    val playersOnBench = new collection.mutable.HashSet ++ playersSortedByDraft.drop(p).toSet
    
    for( rotation <- 1 to m if p < players.size ){
      playersOnCourt.foreach( _.timePlayed += 1 )
      
      val playerOut = playersOnCourt.foldLeft(playersOnCourt.head){ (p1,p2) =>
        if( p1.timePlayed > p2.timePlayed ) p1
        else if( p1.timePlayed < p2.timePlayed ) p2
        else if( p1.draftNumber < p2.draftNumber ) p2
        else p1
      }
      
      val playerIn = playersOnBench.foldLeft(playersOnBench.head){ (p1,p2) =>
        if( p1.timePlayed < p2.timePlayed ) p1
        else if( p1.timePlayed > p2.timePlayed ) p2
        else if( p1.draftNumber < p2.draftNumber ) p1
        else p2
      }
      
      playersOnCourt -= playerOut
      playersOnCourt += playerIn
      playersOnBench += playerOut
      playersOnBench -= playerIn
      
      log( s"$rotation: out:$playerOut in:$playerIn onCourt:${playersOnCourt.mkString}" )
    }
    
    playersOnCourt.toArray.sortBy( _.name ).mkString(" ")
    
  }
}

