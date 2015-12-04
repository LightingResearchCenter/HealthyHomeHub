package org.rpi.lrc.healthyhomes;


import org.rpi.lrc.healthyhomes.MyResource;


import java.util.Map.Entry;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.rpi.lrc.classes.PutJson;
import org.rpi.lrc.database.Dbconnection;
import org.rpi.lrc.schedule.HighCS;
import org.rpi.lrc.schedule.Lightsoff;
import org.rpi.lrc.schedule.Lightson;

@Path("/locupdate")
public class LocationUpdate {
	
	private static String userid="";
	private static int location=-1;
	private static int[] group= new int[10];
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(PutJson loc)
	{
		/**
		 * Getting the Bridge ip Address
		 */
		String ip=MyResource.bridgeIp;
		String username= MyResource.username;
		long start_time=-1;
		float duration=-1;
		long end_time=-1;
		int ctr=-1;
		int[] prevgroup;
		TreeMap<Long,Float> maper= new TreeMap<Long,Float>();
		System.out.println("Location update command recived from IPhone.");
		
		/**
		 *   Getting the username and location of the user
		 */
		userid=loc.getUname();
		location=loc.getLoc();
		
		System.out.println("Updating location for user# : "+userid);
		System.out.println("The updated location is :"+location);
		/**
		 * Connecting to the database and updating the new values to the database 
		 */
		Dbconnection dbconnect= new Dbconnection();
		dbconnect.connection();
		/**
		 * updating location
		 */
		dbconnect.updatelocation(userid, location);
		
		/**
		 * Switching off lights in the previous location
		 */
		 int prevloc=dbconnect.getprevloc(userid);
		 prevgroup=dbconnect.getallgroups(prevloc);
	     Lightsoff switchoffLights= new Lightsoff();
	 	 switchoffLights.run(prevgroup);
		/**
		 * Checking if user is out of home!
		 * This is a redundant call to switch off lights. Should be removed in the next version.
		 */
		if(location==-1)
		{
			switchoffLights.run(prevgroup);
			try{Dbconnection.connection.close();}
			catch(SQLException e){ e.printStackTrace(); }
		}

		/**
		 * Switching on lights in the new location
		 */
		else
		{
			maper= dbconnect.gettimedur(userid);
			group=dbconnect.getallgroups(location);
			Lightson switchonLights= new Lightson();
			switchonLights.run(group);
			
			//Checking and executing if any treatment schedule is available
			for(Entry<Long, Float> entry : maper.entrySet())
			{
				start_time=(Long) entry.getKey();
				duration=(Float) entry.getValue()*60;
				if((System.currentTimeMillis()/1000) >= (start_time+duration))
				{
					
				}
				else
				{
					System.out.println("There exsists a treatment.");
					System.out.println("The treatment starts at: "+start_time+" for "+duration+" minutes.");
					long dur= (long) duration;
					end_time=start_time+dur;
					while((System.currentTimeMillis()/1000) < start_time)
					{
						continue;
					}
					while((System.currentTimeMillis())/1000 >= start_time && (System.currentTimeMillis())/1000 <= end_time)
					{
						if(ctr==-1)
						{
						int i=0;
						while(group[i]!=0 && i<group.length)
						{
							try
					    	{	
								String url1="http://"+ip+"/api/"+username+"/groups/"+group[i]+"/action";
					    		URL url= new URL("http://"+ip+"/api/"+username+"/groups/"+group[i]+"/action");
								HttpURLConnection hurl=(HttpURLConnection) url.openConnection();
								hurl.setRequestMethod("PUT");
								hurl.setDoOutput(true);
								hurl.setRequestProperty("Content-Type", "application/json");
								hurl.setRequestProperty("Accept","application/json");
								OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
								String payload="{\"on\":true,\"bri\":255}";
								osw.write(payload);
								osw.flush();
								osw.close();
								HighCS highcsMode= new HighCS();
								highcsMode.high(group[i]);
								i++;
								Dbconnection.connection.close();
					    	}
					    	catch(Exception e)
					    	{
					    		e.printStackTrace();
					    	}
						}
						
						ctr=1;
						}
		
					}
					Dbconnection db= new Dbconnection();
					db.connection();
					int location=db.getcurrloc(userid);
					group=db.getallgroups(location);
					//time.schedule( new Lightsoff(), end_time);
					int i=0;
					while(group[i]!=0 && i<group.length)
					{
						try
				    	{	
				    		URL url= new URL("http://"+ip+"/api/"+username+"/groups/"+group[i]+"/action");
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
							i++;
				    	}
				    	catch(Exception e)
				    	{
				    		e.printStackTrace();
				    	}
					}
					System.out.println("Treatment has ended.");
					try{Dbconnection.connection.close();}
					catch(SQLException e)
					{e.printStackTrace();}
				}
			}
		}
	
		
	}
}

