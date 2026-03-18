@ECHO off

@rem
@rem Title:         XHIBIT Letter Generation Script
@rem Description:   This script is used to test the generation of letters
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       1.0
@rem 

SETLOCAL

@rem 
@rem Check Parameters
@rem 

set XML_FILE=%1

@rem Check Parameters 
 
IF NOT DEFINED XML_FILE (
    ECHO usage firm_letters.bat dl-xml
    EXIT /B 1
)

@rem 
@rem Call Letters Script
@rem 

SET LETTERS_SCRIPT=%~pd0%letters.bat
       
CALL %LETTERS_SCRIPT% uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.FirmListProcessor %XML_FILE%
IF %ERRORLEVEL% NEQ 0 (
    EXIT /B %ERRORLEVEL%
)

