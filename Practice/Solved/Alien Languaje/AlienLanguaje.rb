class GCJCase 

	attr_reader :word, :solution, :index, :problem
	
	def initialize(index,problem)
		@index = index
		@problem = problem
	end
	
	def log(s,ioout=$stderr)
		# ioout.puts s
	end
	
	def read(ioin=$stdin)
		@word = ioin.gets().chomp!
		self
	end
	
	def dumpSolution(ioout=$stdout)
		ioout.puts "Case ##{index}: #{solution}"
	end
	
	def solve
	  regexp = word.gsub( "(", "[" ).gsub( ")", "]" )
	  regexp = Regexp.new( regexp )
	  @solution = 0
	  problem.words.each{ |w| (@solution += 1; log "#{w}--#{regexp}") if regexp.match w }
    self
	end
end

class GCJProblem
	attr_reader :n, :size , :words
	
	def read( ioin = $stdin )
	  (@size, nwords, @n) = ioin.gets().scan(/\w+/).collect{ |s| s.to_i }
	  @words = []
	  (1..nwords).each{
	    @words << ioin.gets().chomp!
	  }
	  self
	end
	
	def dump( ioout = $stdout )
	  ioout.puts "size: #{size}"
	  ioout.puts "n: #{n}"
	  ioout.puts "nwords: #{words.size}"
	  words.each{ |w| puts w }
	  self
	end
	
	def solve( ioin=$stdin )
		for index in (1 .. n) do
			gcjcase = GCJCase.new(index, self)
			gcjcase.read.solve.dumpSolution
		end
	end
end

GCJProblem.new.read.solve