package org.rpi.lrc.classes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PutJson {

	public String uname;
	public int loc;
	
	PutJson(String uname, int loc)
	{
		this.uname=uname;
		this.loc= loc;
	}
	PutJson()
	{
		
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
}
