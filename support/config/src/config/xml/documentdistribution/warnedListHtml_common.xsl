<?xml version="1.0" encoding="UTF-8"?>
<!--
    +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" 
version="1.2">

    <xsl:output method="html" indent="yes"/>
    
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>
    
    <!-- Set up global Variables -->
    <xsl:variable name="initversion" select="//cs:ListHeader/cs:Version"/>
    <!-- remove the unnecessary character v in the version details -->
    <xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate"/>
    <xsl:variable name="endDate" select="//cs:ListHeader/cs:EndDate"/>
    <xsl:variable name="firstCourtHouseCode">
        <xsl:value-of select="cs:WarnedList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:Description"/>
    </xsl:variable>
    <xsl:variable name="CourtNames">
        <xsl:value-of select="substring-after(cs:WarnedList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseName, 'at ')"/>
        <xsl:for-each select="cs:WarnedList/cs:CourtLists/cs:CourtList/cs:CourtHouse/cs:Description[. != $firstCourtHouseCode]">
            <xsl:if test="position() = 1">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:value-of select="substring-after(../cs:CourtHouseName,'at ')"/>
            <xsl:if test="position() != last()">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
    </xsl:variable>
    
    <xsl:template name="DisplayWarnedList">
        <xsl:param name="language"/>
        <html>
            <meta content="text/html;  charset=UTF-8" http-equiv="Content-Type"/>
            <body>
                <!-- display Crown Court info -->
                <xsl:apply-templates select="cs:WarnedList/cs:CrownCourt">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <!-- display the warned list details -->
                <xsl:call-template name="WarnedListDetails">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- display fixed hearings -->
                <xsl:apply-templates select="cs:WarnedList/cs:CourtLists" mode="main">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <xsl:apply-templates select="cs:WarnedList/cs:CourtLists" mode="satellite">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <!-- display footer info -->
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
         ***********************
        TEMPLATE MATCH 
        ***********************
    -->
    <!-- display hearings for main court -->
    <xsl:template match="cs:CourtLists" mode="main">
        <xsl:param name="language"/>
        <xsl:variable name="lists">
            <xsl:value-of select="count(cs:CourtList/cs:CourtHouse/cs:Description[. = $firstCourtHouseCode])"/>
        </xsl:variable>
        <xsl:variable name="with">
            <xsl:value-of select="count(cs:CourtList/cs:WithFixedDate[../cs:CourtHouse/cs:Description = $firstCourtHouseCode])"/>
        </xsl:variable>
        <xsl:variable name="withOut">
            <xsl:value-of select="count(cs:CourtList/cs:WithoutFixedDate[../cs:CourtHouse/cs:Description = $firstCourtHouseCode])"/>
        </xsl:variable>
        <table width="100%">
            <tr>
                <td width="40%" valign="top">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'The following cases are warned for'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                </td>
                <td width="60%" valign="top">
                    <xsl:for-each select="cs:CourtList/cs:CourtHouse/cs:Description[. = $firstCourtHouseCode]">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="substring-after(../cs:CourtHouseName,'at ')"/>
                        </xsl:call-template>
                        <xsl:if test="position() != last()">
                            <br/>
                        </xsl:if>
                    </xsl:for-each>
                </td>
            </tr>
        </table>
        <hr/>
        <xsl:if test="$with > 0">
            <b>
                <u>
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Fixtures'"/>
                    </xsl:call-template>
                    <br/>
                    <br/>
                </u>
            </b>
        </xsl:if>
        <xsl:for-each select="./cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithFixedDate 
