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

	attr_reader :size, :board, :inarow
	
	def read(ioin=$ioin)
	  @size, @inarow = ioin.gets().scan(/\w+/).collect{ |x| x.to_i }
	  @board = []
		@size.times{ 
		  @board << ioin.gets().chomp
		}
		self
	end
	
	def dump(ioout=$ioout)
    puts "size:#{size}"
    puts "inarow:#{inarow}"
    board.each{ |row| puts row }
	end
	
	def gravityRigth
	  board.collect!{ |x| x.gsub(".","").strip.rjust(size) }
	end
	
	def boardToArray
	  board.collect{ |row| row.scan(/./) }
	end
	
	def look_for_line?(b,ch,x,y,&coords)
	  counter = 0
	  for i in(0...size) do
	    xx, yy = coords.call(x,y,i)
	    return false if xx >= size || yy >= size
	    return false if xx < 0 || yy < 0
	    
	    if b[xx][yy] == ch 
	      counter += 1
	    else
	      counter = 0
	    end
	    
	    return true if counter == inarow
	  end
	  false
	end
	
	def there_is_line?(b,ch)
	  for x in (0...size) do
	    for y in (0...size) do
	      return true if look_for_line?(b,ch,x,y){ |x,y,i| [x+i, y] } #HORIZONTAL
	      return true if look_for_line?(b,ch,x,y){ |x,y,i| [x, y+i] } #VERTICAL
	      return true if look_for_line?(b,ch,x,y){ |x,y,i| [x+i, y+i] } #TOP-LEFT
	      return true if look_for_line?(b,ch,x,y){ |x,y,i| [x+i, y-i] } #TOP-RIGHT
	    end
	  end
	  false
  end
	
	def solve
	  gravityRigth
	  b = boardToArray
	  blue = there_is_line?(b,"B")
	  red = there_is_line?(b,"R")
    
	  @solution = "Both" if blue && red
	  @solution = "Blue" if blue && !red
	  @solution = "Red" if !blue && red
	  @solution = "Neither" if !blue && !red
	  
	  
	  self
	end
end



GCJProblem.new.read.solve
