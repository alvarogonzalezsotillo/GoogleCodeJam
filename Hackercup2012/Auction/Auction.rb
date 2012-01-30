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

  Product = Struct.new( :p, :w, :index )
  Value = Struct.new( :v, :index )

  attr_reader :n, :p1, :w1, :m, :k, :a, :b, :c, :d

  def read(ioin=$ioin)
    (@n,@p1,@w1,@m,@k,@a,@b,@c,@d) = ioin.readline.scan( /\w+/ ).collect{ |x| x.to_i }
    self
  end
  
  def find_cycle_min_max( v1, x, y, z )
    v = v1
    v_min = Value.new( v, 1 )
    v_max = Value.new( v, 1 )
    
    cycle = 0
    
    i = 2
    begin
      
      if v < v_min.v
        v_min = Value.new( v, i )
      end

      if v > v_max.v
        v_max = Value.new( v, i )
      end
      
      v = ((x*v + y)%z)+1
      
      cycle = i if cycle == 0 && v == v1
      
      i += 1
    end while i < n && cycle == 0
    
    log "  cycle:#{cycle}"
    log "  v_min:#{v_min}"
    log "  v_max:#{v_max}"
    
    [cycle, v_min, v_max]
  end
  
  def solve
    for i in (1..1e7) do
      log "#{i}" if i%1000000 == 0
    end
  
    @solution = "ya"
    self
  end
end

####################################################################################

GCJProblems.new.solve

