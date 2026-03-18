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
		<releaseinfo role="meta">Version 2-2</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Bail Order Stylesheet</title>
		<para>File name : bailorder-v2-2.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Bail Order in html format</para>
			</section>
		</partintro>
	</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'2'" />
<xsl:variable name="stylesheet" select="'bailorder-v2-2.xsl'" />
<xsl:variable name="last-modified-date" select="'2004-07-26'" />
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

<xsl:variable name="conditionality">
	<xsl:choose>
	<xsl:when test="//cs:BailDecision='Conditional'">
		<xsl:text>conditionally</xsl:text>
	</xsl:when>
	<xsl:otherwise>
		<xsl:text>unconditionally</xsl:text>
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- end Global Variables -->

	      
<xsl:output method="html" indent="yes"/>

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
			<xsl:apply-templates select="cs:BailOrder/cs:OrderHeader" /> 
			
			<!-- +++++++++ following template process each section of the bail order +++++++++ -->	
			<xsl:call-template name="decisionAndConditions" />
			<xsl:apply-templates select="//cs:Reasons" /> 
			<xsl:call-template name="nextAppearance" /> 
			<xsl:call-template name="associatedCases" /> 
			
			
			</body>
		</html>
	</xsl:template>
	
	<doc:template name="OrderHeader" xmlns="">
		<refpurpose>Creates the Order Header information; including the court details and a list of any associated cases.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:OrderHeader">
	<!-- processes the OrderHeader node - constructs the initial header information for the output -->
		<xsl:variable name="outputType" select="cs:OrderType" />
		<xsl:variable name="caseNums">
			<xsl:for-each select="cs:CaseNumber | //cs:AssociatedCases/cs:AssociatedCase">
					<xsl:value-of select="."/>
					<br />
			</xsl:for-each>
		</xsl:variable>
		<h2>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="70%">
					<h1>In the <xsl:value-of select="cs:CourtHouse/cs:CourtHouseType"/>
					<xsl:text> at </xsl:text>
					<xsl:value-of select="cs:CourtHouse/cs:CourtHouseName"/>
					</h1>
				</td>
				<td WIDTH="15%"><xsl:text>Case No:</xsl:text></td>
				<td WIDTH="15%"><xsl:copy-of select="$caseNums"/></td>
			</tr>
			<tr>
				<td></td>
				<td><xsl:text>Court Code:</xsl:text></td>
				<td><xsl:value-of select="cs:CourtHouse/cs:CourtHouseCode"/></td>
			</tr>
			<!-- Added URN for CR49  -->
			<tr>
				<td></td>
				<td><xsl:text>PTI URN</xsl:text></td>
				<td><xsl:value-of select="cs:Defendant/cs:URN"/></td>
			</tr>
		</table>
		</h2>
		<xsl:apply-templates select="../cs:BailDecision" />
		<hr />
		<xsl:apply-templates select="cs:Defendant/cs:PersonalDetails" />
	</xsl:template>

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
		  	<para>Note: Uses the routine util:address~_oneline to format the person's address all onto a single line
			</para>
		  </listitem>
		  </itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:PersonalDetails">
		<h3>
			<xsl:text>Part 1 Personal details</xsl:text>
		</h3>
		<table WIDTH="100%" >
			<tr>
				<td WIDTH="70%">
					<xsl:text>The </xsl:text>
					<xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
					<xsl:text> : </xsl:text>
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
				<td WIDTH="30%">
					<xsl:text>Date of Birth : </xsl:text>
					<strong>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:text>of : </xsl:text>
					<strong>
					<xsl:call-template name="util:address_oneline" >
						<xsl:with-param name="personalDetails" select="." />
					</xsl:call-template>
					</strong>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:text>applied to this Court for bail.</xsl:text>
				</td>
			</tr>		
		</table>
	</xsl:template>
	
	
	<doc:template name="BailDecision" xmlns="">
		<refpurpose>Outputs the details of the bail decision.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:BailDecision">
		<center>
			<h1>
			<strong>
				<xsl:text>Bail granted </xsl:text>
				<xsl:value-of select="$conditionality"/>
				<xsl:text> (record of decision)</xsl:text>
			</strong>
			</h1>
		</center>
	</xsl:template>
	
	<doc:template name="decisionsAndConditions" xmlns="">
		<refpurpose>Outputs the bail decision and any conditions associated with the bail order.</refpurpose>
		<refdescription>
		  <para>Shows wether bail was granted uncondtionally or dependent on conditions.</para>
		  <para>Templates for pre and post conditions are applied if appropriate</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="decisionAndConditions" >
		<h3>
			<xsl:text>Part 2 The decision and any conditions</xsl:text>
		</h3>
		<p>
			<xsl:text>The court considered the application under the Bail Act 1976 on: </xsl:text>
			<xsl:value-of select="$orderDate"/>
		</p>
		<p>
			<xsl:text>Bail was granted: </xsl:text>
			<xsl:choose>
				<xsl:when test="$conditionality='conditionally'">
					<xsl:text>subject to the following conditions:</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$conditionality"/>
				</xsl:otherwise>
			</xsl:choose>
		</p>
		
		<!-- apply the conditions if any -->
		<xsl:apply-templates select="//cs:BailOrder/cs:PreConditions" /> 
		<xsl:apply-templates select="//cs:BailOrder/cs:PostConditions" /> 
	</xsl:template>
	
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
		  	<para>Note: The currency symbol at the moment is hard coded to pound symbol - ie &#x00A3;, this would need to be changed if switch to euros. 
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
		<!-- will always come first so always pre-fixed with (A) -->
		<h4>(A) To be complied with <em>before</em> release from custody</h4>
		<table width="100%" >
			<xsl:for-each select="cs:Condition">
				<xsl:variable name="text" >
					<xsl:if test="not (cs:Surety | cs:Security)" >
						<xsl:value-of select="normalize-space(cs:Description)"/>
					</xsl:if>
					<xsl:if test="cs:Surety">
						<xsl:text>To provide </xsl:text>
						<xsl:value-of select="normalize-space(cs:Description)"/>
						<xsl:text> in the sum of &#x00A3;</xsl:text>
						<xsl:value-of select="format-number(cs:Surety,'##,###,##0.00')"/>
						<xsl:text> to secure the surrender of the </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="$subjectType"/>
						</xsl:call-template>
						<xsl:text> to custody at the time and place directed (recognizance of </xsl:text>
						<xsl:value-of select="normalize-space(cs:Description)"/>
						<xsl:text> endorsed on Form 
						           5102D: 'Bail: recognizance of a surety').
						</xsl:text>
					</xsl:if>
					<xsl:if test="cs:Security">
						<xsl:text>To provide a security in the sum of &#x00A3;</xsl:text>
						<xsl:value-of select="format-number(cs:Security,'##,###,##0.00')"/>
						<xsl:text> to be deposited with the court.</xsl:text>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
	</xsl:template>
 
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
		  	<para>Note: The currency symbol at the moment is hard coded to pound symbol - ie &#x00A3;, this would need to be changed if switch to euros. 
			</para>
		  </listitem>
		  <para>Note: The template util:bulletRow is called to display each condition as a bulleted item in a row in a table.
			</para>
		  </itemizedlist>
		</refdescription>
	</doc:template>
 
	<xsl:template match="cs:PostConditions" >
		<h4>
			<xsl:text>(B) To be complied with </xsl:text><em>after</em><xsl:text> release from custody</xsl:text>
		</h4>
		<table width="100%">
			<xsl:for-each select="cs:Condition">
				<xsl:variable name="text">
					<xsl:if test="not (cs:Surety | cs:Security)" >
						<xsl:value-of select="normalize-space(cs:Description)"/>
					</xsl:if>
					<xsl:if test="cs:Surety">
						<xsl:text>To provide </xsl:text>
						<xsl:value-of select="normalize-space(cs:Description)"/>
						<xsl:text> in the sum of &#x00A3;</xsl:text>
						<xsl:value-of select="format-number(cs:Surety,'##,###,##0.00')"/>
						<xsl:text> to secure the surrender of the </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="$subjectType"/>
						</xsl:call-template>
						<xsl:text> to custody at the time and place directed (recognizance of </xsl:text>
						<xsl:value-of select="normalize-space(cs:Description)"/>
						<xsl:text> endorsed on Form 
						           5102D: 'Bail: recognizance of a surety').
						</xsl:text>
					</xsl:if>
					<xsl:if test="cs:Security">
						<xsl:text>To provide a security in the sum of &#x00A3;</xsl:text>
						<xsl:value-of select="format-number(cs:Security,'##,###,##0.00')"/>
						<xsl:text> to be deposited with the court.</xsl:text>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
	</xsl:template>
 
 	<doc:template name="Reasons" xmlns="">
		<refpurpose>Outputs any reasons for which the conditions have been imposed.</refpurpose>
		<refdescription>
		  <para>Iterates through the list of reasons displaying them.</para>
		  <para>
		  <emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <para>Note: The template util:bulletRow is called to display each reason as a bulleted item in a row in a table.
			</para>
		  </itemizedlist>
		</refdescription>
	</doc:template>
 
 	<xsl:template match="//cs:Reasons">
		
		<h3>
			<xsl:text>Part 3  Reason(s) for imposing the above conditions</xsl:text>
		</h3>
		<table width="100%" >
			<xsl:for-each select="//cs:Reason" >
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="normalize-space(.)"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
	</xsl:template>
 
 <doc:template name="NextAppearance" xmlns="">
		<refpurpose>Outputs the details of the next court appearance if any, follwed by the standard warning about failing to comply
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
		  <para>Note: util:time is used to show the time portion of AppearanceDateTime (signified by the presence of the 'T' separator
		  in AppearanceDateTime). 
			</para>
		  </itemizedlist>
		  <itemizedlist>
		  <para>Note: template uti:orderSignatory is used to show the signatories for the order. 
			</para>
		  </itemizedlist>
		</refdescription>
	</doc:template>
 
	<xsl:template name="nextAppearance">
		<h3>
			<xsl:text>Part 4  The next appearance</xsl:text>
		</h3>
		<xsl:text>The </xsl:text>
		<xsl:call-template name="str:to-lower">
			<xsl:with-param name="text" select="$subjectType"/>
		</xsl:call-template>
		<xsl:text> shall appear</xsl:text>
		<xsl:choose>
		<xsl:when test="//cs:AppearanceDateTime">
			<xsl:text> on: </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:AppearanceDateTime" />
			</xsl:call-template>
			<xsl:if test="contains(//cs:AppearanceDateTime,'T')" >
				<xsl:text> at </xsl:text>
				<xsl:call-template name="util:time">
				<xsl:with-param name="inTime" select="//cs:AppearanceDateTime" />
			</xsl:call-template>
			</xsl:if>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text> on a date and at a time to be notified</xsl:text>
		</xsl:otherwise>
		</xsl:choose>
		<xsl:text> before</xsl:text>
		<xsl:choose>
			<xsl:when test="//cs:AppearanceCourt/cs:CourtHouseType = 'Crown Court'">
				<xsl:text> the Crown Court sitting at: </xsl:text>
				<xsl:value-of select="//cs:AppearanceCourt/cs:CourtHouseName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>: </xsl:text>
				<xsl:value-of select="//cs:AppearanceCourt/cs:CourtHouseName"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text> or any other place that may be notified, there to surrender to custody.</xsl:text>
		<p />
		<table width="100%">
			<tr>
				<td width="5%" />
				<td width="90%">
					<h3>
						<xsl:text>Warning to the </xsl:text><xsl:value-of select="$subjectType" />
					</h3>
				</td>
			</tr>
		</table>
		<table width="100%">
			<xsl:call-template name="util:bulletRow">
				<xsl:with-param name="bulletWidth" select="'10'"/>
				<xsl:with-param name="bulletText" select="'If you do not surrender to the court you will commit an offence.'"/>
			</xsl:call-template>
			<xsl:call-template name="util:bulletRow">
				<xsl:with-param name="bulletWidth" select="'10'"/>
				<xsl:with-param name="bulletText" select="'You must comply with the conditions in part 2 while you are on bail.'"/>
			</xsl:call-template>
		</table>
		<table width="100%">
			<tr>
				<td width="5%" />
				<td width="90%">
					<xsl:text>If you do not, or if it seems likely that you will not, you may be remanded in custody.</xsl:text>
				</td>
			</tr>
		</table>
		<br />
		<xsl:call-template name="util:orderSignatory" />
	</xsl:template>

	<doc:template name="associatedCases" xmlns="">
		<refpurpose>Outputs the list of associated cases if any..</refpurpose>
		<refdescription>
		  <para>
		  <emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <para>Note: template util:associatedCases is called to display the other cases if any associated with this order.. 
			</para>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="associatedCases" >
		<xsl:call-template name="util:associatedCases">
			<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
		</xsl:call-template>
	</xsl:template>
 
	
</xsl:stylesheet>
