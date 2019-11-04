
import com.vader.sentiment.analyzer.SentimentAnalyzer

object VaderTest {
  
  
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
          val sentimentAnalyzer = new SentimentAnalyzer(i);
          sentimentAnalyzer.analyze()   
          println(sentimentAnalyzer.getPolarity())

        }
     
      
      }
      
      

}