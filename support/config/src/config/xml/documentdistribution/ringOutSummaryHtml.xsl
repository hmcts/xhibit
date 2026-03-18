<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" version="1.0">
    <!-- Import the common List Distribution Letters templates -->
    <xsl:output method="html" indent="yes"/>
    <!-- Parameter passed in to hold the current date -->
    <xsl:param name="java-date"/>
    <xsl:template match="xhibit:DailyListLetters">
        <html>
            <body>
                <center>
                    <font size="3" style="font-family: Helvetica;">
                        <xsl:text>THE CROWN COURT AT  </xsl:text>
                        <xsl:value-of select="xhibit:DailyListLetter[1]/xhibit:CrownCourt/xhibit:CourtHouseName"/>
                        <br/>
                        <br/>
                        <b>
                            <xsl:text>Ring Out Summary</xsl:text>
                            <br/>
                            <xsl:text>for </xsl:text>
                            <xsl:choose>
                                <xsl:when test="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:Duration">
                                    <xsl:choose>
                                        <!-- Pick up the date from the first letter, will be same for all the letters -->
                                        <xsl:when test="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:Duration &lt;7">
                                            <xsl:call-template name="displayDayDate">
                                                <xsl:with-param name="input">
                                                    <xsl:value-of select="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                                </xsl:with-param>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:call-template name="displayDayDate">
                                                <xsl:with-param name="input">
                                                    <xsl:value-of select="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                                </xsl:with-param>
                                            </xsl:call-template>
                                            <xsl:text> to </xsl:text>
                                            <xsl:call-template name="displayDayDate">
                                                <xsl:with-param name="input">
                                                    <xsl:value-of select="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:EndDate"/>
                                                </xsl:with-param>
                                            </xsl:call-template>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="displayDayDate">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:otherwise>
                            </xsl:choose>
                            <br/>
                            <xsl:text>Daily List Edition: </xsl:text>
                            <xsl:value-of select="/xhibit:DailyListLetters/xhibit:DailyListLetter[1]/xhibit:ListHeaderSummary/xhibit:Version"/>
                            <br/>
                            <br/>
                        </b>
                    </font>
                </center>
                <font size="3" style="font-family: Helvetica;">
                    <!-- Call template to display Cases By Solicitor Firm -->
                    <xsl:call-template name="CaseBySolicitor"/>
                    <!-- Call template to display Unrepresented Parties -->
                    <xsl:call-template name="UnrepresentedParties"/>
                    <!-- Call template to display Non CPS Prosecution Agencies -->
                    <xsl:call-template name="NonCPS"/>
                </font>
            </body>
        </html>
    </xsl:template>
    <!-- Template to display Cases By Solicitor Firm -->
    <xsl:template name="CaseBySolicitor">
        <xsl:choose>
            <xsl:when test="count(/xhibit:DailyListLetters/xhibit:DailyListLetter/xhibit:Recipient/xhibit:Solicitor) > 0">
                <b style="font-size:10pt">
                    <xsl:text>Cases By Solicitor Firm</xsl:text>
                </b>
                <br/>
                <br/>
                <!-- Display information for each recipient -->
                <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter/xhibit:Recipient/xhibit:Solicitor">
                    <!-- Translated to get around problem of space character in the names -->
                    <xsl:sort select="translate(xhibit:Party/xhibit:Organisation/xhibit:OrganisationName,' ','.')"/>
                    <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                        <tr valign="top">
                            <td width="53%">
                                <b>
                                    <xsl:choose>
                                        <xsl:when test="xhibit:Party/xhibit:Person">
                                            <xsl:value-of select="xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname"/>
                                        </xsl:when>
                                        <xsl:when test="xhibit:Party/xhibit:Organisation">
                                            <xsl:value-of select="xhibit:Party/xhibit:Organisation/xhibit:OrganisationName"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </b>
                            </td>
                            <td width="26%">
                                <b>
                                    <xsl:choose>
                                        <xsl:when test="xhibit:Party/xhibit:Person">
                                            <xsl:if test="xhibit:Party/xhibit:Person/xhibit:ContactDetails/xhibit:Telephone/xhibit:TelNationalNumber">
                                                <xsl:text>Tel: </xsl:text>
                                                <xsl:value-of select="xhibit:Party/xhibit:Person/xhibit:ContactDetails/xhibit:Telephone/xhibit:TelNationalNumber"/>
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:when test="xhibit:Party/xhibit:Organisation">
                                            <xsl:if test="xhibit:Party/xhibit:Organisation/xhibit:ContactDetails/xhibit:Telephone/xhibit:TelNationalNumber">
                                                <xsl:text>Tel: </xsl:text>
                                                <xsl:value-of select="xhibit:Party/xhibit:Organisation/xhibit:ContactDetails/xhibit:Telephone/xhibit:TelNationalNumber"/>
                                            </xsl:if>
                                        </xsl:when>
                                    </xsl:choose>
                                </b>
                            </td>
                            <!-- Extra space required -->
                            <td width="21%"/>
                        </tr>
                    </table>
                    <xsl:call-template name="DisplayHearingDetails_Solicitor">
                        <xsl:with-param name="PositionOfLetter">0</xsl:with-param>
                    </xsl:call-template>
                    <center>
                        <hr style="border-top: solid #000000;" width="300mm"/>
                    </center>
                    <br/>
                </xsl:for-each>
                <br/>
                <br/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
