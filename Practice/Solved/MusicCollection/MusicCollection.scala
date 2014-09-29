
object MusicCollection extends App{
	
	val nTests = readLine().toInt

	(1 to nTests).foreach{ t =>

		val nSongs = readLine().toInt
		val songs = for( s <- 1 to nSongs ) yield readLine().toUpperCase

		def sortest( s: String ) = {

			val substrings = {
				Seq("") ++ (for( start <- 0 until s.size ; end <- 1 to s.size ) yield s.slice( start, end ))
			}
			def better( s1: String, s2: String ) = {
				if( s1.size != s2.size ) s1.size < s2.size else s1 < s2
			}

			val restOfSongs = songs.toSet - s
			def notInRestOfSongs( s:String ) = restOfSongs.forall( !_.contains(s) )
			val found = substrings.filter(notInRestOfSongs)
			val sorted = found.sortWith(better)
			sorted.headOption.map( s => s""""$s"""").getOrElse( ":(" )
		}

		println( s"Case #$t:" )
		songs.map( sortest ).foreach( println )
	}
}