[not(self::*/@HearingType=preceding::cs:WithFixedDate/@HearingType)]">
			<!--The Hearing/ListNote will be populated with the Hearing Sequence Number so can be used to order by-->
			<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/cs:ListNote" order="ascending" data-type="number"/>
            <xsl:variable name="hearingType" select="./@HearingType"/>
            <!-- display the case details -->
            <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithFixedDate/cs:Fixture/cs:Cases/cs:Case
                                    [../../../@HearingType = $hearingType]">
				<xsl:sort select="substring(cs:CaseNumber, 1, 1)" order="descending"/>
				<xsl:sort select="substring(cs:CaseNumber, 2)" order="ascending" data-type="number"/>
                <xsl:if test="position()=1">
                    <!-- Display the hearing description -->
                    <b>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
                        </xsl:call-template>
                    </b>
                </xsl:if>
                <xsl:call-template name="case">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:for-each>
        </xsl:for-each>
        <xsl:if test="$with >0">
            <hr/>
        </xsl:if>
        <xsl:for-each select="./cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithoutFixedDate[not(self::*/@HearingType=preceding::cs:WithoutFixedDate/@HearingType)]">
        	<!--The Hearing/ListNote will be populated with the Hearing Sequence Number so can be used to order by-->
			<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/cs:ListNote" order="ascending" data-type="number"/>
            <xsl:variable name="hearingType" select="./@HearingType"/>
            <!-- display the case details -->
            <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithoutFixedDate/cs:Fixture/cs:Cases/cs:Case
                                    [../../../@HearingType = $hearingType]">
				<xsl:sort select="substring(./cs:CaseNumber, 1, 1)" order="descending"/>
				<xsl:sort select="substring(./cs:CaseNumber, 2)" order="ascending" data-type="number"/>
                <xsl:if test="position()=1">
                    <!-- Display the hearing description -->
                    <b>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
                        </xsl:call-template>
                    </b>
                </xsl:if>
                <xsl:call-template name="case">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </xsl:for-each>
            <hr/>
        </xsl:for-each>
    </xsl:template>
    
    <!-- display hearing for satellite court -->
    <xsl:template match="cs:CourtLists" mode="satellite">
        <xsl:param name="language"/>
        <xsl:variable name="lists">
            <xsl:value-of select="count(cs:CourtList/cs:CourtHouse/cs:Description[. != $firstCourtHouseCode])"/>
        </xsl:variable>
        <xsl:for-each select="cs:CourtList/cs:CourtHouse/cs:Description[. != $firstCourtHouseCode and not(. = ../../preceding-sibling::cs:CourtList/cs:CourtHouse/cs:Description)]">
            <xsl:variable name="crtCode">
                <xsl:value-of select="."/>
            </xsl:variable>
            <xsl:variable name="with">
                <xsl:value-of select="count(../../../cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithFixedDate)"/>
            </xsl:variable>
            <xsl:variable name="withOut">
                <xsl:value-of select="count(../../../cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithoutFixedDate)"/>
            </xsl:variable>
            <!-- Line breaks to display the satelitte court details -->
            <xsl:if test="$with > 0 or $withOut > 0">
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
				 <table width="100%"> 
					<tr>
                        <td width="40%" valign="top">
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="'The following cases are warned for'"/>
							</xsl:call-template>
							<xsl:text>: </xsl:text>
						</td>
						 <td width="60%" valign="top">
							<xsl:for-each select="../../../cs:CourtList/cs:CourtHouse/cs:Description[. = $crtCode]">
								<xsl:call-template name="getValue">
									<xsl:with-param name="language" select="$language"/>
									<xsl:with-param name="key" select="substring-after(../cs:CourtHouseName,'at ')"/>
								</xsl:call-template>
								<xsl:if test="position() != last()">
									<br/>
								</xsl:if>
							</xsl:for-each>
						</td>
					</tr>
				</table>
                <hr/>
            </xsl:if>
            <xsl:if test="$with > 0">
                <b>
                    <u>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Fixtures'"/>
                        </xsl:call-template>
                        <br/>
                        <br/>
                    </u>
                </b>
            </xsl:if>
			<!--Find all with fixed date that share this hearing type, but will have not been processed before (have court id of crtCode)-->
            <xsl:for-each select="../../../cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithFixedDate[not(self::*/@HearingType=preceding::cs:WithFixedDate[../cs:CourtHouse/cs:Description = $crtCode]/@HearingType)]">
            	<!--The Hearing/ListNote will be populated with the Hearing Sequence Number so can be used to order by-->
				<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/cs:ListNote" order="ascending" data-type="number"/>
                <xsl:variable name="hearingType" select="./@HearingType"/>
                <!-- display the case details -->
                <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithFixedDate/cs:Fixture/cs:Cases/cs:Case
                                    [../../../@HearingType = $hearingType]">
                    <xsl:if test="position()=1">
                        <!-- Display the hearing description -->
                        <b>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
                            </xsl:call-template>
                        </b>
                    </xsl:if>
                    <xsl:call-template name="case">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </xsl:for-each>
            </xsl:for-each>
            <xsl:if test="$with >0">
                <hr/>
            </xsl:if>
            <xsl:for-each select="../../../cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithoutFixedDate[not(self::*/@HearingType=preceding::cs:WithFixedDate[../cs:CourtHouse/cs:Description = $crtCode]/@HearingType)]">
   				<!--The Hearing/ListNote will be populated with the Hearing Sequence Number so can be used to order by-->
				<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/cs:ListNote" order="ascending" data-type="number"/>
                <xsl:variable name="hearingType" select="./@HearingType"/>
                <!-- display the case details -->
                <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithoutFixedDate/cs:Fixture/cs:Cases/cs:Case
                                        [../../../@HearingType = $hearingType]">
                    <xsl:if test="position()=1">
                        <!-- Display the hearing description -->
                        <b>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
                            </xsl:call-template>
                        </b>
                    </xsl:if>
                    <xsl:call-template name="case">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </xsl:for-each>
                <hr/>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    
    <!-- display Crown Court details -->
    <xsl:template match="cs:CrownCourt">
        <xsl:param name="language"/>
        <xsl:variable name="reporttype">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Criminal Warned List'"/>
            </xsl:call-template>
        </xsl:variable>
        <font size="5" style="font-family: Arial Narrow;">
            <xsl:variable name="theCC">
                <xsl:text>The </xsl:text>
                <xsl:value-of select="cs:CourtHouseType"/>
            </xsl:variable>
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="$theCC"/>
            </xsl:call-template>
            <br/>
            <font size="3" style="font-family: Arial Narrow;">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'at'"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="cs:CourtHouseName"/>
                </xsl:call-template>
            </font>
        </font>
        <font size="5" style="font-family: Arial Narrow;">
            <center>
                <xsl:value-of select="$reporttype"/>
                <br/>
            </center>
        </font>
        <center>
            <font size="2" style="font-family: Arial Narrow;">
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
            </font>
        </center>
    </xsl:template>
    <!--
         ***********************
        TEMPLATE NAMES
        ***********************
    -->
    <!-- template to bullet text-->
    <xsl:template name="bulletRow">
        <xsl:param name="bulletText"/>
        <xsl:param name="bulletWidth" select="5"/>
        <xsl:param name="language"/>
        <tr>
            <xsl:element name="td">
                <xsl:attribute name="width"><xsl:value-of select="concat($bulletWidth,'%')"/></xsl:attribute>
                <xsl:attribute name="align">right</xsl:attribute>
                <xsl:attribute name="valign">top</xsl:attribute>
                <li/>
            </xsl:element>
            <xsl:variable name="remainder" select="100-$bulletWidth"/>
            <xsl:element name="td">
                <xsl:attribute name="width"><xsl:value-of select="concat($remainder,'%')"/></xsl:attribute>
                <xsl:choose>
                    <xsl:when test="starts-with(. , 'PLEASE NOTE THIS IS')">
                        <xsl:value-of select="substring-before(.,'--------------------------------------------- ')"/>
                        <br/>
                        <xsl:value-of select="substring-after(.,'WARNED LIST')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$bulletText"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:element>
        </tr>
    </xsl:template>
    
    <!-- display the case details -->
    <xsl:template name="case">
        <xsl:param name="language"/>
        <br/>
        <xsl:variable name="prosecutingref">
			<xsl:choose>
				<xsl:when test="contains(cs:Prosecution//cs:OrganisationName,'Crown Prosecution Service')">
					<xsl:choose>
						<xsl:when test="starts-with(cs:Prosecution/cs:ProsecutingReference , 'CPS:')">
							<xsl:value-of select="substring-after(cs:Prosecution/cs:ProsecutingReference,'CPS:')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="cs:Prosecution/cs:ProsecutingReference"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
        </xsl:variable>
        <!-- display the defendant details -->
        <xsl:call-template name="processdefendants">
            <xsl:with-param name="caseNumText" select="cs:CaseNumber"/>
            <xsl:with-param name="committingText" select="cs:CaseArrivedFrom/cs:OriginatingCourt/cs:Description/@CourtHouseShortName"/>
            <xsl:with-param name="prosecuteRefText" select="$prosecutingref"/>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
        <table width="100%" class="detail">
            <tr>
                <td width="10%"/>
                <td width="90%"/>
            </tr>
            <!-- display prosecuting organisation name -->
            <xsl:if test="(cs:Prosecution//cs:OrganisationName) and not(contains(cs:Prosecution//cs:OrganisationName,'Crown Prosecution Service'))">
                <tr>
                    <td/>
                    <td>
                        <small>
                            <!-- display the prosecuting organisation -->
                            <xsl:variable name="prosecutingorganisation">
                                <xsl:choose>
                                    <xsl:when test="starts-with(cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName, '(') and substring(cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName,string-length(cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName)) = ')'">
                                        <xsl:value-of select="substring(cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName,2,(string-length(cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName)-2))"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:variable>
                            <xsl:text>(</xsl:text>
							<xsl:choose>
								<xsl:when test="contains($prosecutingorganisation,':')">
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="substring-before($prosecutingorganisation,': ')"/>
									</xsl:call-template>
									<xsl:text>: </xsl:text>
									<xsl:variable name="prosecutingorganisationlookup">
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="substring-after($prosecutingorganisation,': ')"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="$prosecutingorganisationlookup"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
								<xsl:text>Prosecutor: </xsl:text>
								<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="$prosecutingorganisation"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
                            <xsl:text>)</xsl:text>
                        </small>
                    </td>
                </tr>
            </xsl:if>
            <!-- check value against default -->
            <xsl:if test="../../cs:FixedDate[not(. = '1900-01-01')]">
                <tr>
                    <td/>
                    <td>
                        <small>
                            <b>
                                <!-- display the fixed date -->
                                <xsl:variable name="fixDate" select="../../cs:FixedDate"/>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Fixed for'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="displayDate_mon">
                                    <xsl:with-param name="input">
                                        <xsl:value-of select="$fixDate"/>
                                    </xsl:with-param>
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'at'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="substring-after(../../../../cs:CourtHouse/cs:CourtHouseName,'at ')"/>
                                </xsl:call-template>
                            </b>
                        </small>
                    </td>
                </tr>
            </xsl:if>
            <!-- Display any Notes that may exist -->
            <xsl:if test="../../cs:Notes">
                <tr>
                    <td/>
                    <td>
                        <small>
                            <b>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="../../cs:Notes"/>
                                </xsl:call-template>
                            </b>
                        </small>
                    </td>
                </tr>
            </xsl:if>
        </table>
        <!-- display any linked case details - only list first 16 linked cases - new table-->
        <xsl:if test="../../cs:LinkedCases">
            <table class="detail" width="100%">
                <tr>
                    <td width="10%"/>
                    <td width="90%"/>
                </tr>
                <tr>
                    <td valign="top">
                        <small>
                            <xsl:text>(</xsl:text>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Linked with'"/>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </small>
                    </td>
                    <td valign="top">
                        <small>
                            <xsl:for-each select="../../cs:LinkedCases/cs:CaseNumber">
                                <xsl:if test="position() &lt;= 16">
                                    <xsl:choose>
                                        <xsl:when test="contains(.,'FIX')">
                                            <xsl:variable name="FIX">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="'FIX'"/>
                                                </xsl:call-template>
                                            </xsl:variable>
                                            <xsl:value-of select="concat(substring-before(.,'FIX'),$FIX,substring-after(.,'FIX'))"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="."/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:if test="position() != last()">
                                        <xsl:text>; </xsl:text>
                                    </xsl:if>
                                </xsl:if>
                                <xsl:if test="position() = 17">
                                    <!--display '& others)' text - not the 17th value -->
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'&amp; others'"/>
                                    </xsl:call-template>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text>)</xsl:text>
                        </small>
                    </td>
                </tr>
            </table>
        </xsl:if>
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
    
    <!-- display name details -->
    <xsl:template name="formalName">
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
            <xsl:with-param name="court" select="/cs:WarnedList/cs:CrownCourt"/>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
    </xsl:template>
    
    <!-- template to construct footer information -->
    <xsl:template name="listFooterDisplay">
        <!-- creates the footer in the output -->
        <xsl:param name="court"/>
        <xsl:param name="language"/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <hr/>
        <table class="detail" width="95%">
            <tr>
                <td align="left">
                    <small>
                        <!-- Display court address details -->
                        <xsl:if test="$court/cs:CourtHouseAddress">
                            <xsl:for-each select="$court/cs:CourtHouseAddress/apd:Line[not (.='-') and not (.=' ')]">
                                <xsl:variable name="addressLine">
                                     <xsl:value-of select="."/>
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
                        <!--<xsl:if test="$court/cs:CourtHouseFax">
                            <xsl:text> Fax: </xsl:text>
                            <xsl:value-of select="$court/cs:CourtHouseFax"/>
                        </xsl:if>-->
                    </small>
                </td>
            </tr>
            <tr>
                <!--published date-->
                <td align="left">
                    <small>
                        <xsl:call-template name="publishDate">
                            <xsl:with-param name="language" select="$language"/>
                        </xsl:call-template>
                    </small>
                </td>
                <td align="right">
                    <small>
                        <!-- display print reference -->
                        <xsl:value-of select="cs:WarnedList/cs:ListHeader/cs:CRESTprintRef"/>
                    </small>
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
    
    <!-- display defendant details -->
    <xsl:template name="processdefendants">
        <xsl:param name="caseNumText"/>
        <xsl:param name="committingText"/>
        <xsl:param name="prosecuteRefText"/>
        <xsl:param name="language"/>
        <table width="100%" class="detail">
			<xsl:if test="not(cs:Defendants/cs:Defendant)">
				<tr>
                    <td width="8%" valign="top">
                        <small>
							<xsl:value-of select="$caseNumText"/>
						</small>
					</td>
					<td/>
					 <td width="8%" valign="top">
						<small>
							NO DEFENDANTS
						</small>
					</td>
				</tr>
			</xsl:if>
            <xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name">
                <xsl:variable name="asterisk">
                    <xsl:if test="../../cs:CustodyStatus = 'In custody' or
                              ../../cs:CustodyStatus = 'On remand'">
                        <strong>
                            <xsl:text>*</xsl:text>
                        </strong>
                    </xsl:if>
                </xsl:variable>
                <!-- set up defendant name details -->
                <xsl:variable name="defendant">
                    <xsl:value-of select="apd:CitizenNameSurname"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="apd:CitizenNameForename[position()=1]"/>
                    <xsl:if test="apd:CitizenNameForename[position()=2]">
                        <xsl:text> </xsl:text>
                        <xsl:variable name="init">
                            <!-- second forename element is the middle name -->
                            <xsl:value-of select="substring(apd:CitizenNameForename[position()=2],1,1)"/>
                        </xsl:variable>
                        <xsl:call-template name="toUpper">
                            <xsl:with-param name="content" select="$init"/>
                        </xsl:call-template>
                    </xsl:if>
					<br/>
					<xsl:if test="../cs:Sex">
						<xsl:variable name="gender">
							<xsl:call-template name="TitleCase">
								<xsl:with-param name="text" select="../cs:Sex"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:if test="contains($gender,'M') or contains($gender,'F')">
							<xsl:variable name="genderlookup">
								<xsl:call-template name="getValue">
									<xsl:with-param name="language" select="$language"/>
									<xsl:with-param name="key" select="$gender"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:value-of select="substring($genderlookup,1,1)"/>
						</xsl:if>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:if test="../cs:DateOfBirth">
						<xsl:call-template name="displayDate_mon">
							<xsl:with-param name="input">
								<xsl:value-of select="../cs:DateOfBirth/apd:BirthDate"/>
							</xsl:with-param>
							<xsl:with-param name="language" select="$language"/>
						</xsl:call-template>
					</xsl:if>
                    <!-- display additional defendant details, only if defendant is in custody-->
                    <xsl:if test="../../cs:CustodyStatus = 'In custody' or ../../cs:CustodyStatus = 'On remand'">
                        <!--also display remand prison name and prisoner no if defendant is in custody-->
						<xsl:text> </xsl:text>
						<!--only display prison location if it is not 'Not Available'-->
						<xsl:if test="../../cs:PrisonLocation/cs:Location!='Not Available'">
							<xsl:variable name="prisonLocation">
								<xsl:call-template name="getValue">
									<xsl:with-param name="language" select="$language"/>
									<xsl:with-param name="key" select="../../cs:PrisonLocation/cs:Location"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="toUpper">
								<xsl:with-param name="content" select="$prisonLocation"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
						</xsl:if>
						<xsl:value-of select="../../cs:PrisonerID"/>
					</xsl:if>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="position()=1">
                        <tr>
                            <td width="8%" valign="top">
                                <small>
                                    <xsl:value-of select="$caseNumText"/>
                                </small>
                            </td>
                            <td width="2%" valign="top">
                                <small>
                                    <xsl:value-of select="$asterisk"/>
                                </small>
                            </td>
                            <td width="30%" valign="top">
                                <small>
                                    <xsl:copy-of select="$defendant"/>
                                </small>
                            </td>
                            <td width="30%" valign="top">
                                <small>
                                    <!-- display solicitor details -->
                                    <xsl:variable name="notrepresented">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'NO REPRESENTATION RECORDED'"/>
                                        </xsl:call-template>
                                    </xsl:variable>
                                    <xsl:call-template name="solicitorDetails">
                                        <xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
                                        <xsl:with-param name="nobody" select="$notrepresented"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </small>
                            </td>
                            <td width="15%" valign="top">
                                <small>
                                    <xsl:value-of select="$committingText"/>
                                </small>
                            </td>
                            <td width="15%" valign="top">
                                <small>
                                    <xsl:value-of select="../../cs:URN"/>
                                    <xsl:if test="contains($prosecuteRefText,'Crown Prosecution Service')=false">
                                      <xsl:value-of select="$prosecuteRefText"/>
                                    </xsl:if>
                                    <xsl:if test="contains($prosecuteRefText,'Crown Prosecution Service') and ($caseNumText='CPP' or $caseNumText = 'cpp')">
										<xsl:text> CPS</xsl:text>
                                    </xsl:if>
                                </small>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:otherwise>
                        <tr>
                            <td/>
                            <td valign="top">
                                <xsl:value-of select="$asterisk"/>
                            </td>
                            <td valign="top">
                                <small>
                                    <xsl:copy-of select="$defendant"/>
                                </small>
                            </td>
                            <td valign="top">
                                <small>
                                    <!-- display solicitor details -->
                                    <xsl:variable name="notrepresented">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'NO REPRESENTATION RECORDED'"/>
                                        </xsl:call-template>
                                    </xsl:variable>
                                    <xsl:call-template name="solicitorDetails">
                                        <xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
                                        <xsl:with-param name="nobody" select="$notrepresented"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </small>
                            </td>
                            <td/>
                            <td width="15%" valign="top">
                                <small>
                                    <xsl:value-of select="../../cs:URN"/>
                                </small>
                            </td>
                        </tr>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </table>
    </xsl:template>
    
    <!-- Display the published Details -->
    <xsl:template name="publishDate">
        <xsl:param name="language"/>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="'Published'"/>
        </xsl:call-template>
        <xsl:text>: </xsl:text>
        <xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime"/>
        <xsl:call-template name="displayDate">
            <xsl:with-param name="input">
                <xsl:value-of select="substring($pubTime,1,10)"/>
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
		<xsl:value-of select="substring($pubTime,12,5)"/>
    </xsl:template>
    
    <!-- display the solicitor details -->
    <xsl:template name="solicitorDetails">
        <xsl:param name="party"/>
        <xsl:param name="nobody" select="'In person'"/>
        <xsl:param name="language"/>
        <xsl:choose>
            <xsl:when test="$party/cs:Person">
                <!-- display the name -->
                <xsl:call-template name="formalName">
                    <xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name"/>
                </xsl:call-template>
                <!-- display the first line of address -->
                <xsl:text>, </xsl:text>
                <xsl:value-of select="$party/cs:Person/cs:PersonalDetails/cs:Address/apd:Line[4]"/>
            </xsl:when>
            <!-- display the organisation name -->
            <xsl:when test="$party/cs:Organisation">
                <xsl:choose>
                    <xsl:when test="$party/cs:Organisation/cs:OrganisationName = 'NO REPRESENTATION RECORDED' or $party/cs:Organisation/cs:OrganisationName = 'NOT REPRESENTED'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="$party/cs:Organisation/cs:OrganisationName"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$nobody"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- template used to format string and upper case first letter of each word -->
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
    
    <!-- display warned list details -->
    <xsl:template name="WarnedListDetails">
        <xsl:param name="language"/>
        <xsl:variable name="WLText">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'The undermentioned cases are warned for hearing during the period'"/>
            </xsl:call-template>
        </xsl:variable>
        <table width="100%">
            <xsl:call-template name="bulletRow">
                <xsl:with-param name="bulletText" select="$WLText"/>
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
        </table>
        <br/>
        <b>
            <center>
                <!-- display report date -->
                <xsl:call-template name="displayDate">
                    <xsl:with-param name="input" select="$reportdate"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'to'"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:call-template name="displayDate">
                    <xsl:with-param name="input" select="$endDate"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </center>
        </b>
        <!--make this line centred and not full length-->
        <table width="100%">
            <tr>
                <td width="33.33%"/>
                <td width="33.33%">
                    <hr/>
                </td>
                <td/>
            </tr>
        </table>
        <!-- display listing instructions -->
        <table width="100%">
            <xsl:for-each select="/cs:WarnedList/cs:ListingInstructions/cs:ListingInstruction">
                <xsl:variable name="text">
                    <xsl:choose>
                        <xsl:when test="starts-with(. , 'no later than') or starts-with(., 'immediately')">
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Any representation about the listing of a case should be made to the Listing Officer'"/>
                            </xsl:call-template>
                            <br/>
                            <strong>
                                <xsl:choose>
                                    <xsl:when test="starts-with(. , 'no later than')">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'no later than'"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                        <xsl:variable name="nolaterthantime">
                                            <xsl:value-of select="substring-before(substring-after(.,'no later than '),' on ')"/>
                                        </xsl:variable>
                                        <xsl:value-of select="$nolaterthantime"/>
                                        <!--<xsl:choose>
                                            <xsl:when test="contains($nolaterthantime,'am')">
                                                <xsl:variable name="nolaterthanam">
                                                    <xsl:call-template name="getValue">
                                                        <xsl:with-param name="language" select="$language"/>
                                                        <xsl:with-param name="key" select="'am'"/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:value-of select="concat(substring-before($nolaterthantime,'am'),$nolaterthanam)"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:variable name="nolaterthanpm">
                                                    <xsl:call-template name="getValue">
                                                        <xsl:with-param name="language" select="$language"/>
                                                        <xsl:with-param name="key" select="'pm'"/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:value-of select="concat(substring-before($nolaterthantime,'pm'),$nolaterthanpm)"/>
                                            </xsl:otherwise>
                                        </xsl:choose>-->
                                        <xsl:text> </xsl:text>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'on'"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                        <xsl:variable name="nolaterthanday">
                                            <xsl:value-of select="substring-before(substring-after(.,' on '),' ')"/>
                                        </xsl:variable>
                                        <xsl:if test="string-length($nolaterthanday) > 0">
                                            <xsl:value-of select="$nolaterthanday"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:variable name="nolaterthanmonth">
                                                <xsl:value-of select="substring-before(substring-after(.,concat(' on ',$nolaterthanday,' ')),' ')"/>
                                            </xsl:variable>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="$nolaterthanmonth"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:variable name="nolaterthanyear">
                                                <xsl:value-of select="substring-after(.,concat($nolaterthanmonth,' '))"/>
                                            </xsl:variable>
                                            <xsl:value-of select="$nolaterthanyear"/>
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'immediately'"/>
                                        </xsl:call-template>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </strong>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="."/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:if test="string-length(normalize-space($text)) > 0">
                    <!-- bullet point the text -->
                    <xsl:call-template name="bulletRow">
                        <xsl:with-param name="bulletText" select="$text"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:for-each>
            <xsl:variable name="text2">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'The prosecuting authority is the Crown Prosecution Service unless otherwise stated.'"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:call-template name="bulletRow">
                <xsl:with-param name="bulletText" select="$text2"/>
                <xsl:with-param name="language" select="$language"/>
            </xsl:call-template>
            <tr>
                <td>
                    <table width="85%">
                        <tr>
                            <td align="right" style="font-size: large; font-weight: bold;">
                                <xsl:text>* </xsl:text>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <xsl:variable name="incustody">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'* Denotes a defendant in custody'"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="substring-after($incustody,'* ')"/>
                </td>
            </tr>
        </table>
        <hr/>
    </xsl:template>
</xsl:stylesheet>
