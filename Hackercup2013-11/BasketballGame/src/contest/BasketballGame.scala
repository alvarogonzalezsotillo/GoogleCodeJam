package contest

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 22/11/13
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
object BasketballGame extends App with ContestStub {



  def extractOneTest(lineIterator: LineIterator): LoadedTest = {
    val header = lineIterator.next
    val Array(n, m, p) = header.split( """\s+""").map(_.toInt)
    val players = lineIterator.take(n).toList
    (header :: players).toArray
  }

  class Player(val name: String, val shotP: Int, val height: Int) {
    var draftNumber = 0
    var timePlayed = 0
    override def toString = name
  }

  // FIRST SHOULD LEFT COURT
  lazy val onCourtOrdering = new Ordering[Player] {
    def compare(x: Player, y: Player) = {
      if (x.timePlayed != y.timePlayed) y.timePlayed - x.timePlayed
      else y.draftNumber - x.draftNumber
    }
  }

  // FIRST WILL ENTER COURT
  lazy val onBenchOrdering = new Ordering[Player] {
    def compare(x: Player, y: Player): Int = {
      if (x.timePlayed != y.timePlayed) x.timePlayed - y.timePlayed
      else x.draftNumber - y.draftNumber
    }
  }

  def solveOneTest(loadedTest: LoadedTest): Solution = {
    val ret1 = solveOneTest_lamer(loadedTest)
    val ret2 = solveOneTest_hacker(loadedTest)
    assert(ret1 == ret2)
    ret1
  }

  def solveOneTest_hacker(loadedTest: LoadedTest): Solution = {

    val Array(_, m, p) = loadedTest.head.split( """\s+""").map(_.toInt)

    val players = loadedTest.tail.map {  s =>
      val Array(name, shotP, height) = s.split( """\s+""")
      new Player(name, shotP.toInt, height.toInt)
    }

    val playersSortedByDraft = players.sortWith {  (p1, p2) =>
      if (p1.shotP != p2.shotP) p1.shotP > p2.shotP
      else p1.height > p2.height
    }
    playersSortedByDraft.zipWithIndex.foreach {
      case (player, index) => player.draftNumber = index + 1
    }

    val team1 = playersSortedByDraft.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
    val team2 = playersSortedByDraft.zipWithIndex.filter(_._2 % 2 == 1).map(_._1)

    def computePlayersOnCourt(playersSortedByDraft: Iterable[Player]) = {
      val playersOnCourt = new collection.mutable.TreeSet()(onCourtOrdering) ++ playersSortedByDraft.take(p)
      val playersOnBench = new collection.mutable.TreeSet()(onCourtOrdering.reverse) ++ playersSortedByDraft.drop(p)

      for (rotation <- 1 to m if playersOnBench.size > 0) {
        playersOnCourt.foreach(_.timePlayed += 1)
        val playerOut = playersOnCourt.head
        val playerIn = playersOnBench.head

        playersOnCourt -= playerOut += playerIn
        playersOnBench += playerOut -= playerIn

        log(s"$rotation: out:$playerOut in:$playerIn onCourt:${playersOnCourt.mkString(" ")}  onBench:${playersOnBench.mkString(" ")}")
      }

      playersOnCourt
    }

    val playersOnCourt = computePlayersOnCourt(team1) ++ computePlayersOnCourt(team2)
    playersOnCourt.toSeq.map(_.name).sorted.mkString(" ")
  }


  def solveOneTest_lamer(loadedTest: LoadedTest): Solution = {

    val Array(_, m, p) = loadedTest.head.split( """\s+""").map(_.toInt)

    val players = loadedTest.tail.map {  s =>
      val Array(name, shotP, height) = s.split( """\s+""")
      new Player(name, shotP.toInt, height.toInt)
    }

    val playersSortedByDraft = players.sortWith {  (p1, p2) =>
      if (p1.shotP != p2.shotP) p1.shotP > p2.shotP
      else p1.height > p2.height
    }
    playersSortedByDraft.zipWithIndex.foreach {
      case (player, index) => player.draftNumber = index + 1
    }

    val team1 = playersSortedByDraft.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
    val team2 = playersSortedByDraft.zipWithIndex.filter(_._2 % 2 == 1).map(_._1)

    def computePlayersOnCourt(playersSortedByDraft: Iterable[Player]) = {
      val playersOnCourt = new collection.mutable.HashSet ++ playersSortedByDraft.take(p).toSet
      val playersOnBench = new collection.mutable.HashSet ++ playersSortedByDraft.drop(p).toSet

      for (rotation <- 1 to m if playersOnBench.size > 0) {
        playersOnCourt.foreach(_.timePlayed += 1)
        val playerOut = playersOnCourt.min(onCourtOrdering)
        val playerIn = playersOnBench.min(onBenchOrdering)

        playersOnCourt -= playerOut += playerIn
        playersOnBench += playerOut -= playerIn

        log(s"$rotation: out:$playerOut in:$playerIn onCourt:${playersOnCourt.mkString(" ")}  onBench:${playersOnBench.mkString(" ")}")
      }

      playersOnCourt
    }

    val playersOnCourt = computePlayersOnCourt(team1) ++ computePlayersOnCourt(team2)
    playersOnCourt.toSeq.map(_.name).sorted.mkString(" ")
  }


  solveAll("BasketballGame")
}

