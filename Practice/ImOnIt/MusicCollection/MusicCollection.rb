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
    super(5)
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
  
  attr_reader :songs
  
  def read(ioin=$ioin)
    n = ioin.gets.to_i
    @songs = []
    n.times{
      @songs << ioin.gets.chomp!.upcase
    }
    self
  end
  
  def find_sortest_substring( str, strs )
    return '""' if strs.size == 1
    #log "buscando para:" + str 
    for len in (1 .. str.size) do
      #log "  len:#{len}" 
      uniques = []
      for index in (0 .. str.size-len)
        substr = str[index, len]
        regexp = Regexp.new( substr )
        
        #log "    index:#{index}" 
        #log "    substr:#{substr}"
        
        unique = true
        strs.each{ |s|
          next if s == str
          regexp.match( s ){ 
            unique = false
            break
          }
        }
        #log "    unique:#{unique}"
        uniques << substr if unique
      end
      
      if uniques.size > 0
        return '"' + uniques.sort![0] + '"'
      end
      
    end
    return ":("
  end
  
  def solve
    solutions = []
    songs.each{ |s|
      subs = find_sortest_substring( s, songs )
      solutions << subs
    }
    
    @solution = ""
    solutions.each{ |sol|
      @solution += "\n" + sol
    }
    self
  end
end

####################################################################################

GCJProblems.new.solve
