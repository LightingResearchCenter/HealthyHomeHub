package org.rpi.lrc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Dbconnection {

	public static Connection connection =null;
	private static final String DB_DRIVER="org.postgresql.Driver";
	private static final String DB_URL="jdbc:postgresql://localhost:5432/healthyhomes";
	private static final String DB_USER="postgres";
	private static final String DB_PASSWORD="postgres";
	private long[] starttime=new long[10];
	public void connection()
	{
		
		try
		{
			Class.forName(DB_DRIVER);
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Include POSTGRESQL in Path");
			e.printStackTrace();
		}
		try
		{
			System.out.println(DB_PASSWORD);
			connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
		
		}
		catch(SQLException e)
		{
			System.out.println("Connection FAILED!!");
			e.printStackTrace();
		}
		if(connection != null)
		{
			System.out.println("Connection Sucessfully created to the database.");
		}
		else
		{
			System.out.println("Connection failed");
		}
	}
	public boolean checkuserid(String subject_id, String nickname)
	{
		try
		{
			String query= "Select subid from subject where subid= ?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, subject_id);
			ResultSet resultset= ps.executeQuery();
			while (resultset.next())
			{	
				if(resultset.getString("subid").equals(subject_id))
						return false;
			}
			addnewuser(subject_id,nickname);
		}
		catch(SQLException eox)
		{
			eox.printStackTrace();
		}
		return true;
	}
	
	public void addnewuser(String subject_id, String nickname) throws SQLException
	{
		System.out.println(subject_id+","+nickname);
		try
		{
			String query= "INSERT into subject(subid,nickname)"+"values(?,?)";
			PreparedStatement ps= (PreparedStatement) connection.prepareStatement(query);
			ps.setString(1, subject_id);
			ps.setString(2, nickname);
			ps.execute();
			System.out.println("query is success");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public boolean addtreatment(Float time,String subject_id, String hubid,float duration)
	{
		try
		{
			String query = "INSERT into treatment(subject_id,hubid,duration,starttime)"+"values(?,?,?,?)";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setFloat(4,time);
			ps.setString(1, subject_id);
			ps.setString(2, hubid);
			ps.setFloat(3, duration);
			ps.execute();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return true;
	}
	public void updatetreatment(long time,String subject_id, int hubid, float duration)
	{
		try
		{
			System.out.println("Inside Update Treatment");
			String query= "UPDATE treatment SET hubid=?, duration=?,starttime=? WHERE subject_id=?";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setInt(1, hubid);
			pst.setFloat(2, duration);
			pst.setLong(3, time);
			pst.setString(4, subject_id);
			pst.execute();
		}
		catch(SQLException sqx)
		{
			sqx.printStackTrace();
		}
	}
	public int updateuser(String id, String nickname)
	{
		try
		{
			String query="UPDATE subject set nickname=? WHERE subid=?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, nickname);
			ps.setString(2, id);
			ps.execute();
		}	
		catch(SQLException sqlx)
		{
			sqlx.printStackTrace();
		}
		return 1;
	}
	public void updatelocation(String subid, int loc)
	{
		try
		{
			/**
			 * Fetching the value of the old location
			 */
			String query="SELECT curr_loc from subject where subid=?";
			PreparedStatement ps= connection.prepareStatement(query);
			ps.setString(1, subid);
			ResultSet rs=ps.executeQuery();
			rs.next();
			int prev_loc=rs.getInt("curr_loc");
			System.out.println("The previous location is : "+prev_loc);
			/**
			 * Updaing the new and old locations
			 */
			String query2="UPDATE subject SET prev_loc=?, curr_loc=? WHERE subid=?";
			PreparedStatement pst= connection.prepareStatement(query2);
			pst.setInt(1, prev_loc);
			pst.setInt(2, loc);
			pst.setString(3, subid);
			pst.execute();
			System.out.println("The new location of the user has been updated in the database.");
		}
		catch(SQLException eox)
		{
			eox.printStackTrace();
		}
	}
	public int getcurrloc(String subid)
	{
		try
		{
			System.out.println("in Curr loc");
			String query="SELECT curr_loc from subject where subid=?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, subid);
			ResultSet rs=ps.executeQuery();
			rs.next();
			int curr_loc= rs.getInt("curr_loc");
			return curr_loc;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	public int getprevloc(String subid)
	{	
		try
		{
			String query="SELECT prev_loc from subject where subid=?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, subid);
			ResultSet rs=ps.executeQuery();
			rs.next();
			int prev_loc= rs.getInt("prev_loc");
			return prev_loc;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	public int[] getallgroups(int location)
	{
		int[] group= new int[5];
		try{
			int i=0;
			String query="SELECT groupid from locationmapping where beconid=?";
			PreparedStatement ps= connection.prepareStatement(query);
			ps.setInt(1, location);
			ResultSet rs= ps.executeQuery();
			 //group=rs.getInt(1);
			while(rs.next())
			{
				group[i]=rs.getInt("groupID");
				i++;
			}
			return group;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return group;
	}
	public int getgroupid(int beaconid)
	{
		int groupid=-1;
		try
		{
			String query="SELECT groupid from locationmapping where beaconid=?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setInt(1, beaconid);
			ResultSet rs= ps.executeQuery();
			rs.next();
			groupid=rs.getInt(1);
			return groupid;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return groupid;
	}
	public long[] endtime(String username)
	{
		int i=-1;
		try
		{
			String query="SELECT starttime from treatment where subject_id=?";
			PreparedStatement ps= connection.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs= ps.executeQuery();
			rs.next();
			while(rs.next())
			{
				i++;
				starttime[i]=rs.getLong(1);
				System.out.println("The start times are: "+ starttime[i]+"\n");
			}
			return starttime;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return starttime;
	}
	public TreeMap<Long,Float> gettimedur(String username)
	{
		int i=0;
		long start_time=-1;
		float dur=-1;
		TreeMap<Long,Float> map=new TreeMap<Long, Float>();
		try
		{
			String query= "SELECT starttime, duration from treatment";
			PreparedStatement ps= connection.prepareStatement(query);
		//	ps.setString(1, username);
			ResultSet rs= ps.executeQuery();
			rs.next();
			while(rs.next())
			{
				i++;
				start_time=rs.getLong(1);
				dur=rs.getFloat(2);
				map.put(start_time, dur);
			}
			return map;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return map;
	}
	public void deletealltreatment()
	{
		try{
		
			String query="Delete from treatment";
			PreparedStatement ps= connection.prepareStatement(query);
			ps.execute();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
	}
}
