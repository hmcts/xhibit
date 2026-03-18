<?xml version="1.0" encoding="utf-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xml.apache.org/fop/extensions" version="1.0">
    <!-- Import data file which hold the hard coded text in the different languages -->
    <xsl:variable name="data" select="document('translation.xml')"/>
    <!-- Default Language -->
    <xsl:variable name="DefaultLang">en</xsl:variable>
    <!-- Include the Translations Template -->
    <xsl:include href="translation.xsl"/>
	<xsl:include href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
    <!-- Template to set up the page [FO Only] -->
    <xsl:template name="PageSetUp">
        <fo:layout-master-set>
            <!-- set up page for first page -->
            <fo:simple-page-master master-name="first" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                <fo:region-body margin-top="45mm" margin-bottom="23mm"/>
                <fo:region-before extent="45mm" region-name="header-first"/>
                <fo:region-after extent="23mm"/>
            </fo:simple-page-master>
            <!-- set up page for rest of pages -->
            <fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                <fo:region-body margin-top="25mm" margin-bottom="23mm"/>
                <fo:region-before extent="25mm" region-name="header-rest"/>
                <fo:region-after extent="23mm"/>
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
    <!-- Set up global Variables -->
    <xsl:variable name="initversion" select="//cs:ListHeader/cs:Version"/>
    <xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate"/>
    <xsl:variable name="endDate" select="//cs:ListHeader/cs:EndDate"/>
    <xsl:variable name="firstCourtHouseCode">
        <xsl:value-of select="cs:WarnedList/cs:CourtLists/cs:CourtList[1]/cs:CourtHouse/cs:Description"/>
    </xsl:variable>
	<xsl:variable name="nl"><xsl:text>
	</xsl:text></xsl:variable>
	<xsl:template match="br">
		<xsl:value-of select="'&#x2028;'" />
	</xsl:template>
    <!-- top level match -->
    <xsl:template name="DisplayWarnedList">
        <xsl:param name="language"/>
        <fo:page-sequence master-reference="document" initial-page-number="1" force-page-count="no-force">
            <!-- Display info for header on first page -->
            <fo:static-content flow-name="header-first">
                <fo:block text-align="left">
                    <xsl:apply-templates select="cs:WarnedList/cs:CrownCourt">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:apply-templates>
                </fo:block>
            </fo:static-content>
            <!-- Display info for all headers apart from the first -->
            <fo:static-content flow-name="header-rest">
                <fo:block text-align-last="left">
                    <xsl:call-template name="HeaderRest">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </fo:static-content>
            <!-- Footer Info -->
            <fo:static-content flow-name="xsl-region-after">
                <!-- display the footer information -->
                <fo:block>
                    <xsl:call-template name="listfooter">
                        <xsl:with-param name="language" select="$language"/>
                    </xsl:call-template>
                </fo:block>
            </fo:static-content>
            <!-- Main Body for document-->
            <fo:flow flow-name="xsl-region-body">
                <fo:block>
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
                </fo:block>
                <fo:block id="{$language}"/>
            </fo:flow>
        </fo:page-sequence>
    </xsl:template>
    <!--
         ***********************
        TEMPLATE MATCH 
        ***********************
    -->
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
        <fo:block>
        <fo:table table-layout="fixed" font-size="11pt">
            <fo:table-column column-width="70mm"/>
            <fo:table-column column-width="120mm"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:call-template name="getValue">
                                <xsl:with-param name="language" select="$language"/>
                                <xsl:with-param name="key" select="'The following cases are warned for '"/>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:for-each select="cs:CourtList/cs:CourtHouse/cs:Description[. = $firstCourtHouseCode]">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="substring-after(../cs:CourtHouseName,'at ')"/>
                                </xsl:call-template>
                                <xsl:if test="position() != last()">
                                    <fo:block/>
                                </xsl:if>
                            </xsl:for-each>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        </fo:block>
        <fo:block space-after="10pt">
            <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
        </fo:block>
        <xsl:if test="$with > 0">
            <fo:block font-weight="bold" space-after="10pt" text-decoration="underline">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Fixtures'"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
        <xsl:for-each select="./cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithFixedDate[not(self::*/@HearingType=preceding::cs:WithFixedDate/@HearingType)]">
			<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/@HearingType" order="ascending" data-type="number"/>
            <xsl:variable name="hearingType" select="./@HearingType"/>
			
            <!-- display the case details -->
            <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithFixedDate/cs:Fixture/cs:Cases/cs:Case [../../../@HearingType = $hearingType]">
				<xsl:sort select="substring(cs:CaseNumber, 1, 1)" order="descending"/>
				<xsl:sort select="substring(cs:CaseNumber, 2)" order="ascending" data-type="number"/>
				<xsl:if test="position()=1">
                    <!-- Display the hearing description -->
                    <fo:block font-size="12pt" font-weight="bold" space-before="10pt">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
                        </xsl:call-template>
                    </fo:block>
                </xsl:if>
                <fo:block>
                <fo:table table-layout="fixed" table-omit-header-at-break="false">
                    <fo:table-column column-width="190mm"/>
                    <!-- set up table header -->
                    <fo:table-header>
                        <fo:table-row>
                            <fo:table-cell>
								
								<!-- New way of doing continued label pt 1 -->
								<fo:block font-size="10pt" font-weight="bold" margin-bottom="12pt">
									<fo:retrieve-table-marker retrieve-class-name="mc2" retrieve-position-within-table="first-starting" retrieve-boundary-within-table="table" />
								</fo:block>
								<!-- End of new continued label pt 1 -->
								
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
						<fo:table-row>
                            <fo:table-cell>
							
								<!-- New way of doing continued label pt 2 -->
								<fo:block>
									<fo:marker marker-class-name="mc2"></fo:marker>
								</fo:block>
								<fo:block font-size="10pt" font-weight="bold">
									<fo:marker marker-class-name="mc2">
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="'Fixtures - Continued; '"/>
										</xsl:call-template>
										<xsl:text> - </xsl:text>
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
										</xsl:call-template>
										<xsl:text> - </xsl:text>
										<xsl:value-of select="cs:CaseNumber"/>
										<xsl:text> - </xsl:text>
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="'Continued'"/>
										</xsl:call-template>
                                    </fo:marker>
                                </fo:block>
								<!-- End of new continued label pt 2 -->
								
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <xsl:call-template name="case">
                                    <xsl:with-param name="language" select="$language"/>
                                </xsl:call-template>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
                </fo:block>
            </xsl:for-each>
        </xsl:for-each>
        <xsl:if test="$with &gt; 0">
            <fo:block space-after="10pt" break-after="page" />
        </xsl:if>
        <xsl:for-each select="./cs:CourtList[cs:CourtHouse/cs:Description =$firstCourtHouseCode]/cs:WithoutFixedDate [not(self::*/@HearingType=preceding::cs:WithoutFixedDate/@HearingType)]">
			<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/@HearingType" order="ascending" data-type="number"/>
			<xsl:variable name="hearingType" select="./@HearingType"/>
            <!-- display the case details -->
            <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $firstCourtHouseCode]/cs:WithoutFixedDate/cs:Fixture/cs:Cases/cs:Case [../../../@HearingType = $hearingType]">
				<xsl:sort select="substring(cs:CaseNumber, 1, 1)" order="descending"/>
				<xsl:sort select="substring(cs:CaseNumber, 2)" order="ascending" data-type="number"/>

				<xsl:if test="position()=1">
                    <!-- Display the hearing description -->
                    <fo:block font-size="12pt" font-weight="bold" space-before="10pt">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
                        </xsl:call-template>
                    </fo:block>
                </xsl:if>
                <fo:block>
                <fo:table table-layout="fixed" table-omit-header-at-break="false">
                    <fo:table-column column-width="190mm"/>
                    <!-- set up table header -->
                    <fo:table-header>
                        <fo:table-row>
                            <fo:table-cell>
								<!-- New way of doing continued label pt 1 -->
								<fo:block font-size="10pt" font-weight="bold" margin-bottom="12pt">
									<fo:retrieve-table-marker retrieve-class-name="mc2" retrieve-position-within-table="first-starting" retrieve-boundary-within-table="table" />
								</fo:block>
								<!-- End of new continued label pt 1 -->
							</fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell>
							
								<!-- New way of doing continued label pt 2 -->
								<fo:block>
									<fo:marker marker-class-name="mc2"></fo:marker>
								</fo:block>
								<fo:block font-size="10pt" font-weight="bold">
									<fo:marker marker-class-name="mc2">
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
										</xsl:call-template>
										<xsl:text> - </xsl:text>
										<xsl:value-of select="cs:CaseNumber"/>
										<xsl:text> - </xsl:text>
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="'Continued'"/>
										</xsl:call-template>
                                    </fo:marker>
                                </fo:block>
								<!-- End of new continued label pt 2 -->
								
                            </fo:table-cell>
                        </fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:call-template name="case">
										<xsl:with-param name="language" select="$language"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
                    </fo:table-body>
                </fo:table>
                </fo:block>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
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
            <xsl:if test="$with > 0 or $withOut > 0">
                <fo:block break-before="page"/>
                <fo:block>
                <fo:table table-layout="fixed" font-size="11pt">
					<fo:table-column column-width="70mm"/>
					<fo:table-column column-width="120mm"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="'The following cases are warned for'"/>
									</xsl:call-template>
									<xsl:text>: </xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
									<xsl:for-each select="../../../cs:CourtList/cs:CourtHouse/cs:Description[. = $crtCode]">
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="substring-after(../cs:CourtHouseName,'at ')"/>
										</xsl:call-template>
										<xsl:if test="position() != last()">
											<fo:block/>
										</xsl:if>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
                </fo:block>
                <fo:block space-after="10pt">
                    <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
                </fo:block>
            </xsl:if>
            <xsl:if test="$with > 0">
                <fo:block font-weight="bold" space-after="10pt" text-decoration="underline">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="'Fixtures'"/>
                    </xsl:call-template>
                </fo:block>
            </xsl:if>
            <xsl:for-each select="../../../cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithFixedDate [not(self::*/@HearingType=preceding::cs:WithFixedDate[../cs:CourtHouse/cs:Description = $crtCode]/@HearingType)]">
				<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/@HearingType" order="ascending" data-type="number"/>
				<xsl:variable name="hearingType" select="./@HearingType"/>

                <!-- display the case details -->
                <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithFixedDate/cs:Fixture/cs:Cases/cs:Case [../../../@HearingType = $hearingType]">
					<xsl:sort select="substring(cs:CaseNumber, 1, 1)" order="descending"/>
					<xsl:sort select="substring(cs:CaseNumber, 2)" order="ascending" data-type="number"/>
					<xsl:if test="position()=1">
						<!-- Display the hearing description -->
						<fo:block font-size="12pt" font-weight="bold" space-before="10pt">
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
							</xsl:call-template>
						</fo:block>
					</xsl:if>
                    <fo:block>
                    <fo:table table-layout="fixed" table-omit-header-at-break="false">
                        <fo:table-column column-width="190mm"/>
                        <!-- set up table header -->
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell>

									<!-- New way of doing continued label pt 1 -->
									<fo:block font-size="10pt" font-weight="bold" margin-bottom="12pt">
										<fo:retrieve-table-marker retrieve-class-name="mc2" retrieve-position-within-table="first-starting" retrieve-boundary-within-table="table" />
									</fo:block>
									<!-- End of new continued label pt 1 -->
									
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
									<!-- New way of doing continued label pt 2 -->
									<fo:block>
										<fo:marker marker-class-name="mc2"></fo:marker>
									</fo:block>
									<fo:block font-size="10pt" font-weight="bold">
										<fo:marker marker-class-name="mc2">
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="'Fixtures - Continued; '"/>
											</xsl:call-template>
											<xsl:text> - </xsl:text>
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
											</xsl:call-template>
											<xsl:text> - </xsl:text>
											<xsl:value-of select="cs:CaseNumber"/>
											<xsl:text> - </xsl:text>
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="'Continued'"/>
											</xsl:call-template>
										</fo:marker>
									</fo:block>
									<!-- End of new continued label pt 2 -->
								
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
                                    <xsl:call-template name="case">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                </xsl:for-each>
            </xsl:for-each>
            <xsl:if test="$with >0">
                <fo:block space-after="10pt" break-after="page"/>
            </xsl:if>
            <xsl:for-each select="../../../cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithoutFixedDate [not(self::*/@HearingType=preceding::cs:WithoutFixedDate[../cs:CourtHouse/cs:Description = $crtCode]/@HearingType)]">
				<xsl:sort select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/@HearingType" order="ascending" data-type="number"/>
				<xsl:variable name="hearingType" select="./@HearingType"/>
                <!-- display the case details -->
                <xsl:for-each select="//cs:CourtList[cs:CourtHouse/cs:Description = $crtCode]/cs:WithoutFixedDate/cs:Fixture/cs:Cases/cs:Case [../../../@HearingType = $hearingType]">
					<xsl:sort select="substring(cs:CaseNumber, 1, 1)" order="descending"/>
					<xsl:sort select="substring(cs:CaseNumber, 2)" order="ascending" data-type="number"/>
					<xsl:if test="position()=1">
						<!-- Display the hearing description -->
						<fo:block font-size="12pt" font-weight="bold" space-before="10pt">
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
							</xsl:call-template>
						</fo:block>
					</xsl:if>
                    <fo:block>
                    <fo:table table-layout="fixed" table-omit-header-at-break="false">
                        <fo:table-column column-width="190mm"/>
                        <!-- set up table header -->
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell>
								
									<!-- New way of doing continued label pt 1 -->
									<fo:block font-size="10pt" font-weight="bold" margin-bottom="12pt">
										<fo:retrieve-table-marker retrieve-class-name="mc2" retrieve-position-within-table="first-starting" retrieve-boundary-within-table="table" />
									</fo:block>
									<!-- End of new continued label pt 1 -->
								
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
								
									<!-- New way of doing continued label pt 2 -->
									<fo:block>
										<fo:marker marker-class-name="mc2"></fo:marker>
									</fo:block>
									<fo:block font-size="10pt" font-weight="bold">
										<fo:marker marker-class-name="mc2">
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="cs:Hearing/cs:HearingDescription"/>
											</xsl:call-template>
											<xsl:text> - </xsl:text>
											<xsl:value-of select="cs:CaseNumber"/>
											<xsl:text> - </xsl:text>
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="'Continued'"/>
											</xsl:call-template>
										</fo:marker>
									</fo:block>
									<!-- End of new continued label pt 2 -->
								
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
                                    <xsl:call-template name="case">
                                        <xsl:with-param name="language" select="$language"/>
                                    </xsl:call-template>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <!-- Display Crown Court Details -->
    <xsl:template match="cs:CrownCourt">
        <xsl:param name="language"/>
        <fo:block font-size="10pt" text-align="right">
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
		<fo:block text-align="left" font-size="9pt">
			 <xsl:call-template name="displayDate">
                 <xsl:with-param name="input" select="//cs:DocumentID/cs:TimeStamp"/>
                 <xsl:with-param name="language" select="$language"/>
             </xsl:call-template>
		</fo:block>
		<xsl:variable name="reporttype">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Annotated Warned List'"/>
            </xsl:call-template>
        </xsl:variable>
        <fo:block text-align="center">
            <fo:block font-size="16pt" font-weight="bold">
                <xsl:variable name="theCC">
                    <xsl:text>The </xsl:text>
                    <xsl:value-of select="cs:CourtHouseType"/>
                </xsl:variable>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="$theCC"/>
                </xsl:call-template>
				<xsl:text> </xsl:text>
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'at'"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
				<xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="cs:Description"/>
                </xsl:call-template>
            </fo:block>
            <fo:block font-size="16pt" text-align="center" font-weight="bold">
                <xsl:value-of select="$reporttype"/>
				<fo:block text-align="center" font-weight="bold">
					<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'For the period '"/>
					</xsl:call-template>
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
				</fo:block>
                <fo:block font-size="12pt" text-align="center" font-weight="bold">
					 <xsl:text>Warned List Edition: </xsl:text>
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
                </fo:block>
            </fo:block>
			<fo:block font-size="12pt" text-align="center" font-weight="bold">
				(Notes Requested: 
				 <xsl:call-template name="getValue">
                        <xsl:with-param name="language" select="$language"/>
                        <xsl:with-param name="key" select="//cs:DocumentID/cs:DocumentInformation"/>
                 </xsl:call-template>
				 )
			</fo:block>
        </fo:block>
    </xsl:template>
    <!--
         ***********************
        TEMPLATE NAMES
        ***********************
    -->
    <!-- display the case details -->
    <xsl:template name="case">
        <xsl:param name="language"/>
        <fo:block space-after="12pt"/>
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
		<xsl:variable name="caseType">
            <xsl:value-of select="substring(cs:CaseNumber, 1, 1)"/>
        </xsl:variable>
		
        <!-- call template to display defendant details -->
        <xsl:call-template name="processdefendants">
            <xsl:with-param name="caseNumText" select="cs:CaseNumber"/>
            <xsl:with-param name="prosecuteRefText" select="$prosecutingref"/>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
        <fo:block>
        <fo:table table-layout="fixed" font-size="10pt">
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="165mm"/>
            <fo:table-body>
                <!-- display prosecuting organisation name -->
                <xsl:if test="(cs:Prosecution//cs:OrganisationName) and not(contains(cs:Prosecution//cs:OrganisationName,'Crown Prosecution Service'))">
                    <fo:table-row>
                        <fo:table-cell/>
                        <fo:table-cell>
                            <fo:block font-size="10pt">
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
                                <xsl:text>)</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="../../cs:FixedDate[not(. = '1900-01-01')]">
                    <fo:table-row>
                        <fo:table-cell/>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
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
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
				<!-- display default list notes -->
				<xsl:if test="../../cs:FixtureNotes/cs:Note">
					<fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
						<fo:table table-layout="fixed" font-size="9pt">
							<fo:table-column column-width="155mm"/>
							<fo:table-body>
								<xsl:for-each select="../../cs:FixtureNotes/cs:Note">
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="."/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell> 
									</fo:table-row>
								</xsl:for-each>
						</fo:table-body>
					</fo:table>
					</fo:table-cell>
				</fo:table-row>
				</xsl:if>
				<!-- display default list notes -->
				<xsl:if test="../../cs:DefaultNotes/cs:Note">
					<fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
						<fo:table table-layout="fixed" font-size="9pt">
							<fo:table-column column-width="155mm"/>
							<fo:table-body>
								<xsl:for-each select="../../cs:DefaultNotes/cs:Note">
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="."/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell> 
									</fo:table-row>
								</xsl:for-each>
						</fo:table-body>
					</fo:table>
					</fo:table-cell>
				</fo:table-row>
				</xsl:if>
				<xsl:if test="cs:MethodOfInstigation">
                    <fo:table-row>
                        <fo:table-cell/>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="'Time Est: '"/>
                                </xsl:call-template>
								<xsl:value-of select="cs:TrialTimeEstimate"/>
                                <xsl:text> Case = </xsl:text>
                                <xsl:value-of select="cs:MethodOfInstigation"/> 
								<xsl:text> old</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
				<!-- display any linked case details -->
				<xsl:if test="../../cs:LinkedCases">
					<fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:text>(</xsl:text>
								<xsl:call-template name="getValue">
									<xsl:with-param name="language" select="$language"/>
									<xsl:with-param name="key" select="'Linked with'"/>
								</xsl:call-template>
								<xsl:text>: </xsl:text>
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
							</fo:block>
						
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						 <fo:table table-layout="fixed" font-size="9pt">
							<fo:table-column column-width="35mm"/>
							<fo:table-column column-width="135mm"/>
							<fo:table-body>
								<!-- Required Judge -->
								<xsl:if test="cs:RequiredJudge">
									<fo:table-row>
										<fo:table-cell>
											 <fo:block font-size="10pt">
												<xsl:call-template name="getValue">
													<xsl:with-param name="language" select="$language"/>
													<xsl:with-param name="key" select="'Required Judge'"/>
												</xsl:call-template>
												<xsl:text>: </xsl:text>
											 </fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="cs:RequiredJudge"/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell> 
									</fo:table-row>
								</xsl:if>
								 <!-- Non Trial Case Charges -->
								 <xsl:if test="./cs:Defendants//cs:Charge and $caseType != 'T'">
									<xsl:for-each select="./cs:Defendants//cs:Charge">
										<fo:table-row>
											<fo:table-cell>
												 <xsl:if test="position() = 1">
													 <fo:block font-size="10pt">
														<xsl:call-template name="getValue">
															<xsl:with-param name="language" select="$language"/>
															<xsl:with-param name="key" select="'Charge(s)'"/>
														</xsl:call-template>
														<xsl:text>: </xsl:text>
													 </fo:block>
												</xsl:if>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:call-template name="format-text-for-wrapping">
														<xsl:with-param name="str" select="cs:OffenceStatement"/>
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>  
										</fo:table-row>
									</xsl:for-each>
								</xsl:if>
								<!-- Trial Case Charges -->
								<xsl:if test="./cs:AdditionalNotes">
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="getValue">
													<xsl:with-param name="language" select="$language"/>
													<xsl:with-param name="key" select="'Charge(s)'"/>
												</xsl:call-template>
												<xsl:text>: </xsl:text>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-size="10pt" white-space-collapse="false" white-space-treatment="preserve">
												<xsl:call-template name="format-text-for-wrapping2">
													<xsl:with-param name="str" select="cs:AdditionalNotes"/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>  
									</fo:table-row>
								</xsl:if>
								<xsl:if test="./cs:Hearing/cs:ListNote">
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="getValue">
													<xsl:with-param name="language" select="$language"/>
													<xsl:with-param name="key" select="'Highlight Note'"/>
												</xsl:call-template>
												<xsl:text>: </xsl:text>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./cs:Hearing/cs:ListNote"/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>  
									</fo:table-row>
								</xsl:if>
								<xsl:if test="./cs:Hearing/cs:InterpreterNote">
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="getValue">
													<xsl:with-param name="language" select="$language"/>
													<xsl:with-param name="key" select="'Interpreter Note'"/>
												</xsl:call-template>
												<xsl:text>: </xsl:text>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-size="10pt">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./cs:Hearing/cs:InterpreterNote"/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>  
									</fo:table-row>
								</xsl:if>
								 <!-- Display any Notes that may exist -->
								<xsl:if test="../../cs:Notes/cs:Note">
									<xsl:for-each select="../../cs:Notes/cs:Note">
										<fo:table-row>
											<fo:table-cell>
												 <xsl:if test="position() = 1">
													 <fo:block font-size="10pt">
														<xsl:call-template name="getValue">
															<xsl:with-param name="language" select="$language"/>
															<xsl:with-param name="key" select="'Other Notes'"/>
														</xsl:call-template>
														<xsl:text>: </xsl:text>
													 </fo:block>
												</xsl:if>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:call-template name="format-text-for-wrapping">
														<xsl:with-param name="str" select="."/>
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>  
										</fo:table-row>
									</xsl:for-each>
								</xsl:if>
								 <!-- Display any Non Available Dates that may exist -->
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="'Non-available Dates'"/>
											</xsl:call-template>
											<xsl:text>: </xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="./cs:AppealCaseDescription">
													<xsl:call-template name="getValue">
														<xsl:with-param name="language" select="$language"/>
														<xsl:with-param name="key" select="./cs:AppealCaseDescription"/>
													</xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="getValue">
														<xsl:with-param name="language" select="$language"/>
														<xsl:with-param name="key" select="'None Affecting List'"/>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>  
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
				</fo:table-row>
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
    <!-- display Header info for pages after the first -->
    <xsl:template name="HeaderRest">
        <xsl:param name="language"/>
        <xsl:variable name="reporttype">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'Annotated Warned List for'"/>
            </xsl:call-template>
        </xsl:variable>
        <fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="120mm"/>
            <fo:table-column column-width="30mm"/>
            <fo:table-column column-width="35mm"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="10pt">
                            <xsl:value-of select="$reporttype"/>
                            <xsl:text> </xsl:text>
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
                        </fo:block>
                        <fo:block font-size="10pt">
                            <xsl:variable name="satelliteCourtHouseNames">
                                <xsl:call-template name="getValue">
                                    <xsl:with-param name="language" select="$language"/>
                                    <xsl:with-param name="key" select="cs:WarnedList/cs:CrownCourt/cs:Description"/>
                                </xsl:call-template>
                            </xsl:variable>
                            <xsl:value-of select="$satelliteCourtHouseNames"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="10pt">
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
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="10pt" text-align="right">
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
        <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
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
        <fo:block>
            <fo:leader leader-pattern="rule" space-before.optimum="8pt" space-after.optimum="8pt" leader-length="100%"/>
        </fo:block>
        <fo:block font-size="8pt">
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
        </fo:block>
        <fo:block space-after="4pt"/>
        <fo:block>
        <fo:table table-layout="fixed">
            <fo:table-column column-width="160mm"/>
            <fo:table-column column-width="30mm"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="8pt">
                            <xsl:call-template name="publishDate">
                                <xsl:with-param name="language" select="$language"/>
                            </xsl:call-template>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="8pt">
                            <xsl:value-of select="cs:WarnedList/cs:ListHeader/cs:CRESTprintRef"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <xsl:if test="$language != $DefaultLang">
                    <fo:table-row font-size="7pt">
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
        <fo:block space-after="4pt"/>
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
    <!-- display defendant details -->
    <xsl:template name="processdefendants">
        <xsl:param name="caseNumText"/>
        <xsl:param name="prosecuteRefText"/>
        <xsl:param name="language"/>
        <fo:block>
        <fo:table table-layout="fixed" font-size="9pt">
            <fo:table-column column-width="17mm"/>
            <fo:table-column column-width="3mm"/>
            <fo:table-column column-width="90mm"/>
            <fo:table-column column-width="3mm"/>
            <fo:table-column column-width="55mm"/>
            <fo:table-column column-width="12mm"/>
            <fo:table-column column-width="5mm"/>
            <fo:table-body>
				<xsl:if test="not(cs:Defendants/cs:Defendant)">
					 <fo:table-row>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$caseNumText"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block>
								NO DEFENDANTS
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
                <xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name">
                    <xsl:variable name="asterisk">
                        <xsl:if test="../../cs:CustodyStatus = 'In custody' or ../../cs:CustodyStatus = 'On remand'">
                            <xsl:text>*</xsl:text>
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
						<xsl:if test="../cs:URN">
							<fo:block/>
							<xsl:value-of select="../cs:URN"/>
						</xsl:if>
						<fo:block/>
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
                        <!-- display additional defendant details, only if defendant is in custody -->
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
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="$caseNumText"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block text-align="center">
                                        <xsl:value-of select="$asterisk"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block hyphenate="true" language="en">
                                        <xsl:copy-of select="$defendant"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell/>
                                <fo:table-cell>
                                    <fo:block hyphenate="true" language="en">
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
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:if test="contains($prosecuteRefText,'Crown Prosecution Service')=false">
                                            <xsl:value-of select="$prosecuteRefText"/>
                                        </xsl:if>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2"/>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="../../cs:URN"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </xsl:when>
                        <xsl:otherwise>
                            <fo:table-row>
                                <fo:table-cell/>
                                <fo:table-cell>
                                    <fo:block text-align="center">
                                        <xsl:value-of select="$asterisk"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block hyphenate="true" language="en">
                                        <xsl:copy-of select="$defendant"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell/>
                                <fo:table-cell>
                                    <fo:block hyphenate="true" language="en">
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
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell/>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2"/>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="../../cs:URN"/>
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
    <!-- Display the published Details -->
    <xsl:template name="publishDate">
        <xsl:param name="language"/>
        <fo:block font-size="8pt">
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
        </fo:block>
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
        <fo:block space-after="10pt" text-align="center">
            <fo:leader leader-pattern="rule" space-after.optimum="12pt" leader-length="400mm"/>
        </fo:block>
        <fo:block start-indent="5mm" font-size="10pt">
            <xsl:call-template name="getValue">
                <xsl:with-param name="language" select="$language"/>
                <xsl:with-param name="key" select="'* Denotes a defendant in custody'"/>
            </xsl:call-template>
        </fo:block>
        <fo:block space-after="10pt">
            <fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
        </fo:block>
    </xsl:template>
</xsl:stylesheet>