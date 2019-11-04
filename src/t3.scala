import java.util.Date

import scala.util.Try
import sys.process._
import java.net.URL
import java.io.File

object t3 {
      def main(args: Array[String]) {
        
//         new URL("https://pbs.twimg.com/ext_tw_video_thumb/1180574378804813824/pu/img/3qGu_Qgroyh0GvUo.jpg") #> new File("/Users/yeli/Desktop/sample1.jpg") !!

         var url = "http://pbs.twimg.com/media/EHUHL5iWwAInzya.jpg"
  
         new URL(url) #> new File("image/sample1.jpg") !!

  
      }
      
      
}