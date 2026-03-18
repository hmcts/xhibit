<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2016. All rights reserved.                                         +
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
				xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
				xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" 
				xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" 
				xmlns:xsd="http://www.w3.org/2001/XMLSchema"
				xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" 
				xmlns:date="http://xsltsl.org/date-time"
				xmlns:str="http://xsltsl.org/string"
				xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"	  
				xmlns:fo="http://www.w3.org/1999/XSL/Format"
				xmlns:xso="http://www.w3.org/1999/XSL/Transform"
				exclude-result-prefixes="fo"
				extension-element-prefixes="util date str doc xsd n1 apd cs">	

<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" /> 
<xsl:include href="gcsUtility.xsl" />			      

<doc:reference xmlns="">
	<referenceinfo>
		<releaseinfo role="meta">Version 1.0</releaseinfo>
		<author>
			<surname>Hingston</surname>
			<firstname>Brian</firstname>
		</author>
	</referenceinfo>
	<title>Release From Prison Order Stylesheet</title>
	<para>File name : ReleaseFromPrisonOrder-v1.0xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Release From Prison Order in html format</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'1'" />
<xsl:variable name="minorVersion" select="'0'" />
<xsl:variable name="stylesheet" select="'ReleaseFromPrisonOrder-v1-0.xsl'" />
<xsl:variable name="last-modified-date" select="'2016-01-15'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="orderDate" >
	<xsl:call-template name="util:ukdate_mon">
		<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate" />
	</xsl:call-template>
</xsl:variable>

<xsl:variable name="subjectType">
	<xsl:call-template name="util:getSubjectType" >
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
				<xsl:call-template name="util:javascript" />
				<xsl:call-template name="util:cssTemplate" />
			</head>
			<body>
				
				<xsl:call-template name="util:showLogo" />
				
				<!-- +++++++++ following template produces order header             +++++++++ -->
				<xsl:call-template name="util:UniversalOrderHeader"> 
					<xsl:with-param name="OrderTitle">
						<p>
							<xsl:text>Bail granted: Order for release from custody</xsl:text>
							<xsl:if test="/cs:ReleaseFromPrisonOrder/cs:BailConditions/@Conditions = 'SubjectToBailOrder'">
								<xsl:text> subject to compliance with conditions</xsl:text>
							</xsl:if>						
						</p>
					</xsl:with-param>					
					<xsl:with-param name="OrderHeaderRoot" select="/cs:ReleaseFromPrisonOrder/cs:OrderHeader"/>
					<xsl:with-param name="OrderAddressee">
						<p>
							<xsl:text>The Governor </xsl:text>
							<xsl:value-of select="/cs:ReleaseFromPrisonOrder/cs:Governor"/>
						</p>
					</xsl:with-param>
				</xsl:call-template>
			
				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:ReleaseFromPrisonOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<xsl:call-template name="releaseDetails" />
				
				<xsl:call-template name="util:orderSignatory" />
								
				<!-- KN 20050517 - CR27 	-->
				<xsl:call-template name="util:copyOrderText" />
				<xsl:call-template name="util:copyrightText" />
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
				<para>Note: Uses the routine util:personsFullName to format the name.	</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:PersonalDetails">
	
		<xsl:text>The </xsl:text>
		<xsl:call-template name="str:to-lower">
			<xsl:with-param name="text" select="$subjectType"/>
		</xsl:call-template>
		
		<br />

		<!-- details of the defendant -->
		<table width="100%">
			<tr>
				<td width="65%">
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
				<td width="35%">
					<xsl:text>Date of birth: </xsl:text>
					<strong>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
					</xsl:call-template>
					</strong>
				</td>
			</tr>			
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- releaseDetails	Template		-->
	<!-- **************************************** -->
	
	<doc:template name="releaseDetails" xmlns="">
		<refpurpose>Shows the conditions of release.</refpurpose>
	</doc:template>
	
	<xsl:template name="releasedDetails" >
		<br />
		<xsl:text>Is now in your custody under a warrant of the </xsl:text>
		<xsl:value-of select="/cs:ReleaseFromPrisonOrder/cs:OriginalCourt/cs:CourtHouse/cs:CourtHouseName"/>
		<xsl:text> Crown Court</xsl:text>
		<br/>
		<xsl:text>Dated </xsl:text>
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="/cs:ReleaseFromPrisonOrder/cs:OriginalCourt/cs:Date"/>
		</xsl:call-template>
		<br/>
		<xsl:text>On the </xsl:text>
		<xsl:value-of select="$orderDate"/>
		<br/>
		<xsl:text>The Crown Court granted bail </xsl:text>
		<xsl:choose>
			<xsl:when test="/cs:ReleaseFromPrisonOrder/cs:BailConditions/@Conditions = 'SubjectToBailOrder'">
				<xsl:text>subject to the conditions given in the record of decision of bail dated: </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="/cs:ReleaseFromPrisonOrder/cs:BailConditions/cs:OrderDate"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>unconditionally.</xsl:text>
			</xsl:otherwise>
		</xsl:choose>	
		<h3>
			<xsl:text>It is ordered that</xsl:text>
		</h3>	
		<xsl:choose>
			<xsl:when test="/cs:ReleaseFromPrisonOrder/cs:BailConditions/@Ordered = 'SubjectToConditions'">
				<xsl:text>upon the  </xsl:text>
				<xsl:value-of select="$subjectType"/>
				<xsl:text> complying with the conditions which must be complied with before release (set out in the record of decision on bail)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> because the </xsl:text>
				<xsl:value-of select="$subjectType"/>
				<xsl:text> has complied with the conditions which had to be complied with before release.</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<br/>	
		<xsl:text>The </xsl:text>
		<xsl:value-of select="$subjectType"/>
		<xsl:text> shall be released from your custody in respect of the above warrant.</xsl:text>
	</xsl:template>
		
	
</xsl:stylesheet>
