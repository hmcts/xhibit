<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:set="http://xml.apache.org/xslt">
<xsl:template match="nar:BRCD_TitleText">
 			<xsl:text>Summons for a defendant to appear at the Crown Court after an offence has been committed during 
			the period of a conditional discharge order</xsl:text>
</xsl:template>
<xsl:template match="nar:BRCD_OrderDate">
	On <xsl:call-template name="FormatDate">
			 <xsl:with-param name="date" select="$baseAll/ord:DateOfOrder"/> 
	</xsl:call-template> you were conditionally discharged  
</xsl:template>
<xsl:template match="nar:BRCD_SubsConvDate">
	On <xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:SubsequentConv/ord:SubsConvDate"/>  
	</xsl:call-template> 
</xsl:template>
<xsl:template match="nar:BRCD_CourtName">
	At <xsl:value-of select="$baseAll/ord:SubsequentConv/ord:CourtName"/>
</xsl:template>
<xsl:template match="nar:DefendantConvicted">
	<xsl:text> You were convicted of an offence committed during the period of conditional discharge.</xsl:text>
</xsl:template>
<xsl:template match="nar:BRCD_SummonedText">
    <xsl:text>You are therefore summoned to appear at</xsl:text> 
</xsl:template>
<xsl:template match="nar:BRCD_SummonedCourt">
	<xsl:value-of select="$baseAll/ord:SummonedToAppearAt/ord:CourtHouseDetails/ord:CourtHouseName"/>
</xsl:template>
<xsl:template match="nar:BRCD_CourtAddress">
<xsl:for-each select="$baseAll/ord:SummonedToAppearAt/ord:CourtHouseDetails/ord:CourtHouseAddress">
     <xsl:call-template name="CallableAddress_Comm_Order"/>
</xsl:for-each>
</xsl:template>
<xsl:template match="nar:BRCD_SummonDate">
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
                <xsl:call-template name="BRCD_DateTimeNotified"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
	
	<xsl:template name="BRCD_DateTimeNotified">
        <fo:inline>
            <xsl:text> on a date and at a time to be notified</xsl:text>
        </fo:inline>
    </xsl:template>
<xsl:template match="nar:BRCD_ConsideredText">
	when the court will consider what order it should make for the offence(s) for which the order of conditional discharge was made.
</xsl:template>
<xsl:template match="nar:BRCD_TimePlaceText">
	or any other place and time to be notified
</xsl:template>
<xsl:template match="nar:BRCD_Signed">
		<!-- call template to display signed info -->
		<xsl:call-template name="SignedInfo"/>
</xsl:template>
 
</xsl:stylesheet>


 