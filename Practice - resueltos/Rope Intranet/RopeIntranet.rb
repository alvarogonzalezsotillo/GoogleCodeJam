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

	attr_reader :wires
	
	def read(ioin=$ioin)
	  n = ioin.gets().to_i
	  @wires = []
		n.times{ 
		  wires << ioin.gets().scan(/\w+/).collect!{ |x| x.to_i }
		}
		self
	end
	
	def solve
	  @solution = 0
	  for i in (0...wires.length) do
	    for j in (i+1...wires.length) do
	      cut1 = wires[i][0] < wires[j][0] && wires[i][1] > wires[j][1]
	      cut2 = wires[i][0] > wires[j][0] && wires[i][1] < wires[j][1]
	      # log "wires[#{i}]=#{wires[i]} wires[#{j}]=#{wires[j]} cut1:#{cut1} cut2:#{cut2}"
	      @solution +=1 if cut1 || cut2
	    end
	  end
	  self
	end
end



GCJProblem.new.read.solve