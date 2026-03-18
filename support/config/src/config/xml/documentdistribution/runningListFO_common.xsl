<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>
    <!-- Template to set up the page [FO Only] -->
    <xsl:template name="PageSetUp">
        <fo:layout-master-set>
            <fo:simple-page-master master-name="first" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                <fo:region-body margin-top="30mm" margin-bottom="20mm"/>
                <fo:region-before extent="30mm" region-name="header-first"/>
                <fo:region-after extent="20mm" region-name="footer-first"/>
            </fo:simple-page-master>
            <!-- set up page for rest of pages -->
            <fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                <fo:region-body margin-top="30mm" margin-bottom="20mm"/>
                <fo:region-before extent="30mm" region-name="header-rest"/>
                <fo:region-after extent="15mm" region-name="footer-rest"/>
            </fo:simple-page-master>
            <fo:page-sequence-master master-name="document">
                <!-- set up page conditions -->
                <fo:repeatable-page-master-alternatives>
                    <fo:conditional-page-master-reference page-position="first" master-reference="first"/>
                    <fo:conditional-page-master-reference page-position="rest" master-reference="rest"/>
                </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
        </fo:layout-master-set>
    </xsl:template>
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
        <fo:page-sequence master-reference="document" initial-page-number="1" force-page-count="no-force">
            <!-- Display Crown Court Details - header for first page-->
            <fo:static-content flow-name="header-first">
                <xsl:for-each select="cs:RunningList/cs:CrownCourt">
                    <xsl:call-template name="HeaderInfoFirst">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </xsl:for-each>
            </fo:static-content>
            <!-- Display footer info -->
            <fo:static-content flow-name="footer-first">
                <fo:block>
                    <xsl:call-template name="listfooter">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </fo:static-content>
            <!--Header for subsequent Pages-->
            <fo:static-content flow-name="header-rest">
                <fo:block>
                    <xsl:call-template name="HeaderInfoOtherPages">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </fo:static-content>
            <!-- Display footer info -->
            <fo:static-content flow-name="footer-rest">
                <fo:block>
                    <xsl:call-template name="FooterInfoOtherPages">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </fo:static-content>
            <fo:flow flow-name="xsl-region-body">
                <fo:block>
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
                    <fo:block text-align="center" font-weight="bold" space-before="10pt" space-after="10pt">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'End of Report'"/>
                        </xsl:call-template>
                    </fo:block>
                    <fo:block>
                        <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                    </fo:block>
                    <fo:block id="{$language}"/>
                </fo:block>
            </fo:flow>
        </fo:page-sequence>
    </xsl:template>
    <!--        
            **************************
            TEMPLATE MATCHES 
            *************************
    -->
    <!-- Display Appeal Case Info ***********************************************************************************************************************************************************-->
    <xsl:template match="cs:AppealCases">
        <xsl:param name="language"/>
        <fo:block space-after="12pt" font-size="9pt" space-before="12pt">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'APPEALS TO THE CROWN COURT'"/>
            </xsl:call-template>
        </fo:block>
        <fo:block/>
		<fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="5mm"/>
            <fo:table-column column-width="60mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="45mm"/>
            <fo:table-column column-width="30mm"/>
            <fo:table-body font-weight="normal" font-size="9pt">
                <!-- call template to display Appeal Case Header info -->
                <xsl:call-template name="displayAppealTable">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <xsl:for-each select="./cs:Case">
                    <xsl:variable name="case" select="."/>
                    <xsl:variable name="caseNum" select="./cs:CaseNumber"/>
                    <xsl:for-each select="./cs:Defendants/cs:Defendant">
                        <fo:table-row>
                            <fo:table-cell>
                                <!-- Just display the case number on it's own -->
                                <fo:block space-after="5pt">
                                    <xsl:value-of select="$caseNum"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell/>
                            <fo:table-cell>
                                <fo:block space-after="5pt" hyphenate="true" language="en">
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                        <xsl:call-template name="toUpper">
                                            <xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                        <!-- Display Defendant forename -->
                                        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
                                            <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
                                            <fo:block/>
                                        </xsl:if>
                                        <!-- Check to see if there is a defendant middlename and display-->
                                        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
                                            <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
                                            <fo:block/>
                                        </xsl:if>
                                    </xsl:if>
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                    </xsl:if>
                                    <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                        <xsl:variable name="gender">
                                            <xsl:call-template name="TitleCase">
                                                <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="$gender"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                        <xsl:text>&#160;&#160;&#160;&#160;</xsl:text>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'Date of Birth:'"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                        <xsl:call-template name="FormatDate">
                                            <xsl:with-param name="input" select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <!-- Display solicitor info -->
                                    <xsl:for-each select="cs:Counsel/cs:Solicitor">
                                        <xsl:call-template name="solicitor">
                                            <xsl:with-param name="party" select="cs:Party"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </xsl:for-each>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!-- Display custody Status -->
                                    <xsl:call-template name="DisplayCustodyStatus">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!--Committing Court/Method of Instigation-->
                                    <xsl:if test="$case/cs:MethodOfInstigation">
                                        <xsl:call-template name="DispMethInst">
                                            <xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
                                            <xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                            <xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
                                            <xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <fo:block/>
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
                                            <fo:block/>
                                        </xsl:if>
                                        <xsl:if test="$case/cs:AppealCaseDescription">
                                            <xsl:value-of select="$case/cs:AppealCaseDescription"/>
                                            <fo:block/>
                                        </xsl:if>
                                    </xsl:if>
                                    <!-- Display prosecution info -->
                                    <xsl:choose>
                                        <xsl:when test="$case/cs:Prosecution">
                                            <xsl:variable name="prosOrg">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
                                                </xsl:call-template>
                                            </xsl:variable>
                                            <xsl:call-template name="toUpper">
                                                <xsl:with-param name="content" select="$prosOrg"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>** </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'NO RESPONDENT'"/>
                                            </xsl:call-template>
                                            <xsl:text> **</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <fo:block/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <xsl:if test="$case/cs:DateOfInstigation">
                                    <fo:block>
                                        <xsl:call-template name="displayDate">
                                            <xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </fo:block>
                                </xsl:if>
                            </fo:table-cell>
                        </fo:table-row>
                        <!--No deft/appllt? then display fixed text-->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" text-align="center">
                                <fo:block>
                                    <xsl:call-template name="noDeftText">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
		</fo:block>
    </xsl:template>
    <!-- Display Committal Case info *********************************************************************************************************************************************************-->
    <xsl:template match="cs:CommitalCases">
        <xsl:param name="language"/>
        <fo:block space-after="12pt" font-size="9pt" space-before="12pt">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'COMMITTALS FOR SENTENCE'"/>
            </xsl:call-template>
        </fo:block>
        <fo:block/>
		<fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="5mm"/>
            <fo:table-column column-width="60mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="45mm"/>
            <fo:table-column column-width="30mm"/>
            <fo:table-body font-weight="normal" font-size="9pt">
                <!-- call template to display Committal Case Header info -->
                <xsl:call-template name="displayCommittalTable">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- Display case and defendant info -->
                <xsl:for-each select="./cs:Case">
                    <xsl:variable name="case" select="."/>
                    <xsl:variable name="caseNum" select="./cs:CaseNumber"/>
                    <xsl:for-each select="./cs:Defendants/cs:Defendant">
                        <fo:table-row>
                            <fo:table-cell>
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                    <xsl:if test="position()=1">
                                        <fo:block text-align="right">
                                            <xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="position()!=1">
                                        <fo:block text-align="right">
                                            -<xsl:number value="position()" format="001"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                        <fo:block space-after="5pt" text-align="right">
                                            <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                        </fo:block>
                                    </xsl:if>
                                </xsl:if>
                                <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                    <fo:block space-after="5pt" text-align="right">
                                        <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                    </fo:block>
                                </xsl:if>
                            </fo:table-cell>
                            <fo:table-cell/>
                            <fo:table-cell>
                                <fo:block space-after="5pt" hyphenate="true" language="en">
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                        <xsl:call-template name="toUpper">
                                            <xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                        <!-- Display Defendant forename -->
                                        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
                                            <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
                                            <fo:block/>
                                        </xsl:if>
                                        <!-- Check to see if there is a defendant middlename and display-->
                                        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
                                            <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
                                            <fo:block/>
                                        </xsl:if>
                                    </xsl:if>
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                    </xsl:if>
                                    <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                        <xsl:variable name="gender">
                                            <xsl:call-template name="TitleCase">
                                                <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="$gender"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                        <xsl:text>&#160;&#160;&#160;&#160;</xsl:text>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'Date of Birth:'"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                        <xsl:call-template name="FormatDate">
                                            <xsl:with-param name="input" select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <!-- Display solicitor info -->
                                    <xsl:for-each select="cs:Counsel/cs:Solicitor">
                                        <xsl:call-template name="solicitor">
                                            <xsl:with-param name="party" select="cs:Party"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </xsl:for-each>
                                    <!-- Display any charge info -->
                                    <!-- Display any charge information -->
                                    <!--if defendant is the first defendant then display all the charge info-->
                                    <xsl:if test="position()=1">
                                        <xsl:if test="cs:AdditionalNotes">
                                            <fo:block/>
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
                                    <fo:block/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!-- Display custody Status -->
                                    <xsl:call-template name="DisplayCustodyStatus">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!--Committing Court/Method of Instigation-->
                                    <xsl:if test="$case/cs:MethodOfInstigation">
                                        <xsl:call-template name="DispMethInst">
                                            <xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
                                            <xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                            <xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
                                            <xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <fo:block/>
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
                                            <fo:block/>
                                        </xsl:if>
                                        <xsl:if test="$case/cs:AppealCaseDescription">
                                            <xsl:value-of select="$case/cs:AppealCaseDescription"/>
                                            <fo:block/>
                                        </xsl:if>
                                    </xsl:if>
                                    <!-- Disply prosecution details -->
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
                                        <xsl:if test="$case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service'">
                                            <fo:block/>
                                            <xsl:if test="./cs:URN">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="'PTI Unique Ref:'"/>
                                                </xsl:call-template>
                                                <xsl:value-of select="./cs:URN"/>
                                                <fo:block/>
                                            </xsl:if>
                                        </xsl:if> 
                                    </xsl:if>
                                    <fo:block/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <xsl:if test="$case/cs:DateOfInstigation">
                                    <fo:block>
                                        <xsl:call-template name="displayDate">
                                            <xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </fo:block>
                                </xsl:if>
                            </fo:table-cell>
                        </fo:table-row>
                        <!--No deft/appllt? then display fixed text-->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" text-align="center">
                                <fo:block>
                                    <xsl:call-template name="noDeftText">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
        </fo:block>
		<!-- only insert page if there are appeal cases -->
        <xsl:if test="../cs:AppealCases">
            <fo:block break-before="page"/>
        </xsl:if>
    </xsl:template>
    <!-- Display Crown Court Details - Header for first page -->
    <xsl:template name="HeaderInfoFirst">
        <xsl:param name="language"/>
        <fo:block>
            <fo:table table-layout="fixed">
                <fo:table-column column-width="150mm"/>
                <fo:table-column column-width="30mm"/>
                <fo:table-body font-size="11pt">
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-size="16pt" font-family="Helvetica" font-weight="bold">
                                <xsl:variable name="theCC">
                                    <xsl:text>The </xsl:text>
                                    <xsl:value-of select="cs:CourtHouseType"/>
                                </xsl:variable>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="$theCC"/>
                                </xsl:call-template>
                            </fo:block>
                            <fo:block font-size="13pt">
                                <xsl:text> </xsl:text>
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
                            </fo:block>
                            <fo:block font-size="13pt">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Running List for period ending'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="displayDate">
                                    <xsl:with-param name="input" select="$reportdate"/>
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell text-align="right">
                            <fo:block font-size="7pt">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Page No'"/>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                                <fo:page-number/>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'of'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <fo:page-number-citation ref-id="{$language}"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <!-- Display Trial Case Information *****************************************************************************************************************************************************-->
    <xsl:template match="cs:TrialCases">
        <xsl:param name="language"/>
        <fo:block space-after="12pt" font-size="9pt" space-before="12pt">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'TRIAL CASES'"/>
            </xsl:call-template>
        </fo:block>
		<fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="5mm"/>
            <fo:table-column column-width="60mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="50mm"/>
            <fo:table-column column-width="30mm"/>
            <fo:table-body font-weight="normal" font-size="9pt">
                <!-- Call template to display header information for Trial Cases -->
                <xsl:call-template name="displayTrialTable">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- Display Case and Defendant Info -->
                <xsl:for-each select="./cs:Case">
                    <xsl:variable name="case" select="."/>
                    <xsl:variable name="caseNum" select="./cs:CaseNumber"/>
                    <xsl:for-each select="./cs:Defendants/cs:Defendant">
                        <fo:table-row>
                            <fo:table-cell vertical-align="top">
                                <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                    <xsl:if test="position()=1">
                                        <fo:block text-align="right">
                                            <xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="position()!=1">
                                        <fo:block text-align="right">
                                            -<xsl:number value="position()" format="001"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                        <fo:block space-after="5pt" text-align="right">
                                            <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                        </fo:block>
                                    </xsl:if>
                                </xsl:if>
                                <xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
                                    <fo:block space-after="5pt" text-align="right">
                                        <xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
                                    </fo:block>
                                </xsl:if>
                            </fo:table-cell>
                            <fo:table-cell/>
                            <fo:table-cell>
                                <fo:block space-after="5pt" hyphenate="true" language="en">
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
                                        <xsl:call-template name="toUpper">
                                            <xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                        <!-- Display Defendant forename -->
                                        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
                                            <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
                                            <fo:block/>
                                        </xsl:if>
                                        <!-- Check to see if there is a defendant middlename and display-->
                                        <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
                                            <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
                                            <fo:block/>
                                        </xsl:if>
                                    </xsl:if>
                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                    </xsl:if>
                                    <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                        <xsl:variable name="gender">
                                            <xsl:call-template name="TitleCase">
                                                <xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="$gender"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                        <xsl:text>&#160;&#160;&#160;&#160;</xsl:text>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'Date of Birth:'"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                        <xsl:call-template name="FormatDate">
                                            <xsl:with-param name="input" select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <!-- check to see if any hearing info and display -->
                                    <xsl:if test="$case/cs:Hearing/cs:HearingDescription">
                                        <fo:block/>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="$case/cs:Hearing/cs:HearingDescription"/>
                                        </xsl:call-template>
                                        <xsl:text>:  </xsl:text>
                                        <xsl:call-template name="FormatDate">
                                            <xsl:with-param name="input" select="$case/cs:Hearing/cs:HearingDate"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <fo:block/>
                                    </xsl:if>
                                    <!-- Display solicitor info -->
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
                                            <fo:block/>
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
                                    <fo:block/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!-- Display custody Status -->
                                    <xsl:call-template name="DisplayCustodyStatus">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!--Committing Court/Method of Instigation-->
                                    <xsl:if test="$case/cs:MethodOfInstigation">
                                        <xsl:call-template name="DispMethInst">
                                            <xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
                                            <xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
                                            <xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
                                            <xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <fo:block/>
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
                                            <fo:block/>
                                        </xsl:if>
                                        <xsl:if test="$case/cs:AppealCaseDescription">
                                            <xsl:value-of select="$case/cs:AppealCaseDescription"/>
                                            <fo:block/>
                                        </xsl:if>
                                    </xsl:if>
                                    <fo:block/>
                                    <!-- Display any prosecution info -->
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
                                        <xsl:if test="$case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service'">
                                            <fo:block/>
                                            <xsl:if test="./cs:URN">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="'PTI Unique Ref:'"/>
                                                </xsl:call-template>
                                                <xsl:value-of select="./cs:URN"/>
                                                <fo:block/>
                                            </xsl:if>
                                        </xsl:if>
                                    </xsl:if>
                                    <fo:block/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <xsl:if test="$case/cs:DateOfInstigation">
                                        <xsl:call-template name="displayDate">
                                            <xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                    <fo:block/>
                                    <!--only display class of case for first defendant on each case-->
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
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <!--No deft/appllt? then display fixed text-->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" text-align="center">
                                <fo:block>
                                    <xsl:call-template name="noDeftText">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
		</fo:block>
        <!-- only insert page if there are committal cases or appeal cases-->
        <xsl:if test="../cs:CommitalCases or ../cs:AppealCases">
            <fo:block break-before="page"/>
        </xsl:if>
    </xsl:template>
    <!--        
            ************************
            TEMPLATE NAMES
            ***********************
    -->
    <!-- Display Appeal Case Header Info -->
    <xsl:template name="displayAppealTable">
        <xsl:param name="language"/>
        <fo:table-row>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CASE/DEFT.NO:'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell/>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'APPELLANT NAME/SEX/'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF BIRTH/SOLICITORS'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <xsl:variable name="bcStatus">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'B/C STATUS'"/>
                    </xsl:call-template>
                </xsl:variable>
                <fo:block>
                    <xsl:value-of select="substring-before($bcStatus,' ')"/>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="substring-after($bcStatus,' ')"/>
                    <xsl:text>:</xsl:text>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'MAGISTRATES / TRANSFERRING COURT/'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'APPEAL DESCRIPTION'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'(RESPONDENT)'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF NOTICE'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'OF APPEAL /'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'TRANSFER/EXEC'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                </fo:block>
                <fo:block space-after="5pt"/>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
            <fo:table-cell number-columns-spanned="6">
                <fo:block>
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
    <!-- Display Committal Case Header Info -->
    <xsl:template name="displayCommittalTable">
        <xsl:param name="language"/>
        <fo:table-row>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CASE/DEFT.NO:'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell/>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DEFENDANT NAME/SEX/'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF BIRTH/SOLICITORS'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CHARGES'"/>
                    </xsl:call-template>
                    <xsl:text>:</xsl:text>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <xsl:variable name="bcStatus">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'B/C STATUS'"/>
                    </xsl:call-template>
                </xsl:variable>
                <fo:block>
                    <xsl:value-of select="substring-before($bcStatus,' ')"/>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="substring-after($bcStatus,' ')"/>
                    <xsl:text>:</xsl:text>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <xsl:variable name="commitTransferCourt">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'COMMITTING/TRANSFERRING COURT:'"/>
                    </xsl:call-template>
                </xsl:variable>
                <fo:block>
                    <xsl:value-of select="substring-before($commitTransferCourt,'/')"/>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="substring-after($commitTransferCourt,'/')"/>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'(PROSECUTOR)'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF COMMITTAL'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'EXEC/TRANSFER/'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'BRING BACK:'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block space-after="5pt"/>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
            <fo:table-cell number-columns-spanned="6">
                <fo:block>
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
    <!-- Template to display the date in a specific format -->
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
    <!-- Display Trial Case Header Info -->
    <xsl:template name="displayTrialTable">
        <xsl:param name="language"/>
        <fo:table-row>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CASE/DEFT.NO:'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell/>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DEFENDANT NAME/SEX/'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF BIRTH/SOLICITORS'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'CHARGES'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'PDH/PRELIMINARY HRG DATE'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <xsl:variable name="bcStatus">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'B/C STATUS'"/>
                    </xsl:call-template>
                </xsl:variable>
                <fo:block>
                    <xsl:value-of select="substring-before($bcStatus,' ')"/>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="substring-after($bcStatus,' ')"/>
                    <xsl:text>:</xsl:text>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <xsl:variable name="commitTransferCourt">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'COMMITTING/TRANSFERRING COURT:'"/>
                    </xsl:call-template>
                </xsl:variable>
                <fo:block>
                    <xsl:value-of select="substring-before($commitTransferCourt,'/')"/>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="substring-after($commitTransferCourt,'/')"/>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'(PROSECUTOR)'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell vertical-align="top">
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'DATE OF COMMITTAL'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'TC/SENT/VB SIGNED/'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'TRANSFER/EXEC'"/>
                    </xsl:call-template>
                    <xsl:text>/</xsl:text>
                </fo:block>
                <fo:block>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'/RE-HEARING ORDERED'"/>
                    </xsl:call-template>
                </fo:block>
                <fo:block space-after="5pt"/>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
            <fo:table-cell number-columns-spanned="6">
                <fo:block>
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" leader-length="100%"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
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
    <!-- Template to display the name in a specific format -->
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
        <!-- creates the footer in the output -->
        <xsl:param name="court"/>
        <xsl:param name="language"/>
        <fo:block>
            <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
        </fo:block>
        <fo:block font-size="8pt">
            <xsl:if test="$court/cs:CourtHouseAddress">
                <xsl:for-each select="$court/cs:CourtHouseAddress/apd:Line[not (.='-') and not (.=' ')]">
                    <xsl:variable name="addLine">
                        <xsl:call-template name="TitleCase">
                            <xsl:with-param name="text" select="."/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="$addLine"/>
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
        </fo:block>
        <fo:block text-align="right" font-size="8pt">
            <!-- display print reference -->
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Ref:'"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
            <xsl:value-of select="cs:RunningList /cs:ListHeader/cs:CRESTprintRef"/>
        </fo:block>
        <fo:block font-size="10pt">
            <xsl:call-template name="displayCopyright">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </fo:block>
        <xsl:if test="$language != $DefaultLang">
            <fo:block font-size="8pt">
                <xsl:call-template name="NOT_FOUND_FOOTER">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
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
    <xsl:template name="publishDate">
        <xsl:param name="language"/>
        <fo:block font-size="11pt" space-before="12pt">
            <xsl:text>Published: </xsl:text>
            <xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime"/>
            <xsl:call-template name="displayDate">
                <xsl:with-param name="input" select="substring($pubTime,1,10)"/>
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
            at <xsl:value-of select="substring($pubTime,12,5)"/>
        </fo:block>
    </xsl:template>
    <!-- Template to display Solicitor Info -->
    <xsl:template name="solicitor">
        <xsl:param name="party"/>
        <xsl:param name="language"/>
        <!-- Put in condition to not display the Solicitor details if the cs:EndDate is populated which means 
        that the solicitor is no longer representing the defendant -->
        <xsl:if test="string-length(cs:EndDate) != 10 and string-length(cs:EndDate) != 9">
            <xsl:choose>
                <xsl:when test="$party/cs:Organisation or $party/cs:Person">
                    <fo:block/>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Sols'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                    <xsl:choose>
                        <xsl:when test="$party/cs:Organisation">
                            <xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
                            <fo:block/>
                            <xsl:if test="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber">
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Tel'"/>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                                <xsl:value-of select="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="FormalName">
                                <xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name"/>
                            </xsl:call-template>
                            <fo:block/>
                            <xsl:text> </xsl:text>
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
    <!-- Template to make first character of string upper case and rest lower case -->
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
    <xsl:template name="HeaderInfoOtherPages">
        <xsl:param name="language"/>
        <fo:block>
            <!-- display the page no -->
            <fo:table table-layout="fixed">
                <fo:table-column column-width="180mm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-size="7pt" text-align="right">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Page No'"/>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                                <fo:page-number/>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'of'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <fo:page-number-citation ref-id="{$language}"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <xsl:template name="FooterInfoOtherPages">
        <xsl:param name="language"/>
        <fo:block text-align="right" font-size="8pt">
            <!-- display print reference -->
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Ref:'"/>
            </xsl:call-template>
            <xsl:value-of select="cs:RunningList /cs:ListHeader/cs:CRESTprintRef"/>
        </fo:block>
        <fo:block font-size="10pt">
            <xsl:call-template name="displayCopyright">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </fo:block>
        <xsl:if test="$language != $DefaultLang">
            <fo:block font-size="8pt">
                <xsl:call-template name="NOT_FOUND_FOOTER">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
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
                <fo:block/>
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
