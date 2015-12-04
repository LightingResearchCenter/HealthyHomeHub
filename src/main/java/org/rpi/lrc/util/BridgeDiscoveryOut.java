package org.rpi.lrc.util;

import java.io.BufferedReader;


import com.google.gson.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;


public class BridgeDiscoveryOut {

	private static HttpURLConnection httpconn;
	private String inpstr="error!";
	private String ipaddress="";
	private static String lrc_username="001788fffe21807b";
	private static String lund_username="001788fffe14ac43";
	public BridgeDiscoveryOut()
	{
		
	}
	public String discover()
	{
	try
	{	
		URL url= new URL("https://www.meethue.com/api/nupnp");
		httpconn = (HttpURLConnection) url.openConnection();
		httpconn.setDoInput(true);
		System.err.println(httpconn.getResponseCode());
		/**
		 * Request Sent
		 */
		System.out.println("1st");
		BufferedReader streamReader= new BufferedReader(new InputStreamReader(httpconn.getInputStream(),"UTF-8"));
		System.out.println("2nd");
		StringBuilder responseStrBuilder= new StringBuilder();
		System.out.println("before while");
		while((inpstr= streamReader.readLine())!= null)
		{
			responseStrBuilder.append(inpstr);
		}
		JSONArray jsonarray=new JSONArray(responseStrBuilder.toString());
		System.out.println(jsonarray.length());
		for(int k=0;k<jsonarray.length();k++)
		{
			JSONObject jsonobject= (JSONObject) jsonarray.get(k);
			String id=jsonobject.optString("id");
			ipaddress=jsonobject.optString("internalipaddress");
			System.out.println("id :"+id);
			System.out.println("ip:"+ipaddress);
			System.out.println("The Mac address used for the philips Bridge is:"+lrc_username);
			if(id.equals(lund_username))
				return ipaddress;
		}
		
		return ipaddress;
		/**
		 * Response Received and converted into a String
		 * temp1=responseStrBuilder.substring(responseStrBuilder.lastIndexOf("{\"id\":\"001788fffe14ac43\",\"internalipaddress\":\""), responseStrBuilder.lastIndexOf("{\"id\":\"001788fffe14ac43\",\"internalipaddress\":\"")+58);
		 * In the following line, change 60->58 for the HUB sent out to LUND. 
		 */
//		temp1=responseStrBuilder.substring(responseStrBuilder.lastIndexOf("{\"id\":\"001788fffe21807b\",\"internalipaddress\":\""), responseStrBuilder.lastIndexOf("{\"id\":\"001788fffe14ac43\",\"internalipaddress\":\"")+60);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return inpstr;
}
}