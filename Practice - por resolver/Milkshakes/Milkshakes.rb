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

  #
  #
  #
  def initialize
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

  P = Struct.new( "Milkshake", :flavor, :malted )
  
  attr_reader :flavors, :customers
  
	def read(ioin=$ioin)

	  @flavors = ioin.gets.to_i
	  ncustomers = ioin.gets.to_i
	  log( flavors )
	  log( ncustomers )
	  arr = []
	  ncustomers.times{ 
	    arr << ioin.gets.scan(/\w+/).collect!{ |x| x.to_i }
	  }
	  @customers = []
	  for i in (0...arr.length) do
	    @customers[i] = []
	    for j in (0...arr[i].length/2) do
	      @customers[i][j] = P.new( arr[i][j*2+1], arr[i][j*2+2]==1 )
	    end
	  end
		self
	end

	def count_satisfied_customers(flavors)
     satisfiedCustomers = Array.new(customers.length) 
     for i in ( 0...flavors.length) do
       if flavors[i] == nil
         next
       end
       for c in (0...customers.length) do
         next if satisfiedCustomers[c] 
         customers[c].each{ |preference|
           if preference.flavor = i+1 && flavors[i] == preference.malted
             satisfiedCustomers[c] = true
           end
         }
       end
     end
     satisfiedCustomers.count(true)    
	end
		
	def try_to_solve( flavors ) #array of flavors, true (malted), false (unmalted) or nil (not prepared)
    return flavors if count_satisfied_customers(flavors) == customers.length
	  
	  for i in (0...flavors.length) do
      next if flavors[i] != nil
	    # PROBAR PRIMERO CON UNMALTED, LUEGO CON MALTED
      newFlavors = Array.new(flavors)
      
	  end
	end  
	  
	def solve
	  
	  @solution = customers.to_s
	  self
	end
end
####################################################################################

GCJProblems.new.solve
  