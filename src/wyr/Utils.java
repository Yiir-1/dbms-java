package wyr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName: Utils
 * @Description:TODO很多工具和检查check(描述这个类的作用)
 * @author: 王怡如
 * @Copyright:
 */
public class Utils {

	public static String gdnum = null;

	/**
	 * 
	 * @Title: checkview
	 * @Description: 对view进行语法和词法分析
	 * @param sql
	 * @return
	 * @return: boolean
	 * @throws
	 */
	public static boolean checkview(String sql) {
		// 首先检查有没有关键字
		int flag = 1;
		int findas = sql.indexOf("as");
		int findselect = sql.indexOf("select");
		int findfrom = sql.indexOf("from");
		if (findas == -1 || findselect == -1 || findfrom == -1) {
			flag = 0;
		}
		// 进行安全性检查
				flag = 0;
				for (int i = 0; i < Grant.grantpeople.size(); i++) {
					if (Input.nowuser.equals(Input.grant.grantpeople.get(i).grantname)) {
						for (int j = 0; j <= Input.grant.grantpeople.get(i).privileges.size(); j++) {
							if (!Input.grant.grantpeople.get(i).privileges.get(j).equals("select")) {
								flag = 1;
								break;
							}
						}
					}
				}
		if (flag == 0)
			return false;
		else
			return true;

	}

	/**
	 * 
	 * @Title: checktable
	 * @Description: 对table进行语法词法分析
	 * @param sql
	 * @return
	 * @return: boolean
	 * @throws
	 */
	public static boolean checktable(String sql) {
		int flag = 1;
		// 检查括号是否匹配
		Stack<String> stack = new Stack<>();
		int index = sql.indexOf("(");
		if (index == -1) {
			flag = 0;
		}
		
		char[] chars = sql.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				stack.push("(");
			} else if (chars[i] == ')') {
				if (stack.empty()) {
					flag = 0;
				} else {
					stack.pop();
				}
			}
		}
		// 取出括号中间的部分
		int tempindex = sql.indexOf('(');
		//没有表名
		String outstrigs = sql.substring(13, tempindex);
		if (outstrigs == null)
			flag = 0;

		// 进行安全性检查
		flag = 0;
		for (int i = 0; i < Input.grant.grantpeople.size(); i++) {
			if (Input.nowuser.equals(Input.grant.grantpeople.get(i).grantname)) {
				for (int j = 0; j <= Input.grant.grantpeople.get(i).privileges.size(); j++) {
					if (!Input.grant.grantpeople.get(i).privileges.get(j).equals("select")) {
						flag = 1;
						break;
					}
				}
			}
		}
		if (flag == 1)
			return true;
		else
			return false;

	}
