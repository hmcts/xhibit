@rem 
@rem Script to generate a client key store from a server certificate.
@rem
@rem The script assumes the certificate file is called xhibit2008.cer
@rem and is stored in the same directory as this script.
@rem

set JAVA_HOME=C:\bea\jdk150_04

set INPUT_FILE=xhibit2008.cer

set OUTPUT_FILE=xhibit.client.jks

%JAVA_HOME%\bin\keytool.exe -import -keystore %OUTPUT_FILE% -storepass password -alias xhibitthin -keypass password -file %INPUT_FILE% -noprompt

