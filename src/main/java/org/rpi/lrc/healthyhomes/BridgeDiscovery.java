package org.rpi.lrc.healthyhomes;
import org.rpi.lrc.util.BridgeDiscoveryOut;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.rpi.lrc.classes.BridgeClass;


@Singleton
public class BridgeDiscovery {
	@Schedule
	public void discover()
	{
		String ipadd="";
		BridgeDiscoveryOut bd = new BridgeDiscoveryOut();
		bd.discover();
		System.out.println("in BridgeDiscovery");
	}

}
