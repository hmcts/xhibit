update cji_document_type
set    stylesheet_name = substr(stylesheet_name,1,instr(stylesheet_name,'v2')-1)||decode(trim(internal_code),'SR','v2-5.xsl','AR','v2-4.xsl','TR','v2-3.xsl')
where  internal_code in ('SR','AR','TR');

update cji_document_type
set    schema_name = substr(schema_name,1,instr(schema_name,'v4-5')-1)||'v5-0.xsd'
where  internal_code not in (
    select internal_code
    from   cji_document_type
    where upper(external_name) = 'UNKNOWN'
);

commit;
