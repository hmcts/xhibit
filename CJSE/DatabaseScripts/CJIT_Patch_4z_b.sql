/* This script will be used to update Production from 0_4z_a to the latest level - 0_4z_b */
/* This update is adds a version table to the CJSE database in order to keep track of schema versions */


/* For RFC - ITechTool */
Update CJI_AHM_DEVICE_TYPE set DEVICE_MOBILESYS_REF = 'mnet:mobilesys:clu' 
where  DEVICE_ID = 1;

/* BT Easy Reach No longer in Business */
delete CJI_AHM_DEVICE_TYPE
where Device_id = 3;

select message_id from cji_ahm where device_id = 3

update  cji_ahm set device_id = 4 where device_id = 3

/* Need to remove MobileSys return Number */
update cji_parameter_value set PARAMETER_VALUE = '100'
where PARAMETER_NAME = 'MobileSysMONumber'


UPDATE CJI_VERSION Set
SCHEMA_VERSION = '0_4z_b',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 0_4z_b',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';



MESSAGE_ID
----------
       983
      1107
      1146
      1147
      1164
      1179
        53
        71
       125
       182
       205
       206
       207
       208
       210
       211
       212
       214
       235
       255
       346
       410
       796
       797
       798
       805
       820
      1260
      1315
      1344
      1358
      1359
      1440
      1448
      1480
