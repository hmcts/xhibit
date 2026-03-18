<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
	<xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
	<!-- ========================= -->
	<!-- root element: data -->
	<!-- ========================= -->
	<xsl:template match="data">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body margin-top="7cm"/>
					<fo:region-before extent="7cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="start" font-size="10pt" font-family="serif" line-height="1em + 2pt">
						<fo:block font-family="Helvetica" font-size="13pt">
							<fo:block font-size="18pt" font-weight="bold">                
								The <xsl:value-of select="courtprefix"/>
							</fo:block>
							at  
							<xsl:value-of select="courtname"/>
						</fo:block>
					</fo:block>
					<fo:block space-after="8pt" text-align="center" font-size="15pt" font-weight="bold">
						Skeleton Schedule
					</fo:block>
					<fo:block>
					<fo:table width="100%" space-after="2mm" table-layout="fixed">
		                        <!-- Four columns of 25% each -->
		                        <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
		                        <fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
		                        <fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
		                        <fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="case"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="judge"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="defendants"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="estimatedcaseduration"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="casevalue"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="judgevalue"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm" hyphenate="true" language="en">
										<xsl:value-of select="defendantvalue"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width=".1mm">
									<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
										<xsl:value-of select="durationvalue"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block/>
					<xsl:for-each select="days">
						<fo:block>
						<fo:table width="100%" table-layout="fixed">
							<fo:table-column/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width=".1mm">
										<fo:block font-size="12pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                 				          	 	<xsl:value-of select="../weeknumber"/>
                                            					<xsl:value-of select="weeknumber"/>
											<xsl:value-of select="../day"/>
											<xsl:value-of select="daynumber"/>
										</fo:block>
										<fo:table width="100%" table-layout="fixed">
											<fo:table-column/>
											<fo:table-column/>
											<fo:table-column/>
											<fo:table-column/>
											<fo:table-column/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell border-style="solid" border-width=".1mm">
														<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
															<xsl:value-of select="../sessiontype"/>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell border-style="solid" border-width=".1mm">
														<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
															<xsl:value-of select="../name"/>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell border-style="solid" border-width=".1mm">
														<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
															<xsl:value-of select="../status"/>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell border-style="solid" border-width=".1mm">
														<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
															<xsl:value-of select="../type"/>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell border-style="solid" border-width=".1mm">
														<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
															<xsl:value-of select="../expected"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:for-each select="morningsessions">
													<fo:table-row>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="sessiontype"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm" hyphenate="true" language="en">
																<xsl:value-of select="name"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="status"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="type"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="expectedtime"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:for-each>
												<xsl:choose>
													<xsl:when test="morningnotes">
														<fo:table-row>
															<fo:table-cell number-columns-spanned="5" border-style="solid" border-width=".1mm">
																<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																	<xsl:value-of select="../notes"/>
																	<xsl:value-of select="morningnotes"/>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</xsl:when>
												</xsl:choose>
												<xsl:for-each select="afternoonsessions">
													<fo:table-row>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="sessiontype"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm" hyphenate="true" language="en">
																<xsl:value-of select="name"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="status"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="type"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-style="solid" border-width=".1mm">
															<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																<xsl:value-of select="expectedtime"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:for-each>
												<xsl:choose>
													<xsl:when test="afternoonnotes">
														<fo:table-row>
															<fo:table-cell number-columns-spanned="5" border-style="solid" border-width=".1mm">
																<fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
																	<xsl:value-of select="../notes"/>
																	<xsl:value-of select="afternoonnotes"/>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</xsl:when>
												</xsl:choose>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:block>
					</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
