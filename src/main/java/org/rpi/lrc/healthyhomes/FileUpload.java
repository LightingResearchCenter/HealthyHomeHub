package org.rpi.lrc.healthyhomes;

import java.io.File;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.derby.client.am.SqlException;
import org.rpi.lrc.database.Dbconnection;
import org.rpi.lrc.util.Readsubjectcsv;
import org.rpi.lrc.util.Readtreatment;
import org.rpi.lrc.util.SaveToFile;
import org.rpi.lrc.util.UnzipUtility;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/fileupload")
public class FileUpload {
	
		public static String uploadlocation="/home/pi/Desktop/";
		public static String archivelocation="Data";
		@POST
	    @Consumes(MediaType.MULTIPART_FORM_DATA)
		@Produces(MediaType.TEXT_PLAIN)
	    public Response uploadFile(
	    		@FormDataParam("file") InputStream uploadedInputStream,
		        @FormDataParam("file") FormDataContentDisposition fileDetail)
	    {
	    	System.out.println("Started Processing the POST Request send via IPhone.");
	    	String output="";
	        String uploadedFileLocation = uploadlocation + fileDetail.getFileName();
	        // save it
	        if(fileDetail.getFileName().equals("archive.zip"))
	        {
		        File  objFile=new File(uploadedFileLocation);
		        System.out.println("creating folder data storage");
		        String temp= String.valueOf(System.currentTimeMillis()/1000);
		        File newFile= new File("/home/pi/Desktop/Data/"+temp);
		        if(!newFile.exists())
		        {
		        	newFile.mkdir();
		        }
		        String subid=null,nickname=null;
		        String[] subjectid= new String[100];
		    	float[] duration= new float[100];
		    	String[] hubid= new String[100];
		    	float[] time= new float[100];
		        int arrayindex;
		        if(objFile.exists())
		        {
		        	objFile.renameTo(newFile);
		            objFile.delete();
	
		        }
		        
		        SaveToFile filesave = new SaveToFile();
		        filesave.saveToFile(uploadedInputStream, uploadedFileLocation);
		        /**
		         * 
		         * File is uploaded to the server
		         */
		        output = "File uploaded via Jersey based RESTFul Webservice to: " + uploadedFileLocation;
		        UnzipUtility unzipFile = new UnzipUtility();
		        unzipFile.unziper();
		        /**
		         * 
		         * File is unzipped to the hub 
		         */
		        
		        System.out.println("now reading the subject file");
		       Readsubjectcsv reader = new Readsubjectcsv();
		       reader.readcsvfile();
		       subid=reader.getsubid();
		       nickname= reader.getnickname();
		       System.out.println(subid);
		        /**
		         * 
		         * .csv files are read
		         */
		       
		        Dbconnection database= new Dbconnection();
		        database.connection();
		        
		        /**
		         * 
		         *  db connected
		         */
		        int ii=0;
		        boolean temp_db= database.checkuserid(subid, nickname);
		        if(temp_db==false)
		        	ii=database.updateuser(subid, nickname);
		        Readtreatment retreat = new Readtreatment();	
		        
		        arrayindex=retreat.readtreatmentcsvfile();

		        duration=retreat.getduration();
		        subjectid=retreat.getsubid();
		        hubid=retreat.gethubid();
		        time=retreat.gettime();
		        //Deleting previous and current treatments. 
		        database.deletealltreatment();
		        for (int ctr=0;ctr<arrayindex;ctr++)
		        {
		        	boolean i=  database.addtreatment(time[ctr],subjectid[ctr], hubid[ctr], duration[ctr]);
		        }
		        System.out.println("File has been successfully uploaded to the hub and data has been stored in the Database.");
		        try{Dbconnection.connection.close();}
		        catch(Exception e){
		      e.printStackTrace();
		        }
		        
		        return Response.status(200).entity(output).build();
		        }
		        else
		        {
		        	output="File Type not Valid";
		        	return Response.status(415).entity(output).build();
		        }
		    }
}
