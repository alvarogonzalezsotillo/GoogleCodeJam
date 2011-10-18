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

  P = Struct.new( "Milkshake", :flavor, :malted )
  
  attr_reader :flavors, :customers
  
	def read(ioin=$ioin)

	  @flavors = ioin.gets.to_i
	  ncustomers = ioin.gets.to_i
	  log( flavors )
	  log( ncustomers )
	  arr = []
	  ncustomers.times{ 
	    arr << ioin.gets.scan(/\w+/).collect!{ |x| x.to_i }
	  }
	  @customers = []
	  for i in (0...arr.length) do
	    @customers[i] = []
	    for j in (0...arr[i].length/2) do
	      @customers[i][j] = P.new( arr[i][j*2+1], arr[i][j*2+2]==1 )
	    end
	  end
	  self
	end

	def count_satisfied_customers(flavors)
      satisfiedCustomers = Array.new(customers.length) 
      for i in ( 0...flavors.length) do
        if flavors[i] == nil
          next
        end
        for c in (0...customers.length) do
          next if satisfiedCustomers[c] 
          customers[c].each{ |preference|
            if preference.flavor = i+1 && flavors[i] == preference.malted
              satisfiedCustomers[c] = true
            end
          }
        end
      end
      satisfiedCustomers.count(true)    
	end

    # CUENTO LOS SABORES QUE REALMENTE GUSTAN, POR SI SOBRAN EN EL ENUNCIADO
	def ensureEveryFlavorNeeded()
      coveredFlavors = Array.new(flavors, false)
      customers.each{ |customer|
        customer.each{ |flavor|
          coveredFlavors[flavor.flavor] = true
        }
      }
      raise "Some flavors not needed: #{coveredFlavors}" if coveredFlavors.count(true) != flavors
	end  
	  
	def solve
	  ensureEveryFlavorNeeded()  
	  @solution = customers.to_s
	  self
	end
end
####################################################################################

GCJProblems.new.solve
  
