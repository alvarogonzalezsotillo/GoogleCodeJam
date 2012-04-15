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
  
  def log(s)
  end

end

####################################################################################
#
# PROBLEM CASE
#
class GCJCase < GCJCase_Base

  attr_reader :surprising, :atleast, :points

  def read(ioin=$ioin)
    line = ioin.readline.scan(/\w+/).collect &:to_i
    @surprising = line[1]
    @atleast = line[2]
    @points = line[3..-1]
    self
  end

  def max_not_surprising(p)
    log "#{p%3}"
    if p%3 == 0
      log "#{p} da 0"
      middle = (p/3).floor
      return [middle,p].min
    end
    if p%3 == 1
      log "#{p} da 1" 
      middle = (p/3).floor
      return [middle+1,p].min
    end
    if p%3 == 2
      log "#{p} da 2"
      middle = ((p-2)/3).floor
      return [middle+1,p].min
    end
    raise "Not possible"
  end
  
  def max_surprising(p)
    if p%3 == 0
      middle = (p/3).floor
      return [middle+1,p].min
    else
      middle = ((p-2)/3).floor
      return [middle+2,p].min
    end
  end
  
  def test(p)
    log p.to_s + " max_not_surprising:" + max_not_surprising(p).to_s + " max_surprising:" + max_surprising(p).to_s
  end


  def solve_lammer
    valid_not_surprising = Array.new(points.length,false)
    valid_surprising = Array.new(points.length,false)

    log "******************"
    points.each_index{ |i|
      p = points[i]
      test(p)
      valid_not_surprising[i] = max_not_surprising(p) >= atleast
      valid_surprising[i] = max_surprising(p) >= atleast if !valid_not_surprising[i]
    }

    log "points:#{points}"
    log "atleast:#{atleast}"
    log "surprising:#{surprising}"
    log "valid_not_surprising:#{valid_not_surprising}"
    log "valid_surprising:#{valid_surprising}"

    vns = valid_not_surprising.count(true)
    vs = valid_surprising.count(true)

    ret = vns + [vs, surprising].min
    log "ret:#{ret}"
    ret
    
  end
  
  def solve
    @solution = solve_lammer
    self
  end
end

####################################################################################

GCJProblems.new.solve
