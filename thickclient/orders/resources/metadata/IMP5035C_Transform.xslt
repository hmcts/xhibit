<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- *************************************-->
	<!-- IMPRISONMENT ORDER START -->
	<!-- *************************************-->
	<!-- IMPO5035 title -->
	<xsl:template match="nar:IMPO5035C_Title">
		<fo:inline>
			<xsl:text>Order for imprisonment</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- IMPO5035 Sub title -->
	<!-- Defendant Details -->
	<xsl:template match="nar:IMPO5035C_DefendantDetails">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Template used to display Committed for Sentence Details -->
	<xsl:template match="nar:IMPO5035C_CommittedForSentence">
		<xsl:if test="$baseAll/ord:CommittingCourt/@selected ='true'">
			<fo:block>
                at <xsl:value-of select="$baseAll/ord:CommittingCourt/ord:CourtHouseName"/>
				<!-- magistrates court--> and committed for sentence to the Crown Court.
            </fo:block>
		</xsl:if>
		<fo:block space-before="12pt">
            Details of the conviction and sentence are on the court record.
        </fo:block>
	</xsl:template>
	<!-- Template used to show Risk or Vulnerability details -->
	<xsl:template match="nar:IMPO5035C_RiskOrVulnerability">
		<fo:inline>
			<xsl:if test="$baseAll/ord:RiskVulnerabilityFactors/@selected = 'true'">
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="$baseAll/ord:RiskVulnerabilityFactors/ord:RiskText"/>
				</xsl:call-template>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Thr court ordered ending text -->
	<xsl:template match="nar:IMPO5035C_PeriodOfImprisonment">
		<fo:inline>
			<xsl:text> that the defendant serve a period of imprisonment, details of which follow.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Custodial Sentence information  -->
	<xsl:template match="nar:IMPO5035C_SentenceText">
		<fo:inline>
			<xsl:choose>
				<!-- Indeterminate Sentence Option start -->
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'indeterminate' ">
					<xsl:text>The court ordered that the provisions of section 225 of the Criminal Justice Act 2003 should apply to the defendant. The court specified the defendant must serve a minimum term of </xsl:text>
					<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
						<xsl:call-template name="TermDuration"/>
						<xsl:text>.</xsl:text>
					</xsl:for-each>
				</xsl:when>
				<!-- Indeterminate Sentence Option end -->
				<!-- Extended Sentence Option start -->
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'extended' ">
					<xsl:text>The Court</xsl:text>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's226a'" >
							<xsl:text>, under Section 226A Criminal Justice Act 2003,</xsl:text>
							<xsl:text> ordered that the defendant serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's279' ">
							<xsl:text> ordered that the defendant be sentenced, under Section 279 Sentencing Act 2020, to serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's282a' ">
							<xsl:text> ordered that the defendant be sentenced, under Section 282A Sentencing Act 2020, to serve an extended sentence of </xsl:text>
						</xsl:when>
					</xsl:choose>
					<!-- years value -->
					<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:CustodialPeriod/ord:Years + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:CustodialPeriod/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
					<!-- months value -->
					<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:CustodialPeriod/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
					<!-- weeks value -->
					<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:CustodialPeriod/ord:Weeks + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Weeks)"/> week(s)
					<!-- days value -->
					<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:CustodialPeriod/ord:Days + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> day(s)
					<xsl:text> comprising a custodial term of </xsl:text>
					<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:CustodialPeriod">
						<xsl:call-template name="TermIncWeeksDays"/>
						<xsl:text> </xsl:text>
					</xsl:for-each>
					<xsl:text>and an extension period of </xsl:text>
					<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
						<xsl:call-template name="TermIncWeeksDays"/>
						<xsl:text> </xsl:text>
					</xsl:for-each>
				</xsl:when>
				<!-- Extended Sentence Option end -->
				<!-- Custodial Sentence Options (life and imprisonment) start-->
				<xsl:otherwise>
					<xsl:text>The Court</xsl:text>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section283 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section285 = 'false'">
									<xsl:text>, under section 224A Criminal Justice Act 2003,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section283 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section285 = 'false'">
									<xsl:text>, under section 225 Criminal Justice Act 2003,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section283 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section285 = 'false'">
									<xsl:text>, under section 283 Sentencing Act 2020,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section285 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section283 = 'false'">
									<xsl:text>, under section 285 Sentencing Act 2020,</xsl:text>
								</xsl:when>
							</xsl:choose>
							<xsl:text> ordered that the defendant be sentenced to imprisonment for life.</xsl:text>
							<xsl:if test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:MinimumTerm/@selected ='true'">
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:MinimumTerm/ord:NoMinimumTerm/@selected ='true'">
										<xsl:text> The Court did not specify a minimum term.</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text> The Court specified that the defendant must serve a minimum term of </xsl:text>
										<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:MinimumTerm/ord:LifePeriod">
											<xsl:call-template name="TermDuration"/>
											<xsl:text>.</xsl:text>
										</xsl:for-each>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
							<xsl:text> ordered that the defendant be sentenced</xsl:text>
							<xsl:if test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@selected ='true'">
								<xsl:text> to</xsl:text>
								<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:CustodialTerm">
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
								<xsl:text> imprisonment</xsl:text>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@TermType ='Concurrent'">
										<xsl:text> (concurrent to S236A sentence below)</xsl:text>
									</xsl:when>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@TermType ='Consecutive'">
										<xsl:text> (consecutive to S236A sentence below)</xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:if test="$baseAll/ord:CustodialSentence/ord:s235-236/@selected ='false'">
									<xsl:text>. </xsl:text>
								</xsl:if>
							</xsl:if>
							<xsl:if test="$baseAll/ord:CustodialSentence/ord:s235-236/@selected ='true'">
								<xsl:text>, and under Section</xsl:text>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CSSectionType = 'section236a'">
										<xsl:text> 236a</xsl:text>
									</xsl:when>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CSSectionType = 'section278'">
										<xsl:text> 278</xsl:text>
									</xsl:when>
									<xsl:otherwise><xsl:text>##</xsl:text><xsl:value-of select="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CSSectionType" /><xsl:text>##</xsl:text></xsl:otherwise>
								</xsl:choose>
								<xsl:text> Sentencing Act 2020, to serve a Special Custodial Sentence of </xsl:text>
								<!-- years value -->
								<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:s235-236/ord:CustodialPeriod/ord:Years + $baseAll/ord:CustodialSentence/ord:s235-236/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:s235-236/ord:CustodialPeriod/ord:Months + $baseAll/ord:CustodialSentence/ord:s235-236/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
								<!-- months value -->
								<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:s235-236/ord:CustodialPeriod/ord:Months + $baseAll/ord:CustodialSentence/ord:s235-236/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
								<!-- weeks value -->
								<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:s235-236/ord:CustodialPeriod/ord:Weeks + $baseAll/ord:CustodialSentence/ord:s235-236/ord:ExtensionPeriod/ord:Weeks)"/> week(s)
								<!-- days value -->
								<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:s235-236/ord:CustodialPeriod/ord:Days + $baseAll/ord:CustodialSentence/ord:s235-236/ord:ExtensionPeriod/ord:Days)"/> day(s)
								<xsl:text> comprising a custodial term of </xsl:text>
								<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CustodialPeriod">
									<xsl:call-template name="TermIncWeeksDays"/>
									<xsl:text> </xsl:text>
								</xsl:for-each>
								<xsl:text>and an extended licence period of </xsl:text>
								<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:s235-236/ord:ExtensionPeriod">
									<xsl:call-template name="TermIncWeeksDays"/>
								</xsl:for-each>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/@TermType = 'Concurrent'">
										<xsl:text> (concurrent to imprisonment above)</xsl:text>
									</xsl:when>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/@TermType = 'Consecutive'">
										<xsl:text> (consecutive to imprisonment above)</xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:text>. </xsl:text>
							</xsl:if>
						</xsl:when>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Custodial Sentence Options (life and imprisonment end-->
		</fo:inline>
	</xsl:template>
	<!-- Custodial Sentence details -->
	<xsl:template match="nar:IMPO5035C_SentenceOption">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:TermType!='Not Applicable'">
				<fo:block space-before="12pt">
                    This sentence was ordered to be
                    <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:TermType">
						<xsl:call-template name="TermType"/>
					</xsl:for-each>
                    any other periods of imprisonment to which the defendant was subject prior to the making of this order.
                </fo:block>
			</xsl:when>
		</xsl:choose>
		<xsl:if test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
			<xsl:choose>
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'AutomaticLife' or
								$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'DiscretionaryLife' or 
								$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'MandatoryLife'">
					<xsl:text> The Sentence is </xsl:text>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'AutomaticLife'">
					<xsl:text>Automatic Life.</xsl:text>
				</xsl:when>
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'DiscretionaryLife'">
					<xsl:text>Discretionary Life.</xsl:text>
				</xsl:when>
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'MandatoryLife'">
					<xsl:text>Mandatory Life.</xsl:text>
				</xsl:when>
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/ord:LifeType = 'WholeLife'">
					<xsl:text>Total custodial period is life. Early release provisions do not apply - sentence is a whole life order.</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- EXTENDED SENTENCE TEMPLATES START -->
	<!-- Extended Sentence Text line 1 -->
	<xsl:template match="nar:IMPO5035C_ES_Text1">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 227 of the Criminal Justice Act 2003 to an extended sentence of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Display Imprisonment extended concurrency -->
	<xsl:template match="nar:IMPO5035C_ExSentence">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SentenceType!='Not Applicable'">
				<xsl:text>This sentence was ordered to be </xsl:text>
				<xsl:call-template name="IMPExtendedSentenceType"/>
				<xsl:text>any other periods of imprisonment to which the defendant was subject prior to the making of this order. </xsl:text>
			</xsl:when>
		</xsl:choose>
		<fo:inline>
			<xsl:text>The provisions of Chapter 6 of the Criminal Justice Act 2003 apply in this case.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text2 -->
	<xsl:template match="nar:IMPO5035C_ES_Text2">
		<fo:inline>
			<xsl:text> comprising </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text3 -->
	<xsl:template match="nar:IMPO5035C_ES_Text3">
		<fo:inline>
			<xsl:text>a custodial term of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Custodial Term Details -->
	<xsl:template match="nar:IMPO5035C_CustDays">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="TermDuration"/>
			<xsl:text> and </xsl:text>
		</xsl:for-each>
	</xsl:template>
	<!-- Extended Sentence Text4 -->
	<xsl:template match="nar:IMPO5035C_ES_Text4">
		<fo:inline>
			<xsl:text>an extension period of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Extended Term Details including days-->
	<xsl:template match="nar:IMPO5035C_ExPeriod">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="TermDuration"/>
			<xsl:text> </xsl:text>
		</xsl:for-each>.
    </xsl:template>
	<!-- Template used to add two dates -->
	<xsl:template match="nar:IMPO5035C_Days">
		<!-- years value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Years + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
        <!-- months value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
        <!-- days value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Days + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> day(s)
    </xsl:template>
	<!-- EXTENDED SENTENCE TEMPLATES END -->
	<!-- Domestic Burglary title -->
	<xsl:template match="nar:IMPO5035C_DomesticBurglaryTitle">
		<fo:inline>
			<xsl:text>Domestic Burglary</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Display Domestic Burglary Text -->
	<xsl:template match="nar:IMPO5035C_DomesticBurglary">
		<xsl:text>The court was satisfied that section 111 of the Powers of Criminal Courts (Sentencing) Act 2000 applied in this case.</xsl:text>
	</xsl:template>
	<!-- Return Defendant title 1 -->
	<xsl:template match="nar:IMPO5035C_ReturnDefendant_Title1">
		<fo:inline>
			<xsl:text>Return of defendants</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Return Defendant title 2 -->
	<xsl:template match="nar:IMPO5035C_ReturnDefendant_Title2">
		<fo:inline>
			<xsl:text>to prison</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Prison -->
	<xsl:template match="nar:IMPO5035C_Prison">
		<fo:inline>
			<xsl:text> prison </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Imprisonment Return Period details -->
	<xsl:template match="nar:IMPO5035C_Return">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType!='Not Applicable'">
				<xsl:text>This period of imprisonment was ordered to be served </xsl:text>
				<xsl:call-template name="IMPServedPeriod"/>
				<xsl:text> any other periods of imprisonment imposed by the court on the same occasion when this order was made.</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ADDED for CCN400 -->
	<!-- Chapter III text specific for 5035C order -->
	<xsl:template match="nar:IMPO5035C_ChapterIII_Text">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of Chapter 5 of Part 12 of the Criminal Justice Act 2003 should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- DeportationReasons Text -->
	<xsl:template match="nar:IMPO5035C_Deportation_Title">
		<fo:inline>
			<xsl:text>Automatic Deportation </xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:IMPO5035C_Deportation_Text">
		<xsl:variable name="defendantOrAppellant">
			<xsl:choose>
				<xsl:when test="substring(/ord:Order/ord:OrderData/ord:ImprisonmentOrder5035C/ord:OrderHeader/ord:CaseNumber,1,1) = 'A'">appellant</xsl:when>
				<xsl:otherwise>defendant</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='custodial'">
				<fo:inline>
					<xsl:text>
						This </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> is liable to deportation because the </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> is a foreign national and has received a custodial sentence of 12 months or more.
					</xsl:text>
				</fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='suspended'">
				<fo:inline>
					<xsl:text>
						This </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> meets the requirements for automatic deportation because the </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> has breached a suspended sentence of 12 months or more.
					</xsl:text>
				</fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='seriousDrugOffence'">
				<fo:inline>
					<xsl:text>
						This </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> is liable to deportation because the </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> is a foreign national and has been sentenced to a period of imprisonment for a serious drugs offence. 
					</xsl:text>
				</fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='recommendedDeportation'">
				<fo:inline>
					<xsl:text>
						This </xsl:text>
					<xsl:value-of select="$defendantOrAppellant"/>
					<xsl:text> is liable to deportation because the Court recommended deportation regardless of offence or sentence length.
					</xsl:text>
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- Template to voting rights -->
	<xsl:template match="nar:VotingRights">
		Convicted offenders sentenced to imprisonment lose the right to vote while they are detained in custody.
	</xsl:template>
	<!-- ***********************************-->
	<!-- IMPRISONMENT ORDER END -->
	<!-- ***********************************-->
</xsl:stylesheet>
