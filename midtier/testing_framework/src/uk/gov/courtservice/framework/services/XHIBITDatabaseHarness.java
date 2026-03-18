package uk.gov.courtservice.framework.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;

import javax.sql.DataSource;

public class XHIBITDatabaseHarness {

	DataSource dataSource;
	String userName;
	String password;
	
	public XHIBITDatabaseHarness (DataSource dataSource) 
	{
		this.dataSource = dataSource;
	}
	
	public void setContext(String username, String password) {
		this.userName = username;
		this.password = password;
	}
	
	public void doDatabaseUpdate(String sql) {
		Connection conn;
		Statement stmt;
		try {
    		conn = dataSource.getConnection(userName, password);
    		conn.setAutoCommit(true);
    		stmt = conn.createStatement();
    		stmt.executeUpdate(sql);
    		stmt.close();
    		conn.close();
    	} catch (Exception exp) {
    		exp.printStackTrace();
    	   }
	}
	
	public Hashtable doDatabaseQuery(String sql) {
		Connection conn;
		Statement stmt;
		Hashtable results = new Hashtable();
		String columnName="";
		ResultSet resultSet=null;
		ResultSetMetaData md = null;
		int columns=0;
		try {
    		conn = dataSource.getConnection(userName, password);
    		stmt = conn.createStatement();
    		resultSet = stmt.executeQuery(sql);
    		md = resultSet.getMetaData();
    		columns = md.getColumnCount();
    		for(int i=1; i<=columns; i++) {
    			columnName = md.getColumnName(i);
    			resultSet.next();
    			switch ( md.getColumnType(i)) {
    			   case java.sql.Types.INTEGER: results.put(columnName,new Integer(resultSet.getInt(i)));break;
    			   case java.sql.Types.DATE: results.put(columnName,resultSet.getDate(i));break;
    			   case java.sql.Types.VARCHAR: results.put(columnName,resultSet.getString(i));break;
    			   case java.sql.Types.NUMERIC: results.put(columnName,new Integer(resultSet.getInt(i)));break;
    			}
    		}
    		stmt.close();
    		conn.close();
    	} catch (Exception exp) {
    		exp.printStackTrace();
    	  }
    	return results;
	}
	
	
	public void doDatabaseDeletion(String sql) {
		Connection conn;
		Statement stmt;
		try {
    		conn = dataSource.getConnection(userName, password);
    		stmt = conn.createStatement();
    		stmt.execute(sql);
    		stmt.close();
    		conn.close();
    	} catch (Exception exp) {
    		exp.printStackTrace();
    	   }
	}
    
    public int getNextSquenceVal (String sequenceName)
    {
        String sql = "select " + sequenceName + ".NEXTVAL from dual";
        
        Hashtable set = doDatabaseQuery(sql);
        
        return (Integer)(set.get("NEXTVAL"));
    }
	
	public int createDefendantAddress() {
		int id=0;
        
		 try {
		     id = getNextSquenceVal("xhb_address_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
        String sqlString = "insert into xhb_address (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4," + "" +
                " TOWN, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) " +
                "values(";
        sqlString = sqlString+id+",'Junit_Address1','Junit_Address2','Junit_Address3','Junit_Address4','Junit_Town','Junit_Country',sysdate,sysdate,'JUNIT','JUNIT',1)";	
		this.doDatabaseUpdate(sqlString);
		return id;		
	}
	
	public void removeDefendantAddress(int address_Id) {
		String sqlString = "delete from xhb_address where address_id=" +address_Id;
		this.doDatabaseUpdate(sqlString);
	}
	
	public int createDefendant(int courtId, int address_id) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_defendant_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
         
         String sqlString = "insert into xhb_defendant (DEFENDANT_ID, CREST_DEFENDANT_ID, FIRST_NAME, " +
              "MIDDLE_NAME, SURNAME, INITIALS,GENDER, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, " + 
              "LAST_UPDATED_BY, VERSION, ADDRESS_ID, COURT_ID) values(";
         sqlString = sqlString+id+","+id+",'JUNIT','JUNIT','JUNIT','JJ',1,sysdate,sysdate,'JUNIT','JUNIT',1,"+address_id+","+courtId+" )";   
        this.doDatabaseUpdate(sqlString);
		return id;		
	}
	
	public int createSitting(int courtRoomId, int courtSiteId) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_sitting_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
        String sqlString = "insert into xhb_sitting (SITTING_ID, SITTING_SEQUENCE_NO, SITTING_TIME, IS_FLOATING, COURT_ROOM_ID, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)  values(";
        sqlString = sqlString+id+",1,sysdate-2,0,"+courtRoomId+","+courtSiteId+",sysdate,sysdate,'JUNIT','JUNIT',1)";	
		this.doDatabaseUpdate(sqlString);
		return id;		
	}
	
	public void removeSitting(int sittingId) {
		String sqlString = "delete from xhb_sitting where sitting_id=" +sittingId;
		this.doDatabaseUpdate(sqlString);
	}
	
