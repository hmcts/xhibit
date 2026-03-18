<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" xmlns:fo="http://www.w3.org/1999/XSL/Format"  version="1.0">

	<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>

    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>

    <!-- Page Sequence Setup and Display the Warned List Letter Info -->
    <xsl:template name="DisplayWarnedListLetter">
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
                                        <xsl:text>       </xsl:text>
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
        <!-- Can be a Defendant, Solicitor or Prosecution -->
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
                                        <fo:block font-family="Courier" font-weight="normal">
                                            <xsl:call-template name="getValue">
                                                <xsl:with-param name="language" select="$language"/>
                                                <xsl:with-param name="key" select="'c/o The Governor'"/>
                                            </xsl:call-template>                                        
                                        </fo:block>
                                        <fo:block font-family="Courier" font-weight="normal">
                                            <xsl:if test="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID">
                                                <xsl:value-of select="xhibit:Defendant/xhibit:PrisonLocation/@PrisonID"/>
                                                <xsl:text> </xsl:text>
                                                <xsl:call-template name="getValue">
                                                    <xsl:with-param name="language" select="$language"/>
                                                    <xsl:with-param name="key" select="substring-after(xhibit:Defendant/xhibit:PrisonLocation/xhibit:Location,' ')"/>
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
                                        <fo:block font-family="Courier"  font-weight="normal">
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
        <!-- Rule for displaying the Warned List Title Details -->
        <fo:block font-family="Courier" line-height="30pt" text-align="center" font-weight="bold">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Criminal Warned List for'"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="../xhibit:ListHeaderSummary/xhibit:Duration">
                    <xsl:choose>
                        <xsl:when test="../xhibit:ListHeaderSummary/xhibit:Duration &lt;=7">
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
        <!--CREST FIXED TEXT-->
        <fo:block font-family="Courier" space-after="11pt">
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
                <xsl:when test="../xhibit:WarnedListDetail/xhibit:DeadLineDate">
                    <xsl:choose>
                        <xsl:when test="../xhibit:WarnedListDetail/xhibit:DeadlineTime">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'no later than'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="../xhibit:WarnedListDetail/xhibit:DeadlineTime"/>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'on'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>
                                <xsl:call-template name="displayDate">
                                    <xsl:with-param name="input" select="../xhibit:WarnedListDetail/xhibit:DeadLineDate"/>
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </fo:inline>
                        </xsl:when>
                        <xsl:otherwise>
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'no later than'"/>
                                </xsl:call-template>
                                <xsl:text> </xsl:text>                            
                                <xsl:call-template name="displayDate">
                                    <xsl:with-param name="input" select="../xhibit:WarnedListDetail/xhibit:DeadLineDate"/>
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </fo:inline>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <fo:inline font-weight="bold">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'immediately'"/>
                        </xsl:call-template>                    
                    </fo:inline>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
        <!-- Call template to display Warned List Details -->
        <xsl:apply-templates select="../xhibit:WarnedListDetail">
            <xsl:with-param name="language" select="$language"/>
        </xsl:apply-templates>
        <!-- Call template to check and display case and defendant details -->
        <xsl:apply-templates select="../xhibit:CourtListSummaries">
            <xsl:with-param name="language" select="$language"/>
        </xsl:apply-templates>
    </xsl:template>
    <!-- Template to display CourtList info -->
    <xsl:template match="xhibit:CourtListSummaries">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:CourtListSummary">
            <!-- Display With Fixed Date Details -->
            <xsl:if test="count(xhibit:WithFixedDateSummary) > 0">
                <fo:block space-after="10pt">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Fixed'"/>
                    </xsl:call-template>                
                    <xsl:for-each select="xhibit:WithFixedDateSummary">
                        <xsl:for-each select="xhibit:FixtureSummary/xhibit:CaseSummaries/xhibit:CaseSummary">
                            <fo:block font-weight="bold" space-before="10pt">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="xhibit:Hearing/xhibit:HearingDescription"/>
                                </xsl:call-template>
                            </fo:block>
                            <xsl:apply-templates select=".">
                                <xsl:with-param name="type">with</xsl:with-param>
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:apply-templates>
                        </xsl:for-each>
                    </xsl:for-each>
                </fo:block>
            </xsl:if>
            <xsl:if test="count(xhibit:WithoutFixedDateSummary) > 0 and count(xhibit:WithFixedDateSummary) > 0">
                <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
            </xsl:if>
            <!-- Display Without Fixed Date Details -->
            <xsl:if test="count(xhibit:WithoutFixedDateSummary) > 0">
                <fo:block space-after="10pt">
					<fo:block>
                    <fo:table table-layout="fixed">
                        <fo:table-column column-width="40mm"/>
                        <fo:table-column column-width="75mm"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:call-template name="getValue">
                                            <xsl:with-param name="language" select="$language"/>
                                            <xsl:with-param name="key" select="'Warned for'"/>
                                        </xsl:call-template>
                                        <xsl:text> : </xsl:text>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="left">
                                    <fo:block>
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
                                                    <fo:block/>
                                                </xsl:for-each>
                                            </xsl:if>
                                        </xsl:for-each>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
					</fo:block>
                    <xsl:for-each select="xhibit:WithoutFixedDateSummary">
                        <xsl:for-each select="xhibit:FixtureSummary/xhibit:CaseSummaries/xhibit:CaseSummary">
                            <fo:block font-weight="bold" space-before="10pt">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="xhibit:Hearing/xhibit:HearingDescription"/>
                                </xsl:call-template>                            
                            </fo:block>
                            <xsl:apply-templates select=".">
                                <xsl:with-param name="type">without</xsl:with-param>
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:apply-templates>
                        </xsl:for-each>
                    </xsl:for-each>
                </fo:block>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display the case information -->
    <xsl:template match="xhibit:CaseSummary">
        <xsl:param name="type"/>
        <xsl:param name="language"/>
		<fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="155mm"/>
            <fo:table-body font-family="Courier" font-weight="normal">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:value-of select="xhibit:CaseNumber"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
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
                                <fo:table table-layout="fixed">
                                    <fo:table-column column-width="5mm"/>
                                    <fo:table-column column-width="115mm"/>
                                    <fo:table-column column-width="35mm"/>
                                    <fo:table-body font-family="Courier" font-weight="normal">
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:if test="xhibit:CustodyStatus = 'In custody' or xhibit:CustodyStatus = 'On remand'">
                                                        <xsl:text>* </xsl:text>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block>
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
                                                <fo:block>
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
														<!-- PTIURN value is displayed when the prosecution is of type: CROWN PROSECUTION SERVICE /CPS -->
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
                            <!-- BUSINESS RULE: Only display if letter not to Prosecution -->
                            <xsl:if test="$LetterType != 'Prosecution'">
                                <fo:block>
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
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="150mm"/>
                                            <fo:table-body font-family="Courier" font-weight="normal">
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block/>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
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
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </xsl:if>
                                </fo:block>
                            </xsl:if>
                            <!-- BUSINESS RULE: If letter for Solicitor then display prosecuting organisation name -->
                            <xsl:if test="$LetterType = 'Solicitor'">
                                <fo:block>
                                    <xsl:choose>
                                        <xsl:when test="@type='Defendant'">
                                        </xsl:when>
                                        <xsl:when test="@type='Prosecution'">
                                            <!--<xsl:value-of select="xhibit:ProsecutionSummary/xhibit:OrganisationName"/> -->
                                        </xsl:when>
                                    </xsl:choose>
                                </fo:block>
                            </xsl:if>
                            <fo:block font-weight="bold">
                                <!-- only display if there is a fixed date and is within the withfixeddate structure -->
                                <xsl:if test="../../xhibit:FixedDate and $type = 'with'">
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="150mm"/>
                                        <fo:table-body font-family="Courier" font-weight="normal">
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block font-weight="bold">
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
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </xsl:if>
                            </fo:block>
                            <xsl:if test="../../xhibit:Notes">
                                <fo:block font-family="Courier" font-weight="bold">
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="150mm"/>
                                        <fo:table-body font-family="Courier" font-weight="normal">
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block font-weight="bold">
                                                        <xsl:call-template name="getValue">
                                                            <xsl:with-param name="language" select="$language"/>
                                                            <xsl:with-param name="key" select="../../xhibit:Notes"/>
                                                        </xsl:call-template>                                                    
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block>
                            </xsl:if>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
		</fo:block>
    </xsl:template>
    <!-- Display Warned List Details -->
    <xsl:template match="xhibit:WarnedListDetail">
        <xsl:param name="language"/>
        <xsl:for-each select="xhibit:Text">
            <fo:block font-family="Courier">
                <xsl:choose>
                    <xsl:when test="contains(.,'---------------------')">
                        <xsl:value-of select="substring-before(.,'---------------------')"/>
                        <fo:block/>
                        <xsl:value-of select="'---------------------'"/>
                        <xsl:value-of select="substring-after(.,'---------------------')"/>
                    </xsl:when>
                    <xsl:otherwise>
						<xsl:call-template name="format-text-for-wrapping">
							<xsl:with-param name="str" select="."/>
						</xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
        </xsl:for-each>
        <fo:block space-before="8pt" font-family="Courier">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'* denotes a defendant in custody.'"/>
            </xsl:call-template>        
        </fo:block>
        <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
    </xsl:template>
</xsl:stylesheet>