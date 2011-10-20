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

  SENTENCE = "welcome to code jam"
  #SENTENCE = "aba"
  attr_reader :text
  
  def read(ioin=$ioin)

    @text = ioin.gets.chomp
   
    self
  end
    
    
  def contains_all( s, pattern )
    s.each_char{ |c|
      return false if pattern.index(c) == nil
    }
    return true
  end
  
  
  def solve
    @solution = "0000"
    
    # SIMPLE OPTIMIZATION: REMOVE EVERY LETTER NOT CONTAINED IN THE SENTENCE
    begin
      new_text = ""
      text.each_char{ |c| (new_text << c ) if SENTENCE.index(c) }
      log text + "\n---->\n" + new_text + "\n"
      @text = new_text
    end
    
    # COMPLEX OPTIMIZATION: REMOVE EVERY LETTER NOT POSSIBLE
    begin
      new_text = ""
      for i in (0...text.length) do
      
        char = text[i]
        if i == 0 || i == text.length-1
          new_text << char
          next
        end
        
        prefix = text[0..i-1]
        suffix = text[i+1..-1]
        sprefix = SENTENCE[0..SENTENCE.index(char)-1]
        sprefix = "" if SENTENCE.index(char)==0
        ssuffix = SENTENCE[SENTENCE.rindex(char)+1..-1]
        ssuffix = "" if SENTENCE.rindex(char)==SENTENCE.length-1
        
        log( "i:" + i.to_s )
        log( "char:" + char )
        log( "prefix:" + prefix )
        log( "suffix:" + suffix )
        log( "sprefix:" + sprefix )
        log( "ssuffix:" + ssuffix )
        
        # CHECK THERE ARE ENOUGH LETTERS BEFORE AND AFTER i
        if contains_all( sprefix, prefix )
          if contains_all( ssuffix, suffix )
            new_text << char
            log "  << metido:" + char
          end
        end
      end
      log "\n" + text + "\n---->\n" + new_text + "\n"
      @text = new_text
      
    end
    
    
    ocurrences = compute_ocurrences_lammer( SENTENCE, text )
    
    @solution = ocurrences.to_s
    self
  end
  
  def compute_ocurrences_lammer( s, t )
  
    return t.count(s) if s.length == 1 
  
    first = s[0]
    ocurrences = 0
    for c in (0 ... t.length) do
      ocurrences += compute_ocurrences_lammer( s[1..-1], t[c+1..-1] ) if t[c] == first 
    end
    ocurrences
  end
  
  
end
####################################################################################

GCJProblems.new.solve
  
