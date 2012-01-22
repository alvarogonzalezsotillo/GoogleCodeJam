#!/usr/bin/ruby

require "./HackerCup"
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

  attr_reader :w, :h, :words

  def read(ioin=$ioin)
    line = ioin.gets.scan(/\w+/)
    @w = line[0].to_i
    @h = line[1].to_i
    @words = line[2..-1].collect{ |s| s.length+1 }
    self
  end
  
  def fits?( size )
    row = 0
    columns = (w/size).floor + 1
    rows = (h/size).floor
    rowsize = 0
    
    words.each{ |nextword|
      return false if nextword > columns
      
      if rowsize + nextword <= columns
        rowsize += nextword
        next
      else
        row += 1
        return false if row >= rows
        rowsize = nextword
      end
    }
    
    return true
  end
  
  def max_size()
    return 0 if !fits?(1)
    # TO BE OPTIMIZED: PERFORM BSEARCH INSTEAD OF LINEAL SEARCH
    for size in (2..1000) do
      return size-1 if !fits?(size)
    end
  end
  
  def solve
    @solution = max_size().to_s
    self
  end
end

####################################################################################

GCJProblems.new.solve
