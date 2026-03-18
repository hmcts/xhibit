<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template name="RSIT">
			<xsl:for-each select="./rsit-values">
				<fo:block break-before="page"/>
				
				<fo:table border="none" table-layout="fixed">
					<fo:table-column column-number="1" column-width="80px"/>
					<fo:table-column column-number="2" column-width="53px"/>
					<fo:table-column column-number="3" column-width="53px"/>
					<fo:table-column column-number="4" column-width="53px"/>
					<fo:table-column column-number="5" column-width="53px"/>
					<fo:table-column column-number="6" column-width="53px"/>
					<fo:table-column column-number="7" column-width="53px"/>
					<fo:table-column column-number="8" column-width="53px"/>
					<fo:table-column column-number="9" column-width="53px"/>
					<fo:table-column column-number="10" column-width="53px"/>
					
					<fo:table-header>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="10" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center">
									<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court_site_name"/>
								</fo:block>
								
								<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
									<xsl:text>&#xD;&#xA;</xsl:text>
									<xsl:text>Crown Court sitting times for </xsl:text><xsl:value-of select="//weekdate"/>
									<xsl:text>&#xD;&#xA;</xsl:text>
									<xsl:text>&#xD;&#xA;</xsl:text>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Court Room No</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Monday</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Tuesday</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Wednesday</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Thursday</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Friday</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Saturday</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Total Hours</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="normal" font-size="9pt">
									<xsl:text>Total Days</xsl:text>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
						
					<fo:table-body>
						<xsl:for-each select="./rooms">
						
							<xsl:variable name="roomnumber">
								<xsl:value-of select="./court_room_no"/>
							</xsl:variable>
						
							<fo:table-row border-style="none" space-before.optimum="10pt">
								<fo:table-cell number-columns-spanned="10" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
									<xsl:call-template name="horizontalLine" />
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row border-style="none" space-before.optimum="10pt">
								<fo:table-cell number-columns-spanned="10" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							
							<xsl:for-each select="./times">
								<fo:table-row border-style="none" space-before.optimum="10pt" keep-together.within-column="always">
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
										<fo:block font-weight="normal">
											<xsl:choose>
												<xsl:when test="./am_pm = 'pm'">
													<xsl:value-of select="$roomnumber"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:text>&#xD;&#xA;</xsl:text>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
									<xsl:if test="./am_pm != 'Total'">
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:value-of select="./am_pm"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./monday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./tuesday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./wednesday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./thursday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./friday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./saturday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:value-of select="./total_hours"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="normal">
												<xsl:value-of select="./total_days"/>
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<xsl:if test="./am_pm = 'Total'">
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block>
												<fo:leader/>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:value-of select="./am_pm"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="bold">
													<xsl:text>_____</xsl:text>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./monday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="bold">
													<xsl:text>_____</xsl:text>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./tuesday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="bold">
													<xsl:text>_____</xsl:text>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./wednesday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="bold">
													<xsl:text>_____</xsl:text>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./thursday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="bold">
													<xsl:text>_____</xsl:text>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./friday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block font-weight="bold">
													<xsl:text>_____</xsl:text>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./saturday"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block>
												<fo:leader/>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:call-template name="convertMinutesToHours">
													<xsl:with-param name="toconvert"><xsl:value-of select="./total_hours"/></xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
											<fo:block>
												<fo:leader/>
											</fo:block>
											<fo:block font-weight="normal">
												<xsl:value-of select="./total_days"/>
											</fo:block>
										</fo:table-cell>
									</xsl:if>
								</fo:table-row>	
							</xsl:for-each>
						</xsl:for-each>
						
						<fo:table-row border-style="none" space-before.optimum="10pt">
							<fo:table-cell number-columns-spanned="10" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<xsl:call-template name="horizontalLine" />
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row border-style="none" space-before.optimum="10pt">
							<fo:table-cell number-columns-spanned="2" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:text>Total Hours Per Day</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./monday"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./tuesday"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./wednesday"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./thursday"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./friday"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./saturday"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:call-template name="convertMinutesToHours">
										<xsl:with-param name="toconvert"><xsl:value-of select="./total_hours"/></xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="12pt">
								<fo:block font-weight="normal">
									<xsl:value-of select="./total_days"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
					</fo:table-body>
				</fo:table>
		</xsl:for-each>  
	</xsl:template>
	
	<xsl:template name="convertMinutesToHours">
		<xsl:param name="toconvert"/>
		<xsl:value-of select='concat(
			format-number(floor($toconvert div 60), "0."),
			format-number(floor($toconvert mod 60), "00"))'/>
	</xsl:template>
	
	<xsl:template name="horizontalLine">
		<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold">									
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>__________________________________________________________________________________</xsl:text>
			<xsl:text>&#xD;&#xA;</xsl:text>
		</fo:block>
	</xsl:template>
	
</xsl:stylesheet>			