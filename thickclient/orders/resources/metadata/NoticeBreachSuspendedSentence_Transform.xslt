<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:set="http://xml.apache.org/xslt">
<xsl:template match="nar:NBSS_Title">
 			<xsl:text>Notice that an offender subject to a suspended sentence order has been dealt with</xsl:text>
</xsl:template>
<xsl:template match="nar:Attention">
	<xsl:text>For Attention</xsl:text>
</xsl:template>
<xsl:template match="nar:Justice">
    <xsl:if test="$baseAll/ord:Addresse/ord:CourtHouseDetails/ord:CourtHouseType='Crown Court'">
				<xsl:text>The Chief Clerk</xsl:text>  
    </xsl:if>
	<xsl:if test="$baseAll/ord:Addresse/ord:CourtHouseDetails/ord:CourtHouseType='Magistrates Court'">
                <xsl:text>The Clerk to the Justices</xsl:text>
    </xsl:if>
</xsl:template>
<xsl:template match="nar:CourtName"> 
 	<xsl:value-of select="$baseAll/ord:Addresse/ord:CourtHouseDetails/ord:CourtHouseName"/>
</xsl:template>
<xsl:template match="nar:NBSS_CourtAddress">
<xsl:for-each select="$baseAll/ord:Addresse/ord:CourtHouseDetails/ord:CourtHouseAddress">
     <xsl:call-template name="CallableAddress_Comm_Order"/>
</xsl:for-each>
</xsl:template>
<xsl:template match="nar:OriginalSentence">
	<xsl:text>Original Sentence</xsl:text>
</xsl:template>
<xsl:template match="nar:NBSS_SuspendedSentenceOrderText">
	<xsl:text>Date suspended sentence order imposed:</xsl:text>
 </xsl:template>
<xsl:template match="nar:NBSS_SuspendedSentenceOrder">
	<xsl:call-template name="FormatDate">
		<xsl:with-param name="date" select="$baseAll/ord:SuspendedDate"/>
	</xsl:call-template>
</xsl:template>
<xsl:template match="nar:SubsequentOffencesText">
	<xsl:text>The offender named above has been dealt with by this court in respect of a suspended sentence order imposed
	by your Court.</xsl:text>
</xsl:template>
<xsl:template match="nar:OffencesLabel">
	<xsl:text>Subsequent Offences</xsl:text>
</xsl:template>
<xsl:template match="nar:OffencesText">
   <xsl:value-of select="$baseAll/ord:SubsequentOffences"/>
</xsl:template>
<xsl:template match="nar:DealtWithText">
	<xsl:text>Date dealt with by this Court.</xsl:text>
</xsl:template>
<xsl:template match="nar:DealtWithDate">
	<xsl:call-template name="FormatDate">
		<xsl:with-param name="date" select="$baseAll/ord:ConvictionDate"/>
	</xsl:call-template>
</xsl:template>
<xsl:template match="nar:OrderOfthisCourt">
	<xsl:text>Order of this Court</xsl:text>
</xsl:template>
<xsl:template match="nar:ChoiceOfSix"> 
	<xsl:if test="$baseAll/ord:OrderEffective/ord:TermNBSS[@selected='true']">
 		<xsl:choose> 
			<xsl:when test="$baseAll/ord:OrderEffective/ord:OrderEffectiveType='SuspendedSentenceOriginalTerm'">
				<xsl:text>Suspended sentence order to take effect with original term unaltered.</xsl:text>
			</xsl:when>
			<xsl:when test="$baseAll/ord:OrderEffective/ord:OrderEffectiveType='SuspendedSentenceSubstitute'">
				<xsl:text>Suspended sentence order to take effect with the substitution for the original term of a lesser term of </xsl:text>
				<xsl:for-each select="$baseAll/ord:OrderEffective/ord:TermNBSS/ord:SuspendedSentenceSubstitute">
					<xsl:call-template name="TermDuration"/>.
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
		<fo:block space-after="10pt"></fo:block>
	 </xsl:if>
 	 <xsl:if test="$baseAll/ord:OrderEffective/ord:FineValue[@selected='true']">
        Fine of £ : <xsl:value-of select="$baseAll/ord:OrderEffective/ord:FineValue"/>
		<fo:block space-after="10pt"></fo:block>
     </xsl:if>
	 <xsl:if test="$baseAll/ord:OrderEffective/ord:CommunityReq[@selected='true']">
        More onerous community requirement(s) imposed: <xsl:value-of select="$baseAll/ord:OrderEffective/ord:CommunityReq"/>.
		<fo:block space-after="10pt"></fo:block>
     </xsl:if>
	 <xsl:if test="$baseAll/ord:OrderEffective/ord:SupervisionalPeriod[@selected='true']">
        Supervision period of the suspended sentence order extended:
 			<xsl:for-each select="$baseAll/ord:OrderEffective/ord:SupervisionalPeriod">
				<xsl:call-template name="TermDuration"/>
			</xsl:for-each>
		<fo:block space-after="10pt">	</fo:block>
    </xsl:if>
	 <xsl:if test="$baseAll/ord:OrderEffective/ord:OperationPeriod[@selected='true']">
        Operational period of the suspended sentence order extended:
 			<xsl:for-each select="$baseAll/ord:OrderEffective/ord:OperationPeriod">
  				<xsl:call-template name="TermDuration"/>
			</xsl:for-each>
     </xsl:if>
 
</xsl:template>
<xsl:template match="nar:NBSS_Signed">
		<!-- call template to display signed info -->
		<xsl:call-template name="SignedInfo"/>
</xsl:template>
<xsl:template match="nar:Attachedare">
	<xsl:text>Attached are:</xsl:text>
</xsl:template>
<xsl:template match="nar:NBSSCourtCopy">
	<xsl:text>Copy of the court record</xsl:text>
</xsl:template>
</xsl:stylesheet>


 