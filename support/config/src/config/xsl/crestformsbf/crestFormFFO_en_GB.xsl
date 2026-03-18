<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:crestformsbf="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf" version="1.0">
	<xsl:template match="crestformsbf:CrestFormF">
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
						<fo:block line-height="5pt" space-after="5pt"> </fo:block>
						<fo:block font-size="18pt" font-weight="bold">
							Court Clerk's Hearing Record (F)
						</fo:block>
						<fo:block line-height="6pt" space-after="6pt"> </fo:block>
						<fo:block font-size="15pt" font-weight="bold" line-height="20pt">
							Results of criminal appeals
						</fo:block>
						<fo:block font-size="15pt" font-weight="bold" line-height="20pt" margin-bottom="1mm">
							from magistrates' courts
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
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".5mm" height="20mm" margin-right="3mm" margin-bottom="2mm">
							<fo:table-column column-width="{$Col1_Inner}"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
								<fo:table-row>
									<fo:table-cell>
										<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
											<!-- This will always be Appellant's name because this form is for a Criminal Appeals -->
											Appellant's name:
											<!-- call template to display defendant details excluding title -->
											<xsl:call-template name="DefendantDetailsExcTitle"/>
										</fo:block>
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
										<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
											Case and Defendant No.:
											<!-- call template to display Appeal Case Details -->
											<xsl:call-template name="AppealCaseNoDetails"/>
										</fo:block>
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
		<!-- call template to display offence information -->
		<xsl:apply-templates select="crestformsbf:Charges/crestformsbf:Charge/crestformsbf:Offence" mode="FormF"/>
		<fo:block space-after="8pt"> </fo:block>
		<!-- Test if there are any unrelated disposals to display - if there are display title and call the template -->
		<xsl:if test="count(crestformsbf:UnrelatedDisposals/crestformsbf:Disposal) &gt; 0">
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="100mm"/>
				<fo:table-column column-width="90mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="14pt" font-weight="bold" line-height="20pt">
								Disposal(s) not related to specific offence(s)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
			<!-- call template to display unrelated disposals information -->
			<xsl:call-template name="UnrelatedDisposal"/>
			<fo:block space-after="8pt"> </fo:block>
		</xsl:if>
		<!-- Test if there is an Appeal Reason, default a single space if not present-->
		<xsl:if test="crestformsbf:AppealCase/crestformsbf:ReasonForAppeal !=' '">
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="100mm"/>
				<fo:table-column column-width="90mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="14pt" font-weight="bold" line-height="20pt">
								The Judge's reason for allowing the appeal
							</fo:block>
						</fo:table-cell>
						<fo:table-cell/>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
			<!-- call template to display Judges reason for appeal -->
			<xsl:apply-templates select="crestformsbf:AppealCase"/>
			<fo:block space-after="8pt"> </fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="crestformsbf:Offence" mode="FormF">
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="2pt" space-after.optimum="2pt" color="#777777" rule-thickness="5pt" leader-length="100%"/>
		</fo:block>
		<fo:block space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="65mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Offence No.
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
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="150mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Appeal against:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- Display appropriate text according to the three AppealTypes available -->
							<xsl:choose>
								<xsl:when test="crestformsbf:AppealType='Conviction'">conviction</xsl:when>
								<xsl:when test="crestformsbf:AppealType='Sentence'">sentence</xsl:when>
								<xsl:when test="crestformsbf:AppealType='ConvictionSentence'">conviction &amp; sentence</xsl:when>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="150mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Offence description
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- chek to ensure that there is an Offence -->
							<xsl:if test="crestformsbf:OffenceCode!=''">
								<xsl:value-of select="crestformsbf:OffenceCode"/>
								<xsl:text> - </xsl:text>
								<xsl:value-of select="crestformsbf:OffenceDesc"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="150mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Result
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- Result information is from the Verdict (Code and Description) plus the judges reason for appeal -->
							<!-- Check to ensure that there is a Verdict -->
							<xsl:if test="crestformsbf:Verdict/crestformsbf:VerdictCode!=''">
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:VerdictCode"/>
								<xsl:text> - </xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:VerdictDesc"/>
							</xsl:if>
							<!-- display any other text -->
							<xsl:if test="crestformsbf:Verdict/crestformsbf:VerdictOtherText">
								<xsl:text> </xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:VerdictOtherText"/>
							</xsl:if>
							<!-- test to see if any alternative offence information -->
							<xsl:if test="crestformsbf:Verdict/crestformsbf:AlternativeOffence">
								<xsl:text> </xsl:text>
								<xsl:value-of select="crestformsbf:Verdict/crestformsbf:AlternativeOffence/crestformsbf:AltOffenceDesc"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="150mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Disposal(s)
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:apply-templates select="crestformsbf:Disposal" mode="FormF"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="18pt" space-after="18pt"> </fo:block>
	</xsl:template>
</xsl:stylesheet>