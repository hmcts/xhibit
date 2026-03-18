

/*  EXI_PROPERTY  */



INSERT INTO EXI_PROPERTY ( PROPERTY_TYPE, PROPERTY_CODE, DECODE_PROPERTY,PROPERTY_TITLE ) VALUES ('JMS_MESSAGE_PROPERTIES', 'MAX TRIES', '3', 'Global for maximum number of tries used exi_jms_message_pkg'); 
INSERT INTO EXI_PROPERTY ( PROPERTY_TYPE, PROPERTY_CODE, DECODE_PROPERTY,PROPERTY_TITLE ) VALUES ('JMS_MESSAGE_PROPERTIES', 'BULK COUNT', '10', 'Global for number of rows to process in bulk by exi_jms_message_pkg'); 
INSERT INTO EXI_PROPERTY ( PROPERTY_TYPE, PROPERTY_CODE, DECODE_PROPERTY,PROPERTY_TITLE ) VALUES ('JMS_MESSAGE_PROPERTIES', 'SLEEP COUNT', '0', 'Global for number of seconds to sleep between tries by exi_jms_message_pkg'); 


/*  EXI_JMS_PROPERTY  */


INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBMessageIdentifier', NULL, 'ExissMessageBuilder'); 
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBRequestingSystemName', 'Xhibit Web Services', 'ExissMessageBuilder'); 
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBRequestingSystemOrgUnitCode', 'XWS', 'ExissMessageBuilder'); 
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBRequestingSystemEnvironment', 'Development', 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBMessageTypeType', NULL, 'ExissMessageBuilder'); 
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBMessageTypeVersion', NULL, 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBMessageSchemaNamespace', NULL, 'ExissMessageBuilder'); 
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBMessageSchemaVersion', NULL, 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBOriginatingSystemName', 'Xhibit', 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBOriginatingSystemOrgUnitCode', NULL, 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBOriginatingSystemEnvironment', 'Development', 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBCreationDateTime', NULL, 'ExissMessageBuilder');
INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ('XHBExpiryDateTime', NULL, 'ExissMessageBuilder');


/*  EXI_REF_GROUP  */
	 
 
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITAppealRecordSheet');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITBailOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITBenchWarrant');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITCharges');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITCommunityOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITCommunityPunishmentOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITCommunityPunishmentRehabilitationOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITCommunityRehabilitationOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITCommittalRecordSheet');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITDiscontinuedCase');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITDailyList');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITFirmList');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITImprisonmentOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITNewCase');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITPrisonDailyList');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITRunningList');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITRemandOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITSkeletonSchedule');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITTrialRecordSheet');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITUpdatedCase');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITWarnedList');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITYoungOffenderOrder');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITEvent');
INSERT INTO EXI_REF_GROUP ( EXTERNAL_NAME ) VALUES ('XHIBITPreSentenceReport');


/*  EXI_REF_OPERATION  */


INSERT INTO EXI_REF_OPERATION ( INTERNAL_CODE, INTERNAL_NAME,EXTERNAL_NAME ) VALUES ('CASE', 'Case Event', 'CaseEvent');
INSERT INTO EXI_REF_OPERATION ( INTERNAL_CODE, INTERNAL_NAME,EXTERNAL_NAME ) VALUES ('CRN', 'Charge', 'CRNEvent');
INSERT INTO EXI_REF_OPERATION ( INTERNAL_CODE, INTERNAL_NAME,EXTERNAL_NAME ) VALUES ('DEFENDANT', 'Defendant Event', 'DefendantEvent');
INSERT INTO EXI_REF_OPERATION ( INTERNAL_CODE, INTERNAL_NAME,EXTERNAL_NAME ) VALUES ('DOCUMENT', 'Document', 'DocumentEvent');


/*  EXI_REF_TYPE  */


INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 20, 'UPDCASE', 'Updated Case', NULL, NULL, NULL, NULL, NULL, NULL); 
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 1, 'ARS', 'Appeal Record Sheet', NULL, NULL, NULL, NULL, NULL, NULL); 
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 2, 'BO', 'Bail Order', NULL, NULL, NULL, NULL, NULL, NULL); 
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 3, 'BW', 'Bench Warrant', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 4, 'CHG', 'Charges', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 5, 'CO', 'Community Order', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 6, 'CPO', 'Community Punishment Order', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 7, 'CPRO', 'Community Punishment Rehabilitation Order', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 8, 'CRO', 'Community Rehabilitation Order', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 9, 'CRS', 'Committal (for Sentence) Record Sheet', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 10, 'DISCASE', 'Discontinued Case', NULL, NULL, NULL, NULL, NULL, NULL); 
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 11, 'DL', 'Daily List', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 12, 'FL', 'Firm List', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 13, 'IO', 'Imprisonment Order', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 14, 'NEWCASE', 'New Case', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 15, 'PDL', 'Prison Daily List', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 16, 'RL', 'Running List', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 17, 'RO', 'Remand Order', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 18, 'SS', 'Skeleton Schedule', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 19, 'TRS', 'Trial Record Sheet', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 21, 'WL', 'Warned List', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME,SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION,VERSION ) VALUES (4, 22, 'YOO', 'Young Offender Order', NULL, NULL, NULL, NULL, NULL, NULL);
set escape on
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10100', 23,1, 'Case Called On');                                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10101', 23,1, 'Resume');                                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10105', 23,1, 'Case Closed');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10106', 23,1, '7/14 day orders');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10200', 23,3, 'Defendant not expected/required');                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10201', 23,3, 'Defendant delayed');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10202', 23,3, 'Defendant fails to attend');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10203', 23,3, 'Defendant identified');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10300', 23,3, 'Bail granted');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10301', 23,3, 'Bail granted - duration of trial same conditions');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10302', 23,3, 'Bail granted - duration of trial varied conditions');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10303', 23,3, 'Bail granted - duration of trial short adj');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10304', 23,3, 'Bail rescinded - RIC');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10305', 23,3, 'Remanded in custody');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10306', 23,3, 'Bail within the court building');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10307', 23,3, 'Custody limits extended to [date]');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10308', 23,3, 'Bail as before');                                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10309', 23,3, 'Bail varied');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10400', 23,3, 'Bench Warrant issued backed for bail');                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10401', 23,3, 'Bench Warrant issued not backed for bail');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10402', 23,3, 'Bench Warrant executed');                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10403', 23,3, 'Bench Warrant withdrawn');                                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10404', 23,3, 'Absconding admitted');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10405', 23,3, 'Absconding not admitted');                                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10406', 23,3, 'Absconding not put');                                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10500', 23,1, 'Indictment to be filed');                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10501', 23,1, 'list for Plea \& Direction hearing');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10502', 23,1, 'Certify Readiness for trial');                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10503', 23,1, 'Directions form completed.');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10600', 23,1, 'Jury Sworn');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10601', 23,1, 'Pros opening');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10602', 23,1, 'Pros TV Link');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10603', 23,1, 'Witness sworn');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10604', 23,1, 'Witness released');                                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10605', 23,1, 'Deft sworn');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10606', 23,1, 'Pros close');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10607', 23,3, 'Def opening');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10608', 23,1, 'Pros speech');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10609', 23,3, 'Def speech');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10610', 23,1, 'Summing up');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10611', 23,1, 'Jury Out');                                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10612', 23,1, 'Jury return');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10614', 23,1, 'Jury retire');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10615', 23,1, 'Jury/juror discharged');                                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10617', 23,1, 'Interpreter sworn');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10618', 23,1, 'Witness Continues');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10619', 23,3, 'Def close');                                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10620', 23,1, 'Time estimate supplied');                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10621', 23,1, '#N/A');                                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10700', 23,1, 'Appln stand out');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10701', 23,1, 'Defence appln');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10702', 23,1, 'Pros appln');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10703', 23,1, 'Other appln');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10800', 23,1, 'Resp open');                                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10801', 23,1, 'Witness sworn');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10802', 23,1, 'Witness released');                                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10803', 23,1, 'Resp close');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10804', 23,3, 'Appellant open');                                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10805', 23,3, 'Appellant submissions');                                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10806', 23,3, 'Appellant close');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10807', 23,1, 'Bench retire');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10808', 23,1, 'Judgment');                                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10809', 23,3, 'Appellant identified');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10810', 23,3, 'Appellant delayed');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10811', 23,3, 'Appellant fails to attend');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10812', 23,1, 'Resp TV Link');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10813', 23,1, 'Appellant sworn');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10814', 23,1, 'Interpreter sworn');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('10816', 23,1, 'Witness Continues');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11000', 23,1, 'Reporting Restrictions: order made under section 4(2) of the Contempt of Court Act 1981');                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11001', 23,1, 'Reporting Restrictions: order made under section 11 of the Contempt of Court Act 1981');                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11002', 23,1, 'Reporting Restrictions: order made under section 39 of the Children \& Young Persons Act 1933');                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11003', 23,1, 'Reporting Restrictions: order made under section 4 of the Sexual Offenders (Amendment) Act 1976');                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11004', 23,1, 'Reporting Restrictions: order made under section 2 of the Sexual Offenders (Amendment) Act 1992');                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11005', 23,1, 'Restrictions lifted');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11100', 23,1, 'Case released until later');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11101', 23,1, 'Case adjourned until later');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11102', 23,1, 'Case to be listed for Trial');                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11103', 23,1, 'Case to be listed for Further Mention/PAD');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11104', 23,1, 'Case to be listed for Sentence');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11105', 23,1, 'Case to be listed on date to be fixed');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11106', 23,1, 'Case to be listed on a fixed date');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11107', 23,1, 'Case to be placed in a warned list');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11108', 23,3, 'Adjourned for Pre-Sentence Report');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11109', 23,1, 'Case reserved');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11110', 23,1, 'Case not reserved');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11111', 23,1, '#N/A');                                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11112', 23,3, '#N/A');                                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11200', 23,1, 'Cracked Trial - Pros accept lesser pleas');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11201', 23,1, 'Cracked Trial - Pros offer no evidence');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11202', 23,1, 'Cracked Trial - Deft bound over');                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11203', 23,1, 'Cracked Trial - Deft unfit to plead');                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11204', 23,1, 'Cracked Trial - Deft deceased');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11205', 23,1, 'Cracked Trial - Guilty pleas');                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11206', 23,1, 'Ineffective Trial - Pros witness fail to attend');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11207', 23,1, 'Ineffective Trial - Pros counsel fail to attend');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11208', 23,1, 'Ineffective Trial - Unused material not disclosed');                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11209', 23,1, 'Ineffective Trial - Pros not ready');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11210', 23,1, 'Ineffective Trial - Pros served late A/E');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11211', 23,1, 'Ineffective Trial - Pros increased T/E (insufficient court time)');                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11212', 23,1, 'Ineffective Trial - Deft failed to attend (Bench Wt issued)');                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11213', 23,1, 'Ineffective Trial - Deft not produced from prison');                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11214', 23,1, 'Ineffective Trial - Deft sacked advocates');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11215', 23,1, 'Ineffective Trial - Deft ill');                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11216', 23,1, 'Ineffective Trial - Def witness fail to attend');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11217', 23,1, 'Ineffective Trial - Def counsel fail to attend');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11218', 23,1, 'Ineffective Trial - Def not ready');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11219', 23,1, 'Ineffective Trial - Def served late alibi notice');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11220', 23,1, 'Ineffective Trial - Def asked for additional pros witnesses');                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11221', 23,1, 'Ineffective Trial - Def increased T/E (insufficient court time)');                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11222', 23,1, 'Ineffective Trial - Insufficient jurors');                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11223', 23,1, 'Ineffective Trial - Floater not reached');                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11224', 23,1, 'Ineffective Trial - Insufficient court time');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11225', 23,1, 'Ineffective Trial - Insufficient judge available');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11226', 23,1, 'Ineffective Trial - O/S committals');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11227', 23,1, 'Ineffective Trial - O/S matters elsewhere');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11300', 23,2, 'Autrefois Acquit');                                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11301', 23,2, 'Autrefois Convict');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11302', 23,2, 'Change of Plea: Not guilty to guilty (no jury sworn)');                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11303', 23,2, 'Change of Plea: Not guilty to guilty (after jury sworn)');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11304', 23,2, 'Change of Plea: Guilty to not guilty');                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11305', 23,2, 'Guilty');                                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11306', 23,2, 'Guilty to alternative offence not charged namely');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11307', 23,2, 'Guilty to lesser offence not charged namely');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11308', 23,2, 'Not Guilty');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11309', 23,2, 'No plea taken');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11310', 23,2, 'Other plea');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11311', 23,2, 'Pardon');                                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11312', 23,2, 'Guilty (Summary Offence)');                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11313', 23,2, 'Not Guilty (Summary Offence)');                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11314', 23,2, 'No Plea Taken (Summary Offence)');                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11315', 23,3, 'Admitted (Breach)');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11316', 23,3, 'Not Admitted (Breach)');                                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11400', 23,2, 'Guilty');                                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11401', 23,2, 'Not Guilty');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11402', 23,2, 'Autrefois Acquit');                                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11403', 23,2, 'Autrefois Convict');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11404', 23,2, 'Defendant found under a disability');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11405', 23,2, 'Not guilty but guilty of alternative offence not charged');                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11406', 23,2, 'Not guilty but guilty of alternative offence on judges direction');                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11407', 23,2, 'Guilty (by jury on judges direction)');                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11408', 23,2, 'Not guilty but guilty of lesser offence not charged');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11409', 23,2, 'Not guilty but guilty of lesser offence judges direction');                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11410', 23,2, 'Jury unable to agree');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11411', 23,2, 'Not guilty by reason of insanity');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11412', 23,2, 'Not guilty (by jury on judges direction)');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11413', 23,2, 'Not guilty under section 17 Criminal Justice Act 1967');                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11414', 23,2, 'No verdict (alternative count)');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11415', 23,2, 'Other verdict');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11416', 23,2, 'Jury discharged, unable to agree. Found guilty by another jury');                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11417', 23,2, 'Jury discharged, unable to agree. Found not guilty by another jury');                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11500', 23,2, 'Imprisonment ');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11501', 23,2, 'Detention in a Y.O.I ');                                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11502', 23,2, 'Imprisonment - Minimum imposed after 3 Strikes (Young Offender) ');                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11503', 23,2, 'Consecutive Sentence for Serving Prisoner ');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11504', 23,2, 'Life Imprisonment ');                                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11505', 23,2, 'Life Imprisonment (with minimum period) ');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11506', 23,2, 'Custody for Life ');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11507', 23,2, 'Mandatory Life Sentence for Second Serious Offence ');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11508', 23,2, 'Mandatory Life Sentence for Second Serious Offence (Young Offender) ');                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11509', 23,2, 'Detained During Her Majestys Pleasure ');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11510', 23,2, 'Detention under s.91 PCC (Sentencing) Act 2000 ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11511', 23,2, 'Detention and Training Order ');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11512', 23,2, '1 days imp/detention/Crown Court Cells ');                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11513', 23,2, 'Imprisonment - Minimum imposed after 3 Strikes ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11514', 23,2, 'Extended Sentence of Imprisonment ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11515', 23,2, 'Custodial Sentence with extended licence for sex/violent offenders ');                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11516', 23,2, 'Revocation of Parole Licence ');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11517', 23,2, 'Hospital \& Limitation Direction ');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11518', 23,2, 'Suspended Sentence ');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11519', 23,2, 'Partially Suspended Sentence ');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11520', 23,2, 'Suspended Sentence Supervision Order ');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11600', 23,2, 'Community Punishment Order');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11601', 23,2, 'Community Punishment Order: Drugs Abstinence requirement');                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11602', 23,2, 'General Community Rehabilitation Order');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11603', 23,2, 'Community Rehabilitation Order: Condition of Residence');                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11604', 23,2, 'Community Rehabilitation Order: Hospital Residence');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11605', 23,2, 'COMM REHAB: NON RESIDENT HOSPITAL TREATMENT');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11606', 23,2, 'Community Rehabilitation Order: Day Centre Attendance');                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11607', 23,2, 'COMM REHAB: REQUIREMENT AS TO ACTIVITIES');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11608', 23,2, 'Community Rehabilitation Order: Drug/Alcohol Treatment');                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11609', 23,2, 'Community Rehabilitation Order with Curfew requirements');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11610', 23,2, 'Community Rehabilitation Order: Drugs Abstinence requirement');                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11611', 23,2, 'Community Rehabilitation Order with Exclusion requirements');                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11612', 23,2, 'COMM PUNISHMENT \& GENERAL REHAB ORDER');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11613', 23,2, 'COMM PUNISHMENT \& REHAB:WITH RESIDENCE CONDITION');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11614', 23,2, 'COMMUNITY PUNISHMENT AND REHABILITATION ORDER: HOSPITAL RESIDENCE');                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11615', 23,2, 'COMMUNITY PUNISHMENT AND REHAB ORDER: NON RESIDENT HOSPITAL TREATMENT');                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11616', 23,2, 'COMM PUNISHMENT \& REHAB:WITH DAY CENTRE ATTENDANCE');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11617', 23,2, 'COMM PUNISHMENT \& REHAB:WITH ACTIVITIES REQUIREMENT');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11618', 23,2, 'COMM PUNISHMENT \& REHAB WITH DRUGS/ALCOHOL TREATMENT');                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11619', 23,2, 'COMMUNITY PUNISHMENT AND REHABILITATION ORDER: CURFEW REQUIREMENTS');                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11620', 23,2, 'COMMUNITY PUNISHMENT AND REHABILITATION ORDER: DRUG ABSTINENCE REQ');                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11621', 23,2, 'COMMUNITY PUNISHMENT AND REHABILITATION ORDER: EXCLUSION REQUIREMENTS');                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11622', 23,2, 'COMM PUNISHMENT WITH D/A \& GENERAL REHAB ORDER');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11623', 23,2, 'COMM PUNISHMENT WITH D/A \& REHAB: RESIDENCE CONDITION');                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11624', 23,2, 'COMMUNITY PUNISHMENT WITH D/A \& REHAB ORDER: HOSPITAL RESIDENCE');                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11625', 23,2, 'COMM PUNISHMENT WITH D/A \& REHAB ORDER: NON RESIDENT HOSP TREAT');                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11626', 23,2, 'COMM PUNISHMENT WITH D/A \& REHAB: DAY CENTRE ATTENDANCE');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11627', 23,2, 'COMM PUNISHMENT WITH D/A \& REHAB: ACTIVITIES REQUIREMENT');                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11628', 23,2, 'COMM PUNISHMENT WITH D/A \& REHAB WITH DRUGS/ALCOHOL TREATMENT');                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11629', 23,2, 'COMMUNITY PUNISHMENT WITH D/A \& REHAB ORDER: CURFEW REQ');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11630', 23,2, 'COMMUNITY PUNISHMENT WITH D/A \& REHAB ORDER: EXCLUSION REQ');                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11631', 23,2, 'COMMUNITY PUNISHMENT WITH D/A \& REHAB ORDER: DRUG ABST. REQ');                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11700', 23,2, 'Supervision Order: Comply with directions of supervisor ');                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11701', 23,2, 'Supervision Order: Comply with directions in order ');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11702', 23,2, 'Supervision Order: With Residence Requirement ');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11703', 23,2, 'Supervision Order: Refrain from activities ');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11704', 23,2, 'Supervision Order: s.63 PCC (Sentencing) Act 2000 ');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11705', 23,2, 'Supervision Order: With school attendance requirement ');                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11706', 23,2, 'Supervision Order: With night restriction ');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11707', 23,2, 'Attendance Centre Order ');                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11708', 23,2, 'Action Plan Order ');                                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11709', 23,2, 'Parenting Order ');                                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11800', 23,2, 'Curfew order without electronic monitoring ');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11801', 23,2, 'Curfew order with electronic monitoring ');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11802', 23,2, 'Absolute Discharge ');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11803', 23,2, 'Conditional Discharge Order');                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11804', 23,2, 'Discharged without penalty (Convicted defendant) ');                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11805', 23,2, 'Bind Over with Conditions ');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11806', 23,2, 'Bind Over ');                                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11807', 23,2, 'Bind over to appear for Judgement ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11808', 23,2, 'Bind Over to Appear for Judgment on Date given ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11809', 23,2, 'PARENT/GUARDIAN BIND OVER');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11810', 23,2, 'Anti Social Behaviour Order (sentence)');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11811', 23,2, 'Anti Social behaviour Order (conditional discharge)');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11900', 23,2, 'Drug Treatment and Testing ');                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11901', 23,2, 'Drug Abstinence Order ');                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('11902', 23,2, 'Pre-Sentence Drug Testing order ');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12000', 23,2, 'Hospital Order ');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12001', 23,2, 'Interim Hospital Order: Specified period ');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12002', 23,2, 'Remand for Report on Mental Condition ');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12003', 23,2, 'Remand for Hospital Treatment ');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12004', 23,2, 'Care Order ');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12005', 23,2, 'Care Order with Charge \& Control ');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12006', 23,2, 'Charge \& Control Added to Care Order ');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12007', 23,2, 'Guardianship Order: Authority ');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12008', 23,2, 'Guardianship Order: Relative ');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12009', 23,2, 'Guardianship Order: Other ');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12010', 23,2, 'Restriction Order without time limit ');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12011', 23,2, 'Restriction Order with time limit ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12012', 23,2, 'Restriction Order under s.41 of Mental Health Act 1983 ');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12100', 23,2, 'Fine ');                                                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12101', 23,2, 'Fine with Imprisonment in default ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12102', 23,2, 'Fine with time to pay and with Imprisonment in default ');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12103', 23,2, 'Fine by instalments with Imprisonment in default ');                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12104', 23,2, 'Prosecution Costs ');                                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12105', 23,2, 'Prosecution Costs with time to pay ');                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12106', 23,2, 'Prosecution Costs by Instalments ');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12107', 23,2, 'Prosecution Costs Taxed ');                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12108', 23,2, 'Prosecution Costs Taxed with time to pay ');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12109', 23,2, 'Prosecution Costs Taxed and paid by instalments ');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12110', 23,2, 'Prosecution Costs Taxed or Alternative ');                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12111', 23,2, 'Prosecution Costs Taxed or Alternative with time to pay ');                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12112', 23,2, 'PROSECUTION COSTS TAXED OR ALTERNATIVE BY INSTALMENTS');                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12113', 23,2, 'Prosecution Costs paid from Central Funds ');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12114', 23,2, 'Compensation Order ');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12115', 23,2, 'Compensation Order with time to pay ');                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12116', 23,2, 'Compensation Order by instalments ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12117', 23,2, 'Reward Order ');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12118', 23,2, 'Reparation Order ');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12119', 23,2, 'Restitution Order ');                                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12200', 23,2, 'Excise Penalty ');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12201', 23,2, 'Excise Penalty with Imprisonment in default ');                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12202', 23,2, 'Excise Penalty with time to pay and Imprisonment in default ');                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12203', 23,2, 'Excise Penalty by instalments with Imprisonment in default ');                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12204', 23,2, 'Defendants Costs Order ');                                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12205', 23,2, 'Defendants Costs Order: Crown Ct Taxed ');                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12206', 23,2, 'Defendants Costs Order: Crown \& Mags Ct Taxed ');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12207', 23,2, 'Recovery of Defence Costs Order ');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12208', 23,2, 'No RDCO order made ');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12209', 23,2, 'Outstanding legal Aid Contributions to be paid ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12210', 23,2, 'Remitted LA Contributions ');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12211', 23,2, 'Refunded Legal Aid Contributions ');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12212', 23,2, 'Retained Legal Aid Contributions ');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12213', 23,2, 'Legal Aid Contribution Arrears Remitted ');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12214', 23,2, 'Legal Aid Contribution Arrears to be Paid ');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12215', 23,2, 'Bail Security Forfeited ');                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12300', 23,2, 'Disqualified from Driving ');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12301', 23,2, 'DISQUALIFIED FROM DRIVING UNTIL TEST PASSED');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12302', 23,2, 'Disqualified under S.35(1) Road Traffic Offenders Act 1988 ');                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12303', 23,2, 'DISQUALIFIED FROM DRIVING UNDER S.147 PCC (SENTENCING) ACT 2000');                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12304', 23,2, 'Licence Endorsed ');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12305', 23,2, 'Licence Endorsed with Penalty Points ');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12306', 23,2, 'Notification of Disability to Licensing Authority ');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12307', 23,2, 'NO DISQUALIFICATION: MITIGATING CIRCUMSTANCES S.35(1) RTOA 1988');                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12308', 23,2, 'No Disqualification: Special Reasons S.34(1) RTOA 1988 ');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12400', 23,2, 'ADULT: DISQUALIFICATION FROM WORKING WITH CHILDREN');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12401', 23,2, 'JUVENILE: DISQUALIFICATION FROM WORKING WITH CHILDREN');                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12402', 23,2, 'NO DISQUALIFICATION FROM WORKING WITH CHILDREN');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12403', 23,2, 'Travel Restriction Order ');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12404', 23,2, 'Disqualification under Directors Disqualification Act ');                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12405', 23,2, 'Restriction Order S15 Football Spectators Act 1989 ');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12406', 23,2, 'Football Banning Order ');                                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12407', 23,2, 'Licensed Premises Exclusion Order ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12408', 23,2, 'Football Exclusion Order ');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12409', 23,2, 'Restraining Order Protection from Harassment Act 1997 ');                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12410', 23,2, 'Restraining Order under s.5A Sexual Offenders Act 1997 ');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12411', 23,2, 'Exclusion Order with Electronic Monitoring ');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12412', 23,2, 'Exclusion order without electronic monitoring ');                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12500', 23,2, 'Cancellation of Firearms/Shotgun Certificate ');                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12501', 23,2, 'FORFEITURE OTHER THAN VEHICLE UNDER S.143 PCC (SENTENCING) ACT 2000');                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12502', 23,2, 'Forfeiture of vehicle under s.143 PCC (Sentencing) Act 2000 ');                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12503', 23,2, 'Deprivation of Rights under s.143 PCC (Sentencing) Act 2000 ');                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12504', 23,2, 'Forfeiture under S.52(1) Firearms Act 1968 ');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12505', 23,2, 'Forfeiture under S.27 Misuse of Drugs Act 1971 ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12506', 23,2, 'Forfeiture under S.25 Public Order Act 1986 ');                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12507', 23,2, 'Confiscation Order under S1(5) DTOA 1986 ');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12508', 23,2, 'Confiscation Order under S71 CJA 1988 ');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12509', 23,2, 'Forfeiture under S.24 Forgery and Counterfeiting Act 1981 ');                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12510', 23,2, 'Forfeiture under S.1 Obscene Publications Act 1964 ');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12511', 23,2, 'Confiscation Order under s.2 Drug Trafficking Act 1994 ');                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12512', 23,2, 'Confiscation order under Proceeds of Crime Act 2002');                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12600', 23,2, 'Recommendation for Deportation ');                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12601', 23,2, 'Recommendation for Deportation: Detained ');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12602', 23,2, 'Recommendation for Deportation: Not Detained ');                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12700', 23,2, 'No Order on Breach of Suspended Sentence ');                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12701', 23,2, 'Order Varied: Substitute New period from Today ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12702', 23,2, 'Sentence to take effect unaltered ');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12703', 23,2, 'Sentence to take effect reduced ');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12704', 23,2, 'Suspended Sentence Supervision Order to Continue ');                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12705', 23,2, 'Suspended Sentence Supervision Order Discharged ');                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12706', 23,2, 'No Order on Breach ');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12707', 23,2, 'Supervision Order Discharged ');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12708', 23,2, 'Community Service Order Revoked ');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12709', 23,2, 'Discharge of Probation Order ');                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12710', 23,2, 'Recognisance Estreated: Time to pay ');                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12711', 23,2, 'Recognisance Estreated: Pay by instalments ');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12800', 23,2, 'Count/Indictment to Remain on File ');                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12801', 23,2, 'Count/Indictment Quashed ');                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12802', 23,2, 'Found under Disability: Admit to Hospital ');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12803', 23,2, 'No Separate Penalty ');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12804', 23,2, 'Remitted to Juvenile Court ');                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12805', 23,2, 'Remitted to Magistrates Court ');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12806', 23,2, 'Defendant Deceased: Indictment of no effect ');                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12807', 23,2, 'Defendant Suicide: Indictment of no effect ');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12808', 23,2, 'Not tried: Certified insane before arraignment ');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12809', 23,2, 'FREE TEXT DISPOSAL');                                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12810', 23,2, 'Disposal removed after appeal ');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12900', 23,2, 'TOTAL EFFECTIVE SENTENCE OF IMPRISONMENT/DETENTION');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12901', 23,2, 'TOTAL EFFECTIVE SUSPENDED SENTENCE');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12902', 23,2, 'Total order ');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12903', 23,2, 'Total Fine ');                                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12904', 23,2, 'Total Costs ');                                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12905', 23,2, 'Total Compensation ');                                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12906', 23,2, 'TOTAL MONETARY ORDER (OTHER)');                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12907', 23,2, 'TOTAL PENALTY POINTS');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('12908', 23,2, 'TOTAL PERIOD OF DISQUALIFICATION (DRIVING)');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13000', 23,2, 'Case Dismissed ');                                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13001', 23,2, 'Charges Dismissed: Remaining Charges remitted to Magistrates Court ');                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13002', 23,2, 'Case Discontinued by Prosecution');                                                                                                                                                                
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13003', 23,2, 'Charges Discontd by Prosecution: Rem Charges remitted to Mag Court ');                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13004', 23,2, 'Other sent for trial disposal not catered for');                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13005', 23,2, '#N/A');                                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13010', 23,3, 'Judgment');                                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13011', 23,3, 'Judgment');                                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13200', 23,2, 'CS Compulsory (Unpaid) Work');                                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13201', 23,2, 'CS Participation in Specified Activities');                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13202', 23,2, 'CS Programmes Aimed at Changing Offender Behaviour');                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13203', 23,2, 'CS Prohibition from Certain Activities');                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13204', 23,2, 'CS Curfew');                                                                                                                                                                                       
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13205', 23,2, 'CS Exclusion from Certain Areas');                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13206', 23,2, 'CS Residence Requirement');                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13207', 23,2, 'CS Mental Health Treatment');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13208', 23,2, 'CS Drug Rehabilitation Requirement');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13209', 23,2, 'CS Alcohol Treatment');                                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13210', 23,2, 'CS Supervision Requirement');                                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13211', 23,2, 'CS Attendance Centre Requirements');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13212', 23,2, 'CS Exclusion and Curfew Requirements');                                                                                                                                                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13300', 23,2, 'Suspended Sentence (CJA 2003) (Consecutive)');                                                                                                                                                     
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13301', 23,2, 'Suspended Sentence (CJA 2003) (Concurrent)');                                                                                                                                                      
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13302', 23,2, 'Compulsory (Unpaid) Work');                                                                                                                                                                        
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13303', 23,2, 'Participation in Specified Activities');                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13304', 23,2, 'Programmes Aimed at Changing Offender Behaviour');                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13305', 23,2, 'Prohibition from Certain Activities');                                                                                                                                                             
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13306', 23,2, 'Curfew');                                                                                                                                                                                          
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13307', 23,2, 'Exclusion from Certain Areas');                                                                                                                                                                    
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13308', 23,2, 'Residence Requirement');                                                                                                                                                                           
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13309', 23,2, 'Mental Health Treatment');                                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13310', 23,2, 'Drug Rehabilitation Requirement');                                                                                                                                                                 
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13311', 23,2, 'Alcohol Treatment');                                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13312', 23,2, 'Supervision Requirement');                                                                                                                                                                         
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13313', 23,2, 'Attendance Centre Requirements');                                                                                                                                                                  
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13314', 23,2, 'Exclusion and Curfew Requirements');                                                                                                                                                               
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13400', 23,2, 'Early Release');                                                                                                                                                                                   
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13500', 23,2, 'Dangerous Offenders (Under Age 18)');                                                                                                                                                              
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) VALUES  ('13501', 23,2, 'Dangerous Offenders (Aged 18 and Over)');                            
INSERT INTO EXI_REF_TYPE (internal_code, group_id, operation_id, internal_name) 
  SELECT 'PSR', group_id ,4, 'Pre Sentence Report'
  FROM EXI_REF_GROUP
  WHERE external_name =  'XHIBITPreSentenceReport';
