MERGE INTO xhb_d20_offence_codes xdoc USING (
-- Accident
SELECT 'AC10' offence_code, '5-10' penalty_points FROM DUAL UNION
SELECT 'AC20' offence_code, '5-10' penalty_points FROM DUAL UNION
SELECT 'AC30' offence_code, '4-9'  penalty_points FROM DUAL UNION
-- Disqualified driver
SELECT 'BA10' offence_code, '6'    penalty_points FROM DUAL UNION
SELECT 'BA30' offence_code, '6'    penalty_points FROM DUAL UNION
SELECT 'BA40' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'BA60' offence_code, '3-11' penalty_points FROM DUAL UNION
-- Careless driving
SELECT 'CD10' offence_code, '3-9'  penalty_points FROM DUAL UNION
SELECT 'CD20' offence_code, '3-9'  penalty_points FROM DUAL UNION
SELECT 'CD30' offence_code, '3-9'  penalty_points FROM DUAL UNION
SELECT 'CD40' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'CD50' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'CD60' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'CD70' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'CD80' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'CD90' offence_code, '3-11' penalty_points FROM DUAL UNION
-- Construction
SELECT 'CU10' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'CU20' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'CU30' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'CU40' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'CU50' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'CU80' offence_code, '3'    penalty_points FROM DUAL UNION
-- Reckless / Dangerous driving
SELECT 'DD10' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DD40' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DD60' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DD80' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DD90' offence_code, '3-9'  penalty_points FROM DUAL UNION
-- Drink
SELECT 'DR10' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DR20' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DR30' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DR31' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DR61' offence_code, '10'   penalty_points FROM DUAL UNION
SELECT 'DR40' offence_code, '10'   penalty_points FROM DUAL UNION
SELECT 'DR50' offence_code, '10'   penalty_points FROM DUAL UNION
SELECT 'DR60' offence_code, '10'   penalty_points FROM DUAL UNION
SELECT 'DR70' offence_code, '4'    penalty_points FROM DUAL UNION
-- Drugs
SELECT 'DG10' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DG60' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DG80' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'DG40' offence_code, '10'   penalty_points FROM DUAL UNION
SELECT 'DG90' offence_code, '10'   penalty_points FROM DUAL UNION
-- Insurance
SELECT 'IN10' offence_code, '6-8'  penalty_points FROM DUAL UNION
-- Licence
SELECT 'LC20' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'LC30' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'LC40' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'LC50' offence_code, '3-6'  penalty_points FROM DUAL UNION
-- Miscellaneous
SELECT 'MS10' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'MS20' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'MS30' offence_code, '2'    penalty_points FROM DUAL UNION
SELECT 'MS50' offence_code, '3-11' penalty_points FROM DUAL UNION
SELECT 'MS60' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'MS70' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'MS80' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'MS90' offence_code, '6'    penalty_points FROM DUAL UNION
-- Motorway
SELECT 'MW10' offence_code, '3'    penalty_points FROM DUAL UNION
--Pedestrian 
SELECT 'PC10' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'PC20' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'PC30' offence_code, '3'    penalty_points FROM DUAL UNION
--Speed
SELECT 'SP10' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'SP20' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'SP30' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'SP40' offence_code, '3-6'  penalty_points FROM DUAL UNION
SELECT 'SP50' offence_code, '3-6'  penalty_points FROM DUAL UNION
-- Traffic
SELECT 'TS10' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'TS20' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'TS30' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'TS40' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'TS50' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'TS60' offence_code, '3'    penalty_points FROM DUAL UNION
SELECT 'TS70' offence_code, '3'    penalty_points FROM DUAL UNION
-- Theft / Unauthorised taking
SELECT 'UT50' offence_code, '3-11' penalty_points FROM DUAL
) subqry
    ON (subqry.offence_code = xdoc.offence_code)
    WHEN MATCHED THEN
         UPDATE SET xdoc.penalty_points = subqry.penalty_points
         WHERE xdoc.penalty_points != subqry.penalty_points; 

commit;