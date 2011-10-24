require 'matrix'

$ioin = $stdin
$ioout = $stdout
$iolog = $stderr



#
# BASE PROBLEM CASE 
#
class GCJCase_Base
  attr_reader :solution, :index, :problem
  
  #
  # REFERENCE TO THE PROBLEM, JUST IN CASE THERE IS GLOBAL DATA
  #
	def initialize(index, problem)
		@index = index
		@problem = problem
	end

  #
  # FOR DEBUGGING PURPOSES
  #
	def log(s)
	  problem.log(s)
	end

  #
  # PRINT THE SOLUTION
  #
	def dumpSolution(ioout=$ioout)
	  raise "#{solution} -- is NaN correct?" if /NaN/.match(solution)
		ioout.puts "Case ##{index}: #{solution}"
	end
  
end 

#
# BASE PROBLEM 
#
class GCJProblem_Base
	attr_reader :n
	
	#
	# PROBLEM FILES USUALLY START WITH THE NUMBER OF CASES
	#
	def read( ioin=$ioin )
		@n = ioin.gets().to_i
		self
	end

  #
  # FOR DEBUGGING PURPOSES
  #
  def log(s,ioout=$iolog)
    ioout.puts s
  end

		
	#
	# READ EACH PROBLEM CASE AND SOLVE IT
	# ALSO PRINT THE TIMING
	#
	def solve( ioin=$ioin,ioout=$ioout )
	  start_of_problem = Time.now.to_i
		for index in (1 .. n) do
		  start_of_case = Time.now.to_i
			gcjcase = GCJCase.new(index,self)
			gcjcase.read(ioin).solve.dumpSolution(ioout)
			$iolog.puts( "case #{index} Elapsed:#{Time.now.to_i-start_of_case} seconds" )
		end
	  seconds = Time.now.to_i - start_of_problem
	  $iolog.puts( "Elapsed:#{seconds} seconds" )
	  $iolog.puts( "        #{seconds/60}:#{seconds%60}" )
	end
end

#
# SOLVE A BUNCH OF "in" FILES, OR THE STDIN IF IT IS REDIRECTED
#
class GCJProblems

  #
  # SOLVE ONE PROBLEM FILE, FROM STDIN (REDIRECTED)
  # OR SOLVE SEVERAL PROBLEM FILES, ENDING WITH ".in"
  #
  def solve
    if $stdin.tty?
      solveFiles
    else
      GCJProblem.new.read.solve
    end
  end

  #
  # SOLVE EACH PROBLEM FILE IN THE DIRECTORY
  #
  def solveFiles(dir=".")
    Dir.foreach(dir){ |file|
      catch(:not_an_in_file) do
        throw :not_an_in_file unless file.upcase.end_with? ".IN"
        
        filenameIn = file
        filenameOut = file.gsub( /in$/i, "out" )
        
        ioin = File.new(filenameIn,"r")
        ioout = File.new(filenameOut, "w")
     	  $iolog.puts( "Procesing #{filenameIn} into #{filenameOut}" )
        GCJProblem.new.read(ioin).solve(ioin,ioout)
        ioin.close
        ioout.close 
      end
    }
  end
end



####################################################################################
#
# PROBLEM (JUST IN CASE THERE IS GLOBAL DATA)
#
class GCJProblem < GCJProblem_Base

  attr_reader :P
  
  #
  #
  #
  def initialize
    @P = Struct.new( "Particle", :s, :v )
    super
  end
  
  #
  # THE BASE CLASS ONLY READS THE NUMBER OF CASES
  # COMPLETE THIS METHOD IF THERE IS MORE GLOBAL DATA  
  #
	def read( ioin=$ioin )
	  super
		self
	end
end

####################################################################################
#
# PROBLEM CASE
#
class GCJCase < GCJCase_Base
  
  attr_reader :particles
  
	def read(ioin=$ioin)
    n = ioin.gets().to_i
    @particles = []
    n.times do
      l = ioin.gets.split( / / )
      raise l.to_s if l.length != 6
      l = l.collect{ |x| x.to_f }
      @particles << problem.P.new( Vector.elements(l[0,3]), Vector.elements(l[3,3]) )  
      end
		self
	end
	  
	def solve
	  
	  p = Vector[0,0,0]
	  meanS = Vector[0,0,0]
    meanV = Vector[0,0,0]
    particles.each{ |part| meanS += part.s ; meanV += part.v }
    meanS /= particles.length
    meanV /= particles.length
    log( "meanS:" + meanS.to_s )
    log( "meanV:" + meanV.to_s )
    
    if meanV.inner_product(meanV) != 0
      landa = -(meanV.inner_product(meanS) - meanV.inner_product(p))/(meanV.inner_product(meanV))
    else
      landa = 0
    end
    landa = 0 if landa < 0
    r = meanS + meanV*landa
    log( "r:" + r.to_s )
    p_to_r = p - (meanS + (meanV*landa))
    d = Math.sqrt(p_to_r.inner_product( p_to_r) )
	  @solution = d.to_s + " " + landa.to_s 
	  self
	end
end
####################################################################################

GCJProblems.new.solve
  