<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:param name="basedir"/>
	<xsl:template match="AppealResultOrder">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">
					<fo:region-body margin-top="1cm" margin-bottom="1cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="AROBody">
						<xsl:with-param name="basedir" select="$basedir" />
					</xsl:call-template>
				</fo:flow>
			</fo:page-sequence>		
		</fo:root>
	</xsl:template>
	
	<xsl:template name="HeaderSection">
		<xsl:param name="path" select="//."/>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="4.5cm"/>
			<fo:table-column column-width="10.5cm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body>
				<fo:table-row font-size="7pt">
					<fo:table-cell number-columns-spanned="2">
						<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="left" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>In the Crown Court</xsl:text>
							<fo:block/>
							<xsl:text>at </xsl:text><xsl:value-of select="$path//CourtName"/>
							<xsl:text>   </xsl:text>                     
						</fo:block>
					</fo:table-cell>
					<fo:table-cell number-rows-spanned="4">
						<fo:block>
							<xsl:value-of select="./basedir"/>
							<fo:external-graphic>
								<xsl:attribute name="src"><xsl:value-of select="concat($basedir, 	'/images/Crown_Logo_Updated_2025.gif')"/></xsl:attribute>
								<xsl:attribute name="height">3.5cm</xsl:attribute>
								<xsl:attribute name="width">4.5cm</xsl:attribute>
								<xsl:attribute name="content-width">scale-to-fit</xsl:attribute>
							</fo:external-graphic>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row >
					<fo:table-cell number-columns-spanned="2">
						<fo:block font-size="10pt" font-family="sans-serif" margin-top="2mm">
							<xsl:text>Case No: </xsl:text>
							<xsl:value-of select="$path/CaseNumber"/>
						</fo:block>	
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block font-size="10pt" font-family="sans-serif" margin-top="2mm">
							<xsl:text>Court Code: </xsl:text>
							<xsl:value-of select="$path/CourtCode"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block font-size="14pt" font-family="sans-serif" text-align="center" font-weight="bold">
							<xsl:text>Result of an Appeal</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>	
	</xsl:template>
	
	<xsl:template name="AROBody">
		<xsl:param name="basedir"/>
		<!-- Copy for Defendant/Appellant -->
		<xsl:call-template name="HeaderSection"/>
		<fo:block>
		<fo:table table-layout="fixed" space-before="7mm">
			<fo:table-column width="30mm"/>
			<fo:table-column width="90mm"/>
			<fo:table-column width="60mm"/>			
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" font-weight="bold" >
							The Defendant
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-top="solid" padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="./DefendantName"/><xsl:text>&#xA;</xsl:text>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="./DefendantAddress"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-top="solid" padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" >
							Date of Birth
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" >
							<xsl:value-of select="./DateOfBirth"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" font-weight="bold" >
							Original Conviction
						</fo:block>
					</fo:table-cell>
					<fo:table-cell number-columns-spanned="2" border-top="solid" padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
							The defendant was convicted on <xsl:value-of select="./MagConvictionDate"/><xsl:text>&#xA;</xsl:text> by <xsl:value-of select="./MagsCourtName"/> of 
						</fo:block>
						<fo:list-block>
							<xsl:for-each select="//OffenceDescription">
								<fo:list-item>
									<fo:list-item-label end-indent="label-end()">
										<fo:block font-size="11pt" font-family="sans-serif"><xsl:value-of select="position()"/>. </fo:block>									
									</fo:list-item-label>
									<fo:list-item-body start-indent="body-start()">
										<fo:block font-size="11pt" font-family="sans-serif"><xsl:value-of select="."/></fo:block>
									</fo:list-item-body>
								</fo:list-item>
							</xsl:for-each>
						</fo:list-block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:if test="//OriginalSentence" >
					<fo:table-row>
						<fo:table-cell padding-top="5mm" padding-bottom="5mm">
							<fo:block font-size="11pt" font-family="sans-serif" text-align="left" font-weight="bold" >
								Original Sentence
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="2" border-top="solid" padding-top="5mm" padding-bottom="5mm">
							<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
								And the following sentence(s)/order(s) were made: 
							</fo:block>
							<fo:list-block>
								<xsl:for-each select="//Offence">
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()">
											<fo:block font-size="11pt" font-family="sans-serif"><xsl:value-of select="position()"/>. </fo:block>
										</fo:list-item-label>
										<fo:list-item-body start-indent="body-start()">
											<xsl:for-each select="./OriginalSentence">
												<fo:block font-size="11pt" font-family="sans-serif"><xsl:value-of select="."/></fo:block>
											</xsl:for-each>
										</fo:list-item-body>
									</fo:list-item>
								</xsl:for-each>
							</fo:list-block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<fo:table-row>
					<fo:table-cell padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" font-weight="bold"  >
							The Appeal
						</fo:block>
						<fo:block font-size="9pt" font-family="sans-serif" text-align="left" font-style="italic"  >
							Crown Court variations are underlined
						</fo:block>
					</fo:table-cell>
					<fo:table-cell number-columns-spanned="2" border-top="solid" padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
							<xsl:choose>
								<xsl:when test="./VerdictCode='AB'">
									<xsl:text> was abandoned before court on </xsl:text><xsl:value-of select="./VerdictDate"/>
								</xsl:when>
								<xsl:when test="./VerdictCode='AC'">
									<xsl:text> was abandoned in court on </xsl:text><xsl:value-of select="./VerdictDate"/>
								</xsl:when>
								<xsl:otherwise>
									<fo:block>
										<xsl:text> was heard on </xsl:text><xsl:value-of select="./VerdictDate"/><xsl:text> and it was ordered that: </xsl:text>
									</fo:block>
									<fo:list-block>
										<xsl:for-each select="//Offence">
											<fo:list-item>
												<fo:list-item-label end-indent="label-end()">
													<fo:block><xsl:value-of select="position()"/>. </fo:block>
												</fo:list-item-label>
												<fo:list-item-body start-indent="body-start()">
													<fo:block font-size="11pt" font-family="sans-serif" white-space-collapse="false" white-space-treatment="preserve"><xsl:value-of select="./AppealResults/AppealResultDescription"/><xsl:text> </xsl:text></fo:block>
													<xsl:if test="string-length(./AppealResults/AppealResultDescription) != 0">
														<xsl:choose>
															<!-- If the Offence Appeal Code is 'AB', 'ACD', 'AD', 'ADAC' or 'ASD' then only display Magistrates disposals -->
															<xsl:when test="./AppealResults/@AppealResultCode='AB' or ./AppealResults/@AppealResultCode='ACD' or ./AppealResults/@AppealResultCode='AD' 
																				or ./AppealResults/@AppealResultCode='ADAC' or ./AppealResults/@AppealResultCode='ASD'">
																<xsl:for-each select="./AppealResults/AppealResultSentence[@CourtType='M']">	
																	<fo:block font-size="11pt" font-family="sans-serif">
																		<xsl:value-of select="."/>
																	</fo:block>
																</xsl:for-each>
															</xsl:when>
															<!-- If the Offence Appeal Code is 'ACALO', 'ACDSI', 'ACDSV', 'ADSI', 'ASASV' or 'ASDSI' then display unvaried Magistrates disposals 
																 as well as Crown Court disposals -->
															<xsl:when test="./AppealResults/@AppealResultCode='ACALO' or ./AppealResults/@AppealResultCode='ACDSI' or ./AppealResults/@AppealResultCode='ACDSV' 
																				or ./AppealResults/@AppealResultCode='ADSI' or ./AppealResults/@AppealResultCode='ASASV' or ./AppealResults/@AppealResultCode='ASDSI'">
																<xsl:for-each select="./AppealResults/AppealResultSentence[@CourtType='C' or (@CourtType='M' and @Varied='N')]">
																	<xsl:choose>
																		<!-- Underline criminal disposals -->
																		<xsl:when test="@CourtType = 'C'">
																			<fo:block font-size="11pt" font-family="sans-serif" text-decoration="underline">
																				<xsl:value-of select="."/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:block font-size="11pt" font-family="sans-serif" >
																				<xsl:value-of select="."/>
																			</fo:block>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:when>
														</xsl:choose>
													</xsl:if>
												</fo:list-item-body>
											</fo:list-item>
										</xsl:for-each>
									</fo:list-block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:if test="./OtherOrders" >
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="11pt" font-family="sans-serif" text-align="left" font-weight="bold" padding-top="5mm" padding-bottom="5mm">
								Other Orders
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="2" border-top="solid" border-bottom="solid" padding-top="5mm" padding-bottom="5mm">
							<xsl:for-each select="//OtherOrder[@CourtType='M' and @Varied='N']">
								<fo:block font-size="11pt" font-family="sans-serif" ><xsl:value-of select="."/></fo:block>
							</xsl:for-each>
							<xsl:for-each select="//OtherOrder[@CourtType='C']">
								<fo:block font-size="11pt" font-family="sans-serif" text-decoration="underline">
									<xsl:value-of select="."/>
								</fo:block>
							</xsl:for-each>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell border-bottom="solid" padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
							<xsl:text>An Officer of the Crown Court</xsl:text>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
							<xsl:text>Signed:</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-bottom="solid" padding-top="5mm" padding-bottom="5mm">
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
							<xsl:text> </xsl:text>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left">
							<xsl:text>Dated: </xsl:text><xsl:value-of select="./TodaysDate"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>