set escape off

/*  EXI_REF_TRACKING_STATUS  */

INSERT INTO EXI_REF_TRACKING_STATUS ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('DISCARDED', 'Discarded'); 
INSERT INTO EXI_REF_TRACKING_STATUS ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ( 'NEW_ITEM', 'New Item');
INSERT INTO EXI_REF_TRACKING_STATUS ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ( 'PROCESSING_MESSAGE', 'Processing Message'); 
INSERT INTO EXI_REF_TRACKING_STATUS ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('SENT_TO_EXISS', 'Sent to ExISS');
INSERT INTO EXI_REF_TRACKING_STATUS ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('SENT_TO_WS_BRIDGE', 'Sent to Web Service Bridge');
INSERT INTO EXI_REF_TRACKING_STATUS ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('UNDELIVERABLE', 'Undeliverable');


/*  EXI_SYS_AUDIT  */ 

INSERT INTO EXI_SYS_AUDIT (table_to_audit, audit_table, auditable) VALUES ('EXI_PROPERTY', 'AUD_PROPERTY', 'Y');


/*  EXI_SYS_USER_INFORMATION  */

INSERT INTO EXI_SYS_USER_INFORMATION (mercator_user_name, connection_pool_user_name) VALUES ( 'MERCATOR', 'EXISS');

