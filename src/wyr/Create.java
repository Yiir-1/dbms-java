package wyr;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






public class Create {

	public static final String path = SQLConstant.getPath();
	public static LinkedList<String> tablename=new LinkedList<String>();
	public static LinkedList<String> viewcontain=new LinkedList<String>();
	public static LinkedList<String> viewname=new LinkedList<String>();
	public static LinkedList<String> indexname=new LinkedList<String>();
	public static LinkedList<Indexclass> indexlist=new LinkedList<Indexclass>();
	public static String tName = "";

	public static void createSql(String sql) throws IOException {
		System.out.println("create语句识别成功！");
		//对语句进行词法语法判断，判断是否合法
		//判断是那个语句
		boolean view = sql.contains(" view ");
		boolean table = sql.contains(" table ");
		boolean index=sql.contains("index");
		if (view) {
			if (Utils.checkview(sql)) {
				System.out.println("语句词法语法检查通过！");
				creatview(sql);
			} else {
				System.out.println("语句的词法语法有误，请重新输入！");
				Input.get();
			}

		}
		if (table) {
			if (Utils.checktable(sql)) {
				System.out.println("语句词法语法检查通过！");
				// System.out.println("sql:"+sql);
				createTable(sql);

			} else {
				System.out.println("语句的词法语法有误，请重新输入！");
				Input.get();
			}
		}
		if(index) {
			if (Utils.checkindex(sql)) {
				System.out.println("语句词法语法检查通过！");
				// System.out.println("sql:"+sql);
				createIndex(sql);

			} else {
				System.out.println("语句的词法语法有误，请重新输入！");
				Input.get();
			}
		}

	}

	/**
	 * 
	 * @Title: createTable   
	 * @Description: TODO(描述这个方法的作用)   
	 * @param sql
	 * @throws IOException      
	 * @return: void      
	 * @throws
	 */
	private static void createTable(String sql) throws IOException {
		try{
		//地址
		String tablePath = SQLConstant.getNowPath();
		int index = sql.indexOf("(");
		tName = sql.substring(13, index - 1);
		tName.trim();
		tablename.add(tName);
		String sep = SQLConstant.getSeparate();
		String str = sql.replaceAll(sep + "", sep + sep);
		//得到括号中间的部分
		Pattern pattern = Pattern.compile("\\(.*\\)");
		Matcher matcher = pattern.matcher(sql);
		String s = "";
		if (matcher.find()) {
			s = matcher.group(0);
		}
		//去掉两侧的括号
		String s1 = s.substring(1, s.length() - 1);
		String[] strings = s1.split(",");
		Table table=new Table();
		String foreigntablename = null ;
		String foreignkey;

		for (int j = 0; j < strings.length; j++) {
			String s3 = strings[j].trim();
			//一行按照空格分开
			String[] strings1 = s3.split(" ");
			
			if (strings1[0].equals("foreign")) {
				//找出foreign的key，即属性列的index=foreignkeyname
				int index3 = strings1[1].indexOf("(");
				String foreignkeyname = strings1[1].substring(index3 + 1, strings1[1].length() - 1);
				
				int iffind = 1;
				for (int i = 0; i < table.gettableindex().size(); i++) {
					if (table.gettableindex().get(i).equals(foreignkeyname)) {
						iffind = 0;
						table.gettabletype().set(i, "foreignkey");
					}
				}
				if (iffind == 1) {
					System.out.println("语句的语义有误，请重新输入！");
				}
				//找出他依赖的外表
				int foreigntableindex=strings1[3].indexOf("(");
				foreigntablename =strings1[3].substring(0, foreigntableindex);

				
				Table foreigntable=new Table();
				foreigntable=Utils.gettable(foreigntablename);
				int flag=0;
				for(int k=0;k<foreigntable.gettableindex().size();k++) {
					
					if(foreignkeyname.equals(foreigntable.gettableindex().get(k))) {
						foreigntable.gettableforeign().set(k, "beiforeign");
						table.gettableforeign().set(k,foreigntablename);
						flag=1;
						break;
					}

				}
				if(flag==0) {
					System.out.println("该语句不符合参照完整性，请重新输入！");
					Input.get();
				}
				foreigntable.writefile(foreigntablename);
			} else {
				table.gettableindex().add(strings1[0]);
				//index的属性，按照（分开
				String[] strings2 = strings1[1].split("\\(");
				if (strings2.length > 1) {
					table.gettabletype().add(strings2[0]);
					int index2 = strings2[1].indexOf(")");
					String maxnum = strings2[1].substring(0, index2);
					table.gettablelength().add(maxnum);
					Contentlist conlist = new Contentlist();
					table.getcontentlists().add(conlist);
				} else {
					table.gettabletype().add(strings2[0]);
					table.gettablelength().add(SQLConstant.getmaxnum());
					Contentlist conlist = new Contentlist();
					table.getcontentlists().add(conlist);
				}

				if (strings1.length == 2) {
					table.gettablebiaoshi().add(SQLConstant.getNull());
					table.addforeign(SQLConstant.getNull());
				} else if (strings1.length >= 3) {
					table.addforeign(SQLConstant.getNull());
					for (int i = 2; i < strings1.length-1; i++) {
						if (strings1[i].equals("not") && strings1[i + 1].equals("null")) {
							table.gettablebiaoshi().add("notnull");
							i++;
						} else if (strings1[i].equals("primary") && strings1[i + 1].equals("key")) {
							table.gettablebiaoshi().add("primarykey");
						} else if (strings1[i].equals("unique")) {
							table.gettablebiaoshi().add("unique");
						}

						else {
							table.gettablebiaoshi().add(strings1[i]);
						}
					}
				}
				

			}
			

		}
		
		table.writefile(tName);
		System.out.println("表格创建成功！");
		Input.get();
		}
		catch(Exception c){
			System.out.println("您输入的语句本dbms识别不了，请检查后重新输入");
			Input.get();
		}
	}


