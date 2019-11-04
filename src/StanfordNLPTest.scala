
import java.util.Properties
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.AnnotatedTree
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

object StanfordNLPTest {
  
   val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline = new StanfordCoreNLP(props)
     

  def detectSentiment(mess: String): Int = {
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

  
    return mainSent
  }
   
   
   
  def main(args: Array[String]) {
        val sent1 = "The food here is great"
        val sent2 = "The food here is great!"
        val sent3 = "The food here is great!!"
        val sent4 = "The food here is GREAT!!"
        val sent5 = "The food here is very great!!"
        val sent6 = "The food here is extremely great"
        val sent7 = "The food here is great, but service sucks"
        val sent8 ="La comida aquí es muy buena."

        val emo1 = ":)"
        val emo2 = ":("
        val emo3 = "^_^"
        
        val acr1 = "LOL"
        val acr2 = "WTF"
    

        val array = Array(sent1,sent2,sent3,sent4,sent5,sent6,sent7,sent8,emo1, emo2, emo3,acr1,acr2)
       
        
        for (i <- array){
          println(i)
          val sentimentAnalyzer = detectSentiment(i);
          println(sentimentAnalyzer)
          

        }
     
      
      }
      
      

}