/* Swiki requests 27 and 29  */

UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'AR' WHERE INTERNAL_CODE = 'ARS';
UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'CH' WHERE INTERNAL_CODE = 'CHG';
UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'CPR' WHERE INTERNAL_CODE = 'CPRO';
UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'DLP' WHERE INTERNAL_CODE = 'PDL';
UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'SR' WHERE INTERNAL_CODE = 'CRS';
UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'TR' WHERE INTERNAL_CODE = 'TRS';
UPDATE EXI_REF_TYPE SET INTERNAL_CODE = 'YOI' WHERE INTERNAL_CODE = 'YOO';

INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION ) VALUES ( 4, 20, 'WW', 'Witness Warrant Order', NULL, NULL, NULL, NULL, NULL, NULL); 

INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION ) VALUES ( 4, 20, 'MC', 'Memorandum Of Conviction', NULL, NULL, NULL, NULL, NULL, NULL); 

INSERT INTO EXI_REF_TYPE ( OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION ) VALUES ( 4, 20, 'NA', 'Notice Of Appeal', NULL, NULL, NULL, NULL, NULL, NULL); 

/* Wiki request 35 */

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'DL';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'RL';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'WL';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'FL';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'DLP';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'SS';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'TR';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'SR';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'AR';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'BW';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'BO';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'YOI';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'RO';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'IO';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'CO';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'CPO';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'CRO';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'CPR';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'CH';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '4.5'
WHERE INTERNAL_CODE = 'PSR';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.hmcs.gov.uk/schemas/crowncourt/msg', VERSION = '1.1'
WHERE INTERNAL_CODE = 'NEWCASE';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.hmcs.gov.uk/schemas/crowncourt/msg', VERSION = '1.1'
WHERE INTERNAL_CODE = 'UPDCASE';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.hmcs.gov.uk/schemas/crowncourt/msg', VERSION = '1.1'
WHERE INTERNAL_CODE = 'DISCASE';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.hmcs.gov.uk/schemas/crowncourt/msg', VERSION = '1.1'
WHERE INTERNAL_CODE = 'WW';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.hmcs.gov.uk/schemas/crowncourt/msg', VERSION = '1.1'
WHERE INTERNAL_CODE = 'MC';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.hmcs.gov.uk/schemas/crowncourt/msg', VERSION = '1.1'
WHERE INTERNAL_CODE = 'NA';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://schemas.cjse.gov.uk/messages/exception/2006-06', VERSION = '1.0'
WHERE INTERNAL_CODE = 'DELIVERERROR';

