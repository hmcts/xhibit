package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Component;

/**
 * <p>
 * Title: PromptComponent
 * </p>
 * <p>
 * Description: Used to render prompts
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.12 $
 */
public interface DisposalComponent {
    /**
     * Get the AWT component
     */
    public Component getComponent();

    /**
     * return true if the disposal has errors
     */
    public boolean isComplete();

    // Header

    /**
     * Set disposal Code
     */
    public void setCode(String code);

    /**
     * Set disposal title
     */
    public void setTitle(String title);

    /**
     * Set disposal version
     */
    public void setVersion(int version);

    // Body

    /**
     * Add a line
     */
    public void add(PromptComponent prompt, DataComponent data, InsertComponent insertComponent);

    /**
     * Used by the deportation panel.
     */
    public void setDeportationReason(DeportationModel reason);
    
    public void setHateCrimeReasons(HateCrimeModel reasons);
    
    public void setAggravatingReasons(AggravatingReasonsModel reasons);
    
    public DeportationModel getDeportationReason();

    public void setDeportationVisibility(boolean visibility);
    
    public HateCrimeModel getHateCrimeReasons();
    
    public void setHateCrimeTabVisibility(boolean hateCrimeTabVisibility);
    
    public AggravatingReasonsModel getAggravatingReasons();
    
    public void setAggravatingTabVisibility(boolean aggravatingTabVisibility);

    // Footer

    /**
     * Set the number of avail lines
     */
    public void setLineAvail(int lineAvail);

    /**
     * Add the listener
     */
    public void addDisposalListener(DisposalListener listener);

    /**
     * Remove the listener
     */
    public void removeDisposalListener(DisposalListener listener);

}
