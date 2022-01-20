package wyr;

import java.util.Comparator;

public class Indexsort implements Comparator<Indexclass> {

	@Override
	public int compare(Indexclass o1, Indexclass o2) {
		// TODO Auto-generated method stub
		return o1.index.compareTo(o2.index);
	}
}
