package uk.gov.courtservice.xhibit.client.results.verdicts;

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

public class VerdictControllerModel {
    private ApplicationCaseModel acm = null;

    private ChargeCompositeValue ccv = null;

    private VerdictIndictmentTableModel indictmentTableModel = null;
    private VerdictFilterModel verdictFilterModel = null;
    private VerdictFilterSelectionModel verdictFilterSelectionModel = null;
    
    private int selectedChargeType = -1;

    private Collection indictmentVerdictRefData = null;

    private Calendar verdictDate = null;

    private int[] selectedIndictments;

    private Integer verdictId;
    private String verdictDescription;
    private String verdictCode;
    private Integer altOffenceId;
    private String altOffenceCode;
    private String altOffenceDesc;
    private boolean dateRangeError = false;
    private boolean resetFilters = false;
    private PropertyChangeSupport pcs;

    public VerdictControllerModel() {
    	pcs = new PropertyChangeSupport(this);
    }

    public ChargeCompositeValue getChargeCompositeValue() {
        return ccv;
    }

    public void setChargeCompositeValue(ChargeCompositeValue ccv) {
        this.ccv = ccv;
    }

    public VerdictIndictmentTableModel getVerdictIndictmentTableModel() {
        return indictmentTableModel;
    }

    public void setVerdictIndictmentTableModel(VerdictIndictmentTableModel pitm) {
        this.indictmentTableModel = pitm;
    }
    
    public VerdictFilterModel getVerdictFilterModel() {
    	return verdictFilterModel;
    }
    
    public void setVerdictFilterModel(VerdictFilterModel verdictFilterModel) {
    	this.verdictFilterModel = verdictFilterModel;
    }
    
    public VerdictFilterSelectionModel getVerdictFilterSelectionModel() {
    	return verdictFilterSelectionModel;
    }
    
    public void setVerdictFilterSelectionModel(VerdictFilterSelectionModel verdictFilterSelectionModel) {
    	this.verdictFilterSelectionModel = verdictFilterSelectionModel;
    }

    public int getSelectedChargeType() {
        return selectedChargeType;
    }

    public void setSelectedChargeType(int selectedChargeType) {
        this.selectedChargeType = selectedChargeType;
    }

    public Collection getIndictmentVerdictRefData() {
        return indictmentVerdictRefData;
    }

    public void setIndictmentVerdictRefData(Collection col) {
        this.indictmentVerdictRefData = col;
    }

    public Calendar getVerdictDate() {
        return verdictDate;
    }

    public void setVerdictDate(Calendar cal) {
        this.verdictDate = cal;
    }

    public int[] getSelectedIndictments() {
        return selectedIndictments;
    }

    public void setSelectedIndictments(int[] selectedIndictments) {
        this.selectedIndictments = selectedIndictments;
    }

    public Integer getVerdictId() {
        return verdictId;
    }

    public void setVerdictId(Integer verdictId) {
        this.verdictId = verdictId;
    }

    public String getVerdictCode() {
        return verdictCode;
    }

    public void setVerdictCode(String verdictCode) {
        this.verdictCode = verdictCode;
    }

    public String getVerdictDescription() {
        return verdictDescription;
    }

    public void setVerdictDescription(String verdictDescription) {
        this.verdictDescription = verdictDescription;
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

}