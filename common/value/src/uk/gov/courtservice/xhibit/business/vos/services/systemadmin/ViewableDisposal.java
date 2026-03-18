package uk.gov.courtservice.xhibit.business.vos.services.systemadmin;

/**
 * Intended for use UIs that View Disposals & Disposal Menus in the same frame.
 * <p>
 * The view over the Disposal Menus & Disposal Items will populate its list with
 * both types. If they both implement this simple interface, then that view can
 * treat them equally with no messy 'instanceof' calls.
 * </p>
 * <p>
 * Basically, it defines isMenu along with two methods that are used to get the
 * common information required by the view. Currenty, this has been assumed (ie.
 * guessed) to be Code and Title. Their type is also assumed to be String. It is
 * then up to the implementor of the interface to map this to the relevant
 * attribute i.e. Code should be mapped to disposalCode for both Menu & Disposal
 * whereas Title should be mapped to RefDisposal.disposalTitle and
 * RefDisposalMenu.title.
 * </p>
 * <p>
 * Sorry; the words teach, grandmother, suck & eggs come to mind!
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public interface ViewableDisposal {

    /**
     * Is this a Menu or a Menu Item - Disposal or Disposal Menu to you and me.
     * <p>
     * Of course, if it isn't a [Disposal] Menu, then it must be a Disposal.
     * </p>
     * 
     * @return boolean
     */
    public boolean isMenu();

    public String getViewableCode();

    public String getViewableTitle();
}