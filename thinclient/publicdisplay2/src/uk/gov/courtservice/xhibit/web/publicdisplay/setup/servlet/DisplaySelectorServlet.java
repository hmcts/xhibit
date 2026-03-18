package uk.gov.courtservice.xhibit.web.publicdisplay.setup.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.setup.ejb.PDSetupControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.common.publicdisplay.setup.drilldown.CourtDrillDown;

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
 * @version $Revision: 1.5 $
 */
public class DisplaySelectorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        String courtIdStr = request.getParameter("courtId");
        if (courtIdStr == null) {
            XhbCourtBasicValue[] allCourts = PDSetupControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                    .getAllCourts();
            request.setAttribute("courts", allCourts);
            request.getRequestDispatcher("/setup/court_selector.jsp").include(request, response);
            return;
        }
        Integer courtId = new Integer(courtIdStr);
        CourtDrillDown drillDownForCourt = PDSetupControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                .getDrillDownForCourt(courtId);
        request.setAttribute("drillDown", drillDownForCourt);
        request.getRequestDispatcher("/setup/display_selector.jsp").include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doRequest(request, response);
    }

}
