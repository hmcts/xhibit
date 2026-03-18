package uk.gov.courtservice.xhibit.client.listings.list.sitting;

import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class SittingModel {
	
	public enum ModelType {
		NEW,
		EDIT
	}
	
	private XhibitApplicationController xac;
	
	private SittingOnListComplexValue sitting;

	private ModelType modelType;

	private boolean dirty;

	public void setModelType(ModelType mode) {
		this.modelType = mode;
	}

	public ModelType getModelType() {
		return modelType;
	}
	
	public boolean isEditModel() {
		return modelType == ModelType.EDIT;
	}
	
	public boolean isNewModel() {
		return modelType == ModelType.NEW;
	}

	public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
	
	public XhibitApplicationController getXac() {
		return xac;
	}

	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public SittingOnListComplexValue getSitting() {
		return sitting;
	}

	public void setSitting(SittingOnListComplexValue sitting) {
		this.sitting = sitting;
	}
}