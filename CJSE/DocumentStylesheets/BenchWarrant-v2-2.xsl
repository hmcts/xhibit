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
				extension-element-prefixes="util date str doc xsd n1 apd cs">	

<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" /> 
<xsl:include href="gcsUtility.xsl" />			      

<doc:reference xmlns="">
	<referenceinfo>
		<releaseinfo role="meta">Version 2.2</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Bench Warrant Stylesheet</title>
	<para>File name : BenchWarrant-v2-2.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Bench Warrant in html format</para>
			<para>PR56839 - Remove Date condition</para>
			<para>Added PR 57273</para>
			<para>removed space from of :</para>
			<para>Covered for PR 57051 - until Mercator issue Release value correctly</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'2a'" />
<xsl:variable name="stylesheet" select="'BenchWarrant-v2-2.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-10-10'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="orderDate" select="//cs:OrderHeader/cs:OrderDate" />
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
				<para>Note: Uses the routine util:javascript to embed the javascript functions (used to load the Crown Logo) in the generated html.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:cssTemplate to embed the CSS stylesheet in the generated html.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:showLogo to embed the link to the Crown logo in the generated html
				</para>
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
						<xsl:text>ALL CONSTABLES ARE ORDERED To Arrest</xsl:text>
					</xsl:with-param>					
					<xsl:with-param name="OrderHeaderRoot" select="/cs:BenchWarrant/cs:OrderHeader"/>
					<xsl:with-param name="OrderTitleDate">					
						<xsl:text>on </xsl:text>
						<xsl:call-template name="date:format-date-time">
							<xsl:with-param name="year" select="substring($orderDate,1,4)" />
							<xsl:with-param name="month" select="substring($orderDate,6,2)" />
							<xsl:with-param name="day" select="substring($orderDate,9,2)" />			
							<xsl:with-param name="format" select="'%D %B %Y'" />
						</xsl:call-template>
					</xsl:with-param>
				</xsl:call-template>
						
				<!-- +++++++++ following template processes the Personal Details+++++++++ -->	
	   			<xsl:apply-templates select="/cs:BenchWarrant/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<!-- +++++++++ following template process each section of the Bench Warrant +++++++++ -->	
				<xsl:call-template name="decisionAndConditions" />
				<xsl:apply-templates select="cs:BenchWarrant/cs:NextAppearance" /> 
				
				<!-- KN 20050517 - CR27 	-->
				<xsl:call-template name="util:copyOrderText" />
				<xsl:call-template name="util:copyrightText" />
						
			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- PersonalDetails					-->
	<!-- **************************************** -->

	<doc:template name="PersonalDetails" xmlns="">
		<refpurpose>Shows the personal information eg name, birth date, address etc.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:personsFullName to format the name.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:address_oneline to format the person's address all onto a single line
				</para>
			</listitem>
		</itemizedlist>
	</doc:template>

	<xsl:template match="cs:PersonalDetails">
		<table width="100%" >
			<tr>
				<td width="20%">
					<xsl:text>The defendant</xsl:text>
				</td>
				<td width="80%">
				
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			<xsl:if test="cs:DateOfBirth">
				<tr>
					<td>
						<xsl:text>Date of birth: </xsl:text>
					</td>
					<td>
						<strong>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
							</xsl:call-template>
						</strong>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="cs:Address">
				<tr>
					<td valign="top">
						<xsl:text>of:</xsl:text>
					</td>
					<td>
						<strong>
						<xsl:call-template name="util:address_oneline" >
							<xsl:with-param name="personalDetails" select="." />
						</xsl:call-template>
						</strong>
					</td>
				</tr>	
			</xsl:if>
			<tr>
				<td />
				<td>
					<hr />
					<xsl:text>who, having been released on bail subject to a duty to surrender to the custody of the Crown Court, </xsl:text>
					<xsl:text>has failed to surrender as required and </xsl:text>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- decisionsAndConditions			-->
	<!-- **************************************** -->
	
	<doc:template name="decisionsAndConditions" xmlns="">
		<refpurpose>Outputs the bail decision and any conditions associated with the bail order.</refpurpose>
		<refdescription>
			<para>Shows wether bail was granted uncondtionally or dependent on conditions.</para>
			<para>Templates for pre and post conditions are applied if appropriate</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="decisionAndConditions" >
		<table width="100%" >
			<tr>
				<td width="20%" />
				<td>
					<xsl:variable name="conditionality" select="//cs:Release" />
					<xsl:choose>
					<xsl:when test="$conditionality = 'Refused'">
						<!-- test removed PR56839 
						<xsl:if test="//cs:NextAppearance/cs:AppearanceDateTime" >
						-->
						<xsl:text>bring </xsl:text>
						<xsl:call-template name="getPersonalGender"/>
						<xsl:text> forthwith before the </xsl:text>
						<xsl:value-of select="//cs:NextAppearance/cs:AppearanceCourt/cs:CourtHouseType"/>
						<!-- test removed PR56839 
						</xsl:if>
						-->
					</xsl:when>
					<xsl:when test="$conditionality = 'Unconditional'">
						<xsl:text>release </xsl:text>
						<xsl:call-template name="getPersonalGender"/>
						<xsl:text> on bail unconditionally.</xsl:text>
					</xsl:when>
					<xsl:when test="$conditionality = 'Conditional' and not (cs:BenchWarrant/cs:PreConditions | cs:BenchWarrant/cs:PostConditions) ">
						<xsl:text>release </xsl:text>
						<xsl:call-template name="getPersonalGender"/>
						<xsl:text> on bail unconditionally.</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<!-- Must be conditional -->
						<xsl:text>release </xsl:text>
						<xsl:call-template name="getPersonalGender"/>
						<xsl:text> on bail subject to the following condition(s):</xsl:text>
						<!-- apply the conditions if any -->
						<xsl:apply-templates select="//cs:BenchWarrant/cs:PreConditions | //cs:BenchWarrant/cs:PostConditions" />
					</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- preConditions					-->
	<!-- **************************************** -->

	<doc:template name="preConditions" xmlns="">
		<refpurpose>Outputs any pre-conditions associated with the bail order.</refpurpose>
		<refdescription>
			<para>Iterates through the list of pre-conditions displaying them.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Note: Show as section A if present
				</para>
				</listitem>
				<listitem>
					<para>Note: If Surety or Security is present the the value should be displayed as formatted currency eg currenct symbol,
				            thousand separator, 2 decimal places, leading zeros suppressed.
				</para>
				</listitem>
				<listitem>
					<para>Note: The currency symbol at the moment is hard coded to GB pound symbol, this would need to be changed if switch to euros. 
				</para>
				</listitem>
				<listitem>
					<para>Note: The template util:bulletRow is called to display each condition as a bulleted item in a row in a table.
				</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:PreConditions" >
		<h4>
			<xsl:text>A. To be complied with </xsl:text>
			<em>
				<xsl:text>BEFORE</xsl:text>
			</em>
			<xsl:text> release on bail:</xsl:text>
		</h4>
		<table width="100%" >
			<xsl:for-each select="cs:Condition">
				<xsl:variable name="text" >
					<xsl:if test="not (cs:Surety | cs:Security)" >
						<xsl:value-of select="normalize-space(cs:Description)"/>
					</xsl:if>
					<xsl:if test="cs:Surety">
						<xsl:text>to provide </xsl:text>
						<xsl:value-of select="normalize-space(cs:Description)"/>
						<xsl:text> in the sum of &#x00A3;</xsl:text>
						<xsl:value-of select="format-number(cs:Surety,'##,###,##0.00')"/>
						<xsl:text> to secure the surrender of the defendant to custody at the time and place directed</xsl:text>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- postConditions					-->
	<!-- **************************************** -->
	
	 <doc:template name="PostConditions" xmlns="">
		<refpurpose>Outputs any post-conditions associated with the bail order.</refpurpose>
		<refdescription>
			<para>Iterates through the list of post-conditions displaying them.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Note: Show as section B if present
				</para>
				</listitem>
				<listitem>
					<para>Note: If Surety or Security is present the the value should be displayed as formatted currency eg currenct symbol,
				            thousand separator, 2 decimal places, leading zeros suppressed.
				</para>
				</listitem>
				<listitem>
					<para>Note: The currency symbol at the moment is hard coded to GB pound symbol, this would need to be changed if switch to euros. 
				</para>
				</listitem>
				<para>Note: The template util:bulletRow is called to display each condition as a bulleted item in a row in a table.
				</para>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:PostConditions" >
		<xsl:if test="//cs:PreConditions" >
			<h4>
				<xsl:text>AND</xsl:text>
			</h4>
		</xsl:if>
		<h4>
			<xsl:text>B. </xsl:text>
			<xsl:text>To be complied with </xsl:text>
			<em>
				<xsl:text>AFTER</xsl:text>
			</em>
			<xsl:text> release on bail:</xsl:text>
		</h4>
		<table width="100%" >
			<xsl:for-each select="cs:Condition">
				<xsl:variable name="text" >
					<xsl:value-of select="normalize-space(cs:Description)"/>
					<xsl:if test="cs:Surety">
						<xsl:text>to provide </xsl:text>
						<xsl:value-of select="normalize-space(cs:Description)"/>
						<xsl:text> in the sum of &#x00A3;</xsl:text>
						<xsl:value-of select="format-number(cs:Surety,'##,###,##0.00')"/>
						<xsl:text> to secure the surrender of the defendant to custody at the time and place directed</xsl:text>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- NextAppearance				-->
	<!-- **************************************** -->

	 <doc:template name="NextAppearance" xmlns="">
		<refpurpose>Outputs the details of the next court appearance, 
			and then calls the routine to show the order signatories.</refpurpose>
		<refdescription>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<para>Note: If AppearanceDateTime is present then it is used to show the date and optionally the time of the next
			              appearance, otherwise the text 'on a date and time to be notified' is used. 
				</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: util:ukdate_mon is used to show the date portion of AppearanceDateTime. 
				</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: util:time is used to show the time portion of AppearanceDateTime (signified by the presence of the 'T' separator in
			  AppearanceDateTime). 
				</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: template util:orderSignatory is used to show the signatories for the order. 
				</para>
			</itemizedlist>
		</refdescription>
	</doc:template>	
	
	<xsl:template match="cs:NextAppearance">
		<table width="100%" >
			<tr>
				<td width="20%" />
				<td>
					<br />
					<xsl:text>to appear at the </xsl:text>
					<xsl:if test="//cs:AppearanceCourt/cs:CourtHouseType = 'Crown Court'">
						<xsl:text> Crown Court at </xsl:text>
					</xsl:if>
					<strong>
						<xsl:value-of select="//cs:AppearanceCourt/cs:CourtHouseName"/>			
					</strong>
					<br />
					<xsl:text>(or such other place as shall be notified)</xsl:text>
					<br />
					<xsl:choose>
					<xsl:when test="//cs:AppearanceDateTime">
						<xsl:variable name="appearance" select="//cs:AppearanceDateTime" />
						<xsl:text> on: </xsl:text>			
						<xsl:choose>
						<xsl:when test="contains($appearance,'T')">
							<strong>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="//cs:AppearanceDateTime" />
								</xsl:call-template>
								<xsl:text> at </xsl:text>
								<xsl:call-template name="util:time">
									<xsl:with-param name="inTime" select="//cs:AppearanceDateTime" />
								</xsl:call-template>
							</strong>
						</xsl:when>
						<xsl:otherwise>
							<strong>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="//cs:AppearanceDateTime" />
								</xsl:call-template>
							</strong>
						</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text> on such day and at such time as the court may direct</xsl:text>
					</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<br />
					<xsl:text> THERE to surrender </xsl:text>
					<xsl:call-template name="getPersonalGender"/>		
					<xsl:text>self into custody.</xsl:text>
				</td>
			</tr>
		</table>
					
		<xsl:call-template name="util:orderSignatory" />
	</xsl:template>

	
	<!-- **************************************** -->
	<!-- PersonalGender					-->
	<!-- **************************************** -->

	<doc:template name="getPersonalGender" xmlns="">
		<refpurpose>If the sex of the defendant is known outputs 'him' or 'her' as appropriate.</refpurpose>
		<refdescription>If PersonalDetails/Sex = 'male' or 'female' show 'him' or 'her' otherwise show 'him/her'</refdescription>
	</doc:template>
	
	<xsl:template name="getPersonalGender" >
		<xsl:choose>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'male'">
				<xsl:text>him</xsl:text>
			</xsl:when>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'female'">
				<xsl:text>her</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>him / her</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
 
</xsl:stylesheet>
