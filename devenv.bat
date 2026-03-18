@echo off
echo.
echo Setting development environment variables...
echo.
set ANT_HOME=C:\bea\modules\org.apache.ant_1.7.1
set JAVA_HOME=C:\bea\jdk1.6.0_45
set JAVA_VENDOR=Oracle
set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%
set XHIBIT_HOME=%~dp0
set WL_HOME=C:\bea\wlserver_10.3
set CLASSPATH=%WL_HOME%\server\lib\wlfullclient.jar
echo ANT_HOME=%ANT_HOME%
echo JAVA_HOME=%JAVA_HOME%
echo JAVA_VENDOR=%JAVA_VENDOR%
echo WL_HOME=%WL_HOME%
echo XHIBIT_HOME=%XHIBIT_HOME%
