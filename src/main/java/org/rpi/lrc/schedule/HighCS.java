package org.rpi.lrc.schedule;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.rpi.lrc.healthyhomes.MyResource;

public class HighCS {

	public void high(int group)
	{	
		String ip=MyResource.bridgeIp;
		String username= MyResource.username;
		try
		{
			URL url= new URL("http://"+ip+"/api/"+username+"/groups/"+group+"/action");
			HttpURLConnection hurl=(HttpURLConnection) url.openConnection();
			hurl.setRequestMethod("PUT");
			hurl.setDoOutput(true);
			hurl.setRequestProperty("Content-Type", "application/json");
			hurl.setRequestProperty("Accept","application/json");
			OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
			String payload="{\"on\":true,\"ct\":200}";
			osw.write(payload);
			osw.flush();
			osw.close();
			System.out.println("The Treatment has started.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}					
	}
}