package wyr;

import java.io.IOException;
import java.util.LinkedList;

/**
 * 
 * @ClassName:  Update   
 * @Description:执行更新语句对数据库进行更新
 * @author: 王怡如  
 * @Copyright:
 */
public class Update {
	/**
	 * 
	 * @Title: updatesql   
	 * @Description: 主要用于判断语句是否符合语法和词法 
	 * @param sql      
	 * @return: void      
	 * @throws IOException 
	 * @throws
	 */
	public static void updatesql(String sql) throws IOException {
		if(Utils.CheckUpdate(sql)) {
			Update(sql);
		}
		else {
			System.out.println("该语句语法词法分析有问题，请重新输入!");
			Input.get();
		}
	}
	/**
	 * 
	 * @Title: Update   
	 * @Description: 主要用于进行数据更新 
	 * @param sql
	 * @throws IOException      
	 * @return: void      
	 * @throws
	 */
	public static void Update(String sql) throws IOException {
		try {
		String[] strings=sql.split(" ");
		int tablenameindex = 0;
		int yaoqiuindex=0;
		int beixiugaiindex=0;
		for(int i=0;i<strings.length;i++) {
			if(strings[i].equals("update"))
				tablenameindex=i+1;
			if(strings[i].equals("set"))
				yaoqiuindex=i+1;
			if(strings[i].equals("where"))
				beixiugaiindex=i+1;
		}
		
		String tablename=strings[tablenameindex].trim();
		//将第二行的要求按照等号划分
		String[]temp=strings[yaoqiuindex].split("=");
		//得到修改的indexname
		String yaoqiuname=temp[0].trim();
		//得到要求的内容
		String yaoqiucontent=temp[1].trim();
		if(yaoqiucontent.contains("'")) {
			yaoqiucontent=yaoqiucontent.substring(1,yaoqiucontent.length()-1);
		}
		if(yaoqiucontent.contains(yaoqiuname)) {
			if(yaoqiucontent.contains("+")) {
				
				String []temp2=yaoqiucontent.split("+");
				yaoqiucontent=Integer.toString(Integer.parseInt(yaoqiucontent)+Integer.parseInt(temp2[1]));
				
			}
			else if(yaoqiucontent.contains("-")) {
				String[]temp2=yaoqiucontent.split("-");
				yaoqiucontent=Integer.toString(Integer.parseInt(yaoqiucontent)-Integer.parseInt(temp2[1]));
			}
			else if(yaoqiucontent.contains("*")) {
				String[]temp2=yaoqiucontent.split("*");
				yaoqiucontent=yaoqiucontent=Integer.toString(Integer.parseInt(yaoqiucontent)*Integer.parseInt(temp2[1]));
			}
			else if(yaoqiucontent.contains("/")) {
				String[]temp2=yaoqiucontent.split("/");
				yaoqiucontent=yaoqiucontent=Integer.toString(Integer.parseInt(yaoqiucontent)*Integer.parseInt(temp2[1]));
			}
			else {
				System.out.println("输入的语法有问题，本dbms目前只支持加减乘除四种运算符号");
				Input.get();
			}
			
		}
		Table table=new Table();
		table=Utils.gettable(tablename);
		//如果存在where语句
		if(sql.contains("where")) {
			//得到要改动的indexname
			Insertclass tempinsertclass=new Insertclass();
			LinkedList<Insertclass> beixiugai=new LinkedList<Insertclass>();
			beixiugai=Utils.whereanalyse(tablename,sql);
			//正式开始改动
			for(int i=0;i<table.gettableindex().size();i++) {
				//如果index(i)相等
				if(yaoqiuname.equals(table.gettableindex().get(i))) {
					if(table.gettablebiaoshi().get(i).equals("primarykey")) {
						System.out.println("该操作可能破坏参照完整性，不允许执行，请重新输入！");
						Input.get();
					}
					for(int k=0;k<beixiugai.size();k++) {
					for(int j=0;j<table.getcontentlists().get(i).length();j++) {
						//如果content相同,则进行修改
						for(int n=0;n<beixiugai.get(k).getcontain().size();n++) {
							if(beixiugai.get(k).getcontain().get(n).equals(table.getcontentlists().get(i).getone(j)))
								table.getcontentlists().get(i).change(beixiugai.get(k).getcontain().get(n), yaoqiucontent);
						}
						
					}
					}
				}
			}
		}
		//如果不存在where语句
		else {
			for(int i=0;i<table.gettableindex().size();i++) {
				if(table.gettableindex().get(i).equals(yaoqiuname)) {
					LinkedList<String>templist=new LinkedList<String>();
					for(int j=0;j<table.getcontentlists().get(i).length();j++) {
						templist.add(yaoqiucontent);
					}
					table.getcontentlists().get(i).replace(templist);
				}
			}
		}
	for(int i=0;i<table.gettableindex().size();i++) {
		int flag=1;
		if(table.gettablebiaoshi().get(i).equals("primarykey")||table.gettablebiaoshi().get(i).equals("unique")) {
			for(int j=0;j<table.getcontentlists().get(i).length();j++) {
				for(int k=j+1;k<table.getcontentlists().get(i).length();k++) {
					if(table.getcontentlists().get(i).getone(j).equals(table.getcontentlists().get(i).getone(k)))
					{
						flag=0;
						break;
					}
			}
			}
			
		}
		if(table.gettablebiaoshi().get(i).equals("notnull")||table.gettablebiaoshi().get(i).equals("primarykey")) {
			for(int j=0;j<table.getcontentlists().get(i).length();j++) {
					if(table.getcontentlists().get(i).getone(j).equals(SQLConstant.getNull()))
					{
						flag=0;
						break;
					}
			}
			}
		if(flag==0) {
			System.out.println("该操作破坏了实体完整性或者用户自定义完整性！请重新输入!");
			Input.get();
		}
	}
	table.writefile(tablename);
	System.out.println("更新操作执行成功！");
	Input.get();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("此dbms不支持此词语，请重新输入！");
			Input.get();
		}
	}
}
