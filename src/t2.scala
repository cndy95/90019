import org.apache.spark._

object t2 {
    def main(args: Array[String]) {
      
      
      
      val sparkConf = new SparkConf().setAppName("ScalaPython").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)

   
    
    
    val lines = sparkContext.parallelize(Array(
        "37.75889318222431,-122.42683635321838,37.7614213,-122.4240097",
        "37.7519528,-122.4208689,37.8709087,-122.2688365"))
        
        
    lines.foreach(println)
    
    val lines1 = lines.pipe("python src/a.py")
    println("Print lines returned from python")

    lines1.foreach(println)
    System.out.println("Exiting ScalaPython.main")

    }
}