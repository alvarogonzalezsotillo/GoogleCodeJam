class GCJCase 

	attr_reader :words, :solution, :index
	
	def initialize(index)
		@index = index
	end
	
	def log(s,ioout=$stderr)
		# ioout.puts s
	end
	
	def read(ioin=$stdin)
		@words = ioin.gets().scan(/\w+/).collect!
		self
	end
	
	def dumpSolution(ioout=$stdout)
		ioout.puts "Case ##{index}: #{solution}"
	end
	
	def solve
    @solution = ""
    words.reverse_each{ |w| @solution << " " + w }
    @solution.strip!
    self
	end
end

class GCJProblem
	attr_reader :n
	
	def solve( ioin=$stdin )
		@n = ioin.gets().to_i
		for index in (1 .. n ) do
			gcjcase = GCJCase.new(index)
			gcjcase.read.solve.dumpSolution
		end
	end
end

GCJProblem.new.solve