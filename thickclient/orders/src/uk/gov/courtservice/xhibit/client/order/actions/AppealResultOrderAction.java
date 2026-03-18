package uk.gov.courtservice.xhibit.client.order.actions;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class AppealResultOrderAction extends AbstractPreviewList {
	
	private static final long serialVersionUID = 1L;

    public AppealResultOrderAction() throws CSRecoverableException {
        super("AppealResultOrder");
    }

	@Override
	protected String getTransformationStylesheetURL() {
		return "config/xsl/results/reports/aro/printAppealResultOrder.xsl";
	}

	@Override
	public String getXml() throws Exception {
		XhibitApplicationController xac = (XhibitApplicationController) getController(); 
		String aroXml = XhibitDelegateHelper.getResults2Delegate().getAROInformation(xac.getApplicationCaseModel().getCaseId());
		if(aroXml != null){
			return aroXml;
		}
		 else
        {
        	throw new CSRecoverableException("appealresults.notfound", "Appeal Order information not found");
        }	
	}
	
	/**
     * Get xml for the currrent court formatted for printing using the parameterMap if it exists
     * 
     * @return String the xml
     * @throws Exception
     */
    protected String getFormatedXml() throws Exception { 
    	String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NHAReport.base");			
		URL url = AppealResultOrderAction.class.getClassLoader().getResource(path);	
		Map parameterMap = buildMap(path,url);
        return XSLServices.getInstance().transform(getXml(), getTransformationStylesheetURL(), Locale.getDefault(), parameterMap);
    }

    @SuppressWarnings("unchecked")
	public Map buildMap(String path, URL url) {
    	Map map = new HashMap();
		String baseDir = url.toString();
        baseDir = baseDir.replace(path, "");
		map.put("basedir",baseDir.substring(0,baseDir.length()-1));
		map.put("mode", "display");
		
		return map;
	}
}