	/**
	 * 
	 * @Title: creatview   
	 * @Description: TODO(描述这个方法的作用)   
	 * @param sql
	 * @throws IOException      
	 * @return: void      
	 * @throws
	 */
	private static void creatview(String sql) throws IOException {
		try {
		// 得到文件路径
		String tablePath = SQLConstant.getNowPath();
		// 找到as位置
		int index = sql.indexOf("as");

		//将视图名字和内容加入静态变量，以便后期使用
		String viewnametemp=sql.substring(12, index - 1);
		Create.viewcontain.add(sql);
		Create.viewname.add(sql.substring(12, index - 1));
		
		String[] strings = sql.split(" ");
		int fromnumber = 0;
		int selectline = 0;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals("from")) {
				fromnumber = i;

			}
			if (strings[i].equals("select")) {
				selectline = i;
			}
		}

		LinkedList<String> list1 = new LinkedList<String>();
		LinkedList<String> list2 = new LinkedList<String>();
		LinkedList<String> list3 = new LinkedList<String>();
		LinkedList<String> list4 = new LinkedList<String>();
		LinkedList<String> list6 = new LinkedList<String>();
		LinkedList<Contentlist> list5 = new LinkedList<Contentlist>();
		strings[fromnumber + 1]=strings[fromnumber + 1].replace(";", "");
		Table table=new Table();
		table=Utils.gettable(strings[fromnumber + 1]);
		String[] linestrings = strings[selectline + 1].split(",");
		// 判断都要select表中的那几列，然后放入linestrings里面
		for (int i = 0; i < linestrings.length; i++) {
			for (int j = 0; j < table.tableindex.size(); j++) {
				if (table.tableindex.get(j).equals(linestrings[i])) {
		
					list1.add(table.tableindex.get(i));
					list2.add(table.tabletype.get(i));
					list3.add(table.tablelength.get(i));
					list4.add(table.tablebiaoshi.get(i));
					list5.add(table.tablecontent.get(i));
					list6.add(table.tableforeign.get(i));
				}
			}

		}

