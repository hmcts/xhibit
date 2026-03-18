/* Fixed the inserts to match what was run on all environments; also added a commit */
insert into XHB_ORDER_TEMPLATE(ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, VERSION, 
LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ORDER_TYPE_ID, OBS_IND) values 
(29, '/metadata/OrderFOPTransform.xslt', '/metadata/BreachSuspendedSentence_Narrative.xml', '/metadata/BreachSuspendedSentenceTemplate.xml', 1, 
'XHIBIT', 'XHIBIT', sysdate, sysdate, 29, null);

insert into XHB_ORDER_TEMPLATE(ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, VERSION, 
LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ORDER_TYPE_ID, OBS_IND) values 
(30, '/metadata/OrderFOPTransform.xslt', '/metadata/ActionConditionalDischarge_Narrative.xml', '/metadata/ActionConditionalDischargeTemplate.xml', 1, 
'XHIBIT', 'XHIBIT', sysdate, sysdate, 30, null);

insert into XHB_ORDER_TEMPLATE(ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, VERSION, 
LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ORDER_TYPE_ID, OBS_IND) values 
(31, '/metadata/OrderFOPTransform.xslt', '/metadata/NoticeDefermentSentence_Narrative.xml', '/metadata/NoticeDefermentSentenceTemplate.xml', 1, 
'XHIBIT', 'XHIBIT', sysdate, sysdate, 31, null);

insert into XHB_ORDER_TEMPLATE(ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, VERSION, 
LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ORDER_TYPE_ID, OBS_IND) values 
(32, '/metadata/OrderFOPTransform.xslt', '/metadata/NoticeBreachSuspendedSentence_Narrative.xml', '/metadata/NoticeBreachSuspendedSentenceTemplate.xml', 1, 
'XHIBIT', 'XHIBIT', sysdate, sysdate, 32, null);

insert into XHB_ORDER_TEMPLATE(ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, VERSION, 
LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ORDER_TYPE_ID, OBS_IND) values 
(33, '/metadata/OrderFOPTransform.xslt', '/metadata/BreachConditionalDischarge_Narrative.xml', '/metadata/BreachConditionalDischargeTemplate.xml', 1, 
'XHIBIT', 'XHIBIT', sysdate, sysdate, 33, null);

commit;
/