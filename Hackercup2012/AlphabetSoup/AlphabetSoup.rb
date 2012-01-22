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

  Sentence = "HACKERCUP"
  Infinite = 1000000000
  attr_reader :words

  def read(ioin=$ioin)
    @words = ioin.gets
    self
  end
  
  def letter_histogram(s)
    ret = Hash.new(0)
    s.each_char{ |c|
      ret[c] = ret[c]+1
    }
    ret
  end
  
  def max_size()
    sentence_h = letter_histogram(Sentence)
    h = letter_histogram(words)
    
    ret = Infinite
    sentence_h.each_pair{ |c,v|
      new_ret = h[c]/v
      ret = new_ret if new_ret < ret
    }
    
    ret
  end
  
  def solve
    @solution = max_size().to_s
    self
  end
end

####################################################################################

GCJProblems.new.solve
