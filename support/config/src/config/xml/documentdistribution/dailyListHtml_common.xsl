<?xml version="1.0" encoding="UTF-8"?>
<!--
    +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.1"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails">

    <xsl:output method="html" indent="yes"/>
    
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>

    <!-- 
        COMMENTS
        Transformer used by MLD to tranform XML into HTML for the Daily List
    -->
    <!-- Global Variables -->
    <!-- Global Variable to hold the Current House House Code -->
    <xsl:variable name="CurrentCourtCode">
        <xsl:value-of select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseCode"/>
    </xsl:variable>
    <!-- Pick up Parent/Satellite Court Names -->
    <xsl:variable name="CourtNames">
        <!-- display the first court name -->
        <xsl:value-of select="/cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseName"/>
        <!-- loop rest of court lists and pick up those which are not of the same court code as the main court (first court list) -->
        <xsl:for-each select="/cs:DailyList/cs:CourtLists/cs:CourtList[not(position() = 1)]/cs:CourtHouse[cs:Description = 'SATELLITE']">
            <xsl:choose>
                <!-- if not last display comma and name -->
                <xsl:when test="position() != last()">
                    <xsl:text>, </xsl:text>
                    <xsl:value-of select="./cs:CourtHouseName"/>
                </xsl:when>
                <!-- when last display 'and ' name -->
                <xsl:otherwise>
                    <xsl:text> and </xsl:text>
                    <xsl:value-of select="./cs:CourtHouseName"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:variable>
    <!-- Pick up the first court house type from the Court List -->
    <xsl:variable name="CourtType">
        <xsl:for-each select="cs:DailyList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:CourtHouseType">
            <xsl:value-of select="."/>
        </xsl:for-each>
    </xsl:variable>
    
    <!-- Display Daily List -->
    <xsl:template name="DisplayDailyList">
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
        <html>
            <meta  content="text/html;  charset=UTF-8"  http-equiv="Content-Type"/>
            <body style="font-family: Arial Narrow;">
                <!-- display the Crown Court details -->
                <xsl:apply-templates select="./cs:CrownCourt">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="listType" select="$listType"/>
                </xsl:apply-templates>
                <!-- display Court List Info -->
                <xsl:apply-templates select="./cs:CourtLists">
                    <xsl:with-param name="language" select="$language"/>
                                 <xsl:with-param name="listType" select="$listType"/>
                </xsl:apply-templates>
                <!-- display footer information -->
                    <xsl:call-template name="ListFooter">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- Finish with Copyright notice -->
                <br/>
                <xsl:call-template name="displayCopyright">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
            </body>
        </html>
    </xsl:template>
    <!--        
            **************************
            TEMPLATE MATCHES 
            *************************
    -->
    <!-- display Court List Info -->
    <xsl:template match="cs:CourtLists">
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
        <xsl:for-each select="cs:CourtList">
            <xsl:if test="count(./cs:CourtHouse/cs:CourtHouseName) = 1">
                <table width="100%" style="font-size:8pt;font-weight:bold">
                    <tr>
                        <td>
                            <!-- display the court house name -->
                            <xsl:variable name="courtHouseName">
                                <xsl:value-of select="./cs:CourtHouse/cs:CourtHouseName"/>
                            </xsl:variable>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="$courtHouseName"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </table>
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
                        <table width="100%" style="font-size:8pt">
                            <tr>
                                <td>
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
                                    <xsl:choose>
                                        <xsl:when test="cs:SittingAt">
                                            <xsl:text> - </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'sitting at'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:apply-templates select="cs:SittingAt"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </table>
                    </xsl:when>
                    <xsl:otherwise>
                        <table width="100%" style="font-size:8pt;font-weight:bold">
                            <tr>
                                <td>
                                    <!-- floating, display the following text -->
                                    <xsl:text>The following may be taken in any court.</xsl:text>

                                </td>
                            </tr>
                        </table>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- display the judge information -->
                <xsl:call-template name="JudgeDetails">
                    <xsl:with-param name="Judge" select="./cs:Judiciary"/>
                    <xsl:with-param name="language" select="$language"/>
                </xsl:call-template>
                <!-- display any sitting note details -->
                <table width="100%" style="font-size:8pt;font-weight:bold">
                    <tr>
                        <td>
                            <xsl:if test="cs:SittingNote">
                                <xsl:variable name="UPPERSittingNote">
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="cs:SittingNote"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:value-of select="cs:SittingNote"/>
                            </xsl:if>
                        </td>
                    </tr>
                </table>
                <!-- display information for each hearing -->
                <!-- Hearings with sequence numbers first -->
                <xsl:for-each select="cs:Hearings/cs:Hearing">
                    <xsl:if test="cs:HearingSequenceNumber != ''">
                        <xsl:call-template name="hearing">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="listType" select="$listType"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
                <!-- Hearings with out sequence numbers -->
                <xsl:for-each select="cs:Hearings/cs:Hearing">
                    <xsl:if test="cs:HearingSequenceNumber = ''">
                        <xsl:call-template name="hearing">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="listType" select="$listType"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
                <hr/>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <!-- display the Crown Court details -->
    <xsl:template match="cs:CrownCourt">
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
        <font size="5">
            <strong>
                <!-- pick up first court type -->
                <xsl:variable name="theCC">
                    <xsl:text>The </xsl:text>
                    <xsl:value-of select="$CourtType"/>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$theCC"/>
                </xsl:call-template>
            </strong>
            <br/>
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'at'"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
            <!-- pick up parent/satellite court info -->
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
        </font>
        <h2>
            <table width="100%">
                <tr>
                    <td align="center">
                        <font size="5pt">
                            <b>
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
                                        <xsl:value-of select="../cs:ListHeader/cs:StartDate"/>
                                    </xsl:with-param>
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </b>
                        </font>
                    </td>
                </tr>
                    <xsl:if test="$listType='DLP'">
                        <tr>
                            <td align="center">
                                <font size="5pt">
                                    <b>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'For Prison Use Only'"/>
                                        </xsl:call-template>
                                    </b>
                                </font>
                            </td>
                        </tr>
                    </xsl:if>
                <tr>
                    <td align="center">
                        <xsl:variable name="initversion" select="../cs:ListHeader/cs:Version"/>                 
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
                    </td>
                </tr>
            </table>
        </h2>
        <hr/>
    </xsl:template>
    <!-- display the sitting at time -->
    <xsl:template match="cs:SittingAt">
        <xsl:choose>
            <xsl:when test="string-length(.)=8">
                <!-- call template to postfix AM or PM -->
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="input">
                        <xsl:value-of select="substring(.,1,5)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="string-length(.)=7">
                <!-- call template to postfix AM or PM -->
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="input">
                        <xsl:value-of select="substring(.,1,4)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--        
            ***********************
            TEMPLATE NAMES 
            **********************
    -->
    <!-- Return number to represent the day of the week
            0   Sunday
            1   Monday
            2   Tuesday
            3   Wednesday
            4   Thursday
            5   Friday
            6   Saturday
    -->
    <xsl:template name="calculate-day-of-the-week">
        <xsl:param name="year"/>
        <xsl:param name="month"/>
        <xsl:param name="day"/>
        <xsl:variable name="a" select="floor((14 - $month) div 12)"/>
        <xsl:variable name="y" select="$year - $a"/>
        <xsl:variable name="m" select="$month + 12 * $a - 2"/>
        <xsl:value-of select="($day + $y + floor($y div 4) - floor($y div 100) + floor($y div 400) + floor((31 * $m) div 12)) mod 7"/>
    </xsl:template>
    <!-- display the defendant details -->
    <xsl:template name="DefendantDetails">
        <xsl:param name="caseNumText"/>
        <xsl:param name="committingText"/>
        <xsl:param name="prosecuteRefText"/>
        <xsl:param name="appendText"/>
        <xsl:param name="language"/>
        <xsl:param name="listType"/>
        <table class="detail" width="100%" style="font-size: 8pt">
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
                        <xsl:with-param name="text" select="apd:CitizenNameForename[position()=1]"/>
                    </xsl:call-template>
                    <!-- middle name, get initial only -->
                    <xsl:if test="apd:CitizenNameForename[position()=2]">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="substring(apd:CitizenNameForename[position()=2],1,1)"/>
                    </xsl:if>
                    <xsl:value-of select="$appendText"/>
                    <!-- only display if DLP -->
                    <xsl:if test="$listType='DLP'">
                        <br/>
                        <table class="info" width="75%" style="font-size: 8pt" cellpadding="0pt" cellspacing="0pt">
                            <tr>
                                <td width="30%">
                                    <xsl:if test="../cs:DateOfBirth">
                                        <xsl:call-template name="displayDate_mon">
                                            <xsl:with-param name="input" select="../cs:DateOfBirth/apd:BirthDate"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                        <xsl:text> </xsl:text>
                                    </xsl:if>
                                </td>
                                <td width="25%">
                                    <xsl:if test="../../cs:PrisonerID">
                                        <xsl:value-of select="../../cs:PrisonerID"/>
                                        <xsl:text> </xsl:text>
                                    </xsl:if>
                                </td>
                                <td width="45%">
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
                                </td>
                            </tr>
                        </table>
                    </xsl:if>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="position()=1">
                        <tr>
                            <td width="10%" valign="top">
                                <xsl:value-of select="$caseNumText"/>
                            </td>
                            <td width="50%" valign="top">
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
                            </td>
                            <td width="10%" valign="top">
                                <xsl:value-of select="$committingText"/>
                            </td>
                            <td width="30%" valign="top">
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
											<br/>
											<xsl:value-of select="$ptiurn"/>
									    </xsl:if>
									    <xsl:if test="$listType='DL'">
											<br/>
											<xsl:value-of select="$ptiurn"/>
									    </xsl:if>
                                        </xsl:otherwise>
                                </xsl:choose>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:otherwise>
                        <tr>
                            <td/>
                            <td>
                                <xsl:copy-of select="$defendant"/>
                            </td>                            
                            <td/>
                            <td>
                            <xsl:choose>
							   <xsl:when test="starts-with($prosecuteRefText,'CPS Ref:')">                                    
                                        <xsl:if test="$ptiurn">
                                             <xsl:value-of select="$ptiurn"/>
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:if test="$listType='DLP'">
											<br/>
											<xsl:value-of select="$ptiurn"/>
									    </xsl:if>
									    <xsl:if test="$listType='DL'">
											<br/>
											<xsl:value-of select="$ptiurn"/>
									    </xsl:if>
                                     </xsl:otherwise>
                            </xsl:choose>                           
                            </td>
                        </tr>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </table>
    </xsl:template>
    <!-- template used to format an input date string e.g. 2003-10-27 and output the date in the required format -->
    <!-- i.e. 27 October 2003 -->
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
    <!-- template used to format an input date string e.g. 2003-10-27 and output the date in the required format (including the day) -->
    <!-- i.e. Monday 27 October 2003 -->
    <xsl:template name="displayDayDate">
        <xsl:param name="input"/>
        <xsl:param name="language"/>
        <xsl:variable name="month" select="substring($input,6,2)"/>
        <xsl:variable name="day" select="substring($input,9,2)"/>
        <xsl:variable name="year" select="substring($input,1,4)"/>
        <xsl:variable name="dayOfWeek">
            <xsl:call-template name="calculate-day-of-the-week">
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
    <!-- template used to format an input date string e.g. 2003-10-27 and output the date in the required format -->
    <!-- i.e. 27-OCT-2003 -->
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
    <!-- template used to format the time from 24hrs to the following format HH:MM [AM/PM]-->
    <!-- display the formatted time and then post fix with AM or PM -->
    <xsl:template name="FormatTime">
        <xsl:param name="input"/>
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
    <!-- Template to get the Sitting At time -->
    <xsl:template name="getSittingTime">
        <xsl:apply-templates select="../../cs:SittingAt"/>
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
        <!-- display TimeMarkingNotes -->
        <table width="100%" style="font-size:8pt;font-weight:bold">
            <tr>
                <td>
                    <xsl:if test="cs:TimeMarkingNote">
                        <xsl:if test="not (cs:TimeMarkingNote = ' ')">
                            <xsl:variable name="upperMarkingNote">
                                <xsl:call-template name="toUpper">
                                    <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                                </xsl:call-template>
                            </xsl:variable>
                            <!--valid text is 'NOT BEFORE' or 'SITTING AT'-->
                            <xsl:variable name="NotBefore">
                                <!--If no NOT BEFORE or SITTING AT in the text then set it to NOT BEFORE -->
                            </xsl:variable>
                            <xsl:choose>
                                <!-- could potentially be blank, so check that it contains a colon -->
                                <xsl:when test="contains(cs:TimeMarkingNote,':')">
                                    <!-- Only Display the Not Before Time if the value is different from the Sitting At Time -->
                                    <xsl:if test=" $SittingAtValue != $NotBeforeTime">
                                        <xsl:choose>
                                            <!-- check to ensure that it does not already contain an AM or PM within the TimeMarkingNote -->
                                            <xsl:when test="not(contains(cs:TimeMarkingNote,'am') or contains(cs:TimeMarkingNote,'pm') or contains(cs:TimeMarkingNote,'AM') or contains(cs:TimeMarkingNote,'PM') or contains(cs:TimeMarkingNote,'Am') or contains(cs:TimeMarkingNote,'Pm') or contains(cs:TimeMarkingNote,'pM') or contains(cs:TimeMarkingNote,'aM'))">
                                                <!-- apply template to format the time -->
                                                <xsl:value-of select="$NotBefore"/>
                                                <xsl:call-template name="FormatTime">
                                                    <xsl:with-param name="input">
                                                        <xsl:value-of select="cs:TimeMarkingNote"/>
                                                    </xsl:with-param>
                                                </xsl:call-template>
                                                <br/>
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
                                        <xsl:if test="not(cs:HearingSequenceNumber=1)">
                                            <!--<xsl:value-of select="cs:HearingSequenceNumber[position()]"></xsl:value-of>-->
                                            <!--<xsl:if test="cs:HearingSequenceNumber[position() !=1]">-->
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
                                            <br/>
                                        </xsl:if>
                                    </xsl:if>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="cs:TimeMarkingNote"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:if>
                    </xsl:if>
                </td>
            </tr>
        </table>
        <!-- display hearing description exc. Miscellaneous text -->
        <xsl:choose>
            <xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
                <table width="100%" style="font-size:8pt;font-weight:bold">
                    <tr>
                        <td>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="substring-after($hearingDescription,'Miscellaneous ')"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <table width="100%" style="font-size:8pt;font-weight:bold">
                    <tr>
                        <td>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="$hearingDescription"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </table>
            </xsl:otherwise>
        </xsl:choose>
        <!-- get the prosecuting organisation name -->
        <xsl:variable name="prosecutingref">
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
            <xsl:when test="cs:Defendants">
                <!-- display the defendant detail -->
                <xsl:call-template name="DefendantDetails">
                    <xsl:with-param name="caseNumText" select="cs:CaseNumber"/>
                    <xsl:with-param name="committingText" select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
                    <xsl:with-param name="prosecuteRefText" select="$prosecutingref"/>
                    <xsl:with-param name="appendText" select="$appendage"/>
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="listType" select="$listType"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <table width="100%" style="font-size: 8pt">
                    <tr>
                        <td width="10%" valign="top">
                            <xsl:value-of select="cs:CaseNumber"/>
                        </td>
                        <td width="50%" valign="top"/>
                        <td width="10%" valign="top">
                            <xsl:value-of select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
                        </td>
                        <td width="30%" valign="top">
                            <xsl:value-of select="$prosecutingref"/>
                        </td>
                    </tr>
                </table>
            </xsl:otherwise>
        </xsl:choose>
        <!-- display any list notes -->
        <xsl:if test="cs:ListNote">
            <table width="100%" style="font-size: 8pt;font-weight:bold">
                <tr>
                    <td>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="cs:ListNote"/>
                        </xsl:call-template>
                    </td>
                </tr>
            </table>
        </xsl:if>
        <br/>
		<br/>
    </xsl:template>
    <!-- display the judge details -->
    <xsl:template name="JudgeDetails">
        <xsl:param name="Judge"/>
        <xsl:param name="language"/>
        <table class="emphasis" width="100%" style="font-size:8pt;font-weight:bold">
            <xsl:for-each select="$Judge/cs:Judge">
                <tr>
                    <td align="center">
                        <strong>
                            <xsl:if test="apd:CitizenNameRequestedName != 'N/A'">
                                <xsl:choose>
                                    <!-- test to see if there is a Full title for the judge -->
                                    <xsl:when test="apd:CitizenNameRequestedName != ' '">
                                        <xsl:variable name="judgeFullTitle">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="apd:CitizenNameRequestedName"/>                                                                          </xsl:call-template>
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
                        </strong>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
        <xsl:variable name="justiceText">
            <xsl:choose>
                <xsl:when test="count(cs:Judiciary/cs:Justice) =  1">
                    <table width="100%" style="font-size:8pt;font-weight:bold">
                        <tr>
                            <td>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Justice'"/>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                            </td>
                        </tr>
                    </table>
                </xsl:when>
                <xsl:when test="count(cs:Judiciary/cs:Justice) &gt; 1">
                    <table width="100%" style="font-size:8pt;font-weight:bold">
                        <tr>
                            <td>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Justices'"/>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                            </td>
                        </tr>
                    </table>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <table width="100%" class="emphasis" style="font-size:8pt;font-weight:bold">
            <!-- display justice information -->
            <xsl:for-each select="$Judge/cs:Justice">
                <xsl:variable name="justice">
                    <xsl:value-of select="apd:CitizenNameRequestedName"/>
                </xsl:variable>
                <tr>
                    <xsl:choose>
                        <xsl:when test="position()=1">
                            <td width="20%" align="right">
                                <xsl:value-of select="$justiceText"/>
                            </td>
                            <td width="80%" align="left">
                                <xsl:value-of select="$justice"/>
                            </td>
                        </xsl:when>
                        <xsl:otherwise>
                            <td/>
                            <td>
                                <xsl:value-of select="$justice"/>
                            </td>
                        </xsl:otherwise>
                    </xsl:choose>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <!-- Template to get the Not Before Time  - this is solely used to obtain the value for the $NotBeforeTime variable that is used-->
    <!-- to compare to the Sitting at time in the ' display TimeMarkingNotes' section above-->
    <xsl:template name="getNotBeforeTime">
        <xsl:param name="language"/>
        <xsl:if test="contains(cs:TimeMarkingNote,':')">
            <xsl:variable name="UPPER_TMN">
                <xsl:call-template name="toUpper">
                    <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:choose>
                <!-- deal with 'NOT BEFORE' timemarking text -->
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
                        <xsl:when test="not(contains(cs:TimeMarkingNote,'am') or contains(cs:TimeMarkingNote,'pm') or contains(cs:TimeMarkingNote,'AM') or contains(cs:TimeMarkingNote,'PM') or contains(cs:TimeMarkingNote,'Am') or contains(cs:TimeMarkingNote,'Pm') or contains(cs:TimeMarkingNote,'pM') or contains(cs:TimeMarkingNote,'aM'))">
                            <xsl:call-template name="FormatTime">
                                <xsl:with-param name="input">
                                    <xsl:value-of select="cs:TimeMarkingNote"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="toLower">
                                <xsl:with-param name="content" select="cs:TimeMarkingNote"/>
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    <!-- display footer information -->
    <xsl:template name="ListFooter">
        <xsl:param name="language"/>
        <table class="detail" width="100%" style="font-size:8pt">
            <tr>
                <td>
                    <!-- display published details -->
                    <xsl:if test="cs:ListHeader/cs:PublishedTime">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Published'"/>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                        <xsl:call-template name="displayDate">
                            <xsl:with-param name="input">
                                <xsl:value-of select="substring(cs:ListHeader/cs:PublishedTime,1,10)"/>
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
                        <xsl:value-of select="substring(cs:ListHeader/cs:PublishedTime,12,5)"/>
                    </xsl:if>
                </td>
                <!-- display the Print Refence -->
                <td align="right">
                    <xsl:value-of select="/cs:DailyList/cs:ListHeader/cs:CRESTprintRef"/>
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
    <!-- convert string to upper case -->
    <xsl:template name="toUpper">
        <xsl:param name="content"/>
        <xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
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
</xsl:stylesheet>