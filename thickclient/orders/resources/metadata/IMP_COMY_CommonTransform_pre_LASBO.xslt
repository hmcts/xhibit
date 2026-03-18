<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ***************************************************************************************************************** -->
	<!-- COMMITMENT TO YOUNG OFFENDERS ORDER AND IMPRISONMENT ORDER COMMON START -->
	<!-- ***************************************************************************************************************** -->
	<!-- convicted of crime -->
	<xsl:template match="nar:COMYIMP_ConvictedOfCrime_pre_LASBO">
		<fo:inline>
			<xsl:text>was convicted of crime on </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display the Conviction Date -->
	<xsl:template match="nar:COMYIMP_ConvictionDate_pre_LASBO">
		<xsl:call-template name="FormatDate">
			<xsl:with-param name="date" select="$baseAll/ord:ConvictionDate"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Court ordered -->
	<xsl:template match="nar:COMYIMP_CourtOrdered_pre_LASBO">
		<fo:inline>
			<xsl:text>The Court ordered</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Custodial Sentence -->
	<xsl:template match="nar:COMYIMP_CustodialSentences_pre_LASBO">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'indeterminate' ">
					<xsl:text>Indeterminate sentence for public protection</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Custodial sentences</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Chapter III title Text1-->
	<xsl:template match="nar:COMYIMP_ChapterIII_Title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Required custodial</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Chapter III title Text2-->
	<xsl:template match="nar:COMYIMP_ChapterIII_Title2_pre_LASBO">
		<fo:inline>
			<xsl:text>sentences for certain</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Chapter III title Text3-->
	<xsl:template match="nar:COMYIMP_ChapterIII_Title3_pre_LASBO">
		<fo:inline>
			<xsl:text>offences</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Chapter III text -->
	<xsl:template match="nar:COMYIMP_ChapterIII_Text_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of Chapter III of Part V of the Powers of Criminal Courts (Sentencing) Act 2000 should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Drug Trafficking Title -->
	<xsl:template match="nar:COMYIMP_DrugTraffickingTitle_pre_LASBO">
		<fo:inline>
			<xsl:text>Third Class A Drug Trafficking Offence</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Display Serious Offence Text -->
	<xsl:template match="nar:COMYIMP_DrugTrafficking_pre_LASBO">
		<xsl:text>The court ordered that a minimum seven years sentence for a third Class A drug trafficking offence, pursuant to section 110(1)(2)(3) of the Powers of the Criminal Courts (Sentencing) Act 2000, should apply to the defendant.</xsl:text>
	</xsl:template>
	<!-- Domestic Burglary title -->
	<xsl:template match="nar:COMYIMP_DomesticBurglaryTitle_pre_LASBO">
		<fo:inline>
			<xsl:text>Third Domestic Burglary</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Display Minimum Sentence Text -->
	<xsl:template match="nar:COMYIMP_DomesticBurglary_pre_LASBO">
		<xsl:text>The court ordered that a minimum three years sentence for a third domestic burglary, pursuant to section 111(1)(2)(5)(6) of the Powers of the Criminal Courts (Sentencing) Act 2000, should apply to the defendant.</xsl:text>
	</xsl:template>
	<!-- Extended Sentence Title text1 -->
	<xsl:template match="nar:COMYIMP_ExtendedSentence_Title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Extended Sentence</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Title text2 -->
	<xsl:template match="nar:COMYIMP_ExtendedSentence_Title2_pre_LASBO">
		<fo:inline>
			<xsl:text>(for sexual or violent</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Title text3 -->
	<xsl:template match="nar:COMYIMP_ExtendedSentence_Title3_pre_LASBO">
		<fo:inline>
			<xsl:text>offences)</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text1 -->
	<xsl:template match="nar:COMYIMP_ES_Text1_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 85 of the Powers of Criminal Courts (Sentencing) Act 2000 to an extended sentence of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template used to add two dates -->
	<xsl:template match="nar:COMYIMP_Days_pre_LASBO">
		<!-- years value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Years + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
        <!-- months value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
        <!-- days value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Days + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> day(s)
    </xsl:template>
	<!-- Extended Sentence Text2 -->
	<xsl:template match="nar:COMYIMP_ES_Text2_pre_LASBO">
		<fo:inline>
			<xsl:text> comprising </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text3 -->
	<xsl:template match="nar:COMYIMP_ES_Text3_pre_LASBO">
		<fo:inline>
			<xsl:text>a custodial term of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Custodial Term Details -->
	<xsl:template match="nar:COMYIMP_CustDays_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="TermIncDays"/>
			<xsl:text> </xsl:text>
		</xsl:for-each>
	</xsl:template>
	<!-- Extended Sentence Text4 -->
	<xsl:template match="nar:COMYIMP_ES_Text4_pre_LASBO">
		<fo:inline>
			<xsl:text>an extension period of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Extended Term Details including days-->
	<xsl:template match="nar:COMYIMP_ExPeriod_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="TermIncDays"/>
			<xsl:text> </xsl:text>
		</xsl:for-each>.
    </xsl:template>
	<!-- Template to display Section 44 details -->
	<xsl:template match="nar:COMYIMP_Section44_pre_LASBO">
		<xsl:if test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:Section44/@selected='true'">
			<fo:block>
                The provisions of section 44 of the Criminal Justice Act 1991, as substituted by section 59 of the Crime and Disorder Act 1998 (as amended by paragraph 141 of Schedule 9 to the Powers of Criminal Courts (Sentencing) Act 2000) apply in this case.
            </fo:block>
		</xsl:if>
	</xsl:template>
	<!-- Return defendant line 1 -->
	<xsl:template match="nar:COMYIMP_ReturnDefendant_Line1_pre_LASBO">
		<fo:inline>
			<xsl:text>The offence for which the defendant has been convicted by this court was committed </xsl:text>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:ReturnToImprisonment/ord:OffenceDate/@Qualifier='on'">
                    on
                </xsl:when>
				<xsl:when test="$baseAll/ord:ReturnToImprisonment/ord:OffenceDate/@Qualifier='on or before'">
                    no later than
                </xsl:when>
			</xsl:choose>
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:ReturnToImprisonment/ord:OffenceDate"/>
			</xsl:call-template>
			<xsl:text>.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Return defendant line 2 -->
	<xsl:template match="nar:COMYIMP_ReturnDefendant_Line2_pre_LASBO">
		<fo:inline>
			<xsl:text>This date was, or appeared to be, earlier than the date on which the defendant would, but for the defendant's release under Part II of the Criminal Justice Act 1991, have completed serving the full sentence imposed by </xsl:text>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:ReturnToImprisonment/ord:SentencingCourt/ord:CourtHouse/ord:CourtHouseType=$CrownCourt">
                    the <xsl:call-template name="CallableSentencingCourtType"/> at <xsl:call-template name="CallableSentencingCourtName"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="CallableSentencingCourtName"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> on </xsl:text>
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:ReturnToImprisonment/ord:SentencingCourt/ord:Date"/>
			</xsl:call-template>
		</fo:inline>
	</xsl:template>
	<xsl:template name="CallableSentencingCourtType">
		<xsl:for-each select="$baseAll/ord:ReturnToImprisonment/ord:SentencingCourt/ord:CourtHouse">
			<xsl:call-template name="PrecedingCourtHouseType"/>
		</xsl:for-each>
	</xsl:template>
	<!-- Template to display Court Type details -->
	<!-- Template to display Sentence Court -->
	<xsl:template name="CallableSentencingCourtName">
		<xsl:value-of select="$baseAll/ord:ReturnToImprisonment/ord:SentencingCourt/ord:CourtHouse/ord:CourtHouseName"/>
	</xsl:template>
	<!-- Return defendant line 3 -->
	<xsl:template match="nar:COMYIMP_ReturnDefendant_Line3_pre_LASBO">
		<fo:inline>
			<xsl:text>The court applied its powers under section 116 of the Powers of Criminal Courts (Sentencing) Act 2000 and ordered that  the defendant be returned to </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display return period -->
	<xsl:template match="nar:COMYIMP_ReturnPeriod_pre_LASBO">
		<xsl:text> for </xsl:text>
		<xsl:if test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/ord:Max116='yes'">
			<xsl:text>the maximum period specified by section 116.</xsl:text>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/ord:Max116='no'">
			<xsl:value-of select="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/ord:PeriodInMonths"/> month(s).
        </xsl:if>
	</xsl:template>
	<!-- Template to display Served Period -->
	<xsl:template name="IMPServedPeriod">
		<xsl:if test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType='Before'">
            before
        </xsl:if>
		<xsl:if test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType='Concurrent'">
            concurrently with
        </xsl:if>
	</xsl:template>
	<!-- Template to display total period of return details -->
	<xsl:template match="nar:COMYIMP_TotalPeriod_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:ReturnToImprisonment">
			<xsl:if test="ord:TotalPeriodOfReturn/@selected='true'">
				<fo:block>
					<xsl:text>The total of the period of return </xsl:text>
					<xsl:if test="ord:TotalPeriodOfReturn/@IncludesNewOffenceTerm='yes'">
						<xsl:text> (and of any custodial term for a new offence) </xsl:text>
					</xsl:if>
					<xsl:text> is </xsl:text>
					<xsl:value-of select="ord:TotalPeriodOfReturn"/>
					<xsl:text> month(s) and because this total period of imprisonment is 12 months or less, Section 40A of the Criminal Justice Act 1991, as substituted by section 116 of the Powers of Criminal Courts (Sentencing) Act 2000, applies.</xsl:text>
				</fo:block>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- Release Prisoners Title 1 -->
	<xsl:template match="nar:COMYIMP_ReleasePrisoners_Title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Release of discretionary</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Release Prisoners Title 2 -->
	<xsl:template match="nar:COMYIMP_ReleasePrisoners_Title2_pre_LASBO">
		<fo:inline>
			<xsl:text>life prisoners</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Release Prisoners text 1 -->
	<xsl:template match="nar:COMYIMP_ReleasePrisoners_Text1_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of section 28 of the Crime Sentences Act 1997, as substituted by paragraph 182 of Schedule 9 to the Powers of Criminal Courts (Sentencing) Act 2000, should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Release Prisoners text 2 -->
	<xsl:template match="nar:COMYIMP_ReleasePrisoners_Text2_pre_LASBO">
		<fo:inline>
			<xsl:text>The court specified the 'relevant part' of the sentence to be </xsl:text>
			<xsl:for-each select="$baseAll/ord:Section28/ord:DiscretionaryRelevantPart">
				<xsl:call-template name="Term"/>
			</xsl:for-each>
			<xsl:text>.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Section 86 title 1 -->
	<xsl:template match="nar:COMYIMP_Section86_Title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Release of prisoners</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Section 86 title 2 -->
	<xsl:template match="nar:COMYIMP_Section86_Title2_pre_LASBO">
		<fo:inline>
			<xsl:text>who have committed</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Section 86 title 3 -->
	<xsl:template match="nar:COMYIMP_Section86_Title3_pre_LASBO">
		<fo:inline>
			<xsl:text>sexual offences before</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Section 86 title 4 -->
	<xsl:template match="nar:COMYIMP_Section86_Title4_pre_LASBO">
		<fo:inline>
			<xsl:text>30th September 1998</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Section 86 text -->
	<xsl:template match="nar:COMYIMP_Section86_Text_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of section 86 of the Powers of Criminal Courts (Sentencing) Act 2000 should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Additional Req title-->
	<xsl:template match="nar:COMYIMP_AddReq_Title_pre_LASBO">
		<fo:inline>
			<xsl:text>Additional Notes</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Additional Notes details -->
	<xsl:template match="nar:COMYIMP_AdditionalNotes_pre_LASBO">
		<xsl:call-template name="FormatTextArea">
			<xsl:with-param name="string" select="$baseAll/ord:AdditionalNotes"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Credit for time on bail -->
	<xsl:template match="nar:COMYIMP_CreditForRemand_title_pre_LASBO">
		<fo:inline>
			<xsl:text>Credit for time served on remand</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_CreditForRemand_pre_LASBO">
		<fo:inline>
			<xsl:text>Under section 240 of the Criminal Justice Act 2003, the court directs that </xsl:text>
			<xsl:for-each select="$baseAll/ord:CreditForRemand/ord:Term">
				<xsl:call-template name="TermOnlyDays"/>
				<xsl:text> spent in custody on remand will count towards the sentence.</xsl:text>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_CreditForBail_title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Credit for time spent on bail</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_CreditForBail_title2_pre_LASBO">
		<fo:inline>
			<xsl:text>with an electronically</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_CreditForBail_title3_pre_LASBO">
		<fo:inline>
			<xsl:text>monitored curfew condition</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_CreditForBail_pre_LASBO">
		<fo:inline>
			<xsl:text>Under section 240A of the Criminal Justice Act 2003, the court directs that </xsl:text>
			<xsl:for-each select="$baseAll/ord:CreditForBail/ord:Term">
				<xsl:call-template name="TermOnlyDays"/>
				<xsl:text> will count towards the sentence.</xsl:text>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_IndeterminateSentence_title1_pre_LASBO">
		<fo:inline>
			<xsl:text>Indeterminate sentence</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_IndeterminateSentence_title2_pre_LASBO">
		<fo:inline>
			<xsl:text>for public protection</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_AutomaticDeportation_title_pre_LASBO">
		<fo:inline>
			<xsl:text>Automatic deportation</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_RecommendedDeportation_title_pre_LASBO">
		<fo:inline>
			<xsl:text>Recommendation for deportation</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_AutomaticDeportation_text_pre_LASBO">
		<fo:inline>
			<xsl:text>As a consequence of the sentence, the provisions of section 32 UK Borders Act 2007 will apply in this case.</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_RecommendedDeportation_text_pre_LASBO">
		<fo:inline>
			<xsl:text>The court, being satisfied that the defendant had been given such notice as is required under section 6(2) of the Immigration Act 1971 at least 7 days before the date of this order, recommended that a deportation order be made in this case.</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:COMYIMP_RecommendedDeportation_pre_LASBO">
		<fo:inline>
			<xsl:text>Recommendation for deportation</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- ************************************************************************************************************** -->
	<!-- COMMITMENT TO YOUNG OFFENDERS ORDER AND IMPRISONMENT ORDER COMMON END -->
	<!-- ************************************************************************************************************** -->
</xsl:stylesheet>
