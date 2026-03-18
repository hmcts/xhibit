<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml C:\temp\MO_temp.xml?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ********************************* -->
	<!-- MONETARY ORDER START -->
	<!-- ********************************* -->
	<!-- Notice of Monetary Order -->
	<xsl:include href="OrdersCommonTransform.xslt"/>
	<xsl:template match="nar:MO_Notice">
		<fo:inline>
			<xsl:text>Notice of Monetary Order</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- In the case of -->
	<xsl:template match="nar:MO_CaseOf">
		<fo:inline>
			<xsl:text>In the case of</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- The Appellant/Defendant -->
	<xsl:template match="nar:MO_TheDefendant">
		<fo:inline>
			<xsl:text>The </xsl:text>
			<xsl:call-template name="AppellantDefendant"/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:MO_Disposals">
		<fo:inline>
			<xsl:text>Disposals</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:MO_Payment">
		<fo:inline>
			<xsl:text>Payment Terms</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:MO_AdditionalDetailsLabel">
		<fo:inline>
			<xsl:text>Additional Details</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:MO_SignedByLabel">
		<fo:inline>
			<xsl:text>Signing Details</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- was convicted on -->
	<xsl:template match="nar:MO_ConvictionDate">
		<fo:inline>
			<xsl:text>The </xsl:text>
			<xsl:call-template name="AppellantDefendant"/>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> was sentenced on </xsl:text>
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:SentenceDate"/>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="($baseAll/ord:InCustody)='Yes'">
					<xsl:text> and is in custody.</xsl:text>
				</xsl:when>
				<xsl:when test="($baseAll/ord:InCustody)='No'">
					<xsl:text> and is not in custody.</xsl:text>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- "Offence" -->
	<xsl:template match="nar:MO_DefWasOrdered">
		<fo:inline>
			<fo:block>
				<xsl:text>The </xsl:text>
				<xsl:call-template name="AppellantDefendant"/>
				<xsl:text> was ordered to pay:</xsl:text>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:MO_OffenceText">
		<fo:inline>
			<fo:block>
				<xsl:call-template name="FormatDisposalArea">
					<xsl:with-param name="string" select="$baseAll/ord:MonetaryDisposals"/>
				</xsl:call-template>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template name="FormatDisposalArea">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="contains($string,'&#10;')">
				<fo:block>
					<xsl:value-of select="substring($string,1, string-length(substring-before($string, '&#10;'))+1)"/>
				</fo:block>
				<xsl:if test="contains(substring-before($string, '&#10;'), 'Date of result:')">
					<fo:block space-after="12pt"/>
				</xsl:if>
				<fo:block hyphenate="true" language="en_GB"/>
				<xsl:call-template name="FormatDisposalArea">
					<xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<fo:block hyphenate="true" language="en_GB">
					<xsl:value-of select="$string"/>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="nar:MO_DisposalTotals">
		<fo:inline>
			<fo:block font-weight="bold" text-decoration="underline">
				<xsl:text>Totals:</xsl:text>
			</fo:block>
			<fo:block>
				<xsl:call-template name="ShowDisposalTotals"/>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template name="ShowDisposalTotals">
		<fo:inline>
			<fo:block>
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="$baseAll/ord:MonetaryTotals"/>
				</xsl:call-template>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<!-- To pay -->
	<xsl:template match="nar:MO_MonetaryOrderDetailAndAmount">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="string(normalize-space($baseAll/ord:PaymentRateValue))">
					<xsl:text>Payment of </xsl:text>
					<xsl:value-of select="$baseAll/ord:PaymentRateValue"/>
					<xsl:text> is to be made to HMCTS </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Payment is as per the monetary disposals, </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Payment address -->
	<xsl:template match="nar:MO_PayeeAddress">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="string(normalize-space($baseAll/ord:ParentGuardianName))">
					<xsl:text>by the Parent/Guardian (</xsl:text>
					<xsl:value-of select="$baseAll/ord:ParentGuardianName"/>
					<xsl:text>)</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>by the </xsl:text>
					<xsl:call-template name="AppellantDefendant"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> at the address below: </xsl:text>
			<fo:block/>
			<fo:block space-after="12pt"/>
			<fo:block>
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="$baseAll/ord:CollectionCentre/ord:CollectionCentreName"/>
				</xsl:call-template>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<!-- Courts impacted -->
	<xsl:template match="nar:MO_CommittingMags">
		<fo:inline>
			<fo:block font-weight="bold">
				<xsl:text>Committing Magistrates Court:</xsl:text>
			</fo:block>
			<fo:block>
				<xsl:call-template name="CommittingMagsCourt"/>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template name="CommittingMagsCourt">
		<xsl:value-of select="//ord:MonetaryOrder/ord:MOCommittingMagsCourt/ord:CourtHouseName"/>
	</xsl:template>
	<xsl:template match="nar:MO_CollectingMags">
		<fo:inline>
			<fo:block font-weight="bold">
				<xsl:text>Collecting Magistrates Court:</xsl:text>
			</fo:block>
			<fo:block>
				<xsl:call-template name="CollectingMagsCourt"/>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template name="CollectingMagsCourt">
		<xsl:value-of select="//ord:MonetaryOrder/ord:MOCollectingMagsCourt/ord:CourtHouseName"/>
	</xsl:template>
	<xsl:template match="nar:MO_SupervisingMags">
		<fo:inline>
			<fo:block font-weight="bold">
				<xsl:text>Supervising Magistrates Court:</xsl:text>
			</fo:block>
			<fo:block>
				<xsl:call-template name="SupervisingMagsCourt"/>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template name="SupervisingMagsCourt">
		<xsl:value-of select="//ord:MonetaryOrder/ord:MOSupervisingMagsCourt/ord:CourtHouseName"/>
	</xsl:template>
	<!-- Additional Details -->
	<xsl:template match="nar:MO_AdditionalDetails">
		<fo:inline>
			<fo:block>
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="//ord:MonetaryOrder/ord:AdditionalDetails"/>
				</xsl:call-template>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:MO_CollectionOrderMade">
		<fo:inline>
			<fo:block>
				<xsl:choose>
					<xsl:when test="(//ord:MonetaryOrder/ord:CollectionOrderMade)='Yes'">
						<xsl:text>A collection order has been made.</xsl:text>
					</xsl:when>
					<xsl:when test="(//ord:MonetaryOrder/ord:CollectionOrderMade)='No'">
						<xsl:text>A collection order has not been made.</xsl:text>
					</xsl:when>
				</xsl:choose>
			</fo:block>
		</fo:inline>
	</xsl:template>
	<!-- Fine (default period) -->
	<xsl:template match="nar:MO_MonetaryOrderFine">
		<fo:inline>
			<xsl:if test="$baseAll/ord:Fine/@selected='true'">
				<xsl:text>To serve</xsl:text>
				<xsl:for-each select="$baseAll/ord:Fine/ord:DefaultPeriod">
					<xsl:call-template name="TermDuration"/>
				</xsl:for-each>
				<xsl:text> imprisonment in default.</xsl:text>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Imprisonment (length of sentence) -->
	<xsl:template match="nar:MO_MonetaryOrderImprisonment">
		<fo:inline>
			<xsl:if test="$baseAll/ord:Imprisonment/@selected='true'">
				<xsl:text>The </xsl:text>
				<xsl:call-template name="AppellantDefendant"/>
				<xsl:text> was sentenced to a term of imprisonment of </xsl:text>
				<xsl:for-each select="$baseAll/ord:Imprisonment/ord:LengthOfSentence">
					<xsl:call-template name="TermDuration"/>
				</xsl:for-each>
				<xsl:text>.</xsl:text>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- ****************************** -->
	<!-- MONETARY ORDER END -->
	<!-- ****************************** -->
</xsl:stylesheet>
