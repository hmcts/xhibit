<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 1-5</releaseinfo>
			<author>
				<surname>Hingston</surname>
				<firstname>Brian</firstname>
			</author>
		</referenceinfo>
		<title>Monetary Order Stylesheet</title>
		<para>File name : MonetaryOrder-v1-5.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Monetary Order in html format</para>
				<para>New Order added as part of L-R-4574-01</para>
				<para>Updated for L-R-4581-01</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'1'"/>
	<xsl:variable name="minorVersion" select="'5'"/>
	<xsl:variable name="stylesheet" select="'MonetaryOrder-v1-5.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2016-10-12'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="subjectType">
		<xsl:call-template name="util:getSubjectType">
			<xsl:with-param name="caseNum" select="//cs:OrderHeader/cs:CaseNumber"/>
		</xsl:call-template>
	</xsl:variable>
	<!-- end Global Variables -->
	<xsl:output method="html" indent="yes"/>
	<!-- **************************************** -->
	<!-- Root Template					-->
	<!-- **************************************** -->
	<doc:template name="/" xmlns="">
		<refpurpose>Controls the sequence of elements to be displayed.</refpurpose>
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
					<xsl:with-param name="OrderTitle">
						<p>
							<xsl:text>Notice Of Monetary Order</xsl:text>
						</p>						
					</xsl:with-param>
					<xsl:with-param name="OrderHeaderRoot" select="/cs:MonetaryOrder/cs:OrderHeader"/>
					
				</xsl:call-template>

				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:MonetaryOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails"/>
				<xsl:call-template name="disposals"/>
				<xsl:call-template name="payment"/>
				<xsl:call-template name="AdditionalDetails"/>
				<xsl:call-template name="Signatory"/>
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
		<refdescription>Shows all personal information plus details of conviction date, court etc.</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:personsFullName to format the name.	</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:address_oneline to format the person's address all onto a single line</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template match="cs:PersonalDetails">
		<!-- details of the defendant -->
		<xsl:variable name="conviction" select="//cs:Conviction"/>
		<table width="100%">
			<tr>
				<td width="20%" valign="top">
					<xsl:text>In the case of the </xsl:text>
					<xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
				</td>
				<td width="80%">
					<table width="100%">
						<tr>
							<td width="65%">
								
									<xsl:call-template name="util:personsFullName">
										<xsl:with-param name="name" select="cs:Name"/>
									</xsl:call-template>
								
							</td>
							<td width="35%">
								<xsl:text>Date of birth : </xsl:text>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
								</xsl:call-template>
							</td>
						</tr>
						<tr>
							<td>
							<xsl:text>of </xsl:text>
								<xsl:call-template name="util:address_oneline">
									<xsl:with-param name="personalDetails" select="."/>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td/>
				<td>
					<xsl:text>The </xsl:text>
					<xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
					<xsl:text> was sentenced on </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:SentenceDate"/>
					</xsl:call-template>
					<xsl:choose>
						<xsl:when test="//cs:InCustody ='yes'">
							<xsl:text> and is in custody.</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> and is not in custody.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<hr/>
				</td>
			</tr>
			<tr>
				<td width="20%" valign="top">
				   
					    <xsl:text>Committing Magistrates Court</xsl:text>
                    				
				</td>
				<td width="80%">
					<xsl:value-of select="//cs:CommittingMagistratesCourt/cs:CourtHouseName"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<hr/>
				</td>
			</tr>
			<tr>
				<td width="20%" valign="top">
				   
					    <xsl:text>Collecting Magistrates Court</xsl:text>
                    				
				</td>
				<td width="80%">
					<xsl:value-of select="//cs:CollectingMagistratesCourt/cs:CourtHouseName"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<hr/>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template name="disposals">
		<!-- this template outputs the disposals details -->
		<h4>
			<u>
				<xsl:text>Disposals </xsl:text>
			</u>
		</h4>	
		<table width="90%">
			<tr>
				<td>
					<xsl:text>The </xsl:text>
					<xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
					<xsl:text> was ordered to pay:</xsl:text>
					<br/>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:call-template name="break">
						<xsl:with-param name="text" select="//cs:MonetaryDisposals"/>
					</xsl:call-template>
				</td>
			</tr>
			<tr/>
		</table>
		<h4>
			<u>
				<xsl:text>Totals:</xsl:text>
			</u>
		</h4>
		<table width="90%">
				<tr>
					<xsl:call-template name="break">
						<xsl:with-param name="text" select="//cs:MonetaryTotals"/>
					</xsl:call-template>
				</tr>
			</table>
	</xsl:template>
	
		<xsl:template name="payment">
		<!-- this template outputs the payment details -->
		<h4>
			<u>
				<xsl:text>Payment Terms</xsl:text>
			</u>
		</h4>	
		<xsl:choose>
						<xsl:when test="//cs:CollectionOrderMade ='yes'">
							<xsl:text>A collection order has been made.</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>A collection order has not been made.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
		<br/>
		<br/>
		<table width="90%">
			<tr>
				<td>
					
				</td>
			</tr>
			<tr/>
			<tr>
				<xsl:choose>
					<xsl:when test="string(normalize-space(//cs:PaymentRateValue))">
						<xsl:text>Payment of </xsl:text>
						<xsl:value-of select="//cs:PaymentRateValue" />
						<xsl:text> is to be made to HMCTS </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>Payment is as per the monetary disposals, </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="string(normalize-space(//cs:ParentGuardianName))">
						<xsl:text>by Parent/Guardian (</xsl:text>
						<xsl:value-of select="//cs:ParentGuardianName" />
						<xsl:text>)</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>by the </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="$subjectType"/>
						</xsl:call-template>
				   </xsl:otherwise>
				</xsl:choose>
				<xsl:text> at the address below: </xsl:text>
				<br/>
			</tr>
			<tr>
				<xsl:call-template name="break">
					<xsl:with-param name="text" select="//cs:CollectionCentre/cs:CollectionCentreName"/>
				</xsl:call-template>
			</tr>
			<tr>
				<br/>
				<xsl:if test="//cs:Fine-LengthOfSentence">
					 <xsl:text>To serve </xsl:text>
					 <xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="//cs:Fine-LengthOfSentence"/>
					</xsl:call-template>
					<xsl:text> imprisonment in default.</xsl:text>
				</xsl:if>
			</tr>
			<tr>
				<br/>
				<xsl:if test="//cs:Imprisonment-LengthOfSentence">
					 <xsl:text>The </xsl:text>
					 <xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
					<xsl:text> was sentenced to a term of imprisonment of  </xsl:text>
					 <xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="//cs:Imprisonment-LengthOfSentence"/>
					</xsl:call-template>
					<xsl:text>.</xsl:text>
				</xsl:if>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- PersonalGender					-->
	<!-- **************************************** -->
	<doc:template name="getPersonalGender" xmlns="">
		<refpurpose>If the sex of the defendant is known outputs 'him' or 'her' as appropriate.</refpurpose>
		<refdescription>If PersonalDetails/Sex = 'male' or 'female' show 'him' or 'her' otherwise show 'him/her'</refdescription>
	</doc:template>
	<xsl:template name="getPersonalGender">
		<xsl:choose>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'male'">
				<xsl:text>his</xsl:text>
			</xsl:when>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'female'">
				<xsl:text>her</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>his / her</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="AdditionalDetails">
		<xsl:if test="//cs:AdditionalDetails">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Additional Details:</xsl:text>
					</td>
					<td width="80%">
						<xsl:value-of select="//cs:AdditionalDetails"/>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	<xsl:template name="Signatory">
		<br/>
		<br/>
		<table width="100%">
			<tr>
				<td width="20%">
					<td width="80%" valign="top">
					<xsl:text>An Officer of the Crown Court</xsl:text>
					<br/>
					<xsl:text>Signed: </xsl:text>
					<strong>
						<xsl:value-of select="//cs:SignedBy/cs:CourtOfficer/apd:CitizenNameTitle"/>
						<xsl:text> </xsl:text>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="//cs:SignedBy/cs:CourtOfficer"/>
						</xsl:call-template>
					</strong>
					</td>
				</td>
			</tr>
			<tr>
				<td> </td>
				<td>
					<xsl:text>Date: </xsl:text>
					<strong>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:SignedDate"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="break">
		<xsl:param name="text" />
		<xsl:choose>
			<xsl:when test="contains($text, '&#xA;')">
				<xsl:value-of select="substring-before($text, '&#xA;')"/>
				<br/>
				<xsl:call-template name="break">
					<xsl:with-param name="text" select="substring-after($text, '&#xA;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
