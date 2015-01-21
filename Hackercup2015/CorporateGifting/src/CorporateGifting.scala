import java.io.{File, FileOutputStream, PrintStream}
import java.util.Scanner

import scala.annotation.tailrec

/**
 * Created by alvaro on 17/01/15.
 *
 * https://www.facebook.com/hackercup/problems.php?pid=759650454070547&round=344496159068801
 *
The fine people of Corpro Corp. are a festive bunch. Every holiday season, everybody buys a gift for their manager. A cynic might say that the employees are just trying to bribe their way to a better performance review, but if you asked them yourself, they'd say they just wanted to spread cheer.

The fine people of Corpro Corp. are a frugal bunch. When they buy gifts, they cooperate to collectively buy the least expensive gifts that they can. A cynic might say that the employees are cheap, but if you asked them yourself, they'd say it's the thought that counts.

There are N employees working at Corpro Corp., and each of them has a manager, except for the CEO who has no manager (the CEO also buys a gift every year, but she donates it to charity). The employees each have a unique employee ID which is an integer from 1 to N. As you might expect, the CEO has the ID 1.

If there exists a set of two or more employees {p1, ..., pk} such that, for all i < k, pi is the manager of pi+1, then we say that p1 is "responsible for" pk. There are never two employees who are responsible for each other. That would be a silly hierarchy indeed.

There are N kinds of gifts available for purchase, and the ith kind of gift costs i dollars. That is, the prices of the different kinds of gifts are {$1, $2, $3, ... $N}. There are N copies of each gift available for purchase.

The only thing that stops all employees from purchasing gifts that cost $1 is the awkwardness of buying a gift for their manager that's the same as the one their manager is giving away. No employee would ever do such a thing!

For example, in a company with just 2 employees, at least $3 must be spent in total. If employee #1 (the CEO) buys a $1 gift to donate to charity, then employee #2 cannot buy a $1 gift for employee #1 (their manager), but they can buy a $2 gift instead. Note that it would be equally optimal for the CEO to buy a $2 gift, while receiving a $1 gift from her subordinate.

What's the minimum possible total expenditure across the whole company during the gift exchange?

Input
Input begins with an integer T, the number of corporate hierarchies to consider. Each hierarchy is made up of two lines. The first line contains the integer N. The second line contains N space-separated integers. The ith integer is the employee ID of the manager of employee i, with the exception that the first integer is always 0, denoting that the CEO has no manager.

Output
For the ith hierarchy, print a line containing "Case #i: " followed by the smallest amount of money the entire company would need to spend.

Constraints
1 ≤ T ≤ 100
1 ≤ N ≤ 200,000
NOTE: The input file is about 10-20MB.
 *
 */
object CorporateGifting extends App {

  def log(s: => String) {
    //System.err.println( s )
  }

  def measure[T](proc: => T) : T = {
    measure( "Elapsed" )(proc)
  }

  def measure[T](msg: String)(proc: => T) : T = {
    val ini = System.currentTimeMillis
    val ret = proc
    val end = System.currentTimeMillis
    System.err.println(s"$msg:${end - ini} ms")
    ret
  }

  def corporateGifting(hierarchy: Array[Int]) = {
    val n = hierarchy.size

    // 0-based indexes are much easier
    for( i <- 0 until hierarchy.size ) hierarchy(i) -= 1

    // Compute depths
    val depth = measure( "Depths" ) {
      Array.tabulate(n) { i =>
        @tailrec
        def depth(employee: Int, accum: Int): Int = hierarchy(employee) match {
          case -1 => accum
          case d => depth(d, accum + 1)
        }
        depth(i, 0)
      }
    }
    log( "Depths:" + depth.mkString(",") )

    // Build adjacency lists
    val adjacency = measure( "Adjacency" ){
      val adj : Array[List[Int]] = Array.fill(n)( Nil )
      for (i <- 0 until n if hierarchy(i) != -1 ) {
        adj(hierarchy(i)) = i :: adj(hierarchy(i))
      }
      adj
    }
    log( "Adjacency:" + adjacency.mkString("\n") )

    val employeesSortedByDepth = measure( "Employees sorted" ) {
      (0 until n).sortBy( -depth(_) )
    }
    log( "Sorted by depth:" + employeesSortedByDepth.mkString(",") )

    // Build max cost for employee, in reversed depth order
    measure( s"$n: maxCostForEmployeeTree" ) {
      def log2(i: Int) = (Math.log(i) / Math.log(2)).toInt
      val maxColor = log2(n) + 1
      val invalidCost = -1
      val maxCostForEmployeeTree = Array.fill(n + 1, maxColor + 1)(invalidCost)
      for (emp <- employeesSortedByDepth; color <- 1 to maxColor) {
        def minOfSubtree(employeeSubtree: Int) = {
          val ret = (1 to maxColor).filter(_ != color).map(maxCostForEmployeeTree(employeeSubtree)).min
          ret ensuring (_ != invalidCost)
        }
        val sumOfSubtrees = adjacency(emp).map(minOfSubtree).sum
        maxCostForEmployeeTree(emp)(color) = color + sumOfSubtrees
      }
      (1 to maxColor).map( maxCostForEmployeeTree(0) ).min
    }
  }

  def solveFile(in: Scanner, out: PrintStream) = {
    val T = in.nextLine().toInt
    log(s"$T cases")
    for (t <- 1 to T) {
      val n = in.nextLine().toInt
      val hierarchy = Array.fill(n)( in.nextInt() )
      in.nextLine()
      val solution = corporateGifting(hierarchy)
      out.println(s"Case #$t: $solution")
    }
  }

  def processFile(file: String) {
    System.err.println(s"Processing: $file")
    val in = new Scanner(new File(file))
    val out = new PrintStream(new FileOutputStream(file + ".out"))
    measure {
      solveFile(in, out)
    }
    in.close()
    out.close()
  }

  (new File(".")).list().filter(_.toLowerCase endsWith ".in").foreach(processFile)
}
