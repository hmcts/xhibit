<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:crestformsbf="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf" version="1.0">
	<xsl:template match="crestformsbf:CrestFormFMisc">
		<!-- set up variables to hold whether to display Appellant or Defendant in the form header.
			This is set up according to the Case type
			CaseReferences begining with A - display Appellant
			All other caseTypes - display Defendant -->
		<xsl:variable name="DefendantAppellant">
			<xsl:choose>
				<xsl:when test="substring(crestformsbf:AppealCase/crestformsbf:CaseReference,1,1)='A'">
					Appellant
				</xsl:when>
				<xsl:otherwise>
					Defendant
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="DefendantAppellantTitle">
			<xsl:choose>
				<xsl:when test="substring(crestformsbf:AppealCase/crestformsbf:CaseReference,1,1)='A'">
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
						<fo:block line-height="5pt" space-after="5pt"> </fo:block>
						<fo:block font-size="18pt" font-weight="bold">
							Court Clerk's Hearing Record (F)
						</fo:block>
						<fo:block line-height="6pt" space-after="6pt"> </fo:block>
						<fo:block font-size="15pt" font-weight="bold" line-height="20pt">
							Results of miscellaneous appeals
						</fo:block>
						<fo:block font-size="15pt" font-weight="bold" line-height="20pt">
							from magistrates' courts
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
		<!-- call template to display charge information -->
		<xsl:apply-templates select="crestformsbf:MiscAppealCharges/crestformsbf:Result" mode="FormFMisc"/>
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
				<fo:table-column column-width="150mm"/>
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

	<xsl:template match="crestformsbf:Result" mode="FormFMisc">
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="2pt" space-after.optimum="2pt" color="#777777" rule-thickness="5pt" leader-length="100%"/>
		</fo:block>
		<xsl:if test="../../crestformsbf:AppealCase/crestformsbf:CaseTitle != ' '">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Appeal title:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<!-- appeal title if the CaseTitle -->
								<xsl:value-of select="../../crestformsbf:AppealCase/crestformsbf:CaseTitle"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>

		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="150mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Appeal type:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!-- Appeal Type : Hard coded as "Miscellaneous" -->
							Miscellaneous
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>

		<xsl:if test="../../crestformsbf:AppealCase/crestformsbf:CaseDesc != ' '">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Case description:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="../../crestformsbf:AppealCase/crestformsbf:CaseDesc "/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>

		<xsl:if test="crestformsbf:Result">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Results:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="crestformsbf:Result"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>

		<xsl:if test="crestformsbf:ResultDate">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Results date:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:call-template name="displayDate2">
									<xsl:with-param name="input">
										<xsl:value-of select="crestformsbf:ResultDate"/>
									</xsl:with-param>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>

		<xsl:if test="crestformsbf:HearingDuration">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Hearing duration:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="crestformsbf:HearingDuration"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>

		<xsl:if test="crestformsbf:HearingDate">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Hearing start date:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:call-template name="displayDate2">
									<xsl:with-param name="input">
										<xsl:value-of select="crestformsbf:HearingDate"/>
									</xsl:with-param>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>

		<xsl:if test="crestformsbf:TransferCourt">
			<fo:block space-after="8pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="35mm"/>
				<fo:table-column column-width="150mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						              Court:
					            </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="crestformsbf:TransferCourt"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
