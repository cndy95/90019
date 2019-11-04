import sys.process._
import java.net.URL
import java.io.File
import org.deeplearning4j.zoo.model.VGG16;
import org.deeplearning4j.zoo._;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.datavec.image.loader.NativeImageLoader
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor
import org.deeplearning4j.zoo.util.imagenet.ImageNetLabels;



object Vgg16_Object {
  
  
  val zooModel = VGG16.builder().build();
  val net:ComputationGraph  =zooModel.initPretrained(PretrainedType.IMAGENET);
  val loader = new NativeImageLoader(224, 224, 3)
  
 
  
  
    def image_prediction(path: String): String = {
      
      val image = loader.asMatrix(new File(path))
      val scaler = new VGG16ImagePreProcessor()
      scaler.transform(image)    
      
      
      val predictions = new ImageNetLabels().decodePredictions(net.output(false,image)(0),1);
      return predictions.get(0).get(0).getLabel
    }

}