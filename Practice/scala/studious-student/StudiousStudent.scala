/**
 * https://www.facebook.com/hackercup/problems.php?pid=188558297823727&round=4
 * https://github.com/alvarogonzalezsotillo/GoogleCodeJam/blob/master/Practice/scala/studious-student/StudiousStudent.scala
 */
object StudiousStudent extends App{
	val nTests = readLine().toInt
	(1 to nTests).foreach{ t =>
		val test = readLine().split(" ").drop(1)
		val solution = test.sortBy( _ + "z"*10 ).mkString
		println( s"case #$t: $solution" );	
	}
}
