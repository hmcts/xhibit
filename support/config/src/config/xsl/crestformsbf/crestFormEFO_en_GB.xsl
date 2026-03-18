<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:crestformsbf="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf" version="1.0">
	<xsl:template match="crestformsbf:CrestFormE">
		<!-- set up variables to hold whether to display Appellant or Defendant in the form header.
			This is set up according to the Case type
			CaseReferences begining with A - display Appellant
			All other caseTypes - display Defendant -->
		<xsl:variable name="DefendantAppellant">
			<xsl:choose>
				<xsl:when test="substring(crestformsbf:Case/crestformsbf:CaseReference,1,1)='A'">
					Appellant
				</xsl:when>
				<xsl:otherwise>
					Defendant
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="DefendantAppellantTitle">
			<xsl:choose>
				<xsl:when test="substring(crestformsbf:Case/crestformsbf:CaseReference,1,1)='A'">
					Appellant's name:
				</xsl:when>
				<xsl:otherwise>
					Defendant's name:
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
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
							Court Clerk's Hearing Record (E)
						</fo:block>
						<fo:block font-size="15pt" font-weight="bold" line-height="30pt">
							Results of breaches of previous orders
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-width=".6mm">
						<fo:block>
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="20mm">
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
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="20mm">
							<fo:table-column column-width="{$Col1_Inner}"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
								<fo:table-row>
									<fo:table-cell>
										<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
											<xsl:value-of select="$DefendantAppellantTitle"/>
											<!-- call template to display defendant details excluding the title -->
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
						<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="20mm">
							<fo:table-column column-width="{$Col2_Inner}"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
								<fo:table-row>
									<fo:table-cell>
										<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
											Case and <xsl:value-of select="$DefendantAppellant"/> No.:
											<!-- call template to display CaseNo and Defendant details excluding title -->
											<xsl:call-template name="CaseNoDetailsExcTitle"/>
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
		<!-- apply the Breach Charge Offence template -->
		<xsl:apply-templates select="crestformsbf:BreachCharges/crestformsbf:BreachCharge/crestformsbf:Offence" mode="FormE"/>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="2pt" space-after.optimum="2pt" color="#777777" rule-thickness="5pt" leader-length="100%"/>
		</fo:block>
	</xsl:template>

	<xsl:template match="crestformsbf:Offence" mode="FormE">
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="2pt" space-after.optimum="2pt" color="#777777" rule-thickness="5pt" leader-length="100%"/>
		</fo:block>
		<fo:block space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="25mm"/>
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
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Committal after a breach:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="../crestformsbf:Breach/crestformsbf:CommittalAfterBreach">
									<!-- Either Yes or No -->
									<xsl:value-of select="../crestformsbf:Breach/crestformsbf:CommittalAfterBreach"/>
								</xsl:when>
								<xsl:otherwise>
									Unspecified
								</xsl:otherwise>
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
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
					              Home Office Proceedings code
				            </fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="../@HOCode"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Bring back:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="../crestformsbf:Breach/crestformsbf:BringBack">
									<!-- Either Yes or No -->
									<xsl:value-of select="../crestformsbf:Breach/crestformsbf:BringBack"/>
								</xsl:when>
								<xsl:otherwise>
									Unspecified
								</xsl:otherwise>
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
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="130mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<fo:inline font-weight="bold">Offence </fo:inline> description
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- check to ensure that there is an Offence -->
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
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="65mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<fo:inline font-weight="bold">Original </fo:inline> Sentence or Order dated
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<!-- Check to see if there is an Original Date -->
						<xsl:choose>
							<xsl:when test="../crestformsbf:Breach/crestformsbf:OriginalDate">
								<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
									<!-- call template to display date in the correct format -->
									<xsl:call-template name="displayDate">
										<xsl:with-param name="input">
											<xsl:value-of select="../crestformsbf:Breach/crestformsbf:OriginalDate"/>
										</xsl:with-param>
									</xsl:call-template>
								</fo:block>
							</xsl:when>
							<xsl:otherwise>
								<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
									Unspecified Sentence or Order date
								</fo:block>
							</xsl:otherwise>
						</xsl:choose>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="158mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<fo:inline font-weight="bold">Original </fo:inline> Court
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- check to ensure that there is a Court House -->
							<xsl:if test="../crestformsbf:Breach/crestformsbf:CourtHouseCode!=''">
								<xsl:value-of select="../crestformsbf:Breach/crestformsbf:CourtHouseCode"/>
								<xsl:text> - </xsl:text>
							</xsl:if>
							<!-- call template to initial the first character and lower case for rest -->
							<xsl:call-template name="TitleCase">
								<xsl:with-param name="text">
									<xsl:value-of select="../crestformsbf:Breach/crestformsbf:CourtHouseName"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="125mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<fo:inline font-weight="bold">Original </fo:inline> sentence or order
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="../crestformsbf:Breach/crestformsbf:OriginalSentence">
									<xsl:value-of select="../crestformsbf:Breach/crestformsbf:OriginalSentence"/>
								</xsl:when>
								<xsl:otherwise>
									Unspecified Original Sentence
								</xsl:otherwise>
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
							Disposal(s)
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- call template to display disposals information -->
							<xsl:apply-templates select="crestformsbf:Disposal"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block font-family="Times" font-size="11pt" line-height="20pt">
			For breaches identified during the hearing <fo:inline font-weight="bold">only</fo:inline>:
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="40mm"/>
			<fo:table-column column-width="75mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Date breach put
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="../crestformsbf:Breach/crestformsbf:BreachPutDate">
									<!-- call template to display the date in the correct format -->
									<xsl:call-template name="displayDate">
										<xsl:with-param name="input">
											<xsl:value-of select="../crestformsbf:Breach/crestformsbf:BreachPutDate"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									N/A
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Admitted:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- Either Yes or No -->
							<xsl:value-of select="../crestformsbf:Breach/crestformsbf:Admitted"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>
