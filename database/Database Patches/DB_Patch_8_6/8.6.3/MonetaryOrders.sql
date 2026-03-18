insert into xhb_order_type (order_type_id, code, description, ref_disposal_type_id) values(22, 'MO','Monetary Order', null);
commit;

insert into xhb_order_template (order_template_id, display_transform_name, narrative_template_name, editor_template_name, order_type_id, obs_ind) values (22, '/metadata/OrderFOPTransform.xslt','/metadata/MonetaryOrder_Narrative.xml','/metadata/MonetaryOrderTemplate.xml',(select order_type_id from xhb_order_type where code='MO'),null);
commit;