package uk.gov.courtservice.xhibit.common.publicdisplay.setup.drilldown;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class DrillDown implements Serializable {
	
	static final long serialVersionUID = -3260495173823304712L;
	
    private final ArrayList arrayList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public DrillDown(String name) {
        arrayList = new ArrayList();
        this.name = name;
    }

    public ArrayList getValues() {
        return arrayList;
    }

    public boolean add(Object object) {
        return arrayList.add(object);
    }
}
