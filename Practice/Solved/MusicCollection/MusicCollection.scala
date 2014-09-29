
object MusicCollection extends App{
	
	def lazyly[T]( seq: Seq[T] ) = seq.view
	def substrings(s:String) = {
		for( start <- lazyly(0 until s.size) ; 
		     end <- lazyly( 1 to s.size) )
		  yield s.slice( start, end )
	}
	def better( s1: String, s2: String ) = {
		if( s1.size != s2.size ){
			s1.size < s2.size
		}
		else{
			s1 < s2
		}
	}
	
	val nTests = readLine().toInt

	(1 to nTests).foreach{ t =>

		val nSongs = readLine().toInt
		val songs = for( s <- 1 to nSongs ) yield readLine().toUpperCase

		def sortest( s: String ) = {
			if( songs.size > 1 ){
				val restOfSongs = songs.toSet - s
				val found = substrings(s).filter{ ss => 
					restOfSongs.forall( !_.contains(ss) )
				}		
				val sorted = found.sortWith(better)
				sorted.headOption.map( s => s""""$s"""").getOrElse( ":(" )
			}
			else{
				""
			}
		}

		println( s"Case #$t:" )
		songs.map( sortest ).foreach(println )
	}

}
