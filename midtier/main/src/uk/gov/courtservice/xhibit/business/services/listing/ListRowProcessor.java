package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;

public class ListRowProcessor extends AbstractRowProcessor {

	Collection<ListBasicValue> lists = new ArrayList<ListBasicValue>();
	
	@Override
	public void processRow(Row row) {
		ListBasicValue list = new ListBasicValue();
		//list.set
		//lists.add(e);

	}

	public Collection<ListBasicValue> getLists() {
		// TODO Auto-generated method stub
		return null;
	}

}