/**
 * 
 * @Title: checkindex   
 * @Description:对index进行语法词法检查  
 * @param sql
 * @return
 * @throws IOException      
 * @return: boolean      
 * @throws
 */
	public static boolean checkindex(String sql) throws IOException {
		int flag = 1;
		String[] strings = sql.split("\\s");
		int tempflag1 = 0;
		int tempflag2 = 0;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals("on")) {
				tempflag1 = i;
			}
			if (strings[i].equals("index")) {
				tempflag2 = 1;
			}
		}
		String tablename = strings[strings.length - 1];
		String indextable = strings[tempflag1 + 1];
		String[] indextables = indextable.split("\\(");
		String fileName = "D:\\MyDatabase\\" + indextables[0] + ".txt";
		try {
			FileReader filereader = new FileReader(fileName);
		} catch (Exception e) {
			System.out.println("语义分析有误，请重新输入！");
			Input.get();
		}
		if (tempflag1 == 0 || tempflag2 == 0) {
			flag = 0;
		}

		// 进行安全性检查
		flag = 0;
		for (int i = 0; i < Input.grant.grantpeople.size(); i++) {
			if (Input.nowuser.equals(Input.grant.grantpeople.get(i).grantname)) {
				for (int j = 0; j <= Input.grant.grantpeople.get(i).privileges.size(); j++) {
					if (!Input.grant.grantpeople.get(i).privileges.get(j).equals("select")) {
						flag = 1;
						break;
					}
				}
			}
		}
		if (flag == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @Title: gettable
	 * @Description: 得到table
	 * @param tablename
	 * @return
	 * @throws IOException
	 * @return: Table
	 * @throws
	 */
	public static Table gettable(String tablename) throws IOException {
		Table table = new Table();
		String path = SQLConstant.getNowPath();
		String nowPath = path + "\\" + tablename + ".txt";
		try {

			FileReader fileReader = new FileReader(nowPath);

		} catch (Exception e) {
			System.out.println("没有此文件，请重试！");
			Input.get();
		}
		FileReader fileReader = new FileReader(nowPath);
		BufferedReader bufferedreader = new BufferedReader(fileReader);
		String tabletxt = bufferedreader.readLine();
		String[] temp = tabletxt.split(",");
		for (int i = 0; i < temp.length; i++) {
			table.addindex(temp[i]);
		}

		tabletxt = bufferedreader.readLine();
		temp = tabletxt.split(",");
		for (int i = 0; i < temp.length; i++) {
			table.addtype(temp[i]);
		}

		tabletxt = bufferedreader.readLine();
		temp = tabletxt.split(",");
		for (int i = 0; i < temp.length; i++) {
			table.addlength(temp[i]);
		}
		tabletxt = bufferedreader.readLine();
		temp = tabletxt.split(",");
		for (int i = 0; i < temp.length; i++) {
			table.addbiaoshi(temp[i]);
		}

		tabletxt = bufferedreader.readLine();
		temp = tabletxt.split(",");
		for (int i = 0; i < temp.length; i++) {
			table.addforeign(temp[i]);
		}
		tabletxt = bufferedreader.readLine();
		temp = tabletxt.split(";");
		for (int i = 0; i < temp.length; i++) {
			String[] temp5 = temp[i].split(",");
			Contentlist conlist = new Contentlist();
			for (int j = 0; j < temp5.length; j++) {
				conlist.add(temp5[j]);
			}
			table.addcontent(conlist);
		}
		if (table.getcontentlists().size() != table.gettableindex().size()) {

			table.creatcontentlist(table.gettableindex().size() - table.getcontentlists().size());
		}
		return table;
	}

	/**
	 * 
	 * @Title: checkinsert
	 * @Description:检查语句是否符合语法和词法
	 * @param sql
	 * @return
	 * @return: boolean
	 * @throws IOException
	 * @throws
	 */
	public static boolean checkinsert(String sql) throws IOException {
		// TODO Auto-generated method stub
		int tempflag = 1;
		int index = sql.indexOf("values");
		String name = sql.substring(12, index - 1);
		String tableName = name.trim();
		Table table = new Table();

		// 将table拿出来
		table = Utils.gettable(tableName);
		Stack<String> stack = new Stack<>();
		index = sql.indexOf("(");
		if (index == -1) {
			tempflag = 0;
		}
		// 检查括号的匹配
		char[] chars = sql.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				stack.push("(");
			} else if (chars[i] == ')') {
				if (stack.empty()) {
					tempflag = 0;
				} else {
					stack.pop();
				}
			}
		}
		// 检查关键字是否存在
		if (sql.contains("into") && sql.contains("values")) {
			tempflag = 1;
		} else {
			tempflag = 0;
		}
		// 获取insert也就中间括号中的内容
		int indexstart = 0;
		indexstart = sql.indexOf("(") + 1;
		int indexend = sql.indexOf(")");
		int indexvalstart = sql.indexOf("values") + 7;
		int indexvalend = sql.length() - 2;
		// 如果有括号
		int indexval = sql.indexOf("values");
		if (indexstart < indexval && indexstart != -1) {
			// 如果内部有逗号，也就是有两个以上的数据,以下开始进行判断
			if (sql.substring(indexstart, indexend).contains(",")) {
				int flag = 0;
				String[] temp = sql.substring(indexstart, indexend).split(",");
				// 删除头部和尾部多于的空格
				for (int i = 0; i < temp.length; i++) {
					temp[i] = temp[i].trim();
				}
				// 首先判断表格中是否有这几个属性
				for (int i = 0; i < temp.length; i++) {
					for (int j = 0; j < table.gettableindex().size(); j++) {
						if (temp[i].equals(table.gettableindex().get(j))) {
							flag = 1;
							break;
						}

					}
					if (flag == 1) {
						flag = 0;
					} else {
						tempflag = 0;
						break;
					}
				}
				// 其次判断下方的values和上方数据个数是否一致

				String[] values = sql.substring(indexvalstart, indexvalend).split(",");
				if (values.length != temp.length) {
					tempflag = 0;
				}

			}
			// 如果括号内部没有逗号，也就是只有一列
			else {
				String temp = sql.substring(indexstart, indexend);
				int flag = 0;
				for (int i = 0; i < table.gettableindex().size(); i++) {
					if (temp == table.gettableindex().get(i)) {
						flag = 1;
						break;
					}

				}
				if (flag == 0) {
					tempflag = 0;
				}
				if (sql.substring(indexvalstart, indexvalend).contains(",")) {
					tempflag = 0;
				}
			}

		}
		// 如果没有括号，就判断
		String[] values = sql.substring(indexvalstart, indexvalend).split(",");
		if (values.length != table.gettableindex().size()) {
			tempflag = 0;
		}
		// 进行安全性检查
		tempflag = 0;
		for (int i = 0; i < Input.grant.grantpeople.size(); i++) {
			if (Input.nowuser.equals(Input.grant.grantpeople.get(i).grantname)) {
				for (int j = 0; j <= Input.grant.grantpeople.get(i).privileges.size(); j++) {
					if (Input.grant.grantpeople.get(i).privileges.get(j).equals("all")
							|| Input.grant.grantpeople.get(i).privileges.get(j).equals("insert")) {
						tempflag = 1;
						break;
					}
				}
			}
		}
		if (tempflag == 0)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @Title: Checkdelete
	 * @Description: 检查delete的语法和词法
	 * @param sql
	 * @return
	 * @return: boolean
	 * @throws
	 */
	public static boolean Checkdelete(String sql) {
		int flag = 1;
		if (!sql.contains("from")) {
			flag = 0;
		}
		String[] strings = sql.split(" ");

		// 找到where的序号，然后提取出来
		if (sql.contains("where")) {
			int whereindex = 0;
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].equals("where")) {
					whereindex = i + 1;
				}
			}
			String tiaojian = strings[whereindex];
		}
		// 进行安全性检查
		flag = 0;
		for (int i = 0; i < Grant.grantpeople.size(); i++) {
			if (Input.nowuser.equals(Grant.grantpeople.get(i).grantname)) {
				for (int j = 0; j <= Grant.grantpeople.get(i).privileges.size(); j++) {
					if (Grant.grantpeople.get(i).privileges.get(j).equals("all")
							|| Grant.grantpeople.get(i).privileges.get(j).equals("delete")) {
						flag = 1;
						break;
					}
				}
			}
		}
		if (flag == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: istablehaveindex
	 * @Description: 检查一个table中是否存在一个Linkedlist中的各种index
	 * @param a
	 * @return: boolean
	 * @throws
	 */
	public static boolean istablehaveindex(LinkedList<String> a, Table table) {
		int flag = 1;

		for (int i = 0; i < a.size(); i++) {
			int tempflag = 0;
			for (int j = 0; j < table.gettableindex().size(); j++) {
				if (table.gettableindex().get(j).equals(a.get(i))) {
					tempflag = 1;
					break;
				}
			}
			if (tempflag == 0) {
				flag = 0;
				break;
			}
		}
		if (flag == 1)
			return true;
		else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: istablehavecontent
	 * @Description: 查询table是否包含这个内容
	 * @param indexname
	 * @param table
	 * @param a
	 * @return
	 * @return: boolean
	 * @throws
	 */
	public static boolean istablehavecontent(String indexname, Table table, String a) {
		int flag = 1;
		int indexnum = 0;

		for (int i = 0; i < table.gettableindex().size(); i++) {
			if (indexname.equals(table.gettableindex().get(i))) {
				indexnum = i;
			}
		}
		for (int i = 0; i < table.getcontentlists().get(indexnum).length(); i++) {
			int tempflag = 0;
			if (a.equals(table.getcontentlists().get(indexnum).getone(i))) {
				tempflag = 1;
			}
			if (tempflag == 0) {
				flag = 0;
			}
		}
		if (flag == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: CheckUpdate
	 * @Description: 对update语句进行语法和词法检验
	 * @param sql
	 * @return
	 * @return: boolean
	 * @throws
	 */
	public static boolean CheckUpdate(String sql) {
		int flag = 1;
		// 检查括号是否匹配
		Stack<String> stack = new Stack<>();
		char[] chars = sql.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				stack.push("(");
			} else if (chars[i] == ')') {
				if (stack.empty()) {
					flag = 0;
				} else {
					stack.pop();
				}
			}
		}
		// 进行安全性检查
		flag = 0;
		for (int i = 0; i < Input.grant.grantpeople.size(); i++) {
			if (Input.nowuser.equals(Input.grant.grantpeople.get(i).grantname)) {
				for (int j = 0; j <= Input.grant.grantpeople.get(i).privileges.size(); j++) {
					if (Input.grant.grantpeople.get(i).privileges.get(j).equals("all")
							|| Input.grant.grantpeople.get(i).privileges.get(j).equals("update")) {
						flag = 1;
						break;
					}
				}
			}
		}
		// 检验关键字
		if (!sql.contains("set")) {
			flag = 0;
		}

		if (flag == 1)
			return true;
		else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: whereanalyse
	 * @Description: 对where语句进行分析
	 * @param sql
	 * @return: LinkedList<Insertclass>
	 * @throws IOException
	 * @throws
	 */
	public static LinkedList<Insertclass> whereanalyse(String tablename, String sql) throws IOException {
		try {
		LinkedList<Insertclass> ans = new LinkedList<Insertclass>();
		Table table = new Table();
		table = Utils.gettable(tablename); // 得到表格
		LinkedList<Contentlist> yaoqiuindex = new LinkedList<Contentlist>();
		LinkedList<Contentlist> yaoqiucontent = new LinkedList<Contentlist>();
		Contentlist indexcontentlist = new Contentlist();
		Contentlist contentcontentlist = new Contentlist();
		Insertclass returnclass = new Insertclass();
		int startindex = 0;
		String s;
		String s1;
		String[] strings;
		if (sql.contains("group")) {
			String pattern = "where.*group";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(sql);
			s = "";
			if (m.find()) {
				s = m.group(0);
			}
			// 得到where后面的语句
			s1 = s.substring(6, s.length() - 6);
			strings = s1.split(" ");
		} else {
			String pattern = "where.*;";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(sql);
			s = "";
			if (m.find()) {
				s = m.group(0);
			}
			// 得到where后面的语句
			if(s.length()-1<6) {
				for(int i=0;i<table.getcontentlists().get(0).length();i++) {
					Insertclass tempreturnclass = new Insertclass();
					tempreturnclass.index.add(table.gettableindex().get(0));
					tempreturnclass.contain.add(table.getcontentlists().get(0).getone(i));
					ans.add(tempreturnclass);
					}
				return ans;
				
			}
			s1 = s.substring(6, s.length() - 1);
			strings = s1.split(" ");
		}
		int addindex = 0;
		// 如果语句里面存在in，说明是使用的in格式
		if (s.contains("in")) {
			String[] temp = strings[2].split(",");
			if (temp[0].contains("'")) {
				temp[0] = temp[0].substring(2, temp[0].length() - 1);
				temp[temp.length - 1] = temp[temp.length - 1].substring(1, temp[temp.length - 1].length() - 2);
			} else {
				temp[0] = temp[0].substring(1, temp[0].length());
				temp[temp.length - 1] = temp[temp.length - 1].substring(0, temp[temp.length - 1].length() - 1);
			}
			for (int i = 1; i < temp.length - 1; i++) {
				if (temp[i].contains("'")) {
					temp[i] = temp[i].substring(1, temp[i].length() - 1);
				} else {
					temp[i] = temp[i].substring(0, temp[i].length());
				}

			}
			for (int j = 0; j < table.gettableindex().size(); j++) {
				if (table.gettableindex().get(j).equals(strings[0])) {
					for (int i = 0; i < temp.length; i++) {
						for (int k = 0; k < table.getcontentlists().get(j).length(); k++) {
							if ((temp[i].trim()).equals(table.getcontentlists().get(j).getone(k))) {
								Insertclass tempreturnclass = new Insertclass();
								tempreturnclass.index.add(table.gettableindex().get(j));
								tempreturnclass.contain.add(table.getcontentlists().get(j).getone(k));
								ans.add(tempreturnclass);
							}
						}
					}
				}
			}
			yaoqiucontent.add(contentcontentlist);
			yaoqiuindex.add(indexcontentlist);
		} else if (s.contains("between")) {
			for (int i = 0; i < table.gettableindex().size(); i++) {
				if (table.gettableindex().get(i).equals(strings[0].trim())) {
					for (int j = 0; j < table.getcontentlists().get(i).length(); j++) {
						if (Integer.parseInt(table.getcontentlists().get(i).getone(j).trim()) > Integer.parseInt(strings[2].trim())
								&& Integer.parseInt(table.getcontentlists().get(i).getone(j)) < Integer
										.parseInt(strings[4].trim())) {
							Insertclass tempreturnclass = new Insertclass();
							tempreturnclass.index.add(table.gettableindex().get(i));
							tempreturnclass.contain.add(table.getcontentlists().get(i).getone(j));
							ans.add(tempreturnclass);
						}
					}
				}
			}

		} else {
			for (int i = 0; i < strings.length; i++) {
				// 如果存在等于号
				if (strings[i].contains("=")) {
					// 将第二行的要求按照等号划分
					String[] temp = strings[i].split("=");
					// 得到修改的indexname
					String tempyaoqiuname = temp[0].trim();
					// 得到要求的内容
					String tempyaoqiucontent = temp[1].trim();
					if (tempyaoqiucontent.contains("'")) {
						tempyaoqiucontent = tempyaoqiucontent.substring(1, tempyaoqiucontent.length() - 1);
					}
					if (tempyaoqiucontent.contains(tempyaoqiuname)) {
						if (tempyaoqiucontent.contains("+")) {
							String[] temp2 = tempyaoqiucontent.split("+");
							tempyaoqiucontent = Integer.toString(Integer.parseInt(tempyaoqiucontent) + Integer.parseInt(temp2[1]));

						} else if (tempyaoqiucontent.contains("-")) {
							String[] temp2 = tempyaoqiucontent.split("-");
							tempyaoqiucontent = Integer.toString(Integer.parseInt(tempyaoqiucontent) - Integer.parseInt(temp2[1]));
						} else if (tempyaoqiucontent.contains("*")) {
							String[] temp2 = tempyaoqiucontent.split("*");
							tempyaoqiucontent = tempyaoqiucontent = Integer.toString(Integer.parseInt(tempyaoqiucontent) * Integer.parseInt(temp2[1]));
						} else if (yaoqiucontent.contains("/")) {
							String[] temp2 = tempyaoqiucontent.split("/");
							tempyaoqiucontent = tempyaoqiucontent = Integer.toString(Integer.parseInt(tempyaoqiucontent) * Integer.parseInt(temp2[1]));
						} else {
							System.out.println("输入的语法有问题，本dbms目前只支持加减乘除四种运算符号");
							Input.get();
						}

					}
					contentcontentlist.add(tempyaoqiucontent);
					indexcontentlist.add(tempyaoqiuname);
				}
				// 如果不存在等于号
				else {
					if (strings[i].equals("and")) {
						Contentlist tempindexcontentlist = new Contentlist();
						Contentlist tempcontentcontentlist = new Contentlist();
						for (int t = 0; t < contentcontentlist.length(); t++) {
							tempcontentcontentlist.add(contentcontentlist.getone(t));
							tempindexcontentlist.add(indexcontentlist.getone(t));
						}
						yaoqiuindex.add(tempindexcontentlist);
						yaoqiucontent.add(tempcontentcontentlist);
						contentcontentlist.empty();
						indexcontentlist.empty();

					}
					if (strings[i].equals("or")) {
						continue;
					}
				}
			}

			Contentlist tempindexcontentlist = new Contentlist();
			Contentlist tempcontentcontentlist = new Contentlist();
			for (int t = 0; t < contentcontentlist.length(); t++) {
				tempcontentcontentlist.add(contentcontentlist.getone(t));
				tempindexcontentlist.add(indexcontentlist.getone(t));
			}
			yaoqiuindex.add(tempindexcontentlist);
			yaoqiucontent.add(tempcontentcontentlist);
			contentcontentlist.empty();
			indexcontentlist.empty();
			// 开始查询
			// 先查等式的
			int chaxunflag1 = 0;
			int chaxunflag2 = 0;
			int tempflag2 = 1;
			for (int i = 0; i < yaoqiucontent.size(); i++) {
				for (int j = 0; j < table.gettableindex().size(); j++) {
					if (table.gettableindex().get(j).equals(yaoqiuindex.get(i).getone(0))) {
						chaxunflag1 = 1;
						for (int k = 0; k < yaoqiucontent.get(i).length(); k++) {
							for (int n = 0; n < table.getcontentlists().get(j).length(); n++) {
								if (yaoqiucontent.get(i).getone(k).equals(table.getcontentlists().get(j).getone(n))) {
									chaxunflag2 = 1;
									Insertclass tempreturnclass = new Insertclass();
									tempreturnclass.index.add(table.gettableindex().get(j));
									tempreturnclass.contain.add(yaoqiucontent.get(i).getone(k));
									ans.add(tempreturnclass);
								}
							}
						}
					}

				}
				if (chaxunflag1 == 0 || chaxunflag2 == 0) {
					tempflag2 = 0;
					chaxunflag1 = 0;
					chaxunflag2 = 0;
				}
			}
			if (tempflag2 == 0) {
				System.out.println("您输入的语义有问题！请重新输入！");
				Input.get();
			}
		}
		return ans;
		}
		catch(Exception x) {
			System.out.println("本dbms不支持此操作，请重新输入！");
			Input.get();
			return null;
		}
		
	}
/**
 * 
 * @Title: CheckSelect   
 * @Description: TODO(描述这个方法的作用)   
 * @param sql
 * @return      
 * @return: boolean      
 * @throws
 */
	public static boolean CheckSelect(String sql) {
		int flag = 1;
		// 进行关键字检查
		if (!sql.contains("from")) {
			flag = 0;
		}
		// 进行安全性检查
		for (int i = 0; i < Grant.grantpeople.size(); i++) {
			if (Input.nowuser.equals(Grant.grantpeople.get(i).grantname)) {
				
			}
		}
		// 检查括号
		char[] chars = sql.toCharArray();
		Stack<String> stack = new Stack<>();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				stack.push("(");
			} else if (chars[i] == ')') {
				if (stack.empty()) {
					flag = 0;
				} else {
					stack.pop();
				}
			}
		}

		if (flag == 1) {
			return true;

		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: havinganalyse
	 * @Description: 对having语句进行分析
	 * @param sql
	 * @param tempselectans
	 * @return
	 * @throws IOException
	 * @return: LinkedList<Insertclass>
	 * @throws
	 */
	public static LinkedList<Insertclass> havinganalyse(String sql, selectclass tempselectans) throws IOException {
		try {
			LinkedList<Insertclass> ans = new LinkedList<Insertclass>();
			Table table = new Table();
			for (int i = 0; i < tempselectans.index.size(); i++) {
				table.tableindex.add(tempselectans.getindex(i));
				table.tablecontent.add(tempselectans.contain.get(i));
			}
			LinkedList<Contentlist> yaoqiuindex = new LinkedList<Contentlist>();
			LinkedList<Contentlist> yaoqiucontent = new LinkedList<Contentlist>();
			Contentlist indexcontentlist = new Contentlist();
			Contentlist contentcontentlist = new Contentlist();

			Insertclass returnclass = new Insertclass();
			int startindex = 0;
			// 得到where后面的语句
			String s1 = sql.substring(0, sql.length() - 1);
			String[] strings = s1.split(" ");
			int addindex = 0;
			// 如果语句里面存在in，说明是使用的in格式
			if (sql.contains("in")) {
				String[] temp = strings[2].split(",");
				if (temp[0].contains("'")) {
					temp[0] = temp[0].substring(2, temp[0].length() - 1);
					temp[temp.length - 1] = temp[temp.length - 1].substring(1, temp[temp.length - 1].length() - 2);
				} else {
					temp[0] = temp[0].substring(1, temp[0].length());
					temp[temp.length - 1] = temp[temp.length - 1].substring(0, temp[temp.length - 1].length() - 1);
				}
				for (int i = 1; i < temp.length - 1; i++) {
					if (temp[i].contains("'")) {
						temp[i] = temp[i].substring(1, temp[i].length() - 1);
					} else {
						temp[i] = temp[i].substring(0, temp[i].length());
					}

				}
				for (int j = 0; j < table.gettableindex().size(); j++) {
					if (table.gettableindex().get(j).equals(strings[0])) {
						for (int i = 0; i < temp.length; i++) {
							for (int k = 0; k < table.getcontentlists().get(j).length(); k++) {
								if ((temp[i].trim()).equals(table.getcontentlists().get(j).getone(k))) {
									Insertclass tempreturnclass = new Insertclass();
									tempreturnclass.index.add(table.gettableindex().get(j));
									tempreturnclass.contain.add(table.getcontentlists().get(j).getone(k));
									ans.add(tempreturnclass);
								}
							}
						}
					}
				}
				yaoqiucontent.add(contentcontentlist);
				yaoqiuindex.add(indexcontentlist);
			} else if (sql.contains("between")) {
				for (int i = 0; i < table.gettableindex().size(); i++) {
					if (table.gettableindex().get(i).equals(strings[0].trim())) {
						for (int j = 0; j < table.getcontentlists().get(i).length(); j++) {
							if (Integer.parseInt(table.getcontentlists().get(i).getone(j)) > Integer
									.parseInt(strings[2])
									&& Integer.parseInt(table.getcontentlists().get(i).getone(j)) < Integer
											.parseInt(strings[4])) {
								Insertclass tempreturnclass = new Insertclass();
								tempreturnclass.index.add(table.gettableindex().get(i));
								tempreturnclass.contain.add(table.getcontentlists().get(i).getone(j));
								ans.add(tempreturnclass);
							}
						}
					}
				}

			} else {
				for (int i = 0; i < strings.length; i++) {
					// 如果存在等于号
					if (strings[i].contains("=")) {
						// 将第二行的要求按照等号划分
						String[] temp = strings[i].split("=");
						// 得到修改的indexname
						String tempyaoqiuname = temp[0].trim();
						// 得到要求的内容
						String tempyaoqiucontent = temp[1].trim();
						if (tempyaoqiucontent.contains("'")) {
							tempyaoqiucontent = tempyaoqiucontent.substring(1, tempyaoqiucontent.length() - 1);
						}
						if (tempyaoqiucontent.contains(tempyaoqiuname)) {
							if (tempyaoqiucontent.contains("+")) {

								String[] temp2 = tempyaoqiucontent.split("+");
								tempyaoqiucontent = Integer
										.toString(Integer.parseInt(tempyaoqiucontent) + Integer.parseInt(temp2[1]));

							} else if (tempyaoqiucontent.contains("-")) {
								String[] temp2 = tempyaoqiucontent.split("-");
								tempyaoqiucontent = Integer
										.toString(Integer.parseInt(tempyaoqiucontent) - Integer.parseInt(temp2[1]));
							} else if (tempyaoqiucontent.contains("*")) {
								String[] temp2 = tempyaoqiucontent.split("*");
								tempyaoqiucontent = tempyaoqiucontent = Integer
										.toString(Integer.parseInt(tempyaoqiucontent) * Integer.parseInt(temp2[1]));
							} else if (yaoqiucontent.contains("/")) {
								String[] temp2 = tempyaoqiucontent.split("/");
								tempyaoqiucontent = tempyaoqiucontent = Integer
										.toString(Integer.parseInt(tempyaoqiucontent) * Integer.parseInt(temp2[1]));
							} else {
								System.out.println("输入的语法有问题，本dbms目前只支持加减乘除四种运算符号");
								Input.get();
							}

						}
						contentcontentlist.add(tempyaoqiucontent);
						indexcontentlist.add(tempyaoqiuname);
					}
					// 如果不存在等于号
					else {
						if (strings[i].equals("and")) {
							Contentlist tempindexcontentlist = new Contentlist();
							Contentlist tempcontentcontentlist = new Contentlist();
							for (int t = 0; t < contentcontentlist.length(); t++) {
								tempcontentcontentlist.add(contentcontentlist.getone(t));
								tempindexcontentlist.add(indexcontentlist.getone(t));
							}
							yaoqiuindex.add(tempindexcontentlist);
							yaoqiucontent.add(tempcontentcontentlist);
							contentcontentlist.empty();
							indexcontentlist.empty();

						}
						if (strings[i].equals("or")) {
							continue;
						}
					}
				}

				Contentlist tempindexcontentlist = new Contentlist();
				Contentlist tempcontentcontentlist = new Contentlist();
				for (int t = 0; t < contentcontentlist.length(); t++) {
					tempcontentcontentlist.add(contentcontentlist.getone(t));
					tempindexcontentlist.add(indexcontentlist.getone(t));
				}
				yaoqiuindex.add(tempindexcontentlist);
				yaoqiucontent.add(tempcontentcontentlist);
				contentcontentlist.empty();
				indexcontentlist.empty();
				// 开始查询
				// 先查等式的
				int chaxunflag1 = 0;
				int chaxunflag2 = 0;
				int tempflag2 = 1;
				for (int i = 0; i < yaoqiucontent.size(); i++) {
					for (int j = 0; j < table.gettableindex().size(); j++) {
						if (table.gettableindex().get(j).equals(yaoqiuindex.get(i).getone(0))) {
							chaxunflag1 = 1;
							for (int k = 0; k < yaoqiucontent.get(i).length(); k++) {
								for (int n = 0; n < table.getcontentlists().get(j).length(); n++) {
									if (yaoqiucontent.get(i).getone(k)
											.equals(table.getcontentlists().get(j).getone(n))) {
										chaxunflag2 = 1;
										Insertclass tempreturnclass = new Insertclass();
										tempreturnclass.index.add(table.gettableindex().get(j));
										tempreturnclass.contain.add(yaoqiucontent.get(i).getone(k));
										ans.add(tempreturnclass);
									}
								}
							}
						}

					}
					if (chaxunflag1 == 0 || chaxunflag2 == 0) {
						tempflag2 = 0;
						chaxunflag1 = 0;
						chaxunflag2 = 0;
					}
				}
				if (tempflag2 == 0) {
					System.out.println("此次无查询结果！");
					Input.get();
				}
			}
			return ans;
		} catch (Exception x) {
			System.out.println("本dbms不支持此操作，请重新输入！");
			Input.get();
			return null;
		}
	}

	/**
	 * 
	 * @Title: CheckGrant
	 * @Description: 对grant和revoke进行语句检查
	 * @param sql
	 * @return
	 * @throws IOException
	 * @return: boolean
	 * @throws
	 */
	public static boolean CheckGrant(String sql) throws IOException {
		int flag = 1;
		int tablenameindex = 0;
		int usernameindex = 0;
		int grantindex = 0;
		if ((!sql.contains("to") || !sql.contains("on")) && (!sql.contains("from") || !sql.contains("on"))) {
			flag = 0;
		}

		String[] strings = sql.split(" ");
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals("on")) {
				tablenameindex = i + 1;
			}
			if (strings[i].equals("to")) {
				usernameindex = i + 1;
			}
		}

		String username = strings[usernameindex];
		String tablename = strings[tablenameindex];
		String priname = strings[1];
		String[] prinames = priname.split(",");

		Table table = new Table();
		table = Utils.gettable(tablename);

		for (int i = 0; i < prinames.length; i++) {
			if (!prinames[i].equals("select") && !prinames[i].equals("insert") && !prinames[i].equals("delete")
					&& !prinames[i].equals("update") && !prinames[i].equals("all")) {
				flag = 0;
			}
		}

		if (flag == 1) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 * @Title: write   
	 * @Description: TODO(描述这个方法的作用)   
	 * @param name      
	 * @return: void      
	 * @throws
	 */
	public static void writeindex(String name,LinkedList<Indexclass> indexlist) {
		String path = SQLConstant.getNowPath();
		//
		String nowPath = path + "\\" + name+"_index.txt";
		String str="";
		for(int i=0;i<indexlist.size()-1;i++) {
			str+=indexlist.get(i).contain+SQLConstant.getSeparate()+indexlist.get(i).index;
			str += "\r\n";
		}
		str+=indexlist.get(indexlist.size()-1).contain+SQLConstant.getSeparate()+indexlist.get(indexlist.size()-1).index;
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
	/**
	 * 
	 * @Title: readindex   
	 * @Description: 读
	 * @param name
	 * @param indexlist      
	 * @return: void      
	 * @throws IOException 
	 * @throws
	 */
	public static LinkedList<Indexclass> readindex(String name) throws IOException {
		LinkedList<Indexclass> indextemp = new LinkedList<Indexclass>();
		String path = SQLConstant.getNowPath();
		String nowPath = path + "\\" + name + "_index.txt";
		try {

			FileReader fileReader = new FileReader(nowPath);

		} catch (Exception e) {
			System.out.println("没有此文件，请重试！");
			Input.get();
		}
		FileReader fileReader = new FileReader(nowPath);
		BufferedReader bufferedreader = new BufferedReader(fileReader);
		
		String tabletxt = bufferedreader.readLine();
		while(tabletxt!= null) {
			String[] temp = tabletxt.split(",");
			Indexclass tempindex=new Indexclass();
			tempindex.contain=(String) temp[0];
			tempindex.index=(String) temp[1];
			indextemp.add(tempindex);
			tabletxt = bufferedreader.readLine();
		}
		
		return indextemp;
	
	}
	/**
	 * 
	 * @Title: writegrant   
	 * @Description: 对grant语句写入文件 
	 * @param grantpeople      
	 * @return: void      
	 * @throws
	 */
	public static void writegrant(LinkedList<Grantclass> grantpeople) {
		String path = SQLConstant.getNowPath();
		String sep = SQLConstant.getSeparate();
		String nowPath = path + "\\" +"grant.txt";
		String str="";
		for(int i=0;i<grantpeople.size()-1;i++) {
			str += grantpeople.get(i).grantname + sep;
			str+=grantpeople.get(i).havename+sep;
			str+=grantpeople.get(i).grantoption+sep;
			for(int j=0;j<grantpeople.get(i).privileges.size();j++) {
				str+=grantpeople.get(i).privileges.get(j)+sep;
			}
			str += "\r\n";
		}
		str += grantpeople.get(grantpeople.size()-1).grantname + sep;
		str+=grantpeople.get(grantpeople.size()-1).havename+sep;
		str+=grantpeople.get(grantpeople.size()-1).grantoption+sep;
		for(int j=0;j<grantpeople.get(grantpeople.size()-1).privileges.size();j++) {
			str+=grantpeople.get(grantpeople.size()-1).privileges.get(j)+sep;
		}
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
}
