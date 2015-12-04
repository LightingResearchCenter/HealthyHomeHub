package org.rpi.lrc.healthyhomes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.rpi.lrc.util.BridgeInformation;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/username")
public class BridgeId {

	@GET
	public String run()
	{
		String uname="";
		BridgeInformation bi= new BridgeInformation();
		//uname=bi.run();
		return uname;
	}
}
