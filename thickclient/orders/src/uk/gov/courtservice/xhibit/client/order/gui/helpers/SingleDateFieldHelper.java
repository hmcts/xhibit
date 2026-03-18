package uk.gov.courtservice.xhibit.client.order.gui.helpers;

public class SingleDateFieldHelper {
	private String[] values;
	
	private int valueMax=99;
	
	private void populateValues(){
    	values = new String[getValueMax()];
    	int element = 0;
    	for (int i = 1; i <= getValueMax(); i++){
    		values[i-1] = String.valueOf(element);
    		element++;
    	}
    }
	
	public String[] getValues(){
    	populateValues();
    	return this.values;
    }
	
	public int getValueMax(){
    	return this.valueMax;
    }
	
	public void setValueMax(int newValueMax){
    	this.valueMax = newValueMax + 1;
    }
	
	
}
