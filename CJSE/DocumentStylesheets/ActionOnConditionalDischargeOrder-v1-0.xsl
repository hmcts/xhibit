<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2018. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 1</releaseinfo>
			<author>
				<surname>Hingston</surname>
				<firstname>Brian</firstname>
			</author>
		</referenceinfo>
		<title>Action On Conditional Discharge Order Stylesheet</title>
		<para>File name : ActionOnConditionalDischargeOrder-v1-0.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Action On Conditional Discharge Order in html format</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'1'"/>
	<xsl:variable name="minorVersion" select="'0'"/>
	<xsl:variable name="stylesheet" select="'ActionOnConditionalDischargeOrder-v1-0.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2018-05-22'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="OrderTITLE">
		<xsl:text>Notice of action taken on a Conditional Discharge upon conviction of a further offence</xsl:text>
	</xsl:variable>
	<!-- end Global Variables -->
	<xsl:output method="html" indent="yes"/>
	<!-- **************************************** -->
	<!-- Root Template						-->
	<!-- **************************************** -->
	<doc:template name="/" xmlns="">
		<refpurpose>Controls the sequence of elements to be created in the html page.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:javascript to embed the javascript functions (used to load the Crown Logo) in the generated html.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:cssTemplate to embed the CSS stylesheet in the generated html.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:showLogo to embed the link to the Crown logo in the generated html</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:javascript"/>
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<xsl:call-template name="util:showLogo"/>
				<!-- +++++++++ following template produces order header             +++++++++ -->
				<xsl:call-template name="util:UniversalOrderHeader">
					<xsl:with-param name="OrderTitle" select="$OrderTITLE"/>
					<xsl:with-param name="OrderHeaderRoot" select="/cs:ActionOnConditionalDischargeOrder /cs:OrderHeader"/>
				</xsl:call-template>
				<!-- +++++++++ following template processes the Personal Details+++++++++ -->
				<xsl:apply-templates select="/cs:ActionOnConditionalDischargeOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails"/>
				<xsl:call-template name="ACD_ForAttention"/>
				<xsl:call-template name="ACD_OriginalSentence"/>
				<xsl:call-template name="ACD_Offences"/>
				<xsl:call-template name="ACD_Order"/>
				<xsl:call-template name="util:orderSignatory"/>
				<br/>
				<xsl:call-template name="util:associatedCases">
					<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
					<xsl:with-param name="rulesRequired" select="'false'"/>
				</xsl:call-template>
				<br/>		
				<xsl:call-template name="util:copyOrderText"/>
				<xsl:call-template name="util:copyrightText"/>
			</body>
		</html>
	</xsl:template>
	<!-- **************************************** -->
	<!-- PersonalDetails Template			-->
	<!-- **************************************** -->
	<doc:template name="PersonalDetails" xmlns="">
		<refpurpose>Shows the personal information eg name, birth date, address etc.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:personsFullName to format the name.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:address_oneline to format the person's address all onto a single line</para>
			</listitem>
			<listitem>
				<para>Note: If there is data avalable for the Petty Sessional area then that is output also.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template match="cs:PersonalDetails">
		<table WIDTH="100%">
			<tr>
				<td WIDTH="70%">
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			<tr>
				<td WIDTH="70%">
					<strong>
						<xsl:call-template name="util:address_oneline">
							<xsl:with-param name="personalDetails" select="."/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			<tr>
				<td WIDTH="70%">
					<xsl:text>Date of birth : </xsl:text>
					<strong>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	
	<!-- Template to display Addressee Court Text -->
	<xsl:template name="ACD_ForAttention">
		<h2>For Attention</h2>
		<xsl:text>The Chief Clerk</xsl:text>
		<br/>
		<xsl:value-of select="/cs:ActionOnConditionalDischargeOrder/cs:AddresseeCourt/cs:CourtHouseName"/>
		<br/>
		<xsl:for-each select="/cs:ActionOnConditionalDischargeOrder/cs:AddresseeCourt/cs:CourtHouseAddress/apd:Line">
			<xsl:if test=".!='-'">
				<xsl:value-of select="."/>
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:value-of select="/cs:ActionOnConditionalDischargeOrder/cs:AddresseeCourt/cs:CourtHouseAddress/apd:PostCode"/>
		<br/>
		<xsl:value-of select="/cs:ActionOnConditionalDischargeOrder/cs:AddresseeCourt/cs:CourtHouseDX"/>
		<br/>
	</xsl:template>
	
	<!-- Template to display Original Sentence text -->
	<xsl:template name="ACD_OriginalSentence">
		<h2>Original Sentence</h2>
		<xsl:text>Date order made by your Court: </xsl:text>
		<xsl:value-of select="/cs:ActionOnConditionalDischargeOrder/cs:OriginalOrderDate"/>
		<br/>
		<br/>
	</xsl:template>
	
	<!-- Template to display Original Sentence text -->
	<xsl:template name="ACD_Offences">
		<xsl:text>The offender named above has been convicted of a further offence while still subject to an order of conditional discharge made by your Court.</xsl:text>
		<br/>
		<xsl:text>The court record is available.</xsl:text>
		<br/>
		<br/>
		<xsl:text>Date convicted by this Court: </xsl:text>
		<xsl:value-of select="/cs:ActionOnConditionalDischargeOrder/cs:DateOfConviction"/>
	</xsl:template>
	
	<!-- Template to display Court Ordered text -->
	<xsl:template name="ACD_Order">
		<h2>Order of this Court</h2>
		<xsl:choose>
			<xsl:when test="/cs:ActionOnConditionalDischargeOrder/cs:CourtOrdered ='DidNotDealWithDefendant'">
				<xsl:text>The Court did not deal with the defendant for the offences for which the order of conditional discharge was made.</xsl:text>
			</xsl:when>
			<xsl:when test="/cs:ActionOnConditionalDischargeOrder/cs:CourtOrdered ='OrderForOffences'">
				<xsl:text>An order was made for the offences for which the order of conditional discharge was made.</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/cs:ActionOnConditionalDischargeOrder/cs:CourtOrdered" />			
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
		
	<!-- Template to display a bullet point with text -->
	<xsl:template name="bullet">
		<xsl:param name="text"/>
		<ul>
			<li>
				<xsl:value-of select="$text"/>
			</li>
		</ul>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="no_bullet">
		<xsl:param name="text"/>
		<table width="100%">
			<td width="5%"/>
			<td width="95%">
				<xsl:value-of select="$text"/>
			</td>
		</table>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="FormatTime">
		<xsl:param name="text"/>
		<xsl:value-of select="$text"/>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="FormatDate">
		<xsl:param name="text"/>
		<xsl:value-of select="$text"/>
	</xsl:template>
</xsl:stylesheet>
