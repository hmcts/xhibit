<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" 
xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:dt="http://xsltsl.org/date-time" 
xmlns="http://www.courtservice.gov.uk/schemas/courtservice" 
version="1.0">

    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>
    
    <!-- Template to set up the page [FO Only] -->
    <xsl:template name="PageSetUp">
        <fo:layout-master-set>
            <!-- set up page for first page -->
            <fo:simple-page-master master-name="first" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                <fo:region-body margin-top="15mm" margin-bottom="20mm"/>
                <fo:region-before extent="30mm" region-name="header-first"/>
                <fo:region-after extent="20mm"/>
            </fo:simple-page-master>
            <!-- set up page for rest of pages -->
            <fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                <fo:region-body margin-top="15mm" margin-bottom="20mm"/>
                <fo:region-before extent="25mm" region-name="header-rest"/>
                <fo:region-after extent="20mm"/>
            </fo:simple-page-master>
            <!-- set up page conditions -->
            <fo:page-sequence-master master-name="document">
                <fo:repeatable-page-master-alternatives>
                    <fo:conditional-page-master-reference page-position="first" master-reference="first"/>
                    <fo:conditional-page-master-reference page-position="rest" master-reference="rest"/>
                </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
        </fo:layout-master-set>
    </xsl:template>
    
    <!-- Display Daily List -->
    <xsl:template name="DisplayDailyList">
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
        <!-- set up main document -->
        <fo:page-sequence master-reference="document" initial-page-number="1" force-page-count="no-force">
            <!-- Display info for header on first page -->
            <fo:static-content flow-name="header-first">
                <fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="150mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell>
                                <!-- call template to display Crown Court details -->
                                <xsl:call-template name="CourtDetails">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </fo:table-cell>
                            <fo:table-cell text-align="right">
                                <!-- display the page no -->
                                <fo:block font-size="9pt">
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
            </fo:static-content>
            <!-- Display info for all headers apart from the first -->
            <fo:static-content flow-name="header-rest">
                <!-- call template to display header info -->
                <xsl:call-template name="displayHeaderRest">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="listType" select="$listType"/>
                </xsl:call-template>
            </fo:static-content>
            <!-- Footer Info -->
            <fo:static-content flow-name="xsl-region-after">
                <fo:block>
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
                <!-- call template to display Footer Info -->
                <xsl:call-template name="ListFooter">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </fo:static-content>
            <!-- Main Body for document-->
            <fo:flow flow-name="xsl-region-body">
                <!-- call template to display the header info -->
                <xsl:call-template name="HeaderInfo">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="listType" select="$listType"/>
                </xsl:call-template>
                <fo:block space-after="8pt">
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
                <!-- display court list info -->
                <xsl:apply-templates select="cs:CourtLists">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="listType" select="$listType"/>
                </xsl:apply-templates>
                <fo:block id="{$language}"/>
            </fo:flow>
        </fo:page-sequence>
    </xsl:template>
    <!-- 
        COMMENTS
        Transformer used by Xhibit for display of the Daily List, Tomorrow's List and MLD to
        transform the Daily List into PDF
    -->
    <!-- 
        *****************************************************************
        GLOBAL VARIABLES IN NON-ALPHABETICAL ORDER
        *****************************************************************
     -->
    <!-- Global Variable to hold the Current House House Code -->
    <xsl:variable name="CurrentCourtCode">
        <xsl:value-of select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseCode"/>
    </xsl:variable>
    
    <!-- Pick up the first court house type from the Court List -->
    <xsl:variable name="CourtType">
        <xsl:for-each select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseType">
            <xsl:value-of select="."/>
        </xsl:for-each>
    </xsl:variable>
    <!-- 
        **************************  
        TEMPLATE MATCHES
        **************************
     -->
    <!-- pick up the Judge's name and place it in the CitizenNameFullTitle element -->
    <xsl:template match="cs:Judiciary/cs:Judge">
        <xsl:param name="language"/>
        <xsl:element name="Judge">
            <xsl:element name="CitizenNameFullTitle">
                <xsl:if test="apd:CitizenNameRequestedName != 'N/A'">
                    <xsl:value-of select="apd:CitizenNameRequestedName "/>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <!-- display Court List Info -->
    <xsl:template match="cs:CourtLists">
        <xsl:param name="language"/>
             <xsl:param name="listType"/>
        <xsl:for-each select="cs:CourtList">
            <xsl:if test="position() != 1">
                <fo:block break-before="page"/>
            </xsl:if>
            <xsl:if test="count(./cs:CourtHouse/cs:CourtHouseName) = 1">
                <!-- display the court house name -->
                <fo:block font-weight="bold" space-after="10pt">
                    <xsl:variable name="courtHouseName">
                        <xsl:value-of select="./cs:CourtHouse/cs:CourtHouseName"/>
                    </xsl:variable>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="$courtHouseName"/>
                    </xsl:call-template>
                </fo:block>
            </xsl:if>
            <xsl:for-each select="cs:Sittings/cs:Sitting">
                <xsl:sort select="cs:SittingPriority" order="descending"/>
                <xsl:sort select="number(string-length(translate(cs:CourtRoomNumber, translate(cs:CourtRoomNumber,'0123456789',''),''))=0)"/>
                <xsl:sort select="number(translate(cs:CourtRoomNumber,concat('-',translate(cs:CourtRoomNumber,'-0123456789','')),'.'))" data-type="number"/>
                <!--
                'SittingPriority' logic used to determine if the case is floating
                F = Floating case
                T = Top Priority
            -->
                <xsl:choose>
                    <!-- not floating display court room info -->
                    <xsl:when test="not(cs:SittingPriority = 'F')">
                        <fo:block font-size="10pt">
                            <!-- pick out the Court Room number -->
                            <xsl:choose>
                                <xsl:when test="string-length(translate(cs:CourtRoomNumber, translate(cs:CourtRoomNumber,'-0123456789',''),''))>0">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'Court'"/>
                                    </xsl:call-template>
                                    <xsl:text> </xsl:text>
                                    <xsl:value-of select="translate(cs:CourtRoomNumber, translate(cs:CourtRoomNumber,'-0123456789',''),'')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="cs:CourtRoomNumber"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!-- if hearings then display sitting at time otherwise display 'NOT SITTING' text-->
                            <xsl:choose>
                                <!--<xsl:when test="count(Hearings/Hearing/CaseNumber) &gt; 0">-->
                                <xsl:when test="cs:SittingAt">
                                    <xsl:text> - </xsl:text>
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'sitting at'"/>
                                    </xsl:call-template>
                                    <xsl:text> </xsl:text>
                                    <xsl:apply-templates select="cs:SittingAt"/>
                                </xsl:when>
                                <!--</xsl:when>-->
                                <!--<xsl:otherwise>-->
                                <!--<xsl:text> - NOT SITTING</xsl:text>-->
                                <!--</xsl:otherwise>-->
                            </xsl:choose>
                        </fo:block>
                    </xsl:when>
					<xsl:otherwise>
						<fo:block font-weight="bold" font-size="10pt">
							<!-- floating, display the following text -->
							<xsl:text>The following may be taken in any court.</xsl:text>
						</fo:block>
					</xsl:otherwise>
                </xsl:choose>
                <!-- display the judge information -->
                <xsl:call-template name="JudgeDetails">
                    <xsl:with-param name="Judge" select="cs:Judiciary"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- display any sitting note information -->
                <fo:block font-weight="bold" space-after="8pt" font-size="10pt">
                    <xsl:if test="cs:SittingNote">
                        <xsl:variable name="UPPERSittingNote">
                            <xsl:call-template name="toUpper">
                                <xsl:with-param name="content" select="SittingNote"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <!-- only display Sitting Note if it does not contain 'NOT SITTING' text as would have been displayed as 
                            part of sitting information -->
                        <!--<xsl:if test="not(contains($UPPERSittingNote,'NOT SITTING'))">-->
                        <xsl:value-of select="cs:SittingNote"/>
                        <!--</xsl:if>-->
                    </xsl:if>
                </fo:block>
                <!-- display information for each hearing -->
                <xsl:for-each select="cs:Hearings/cs:Hearing">
                    <xsl:call-template name="hearing">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="listType" select="$listType"/>
                    </xsl:call-template>
                </xsl:for-each>
                <fo:block space-after="8pt">
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    
    <!-- only display the first 5 character i.e. 10:30 -->
    <!-- Needs to be able to handle both SittingAt data from the Xhibit XML and the CS Xml -->
    <xsl:template match="cs:SittingAt">
        <xsl:param name="language"/>
        <xsl:choose>
            <!-- Handle SittingAt data 10:00:00 -->
            <xsl:when test="string-length(.)=8">
                <!-- call template to postfix AM or PM -->
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="input">
                        <xsl:value-of select="substring(.,1,5)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <!-- Handle SittingAt data 9:00:00 -->
            <xsl:when test="string-length(.)=7">
                <!-- call template to postfix AM or PM -->
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="input">
                        <xsl:value-of select="substring(.,1,4)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <!-- Handle SittingAt data 10:00 -->
            <xsl:when test="string-length(.)=5">
                <!-- call template to postfix AM or PM -->
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="input">
                        <xsl:value-of select="substring(.,1,5)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <!-- Handle SittingAt data 9:00 -->
            <xsl:when test="string-length(.)=4">
                <!-- call template to postfix AM or PM -->
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="input">
                        <xsl:value-of select="substring(.,1,4)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <!-- 
                Handle other time formats
                2003-11-21T10:00:00.000Z
            -->
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="contains(.,'T')">
                        <!-- variable to hold complete time format -->
                        <xsl:variable name="Time">
                            <xsl:value-of select="substring-after(.,'T')"/>
                        </xsl:variable>
                        <!-- extract the hours value from the time -->
                        <xsl:variable name="Hours">
                            <xsl:value-of select="substring-before($Time,':')"/>
                        </xsl:variable>
                        <!-- extract the mins value from the time -->
                        <xsl:variable name="Mins">
                            <xsl:variable name="TempTime">
                                <xsl:value-of select="substring-after($Time,':')"/>
                            </xsl:variable>
                            <xsl:value-of select="substring-before($TempTime,':')"/>
                        </xsl:variable>
                        <!-- variable to hold complete time value -->
                        <xsl:variable name="TimeToFormat">
                            <xsl:value-of select="$Hours"/>
                            <xsl:text>:</xsl:text>
                            <xsl:value-of select="$Mins"/>
                        </xsl:variable>
                        <!-- call template to format the time -->
                        <xsl:call-template name="FormatTime">
                            <xsl:with-param name="input">
                                <xsl:value-of select="$TimeToFormat"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--        
            ***********************
            TEMPLATE NAMES 
            **********************
    -->
    <!-- from dt:date-time.xsl -->
    <!-- Return number to represent the day of the week
            0   Sunday
            1   Monday
            2   Tuesday
            3   Wednesday
            4   Thursday
            5   Friday
            6   Saturday
    -->
    <xsl:template name="dt:calculate-day-of-the-week">
        <xsl:param name="year"/>
        <xsl:param name="month"/>
        <xsl:param name="day"/>
        <xsl:variable name="a" select="floor((14 - $month) div 12)"/>
        <xsl:variable name="y" select="$year - $a"/>
        <xsl:variable name="m" select="$month + 12 * $a - 2"/>
        <xsl:value-of select="($day + $y + floor($y div 4) - floor($y div 100) + floor($y div 400) + floor((31 * $m) div 12)) mod 7"/>
    </xsl:template>
    
    <!-- Display Court Details from CourtList info -->
    <xsl:template name="CourtDetails">
        <xsl:param name="language"/>
        <fo:block font-size="16pt">
            <fo:inline font-weight="bold">
                <!-- Court Type -->
                <xsl:variable name="theCC">
                    <xsl:text>The </xsl:text>
                    <xsl:value-of select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseType"/>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$theCC"/>
                </xsl:call-template>
            </fo:inline>
            <fo:block/>
            <!--    Court name -->
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'at'"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
            <!-- Display the parent / satellite court names -->
            <!-- Display the first court house name -->
            <xsl:variable name="firstcourthousename">
                <xsl:value-of select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseName"/>
            </xsl:variable>
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="$firstcourthousename"/>
            </xsl:call-template>
            <!-- Pick up all court that are not the same as the main court to construct list of court names -->
            <xsl:for-each select="/cs:DailyList/cs:CourtLists/cs:CourtList[not(position() = 1)]/cs:CourtHouse[cs:Description = 'SATELLITE']">
                <xsl:choose>
                    <!-- if not last display comma and name -->
                    <xsl:when test="position() != last()">
                        <xsl:text>, </xsl:text>
                    </xsl:when>
                    <!-- when last display 'and ' name -->
                    <xsl:otherwise>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'and'"/>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:variable name="nextcourthousename">
                    <xsl:value-of select="./cs:CourtHouseName"/>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$nextcourthousename"/>
                </xsl:call-template>
            </xsl:for-each>
        </fo:block>
    </xsl:template>
    
    <!-- display the defendant details -->
    <xsl:template name="DefendantDetails">
        <xsl:param name="caseNumText"/>
        <xsl:param name="committingText"/>
        <xsl:param name="prosecuteRefText"/>
        <xsl:param name="appendText"/>
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
		<fo:block>
        <fo:table table-layout="fixed" font-size="9pt">
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="100mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="45mm"/>
            <fo:table-body>
                <xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name">
                    <xsl:variable name="ptiurn">
                        <xsl:if test="../../cs:URN">
                            <xsl:value-of select="../../cs:URN"/>
                        </xsl:if>
                    </xsl:variable>
                    <xsl:variable name="translatedProsecuteRefText">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="$prosecuteRefText"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <!-- get the formatted forename for the defendant : to handle long defendant forenames -->
                    <xsl:variable name="FormattedForename">
                        <xsl:call-template name="StringManipulation">
                            <xsl:with-param name="InputString" select="apd:CitizenNameForename[position()=1]"/>
                            <xsl:with-param name="StringLength" select="45"/>
                        </xsl:call-template>
                    </xsl:variable>
					<xsl:variable name="UBCaseTitle">
                        <xsl:value-of select="apd:CitizenNameSurname"/>
						<xsl:if test="apd:CitizenNameForename[position()=1]">
							<xsl:value-of select="apd:CitizenNameForename[position()=1]"/>
						</xsl:if>
						<xsl:if test="apd:CitizenNameForename[position()=2]">
							<xsl:value-of select="apd:CitizenNameForename[position()=2]"/>
						</xsl:if>
                    </xsl:variable>
                    <xsl:variable name="defendant">
                        <xsl:value-of select="apd:CitizenNameSurname"/>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="TitleCase">
                            <xsl:with-param name="text" select="$FormattedForename"/>
                        </xsl:call-template>
                        <!-- middle name, get initial only -->
                        <!-- Court Service Stores Middle name using 2nd Forename element
                            Xhibit stored the Middle name in the Suffix element -->
                        <xsl:if test="apd:CitizenNameForename[position()=2]">
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="substring(apd:CitizenNameForename[position()=2],1,1)"/>
                        </xsl:if>
                        <xsl:if test="CitizenNameSuffix">
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="substring(apd:CitizenNameSuffix,1,1)"/>
                        </xsl:if>
                        <xsl:value-of select="$appendText"/>
                        <!-- only display if DLP -->
                        <xsl:if test="$listType='DLP'">
                            <fo:block/>
                            <fo:table table-layout="fixed" font-size="9pt">
                                <fo:table-column column-width="30mm"/>
                                <fo:table-column column-width="25mm"/>
                                <fo:table-column column-width="35mm"/>
                                <fo:table-body>
                                    <fo:table-row>
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:if test="../cs:DateOfBirth">
                                                    <xsl:call-template name="displayDate_mon">
                                                        <xsl:with-param name="input" select="../cs:DateOfBirth/apd:BirthDate"/>
                                                        <xsl:with-param name="language" select="$language"/>
                                                    </xsl:call-template>
                                                    <xsl:text> </xsl:text>
                                                </xsl:if>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:if test="../../cs:PrisonerID">
                                                    <xsl:value-of select="../../cs:PrisonerID"/>
                                                    <xsl:text> </xsl:text>
                                                </xsl:if>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:if test="../../cs:PrisonLocation">
                                                    <xsl:variable name="prisonLocation">
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="../../cs:PrisonLocation/cs:Location"/>
                                                        </xsl:call-template>
                                                    </xsl:variable>
                                                    <xsl:call-template name="toUpper">
                                                        <xsl:with-param name="content" select="$prisonLocation"/>
                                                    </xsl:call-template>
                                                </xsl:if>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>
                        </xsl:if>
                    </xsl:variable>
                    <xsl:choose>
                        <!-- display all the details for the first time -->
                        <xsl:when test="position()=1">
                            <fo:table-row space-after="10pt">
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="$caseNumText"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
										<xsl:choose>
											<xsl:when test="substring($caseNumText,1,1)='B' or substring($caseNumText,1,1)='U'">
												<!-- U and B cases display the case title which has been split up in the defendant details -->
												<xsl:copy-of select="$UBCaseTitle"/>
											</xsl:when>
											<xsl:otherwise>
												<!-- All other case type, put defendant details -->
												<xsl:copy-of select="$defendant"/>
											</xsl:otherwise>
										</xsl:choose>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="$committingText"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:choose>
                                            <xsl:when test="starts-with($prosecuteRefText,'CPS Ref:')"> 
												<xsl:choose>
													<xsl:when test="normalize-space($ptiurn) = '' or string-length($ptiurn) = 0"> 
														<xsl:text>CPS</xsl:text>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$translatedProsecuteRefText"/>
														<xsl:value-of select="$ptiurn"/>
													 </xsl:otherwise>
												</xsl:choose>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="$prosecuteRefText"/>
                                                <xsl:if test="$listType='DLP'">
    												<fo:block>  
														<xsl:value-of select="$ptiurn"/> 	
													</fo:block>															
												</xsl:if>
				                               <xsl:if test="$listType='DL'">
													<fo:block>  
														<xsl:value-of select="$ptiurn"/> 	
													</fo:block>															
												</xsl:if>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </xsl:when>
                        <!-- only display the defendant details -->
                        <xsl:otherwise>
                            <fo:table-row space-after="10pt">
                                <!-- top table-cell is blank so generation using Apache FOP works -->
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:text> </xsl:text>
                                    </fo:block>
                                </fo:table-cell>                                
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:copy-of select="$defendant"/>
                                    </fo:block>
                                </fo:table-cell>
                                <!-- third table-cell is blank so generation using Apache FOP works -->
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:text> </xsl:text>
                                    </fo:block>
                                </fo:table-cell>                                
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:choose>
                                            <xsl:when test="starts-with($prosecuteRefText,'CPS Ref:')">
                                                <xsl:if test="$ptiurn">
                                                    <xsl:value-of select="$ptiurn"/>                                                       
                                                </xsl:if>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:if test="$listType='DLP'">
    												<fo:block>  
														<xsl:value-of select="$ptiurn"/> 	
													</fo:block>															
												</xsl:if>
				                               <xsl:if test="$listType='DL'">
													<fo:block>  
														<xsl:value-of select="$ptiurn"/> 	
													</fo:block>															
												</xsl:if>
                                            </xsl:otherwise>
                                        </xsl:choose>                                    
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
		</fo:block>
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
    
    <!-- template to format date string : abreviated month value -->
    <xsl:template name="displayDate_mon">
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
    
    <!-- display the date including the Day -->
    <xsl:template name="displayDayDate">
        <xsl:param name="input"/>
        <xsl:param name="language"/>
        <xsl:variable name="month" select="substring($input,6,2)"/>
        <xsl:variable name="day" select="substring($input,9,2)"/>
        <xsl:variable name="year" select="substring($input,1,4)"/>
        <xsl:variable name="dayOfWeek">
            <xsl:call-template name="dt:calculate-day-of-the-week">
                <xsl:with-param name="year" select="$year"/>
                <xsl:with-param name="month" select="$month"/>
                <xsl:with-param name="day" select="$day"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$dayOfWeek=0">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Sunday'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$dayOfWeek=1">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Monday'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$dayOfWeek=2">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Tuesday'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$dayOfWeek=3">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Wednesday'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$dayOfWeek=4">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Thursday'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$dayOfWeek=5">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Friday'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$dayOfWeek=6">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Saturday'"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:call-template name="displayDate">
            <xsl:with-param name="input">
                <xsl:value-of select="$input"/>
            </xsl:with-param>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
    </xsl:template>
    
    <!-- Display the header for pages other than the first -->
    <xsl:template name="displayHeaderRest">
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
		<fo:block>
        <fo:table table-layout="fixed" font-size="9pt">
            <fo:table-column column-width="110mm"/>
            <fo:table-column column-width="40mm"/>
            <fo:table-column column-width="30mm"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:choose>
                                <xsl:when test="$listType='DL'">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'Daily List for'"/>
                                    </xsl:call-template>
                                    <xsl:text> </xsl:text>
                                </xsl:when>
                                <xsl:when test="$listType='DLP'">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'Daily List for Prison Service for'"/>
                                    </xsl:call-template>
                                    <xsl:text> </xsl:text>
                                </xsl:when>
                            </xsl:choose>
                            <!-- call template to display the date incl. the day -->
                            <xsl:call-template name="displayDayDate">
                                <xsl:with-param name="input">
                                    <xsl:value-of select="cs:ListHeader/cs:StartDate"/>
                                </xsl:with-param>
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
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
                    <fo:table-cell>
                        <fo:block>
                            <!-- call template to display version details -->
                            <xsl:call-template name="displayVersionDetails">
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <!-- Display the parent / satellite court names -->
                            <!-- Display the first court house name -->
                            <xsl:variable name="firstCourtTitle">
                                <xsl:value-of select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseName"/>
                            </xsl:variable>
                            <xsl:variable name="firstCourtHouseName">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="$firstCourtTitle"/>
                                </xsl:call-template>
                            </xsl:variable>
                            <xsl:value-of select="$firstCourtHouseName"/>
                            <!-- Pick up all court that are not the same as the main court to construct list of court names -->
                            <xsl:for-each select="/cs:DailyList/cs:CourtLists/cs:CourtList[not(position() = 1)]/cs:CourtHouse[cs:Description = 'SATELLITE']">
                                <xsl:choose>
                                     <!-- if not last display comma and name -->
                                    <xsl:when test="position() != last()">
                                        <xsl:text>, </xsl:text>
                                    </xsl:when>
                                     <!-- when last display 'and ' name -->
                                    <xsl:otherwise>
                                        <xsl:text> </xsl:text>
                                        <xsl:variable name="and">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'and'"/>
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:call-template name="toUpper">
                                            <xsl:with-param name="content" select="$and"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <xsl:variable name="nextCourtTitle">
                                    <xsl:value-of select="./cs:CourtHouseName"/>
                                </xsl:variable>
                                <xsl:variable name="nextCourtHouseName">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="$nextCourtTitle"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:value-of select="$nextCourtHouseName"/>
                            </xsl:for-each>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
		</fo:block>
        <fo:block space-after="8pt">
            <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
        </fo:block>
    </xsl:template>
    
    <!-- Display the version details -->
    <xsl:template name="displayVersionDetails">
        <xsl:param name="language"/>
        <xsl:variable name="initversion">
            <!-- variable to hold CONSTANT null edition field value -->
            <xsl:variable name="NULL_EDITION">
                <xsl:value-of select="-1"/>
            </xsl:variable>
            <!-- variable to hold the length of Version text -->
            <xsl:variable name="vLength">
                <xsl:value-of select="string-length(/cs:DailyList/cs:ListHeader/cs:Version)"/>
            </xsl:variable>
            <!-- variable to hold the length of null edition  text -->
            <xsl:variable name="edLength">
                <xsl:value-of select="string-length($NULL_EDITION)"/>
            </xsl:variable>
            <xsl:choose>
                <!-- Test to see if the Version ends with -1, which means that the Edition No is null -->
                <xsl:when test="substring(/cs:DailyList/cs:ListHeader/cs:Version, $vLength - $edLength + 1) = $NULL_EDITION">
                    <!-- display first part of version plus additional text at end indicating that the Edition No is null -->
                    <xsl:value-of select="substring(/cs:DailyList/cs:ListHeader/cs:Version, 1, $vLength - $edLength)"/>
                    <xsl:text> </xsl:text> Unspecified Edition No
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="/cs:DailyList/cs:ListHeader/cs:Version"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- strip out an unnecessary text in the version (v) -->
        <xsl:variable name="version">
            <xsl:choose>
                <xsl:when test="contains($initversion,'v')">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="substring-before($initversion,' v')"/>
                    </xsl:call-template>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="substring-after($initversion,'v')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="$initversion"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- display the version value -->
        <xsl:value-of select="$version"/>
    </xsl:template>
    
    <!-- template used to format the time from 24hrs to the following format HH:MM [AM/PM]-->
    <!-- display the formatted time and then post fix with AM or PM -->
    <xsl:template name="FormatTime">
        <xsl:param name="input"/>
        <xsl:param name="language"/>
        <xsl:choose>
            <!-- if less than 12 postfix AM -->
            <xsl:when test="substring-before($input,':') &lt; 12">
                <xsl:value-of select="$input"/>
                <xsl:text> am</xsl:text>
            </xsl:when>
            <!-- otherwise format the hours element and postfix with PM -->
            <xsl:otherwise>
                <!-- get the hours element from the string -->
                <xsl:variable name="hrs" select="substring-before($input,':')"/>
                <xsl:choose>
                    <!-- if 12 just display value as no formatting required -->
                    <xsl:when test="$hrs = 12">
                        <xsl:value-of select="$hrs"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- subtract 12 from the 24 hours element -->
                        <xsl:variable name="fmtHrs" select="$hrs - 12"/>
                        <!-- if single element return prefix 0 to the hours -->
                        <!-- i.e. 9 become 09 -->
                        <xsl:if test="string-length($fmtHrs) = 1">
                            <xsl:text>0</xsl:text>
                        </xsl:if>
                        <!-- display the value of the formatted hours -->
                        <xsl:value-of select="$fmtHrs"/>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- display the rest of the time -->
                <xsl:text>:</xsl:text>
                <xsl:value-of select="substring-after($input,':')"/>
                <xsl:text> pm</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- Template to get the Not Before Time -->
    <xsl:template name="getNotBeforeTime">
        <xsl:param name="language"/>
        <xsl:if test="contains(cs:TimeMarkingNote,':')">
            <xsl:variable name="UPPER_TMN">
                <xsl:call-template name="toUpper">
                    <xsl:with-param name="content" select="TimeMarkingNote"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="contains($UPPER_TMN,'NOT BEFORE')">
                    <xsl:call-template name="toLower">
                        <xsl:with-param name="content" select="substring-after($UPPER_TMN,'NOT BEFORE ')"/>
                    </xsl:call-template>
                </xsl:when>
                <!-- deal with 'SITTING AT' timemarking text -->
                <xsl:when test="contains($UPPER_TMN,'SITTING AT')">
                    <xsl:call-template name="toLower">
                        <xsl:with-param name="content" select="substring-after($UPPER_TMN,'SITTING AT ')"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(contains(cs:TimeMarkingNote,'am') or contains(cs:TimeMarkingNote,'pm') or contains(cs:TimeMarkingNote,'AM') or contains(cs:TimeMarkingNote,'PM')or contains(cs:TimeMarkingNote,'Am') or contains(cs:TimeMarkingNote,'Pm')or contains(cs:TimeMarkingNote,'aM') or contains(cs:TimeMarkingNote,'pM'))">
                            <xsl:call-template name="FormatTime">
                                <xsl:with-param name="input">
                                    <xsl:value-of select="cs:TimeMarkingNote"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="toLower">
                                <xsl:with-param name="content" select="TimeMarkingNote"/>
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    
    <!-- Template to get the Sitting At Time -->
    <xsl:template name="getSittingTime">
        <xsl:param name="language"/>
        <xsl:apply-templates select="../../cs:SittingAt"/>
    </xsl:template>
    
    <!-- display information for the header -->
    <xsl:template name="HeaderInfo">
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
        <fo:block space-after="8pt" text-align="center" font-size="15pt" font-weight="bold">
            <xsl:choose>
                <xsl:when test="$listType='DL'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Daily List for'"/>
                    </xsl:call-template>
                    <xsl:text> </xsl:text>
                </xsl:when>
                <xsl:when test="$listType='DLP'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Daily List for Prison Service for'"/>
                    </xsl:call-template>
                    <xsl:text> </xsl:text>
                </xsl:when>
            </xsl:choose>
            <!-- call template to display the date incl. the day -->
            <xsl:call-template name="displayDayDate">
                <xsl:with-param name="input">
                    <xsl:value-of select="cs:ListHeader/cs:StartDate"/>
                </xsl:with-param>
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </fo:block>
        <xsl:if test="$listType='DLP'">
            <fo:block text-align="center" font-size="12pt" line-height="18pt" font-weight="bold">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'For Prison Use Only'"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
        <fo:block text-align="center" font-size="9pt" line-height="18pt">
            <!-- call template to display version details -->
            <xsl:call-template name="displayVersionDetails">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </fo:block>
    </xsl:template>
    
    <!-- display hearing details -->
    <xsl:template name="hearing">
        <xsl:param name="language"/>
             <xsl:param name="listType"/>
        <xsl:variable name="hearingDescription">
            <xsl:choose>
                <xsl:when test="not (position()=1)">
                    <xsl:variable name="pos" select="position()"/>
                    <!-- check to ensure that the previous description is not the same, only display if different -->
                    <xsl:if test="not (cs:HearingDetails/cs:HearingDescription = ../cs:Hearing[position()=$pos - 1]/cs:HearingDetails/cs:HearingDescription)">
                        <xsl:value-of select="cs:HearingDetails/cs:HearingDescription"/>
                    </xsl:if>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="cs:HearingDetails/cs:HearingDescription"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- set up bit to append to defendant if needed -->
        <xsl:variable name="appendage">
            <xsl:choose>
                <xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
                    <xsl:value-of select="concat('-v-',cs:Prosecution//cs:OrganisationName)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="''"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- Get the Sitting At time -->
        <xsl:variable name="SittingAtValue">
            <xsl:call-template name="getSittingTime"/>
        </xsl:variable>
        <!-- Get the Not Before Time -->
        <xsl:variable name="NotBeforeTime">
            <xsl:call-template name="getNotBeforeTime"/>
        </xsl:variable>
        <!--    Needs to be able to handle both XHIBIT Internal TimeMarking Note where there is just a time value i.e. 10:00 which needs to be formatted
            and also the CS Tomorrow DailyList TimeMarking Note which contains the 'Not Before' text and also the time has been formatted i.e. Not Before 10:00 am -->
        <!-- display TimeMarkingNotes -->
        <fo:block font-weight="bold" font-size="10pt">
            <xsl:if test="cs:TimeMarkingNote">
                <xsl:if test="not (cs:TimeMarkingNote = ' ')">
                    <xsl:variable name="upperMarkingNote">
                        <xsl:call-template name="toUpper">
                            <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:variable name="NotBefore">
                        <!--<xsl:if test="not(contains($upperMarkingNote,'NOT BEFORE')or contains($upperMarkingNote,'SITTING AT'))">-->
                        <!--    <xsl:text>NOT BEFORE </xsl:text>-->
                        <!--</xsl:if>-->
                    </xsl:variable>
                    <xsl:choose>
                        <!-- could potentially be blank, so check that it contains a colon -->
                        <xsl:when test="contains(cs:TimeMarkingNote,':')">
                            <!-- Only Display the Not Before Time if the value is different from the Sitting At Time -->
                            <xsl:if test="$SittingAtValue != $NotBeforeTime">
                                <xsl:choose>
                                    <!-- check to ensure that it does not already contain an AM or PM within the TimeMarkingNote -->
                                    <xsl:when test="not(contains(cs:TimeMarkingNote,'am') or contains(cs:TimeMarkingNote,'pm') or contains(cs:TimeMarkingNote,'AM') or contains(cs:TimeMarkingNote,'PM')or contains(cs:TimeMarkingNote,'Am') or contains(cs:TimeMarkingNote,'Pm')or contains(cs:TimeMarkingNote,'aM') or contains(cs:TimeMarkingNote,'pM'))">
                                        <!-- apply template to format the time -->
                                        <!-- Prefix the following text : Will only appear for Time Marking Notes that do not contain am or pm postfix -->
                                        <!--<xsl:text>NOT BEFORE </xsl:text>-->
                                        <xsl:call-template name="FormatTime">
                                            <xsl:with-param name="input">
                                                <xsl:value-of select="cs:TimeMarkingNote"/>
                                            </xsl:with-param>
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <!-- if already contain AM or PM just display the value as it is -->
                                        <xsl:choose>
                                            <xsl:when test="$NotBefore=''">
                                                <!--display the 'NOT BEFORE', 'SITTING AT' as required-->
                                                <xsl:if test="contains($upperMarkingNote,'NOT BEFORE')">
                                                    <xsl:call-template name="getValue">
                                                        <xsl:with-param name="language" select="$language"/>
                                                        <xsl:with-param name="key" select="'NOT BEFORE'"/>
                                                    </xsl:call-template>
                                                    <xsl:text> </xsl:text>
                                                    <!--make the am or pm lowercase-->
                                                    <xsl:call-template name="toLower">
                                                        <xsl:with-param name="content" select="substring-after($upperMarkingNote,'NOT BEFORE')"/>
                                                    </xsl:call-template>
                                                </xsl:if>
                                                <xsl:if test="contains($upperMarkingNote,'SITTING AT')">
                                                    <xsl:variable name="sittingAt">
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="'sitting at'"/>
                                                        </xsl:call-template>
                                                    </xsl:variable>
                                                    <xsl:call-template name="toUpper">
                                                        <xsl:with-param name="content" select="$sittingAt"/>
                                                    </xsl:call-template>
                                                    <xsl:text> </xsl:text>
                                                    <!--make the am or pm lowercase-->
                                                    <xsl:call-template name="toLower">
                                                        <xsl:with-param name="content" select="substring-after($upperMarkingNote,'SITTING AT')"/>
                                                    </xsl:call-template>
                                                </xsl:if>
                                                <!--if no NOT BEFORE or SITTING AT then just display time-->
                                                <xsl:if test="not(contains($upperMarkingNote,'NOT BEFORE') or contains($upperMarkingNote,'SITTING AT'))">
                                                    <!-- The time marking note already contains an am or pm value (upper, lower case combinations) just convert to lower case -->
                                                    <xsl:call-template name="toLower">
                                                        <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                                                    </xsl:call-template>
                                                </xsl:if>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="$NotBefore"/>
                                                <xsl:call-template name="toLower">
                                                    <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                                                </xsl:call-template>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:if>
                            <!--if Sitting At time and Not Before time are equal and it is not the first hearing for a courtroom then do not display Not before time - short ot itme before deadline hence - copy and paste approach - rework into template at some point-->
                            <xsl:if test=" $SittingAtValue= $NotBeforeTime ">
                                <xsl:if test="not(HearingSequenceNumber=1)">
                                    <!--<xsl:value-of select="HearingSequenceNumber[position()]"></xsl:value-of>-->
                                    <xsl:choose>
                                        <xsl:when test="$NotBefore=''">
                                            <!--display the 'NOT BEFORE', 'SITTING AT' as required-->
                                            <xsl:if test="contains($upperMarkingNote,'NOT BEFORE')">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="'NOT BEFORE'"/>
                                                </xsl:call-template>
                                                <xsl:text> </xsl:text>
                                                <!--make the am or pm lowercase-->
                                                <xsl:call-template name="toLower">
                                                    <xsl:with-param name="content" select="substring-after($upperMarkingNote,'NOT BEFORE')"/>
                                                </xsl:call-template>
                                            </xsl:if>
                                            <xsl:if test="contains($upperMarkingNote,'SITTING AT')">
                                                <xsl:variable name="sittingAt">
                                                    <xsl:call-template name="getValue">
                                                        <xsl:with-param name="language" select="$language"/>
                                                        <xsl:with-param name="key" select="'sitting at'"/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:call-template name="toUpper">
                                                    <xsl:with-param name="content" select="$sittingAt"/>
                                                </xsl:call-template>
                                                <!--make the am or pm lowercase-->
                                                <xsl:call-template name="toLower">
                                                    <xsl:with-param name="content" select="substring-after($upperMarkingNote,'SITTING AT')"/>
                                                </xsl:call-template>
                                            </xsl:if>
                                            <!--if no NOT BEFORE or SITTING AT then just display time-->
                                            <xsl:if test="not(contains($upperMarkingNote,'NOT BEFORE') or contains($upperMarkingNote,'SITTING AT'))">
                                                <xsl:value-of select="cs:TimeMarkingNote"/>
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="$NotBefore"/>
                                            <xsl:call-template name="toLower">
                                                <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                                            </xsl:call-template>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <fo:block/>
                                </xsl:if>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="cs:TimeMarkingNote"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <fo:block space-after="5pt"/>
                </xsl:if>
            </xsl:if>
            <!-- display hearing description exc. Miscellaneous text -->
            <xsl:choose>
                <xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="substring-after($hearingDescription,'Miscellaneous ')"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="$hearingDescription"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
        <!-- get the prosecuting organisation name -->
        <xsl:variable name="prosecutingref">
            <!-- Details held within Prosecuting Reference -->
            <!-- Pick up the Prosecuting Reference -->
            <xsl:choose>
                <xsl:when test="cs:Prosecution/cs:ProsecutingReference='CPS'">
                    <xsl:text>CPS Ref:</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="prosecutingOrganisation">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="cs:Prosecution/cs:ProsecutingReference"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:call-template name="toUpper">
                        <xsl:with-param name="content" select="$prosecutingOrganisation"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- set up variable to hold the formatted prosecuting refererence details -->
        <xsl:variable name="Formattedprosecutingref">
            <xsl:call-template name="StringManipulation">
                <xsl:with-param name="InputString" select="$prosecutingref"/>
                <xsl:with-param name="StringLength" select="20"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:choose>
            <!-- Display of Defendant Details on the Daily List for U and B Type cases.  Display Case Title instead
                
                For the MLD Daily List the CaseTitle is placed in the CitizenNameSurname element of a defendant and is
                displayed as per a normal defendant

                For the Internal Xhibit Daily List display the Case Title is placed in the CaseTitle element, there are no
                defendants and is covered by the otherwise condition of the when clause below
            -->
            <xsl:when test="cs:Defendants/cs:Defendant">
                <!-- display the defendant detail -->
                <xsl:call-template name="DefendantDetails">
                    <xsl:with-param name="caseNumText" select="cs:CaseNumber"/>
                    <xsl:with-param name="committingText" select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
                    <xsl:with-param name="prosecuteRefText" select="$Formattedprosecutingref"/>
                    <xsl:with-param name="appendText" select="$appendage"/>
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="listType" select="$listType"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
				<fo:block>
                <fo:table table-layout="fixed" font-size="9pt">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="45mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>
                                    <xsl:value-of select="cs:CaseNumber"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <!-- Capture scenario of internal Xhibit Daily List where there are no defendants for U and B type cases. Display the CaseTitle (only available in the internal Xhibit Daily List XML) -->
                                    <xsl:if test="substring(cs:CaseNumber,1,1)='B' or substring(cs:CaseNumber,1,1)='U'">
                                        <xsl:value-of select="cs:CaseTitle"/>
                                    </xsl:if>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <xsl:value-of select="cs:CommittingCourt/cs:CourtHouseCode"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <xsl:value-of select="$Formattedprosecutingref"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
				</fo:block>
            </xsl:otherwise>
        </xsl:choose>
        <!-- display any list notes that may exist -->
        <xsl:if test="cs:ListNote">
            <fo:block font-weight="bold" font-size="10pt">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="cs:ListNote"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
		 <fo:block space-after="14pt"/>
    </xsl:template>
    
    <!-- template to display footer information -->
    <xsl:template name="ListFooter">
        <xsl:param name="language"/>
		<fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="160mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-body>
                <xsl:for-each select="cs:DocumentID">
                    <fo:table-row>
                        <!-- display published details -->
                        <xsl:if test="../cs:ListHeader/cs:PublishedTime">
                            <fo:table-cell>
                                <fo:block font-size="9pt">
                                    <xsl:choose>
                                        <!-- Test to see if the Published Date is the default -->
                                        <xsl:when test="substring(../cs:ListHeader/cs:PublishedTime,1,4)='1900'">
                                            Unspecified Published Time
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'Published'"/>
                                            </xsl:call-template>
                                            <xsl:text>: </xsl:text>
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input">
                                                    <xsl:value-of select="substring(../cs:ListHeader/cs:PublishedTime,1,10)"/>
                                                </xsl:with-param>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'at'"/>
                                                <xsl:with-param name="context" select="'time'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="substring(../cs:ListHeader/cs:PublishedTime,12,5)"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block font-size="9pt">
                                     <!--CREST Print Ref-->
                                    <xsl:value-of select="../cs:ListHeader/cs:CRESTprintRef"/>
                                </fo:block>
                            </fo:table-cell>
                        </xsl:if>
                    </fo:table-row>
                </xsl:for-each>
                        <xsl:if test="$language != $DefaultLang">
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="9pt">
                                        <xsl:call-template name="NOT_FOUND_FOOTER">
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>                                        
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </xsl:if>               
            </fo:table-body>
        </fo:table>
		</fo:block>
        <fo:block space-after="8pt">
            <xsl:text> </xsl:text>
        </fo:block>
        <fo:block font-size="10pt">
            <xsl:call-template name="displayCopyright">
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </fo:block>
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
    
    <!-- display the judge details -->
    <xsl:template name="JudgeDetails">
        <xsl:param name="Judge"/>
        <xsl:param name="language"/>
		<fo:block>
		<fo:table table-layout="fixed" font-size="10pt">
            <fo:table-column column-width="180mm"/>
            <fo:table-body>
                <xsl:for-each select="cs:Judiciary/cs:Judge">
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block space-after="5pt" space-before="5pt" text-align="center" font-weight="bold">
                                <xsl:if test="apd:CitizenNameRequestedName != 'N/A'">
                                    <xsl:choose>
                                        <!-- test to see if there is a Full title for the judge -->
                                        <xsl:when test="apd:CitizenNameRequestedName != ' '">
                                            <xsl:variable name="judgeFullTitle">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="apd:CitizenNameRequestedName"/>
                                                </xsl:call-template>
                                            </xsl:variable>
                                            <xsl:call-template name="toUpper">
                                                <xsl:with-param name="content" select="$judgeFullTitle"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <!-- otherwise display the judge's surname -->
                                        <xsl:otherwise>
                                            <xsl:value-of select="apd:CitizenNameSurname"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:if>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
        </fo:block>
        <!-- see if there is any justice information -->
        <xsl:variable name="justiceText">
            <xsl:choose>
                <xsl:when test="count(cs:Judiciary/cs:Justice) =  1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Justice'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                </xsl:when>
                <xsl:when test="count(cs:Judiciary/cs:Justice) &gt; 1">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Justices'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
		<fo:block>
		<fo:table table-layout="fixed" font-size="10pt">
            <fo:table-column column-width="40mm"/>
            <fo:table-column column-width="140mm"/>
            <fo:table-body>
                <xsl:for-each select="cs:Judiciary/cs:Justice">
                    <fo:table-row>
                        <xsl:choose>
                            <xsl:when test="position()=1">
                                <fo:table-cell>
                                    <fo:block font-weight="bold" text-align="right">
                                        <xsl:value-of select="$justiceText"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-weight="bold" text-align="left">
                                        <xsl:value-of select="apd:CitizenNameTitle"/>
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="apd:CitizenNameRequestedName"/>
                                    </fo:block>
                                </fo:table-cell>
                            </xsl:when>
                            <xsl:otherwise>
                                <!-- top one is blank so generation using Apache FOP works -->
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:text> </xsl:text>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-weight="bold" text-align="left">
                                        <xsl:value-of select="apd:CitizenNameTitle"/>
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="apd:CitizenNameRequestedName"/>
                                    </fo:block>
                                </fo:table-cell>
                            </xsl:otherwise>
                        </xsl:choose>
                    </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
		</fo:block>
    </xsl:template>
    
    <!-- 
    Template used to insert spaces into text if length is greater than provided parameter length 
        Parameters:     InputString - The String to be checked 
                        StringLength - After what Length should a space be inserted
    -->
    <xsl:template name="StringManipulation">
        <xsl:param name="InputString"/>
        <xsl:param name="StringLength"/>
        <!-- variable to hold the Input String Length -->
        <xsl:variable name="ISL" select="string-length($InputString)"/>
        <xsl:choose>
            <!-- check if the string length is greater than the provided paramater -->
            <xsl:when test="$ISL>$StringLength">
                <xsl:choose>
                    <!-- check if the string contains any spaces -->
                    <xsl:when test="contains($InputString,' ')">
                        <!-- call template using string up to the space character -->
                        <xsl:call-template name="StringManipulation">
                            <xsl:with-param name="InputString">
                                <xsl:value-of select="substring-before($InputString, ' ')"/>
                            </xsl:with-param>
                            <xsl:with-param name="StringLength" select="$StringLength"/>
                        </xsl:call-template>
                        <!-- insert a space character -->
                        <xsl:text> </xsl:text>
                        <!-- call template with the rest of the string i.e. after the space character-->
                        <xsl:call-template name="StringManipulation">
                            <xsl:with-param name="InputString">
                                <xsl:value-of select="substring-after($InputString, ' ')"/>
                            </xsl:with-param>
                            <xsl:with-param name="StringLength" select="$StringLength"/>
                        </xsl:call-template>
                    </xsl:when>
                    <!--     if it does not contain spaces display the first chunk of text upto 
                        the limit, insert a space and pass rest through the template again -->
                    <xsl:otherwise>
                        <xsl:value-of select="substring($InputString,1,$StringLength)"/>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="StringManipulation">
                            <xsl:with-param name="InputString">
                                <xsl:value-of select="substring($InputString,$StringLength+1,$ISL)"/>
                            </xsl:with-param>
                            <xsl:with-param name="StringLength" select="$StringLength"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- Text within String Lenght limit, so display as it is -->
            <xsl:otherwise>
                <xsl:value-of select="$InputString"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- template used to capitalise the first character of text and put the rest into lower case  -->
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
    
    <!-- template used to convert a string to Lower Case -->
    <xsl:template name="toLower">
        <xsl:param name="content"/>
        <xsl:value-of select="translate($content,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
    </xsl:template>
    
    <!-- template used to convert a string to upper case -->
    <xsl:template name="toUpper">
        <xsl:param name="content"/>
        <xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    </xsl:template>
    
</xsl:stylesheet>