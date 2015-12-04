package org.rpi.lrc.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

	
	  private static final int BUFFER_SIZE = 4096;
	 
	    
	    
//	    String zipFilePath="/users/saurabh/Desktop/archive.zip";
//	    String destDirectory="/users/saurabh/Desktop/";
	    String zipFilePath="/home/pi/Desktop/archive.zip";
	    String destDirectory="/home/pi/Desktop/archive/";
	    
	    public void unziper()
	    {
	    	try
	    	{
	    		System.out.println("calling unzipUtility->unzip");
	    	unzip(zipFilePath, destDirectory);
	    	}
	    	catch(IOException e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	    public void unzip(String zipFilePath, String destDirectory) throws IOException {
	        File destDir = new File(destDirectory);
	        if (!destDir.exists()) {
	        	System.out.println("making a directory");
	            destDir.mkdir();
	        }
	        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	        ZipEntry entry = zipIn.getNextEntry();
	        // iterates over entries in the zip file
	        while (entry != null) {
	            String filePath = destDirectory+ "/" + entry.getName();
	            if (!entry.isDirectory()) {
	            	System.out.println("Extracting the files");
	                // if the entry is a file, extracts it
	                extractFile(zipIn, filePath);
	            } else {
	                // if the entry is a directory, make the directory
	                File dir = new File(filePath);
	                dir.mkdir();
	            }
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
	        zipIn.close();
	    }
	    
	    /**
	     * Extracts a zip entry (file entry)
	     * @param zipIn
	     * @param filePath
	     * @throws IOException
	     */
	    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
	        byte[] bytesIn = new byte[BUFFER_SIZE];
	        int read = 0;
	        while ((read = zipIn.read(bytesIn)) != -1) {
	            bos.write(bytesIn, 0, read);
	        }
	        bos.close();
	    }
}