	public int createScheduledHearing(int hearingId, int sittingId) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_scheduled_hearing_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
         String sqlString = "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID, SEQUENCE_NO, NOT_BEFORE_TIME, ORIGINAL_TIME, LISTING_NOTE, HEARING_PROGRESS, SITTING_ID, HEARING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, END_TIME, START_TIME, DATE_OF_HEARING, IS_CASE_ACTIVE)  values(";
         sqlString = sqlString+id+",1,sysdate+20,sysdate,'JUNIT',0,"+sittingId+","+hearingId+",sysdate,sysdate,'JUNIT','JUNIT',1,sysdate,sysdate-10,sysdate,'Y')";	
		this.doDatabaseUpdate(sqlString);
		return id;		
	}
	
	public void removeScheduledHearing(int schHearingId) {
		String sqlString = "delete from xhb_scheduled_hearing where scheduled_hearing_id=";
		
		sqlString = sqlString+schHearingId;
		this.doDatabaseUpdate(sqlString);
	}
	
	public int createCharge(int caseId) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_charge_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
        String sqlString = "insert into xhb_charge (CHARGE_ID, CHARGE_TYPE, CASE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, OBS_IND) values(";
        sqlString = sqlString+id+",'S',"+caseId+",sysdate,sysdate,'JUNIT','JUNIT',1,'N')";	
		this.doDatabaseUpdate(sqlString);
		return id;		
	}
	
	public void removeCharge(int caseId) {
		String sqlString = "delete from xhb_charge where charge_id=" +caseId; 
		this.doDatabaseUpdate(sqlString);
	}
	
	public int createHearing(int courtId, int caseId) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_hearing_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
        String sqlString = "insert into xhb_hearing (HEARING_ID, CASE_ID, REF_HEARING_TYPE_ID, " +
               "LAST_UPDATE_DATE, CREATION_DATE, LAST_UPDATED_BY, CREATED_BY, VERSION, COURT_ID, " +
               "MP_HEARING_TYPE, HEARING_START_DATE, HEARING_END_DATE) values(";
        sqlString = sqlString+id+","+caseId+",18234,sysdate,sysdate,'JUNIT','JUNIT',4,"+courtId+",'P',sysdate-10,sysdate-2)";   
        this.doDatabaseUpdate(sqlString);
		return id;		
	}
	
	public void removeHearing(int hearingId) {
		String sqlString = "delete from xhb_hearing where hearing_id=" +hearingId;
		this.doDatabaseUpdate(sqlString);
	}
	
	public void removeDefendant(int defend_Id) {
		String sqlString = "delete from xhb_defendant where defendant_id=" +defend_Id;
		this.doDatabaseUpdate(sqlString);
	}
	
	public int createDefendantOnCase(int caseId, int defendantId) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_defendant_on_case_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
         
        String sqlString = "insert into xhb_defendant_on_case (DEFENDANT_ON_CASE_ID, CASE_ID, DEFENDANT_ID, " +
               "LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, DEFENDANT_NUMBER, " +
               "DATE_OF_COMMITTAL )  values(";
		sqlString = sqlString+id+","+caseId+","+defendantId+",sysdate,sysdate,'JUNIT','JUNIT',5,1,sysdate-10)";	
		this.doDatabaseUpdate(sqlString);
		return id;
		
	}
	
	public void removeDefendantOnCase(int def_case_Id) {
		String sqlString = "delete from xhb_defendant_on_case where defendant_on_case_id=" +def_case_Id;
		this.doDatabaseUpdate(sqlString);
	}
	
	public void removeCase(int case_Id) {
		String sqlString = "delete from xhb_case where case_id=" +case_Id;
		this.doDatabaseUpdate(sqlString);
	}
	
	public int createCase(int courtId, int caseNumber, String caseType) {
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_case_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
        String sqlString = "insert into xhb_case (CASE_ID, CASE_NUMBER, CASE_TYPE, COURT_ID, LAST_UPDATE_DATE, " +
                   "CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) values(";
		sqlString = sqlString+id+","+caseNumber+",'"+caseType+"',"+courtId+",sysdate,sysdate,'JUNIT','JUNIT',1 )"; 
		this.doDatabaseUpdate(sqlString);
		return id;
		
	}
	
	public int createDisposal(int defendantId) {
		String refDisposalId="200"; /* Total Order */
		String category="None";
		int id=0;
		
		 try {
		     id = getNextSquenceVal("xhb_disposal_seq");
		 } catch (Exception exp) {
			  exp.printStackTrace();
		    }
        String sqlString = "insert into xhb_disposal (DISPOSAL_ID, DEF_ON_CASE_OR_OFFENCE, FREETEXT, " 
            + "DATE_TIME, DEFENDANT_ON_CASE_ID, REF_DISPOSAL_ID, VERSION, CATEGORY, LAST_UPDATED_BY, "
            + "CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) values(";
        sqlString = sqlString+id+",'c','JUNIT',sysdate,"+defendantId+","+refDisposalId+",1,'"+category+"','JUNIT','JUNIT',sysdate,sysdate)";
		this.doDatabaseUpdate(sqlString);
		return id;	
	}
	
	public void removeOrder(int order_Id) {
		String sqlString = "delete from xhb_order where order_id=" +order_Id;
		this.doDatabaseUpdate(sqlString);
		}
	
	public void close() {
		dataSource=null;
	}
		
	public void removeDisposal(int disposal_Id) {
		String sqlString = "delete from xhb_disposal where disposal_id=" +disposal_Id;;
		this.doDatabaseUpdate(sqlString);
		
    }
	
}
