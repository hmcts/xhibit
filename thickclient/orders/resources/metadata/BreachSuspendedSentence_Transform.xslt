<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
<xsl:template match="nar:BRSS_TitleText">
 			<xsl:text>Summons for a defendant to appear at the CROWN COURT after an offence has been committed during a 
			suspended sentence order</xsl:text>
</xsl:template>
<xsl:template match="nar:BRSS_OrderDate">
	 On <xsl:call-template name="FormatDate">
			 <xsl:with-param name="date" select="$baseAll/ord:DateOfOrder"/> 
	</xsl:call-template>
</xsl:template>
<xsl:template match="nar:DefendantSentence">
	<xsl:text>you were sentenced to</xsl:text>
</xsl:template>
<xsl:template match="nar:BRSS_ImprisonmentPeriod">
		<xsl:for-each select="$baseAll/ord:TermOfImprisonment">
				<xsl:call-template name="TermDuration"/>
		</xsl:for-each>
</xsl:template>
<xsl:template match="nar:DefendantImprisonment">
	<xsl:text>imprisonment and the sentence was suspended for</xsl:text>
</xsl:template>
<xsl:template match="nar:BRSS_SuspendedPeriod">
 	<xsl:for-each select="$baseAll/ord:SuspendedFor">
		<xsl:call-template name="TermDuration"/>
	</xsl:for-each>
</xsl:template>
<xsl:template match="nar:BRSS_SubsConvDate">
	<fo:block space-before="10pt"></fo:block>
 	On <xsl:call-template name="FormatDate">
				 <xsl:with-param name="date" select="$baseAll/ord:SubsequentConviction/ord:SubsConvDate"/>
	</xsl:call-template>
	at <xsl:value-of select="$baseAll/ord:SubsequentConviction/ord:CourtHouseDetails/ord:CourtHouseName"/> you were convicted of an offence, punishable with imprisonment, committed by you during the period of suspension.
</xsl:template>
<xsl:template match="nar:BRSS_SentencedText">
    <xsl:text>You are therefore summoned to appear at </xsl:text> <xsl:value-of select="$baseAll/ord:SummonedToAppearAt/ord:CourtHouseDetails/ord:CourtHouseName"/>
</xsl:template>
<xsl:template match="nar:BRSS_CourtAddress">
<xsl:for-each select="$baseAll/ord:SummonedToAppearAt/ord:CourtHouseDetails/ord:CourtHouseAddress">
     <xsl:call-template name="CallableAddress_Comm_Order"/>
</xsl:for-each>
</xsl:template>
<xsl:template match="nar:BRSS_SummonDate">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:SummonedToAppearAt/ord:SummonedDateTime/@selected='true'">
                on: 
                <xsl:call-template name="FormatDate">
                    <xsl:with-param name="date" select="$baseAll/ord:SummonedToAppearAt/ord:SummonedDateTime/ord:SummonedDate"/>
                </xsl:call-template>
                at 
                <xsl:call-template name="FormatTime">
                    <xsl:with-param name="time" select="$baseAll/ord:SummonedToAppearAt/ord:SummonedDateTime/ord:SummonedTime"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="BRSS_DateTimeNotified"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
	
	<xsl:template name="BRSS_DateTimeNotified">
        <fo:inline>
            <xsl:text> on a date and at a time to be notified</xsl:text>
        </fo:inline>
    </xsl:template>
<xsl:template match="nar:BRSS_ConsideredText">
	when the court will consider whether you should serve the suspended sentence.
</xsl:template>
<xsl:template match="nar:BRSS_TimePlaceText">
	or any other place and time to be notified
</xsl:template>
</xsl:stylesheet>


 