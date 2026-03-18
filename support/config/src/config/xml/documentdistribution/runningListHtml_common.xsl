<?xml version="1.0" encoding="UTF-8"?>
<!--
    +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" version="1.1">
    <xsl:output method="html" indent="yes"/>
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>
    <!-- set up global variable to hold the report date -->
    <xsl:variable name="reportdate">
        <xsl:choose>
            <xsl:when test="//cs:ListHeader/cs:EndDate">
                <xsl:value-of select="//cs:ListHeader/cs:EndDate"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="//cs:ListHeader/cs:StartDate"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <!-- top level match -->
    <xsl:template name="DisplayRunningList">
        <xsl:param name="language"/>
        <html>
            <meta content="text/html;  charset=UTF-8" http-equiv="Content-Type"/>
            <body>
                <!-- Display Crown Court Details -->
                <xsl:apply-templates select="cs:RunningList/cs:CrownCourt">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <!-- Display Trial Case Info -->
                <xsl:apply-templates select="cs:RunningList/cs:TrialCases">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <!-- Display Commital Case Info -->
                <xsl:apply-templates select="cs:RunningList/cs:CommitalCases">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <!-- Display Appeal Case Info -->
                <xsl:apply-templates select="cs:RunningList/cs:AppealCases">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <center>
                    <b>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'End of Report'"/>
                        </xsl:call-template>
                    </b>
                </center>
                <!-- Display footer info -->
                <xsl:call-template name="listfooter">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- Finish with Copyright notice -->
                <br/>
                <xsl:call-template name="displayCopyright">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- End Finish with Copyright notice -->
            </body>
        </html>
    </xsl:template>
    <!--        
            **************************
            TEMPLATE MATCHES 
            *************************
    -->
    <!-- Display Appeal Case Information ******************************************************************************************************************************************** -->
    <xsl:template match="cs:AppealCases">
        <xsl:param name="language"/>
        <br/>
        <font size="1">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'APPEALS TO THE CROWN COURT'"/>
            </xsl:call-template>
        </font>
        <br/>
        <table class="detail" width="92%">
            <!-- call template to display Appeal Case Header info -->
            <xsl:call-template name="displayAppealTable">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
            <!-- Display Case and defendant info -->
            <xsl:for-each select="./cs:Case">
                <xsl:variable name="case" select="."/>
                <xsl:variable name="caseNum" select="./cs:CaseNumber"/>
                <xsl:for-each select="./cs:Defendants/cs:Defendant">
                    <tr>
                        <td valign="top">
                            <!-- Just display the case number if it is the first defendant -->
                            <font size="1">
                                <xsl:value-of select="$caseNum"/>
                            </font>
                            <!--
                            <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                <font size="1">
                                    <xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
                                </font>
                            </xsl:if>
                            <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                <font size="1">
                                    <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                </font>
                            </xsl:if>
                            -->
                        </td>
                        <td valign="top">
                            <font size="1">
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                    </xsl:call-template>
                                    <br/>
                                    <!-- Display defendants Forename if exists -->
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
                                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
                                        <br/>
                                    </xsl:if>
                                    <!-- Check to see if there is a defendants Middle Name and display -->
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
                                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
                                        <br/>
                                    </xsl:if>
                                </xsl:if>
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                    </xsl:call-template>
                                    <br/>
                                </xsl:if>
                                <!-- check to see if sex or DOB -->
                                <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown' or cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                    <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                        <tr>
                                            <td width="25%" valign="top">
                                                <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                                    <font size="1">
                                                        <xsl:variable name="gender">
                                                            <xsl:call-template name="TitleCase">
                                                                <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                                                            </xsl:call-template>
                                                        </xsl:variable>
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="$gender"/>
                                                        </xsl:call-template>
                                                    </font>
                                                </xsl:if>
                                            </td>
                                            <td width="75%" valign="top">
                                                <xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                    <font size="1">
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="'Date of Birth:'"/>
                                                        </xsl:call-template>
                                                        <xsl:text> </xsl:text>
                                                        <xsl:call-template name="FormatDate">
                                                            <xsl:with-param name="input">
                                                                <xsl:value-of select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                                                            </xsl:with-param>
                                                            <xsl:with-param name="language" select="$language"/>
                                                        </xsl:call-template>
                                                    </font>
                                                </xsl:if>
                                            </td>
                                        </tr>
                                    </table>
                                </xsl:if>
                                <!-- Display solicitor details -->
                                <xsl:for-each select="cs:Counsel/cs:Solicitor">
                                    <xsl:call-template name="solicitor">
                                        <xsl:with-param name="party" select="cs:Party"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:for-each>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <!-- Display custody Status -->
                                <xsl:call-template name="DisplayCustodyStatus">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <!--Committing Court/Method of Instigation-->
                                <xsl:if test="$case/cs:MethodOfInstigation">
                                    <xsl:call-template name="DispMethInst">
                                        <xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
                                        <xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                        <xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
                                        <xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                    <br/>
                                </xsl:if>
                                <!--what if no method of instigation? - show committing court stuff or appeal case description-->
                                <xsl:if test="not($case/cs:MethodOfInstigation)">
                                    <!-- Display originating court details -->
                                    <xsl:if test="$case/cs:CaseArrivedFrom">
                                        <xsl:call-template name="do-replace">
                                            <xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'CC'"/>
                                            </xsl:call-template>
                                        </xsl:if>
                                        <br/>
                                    </xsl:if>
                                    <xsl:if test="$case/cs:AppealCaseDescription">
                                        <xsl:value-of select="$case/cs:AppealCaseDescription"/>
                                        <br/>
                                    </xsl:if>
                                </xsl:if>
                            </font>
                            <!-- Display prosecution info -->
                            <xsl:choose>
                                <xsl:when test="$case/cs:Prosecution">
                                    <font size="1">
                                        <xsl:variable name="prosOrg">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="toUpper">
                                            <xsl:with-param name="content" select="$prosOrg"/>
                                        </xsl:call-template>
                                    </font>
                                </xsl:when>
                                <xsl:otherwise>
                                    <font size="1">
                                        <xsl:text>** </xsl:text>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'NO RESPONDENT'"/>
                                        </xsl:call-template>
                                        <xsl:text> **</xsl:text>
                                    </font>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        <td valign="top">
                            <xsl:if test="$case/cs:DateOfInstigation">
                                <font size="1">
                                    <xsl:call-template name="displayDate">
                                        <xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </font>
                            </xsl:if>
                        </td>
                    </tr>
                    <tr/>
                    <!--No deft/appllt? then display fixed text-->
                    <tr>
                        <td colspan="5" align="center">
                            <font size="1">
                                <xsl:call-template name="noDeftText">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </font>
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:for-each>
        </table>
    </xsl:template>
    <!-- Display Committal Case Info ************************************************************************************************************************************************** -->
    <xsl:template match="cs:CommitalCases">
        <xsl:param name="language"/>
        <br/>
        <font size="1">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'COMMITTALS FOR SENTENCE'"/>
            </xsl:call-template>
        </font>
        <br/>
        <table class="detail" width="92%">
            <!-- call template to display Committal Case Header info -->
            <xsl:call-template name="displayCommittalTable">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
            <!-- Display case and defendant info -->
            <xsl:for-each select="./cs:Case">
                <xsl:variable name="case" select="."/>
                <xsl:variable name="caseNum" select="./cs:CaseNumber"/>
                <xsl:for-each select="./cs:Defendants/cs:Defendant">
                    <tr>
                        <td valign="top" align="right">
                            <!--Defendant numbering-->
                            <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                <xsl:if test="position()=1">
                                    <font size="1">
                                        <xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
                                    </font>
                                </xsl:if>
                                <xsl:if test="position()!=1">
                                    <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                        <tr valign="top">
                                            <td width="100%" align="right" valign="top">
                                                <font size="1">
                                                                        -<xsl:number value="position()" format="001"/>
                                                </font>
                                            </td>
                                        </tr>
                                    </table>
                                </xsl:if>
                            </xsl:if>
                            <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                <font size="1">
                                    <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                </font>
                            </xsl:if>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                    </xsl:call-template>
                                    <br/>
                                    <!-- Display defendants Forename if exists -->
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
                                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
                                        <br/>
                                    </xsl:if>
                                    <!-- Check to see if there is a defendants Middle Name and display -->
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
                                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
                                        <br/>
                                    </xsl:if>
                                </xsl:if>
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                    </xsl:call-template>
                                    <br/>
                                </xsl:if>
                                <!-- check to see if sex or DOB -->
                                <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown' or cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                    <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                        <tr>
                                            <td width="25%" valign="top">
                                                <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                                    <font size="1">
                                                        <xsl:variable name="gender">
                                                            <xsl:call-template name="TitleCase">
                                                                <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                                                            </xsl:call-template>
                                                        </xsl:variable>
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="$gender"/>
                                                        </xsl:call-template>
                                                    </font>
                                                </xsl:if>
                                            </td>
                                            <td width="75%" valign="top">
                                                <xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                    <font size="1">
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="'Date of Birth:'"/>
                                                        </xsl:call-template>
                                                        <xsl:text> </xsl:text>
                                                        <xsl:call-template name="FormatDate">
                                                            <xsl:with-param name="input">
                                                                <xsl:value-of select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                                                            </xsl:with-param>
                                                            <xsl:with-param name="language" select="$language"/>
                                                        </xsl:call-template>
                                                    </font>
                                                </xsl:if>
                                            </td>
                                        </tr>
                                    </table>
                                </xsl:if>
                                <!-- Display solicitor details -->
                                <xsl:for-each select="cs:Counsel/cs:Solicitor">
                                    <xsl:call-template name="solicitor">
                                        <xsl:with-param name="party" select="cs:Party"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:for-each>
                                <!-- Display any charge information -->
                                <!--if defendant is the first defendant then display all the charge info-->
                                <xsl:if test="position()=1">
                                    <xsl:if test="cs:AdditionalNotes">
                                        <br/>
                                        <xsl:variable name="chargeText">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'CHARGES'"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="TitleCase">
                                            <xsl:with-param name="text" select="$chargeText"/>
                                        </xsl:call-template>
                                        <xsl:text>:  </xsl:text>
                                    </xsl:if>
                                    <xsl:value-of select="cs:AdditionalNotes"/>
                                </xsl:if>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <!-- Display custody Status -->
                                <xsl:call-template name="DisplayCustodyStatus">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <!--Display info depending on method of instigation -->
                                <xsl:if test="$case/cs:MethodOfInstigation">
                                    <xsl:call-template name="DispMethInst">
                                        <xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
                                        <xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                        <xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
                                        <xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                    <br/>
                                </xsl:if>
                                <!--what if no method of instigation? - show committing court stuff or appeal case description-->
                                <xsl:if test="not($case/cs:MethodOfInstigation)">
                                    <!-- Display originating court details -->
                                    <xsl:if test="$case/cs:CaseArrivedFrom">
                                        <xsl:call-template name="do-replace">
                                            <xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
                                            <xsl:text> CC</xsl:text>
                                        </xsl:if>
                                        <br/>
                                    </xsl:if>
                                    <xsl:if test="$case/cs:AppealCaseDescription">
                                        <br/>
                                        <xsl:value-of select="$case/cs:AppealCaseDescription"/>
                                        <br/>
                                    </xsl:if>
                                </xsl:if>
                                <!-- Display prosecution details -->
                                <xsl:if test="$case/cs:Prosecution">
                                    <xsl:variable name="prosOrg">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
                                        </xsl:call-template>
                                    </xsl:variable>
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="$prosOrg"/>
                                    </xsl:call-template>
                                    <br/>                                    
                                    <xsl:if test="$case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service'">
                                        <xsl:if test="./cs:URN">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'PTI Unique Ref:'"/>
                                            </xsl:call-template>
                                            <xsl:value-of select="./cs:URN"/>
                                        </xsl:if>
                                    </xsl:if>   
                                    <br/>
                                </xsl:if>
                            </font>
                        </td>
                        <td valign="top">
                            <xsl:if test="$case/cs:DateOfInstigation">
                                <font size="1">
                                    <xsl:call-template name="displayDate">
                                        <xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </font>
                            </xsl:if>
                        </td>
                    </tr>
                    <tr/>
                    <!--No deft/appllt? then display fixed text-->
                    <tr>
                        <td colspan="5" align="center">
                            <font size="1">
                                <xsl:call-template name="noDeftText">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </font>
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:for-each>
        </table>
    </xsl:template>
    <!-- Display Crown Court Details -->
    <xsl:template match="cs:CrownCourt">
        <xsl:param name="language"/>
        <font size="5" style="font-family: Helvetica;">
            <xsl:variable name="theCC">
                <xsl:text>The </xsl:text>
                <xsl:value-of select="cs:CourtHouseType"/>
            </xsl:variable>
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="$theCC"/>
            </xsl:call-template>
        </font>
        <br/>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="'at'"/>
        </xsl:call-template>
        <xsl:text> </xsl:text>
        <xsl:variable name="courthousename">
            <xsl:call-template name="TitleCase">
                <xsl:with-param name="text" select="cs:CourtHouseName"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="$courthousename"/>
        </xsl:call-template>
        <br/>
        <font size="3" style="font-family:Ariel">
            <xsl:text>  </xsl:text>
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Running List for period ending'"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
            <xsl:call-template name="displayDate">
                <xsl:with-param name="input">
                    <xsl:value-of select="$reportdate"/>
                </xsl:with-param>
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </font>
        <br/>
        <br/>
    </xsl:template>
    <!-- Display Trial Case Information                                                                     ***************************************************************************************************************************************-->
    <xsl:template match="cs:TrialCases">
        <xsl:param name="language"/>
        <font size="1">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'TRIAL CASES'"/>
            </xsl:call-template>
        </font>
        <br/>
        <table class="detail" width="92%">
            <!-- Call template to display header information for Trial Cases -->
            <xsl:call-template name="displayTrialTable">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
            <!-- Display Case and Defendant Information -->
            <xsl:for-each select="./cs:Case">
                <xsl:variable name="case" select="."/>
                <xsl:variable name="caseNum" select="./cs:CaseNumber"/>
                <xsl:for-each select="./cs:Defendants/cs:Defendant">
                    <tr>
                        <td valign="top" align="right">
                            <!--Defendant numbering-->
                            <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                <xsl:if test="position()=1">
                                    <font size="1">
                                        <xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
                                    </font>
                                </xsl:if>
                                <xsl:if test="position()!=1">
                                    <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                        <tr valign="top">
                                            <td width="100%" align="right" valign="top">
                                                <font size="1">
                                                                        -<xsl:number value="position()" format="001"/>
                                                </font>
                                            </td>
                                        </tr>
                                    </table>
                                </xsl:if>
                            </xsl:if>
                            <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                <font size="1">
                                    <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                </font>
                            </xsl:if>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                    </xsl:call-template>
                                    <br/>
                                    <!-- Display defendants Forename if exists -->
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
                                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
                                        <br/>
                                    </xsl:if>
                                    <!-- Check to see if there is a defendants Middle Name and display -->
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
                                        <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
                                        <br/>
                                    </xsl:if>
                                </xsl:if>
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                    </xsl:call-template>
                                    <br/>
                                </xsl:if>
                                <!-- check to see if sex or DOB -->
                                <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown' or cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                    <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                        <tr>
                                            <td width="25%" valign="top">
                                                <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                                    <font size="1">
                                                        <xsl:variable name="gender">
                                                            <xsl:call-template name="TitleCase">
                                                                <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                                                            </xsl:call-template>
                                                        </xsl:variable>
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="$gender"/>
                                                        </xsl:call-template>
                                                    </font>
                                                </xsl:if>
                                            </td>
                                            <td width="75%" valign="top">
                                                <xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                    <font size="1">
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="'Date of Birth:'"/>
                                                        </xsl:call-template>
                                                        <xsl:call-template name="FormatDate">
                                                            <xsl:with-param name="input">
                                                                <xsl:value-of select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                                                            </xsl:with-param>
                                                            <xsl:with-param name="language" select="$language"/>
                                                        </xsl:call-template>
                                                    </font>
                                                </xsl:if>
                                            </td>
                                        </tr>
                                    </table>
                                </xsl:if>
                                <!-- Display hearing info if exists -->
                                <xsl:if test="$case/cs:Hearing/cs:HearingDescription">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="$case/cs:Hearing/cs:HearingDescription"/>
                                    </xsl:call-template>
                                    <xsl:text>:  </xsl:text>
                                    <xsl:call-template name="FormatDate">
                                        <xsl:with-param name="input" select="$case/cs:Hearing/cs:HearingDate"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:if>
                                <!-- Display solicitor details -->
                                <xsl:for-each select="cs:Counsel/cs:Solicitor">
                                    <br/>
                                    <xsl:call-template name="solicitor">
                                        <xsl:with-param name="party" select="cs:Party"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:for-each>
                                <!-- Display any charge information -->
                                <!--if defendant is the first defendant then display all the charge info-->
                                <xsl:if test="position()=1">
                                    <xsl:if test="cs:AdditionalNotes">
                                        <br/>
                                        <xsl:variable name="chargeText">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'CHARGES'"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="TitleCase">
                                            <xsl:with-param name="text" select="$chargeText"/>
                                        </xsl:call-template>
                                        <xsl:text>:  </xsl:text>
                                    </xsl:if>
                                    <xsl:value-of select="cs:AdditionalNotes"/>
                                </xsl:if>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <!-- Display custody Status -->
                                <xsl:call-template name="DisplayCustodyStatus">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <!--Committing Court/Method of Instigation-->
                                <xsl:if test="$case/cs:MethodOfInstigation">
                                    <xsl:call-template name="DispMethInst">
                                        <xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
                                        <xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                        <xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
                                        <xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                    <br/>
                                </xsl:if>
                                <!--what if no method of instigation? - show committing court stuff or appeal case description-->
                                <xsl:if test="not($case/cs:MethodOfInstigation)">
                                    <!-- Display originating court details -->
                                    <xsl:if test="$case/cs:CaseArrivedFrom">
                                        <xsl:call-template name="do-replace">
                                            <xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'CC'"/>
                                            </xsl:call-template>
                                        </xsl:if>
                                        <br/>
                                    </xsl:if>
                                    <xsl:if test="$case/cs:AppealCaseDescription">
                                        <xsl:value-of select="$case/cs:AppealCaseDescription"/>
                                        <br/>
                                    </xsl:if>
                                </xsl:if>
                                <!-- Display prosecuting organisation details -->
                                <xsl:if test="$case/cs:Prosecution">
                                    <xsl:variable name="prosOrg">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
                                        </xsl:call-template>
                                    </xsl:variable>
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="$prosOrg"/>
                                    </xsl:call-template>
                                    <br/>                                    
                                    <xsl:if test="$case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service'">
                                        <xsl:if test="./cs:URN">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'PTI Unique Ref:'"/>
                                            </xsl:call-template>
                                            <xsl:value-of select="./cs:URN"/>
                                        </xsl:if>
                                    </xsl:if>                                        
                                    <br/>
                                </xsl:if>
                            </font>
                        </td>
                        <td valign="top">
                            <font size="1">
                                <xsl:if test="$case/cs:DateOfInstigation">
                                    <xsl:call-template name="displayDate">
                                        <xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:if>
                                <br/>
                                <!--Display class of case for first defendant on each case only-->
                                <xsl:if test="position()=1">
                                    <xsl:if test="$case/cs:CaseClassNumber">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'Class'"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="$case/cs:CaseClassNumber"/>
                                    </xsl:if>
                                </xsl:if>
                            </font>
                        </td>
                    </tr>
                    <tr/>
                    <!--No deft/appllt? then display fixed text-->
                    <tr>
                        <td colspan="5" align="center">
                            <font size="1">
                                <xsl:call-template name="noDeftText">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </font>
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:for-each>
        </table>
    </xsl:template>
    <!--        
            ************************
            TEMPLATE NAMES
            ***********************
    -->
    <!-- Display Appeal Case Header Info -->
    <xsl:template name="displayAppealTable">
        <xsl:param name="language"/>
        <!-- Display header info -->
        <tr>
            <td width="10%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CASE/DEFT.NO:'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="30%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'APPELLANT NAME/SEX/'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF BIRTH/SOLICITORS'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="10%" valign="top">
                <font size="1">
                    <xsl:variable name="bcStatus">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'B/C STATUS'"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="substring-before($bcStatus,' ')"/>
                    <br/>
                    <xsl:value-of select="substring-after($bcStatus,' ')"/>
                    <xsl:text>:</xsl:text>
                </font>
            </td>
            <td width="30%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'MAGISTRATES / TRANSFERRING COURT/'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'APPEAL DESCRIPTION'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'(RESPONDENT)'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="15%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF NOTICE'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'OF APPEAL /'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'TRANSFER/EXEC'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                </font>
            </td>
        </tr>
        <tr>
            <td colspan="5">
                <hr/>
            </td>
        </tr>
    </xsl:template>
    <!-- Display Committal Case Header Info -->
    <xsl:template name="displayCommittalTable">
        <xsl:param name="language"/>
        <!-- Dispay Header information -->
        <tr>
            <td width="10%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CASE/DEFT.NO:'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="30%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DEFENDANT NAME/SEX/'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF BIRTH/SOLICITORS'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CHARGES'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                </font>
            </td>
            <td width="10%" valign="top">
                <font size="1">
                    <xsl:variable name="bcStatus">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'B/C STATUS'"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="substring-before($bcStatus,' ')"/>
                    <br/>
                    <xsl:value-of select="substring-after($bcStatus,' ')"/>
                    <xsl:text>:</xsl:text>
                </font>
            </td>
            <td width="30%" valign="top">
                <font size="1">
                    <xsl:variable name="commitTransferCourt">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'COMMITTING/TRANSFERRING COURT:'"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="substring-before($commitTransferCourt,'/')"/>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:value-of select="substring-after($commitTransferCourt,'/')"/>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'(PROSECUTOR)'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="15%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF COMMITTAL'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'EXEC/TRANSFER/'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'BRING BACK:'"/>
                    </xsl:call-template>
                </font>
            </td>
        </tr>
        <tr>
            <td colspan="5">
                <hr/>
            </td>
        </tr>
    </xsl:template>
    <!-- Display Trial Case Header Info -->
    <xsl:template name="displayTrialTable">
        <xsl:param name="language"/>
        <!-- Display header information -->
        <tr>
            <td width="10%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CASE/DEFT.NO:'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="30%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DEFENDANT NAME/SEX/'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF BIRTH/SOLICITORS'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CHARGES'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'PDH/PRELIMINARY HRG DATE'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="10%" valign="top">
                <font size="1">
                    <xsl:variable name="bcStatus">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'B/C STATUS'"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="substring-before($bcStatus,' ')"/>
                    <br/>
                    <xsl:value-of select="substring-after($bcStatus,' ')"/>
                    <xsl:text>:</xsl:text>
                </font>
            </td>
            <td width="30%" valign="top">
                <font size="1">
                    <xsl:variable name="commitTransferCourt">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'COMMITTING/TRANSFERRING COURT:'"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="substring-before($commitTransferCourt,'/')"/>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:value-of select="substring-after($commitTransferCourt,'/')"/>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'(PROSECUTOR)'"/>
                    </xsl:call-template>
                </font>
            </td>
            <td width="15%" valign="top">
                <font size="1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF COMMITTAL'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'TC/SENT/VB SIGNED/'"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'TRANSFER/EXEC'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                    <br/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'/RE-HEARING ORDERED'"/>
                    </xsl:call-template>
                </font>
            </td>
        </tr>
        <tr>
            <td colspan="5">
                <hr/>
            </td>
        </tr>
    </xsl:template>
    <!-- Display the date in a specified format -->
    <xsl:template name="displayDate">
        <xsl:param name="input"/>
        <xsl:param name="language"/>
        <xsl:variable name="month" select="substring($input,6,2)"/>
        <xsl:variable name="day" select="substring($input,9,2)"/>
        <xsl:variable name="year" select="substring($input,1,4)"/>
        <xsl:value-of select="$day"/>
        <xsl:text> </xsl:text>
        <xsl:choose>
            <xsl:when test="$month='01'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'January'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='02'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'February'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='03'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'March'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='04'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'April'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='05'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'May'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='06'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'June'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='07'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'July'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='08'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'August'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='09'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'September'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='10'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'October'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='11'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'November'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='12'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'December'"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$year"/>
    </xsl:template>
    <!-- Template used to display the date in a specific format -->
    <xsl:template name="FormatDate">
        <xsl:param name="input"/>
        <xsl:param name="language"/>
        <xsl:variable name="month" select="substring($input,6,2)"/>
        <xsl:variable name="day" select="substring($input,9,2)"/>
        <xsl:variable name="year" select="substring($input,1,4)"/>
        <xsl:value-of select="$day"/>
        <xsl:text>-</xsl:text>
        <xsl:choose>
            <xsl:when test="$month='01'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'JAN'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='02'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'FEB'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='03'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'MAR'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='04'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'APR'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='05'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'MAY'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='06'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'JUN'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='07'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'JUL'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='08'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'AUG'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='09'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'SEP'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='10'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'OCT'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='11'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'NOV'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$month='12'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'DEC'"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
        <xsl:text>-</xsl:text>
        <xsl:value-of select="$year"/>
    </xsl:template>
    <!-- Template to format the name to a specific format -->
    <xsl:template name="FormalName">
        <xsl:param name="name"/>
        <xsl:variable name="newname">
            <xsl:value-of select="$name/apd:CitizenNameTitle"/>
            <xsl:text> </xsl:text>
            <xsl:for-each select="$name/apd:CitizenNameForename">
                <xsl:value-of select="substring(.,1,1)"/>
            </xsl:for-each>
            <xsl:text> </xsl:text>
            <xsl:value-of select="$name/apd:CitizenNameSurname"/>
            <xsl:for-each select="$name/apd:CitizenNameSuffix">
                <xsl:text> </xsl:text>
                <xsl:value-of select="."/>
            </xsl:for-each>
        </xsl:variable>
        <xsl:copy-of select="$newname"/>
    </xsl:template>
    <!-- display the footer information -->
    <xsl:template name="listfooter">
        <xsl:param name="language"/>
        <!-- call template to display details -->
        <xsl:call-template name="listFooterDisplay">
            <xsl:with-param name="court" select="/cs:RunningList/cs:CrownCourt"/>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
    </xsl:template>
    <!-- template to construct footer information -->
    <xsl:template name="listFooterDisplay">
        <xsl:param name="language"/>
        <!-- creates the footer in the output -->
        <xsl:param name="court"/>
        <hr/>
        <table class="detail" width="92%">
            <tr>
                <td align="left">
                    <font size="1">
                        <!-- Display court address details -->
                        <xsl:if test="$court/cs:CourtHouseAddress">
                            <xsl:for-each select="$court/cs:CourtHouseAddress/apd:Line[not (.='-') and not (.=' ')]">
                                <xsl:variable name="addressLine">
                                    <xsl:call-template name="TitleCase">
                                        <xsl:with-param name="text" select="."/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="$addressLine"/>
                                </xsl:call-template>
                                <xsl:if test="not (position() = last())">
                                    <xsl:if test="string-length() &gt; 0">
                                        <xsl:text>, </xsl:text>
                                    </xsl:if>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="$court/cs:CourtHouseAddress/apd:PostCode"/>
                            <xsl:text>. </xsl:text>
                        </xsl:if>
                        <xsl:if test="$court/cs:CourtHouseDX">
                            <xsl:value-of select="$court/cs:CourtHouseDX"/>
                        </xsl:if>
                        <xsl:if test="$court/cs:CourtHouseTelephone">
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Tel'"/>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="$court/cs:CourtHouseTelephone"/>
                        </xsl:if>
                        <xsl:if test="$court/cs:CourtHouseFax">
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Fax'"/>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="$court/cs:CourtHouseFax"/>
                        </xsl:if>
                    </font>
                </td>
            </tr>
            <tr>
                <td align="right">
                    <font size="1">
                        <!-- display print reference -->
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Ref:'"/>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="cs:RunningList /cs:ListHeader/cs:CRESTprintRef"/>
                    </font>
                </td>
            </tr>
            <xsl:if test="$language != $DefaultLang">
                <tr>
                    <td width="100%">
                        <font size="1">
                            <xsl:call-template name="NOT_FOUND_FOOTER">
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                        </font>
                    </td>
                </tr>
            </xsl:if>
        </table>
    </xsl:template>
    <!-- Display Copyright Info -->
    <xsl:template name="displayCopyright">
        <xsl:param name="language"/>
        <xsl:text>&#x00A9; </xsl:text>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="'Crown copyright 2003. All rights reserved. Issued by HM Courts '"/>
        </xsl:call-template>
        <xsl:if test="$language = $DefaultLang">
            <xsl:text>&amp;</xsl:text>
        </xsl:if>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="' Tribunals Service.'"/>
        </xsl:call-template>
    </xsl:template>
    <!-- Display the published Details -->
    <!-- <xsl:template name="publishDate">
        <xsl:param name="language"/>

        <table width="100%">
            <tr>
                <td align="left">
                    <xsl:text>Published: </xsl:text>
                    <xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime"/>
                    <xsl:call-template name="displayDate">
                        <xsl:with-param name="input">
                            <xsl:value-of select="substring($pubTime,1,10)"/>
                        </xsl:with-param>
                    </xsl:call-template>
                at <xsl:value-of select="substring($pubTime,12,5)"/>
                </td>
                <td align="right">
            </td>
            </tr>
        </table>
    </xsl:template> -->
    <!-- Display solicitor details -->
    <xsl:template name="solicitor">
        <xsl:param name="party"/>
        <xsl:param name="language"/>
        <!-- Put in condition to not display the Solicitor details if the cs:EndDate is populated which means 
        that the solicitor is no longer representing the defendant -->
        <xsl:if test="string-length(cs:EndDate) != 10 and string-length(cs:EndDate) != 9">
            <xsl:choose>
                <xsl:when test="$party/cs:Organisation or $party/cs:Person">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Sols'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                    <xsl:choose>
                        <xsl:when test="$party/cs:Organisation">
                            <xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
                            <br/>
                            <xsl:if test="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Tel'"/>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                                <xsl:value-of select="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- Format the name -->
                            <xsl:call-template name="FormalName">
                                <xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name"/>
                            </xsl:call-template>
                            <br/>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Tel'"/>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="$party/cs:Person/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    <!-- Template to make the first character a capital and rest in lower case -->
    <xsl:template name="TitleCase">
        <xsl:param name="text"/>
        <xsl:param name="lastletter" select="' '"/>
        <xsl:if test="$text">
            <xsl:variable name="thisletter" select="substring($text,1,1)"/>
            <xsl:choose>
                <xsl:when test="$lastletter=' '">
                    <xsl:value-of select="translate($thisletter,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="translate($thisletter,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="TitleCase">
                <xsl:with-param name="text" select="substring($text,2)"/>
                <xsl:with-param name="lastletter" select="$thisletter"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    <!-- template used to convert a string to upper case -->
    <xsl:template name="toUpper">
        <xsl:param name="content"/>
        <xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    </xsl:template>
    <!-- Replace Instances of 'Magistrates Court with MC' in originating court names-->
    <!-- Replace Instances of 'Youth Court with YC' in originating court names-->
    <!-- Replace Instances of 'Crown Court with CC' in originating court names-->
    <xsl:param name="MCreplace" select='" MAGISTRATES&apos; COURT"'/>
    <xsl:param name="MCby" select="'MC'"/>
    <xsl:param name="YCreplace" select='" YOUTH COURT"'/>
    <xsl:param name="YCby" select="'YC'"/>
    <xsl:param name="CCreplace" select='" CROWN COURT"'/>
    <xsl:param name="CCby" select="'CC'"/>
    <xsl:template name="do-replace">
        <xsl:param name="text"/>
        <xsl:param name="language"/>
        <xsl:choose>
            <xsl:when test="contains($text, $MCreplace)">
                <xsl:variable name="crtName">
                    <xsl:call-template name="TitleCase">
                        <xsl:with-param name="text" select="substring-before($text, $MCreplace)"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$crtName"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$MCby"/>
                </xsl:call-template>
                <xsl:call-template name="do-replace">
                    <xsl:with-param name="text" select="substring-after($text, $MCreplace)"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains($text, $YCreplace)">
                <xsl:variable name="crtName">
                    <xsl:call-template name="TitleCase">
                        <xsl:with-param name="text" select="substring-before($text, $YCreplace)"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$crtName"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$YCby"/>
                </xsl:call-template>
                <xsl:call-template name="do-replace">
                    <xsl:with-param name="text" select="substring-after($text, $YCreplace)"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains($text, $CCreplace)">
                <xsl:variable name="crtName">
                    <xsl:call-template name="TitleCase">
                        <xsl:with-param name="text" select="substring-before($text, $CCreplace)"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$crtName"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$CCby"/>
                </xsl:call-template>
                <xsl:call-template name="do-replace">
                    <xsl:with-param name="text" select="substring-after($text, $CCreplace)"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="DispMethInst">
        <xsl:param name="methInst"/>
        <xsl:param name="court"/>
        <xsl:param name="courtType"/>
        <xsl:param name="caseType"/>
        <xsl:param name="language"/>
        <xsl:variable name="upperCourtType">
            <xsl:call-template name="toUpper">
                <xsl:with-param name="content" select="$courtType"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="newCourt">
            <xsl:choose>
                <xsl:when test="$courtType = 'Crown Court' and not(contains($court,$CCreplace))">
                    <xsl:value-of select="$court"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="$upperCourtType"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$court"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$methInst='Committal'">
                <!-- Display originating court details -->
                <xsl:call-template name="do-replace">
                    <xsl:with-param name="text" select="$newCourt"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$methInst='Voluntary bill'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Voluntary Bill'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$methInst='Sending'">
            </xsl:when>
            <xsl:when test="$methInst='Execution'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Bench Warrant Executed'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$methInst='Transfer'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'TI'"/>
                </xsl:call-template>
                <br/>
                <xsl:call-template name="do-replace">
                    <xsl:with-param name="text" select="$newCourt"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$methInst='Transfer certificated'">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Transfer Certificate Case'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$methInst='Rehearing ordered'">
                <!-- if case type = A display Re-hearing all else display Re-trial -->
                <xsl:choose>
                    <xsl:when test="$caseType = 'A'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Re-hearing'"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Re-trial'"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <!--covers 'CMC' and empty string methods of instigation  -->
                <!-- Display originating court details -->
                <xsl:call-template name="do-replace">
                    <xsl:with-param name="text" select="$newCourt"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!--    <xsl:if test="$case/cs:AppealCaseDescription">-->
                <!--    <xsl:value-of select="$case/cs:AppealCaseDescription"/>-->
                <!--    </xsl:if>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="noDeftText">
        <xsl:param name="language"/>
        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'**********Warning: Case party details are incomplete in the above case*********'"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    <!-- Display Custody Details -->
    <xsl:template name="DisplayCustodyStatus">
        <xsl:param name="language"/>
        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
            <xsl:if test="not(cs:CustodyStatus)">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'N/A'"/>
                </xsl:call-template>
            </xsl:if>
            <xsl:if test="(cs:CustodyStatus)">
                <xsl:choose>
                    <xsl:when test="cs:CustodyStatus='Not applicable'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'N/A'"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="not (contains(cs:CustodyStatus,'Not applicable'))">
                        <xsl:variable name="custody">
                            <xsl:call-template name="TitleCase">
                                <xsl:with-param name="text" select="cs:CustodyStatus"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="$custody"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'N/A'"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
