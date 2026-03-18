<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:set="http://xml.apache.org/xslt">
<xsl:template match="nar:NDS_Title">
 			<xsl:text>Notice of Deferment of Sentence</xsl:text>
</xsl:template>
<xsl:template match="nar:NDS_Notice">
 			<xsl:text>TAKE NOTICE</xsl:text>
</xsl:template>
<xsl:template match="nar:NoticeText">
    <xsl:text>that, following your consent, the court will not pass sentence upon you until </xsl:text> 
</xsl:template> 
<xsl:template match="nar:NDS_OrderDate">
     <xsl:call-template name="FormatDate">
			<xsl:with-param name="date" select="$baseAll/ord:DateOfOrder"/>
	</xsl:call-template>
</xsl:template>
<xsl:template match="nar:EndNoticeText">
	<fo:block space-after="10pt">
		<xsl:text>for the following offence/offences: </xsl:text> 
	</fo:block>
</xsl:template>
 <!-- Template to display NDS Offences text -->
 <xsl:template match="nar:NDS_Offences">
 	<fo:block space-after="10pt">
        <xsl:choose>
            <xsl:when test="count($baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement)=1">
                <xsl:call-template name="FormatBulletTextArea">
                    <xsl:with-param name="string" select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge[1]/ord:OffenceStatement"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge">
                    <xsl:if test="position()!=1">
                        <xsl:if test="ord:OffenceStatement != ''">
                            <xsl:value-of select="ord:CaseNumber"/>
                            <xsl:text> / </xsl:text>
                            <xsl:value-of select="ord:OffenceStatement"/>
                            <fo:block/>
                        </xsl:if>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
        <fo:block/>
        <!-- Check to if there are any linked offences for the same defendant to display -->
        <xsl:if test="$baseAll/ord:LinkedOffences">
            <fo:block>
                <xsl:for-each select="$baseAll/ord:LinkedOffences/ord:LinkedOffence">
                    <xsl:value-of select="ord:LinkedCaseNumber"/>
                    <xsl:text> / </xsl:text>
                    <xsl:value-of select="ord:LinkedOffenceStatement"/>
                    <fo:block/>
                </xsl:for-each>
            </fo:block>
        </xsl:if>
		</fo:block>
    </xsl:template>

	<xsl:template match="nar:AttendText">
    <xsl:text>You are required to attend court</xsl:text> 
</xsl:template>
<xsl:template match="nar:At">
	<xsl:text>at:</xsl:text>
</xsl:template>
<xsl:template match="nar:CrownCourt">
	<xsl:value-of select="$baseAll/ord:CrownCourtToAttend/ord:CourtHouseDetails/ord:CourtHouseName"/>
</xsl:template>
<xsl:template match="nar:NDS_CourtAddress">
<xsl:for-each select="$baseAll/ord:CrownCourtToAttend/ord:CourtHouseDetails/ord:CourtHouseAddress">
     <xsl:call-template name="CallableAddress_Comm_Order"/>
</xsl:for-each>
</xsl:template>
<xsl:template match="nar:SentenceOfCourt">
 	<fo:block space-after="10pt">
	<xsl:text>that day without fail to be dealt with by the court.  If you do not attend, a warrant for your arrest may be issued.  You have agreed to comply with the following requirements until that date:</xsl:text>
	</fo:block>
</xsl:template> 
	<!-- Requirements -->
	<xsl:template match="nar:NDS_Requirements">
		<fo:inline>
			<fo:block>
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="//ord:Requirements"/>
				</xsl:call-template>
			</fo:block>
		</fo:inline>
	</xsl:template>   
<xsl:template match="nar:Warning">
 			<xsl:text>And further take notice:</xsl:text>
</xsl:template>
<xsl:template match="nar:WarningText">
	<xsl:text>that the court may deal with you before that date if you fail to comply with any of the requirements outlined above or are convicted in Great Britain of any offence during the period that the sentence is deferred</xsl:text>
</xsl:template> 
<xsl:template match="nar:NDS_Signed">
		<!-- call template to display signed info -->
		<xsl:call-template name="SignedInfo"/>
</xsl:template>
 
</xsl:stylesheet>


 