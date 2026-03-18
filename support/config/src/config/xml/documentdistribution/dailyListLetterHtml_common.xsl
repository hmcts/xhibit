<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" version="1.0">
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>

    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>

    <xsl:template name="DisplayDailyListLetter">
        <xsl:param name="language"/>
                <!-- Display the HEADER information -->
                <font size="5" style="font-family: Helvetica;">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'The Crown Court'"/>
                    </xsl:call-template>
            <br/>
                </font>
                <font size="3" style="font-family: Helvetica;">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'at'"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:variable name="courthousename">
                    <xsl:call-template name="TitleCase">
                        <xsl:with-param name="text" select="xhibit:CrownCourt/xhibit:CourtHouseName"/>
                   </xsl:call-template>
                 </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$courthousename"/>
                </xsl:call-template>
                </font>
                <br/>
                <br/>
                <hr style="border-top: solid #000000;"/>
                <br/>
                <!-- MAIN BODY -->
                <!-- Display the recipient details -->
                <xsl:apply-templates select="xhibit:Recipient">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <div align="center" style="font-family: Courier New, Courier">
                    <div style="font-size: 10pt">
                        <b>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Daily List for'"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:choose>
                                <xsl:when test="xhibit:ListHeaderSummary/xhibit:Duration">
                                    <xsl:choose>
                                        <xsl:when test="xhibit:ListHeaderSummary/xhibit:Duration &lt;7">
                                            <xsl:call-template name="displayDayDate">
                                                <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:call-template name="displayDayDate">
                                                <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'to'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="displayDayDate">
                                                <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:EndDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="displayDayDate">
                                        <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:otherwise>
                            </xsl:choose>
                        </b>
                    </div>
                </div>
                <hr style="border-top: solid #000000;"/>
                <!-- Call template to display case and defendant details -->
                <xsl:apply-templates select="xhibit:CourtListSummaries">
                    <xsl:with-param name="language" select="$language"/>
                </xsl:apply-templates>
                <!-- Display the FOOTER information -->
                <br/>
                <br/>
                <br/>
                <br/>
                <hr style="border-top: solid #000000;"/>
                <div style="font-family: Arial">
                    <div style="font-size: 8pt">
                        <xsl:for-each select="xhibit:CrownCourt/xhibit:CourtHouseAddress/xhibit:Line">
                            <xsl:if test=". != '-'">
                                <xsl:variable name="addLine">
                            <xsl:call-template name="TitleCase">
                                <xsl:with-param name="text" select="."/>
                            </xsl:call-template>
                                </xsl:variable>
                                <xsl:variable name="addLine2">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="$addLine"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:call-template name="toUpper">
                                    <xsl:with-param name="content" select="$addLine2"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                            </xsl:if>
                        </xsl:for-each>
                        <xsl:value-of select="xhibit:CrownCourt/xhibit:CourtHouseAddress/xhibit:PostCode"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="xhibit:CrownCourt/xhibit:CourtHouseDX"/>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Tel'"/>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                        <xsl:value-of select="xhibit:CrownCourt/xhibit:CourtHouseTelephone"/>
                    </div>
                </div>
                <xsl:if test="$language != $DefaultLang">
                    <div style="font-family: Arial">
                        <div style="font-size: 8pt">
                            <xsl:if test="$language != $DefaultLang">
                                <xsl:call-template name="NOT_FOUND_FOOTER">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>                                        
                            </xsl:if>
                        </div>
                    </div>
                </xsl:if>
    </xsl:template>
    <xsl:template match="xhibit:CourtListSummaries">
        <xsl:param name="language"/>
        <!-- Display info for each court list -->
        <xsl:for-each select="xhibit:CourtListSummary">
            <br/>
            <b>
                <xsl:variable name="courthousename">
                    <xsl:call-template name="TitleCase">
                        <xsl:with-param name="text" select="xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="crtName">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="$courthousename"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="toUpper">
                    <xsl:with-param name="content" select="$crtName"/>
                </xsl:call-template>
            </b>
            <br/>
            <xsl:apply-templates select="xhibit:SittingSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
        </xsl:for-each>
    </xsl:template>
    <!-- Display Sitting Info -->
    <xsl:template match="xhibit:SittingSummaries">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:SittingSummary">
            <!-- Sort the sitting by sitting priority to ensure floating at end and then by court room order -->
            <xsl:sort select="xhibit:SittingPriority" order="descending"/>
            <xsl:sort select="xhibit:CourtRoomNumber" data-type="number"/>
            <br/>
            <!-- Display Hearing and Case Information  -->
            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                <tr valign="top">
                    <td width="60%">
                        <xsl:choose>
                            <xsl:when test="xhibit:SittingPriority = 'T'">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Court'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="xhibit:CourtRoomNumber"/>
                                <xsl:if test="xhibit:SittingAt">
                                    <xsl:text> - </xsl:text>
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'sitting at'"/>
                                    </xsl:call-template>
                                    <xsl:text> </xsl:text>
                                    <!-- Call template to format the time -->
                                    <xsl:choose>
                                <!-- Handle SittingAt data 10:00:00 -->
                                <xsl:when test="string-length(xhibit:SittingAt)=8">
                                    <!-- call template to postfix AM or PM -->
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(xhibit:SittingAt,1,5)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:when>
                                <!-- Handle SittingAt data 9:00:00 -->
                                <xsl:when test="string-length(xhibit:SittingAt)=7">
                                    <!-- call template to postfix AM or PM -->
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(xhibit:SittingAt,1,4)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:when>
                                    </xsl:choose>
                                </xsl:if>
                            </xsl:when>
                            <xsl:when test="xhibit:SittingPriority = 'F'">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'The following may be taken in any court'"/>
                                </xsl:call-template>
                                <xsl:text>.</xsl:text>
                            </xsl:when>
                        </xsl:choose>
                    </td>
                    <td width="40%">
                        <b>
                            <!-- Check to ensure that the case is not floating, only display for non floating cases -->
                            <xsl:if test="xhibit:SittingPriority = 'T'">
                                <xsl:if test="xhibit:Judiciary/xhibit:Judge/xhibit:CitizenNameRequestedName != 'N/A'">
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="xhibit:Judiciary/xhibit:Judge/xhibit:CitizenNameRequestedName"/>
                                    </xsl:call-template>
                                </xsl:if>
                            </xsl:if>
                        </b>
                    </td>
                </tr>
            </table>
            <br/>
            <xsl:if test="xhibit:SittingNote">
                <b style="font-size:10pt">
                    <xsl:value-of select="xhibit:SittingNote"/>
                </b>
                <br/>
            </xsl:if>
            <xsl:apply-templates select="xhibit:HearingSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
        </xsl:for-each>
    </xsl:template>
    <!-- Display the recipient details -->
    <xsl:template match="xhibit:Recipient">
        <xsl:param name="language"/>
        <xsl:if test="xhibit:Solicitor">
            <!-- Can be a Solicitor Person -->
            <xsl:if test="xhibit:Solicitor/xhibit:Party/xhibit:Person">
                <div style="font-family: Courier">
                    <div style="font-size: 10pt">
                        <xsl:call-template name="TitleCase">
                            <xsl:with-param name="text">
                                <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename"/>
                            </xsl:with-param>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="TitleCase">
                            <xsl:with-param name="text">
                                <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname"/>
                            </xsl:with-param>
                        </xsl:call-template>
                        <br/>
                        <xsl:for-each select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Address/xhibit:Line">
                            <xsl:if test=". != '-'">
                                <xsl:value-of select="."/>
                                <br/>
                            </xsl:if>
                        </xsl:for-each>
                        <xsl:choose>
                            <xsl:when test="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Address/xhibit:PostCode">
                                <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Address/xhibit:PostCode"/>    
                            </xsl:when>
                            <xsl:otherwise>
                                <br/>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:for-each select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Address/xhibit:Line">
                            <xsl:if test=". = '-'">
                                <br/>
                            </xsl:if>
                        </xsl:for-each>
                    </div>
                </div>
                <div style="font-size: 10pt">
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                </div>
                <div align="right" style="font-family: Courier New, Courier">
                    <xsl:call-template name="DisplayStartDate">
                        <xsl:with-param name="date" select="$java-date"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                    <br/>
                </div>
            </xsl:if>
            <!-- Can be a Solicitor Organisation -->
            <xsl:if test="xhibit:Solicitor/xhibit:Party/xhibit:Organisation">
                <div style="font-family: Courier">
                    <div style="font-size: 10pt">
                        <xsl:value-of select="xhibit:Solicitor/xhibit:FirmName"/>
                        <br/>
                        <xsl:choose>
                            <!-- BUSINESS RULE: When there is a Issuing Court DX and Solicitor DX display the Solicitor DX otherwise normal address -->
                            <xsl:when test="string-length(../xhibit:CrownCourt/xhibit:CourtHouseDX) > 0 and string-length(xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationDX) > 0">
                                <b>
                                    <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationDX"/>
                                    <br/>
                                    <br/>
                                    <br/>
                                    <br/>
                                    <br/>
                                    <br/>
                                </b>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:for-each select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationAddress/xhibit:Line">
                                    <xsl:if test=". != '-'">
                                        <xsl:value-of select="."/>
                                        <br/>
                                    </xsl:if>
                                </xsl:for-each>
                                <xsl:choose>
                                    <xsl:when test="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationAddress/xhibit:PostCode">
                                        <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationAddress/xhibit:PostCode"/>
                                        <br/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <br/>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <xsl:for-each select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationAddress/xhibit:Line">
                                    <xsl:if test=". = '-'">
                                        <br/>
                                    </xsl:if>
                                </xsl:for-each>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </div>
                <div style="font-size: 10pt">
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                </div>
                <div align="right" style="font-family: Courier New, Courier">
                    <xsl:call-template name="DisplayStartDate">
                        <xsl:with-param name="date" select="$java-date"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                    <br/>
                </div>
            </xsl:if>
        </xsl:if>
        <!-- Test if Prosecution -->
        <xsl:if test="xhibit:Prosecution">
            <div style="font-family: Courier">
                <div style="font-size: 10pt">
                    <xsl:variable name="prosOrg">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationName"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:call-template name="toUpper">
                        <xsl:with-param name="content" select="$prosOrg"/>
                    </xsl:call-template>
                    <br/>
                    <xsl:choose>
                        <!-- BUSINESS RULE: When there is a Issuing Court DX and Prosecuting DX display the Prosecuting DX otherwise normal address -->
                        <xsl:when test="string-length(../xhibit:CrownCourt/xhibit:CourtHouseDX) > 0 and string-length(xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationDX) > 0">
                            <b>
                                <xsl:value-of select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationDX"/>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                            </b>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationAddress/xhibit:Line">
                                <xsl:if test=". != '-'">
                                    <xsl:value-of select="."/>
                                    <br/>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:choose>
                                <xsl:when test="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationAddress/xhibit:PostCode">
                                    <xsl:value-of select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationAddress/xhibit:PostCode"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <br/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:for-each select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationAddress/xhibit:Line">
                                <xsl:if test=". = '-'">
                                    <br/>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </div>
            <div style="font-size: 10pt">
                <br/>
                <br/>
                <br/>
                <br/>
            </div>
            <div align="right" style="font-family: Courier New, Courier">
                    <xsl:call-template name="DisplayStartDate">
                        <xsl:with-param name="date" select="$java-date"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                <br/>
            </div>
        </xsl:if>
        <!-- Test If Defendant -->
        <xsl:if test="xhibit:Defendant">
            <div style="font-family: Courier">
                <div style="font-size: 10pt">
                    <!-- BUSINESS RULE: If the defendant is in custody or on remand display the prisoner and prison details -->
                    <xsl:choose>
                        <xsl:when test="xhibit:Defendant/xhibit:CustodyStatus = 'In custody' or xhibit:Defendant/xhibit:CustodyStatus = 'On remand'">
                            <xsl:call-template name="TitleCase">
                                <xsl:with-param name="text">
                                    <xsl:value-of select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename[1]"/>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:if test="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename[2]">
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="toUpper">
                                    <xsl:with-param name="content" select="substring(xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename[2],1,1)"/>
                                </xsl:call-template>
                            </xsl:if>
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="TitleCase">
                                <xsl:with-param name="text">
                                    <xsl:value-of select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname"/>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="xhibit:Defendant/xhibit:PrisonerID"/>
                            <br/>
                            <b>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'c/o The Governor'"/>
                                </xsl:call-template>                                        
                                <br/>
                                <xsl:if test="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID">
                                    <xsl:value-of select="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID"/>
                                    <xsl:text> </xsl:text>
                        <xsl:variable name="prisonLocation">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="xhibit:Defendant/xhibit:PrisonLocation/xhibit:Location"/>
                        </xsl:call-template>
                        </xsl:variable>
                        <xsl:call-template name="toUpper">
                        <xsl:with-param name="content" select="$prisonLocation"/>
                        </xsl:call-template>
                                </xsl:if>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                            </b>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="TitleCase">
                                <xsl:with-param name="text">
                                    <xsl:value-of select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename[1]"/>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:if test="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename[2]">
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="toUpper">
                                    <xsl:with-param name="content" select="substring(xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename[2],1,1)"/>
                                </xsl:call-template>
                            </xsl:if>
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="TitleCase">
                                <xsl:with-param name="text">
                                    <xsl:value-of select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname"/>
                                </xsl:with-param>
                            </xsl:call-template>
                            <br/>
                            <xsl:for-each select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Address/xhibit:Line">
                                <xsl:if test=". != '-'">
                                    <xsl:value-of select="."/>
                                    <br/>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:choose>
                                <xsl:when test="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Address/xhibit:PostCode">
                                    <xsl:value-of select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Address/xhibit:PostCode"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <br/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:for-each select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Address/xhibit:Line">
                                <xsl:if test=". = '-'">
                                    <br/>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </div>
            <div style="font-size: 10pt">
                <br/>
                <br/>
                <br/>
                <br/>
            </div>
            <div align="right" style="font-family: Courier New, Courier">
                    <xsl:call-template name="DisplayStartDate">
                        <xsl:with-param name="date" select="$java-date"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                <br/>
            </div>
        </xsl:if>
        <br/>
    </xsl:template>
    <!-- Template to display the hearing information -->
    <xsl:template match="xhibit:HearingSummaries">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:HearingSummary">
            <xsl:if test="xhibit:TimeMarkingNote">
                <b style="font-size:10pt">
                    <xsl:choose>
                        <xsl:when test="starts-with(xhibit:TimeMarkingNote,'SITTING AT')">
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
                            <xsl:value-of select="substring-after(xhibit:TimeMarkingNote,'SITTING AT')"/>
                        </xsl:when>
                        <xsl:when test="starts-with(xhibit:TimeMarkingNote,'NOT BEFORE')">
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'NOT BEFORE'"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="substring-after(xhibit:TimeMarkingNote,'NOT BEFORE')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="xhibit:TimeMarkingNote"/>
                        </xsl:otherwise>                        
                    </xsl:choose>
                </b>
                <br/>
            </xsl:if>
            <br/>
            <b style="font-size:10pt">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="xhibit:HearingDetails/xhibit:HearingDescription"/>
                </xsl:call-template>
            </b>
            <xsl:apply-templates select="xhibit:DefendantSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
            <br/>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display the case and defendant information -->
    <xsl:template match="xhibit:DefendantSummaries">
        <xsl:param name="language"/>
        <xsl:variable name="LetterType">
            <xsl:choose>
                <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Solicitor">Solicitor</xsl:when>
                <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Prosecution">Prosecution</xsl:when>
                <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Defendant">Defendant</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
            <tr valign="top">
                <td width="20%">
                    <xsl:value-of select="../xhibit:CaseNumber"/>
                </td>
                <td width="80%">
                    <xsl:for-each select="xhibit:DefendantSummary">
                        <xsl:variable name="ptiurn">
                            <xsl:if test="xhibit:PTIURN">
                                <xsl:value-of select="xhibit:PTIURN"/>
                            </xsl:if>                
                        </xsl:variable>                   
                        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                            <tr valign="top">
                                <td width="50%">
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
                                    <br/>
									<!-- BUSINESS RULE: Only display if letter not to Prosecution -->
										<xsl:if test="$LetterType != 'Prosecution'">
											<!-- Hold no of defendants text -->
											<xsl:variable name="Defendants">
												<xsl:choose>
													<xsl:when test="../../xhibit:NumberOfDefendants">
														<xsl:variable name="DefsOnDLL">
															<xsl:value-of select="count(../xhibit:DefendantSummaries/xhibit:DefendantSummary)"/>
														</xsl:variable>
														<xsl:value-of select="../../xhibit:NumberOfDefendants - $DefsOnDLL"/>
													</xsl:when>
													<xsl:otherwise>0</xsl:otherwise>
												</xsl:choose>
											</xsl:variable>
											<!-- Only display if no of defendants has a value -->
											<xsl:if test="$Defendants != 0 and $Defendants > 0">
												<xsl:choose>
													<xsl:when test="$Defendants = 1">
														<xsl:call-template name="getValue">
															<xsl:with-param name="language" select="$language"/>
															<xsl:with-param name="key" select="'and another'"/>
														</xsl:call-template>
													</xsl:when>
													<xsl:when test="$Defendants > 1">
														<xsl:call-template name="getValue">
															<xsl:with-param name="language" select="$language"/>
															<xsl:with-param name="key" select="'and'"/>
														</xsl:call-template>
														<xsl:text> </xsl:text>
														<xsl:value-of select="$Defendants"/>
														<xsl:text> </xsl:text>
														<xsl:call-template name="getValue">
															<xsl:with-param name="language" select="$language"/>
															<xsl:with-param name="key" select="'others'"/>
														</xsl:call-template>
													</xsl:when>
												</xsl:choose>
											</xsl:if>
										</xsl:if>										
                                    
                                    
                                </td>
                                <td width="15%">   
                                    <!-- Display for the Defendant Hearing Summaries only -->
                                    <xsl:if test="../../@type='Defendant'">
                                        <xsl:if test="xhibit:SolicitorRef">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'Ref:'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="xhibit:SolicitorRef"/>
                                        </xsl:if>
                                    </xsl:if>
                                </td>
                                <td width="15%">
                                    <xsl:if test="position()=1">
                                        <xsl:value-of select="../../xhibit:CommittingCourtSummary/xhibit:CourtHouseCode/@CourtHouseShortName"/>
                                    </xsl:if>
                                </td>
                                <td width="20%">
                                    <!-- BUSINESS RULE : Display the PTIURN for each defendant or org name / pros ref (per case) -->
                                    <xsl:choose>
                                        <xsl:when test="$LetterType='Solicitor'">
                                            <xsl:choose>
                                                <xsl:when test="contains(../../xhibit:ProsecutionSummary/xhibit:OrganisationName,'Crown Prosecution Service')">
                                                <!-- Display the CPS ref for every defendant -->
                                                    <xsl:if test="$ptiurn">
                                                        <xsl:if test="not(normalize-space($ptiurn) = '')">
                                                             <xsl:call-template name="getValue">
                                                                 <xsl:with-param name="language" select="$language"/>
                                                                 <xsl:with-param name="key" select="'CPS Ref:'"/>
                                                             </xsl:call-template>
                                                             <xsl:text> </xsl:text>
                                                             <xsl:value-of select="$ptiurn"/>
                                                        </xsl:if>
                                                    </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <!-- Display the prosecuting org for the first row -->
                                                    <xsl:if test="position()=1">
                                                        <xsl:variable name="prosecutingOrganisation">
                                                            <xsl:call-template name="getValue">
                                                                <xsl:with-param name="language" select="$language"/>
                                                                <xsl:with-param name="key" select="../../xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
                                                            </xsl:call-template>
                                                        </xsl:variable>
                                                        <xsl:call-template name="toUpper">
                                                            <xsl:with-param name="content" select="$prosecutingOrganisation"/>
                                                        </xsl:call-template>
                                                    </xsl:if>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>
                                        <xsl:when test="$LetterType='Prosecution'">
                                            <xsl:choose>
                                                <xsl:when test="contains(../../xhibit:ProsecutionSummary/xhibit:OrganisationName,'Crown Prosecution Service')">
                                                    <!-- Display the CPS ref for every defendant. Unlikely to happen for a Prosecution Letter -->
                                                    <xsl:if test="$ptiurn">
                                                        <xsl:if test="not(normalize-space($ptiurn) = '')">
                                                            <xsl:call-template name="getValue">
                                                                <xsl:with-param name="language" select="$language"/>
                                                                <xsl:with-param name="key" select="'CPS Ref:'"/>
                                                            </xsl:call-template>
                                                            <xsl:text> </xsl:text>
                                                            <xsl:value-of select="$ptiurn"/>
                                                        </xsl:if>
                                                    </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <!-- Display the prosecuting ref for row 1 only -->
                                                    <xsl:if test="position()=1">
                                                        <xsl:if test="../../xhibit:ProsecutionSummary/xhibit:ProsecutingReference">
                                                            <xsl:call-template name="getValue">
                                                                <xsl:with-param name="language" select="$language"/>
                                                                <xsl:with-param name="key" select="'Ref:'"/>
                                                            </xsl:call-template>
                                                            <xsl:text> </xsl:text>
                                                            <xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:ProsecutingReference"/>
                                                        </xsl:if>
                                                    </xsl:if>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>
                                        <xsl:when test="$LetterType='Defendant'">
                                            <xsl:choose>
                                                <xsl:when test="contains(../../xhibit:ProsecutionSummary/xhibit:OrganisationName,'Crown Prosecution Service')">
                                                    <!-- Display the CPS ref for every defendant -->
                                                    <xsl:if test="$ptiurn">
                                                        <xsl:if test="not(normalize-space($ptiurn) = '')">
                                                            <xsl:call-template name="getValue">
                                                                <xsl:with-param name="language" select="$language"/>
                                                                <xsl:with-param name="key" select="'CPS Ref:'"/>
                                                            </xsl:call-template>
                                                            <xsl:text> </xsl:text>
                                                            <xsl:value-of select="$ptiurn"/>
                                                        </xsl:if>
                                                    </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <!-- Display the prosecuting ref for row 1 only -->
                                                    <xsl:if test="position()=1">
                                                        <xsl:if test="../../xhibit:ProsecutionSummary/xhibit:ProsecutingReference">
                                                            <xsl:call-template name="getValue">
                                                                <xsl:with-param name="language" select="$language"/>
                                                                <xsl:with-param name="key" select="'Ref:'"/>
                                                            </xsl:call-template>
                                                            <xsl:text> </xsl:text>
                                                            <xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:ProsecutingReference"/>
                                                        </xsl:if>
                                                    </xsl:if>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </table>
                    </xsl:for-each>
                </td>
            </tr>
        </table>
        
        <br/>
        <b style="font-size:10pt">
            <xsl:if test="../xhibit:ListNote">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="../xhibit:ListNote"/>
                </xsl:call-template>
            </xsl:if>
        </b>
    </xsl:template>
    
</xsl:stylesheet>
