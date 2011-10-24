$ioin = $stdin
$ioout = $stdout

class GCJCase_Base
  attr_reader :solution, :index, :problem
  
	def initialize(index, problem)
		@index = index
		@problem = problem
	end

	def log(s,ioout=$stderr)
	 #ioout.puts s
	end

	def dumpSolution(ioout=$ioout)
		ioout.puts "Case ##{index}: #{solution}"
	end
  
end 

class GCJProblem_Base
	attr_reader :n
	
	def read( ioin=$ioin )
		@n = ioin.gets().to_i
		self
	end
	
	def solve( ioin=$ioin )
		for index in (1 .. n) do
			gcjcase = GCJCase.new(index,self)
			gcjcase.read.solve.dumpSolution
		end
	end
end

class GCJProblem < GCJProblem_Base
end

class GCJCase < GCJCase_Base

	attr_reader :created, :tocreate
	
	def read(ioin=$ioin)
	  ncreated, ntocreate = ioin.gets().scan(/\w+/).collect!{ |x| x.to_i }
	  @created = []
	  @tocreate = []
	  ncreated.times{
	    @created << ioin.gets.chomp
	  }
	  ntocreate.times{
	    @tocreate << ioin.gets.chomp
	  }
		self
	end
	
	def solve
	  @solution = created.length

	  tocreate.each{ |path|
	    candidate = ""
	    path.scan(/\w+/).each{ |d|
	      candidate = candidate + "/" + d
	      created << candidate if not created.include? candidate
	    }
	  }
	  
	  @solution = created.length - @solution
	  self
	end
end



GCJProblem.new.read.solve