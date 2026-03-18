package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

//import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

/**
* <p>
* Title: CourtSiteValueHelper
* </p>
* <p>
* Description: This class represents a court site in the court site tree
* </p>
* <p>
* Copyright: Copyright (c) 2008
* </p>
* <p>
* Company: Logica
* </p>
* 
* @author James Powell
* @version 1.0
*/

public class CourtSiteValueHelper {
  private XhbCourtSiteBasicValue model;
  private Vector <UnauthorisedCaseStatusTableRowModel> data = new Vector<UnauthorisedCaseStatusTableRowModel>();

  public CourtSiteValueHelper(XhbCourtSiteBasicValue obj) {
      setModel(obj);
  }

  public String toString() {
      return (getModel().getCourtSiteName() == null ? "" : getModel().getCourtSiteName());
  }

  public void setModel(XhbCourtSiteBasicValue model) {
      this.model = model;
  }

  public XhbCourtSiteBasicValue getModel() {
      return model;
  }

public void setData(Vector <UnauthorisedCaseStatusTableRowModel> data) {
    this.data = data;
}

public Vector <UnauthorisedCaseStatusTableRowModel> getData() {
    return data;
}
}
