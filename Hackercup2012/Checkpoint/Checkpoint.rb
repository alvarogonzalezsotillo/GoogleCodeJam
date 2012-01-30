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

  Cached_posibilities = {}
  attr_reader :s

  def read(ioin=$ioin)
    @s = ioin.gets.to_i
    self
  end

  def factorize(orig)
      factors = {}
      factors.default = 0     # return 0 instead nil if key not found in hash
      n = orig
      i = 2
      sqi = 4                 # square of i
      while sqi <= n do
          while n.modulo(i) == 0 do
              n /= i
              factors[i] += 1
              # puts "Found factor #{i}"
          end
          # we take advantage of the fact that (i +1)**2 = i**2 + 2*i +1
          sqi += 2 * i + 1
          i += 1
      end
      
      if (n != 1) && (n != orig)
          factors[n] += 1
      end
      factors
  end

  def posibilities_impl(rows,columns)
    return posibilities_impl(columns,rows) if columns < rows
    return 1 if rows <= 1 || columns <= 1
    
    key = [rows,columns]
    cached = Cached_posibilities[key]
    return cached if cached != nil
    
    right = posibilities_impl(rows, columns-1)
    top = posibilities_impl(rows-1,columns)
    
    cached = right + top
    Cached_posibilities[key] = cached
    
    return cached
  end
  
  def posibilities(rows,columns)
    ret = posibilities_impl(rows,columns)
    #log "                posibilities #{rows},#{columns}: #{ret}"
    ret
  end
  
  def solve_brute_force
    # HIPOTESIS: GRID IS ALMOST A RECTANGLE
    # HIPOTESIS: CHECKPOINT IS ABOUT THE CENTER OF THE RECTANGLE
    t = 1
    while true
    
      log "  t:#{t}"
      
      for i in (1 .. t-1) do
        rows = t + 1 - i
        cols = t + 2 - rows
        
        #log "    rows:#{rows} cols:#{cols}"
        next if rows == 0 or cols == 0
        
        # SIMETRY: ROWS ALLWAYS LESS OR EQUAL THAN COLS
        next if rows > cols
        
        # LAME OPTIMIZATION: ENSURE ENOUGTH ROOM
        # next if posibilities(rows,cols) < s
        
        for checkpoint_row in (1..rows) do
          for checkpoint_col in (1..cols) do
            
            # CHECkPOINT CANT BE ORIGIN NOR GOAL
            next if checkpoint_row == 1 && checkpoint_col == 1
            next if checkpoint_row == rows && checkpoint_col == cols

            #log "      checkpoint:#{checkpoint_row},#{checkpoint_col}"
            
            posibilities_to_checkpoint = posibilities(checkpoint_row,checkpoint_col)
            posibilities_to_goal = posibilities(rows-checkpoint_row+1,cols-checkpoint_col+1)
            hint = posibilities_to_checkpoint * posibilities_to_goal
            
            #log "      posibilities_to_checkpoint:#{posibilities_to_checkpoint}"
            #log "      posibilities_to_goal:#{posibilities_to_goal}"
            #log "      hint:#{hint}"
            
            return t if hint == s
          end
        end
      end
  
      t += 1
    end
  end
  
  def solve
    
    @solution = solve_brute_force
    self
  end
end

####################################################################################

GCJProblems.new.solve
