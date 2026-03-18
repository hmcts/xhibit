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
			<releaseinfo role="meta">Version 1-0</releaseinfo>
			<author>
				<surname>Hingston</surname>
				<firstname>Brian</firstname>
			</author>
		</referenceinfo>
		<title>Detention And Training Order Stylesheet</title>
		<para>File name : DetentionAndTrainingOrder-v1-0.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Detention And Training Order in html format</para>
				<para>New Order added as part of L-R-4410-01</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'1'"/>
	<xsl:variable name="minorVersion" select="'0'"/>
	<xsl:variable name="stylesheet" select="'DetentionAndTrianingOrder-v1-0.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2015-05-19'"/>
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
							<xsl:text>Detention And Training Order</xsl:text>
						</p>						
					</xsl:with-param>
					<xsl:with-param name="OrderSubHeading">
						<xsl:choose>
							<xsl:when test="/cs:DetentionAndTrainingOrder/cs:Section105">
								<xsl:text>Offences committed during curency of the order</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>Order made under Section 100 Powers of Criminal Courts (Sentencing) Act 2000</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="OrderHeaderRoot" select="/cs:DetentionAndTrainingOrder/cs:OrderHeader"/>
					
				</xsl:call-template>

				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:DetentionAndTrainingOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails"/>
				<xsl:call-template name="util:AdditionalNotes"/>
				<xsl:call-template name="util:orderSignatory"/>
				<xsl:call-template name="util:associatedCases">
					<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
					<xsl:with-param name="rulesRequired" select="'false'"/>
				</xsl:call-template>
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
					<xsl:text>The </xsl:text>
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
								<xsl:text>was convicted of crime on </xsl:text>
								<xsl:variable name="convictDate">
									<xsl:choose>
										<xsl:when test="$conviction/cs:ConvictingCourt">
											<xsl:value-of select="$conviction/cs:ConvictingCourt/cs:Date"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$conviction/cs:ConvictionDate"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="$convictDate"/>
								</xsl:call-template>
							</td>
							<td/>
						</tr>
					</table>
				</td>
			</tr>
			<xsl:if test="$conviction/cs:ConvictingCourt">
				<tr>
					<td/>
					<td>
						<xsl:text>at </xsl:text>
						<xsl:value-of select="$conviction/cs:ConvictingCourt/cs:CourtHouse/cs:CourtHouseName"/>
						<xsl:if test="//cs:Conviction/cs:ConvictingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
							<xsl:text> Crown Court</xsl:text>
						</xsl:if>
						<xsl:text> and committed for sentence to the Crown Court.</xsl:text>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td/>
				<td>
					<xsl:text>Details of the conviction and sentence are on the court record.</xsl:text>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<hr/>
				</td>
			</tr>
			<tr>
				<td width="20%" valign="top">
				   
					    <xsl:text>The Court ordered</xsl:text>
                    				
				</td>
				<td width="80%">
					<xsl:text>on </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
					</xsl:call-template>
					<xsl:text> that for a term of </xsl:text>
				</td>
			</tr>
			<tr>
				<td/>
				<td>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderPeriod"/>
					</xsl:call-template>
					<xsl:choose>
						<xsl:when test="/cs:DetentionAndTrainingOrder/cs:Section105">
							<xsl:text> the defendant was made subject to a Detention and Training Order.</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> the defendant is subject to a period of detention and training followed by a period of supervision.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td/>
				<td>
					<xsl:choose>
						<xsl:when test="/cs:DetentionAndTrainingOrder/cs:Section105">
							<xsl:text>The Court ordered that under the provisions of Section 105 of the Powers of the Criminal Courts (Sentencing) Act 2000 the defendant be detained in secure accomodation for a further period of </xsl:text>
							<xsl:call-template name="util:decodeDuration">
								<xsl:with-param name="duration" select="//cs:Section105"/>
							</xsl:call-template>
							<xsl:text>. The period of detention to be served in such secure accomodation as may be determined by the Secretary of State or by such other person as authorised by him for that purpose. </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>The first period, of detention and training, is to be served in youth detention accomodation as determined by the Secretary of State. The second period, of supervision, is to be served in the community.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
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
</xsl:stylesheet>
