package es.upm.fi.oeg.morph.streams.esper

import org.scalatest.junit.JUnitSuite
import org.slf4j.LoggerFactory
import es.upm.fi.oeg.morph.esper.EsperServer
import es.upm.fi.oeg.morph.stream.esper.EsperAdapter
import org.junit.Before
import es.upm.fi.oeg.morph.esper.EsperProxy
import es.upm.fi.oeg.morph.stream.esper.DemoStreamer
import org.junit.Test
import org.junit.After
import es.upm.fi.oeg.morph.stream.esper.HslStreamer

class HslStreamerTest extends JUnitSuite {
	private val log = LoggerFactory.getLogger(this.getClass)
	
	lazy val esper = new EsperServer
	
	val eval = new EsperAdapter(esper.system)
	
	@Before def setUpBeforeClass() {
	    esper.startup()
	    val proxy = new EsperProxy(esper.system)
	    val demo = new HslStreamer("RHKL00040", "hsl", 1, proxy) 
	    demo.schedule
	    println("finish init")
	}
	
	@Test def naiveTest() {
	  Thread.sleep(10000)
	}
	
	@After def after(){
	    log.debug("*************** EXITING NOW ***************")
	    esper.shutdown
	}
}