UPDATE EXI_REF_TYPE
SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice', VERSION = '1.0'
WHERE GROUP_ID  = (SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME = 'XHIBITEvent');


/*  1669 Changes */

INSERT INTO EXI_REF_GROUP (external_name) VALUES ('DELIVERERROR');

INSERT INTO EXI_REF_OPERATION (internal_code, internal_name, external_name) VALUES ('DELIVERERROR', 'DeliverError', 'DeliverError');

INSERT INTO EXI_REF_TYPE (operation_id, group_id, internal_code, internal_name) 
VALUES ((SELECT operation_id FROM EXI_REF_OPERATION WHERE internal_code = 'DELIVERERROR'), (SELECT group_id FROM EXI_REF_GROUP WHERE external_name = 'DELIVERERROR'), 'DELIVERERROR', 'Deliver Error');

INSERT INTO EXI_TABLE_TIMESTAMP (name, last_updated) VALUES ('EXI_PROPERTY', TRUNC(SYSDATE));

INSERT INTO EXI_JMS_PROPERTY ( NAME, VALUE, MESSAGE_TYPE ) VALUES ( 
'XHBMessageSchemaIdentifier', NULL, 'ExissMessageBuilder');

INSERT INTO EXI_PROPERTY ( PROPERTY_TYPE, PROPERTY_CODE, DECODE_PROPERTY,PROPERTY_TITLE ) VALUES ('JMS_MESSAGE_PROPERTIES', 'DISABLE SCJSE GATEWAY', 'FALSE', 'Global for disabling processing of JMS messages'); 

