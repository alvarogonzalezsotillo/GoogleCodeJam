$ioin = $stdin
$ioout = $stdout

class GCJCase_Base
  attr_reader :solution, :index, :problem
  
	def initialize(index, problem)
		@index = index
		@problem = problem
	end

	def log(s,ioout=$stderr)
	  ioout.puts s
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

########################################################################
require 'set'
class GCJCase < GCJCase_Base

	attr_reader :symbols
	
	def read(ioin=$ioin)
	  @symbols = ioin.gets().chomp!
		self
	end
	
	def value_of( symbols, values )
	  log "symbols:#{symbols}  values:#{values}"
	  base = values.length
	  base = 2 if base == 1
	  mult = 1
	  ret = 0
	  symbols.reverse.each_char{ |c|
	    val = values[c]*mult
	    log "#{values[c]}*#{mult}=#{val}"
	    ret += val
	    mult *= base
	  }
	  ret
	end
	
	def solve1
	  values = Hash.new
	  nextValue = 0
	  values[symbols[0]] = 1
	  
	  symbols.each_char{ |c|
	    if not values.key?(c)
	      nextValue = 2 if nextValue == 1
	      values[c] = nextValue
	      nextValue += 1
	    end
	  }
	  
	  value_of( symbols, values )
	end
	
	def solve2
	  uniques = []
	  symbols.each_char{ |c| uniques << c if not uniques.include? c }
	  base = uniques.length
	  
	  valuesArray = []
	  base.times{ |i| valuesArray << i }
	  min = nil
	  valuesArray.permutation{ |p|
	    values = {}
	    for i in (0...uniques.length) do
	      values[uniques[i]]=p[i]
	    end
	    
	    if values[symbols[0]] != 0 
        val = value_of( symbols, values )
        min = val if min == nil || val < min
      end
	  }
	  
	  min
	end
	  
	def solve
	  @solution = solve1
	  # raise ">>>> algo pasa #{solve2} no es igual que #{solve1} <<<<<" if solution != solve2
	  self
	end
end
#########################################################################


GCJProblem.new.read.solve