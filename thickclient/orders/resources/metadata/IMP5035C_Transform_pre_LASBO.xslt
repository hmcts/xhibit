<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- *************************************-->
	<!-- IMPRISONMENT ORDER START -->
	<!-- *************************************-->
	<!-- IMPO5035 title -->
	<xsl:template match="nar:IMPO5035C_Title_pre_LASBO">
		<fo:inline>
			<xsl:text>Order for imprisonment</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- IMPO5035 Sub title -->
	<xsl:template match="nar:IMPO5035C_SubTitle_pre_LASBO">
		<fo:inline>
			<xsl:text>Made under the Criminal Justice Act 2003</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Defendant Details -->
	<xsl:template match="nar:IMPO5035C_DefendantDetails_pre_LASBO">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Template used to display Committed for Sentence Details -->
	<xsl:template match="nar:IMPO5035C_CommittedForSentence_pre_LASBO">
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
	<!-- Thr court ordered ending text -->
	<xsl:template match="nar:IMPO5035C_PeriodOfImprisonment_pre_LASBO">
		<fo:inline>
			<xsl:text> that the defendant serve a period of imprisonment, details of which follow.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Custodial Sentence information  -->
	<xsl:template match="nar:IMPO5035C_SentenceText_pre_LASBO">
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
				<!-- Custodial Sentence Options (life and imprisonment start-->
				<xsl:otherwise>
					<xsl:text>The court ordered that the defendant be sentenced to </xsl:text>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
						imprisonment for life.		
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
							<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
								<xsl:call-template name="TermDuration"/>
								<xsl:text> imprisonment.</xsl:text>
							</xsl:for-each>
						</xsl:when>
					</xsl:choose>
				</xsl:otherwise>
				<!-- Custodial Sentence Options (life and imprisonment end-->
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Custodial Sentence details -->
	<xsl:template match="nar:IMPO5035C_SentenceOption_pre_LASBO">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:TermType!='Not Applicable'">
				<xsl:choose>
					<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
						<fo:block space-before="12pt">
                            This sentence was ordered to be
                            <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:TermType">
								<xsl:call-template name="TermType"/>
							</xsl:for-each>
                            any other periods of imprisonment to which the defendant was subject prior to the making of this order.
                        </fo:block>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- EXTENDED SENTENCE TEMPLATES START -->
	<!-- Extended Sentence Text line 1 -->
	<xsl:template match="nar:IMPO5035C_ES_Text1_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 227 of the Criminal Justice Act 2003 to an extended sentence of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Display Imprisonment extended concurrency -->
	<xsl:template match="nar:IMPO5035C_ExSentence_pre_LASBO">
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
	<xsl:template match="nar:IMPO5035C_ES_Text2_pre_LASBO">
		<fo:inline>
			<xsl:text> comprising </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text3 -->
	<xsl:template match="nar:IMPO5035C_ES_Text3_pre_LASBO">
		<fo:inline>
			<xsl:text>a custodial term of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Custodial Term Details -->
	<xsl:template match="nar:IMPO5035C_CustDays_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="TermDuration"/>
			<xsl:text> and </xsl:text>
		</xsl:for-each>
	</xsl:template>
	<!-- Extended Sentence Text4 -->
	<xsl:template match="nar:IMPO5035C_ES_Text4_pre_LASBO">
		<fo:inline>
			<xsl:text>an extension period of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Extended Term Details including days-->
	<xsl:template match="nar:IMPO5035C_ExPeriod_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="TermDuration"/>
			<xsl:text> </xsl:text>
		</xsl:for-each>.
    </xsl:template>
	<!-- Template used to add two dates -->
	<xsl:template match="nar:IMPO5035C_Days_pre_LASBO">
		<!-- years value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Years + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
        <!-- months value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
        <!-- days value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Days + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> day(s)
    </xsl:template>
	<!-- EXTENDED SENTENCE TEMPLATES END -->
	<!-- Domestic Burglary title -->
	<xsl:template match="nar:IMPO5035C_DomesticBurglaryTitle_pre_LASBO">
		<fo:inline>
			<xsl:text>Domestic Burglary</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Display Domestic Burglary Text -->
	<xsl:template match="nar:IMPO5035C_DomesticBurglary_pre_LASBO">
		<xsl:text>The court was satisfied that section 111 of the Powers of Criminal Courts (Sentencing) Act 2000 applied in this case.</xsl:text>
	</xsl:template>
	<!-- Return Defendant title 1 -->
	<xsl:template match="nar:IMPO5035C_ReturnDefendant_Title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Return of defendants</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Return Defendant title 2 -->
	<xsl:template match="nar:IMPO5035C_ReturnDefendant_Title2_pre_LASBO">
		<fo:inline>
			<xsl:text>to prison</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Prison -->
	<xsl:template match="nar:IMPO5035C_Prison_pre_LASBO">
		<fo:inline>
			<xsl:text> prison </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Imprisonment Return Period details -->
	<xsl:template match="nar:IMPO5035C_Return_pre_LASBO">
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
	<xsl:template match="nar:IMPO5035C_ChapterIII_Text_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of Chapter 5 of Part 12 of the Criminal Justice Act 2003 should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- DeportationReasons Text -->
	<xsl:template match="nar:IMPO5035C_Deportation_Title_pre_LASBO">
		<fo:inline>
			<xsl:text>Automatic Deportation </xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:IMPO5035C_Deportation_Text_pre_LASBO">
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
	<!-- ***********************************-->
	<!-- IMPRISONMENT ORDER END -->
	<!-- ***********************************-->
</xsl:stylesheet>
