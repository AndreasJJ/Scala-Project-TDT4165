object Concurrency extends App {

	def threadWrapper(function: =>Unit) : Thread = {
		new Thread {
	    	override def run() = function
	    }
	}

	private var counter: Int = 0

    /*
	// Old increaseCounter
	def increaseCounter(): Unit = {
		counter += 1
	}
	*/

	def increaseCounter(): Unit = this.synchronized {
		counter += 1
	}

	def printCounter(): Unit = {
		println("counter " + counter)
	}

	val t1 = threadWrapper(increaseCounter)
	val t2 = threadWrapper(increaseCounter)
	val t3 = threadWrapper(printCounter)
	t1.start()
	t2.start()
	t3.start()

	// Joins ensure deadlock does not interfere with above code
	t1.join()
	t2.join()
	t3.join()

	object Alpha {lazy val x: Int = Beta.y}
	object Beta {lazy val y: Int = Alpha.x}

	val ta = threadWrapper(Alpha.x)
	val tb = threadWrapper(Beta.y)
	ta.start()
	tb.start()
}