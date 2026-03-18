<%@ page import="uk.gov.courtservice.xhibit.business.services.admin.*,
	         uk.gov.courtservice.xhibit.business.entities.xhb_formatting.XhbFormattingBasicValue,
	         java.util.*"%>

<%
/* retrieve request variables */ 

int blobId = Integer.parseInt(request.getParameter("BLOB_ID"));

RefDataAdminControllerBeanBusinessDelegate controller = RefDataAdminControllerBeanBusinessDelegate.DelegateFactory.getInstance();
String html = controller.getIWPHTML(blobId);

out.print(html);
%>
