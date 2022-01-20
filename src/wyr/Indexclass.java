package wyr;

public class Indexclass {
	public String contain;
	public String index;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contain == null) ? 0 : contain.hashCode());
		return result;
	}
	@Override
	public String toString() {
		return "contain=" + contain + ", index=" + index;
	}
	
	
}
