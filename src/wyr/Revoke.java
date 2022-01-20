package wyr;

import java.io.IOException;

public class Revoke {
	public static void revoke(String sql) throws IOException {
		// 命令解析语句
		if (Utils.CheckGrant(sql)) {
			Revoke.revokesql(sql);
		} else {
			System.out.println("您输入的语法词法有问题，请重新输入!");
			Input.get();
		}
		
	}
	public static void revokesql(String sql) throws IOException {
		int tablenameindex=0;
		int usernameindex=0;
		String[] strings=sql.split(" ");
		for(int i=0;i<strings.length;i++) {
			if(strings[i].equals("on")) {
				tablenameindex=i+1;
			}
			if(strings[i].equals("from")) {
				usernameindex=i+1;
			}
			
		}
		String username=strings[usernameindex].trim();
		String tablename=strings[tablenameindex].trim();
		String priname=strings[1].trim();
		String[] prinames=priname.split(",");
		//判断语义
		for(int i=0;i<Input.grant.grantpeople.size();i++) {
			if(Input.grant.grantpeople.get(i).havename.equals(username)) {
				System.out.println("由于此前有级联授权，因此本次revoke失败，请重新输入！");
				Input.get();
			}
		}
		if(username.contains(";")) {
			username=username.substring(0,username.length()-1);
		}
		//删除操作
		
		for(int i=0;i<Input.grant.grantpeople.size();i++) {
			if(Input.grant.grantpeople.get(i).grantname.equals(username)) {
				
				for(int j=0;j<prinames.length;j++) {
					for(int k=0;k<Input.grant.grantpeople.get(i).privileges.size();k++) {
						if(prinames[j].equals(Input.grant.grantpeople.get(i).privileges.get(k))) {
							Input.grant.grantpeople.get(i).privileges.remove(k);
							
						}
					}
				}
			}
		}
		System.out.println("删除授权成功！请重新输入！");
		Input.get();
	}
}
