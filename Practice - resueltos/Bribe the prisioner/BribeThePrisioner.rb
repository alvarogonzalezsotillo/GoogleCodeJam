$ioin = $stdin
$ioout = $stdout
$iolog = $stderr

class GCJCase_Base
  attr_reader :solution, :index, :problem
  
	def initialize(index, problem)
		@index = index
		@problem = problem
	end

	def log(s,ioout=$iolog)
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
	  start = Time.now.to_i
		for index in (1 .. n) do
		  start_of_case = Time.now.to_i
			gcjcase = GCJCase.new(index,self)
			gcjcase.read.solve.dumpSolution
			$iolog.puts( "case #{index} Elapsed:#{Time.now.to_i-start_of_case} seconds" )
		end
	  seconds = Time.now.to_i - start
	  $iolog.puts( "Elapsed:#{seconds} seconds" )
	  $iolog.puts( "        #{seconds/60}:#{seconds%60}" )
	end
end

class GCJProblem < GCJProblem_Base
end

##########################################
class GCJCase < GCJCase_Base

	attr_reader :cells, :torelease
	attr_reader :orden1, :orden2, :orden3, :cache
	
	def read(ioin=$ioin)
	  ncells, ntorelease = ioin.gets().scan(/\w+/).collect!{ |x| x.to_i }
	  @cells = Array.new(ncells,1)
	  @torelease = ioin.gets().scan(/\w+/).collect!{ |x| x.to_i-1 }
	  @torelease.sort!
	  raise "algo pasa" if torelease.length != ntorelease
		self
	end
	
	def release_cell ( cell, torelease )
	  @orden1 << cell
	  torelease.delete cell
	  cells[cell] = 0
	  ret = 0
	  for i in (cell+1...cells.length) do
	    break if cells[i]==0
	    ret += 1
	  end
	  for i in (0..cell-1).reverse_each do
	    break if cells[i]==0
	    ret += 1
	  end
	  ret
	end
	
	def release( ini, fin, torelease )
	  return 0 if ini >= fin
	  log( "ini:#{ini}  fin:#{fin}" )
	  log( "  torelease:#{torelease}" )
	  inside = torelease.count{ |x| x >= ini && x <= fin }
	  log( "  inside:#{inside}" )
	  return 0 if inside == 0
	  middle = (ini + fin)/2
	  candidates = torelease.sort{ |a,b| 
	    ret = (a-middle).abs - (b-middle).abs
	    if ret == 0
	      0
	    elsif ret > 0
	      1
	    else
	      -1
	    end
	  }
	  log( "  cells:#{cells}" )
	  log( "  candidates:#{candidates}" )
	  candidate = candidates.first
	  bribes = release_cell( candidate , torelease)
	  log( "  bribes:#{bribes}" )
	  bribes += release( ini, candidate - 1,torelease )
	  bribes += release( candidate + 1 , fin, torelease )
	  bribes
	end
	
	def solve1
	  @orden1 = []
	  release( 0, cells.length,Array.new(torelease) )
	end
	
	
	def suelta( ini, fin )
	  return [0,[]] if ini >= fin
	  ret = 0
	  
	  return cache[[ini,fin]] if cache.include?( [ini,fin] ) 
	  
	  released = nil
	  torelease.each{ |cell|
	    if cell >= ini && cell <= fin
        r1,o1 = suelta( ini, cell-1 )
        r2,o2 = suelta( cell+1, fin )
        r = (fin-ini) + r1 + r2
        (ret = r; released = o2.concat(o1) << cell ) if ret == 0 || ret > r
      end
	  }
	  
	  orden = released != nil ? released : []
	  
	  cache[[ini,fin]] = [ret, orden]
	  [ret, orden]
	end
	
	def solve2
	  @cache = {}
	  ret, @orden2 = suelta(0, cells.length-1)
	  ret
	end
	  
	def solve3
	  ret = nil
	  torelease.permutation{ |perm|
	    coins = 0
	    prisioners = Array.new( cells.length, 1 )
      c = 0
	    perm.each{ |x|
	      prisioners[x] = 0
        for i in (x+1...prisioners.length) do
          break if prisioners[i]==0
          c += 1
        end
        for i in (0..x-1).reverse_each do
          break if prisioners[i]==0
          c += 1
        end
	    }
	    ret = [c,perm] if ret == nil || c < ret[0]
	  }
	  ret
	end
	  
	def solve
    s1 = solve1
    s2 = solve2
    s3, @orden3 = solve3
	  @solution = s2
	  log( cache )
	  #raise "1-3 #{s1},#{s2},#{s3} : case #{index} #{torelease} - #{cells.length} : #{orden1} #{orden3}"  if s1 != s3
	  raise "3-2 #{s1},#{s2},#{s3} : case #{index} #{torelease} - #{cells.length} : #{orden3} #{orden2}"  if s3 != s2
	  #raise "1-2 #{s1},#{s2},#{s3} : case #{index} #{torelease} - #{cells.length} : #{orden1} #{orden2}"  if s1 != s2
	  puts "---" + orden2.to_s + " --- " + orden3.to_s
	  self
	end
end
##########################################


GCJProblem.new.read.solve