<!-- Template to display Unrepresented Parties -->
    <xsl:template name="UnrepresentedParties">
        <xsl:choose>
            <xsl:when test="count(/xhibit:DailyListLetters/xhibit:DailyListLetter/xhibit:Recipient/xhibit:Defendant) > 0">
                <b style="font-size:10pt">
                    <xsl:text>Unrepresented Parties By Courtroom</xsl:text>
                </b>
                <br/>
                <br/>
                <!-- Display information for each recipient -->
                <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter[xhibit:Recipient/xhibit:Defendant]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                    <!-- sort the output into court name, sitting priority and court room number -->
                    <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                    <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                    <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                    <xsl:sort select="xhibit:CaseNumber"/>
                    <xsl:call-template name="DisplayHearingDetails_Unrepresented">
                        <xsl:with-param name="PositionOfLetter">
                            <xsl:value-of select="position()"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:for-each>
                <!-- For each Hearing Summary -->
                <br/>
                <br/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
<!-- Template to display Non CPS Agencies -->
    <xsl:template name="NonCPS">
        <xsl:choose>
            <xsl:when test="count(/xhibit:DailyListLetters/xhibit:DailyListLetter/xhibit:Recipient/xhibit:Prosecution) > 0">
                <b style="font-size:10pt">
                    <xsl:text>Non-CPS Prosecution Agencies By Courtroom</xsl:text>
                </b>
                <br/>
                <br/>
                <!-- Display information for each recipient -->
                <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter[xhibit:Recipient/xhibit:Prosecution]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                    <!-- sort the output into court name, sitting priority and court room number -->
                    <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                    <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                    <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                    <xsl:sort select="xhibit:CaseNumber"/>
                    <xsl:call-template name="DisplayHearingDetails_NonCPS">
                        <xsl:with-param name="PositionOfLetter">
                            <xsl:value-of select="position()"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:for-each>
                <br/>
                <br/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
     <!-- Display the hearing information for solicitor letters-->
    <xsl:template name="DisplayHearingDetails_Solicitor">
        <xsl:param name="PositionOfLetter"/>
        <xsl:for-each select="../../xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
            <!-- Sort by Court Name, Sitting Priority and Court Room Number -->
            <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
            <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
            <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                <xsl:choose>
                    <!-- Handle Solicitor letters -->
                    <xsl:when test="$PositionOfLetter='0'">
                        <xsl:choose>
                            <xsl:when test="position()=1">
                                <tr valign="top">
                                    <td width="53%">
                                        <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                                    </td>
                                    <td width="26%">
                                        <!-- Handle floating cases -->
                                        <xsl:choose>
                                            <!-- Display default text if floating case, otherwise display the court room number -->
                                            <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                                <xsl:text>- Floater</xsl:text>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:text>- Court </xsl:text>
                                                <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </td>
                                    <!-- Extra space required -->
                                    <td width="21%"/>
                                </tr>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:variable name="thisCourt">
                                    <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                                </xsl:variable>
                                <xsl:variable name="thisCourtRoomNumber">
                                    <xsl:choose>
                                        <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                            <xsl:text>- Floater</xsl:text>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>- Court </xsl:text>
                                            <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <xsl:variable name="newPos">
                                    <xsl:value-of select="position()-1"/>
                                </xsl:variable>
                                <xsl:variable name="previousCourt">
                                    <xsl:for-each select="../../../../../../xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                                        <!-- Re sort the info as previously -->
                                        <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                                        <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                                        <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                                        <xsl:if test="position()=$newPos">
                                            <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:variable>
                                <xsl:variable name="previousCourtRoomNumber">
                                    <xsl:for-each select="../../../../../../xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                                        <!-- Re sort the info as previously -->
                                        <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                                        <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                                        <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                                        <xsl:if test="position()=$newPos">
                                            <xsl:choose>
                                                <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                                    <xsl:text>- Floater</xsl:text>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:text>- Court </xsl:text>
                                                    <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:variable>
                                <xsl:if test="($thisCourt != $previousCourt) or ($thisCourtRoomNumber != $previousCourtRoomNumber)">
                                    <!-- only display if different -->
                                    <tr valign="top">
                                        <td width="53%">
                                            <xsl:value-of select="$thisCourt"/>
                                        </td>
                                        <td width="26%">
                                            <xsl:value-of select="$thisCourtRoomNumber"/>
                                        </td>
                                        <!-- Extra space required -->
                                        <td width="21%"/>
                                    </tr>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                </xsl:choose>
            </table>
            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                <tr valign="top">
                    <td width="79%">
                        <!-- Business Rule If timemarking note display this, otherwise if sitting at display this -->
                        <xsl:choose>
                            <xsl:when test="xhibit:TimeMarkingNote != ' '">
                                <xsl:value-of select="xhibit:TimeMarkingNote"/>
                                <xsl:text>: </xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="../../xhibit:SittingAt">
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(../../xhibit:SittingAt,1,5)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                    <xsl:text>: </xsl:text>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:value-of select="xhibit:HearingDetails/xhibit:HearingDescription"/>
                    </td>
                    <!-- Extra space required -->
                    <td width="21%"/>
                </tr>
            </table>
            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                <tr valign="top">
                    <!-- Defendant Details -->
                    <td width="100%">
                        <xsl:for-each select="xhibit:DefendantSummaries/xhibit:DefendantSummary">
                            <xsl:variable name="ptiurn">
                                <xsl:if test="xhibit:PTIURN">
                                    <xsl:value-of select="xhibit:PTIURN"/>
                                </xsl:if>                
                            </xsl:variable> 
				
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt" >
                                <tr valign="top">
                                    <td width="19%">
                                        <xsl:if test="position()=1">
                                            <xsl:value-of select="../../xhibit:CaseNumber"/>
                                        </xsl:if>
                                    </td>
                                    <td width="39%">
                                        <xsl:value-of select="xhibit:CitizenNameSurname"/>
                                        <xsl:if test="xhibit:CitizenNameForename[1]">
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="xhibit:CitizenNameForename[1]"/>
                                        </xsl:if>
                                        <xsl:if test="xhibit:CitizenNameForename[2]">
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="toUpper">
                                                <xsl:with-param name="content" select="substring(xhibit:CitizenNameForename[2],1,1)"/>
                                            </xsl:call-template>
                                        </xsl:if>
                                    </td>
                                    <td width="19%">
										<xsl:if test="normalize-space($ptiurn) = ''">
											<xsl:if test="xhibit:SolicitorRef">
												<xsl:text>Ref: </xsl:text>
												<xsl:value-of select="xhibit:SolicitorRef"/>
											</xsl:if>
										</xsl:if>
                                    </td>
                                    <td width="23%">
										<xsl:choose>
											<xsl:when test="$ptiurn">
												<xsl:if test="not(normalize-space($ptiurn) = '')">
													 <xsl:text>CPS Ref: </xsl:text>
													 <xsl:value-of select="$ptiurn"/>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:if test="position()=1">
													<xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
												</xsl:if>
											</xsl:otherwise>
										</xsl:choose>
                                    </td>
                                </tr>
                            </table>
                        </xsl:for-each>
                    </td>
                </tr>
            </table>
            <br/>
            <br/>
        </xsl:for-each>
    </xsl:template>
   
    <!-- Display the hearing information for defendant letters -->
    <xsl:template name="DisplayHearingDetails_Unrepresented">
        <xsl:param name="PositionOfLetter"/>
        <xsl:variable name="thisCaseNumber">
            <xsl:value-of select="xhibit:CaseNumber"/>
        </xsl:variable>
        <xsl:variable name="previousCaseNumber">
            <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter    [xhibit:Recipient/xhibit:Defendant]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                <!-- sort the output into court name, sitting priority and court room number, casenumber -->
                <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                <xsl:sort select="xhibit:CaseNumber"/>
                <xsl:if test="position() = $PositionOfLetter - 1">
                    <xsl:value-of select="xhibit:CaseNumber"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:if test="($thisCaseNumber != $previousCaseNumber) and $PositionOfLetter != 1">
            <!-- Display a line if different case number -->
            <br/>
            <center>
                <hr style="border-top: solid #000000;" width="300mm"/>
            </center>
            <br/>
        </xsl:if>
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
            <xsl:choose>
                <xsl:when test="$PositionOfLetter='1'">
                    <tr valign="top">
                        <td width="53%">
                            <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <!-- Insert an extra space-->
                            <br/>
                            <br/>
                        </td>
                        <td width="26%">
                            <!-- Handle floating cases -->
                            <xsl:choose>
                                <!-- Display default text if floating case, otherwise display the court room number -->
                                <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                    <xsl:text>- Floater</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>- Court </xsl:text>
                                    <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        <!-- Extra space required -->
                        <td width="21%"/>
                    </tr>
                </xsl:when>
                <xsl:otherwise>
                    <!-- Check to ensure that different from previous before displaying -->
                    <xsl:variable name="thisCourt">
                        <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                    </xsl:variable>
                    <xsl:variable name="thisCourtRoomNumber">
                        <xsl:choose>
                            <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                <xsl:text>- Floater</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>- Court </xsl:text>
                                <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="previousCourt">
                        <!-- only need to handle prosecution and defendant letters because for all the solicitor letters need to display the court and court room number -->
                        <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter[xhibit:Recipient/xhibit:Defendant]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                            <!-- sort the output into court name, sitting priority and court room number, casenumber -->
                            <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                            <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                            <xsl:sort select="xhibit:CaseNumber"/>
                            <xsl:if test="position() = $PositionOfLetter - 1">
                                <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:variable name="previousCourtRoomNumber">
                        <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter    [xhibit:Recipient/xhibit:Defendant]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                            <!-- sort the output into court name, sitting priority and court room number, casenumber -->
                            <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                            <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                            <xsl:sort select="xhibit:CaseNumber"/>
                            <xsl:if test="position() = $PositionOfLetter - 1">
                                <xsl:choose>
                                    <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                        <xsl:text>- Floater</xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>- Court </xsl:text>
                                        <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:if test="($thisCourt != $previousCourt) or ($thisCourtRoomNumber != $previousCourtRoomNumber)">
                        <!-- only display if different -->
                        <tr valign="top">
                            <td width="53%">
                                <xsl:value-of select="$thisCourt"/>
                                <!-- Insert an extra space-->
                                <br/>
                                <br/>
                            </td>
                            <td width="26%">
                                <!-- Handle floating cases -->
                                <xsl:value-of select="$thisCourtRoomNumber"/>
                            </td>
                            <!-- Extra space required -->
                            <td width="21%"/>
                        </tr>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </table>
        <xsl:if test="$thisCaseNumber != $previousCaseNumber">
            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                <tr valign="top">
                    <td width="79%">
                        <!-- Business Rule If timemarking note display this, otherwise if sitting at display this -->
                        <xsl:choose>
                            <xsl:when test="xhibit:TimeMarkingNote != ' '">
                                <xsl:value-of select="xhibit:TimeMarkingNote"/>
                                <xsl:text>: </xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="../../xhibit:SittingAt">
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(../../xhibit:SittingAt,1,5)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                    <xsl:text>: </xsl:text>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:value-of select="xhibit:HearingDetails/xhibit:HearingDescription"/>
                    </td>
                    <!-- Extra space required -->
                    <td width="21%"/>
                </tr>
            </table>
        </xsl:if>
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
            <tr valign="top">
                <!-- Defendant Details -->
                <td width="100%">
                    <xsl:for-each select="xhibit:DefendantSummaries/xhibit:DefendantSummary">
                        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                            <xsl:variable name="ptiurn">
                                <xsl:if test="xhibit:PTIURN">
                                    <xsl:value-of select="xhibit:PTIURN"/>
                                </xsl:if>                
                            </xsl:variable> 

                            <tr valign="top">
                                <td width="19%">
                                    <xsl:if test="position()=1">
                                        <xsl:if test="$thisCaseNumber != $previousCaseNumber">
                                            <xsl:value-of select="../../xhibit:CaseNumber"/>
                                        </xsl:if>
                                    </xsl:if>
                                </td>
                                
                                <td width="39%">
                                    <xsl:value-of select="xhibit:CitizenNameSurname"/>
                                    <xsl:if test="xhibit:CitizenNameForename[1]">
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="xhibit:CitizenNameForename[1]"/>
                                    </xsl:if>
                                    <xsl:if test="xhibit:CitizenNameForename[2]">
                                        <xsl:text> </xsl:text>
                                        <xsl:call-template name="toUpper">
                                            <xsl:with-param name="content" select="substring(xhibit:CitizenNameForename[2],1,1)"/>
                                        </xsl:call-template>
                                    </xsl:if>
                                </td>
                                
                                <td width="19%">
                                    <!-- Display Solictor Refence -->
                                    <!-- Display nothing -->
                                </td>
                                
                                <td width="23%">
									<xsl:choose>
										<xsl:when test="$ptiurn">
											<!-- Display the CPS ref for every defendant -->
											<xsl:if test="not(normalize-space($ptiurn) = '')">
												 <xsl:text>CPS Ref: </xsl:text>
												 <xsl:value-of select="$ptiurn"/>
											</xsl:if>
											<xsl:if test="normalize-space($ptiurn) = ''">
												 <xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<!-- Display the Organisation Name for the first row -->
											<xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
										</xsl:otherwise>
									</xsl:choose>
                                </td>    
                                                           
                            </tr>
                        </table>
                    </xsl:for-each>
                </td>
            </tr>
        </table>
    </xsl:template>
    
    <!-- Display the hearing information for prosecution letters -->
    <xsl:template name="DisplayHearingDetails_NonCPS">
        <xsl:param name="PositionOfLetter"/>
        <xsl:variable name="thisCaseNumber">
            <xsl:value-of select="xhibit:CaseNumber"/>
        </xsl:variable>
        <xsl:variable name="previousCaseNumber">
            <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter    [xhibit:Recipient/xhibit:Prosecution]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                <!-- sort the output into court name, sitting priority and court room number, casenumber -->
                <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                <xsl:sort select="xhibit:CaseNumber"/>
                <xsl:if test="position() = $PositionOfLetter - 1">
                    <xsl:value-of select="xhibit:CaseNumber"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:if test="($thisCaseNumber != $previousCaseNumber) and $PositionOfLetter != 1">
            <!-- Display a line if different case number -->
            <br/>
            <center>
                <hr style="border-top: solid #000000;" width="300mm"/>
            </center>
            <br/>
        </xsl:if>
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
            <xsl:choose>
                <xsl:when test="$PositionOfLetter='1'">
                    <tr valign="top">
                        <td width="53%">
                            <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <!-- Insert an extra space-->
                            <br/>
                            <br/>
                        </td>
                        <td width="26%">
                            <!-- Handle floating cases -->
                            <xsl:choose>
                                <!-- Display default text if floating case, otherwise display the court room number -->
                                <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                    <xsl:text>- Floater</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>- Court </xsl:text>
                                    <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        <!-- Extra space required -->
                        <td width="21%"/>
                    </tr>
                </xsl:when>
                <xsl:otherwise>
                    <!-- Check to ensure that different from previous before displaying -->
                    <xsl:variable name="thisCourt">
                        <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                    </xsl:variable>
                    <xsl:variable name="thisCourtRoomNumber">
                        <xsl:choose>
                            <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                <xsl:text>- Floater</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>- Court </xsl:text>
                                <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="previousCourt">
                        <!-- only need to handle prosecution and defendant letters because for all the solicitor letters need to display the court and court room number -->
                        <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter[xhibit:Recipient/xhibit:Prosecution]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                            <!-- sort the output into court name, sitting priority and court room number, casenumber -->
                            <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                            <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                            <xsl:sort select="xhibit:CaseNumber"/>
                            <xsl:if test="position() = $PositionOfLetter - 1">
                                <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:variable name="previousCourtRoomNumber">
                        <xsl:for-each select="/xhibit:DailyListLetters/xhibit:DailyListLetter[xhibit:Recipient/xhibit:Prosecution]/xhibit:CourtListSummaries/xhibit:CourtListSummary/xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:HearingSummaries/xhibit:HearingSummary">
                            <!-- sort the output into court name, sitting priority and court room number, casenumber -->
                            <xsl:sort select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <xsl:sort select="../../xhibit:SittingPriority" order="descending"/>
                            <xsl:sort select="../../xhibit:CourtRoomNumber" data-type="number"/>
                            <xsl:sort select="xhibit:CaseNumber"/>
                            <xsl:if test="position() = $PositionOfLetter - 1">
                                <xsl:choose>
                                    <xsl:when test="../../xhibit:SittingPriority = 'F'">
                                        <xsl:text>- Floater</xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>- Court </xsl:text>
                                        <xsl:value-of select="../../xhibit:CourtRoomNumber"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:if test="($thisCourt != $previousCourt) or ($thisCourtRoomNumber != $previousCourtRoomNumber)">
                        <!-- only display if different -->
                        <tr valign="top">
                            <td width="53%">
                                <xsl:value-of select="$thisCourt"/>
                                <!-- Insert an extra space-->
                                <br/>
                                <br/>
                            </td>
                            <td width="26%">
                                <!-- Handle floating cases -->
                                <xsl:value-of select="$thisCourtRoomNumber"/>
                            </td>
                            <!-- Extra space required -->
                            <td width="21%"/>
                        </tr>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </table>
        <xsl:if test="$thisCaseNumber != $previousCaseNumber">
            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                <tr valign="top">
                    <td width="79%">
                        <!-- Business Rule If timemarking note display this, otherwise if sitting at display this -->
                        <xsl:choose>
                            <xsl:when test="xhibit:TimeMarkingNote != ' '">
                                <xsl:value-of select="xhibit:TimeMarkingNote"/>
                                <xsl:text>: </xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="../../xhibit:SittingAt">
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(../../xhibit:SittingAt,1,5)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                    <xsl:text>: </xsl:text>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:value-of select="xhibit:HearingDetails/xhibit:HearingDescription"/>
                    </td>
                    <!-- Extra space required -->
                    <td width="21%"/>
                </tr>
            </table>
        </xsl:if>
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
            <tr valign="top">
                <!-- Defendant Details -->
                <td width="77%">
                    <xsl:for-each select="xhibit:DefendantSummaries/xhibit:DefendantSummary">
                        <xsl:variable name="ptiurn">
                            <xsl:if test="xhibit:PTIURN">
                                <xsl:value-of select="xhibit:PTIURN"/>
                            </xsl:if>                
                        </xsl:variable>     
                                        
                        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                            <tr valign="top">
                                <td width="19%">
                                    <xsl:if test="position()=1">
                                        <xsl:if test="$thisCaseNumber != $previousCaseNumber">
                                            <xsl:value-of select="../../xhibit:CaseNumber"/>
                                        </xsl:if>
                                    </xsl:if>
                                </td>
                                <td width="39%">
                                    <!-- If the letter to Prosecution display just the defendant surnames in brackets-->
                                    <xsl:text>[</xsl:text>
                                    <xsl:value-of select="xhibit:CitizenNameSurname"/>
                                    <xsl:text>]</xsl:text>
                                </td>
                                <td width="19%">
                                    <!-- Display Solictor Refence -->
                                    <!-- only display once as will be the same for each defendant -->
                                    <xsl:if test="position()=1">
                                        <xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:SolicitorRef"/>
                                    </xsl:if>
                                </td>
                                <td width="23%">
									<!-- Display the CPS ref for every defendant -->
									<xsl:if test="$ptiurn">
										<xsl:if test="not(normalize-space($ptiurn) = '')">
											 <xsl:text>CPS Ref: </xsl:text>
											 <xsl:value-of select="$ptiurn"/>
										</xsl:if>
										<xsl:if test="normalize-space($ptiurn) = ''">
											 <xsl:value-of select="../../../../../../xhibit:Recipient/xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationName"/>
										</xsl:if>
									</xsl:if>
									<xsl:if test="not($ptiurn)">
										<xsl:value-of select="../../../../../../xhibit:Recipient/xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationName"/>
									</xsl:if>
									
                                </td>                                
                            </tr>
                        </table>
                    </xsl:for-each>
                </td>
            </tr>
        </table>
        <br/>
        <!-- Display the prosecution details if the letter to prosecution -->
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
			<tr>
				<td>&nbsp;
				</td>
			</tr>
            <tr valign="top">
                <td width="53%">
                    <xsl:choose>
                        <xsl:when test="substring(xhibit:CaseNumber,1,1) = 'A'">
                            <xsl:text>Resp: </xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>Pros: </xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                    <b>
                        <xsl:value-of select="../../../../../../xhibit:Recipient/xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationName"/>
                    </b>
                </td>
                <td width="24%">
                    <b>
                        <xsl:if test="../../../../../../xhibit:Recipient/xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:ContactDetails/xhibit:Telephone/xhibit:TelNationalNumber">
                            <xsl:text>Tel: </xsl:text>
                            <xsl:value-of select="../../../../../../xhibit:Recipient/xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:ContactDetails/xhibit:Telephone/xhibit:TelNationalNumber"/>
                        </xsl:if>
                    </b>
                </td>
                <td width="23%">
                    <xsl:if test="xhibit:ProsecutionSummary/xhibit:ProsecutingReference">
                        <xsl:text>Ref: </xsl:text>
                        <xsl:value-of select="xhibit:ProsecutionSummary/xhibit:ProsecutingReference"/>
                    </xsl:if>
                </td>
            </tr>
        </table>
    </xsl:template>
    
