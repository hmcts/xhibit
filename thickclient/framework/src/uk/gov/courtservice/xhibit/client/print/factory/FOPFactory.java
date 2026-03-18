package uk.gov.courtservice.xhibit.client.print.factory;

import java.net.URI;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;

import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.helper.PrintDialogHelper;
import uk.gov.courtservice.xhibit.client.print.helper.PrintPreviewHelper;

/**
 * <p>
 * Title: FOPFactory
 * </p>
 * <p>
 * Description: Returns the appropriate implementation of FOPInterface depending
 * on wether or not a print preview is required
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class FOPFactory {
    private static PrintPreviewHelper printPreviewHelper;

    private static PrintDialogHelper printDialogHelper;

    /**
     * Returns the FopFactoryBuilder with backwards compatibility
     * 
     * @param Uri uri
     * @return The new FopFactoryBuilder
     */
    public static FopFactoryBuilder getFopFactoryBuilder(final URI uri) {
		FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(uri);
    	// Set the Fop to backwards compatibility validation
    	fopFactoryBuilder.setStrictFOValidation(false);
    	return fopFactoryBuilder;
    }
    
    /**
     * Returns the equivalent of new FopFactory.newInstance
     * 
     * @param Uri uri
     * @return The new FopFactory
     */
    public static FopFactory getFopFactory(final URI uri) {
    	return getFopFactoryBuilder(uri).build();
    }
    
	public static FOUserAgent getFoUserAgent(FopFactory fopfactory) {
		FOUserAgent foUserAgent = fopfactory.newFOUserAgent();
		return foUserAgent;
	}
    
    /**
     * Returns the appropriate FOPInterface. The FOPInterface will either show a
     * preview dialog (<code>PrintPreviewHelper</code>) or just a print
     * dialog (<code>PrintDialogHelper</code>)
     * 
     * @see PrintDialogHelper, PrintPreviewHelper
     * @param preview
     *            boolean to determine if preview is required
     * @return The appropriate implementation of FOPInterface
     */
    public static FOPInterface getFOPRenderer(boolean preview) {
        if (preview == true) {
            return new PrintPreviewHelper();
        } else {
            return new PrintDialogHelper();
        }
    }

    /**
     * Returns the default FOPInterface (<code>PrintPreviewHelper</code>)
     * 
     * @return PrintPreviewHelper
     */
    public FOPInterface getFOPRenderer() {
        return new PrintPreviewHelper();
    }

    private static FOPInterface getHelper(boolean preview) {
        if (preview == true) {
            if (null == printPreviewHelper) {
                printPreviewHelper = new PrintPreviewHelper();
            }
            return printPreviewHelper;
        } else {
            if (null == printDialogHelper) {
                printDialogHelper = new PrintDialogHelper();
            }
            return printDialogHelper;
        }

    }
}
