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
      ##log( "a: #{a}  b: #{b}  firstPlayer:#{firstPlayer}")
    
      # COMPUTE POSSIBLE NEXT MOVES
      greatestBite = ( a/b ).floor
      canReachZero = (a % b) == 0

      #log( "  greatestBite: #{greatestBite}" )
      #log( "  canReachZero: #{canReachZero}" )

      if greatestBite > 1 && canReachZero
        aSmall = a - b*(greatestBite-1)
        aBig = a - b*(greatestBite-2)
      elsif greatestBite > 1 && !canReachZero
        aSmall = a - b*greatestBite
        aBig = a - b*(greatestBite-1)
      elsif greatestBite <= 1 && canReachZero
        aSmall = a - b*greatestBite
        aBig = aSmall
      elsif greatestBite <= 1 && !canReachZero
        aSmall = a - b*greatestBite
        aBig = aSmall
      end
      #log( "  aBig: #{aBig}  aSmall: #{aSmall}" )

      thereIsChoice = aBig != aSmall
      #log( "  thereIsChoice: #{thereIsChoice}" )
      
      # IF THERE IS CHOICE FOR FIRST PLAYER, HE WINS
      return firstPlayer if thereIsChoice
      
      # IF 0 IS REACHED, THAT PLAYER LOOSES
      return !firstPlayer if aSmall <= 0
      
      # PREPARE NUMBERS FOR NEXT ROUND
      firstPlayer = !firstPlayer
      if thereIsChoice
        b = a
        a = aSmall
      else
        a = aSmall
      end
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
  