		// 如果其存在where语句的话，即对where语句进行处理
		if (sql.contains("where")) {
			
			int whereindex = 0;
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].equals("where"))
					whereindex = i;
			}
			LinkedList<Insertclass> tempwhereans=new LinkedList<Insertclass>();
			tempwhereans=Utils.whereanalyse(strings[fromnumber + 1], sql);
			//原始
			/*
			String[] wherestrings = strings[whereindex + 1].split("and");
			String[] temp2;

			LinkedList<Integer> nolist = new LinkedList<Integer>();
			for (int i = 0; i < wherestrings.length; i++) {

				temp2 = wherestrings[i].split("=");

				temp2[1] = temp2[1].substring(1, temp2[1].length() - 1);
				int flag = 0;
				int flag1 = 0;
				
				for (int j = 0; j < list1.size(); j++) {
					if (list1.equals(temp2[0])) {
						flag = 1;
						Contentlist temp3 = list5.get(j);
	
						for (int m = 0; m < temp3.length(); m++) {
							if (!(temp3.getone(m).equals(temp3))) {
								for (int n = 0; n < nolist.size(); n++) {
									if (nolist.get(n) == m) {
										flag1 = 1;
									}
								}
								if (flag1 == 0)
									nolist.add(m);
							}
						}
					}
				}

				if (flag == 0) {
					System.out.println("语句的语义分析有误，请重新输入！");
					Input.get();
				}
			}

			nolist.sort(null);
			for(int i=0;i<list5.size();i++) {
				for(int j=0;j<nolist.size();j++) {
					list5.get(i).del(nolist.get(j));
				}
			}
			*/
			LinkedList<Contentlist> list7 = new LinkedList<Contentlist>();
			for(int i=0;i<tempwhereans.size();i++) {
				for(int j=0;j<list1.size();j++) {
					//如果两个相同的话
					if(tempwhereans.get(i).index.get(0).equals(list1.get(j))) {
						for(int q=0;q<list5.get(j).length();q++) {
							for(int p=0;p<tempwhereans.get(i).contain.size();p++) {
								//如果两者内容相同的话，就需要取出这一列
								if(tempwhereans.get(i).contain.get(p).equals(list5.get(j).getone(p))) {
									Contentlist tempcon=new Contentlist();
									tempcon.add(list5.get(j).getone(p));
									list7.add(tempcon);
								}
							}
						}
					}
				}
			}
			list5=list7;
		}
		if (list1.size() < 1) {
			System.out.println("语句的语义分析有误，请重新输入！");
			Input.get();
		} else {
			File file = new File(tablePath, tName);
			if (!file.exists()) {

				
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//写文件
				
				writeFile(sql);

				for (int i = 0; i < list5.size(); i++) {
					for (int j = 0; j < list5.get(i).length(); j++) {
						writeFile3(list5.get(i).gets());
					}
					writeFile1(";");
				}
				System.out.println("view创建成功！");
			} else {
				System.out.println("文件已存在，请重试");
			}
			Input.get();
		}
		}
		catch(Exception c) {
			System.out.println("本dbms不支持以上输入，请重新输入");
			Input.get();
		}
		
	}
	/**
	 * 
	 * @Title: createIndex   
	 * @Description: 创建index
	 * @param sql      
	 * @return: void      
	 * @throws IOException 
	 * @throws
	 */
	public static void createIndex(String sql) throws IOException {
		try {
		String[]strings=sql.split("\\s");
    	int tempflag1=0;
    	for(int i=0;i<strings.length;i++) {
    		if(strings[i].equals("on")) {
    			tempflag1=i;
    			break;
    		}
    		
    	}
		tName = strings[tempflag1-1] + ".txt";
		tName.trim();
		String indexnametemp=strings[tempflag1-1] ;
		indexname.add(indexnametemp);
		String tablename=strings[strings.length-1];
		String indextable=strings[tempflag1+1];
		String[]indextables=indextable.split("\\(");
		//读文件，将文件存成一个table格式
		String fileName = indextables[0];
		Table table=new Table();
		table=Utils.gettable(fileName);
		//将属性分割出来，
		String index=indextables[1].substring(0, indextables[1].length()-2);
		//判断是否存在
		int tempflag2=-1;
		for(int i=0;i<table.gettableindex().size();i++) {
			if(index.equals(table.gettableindex().get(i))) {
				tempflag2=i;
			}
		}
		if(tempflag2==-1) {
			System.out.println("index 有语义错误，请重试！");
		}
		//判断存在后，需要开始创建索引
		//将需要索引的一列找出来
		 // 创建索引
		 for(int i=0;i<table.gettableindex().size();i++) {
			 if(table.gettableindex().get(i).equals(index)) {
				 for(int j=0;j<table.getcontentlists().get(i).length();j++) {
					 Indexclass indexclass=new Indexclass();
					 indexclass.contain=Integer.toString(j);
					 indexclass.index=Integer.toString(indexclass.hashCode());
					 Create.indexlist.add(indexclass);
				 }
			 }
		 }
		 indexlist.sort(new Indexsort());
		 Utils.writeindex(indexnametemp,indexlist);
		 System.out.println("index 创建成功！");
		 Input.get(); 
	}
	catch(Exception e) {
		System.out.println("本dbms不支持此语句，请重新输入！");
		Input.get();
	}
	}
	/**
	 * 
	 * @Title: writeFile   
	 * @Description: table书写文件 
	 * @param list      
	 * @return: void      
	 * @throws
	 */
	private static void writeFile(String list) {
		//写入文件
		String str = "";

		// 获取分隔符，本dbms使用的是；
		String sep = SQLConstant.getSeparate();
		// 获取路径
		String path = SQLConstant.getNowPath();
		String nowPath = path + "\\" + tName;
		// 
		
			str += list + ";";
	

		try {
			FileOutputStream fos = new FileOutputStream(new File(nowPath), true);
			str += "\r\n";
			fos.write(str.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeFile1(String list) {
		String sep = SQLConstant.getSeparate();
		String path = SQLConstant.getNowPath();
		String nowPath = path + "\\" + tName;

		try {
			FileOutputStream fos = new FileOutputStream(new File(nowPath), true);
			fos.write(list.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void writeFile3(List<String> list) {
		String str = "";

		String sep = SQLConstant.getSeparate();
		String path = SQLConstant.getNowPath();
		String nowPath = path + "\\" + tName;
		for (int i=0;i<list.size();i++ ) {

			str += list.get(i) + sep;
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(nowPath), true);
			fos.write(str.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
