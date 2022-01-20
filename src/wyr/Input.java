package wyr;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 
 * @ClassName:  Input   
 * @Description:进行输入，并对语句进行预处理   
 * @author: 王怡如  
 * @Copyright:
 */
public class Input {
	public  static Grant grant =new Grant();
	public static String nowuser;
	public static LinkedList<String >nowtable=new LinkedList<String>();
    public static void get() throws IOException{

    	System.out.println("请输入一段数据库语言：(以分号结尾）");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        //开始循环，判断语句，以分号结尾
        do{
            input += " " + scanner.nextLine();
            //解决大小写不匹配的问题
            input=input.toLowerCase();
            //System.out.println(input);
        }while(!input.endsWith(";"));

        String sql;
        sql = input.trim();
        //通过正则表达式将多个空格转换为一个空格
        sql= sql.replaceAll("\\s+", " ");
        System.out.println(sql);
        System.out.println("----------");
        //进入分析语句阶段
        SqlAnalysis.analysis(sql);
    }

    public static void user()
    {
    	System.out.println("自动创建管理员，拥有所有权限");
    	LinkedList<String>temp=new LinkedList<String>();
    	temp.add("all");
    	grant.dbagrantuser("DBA",temp,true);
    	
    	nowuser="DBA";
    }
}