INSERT INTO EXI_REF_TRACKING_STATUS (INTERNAL_CODE, INTERNAL_NAME, TRACKING_ENABLED)  VALUES ('EXCEPTION', 'A Third Party Agency failed to process this record', 'Y');

INSERT INTO EXI_REF_TRACKING_STATUS (INTERNAL_CODE, INTERNAL_NAME, TRACKING_ENABLED)   VALUES ('SCJSE_MESSAGE_SENT_OK', 'SCJSE Message Successfully sent to SCJSE', 'Y');
 
INSERT INTO EXI_REF_TRACKING_STATUS (INTERNAL_CODE, INTERNAL_NAME, TRACKING_ENABLED)   VALUES ('SCJSE_MESSAGE_SENT_ERROR', 'SCJSE Message not sent to SCJSE and failed with a retriable error', 'Y');
  
INSERT INTO EXI_REF_TRACKING_STATUS (INTERNAL_CODE, INTERNAL_NAME, TRACKING_ENABLED)   VALUES ('SCJSE_MESSAGE_SENT_FATAL', 'SCJSE Message not sent to SCJSE and failed with a fatal error', 'Y');
  
INSERT INTO EXI_REF_TRACKING_STATUS (INTERNAL_CODE, INTERNAL_NAME, TRACKING_ENABLED)   VALUES ('SCJSE_MESSAGE_GDDB_OK', 'SCJSE Message successfully added to GD Gate Database', 'Y');
  
