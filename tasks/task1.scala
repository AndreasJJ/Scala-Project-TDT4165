object Array extends App {
	// The cool way to make an array with elements 1 to 50
	//val array = (for(i <- 1 to 50) yield i).toArray;
	
	// The not cool way to make an array with elements 1 to 50
	val array = new Array[Int](50)
	for(i <- 0 until 50) {
		array(i) = i+1
	}
	
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

	// naive implementation
	def fibRec(n:BigInt) : BigInt = {
		if(n < 2) {
			n
		} else {
			fibRec(n-1)+fibRec(n-2)
		}
	}
	
	// Prints to test the functions
	println(array.mkString(" "))
	println(sum(array))
	println(sumRec(array))
	println(fibRec(10))
}