<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
    <xsl:include href="date-time.xsl"/>
    <xsl:include href="string.xsl"/>
    <xsl:include href="gcsUtility.xsl"/>
    <doc:reference xmlns="">
        <referenceinfo>
            <releaseinfo role="meta">Version 1.0</releaseinfo>
            <author>
                <surname>Nicholson</surname>
                <firstname>Kevin</firstname>
            </author>
        </referenceinfo>
        <title>PSR Request Stylesheet</title>
        <para>File name : 'PSRRequest-v1-0.xsl</para>
        <partintro>
            <section>
                <title>Introduction</title>
                <para>This module produces the PSR Request in html format</para>
            </section>
        </partintro>
    </doc:reference>

    <!-- Version Information -->
    <xsl:variable name="majorVersion" select="'1'"/>
    <xsl:variable name="minorVersion" select="'0'"/>
    <xsl:variable name="stylesheet" select="'PSRRequest-v1-0.xsl'"/>
    <xsl:variable name="last-modified-date" select="'2007-01-04'"/>
    <!-- End Version Information -->

    <!-- Global Variables -->
    <xsl:variable name="PlaceHolder">
        <em style="background-color: #778899"> -- Enter Data Here -- </em>
    </xsl:variable>
    <xsl:variable name="CaseNumber" select="/cs:PreSentenceReport/cs:CaseNumber" />
    <!-- end Global Variables -->

    <xsl:output method="html" indent="yes"/>
    <!-- **************************************** -->
    <!-- Root Template                  -->
    <!-- **************************************** -->
    <doc:template name="/" xmlns="">
        <refpurpose>Controls the sequence of elements to be displayed.</refpurpose>
        <para>
            <emphasis role="bold">Special Rules</emphasis>
        </para>
        <itemizedlist>
            <listitem>
                <para>Note: Uses the routine util:javascript to embed the javascript functions (used to load the Crown Logo) in the generated html.</para>
            </listitem>
            <listitem>
                <para>Note: Uses the routine util:cssTemplate to embed the CSS stylesheet in the generated html.</para>
            </listitem>
            <listitem>
                <para>Note: Uses the routine util:showLogo to embed the link to the Crown logo in the generated html</para>
            </listitem>
        </itemizedlist>
    </doc:template>
    
    <xsl:template match="/">
        <html>
            <head>
                <xsl:call-template name="util:cssTemplate"/>
            </head>
            <body>
                <xsl:call-template name="PSRtitle"/>
                <hr/>
                <xsl:call-template name="JudgeDetails"/>
                <hr/>           
                <xsl:call-template name="DefendantDetails"/>
                <hr/>
                <xsl:call-template name="Solicitor"/>
                <hr/>   
                <xsl:call-template name="CPSInfo"/>
                <hr/>
                <xsl:call-template name="OffencesGuilty"/>
                <hr/>
                <xsl:call-template name="Offences"/>
                <hr/>
                <xsl:call-template name="PreviousConvictions"/>
                <hr/>
                <xsl:call-template name="CoDefendants"/>
                <hr/>
                <xsl:call-template name="Circumstances"/>
                <hr/>
                <xsl:call-template name="Comments"/>
                <hr/>
                <xsl:call-template name="OtherReports"/>
                <hr/>
                <xsl:call-template name="FurtherInfomation"/>
                <hr/>
                <xsl:call-template name="Interview"/>
                <hr/>
                <xsl:call-template name="Signed"/>
                <!-- CR27   -->
                <xsl:call-template name="util:copyrightText2006"/>
            </body>
        </html>
    </xsl:template>

    <!-- Display document title -->
    <xsl:template name="PSRtitle">
        <table width="100%">
            <tr>
                <td width="80%">
                </td>
                <td width="20%">
                    <xsl:text>Case Number: </xsl:text>
                    <xsl:value-of select="/cs:PreSentenceReport/cs:CaseNumber"/>
                </td>
            </tr>
        </table>
        <center>
            <h1>
                <xsl:text>Pre-Sentence Report Request</xsl:text>
            </h1>
        </center>
    </xsl:template>
    
    <!-- Display Judge Details -->  
    <xsl:template name="JudgeDetails">
        <xsl:text>Adjourned by </xsl:text>
        <xsl:value-of select="/cs:PreSentenceReport/cs:Judiciary/cs:Judge/apd:CitizenNameRequestedName"/>
        <xsl:if test="not(/cs:PreSentenceReport/cs:Judiciary)">
            <xsl:copy-of select="$PlaceHolder"/>
        </xsl:if>
        <xsl:text> on </xsl:text>
        <xsl:call-template name="util:ukdate_mon">
            <xsl:with-param name="inDate" select="/cs:PreSentenceReport/cs:PSRRequestDate" />
        </xsl:call-template>
        <xsl:text> at </xsl:text>
        <xsl:value-of select="/cs:PreSentenceReport/cs:PSRRequestCourt/cs:CourtHouseName"/>
        <xsl:text> for hearing on </xsl:text>
        <xsl:if test="/cs:PreSentenceReport/cs:LongAdjournmentDate">
            <strong>
                <xsl:call-template name="util:ukdate_mon">
                    <xsl:with-param name="inDate" select="/cs:PreSentenceReport/cs:LongAdjournmentDate" />
                </xsl:call-template>
            </strong>
        </xsl:if>
        <xsl:if test="not(/cs:PreSentenceReport/cs:LongAdjournmentDate)">
            <xsl:copy-of select="$PlaceHolder"/>
        </xsl:if>
        <br/>
        <table width="100%">
            <tr>
                <td width="15%">
                    <strong><xsl:text>To:</xsl:text></strong>
                </td>
                <td width="85%">
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>
            <tr>
                <td>
                    <strong><xsl:text>Address:</xsl:text></strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>
            <tr>
                <td>
                    <strong><xsl:text>E-mail:</xsl:text></strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>
            <tr>
                <td>
                    <strong><xsl:text>Fax:</xsl:text></strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <!-- Display Main Defendant Details  -->
    <xsl:template name="DefendantDetails">
        <strong>
            <xsl:text>Offender Details:</xsl:text>
        </strong>
        <table width="100%">
            <tr>
                <td width="15%">
                    <strong>Surname:</strong>
                </td>
                <td width="85%">
                    <xsl:value-of select="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                </td>
            </tr>
            <tr>
                <td>
                    <strong>Forename:</strong>
                </td>
                <td>
                    <xsl:for-each select="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:Name/apd:CitizenNameForename">
                        <xsl:value-of select="."/>
                        <xsl:text> </xsl:text>
                    </xsl:for-each>                     
                </td>
            </tr>
            <xsl:if test="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                <tr>
                    <td>
                        <strong>DOB:</strong>
                    </td>
                    <td>
                        <xsl:call-template name="util:ukdate_mon">
                            <xsl:with-param name="inDate" select="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate" />
                        </xsl:call-template>
                    </td>
                </tr>       
            </xsl:if>   
            <!-- Data for Age should only be displayed if Date of BIrth is not present -->
            <xsl:if test="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:Age">
                <tr>
                    <td>
                        <strong>Age:</strong>
                    </td>
                    <td>
                        <xsl:value-of select="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:Age" /> 
                        <xsl:text> years</xsl:text>
                    </td>
                </tr>               
            </xsl:if>       
            <tr>
                <td>
                    <strong>Gender:</strong>
                </td>
                <td>
                    <xsl:call-template name="util:transformCaseSpecial">
                        <xsl:with-param name="text" select="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails/cs:Sex" />
                    </xsl:call-template>
                </td>
            </tr>       
            <!-- Data for Ethnicity is NOT availble in XHIBIT so this is ALWAYS left blank -->                      
            <tr>
                <td>
                    <strong>Ethnicity:</strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>                               
            <tr>
                <td>
                    <strong>Home Address:</strong>
                </td>
                <td>
                    <xsl:call-template name="util:address_oneline">
                        <xsl:with-param name="personalDetails" select="/cs:PreSentenceReport/cs:Defendant/cs:PersonalDetails"/>
                    </xsl:call-template>
                </td>
            </tr>                                   
            <tr>
                <td>
                    <strong>Tel: Home</strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>                                   
            <tr>
                <td>
                    <strong>Tel: Work</strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>                   
            <tr>
                <td>
                    <strong>Tel: Mobile</strong>
                </td>
                <td>
                    <xsl:copy-of select="$PlaceHolder"/>
                </td>
            </tr>                   
            <tr>
                <td>
                    <strong>Bail Status:</strong>
                </td>
                <td>
                    <xsl:value-of select="/cs:PreSentenceReport/cs:Defendant/cs:CustodyStatus"/>
                </td>
            </tr>                                                   
        </table>
        
        <xsl:text>Note: PSR authors should check remand/bail status as this may change after the PSR has been requested.</xsl:text>
    
    </xsl:template>
    
    <!-- Displays main defendands solicitor - current one only i.e. without an end date -->
    <xsl:template name="Solicitor">

        <xsl:for-each select="/cs:PreSentenceReport/cs:Defendant/cs:Counsel/cs:Solicitor">  
            <xsl:if test="not(./cs:EndDate)">
                <table width="100%">
                    <tr>
                        <td width="15%"><strong>Solicitors:</strong></td>
                        <td width="85%"><xsl:value-of select="./cs:Party/cs:Organisation/cs:OrganisationName"/> </td>
                    </tr>
                    <tr>
                        <td><strong>Telephone:</strong></td>
                        <td><xsl:value-of select="./cs:Party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/></td>
                    </tr>
                        <tr>
                        <td><strong>Address:</strong></td>
                        <td>
                            <xsl:for-each select="./cs:Party/cs:Organisation/cs:OrganisationAddress/apd:Line    [not (position()=2 and .='-')]">
                                <xsl:call-template name="str:capitalise">
                                    <xsl:with-param name="text" select="."/>
                                </xsl:call-template>        
                                <xsl:if test="not (position() = last())"> 
                                    <xsl:if test = "string-length() &gt; 0">
                                        <xsl:text>, </xsl:text>
                                    </xsl:if>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text> </xsl:text>
                            <xsl:value-of   select="./cs:Party/cs:Organisation/cs:OrganisationAddress/apd:PostCode"/>
                            <xsl:call-template name="util:address_oneline">
                                <xsl:with-param name="personalDetails"  select="./cs:Party/cs:Organisation/cs:OrganisationAddress"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </table>
            </xsl:if>
        </xsl:for-each>

    </xsl:template>
    
    <!-- Display CPS details -->
    <xsl:template name="CPSInfo">
        <strong><xsl:text>Prosecution information should be available from: </xsl:text></strong>
        
        <xsl:value-of select="/cs:PreSentenceReport/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
        <xsl:text>, </xsl:text>
        <xsl:for-each select="/cs:PreSentenceReport/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress/apd:Line[not (position()=2 and .='-')]">
            <xsl:call-template name="str:capitalise">
                <xsl:with-param name="text" select="."/>
            </xsl:call-template>                        
            <xsl:if test="not (position() = last())"> 
                <xsl:if test = "string-length() &gt; 0">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:if>
        </xsl:for-each>
        <xsl:text> </xsl:text>
        <xsl:value-of select="/cs:PreSentenceReport/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress/apd:PostCode"/>
        <xsl:text> (if not previously forwarded). </xsl:text>
        <xsl:if test="/cs:PreSentenceReport/cs:Defendant/cs:URN">
            <xsl:text>PTI URN: </xsl:text>
            <xsl:value-of select="/cs:PreSentenceReport/cs:Defendant/cs:URN"/>      
            <xsl:text>.</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <!-- Display charges that have Guilty Verdicts or Pleas -->
    <xsl:template name="OffencesGuilty">
        <strong>
            <xsl:text>Offences (Convicted):</xsl:text>
        </strong>
        <br></br>       
    
        <xsl:choose>
            
            <xsl:when test="substring($CaseNumber,1,1) = 'A' or substring($CaseNumber,1,1) = 'B' or substring($CaseNumber,1,1) = 'U'">
                
                    <!-- If case type is A, B or U then ignore -->
                
            </xsl:when>
                            
            <xsl:when test="substring($CaseNumber,1,1) = 'T' or substring($CaseNumber,1,1) = 'S'">

                <!-- For a T case or S case-->
                <xsl:for-each select="/cs:PreSentenceReport/cs:Defendant/cs:Charges/cs:Charge ">
                
                    <xsl:choose>
                        <xsl:when test="./cs:Plea='Autrefois Convict'  or   
                                        ./cs:Plea='Guilty' or 
                                        ./cs:Plea='Admitted' or 
                                        starts-with(./cs:Plea,'Guilty to alternative offence not charged namely') or 
                                        starts-with(./cs:Plea,'Guilty to lesser offence not charged namely') or 
                                        starts-with(./cs:Plea,'Guilty to lesser offence namely') or 
                                        ./cs:Plea='Change of Plea: Not guilty to guilty (no jury sworn)' or 
                                        ./cs:Plea='Change of Plea: Not guilty to guilty (after jury sworn)' or 
                                        ./cs:Verdict='Autrefois Convict' or ./cs:Verdict='Guilty' or 
                                        starts-with(./cs:Verdict,'Not guilty but guilty of alternative offence not charged namely') or 
                                        starts-with(./cs:Verdict,'Guilty (by Jury on Judge') or 
                                        starts-with(./cs:Verdict,'Not guilty but guilty of lesser offence not charged namely') or 
                                        starts-with(./cs:Verdict,'Not guilty but guilty of lesser offence on Judge') or 
                                        starts-with(./cs:Verdict,'Not guilty but guilty of alternative offence on Judge') or 
                                        ./cs:Verdict='Original Jury discharged, unable to agree. Found guilty by another Jury' ">
                                                    <!-- Display offence details depending on verdict/plea otherwise just display the offence statement-->
                                                    <xsl:choose>
                                                        <xsl:when test="starts-with(./cs:Plea,'Guilty to alternative offence not charged namely')">
                                                            <xsl:value-of select="substring-after(./cs:Plea,'charged namely ')"/>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with(./cs:Plea,'Guilty to lesser offence namely')">
                                                            <xsl:value-of select="substring-after(./cs:Plea,'offence namely ')"/>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with(./cs:Plea,'Guilty to lesser offence not charged namely')">
                                                            <xsl:value-of select="substring-after(./cs:Plea,'charged namely ')"/>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with(./cs:Verdict,'Not guilty but guilty of alternative offence not charged namely')">
                                                            <xsl:value-of select="substring-after(./cs:Verdict,'charged namely ')"/>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with(./cs:Verdict,'Not guilty but guilty of lesser offence not charged namely')">
                                                            <xsl:value-of select="substring-after(./cs:Verdict,'charged namely ')"/>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with(./cs:Verdict,'Not guilty but guilty of lesser offence on Judge')">
                                                            <xsl:value-of select="substring-after(./cs:Verdict,'direction namely ')"/>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with(./cs:Verdict,'Not guilty but guilty of alternative offence on Judge')">
                                                            <xsl:value-of select="substring-after(./cs:Verdict,'direction namely ')"/>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <xsl:value-of select="cs:OffenceStatement"/>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                    <br></br>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
    
            </xsl:when>
                
        </xsl:choose>

    </xsl:template>

    <!-- Display charges that do NOT have Guilty Verdicts or Pleas -->
    <xsl:template name="Offences">
        <strong>
            <xsl:text>Offences :</xsl:text>
        </strong>
        <br/>
        
        <xsl:choose>
        
            <xsl:when test="substring($CaseNumber,1,1) = 'A' or substring($CaseNumber,1,1) = 'B' or substring($CaseNumber,1,1) = 'U'">
                
                    <!-- If case type is A, B or U then ignore -->
                    
            </xsl:when>
    
            <xsl:when test="substring($CaseNumber,1,1) = 'T' or substring($CaseNumber,1,1) = 'S'">

                <!-- For a T case or S case-->
                
                <xsl:for-each select="/cs:PreSentenceReport/cs:Defendant/cs:Charges/cs:Charge ">
                
                    <!-- S.Bachra 10/10/06 Remove condition RFC 1376 Testing - display all charge details
                    <xsl:choose>
                        <xsl:when test="./cs:Plea='Autrefois Convict'  or   ./cs:Plea='Guilty' or 
                                        ./cs:Plea='Guilty to alternative offence not charged namely' or 
                                        ./cs:Plea='Guilty to lesser offence not charged namely' or 
                                        ./cs:Plea='Change of Plea: Not guilty to guilty (no jury sworn)' or 
                                        ./cs:Plea='Change of Plea: Not guilty to guilty (after jury sworn)' or 
                                        ./cs:Verdict='Autrefois Convict' or ./cs:Verdict='Guilty' or 
                                        ./cs:Verdict='Not guilty but guilty of alternative offence not charged namely' or 
                                        ./cs:Verdict=concat('Guilty (by Jury on Judge', '&amp;quot;' ,'s direction)') or 
                                        ./cs:Verdict='Not guilty but guilty of lesser offence not charged namely' or 
                                        ./cs:Verdict=concat('Not guilty but guilty of lesser offence on Judge','&amp;quot;','s direction namely') or 
                                        ./cs:Verdict=concat('Not guilty but guilty of alternative offence on Judge','&amp;quot;','s direction namely') or 
                                        ./cs:Verdict='Jury unable to agree' or 
                                        ./cs:Verdict='Original Jury discharged, unable to agree. Found guilty by another Jury' "> -->
                                <!-- Do nothing -->
                        <!-- </xsl:when>
                        <xsl:otherwise> -->
                            <xsl:value-of select="./cs:OffenceStatement"/>
                            <br/>
                        <!--</xsl:otherwise>
                    </xsl:choose> -->
                </xsl:for-each>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="PreviousConvictions">
        <strong>
            <xsl:text>Previous Convictions:</xsl:text>
        </strong>
        <br/>
        <p>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>
    
    <!-- Pull out co-defendant information - if none present then skip section -->
    <xsl:template name="CoDefendants">
        <strong>
            <xsl:text>Co-defendants:</xsl:text>
        </strong>
        <br/>
        <table>
            <xsl:for-each select="/cs:PreSentenceReport/cs:CoDefendants/cs:Defendant">
                <tr>
                    <td width="30%">
                        <xsl:for-each select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename">
                            <xsl:value-of select="."/>
                            <xsl:text> </xsl:text>
                        </xsl:for-each> 
                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                    </td>
                    <td width="20%">
                        <xsl:if test="./cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                            <xsl:text>DOB: </xsl:text>
                            <xsl:call-template name="util:ukdate_mon">
                                <xsl:with-param name="inDate" select="./cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate" />
                            </xsl:call-template>
                        </xsl:if>
                        <xsl:if test="./cs:PersonalDetails/cs:Age">
                            <xsl:text>Age: </xsl:text>
                            <xsl:value-of select="./cs:PersonalDetails/cs:Age" />
                            <xsl:text> years</xsl:text>
                        </xsl:if>
                    </td>
                    <td width="10%">
                        <xsl:call-template name="util:transformCaseSpecial">
                            <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex" />
                        </xsl:call-template>
                    </td>
                    <td width="40%">
                        <xsl:call-template name="util:address_oneline">
                            <xsl:with-param name="personalDetails" select="./cs:PersonalDetails"/>
                        </xsl:call-template>            
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template name="Circumstances">
        <strong>
            <xsl:text>Circumstances of Offences:</xsl:text>
        </strong>
        <p>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>
    
    <xsl:template name="Comments">
        <strong>
            <xsl:text>Judges Comments/Adjournment Statement:</xsl:text>
        </strong>
        <p>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>
    
    <xsl:template name="OtherReports">
        <strong>
            <xsl:text>Other Reports (e.g. Psychiatric):</xsl:text>
        </strong>
        <p>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>

    <xsl:template name="FurtherInfomation">
        <strong>
            <xsl:text>Further Information (e.g. interpreter needed, risk information, PPO status?):</xsl:text>
        </strong>
        <p>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>

    <xsl:template name="Interview">
        <strong>
            <xsl:text>Interview Availability/Appointment Details (if made):</xsl:text>
        </strong>
        <p>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>
    
    <xsl:template name="Signed">
        <p>
            <strong><xsl:text>Crown Court Probation Service:</xsl:text></strong>
        </p>

        <table width="100%">
            <tr>
                <td width="15%">
                    <strong>Address:</strong>
                </td>
                <td width="85%">
                    <xsl:for-each select="/cs:PreSentenceReport/cs:ProbationDepartment/cs:OrganisationAddress/apd:Line[not (position()=2 and .='-')]">
                        <xsl:call-template name="str:capitalise">
                            <xsl:with-param name="text" select="."/>
                        </xsl:call-template>                        
                        <xsl:if test="not (position() = last())"> 
                            <xsl:if test = "string-length() &gt; 0">
                                <xsl:text>, </xsl:text>
                            </xsl:if>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="/cs:PreSentenceReport/cs:ProbationDepartment/cs:OrganisationAddress/apd:PostCode"/>
                </td>
            </tr>
            <tr>
                <td>
                    <strong>Phone:</strong>
                </td>
                <td>
                    <xsl:value-of select="/cs:PreSentenceReport/cs:ProbationDepartment/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
                </td>
            </tr>                   
            <tr>
                <td>
                    <strong>E-mail:</strong>
                </td>
                <td>
                    <xsl:value-of select="/cs:PreSentenceReport/cs:ProbationDepartment/cs:ContactDetails/apd:Email/apd:EmailAddress"/>
                </td>
            </tr>                   
            <tr>
                <td>
                    <strong>Fax:</strong>
                </td>
                <td>
                    <xsl:value-of select="/cs:PreSentenceReport/cs:ProbationDepartment/cs:ContactDetails/apd:Fax/apd:FaxNationalNumber"/>                                           </td>
            </tr>                   
            
        </table>
        <p>
            <strong><xsl:text>Please send report to: </xsl:text></strong>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
        <p>
            <strong><xsl:text>To arrive no later than: </xsl:text></strong>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
        <p>
            <strong><xsl:text>Name of Officer Completing Report: </xsl:text></strong>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>        
        <p>
            <strong><xsl:text>Date: </xsl:text></strong>
            <xsl:copy-of select="$PlaceHolder"/>
        </p>
    </xsl:template>

</xsl:stylesheet>
