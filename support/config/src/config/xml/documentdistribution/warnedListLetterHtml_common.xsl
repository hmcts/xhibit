<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" version="1.0">
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>

    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>

    <xsl:template name="DisplayWarnedListLetter">
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
                <hr style="border-top: solid #000000;"/>
                <div align="center" style="font-family: Courier New, Courier">
                    <div style="font-size: 10pt">
                        <b>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Criminal Warned List for'"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:choose>
                                <xsl:when test="xhibit:ListHeaderSummary/xhibit:Duration">
                                    <xsl:choose>
                                        <xsl:when test="xhibit:ListHeaderSummary/xhibit:Duration &lt;=7">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'week commencing'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'to'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>                                            
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:EndDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'week commencing'"/>
                                    </xsl:call-template>
                                    <xsl:text> </xsl:text>                                
                                    <xsl:call-template name="displayDate">
                                        <xsl:with-param name="input" select="xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </xsl:otherwise>
                            </xsl:choose>
                        </b>
                    </div>
                </div>
                <br/>
                <br/>
                <!-- Call template to display Listing Instructions -->
                <div style="font-family: Courier New, Courier">
                    <div style="font-size: 10pt">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'The undermentioned cases are warned for hearing during the above period. Please ensure that clients and witnesses are given this information.'"/>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Representations regarding the listing of a case should be made to the Listing Officer'"/>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                        <!-- BUSINESS RULE : If there is a WarnedListDetail/DeadLineDate display the DeadLineDate and time if there is one otherwise just display immediately text-->
                        <xsl:choose>
                            <xsl:when test="xhibit:WarnedListDetail/xhibit:DeadLineDate">
                                <xsl:choose>
                                    <xsl:when test="xhibit:WarnedListDetail/xhibit:DeadlineTime">
                                        <b>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'no later than'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="xhibit:WarnedListDetail/xhibit:DeadlineTime"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'on'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input" select="xhibit:WarnedListDetail/xhibit:DeadLineDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                        </b>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <b>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'no later than'"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>                            
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input" select="xhibit:WarnedListDetail/xhibit:DeadLineDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                        </b>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                            <xsl:otherwise>
                                <b>
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'immediately'"/>
                                    </xsl:call-template>                                
                                </b>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </div>
                <!-- Call template to display Warned List Details -->
                <div style="font-family: Courier New, Courier">
                    <div style="font-size: 10pt">
                        <br/>
                        <xsl:apply-templates select="xhibit:WarnedListDetail">
                            <xsl:with-param name="language" select="$language"/>
                        </xsl:apply-templates>
                    </div>
                </div>
                <!-- Call template to check and display case and defendant details -->
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
                            <xsl:if test=". !='-'">
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
    <!-- Template to display CourtList info -->
    <xsl:template match="xhibit:CourtListSummaries">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:CourtListSummary">
            <!-- Display With Fixed Date Details -->
            <xsl:if test="count(xhibit:WithFixedDateSummary) > 0">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Fixed'"/>
                </xsl:call-template>
                <br/>
                <xsl:for-each select="xhibit:WithFixedDateSummary">
                    <xsl:for-each select="xhibit:FixtureSummary/xhibit:CaseSummaries/xhibit:CaseSummary">
                        <br/>
                        <b style="font-size:10pt">
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="xhibit:Hearing/xhibit:HearingDescription"/>
                            </xsl:call-template>                        
                        </b>
                        <br/>
                        <xsl:apply-templates select=".">
                            <xsl:with-param name="type">with</xsl:with-param>
                            <xsl:with-param name="language" select="$language"/>
                        </xsl:apply-templates>
                    </xsl:for-each>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="count(xhibit:WithoutFixedDateSummary) > 0 and count(xhibit:WithFixedDateSummary) > 0">
                <hr style="border-top: solid #000000;"/>
            </xsl:if>
            <!-- Display Without Fixed Date Details -->
            <xsl:if test="count(xhibit:WithoutFixedDateSummary) > 0">
                <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                    <tr valign="top">
                        <td width="15%">
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'Warned for'"/>
                            </xsl:call-template>
                            <xsl:text> : </xsl:text>                        
                        </td>
                        <td width="85%">
                            <!-- Pick the first CourtListSummary withoutfixed date and display the warned for courts in there.. sames for all the CourtListSummary's -->
                            <xsl:for-each select="xhibit:WithoutFixedDateSummary">
                                <xsl:if test="position()=1">
                                    <xsl:for-each select="../xhibit:WarnedForCourts/xhibit:WarnedForCourt">
                                        <xsl:choose>
                                            <xsl:when test="starts-with(.,'at')">
                                                <xsl:variable name="courthousename">
                                                    <xsl:call-template name="toUpper">
                                                        <xsl:with-param name="content" select="substring-after(.,'at ')"/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:variable name="courthousename2">
                                                    <xsl:call-template name="getValue">
                                                        <xsl:with-param name="language" select="$language"/>
                                                        <xsl:with-param name="key" select="$courthousename"/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:call-template name="TitleCase">
                                                    <xsl:with-param name="text" select="$courthousename2"/>
                                                </xsl:call-template>                                            
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:variable name="courthousename">
                                                    <xsl:call-template name="toUpper">
                                                        <xsl:with-param name="content" select="."/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:variable name="courthousename2">
                                                    <xsl:call-template name="getValue">
                                                        <xsl:with-param name="language" select="$language"/>
                                                        <xsl:with-param name="key" select="$courthousename"/>
                                                    </xsl:call-template>
                                                </xsl:variable>
                                                <xsl:call-template name="TitleCase">
                                                    <xsl:with-param name="text" select="$courthousename2"/>
                                                </xsl:call-template>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <br/>
                                    </xsl:for-each>
                                </xsl:if>
                            </xsl:for-each>
                        </td>
                    </tr>
                </table>
                <xsl:for-each select="xhibit:WithoutFixedDateSummary">
                    <xsl:for-each select="xhibit:FixtureSummary/xhibit:CaseSummaries/xhibit:CaseSummary">
                        <br/>
                        <b style="font-size:10pt">
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="xhibit:Hearing/xhibit:HearingDescription"/>
                            </xsl:call-template>                        
                        </b>
                        <br/>
                        <xsl:apply-templates select=".">
                            <xsl:with-param name="type">without</xsl:with-param>
                            <xsl:with-param name="language" select="$language"/>                            
                        </xsl:apply-templates>
                    </xsl:for-each>
                </xsl:for-each>
                <br/>
                <br/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display the case information -->
    <xsl:template match="xhibit:CaseSummary">
        <xsl:param name="type"/>
        <xsl:param name="language"/>        
        <!-- Display Hearing and Case Information  -->
        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
            <tr valign="top">
                <td width="15%">
                    <xsl:value-of select="xhibit:CaseNumber"/>
                </td>
                <td width="85%">
                    <xsl:variable name="LetterType">
                        <xsl:choose>
                            <xsl:when test="../../../../../../xhibit:Recipient/xhibit:Defendant">Defendant</xsl:when>
                            <xsl:when test="../../../../../../xhibit:Recipient/xhibit:Solicitor">Solicitor</xsl:when>
                            <xsl:when test="../../../../../../xhibit:Recipient/xhibit:Prosecution">Prosecution</xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="orgname">
							<xsl:if test="xhibit:ProsecutionSummary/xhibit:OrganisationName">
								<xsl:call-template name="toUpper">
								   <xsl:with-param name="content" select="xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
								</xsl:call-template> 
							</xsl:if> 
                    </xsl:variable>
                    <xsl:for-each select="xhibit:DefendantSummaries/xhibit:DefendantSummary">
                        <xsl:variable name="ptiurn">
                            <xsl:if test="xhibit:PTIURN">
                                <xsl:value-of select="xhibit:PTIURN"/>
                            </xsl:if>                
                        </xsl:variable>
                        <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                            <tr valign="top">
                                <td width="2%">
                                    <xsl:if test="xhibit:CustodyStatus = 'In custody' or xhibit:CustodyStatus = 'On remand'">
                                        <xsl:text>* </xsl:text>
                                    </xsl:if>
                                </td>
                                <td width="83%">
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
                                </td>
                                <td width="15%">
                                    <!-- BUSINESS RULE : Display the Prosecuting Reference, applies to all defendants -->
                                    <xsl:choose>
                                        <xsl:when test="$LetterType='Solicitor'">
                                            <!-- Check who solicitor representing -->
                                            <xsl:choose>
                                                <xsl:when test="../../@type='Defendant'">
                                                    <xsl:value-of select="xhibit:SolicitorRef"/>
                                                </xsl:when>
                                                <xsl:when test="../../@type='Prosecution'">
                                                    <xsl:if test="position()=1">
                                                        <xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:SolicitorRef"/>
                                                    </xsl:if>
                                                </xsl:when>
                                            </xsl:choose>
                                        </xsl:when>
                                        <!-- Check to see if it is a prosecution case -->
                                        <xsl:when test="$LetterType='Prosecution'">
											<!-- Check to see if it PTIURN value is needed to be dispalyed -->
												<!-- PTIURN value is displayed when the prosecution is of type: CROWN PROSECUTION SERVICE -->
													<xsl:if test="contains($orgname,'CROWN PROSECUTION SERVICE')">
														<xsl:if test="$ptiurn">
															 <xsl:if test="not(normalize-space($ptiurn) = '')">
																 <xsl:value-of select="$ptiurn"/>
															 </xsl:if>   
														</xsl:if>
													</xsl:if>
													<xsl:if test="contains($orgname,'CPS')">
														<xsl:if test="$ptiurn">
															 <xsl:if test="not(normalize-space($ptiurn) = '')">
																 <xsl:value-of select="$ptiurn"/>
															 </xsl:if>   
														</xsl:if>
													</xsl:if>
													<!-- Catch instances when PTIURN should not be displayed and the Prsecuting reference value should -->
													<xsl:if test="contains($orgname,'CROWN PROSECUTION SERVICE')=false">
														<xsl:if test="position()=1">
															<xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:ProsecutingReference"/>
														</xsl:if>
													</xsl:if>
													<xsl:if test="contains($orgname,'CPS')=false">
														<xsl:if test="position()=1">
															<xsl:value-of select="../../xhibit:ProsecutionSummary/xhibit:ProsecutingReference"/>
														</xsl:if>
													</xsl:if>
												</xsl:when>
                                        <xsl:when test="$LetterType='Defendant'">
                                            <!-- Display nothing -->
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </table>
                    </xsl:for-each>
                    <!-- BUSINESS RULE: Only display if letter not to Prosecution -->
                    <xsl:if test="$LetterType != 'Prosecution'">
                        <!-- Hold no of defendants text -->
                        <xsl:variable name="Defendants">
                            <xsl:choose>
                                <xsl:when test="xhibit:NoOfDefsOnCase">
                                    <xsl:variable name="DefsOnWLL">
                                        <xsl:value-of select="count(xhibit:DefendantSummaries/xhibit:DefendantSummary)"/>
                                    </xsl:variable>
                                    <xsl:value-of select="xhibit:NoOfDefsOnCase - $DefsOnWLL"/>
                                </xsl:when>
                                <xsl:otherwise>0</xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <!-- Only display if no of defendants has a value -->
                        <xsl:if test="$Defendants != 0 and $Defendants > 0">
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                                <tr valign="top">
                                    <td width="2%"/>
                                    <td width="98%">
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
                                    </td>
                                </tr>
                            </table>
                        </xsl:if>
                    </xsl:if>
                    <!-- BUSINESS RULE: If letter for Solicitor then display prosecuting organisation name -->
                    <xsl:if test="$LetterType = 'Solicitor'">
                        <xsl:choose>
                            <xsl:when test="@type='Defendant'">
                            </xsl:when>
                            <xsl:when test="@type='Prosecution'">
                                <!--<xsl:value-of select="xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
                                <br/> -->
                            </xsl:when>
                        </xsl:choose>
                    </xsl:if>
                    <b>
                        <xsl:if test="../../xhibit:FixedDate and $type = 'with'">
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                                <tr valign="top">
                                    <td width="2%"/>
                                    <td width="98%">
                                        <b>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'Fixed for'"/>
                                            </xsl:call-template>                                                    
                                            <xsl:text> </xsl:text>                                        
                                            <xsl:call-template name="displayDate">
                                                <xsl:with-param name="input" select="../../xhibit:FixedDate"/>
                                                <xsl:with-param name="language" select="$language"/>
                                            </xsl:call-template>
                                            <xsl:text> </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'at'"/>
                                            </xsl:call-template>                                                    
                                            <xsl:text> </xsl:text>
                                            <xsl:variable name="crtName">
                                                <xsl:choose>
                                                    <xsl:when test="starts-with(../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName,'at')">
                                                        <xsl:value-of select="substring-after(../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName,'at ')"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="../../../../xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                                                    </xsl:otherwise>
                                                </xsl:choose>                                                        
                                            </xsl:variable>
                                            <xsl:variable name="courthousename">
                                                <xsl:call-template name="toUpper">
                                                    <xsl:with-param name="content" select="$crtName"/>
                                                </xsl:call-template>
                                            </xsl:variable>
                                            <xsl:variable name="courthousename2">
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="$courthousename"/>
                                                </xsl:call-template>
                                            </xsl:variable>
                                            <xsl:call-template name="TitleCase">
                                                <xsl:with-param name="text" select="$courthousename2"/>
                                            </xsl:call-template>
                                        </b>
                                    </td>
                                </tr>
                            </table>
                        </xsl:if>
                    </b>
                    <xsl:if test="../../xhibit:Notes">
                        <b>
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:10pt">
                                <tr valign="top">
                                    <td width="2%"/>
                                    <td width="98%">
                                        <b>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="../../xhibit:Notes"/>
                                            </xsl:call-template>                                        
                                        </b>
                                    </td>
                                </tr>
                            </table>
                        </b>
                    </xsl:if>
                </td>
            </tr>
        </table>
    </xsl:template>
    <!-- Display Warned List Details -->
    <xsl:template match="xhibit:WarnedListDetail">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:Text">
            <xsl:choose>
                <xsl:when test="contains(.,'---------------------')">
                    <xsl:value-of select="substring-before(.,'---------------------')"/>
                   <br/>
                    <xsl:value-of select="'---------------------'"/>
                    <xsl:value-of select="substring-after(.,'---------------------')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
            <br/>
        </xsl:for-each>
        <br/>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="'* denotes a defendant in custody.'"/>
        </xsl:call-template>
        <hr style="border-top: solid #000000;"/>
    </xsl:template>
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
                            <xsl:if test=". !='-'">
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
                <div align="right" style="font-size: 10pt">
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
                                    <xsl:if test=". !='-'">
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
                <div align="right" style="font-size: 10pt">
                    <xsl:call-template name="DisplayStartDate">
                        <xsl:with-param name="date" select="$java-date"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                    <br/>
                </div>
            </xsl:if>
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
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'c/o The Governor'"/>
                            </xsl:call-template>                            
                            <br/>
                            <xsl:if test="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID">
                                <xsl:value-of select="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID"/>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="substring-after(xhibit:Defendant/xhibit:PrisonLocation/xhibit:Location,' ')"/>
                                </xsl:call-template>                                
                            </xsl:if>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
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
                                <xsl:if test=". !='-'">
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
                                <xsl:if test=". !='-'">
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
            <div align="right" style="font-size: 10pt">
                    <xsl:call-template name="DisplayStartDate">
                        <xsl:with-param name="date" select="$java-date"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                <br/>
            </div>
        </xsl:if>
        <br/>
    </xsl:template>
</xsl:stylesheet>