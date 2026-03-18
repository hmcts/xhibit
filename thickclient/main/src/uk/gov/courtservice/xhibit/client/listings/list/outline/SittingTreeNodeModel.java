package uk.gov.courtservice.xhibit.client.listings.list.outline;

import org.apache.commons.lang.StringUtils;

import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;

/**
 * Model class for data required for a sitting in the outline control.
 * 
 * @author uphillj
 * 
 * @amend groenm
 * Updated the display method
 *
 */
public class SittingTreeNodeModel extends AbstractTreeNodeModel {

	private static final long serialVersionUID = 1L;
	
	private SittingOnListComplexValue sittingOnList;

	public SittingOnListComplexValue getSittingOnList() {
		return sittingOnList;
	}

	public void setSittingOnList(SittingOnListComplexValue sitting) {
		this.sittingOnList = sitting;
	}

	@Override
	public String getDisplayName() {
		String judgeName = null;
		
		if(sittingOnList.getRefJudge() != null){
			// Use full list title1 if it has been set
			if(StringUtils.isNotBlank(sittingOnList.getRefJudge().getFullListTitle1())){
				judgeName = sittingOnList.getRefJudge().getFullListTitle1();
			}
			// Else concatenate judge type and name
			else{
				judgeName = getFullName(sittingOnList.getRefJudge().getJudgeType(),
										sittingOnList.getRefJudge().getTitle(),
										sittingOnList.getRefJudge().getFirstName(),
										sittingOnList.getRefJudge().getSurname());
			}
		}
		else{
			judgeName = "No judge assigned";
		}

		return judgeName;
	}
	
	private String getFullName(String judgeType, String title, String firstName, String surname) {
		return StringUtils.join(new String[] {judgeType, title, firstName, surname}, ' ');
	}

}
