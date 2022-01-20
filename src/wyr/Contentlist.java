package wyr;

import java.util.LinkedList;
import java.util.ListIterator;

public class Contentlist {
	public LinkedList<String>content=new LinkedList<String>();
	public Contentlist() {
		// TODO Auto-generated constructor stub
	}
	
	public Contentlist(String a)
	{
		content.add(a);
	}
	
	public void add(String a) {
		content.add(a);
	}
	
	public int length()
	{
		return content.size();
	}
	public void replace( LinkedList<String>a) {
		this.content=a;
	}
	
	public String getone(int i) {
		return content.get(i);
	}
	
	public LinkedList<String> gets() {
		return this.content;
	}
	
	public void del(int a) {
		content.remove(a);
	}
	public void change(String  old,String change){
		ListIterator<String> it = content.listIterator();
		it = this.content.listIterator();
		while(it.hasNext()) {
			String item = it.next();
			if(old.equals(item)) {
				it.set(change);
			}
		}
	}
	public void empty() {
		LinkedList<String>temp=new LinkedList<String>();
		this.content=temp;
	}
}
