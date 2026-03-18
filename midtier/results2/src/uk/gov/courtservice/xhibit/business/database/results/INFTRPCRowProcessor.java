package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCMainValue;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCReport;

public class INFTRPCRowProcessor extends AbstractRowProcessor {
	
private final INFTRPCReport inftrpcList = new INFTRPCReport ();

public INFTRPCReport getINFTRPCMainValues(){
	return inftrpcList;
}

	
	@Override	
	public void processRow(Row row){
		INFTRPCMainValue ov = new INFTRPCMainValue();
		
		ov.setPnumberListedTrials(row.getString("Pnumber_Listed_Trials"));
		ov.setQnumberEffectiveTrials(row.getString("Qnumber_Effective_Trials"));
		ov.setRnumberCrackedTrials(row.getString("Rnumber_Cracked_Trials"));
		ov.setSnumberIneffectiveListings(row.getString("Snumber_Ineffective_Listings"));
		ov.setTnumberDisposedTrials(row.getString("Tnumber_Disposed_Trials"));		
		ov.setAreasonsCrackedTrials(row.getString("Areasons_Cracked_Trials"));
		ov.setBreasonsCrackedTrials(row.getString("Breasons_Cracked_Trials"));
		ov.setCreasonsCrackedTrials(row.getString("Creasons_Cracked_Trials"));
		ov.setDreasonsCrackedTrials(row.getString("Dreasons_Cracked_Trials"));
		ov.setEreasonsCrackedTrials(row.getString("Ereasons_Cracked_Trials"));
		ov.setFreasonsCrackedTrials(row.getString("Freasons_Cracked_Trials"));
		ov.setGreasonsCrackedTrials(row.getString("Greasons_Cracked_Trials"));
		ov.setHreasonsCrackedTrials(row.getString("Hreasons_Cracked_Trials"));
		ov.setIreasonsCrackedTrials(row.getString("Ireasons_Cracked_Trials"));
		ov.setJreasonsCrackedTrials(row.getString("Jreasons_Cracked_Trials"));
		ov.setKreasonsCrackedTrials(row.getString("Kreasons_Cracked_Trials"));
		ov.setLreasonsCrackedTrials(row.getString("Lreasons_Cracked_Trials"));
		ov.setM1Ineffective(row.getString("M1_Ineffective"));
		ov.setM2Ineffective(row.getString("M2_Ineffective"));
		ov.setM3Ineffective(row.getString("M3_Ineffective"));
		ov.setN1Ineffective(row.getString("N1_Ineffective"));
		ov.setN2Ineffective(row.getString("N2_Ineffective"));
		ov.setN3Ineffective(row.getString("N3_Ineffective"));
		ov.setO1Ineffective(row.getString("O1_Ineffective"));
		ov.setO2Ineffective(row.getString("O2_Ineffective"));
		ov.setPineffective(row.getString("Pineffective"));
		ov.setQ1Ineffective(row.getString("Q1_Ineffective"));
		ov.setQ2Ineffective(row.getString("Q2_Ineffective"));
		ov.setQ3Ineffective(row.getString("Q3_Ineffective"));
		ov.setRineffective(row.getString("Rineffective"));
		ov.setS1Ineffective(row.getString("S1_Ineffective"));
		ov.setS2Ineffective(row.getString("S2_Ineffective"));
		ov.setS3Ineffective(row.getString("S3_Ineffective"));
		ov.setS4Ineffective(row.getString("S4_Ineffective"));
		ov.setTineffective(row.getString("Tineffective"));
		ov.setU1Ineffective(row.getString("U1_Ineffective"));
		ov.setU2Ineffective(row.getString("U2_Ineffective"));
		ov.setVineffective(row.getString("Vineffective"));
		ov.setW1Ineffective(row.getString("W1_Ineffective"));
		ov.setW2Ineffective(row.getString("W2_Ineffective"));
		ov.setW3Ineffective(row.getString("W3_Ineffective"));
		ov.setW4Ineffective(row.getString("W4_Ineffective"));
		ov.setW5Ineffective(row.getString("W5_Ineffective"));
		ov.setXineffective(row.getString("Xineffective"));
		ov.setYineffective(row.getString("Yineffective"));
		ov.setZineffective(row.getString("Zineffective"));		
		ov.setBroadCrackedTrial(row.getString("Broad_Cracked_Trial"));
		ov.setRnumberCrackedTrialsPerc(row.getString("Rnumber_Cracked_Trials_Perc"));
		ov.setQnumberEffectiveTrialsPerc(row.getString("Qnumber_Effective_Trials_Perc"));
		ov.setSnumberIneffectivePerc(row.getString("Snumber_Ineffective_Perc"));
		ov.setAreasonsCrackedTrialsPerc(row.getString("Areasons_Cracked_Trials_Perc"));
		ov.setBreasonsCrackedTrialsPerc(row.getString("Breasons_Cracked_Trials_Perc"));
		ov.setCreasonsCrackedTrialsPerc(row.getString("Creasons_Cracked_Trials_Perc"));
		ov.setDreasonsCrackedTrialsPerc(row.getString("Dreasons_Cracked_Trials_Perc"));
		ov.setEreasonsCrackedTrialsPerc(row.getString("Ereasons_Cracked_Trials_Perc"));
		ov.setFreasonsCrackedTrialsPerc(row.getString("Freasons_Cracked_Trials_Perc"));
		ov.setGreasonsCrackedTrialsPerc(row.getString("Greasons_Cracked_Trials_Perc"));
		ov.setHreasonsCrackedTrialsPerc(row.getString("Hreasons_Cracked_Trials_Perc"));
		ov.setIreasonsCrackedTrialsPerc(row.getString("Ireasons_Cracked_Trials_Perc"));
		ov.setJreasonsCrackedTrialsPerc(row.getString("Jreasons_Cracked_Trials_Perc"));
		ov.setKreasonsCrackedTrialsPerc(row.getString("Kreasons_Cracked_Trials_Perc"));
		ov.setLreasonsCrackedTrialsPerc(row.getString("Lreasons_Cracked_Trials_Perc"));	
		ov.setAcrackedProsecution(row.getString("Acracked_Prosecution"));
		ov.setAcrackedProsecutionPerc(row.getString("Acracked_Prosecution_Perc"));
		ov.setBcrackedDefendant(row.getString("Bcracked_Defendant"));
		ov.setBcrackedDefendantPerc(row.getString("Bcracked_Defendant_Perc"));
		ov.setM1IneffectivePerc(row.getString("M1_Ineffective_Perc"));
		ov.setM2IneffectivePerc(row.getString("M2_Ineffective_Perc"));
		ov.setM3IneffectivePerc(row.getString("M3_Ineffective_Perc"));
		ov.setN1IneffectivePerc(row.getString("N1_Ineffective_Perc"));
		ov.setN2IneffectivePerc(row.getString("N2_Ineffective_Perc"));
		ov.setN3IneffectivePerc(row.getString("N3_Ineffective_Perc"));
		ov.setO1IneffectivePerc(row.getString("O1_Ineffective_Perc"));
		ov.setO2IneffectivePerc(row.getString("O2_Ineffective_Perc"));
		ov.setPineffectivePerc(row.getString("Pineffective_Perc"));
		ov.setQ1IneffectivePerc(row.getString("Q1_Ineffective_Perc"));
		ov.setQ2IneffectivePerc(row.getString("Q2_Ineffective_Perc"));
		ov.setQ3IneffectivePerc(row.getString("Q3_Ineffective_Perc"));
		ov.setRineffectivePerc(row.getString("Rineffective_Perc"));
		ov.setS1IneffectivePerc(row.getString("S1_Ineffective_Perc"));
		ov.setS2IneffectivePerc(row.getString("S2_Ineffective_Perc"));
		ov.setS3IneffectivePerc(row.getString("S3_Ineffective_Perc"));
		ov.setS4IneffectivePerc(row.getString("S4_Ineffective_Perc"));
		ov.setTineffectivePerc(row.getString("Tineffective_Perc"));
		ov.setU1IneffectivePerc(row.getString("U1_Ineffective_Perc"));
		ov.setU2IneffectivePerc(row.getString("U2_Ineffective_Perc"));
		ov.setVineffectivePerc(row.getString("Vineffective_Perc"));
		ov.setW1IneffectivePerc(row.getString("W1_Ineffective_Perc"));
		ov.setW2IneffectivePerc(row.getString("W2_Ineffective_Perc"));
		ov.setW3IneffectivePerc(row.getString("W3_Ineffective_Perc"));
		ov.setW4IneffectivePerc(row.getString("W4_Ineffective_Perc"));
		ov.setW5IneffectivePerc(row.getString("W5_Ineffective_Perc"));
		ov.setXineffectivePerc(row.getString("Xineffective_Perc"));
		ov.setYineffectivePerc(row.getString("Yineffective_Perc"));
		ov.setZineffectivePerc(row.getString("Zineffective_Perc"));
		ov.setCalcmonth(row.getString("calcmonth"));
		

		inftrpcList.getInftrpcMainValues().add(ov);
	}
}








	

















  









