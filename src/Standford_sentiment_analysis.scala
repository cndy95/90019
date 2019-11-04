

import java.util.Properties
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.AnnotatedTree
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

object Standford_sentiment_analysis {

    
  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline = new StanfordCoreNLP(props)
     

  def detectSentiment(mess: String): String = {
    var mainSent = 0

    // check for non empty tweet text
    if (mess != null && mess.length > 0) {
      var longest = 0
      val annotation = pipeline.process(mess)
      val list = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
      val iter = list.iterator()
      while (iter.hasNext)
      {
        val sentence = iter.next()
        val annotatedTree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
        val prediction = RNNCoreAnnotations.getPredictedClass(annotatedTree)
        val partText = sentence.toString
        if (partText.length > longest) {
          mainSent = prediction
          longest = partText.length
        }
      }
    }

    // define type of sentiment while avoiding the very positive and very negative values
    // which are included in the positive and negative values respectively
    mainSent match {
      case x if x == 0 || x == 1 => "Negative"
      case 2 => "Neutral"
      case x if x == 3 || x == 4 => "Positive"
    }
  }
}