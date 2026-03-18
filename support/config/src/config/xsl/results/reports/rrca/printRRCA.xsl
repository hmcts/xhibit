<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template name="RRCA">
		<xsl:for-each select="./rrca-summary-detail">
			<xsl:for-each select="./summary-detail">
				<xsl:if test="position()=1">
				
					<xsl:choose>
						<xsl:when test="./court_site_name = 'All'">
							<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" break-before="page"  linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="//court-name"/>
							</fo:block>
							<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>Trial cases outstanding at ending </xsl:text><xsl:value-of select="//weekmonthdate"/>
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>for administrative centre including all satellites</xsl:text>
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>&#xD;&#xA;</xsl:text>
							</fo:block>
						</xsl:when>
						<xsl:otherwise>
							<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" break-before="page"  linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court_site_name"/>
							</fo:block>
							<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>Trial cases outstanding at ending </xsl:text><xsl:value-of select="//weekmonthdate"/>
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>&#xD;&#xA;</xsl:text>
							</fo:block>
						</xsl:otherwise>
					</xsl:choose>

					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="180px"/>
						<fo:table-column column-number="2" column-width="100px"/>
						<fo:table-column column-number="3" column-width="100px"/>
						<fo:table-column column-number="4" column-width="100px"/>
						
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell  border-style="none" border-width="1px" text-align="center" border-collapse="collapse" padding="1mm">
									<fo:block font-weight="normal" font-size="9pt">
										<xsl:text>Waiting Time Between Date of Committal / Sent for Trial and end of stats period</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell  border-style="none" border-width="1px" text-align="center" border-collapse="collapse" padding="1mm">
									<fo:block font-weight="normal" font-size="9pt">
										<xsl:text>Number of Custody Cases</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell  border-style="none" border-width="1px" text-align="center" border-collapse="collapse" padding="1mm">
									<fo:block font-weight="normal" font-size="9pt">
										<xsl:text>Number of Non-Custody Cases</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell  border-style="none" border-width="1px" text-align="center" border-collapse="collapse" padding="1mm">
									<fo:block font-weight="normal" font-size="9pt">
										<xsl:text>Total</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
			
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell border-style="none">
									<fo:block font-weight="normal"/>
								 </fo:table-cell>
							</fo:table-row>
							<fo:table-row border="none">
								<fo:table-cell border-style="none">
									<fo:block font-weight="normal"/>
								 </fo:table-cell>
							</fo:table-row>
							<fo:table-row border="none">
								<fo:table-cell border-style="none">
									<fo:block font-weight="normal"/>
								 </fo:table-cell>
							</fo:table-row> 
							<fo:table-row border="none">
								<fo:table-cell border-style="none">
									<fo:block font-weight="normal"/>
								 </fo:table-cell>
							</fo:table-row> 
						</fo:table-body>
					</fo:table>
		
					<fo:block font-size="13pt" font-weight="normal">
						<xsl:text>&#xD;&#xA;</xsl:text>
						<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
						<xsl:text>&#xD;&#xA;</xsl:text>
					</fo:block>
			
					<fo:table border="none" table-layout="fixed" font-size="11pt">
						<fo:table-column column-number="1" column-width="180px"/>
						<fo:table-column column-number="2" column-width="100px"/>
						<fo:table-column column-number="3" column-width="100px"/>
						<fo:table-column column-number="4" column-width="100px"/>
							
						<fo:table-body>
							<xsl:for-each select="./bands">
								<fo:table-row border-style="none" space-before.optimum="12pt">
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" >
										<fo:block font-weight="normal">
											<xsl:if test="./age_band_code = 'Z'">
												<fo:block font-weight="bold">
													<xsl:text>_____________________________________________</xsl:text>
												</fo:block>
											</xsl:if>
											<xsl:value-of select="./age_band_text"/>
										</fo:block>
									</fo:table-cell>
									
									<xsl:if test="./age_band_code = 'Z'">
									<xsl:for-each select="./bandrows">
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" text-align="center" padding="1mm" >
											<fo:block font-weight="bold">
													<xsl:text>_____________________________________________</xsl:text>
												</fo:block>
											<fo:block font-weight="normal">
												<xsl:value-of select="./custody_cases"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" text-align="center" padding="1mm" >
											<fo:block font-weight="bold">
													<xsl:text>______________________________</xsl:text>
												</fo:block>
											<fo:block font-weight="normal">
												<xsl:value-of select="./non_custody_cases"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" text-align="center" padding="1mm" >
											<fo:block font-weight="bold">
													<xsl:text>________________________</xsl:text>
												</fo:block>
											<fo:block font-weight="normal">
												<xsl:value-of select="./total"/>
											</fo:block>
										</fo:table-cell>
									</xsl:for-each>
									</xsl:if>
									
									<xsl:if test="./age_band_code != 'Z'">
									<xsl:for-each select="./bandrows">
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" text-align="center" padding="1mm" >
											<fo:block font-weight="normal">
												<xsl:value-of select="./custody_cases"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" text-align="center" padding="1mm" >
											<fo:block font-weight="normal">
												<xsl:value-of select="./non_custody_cases"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" text-align="center" padding="1mm" >
											<fo:block font-weight="normal">
												<xsl:value-of select="./total"/>
											</fo:block>
										</fo:table-cell>
									</xsl:for-each>
									</xsl:if>
								</fo:table-row>
							</xsl:for-each>	
						</fo:table-body>
					</fo:table>
				</xsl:if>
				
				<xsl:if test="position()=2 and ./court_site_name != 'All'">
				
					<fo:table border="none" table-layout="fixed" break-before="page">
						<fo:table-column column-number="1" column-width="540px"/>
					
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
									<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center">
										<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court_site_name"/>
									</fo:block>
									<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:text>Trial cases outstanding at ending </xsl:text><xsl:value-of select="//weekmonthdate"/>
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:text>(* = age calculated using bench warrant executed date)</xsl:text>
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:text>&#xD;&#xA;</xsl:text>
									</fo:block>
									
									<fo:block font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:text>Custody Cases</xsl:text>
										<xsl:text>&#xD;&#xA;</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
									<xsl:for-each select="./bands">
										<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:value-of select="./age_band"/>
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:for-each select="./statuses[status='C']">
												<xsl:for-each select="./cases">
													<xsl:if test="substring(./case_num,1,6) != 'NOCASE'">
														<xsl:value-of select="./case_num"/>
														<xsl:if test="position() mod 5 = 0">
															<xsl:text>&#xD;&#xA;</xsl:text>
														</xsl:if>
														<xsl:if test="position() mod 5 != 0">
															<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;</xsl:text>
														</xsl:if>
													</xsl:if>
												</xsl:for-each>
											</xsl:for-each>
										</fo:block>
									</xsl:for-each>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				
					<fo:table border="none" table-layout="fixed" break-before="page">
						<fo:table-column column-number="1" column-width="540px"/>
					
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
									<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center">
										<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court_site_name"/>
									</fo:block>
									<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:text>Trial cases outstanding at ending </xsl:text><xsl:value-of select="//weekmonthdate"/>
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:text>(* = age calculated using bench warrant executed date)</xsl:text>
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:text>&#xD;&#xA;</xsl:text>
									</fo:block>
									
									<fo:block font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:text>Non Custody Cases</xsl:text>
										<xsl:text>&#xD;&#xA;</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
									<xsl:for-each select="./bands">
										<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:value-of select="./age_band"/>
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:for-each select="./statuses[status!='C']"> 
												<xsl:for-each select="./cases">
													<xsl:if test="substring(./case_num,1,6) != 'NOCASE'">
														<xsl:value-of select="./case_num"/>
														<xsl:if test="position() mod 5 = 0">
															<xsl:text>&#xD;&#xA;</xsl:text>
														</xsl:if>
														<xsl:if test="position() mod 5 != 0">
															<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;</xsl:text>
														</xsl:if>
													</xsl:if>
												</xsl:for-each>
											</xsl:for-each>
										</fo:block>
									</xsl:for-each>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>			