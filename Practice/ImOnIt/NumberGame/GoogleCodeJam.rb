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
    start_of_problem = Time.now.to_f
    for index in (1 .. n) do
      start_of_case = Time.now.to_f
      gcjcase = GCJCase.new(index,self)
      gcjcase.read(ioin).solve.dumpSolution(ioout)
      $iolog.puts( "case #{index} Elapsed:#{(1000*(Time.now.to_r-start_of_case)).to_i} ms" )
    end
    seconds = Time.now.to_f - start_of_problem
    seconds = seconds.to_i
    $iolog.printf( "File total: %i:%02i", seconds/60, seconds%60 )
    $iolog.puts
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




