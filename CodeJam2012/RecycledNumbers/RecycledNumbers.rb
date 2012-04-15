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

  attr_reader :a, :b

  def read(ioin=$ioin)
    (@a,@b) = ioin.readline.scan(/\w+/).collect &:to_i
    self
  end

  def recycled?(a,b)
    #return false if Math.log10(a).floor != Math.log10(b).floor
    #log "same digit number"
    a = a.to_s
    b = b.to_s
    offset = b.index(a[0])
    return false if offset == nil
    
    #log "start of a is in b"
    for i in (0...a.length) do
      return false if a[i] != b[(i+offset)%a.length]
    end
    
    true
  end
  
  def test(a,b)
    log "recycled?(#{a},#{b}):#{recycled?(a,b)}"
  end

  def solve_lammer(a,b)
    ret = 0
    for n in (a...b) do
      for m in (n+1..b) do
        r = recycled?(n,m)
        ret+=1 if r
        log "#{n} #{m} ****" if r
        log "#{n} #{m}" if !r
      end
    end
    ret
  end


  def how_many_recycled( n, b )
    l = {}
    ns = n.to_s
    (ns.length-1).times{
      ns = ns[-1] + ns[0...-1]
      m = ns.to_i
      l[m] = true if m > n && m <= b
    }
    #log "#{n}: #{l.to_s}"
    l.length
  end
  
  def solve_hacker(a,b)
    ret = 0
    for i in(a...b) do
      hmr = how_many_recycled(i,b)
      ret += hmr
    end
    ret
  end
  
  def solve
    @solution = solve_hacker(a,b)
    self
  end
end

####################################################################################

GCJProblems.new.solve
