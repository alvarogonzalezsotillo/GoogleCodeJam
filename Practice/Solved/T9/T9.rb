class GCJCase 

	attr_reader :words, :solution, :index
	
	def initialize(index)
		@index = index
	end
	
	def log(s,ioout=$stderr)
		# ioout.puts s
	end
	
	def read(ioin=$stdin)
		@words = ioin.gets().chomp!
		self
	end
	
	def dumpSolution(ioout=$stdout)
		ioout.puts "Case ##{index}: #{solution}"
	end
	
	def keys(ch)
	  letters = [ 
	    " ",
	    "",
	    "abc",
	    "def",
	    "ghi",
	    "jkl",
	    "mno",
	    "pqrs",
	    "tuv",
	    "wxyz"
	  ]
	  
	  
	  for i in (0 ... letters.length)
	    if letters[i].include? ch
	      return i.to_s * (letters[i].index(ch)+1)
	    end
	  end
	  
	  raise "No encuentro la tecla para -#{ch}-"
  end
	
	def solve
	
   log "#{words}"
	  @solution = ""
	  words.each_char{ |c|
	    k = keys(c)
	    (@solution << " " if @solution.end_with? k[0]) # unless k[0] == "0" 
	    @solution << k
	  }
	  
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