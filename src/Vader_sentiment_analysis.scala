import com.vader.sentiment.analyzer.SentimentAnalyzer


object Vader_sentiment_analysis {
      
  def detectSentiment(mess: String): String = {
    val sentimentAnalyzer = new SentimentAnalyzer(mess);
          
    sentimentAnalyzer.analyze()      
    val map = sentimentAnalyzer.getPolarity()
      
    if (map.get("negative") >= map.get("neutral") && map.get("negative") >= map.get("positive"))
      return "negative"
    else if (map.get("positive") >= map.get("negative") && map.get("positive") >= map.get("negative"))
      return "positive"
    else
      return "neutral"
  }

}
