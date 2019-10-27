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
		println("counter " + counter)
	}

	threadWrapper(increaseCounter).start()
	threadWrapper(increaseCounter).start()
	threadWrapper(printCounter).start()
}