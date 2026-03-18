<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2016. All rights reserved.                                         +
-->

<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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
		<releaseinfo role="meta">Version 1.1</releaseinfo>
		<author>
			<surname>Hingston</surname>
			<firstname>Brian</firstname>
		</author>
	</referenceinfo>
	<title>Notice Of Acquittal Order Stylesheet</title>
	<para>File name : NoticeOfAcquittalOrder-v1.1xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Notice Of Acquittal Order in html format</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'1'" />
<xsl:variable name="minorVersion" select="'1'" />
<xsl:variable name="stylesheet" select="'NoticeOfAcquittalOrder-v1-1.xsl'" />
<xsl:variable name="last-modified-date" select="'2016-01-25'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="orderDate" >
	<xsl:call-template name="util:ukdate_mon">
		<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate" />
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
							<xsl:text>Notice of Acquittal</xsl:text>						
						</p>
					</xsl:with-param>					
					<xsl:with-param name="OrderHeaderRoot" select="/cs:NoticeOfAcquittalOrder/cs:OrderHeader"/>
					<xsl:with-param name="OrderAddressee">
						<p>
							<xsl:text>The Governor </xsl:text>
							<xsl:value-of select="/cs:NoticeOfAcquittalOrder/cs:Governor"/>
						</p>
					</xsl:with-param>
				</xsl:call-template>
			
				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:NoticeOfAcquittalOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<xsl:call-template name="acquittalDetails" />
				
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
	
		<xsl:text>The defendant</xsl:text>
		
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
	<!-- acquittalDetails	Template		-->
	<!-- **************************************** -->
	
	<doc:template name="acquittalDetails" xmlns="">
		<refpurpose>Shows the conditions of acquittal.</refpurpose>
	</doc:template>
	
	<xsl:template name="acquittalDetails" >
		<br />
		<xsl:text>was sent for trial from </xsl:text>
		<xsl:value-of select="/cs:NoticeOfAcquittalOrder/cs:SentFromCourt/cs:CourtHouse/cs:CourtHouseName"/>
		<br/>
		<xsl:text>on </xsl:text>
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="/cs:NoticeOfAcquittalOrder/cs:SentFromCourt/cs:Date"/>
		</xsl:call-template>
		<br/>
		<xsl:text>charged with: </xsl:text>
		<xsl:value-of select="/cs:NoticeOfAcquittalOrder/cs:Charges"/>
		<br/>
		<br/>
		<xsl:text>On the </xsl:text>
		<xsl:value-of select="$orderDate"/>
		<br/>
		<xsl:text>the prosecution notified the court that it proposed to offer no evidence against the defendant who is currently in custody awaiting trial on this matter. </xsl:text>
		<xsl:text>With the consent of the defence and the trial judge, the case has today been dealt with </xsl:text>
		<xsl:if test="/cs:NoticeOfAcquittalOrder/cs:Details/@Consent = 'yes'">
				<xsl:text>in the absence of the parties </xsl:text>
		</xsl:if>	
		<xsl:text>and a verdict of not guilty entered under section 17 of the Criminal Justice Act 1967 for </xsl:text>
		<xsl:choose>
			<xsl:when test="/cs:NoticeOfAcquittalOrder/cs:Details/@Counts = 'All'">
				<xsl:text>each count on this indictment.</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>the count(s) on the indictment which (is) (are) numbered: </xsl:text>
				<xsl:value-of select="/cs:NoticeOfAcquittalOrder/cs:Details/cs:CountNumbers"/>
				<xsl:text>.</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<h3>
			<xsl:text>The defendant should therefore be discharged at once from your custody in respect of the above count(s) on this indictment</xsl:text>
		</h3>	
		<br/>	
		<xsl:text>A copy of the court record sheet is available on the XHIBIT portal.</xsl:text>
	</xsl:template>
		
	
</xsl:stylesheet>
