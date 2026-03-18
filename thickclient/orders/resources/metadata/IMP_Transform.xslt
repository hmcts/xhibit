<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- *************************************-->
    <!-- IMPRISONMENT ORDER START -->
    <!-- *************************************-->
    <!-- IMPO title -->
    <xsl:template match="nar:IMPO_Title">
        <fo:inline>
            <xsl:text>Order for imprisonment</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template used to display Committed for Sentence Details -->
    <xsl:template match="nar:IMPO_CommittedForSentence">
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
    <!-- Period of Imprisonment -->
    <xsl:template match="nar:IMPO_PeriodOfImprisonment">
        <fo:inline>
            <xsl:text> that the defendant serve a period of imprisonment, details of which follow.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Defendant be sentenced -->
    <xsl:template match="nar:IMPO_DefendantBeSentenced">
        <fo:inline>
            <xsl:text>The court ordered that the defendant be sentenced to </xsl:text>
        <xsl:choose>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
                imprisonment for life.		
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
                <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
                    <xsl:call-template name="TermIncDays"/>
                    <xsl:text> imprisonment.</xsl:text>
                </xsl:for-each>
            </xsl:when>
        </xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Imprison Sentence details -->
    <xsl:template match="nar:IMPO_SentenceOption">
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
    <!-- Serious Offence Text -->
    <xsl:template match="nar:IMPO_SeriousOffence">
        <fo:inline>
            <xsl:text>Second Serious Offence</xsl:text>
        </fo:inline>
    </xsl:template>    
    <!-- Display Serious Offence Text -->
    <xsl:template match="nar:IMPO_SeriousOffenceText">
        <xsl:text>The court ordered that a mandatory (automatic) life sentence for a second serious offence, pursuant to section 109(1)(2)(4) of the Powers of the Criminal Courts (Sentencing) Act 2000, should apply to the defendant.</xsl:text>
    </xsl:template>
    <!-- Minimum Sentence Text -->
    <xsl:template match="nar:IMPO_MinimumSentenceTitle">
        <fo:inline>
            <xsl:text>Minimum Sentence</xsl:text>
        </fo:inline>
    </xsl:template>    
    <!-- Display Minimum Sentence Text -->
    <xsl:template match="nar:IMPO_MinimumSentence">
        <xsl:text>The court ordered that the defendant do serve a minimum of </xsl:text>
        <xsl:value-of select="$baseAll/ord:MinimumLifeTerm/ord:SentenceTerm/ord:Years"/>
        <xsl:text> years imprisonment of </xsl:text>
        <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
            <xsl:call-template name="HisHer"/>
        </xsl:for-each>
        <xsl:text> mandatory life sentence, pursuant to section 269 of the Criminal Justice Act 2003.</xsl:text>
    </xsl:template>
    <!-- Template to display Imprisonment extended currency -->
    <xsl:template match="nar:IMPO_ExSentence">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SentenceType!='Not Applicable'">
                <xsl:text>This sentence was ordered to be </xsl:text>
                <xsl:call-template name="IMPExtendedSentenceType"/>
                <xsl:text>any other periods of imprisonment to which the defendant was subject prior to the making of this order.</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Return Defendant title 1 -->
    <xsl:template match="nar:IMPO_ReturnDefendant_Title1">
        <fo:inline>
            <xsl:text>Return of defendants</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Return Defendant title 2 -->
    <xsl:template match="nar:IMPO_ReturnDefendant_Title2">
        <fo:inline>
            <xsl:text>to prison</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Prison -->
    <xsl:template match="nar:IMPO_Prison">
        <fo:inline>
            <xsl:text> prison </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Imprisonment Return Period details -->
    <xsl:template match="nar:IMPO_Return">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType!='Not Applicable'">
                <xsl:text>This period of imprisonment was ordered to be served </xsl:text>
                <xsl:call-template name="IMPServedPeriod"/>
                <xsl:text> any other periods of imprisonment imposed by the court on the same occasion when this order was made.</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- ADDED for CCN400 -->
    <!-- DeportationReasons Text -->
    <xsl:template match="nar:IMPO_Deportation_Title">
        <fo:inline>
            <xsl:text>Automatic Deportation </xsl:text>
        </fo:inline>
    </xsl:template>
	<xsl:template match="nar:IMPO_Deportation_Text">
		<xsl:variable name="defendantOrAppellant">
			<xsl:choose>
				<xsl:when test="substring(/ord:Order/ord:OrderData/ord:ImprisonmentOrder/ord:OrderHeader/ord:CaseNumber,1,1) = 'A'">appellant</xsl:when>
				<xsl:otherwise>defendant</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='custodial'">
				<fo:inline>
					<xsl:text>
						This </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> is liable to deportation because the </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> is a foreign national and has received a custodial sentence of 12 months or more.
					</xsl:text>
				</fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='suspended'">
				<fo:inline>
					<xsl:text>
						This </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> meets the requirements for automatic deportation because the </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> has breached a suspended sentence of 12 months or more.
					</xsl:text>
				</fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='seriousDrugOffence'">
				<fo:inline>
					<xsl:text>
						This </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> is liable to deportation because the </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> is a foreign national and has been sentenced to a period of imprisonment for a serious drugs offence. 
					</xsl:text>
				</fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:DeportationSection/ord:DeportationReasons/ord:Reason='recommendedDeportation'">
				<fo:inline>
					<xsl:text>
						This </xsl:text><xsl:value-of select="$defendantOrAppellant"/><xsl:text> is liable to deportation because the Court recommended deportation regardless of offence or sentence length.
					</xsl:text>
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
    
    <!-- ***********************************-->
    <!-- IMPRISONMENT ORDER END -->
    <!-- ***********************************-->
</xsl:stylesheet>
