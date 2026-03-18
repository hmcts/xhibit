<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ************************************************** -->
    <!-- RELEASE FROM PRISON ORDER START -->
    <!-- ************************************************** -->
    <xsl:template match="nar:RP_Header">
        <fo:inline>
            <xsl:text>Bail granted: Order for release from custody</xsl:text>
			<xsl:if test="$baseAll/ord:Bail/ord:BailCondition='SubjectToBailOrder'">
				<xsl:text> subject to compliance with conditions</xsl:text>
			</xsl:if>
        </fo:inline>
    </xsl:template>
    <!-- The -->
    <xsl:template match="nar:RP_The">
        <fo:inline>
            <xsl:text>The</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Defendant -->
    <xsl:template match="nar:RP_Defendant">
        <fo:inline>
			<xsl:choose>
				<xsl:when test="substring($baseAll/ord:OrderHeader/ord:CaseNumber,1,1)='A'">
                    Appellant
                </xsl:when>
				<xsl:otherwise>
                    Defendant
                </xsl:otherwise>
			</xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- Defendant Details -->
	<xsl:template match="nar:RP_DefendantDetails">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
    <!-- Court -->
    <xsl:template match="nar:RP_Court">
        <fo:inline>
            <xsl:text>Court</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- defendant custody text -->
    <xsl:template match="nar:RP_CustodyWarrant">
        <fo:inline>
            <xsl:text>Is now in your custody under a warrant of the </xsl:text>
            <xsl:value-of select="$baseAll/ord:OriginalCourt/ord:Court/ord:CourtHouseName"/>
            <xsl:if test="$baseAll/ord:OriginalCourt/ord:Court/ord:CourtHouseType='Crown Court'">
				Crown Court.
			</xsl:if>
        </fo:inline>
    </xsl:template>
    <!-- defendant custody date-->
    <xsl:template match="nar:RP_CustodyDate">
		<fo:inline>
            <xsl:text>Dated </xsl:text>
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:OriginalCourt/ord:OriginalWarrantDate"/>
            </xsl:call-template>
        </fo:inline>
    </xsl:template>
    <xsl:template match="nar:RP_ReleaseText">
		<fo:inline>
            <xsl:text>On </xsl:text>
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:OrderHeader/ord:OrderDate"/>
            </xsl:call-template>
            <xsl:text> granted bail </xsl:text>
            <xsl:choose>
				<xsl:when test="$baseAll/ord:Bail/ord:BailCondition='Unconditional'">
					<xsl:text>unconditionally. </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>subject to the conditions given in the attached record of decision of bail dated : </xsl:text>
					<xsl:call-template name="FormatDate">
						<xsl:with-param name="date" select="$baseAll/ord:Bail/ord:BailOrderDate"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- Ordered text-->
    <xsl:template match="nar:RP_Ordered">
        <fo:inline>
			<xsl:text>It is ordered that </xsl:text>
			<xsl:if test="$baseAll/ord:Bail/ord:BailCondition='SubjectToBailOrder'">
				<xsl:choose>
					<xsl:when test="$baseAll/ord:Ordered='SubjectToConditions'">
						<xsl:text>upon the </xsl:text>
						<xsl:choose>
							<xsl:when test="substring($baseAll/ord:OrderHeader/ord:CaseNumber,1,1)='A'">
								appellant
							</xsl:when>
							<xsl:otherwise>
								defendant
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text> complying with the conditions which must be complied with before release (set out in the record of decision on bail), </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>because the </xsl:text>
						<xsl:choose>
							<xsl:when test="substring($baseAll/ord:OrderHeader/ord:CaseNumber,1,1)='A'">
								appellant
							</xsl:when>
							<xsl:otherwise>
								defendant
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text> has complied with the conditions which had to be complied with before release, </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
        </fo:inline>
    </xsl:template>
    <!-- Other Place -->
    <xsl:template match="nar:RP_Release">
        <fo:inline>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:Bail/ord:BailCondition='Unconditional'">
					<xsl:text>The </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>the </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
            <xsl:choose>
				<xsl:when test="substring($baseAll/ord:OrderHeader/ord:CaseNumber,1,1)='A'">
					appellant
				</xsl:when>
				<xsl:otherwise>
					defendant
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> shall be released from your custody in respect of the above warrant.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- *********************************************** -->
    <!-- RELEASE FROM PRISON ORDER END -->
    <!-- *********************************************** -->
</xsl:stylesheet>
