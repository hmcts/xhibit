package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import uk.gov.courtservice.xhibit.client.order.screens.model.OffenceModel;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.DefendantsTableRowModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.OriginalChargesTableRowModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * Class handles the properties of the Offence Link Selection table
 * @author Ross McArthur
 *
 */
public class OffenceSelectorModel extends XHIBITDefaultTableModel {

	public static final int CHECKBOX_COL =0;
	public static final int DVLA_OFF_CODE_COL =1;
	public static final int OFFENCE_CODE_COL =2;
	public static final int CONV_DATE_COL = 3;
	public static final int DESC_COL=4; 
	public static final int DATA_SENT_COL=5;
	public OrderOffencePanel parent;
	
	public OffenceSelectorModel()
	{
		super();
		setup(new ArrayList<OffenceSelectorRowModel>(), this.columnNames); 
	}
	
	/**
	 * 
	 * @param parent
	 * @param model
	 */
	public OffenceSelectorModel(OrderOffencePanel parent, OrderOffenceModel model)
	{
		super();
		this.parent = parent;
		ArrayList<OffenceSelectorRowModel> offenceRows= new ArrayList<OffenceSelectorRowModel>();
		
		for(OffenceModel offence: model.getOffences())
		{
			offenceRows.add(new OffenceSelectorRowModel(parent, model, offence));
			
		}
		
		setup(offenceRows, this.columnNames); 
		
		
	}
	
	
	
	
	/**
	 * 
	 * @param data
	 * @param columnNames
	 */
	 protected void setup(Collection<OffenceSelectorRowModel> data, String[] columnNames) {
		 	this.setData(data);
		 	
	        super.setColumnNames(columnNames);
	        this.setColumnNames(columnNames);
	        
	        
	    }
	
	 /**
	  * Column headers for the table
	  */
	private String[] columnNames = new String[] { " ",
	        XHIBITConstant.getResource(XhibitBundles.OrderOffenceResources, "dvlaOffenceLabel"),
	        XHIBITConstant.getResource(XhibitBundles.OrderOffenceResources, "offenceLabel"),
	        XHIBITConstant.getResource(XhibitBundles.OrderOffenceResources, "convictionDateLabel"), 
	        XHIBITConstant.getResource(XhibitBundles.OrderOffenceResources, "descriptionOffenceLabel"),
	        XHIBITConstant.getResource(XhibitBundles.OrderOffenceResources, "dateOffenceLabel"),
	        
	        
	 };
	
	 public boolean isCellEditable(int r, int c) {
	        
        OffenceSelectorRowModel trm = (OffenceSelectorRowModel)getDataAt(r);
        
        if((c== this.CHECKBOX_COL) && !(trm.getFinalD20() || trm.getINTD20()))
        {
        	return true;
        }
        else
        {
        	return false;
        }
	 }
	
	/**
	 * 
	 * @param r
	 * @param c
	 * @return
	 */
	public Object getValueAt(int r, int c) {
		OffenceSelectorRowModel trm = (OffenceSelectorRowModel)getDataAt(r);
        switch (c) {
        	case CHECKBOX_COL: 
        		return trm.getSelectorBox();
            case DVLA_OFF_CODE_COL:
                return trm.getDVLAOffence();
            case OFFENCE_CODE_COL:
                return trm.getOffenceCode();
            case DESC_COL:
                return trm.getOffenceDescription();
            case DATA_SENT_COL: 
            	return trm.getDateSentOnD20();
            case CONV_DATE_COL: 
            	return trm.getConvictionDate();
            default:
                return "";
        }
    }

	/**
	 * 
	 * @param col
	 * @return
	 */
    public Class getColumnClass(int col) {
        switch (col) {
            case DESC_COL:
            case DVLA_OFF_CODE_COL:
            case OFFENCE_CODE_COL:
            case CONV_DATE_COL:
            case DATA_SENT_COL: 
            	 return String.class;
            case CHECKBOX_COL:
            	return Boolean.class;
            default:
                return Object.class;
        }
    }
    
    /**
     * Prints data off all rows in table
     */
    public void printModel() {
        XHIBITConstant.debug("OffenceLinkData");
        XHIBITConstant.debug("================");
        for( int x = 0; x < getRowCount(); x++ ) {
        	OffenceSelectorRowModel item = (OffenceSelectorRowModel)getDataAt(x);
            XHIBITConstant.debug("convictionDate: "+item.getConvictionDate());
            XHIBITConstant.debug("dvla             : "+item.getDVLAOffence());
            XHIBITConstant.debug("offenceCode   : "+item.getOffenceCode());
            XHIBITConstant.debug("offenceDescription              : "+item.getOffenceDescription());
            XHIBITConstant.debug("dataSent             : "+item.getDateSentOnD20());
        }
    }
    
    /**
     * Method Sets value of cell items 
     */
    public void setValueAt(Object value, int r, int c) {
    	try
    	{
	    	OffenceSelectorRowModel trm = (OffenceSelectorRowModel)getDataAt(r);
	    	if(c== this.CHECKBOX_COL)
	    	{
	    		trm.setSelectorBox(((Boolean)value).booleanValue());
	    		this.fireTableDataChanged();
	    		if(parent!=null)
	    		{
	    			parent.stepUpdateViewState();
	    		}
	    	
	    	}
    	}
    	catch(Exception e)
    	{
    		 XHIBITConstant.debug("No row value set");
    	}
    }
    
    
	

}
