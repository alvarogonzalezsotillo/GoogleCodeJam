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

  attr_reader :m_sequence, :m_n

  def read(ioin=$ioin)
    @m_n = ioin.gets.to_i
    @m_sequence = ioin.gets
    self
  end

  def merge_sort(arr,sequence)
    n = arr.length
    if n <= 1 
        return arr
    end

    # arr is indexed 0 through n-1, inclusive
    mid = (n/2).floor
    
    first_half = merge_sort(arr[0 .. mid-1],sequence)
    second_half = merge_sort(arr[mid .. n-1],sequence)
    return merge(first_half, second_half,sequence)
  end

  def merge(arr1, arr2, sequence)
    result = []
    while arr1.length() > 0 and arr2.length() > 0
    
      if sequence == nil
    
        if arr1[0] < arr2[0]
            log "1" # for debugging
            result.push(arr1[0])
            arr1.slice!(0)
        else
            log "2" # for debugging
            result.push(arr2[0])
            arr2.slice!(0)
        end
        
      else
      
        next_comparation = sequence[0]
        sequence.slice!(0)
        if next_comparation == "1" 
          result.push(arr1[0])
          arr1.slice!(0)
        else
          result.push(arr2[0])
          arr2.slice!(0)
        end
      end
        
    end
            
    result.push(arr1)
    result.push(arr2)
    return result.flatten
  end

  def recover_permutation
    arr = []
    (1..m_n).each  { |i| arr << i }
    arr = merge_sort( arr, m_sequence )
    ret = Array.new(m_n)
    arr.each_index{ |i|
      ret[ arr[i]-1 ] = i+1
    }
    ret
  end
  
  def checksum(arr)
    result = 1
    for i in (0... arr.length()) do
        result = (31 * result + arr[i]) % 1000003
    end
    result
  end
  
  def solve
    permutation = recover_permutation()
    log permutation
    @solution = checksum(permutation)
    self
  end
end

####################################################################################

GCJProblems.new.solve
