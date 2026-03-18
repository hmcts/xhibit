/************************************************************
*  CCN1380 - Related Changes - Kelvin Davies - 22/05/2009    *
************************************************************/

-- Alter xhb_def_on_case audit table
ALTER TABLE aud_Defendant_On_case 
      ADD first_fixed_trial DATE
      Add first_hearing_type VARCHAR2(10);

-----------------------------------------------------------------

--Alter xhb_def_on_case table to include new column
ALTER TABLE xhb_Defendant_On_case
      ADD first_fixed_trial DATE
      Add first_hearing_type VARCHAR2(10);
      
      -----------------------------------------------------------------
      
--Alter xhb_def_on_case triggers
@XHB_DEFENDANTONCASE_BUR_TR


