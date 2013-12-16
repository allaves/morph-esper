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

class Hsl(ids:String) { 

  //private val client = Client.create
  private val webResource = {
    // Request for all vehicles within lat/lng
    val svc = url("http://83.145.232.209:10001/")
    // ?type=vehicles&lng1=23&lat1=60&lng2=26&lat2=61"
    svc.addQueryParameter("type", "vehicles")
    svc.addQueryParameter("lng1", "23")
    svc.addQueryParameter("lat1", "60")
    svc.addQueryParameter("lng2", "26")
    svc.addQueryParameter("lat2", "61")
    svc.addQueryParameter("ids", ids)
    // Vehicle id, e.g. RHKL00109
    // TODO: For various vehicles, separate ids with underscore, e.g. RHKL00109_RHKL00040
    svc   
  }
  
  def getData = {
    val res = Http(webResource OK as.String)
    // Trying futures and promises
//    val res: Future[String] = future {
//      Http(webResource OK as.String)
//    }
//    
//    res onComplete {
//      case Success(rawObs) => obs = Observation.fromString(rawObs toString)
//      case Failure(t) => println("An error has occurred: " + t.getMessage)
//    }
    val obs = Observation.fromString(res toString)
	obs
  }
}

//case class Location(full:String,neighborhood:String,
//    city:String,state:String,latitude:Double,longitude:Double){
//}
//
//object Location{
//  def fromXml(loc:Node)=
//    Location(loc\"full" text,loc\"neighborhood" text,
//        loc\"city" text,loc\"state" text,
//        (loc\"latitude").text toDouble,
//        (loc\"longitude").text toDouble)
//}

object Observation {
  private val log = LoggerFactory.getLogger(this.getClass) 
  //private val format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.UK)
  //format.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"))
  
  def fromString(obs:String) = {
    //val timeString:String=(obs\"observation_time_rfc822").text
    var obsArray = new Array[String](9)
    obsArray = obs.split(";")
    //log.debug(obs)
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