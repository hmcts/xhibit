INSERT INTO XHB_REMAND_REASON_DESCRIPTION
(REASON_CATEGORY, REASON_DESCRIPTION)
SELECT subqry.* FROM (
SELECT 'Seriousness of offence' REASON_CATEGORY, 'The offence must be violent or sexual offence' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Seriousness of offence' REASON_CATEGORY, 'An offence carrying 14 years imprisionment or more in the case of an adult' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Seriousness of offence' REASON_CATEGORY, 'It is very likely that the offence would result in conviction and attract a custodial sentence' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'History' REASON_CATEGORY, 'the child has a recent and significant history of absconding while subject to a custodial remand '||
'and that history is relevant in all the circumstances of the case' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'History' REASON_CATEGORY, 'the child has recent and significant history of committing imprisonable offences while on bail '||
'or subject to a custodial remand and that history is relevant in all the circumstances of the case' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'History' REASON_CATEGORY, 'it is very likely that the offence would result in a conviction and attract a custodial sentence' 
REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Necessity Condition' REASON_CATEGORY, 'the court has considered all other options but the child poses a risk of harm or '||
'offending AND the risk posed by the child cannot be managed safely in the community' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Other reasons' REASON_CATEGORY, 'for welfare reasons' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Other reasons' REASON_CATEGORY, 'For own protection' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Other reasons' REASON_CATEGORY, 'lack of suitable placement in the community' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Other reasons' REASON_CATEGORY, 'Bail ISS not available' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Other reasons' REASON_CATEGORY, 'bail package inadequate' REASON_DESCRIPTION FROM DUAL
) subqry 
WHERE NOT EXISTS (SELECT NULL FROM XHB_REMAND_REASON_DESCRIPTION x1 WHERE x1.REASON_CATEGORY = subqry.REASON_CATEGORY AND ROWNUM = 1);

commit;
/