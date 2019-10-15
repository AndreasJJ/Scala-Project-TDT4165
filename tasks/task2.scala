object Concurrency extends App {

	def threadWrapper(function: =>Unit) : Thread = {
		val thread = new Thread {
	    	override def run() = function
	    }
	    thread.start()
	    thread
	}

	val prnt = (string : String) => {
		println(string)
	}

	threadWrapper(prnt("hello")).join()
}