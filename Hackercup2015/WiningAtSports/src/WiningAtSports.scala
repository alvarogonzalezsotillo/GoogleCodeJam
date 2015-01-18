import java.io.{FileOutputStream, File, PrintStream}
import java.util.Scanner

import scala.annotation.tailrec

/**
 * Created by alvaro on 17/01/15.
 *
 * https://www.facebook.com/hackercup/problems.php?pid=688426044611322&round=344496159068801
 *
In the game of Sports, the object is have more points than the other team after a certain amount of time has elapsed. Scores are denoted by two hyphen-separated integers. For example, scores may include 3-2, 4-1, or 10-0. The first number is how many points you've scored, and the second is the number of points scored by the opposing team. You're very good at Sports, and consequently you always win. However, you don't always achieve victory the same way every time.

The two most extreme kinds of victory are called stress-free and stressful. In a stress-free victory, you score the first point and from then on you always have more points than your opponent. In a stressful victory, you never have more points than your opponent until after their score is equal to their final score.

Given the final score of a game of Sports, how many ways could you arrange the order in which the points are scored such that you secure a stress-free or stressful win?

Input
Input begins with an integer T, the number of games you'll play. For each game, there is one line containing the final score of the game in the format described above.

Output
For the ith game, print a line containing "Case #i: " followed by two space-separated integers, the number of ways you can achieve a stress-free or stressful win, respectively. Since these numbers may be very large, output them modulo 1,000,000,007.

Constraints
1 ≤ T ≤ 100
Since you always win, the first number in any final score will always be larger than the second. Both scores will be non-negative integers not exceeding 2000.

Explanation of Sample
In the third test case, you can get a stress-free win by scoring points 1, 2, and 4, or points 1, 2, and 3. You can get a stressful win by scoring points 2, 4, and 5, or points 3, 4, and 5.
 *
 */
object WiningAtSports extends App {

  val modulo = 1000000007

  def log(s: => String) {
    System.err.println( s )
  }

  def measure[T](proc: => T) = {
    val ini = System.currentTimeMillis
    val ret = proc
    val end = System.currentTimeMillis
    log(s"${end - ini} ms")
    ret
  }

  object stressFree extends ((Int, Int) => Int) {

    private val cache = scala.collection.mutable.Map[(Int, Int), Int]()

    def apply(mine: Int, theirs: Int): Int = {

      if (mine == theirs) {
        0
      }
      else if (mine == 0) {
        0
      }
      else if (theirs == 0) {
        1
      }
      else {
        cache.getOrElseUpdate((mine, theirs), (apply(mine - 1, theirs) + apply(mine, theirs - 1)) % modulo)
      }
    }
  }


  object stressFull extends ((Int, Int) => Int) {

    private val cache = scala.collection.mutable.Map[(Int, Int), Int]()

    def apply(mine: Int, theirs: Int): Int = {

      if (mine == theirs) {
        0
      }
      else if (mine == 0) {
        0
      }
      else if (theirs == 0) {
        1
      }
      else if (theirs == 1) {
        1
      }
      else if (mine - theirs > 1) {
        // THIS LEADS TO https://oeis.org/A000108
        cache.getOrElseUpdate((mine, theirs), apply(theirs + 1, theirs))
      }
      else {
        assert( mine-1 == theirs, s"mine:$mine theirs:$theirs" )
        cache.getOrElseUpdate((mine, theirs), stressFree(mine, theirs))
      }
    }


  }

  def solveFile(in: Scanner, out: PrintStream) = {
    val T = in.nextLine().toInt
    log(s"$T cases")
    for (t <- 1 to T) {
      val Array(mine, theirs) = in.nextLine.split( """\\s+|-""").map(_.toInt)
      val free = stressFree(mine, theirs)
      val full = stressFull(mine, theirs)
      out.println(s"Case #$t: $free $full")
    }
  }

  def processFile(file: String) {
    log(s"Processing: $file")
    val in = new Scanner(new File(file))
    val out = new PrintStream(new FileOutputStream(file + ".out"))
    measure {
      solveFile(in, out)
    }
    in.close()
    out.close()
  }


/*
  for (m <- 0 to 2000 by 100) {
    for( t <- 0 until m by 50 ) {
      println(s"$m,$t stressFree:${stressFree(m, t)}  stressFull:${stressFull(m,t)}")
    }
  }
*/

  (new File(".")).list().filter(_.toLowerCase endsWith ".in").foreach(processFile)

}
