<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:crestformsbf="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf" version="1.0">
	<xsl:template match="crestformsbf:CrestFormB">
		<!-- Test to see if a page break is required -->
		<xsl:if test="count(../crestformsbf:CrestFormB) &gt; 0 or count(../crestformsbf:CrestFormC) &gt; 0 or count(../crestformsbf:CrestFormD) &gt; 0 or count(../crestformsbf:CrestFormE) &gt; 0 or count(../crestformsbf:CrestFormF) &gt; 0 or count(../crestformsbf:CrestFormFMisc) &gt; 0">
			<fo:block break-before="page" line-height="1pt" space-after="1pt"> </fo:block>
		</xsl:if>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="{$Col1_Total}"/>
			<fo:table-column column-width="{$Col2_Total}"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block line-height="15pt" space-after="15pt"> </fo:block>
						<fo:block font-size="18pt" font-weight="bold">
							Court Clerk's Hearing Record (B)
						</fo:block>
						<fo:block font-size="15pt" font-weight="bold" line-height="30pt" margin-bottom="1mm">
							Results of counts on the indictment(s)
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-width=".6mm">
						<fo:block>
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".5mm" height="20mm">
							<fo:table-column column-width="{$Col2_Inner}"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
								<fo:table-row>
									<fo:table-cell>
										<!-- call common template to display Court Clerk Details -->
										<xsl:call-template name="CourtClerkDetails"/>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell border-width=".6mm">
						<fo:block>
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".5mm" height="20mm" margin-right="3mm">
							<fo:table-column column-width="{$Col1_Inner}"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
								<fo:table-row>
									<fo:table-cell>
										<!-- call common template to display defendant details -->
										<xsl:call-template name="DefendantDetails"/>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-width=".6mm">
						<fo:block>
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".5mm" height="20mm">
							<fo:table-column column-width="{$Col2_Inner}"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
								<fo:table-row>
									<fo:table-cell>
										<!-- call common template to display Case No and Defendant Details -->
										<xsl:call-template name="CaseNoDetails"/>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block space-after="8pt"/>
		<!-- apply the offence template -->
		<xsl:apply-templates select="crestformsbf:Charges/crestformsbf:Charge/crestformsbf:Offence" mode="FormB"/>
		<fo:block space-after="8pt"/>
		<!-- Test if there are any unrelated disposals to display - if there are display title and call the template -->
		<xsl:if test="count(crestformsbf:UnrelatedDisposals/crestformsbf:Disposal) &gt; 0">
			<fo:block font-family="Times" font-size="14pt" font-weight="bold" line-height="20pt">
				Disposal(s) not related to specific offence(s)
			</fo:block>
			<fo:block>
				<xsl:call-template name="UnrelatedDisposal"/>
			</fo:block>
			<fo:block space-after="8pt"/>
		</xsl:if>
		<xsl:if test="crestformsbf:TICS">
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="140mm"/>
				<fo:table-column column-width="40mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="14pt" font-weight="bold" line-height="20pt">
								Number of offences taken into consideration
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt" text-align="right">
								<xsl:value-of select="crestformsbf:TICS"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="crestformsbf:Offence" mode="FormB">
		<fo:block space-after="8pt"/>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="65mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Indictment number
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="../@IndictmentNumber"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Count number
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="crestformsbf:OffenceNo"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="65mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
              					Arraignment date
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:call-template name="displayDate">
								<xsl:with-param name="input">
									<xsl:value-of select="crestformsbf:Plea/crestformsbf:ArraignmentDate"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="10mm"/>
			<fo:table-column column-width="175mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
              					Plea
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- check to ensure that there is a Plea -->
							<xsl:if test="crestformsbf:Plea/crestformsbf:PleaCode!=' '">
								<xsl:value-of select="crestformsbf:Plea/crestformsbf:PleaCode"/>
								<xsl:text> - </xsl:text>
								<xsl:value-of select="crestformsbf:Plea/crestformsbf:PleaDesc"/>
								<!-- test to see if any alternative offence information -->
								<xsl:if test="crestformsbf:Plea/crestformsbf:AlternativeOffence">
									<xsl:text> </xsl:text>
									<xsl:value-of select="crestformsbf:Plea/crestformsbf:AlternativeOffence/crestformsbf:AltOffenceDesc"/>
									<xsl:text>(</xsl:text>
									<xsl:value-of select="crestformsbf:Plea/crestformsbf:AlternativeOffence/crestformsbf:AltOffenceCode"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
								<!-- only display if 'Other' type of plea code -->
								<xsl:if test="crestformsbf:Plea/crestformsbf:PleaCode='O' and crestformsbf:Plea/crestformsbf:PleaFreeText!=' '">
									<xsl:text> - </xsl:text>
									<xsl:value-of select="crestformsbf:Plea/crestformsbf:PleaFreeText"/>
								</xsl:if>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="105mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt" text-align="right">
							<!-- VerdictType will be set to blank if there is no Verdict or Plea -->
							<xsl:choose>
								<xsl:when test="crestformsbf:VCOFlag">
									<xsl:choose>
										<xsl:when test="crestformsbf:VCOFlag=''">
											<!-- If VerdictType is blank display message -->
											<fo:block font-weight="bold">
												No verdict/conviction/other
											</fo:block>
										</xsl:when>
										<xsl:when test="crestformsbf:VCOFlag !=''">
											<!-- If there is a Verdict Type then display the Type and the Date -->
											<xsl:value-of select="crestformsbf:VCOFlag"/>
											<xsl:text> </xsl:text>
											 on <fo:inline font-style="italic">(date)</fo:inline>
											<xsl:text> </xsl:text>
											<xsl:choose>
												<xsl:when test="crestformsbf:VCODate">
													<fo:inline font-family="Courier" text-decoration="underline" font-size="15pt">
														<xsl:call-template name="displayDate">
															<xsl:with-param name="input">
																<xsl:value-of select="crestformsbf:VCODate"/>
															</xsl:with-param>
														</xsl:call-template>
													</fo:inline>							 
												</xsl:when>
												<xsl:otherwise>
													Unspecified Date
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<!-- If there is no VerdictType element display message -->
									<fo:block font-weight="bold">
										No verdict/conviction/other
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="15mm"/>
			<fo:table-column column-width="170mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Verdict
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- check to ensure that there is a Verdict -->
							<xsl:if test="crestformsbf:Verdict/crestformsbf:VerdictCode!=''">
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:VerdictCode"/>
								<xsl:text> - </xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:VerdictDesc"/>
							</xsl:if>
							<xsl:if test="crestformsbf:Verdict/crestformsbf:VerdictOtherText">
								<xsl:text> </xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:VerdictOtherText"/>
							</xsl:if>
							<!-- test to see if any alternative offence information -->
							<xsl:if test="crestformsbf:Verdict/crestformsbf:AlternativeOffence">
								<xsl:text> </xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:AlternativeOffence/crestformsbf:AltOffenceDesc"/>
								<xsl:text>(</xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:AlternativeOffence/crestformsbf:AltOffenceCode"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="40mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Number of jurors:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Assenting
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="crestformsbf:Verdict/crestformsbf:JurorsAssenting"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Dissenting
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="crestformsbf:Verdict/crestformsbf:JurorsDissenting"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="165mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Disposal(s)
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:apply-templates select="crestformsbf:Disposal"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="18pt" space-after="18pt"> </fo:block>
	</xsl:template>
</xsl:stylesheet>
