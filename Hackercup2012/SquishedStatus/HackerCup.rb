#!/usr/bin/ruby
require 'matrix'
require 'set'


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
    @solution = solution.to_s
    raise "#{solution} -- is NaN correct?" if /NaN/.match(solution)
    ioout.puts "Case ##{index}: #{solution}"
  end
  
end 

#
# BASE PROBLEM 
#
class GCJProblem_Base
  attr_reader :n, :cores
  
  # SINGLE THREAD BY DEFAULT
  def initialize( cores = 1 )
    @cores = cores
  end

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
    cases = []

    start_of_problem = Time.now.to_f

    # READ EACH CASE
    for index in (1 .. n) do
      gcjcase = GCJCase.new(index,self)
      gcjcase.read(ioin)
      cases << gcjcase
    end
    
    # SOLVE EACH CASE
    solve_each_case( cases ) if cores <= 1
    solve_each_case_mt( cases, cores ) if cores > 1
    
    # CREATE SOLUTION FILE
    cases.each{ |gcjcase|
      gcjcase.dumpSolution(ioout)
    }
    
    seconds = Time.now.to_f - start_of_problem
    seconds = seconds.to_i
    $iolog.printf( "File total: %i:%02i", seconds/60, seconds%60 )
    $iolog.puts
  end
  
  # SOLVE EACH CASE IN THE SIMPLE WAY
  def solve_each_case( cases )
    cases.each{ |c| solve_one_case(c) }
  end

  # SOLVE EACH CASE USING A NUMBER OF CPU CORES
  def solve_each_case_mt( cases, cores )
    m = Mutex.new
    solving = 0
    solved = 0
    threads = []
    cores.times{
      threads << Thread.new{
        to_solve = 0
        while to_solve < cases.size do
          m.synchronize{
            to_solve = solving
            solving += 1
          }
          if to_solve < cases.size 
            solve_one_case( cases[to_solve] )
            m.synchronize{
              solved += 1
            }
          end
        end
      }
    }
    threads.each{ |t| t.join }
    
    # I DONT UNDERSTAND join METHOD. WHY I CAN JOIN ALL THE THREADS IN THE ARRAY? AS SAID IN
    # DOCUMENTATION, main THREAD SHOULD WAIT UNTIL FIRST THREAD ENDS, BUT ALL THE THREADS ARE
    # JOINED. IS IT A MATTER OF PRIORITIES?
    Thread.pass while( solved < cases.size )
  end
  
  # SOLVE A SINGLE CASE, AND DO THE TIMING
  def solve_one_case( gcjcase )
      start_of_case = Time.now.to_f
      gcjcase.solve
      $iolog.puts( "case #{gcjcase.index} Elapsed:#{(1000*(Time.now.to_r-start_of_case)).to_i} ms" )
  end
  
end

#
# SOLVE A BUNCH OF "in" FILES, OR THE STDIN IF IT IS REDIRECTED
#
class GCJProblems

  #
  # SOLVE ONE PROBLEM FILE, FROM STDIN (REDIRECTED OR WITH "-" PARAMETER)
  # OR SOLVE SEVERAL PROBLEM FILES, ENDING WITH ".in"
  #
  def solve
    if $stdin.tty? && ARGV.length == 0
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
      next unless file.upcase.end_with? ".IN"
      
      filenameIn = file
      filenameOut = file.gsub( /in$/i, "out" )
      
      ioin = File.new(filenameIn,"r")
      ioout = File.new(filenameOut, "w")
      $iolog.puts( "\nProcesing #{filenameIn} into #{filenameOut}" )
      GCJProblem.new.read(ioin).solve(ioin,ioout)
      ioin.close
      ioout.close 
    }
  end
end




