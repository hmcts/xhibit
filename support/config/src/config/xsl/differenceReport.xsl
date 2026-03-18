<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template match="resyncreport">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="first" page-height="21cm" page-width="29.7cm">
					<fo:region-body margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="0.5cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="first">
				<fo:flow flow-name="xsl-region-body">
					<fo:block space-after.optimum="10pt" font-weight="bold" font-size="14pt" text-align="center">
						<xsl:text>Refresh Differences Report for Case </xsl:text>
						<xsl:value-of select="casetype"/>
						<xsl:value-of select="casenumber"/>
					</fo:block>
					<fo:block>
					<fo:table table-layout="fixed">
						<fo:table-column column-number="1" column-width="9cm"/>
						<fo:table-column column-number="2" column-width="9.5cm"/>
						<fo:table-column column-number="3" column-width="9.5cm"/>
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell border-style="solid" margin="5mm" font-size="12pt" font-weight="bold">
									<fo:block>
										<xsl:text>Field Name</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" margin="5mm" font-size="12pt" font-weight="bold">
									<fo:block>
										<xsl:text>XHIBIT Value</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" margin="5mm" font-size="12pt" font-weight="bold">
									<fo:block>
										<xsl:text>CREST Value</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body font-size="9pt">
							<!-- Prosecuting agencies -->
							<xsl:call-template name="prosAgency"/>
							<!-- Appeal case level result -->
							<xsl:if test="casetype = 'A'">
								<!-- only display if Appeal Type Case -->
								<xsl:call-template name="appealcaselevelResult"/>
							</xsl:if>
							<!-- DefendantDetails -->
							<xsl:call-template name="defendantDetails"/>
							<!-- Charges details -->
							<xsl:call-template name="chargesDetails"/>
						</fo:table-body>
					</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!--prosecution agency details -->
	<xsl:template name="prosAgency" match="prosAgency">
		<xsl:call-template name="empty"/>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
				<fo:block>
					<xsl:text>Prosecuting Agency Details</xsl:text>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:call-template name="empty"/>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text>Prosecuting Agency</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="caseprosecutingagencies/XHIBITcaseprosecutingagency"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="caseprosecutingagencies/CRESTcaseprosecutingagency"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Appeal Case Level Results -->
	<xsl:template name="appealcaselevelResult" match="appealcaselevelResult">
		<xsl:call-template name="empty"/>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
				<fo:block>
					<xsl:text>Appeal Case Level Result</xsl:text>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:call-template name="empty"/>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text>Result</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/appealresult/XHIBITappealresult"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/appealresult/CRESTappealresult"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text>Result Date</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/appealresultDate/XHIBITappealresultDate"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/appealresultDate/CRESTappealresultDate"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text>Misc. Appeal Transfer Court</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/misctransferTo/XHIBITmisctransferTo"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/misctransferTo/CRESTmisctransferTo"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text>Misc. Appeal Hearing Start Date</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/mischearingstartDate/XHIBITmischearingstartdate"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/mischearingstartDate/CRESTmischearingstartdate"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text>Misc. Appeal Hearing Duration</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/mischearingDuration/XHIBITmischearingDuration"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:value-of select="appealcaselevelResult/mischearingDuration/CRESTmischearingDuration"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Defendant details -->
	<xsl:template name="defendantDetails" match="defendantDetails">
		<!-- Display Defendant and Disposal Info -->
		<xsl:for-each select="defendantoncasedetails/defendant">
			<xsl:call-template name="empty"/>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
					<fo:block>
						<xsl:text>Defendant Details</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:call-template name="empty"/>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>XHIBIT defendant id</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantids/XHIBITdefendantID"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:text>N/A</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>CREST defendant id</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantids/XHIBITcrestdefendantID"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantids/CRESTdefendantID"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>Surname</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantlastname/XHIBITdefendantlastname"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantlastname/CRESTdefendantlastname"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>First name</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantforename/XHIBITdefendantforename"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantforename/CRESTdefendantforename"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>Number of TICs</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="nooftics/XHIBITnooftics"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="nooftics/CRESTnooftics"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>Last conviction date</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="lastconvdate/XHIBITlastconvdate"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="lastconvdate/CRESTlastconvdate"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>Defendant number</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantno/XHIBITdefendantno"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="defendantno/CRESTdefendantno"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>PTIURN</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="ptiurn/XHIBITptiurn"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="ptiurn/CRESTptiurn"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<!-- Check if any un-related disposals to display -->
			<xsl:if test="UnrelatedDisposals/UnrelatedDisposal">
				<xsl:for-each select="UnrelatedDisposals/UnrelatedDisposal">
					<xsl:call-template name="empty"/>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="8mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
							<fo:block>
								<xsl:text>Defendant Related Disposals</xsl:text>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:call-template name="empty"/>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
							<fo:block>
								<xsl:text>Disposal Code</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalCode/XHIBITdisposalCode"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalCode/CRESTdisposalCode"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
							<fo:block>
								<xsl:text>Disposal Template Version</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposaltemplateVersion/XHIBITtemplateVersion"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposaltemplateVersion/CRESTemplateVersion"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
							<fo:block>
								<xsl:text>Disposal Court Type</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalcourtType/XHIBITdisposalcourtType"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalcourtType/CRESTdisposalcourtType"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<!-- Check if any Disposal Line Info -->
					<xsl:if test="DISPOSALdisposalLine">
						<xsl:call-template name="empty"/>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="8mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
								<fo:block>
									<xsl:text>Disposal Line Details</xsl:text>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:call-template name="empty"/>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
								<fo:block>
									<xsl:text>Data</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/Data/XHIBITData"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/Data/CRESTData"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
								<fo:block>
									<xsl:text>Deleted Data</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/DelData/XHIBITdelData"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/DelData/CRESTdelData"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
								<fo:block>
									<xsl:text>Deleted Multiple Choice item (level 1)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG1/XHIBITdelG1"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG1/CRESTdelG1"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
								<fo:block>
									<xsl:text>Deleted Multiple Choice item (level 2)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG2/XHIBITdelG2"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG2/CRESTdelG2"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
			<xsl:call-template name="empty"/>
		</xsl:for-each>
	</xsl:template>

	<!--Charges Details -->
	<xsl:template name="chargesDetails" match="chargesDetails">
		<xsl:for-each select="charges/charge">
			<xsl:call-template name="empty"/>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
					<fo:block>
						<xsl:text>Charge Details</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:call-template name="empty"/>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>Charge type</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm" number-columns-spanned="2">
					<fo:block>
						<xsl:value-of select="typeofcharge"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
					<fo:block>
						<xsl:text>XHIBIT charge id</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:value-of select="XHIBITchargeID"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="solid" margin="5mm">
					<fo:block>
						<xsl:text>N/A</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:if test="chargeseqno/XHIBITchargeseqno != '' or chargeseqno/CRESTchargeseqno != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Charge sequence number</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="chargeseqno/XHIBITchargeseqno"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="chargeseqno/CRESTchargeseqno"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="breachorigsentence/XHIBITbreachorigsentence != '' or breachorigsentence/CRESTbeachorigsentence != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Breach - original sentence</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachorigsentence/XHIBITbreachorigsentence"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachorigsentence/CRESTbeachorigsentence"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="breachorigsentencedate/XHIBITbreachorigsentencedate != '' or breachorigsentencedate/CRESTbreachorigsetencedate != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Breach - original sentence date</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachorigsentencedate/XHIBITbreachorigsentencedate"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachorigsentencedate/CRESTbreachorigsetencedate"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="breachorigcourtcode/XHIBITbreachorigcourtcode != '' or breachorigcourtcode/CRESTbreachorigcourtcode != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Breach - original court code</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachorigcourtcode/XHIBITbreachorigcourtcode"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachorigcourtcode/CRESTbreachorigcourtcode"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="breachdateput/XHIBITbreachdateput != '' or breachdateput/CRESTbreachdateput != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Breach - date put</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachdateput/XHIBITbreachdateput"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachdateput/CRESTbreachdateput"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="breachcommittalforbreach/XHIBITbreachcommittalforbreach != '' or breachcommittalforbreach/CRESTbreachcommittalforbreach != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Breach - committal for breach</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachcommittalforbreach/XHIBITbreachcommittalforbreach"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachcommittalforbreach/CRESTbreachcommittalforbreach"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="breachadmitted/XHIBITbreachadmitted!= '' or breachadmitted/CRESTbreachadmitted!= ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
						<fo:block>
							<xsl:text>Breach - admitted</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachadmitted/XHIBITbreachadmitted"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="breachadmitted/CRESTbreachadmitted"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:call-template name="offenceDetails"/>
		</xsl:for-each>
	</xsl:template>

	<!-- Offence details -->
	<xsl:template name="offenceDetails" match="offenceDetails">
		<xsl:for-each select="offences/offence">
			<xsl:call-template name="empty"/>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="8mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
					<fo:block>
						<xsl:text>Offence Details</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:call-template name="empty"/>
			<xsl:if test="XHIBIToffenceid != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
						<fo:block>
							<xsl:text>XHIBIT offence id</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="XHIBIToffenceid"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:text>N/A</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="offencecodes/XHIBIToffencecode != '' or offencecodes/CRESToffencecode!= ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
						<fo:block>
							<xsl:text>Offence code</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="offencecodes/XHIBIToffencecode"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="offencecodes/CRESToffencecode"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="offencedescriptions/XHIBIToffencedescription!= '' or offencedescriptions/CRESToffencedescription!= ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
						<fo:block>
							<xsl:text>Offence description</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="offencedescriptions/XHIBIToffencedescription"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="offencedescriptions/CRESToffencedescription"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="offenceseqnos/XHIBIToffenceseqno != '' or offenceseqnos/CRESToffenceseqno!= ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="8mm" font-weight="bold">
						<fo:block>
							<xsl:text>Offence sequence number</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="offenceseqnos/XHIBIToffenceseqno"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="offenceseqnos/CRESToffenceseqno"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:call-template name="defendantOnOffenceDetails"/>
		</xsl:for-each>
	</xsl:template>

	<!-- defendantOnOffenceDetails -->
	<xsl:template name="defendantOnOffenceDetails" match="defendantOnOffenceDetails">
		<xsl:for-each select="defendantonoffences/defendantonoffence">
			<xsl:call-template name="empty"/>
			<fo:table-row>
				<fo:table-cell border-style="solid" margin="11mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
					<fo:block>
						<xsl:text>Defendant On Offence Details</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:call-template name="empty"/>
			<xsl:if test="defendantids/XHIBITdefendantid != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="11mm" font-weight="bold">
						<fo:block>
							<xsl:text>XHIBIT defendant id</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantids/XHIBITdefendantid"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:text>N/A</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="defendantids/XHIBITcrestdefendantid != '' or defendantids/CRESTdefendantid != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="11mm" font-weight="bold">
						<fo:block>
							<xsl:text>CREST defendant id</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantids/XHIBITcrestdefendantid "/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantids/CRESTdefendantid"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="defendantonoffenceforename/XHIBITdefendantonoffenceforename != '' or defendantonoffenceforename/CRESTdefendantonoffenceforename != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="11mm" font-weight="bold">
						<fo:block>
							<xsl:text>Defendant First Name</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantonoffenceforename/XHIBITdefendantonoffenceforename"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantonoffenceforename/CRESTdefendantonoffenceforename"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="defendantonoffencelastname/XHIBITdefendantonoffencelastname != '' or defendantonoffencelastname/CRESTdefendantonoffencelastname != ''">
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="11mm" font-weight="bold">
						<fo:block>
							<xsl:text>Defendant Surname</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantonoffencelastname/XHIBITdefendantonoffencelastname"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" margin="5mm">
						<fo:block>
							<xsl:value-of select="defendantonoffencelastname/CRESTdefendantonoffencelastname"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<!-- Check if any Plea Information -->
			<!-- Only Display section if CaseType is Trial -->
			<xsl:if test="Plea and /resyncreport/casetype='T'">
				<xsl:call-template name="empty"/>
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="14mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
						<fo:block>
							<xsl:text>Plea on Count or Summary Offence</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:call-template name="empty"/>
				<xsl:if test="Plea/plea/XHIBITplea !='' or Plea/plea/CRESTplea !=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Plea</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/plea/XHIBITplea"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/plea/CRESTplea"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="Plea/plealesseroff/XHIBITplealesseroff!='' or Plea/plealesseroff/CRESTplealesseroff!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Alternate/Lesser Offence</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/plealesseroff/XHIBITplealesseroff"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/plealesseroff/CRESTplealesseroff"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="Plea/ArraignmentDate/XHIBITArraignmentDate!='' or Plea/ArraignmentDate/CRESTArraignmentDate!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Arraignment Date</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/ArraignmentDate/XHIBITArraignmentDate"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/ArraignmentDate/CRESTArraignmentDate"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="Plea/OtherPleaText/XHIBITOtherPleaText!='' or Plea/OtherPleaText/CRESTOtherPleaText!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Plea Text ('Other' Plea)</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/OtherPleaText/XHIBITOtherPleaText"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Plea/OtherPleaText/CRESTOtherPleaText"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:call-template name="empty"/>
			</xsl:if>
			<!-- Check if any Verdict Information -->
			<!-- Only Display section if CaseType is Trial -->
			<xsl:if test="Verdict and /resyncreport/casetype='T'">
				<xsl:call-template name="empty"/>
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="14mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
						<fo:block>
							<xsl:text>Verdict on Count</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:call-template name="empty"/>
				<xsl:if test="Verdict/VERDICTverdict/XHIBITverdict!='' or Verdict/VERDICTverdict/CRESTverdict!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Verdict</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTverdict/XHIBITverdict"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTverdict/CRESTverdict"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="Verdict/VERDICTjurorsAssenting/XHIBITjurorsAssenting!='' or Verdict/VERDICTjurorsAssenting/CRESTjurorsAssenting!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Jurors Assenting</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTjurorsAssenting/XHIBITjurorsAssenting"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTjurorsAssenting/CRESTjurorsAssenting"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="Verdict/VERDICTjurorsDissenting/XHIBITjurorsDissenting!='' or Verdict/VERDICTjurorsDissenting/CRESTjurorsDissenting!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Jurors Dissenting</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTjurorsDissenting/XHIBITjurorsDissenting"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTjurorsDissenting/CRESTjurorsDissenting"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="Verdict/VERDICTOtherVerdictText/XHIBITotherVerdictText!='' or Verdict/VERDICTOtherVerdictText/CRESTotherVerdictText!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Verdict Text ('Other' Verdict)</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTOtherVerdictText/XHIBITotherVerdictText"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="Verdict/VERDICTOtherVerdictText/CRESTotherVerdictText"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
			</xsl:if>
			<!-- Check if any VCO Information -->
			<!-- Only Display section if CaseType is Trial -->
			<xsl:if test="vcoDetails and /resyncreport/casetype='T'">
				<xsl:call-template name="empty"/>
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="14mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
						<fo:block>
							<xsl:text>Verdict/Conviction or Other Details</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:call-template name="empty"/>
				<xsl:if test="vcoDetails/VCOFlag/XHIBITVCOFlag!='' or vcoDetails/VCOFlag/CRESTVCOFlag!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Verdict/Conviction/Other Indicator</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="vcoDetails/VCOFlag/XHIBITVCOFlag"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="vcoDetails/VCOFlag/CRESTVCOFlag"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="vcoDetails/VCODate/XHIBITVCODate!='' or vcoDetails/VCODate/CRESTVCODate!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Verdict/Conviction/Other Date</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="vcoDetails/VCODate/XHIBITVCODate"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="vcoDetails/VCODate/CRESTVCODate"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
			</xsl:if>
			<!-- Check if any Criminal Appeal Offence Level Results Information -->
			<!-- Only Display section if CaseType is Appeal -->
			<xsl:if test="AppealResult and /resyncreport/casetype='A'">
				<xsl:call-template name="empty"/>
				<fo:table-row>
					<fo:table-cell border-style="solid" margin="14mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
						<fo:block>
							<xsl:text>Criminal Appeal Offence Level Result</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:call-template name="empty"/>
				<xsl:if test="AppealResult/appealoffenceResult/XHIBITappealoffenceResult!='' or AppealResult/appealoffenceResult/CRESTappealoffenceResult!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Appeal Result</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="AppealResult/appealoffenceResult/XHIBITappealoffenceResult"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="AppealResult/appealoffenceResult/CRESTappealoffenceResult"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="AppealResult/appealoffenceresultDate/XHIBITappealoffenceresultDate!='' or AppealResult/appealoffenceresultDate/CRESTappealoffenceresultDate!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Appeal Result Date</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="AppealResult/appealoffenceresultDate/XHIBITappealoffenceresultDate"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="AppealResult/appealoffenceresultDate/CRESTappealoffenceresultDate"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="AppealResult/appealresultlesseroff/XHIBITappealresultlesseroff!='' or AppealResult/appealresultlesseroff/CRESTappealresultlesseroff!=''">
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Lesser Offence Substituted (ACALO Result)</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="AppealResult/appealresultlesseroff/XHIBITappealresultlesseroff"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="AppealResult/appealresultlesseroff/CRESTappealresultlesseroff"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
			</xsl:if>
			<!-- Check if any disposals to display -->
			<xsl:if test="Disposals/Disposal">
				<xsl:for-each select="Disposals/Disposal">
					<xsl:call-template name="empty"/>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
							<fo:block>
								<xsl:text>Offence Related Disposals</xsl:text>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:call-template name="empty"/>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Disposal Code</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalCode/XHIBITdisposalCode"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalCode/CRESTdisposalCode"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Disposal Template Version</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposaltemplateVersion/XHIBITtemplateVersion"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposaltemplateVersion/CRESTemplateVersion"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
							<fo:block>
								<xsl:text>Disposal Court Type</xsl:text>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalcourtType/XHIBITdisposalcourtType"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" margin="5mm">
							<fo:block>
								<xsl:value-of select="disposalInfo/disposalcourtType/CRESTdisposalcourtType"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<!-- Check if any Disposal Line Info -->
					<xsl:if test="DISPOSALdisposalLine">
						<xsl:call-template name="empty"/>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="14mm" font-weight="bold" number-columns-spanned="3" background-color="silver" font-style="italic">
								<fo:block>
									<xsl:text>Disposal Line Details</xsl:text>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:call-template name="empty"/>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
								<fo:block>
									<xsl:text>Data</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/Data/XHIBITData"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/Data/CRESTData"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
								<fo:block>
									<xsl:text>Deleted Data</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/DelData/XHIBITdelData"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/DelData/CRESTdelData"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
								<fo:block>
									<xsl:text>Deleted Multiple Choice item (level 1)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG1/XHIBITdelG1"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG1/CRESTdelG1"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell border-style="solid" margin="14mm" font-weight="bold">
								<fo:block>
									<xsl:text>Deleted Multiple Choice item (level 2)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG2/XHIBITdelG2"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" margin="5mm">
								<fo:block>
									<xsl:value-of select="DISPOSALdisposalLine/delG2/CRESTdelG2"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
			<xsl:call-template name="empty"/>
		</xsl:for-each>
	</xsl:template>

	<!--empty thick line-->
	<xsl:template name="empty" match="empty">
		<fo:table-row>
			<fo:table-cell border-style="solid" margin="5mm" font-weight="bold">
				<fo:block>
					<xsl:text> </xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:text> </xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" margin="5mm">
				<fo:block>
					<xsl:text> </xsl:text>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>