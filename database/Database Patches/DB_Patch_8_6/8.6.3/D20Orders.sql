insert into xhb_order_type (order_type_id, code, description, ref_disposal_type_id) values(23, 'D20','D20', null);
commit;

insert into xhb_order_template (order_template_id, display_transform_name, narrative_template_name, editor_template_name, order_type_id, obs_ind) values (23, '/metadata/OrderFOPTransform.xslt','/metadata/D20_Narrative.xml','/metadata/D20OrderTemplate.xml',(select order_type_id from xhb_order_type where code='D20'),null);
commit;