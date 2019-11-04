import scala.util.matching.Regex


object t4 {
    def main(args: Array[String]) {
          val str = "@DeFriendlyTroll Ooh that'd be fun to figure out. \nI've been listening to a podcast that just talks about Star Wars… https://t.co/gZ9fkOKO52"
      
          
          
          
          val address = str.replaceAll("@\\S+", "").replaceAll("http\\S+", "").replaceAll("RT", "").replaceAll("\\n|\\r", " ")
          
          
          println(address.trim())
          
    }
}