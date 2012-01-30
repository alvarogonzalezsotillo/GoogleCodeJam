#!/usr/bin/ruby

require "./HackerCup"
require "scanf"
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

  attr_reader :max_char, :message
  
  def read_until_whitespace(ioin=$ioin)
    # CANT FIND ANY WORKING SCANF FOR RUBY (!)
    return nil if ioin.eof?

    char = ""
    begin
      char = ioin.readchar
    end while !ioin.eof? && /\s/.match(char)!=nil
    return nil if ioin.eof?
    
    ret = char
    begin
      char = ioin.readchar
      ret << char
    end while !ioin.eof? && /\s/.match(char)==nil
    
    ret.chomp.strip
  end

  def read(ioin=$ioin)
    @max_char = read_until_whitespace.to_i
    @message = read_until_whitespace
    self
  end


  def compute_posibilities( msg, maxc )
    max_candidate_length = 3 # MAXC IS 255 AT MOST
    posibilities_from = Array.new(message.size+max_candidate_length,0)
    (message.size ... posibilities_from.size).each{ |i| posibilities_from[i] = 1 }
    
    first_candidate_found = false
    
    for index in (message.size-1).downto(0) do
     
      #log "index:#{index}"
    
      # FOR EACH INDEX, TEST 3 CANDIDATES [index..index] [index..index+1] [index..index+2]
      for end_index in (index...index+max_candidate_length) do
      
        next if end_index >= message.size
        #log "  end_index:#{end_index}"
        candidate = message[index..end_index]
        #log "  candidate:#{candidate}"
        next if candidate[0] == "0"
        candidate = candidate.to_i
        
        # VALID CHARS ARE FROM 1 TO maxc, INCLUSIVE
        next if candidate == 0
        next if candidate > maxc # CANT REMEMBER HOW TO "BREAK" A LOOP IN RUBY
        
        # IF THIS IS A CANDIDATE, SUM THE POSIBILITIES FROM THE NEXT TO ITS END
        posibilities_from[index] += posibilities_from[end_index+1]
        posibilities_from[index] %= 4207849484
      end
      #log "posibilities:#{posibilities_from}"
    end
    
    posibilities_from[0]
    
  end

  
  def solve
    log max_char.to_s + " -- " + message
    @solution = compute_posibilities( message, max_char )
    self
  end
end

####################################################################################

GCJProblems.new.solve
