words = "abcdefghij".scan(/./)
numbers = [1,2,3,4,5,6,7,8,9,10]
symbols = "*^?!.;".scan(/./)

Thread.main.priority=1000

words *= 100
numbers *= 100
symbols *= 1000
t1 = Thread.new{ 
  puts( "comienza t1" )
  sleep 10
  words.each{ |word| print( word ) } 
} 
t2 = Thread.new{ 
  puts( "comienza t2" )
  sleep 10
  numbers.each{ |number| print( number ) } 
}
t3 = Thread.new{ 
  puts( "comienza t3" )
  sleep 10
  symbols.each{ |number| print( number ) } 
  gets()
}

t1.priority = 100
t2.priority = 100
t3.priority = 100

puts( "Lanzo t2, t3" )
[t2,t3].each{ |t| t.join }

puts( "Lanzo t1" )

[t1].each{ |t| t.join }
puts( "fin" )

