@ECHO OFF

SET SOURCE_FOLDER="D:\Apps\XHIBIT"

ECHO.
ECHO About to create a directory called %1 under current directory, are you sure?
ECHO.

:LOOP

SET Choice=
SET /P Choice=Y or N: 

IF NOT '%Choice%'=='' SET Choice=%Choice:~0,1%
ECHO.

IF /I '%Choice%'=='Y' GOTO LabelYes
IF /I '%Choice%'=='N' GOTO LabelNo

ECHO "%Choice%" is not valid. Please try again.
ECHO.
GOTO LOOP

:LabelYes

mkdir %1
cd %1
mkdir "Pre Prod"
mkdir "Prod"
mkdir "System Test"


copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtSiteRoom_SysTest.sql" ".\System Test\04_XHIBIT2_Standing_CourtSiteRoom_SysTest.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtSiteRoom_PreProd.sql" ".\Pre Prod\04_XHIBIT2_Standing_CourtSiteRoom_PreProd.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtSiteRoom_Prod.sql" ".\Prod\04_XHIBIT2_Standing_CourtSiteRoom_Prod.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtLogStaticData.sql" ".\System Test\05_XHIBIT2_Standing_CourtLogStaticData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtLogStaticData.sql" ".\Pre Prod\05_XHIBIT2_Standing_CourtLogStaticData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtLogStaticData.sql" ".\Prod\05_XHIBIT2_Standing_CourtLogStaticData.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicNotice_Ref.sql" ".\System Test\06_XHIBIT2_Standing_PublicNotice_Ref.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicNotice_Ref.sql" ".\Pre Prod\06_XHIBIT2_Standing_PublicNotice_Ref.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicNotice_Ref.sql" ".\Prod\06_XHIBIT2_Standing_PublicNotice_Ref.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic.sql" ".\System Test\07_XHIBIT2_Standing_CRESTImportStatic.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic.sql" ".\Pre Prod\07_XHIBIT2_Standing_CRESTImportStatic.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic.sql" ".\Prod\07_XHIBIT2_Standing_CRESTImportStatic.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_HTTPRetryData.sql" ".\System Test\07a_XHIBIT2_Standing_HTTPRetryData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_HTTPRetryData.sql" ".\Pre Prod\07a_XHIBIT2_Standing_HTTPRetryData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_HTTPRetryData.sql" ".\Prod\07a_XHIBIT2_Standing_HTTPRetryData.sql"

copy "%SOURCE_FOLDER%\database\Mercator\m4ora8.sql" ".\System Test\08_m4ora8.sql"
copy "%SOURCE_FOLDER%\database\Mercator\m4ora8.sql" ".\Pre Prod\08_m4ora8.sql"
copy "%SOURCE_FOLDER%\database\Mercator\m4ora8.sql" ".\Prod\08_m4ora8.sql"

copy "%SOURCE_FOLDER%\database\Mercator\msp_record_events1.sql" ".\Pre Prod\09_msp_record_events1.sql"
copy "%SOURCE_FOLDER%\database\Mercator\msp_record_events1.sql" ".\Prod\09_msp_record_events1.sql"
copy "%SOURCE_FOLDER%\database\Mercator\Merc_Standing_Data.sql" ".\System Test\09_Merc_Standing_Data.sql"

copy "%SOURCE_FOLDER%\database\Mercator\Merc_Standing_Data.sql" ".\Pre Prod\10_Merc_Standing_Data.sql"
copy "%SOURCE_FOLDER%\database\Mercator\Merc_Standing_Data.sql" ".\Prod\10_Merc_Standing_Data.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CC_Info.sql" ".\System Test\11_XHIBIT2_Standing_CC_Info.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CC_Info.sql" ".\Pre Prod\12_XHIBIT2_Standing_CC_Info.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CC_Info.sql" ".\Prod\12_XHIBIT2_Standing_CC_Info.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Status_Data.sql" ".\System Test\12_XHIBIT2_Standing_Orders_Status_Data.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Status_Data.sql" ".\Pre Prod\13_XHIBIT2_Standing_Orders_Status_Data.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Status_Data.sql" ".\Prod\13_XHIBIT2_Standing_Orders_Status_Data.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Skeleton_Delivery_Status.sql" ".\System Test\13_XHIBIT2_Standing_Skeleton_Delivery_Status.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Skeleton_Delivery_Status.sql" ".\Pre Prod\14_XHIBIT2_Standing_Skeleton_Delivery_Status.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Skeleton_Delivery_Status.sql" ".\Prod\14_XHIBIT2_Standing_Skeleton_Delivery_Status.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Document_Reply.sql" ".\System Test\14_XHIBIT2_Standing_Document_Reply.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Document_Reply.sql" ".\Pre Prod\15_XHIBIT2_Standing_Document_Reply.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Document_Reply.sql" ".\Prod\15_XHIBIT2_Standing_Document_Reply.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay.sql" ".\System Test\15_XHIBIT2_Standing_PublicDisplay.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay.sql" ".\Pre Prod\16_XHIBIT2_Standing_PublicDisplay.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay.sql" ".\Prod\16_XHIBIT2_Standing_PublicDisplay.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay_Court.sql" ".\System Test\16_XHIBIT2_Standing_PublicDisplay_Court.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay_Court.sql" ".\Pre Prod\17_XHIBIT2_Standing_PublicDisplay_Court.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay_Court.sql" ".\Prod\17_XHIBIT2_Standing_PublicDisplay_Court.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CR_Live_Status.sql" ".\System Test\17_XHIBIT2_Standing_CR_Live_Status.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CR_Live_Status.sql" ".\Pre Prod\18_XHIBIT2_Standing_CR_Live_Status.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CR_Live_Status.sql" ".\Prod\18_XHIBIT2_Standing_CR_Live_Status.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic_Court.sql" ".\System Test\18_XHIBIT2_Standing_CRESTImportStatic_Court.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic_Court.sql" ".\Pre Prod\19_XHIBIT2_Standing_CRESTImportStatic_Court.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic_Court.sql" ".\Prod\19_XHIBIT2_Standing_CRESTImportStatic_Court.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Types_and_Templates.sql" ".\System Test\19_XHIBIT2_Standing_Orders_Types_and_Templates.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Types_and_Templates.sql" ".\Pre Prod\20_XHIBIT2_Standing_Orders_Types_and_Templates.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Types_and_Templates.sql" ".\Prod\20_XHIBIT2_Standing_Orders_Types_and_Templates.sql"


ECHO.
ECHO PLEASE ENSURE THAT THE CORRESPONDING PATCH FILES ARE COPIED TO THE RELEASE DIRECTORY
ECHO.

cd ..

:LabelNo
ECHO.
