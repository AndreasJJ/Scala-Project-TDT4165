object Concurrency extends App {

	def threadWrapper(function: =>Unit) : Thread = {
		new Thread {
	    	override def run() = function
	    }
	}

	private var counter: Int = 0

    def increaseCounter(): Unit = {
		counter += 1
	}

	def printCounter(): Unit = {
		println(counter)
	}

	val t1 = threadWrapper(increaseCounter)
	val t2 = threadWrapper(increaseCounter)
	val t3 = threadWrapper(printCounter)
	
	t1.start()
	t2.start()
	t3.start()
}