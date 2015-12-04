package org.rpi.lrc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Readtreatment {
	
//	private String[] subid=null;
//	private float[] duration=null;
//	private int[] hubid=null;
//	private long[] time=null;
	private String[] subid= new String[100];
	private float[] duration= new float[100];
	private String[] hubid= new String[100];
	private float[] time= new float[100];
	
	public int readtreatmentcsvfile()
	{
		int numberOfRows=0,rowNumber = 0,columnNumber = 0;
        String filename="";
        File f= new File("/home/pi/Desktop/archive/treatment.csv");
      //  File f= new File("/users/saurabh/Desktop/archive/treatment.csv");
        
        if(f.exists())
        {
         }
        filename="/home/pi/Desktop/archive/treatment.csv";
       // filename="/users/saurabh/desktop/archive/treatment.csv";
        try
        {

			BufferedReader br= new BufferedReader(new FileReader(filename));
			StringTokenizer st=null;
			int arrayindex=0;
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
					
					System.out.println("outside if");
					if(rowNumber>=2 && columnNumber>=1)
					{
						System.out.println("inside the if");
						 time[arrayindex]=Float.parseFloat(st.nextToken());
						 duration[arrayindex]= Float.parseFloat(st.nextToken());					 
						 subid[arrayindex]= st.nextToken();
						 hubid[arrayindex]=st.nextToken();
						 arrayindex++;
						continue;
					}
					st.nextToken();
				}
				
				columnNumber=0;
				
			}
			br.close();
			return arrayindex;		
        }
        catch(IOException e)
        {

        	e.printStackTrace();
        }
		return -1;

	}
		public String[] getsubid()
		{
			return subid;
		}
		public String[] gethubid()
		{
			return hubid;
		}
		public float[] getduration()
		{
			return duration;
		}
		public float[] gettime()
		{
			return time;
		}

}

