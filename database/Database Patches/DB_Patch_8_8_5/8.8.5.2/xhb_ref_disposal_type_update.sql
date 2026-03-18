MERGE INTO xhb_ref_disposal_type x1 USING (
SELECT 'DET'     disposal_code, 'P' d20_other_sentence, 5 template_version FROM DUAL UNION
SELECT 'DLFMESW' disposal_code, 'A' d20_other_sentence, 1 template_version FROM DUAL UNION
SELECT 'IMPMESW' disposal_code, 'A' d20_other_sentence, 1 template_version FROM DUAL 
) subqry
    ON (x1.disposal_code = subqry.disposal_code AND 
        x1.template_version = subqry.template_version)
    WHEN MATCHED THEN
         UPDATE SET x1.d20_other_sentence = subqry.d20_other_sentence
         WHERE NVL(x1.d20_other_sentence,'~') != subqry.d20_other_sentence;

commit;