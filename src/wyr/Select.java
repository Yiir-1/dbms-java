package wyr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @ClassName: Select
 * @Description:实现select语句
 * @author: 王怡如
 * @Copyright:
 */
public class Select {
	public static void selectSql(String sql) throws IOException {
		try {
		System.out.println("查询语句辨认成功！");
		int fenge;
		String firstselect = null;
		String secondselect = null;
		if (Utils.CheckSelect(sql)) {
			//如果存在这三类，就开始集合查询
			int flag=0;
			if(sql.contains("union"))
			{
				fenge=sql.indexOf("union");
				firstselect=sql.substring(0,fenge-1);
				secondselect=sql.substring(fenge+6, sql.length());
				flag=1;
			}
			if(sql.contains("intersect")) {
				fenge=sql.indexOf("intersect");
				firstselect=sql.substring(0,fenge-1);
				secondselect=sql.substring(fenge+10, sql.length());
				flag=2;
			}
			if(sql.contains("except")) {
				fenge=sql.indexOf("except");
				firstselect=sql.substring(0,fenge-1);
				secondselect=sql.substring(fenge+7, sql.length());
				flag=3;
			}
			if(flag==0) {
				Select(sql);
				Input.get();
			}
			else {
				selectclass ans1 = new selectclass();
				selectclass ans2 = new selectclass();
				ans1 = Select(firstselect);
				ans2 = Select(secondselect);
				if(flag==1) {
					ans1=ans1.classunion(ans2);
					System.out.println("union查询结果如下：");
					ans1.print();
					Input.get();
				}
				else if(flag==2) {
					ans1=ans1.classintersect(ans2);
					System.out.println("intersect查询结果如下：");
					ans1.print();
					Input.get();
				}
				else {
					ans1=ans1.classexcept(ans2);
					System.out.println("except查询结果如下：");
					ans1.print();
					Input.get();
				}
				
			}
			
			
			
		} else {
			System.out.println("该语句语法词法分析有问题，请重新输入!");
			Input.get();
		}
		}
		catch(Exception e) {
			System.out.println("语句有问题，请重新输入！");
			Input.get();
		}
	}

