package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * 
 * @author grewalg
 *
 */
public class CourtRoomTableModel extends XHIBITDefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 983209051955022226L;

	protected final String resources = XhibitBundles.HomeCourt;

	public static final int COL_COURTROOM_NUMBER = 0;
	public static final int COL_SECURE = 1;
	public static final int COL_VIDEO_LINK = 2;
	public static final int COL_NOT_IN_USE = 3;

	private final Class[] columnClass = new Class[] { Integer.class, Boolean.class, Boolean.class, Boolean.class };

	String[] columnHeaders = new String[] {
			XHIBITConstant.getResource(resources, "CourtSite.CourtroomsTableCourtroomNumberColumn"),
			XHIBITConstant.getResource(resources, "CourtSite.CourtroomsTableSecureColumn"),
			XHIBITConstant.getResource(resources, "CourtSite.CourtroomsTableVideoLinkColumn"),
			XHIBITConstant.getResource(resources, "CourtSite.CourtroomsTableNotInUseColumn") };

	public CourtRoomTableModel() {
		super();
		setColumnNames(columnHeaders);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}

	@Override
	public String getColumnName(int column) {
		return columnHeaders[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object cellValue = null;

		if (_data.length <= 0) {
			return null;
		}
		CourtRoomBasicValue courtRoom = (CourtRoomBasicValue) _data[rowIndex];

		switch (columnIndex) {
		case COL_COURTROOM_NUMBER:
			cellValue = courtRoom.getCrestCourtRoomNo();
			break;
		case COL_SECURE:
			cellValue = decodeValue(courtRoom.getSecurityInd());
			break;
		case COL_VIDEO_LINK:
			cellValue = decodeValue(courtRoom.getVideoInd());
			break;
		case COL_NOT_IN_USE:
			cellValue = decodeValue(courtRoom.getObsInd());
			break;
		}
		return cellValue;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (_data.length > 0) {
			CourtRoomBasicValue row = (CourtRoomBasicValue) _data[rowIndex];

			if (0 == columnIndex) {
				if (aValue != null) {
					if (!aValue.equals("")) {
						if (aValue instanceof Integer) {
							row.setCrestCourtRoomNo((Integer) aValue);
						} else if (aValue instanceof String) {
							row.setCrestCourtRoomNo(Integer.parseInt((String) aValue));
						}
					} else {
						row.setCrestCourtRoomNo(null);
					}
				} else {
					row.setCrestCourtRoomNo(null);
				}
			} else if (1 == columnIndex) {
				row.setSecurityInd(((Boolean) aValue).booleanValue() == true ? "Y" : "N");
			} else if (2 == columnIndex) {
				row.setVideoInd(((Boolean) aValue).booleanValue() == true ? "Y" : "N");
			} else if (3 == columnIndex) {
				row.setObsInd(((Boolean) aValue).booleanValue() == true ? "Y" : "N");
			}
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		CourtRoomBasicValue basic = (CourtRoomBasicValue) _data[row];
		if ((basic.getId() == null && column == 3)) {
			return false;
		} else {
			return true;
		}
	}

	private Boolean decodeValue(String value) {
		if ("N".equals(value)) {
			return Boolean.FALSE;
		} else if ("Y".equals(value)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Adds a new row in the CourtRooms table. Before doing so it checks if
	 * existing rows contain mandatory data.
	 * 
	 * @param row
	 */
	public void addRow(Object[] row) {
		CourtRoomBasicValue courtRoom = new CourtRoomBasicValue();
		courtRoom.setObsInd("N");
		courtRoom.setSecurityInd("N");
		courtRoom.setVideoInd("N");

		addCourtRoom(courtRoom);
	}

	/**
	 * Add the courtRoom to the model.
	 * 
	 * @param courtRoom
	 */
	public void addCourtRoom(CourtRoomBasicValue courtRoom) {
		List<Object> list = getDataArrayAsList();

		list.add(0,courtRoom);
		setData(list);
	}

	/**
	 * Add a collection of CourtRoomBasicValue to the model.
	 * 
	 * @param courtRooms
	 *            The collection of rows to add.
	 */
	public void addCourtRooms(Collection<CourtRoomBasicValue> courtRooms) {
		List<Object> list = getDataArrayAsList();

		if (courtRooms != null) {
			for (final CourtRoomBasicValue row : courtRooms) {
					list.add(row);
			}
		}
		setData(list);
	}

	/**
	 * Converts data array to a list to allow a range of operations to be
	 * performed.
	 * 
	 * @return
	 */
	private List<Object> getDataArrayAsList() {
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data));
		}
		return list;
	}

	/**
	 * Checks to see if a newly created row contains the same court room number
	 * as a previous entered row.
	 * 
	 * @param roomNo
	 * @param row
	 * @return
	 */
	public boolean isRoomNumberExist(Integer roomNo, int row) {
		boolean exists = false;
		List list = getDataArrayAsList();

		int index = 0;
		for (Object o : list) {
			CourtRoomBasicValue val = (CourtRoomBasicValue) o;
			if (roomNo.equals(val.getCrestCourtRoomNo()) && row != index && row > index) {
				exists = true;
				break;
			}
			index++;
		}
		return exists;
	}
}
