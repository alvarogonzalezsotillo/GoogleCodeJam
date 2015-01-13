
import java.util.Scanner

import BFS.BFSDefinition

import scala.collection.{SeqView, TraversableView, mutable}

/*
 *https://www.facebook.com/hackercup/problems.php?pid=1523599254559737&round=742632349177460
 *
 */

object LaserMaze extends App with ContestStub {

  def extractOneTest(lineIterator: LineIterator) = {
    val rowsCols = lineIterator.next()
    val Array(nrows, ncols) = rowsCols.split( """\s+""").map(_.toInt)
    Array(rowsCols) ++ lineIterator.take(nrows)
  }


  trait Map {
    def width: Int

    def height: Int

    def clockWiseMap: Map

    def cell(row: Int, col: Int): Char

    def inside(row: Int, col: Int): Boolean

    def inside(p: (Int, Int)): Boolean

    def validForHero(p: (Int, Int)): Boolean

    def validForHero(row: Int, col: Int): Boolean
  }

  trait Maze {
    def map: Map

    def goal: (Int, Int)

    def hero: (Int, Int)

    def laserHitHero: Boolean

    def nextStep: Seq[Maze]

    def goalReached = goal == hero

    def dumpString: String
  }

  object Map {

    class MapImpl(val rows: Array[String]) extends Map {
      var clockWiseMap: MapImpl = null
      val width = rows(0).size
      val height = rows.size

      def cell(row: Int, col: Int) = {
        if (inside(row, col)) {
          rows(row)(col)
        }
        else {
          '.'
        }
      }

      def inside(row: Int, col: Int): Boolean = row >= 0 && row < height && col >= 0 && col < width

      def inside(p: (Int, Int)): Boolean = inside(p._1, p._2)

      def validForHero(p: (Int, Int)): Boolean = validForHero(p._1, p._2)

      def validForHero(row: Int, col: Int): Boolean = cell(row, col) == '.' && inside(row, col)
    }

    def rotate(rows: Array[String]) = {
      rows.map(_.replace('v', '%').replace('>', 'v').replace('^', '>').replace('<', '^').replace('%', '<'))
    }

    def apply(rows: Array[String]) = {
      val emptyRows = rows.map(_.replace('S', '.').replace('G', '.'))
      val m1 = new MapImpl(emptyRows)
      val m2 = new MapImpl(rotate(m1.rows))
      val m3 = new MapImpl(rotate(m2.rows))
      val m4 = new MapImpl(rotate(m3.rows))
      m1.clockWiseMap = m2
      m2.clockWiseMap = m3
      m3.clockWiseMap = m4
      m4.clockWiseMap = m1
      m1
    }
  }

  object Maze {


    class MazeImpl(override val map: Map, override val hero: (Int, Int), override val goal: (Int, Int)) extends Maze {

      override def nextStep = {
        val nextMap = map.clockWiseMap
        val up = (hero._1 - 1, hero._2)
        val down = (hero._1 + 1, hero._2)
        val left = (hero._1, hero._2 - 1)
        val right = (hero._1, hero._2 + 1)

        for (nextHero <- Seq(up, down, left, right) if map.validForHero(nextHero)) yield new MazeImpl(nextMap, nextHero, goal)
      }

      override lazy val toString = dumpString

      override def equals(o: Any) = String.valueOf(o) == toString

      override lazy val hashCode = toString.hashCode

      override lazy val laserHitHero = {
        val (hrow, hcol) = hero

        def limit(line: IndexedSeq[Char]) = line.dropWhile(_ == '.').headOption

        val upLimit = limit((hrow - 1 to 0 by -1).map(r => map.cell(r, hcol)))
        val downLimit = limit((hrow + 1 to map.height).map(r => map.cell(r, hcol)))
        val leftLimit = limit((hcol - 1 to 0 by -1).map(c => map.cell(hrow, c)))
        val rightLimit = limit((hcol + 1 to map.width).map(c => map.cell(hrow, c)))

        upLimit.contains('v') || downLimit.contains('^') || leftLimit.contains('>') || rightLimit.contains('<')

      }

      override def dumpString: String = {
        val ret = for (r <- 0 until map.height) yield {
          val row = for (c <- 0 until map.width) yield {
            if ((r, c) == goal) 'G'
            else if ((r, c) == hero) '@'
            else map.cell(r, c)
          }
          row.mkString + "\n"
        }
        "\n" + ret.mkString
      }
    }

    def apply(rows: Array[String]) = {
      def find(c: Char) = {
        val row = rows.indexWhere(_.contains(c))
        val col = rows(row).indexOf(c)
        (row, col)
      }
      val goal = find('G')
      val hero = find('S')
      val map = Map(rows)
      new MazeImpl(map, hero, goal)
    }
  }

  def bfs(maze: Maze) = {
    val bfsDef = new BFS.BFSDefinition[Maze] {
      override def expand(t: Maze): Seq[Maze] = t.nextStep.filterNot(_.laserHitHero)

      override def equal(m1: Maze, m2: Maze) = {

        m1.map == m2.map && m1.hero == m2.hero
      }

      override def found(t: Maze): Boolean = {
        t.goalReached && !t.laserHitHero
      }

      override def heuristic(t: Maze) = {
        val h = t.hero
        val g = t.goal
        val dx = (h._1 - g._1)
        val dy = (h._2 - g._2)
        dx * dx + dy * dy // ALMOST EUCLIDEAN
        //Math.abs(dx) + Math.abs(dy) // MANHATAN
      }

    }

    implicit val mazeOrdering = Ordering.by((b: Maze) => b.dumpString)

    BFS(maze, bfsDef)
  }

  def solveOneTest(in: LoadedTest) = {
    val Array(nrows, ncols) = in(0).split( """\s+""").map(_.toInt)

    val rows = in.drop(1).map(_.trim)

    val maze = Maze(rows)

    val search = bfs(maze)
    val ret = search.search()



    ret match {
      case Some(node) =>
        println(node.node.dumpString)
        "" + (node.pathToRoot.size - 1)
      case None =>
        println("imposible")
        "impossible"
    }
  }

  solveAll(".", true)


}
