package wyr;

import java.util.LinkedList;

public class Grantclass {
	public String grantname;
	public String havename;
	public LinkedList<String> privileges;
	public boolean grantoption=false;
	Grantclass(String grantname,String havename,LinkedList<String> privileges)
	{
		this.grantname=grantname;
		this.havename=havename;
		this.privileges=privileges;
	}
	Grantclass()
	{
		this.grantname=null;
		this.havename=null;
		this.privileges=null;
	}
}
