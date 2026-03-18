package uk.gov.courtservice.xhibit.business.entities.shjudge;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface ShJudge extends CSEntityLocal {
    public Integer getShJudgeId();

    public void setDeputyHcj(String deputyHcj);

    public String getDeputyHcj();

    public void setRefJudgeId(Integer refJudgeId);

    public Integer getRefJudgeId();

    public void setShAttendeeId(Integer shAttendeeId);

    public Integer getShAttendeeId();
}