<!-- Common template to display date including the day -->
    <xsl:template name="displayDayDate">
        <xsl:param name="input"/>
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
                <xsl:text>Sunday</xsl:text>
            </xsl:when>
            <xsl:when test="$dayOfWeek=1">
                <xsl:text>Monday</xsl:text>
            </xsl:when>
            <xsl:when test="$dayOfWeek=2">
                <xsl:text>Tuesday</xsl:text>            
            </xsl:when>
            <xsl:when test="$dayOfWeek=3">
                <xsl:text>Wednesday</xsl:text>          
            </xsl:when>
            <xsl:when test="$dayOfWeek=4">
                <xsl:text>Thursday</xsl:text>           
            </xsl:when>
            <xsl:when test="$dayOfWeek=5">
                <xsl:text>Friday</xsl:text>         
            </xsl:when>
            <xsl:when test="$dayOfWeek=6">
                <xsl:text>Saturday</xsl:text>           
            </xsl:when>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:call-template name="displayDate">
            <xsl:with-param name="input" select="$input"/>
        </xsl:call-template>
    </xsl:template>
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
    
    <!-- Template to display the date in a specific format -->
    <xsl:template name="displayDate">
        <xsl:param name="input"/>
        <xsl:variable name="month" select="substring($input,6,2)"/>
        <xsl:variable name="day" select="substring($input,9,2)"/>
        <xsl:variable name="year" select="substring($input,1,4)"/>
        <xsl:value-of select="$day"/>
        <xsl:text> </xsl:text>
        <xsl:choose>
            <xsl:when test="$month='01'">
                <xsl:text>January</xsl:text>
            </xsl:when>
            <xsl:when test="$month='02'">
                <xsl:text>February</xsl:text>
            </xsl:when>
            <xsl:when test="$month='03'">
                <xsl:text>March</xsl:text>
            </xsl:when>
            <xsl:when test="$month='04'">
                <xsl:text>April</xsl:text>
            </xsl:when>
            <xsl:when test="$month='05'">
                <xsl:text>May</xsl:text>
            </xsl:when>
            <xsl:when test="$month='06'">
                <xsl:text>June</xsl:text>
            </xsl:when>
            <xsl:when test="$month='07'">
                <xsl:text>July</xsl:text>
            </xsl:when>
            <xsl:when test="$month='08'">
                <xsl:text>August</xsl:text>
            </xsl:when>
            <xsl:when test="$month='09'">
                <xsl:text>September</xsl:text>
            </xsl:when>
            <xsl:when test="$month='10'">
                <xsl:text>October</xsl:text>
            </xsl:when>
            <xsl:when test="$month='11'">
                <xsl:text>November</xsl:text>
            </xsl:when>
            <xsl:when test="$month='12'">
                <xsl:text>December</xsl:text>
            </xsl:when>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$year"/>
    </xsl:template>
    <!-- Display the formatted time and then post fix with AM or PM -->
    <xsl:template name="FormatTime">
        <xsl:param name="input"/>
        <xsl:choose>
            <!-- if less than 12 postfix AM -->
            <xsl:when test="substring-before($input,':') &lt; 12">
                <xsl:value-of select="$input"/>
                <xsl:text> AM</xsl:text>
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
                        <!-- display the value of the formatted hours -->
                        <xsl:value-of select="$fmtHrs"/>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- display the rest of the time -->
                <xsl:text>:</xsl:text>
                <xsl:value-of select="substring-after($input,':')"/>
                <xsl:text> PM</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Common template to convert text to upper case -->
    <xsl:template name="toUpper">
        <xsl:param name="content"/>
        <xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    </xsl:template>
</xsl:stylesheet>