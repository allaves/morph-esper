package es.upm.fi.oeg.morph.stream.esper
import scala.util.Random
import scala.compat.Platform
import es.upm.fi.oeg.morph.esper.EsperProxy
import concurrent.duration._
import scala.language.postfixOps

class DemoStreamer(vehicleId:String, extent:String, rate:Int, proxy:EsperProxy) {
  private var latestTime:Long=0
  private val rand = new Random
  def generateData={
    latestTime=5//data.internalTime
//	Some(List("stationId"->stationid,//data.stationId,	      
//	     "internalTime"->Platform.currentTime,////data.internalTime,
//	     "observationTime"->rand.nextLong,
//	     "airPressure"-> rand.nextDouble,
//	     "temperature"->rand.nextDouble,
//	     "relativeHumidity"->rand.nextDouble,
//	     "windSpeed"->rand.nextDouble,
//	     "timestamp"-> rand.nextDouble).toMap)	    
    
    Some(List("vehicleId" -> vehicleId,	      
	     "route" -> rand.nextString(4),
	     "lat" -> rand.nextDouble,
	     "lon" -> rand.nextDouble,
	     "bearing" -> rand.nextDouble,
	     "direction" -> rand.nextInt,
	     "previousStop" -> rand.nextDouble,
	     "currentStop" -> rand.nextDouble,
	     "departure" -> rand.nextString(4)).toMap)
  }

  def schedule{
    val eng = proxy.engine
    import  proxy.system.dispatcher
    proxy.system.scheduler.schedule(0 seconds, rate seconds) {
      val tosend = generateData.get
      println("sending" + tosend.mkString("--"))
      eng ! es.upm.fi.oeg.morph.esper.Event(extent,tosend)
    }             
  }
  
}