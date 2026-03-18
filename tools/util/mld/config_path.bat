@ECHO off

@rem
@rem Title:         XHIBIT Environment Config Script
@rem Description:   This script is used to configure the environment.
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       1.0
@rem 

@rem 
@rem Set PATH Environment Variables
@rem 

@rem JAVA PATH
PATH=%JAVA_HOME%\jre\bin
PATH=%PATH%;%JAVA_HOME%\bin
@rem WEBLOGIC PATH
PATH=%PATH%;%WL_HOME%\server\native\win\32

@rem SET PATH
@rem ECHO;