INSERT INTO EXI_REF_TRACKING_STATUS (INTERNAL_CODE, INTERNAL_NAME, TRACKING_ENABLED)   VALUES ('SCJSE_MESSAGE_GDDB_ERROR', 'SCJSE Message unsuccessfully added to GD Gate Database and has failed with an error', 'Y');

UPDATE EXI_JMS_PROPERTY SET VALUE = 'XWS001' WHERE NAME = 'XHBRequestingSystemOrgUnitCode';

INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE, ORG_UNIT_CODE ) VALUES ( '401', 'C00AY00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '402', 'C00BR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '403', 'C00HU00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '404', 'C00BI00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '406', 'C00BN00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '407', 'C00DR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '408', 'C00BT00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '409', 'C00BU00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '410', 'C00CM00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '411', 'C00CR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '412', 'C00CL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '413', 'C00CE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '414', 'C00CF00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '415', 'C00CS00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '416', 'C00CC00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '417', 'C00CO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '418', 'C00CY00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '419', 'C00DE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '420', 'C00DN00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '421', 'C00WO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '422', 'C00DU00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '423', 'C00EX00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '424', 'C00GL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '425', 'C00GR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '426', 'C00IP00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '427', 'C00KT00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '428', 'C00BL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '429', 'C00LE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '430', 'C00LC00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '431', 'C00LW00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '432', 'C00LI00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '433', 'C00LV00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '434', 'C00MA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '435', 'C00MC00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '436', 'C00MM00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '437', 'C00MT00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '438', 'C00MO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '439', 'C00NE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '440', 'C00IL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '441', 'C00NG00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '442', 'C00NO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '443', 'C00NR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '444', 'C00NT00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '445', 'C00OX00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '446', 'C00PL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '447', 'C00PO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '448', 'C00PR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '449', 'C00RE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '450', 'C00ST00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '451', 'C00SH00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '452', 'C00SR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '453', 'C00SN00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '454', 'C00SA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '455', 'C00SF00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '456', 'C00SK00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '457', 'C00SS00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '458', 'C00SD00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '459', 'C00TA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '460', 'C00TE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '461', 'C00BS00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '463', 'C00WR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '464', 'C00MG00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '465', 'C00WI00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '466', 'C00WC00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '467', 'C00YO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '468', 'C00HA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '469', 'C00WG00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '470', 'C00BO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '471', 'C00SW00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '472', 'C00WL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '473', 'C00PE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '474', 'C00GU00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '475', 'C00IS00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '476', 'C00LU00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '477', 'C00TR00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '478', 'C00NI00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '479', 'C00CN00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '480', 'C00SL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '750', 'C00BA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '751', 'C00BF00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '754', 'C00BE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '755', 'C00CA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '756', 'C00CH00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '758', 'C00DO00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '761', 'C00HV00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '762', 'C00HE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '765', 'C00KL00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '767', 'C00KN00');
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '768', 'C00LA00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '772', 'C00SE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '774', 'C00WE00'); 
INSERT INTO EXI_CREST_ORG_UNIT_LOOKUP ( CREST_COURT_CODE,ORG_UNIT_CODE ) VALUES ( '775', 'C00WA00'); 
COMMIT;


/*  Wiki No 38 Changes */


UPDATE EXI_JMS_PROPERTY SET value = 'C000000' WHERE name = 'XHBRequestingSystemOrgUnitCode';
UPDATE EXI_JMS_PROPERTY SET value = 'C000000' WHERE name = 'XHBOriginatingSystemOrgUnitCode';

UPDATE EXI_JMS_PROPERTY SET value = 'CourtServicesHub' WHERE name = 'XHBRequestingSystemName';
UPDATE EXI_JMS_PROPERTY SET value = 'CourtServicesHub' WHERE name = 'XHBOriginatingSystemName';

COMMIT;
