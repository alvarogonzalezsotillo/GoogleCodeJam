class GCJCase 

	attr_reader :solution, :index, :v1, :v2, :size
	
	def initialize(index)
		@index = index
	end
	
	def log(s,ioout=$stderr)
	 ioout.puts s
	end
	
	def read(ioin=$stdin)
	  @size = ioin.gets().to_i
		@v1 = ioin.gets().scan(/-*\w+/).collect!{ |x| x.to_i }
		@v2 = ioin.gets().scan(/-*\w+/).collect!{ |x| x.to_i }
		raise "mal" if size != v1.length || size != v2.length
		self
	end
	
	def dumpSolution(ioout=$stdout)
		ioout.puts "Case ##{index}: #{solution}"
	end
	
	def product(u,v)
	  ret = 0
	  for i in (0 ... u.length ) do
	    ret += u[i]*v[i]
	  end
	  ret
	end
	
	def solve1
	  u = v1.sort
	  v = v2.sort.reverse
	  [product( v1.sort, v2.sort.reverse), u.to_s + v.to_s ]
	end
	
	def solve2
	  u = v2.sort
	  min = product(v1,u)
	  s = v1.to_s + v2.to_s
	  v1.permutation{ |v|
	    p = product(v,v2)
	    (min = p; s = v.to_s + v2.to_s + min.to_s) if p < min
	  }
	  [ min, s ]
	end
	
	def solve
	  s1 = solve1
	  if v1.length < 10 
      s2 = solve2
      raise s1.to_s + s2.to_s if s1[0] != s2[0]
    end
	  @solution = s1[0]
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