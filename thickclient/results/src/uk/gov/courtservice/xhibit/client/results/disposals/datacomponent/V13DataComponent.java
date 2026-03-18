package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentAdapter;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V13DataComponent
 * </p>
 * <p>
 * Description: The text day or days depending on previous value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class V13DataComponent extends LabelDataComponent implements DataComponent {
    private final static String SINGLE_OPTION = "day";

    private final static String PLURAL_OPTION = "days";

    /**
     * DefaultDataComponent
     */
    public V13DataComponent() {
        super(PLURAL_OPTION);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v13DataComponentToolTip");
    }

    /**
     * DataCompnent Set the previous data component.
     */
    public void setPreviousDataComponent(DataComponent previousDataComponent) {
        if (previousDataComponent != null) {
            setText(delegate.isSingular(previousDataComponent) ? SINGLE_OPTION : PLURAL_OPTION);
            previousDataComponent.addDataComponentListener(new DataComponentAdapter() {
                public void dataChanged(DataComponentEvent e) {
                    setText(delegate.isSingular(e.getDataComponent()) ? SINGLE_OPTION : PLURAL_OPTION);
                }
            });
        }
    }
}
