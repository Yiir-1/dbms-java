package wyr;

import java.util.LinkedList;

public class Insertclass {
public LinkedList<String> index=new LinkedList<String>();
public LinkedList<String> contain=new LinkedList<String>();
public LinkedList<String> getindex(){
	return this.index;
}
public LinkedList<String> getcontain(){
	return this.contain;
}

public void insertindex(String a) {
	index.add(a);
}

public void insertcontain(String a) {
	contain.add(a);
}
public int getsize() {
	return this.index.size();
}
public String getoneindex(int i){
	return this.index.get(i);
}
public int size() {
	return this.index.size();
}
@Override
public String toString() {
	String ret="";
	ret+= "index  contain ";
	for(int i=0;i<getsize();i++) {
		ret+=index.get(i);
		ret+=" ";
		ret+=contain.get(i);
		ret+="\r\n";
	}
	return "index  contain ";
	
}
}
