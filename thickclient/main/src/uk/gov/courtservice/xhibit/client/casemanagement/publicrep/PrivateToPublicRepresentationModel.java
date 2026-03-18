package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

import java.util.Date;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.util.XPanel;


/**
 * The model used for the fields on the private to public representation screen.
 * @author waltersn
 *
 */
public class  PrivateToPublicRepresentationModel {

	private String solicitorName;
	private String solicitorAddress1;
	private String solicitorAddress2;
	private String solicitorAddress3;
	private String solicitorAddress4;
	private String solicitorTown;
	private String solicitorCounty;
	private String solicitorPostCode;
	private String solicitorDocExRef;
	private String solicitorTelephoneNo;
	private String solicitorFaxNo;
	private String solicitorSecureEmail;
	private String solicitorNonSecureEmail;
	private String solicitorRef;
	private Date startDate;
	private Date endDate;
	
	private Integer id;
	private XPanel callingClass;
	private CaseType caseType;


	
	public PrivateToPublicRepresentationModel() {
		clearmodel();
	}
	
	public PrivateToPublicRepresentationModel(final Integer id, XPanel callingClass) {
		this.id = id;
		this.callingClass = callingClass;
		clearmodel();
	}
	
	public PrivateToPublicRepresentationModel(final Integer id, XPanel callingClass, CaseType caseType) {
		this.id = id;
		this.callingClass = callingClass;
		this.caseType = caseType;
		clearmodel();
	}


	public String getSolicitorName() {
		return solicitorName;
	}


	public void setSolicitorName(String solicitorName) {
		this.solicitorName = solicitorName;
	}


	public String getSolicitorAddress1() {
		return solicitorAddress1;
	}


	public void setSolicitorAddress1(String solicitorAddress1) {
		this.solicitorAddress1 = solicitorAddress1;
	}


	public String getSolicitorAddress2() {
		return solicitorAddress2;
	}


	public void setSolicitorAddress2(String solicitorAddress2) {
		this.solicitorAddress2 = solicitorAddress2;
	}


	public String getSolicitorAddress3() {
		return solicitorAddress3;
	}


	public void setSolicitorAddress3(String solicitorAddress3) {
		this.solicitorAddress3 = solicitorAddress3;
	}


	public String getSolicitorAddress4() {
		return solicitorAddress4;
	}


	public void setSolicitorAddress4(String solicitorAddress4) {
		this.solicitorAddress4 = solicitorAddress4;
	}


	public String getSolicitorTown() {
		return solicitorTown;
	}


	public void setSolicitorTown(String solicitorTown) {
		this.solicitorTown = solicitorTown;
	}


	public String getSolicitorCounty() {
		return solicitorCounty;
	}


	public void setSolicitorCounty(String solicitorCounty) {
		this.solicitorCounty = solicitorCounty;
	}


	public String getSolicitorPostCode() {
		return solicitorPostCode;
	}


	public void setSolicitorPostCode(String solicitorPostCode) {
		this.solicitorPostCode = solicitorPostCode;
	}


	public String getSolicitorDocExRef() {
		return solicitorDocExRef;
	}


	public void setSolicitorDocExRef(String solicitorDocExRef) {
		this.solicitorDocExRef = solicitorDocExRef;
	}


	public String getSolicitorTelephoneNo() {
		return solicitorTelephoneNo;
	}


	public void setSolicitorTelephoneNo(String solicitorTelephoneNo) {
		this.solicitorTelephoneNo = solicitorTelephoneNo;
	}


	public String getSolicitorFaxNo() {
		return solicitorFaxNo;
	}


	public void setSolicitorFaxNo(String solicitorFaxNo) {
		this.solicitorFaxNo = solicitorFaxNo;
	}


	public String getSolicitorSecureEmail() {
		return solicitorSecureEmail;
	}


	public void setSolicitorSecureEmail(String solicitorSecureEmail) {
		this.solicitorSecureEmail = solicitorSecureEmail;
	}


	public String getSolicitorNonSecureEmail() {
		return solicitorNonSecureEmail;
	}


	public void setSolicitorNonSecureEmail(String solicitorNonSecureEmail) {
		this.solicitorNonSecureEmail = solicitorNonSecureEmail;
	}


	public String getSolicitorRef() {
		return solicitorRef;
	}


	public void setSolicitorRef(String solicitorRef) {
		this.solicitorRef = solicitorRef;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Integer getId() {
		return id;
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public void clearmodel() {
		setSolicitorName(null);
		setSolicitorAddress1(null);
		setSolicitorAddress2(null);
		setSolicitorAddress3(null);
		setSolicitorAddress4(null);
		setSolicitorTown(null);
		setSolicitorCounty(null);
		setSolicitorPostCode(null);
		setSolicitorDocExRef(null);
		setSolicitorTelephoneNo(null);
		setSolicitorFaxNo(null);
		setSolicitorSecureEmail(null);
		setSolicitorNonSecureEmail(null);
		setSolicitorRef(null);
		setStartDate(null);
		setEndDate(null);
    }
	
	@Override
	public String toString() {
		return "Name "+getSolicitorName()+" and Address :"+getSolicitorAddress1()
		+" , "+getSolicitorAddress2()+" , "+getSolicitorAddress3()
		+" , "+getSolicitorAddress4()+" , "+getSolicitorTown()
		+" , "+getSolicitorCounty()+" , "+getSolicitorPostCode()
		+" , "+getSolicitorDocExRef()+" , "+getSolicitorTelephoneNo()
		+" , "+getSolicitorFaxNo()+" , "+getSolicitorSecureEmail()
		+" , "+getSolicitorNonSecureEmail()+" , and ref: "+getSolicitorRef()
		+" and start date : "+getStartDate()+" and end date "+getEndDate();
	}

	
	
}
