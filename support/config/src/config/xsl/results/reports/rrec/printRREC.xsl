<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template name="RREC">
					<xsl:for-each select="./rrec-summary-detail">
						<xsl:for-each select="./summary-detail">
							<xsl:if test="position()=1">
							
								<xsl:choose>
									<xsl:when test="./court_site_name = 'All'">
										<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" break-before="page"  linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="//court-name"/>
										</fo:block>
										<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>Cases Received and Disposed of during </xsl:text><xsl:value-of select="//weekmonthdate"/>
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>for administrative centre including all satellites</xsl:text>
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>&#xD;&#xA;</xsl:text>
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" break-before="page"  linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court_site_name"/>
										</fo:block>
										<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>Cases Received and Disposed of during </xsl:text><xsl:value-of select="//weekmonthdate"/>
											<xsl:text>&#xD;&#xA;</xsl:text>
											<xsl:text>&#xD;&#xA;</xsl:text>
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>

							<fo:table border="none" table-layout="fixed">
							<fo:table-column column-number="1" column-width="65px"/>
							<fo:table-column column-number="2" column-width="60px"/>
							<fo:table-column column-number="3" column-width="60px"/>
							<fo:table-column column-number="4" column-width="60px"/>
							<fo:table-column column-number="5" column-width="60px"/>
							<fo:table-column column-number="6" column-width="60px"/>
							<fo:table-column column-number="7" column-width="60px"/>
							<fo:table-column column-number="8" column-width="60px"/>
							<fo:table-column column-number="9" column-width="60px"/>
					
							<fo:table-header>
								<fo:table-row>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt">
											<xsl:text>&#x00A0;&#x00A0;&#x00A0;</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(1)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Outstanding at Beginning of Period</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(2)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Received from PSDS</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(3)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Transferred from other Crown Court Centres</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(4)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Bench Warrants Executed</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(5)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Transferred to other Courts</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(6)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Net Receipts (2)+(3)+(4)-(5)</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(7)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Total Dealt With</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>(8)</xsl:text>
										</fo:block>
										<fo:block font-weight="bold" font-size="9pt" text-align="center">
											<xsl:text>Outstanding at End of Period (1)+(6)-(7)</xsl:text>
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
								<fo:table-row border="none">
									<fo:table-cell border-style="none">
										<fo:block font-weight="normal"/>
									</fo:table-cell>
								</fo:table-row> 
							</fo:table-body>
						</fo:table>
	
				<fo:block font-size="11pt" font-weight="bold">
					<xsl:text>&#xD;&#xA;</xsl:text>
					<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
					<xsl:text>&#xD;&#xA;</xsl:text>
				</fo:block>
		
				<xsl:for-each select="./types">
					<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-decoration="underline" space-before="8pt" space-after="8pt">
					    <xsl:value-of select="./case_type"/>
					</fo:block>
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="65px"/>
						<fo:table-column column-number="2" column-width="60px"/>
						<fo:table-column column-number="3" column-width="60px"/>
						<fo:table-column column-number="4" column-width="60px"/>
						<fo:table-column column-number="5" column-width="60px"/>
						<fo:table-column column-number="6" column-width="60px"/>
						<fo:table-column column-number="7" column-width="60px"/>
						<fo:table-column column-number="8" column-width="60px"/>
						<fo:table-column column-number="9" column-width="60px"/>
					
					<xsl:for-each select="./typerows">
							<fo:table-body>
								<fo:table-row border-style="none" space-before.optimum="12pt">
									<xsl:if test="./case_subhdg = 'Total'">
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
											<fo:block font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
												<xsl:text>&#xD;&#xA;&#x00A0;&#x00A0;Totals:</xsl:text>
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<xsl:if test="./case_subhdg != 'Total' ">
										<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
											<xsl:if test="./case_subhdg != 'NULL'">
												<fo:block font-weight="normal">
													<xsl:value-of select="./case_subhdg"/>
												</fo:block>
											</xsl:if>
											<xsl:if test="./case_subhdg = 'NULL'">
												<fo:block font-weight="normal">
													<xsl:text>No Case Heading Specified</xsl:text>
												</fo:block>
											</xsl:if>
										</fo:table-cell>
									</xsl:if>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section1"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section1"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section2"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section2"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section3"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section3"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section4"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section4"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section5"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section5"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section6"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section6"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>__________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section7"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section7"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
									<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
										<xsl:if test="./case_subhdg = 'Total'">
											<fo:block font-weight="bold">
												<xsl:text>________</xsl:text>
											</fo:block>
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section8"/>
											</fo:block>
										</xsl:if>
										<xsl:if test="./case_subhdg != 'Total'">
											<fo:block font-weight="normal" text-align="center">
												<xsl:value-of select="./section8"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
					</xsl:for-each>
					
					
					</fo:table>
					
					<fo:block font-size="11pt" font-weight="bold" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>&#xD;&#xA;</xsl:text>
						<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
					</fo:block>
				
			  	</xsl:for-each>
				
				</xsl:if>
				
				<xsl:if test="position()=2 and ./court_site_name != 'All'">
				
				<fo:table border="none" table-layout="fixed" break-before="page">
					<fo:table-column column-number="1" column-width="540px"/>
				
					<fo:table-header>
						<fo:table-row>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
									<xsl:text>&#xD;&#xA;</xsl:text>
									<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court_site_name"/>
								</fo:block>
								<fo:block linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:text>&#xD;&#xA;</xsl:text>
								</fo:block>

								<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
									<xsl:text>Cases Dealt With within this Period </xsl:text><xsl:value-of select="//weekmonthdate"/>
									<xsl:text>&#xD;&#xA;</xsl:text>
									<xsl:text>&#xD;&#xA;</xsl:text>
								</fo:block>	
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
								<xsl:for-each select="./subheadings">
									<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-decoration="underline" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:text>&#xD;&#xA;</xsl:text>
										<xsl:choose>
											<xsl:when test="./case_subhdg != 'NULL'">
												<xsl:value-of select="./case_subhdg"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text>No Case Heading Specified</xsl:text>
											</xsl:otherwise>
										</xsl:choose>
										<fo:inline text-decoration="no-underline">
											<xsl:text>&#xD;&#xA;</xsl:text>
										</fo:inline>
										<fo:inline text-decoration="no-underline">
											<xsl:for-each select="./cases">
												<xsl:if test="contains(./case_type,'APPEAL')">
													<xsl:text>A</xsl:text>
												</xsl:if>
												<xsl:if test="contains(./case_type,'TRIAL')">
													<xsl:text>T</xsl:text>
												</xsl:if>
												<xsl:if test="contains(./case_type,'SENTENCE')">
													<xsl:text>S</xsl:text>
												</xsl:if>
												<xsl:value-of select="./case_number"/>
													<xsl:text>&#x00A0;&#x00A0;</xsl:text>
												<xsl:if test="position() mod 7 = 0">
													<xsl:text>&#xD;&#xA;</xsl:text>
												</xsl:if>
											</xsl:for-each>
										</fo:inline>
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