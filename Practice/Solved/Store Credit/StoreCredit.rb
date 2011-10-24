class GCJCase 

	attr_reader :c, :i, :ps, :solution, :index
	
	def initialize(index)
		@index = index
	end
	
	def log(s,ioout=$stderr)
		# ioout.puts s
	end
	
	def read(ioin=$stdin)
		@c = ioin.gets().to_i
		@i = ioin.gets().to_i
		@ps = ioin.gets().scan(/\w+/).collect!{ |x| x.to_i }
		raise "No se que pasa ps: #{ps} i:#{i}" unless ps.length == i
		self
	end
	
	def dump(ioout=$stdout)
		ioout.puts "C: #{c}"
		ioout.puts "I: #{i}"
		ioout.puts "Ps: #{ps}"
	end
	
	def dumpSolution(ioout=$stdout)
		ioout.puts "Case ##{index}: #{solution}"
	end
	
	def solve
		for j in (0 ... ps.length-1) do
			for k in (j+1 ... ps.length) do
				log "j=#{j} k=#{k}"
				break if k <= j
				log "ps[#{j}]=#{ps[j]} ps[#{k}]=#{ps[k]}"
				if ps[j] + ps[k] == c
					@solution = "#{j+1} #{k+1}"
					log "solucion=#{solution}"
					break
				end
			end
			break if solution != nil
		end
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