package wyr;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @ClassName: Delete
 * @Description:进行数据库删除
 * @author: 王怡如
 * @Copyright:
 */
public class Delete {
	public static void deleteSql(String sql) throws IOException {
		try {
		Table table=new Table();
		String tablename;
		//将sql进行分割
		int nameindex = 0;
		String[] sqlsplit=sql.split(" ");
		//找到表名
		for(int i=0;i<sqlsplit.length;i++) {
			if(sqlsplit[i].equals("from")) {
				nameindex=i;
			}
		}
		//读表
		tablename=sqlsplit[nameindex+1];
		table=Utils.gettable(tablename);
		//进行语法和词法分析
		if(Utils.Checkdelete(sql)) {
			
		String yaoqiu;
		System.out.println("语法词法检查通过！");
		int num=0;
		//有where 相当于有要求的时候
		
		if(sql.contains("where")) {
			int whereindex = 0;
			for(int i=0;i<sqlsplit.length;i++) {
				if(sqlsplit[i].equals("where")) {
					whereindex=i+1;
				}
			}
		yaoqiu=sqlsplit[whereindex];
		//分析where语句
		LinkedList<Insertclass> yaoqius=new LinkedList<Insertclass>();
		yaoqius=Utils.whereanalyse(tablename,sql);
		for(int i=0;i<yaoqius.size();i++) {
			for(int j=0;j<yaoqius.get(i).getcontain().size();j++) {
				num++;
			}
		}
		LinkedList<String>temp=new LinkedList<String>();
		//开始删除
		for(int i=0;i<table.gettableindex().size();i++) {
			for(int n=0;n<yaoqius.size();n++)
				
			{
			for(int j=0;j<yaoqius.get(n).getindex().size();j++) {
				//index相同
				if(table.gettableindex().get(i).equals(yaoqius.get(n).getindex().get(j))){
					for(int k=0;k<table.getcontentlists().get(i).length();k++) {
						//如果内容相同
						for(int t=0;t<yaoqius.get(n).getcontain().size();t++) {
							if(yaoqius.get(n).getcontain().get(t).equals(table.getcontentlists().get(i).getone(k))) {
								//判断删除之后是否会违反参照完整性，在进行删除
								//如果发现该属性是被参照属性，则拒绝删除
								if(table.gettableforeign().get(i).equals("beiforeign")) {
									System.out.println("该属性删除可能破坏参照完整性，拒绝删除！");
									Input.get();
							}
							
						}
						//如果内容不相同
						else {
							temp.add(table.getcontentlists().get(i).getone(k));
						}
						}
					}
					table.getcontentlists().get(i).replace(temp);
				}
			}
		}
		}
		}
		//全部删除
		else {
			int flag=1;
			for(int i=0;i<table.gettableforeign().size();i++) {
				if(table.gettableforeign().get(i).equals("beiforeign")) {
					flag=0;
					break;
				}
			}
			if(flag==0) {
				System.out.println("该属性删除可能破坏参照完整性，拒绝删除！");
				Input.get();
			}
			for(int i=0;i<table.getcontentlists().size();i++) {
				for(int j=0;j<table.getcontentlists().get(i).length();j++) {
					num+=table.getcontentlists().get(i).length();
					LinkedList<String>a=new LinkedList<String>();
					table.getcontentlists().get(i).replace(a);
				}
			}
		}
		table.writefile(tablename);
		System.out.print("删除成功!本次删除了");

		System.out.print( num);
		System.out.println("个元素");
		Input.get();
		}
		else {
			System.out.println("此语句语法词法分析有问题，请重新输入！");
			Input.get();
		}

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("本dbms不支持此语句，请重新输入！");
			Input.get();
		}
}
}
