/*
---------------------------------------------------
-- Static Data for XHB_ORDER_TYPE
---------------------------------------------------
*/
insert into xhb_order_type (ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID)
values (10, 'SUS', 'Suspended Sentence Order (6057)', null);

insert into xhb_order_type (ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID)
values (11, 'IO5035C', 'Imprisonment Order(5035C)', null);

insert into xhb_order_type (ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID)
values (12, 'YO5044C', 'Young Offenders Order (5044C)', null);

insert into xhb_order_type (ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID)
values (13, 'YO5044D', 'Young Offenders Order (5044D)', null);

insert into xhb_order_type (ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID)
values (14, 'BWA', 'Warrant After Failure to Attend (5061A)', null);

insert into xhb_order_type (ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID)
values (15, 'BWC', 'Warrant After Failure to Comply (5061B)', null);


/*
---------------------------------------------------
-- Static Data for XHB_ORDER_TEMPLATE
---------------------------------------------------
*/
insert into xhb_order_template (ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID )
values (10, '/metadata/OrderFOPTransform.xslt', '/metadata/SuspendedSentenceOrder_Narrative.xml', '/metadata/SuspendedSentenceOrderTemplate.xml', 10);

insert into xhb_order_template (ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID )
values (11, '/metadata/OrderFOPTransform.xslt', '/metadata/ImprisonmentOrder5035C_Narrative.xml', '/metadata/ImprisonmentOrder5035CTemplate.xml', 11);

insert into xhb_order_template (ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID )
values (12, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder5044C_Narrative.xml', '/metadata/YoungOffendersOrder5044CTemplate.xml', 12);

insert into xhb_order_template (ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID )
values (13, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder5044D_Narrative.xml', '/metadata/YoungOffendersOrder5044DTemplate.xml', 13);

insert into xhb_order_template (ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID )
values (14, '/metadata/OrderFOPTransform.xslt', '/metadata/WarrantAfterFailureOrder5061_Narrative.xml', '/metadata/WarrantAfterFailureOrder5061Template.xml', 14);

insert into xhb_order_template (ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID )
values (15, '/metadata/OrderFOPTransform.xslt', '/metadata/WarrantAfterFailureOrder5061_Narrative.xml', '/metadata/WarrantAfterFailureOrder5061Template.xml', 15);

/*
---------------------------------------------------
-- Static Data for XHB_ORDER_TYPEMAPPING
---------------------------------------------------
*/
insert into xhb_order_type_mapping (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY)
values (10, 10, 10 );

insert into xhb_order_type_mapping (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY)
values (11, 11, null );

insert into xhb_order_type_mapping (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY)
values (12, 12, null);

insert into xhb_order_type_mapping (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY)
values (13, 13, null);

insert into xhb_order_type_mapping (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY)
values (14, 14, null);

insert into xhb_order_type_mapping (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY)
values (15, 15, null);

Commit;
/
