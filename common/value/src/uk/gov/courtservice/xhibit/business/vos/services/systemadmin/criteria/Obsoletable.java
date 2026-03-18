package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Marker interface to indicate if a search item can be marked
 * obselete. If the <Item>SearchCriteria class implements this interface then
 * the obsInd attribute included in this interface can be used to include obsInd
 * in the search criteria. If the obsInd attribute is NOT set in the search
 * criteria then the query will have " AND NOT o.obsInd = 'Y'" appended by
 * default when it is created in the AbstractSearchCriteria class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: Obsoletable.java,v 1.2 2006/05/31 14:20:39 bzjrnl Exp $
 */

public interface Obsoletable {
    public static final String ATTRIBUTE_NAME_OBSIND = "obsInd";
}