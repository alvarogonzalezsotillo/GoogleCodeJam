import java.io.{FileOutputStream, File, PrintStream}
import java.util
import java.util.Scanner

import scala.annotation.tailrec

/**
 * Created by alvaro on 17/01/15.
 *
 *
Since you crave state-of-the-art technology, you've just purchased a phone with a great new feature: autocomplete! Your phone's version of autocomplete has some pros and cons. On the one hand, it's very cautious. It only autocompletes a word when it knows exactly what you're trying to write. On the other hand, you have to teach it every word you want to use.

You have N distinct words that you'd like to send in a text message in order. Before sending each word, you add it to your phone's dictionary. Then, you write the smallest non-empty prefix of the word necessary for your phone to autocomplete the word. This prefix must either be the whole word, or a prefix which is not a prefix of any other word yet in the dictionary.

What's the minimum number of letters you must type to send all N words?

Input
Input begins with an integer T, the number of test cases. For each test case, there is first a line containing the integer N. Then, N lines follow, each containing a word to send in the order you wish to send them.

Output
For the ith test case, print a line containing "Case #i: " followed by the minimum number of characters you need to type in your text message.

Constraints
1 ≤ T ≤ 100
1 ≤ N ≤ 100,000
The N words will have a total length of no more than 1,000,000 characters.
The words are made up of only lower-case alphabetic characters.
The words are pairwise distinct.
NOTE: The input file is about 10-20MB.
 *
 */
object Autocomplete extends App{
  def log( s : => String ){
    //System.err.println( s )
  }

  def measure[T](proc: => T) = {
    val ini = System.currentTimeMillis
    val ret = proc
    val end = System.currentTimeMillis
    log(s"${end - ini} ms")
    ret
  }



  def solveCase( words: IndexedSeq[String] ) = {


    val endOfWord = ('z' + 1).toChar
    val nLetters = endOfWord - 'a' + 1

    class Node(val depth: Int){
      val children = new Array[Node](nLetters)
      private var directChildren = 0
      private var totalChildren = 0
      def child( c: Int ) = children(c - 'a')
      def child_=( c: Int, node: Node) : Unit = children(c - 'a') = node
      def addChild( c: Char ) = {
        totalChildren += 1
        if( child(c) == null ) {
          children(c-'a') = new Node(depth+1)
          directChildren += 1
        }
        child(c)
      }
      def numberOfChildren = directChildren
      def add( s: String ) = s.foldLeft(this)( (node,c) => node.addChild(c) )
      @tailrec
      final def findFirstNotAmbiguousNode(s: String) : Node = {
        val c = s.head
        val t = s.tail
        if( child(c).totalChildren < 2 ) {
          this
        }
        else if( t == "" + endOfWord ){
          this
        }
        else{
          child(s.head).findFirstNotAmbiguousNode(s.tail)
        }
      }

      def dump( level: Int ){
        val s = children.zipWithIndex.map{ case(c,i) => if(c == null) '_' else ('a'+i).toChar }.mkString
        log( ("   " * level) + s + "  " + totalChildren )
        for( c <- children if c != null ){
          c.dump(level+1)
        }
      }
    }


    var ret = 0
    val tree = new Node(1)
    for( w <- words.map( _ + endOfWord) ){
      log( w )
      tree.add(w)
      //tree.dump(0)
      val n = tree.findFirstNotAmbiguousNode(w)
      log( s"$w --> ${n.depth} ")
      ret += n.depth
    }

    ret

  }

  def solveFile( in: Scanner, out: PrintStream ) = {
    val T = in.nextLine().toInt
    log( s"$T cases")
    for( t <- 1 to T ){

      val n = in.nextLine().toInt
      log( s"$n words" )
      val words = (1 to n).map( _ => in.nextLine.trim  )
      log( s"words: ${words.mkString(",")}" )
      val s = solveCase(words)
      out.println( s"Case #$t: $s")
    }
  }

  def processFile( file: String ){
    log( s"Processing: $file" )
    val in = new Scanner( new File(file) )
    val out = new PrintStream( new FileOutputStream( file + ".out" ) )
    measure {
      solveFile(in, out)
    }
    in.close()
    out.close()
  }

  (new File(".")).list().filter( _.toLowerCase endsWith ".in").foreach(processFile)

}
