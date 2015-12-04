
package org.rpi.lrc.healthyhomes;

import javax.ws.rs.Consumes;

import org.rpi.lrc.util.BridgeDiscoveryOut;
import org.rpi.lrc.util.BridgeInformation;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.rpi.lrc.classes.PutJson;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/myresource")
public class MyResource {
    
	public static  String bridgeIp= new String();
	public static String username= new String();
    @GET 
    @Produces("text/plain")
    public String getIt() 
    {
    	/*
    	 *Getting the Bridge Ip Address 
    	 */
    	String res="success";
    	String temp="";
    	String temp1="";
        BridgeDiscoveryOut bd= new BridgeDiscoveryOut();
        bridgeIp=bd.discover();
      //  System.out.println("The IP Address Assigned to the bridge is: "+bridgeIp);
        /*
         * Getting the username from the Bridge
         */
        BridgeInformation bi= new BridgeInformation();
       temp= bi.run(bridgeIp);
       
       temp1=temp.substring(temp.lastIndexOf(":\""),temp.indexOf("\"}}]"));
       System.out.println(username);
       username=temp1.substring(2);
       System.out.println(username+" is the username assigned.");
        if(temp.contains("error")|| bridgeIp=="error!")
        {
        	res="failure";
        	return res;
        }
        return res;
        
    }
 }

