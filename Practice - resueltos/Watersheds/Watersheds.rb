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

  attr_reader :cells
  
  OO = 20000
  N = lambda{ |r,c| [r-1,c] }
  S = lambda{ |r,c| [r+1,c] }
  E = lambda{ |r,c| [r,c+1] }
  W = lambda{ |r,c| [r,c-1] }
  K = lambda{ |r,c| [r,c] }
   
  
  def read(ioin=$ioin)

    (rows, cols) = ioin.gets.scan(/\w+/).collect!{ |x| x.to_i }
    @cells = []
    rows.times{
      @cells << ioin.gets.scan(/\w+/).collect!{ |x| x.to_i }
    }
    
    self
  end
    
  def solve
    flowField = create_flow_field()
    basins = find_sinks_and_create_basins( flowField )
    #@solution = create_cells_string() + create_flow_field_string(flowField) + create_basins_string(basins)
    @solution = create_basins_string(basins)
    self
  end
  
  def create_cells_string()
    # JUST FOR TESTING
    ret = "\n"
    cells.each{ |row|
      row.each{ |cell|
        ret << cell.to_s
      }
      ret << "\n"
    }
    ret
  end
  
  def create_flow_field()
    dir = lambda{ |a,d,r,c| 
      (newr, newc) = d.call(r,c)
      return OO if newr < 0 || newr >= a.length
      return OO if newc < 0 || newc >= a[r].length
      return a[newr][newc]
    }
   
    # ANOTHER ARRAY OF SAME DIMENSIONS AS cells
    # t[r][c] WILL CONTAIN n,s,e,w, or k (sink)
    t = []
    cells.each{ |c| t << Array.new( c.length ) }
    
    for row in (0...t.length) do
      for column in (0...t[row].length) do
        # SEARCH WHERE THE FLOW GOES
        flow = K
        min = cells[row][column]
        [N,W,E,S].each{ |direction|
          m = dir.call(cells,direction,row,column)
          if min > m 
            min = m
            flow = direction
          end
        }
        t[row][column] = flow
      end
    end
    
    t
    
  end
    
  def create_flow_field_string( flowField )
    # VECTOR FIELD OF FLOW (JUST FOR TESTING)
    vect = "\n"
    flowField.each{ |trow|
      trow.each{ |tcell|
        vect << "^" if tcell == N
        vect << "*" if tcell == K
        vect << "v" if tcell == S
        vect << ">" if tcell == E
        vect << "<" if tcell == W
      }
      vect << "\n"
    }
    vect
  end
  
  def find_sinks_and_create_basins( flowField )
    basins = []
    for row in (0 ... flowField.length) do
      for column in (0 ... flowField[row].length) do
        basins << create_basin_from_cell( flowField, row, column ) if flowField[row][column] == K
      end
    end
    basins.sort!
  end
  
  def create_basin_from_cell( flowField, row, column )
    basin = []
    basin << [row,column]
    [N,S,E,W].each{ |dir|
      (ro,co) = dir.call(row,column)
      next if ro < 0 || co < 0 || ro >= flowField.length || co >= flowField[0].length
      if flowField[ro][co].call(ro,co) == [row,column]
        basin.concat create_basin_from_cell(flowField, ro, co )
      end
    }
    basin.sort!
  end
  
  def create_basins_string( basins )
    ret = []
    cells.each{ ret << " "*2*cells[0].length }
    name = 'a'
    basins.each{ |basin|
      basin.each{ |cell|
        ret[cell[0]][2*cell[1]] = name
      }
      name.next!
    }
    
    retS ="\n"
    ret.each{ |r|
      retS << r
      retS << "\n"
    }
    
    retS
  end
  
end
####################################################################################

GCJProblems.new.solve
  
