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
    

  def log_comb( s, t, comb )
    # JUST FOR TESTING, NOT USED
    t.each_char{ |c| $iolog.print "\t" + c }
    $iolog.puts
    for sIndex in (0 ... s.length ) do
      $iolog.print s[sIndex] 
      comb.each{ |co|
        $iolog.print "\t" + co[sIndex].to_s
      }
      $iolog.print "\n"
    end
    $iolog.print "\n"
  end
  
  def modulus(i)
    i % 10000
  end
    
  def compute_ocurrences( s, t )
  
    # ARRAY OF COMBINATIONS
    comb = []
    t.each_char{ comb << Array.new( s.length, 0 ) }
    
    # EACH LAST CHAR OF s COUNTS AS ONE OCURRENCE OF HIMSELF IN t
    sIndex = 0
    comb.each_index{ |tIndex|
      comb[tIndex][sIndex] = 1 if t[tIndex] == s[sIndex]
    }
    
    # LOOK UP EACH CHAR OF s 
    for sIndex in (1 ... s.length ) do
      for tIndex in (0 ... t.length ) do
        next if t[tIndex] != s[sIndex]
        
        # IF THIS CHAR IN THE text IS THE SAME AS THE CHAR IN THE sentence
        # THEN SUM ALL THE OCURRENCES OF THE sentence UP TO THE sIndex CHARACTER
        sum = 0
        for i in (0...tIndex) do
          sum += comb[i][sIndex-1]
          sum = modulus(sum)
        end
        comb[tIndex][sIndex] = sum
      end
    end
    
    
    # THE RESULT IS THE SUM OF ALL THE OCURRENCES ADDED FOR THE LAST CHARARCTER
    sum = 0
    comb.each_index{ |tIndex|
      sum += comb[tIndex][-1]
      sum = modulus(sum)
    }
    sum
    
  end
    
  def solve
    @solution = "0000"
    
    # SIMPLE OPTIMIZATION: REMOVE EVERY LETTER NOT CONTAINED IN THE SENTENCE
    begin
      new_text = ""
      text.each_char{ |c| (new_text << c ) if SENTENCE.index(c) }
      @text = new_text
    end if false
    
    ocurrences = compute_ocurrences( SENTENCE, text )
    raise "error" if text.length <=30 && ocurrences != compute_ocurrences_lamer( SENTENCE, text )
    
    @solution = sprintf( "%04d", ocurrences )
    self
  end
  
  def compute_ocurrences_lamer( s, t )
    # USED FOR TESTING IN THE SMALL CASE
  
    return t.count(s) if s.length == 1 
  
    first = s[0]
    ocurrences = 0
    for c in (0 ... t.length) do
      ocurrences += compute_ocurrences_lamer( s[1..-1], t[c+1..-1] ) if t[c] == first 
      ocurrences = modulus(ocurrences)
    end
    ocurrences
  end
  
  
end
####################################################################################

GCJProblems.new.solve
  

