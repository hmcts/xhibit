<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ****************************************************************** -->
	<!-- COMMITMENT TO YOUNG OFFENDERS ORDER START -->
	<!-- *****************************************************************  -->
	<!-- *************************************************************** -->
	<!--  TEMPLATES SPECIFIC TO  YO544C						 -->
	<!-- **************************************************************  -->
	<!-- YO5044C title -->
	<xsl:template match="nar:YO5044C_Title_pre_LASBO">
		<fo:block>
			<xsl:text>Custodial Order for persons</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:text>under 18 years old</xsl:text>
		</fo:block>
	</xsl:template>
	<!-- Custodial Sentence initial text  -->
	<xsl:template match="nar:YO5044C_CustodialSentencesText_pre_LASBO">
		<fo:inline>
			<xsl:choose>
				<!-- Indeterminate Sentence Option start -->
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'indeterminate' ">
					<xsl:text>The court ordered that the provisions of section 226 of the Criminal Justice Act 2003 should apply to the defendant. 
					The court specified the defendant must serve a minimum term of </xsl:text>
					<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
						<xsl:call-template name="TermDuration"/>
						<xsl:text>.</xsl:text>
					</xsl:for-each>
				</xsl:when>
				<!-- Indeterminate Sentence Option end -->
				<!-- Custodial Sentence Option start -->
				<xsl:otherwise>
					<fo:inline>
						<xsl:text>The court ordered that the defendant be detained</xsl:text>
					</fo:inline>
				</xsl:otherwise>
				<!-- Custodial Sentence Option end-->
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!--Custodial Sentence Imprisonment type -->
	<xsl:template match="nar:YO5044C_ImprisonmentType_pre_LASBO">
		<xsl:choose>
			<!-- Life option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
				<xsl:call-template name="callableYO5044CLifeText"/>
			</xsl:when>
			<!-- His Majesty's pleasure option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='hmpleasure'">
                during His Majesty's pleasure
            </xsl:when>
			<!-- Period (term) option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
				<xsl:call-template name="callableYO5044CDetentionText"/>
				<xsl:call-template name="callableYO5044CExtendedSentence"/>
			</xsl:when>
			<!-- Section 91 option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='false'">
				<xsl:call-template name="callableSection91Text"/>
			</xsl:when>
			<!-- Section91 Life option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section91TermType ='section91life'">
				<xsl:call-template name="callableSection91Text"/>
				<xsl:call-template name="callableYO5044CLifeText"/>
				<xsl:call-template name="callableYO5044CExtendedSentence"/>
			</xsl:when>
			<!-- Section91 Term option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section91TermType ='section91term'">
				<xsl:call-template name="callableSection91Text"/>
				<xsl:call-template name="callableYO5044CDetentionText"/>
				<xsl:call-template name="callableYO5044CExtendedSentence"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- Callable Detention duration -->
	<xsl:template name="callableYO5044CDetentionText_pre_LASBO">
		<xsl:text>for </xsl:text>
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<!-- S.Bachra 22/4/03 Display days value (Tracker 52680) -->
			<xsl:call-template name="TermDuration"/>
		</xsl:for-each>
		<xsl:text> </xsl:text>
	</xsl:template>
	<!-- Callable  Life Details -->
	<xsl:template name="callableYO5044CLifeText_pre_LASBO">
        for life or for public protection for serious offences under section 226 of the Criminal Justice Act 2003.	
    </xsl:template>
	<!-- Extended Sentence text line 1 (specific to YO5044C) -->
	<xsl:template match="nar:YO5044C_ES_Text1_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 228 of the Criminal Justice Act 2003 to an extended sentence of </xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Callable Template to display ExtendedSentence -->
	<xsl:template name="callableYO5044CExtendedSentence_pre_LASBO">
		<xsl:if test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/@selected='true'">
	        extended under section 228 of the Criminal Justice Act 2003
		</xsl:if>
	</xsl:template>
	<!-- Domestic Burglary Title -->
	<xsl:template match="nar:YO5044C_DomesticBurglaryTitle_pre_LASBO">
		<fo:inline>
			<xsl:text>Domestic Burglary</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Domestic Burglary Text -->
	<xsl:template match="nar:YO5044C_DomesticBurglary_pre_LASBO">
		<xsl:text>The court was satisfied that section 111 of the Powers of the Criminal Courts (Sentencing) Act 2000 applied in this case.</xsl:text>
	</xsl:template>
	<!-- *************************************************************** -->
	<!--  TEMPLATES SPECIFIC TO  YO544D 						 -->
	<!-- **************************************************************  -->
	<!-- YO5044D title -->
	<xsl:template match="nar:YO5044D_Title_pre_LASBO">
		<fo:block>
			<xsl:text>Custodial Order for persons who are</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:text>  18 or older but under 21 years old</xsl:text>
		</fo:block>
	</xsl:template>
    <!-- The Court Ordered text -->
	<xsl:template match="nar:YO5044D_PeriodOf_pre_LASBO">
		<fo:inline>
			<xsl:text> that the defendant serve a period of detention in a young offender institution, details of which follow.</xsl:text>
		</fo:inline>
		<fo:inline>
			<fo:block space-after="12pt"/>
			<fo:block>
				<xsl:text>The Crown Court had, or would have had but for the statutory restrictions upon the imprisonment of young offenders, power to impose imprisonment on the defendant.</xsl:text>
			</fo:block>
            </fo:inline>
	</xsl:template>
	<!-- Custodial Sentence initial text  -->
	<xsl:template match="nar:YO5044D_CustodialSentencesText_pre_LASBO">
		<fo:inline>
			<xsl:choose>
				<!-- Indeterminate Sentence Option start -->
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'indeterminate' ">
					<xsl:text>The court ordered that the provisions of section 225 of the Criminal Justice Act 2003 should apply to the defendant. 
					The court specified the defendant must serve a minimum term of </xsl:text>
					<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
						<xsl:call-template name="TermDuration"/>
						<xsl:text>.</xsl:text>
					</xsl:for-each>
				</xsl:when>
				<!-- Indeterminate Sentence Option end -->
				<!-- Custodial Sentence Option start -->
				<xsl:otherwise>
					<fo:inline>
						<xsl:text>The court ordered that the defendant be sentenced to</xsl:text>
					</fo:inline>
				</xsl:otherwise>
				<!-- Custodial Sentence Option end-->
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!--Custodial Sentence Imprisonment type -->
	<xsl:template match="nar:YO5044D_ImprisonmentType_pre_LASBO">
		<xsl:choose>
			<!-- Life option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
				<xsl:call-template name="callableYO5044DLifeText"/>
			</xsl:when>
			<!-- Period (term) option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
				<xsl:call-template name="callableYO5044DDetentionText"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- Template to display Detention or Imprisonment details -->
	<xsl:template name="callableYO5044DDetentionText_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<!-- S.Bachra 22/4/03 Display days value (Tracker 52680) -->
			<xsl:call-template name="TermDuration"/>
		</xsl:for-each>
		<xsl:text> detention </xsl:text>
	</xsl:template>
	<!-- Callable Life Details -->
	<xsl:template name="callableYO5044DLifeText_pre_LASBO">
        detention for life.	
    </xsl:template>
	<!-- Chapter III text specific for YO5044D order -->
	<xsl:template match="nar:YO5044D_ChapterIII_Text_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of Chapter 5 of Part 12 of the Criminal Justice Act 2003 should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text line 1 Specific to YO5044D -->
	<xsl:template match="nar:YO5044D_ES_Text1_pre_LASBO">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 227 of the Criminal Justice Act 2003 to an extended sentence of </xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- *************************************************************** -->
	<!-- COMMON TEMPLATES FOR YO544C AND YO544D -->
	<!-- **************************************************************  -->
	<!-- Template used to display Defendant Details -->
	<xsl:template match="nar:YO5044_DefendantDetails_pre_LASBO">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Template used to display Defendant Details -->
	<xsl:template match="nar:YO5044_DefendantDetails_pre_LASBO">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- The Court Ordered text -->
	<xsl:template match="nar:YO5044_PeriodOf_pre_LASBO">
		<fo:inline>
			<xsl:text> that the defendant serve a period of detention, details of which follow.</xsl:text>
		</fo:inline>
		<fo:inline>
			<fo:block space-after="12pt"/>
			<fo:block>
				<xsl:text>The Crown Court had, or would have had but for the statutory restrictions upon the imprisonment of young offenders, power to impose imprisonment on the defendant.</xsl:text>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Custodial Term Details -->
	<xsl:template match="nar:YO5044_CustDays_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="TermDuration"/>
			<xsl:text> and </xsl:text>
		</xsl:for-each>
	</xsl:template>
	<!-- Template used to add two dates -->
	<xsl:template match="nar:YO5044_Days_pre_LASBO">
		<!-- years value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Years + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
        <!-- months value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
        <!-- days value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Days + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> day(s)
    </xsl:template>
	<!-- Extended Sentence Text2 -->
	<xsl:template match="nar:YO5044_ES_Text2_pre_LASBO">
		<fo:inline>
			<xsl:text> comprising </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text3 -->
	<xsl:template match="nar:YO5044_ES_Text3_pre_LASBO">
		<fo:inline>
			<xsl:text>a custodial term of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text4 -->
	<xsl:template match="nar:YO5044_ES_Text4_pre_LASBO">
		<fo:inline>
			<xsl:text>an extension period of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Term Details including days-->
	<xsl:template match="nar:YO5044_ExPeriod_pre_LASBO">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="TermDuration"/>
		</xsl:for-each>.
		<fo:block space-after="10pt"/>
	</xsl:template>
	<!-- Extended Sentenence imprisonment concurrency -->
	<xsl:template match="nar:YO5044_ExSentenceConcurrency_pre_LASBO">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SentenceType!='Not Applicable'">
				<xsl:text>This sentence was ordered to be </xsl:text>
				<xsl:call-template name="IMPExtendedSentenceType"/>
				<xsl:text>any other periods of detention to which the defendant was subject prior to the making of this order. </xsl:text>
			</xsl:when>
		</xsl:choose>
		<fo:inline>
			<xsl:text>The provisions of Chapter 6 of the Criminal Justice Act 2003 apply in this case.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- *************************************************************** -->
	<!-- COMMITMENT TO YOUNG OFFENDERS ORDER END -->
	<!-- **************************************************************  -->
</xsl:stylesheet>
