insert into xhb_order_type (order_type_id, code, description, ref_disposal_type_id) values(26, 'BCBCase','Bail Conditions for B Cases', null);
commit;

insert into xhb_order_template (order_template_id, display_transform_name, narrative_template_name, editor_template_name, order_type_id, obs_ind) values (26, '/metadata/OrderFOPTransform.xslt','/metadata/BailOrder_Narrative.xml','/metadata/BailOrderTemplateBCase.xml',(select order_type_id from xhb_order_type where code='BCBCase'),null);
commit;