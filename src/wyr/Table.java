package wyr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
/**
 * 
 * @ClassName:  Table   
 * @Description:table表格存储
 * @author: 王怡如  
 * @Copyright:
 */


public class Table {
public LinkedList<String>tableindex=new LinkedList<String>();
public  LinkedList<String>tabletype=new LinkedList<String>();//类型
public LinkedList<String>tablelength=new LinkedList<String>();//长度
public LinkedList<String>tablebiaoshi=new LinkedList<String>();//标识（是否为主码）
public LinkedList<String >tableforeign=new LinkedList<String>();//被参照表是什么
public LinkedList<Contentlist>tablecontent=new LinkedList<Contentlist>();//内容
public  boolean isindex=false;
public LinkedList<String> gettableindex(){
	return tableindex;
}

public void changeindex() {
	isindex=true;	
}

public LinkedList<String> gettabletype(){
	return tabletype;
}

public LinkedList<Contentlist> getcontentlists(){
	return this.tablecontent; 
}

public LinkedList<String> gettablelength(){
	return this.tablelength; 
}

public LinkedList<String> gettablebiaoshi(){
	return this.tablebiaoshi; 
}

public LinkedList<String> gettableforeign(){
	return this.tableforeign; 
}

public void addindex(String a) {
	this.tableindex.add(a);
}

public void addtype(String a) {
	this.tabletype.add(a);
}

public void addlength(String a) {
	this.tablelength.add(a);
}

public void addbiaoshi(String a) {
	this.tablebiaoshi.add(a);
}

public void addforeign(String a) {
	this.tableforeign.add(a);
}

public void addcontent(Contentlist a) {
	this.tablecontent.add(a);
}

public void creatcontentlist(int a) {
	for(int i=0;i<a;i++) {
		Contentlist temp=new Contentlist();
		this.tablecontent.add(temp);
	}
}

public void writefile(String name) {
	String str = "";
//得到书写的路径，和分隔符
	String sep = SQLConstant.getSeparate();

	String path = SQLConstant.getNowPath();
	//
	String nowPath = path + "\\" + name+".txt";
	
	for (String s1 : tableindex) {
		str += s1 + sep;
	}
	str += "\r\n";
	for (String s1 : tabletype) {
		str += s1 + sep;
	}
	str += "\r\n";
	for (String s1 : tablelength) {
		str += s1 + sep;
	}
	str += "\r\n";
	for (String s1 : tablebiaoshi) {
		str += s1 + sep;
	}
	str += "\r\n";
	for(String s1:tableforeign) {
		str+=s1+sep;
	}
	str += "\r\n";
	for (Contentlist s1 : tablecontent) {
		for(int i=0;i<s1.length();i++)
		{
			str+=s1.getone(i)+sep;
		}
		str += ";";
	}
	str += "\r\n";
	File file =new File(nowPath);
	try {
		 if(!file.exists()) {
             file.createNewFile();
         }
         FileWriter fileWriter =new FileWriter(file);
         fileWriter.write("");
         fileWriter.flush();
         fileWriter.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	try {
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWriter =new FileWriter(file, true);

        fileWriter.write(str);
        fileWriter.flush();
        fileWriter.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public boolean ifcanzhao() {
	// TODO Auto-generated method stub
	int flag=1;
	
	if(flag==1)
		return true;
	else {
		return false;
	}	
}
}