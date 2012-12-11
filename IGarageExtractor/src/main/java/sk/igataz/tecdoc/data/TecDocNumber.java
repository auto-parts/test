package sk.igataz.tecdoc.data;

public class TecDocNumber {
	private String originalNumber;
	private String searchNumber;
	
	public TecDocNumber(String originalNumber, String searchNumber) {
		super();
		this.originalNumber = originalNumber;
		this.searchNumber = searchNumber;
	}

	public String getOriginalNumber() {
		return originalNumber;
	}

	public void setOriginalNumber(String originalNumber) {
		this.originalNumber = originalNumber;
	}

	public String getSearchNumber() {
		return searchNumber;
	}

	public void setSearchNumber(String searchNumber) {
		this.searchNumber = searchNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((searchNumber == null) ? 0 : searchNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TecDocNumber other = (TecDocNumber) obj;
		if (searchNumber == null) {
			if (other.searchNumber != null)
				return false;
		} else if (!searchNumber.equals(other.searchNumber))
			return false;
		return true;
	}
	
	
	public String toString() {
		return getSearchNumber();
	}
	
	
	
}
