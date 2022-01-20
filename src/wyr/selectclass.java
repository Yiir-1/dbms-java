package wyr;

import java.io.IOException;
import java.util.LinkedList;

public class selectclass {
	public LinkedList<String>index=new LinkedList<String>();
	public LinkedList<Contentlist> contain=new LinkedList<Contentlist>();
	public void addindex(String a) {
		this.index.add(a);
	}
	public void addcontain(String a,int i) {
		this.contain.get(i).add(a);
	}

	public void print() {
		for(int i=0;i<index.size();i++) {
			System.out.print(index.get(i)+"   ");
			
		}
		System.out.println();
		for(int i=0;i<contain.get(0).length();i++) {
			for(int j=0;j<contain.size();j++) {
				if(i>=contain.get(j).length()) {
					System.out.print(contain.get(j).getone(i-1)+"   ");
				}
				else {
				System.out.print(contain.get(j).getone(i)+"   ");
				}
			}
			System.out.println();
		}
	}
	public String getindex(int i) {
		return this.index.get(i);
	}
	/**
	 * 
	 * @Title: getfrominsertclass   
	 * @Description: 主要用于辅助查询，从class里面提取where和having语句分析的结果
	 * @param a
	 * @return
	 * @throws IOException      
	 * @return: selectclass      
	 * @throws
	 */
	public selectclass getfrominsertclass(LinkedList<Insertclass> a) throws IOException {
		selectclass ret=new selectclass();
		for(int i=0;i<this.index.size();i++) {
			ret.index.add(this.index.get(i));
			Contentlist newcon = new Contentlist();
			ret.contain.add(newcon);
		}

		//从tempselectans中找出符合条件的语句
		for(int p=0;p<this.index.size();p++) {
			for(int q=0;q<a.size();q++) {
				//如果他们两个相同的话，判断所含的内容是否相同，如果相同，则拿出来
				if(this.index.get(p).equals(a.get(q).index.get(0))) {
					for(int i=0;i<this.contain.get(p).length();i++) {
						for(int j=0;j<a.get(q).contain.size();j++) {
							//如果两者的内容相同的话
							if(this.contain.get(p).getone(i).equals(a.get(q).contain.get(j))) {
								//就表明选中了这一行，所以就需要向ret中加入
								//开始加入
								for(int k=0;k<this.contain.size();k++){
									ret.contain.get(k).add(this.contain.get(k).getone(i));
								}
							}
						}
					}
				}
			}
		}
		if(ret.contain.get(0).length()==0) {
			System.out.println("输入having有问题，请重新输入！");
			Input.get();
		}
		return ret;
	}
	/**
	 * 
	 * @Title: classunion   
	 * @Description: 集合操作union (并）  
	 * @return      
	 * @return: selectclass      
	 * @throws
	 */
	public selectclass classunion(selectclass a) {
		selectclass ret=new selectclass();
		for(int i=0;i<a.index.size();i++) {
			ret.index.add(a.index.get(i));
			Contentlist newcon = new Contentlist();
			ret.contain.add(newcon);
		}
		//先把其中一个表的内容加上
		for(int i=0;i<a.index.size();i++) {
			for(int j=0;j<a.contain.get(i).length();j++) {
				ret.contain.get(i).add(a.contain.get(i).getone(j));
			}
		}
		//在判断两个表，将不一样的地方加进去
		for(int i=0;i<this.index.size();i++) {
			for(int j=0;j<ret.index.size();j++)
			{
				//如果两个index相等的话
				if(this.index.get(i).equals(ret.index.get(j))) {
					//判断在this里面是不是有ret没有的东西
					for(int p=0;p<this.contain.get(i).length();p++)
					{
						int flag=0;
						for(int q=0;q<ret.contain.get(j).length();q++) {
							if(this.contain.get(i).getone(p).equals(ret.contain.get(j).getone(q))) {
								flag=1;
								break;
							}
							
						}
						if(flag==0) {
							//添加
							ret.contain.get(j).add(this.contain.get(i).getone(p));
						}
					}
				}
			}
			}
		
		return ret;
	}
	/**
	 * 
	 * @Title: classintersect   
	 * @Description: 集合操作   intersect (与）
	 * @return      
	 * @return: selectclass      
	 * @throws
	 */
	public selectclass classintersect(selectclass temp) {
		int flag=0;
		selectclass ret=new selectclass();
		for(int i=0;i<temp.index.size();i++) {
			ret.index.add(temp.index.get(i));
			Contentlist newcon = new Contentlist();
			ret.contain.add(newcon);
		}
		for(int i=0;i<temp.index.size();i++) {
			for(int j=0;j<this.index.size();j++) {
				//如果两个的index 相同的话  开始遍历两者的内容
				if(temp.index.get(i).equals(this.index.get(j))) {
					for(int p=0;p<temp.contain.get(i).length();p++) {
						for(int q=0;q<this.contain.get(j).length();q++) {
							//如果两者内容相同的话，就添加到ret里面
							if(temp.contain.get(i).getone(p).equals(this.contain.get(j).getone(q))) {
								ret.contain.get(i).add(temp.contain.get(j).getone(q));
								
							}
						}
					}
				}
			}
		}
		return ret;
	}
	/**
	 * 
	 * @Title: classexcept   
	 * @Description: 集合操作except   (差,this减去temp）
	 * @return      
	 * @return: selectclass      
	 * @throws
	 */
	public selectclass classexcept(selectclass temp) {
		selectclass ret=new selectclass();
		for(int i=0;i<temp.index.size();i++) {
			ret.index.add(temp.index.get(i));
			Contentlist newcon = new Contentlist();
			ret.contain.add(newcon);
		}
		for(int i=0;i<temp.index.size();i++) {
			for(int j=0;j<this.index.size();j++) {
				//如果两个的index 相同的话  开始遍历两者的内容
				if(temp.index.get(i).equals(this.index.get(j))) {
					for(int p=0;p<temp.contain.get(i).length();p++) {
						for(int q=0;q<this.contain.get(j).length();q++) {
							//如果两者内容相同的话，就添加到ret里面
							if(!(temp.contain.get(i).getone(p).equals(this.contain.get(j).getone(q)))) {
								ret.contain.get(i).add(temp.contain.get(j).getone(q));
								
							}
						}
					}
				}
			}
		}
		return ret;
	}
}
