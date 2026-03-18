<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ************************************************** -->
    <!-- NOTICE OF ACQUITTAL ORDER START -->
    <!-- ************************************************** -->
    <xsl:template match="nar:NA_Header">
        <fo:inline>
            <xsl:text>Notice of Acquittal</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- The -->
    <xsl:template match="nar:NA_The">
        <fo:inline>
            <xsl:text>The</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Defendant -->
    <xsl:template match="nar:NA_Defendant">
        <fo:inline>
            <xsl:text>Defendant</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Defendant Details -->
	<xsl:template match="nar:NA_DefendantDetails">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
    <!-- defendant text -->
    <xsl:template match="nar:NA_SentFrom">
        <fo:inline>
            <xsl:text>was sent for trial from: </xsl:text>
            <xsl:value-of select="$baseAll/ord:SentFromCourt/ord:Court/ord:CourtHouseName"/>
            <xsl:if test="$baseAll/ord:SentFromCourt/ord:Court/ord:CourtHouseType='Crown Court'">
				Crown Court.
			</xsl:if>
        </fo:inline>
    </xsl:template>
    <!-- defendant custody date-->
    <xsl:template match="nar:NA_SentDate">
		<fo:inline>
            <xsl:text>on </xsl:text>
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:SentFromCourt/ord:SentDate"/>
            </xsl:call-template>
        </fo:inline>
    </xsl:template>
    <!-- charges text -->
    <xsl:template match="nar:NA_Charges">
        <fo:inline>
            <xsl:text>charged with: </xsl:text>
            <xsl:value-of select="$baseAll/ord:Charges"/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="nar:NA_ReleaseText">
		<fo:inline>
            <xsl:text>On </xsl:text>
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:OrderHeader/ord:OrderDate"/>
            </xsl:call-template>
            <xsl:text> the prosecution notified the court that it proposed to offer no evidence against the defendant who is currently in custody awaiting trial on this matter. </xsl:text>
            <xsl:text>With the consent of the defence and the trial judge, the case has today been dealt with </xsl:text>
			<xsl:if test="$baseAll/ord:Absence/@selected='true'">
				<xsl:text>in the absence of the parties  </xsl:text>
			</xsl:if>
			<xsl:text>and a verdict of not guilty entered under section 17 of the Criminal Justice Act 1967 for </xsl:text>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:Count/ord:WhichCounts='AllCounts'">
					<xsl:text>each count on this indictment.</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>the count(s) on the indictment which (is) (are) numbered: </xsl:text>
					<xsl:value-of select="$baseAll/ord:Count/ord:CountNumbers"/>
					<xsl:text>.</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- Ordered text-->
    <xsl:template match="nar:NA_Ordered">
        <fo:inline font-weight="bold">
            <xsl:text>The defendant should therefore be discharged at once from your custody in respect of the above count(s) on this indictment.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- RecordSheet text-->
    <xsl:template match="nar:NA_RecordSheet">
        <fo:inline>
			<xsl:text>A copy of the court record sheet is attached</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- *********************************************** -->
    <!-- NOTICE OF ACQUITTAL ORDER END -->
    <!-- *********************************************** -->
</xsl:stylesheet>
