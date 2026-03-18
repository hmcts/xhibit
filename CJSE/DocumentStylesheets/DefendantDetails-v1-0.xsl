<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2011. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 1-0</releaseinfo>
			<author>
				<surname>Hingston</surname>
				<firstname>Brian</firstname>
			</author>
		</referenceinfo>
		<title>Defendant Details Stylesheet</title>
		<para>File name : DefendantDetails-v1-0.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Defendant Details in html format</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'1'"/>
	<xsl:variable name="minorVersion" select="'0'"/>
	<xsl:variable name="stylesheet" select="'defendantdetails-v1-0.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2011-07-05'"/>
	<!-- End Version Information -->
	<!-- **************************************** -->
	<!-- Root Template                  -->
	<!-- **************************************** -->
	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
				<xsl:call-template name="header"/>
				<xsl:call-template name="details"/>
				<xsl:call-template name="util:copyrightText"/>
			</body>
		</html>
	</xsl:template>
	<!-- **************************************** -->
	<!-- lineBreak Template                    -->
	<!-- **************************************** -->
	<xsl:template name="lineBreak">
		<xsl:param name="text"/>
		<xsl:choose>
			<xsl:when test="contains($text, '&#xa;')">
				<xsl:value-of select="substring-before($text, '&#xa;')"/>
				<br/>
				<xsl:call-template name="lineBreak">
					<xsl:with-param name="text" select="substring-after($text, '&#xa;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- Court Template                             -->
	<!-- **************************************** -->
	<xsl:template name ="header">
		<xsl:variable name="courthouse" select="//cs:DefendantDetails/cs:CourtHouse"/>
		<table WIDTH="100%">
			<tr>
				<td align="right">
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:DefendantDetails/cs:DocumentID/cs:TimeStamp"/>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<font size="5">
			<strong>
				<!-- pick up first court type -->
				The <xsl:value-of select="$courthouse/cs:CourtHouseType"/>
			</strong>
			<br/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="$courthouse/cs:CourtHouseName"/>
			<br/>
			<xsl:text>Case Number : </xsl:text>
			<xsl:value-of select="//cs:DefendantDetails/cs:CaseNumber"/>
		</font>
		<h2>
			<table width="100%">
				<tr>
					<td align="center">
						<font size="5pt">
							<b>
								<xsl:text>Defendant Details</xsl:text>
							</b>
						</font>
					</td>
				</tr>
			</table>
		</h2>
		<xsl:text>Data marked in bold has changed</xsl:text>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- header Template                    -->
	<!-- **************************************** -->
	<doc:template name="details" xmlns="">
		<refpurpose>Create the defendant details.</refpurpose>
	</doc:template>
	<xsl:template name="details">
		<!-- processes the Header information - defendant and court -->
		<xsl:variable name="personalDetails" select="//cs:DefendantDetails/cs:Defendant/cs:PersonalDetails"/>
		<span class="emphasis">
			<xsl:text>Defendant Name</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'Name'">
			<b>
				<xsl:value-of select="$personalDetails/cs:Name/apd:CitizenNameTitle"/>
				<xsl:text> </xsl:text>
				<xsl:call-template name="util:personsFullName">
					<xsl:with-param name="name" select="$personalDetails/cs:Name"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
				<xsl:value-of select="personalDetails/cs:Name/apd:CitizenNameSuffix"/>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:value-of select="personalDetails/cs:Name/apd:CitizenNameTitle"/>
				<xsl:text> </xsl:text>
				<xsl:call-template name="util:personsFullName">
					<xsl:with-param name="name" select="$personalDetails/cs:Name"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
				<xsl:value-of select="$personalDetails/cs:Name/apd:CitizenNameSuffix"/>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>Gender</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'Sex'">
			<b>
				<xsl:if test="$personalDetails/cs:Sex">
					<xsl:choose>
						<xsl:when test="$personalDetails/cs:Sex = 'unknown'">
							<xsl:text>Company</xsl:text>
						</xsl:when>
						<xsl:when test="$personalDetails/cs:Sex != 'unknown'">
							<xsl:value-of select="$personalDetails/cs:Sex"/>
						</xsl:when>
					</xsl:choose>
				</xsl:if>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:if test="$personalDetails/cs:Sex">
					<xsl:choose>
						<xsl:when test="$personalDetails/cs:Sex = 'unknown'">
							<xsl:text>Company</xsl:text>
						</xsl:when>
						<xsl:when test="$personalDetails/cs:Sex != 'unknown'">
							<xsl:value-of select="$personalDetails/cs:Sex"/>
						</xsl:when>
					</xsl:choose>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>Date of Birth</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'DateofBirth'">
			<b>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="$personalDetails/cs:DateOfBirth/apd:BirthDate"/>
				</xsl:call-template>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="$personalDetails/cs:DateOfBirth/apd:BirthDate"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>Address</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'Address'">
			<b>
				<xsl:call-template name="util:address">
					<xsl:with-param name="personalDetails" select="$personalDetails"/>
				</xsl:call-template>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:call-template name="util:address">
					<xsl:with-param name="personalDetails" select="$personalDetails"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>Nationality</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'Nationality'">
			<b>
				<xsl:value-of select="$personalDetails/cs:Nationality"/>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:value-of select="$personalDetails/cs:Nationality"/>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>Masked Details</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'MaskedDetails'">
				<xsl:text>Is masked?:</xsl:text>
				<b>
					<xsl:value-of select="$personalDetails/cs:IsMasked"/>
				</b>
				<br/>
				<xsl:text>Masked Name:</xsl:text>
				<b>
					<xsl:value-of select="$personalDetails/cs:MaskedName"/>
				</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:text>Is masked?:</xsl:text>
				<xsl:value-of select="$personalDetails/cs:IsMasked"/>
				<br/>
				<xsl:text>Masked Name:</xsl:text>
				<xsl:value-of select="$personalDetails/cs:MaskedName"/>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>ASN No: </xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'ASN'">
			<b>
				<xsl:value-of select="//cs:DefendantDetails/cs:Defendant/cs:ASNs/cs:ASN"/>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:value-of select="//cs:DefendantDetails/cs:Defendant/cs:ASNs/cs:ASN"/>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>PTI Unique Ref: </xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'PTIURN'">
			<b>
				<xsl:value-of select="//cs:DefendantDetails/cs:Defendant/cs:URN"/>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:value-of select="//cs:DefendantDetails/cs:Defendant/cs:URN"/>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<span class="emphasis">
			<xsl:text>Last Conviction Date:</xsl:text>
			<br/>
		</span>
		<td>
		<xsl:choose>
			<xsl:when test="//cs:DefendantDetails/cs:SectionsAmended/cs:SectionAmended = 'LastConvictionDate'">
			<b>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:DefendantDetails/cs:Defendant/cs:LastConvictionDate"/>
				</xsl:call-template>
			</b>
			</xsl:when> 
			<xsl:otherwise>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:DefendantDetails/cs:Defendant/cs:LastConvictionDate"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		</td>
		<br/>
		<br/>
		<hr/>
	</xsl:template>
</xsl:stylesheet>