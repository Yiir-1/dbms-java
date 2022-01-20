package wyr;

import java.io.IOException;
import java.util.LinkedList;
/**
 * 
 * @ClassName:  Grant   
 * @Description:TODO主要是来授权的
 * @author: 王怡如  
 * @Copyright:
 */
public class Grant {
	public static LinkedList<Grantclass> grantpeople=new LinkedList<Grantclass>();
	public static void grant(String sql) throws IOException
	{
		//命令解析语句
		if(Utils.CheckGrant(sql)) {
			Grant.Grantsql(sql);
		}
		else {
			System.out.println("您输入的语法词法有问题，请重新输入!");
			Input.get();
		}
		
	}
	public static void Grantsql(String sql) throws IOException {
		int tablenameindex=0;
		int usernameindex=0;
		boolean grantop=false;
		String[] strings=sql.split(" ");
		for(int i=0;i<strings.length;i++) {
			if(strings[i].equals("on")) {
				tablenameindex=i+1;
			}
			if(strings[i].equals("to")) {
				usernameindex=i+1;
			}
			if(strings[i].equals("with")&&strings[i+1].equals("grant")&&strings[i+2].equals("option;")) {
				grantop=true;
			}
		}
		
		String username=strings[usernameindex].trim();
		String tablename=strings[tablenameindex].trim();
		String priname=strings[1].trim();
		String[] prinames=priname.split(",");
		LinkedList<String>primelist=new LinkedList<String>();
		for(int i=0;i<prinames.length;i++) {
			prinames[i]=prinames[i].trim();
			primelist.add(prinames[i]);
			for(int j=0;j<Grant.grantpeople.size();j++) {
				if(Grant.grantpeople.get(i).equals(Input.nowuser)) {
					int flag=0;
					for(int k=0;k<Grant.grantpeople.get(i).privileges.size();k++) {
						if(Grant.grantpeople.get(i).privileges.get(k).equals(prinames[i])||Grant.grantpeople.get(i).privileges.get(k).equals("all")) {
							flag=1;
						}
					}
					if(Grant.grantpeople.get(i).grantoption==false) {
						flag=0;
					}
					if(flag==0) {
						System.out.println("您输入的语句语义有问题，请重新输入！");
					}
				}
			}
		}
		usergrantuser(username,Input.nowuser,primelist,grantop);
		System.out.println("设置权限成功！");
		Utils.writegrant(grantpeople);
		Input.get();
		
	}
	//用户赋权，使用此函数
	public static void usergrantuser(String user,String haveuser,LinkedList<String> p,boolean grantoption)
	{
		
		Grantclass tempgrant=new Grantclass(user,haveuser,p);
		tempgrant.grantoption=grantoption;
		Grant.grantpeople.add(tempgrant);
	}
	//若不是用户赋权，则使用此函数
	public  void dbagrantuser(String user,LinkedList<String> p,boolean grantoption)
	{
		
		Grantclass tempgrant=new Grantclass(user,"DBA",p);
		tempgrant.grantoption=grantoption;
		Input.grant.grantpeople.add(tempgrant);
	}
}
