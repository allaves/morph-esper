package es.upm.fi.oeg.morph.stream.esper
import es.upm.fi.oeg.morph.stream.wrapper.Hsl
import es.upm.fi.oeg.morph.esper.EsperProxy
import scala.language.postfixOps
import concurrent.duration._

class HslStreamer(vehicleId:String, extent:String, rate:Int, proxy:EsperProxy) {
  private val hsl = new Hsl()
  //private var latestTime:Long = 0
  
  def generateData(vehicleId: String) = {
    val data = hsl.getData(vehicleId)
    
    Some(List("vehicleId" -> data.vehicleId,
    	"route" -> data.route,
    	"lat" -> data.lat,
    	"lon" -> data.lon,
    	"bearing" -> data.bearing,
    	"direction" -> data.direction,
    	"previousStop" -> data.previousStop,
    	"currentStop" -> data.currentStop,
    	"departure" -> data.departure).toMap)
  }
  
  def schedule{
    val eng = proxy.engine
    import  proxy.system.dispatcher
    proxy.system.scheduler.schedule(0 seconds, rate seconds) {
      val tosend = generateData(vehicleId).get
      println("Sending... " + tosend)
      eng ! es.upm.fi.oeg.morph.esper.Event(extent, tosend)
    }             
  }
}