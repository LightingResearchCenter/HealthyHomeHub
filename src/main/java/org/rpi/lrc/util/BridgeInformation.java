package org.rpi.lrc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jettison.json.JSONArray;

public class BridgeInformation {

	public String run(String ip)
	{
		String line="error!";
		System.out.println("PLEASE PRESS THE BUTTON!!!!!");
		int delay_in_pressing_button=2;
		Long starttime;
//		while(System.currentTimeMillis()/1000 != starttime)
//		{
//			continue;
//		}
		for (int i=0;i<5;i++)
		{
			try
	    	{	
				System.out.println("Sending Signal # "+i);
	    		URL url= new URL("http://"+ip+"/api");
				HttpURLConnection hurl=(HttpURLConnection) url.openConnection();
				hurl.setRequestMethod("POST");
				hurl.setDoOutput(true);
				hurl.setRequestProperty("Content-Type", "application/json");
				hurl.setRequestProperty("Accept","application/json");
				OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
				String payload="{\"devicetype\":\"my_hue_app#iphone\"}";
				osw.write(payload);
				osw.flush();
				osw.close();
				System.err.println(hurl.getResponseCode());
				BufferedReader br= new BufferedReader(new InputStreamReader(hurl.getInputStream()));
				StringBuilder sb= new StringBuilder();
				if((line=br.readLine()).contains("error"))
				{
					starttime=System.currentTimeMillis()/1000+delay_in_pressing_button;
					while(System.currentTimeMillis()/1000!=starttime)
						{
						}
				}
				else
				{
					System.out.println(line);
					sb.append(line);
					br.close();
					return sb.toString();
				}
				 
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
		}
		return line;
	}
}