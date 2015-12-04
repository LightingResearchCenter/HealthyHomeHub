package org.rpi.lrc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Readsubjectcsv {
	

	private String subid,dob,nickname;
	
	public void readcsvfile()
	{
		int numberOfRows=0,rowNumber = 0,columnNumber = 0;
        String filename="";
        File f= new File("/home/pi/Desktop/archive/subject.csv");
      //  File f= new File("/users/saurabh/desktop/archive/subject.csv");
        if(f.exists())
        {
        	System.out.println("File exsists!");
        	System.out.println("The name of the file is:"+f.getName());
        }
        filename="/home/pi/Desktop/archive/subject.csv";
        //filename="/users/saurabh/desktop/archive/subject.csv";
        try
        {

			BufferedReader br= new BufferedReader(new FileReader(filename));
			StringTokenizer st=null;	
			while((filename=br.readLine())!=null)
			{
				rowNumber++;
				
				numberOfRows++;
				
				System.out.println("rownumber"+rowNumber);
				st = new StringTokenizer(filename,",");
				
				while(st.hasMoreTokens())
				{
					columnNumber++;				
//					System.out.println("Row"+rowNumber+",Column"+columnNumber+",Data:"+st.nextToken());
					if(rowNumber==2 && columnNumber==1)
					{
						 subid= st.nextToken();
						 nickname= st.nextToken();
						 System.out.println(subid);
						 System.out.println(nickname);
					}
					st.nextToken();
				}
				
				columnNumber=0;
				
			}
			
			br.close();
        }
        catch(IOException e)
        {

        	e.printStackTrace();
        }

	}
		public String getsubid()
		{
			return subid;
		}
		public String getnickname()
		{
			return nickname;
		}
		public String getdob()
		{
			return dob;
		}

}

