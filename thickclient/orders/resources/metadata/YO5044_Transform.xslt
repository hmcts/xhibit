<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ****************************************************************** -->
	<!-- COMMITMENT TO YOUNG OFFENDERS ORDER START -->
	<!-- *****************************************************************  -->
	<!-- *************************************************************** -->
	<!--  TEMPLATES SPECIFIC TO  YO544C						 -->
	<!-- **************************************************************  -->
	<!-- YO5044C title -->
	<xsl:template match="nar:YO5044C_Title">
		<fo:block>
			<xsl:text>Custodial Order for persons</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:text>under 18 years old</xsl:text>
		</fo:block>
	</xsl:template>
	<!-- Custodial Sentence initial text  -->
	<xsl:template match="nar:YO5044C_CustodialSentencesText">
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
				<!-- Extended Sentence Option start -->
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'extended' ">
					<xsl:text>The Court</xsl:text>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's226b'" >
							<xsl:text>, under section 226B Criminal Justice Act 2003,</xsl:text>
							<xsl:text> ordered that the defendant serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's228' ">
							<xsl:text>, under section 228 Criminal Justice Act 2003,</xsl:text>
							<xsl:text> ordered that the defendant serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's254' ">
							<xsl:text>, under section 254 Sentencing Act 2020,</xsl:text>
							<xsl:text> ordered that the defendant serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's268a' ">
							<xsl:text> ordered that the defendant be sentenced, under section 268A Sentencing Act 2020,</xsl:text>
							<xsl:text> to serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> ordered that the defendant serve an extended sentence of </xsl:text>
						</xsl:otherwise>
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
				<!-- Custodial Sentence Option start -->
				<xsl:otherwise>
					<fo:inline>
						<xsl:text>The Court</xsl:text>
						<xsl:choose>
							<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section226 = 'true' and $baseAll/ord:CustodialSentence/ord:ImprisonmentType ='hmpleasure'" >
								<xsl:text>, under section 226 Criminal Justice Act 2003,</xsl:text>
								<xsl:text> ordered that the defendant be detained</xsl:text>
							</xsl:when>
							<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period' and $baseAll/ord:CustodialSentence/ord:s235-236/@selected ='true'">
								<xsl:text> ordered that the defendant be sentenced</xsl:text>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@selected ='true'">
										<xsl:text> to</xsl:text>
										<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:CustodialTerm">
											<xsl:call-template name="TermDuration"/>
										</xsl:for-each>
										<xsl:text> detention </xsl:text>
										<xsl:call-template name="callableYO5044CConcurrentText"/>
										<xsl:text> and</xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:text>, under Section 252A Sentencing Act 2020, to</xsl:text>
								<xsl:text> serve a Special Custodial Sentence of </xsl:text>
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
								<xsl:call-template name="callableYO5044ConcurrentAboveText"/>
								<xsl:text>. </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@selected ='true'">
										<xsl:text> ordered that the defendant be sentenced to</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text> ordered that the defendant be detained</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</fo:inline>
				</xsl:otherwise>
				<!-- Custodial Sentence Option end-->
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Template used to show Risk or Vulnerability details -->
	<xsl:template match="nar:YO5044C_RiskOrVulnerability">
		<fo:inline>
			<xsl:if test="$baseAll/ord:RiskVulnerabilityFactors/@selected = 'true'">
				<xsl:value-of select="$baseAll/ord:RiskVulnerabilityFactors/ord:RiskText"/>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!--Custodial Sentence Imprisonment type -->
	<xsl:template match="nar:YO5044C_ImprisonmentType">
		<xsl:choose>
			<!-- Life option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
				<xsl:call-template name="callableYO5044CLifeText"/>
			</xsl:when>
			<!-- Her Majesty's pleasure option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='hmpleasure'">
				<xsl:choose>
					<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section226 = 'true'">
						<xsl:text>for life.</xsl:text>
					</xsl:when>
					<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section226 = 'false'">
						<xsl:text>during His Majesty's pleasure.</xsl:text>
					</xsl:when>
				</xsl:choose>                
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
			<!-- Period (term) option -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period' and $baseAll/ord:CustodialSentence/ord:s235-236/@selected !='true'">
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
			<!-- Section 250 -->
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section250'">
				<xsl:text>under section 250 of the Sentencing Act 2020</xsl:text>
				<xsl:choose>
					<xsl:when test="$baseAll/ord:CustodialSentence/ord:Section250TermType/@selected='true'">
						<xsl:choose>
							<!-- Life -->
							<xsl:when test="$baseAll/ord:CustodialSentence/ord:Section250TermType/ord:TermType ='section250life'">
								<xsl:call-template name="callableYO5044CLifeText"/>
							</xsl:when>
							<!-- Term -->
							<xsl:when test="$baseAll/ord:CustodialSentence/ord:Section250TermType/ord:TermType ='section250term'">
								<xsl:text>. The sentence is </xsl:text>
								<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Section250TermType/ord:Term">
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
		<!-- Custodial Sentence details -->
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:TermType!='Not Applicable'">
				<fo:block space-before="12pt">
                    This sentence was ordered to be
                    <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:TermType">
						<xsl:call-template name="TermType"/>
					</xsl:for-each>
                    any other periods of detention to which the defendant was subject prior to the making of this order.
                </fo:block>
			</xsl:when>
		</xsl:choose>
		<xsl:if test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='hmpleasure' or ($baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section91TermType ='section91life') or ($baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section250' and $baseAll/ord:CustodialSentence/ord:Section250TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section250TermType/ord:TermType ='section250life')">
		<xsl:text> The Sentence is </xsl:text>
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
		</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- Callable Detention duration -->
	<xsl:template name="callableYO5044CDetentionText">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
				<xsl:choose>
					<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@selected ='true'">
						<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:CustodialTerm">
							<xsl:call-template name="TermDuration"/>
						</xsl:for-each>
						<xsl:text> detention</xsl:text>
						<xsl:call-template name="callableYO5044CConcurrentText"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>for </xsl:text>
						<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:CustodialTerm">
							<xsl:call-template name="TermDuration"/>
						</xsl:for-each>
						<xsl:text> </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>for </xsl:text>
				<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
					<xsl:call-template name="TermDuration"/>
				</xsl:for-each>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>		
	</xsl:template>
	<!-- Callable Concurrent text -->
	<xsl:template name="callableYO5044CConcurrentText">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@TermType ='Concurrent'">
				<xsl:text> (concurrent to S252A sentence below)</xsl:text>
			</xsl:when>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@TermType ='Consecutive'">
				<xsl:text> (consecutive to S252A sentence below)</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="callableYO5044ConcurrentAboveText">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/@TermType ='Concurrent'">
				<xsl:text> (concurrent to detention above)</xsl:text>
			</xsl:when>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/@TermType ='Consecutive'">
				<xsl:text> (consecutive to detention above)</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="callableYO5044DConcurrentBelowText">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@TermType ='Concurrent'">
				<xsl:text> (concurrent to </xsl:text>
				<xsl:call-template name="callableYO5044DSectionText"/>
				<xsl:text> sentence below)</xsl:text>
			</xsl:when>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@TermType ='Consecutive'">
				<xsl:text> (consecutive to </xsl:text>
				<xsl:call-template name="callableYO5044DSectionText"/>
				<xsl:text> sentence below)</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="callableYO5044DSectionText">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CSSectionType = 'section252a'">
				<xsl:text> 252A</xsl:text>
			</xsl:when>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CSSectionType = 'section265'">
				<xsl:text> 265</xsl:text>
			</xsl:when>
			<xsl:otherwise><xsl:text>##</xsl:text><xsl:value-of select="$baseAll/ord:CustodialSentence/ord:s235-236/ord:CSSectionType" /><xsl:text>##</xsl:text></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Callable  Life Details -->
	<xsl:template name="callableYO5044CLifeText">
        for life.	
    </xsl:template>
	<!-- Extended Sentence text line 1 (specific to YO5044C) -->
	<xsl:template match="nar:YO5044C_ES_Text1">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 254 of the Sentencing Act 2020 to an extended sentence of </xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Callable Template to display ExtendedSentence -->
	<xsl:template name="callableYO5044CExtendedSentence">
		<xsl:if test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/@selected='true'">
	        extended under section 228 of the Criminal Justice Act 2003
		</xsl:if>
	</xsl:template>
	<!-- Domestic Burglary Title -->
	<xsl:template match="nar:YO5044C_DomesticBurglaryTitle">
		<fo:inline>
			<xsl:text>Domestic Burglary</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Domestic Burglary Text -->
	<xsl:template match="nar:YO5044C_DomesticBurglary">
		<xsl:text>The court was satisfied that section 111 of the Powers of the Criminal Courts (Sentencing) Act 2000 applied in this case.</xsl:text>
	</xsl:template>
	<!-- *************************************************************** -->
	<!--  TEMPLATES SPECIFIC TO  YO544D 						 -->
	<!-- **************************************************************  -->
	<!-- YO5044D title -->
	<xsl:template match="nar:YO5044D_Title">
		<fo:block>
			<xsl:text>Custodial Order for persons who are</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:text>  18 or older but under 21 years old</xsl:text>
		</fo:block>
	</xsl:template>
    <!-- The Court Ordered text -->
	<xsl:template match="nar:YO5044D_PeriodOf">
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
	<xsl:template match="nar:YO5044D_CustodialSentencesText">
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
				<!-- Extended Sentence Option start -->
				<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType = 'extended' ">
					<xsl:text>The Court</xsl:text>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's226a'" >
							<xsl:text>, under Section 226A Criminal Justice Act 2003,</xsl:text>
							<xsl:text> ordered that the defendant serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's266' ">
							<xsl:text> ordered that the defendant be sentenced, under Section 266 Sentencing Act 2020, to serve an extended sentence of </xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SectionType = 's268a' ">
							<xsl:text> ordered that the defendant be sentenced, under section 268A Sentencing Act 2020,</xsl:text>
							<xsl:text> to serve an extended sentence of </xsl:text>
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
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94= 'false'">
									<xsl:text>, under section 224A Criminal Justice Act 2003,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false'
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false'
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94= 'false'">
									<xsl:text>, under section 225 Criminal Justice Act 2003,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false'
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94= 'false'">
									<xsl:text>, under Section 272 Sentencing Act 2020,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94= 'false'">
									<xsl:text>, under Section 273 Sentencing Act 2020,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false'
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94 = 'false'">
									<xsl:text>, under Section 274 Sentencing Act 2020,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false'  
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94 = 'false'">
									<xsl:text>, under Section 275 Sentencing Act 2020,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false'
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94 = 'false'">
									<xsl:text>, under Section 93 of the Powers of Criminal Courts (Sentencing) Act 2000,</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:CustodialSentence/ord:LifeSentence/@Section94 = 'true' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section224a = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section225 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section272 = 'false' 
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section273 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section274 = 'false'
												and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section275 = 'false' and $baseAll/ord:CustodialSentence/ord:LifeSentence/@Section93 = 'false'">
									<xsl:text>, under Section 94 of the Powers of Criminal Courts (Sentencing) Act 2000,</xsl:text>
								</xsl:when>
							</xsl:choose>
							<xsl:text> ordered that the defendant be sentenced to detention for life.</xsl:text>
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
								<xsl:text> detention</xsl:text>
								<xsl:call-template name="callableYO5044DConcurrentBelowText"/>
								<xsl:if test="$baseAll/ord:CustodialSentence/ord:s235-236/@selected ='false'">
									<xsl:text>. </xsl:text>
								</xsl:if>
							</xsl:if>
							<xsl:if test="$baseAll/ord:CustodialSentence/ord:s235-236/@selected ='true'">
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@selected ='true'">
										<xsl:text> and under Section</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>, under Section</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:call-template name="callableYO5044DSectionText"/>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:CustodialSentence/ord:CustodialTerm/@selected ='true'">
										<xsl:text> Sentencing Act 2020</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text> Sentencing Act 2020, to</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text> serve a Special Custodial Sentence of </xsl:text>
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
								<xsl:call-template name="callableYO5044ConcurrentAboveText"/>
								<xsl:text>. </xsl:text>
							</xsl:if>
						</xsl:when>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Custodial Sentence Options (life and imprisonment end-->
		</fo:inline>
		<!-- Custodial Sentence details -->
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:TermType!='Not Applicable'">
				<fo:block space-before="12pt">
                    This sentence was ordered to be
                    <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:TermType">
						<xsl:call-template name="TermType"/>
					</xsl:for-each>
                    any other periods of detention to which the defendant was subject prior to the making of this order.
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
	<!--Custodial Sentence Imprisonment type -->
	<xsl:template match="nar:YO5044D_ImprisonmentType">
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
	<xsl:template name="callableYO5044DDetentionText">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<!-- S.Bachra 22/4/03 Display days value (Tracker 52680) -->
			<xsl:call-template name="TermDuration"/>
		</xsl:for-each>
		<xsl:text> detention </xsl:text>
	</xsl:template>
	<!-- Callable Life Details -->
	<xsl:template name="callableYO5044DLifeText">
        detention for life.	
    </xsl:template>
	<!-- Chapter III text specific for YO5044D order -->
	<xsl:template match="nar:YO5044D_ChapterIII_Text">
		<fo:inline>
			<xsl:text>The court ordered that the provisions of Chapter 5 of Part 12 of the Criminal Justice Act 2003 should apply to the defendant.</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text line 1 Specific to YO5044D -->
	<xsl:template match="nar:YO5044D_ES_Text1">
		<fo:inline>
			<xsl:text>The court ordered that the defendant be sentenced under section 227 of the Criminal Justice Act 2003 to an extended sentence of </xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- *************************************************************** -->
	<!-- COMMON TEMPLATES FOR YO544C AND YO544D -->
	<!-- **************************************************************  -->
	<!-- Template used to display Defendant Details -->
	<xsl:template match="nar:YO5044_DefendantDetails">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Template used to show Risk or Vulnerability details -->
	<xsl:template match="nar:YO5044_RiskOrVulnerability">
		<fo:inline>
			<xsl:if test="$baseAll/ord:RiskVulnerabilityFactors/@selected = 'true'">
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="$baseAll/ord:RiskVulnerabilityFactors/ord:RiskText"/>
				</xsl:call-template>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- The Court Ordered text -->
	<xsl:template match="nar:YO5044_PeriodOf">
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
	<xsl:template match="nar:YO5044_CustDays">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="TermDuration"/>
			<xsl:text> and </xsl:text>
		</xsl:for-each>
	</xsl:template>
	<!-- Template used to add two dates -->
	<xsl:template match="nar:YO5044_Days">
		<!-- years value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Years + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor((($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> year(s)
        <!-- months value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Months + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> month(s)
        <!-- days value -->
		<xsl:value-of select="($baseAll/ord:CustodialSentence/ord:Term/ord:Days + $baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> day(s)
    </xsl:template>
	<!-- Extended Sentence Text2 -->
	<xsl:template match="nar:YO5044_ES_Text2">
		<fo:inline>
			<xsl:text> comprising </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text3 -->
	<xsl:template match="nar:YO5044_ES_Text3">
		<fo:inline>
			<xsl:text>a custodial term of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Text4 -->
	<xsl:template match="nar:YO5044_ES_Text4">
		<fo:inline>
			<xsl:text>an extension period of </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Extended Sentence Term Details including days-->
	<xsl:template match="nar:YO5044_ExPeriod">
		<xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="TermDuration"/>
		</xsl:for-each>.
		<fo:block space-after="10pt"/>
	</xsl:template>
	<!-- Extended Sentenence imprisonment concurrency -->
	<xsl:template match="nar:YO5044_ExSentenceConcurrency">
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
	<!-- Template to voting rights -->
	<xsl:template match="nar:VotingRights">
		Convicted offenders sentenced to imprisonment lose the right to vote while they are detained in custody.
	</xsl:template>
	<!-- *************************************************************** -->
	<!-- COMMITMENT TO YOUNG OFFENDERS ORDER END -->
	<!-- **************************************************************  -->
</xsl:stylesheet>