	public static selectclass Select(String sql) throws IOException {
		String[] strings = sql.split(" ");
		int fromindex = 0;
		int whereindex = 0;
		int groupindex1=0;
		// 先找出位置
		int groupflag=0;
		if(sql.contains("group")) {
			groupflag=1;
		}
		for (int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
			if (strings[i].equals("where")) {
				whereindex = i;
			}
			if (strings[i].equals("from")) {
				fromindex = i;
			}
			if(groupflag==1) {
				if(strings[i].equals("group")) {
					groupindex1=i;
				}
			}
		}
		String[] indexcontains = strings[1].split(",");
		selectclass selectans = new selectclass();
		for (int i = 0; i < indexcontains.length; i++) {
			Contentlist newcon = new Contentlist();
			selectans.contain.add(newcon);
		}
		String[] tablename = strings[fromindex + 1].split(",");
		Table table1 ;
		LinkedList<Insertclass> chaxun = new LinkedList<Insertclass>();
		if (tablename.length > 1) {
			//连接查询
			table1 = new Table();
			Table table2 = new Table();
			table1 = Utils.gettable(tablename[0]);
			table2 = Utils.gettable(tablename[1]);
			String wherecontain;
			String keyindex = null;
			if(sql.contains("group")) {
			wherecontain=strings[whereindex+1];}
			else {
				wherecontain=strings[whereindex+1];
			}
			String s="";
			for(int i=0;i<whereindex;i++) {
				s+=strings[i]+" ";
			}
			s+=strings[whereindex];
			if(whereindex+3>strings.length) {
				s+=";";
			}
			else {
			for(int i=whereindex+3;i<strings.length-1;i++) {
				s+=strings[i]+" ";
			}
			s+=strings[strings.length-1];
			}
			
				//如果三个都存在的话，那就说明是说两个怎么去连接的，那就需要提取怎么分。
				if(wherecontain.contains("=")&&wherecontain.contains(tablename[0])&&wherecontain.contains(tablename[1])){
					String temp1strings=wherecontain.substring(tablename[0].length()+1,wherecontain.indexOf("="));
					keyindex=temp1strings;
				}
				

			//找到两个表用什么连接了之后，就开始进行连接
			LinkedList<Integer> indexlist=new LinkedList<Integer>();
			for(int i=0;i<table1.gettableindex().size();i++) {
				for(int j=0;j<table2.gettableindex().size();j++) {
					if(table1.gettableindex().get(i).equals(keyindex)&&table2.gettableindex().get(j).equals(keyindex)) {
						//如果两个都等于这一列，则先对两个表进行处理，只留下两个表相同的部分
						for(int p=0;p<table1.getcontentlists().get(i).length();p++) {
							int flag=0;
							for(int q=0;q<table2.getcontentlists().get(j).length();q++) {
								if(table2.getcontentlists().get(j).getone(q).equals(table1.getcontentlists().get(i).getone(p)))
								{
									indexlist.add(q);
									flag=1;
									break;
								}
							}
							if(flag==0) {
								for(int k=0;k<table1.getcontentlists().size();k++) {
									table1.getcontentlists().get(k).del(p);
								}
							}
						}
						//再来一遍删除table2的
						for(int p=0;p<table2.getcontentlists().get(j).length();p++) {
							int flag=0;
							for(int q=0;q<table1.getcontentlists().get(i).length();q++) {
								if(table1.getcontentlists().get(i).getone(q).equals(table2.getcontentlists().get(j).getone(p))) {
									flag=1;
									break;
								}
							}
							if(flag==0) {
								for(int k=0;k<table2.getcontentlists().size();k++) {
									table2.getcontentlists().get(k).del(p);
								}
							}
						}
						
					}
				}
			}
			//到目前已经得到了两个只拥有相同列的表
			//首先把两个表的index补充道1好表中
			for(int i=0;i<table2.gettableindex().size();i++) {
				int flag=0;
				for(int j=0;j<table1.gettableindex().size();j++) {
					if(table1.gettableindex().get(j).equals(table2.gettableindex().get(i))) {
						flag=1;
						break;
					}
				}
				if(flag==0) {
					table1.gettableindex().add(table2.gettableindex().get(i));
					Contentlist newcon=new Contentlist();
					for(int p=0;p<indexlist.size();p++) {
						newcon.add(table2.getcontentlists().get(i).getone(indexlist.get(p)));
					}
					table1.getcontentlists().add(newcon);
				}
			}
			//得到的table1为一张正确的表
			s=s.trim();
			
			chaxun = Utils.whereanalyse(tablename[0], s);
		} else {
			 table1 = new Table();
			table1 = Utils.gettable(tablename[0]);
			// 单表查询
			
			chaxun = Utils.whereanalyse(tablename[0], sql);
		}
			for (int j = 0; j < chaxun.size(); j++) {
				for (int i = 0; i < table1.gettableindex().size(); i++) {
					// 如果查询的和表的index相等的话
					if (chaxun.get(j).getindex().get(0).equals(table1.gettableindex().get(i))) {
						// 找出表的那一列，然后开始遍历
						for (int k = 0; k < table1.getcontentlists().get(i).length(); k++) {
							for (int n = 0; n < chaxun.get(j).getcontain().size(); n++) {
								// 如果两者的内容是一样的的话
								if (table1.getcontentlists().get(i).getone(k)
										.equals(chaxun.get(j).getcontain().get(n))) {
									// 那就找出表中其余的index的这一行，然后把他们都拿出来
									for (int p = 0; p < indexcontains.length; p++) {
										for (int q = 0; q < table1.gettableindex().size(); q++) {
											// 如果需要查找的是这一列，那行和列都找到了，就开始插入
											int flag = 0;
											if (table1.gettableindex().get(q).equals(indexcontains[p])) {
												//如果index里面没有内容，就直接两个都插入
												if(selectans.index.size()==0) {
													for(int h=0;h<selectans.index.size();h++) {
														if(indexcontains[p].equals(selectans.getindex(h)))
														{
															flag=h;
														}
													}
													selectans.addindex(indexcontains[p]);
													selectans.addcontain(table1.getcontentlists().get(q).getone(k), flag);
													
												}
												else {
													int flag2=0;
													int flag3=0;
													//先判断一下有没有这一列，如果没有这一列就加上
													for(int r=0;r<selectans.index.size();r++) {
														if(indexcontains[p].equals(selectans.index.get(r))) {
															flag3=1;
														}
													}
													if(flag3==0) {
														selectans.index.add(indexcontains[p]);
													}
												for(int r=0;r<selectans.index.size();r++) {
													if(indexcontains[p].equals(selectans.index.get(r))) {
													for(int u=0;u<selectans.contain.get(r).length();u++) {
														if(selectans.contain.get(r).getone(u).equals(table1.getcontentlists().get(q).getone(k))) {
															flag2=1;
														}
														
													}
															if(selectans.getindex(r).equals(indexcontains[p])&&flag2==0)
															{
																for(int h=0;h<selectans.index.size();h++) {
																	if(indexcontains[p].equals(selectans.getindex(h)))
																	{
																		flag=h;
																	}
																}
																selectans.addcontain(table1.getcontentlists().get(q).getone(k), flag);
																
															}
															else {
																if(flag2==0)
																{
																	int flag4=0;
																	for(int h=0;h<selectans.index.size();h++) {
																		if(indexcontains[p].equals(selectans.getindex(h)))
																		{
																			flag4=1;
																			flag=h;
																		}
																	}
																	if(flag4==0) {
																		flag=selectans.index.size();
																	}
																	if(flag4!=0)
																	{
																		selectans.addcontain(table1.getcontentlists().get(q).getone(k), flag);
																	}
																	else {
															selectans.addindex(indexcontains[p]);
															selectans.addcontain(table1.getcontentlists().get(q).getone(k), flag);
																	}
																}
															}
													}
													
												}
											}
											}
										}
									}
								}
							}
						}
					}
				}

			}
			// 如果有groupby 语句，则讲结果进行以下操作
			if (sql.contains("group") && sql.contains("by")) {
				int groupindex = 0;
				for (int i = 0; i < strings.length; i++) {
					if (strings[i].equals("group") && strings[i + 1].equals("by")) {
						groupindex = i + 2;
					}
				}
				System.out.println("Group by语句查询结果如下：");
				// 找出groupby所要聚集的内容
				String groupcontain;
				if(strings[groupindex].contains(";"))
					 groupcontain = strings[groupindex].substring(0,strings[groupindex].length()-1).trim();
				else {
					groupcontain = strings[groupindex].substring(0,strings[groupindex].length()).trim();
				}
				LinkedList<String> temp = new LinkedList<String>();
				for (int i = 0; i < table1.gettableindex().size(); i++) {
					// 如果index和需要聚集的是一样的
					if (table1.gettableindex().get(i).equals(groupcontain)) {
						// 那么就开始聚集
						// 首先查看一下里面有多少个内容
						int flag1 = 0;
						for (int j = 0; j < table1.getcontentlists().get(i).length(); j++) {
							for (int k = 0; k < temp.size(); k++) {
								if (temp.get(k).equals(table1.getcontentlists().get(i).getone(j))) {
									flag1 = 1;
								}
							}
							if (flag1 == 0) {
								temp.add(table1.getcontentlists().get(i).getone(j));
							}
						}
						// 拿到他的内容之后，我们就开始分组
						int flag3=0;
						for (int k = 0; k < selectans.index.size(); k++) {
							if (selectans.getindex(k).equals(groupcontain)) {
								flag3=1;
								for (int j = 0; j < temp.size(); j++) {
									selectclass tempselectans=new selectclass();
									//复制一份相同列数的
									for(int p=0;p<selectans.index.size();p++) {
										tempselectans.addindex(selectans.getindex(p));
									}
									for (int q = 0; q < tempselectans.index.size(); q++) {
										Contentlist newcon = new Contentlist();
										tempselectans.contain.add(newcon);
									}
									//开始寻找行数
									//如果两者的行的内容相同，泽说明找到行了
									for(int p=0;p<selectans.contain.get(k).length();p++) {
										if(selectans.contain.get(k).getone(p).equals(temp.get(j))) {
											//开始全部列都添加
											for(int n=0;n<selectans.index.size();n++) {
													tempselectans.addcontain(selectans.contain.get(n).getone(p), n);
											}
										}
									}
									//在这里，拿出集合操作得到的结果，然后对其做having语句
									int havingindex=0;
									if(sql.contains("having"))
									{
										
										for(int t=0;t<strings.length;t++) {
											if(strings[t].equals("having")) {
												havingindex=t+1;
											}
										}
										//得到having后面的内容
										String havingcontain=strings[havingindex];
										LinkedList<Insertclass> havingfenxi=new LinkedList<Insertclass>();
										
										havingfenxi=Utils.havinganalyse(havingcontain, tempselectans);
										//从tempselectans中找出符合条件的语句
										tempselectans=tempselectans.getfrominsertclass(havingfenxi);
										for(int p=0;p<tempselectans.index.size();p++) {
											System.out.print(tempselectans.index.get(p)+"    ");
										}
										System.out.println();
										for(int p=0;p<tempselectans.contain.size();p++)
										{
											System.out.print(tempselectans.contain.get(p).getone(0)+"    ");
										}
										System.out.println();
									}
									else
									tempselectans.print();
								}
							}
						}
						if(flag3==0) {
							System.out.println("输入语句语义有问题！分组对象不在查询结果中！");
						}
						
					}
				}
				
			}
			else {
			System.out.println("查询成功，查询结果为：");
			selectans.print();
			}
		

		return selectans;
	}
}
