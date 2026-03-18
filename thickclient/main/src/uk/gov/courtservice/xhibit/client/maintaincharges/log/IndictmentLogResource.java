package uk.gov.courtservice.xhibit.client.maintaincharges.log;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT 2 - Indictment Log Resource
 * </p>
 * <p>
 * Description: A resource bundle of indictment log related text.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public abstract class IndictmentLogResource {
    private static ResourceBundle resources = null;

    static {
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.IndictmentLogResources);
    }

    public static final String ANDSMALL = XHIBITConstant.getResource(resources, "andSmall");

    public static final String CASESBIG = XHIBITConstant.getResource(resources, "casesBig");

    public static final String ADDEDBIG = XHIBITConstant.getResource(resources, "addedBig");

    public static final String ADDEDSMALL = XHIBITConstant.getResource(resources, "addedSmall");

    public static final String JOINEDON = XHIBITConstant.getResource(resources, "joinedOn");

    public static final String GRANTEDBY = XHIBITConstant.getResource(resources, "grantedBy");

    public static final String DIALOGTITLE = XHIBITConstant.getResource(resources, "dialogTitle");

    public static final String DIALOGNARRATOR = XHIBITConstant.getResource(resources, "dialogNarrator");

    public static final String QUASHEDBIG = XHIBITConstant.getResource(resources, "quashedBig");

    public static final String QUASHEDSMALL = XHIBITConstant.getResource(resources, "quashedSmall");

    public static final String STAYEDBIG = XHIBITConstant.getResource(resources, "stayedBig");

    public static final String STAYEDSMALL = XHIBITConstant.getResource(resources, "stayedSmall");

    public static final String ON = XHIBITConstant.getResource(resources, "on");

    public static final String CHARGE = XHIBITConstant.getResource(resources, "charge");

    public static final String INDICTMENT = XHIBITConstant.getResource(resources, "indictment");

    public static final String DEFENDANTBIG = XHIBITConstant.getResource(resources, "defendantBig");
    
    public static final String DEFENDANTSMALL = XHIBITConstant.getResource(resources, "defendantSmall");

    public static final String COUNTSMALL = XHIBITConstant.getResource(resources, "countSmall");

    public static final String COUNTBIG = XHIBITConstant.getResource(resources, "countBig");

    public static final String REMOVEDBIG = XHIBITConstant.getResource(resources, "removedBig");

    public static final String REMOVEDSMALL = XHIBITConstant.getResource(resources, "removedSmall");
    
    public static final String DELETEDBIG = XHIBITConstant.getResource(resources, "deletedBig");

    public static final String DELETEDSMALL = XHIBITConstant.getResource(resources, "deletedSmall");

    public static final String CHANGEDSMALL = XHIBITConstant.getResource(resources, "changedSmall");

    public static final String CHANGEDBIG = XHIBITConstant.getResource(resources, "changedBig");
    
    public static final String RENUMBEREDBIG = XHIBITConstant.getResource(resources, "renumberedBig");
    
    public static final String RENUMBEREDSMALL = XHIBITConstant.getResource(resources, "renumberedSmall");

    public static final String TO = XHIBITConstant.getResource(resources, "to");

    public static final String PARTICULARSAMMENED = XHIBITConstant.getResource(resources, "particularsAmmended");

    public static final String LIEONFILE = XHIBITConstant.getResource(resources, "lieOnFile");

    public static final String LIEONFILEBIG = XHIBITConstant.getResource(resources, "lieOnFileBig");

    public static final String TOOMANYCHARS = XHIBITConstant.getResource(resources, "tooManyCharacters");

    public static final String TOOMANYCHARS_TITLE = XHIBITConstant.getResource(resources, "tooManyChars.Title");

    public static final String TOOMANYCHARS_MESSAGE = XHIBITConstant.getResource(resources, "tooManyChars.Message");

    public static final String INDABBREVBIG = XHIBITConstant.getResource(resources, "indabbrevBig");

    public static final String FORWARDSLASH = XHIBITConstant.getResource(resources, "forwardSlash");

    public static final String FROM = XHIBITConstant.getResource(resources, "from");

    public static final String EDITEDBIG = XHIBITConstant.getResource(resources, "editedBig");

    public static final String EDITEDSMALL = XHIBITConstant.getResource(resources, "editedSmall");
}