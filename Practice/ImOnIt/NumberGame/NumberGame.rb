#!/usr/bin/ruby

require "./GoogleCodeJam"
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
  
  #def log(s)
  #end
end

####################################################################################
#
# PROBLEM CASE
#
class GCJCase < GCJCase_Base


  attr_reader :a1, :a2, :b1, :b2
  
  def read(ioin=$ioin)
    (@a1,@a2,@b1,@b2) = ioin.gets.scan(/\w+/).collect!{ |x| x.to_i }
    self
  end
  
  def solve
    #compute_first_win( a1, a2 )
    #@solution = "hola"
    real_solve()
    self
  end
  
    
  def compute_first_win(a,b)
    
    firstPlayer = true

    while b != 0 do

      # ENSURE a IS BIGGER THAN b
      (a,b) = [b,a] if a < b
      log( "a: #{a}  b: #{b}  firstPlayer:#{firstPlayer}")
    
      # COMPUTE POSSIBLE NEXT MOVES
      log( "a/b #{a/b}" )
      log( "(a/b).floor #{(a/b).floor}" )
      aSmall = a - b*((a/b).floor)
      aBig = a - b*((a/b).floor-1)
      log( "  aBig: #{aBig}  aSmall: #{aSmall}" )
      
      # IF THERE IS A MOVE WITH NO OPTION FOR THE PLAYER, HE LOOSES
      return !firstPlayer if aSmall <= 0
      
      # PREPARE NUMBERS FOR NEXT ROUND
      firstPlayer = !firstPlayer
      b = a
      a = aSmall
    end
    
  end

    
  def real_solve()
    @solution = 0
    for a in (a1 .. a2) do
      for b in (b1 .. b2) do
        @solution += 1 if compute_first_win(a,b)
      end
    end
  end

end

####################################################################################

GCJProblems.new.solve
  

