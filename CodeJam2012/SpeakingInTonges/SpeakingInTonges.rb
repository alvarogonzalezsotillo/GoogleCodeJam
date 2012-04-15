#!/usr/bin/ruby

require "./GoogleCodeJam"
####################################################################################
#
# PROBLEM (JUST IN CASE THERE IS GLOBAL DATA)
#
class GCJProblem < GCJProblem_Base

  attr_reader :translations

  EXAMPLES = { "qee" => "zoo",
               "z" => "q", # THIS IS MY OWN
               "y" => "a",
               "ejp mysljylc kd kxveddknmc re jsicpdrysi" => "our language is impossible to understand",
               "rbcpc ypc rtcsra dkh wyfrepkym veddknkmkrkcd" => "there are twenty six factorial possibilities",
               "de kr kd eoya kw aej tysr re ujdr lkgc jv" => "so it is okay if you want to just give up" }  
               
              

  #
  #
  #
  def initialize
    super
    @translations = computeTranslations(EXAMPLES)
    log( translations.to_s )
  end
  
  def computeTranslations(examples)
    ret = {}
     
    examples.each{ |c,p|
      raise "Not equal length" if c.length != p.length
      for i in (0..c.length) do
        if ret[c[i]] != nil && ret[c[i]] != p[i]
          raise "Incompatible translation from #{c[i]} to #{p[i]}"
        end
        ret[c[i]] = p[i]
      end
    }
    
    for c in (?a..?z) do
      if ret[c.chr] == nil
        log "No translation for:" + c
      end
    end
    
    ret
  end
  
  def translate(str)
    ret = ""
    str.each_char{ |c|
      log "translating #{c} into #{translations[c]}"
      ret += translations[c] 
    }
    ret
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

  attr_reader :line

  def read(ioin=$ioin)
    @line = ioin.gets.chomp
    self
  end
  
  def solve
    
    @solution = problem.translate(line)
    self
  end
end

####################################################################################

GCJProblems.new.solve
