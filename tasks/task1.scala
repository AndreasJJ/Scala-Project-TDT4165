object Array extends App {
	// The cool way to make an array with elements 1 to 50
	val array = (for(i <- 1 to 50) yield i).toArray;
	// The cool way to make an array with elements 1 to 50
	//val array = new Array[Int](50)
	//for(i <- 0 until 50) {array(i) = i+1}
	
	// Function that returns the sum of an array with the help of a for loop
	val sum = (array : Array[Int]) => {
		var acc : Int = 0;
		for(i : Int <- array) acc += i;
		acc;
	}

	// Function that returns the sum of an array with the help of recursion
	val sumRec = (array : Array[Int]) => {
		def sum(array : Array[Int], acc : Int) : Int = {
			if(array.length > 0) {
				sum(array.tail, array.head + acc)
			} else {
				acc
			}
		}
		sum(array, 0)
	}

	// Method that returns the n'th fibonacci number
	def fibRec(n : BigInt) : BigInt = {
		def fib(n : BigInt, prevPrev : BigInt, prev : BigInt) : BigInt = {
			if(n > 0) {
				fib(n-1, prev, prevPrev+prev)
			} else {
				prevPrev+prev
			}
		}
		fib(n-2, 0, 1)
	}

	// Prints to test the functions
	println(array.mkString(" "))
	println(sum(array))
	println(sumRec(array))
	println(fibRec(100))
}