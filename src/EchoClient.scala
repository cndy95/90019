
import java.net._
import java.io._
import scala.io._


object SimpleClient {
  def main(args : Array[String]) : Unit = {
//    println(InetAddress.getLocalHost())
    val s = new Socket(InetAddress.getByName("45.113.234.239"), 65432)
    val out = new PrintStream(s.getOutputStream())
    lazy val in = new BufferedSource(s.getInputStream()).getLines()

    out.println("https://pbs.twimg.com/media/EHe6SlIX4AAdlKy.jpg")
    out.flush()
    println("Received: " + in.next())

    s.close()
    
    
    
  }
}