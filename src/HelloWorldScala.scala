import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.elasticsearch.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.time.LocalDateTime

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

import scala.util.Try
import sys.process._
import java.net.URL
import java.io.File

object HelloWorldScala {

  def main(args: Array[String]) {
    println("Hello Scala !!")
    val filters = Array("food")

    System.setProperty("twitter4j.oauth.consumerKey", "ESFvC3k7nK8RDA32ugWubDjbv")
    System.setProperty("twitter4j.oauth.consumerSecret", "PHq0uBnLnIKbI7HzemlnEVab1GxF4LRtQJj21A4oZoElwcmQpI")
    System.setProperty("twitter4j.oauth.accessToken", "1123536791389102080-zKztR8VKmfZ7yTXuyghfydO4eJ8KTx")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "xumsDCbvF82A49IoF0SoRdw8ZKi7NW8Uj0BNh5cJKHNcT")

    val conf = new SparkConf().setAppName("TwitterSentimentAnalysis").setMaster("local[4]")

    val ssc = new StreamingContext(conf, Seconds(1))
    val stream = TwitterUtils.createStream(ssc, None, filters)
        
    
    stream.foreachRDD{ (rdd, time) =>
      
      
      
      val a = rdd.map(t => {
        var media = t.getMediaEntities.map(_.getMediaURL)
        var media_url = ""
        var object_in_image = ""
        if (media.length > 0 ){
          media_url = media(0)
       
          var path = "image/"+t.getId+".jpg"
          new URL(media_url) #> new File(path) !!
          
          object_in_image = Vgg16_Object.image_prediction(path)
          
        }
        Map(
          "user" -> t.getUser.getScreenName.toString,
          "created_at" -> t.getCreatedAt.getTime.toString,
          "id" -> t.getId,
          "media_url" ->media_url,
          "object" -> object_in_image
          
         
        )
      })
      a.foreach(println)
      
    }
      
      
    ssc.start()
    ssc.awaitTermination()

  }
}
