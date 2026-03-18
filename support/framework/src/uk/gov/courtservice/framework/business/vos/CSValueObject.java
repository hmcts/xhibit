package uk.gov.courtservice.framework.business.vos;

import java.io.Serializable;

public interface CSValueObject extends Serializable {

    // these might be not needed if value objects just been used between
    // business and web / client
    // public Integer getPrimaryKey();
    // public void setPrimaryKey(Integer primaryKey);
    public void setUpdateCount(int updateCount);

    public int getUpdateCount();

    public Integer getVersion();

    public Integer getId();
}