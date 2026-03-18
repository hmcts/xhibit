package uk.gov.courtservice.xhibit.client.results.pleas;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class PleaControllerModel {
    public static final String ARRAIGNMENT_DATE_PROPERTY = "arraignment";

    private ApplicationCaseModel acm = null;
    private ChargeCompositeValue ccv = null;

    private PleaIndictmentTableModel indictmentTableModel = null;
    private PleaSection41TableModel section41TableModel = null;
    private PleaBreachTableModel pleaBreachTableModel = null;
    private PleaBailActTableModel pleaBailActTableModel = null;

    private PleaFilterModel indictmentFilterModel = null;
    private PleaFilterModel s41FilterModel = null;

    private PleaFilterSelectionModel indictmentFilterSelectionModel = null;
    private PleaFilterSelectionModel s41FilterSelectionModel = null;

    private int selectedChargeType = -1;

    private Collection indictmentPleaRefData = null;
    private Collection section41PleaRefData = null;
    private Collection breachPleaRefData = null;
    private Collection bailActPleaRefData = null;
    
    private Calendar arraignmentDate = null;

    private int[] selectedIndictments;

    private Integer pleaId;
    private String pleaDescription;
    private String pleaCode;
    private Integer altOffenceId;
    private String altOffenceCode;
    private String altOffenceDesc;
    private String otherPleaText;

    private PropertyChangeSupport pcs;
    private boolean dateRangeError = false;
    private boolean resetFilters = false;
    private MultiplePleasPanel multiplePleasPanel;
    
    public PleaControllerModel() {
        pcs = new PropertyChangeSupport(this);
    }

    public ChargeCompositeValue getChargeCompositeValue() {
        return ccv;
    }

    public void setChargeCompositeValue(ChargeCompositeValue ccv) {
        this.ccv = ccv;
    }

    public PleaIndictmentTableModel getPleaIndictmentTableModel() {
        return indictmentTableModel;
    }

    public void setPleaIndictmentTableModel(PleaIndictmentTableModel pitm) {
        this.indictmentTableModel = pitm;
    }

    public PleaSection41TableModel getPleaSection41TableModel() {
        return section41TableModel;
    }

    public void setPleaSection41TableModel(PleaSection41TableModel pstm) {
        this.section41TableModel = pstm;
    }

    public PleaBreachTableModel getPleaBreachTableModel() {
        return pleaBreachTableModel;
    }

    public void setPleaBreachTableModel(PleaBreachTableModel pbtm) {
        this.pleaBreachTableModel = pbtm;
    }
    
    public PleaBailActTableModel getPleaBailActTableModel() {
        return pleaBailActTableModel;
    }

    public void setPleaBailActTableModel(PleaBailActTableModel pbatm) {
        this.pleaBailActTableModel = pbatm;
    }
    public PleaFilterModel getIndictmentFilterModel() {
        return indictmentFilterModel;
    }

    public void setIndictmentFilterModel(PleaFilterModel indictmentFilterModel) {
        this.indictmentFilterModel = indictmentFilterModel;
    }

    public PleaFilterModel getS41FilterModel() {
        return s41FilterModel;
    }

    public void setS41FilterModel(PleaFilterModel s41FilterModel) {
        this.s41FilterModel = s41FilterModel;
    }

    public PleaFilterSelectionModel getIndictmentFilterSelectionModel() {
        return indictmentFilterSelectionModel;
    }

    public void setIndictmentFilterSelectionModel(PleaFilterSelectionModel indictmentFilterSelectionModel) {
        this.indictmentFilterSelectionModel = indictmentFilterSelectionModel;
    }

    public PleaFilterSelectionModel getS41FilterSelectionModel() {
        return s41FilterSelectionModel;
    }

    public void setS41FilterSelectionModel(PleaFilterSelectionModel s41FilterSelectionModel) {
        this.s41FilterSelectionModel = s41FilterSelectionModel;
    }

    public int getSelectedChargeType() {
        return selectedChargeType;
    }

    public void setSelectedChargeType(int selectedChargeType) {
        this.selectedChargeType = selectedChargeType;
    }

    public Collection getIndictmentPleaRefData() {
        return indictmentPleaRefData;
    }

    public void setIndictmentPleaRefData(Collection col) {
        this.indictmentPleaRefData = col;
    }

    public Collection getSection41PleaRefData() {
        return section41PleaRefData;
    }

    public void setSection41PleaRefData(Collection col) {
        this.section41PleaRefData = col;
    }

    public Collection getBreachPleaRefData() {
        return breachPleaRefData;
    }

    public void setBreachPleaRefData(Collection col) {
        this.breachPleaRefData = col;
    }
    
    public Collection getBailActPleaRefData() {
        return bailActPleaRefData;
    }

    public void setBailActPleaRefData(Collection col) {
        this.bailActPleaRefData = col;
    }

    public Calendar getArraignmentDate() {
        return arraignmentDate;
    }

    public void setArraignmentDate(Calendar cal) {
        pcs.firePropertyChange(ARRAIGNMENT_DATE_PROPERTY, arraignmentDate, cal);
        this.arraignmentDate = cal;
    }

    public int[] getSelectedIndictments() {
        return selectedIndictments;
    }

    public void setSelectedIndictments(int[] selectedIndictments) {
        this.selectedIndictments = selectedIndictments;
    }

    public Integer getPleaId() {
        return pleaId;
    }

    public void setPleaId(Integer pleaId) {
        this.pleaId = pleaId;
    }

    public String getPleaCode() {
        return pleaCode;
    }

    public void setPleaCode(String pleaCode) {
        this.pleaCode = pleaCode;
    }

    public String getPleaDescription() {
        return pleaDescription;
    }

    public void setPleaDescription(String pleaDescription) {
        this.pleaDescription = pleaDescription;
    }

    public Integer getAltOffenceId() {
        return altOffenceId;
    }

    public void setAltOffenceId(Integer altOffenceId) {
        this.altOffenceId = altOffenceId;
    }

    public String getAltOffenceDesc() {
        return altOffenceDesc;
    }

    public void setAltOffenceDesc(String altOffenceDesc) {
        this.altOffenceDesc = altOffenceDesc;
    }

    public String getAltOffenceCode() {
        return altOffenceCode;
    }

    public void setAltOffenceCode(String altOffenceCode) {
        this.altOffenceCode = altOffenceCode;
    }

    public String getOtherPleaText() {
        return otherPleaText;
    }

    public void setOtherPleaText(String otherPleaText) {
        this.otherPleaText = otherPleaText;
    }

    public ApplicationCaseModel getACM() {
        return acm;
    }

    public void setACM(ApplicationCaseModel acm) {
        this.acm = acm;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public boolean isDateRangeError() {
        return dateRangeError;
    }

    public void setDateRangeError(boolean error) {
        dateRangeError = error;
    }

    public boolean isResetFilters() {
        return resetFilters;
    }

    public void setResetFilters(boolean resetFilters) {
        this.resetFilters = resetFilters;
    }

    public MultiplePleasPanel getMultiplePleasPanel() {
        return multiplePleasPanel;
    }

    public void setMultiplePleasPanel(MultiplePleasPanel multiplePleasPanel) {
        this.multiplePleasPanel = multiplePleasPanel;
    }
}