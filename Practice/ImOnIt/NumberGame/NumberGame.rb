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

  INFINITE = 1000001
  @@constant = nil

  attr_reader :a1, :a2, :b1, :b2
  
  def read(ioin=$ioin)
    (@a1,@a2,@b1,@b2) = ioin.gets.scan(/\w+/).collect!{ |x| x.to_i }
    self
  end
  
  def solve
    sh = solve_hacker() 

    if (a2-a1)<30 && (b2-b1)<30
      sl = solve_lamer()
      raise "WTF #{a1} #{a2} #{b1} #{b2} -- #{sl} #{sh}" if sl != sh
    end
    
    @solution = sh.to_s
    self
  end
  
    
  def compute_first_win(a,b)
    
    firstPlayer = true

    while b != 0 do

      # ENSURE a IS BIGGER THAN b
      (a,b) = [b,a] if a < b
    
      # COMPUTE POSSIBLE NEXT MOVES
      greatestBite = ( a/b ).floor
      canReachZero = (a % b) == 0

      if greatestBite > 1 && canReachZero
        aSmall = a - b*(greatestBite-1)
        aBig = a - b*(greatestBite-2)
      elsif greatestBite > 1 && !canReachZero
        aSmall = a - b*greatestBite
        aBig = a - b*(greatestBite-1)
      elsif greatestBite <= 1 && canReachZero
        aSmall = a - b*greatestBite
        aBig = aSmall
      elsif greatestBite <= 1 && !canReachZero
        aSmall = a - b*greatestBite
        aBig = aSmall
      end

      thereIsChoice = aBig != aSmall
      
      # IF THERE IS CHOICE FOR FIRST PLAYER, HE WINS
      return firstPlayer if thereIsChoice
      
      # IF 0 IS REACHED, THAT PLAYER LOOSES
      return !firstPlayer if aSmall <= 0
      
      # PREPARE NUMBERS FOR NEXT ROUND
      firstPlayer = !firstPlayer
      if thereIsChoice
        b = a
        a = aSmall
      else
        a = aSmall
      end
    end
    
  end

  def solve_hacker()
    
=begin    
    a   
   ########### ldb
  #  #########
b1#   ########
  ##    ######
  ###     #### fad
  ###      ###
  ####       #
b2####
  #####
  ######
  ######
  #######
=end
    
    (@a1,@a2,@b1,@b2) = [b1,b2,a1,a2] if (a2-a1) > (b2-b1)

    overlap = 0
    counter = 0    
    for a in (a1..a2) do
      lbd = estimate_last_before_diagonal(a)
      fad = estimate_first_after_diagonal(a)
      
      # COMPUTE OVERLAP BETWEEN 
      #  [1..lbd] U [fad..infinite] 
      # AND
      #  [b1..b2]
      overlap += segment_overlap(1,lbd,b1,b2)
      overlap += segment_overlap(fad,INFINITE,b1,b2)
      counter += 1
      log( (1.0*(a2-a1-counter)/(a2-a1)).to_s ) if counter%10000==0
    end
    
    overlap
  end
  
  def segment_overlap(x1,x2,y1,y2)
    ret = 0
    if x1.between?(y1,y2) || x2.between?(y1,y2) || y1.between?(x1,x2) || y2.between?(x1,x2)
      min = [x1,y1].max
      max = [x2,y2].min
      ret += max-min+1
    end
    #log( "    overlap #{x1},#{x2}  #{y1},#{y2} -- #{ret}" )
    ret
  end
    
  def solve_lamer()
    ret = 0
    for a in (a1 .. a2) do
      for b in (b1 .. b2) do
        ret += 1 if compute_first_win(a,b)
      end
    end
    ret
  end
  
  def draw_shape(a1,a2,b1,b2)
    for a in (a1..a2) do
      print a.to_s + "\t"
      for b in (b1..b2) do
        w = compute_first_win(a,b)
        print (w ? "#" : " ")
      end
      puts
    end
  end

  def compute_first_after_diagonal(a)
    first = 0
    b = a+1
    while first == 0
      first = b if compute_first_win(a,b)
      b += 1
    end
    return first
  end

  def compute_last_before_diagonal(a)
    return 0 if a == 1
    
    last = 0
    b = a-1
    while last == 0
      raise "WFT" if b <= 0
      last = b if compute_first_win(a,b)
      b -= 1
    end
    return last
  end
  
  def estimate_first_after_diagonal(a)
    hint = (a*constant()).ceil
    # CHANCHULLO FACTOR: SOMETIMES THE PRECISSION IS NOT ENOUGH
    return hint-1 if compute_first_win(a,hint-1)
    hint
  end

  def estimate_last_before_diagonal(a)
    hint = (a/constant()).ceil - 1
    # CHANCHULLO FACTOR: SOMETIMES THE PRECISSION IS NOT ENOUGH
    return hint+1 if compute_first_win(a,hint+1)
    hint
  end

  
  def constant()
    return @@constant if @@constant != nil
    big_big_number = INFINITE
    @@constant = Rational(compute_first_after_diagonal(big_big_number),big_big_number)
  end

  def display_constant_up_to(size)
    for a in (1..size) do
      first = compute_first_after_diagonal(a)
      puts "#{a}: #{first}  -- #{(1.0*a/first)}"
    end
  end
  
  
  def check_constant(a1,a2)
    constant = constant()
    for i in (a1..a2) do
      first = compute_first_after_diagonal(i)
      prevision_first = estimate_first_after_diagonal(i)
      hit_first = first == prevision_first
      
      last = compute_last_before_diagonal(i)
      prevision_last = estimate_last_before_diagonal(i)
      hit_last = last == prevision_last

      puts "first " + (hit_first ? "HIT " : "FAIL") + " #{i}: #{first} -- #{prevision_first}"
      puts "last  " + (hit_last ? "HIT " : "FAIL") + " #{i}: #{last} -- #{prevision_last}"
      
      raise "FAIL" if !hit_first || !hit_last
    end
  end
  

end

####################################################################################

  
#GCJCase.new( nil, nil ).draw_shape(43784-10, 43784+10, 27060-10, 27060+10)
#GCJCase.new( nil, nil ).compute_constant_up_to(60)
#GCJCase.new( nil, nil ).check_constant(43784-10, 43784+10)

GCJProblems.new.solve
