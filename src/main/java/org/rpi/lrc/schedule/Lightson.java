package org.rpi.lrc.schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;
import org.rpi.lrc.healthyhomes.LocationUpdate;
import org.rpi.lrc.healthyhomes.MyResource;
public class Lightson {
	
public	Lightson()
	{
		System.out.println("In the constructor!!");
	}
	
	public void run(int[] group)
	{
		int i=0;
		String ip=MyResource.bridgeIp;
		String username= MyResource.username;
		while(group[i]!=0 && i<=group.length)
		{
			try
	    	{	
				System.out.println("Switching on Lights for group: "+group[i]);
	    		URL url= new URL("http://"+ip+"/api/"+username+"/groups/"+group[i]+"/action");
	    		System.out.println("the url is: "+url);
				HttpURLConnection hurl=(HttpURLConnection) url.openConnection();
				hurl.setRequestMethod("PUT");
				hurl.setDoOutput(true);
				hurl.setRequestProperty("Content-Type", "application/json");
				hurl.setRequestProperty("Accept","application/json");
				OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
				String payload="{\"on\":true,\"bri\":80,\"ct\":370}";
				osw.write(payload);
				osw.flush();
				osw.close();
//				System.out.println(hurl.getResponseCode());
//				System.out.println(hurl.getResponseMessage());
//				BufferedReader br= new BufferedReader(new InputStreamReader(hurl.getInputStream()));
//				String line="";
//				while((line=br.readLine())!= null)
//				{
//					System.out.println(line);
//				}
//				osw.flush();
//				osw.close();
				System.out.println(hurl.getResponseCode());
				System.out.println(hurl.getResponseMessage());
				BufferedReader br= new BufferedReader(new InputStreamReader(hurl.getInputStream()));
				String line="";
				while((line=br.readLine())!= null)
				{
					System.out.println(line);
				}
				i++;
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
		}
		System.out.println("Lights have been switched on in the new location.");
	}
	
}

