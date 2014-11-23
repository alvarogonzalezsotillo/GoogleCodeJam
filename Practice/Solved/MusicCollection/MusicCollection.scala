
object MusicCollection extends App{
	
	val nTests = readLine().toInt

	(1 to nTests).foreach{ t =>

		val nSongs = readLine().toInt
		val songs = for( s <- 1 to nSongs ) yield readLine().toUpperCase

		def sortest( s: String ) = {

			val substrings = {
				Seq("") ++ (for( start <- (0 until s.size).view ; (end <- 1 to s.size).view ) yield s.slice( start, end ))
			}

			val restOfSongs = songs.toSet - s
			def notInRestOfSongs( s:String ) = restOfSongs.forall( !_.contains(s) )
			val found = substrings.find(notInRestOfSongs)
			found.map( s => s""""$s"""").getOrElse( ":(" )
		}

		println( s"Case #$t:" )
		songs.map( sortest ).foreach( println )
	}
}
