<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:set="http://xml.apache.org/xslt">
<xsl:template match="nar:ACTCD_TitleText">
 			<xsl:text>Notice of action taken on an order of conditional discharge upon conviction of a further offence</xsl:text>
</xsl:template>
<xsl:template match="nar:ACTCD_Justice">
    <xsl:if test="$baseAll/ord:Addressee/ord:CourtHouseDetails/ord:CourtHouseType='Crown Court'">
				<xsl:text>The Chief Clerk</xsl:text>
    </xsl:if>
	<xsl:if test="$baseAll/ord:Addressee/ord:CourtHouseDetails/ord:CourtHouseType='Magistrates Court'">
                <xsl:text>The Clerk to the Justices</xsl:text>
    </xsl:if>
</xsl:template>
<xsl:template match="nar:ACTCD_CourtName">
	<xsl:value-of select="$baseAll/ord:Addressee/ord:CourtHouseDetails/ord:CourtHouseName"/>
</xsl:template>
<xsl:template match="nar:ACTCD_CourtAddress">
<xsl:for-each select="$baseAll/ord:Addressee/ord:CourtHouseDetails/ord:CourtHouseAddress">
     <xsl:call-template name="CallableAddress_Comm_Order"/>
</xsl:for-each>
</xsl:template>
<xsl:template match="nar:ACTCD_DateOrder">
    <xsl:text>Date order made by your Court. </xsl:text>
	<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:DateOfOrder"/>
	</xsl:call-template>	
</xsl:template>
<xsl:template match="nar:ACTCD_ConvictedText">
	<xsl:text>The Offender named above has been convicted of a further offence while still subject to an order of conditional discharge made by your Court.</xsl:text>
</xsl:template>
<xsl:template match="nar:ACTCD_RecordAvailable">
    <xsl:text>The Court record is available.</xsl:text> 
</xsl:template>
<xsl:template match="nar:ACTCD_ConvictedDate">
	<fo:block space-after="10pt"></fo:block>
 	<xsl:text>Date convicted by this Court. </xsl:text>
 	<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:ConvictionDate"/>
	</xsl:call-template>
</xsl:template>
<xsl:template match="nar:ChoiceOfTwo">
    <xsl:choose> 
        <xsl:when test="$baseAll/ord:CourtOrdered/ord:CourtOrderedType='DidNotDealWithDefendant'">
            <xsl:text>The Court did not deal with the defendant for the offences for which the order of conditional discharge was made.</xsl:text>
        </xsl:when>
		<xsl:when test="$baseAll/ord:CourtOrdered/ord:CourtOrderedType='OrderForOffences'">
            <xsl:text>An order was made for the offences for which the order of conditional discharge was made.</xsl:text>
        </xsl:when>
	</xsl:choose>
</xsl:template>
<xsl:template match="nar:ACTCD_Signed">
		<!-- call template to display signed info -->
		<xsl:call-template name="SignedInfo"/>
</xsl:template>
</xsl:stylesheet>


 