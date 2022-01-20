package wyr;

import java.io.IOException;
import java.util.LinkedList;
/**
 * 
 * @ClassName:  Help   
 * @Description:用于实现help功能 
 * @author: 王怡如  
 * @Copyright:
 */
public class Help {


    public static void help(String sql) throws IOException{
       System.out.println("本dbms只支持以下四种语句：help database; help table; help view; help index");
       if(sql.equals("help database;")) {
    	   System.out.print("table:");
    	   for(int i=0;i<Create.tablename.size();i++) {
    		   System.out.print(Create.tablename.get(i)+" ");
    	   }
    	   System.out.println();
    	   System.out.print("view:");
    	   for(int i=0;i<Create.viewname.size();i++) {
    		   System.out.print(Create.viewname.get(i)+" ");
    	   }
    	   System.out.println();
    	   System.out.print("index:");
    	   for(int i=0;i<Create.indexname.size();i++) {
    		   System.out.print(Create.indexname.get(i)+" ");
    	   }
    	   System.out.println();
    	   System.out.println("help语句执行成功！请重新输入！");
    	   Input.get();
       }
       else if(sql.contains("help table;")) {
    	   String[] strings=sql.split(" ");
    	   Table table=new Table();
    	   table=Utils.gettable(strings[1].substring(0, strings[1].length()-1));
    	   for(int i=0;i<table.gettableindex().size();i++) {
    		   System.out.print(table.gettableindex().get(i)+" ");
    	   }
    	   System.out.println();
    	   
    	   for(int i=0;i<table.gettabletype().size();i++) {
    		   System.out.print(table.gettabletype().get(i)+" ");
    	   }
    	   System.out.println();
    	 
    	   for(int i=0;i<table.gettablelength().size();i++) {
    		   System.out.print(table.gettablelength().get(i)+" ");
    	   }
    	   System.out.println();
    	   
    	   for(int i=0;i<table.gettablebiaoshi().size();i++) {
    		   System.out.print(table.gettablebiaoshi().get(i)+" ");
    	   }
    	   System.out.println();
    	   for(int i=0;i<table.gettableforeign().size();i++) {
    		   System.out.print(table.gettableforeign().get(i)+" ");
    	   }
    	   System.out.println();
    	   System.out.println("help语句执行成功！请重新输入！");
    	   Input.get();
       }
       else if(sql.contains("help view;")) {
    	   String[] strings=sql.split(" ");
    	   Table table=new Table();
    	   String viewnametemp=strings[1].substring(0,strings[1].length()-1);
    	   for(int i=0;i<Create.viewcontain.size();i++) {
    		   if(Create.viewcontain.contains(viewnametemp)) {
    			   System.out.println(Create.viewcontain.get(i));
    		   }
    	   }
    	   System.out.println("help语句执行成功！请重新输入！");
    	   Input.get();
       }
       else if(sql.contains("help index;")) {
    	   String[] strings=sql.split(" ");
    	   String indexnametemp=strings[1].substring(0,strings[1].length()-1);
    	   System.out.println("index为：");
    	   LinkedList<Indexclass> tempindex=new LinkedList<Indexclass>();
    	   tempindex=Utils.readindex(indexnametemp);
    	   for(int i=0;i<tempindex.size();i++) {
    		   System.out.println(tempindex.get(i));
    	   }
    	   System.out.println("help语句执行成功！请重新输入！");
    	   Input.get();
       }
       else {
		System.out.println("本dbms不支持此语句，请重新输入！");
		Input.get();
	}
    }
}
