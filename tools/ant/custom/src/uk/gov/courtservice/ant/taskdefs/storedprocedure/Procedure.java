package uk.gov.courtservice.ant.taskdefs.storedprocedure;

import java.util.ArrayList;
import java.util.List;

public class Procedure {

    private String procName;

    private List argTypes = new ArrayList();

    public Procedure(String newProcName) {
        procName = newProcName;
    }

    public void addArgument(int sqlType) {
        argTypes.add(new Integer(sqlType));
    }

    public String getCallString() {
        StringBuffer callString = new StringBuffer("{call " + procName + "(?");
        for (int i = 0; i < argTypes.size(); i++) {
            callString.append(",?");
        }
        callString.append(")}");
        return callString.toString();
    }

    public String getTypeArray() {
        String typeArray = argTypes.toString();
        return typeArray.substring(1, typeArray.length() - 1);
    }

    public String getName() {
        return procName;
    }

}