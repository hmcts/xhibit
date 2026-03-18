--ctx-2080
INSERT INTO WMB_MESSAGE_ROUTE (SCHEMA_NAME
                              ,TABLE_NAME
                              ,COLUMN_NAME
                              ,COLUMN_VALUE
                              ,OPERATION
                              ,QUEUE_MANAGER_NAME
                              ,QUEUE_NAME
                              ,MAP_NAME
                              ,LAST_UPDATE_DATE
                              ,CREATION_DATE
                              ,CREATED_BY
                              ,LAST_UPDATED_BY
                              ,VERSION)
VALUES   ('XHIBIT'
         ,'XHB_DEFENDANT_ON_CASE'
         ,'COA_STATUS'
         ,'E'
         ,'INSERT'
         ,NULL
         ,'TRIGGER.IN'
         ,'Exp_Res_CaseDef_Trigger_COA'
         ,SYSDATE
         ,SYSDATE
         ,'XHIBIT'
         ,'XHIBIT'
         ,1);
         
INSERT INTO WMB_MESSAGE_ROUTE (SCHEMA_NAME
                              ,TABLE_NAME
                              ,COLUMN_NAME
                              ,COLUMN_VALUE
                              ,OPERATION
                              ,QUEUE_MANAGER_NAME
                              ,QUEUE_NAME
                              ,MAP_NAME
                              ,LAST_UPDATE_DATE
                              ,CREATION_DATE
                              ,CREATED_BY
                              ,LAST_UPDATED_BY
                              ,VERSION)
VALUES   ('XHIBIT'
         ,'XHB_DEFENDANT_ON_CASE'
         ,'COA_STATUS'
         ,'E'
         ,'UPDATE'
         ,NULL
         ,'TRIGGER.IN'
         ,'Exp_Res_CaseDef_Trigger_COA'
         ,SYSDATE
         ,SYSDATE
         ,'XHIBIT'
         ,'XHIBIT'
         ,1);         