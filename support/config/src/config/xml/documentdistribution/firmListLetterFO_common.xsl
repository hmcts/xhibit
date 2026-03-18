<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit"  xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

	<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>

    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>

    <!-- Page Sequence Setup and Display the Daily List Letter Info -->
    <xsl:template name="DisplayFirmListLetter">
        <xsl:param name="language"/>
        <!-- for the page sequence set the force-page-count to no-force to stop an extra blank page being inserted after each new section -->
        <fo:page-sequence master-reference="document" initial-page-number="1" force-page-count="no-force">
            <!-- Header Info for the first page-->
            <fo:static-content flow-name="header-first">
                <fo:block text-align="start" font-size="10pt" font-family="serif" line-height="1em + 2pt">
                    <fo:retrieve-marker retrieve-class-name="term" retrieve-boundary="page" retrieve-position="first-starting-within-page"/>
                    <fo:block>
					<fo:table table-layout="fixed">
                        <fo:table-column column-width="150mm"/>
                        <fo:table-column column-width="30mm"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-family="Helvetica" font-size="13pt">
                                        <fo:block font-size="18pt">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'The Crown Court'"/>
                                            </xsl:call-template>
                                        </fo:block>
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
                                    </fo:block>
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
                                        <!-- make sure that each WLL has a unique reference -->
                                        <fo:page-number-citation ref-id="{$language}{generate-id()}"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
                    <fo:retrieve-marker retrieve-class-name="term" retrieve-boundary="page" retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:static-content>
            <!-- Header Info for pages after the first -->
            <fo:static-content flow-name="header-rest">
                <fo:block text-align="start" font-size="10pt" font-family="serif" line-height="1em + 2pt">
                    <fo:retrieve-marker retrieve-class-name="term" retrieve-boundary="page" retrieve-position="first-starting-within-page"/>
                    <fo:block>
					<fo:table table-layout="fixed">
                        <fo:table-column column-width="150mm"/>
                        <fo:table-column column-width="30mm"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-family="Helvetica" font-size="13pt">
                                        <fo:block font-size="18pt">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'The Crown Court'"/>
                                            </xsl:call-template>
                                        </fo:block>
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
                                    </fo:block>
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
                                        <!-- make sure that each WLL has a unique reference -->
                                        <fo:page-number-citation ref-id="{$language}{generate-id()}"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
                    <fo:retrieve-marker retrieve-class-name="term" retrieve-boundary="page" retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:static-content>
            <!-- Used to display fold line in the page -->
            <fo:static-content flow-name="page-fold">
                <!-- call template to display fold line -->
                <xsl:call-template name="FoldLine"/>
            </fo:static-content>
            <!-- Footer Info -->
            <fo:static-content flow-name="xsl-region-after" font-size="7pt">
                <fo:block>
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
				<fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body font-family="Helvetica" font-weight="normal">
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>
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
                                            
                                            <fo:inline white-space-collapse="false">
                                                <xsl:text>,       </xsl:text>
                                            </fo:inline>
                                        </xsl:if>
                                    </xsl:for-each>
                                    <xsl:value-of select="xhibit:CrownCourt/xhibit:CourtHouseAddress/xhibit:PostCode"/>
                                    <fo:inline white-space-collapse="false">
                                        <xsl:text>.       </xsl:text>
                                    </fo:inline>
                                    <xsl:value-of select="xhibit:CrownCourt/xhibit:CourtHouseDX"/>
                                    <fo:inline white-space-collapse="false">
                                        <xsl:text>       </xsl:text>
                                    </fo:inline>
                                    <xsl:call-template name="getValue">
                                        <xsl:with-param name="language" select="$language"/>
                                        <xsl:with-param name="key" select="'Tel'"/>
                                    </xsl:call-template>
                                    <xsl:text>: </xsl:text>                                    
                                    <xsl:value-of select="xhibit:CrownCourt/xhibit:CourtHouseTelephone"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <xsl:if test="$language != $DefaultLang">
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block>
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
            </fo:static-content>
            <fo:flow flow-name="xsl-region-body" font-size="10pt">
                <fo:block>
                    <!-- Call template to display Recipient Details -->
                    <xsl:apply-templates select="xhibit:Recipient">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:apply-templates>
                    <fo:block id="{$language}{generate-id()}"/>
                </fo:block>
            </fo:flow>
        </fo:page-sequence>
    </xsl:template>
    <xsl:template match="xhibit:Recipient">
        <xsl:param name="language"/>
        <!-- Can be a Solicitor or Prosecution -->
        <fo:block space-after="10pt">
            <fo:leader leader-pattern="rule" leader-length="100%"/>
        </fo:block>
        <fo:block>
            <!-- Test if Solicitor -->
            <xsl:if test="xhibit:Solicitor">
                <!-- Can be a Solicitor Person -->
                <xsl:if test="xhibit:Solicitor/xhibit:Party/xhibit:Person">
					<fo:block>
                    <fo:table table-layout="fixed">
                        <fo:table-column column-width="90mm"/>
                        <fo:table-body font-family="Helvetica" font-weight="normal">
                            <fo:table-row>
                                <fo:table-cell height="37mm" hyphenate="true" language="en">
                                    <fo:block font-family="Courier">
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
                                    </fo:block>
                                    <xsl:for-each select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Address/xhibit:Line">
                                        <xsl:if test=". !='-'">
                                            <fo:block font-family="Courier" font-weight="normal">
                                                <xsl:value-of select="."/>
                                            </fo:block>
                                        </xsl:if>
                                    </xsl:for-each>
                                    <fo:block font-family="Courier" font-weight="normal">
                                        <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Address/xhibit:PostCode"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
					<fo:block>
                    <fo:table table-layout="fixed">
                        <fo:table-column column-width="185mm"/>
                        <fo:table-body font-family="Helvetica" font-weight="normal">
                            <fo:table-row>
                                <fo:table-cell text-align="right" height="6mm">
                                    <fo:block font-family="Courier" font-weight="bold">
                                        <xsl:call-template name="DisplayStartDate">
                                            <xsl:with-param name="date" select="$java-date"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
                </xsl:if>
                <!-- Can be a Solicitor Organisation -->
                <xsl:if test="xhibit:Solicitor/xhibit:Party/xhibit:Organisation">
					<fo:block>
                    <fo:table table-layout="fixed">
                        <fo:table-column column-width="90mm"/>
                        <fo:table-body font-family="Helvetica" font-weight="normal">
                            <fo:table-row>
                                <fo:table-cell height="37mm" hyphenate="true" language="en">
                                    <fo:block font-family="Courier">
                                        <xsl:value-of select="xhibit:Solicitor/xhibit:FirmName"/>
                                    </fo:block>
                                    <xsl:choose>
                                        <!-- BUSINESS RULE: When there is a Issuing Court DX and Solicitor DX display the Solicitor DX otherwise normal address -->
                                        <xsl:when test="string-length(../xhibit:CrownCourt/xhibit:CourtHouseDX) > 0 and string-length(xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationDX) > 0">
                                            <fo:block font-family="Courier" font-weight="bold">
                                                <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationDX"/>
                                            </fo:block>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:for-each select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationAddress/xhibit:Line">
                                                <xsl:if test=". !='-'">
                                                    <fo:block font-family="Courier" font-weight="normal">
                                                        <xsl:value-of select="."/>
                                                    </fo:block>
                                                </xsl:if>
                                            </xsl:for-each>
                                            <fo:block font-family="Courier" font-weight="normal">
                                                <xsl:value-of select="xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationAddress/xhibit:PostCode"/>
                                            </fo:block>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
					<fo:block>
                    <fo:table table-layout="fixed">
                        <fo:table-column column-width="185mm"/>
                        <fo:table-body font-family="Helvetica" font-weight="normal">
                            <fo:table-row>
                                <fo:table-cell text-align="right" height="6mm">
                                    <fo:block font-family="Courier" font-weight="bold">
                                        <xsl:call-template name="DisplayStartDate">
                                            <xsl:with-param name="date" select="$java-date"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
                </xsl:if>
            </xsl:if>
            <!-- Test If Defendant -->
            <xsl:if test="xhibit:Defendant">
				<fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body font-family="Helvetica" font-weight="normal">
                        <fo:table-row>
                            <fo:table-cell height="37mm" hyphenate="true" language="en">
                                <!-- BUSINESS RULE: If the defendant is in custody or on remand display the prisoner and prison details -->
                                <xsl:choose>
                                    <xsl:when test="xhibit:Defendant/xhibit:CustodyStatus = 'In custody' or xhibit:Defendant/xhibit:CustodyStatus = 'On remand'">
                                        <fo:block font-family="Courier">
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
                                        </fo:block>
                                        <fo:block font-family="Courier" font-weight="bold">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'c/o The Governor'"/>
                                            </xsl:call-template>                                        
                                        </fo:block>
                                        <fo:block font-family="Courier" font-weight="bold">
                                            <xsl:if test="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID">
                                                <xsl:value-of select="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID"/>
                                                <xsl:text> </xsl:text>
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="xhibit:Defendant/xhibit:PrisonLocation/xhibit:Location"/>
                                            </xsl:call-template>                                        
                                            </xsl:if>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <fo:block font-family="Courier">
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
                                        </fo:block>
                                        <xsl:for-each select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Address/xhibit:Line">
                                            <xsl:if test=". !='-'">
                                                <fo:block font-family="Courier" font-weight="normal">
                                                    <xsl:value-of select="."/>
                                                </fo:block>
                                            </xsl:if>
                                        </xsl:for-each>
                                        <fo:block font-family="Courier" font-weight="normal">
                                            <xsl:value-of select="xhibit:Defendant/xhibit:PersonalDetails/xhibit:Address/xhibit:PostCode"/>
                                        </fo:block>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
				</fo:block>
				<fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="185mm"/>
                    <fo:table-body font-family="Helvetica" font-weight="normal">
                        <fo:table-row>
                            <fo:table-cell text-align="right" height="6mm">
                                <fo:block font-family="Courier" font-weight="bold">
                                        <xsl:call-template name="DisplayStartDate">
                                            <xsl:with-param name="date" select="$java-date"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
				</fo:block>
            </xsl:if>
            <!-- Test if Prosecution -->
            <xsl:if test="xhibit:Prosecution">
				<fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="130mm"/>
                    <fo:table-body font-family="Helvetica" font-weight="normal">
                        <fo:table-row>
                            <fo:table-cell height="37mm" hyphenate="true" language="en">
                                <fo:block font-family="Courier">
                                    <xsl:variable name="prosOrg">
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationName"/>
                                        </xsl:call-template>
                                    </xsl:variable>
                                    <xsl:call-template name="toUpper">
                                        <xsl:with-param name="content" select="$prosOrg"/>
                                    </xsl:call-template>
                                </fo:block>
                                <xsl:choose>
                                    <!-- BUSINESS RULE: When there is a Issuing Court DX and Prosecuting DX display the Prosecuting DX otherwise normal address -->
                                    <xsl:when test="string-length(../xhibit:CrownCourt/xhibit:CourtHouseDX) > 0 and string-length(xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationDX) > 0">
                                        <fo:block font-family="Courier" font-weight="bold">
                                            <xsl:value-of select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationDX"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:for-each select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationAddress/xhibit:Line">
                                            <xsl:if test=". !='-'">
                                                <fo:block font-family="Courier" font-weight="normal">
                                                    <xsl:value-of select="."/>
                                                </fo:block>
                                            </xsl:if>
                                        </xsl:for-each>
                                        <fo:block font-family="Courier" font-weight="normal">
                                            <xsl:value-of select="xhibit:Prosecution/xhibit:ProsecutingOrganisation/xhibit:OrganisationAddress/xhibit:PostCode"/>
                                        </fo:block>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
				</fo:block>
				<fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="185mm"/>
                    <fo:table-body font-family="Helvetica" font-weight="normal">
                        <fo:table-row>
                            <fo:table-cell text-align="right" height="6mm">
                                <fo:block font-family="Courier" font-weight="bold">
                                        <xsl:call-template name="DisplayStartDate">
                                            <xsl:with-param name="date" select="$java-date"/>
                                            <xsl:with-param name="language" select="$language"/>
                                        </xsl:call-template>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
				</fo:block>
            </xsl:if>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
        </fo:block>
        <!-- Display Daily List Letter Title Details -->
        <fo:block font-family="Courier" line-height="30pt" text-align="center" font-weight="bold">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Criminal Firm List for'"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="../xhibit:ListHeaderSummary/xhibit:Duration">
                    <xsl:choose>
                        <xsl:when test="../xhibit:ListHeaderSummary/xhibit:Duration =4">
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'week commencing'"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="displayDate">
                                <xsl:with-param name="input" select="../xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="displayDate">
                                <xsl:with-param name="input" select="../xhibit:ListHeaderSummary/xhibit:StartDate"/>
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'to'"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <xsl:call-template name="displayDate">
                                <xsl:with-param name="input" select="../xhibit:ListHeaderSummary/xhibit:EndDate"/>
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
                        <xsl:with-param name="input" select="../xhibit:ListHeaderSummary/xhibit:StartDate"/>
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
        <!-- Call template to display standard firm list text details -->
        <xsl:call-template name="DefaultFLLText">
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
        <!-- Call template to check and display case and defendant details -->
        <xsl:apply-templates select="../xhibit:CourtListSummaries">
            <xsl:with-param name="language" select="$language"/>
        </xsl:apply-templates>
        <xsl:apply-templates select="../xhibit:ReserveListSummary">
            <xsl:with-param name="language" select="$language"/>
        </xsl:apply-templates>
    </xsl:template>
    <!-- Display Court List Information -->
    <xsl:template match="xhibit:CourtListSummaries">
        <xsl:param name="language"/>
        <!-- Display info for each court list -->
        <xsl:for-each select="xhibit:CourtListSummary">
            <xsl:sort select="./@SittingDate"/>
            <xsl:sort select="xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
            <xsl:sort select="xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:SittingPriority" order="descending"/>
            <xsl:sort select="xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:CourtRoomNumber" data-type="number"/>
            <xsl:choose>
                <xsl:when test="position()=1">
                    <fo:block space-before="10pt" font-weight="bold" space-after="10pt">
                        <xsl:variable name="courthousename">
                            <xsl:call-template name="toUpper">
                                <xsl:with-param name="content" select="xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
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
                    </fo:block>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="pos">
                        <xsl:value-of select="position()-1"/>
                    </xsl:variable>
                    <xsl:variable name="previousCourt">
                        <xsl:for-each select="../xhibit:CourtListSummary">
                            <xsl:sort select="./@SittingDate"/>
                            <xsl:sort select="xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            <xsl:sort select="xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:SittingPriority" order="descending"/>
                            <xsl:sort select="xhibit:SittingSummaries/xhibit:SittingSummary/xhibit:CourtRoomNumber" data-type="number"/>
                            <xsl:if test="position()=$pos">
                                <xsl:value-of select="xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                            </xsl:if>
                        </xsl:for-each>                            
                    </xsl:variable>
                    <xsl:variable name="nextCourt">
                        <xsl:value-of select="xhibit:CourtHouseSummary/xhibit:CourtHouseName"/>
                    </xsl:variable>
                    <xsl:if test="$nextCourt != $previousCourt">
                        <fo:block space-before="10pt" font-weight="bold" space-after="10pt">
                            <xsl:variable name="courthousename">
                                <xsl:call-template name="toUpper">
                                    <xsl:with-param name="content" select="$nextCourt"/>
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
                        </fo:block> 
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="xhibit:SittingSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
        </xsl:for-each>
    </xsl:template>
    <!-- Display Sitting Info -->
    <xsl:template match="xhibit:SittingSummaries">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:SittingSummary">
            <!-- Sort by Sitting Priority desc to ensure that floating are at bottom then sort by Court Room Number -->
            <xsl:sort select="xhibit:SittingPriority" order="descending"/>
            <xsl:sort select="xhibit:CourtRoomNumber" data-type="number"/>
            <fo:block font-weight="bold">
                <xsl:choose>
                    <!-- When the sitting priority is T (top priority) display court room number -->
                    <xsl:when test="xhibit:SittingPriority='T'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Court'"/>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="xhibit:CourtRoomNumber"/>
                    </xsl:when>
                    <!-- When the sitting priority is F (Floating) display floating default text -->
                    <xsl:when test="xhibit:SittingPriority='F'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'The following may be taken in any court'"/>
                        </xsl:call-template>
                    </xsl:when>
                    <!-- When the sitting priority is R (Reserved) display reserved default text -->
                    <xsl:when test="xhibit:SittingPriority='R'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'Reserve Case(s)'"/>
                        </xsl:call-template>
                    </xsl:when>
                </xsl:choose>
            </fo:block>
            <!-- Display the hearing summary info for the sitting -->
            <xsl:apply-templates select="xhibit:HearingSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display the hearing information -->
    <xsl:template match="xhibit:HearingSummaries">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:HearingSummary">
            <xsl:variable name="HearingString">
                <xsl:choose>
                    <!-- If Sitting Priority is T then display the hearing date and sitting at time -->
                    <xsl:when test="../../xhibit:SittingPriority= 'T'">
                        <!-- display the hearing date -->
                            <xsl:call-template name="displayDayDate">
                                <xsl:with-param name="input" select="xhibit:HearingDetails/xhibit:HearingDate"/>
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                        <xsl:if test="../../xhibit:SittingAt">
                            <xsl:text> - </xsl:text>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'sitting at'"/>
                            </xsl:call-template>
                            <xsl:text> </xsl:text>
                            <!-- Call template to format the time -->
                            <xsl:choose>
                                <!-- Handle SittingAt data 10:00:00 -->
                                <xsl:when test="string-length(../../xhibit:SittingAt)=8">
                                    <!-- call template to postfix AM or PM -->
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(../../xhibit:SittingAt,1,5)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:when>
                                <!-- Handle SittingAt data 9:00:00 -->
                                <xsl:when test="string-length(../../xhibit:SittingAt)=7">
                                    <!-- call template to postfix AM or PM -->
                                    <xsl:call-template name="FormatTime">
                                        <xsl:with-param name="input">
                                            <xsl:value-of select="substring(../../xhibit:SittingAt,1,4)"/>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:if>
                    </xsl:when>
                    <!-- If Sitting Priority is F then display the hearing date only -->
                    <xsl:when test="../../xhibit:SittingPriority= 'F'">
                        <xsl:call-template name="displayDayDate">
                            <xsl:with-param name="input" select="xhibit:HearingDetails/xhibit:HearingDate"/>
                            <xsl:with-param name="language" select="$language"/>
                        </xsl:call-template>
                    </xsl:when>
                    <!-- If Sitting Priority is R then display nothing -->
                    <xsl:when test="../../xhibit:SittingPriority= 'R'"/>
                </xsl:choose>
            </xsl:variable>
            <fo:block space-before="10pt" text-decoration="underline">
                <fo:inline text-decoration="underline">
                    <xsl:value-of select="$HearingString"/>
                </fo:inline>
            </fo:block>
            <!-- display the time marking note -->
            <xsl:if test="xhibit:TimeMarkingNote">
                <fo:block>
                    <xsl:call-template name="DisplayTimeMarking">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </xsl:if>
            <fo:block space-after="10pt"/>
            <fo:block font-weight="bold">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="xhibit:HearingDetails/xhibit:HearingDescription"/>
                </xsl:call-template>
            </fo:block>
            <xsl:apply-templates select="xhibit:DefendantSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
            <fo:block space-after="10pt"/>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display the case and defendant information -->
    <xsl:template match="xhibit:DefendantSummaries">
        <xsl:param name="language"/>
        <!-- BUSINESS RULE: Only display if letter not to Prosecution -->
        <!-- Also handle scenario of reserved cases which have a different xpath -->
        <xsl:variable name="LetterType">
            <xsl:choose>
                <xsl:when test="../../../xhibit:ReserveListSummary">
                    <xsl:choose>
                        <xsl:when test="../../../xhibit:Recipient/xhibit:Defendant">Defendant</xsl:when>
                        <xsl:when test="../../../xhibit:Recipient/xhibit:Solicitor">Solicitor</xsl:when>
                        <xsl:when test="../../../xhibit:Recipient/xhibit:Prosecution">Prosecution</xsl:when>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Defendant">Defendant</xsl:when>
                        <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Solicitor">Solicitor</xsl:when>
                        <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Prosecution">Prosecution</xsl:when>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="orgname">
            <xsl:if test="../xhibit:ProsecutionSummary/xhibit:OrganisationName">
			<xsl:call-template name="toUpper">
                <xsl:with-param name="content"  select="../xhibit:ProsecutionSummary/xhibit:OrganisationName"/>
                </xsl:call-template>
            </xsl:if>                
        </xsl:variable>
		<fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="30mm"/>
            <!-- Case Number -->
            <fo:table-column column-width="150mm"/>
            <!-- Defendant and sol/pros details -->
            <fo:table-body font-weight="normal">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:value-of select="../xhibit:CaseNumber"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:for-each select="xhibit:DefendantSummary">
                                <xsl:variable name="ptiurn">
                                    <xsl:if test="xhibit:PTIURN">
                                        <xsl:value-of select="xhibit:PTIURN"/>
                                    </xsl:if>                
                                </xsl:variable>
                                <fo:table table-layout="fixed">
                                    <fo:table-column column-width="120mm"/>
                                    <!-- Defendant Name details -->
                                    <fo:table-column column-width="30mm"/>
                                    <!-- Solicitor Defendant Ref or Prosecuting Reference -->
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <!--<xsl:if test="xhibit:CustodyStatus = 'In custody' or xhibit:CustodyStatus = 'On remand'">
                                                        <xsl:text>* </xsl:text>
                                                    </xsl:if>-->
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
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <!-- BUSINESS RULE : If letter to solicitor then display defendant sol ref or pros sol ref, if letter to prosecution display prosecuting reference, if letter to defendant display nothing -->
                                                <fo:block>
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
                                                        <xsl:when test="$LetterType='Prosecution'">
															<!-- display PTIURN value -->
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
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </xsl:for-each>
                            <xsl:if test="$LetterType != 'Prosecution'">
                                <fo:block>
                                    <!-- Hold no of defendants text -->
                                    <xsl:variable name="Defendants">
                                        <xsl:choose>
                                            <xsl:when test="../xhibit:NumberOfDefendants">
                                                <xsl:variable name="DefsOnDLL">
                                                    <xsl:value-of select="count(../xhibit:DefendantSummaries/xhibit:DefendantSummary)"/>
                                                </xsl:variable>
                                                <xsl:value-of select="../xhibit:NumberOfDefendants - $DefsOnDLL"/>
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
                                </fo:block>
                            </xsl:if>
                            <!-- BUSINESS RULE: If letter for Solicitor then display prosecuting organisation name -->
                            <xsl:if test="$LetterType = 'Solicitor'">
                                <fo:block>
                                    <xsl:variable name="SummaryType">
                                        <xsl:value-of select="../@type"/>
                                    </xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="$SummaryType='Prosecution'">
                                            <!--<xsl:value-of select="../xhibit:ProsecutionSummary/xhibit:OrganisationName"/>-->
                                        </xsl:when>
                                        <xsl:when test="$SummaryType='Defendant'">
                                            <!--<xsl:choose>
                                                <xsl:when test="../../../xhibit:ReserveListSummary">
                                                    <xsl:choose>
                                                        <xsl:when test="../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Person">
                                                            <xsl:value-of select="../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename"/>
                                                            <xsl:text> </xsl:text>
                                                            <xsl:value-of select="../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname"/>
                                                        </xsl:when>
                                                        <xsl:when test="../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Organisation">
                                                            <xsl:value-of select="../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationName"/>
                                                        </xsl:when>
                                                    </xsl:choose>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:choose>
                                                        <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Person">
                                                            <xsl:value-of select="../../../../../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameForename"/>
                                                            <xsl:text> </xsl:text>
                                                            <xsl:value-of select="../../../../../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Person/xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname"/>
                                                        </xsl:when>
                                                        <xsl:when test="../../../../../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Organisation">
                                                            <xsl:value-of select="../../../../../../../xhibit:Recipient/xhibit:Solicitor/xhibit:Party/xhibit:Organisation/xhibit:OrganisationName"/>
                                                        </xsl:when>
                                                    </xsl:choose>
                                                </xsl:otherwise>
                                            </xsl:choose>-->
                                        </xsl:when>
                                    </xsl:choose>
                                </fo:block>
                            </xsl:if>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
		</fo:block>
        <fo:block space-before="10pt" font-weight="bold">
            <xsl:if test="../xhibit:ListNote">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="../xhibit:ListNote"/>
                </xsl:call-template>            
            </xsl:if>
        </fo:block>
    </xsl:template>
    <!-- Display reserved list summary details -->
    <xsl:template match="xhibit:ReserveListSummary">
        <xsl:param name="language"/>
        <fo:block font-weight="bold" space-before="10pt">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Reserve Case(s)'"/>
            </xsl:call-template>
        </fo:block>
        <xsl:for-each select="xhibit:HearingSummary">
            <!-- display the time marking note -->
            <xsl:if test="xhibit:TimeMarkingNote">
                <fo:block>
                    <xsl:call-template name="DisplayTimeMarking">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </xsl:if>
            <fo:block space-after="10pt"/>
            <fo:block font-weight="bold">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="xhibit:HearingDetails/xhibit:HearingDescription"/>
                </xsl:call-template>
            </fo:block>
            <xsl:apply-templates select="xhibit:DefendantSummaries">
                <xsl:with-param name="language" select="$language"/>
            </xsl:apply-templates>
            <fo:block space-after="10pt"/>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display standard FLL text -->
    <xsl:template name="DefaultFLLText">
        <xsl:param name="language"/>
        <fo:block font-family="Courier">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'The undermentioned cases have been included in the above list for hearing as shown. If it becomes necessary to alter a date of hearing, you will be notified of the change as soon as possible. Please inform the Listing Officer immediately of any matters likely to affect the listing of the case.'"/>
            </xsl:call-template>
            <fo:block space-after="10pt"/>
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Cases which have been designated as reserve cases are liable to be listed at short notice on any day during the period covered by the list.'"/>
            </xsl:call-template>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
        </fo:block>
    </xsl:template>
    
    <xsl:template name="DisplayTimeMarking">
        <xsl:param name="language"/>
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
    </xsl:template>
</xsl:stylesheet>
