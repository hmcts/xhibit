<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="LFIX">	
	<fo:table border="none" table-layout="fixed">
		<fo:table-column column-number="1" column-width="150px"/>
		<fo:table-column column-number="2" column-width="390px"/>
	
		<fo:table-body>
			<xsl:for-each select="./lfix-report-values">
				<fo:table-row border="none" space-before.optimum="12pt">
					<!-- Hearing Type -->
					<fo:table-cell number-columns-spanned="2" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block wrap-option="wrap" font-weight="bold">
							<xsl:value-of select="./hearingtype"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="./cases">
				  <xsl:for-each select="./fixtures">
				  
					<fo:table-row border="none" space-before.optimum="12pt">
						<!-- Case Number -->
						<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<xsl:if test="position()= 1">
								<fo:block wrap-option="wrap">
									<xsl:value-of select="./casenumber"/>
								</fo:block>
							</xsl:if>
							<xsl:if test="position()!= 1">
								<fo:block/>
							</xsl:if>
						</fo:table-cell>
						
						<!-- Defendant, Representative and Fixture Details -->
						<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<fo:table border="none" table-layout="fixed">
								<fo:table-column column-number="1" column-width="150px"/>
								<fo:table-column column-number="2" column-width="150px"/>
								<fo:table-column column-number="3" column-width="90px"/>
								<fo:table-body>
									<xsl:for-each select="./defendants">
										<fo:table-row border="none" space-before.optimum="12pt">
											<!-- Defendant Details -->
											<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
												<fo:block wrap-option="wrap">
													<xsl:call-template name="format-text-for-wrapping">
														<xsl:with-param name="str" select="./defendant"/>
													</xsl:call-template>
												</fo:block>
												<fo:block wrap-option="wrap">
													<xsl:value-of select="./gender"/><xsl:text> </xsl:text><xsl:value-of select="./dob"/>
												</fo:block>
											</fo:table-cell>
											<!-- Representative Details -->
											<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
												<fo:block wrap-option="wrap">
													<xsl:choose>
														<xsl:when test="normalize-space(./representative) != ''">
															<xsl:call-template name="format-text-for-wrapping">
																<xsl:with-param name="str" select="./representative"/>
															</xsl:call-template>
														</xsl:when>
														<xsl:otherwise>
															<xsl:text>NOT REPRESENTED</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
											   </fo:block>
											</fo:table-cell>
											<!-- PTI URN -->
											<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
												<fo:block wrap-option="wrap">
													<xsl:value-of select="./ptiurn"/>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
							
									<fo:table-row border="none" space-before.optimum="12pt">	
										<!-- Prosecutor and Fixture Details -->
										<fo:table-cell number-columns-spanned="3" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
											<fo:block wrap-option="wrap">
												<xsl:text>(Prosecutor: </xsl:text>
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./prosecutor"/>
												</xsl:call-template>
												<xsl:text>)</xsl:text>
											</fo:block>
											<fo:block wrap-option="wrap" font-weight="bold">
												<xsl:text>Fixed for </xsl:text><xsl:value-of select="./hearingdate"/>
												<xsl:text> at </xsl:text><xsl:value-of select="./hearingvenue"/>
											</fo:block>
											<fo:block wrap-option="wrap" font-weight="bold">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./notes"/>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
						
					</fo:table-row>	
						
				  </xsl:for-each>
				</xsl:for-each>   
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
</xsl:template>
</xsl:stylesheet>