<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
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
			      extension-element-prefixes="date str doc util xsd n1 apd cs">	
			      
<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" />
<xsl:include href="gcsUtility.xsl" />

<doc:reference xmlns="">
	<referenceinfo>
		<releaseinfo role="meta">Version 2</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Skeleton schedule</title>
	<para>File name : Skeleton-v2.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Skeleton schedule in html format</para>
		</section>
	</partintro>
</doc:reference>


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'0'" />
<xsl:variable name="stylesheet" select="'Skeleton-v2.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-05-18'" />
<!-- End Version Information -->

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
				<para>Note: Uses the routine util:cssTemplate to embed the CSS stylesheet in the generated html.</para>
			</listitem>
		</itemizedlist>
	</doc:template>

	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate" />
			</head>
			<body>

				<!-- +++++++++ following template produces list header             +++++++++ -->
				<xsl:apply-templates select="cs:SkeletonSchedule/cs:IssuingCourtHouse" /> 
				
				<!-- +++++++++ following template produces list body               +++++++++ -->			
				<xsl:apply-templates select="cs:SkeletonSchedule/cs:Days" /> 
				
				<!-- KN 20050517 - CR27 	-->
				<xsl:call-template name="util:copyrightText" />

			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- IssuingCourtHouse Template		-->
	<!-- **************************************** -->

	<doc:template name="IssuingCourtHouse" xmlns="">
		<refpurpose>Constructs the report header.</refpurpose>
	</doc:template>

	<xsl:template match="cs:IssuingCourtHouse">
	<!-- processes the CrownCourt node - constructs the initial header information for the output -->
		<xsl:variable name="reporttype" select="'Skeleton Schedule'" />
		<h1> The <xsl:value-of select="cs:CourtHouseType"/>
		<xsl:text> at </xsl:text>
		<xsl:value-of select="cs:CourtHouseName"/>
		</h1>
		<h2>
		<table width="100%">
			<tr>
				<td width="75%">
					<xsl:value-of select="$reporttype" />
				</td>
				<td width="25%">
					<xsl:text>Case : </xsl:text>
					<xsl:value-of select="../cs:CaseNumber" />
				</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="../cs:CaseTitle" />
				</td>
				<td />
			</tr>
			<xsl:choose>
			<xsl:when test="../cs:VenueCourtHouse" >
				<tr>
					<td>
						<xsl:text>Venue : </xsl:text>
						<xsl:choose>
							<xsl:when test="//cs:VenueCourtHouse/cs:CourtHouseType = 'Crown Court'">
								<xsl:text> The </xsl:text>
								<xsl:value-of select="//cs:VenueCourtHouse/cs:CourtHouseName"/>
								<xsl:text> Crown Court</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//cs:VenueCourtHouse/cs:CourtHouseName"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr>
					<td>
						<xsl:text>Venue : </xsl:text>
						<xsl:choose>
							<xsl:when test="//cs:IssuingCourtHouse/cs:CourtHouseType = 'Crown Court'">
								<xsl:text> The </xsl:text>
								<xsl:value-of select="//cs:IssuingCourtHouse/cs:CourtHouseName"/>
								<xsl:text> Crown Court</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//cs:IssuingCourtHouse/cs:CourtHouseName"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:otherwise>
			</xsl:choose>
		</table>
		</h2>
		<hr />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Days	Template				-->
	<!-- **************************************** -->
	
	<doc:template name="Days" xmlns="">
		<refpurpose>Constructs the report body.</refpurpose>
		<refdescription>
			<para>Iterates through the Day nodes, and then for each Day node iterates
		through the AMSession and PMSession nodes to create the report body. The 
		template 'witnesses' is called to format the detail for each session.</para>
		</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: If the Date attribute is present for the Day node then the date value is shown.</para>
				<para>If it is not present then each Day node is output in relative format eg 'Week 1 Day 1, Week 1 Day 2..' etc</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine date:format-date-time to format the Date attribute of the Day if it is present</para>
			</listitem>
		</itemizedlist>
	</doc:template>

	<xsl:template match="cs:Days">
	<!-- processes the Days nodesets - constructs the body of the output -->
		<xsl:for-each select="cs:Day" >
			<strong>
				<xsl:choose>
				<xsl:when test="./@Date">
					<xsl:text>Day </xsl:text>
					<xsl:value-of select="position()"/>
					<xsl:text> : </xsl:text>
					<xsl:call-template name="date:format-date-time">
						<xsl:with-param name="year" select="substring(./@Date,1,4)" />
						<xsl:with-param name="month" select="substring(./@Date,6,2)" />
						<xsl:with-param name="day" select="substring(./@Date,9,2)" />			
						<xsl:with-param name="format" select="'%A %D %B %Y'" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Week Number </xsl:text>
					<xsl:variable name="week" >
						<xsl:choose>
						<xsl:when test="(position() mod 5)=0">
							<xsl:value-of select="floor(position() div 5)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="floor(position() div 5) + 1" />
						</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="day" >
						<xsl:choose>
						<xsl:when test="(position() mod 5) = 0">
							<xsl:value-of select = "5" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="position() mod 5"/>
						</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:value-of select="$week"/>
					<xsl:text>  Trial Day </xsl:text>
					<xsl:value-of select="$day"/>					
				</xsl:otherwise>
				</xsl:choose>
			</strong>
			<br />
			<xsl:for-each select="cs:AMsession | cs:PMsession">
				<h4>
				<xsl:choose>
					<xsl:when test="name()='cs:AMsession'">
						<xsl:text>A.M. Session</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>P.M. Session</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				</h4>
				<br />
				
				<!-- +++++++++++  show the witenesses for the session +++++++++++++ -->
				<xsl:call-template name="witnesses" >
					<xsl:with-param name="witnesses" select="./cs:Witnesses" />
				</xsl:call-template>
				
				
				<br />
				<xsl:value-of select="./cs:AMnotes | ./cs:PMnotes" />
				
			</xsl:for-each>
			<hr />				
		</xsl:for-each>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- witnesses	Template			-->
	<!-- **************************************** -->
	
	<doc:template name="witnesses" xmlns="">
		<refpurpose>Constructs the output of witness details for each session.</refpurpose>
		<refdescription>
			<para>Iterates through the Witness nodes sorted into time order, and then for each Witness 
		node outputs the time, the witness name, type (defence, prosecution) and status.</para>
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>witnesses</term>
					<listitem>
						<para>Witnesses node for list of witnesses for a particular session. </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
	</doc:template>
	
	<xsl:template name="witnesses" >
	<!-- creates the list of witnesses for a particular session 
	     parameter - witnesses  - parent node for all witnesses in session
	-->
		<xsl:param name="witnesses" />
		<table width="100%">
		<xsl:for-each select="$witnesses/cs:Witness" >
			<xsl:sort select="substring(./@Time,1,5)" />
			<tr>
				<td width="5%" />
				<td width="15%">
					<xsl:value-of select="substring(./@Time,1,5)" />
				</td>
				<td width="30%">
					<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName" />
				</td>
				<td width="25%">
					<xsl:value-of select="./@WitnessType" />
				</td>
				<td width="25%">
					<xsl:value-of select="./@WitnessStatus" />
				</td>
			</tr>
		</xsl:for-each>
		</table>
	
	</xsl:template>
	
</xsl:stylesheet>
