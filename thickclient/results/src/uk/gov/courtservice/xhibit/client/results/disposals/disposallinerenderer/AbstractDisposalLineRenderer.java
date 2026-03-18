package uk.gov.courtservice.xhibit.client.results.disposals.disposallinerenderer;

import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalLineProcessorManager;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalLineRenderer;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentFactory;
import uk.gov.courtservice.xhibit.client.results.disposals.PromptComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.PromptComponentFactory;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: AbstractDisposalLineRenderer
 * </p>
 * <p>
 * Description: Provide common line renderer behaviour.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.16 $
 */
public abstract class AbstractDisposalLineRenderer implements DisposalLineRenderer {
    /**
     * The prompt component
     */
    private PromptComponent promptComponent;

    /**
     * The prompt component
     */
    private InsertComponent insertComponent;

    /**
     * The data component
     */
    private DataComponent dataComponent;

    /**
     * The reference value can only be set once
     */
    private DisposalLineReferenceValue reference;

    /**
     * DisposalLineRenderer Implementation, process the reference then set it!
     */
    public void setReference(DisposalLineReferenceValue reference) {
        if (this.reference != null) {
            throw new IllegalStateException("reference: " + reference);
        }
        if (reference == null) {
            throw new IllegalArgumentException("reference: null");
        }
        this.reference = reference;
        process();
    }

    /**
     * DisposalLineRenderer Implementation, process the reference then set it!
     */
    protected void process() {
        DisposalLineProcessorManager.process(reference);
    }

    /**
     * DisposalLineRenderer Implementation
     */
    public DisposalLineReferenceValue getReference() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        return reference;
    }

    /**
     * DisposalLineRenderer Implementation
     */
    public void setLine(XhbDisposalLineBasicValue line) {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        if (line == null) {
            throw new IllegalArgumentException("line: null");
        }
        Integer lineNumber = line.getLineNumber();
        int index = lineNumber == null ? 0 : lineNumber.intValue();
        if (index == 0) {
            DataComponent dc = getDataComponent();
            if (dc != null) {
                String data = reference.getData(line);
                if (data != null) {
                    dc.setData(data);
                }

                if (reference.isDeletedG1(line) || reference.isDeleted(line)) {
                    dc.setDeletedG1(true);
                }

                if (reference.isDeletedG2(line)) {
                    dc.setDeletedG2(true);
                }
            }
        } else if (index > 0) {
            InsertComponent ic = getInsertComponent();
            if (ic != null) {
                String data = reference.getData(line);
                if (data != null) {
                    ic.setLine(index - 1, data); // convert into
                    // insertIndex
                }
            }
        }
    }

    public int getLineCount() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        InsertComponent ic = getInsertComponent();
        return ic == null ? 1 : (1 + ic.getLineCount());
    }

    /**
     * DisposalLineRenderer Implementation
     */
    public XhbDisposalLineBasicValue getLine(int index) {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }

        if (index == 0) {
            if (dataEntered()) {
                DataComponent dc = getDataComponent();
                if (dc != null) {
                    XhbDisposalLineBasicValue line = reference.createValue(0, dc.getData());
                    if (dc.isDeletedG1()) {
                        if (dc.getNameG1().equals(DataComponent.SINGLE_GROUP1_NAME)) {
                            reference.delete(line);
                        } else {
                            reference.deleteG1(line);
                        }
                    }
                    if (dc.isDeletedG2()) {
                        reference.deleteG2(line);
                    }
                    return line;
                }
            }
        } else {
            InsertComponent ic = getInsertComponent();
            if (ic != null) {
                if (index > 0 && index <= ic.getLineCount()) {
                    XhbDisposalLineBasicValue line = reference.createValue(index, ic.getLine(index - 1)); // convert
                                                                                                            // to
                    // insert index

                    DataComponent dc = getDataComponent();
                    if (dc != null) {
                        if (dc.isDeletedG1()) {
                            if (dc.getNameG1().equals(DataComponent.SINGLE_GROUP1_NAME)) {
                                reference.delete(line);
                            } else {
                                reference.deleteG1(line);
                            }
                        }
                        if (dc.isDeletedG2()) {
                            reference.deleteG2(line);
                        }
                    }
                    return line;
                }
            }
        }

        return null;
    }

    /**
     * Return true if the line has modified the reference line
     */
    private boolean dataEntered() {
        DataComponent dc = getDataComponent();
        return dc != null
                && ((reference.isInput() && !DisposalUtil.equals(reference.getData(), dc.getData()))
                        || dc.isDeletedG1() || dc.isDeletedG2() ||
                        (!reference.isInput() && reference.isFormPrint()));
    }

    /**
     * Get an instance of the prompt component
     */
    public PromptComponent getPromptComponent() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }

        if (promptComponent == null && reference.isScreenPrint()) {
            String prompt = reference.getPrompt();
            if (prompt != null) {
                promptComponent = createPromptComponent();
                promptComponent.setPrompt(prompt);
            }
        }
        return promptComponent;
    }

    /**
     * Get an instance of the insert component
     */
    public InsertComponent getInsertComponent() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }

        if (insertComponent == null && reference.isLineInsert()) {
            insertComponent = createInsertComponent();
            
            insertComponent.setMandatory(reference.isMandatory());
            insertComponent.setMaxChars(reference.getCharMax());
        }
        return insertComponent;
    }

    /**
     * Get an instance of the data component
     */
    public DataComponent getDataComponent() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }

        // Added non screen printing multiple choice so that they are deleted
        // correctly if
        // the group is deleted. This effects CJCO(3) DT94CON (1) DTCO (3)
        // POCCON (1)
        if (dataComponent == null && (reference.isScreenPrint() || reference.isMultipleChoice())) {
            dataComponent = createDataComponent();

            dataComponent.setData(reference.getData());
            dataComponent.setMandatory(reference.isMandatory());
            dataComponent.setMaxChars(reference.getCharMax());
            dataComponent.setScreenPrint(reference.isScreenPrint());

            String nameG1 = reference.getMcGroup1();
            if (nameG1 != null) {
                dataComponent.setNameG1(nameG1);
            } else if (reference.isMultipleChoice()) {
                dataComponent.setNameG1(DataComponent.SINGLE_GROUP1_NAME);
            }

            String nameG2 = reference.getMcGroup2();
            if (nameG2 != null) {
                dataComponent.setNameG2(nameG2);
            }
        }
        return dataComponent;
    }

    /**
     * Create an instance of the prompt component
     */
    protected PromptComponent createPromptComponent() {
        return PromptComponentFactory.create();
    }

    /**
     * Create an isntance of the data component
     */
    protected abstract InsertComponent createInsertComponent();

    /**
     * Create an isntance of the data component
     */
    protected abstract DataComponent createDataComponent();

}
