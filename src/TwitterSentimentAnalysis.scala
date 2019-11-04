import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.elasticsearch.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import sys.process._

import scala.util.Try
import Standford_sentiment_analysis._
import Vader_sentiment_analysis._
import java.net.URL
import java.io.File

import java.net._
import java.io._
import scala.io._

object TwitterSentimentAnalysis {
  def main(args: Array[String]) {
  
    
    
    val filters = Array("animal")

    System.setProperty("twitter4j.oauth.consumerKey", "ESFvC3k7nK8RDA32ugWubDjbv")
    System.setProperty("twitter4j.oauth.consumerSecret", "PHq0uBnLnIKbI7HzemlnEVab1GxF4LRtQJj21A4oZoElwcmQpI")
    System.setProperty("twitter4j.oauth.accessToken", "1123536791389102080-zKztR8VKmfZ7yTXuyghfydO4eJ8KTx")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "xumsDCbvF82A49IoF0SoRdw8ZKi7NW8Uj0BNh5cJKHNcT")

    val conf = new SparkConf().setAppName("TwitterSentimentAnalysis").setMaster("local[4]")

  
    val ssc = new StreamingContext(conf, Seconds(1))

    val tweets = TwitterUtils.createStream(ssc, None, filters)

    
    tweets.filter(_.getLang.toString()=="en")
    
    tweets.foreachRDD { (rdd, time) =>
      val media_url = rdd.flatMap(status => status.getMediaEntities.map(_.getMediaURL))

      
      val a = rdd.map(t => {
        
        var media = t.getMediaEntities.map(_.getMediaURL)
        var media_url = ""
        var object_in_image = ""
        var sent_in_image = ""
        
        if (media.length > 0 ){
          media_url = media(0)
          try{
            var path = "image/"+t.getId+".jpg"
            new URL(media_url) #> new File(path) !!
            
            object_in_image = Vgg16_Object.image_prediction(path)
            
            
            val s = new Socket(InetAddress.getByName("45.113.234.239"), 65432)
            lazy val in = new BufferedSource(s.getInputStream()).getLines()
            val out = new PrintStream(s.getOutputStream())
            out.println(media_url)
            out.flush()
            sent_in_image = in.next()
            s.close()
          }catch{
             case x: FileNotFoundException => println("Exception: FileNotFoundException") 

          }
        }
        
        
       val raw_text = t.getText.replaceAll("@\\S+", "").replaceAll("http\\S+", "").replaceAll("#\\S+", "").replaceAll("RT", "").replaceAll("\\n|\\r", " ").trim()
       val sentiment_stf = Standford_sentiment_analysis.detectSentiment(raw_text).toString
       val sentiment_vdr = Vader_sentiment_analysis.detectSentiment(raw_text).toString
      
       var sentiment_matched = false
       if (sentiment_stf.equals(sentiment_vdr)){
         sentiment_matched = true
       }
        val a = t.getHashtagEntities.map(_.getText)
        Map(
          "user" -> t.getUser.getScreenName,
          "created_at" -> t.getCreatedAt,
          "location" -> Option(t.getGeoLocation).map(geo => { s"${geo.getLatitude},${geo.getLongitude}" }),
          "text" -> raw_text,
          "hashtags" -> t.getHashtagEntities.map(_.getText),
          "retweet" -> t.getRetweetCount,
          "sentiment_stf" -> sentiment_stf,
          "sentiment_vdr" -> sentiment_vdr,
          "sentiment_matched" -> sentiment_matched,
          "media_url" ->media_url,
          "object" -> object_in_image,
          "sent_in_image" -> sent_in_image
        )
      })
      

      a.saveToEs("twitter")

//      a.foreach(println)
    }

        
    ssc.start()
    ssc.awaitTermination()
  }
}