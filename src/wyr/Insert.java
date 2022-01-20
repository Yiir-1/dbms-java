package wyr;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Scanner;
/**
 * 
 * @ClassName: Insert
 * @Description:一个insert的类
 * @author: 王怡如
 * @Copyright:
 */
public class Insert {
	private static String tableName;

	public static void insertSql(String sql) throws IOException{
		try {
        int index = sql.indexOf("values");
        String name = sql.substring(12, index-1);
        Insertclass insertclass=new Insertclass();
        
        tableName = name.trim();
        Table table =new Table();

        table=Utils.gettable(tableName);
        if (Utils.checkinsert(sql)) {
			System.out.println("语句词法语法检查通过！");
            //获取insert命令中index中的内容
			 int indexstart=0;
		        indexstart=sql.indexOf("(")+1;
		        int indexend=sql.indexOf(")");
		        int indexvalstart=sql.indexOf("values")+7;
				int indexvalend=sql.length()-2;
				int indexval=sql.indexOf("values");
		        //如果有括号
				if(indexstart<indexval&&indexstart!=-1) {
		        	//对括号内部进行分割，然后得到属性值
					String[] indexline=sql.substring(indexstart, indexend).split(",");
					for(int i=0;i<indexline.length;i++) {
						insertclass.insertindex(indexline[i]);
						
					}
		        } 
				else {
					for(int i=0;i<table.gettableindex().size();i++) {
						insertclass.insertindex(table.gettableindex().get(i));
					}
				}
				//得到内容
				String[] values=sql.substring(indexvalstart,indexvalend).split(",");
				for(int i=0;i<values.length;i++) {
					if(values[i].contains("'")) {
						values[i]=values[i].substring(1,values[i].length()-1);
					}
				}
				for(int i=0;i<values.length;i++) {
					insertclass.insertcontain(values[i]);
					
				}
				//开始在table中修改
				LinkedList<Integer>flag=new LinkedList<Integer>();
				for(int i=0;i<insertclass.getsize();i++) {
					flag.add(0);
				}
				for(int i=0;i<insertclass.getsize();i++) {
					for(int j=0;j<table.gettableindex().size();j++) {
						//如果两处的index相同的话,进行添加
						if(insertclass.getoneindex(i).equals(table.gettableindex().get(j))) {
							table.getcontentlists().get(j).add(insertclass.getcontain().get(i));
							//利用flag进行标记
							flag.set(i, 1);
					}
					}
				}
				//如果flag的某一个不是1，则说明其没有出现，则用null填充
				for(int i=0;i<flag.size();i++) {
					if(flag.get(i)!=1) {
						table.getcontentlists().get(i).add(SQLConstant.getNull());
					}
				}
				//完整性约束条件检查！
				for(int i=0;i<table.tabletype.size();i++) {
					if(table.tabletype.equals("notnull")) {
						for(int j=0;j<table.getcontentlists().get(i).length();j++) {
							if(table.getcontentlists().get(i).getone(j).equals(SQLConstant.getNull())) {
								System.out.println("用户定义完整性有问题，请重新输入!");
								Input.get();
							}
						}
					}
					if(table.tabletype.equals("primarykey")) {
						for(int j=0;j<table.getcontentlists().get(i).length();j++) {
							if(table.getcontentlists().get(i).getone(j).equals(SQLConstant.getNull())) {
								System.out.println("实体完整性有问题，请重新输入!");
								Input.get();
							}
						}
					}
					if(table.tabletype.equals("unique")) {
						for(int j=0;j<table.getcontentlists().get(i).length()-1;j++) {
							for(int k=j+1;k<table.getcontentlists().get(i).length();k++) {
								if(table.getcontentlists().get(i).getone(j)==table.getcontentlists().get(i).getone(k)) {
									System.out.println("用户定义完整性有问题，请重新输入!");
									Input.get();
								}
							}
						}
					}
					//判断参照完整性
					if(table.tabletype.equals("foreignkey")) {
						for(int j=0;j<table.getcontentlists().get(i).length();j++) {
							if(!(table.getcontentlists().get(i).getone(j).equals(SQLConstant.getNull()))){
								if(!(table.ifcanzhao())) {
									System.out.println("违背参照完整性，请重新输入！");
									Input.get();
								}
									
							}
						}
					}
				}
				table.writefile(tableName);

    }
        //语法语义检查没通过，则重新输入
        else {
			System.out.println("语法词法检查没有通过，请重新输入！");
			Input.get();
		}
        System.out.println("添加成功！");
        Input.get();
	}
	
		catch(Exception e){
			System.out.println("本dbms不支持此语句，请重新输入！");
			Input.get();
		}
}

}



