package uk.gov.courtservice.xhibit.client.results;

public class Item {
	private int key;
	private String value;
	
	public Item(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public int getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
