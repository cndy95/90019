import java.net._
import java.io._
import scala.io._

object SimpleServer {
    def main(args : Array[String]) : Unit = {

      val server = new ServerSocket(9999)
      while (true) {
      val s = server.accept()
      val in = new BufferedSource(s.getInputStream()).getLines()
      val out = new PrintStream(s.getOutputStream())
      
      
      println("received",in.next())
      out.println("from server 222")
      out.flush()
      s.close()
      }
    }
}