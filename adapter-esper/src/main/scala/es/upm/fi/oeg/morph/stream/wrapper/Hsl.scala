package es.upm.fi.oeg.morph.stream.wrapper

import scala.xml._
import dispatch._
import java.text.DateFormat
import java.util.Locale
import java.util.TimeZone
import scala.language.postfixOps
import org.slf4j.LoggerFactory
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.Random

class Hsl { 
  
  def hslService(vehicleId: String) = { host("83.145.232.209:10001") / 
    ("?type=vehicles&lng1=23&lat1=60&lng2=26&lat2=61&ids=" + vehicleId)
  }
    
    // ?type=vehicles&lng1=23&lat1=60&lng2=26&lat2=61"
//    svc.addQueryParameter("type", "vehicles")
//    svc.addQueryParameter("lng1", "23")
//    svc.addQueryParameter("lat1", "60")
//    svc.addQueryParameter("lng2", "26")
//    svc.addQueryParameter("lat2", "61")
//    svc.addQueryParameter("ids", ids)
    // Vehicle id, e.g. RHKL00109
    // TODO: For various vehicles, separate ids with underscore, e.g. RHKL00109_RHKL00040
  
  def getData(vehicleId: String) = {
    val res = Http(hslService(vehicleId) OK as.String)
    val data = res()
    Observation.fromString(data)
  }
}

object Observation {
  private val log = LoggerFactory.getLogger(this.getClass) 
  private val rand = new Random
  
  def fromString(obs:String) = {
    //var aux = new Array[String](9)
    var obsArray = obs.split(";")
    if (obsArray.length > 1) {
		Observation(obsArray(0),
	    obsArray(1),
	    obsArray(2) toDouble,
	    obsArray(3) toDouble,
	    obsArray(4) toDouble,
	    obsArray(5) toInt,
	    obsArray(6) toDouble,
	    obsArray(7) toDouble,
	    obsArray(8))
	}
    else {
      Observation(rand.nextString(9),	      
	     rand.nextString(4),
	     rand.nextDouble,
	     rand.nextDouble,
	     rand.nextDouble,
	     rand.nextInt,
	     rand.nextDouble,
	     rand.nextDouble,
	     rand.nextString(4))
    }
  }
}

case class Observation(vehicleId:String, 
						route:String, 
						lat:Double, 
						lon:Double,
						bearing:Double, 
						direction:Integer, 
						previousStop:Double, 
						currentStop:Double, 
						departure:String) {}