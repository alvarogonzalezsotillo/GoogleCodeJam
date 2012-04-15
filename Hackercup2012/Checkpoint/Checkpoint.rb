#!/usr/bin/ruby

require "./HackerCup"
####################################################################################
#
# PROBLEM (JUST IN CASE THERE IS GLOBAL DATA)
#
class GCJProblem < GCJProblem_Base

  Cached_posibilities = {}

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
    log "posibilities #{rows},#{columns}"
    posibilities_impl(rows,columns)
  end


  def compute_posibilities_array
    max_s = 10000000
    posibilities_array = []
    cols = 1
    loop do
      rows = 1
      break if posibilities(rows,cols) > max_s
      begin
        pos = posibilities(rows,cols)
        posibilities_array << [rows,cols,pos]
        rows += 1
      end while pos <= max_s
      cols += 1
    end
  end

  #
  #
  #
  def initialize
    compute_posibilities_array
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
        
        for checkpoint_row in (1..rows) do
          for checkpoint_col in (1..cols) do
            
            # CHECkPOINT CANT BE ORIGIN NOR GOAL
            next if checkpoint_row == 1 && checkpoint_col == 1
            next if checkpoint_row == rows && checkpoint_col == cols

            #log "      checkpoint:#{checkpoint_row},#{checkpoint_col}"
            
            posibilities_to_checkpoint = problem.posibilities(checkpoint_row,checkpoint_col)
            posibilities_to_goal = problem.posibilities(rows-checkpoint_row+1,cols-checkpoint_col+1)
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
