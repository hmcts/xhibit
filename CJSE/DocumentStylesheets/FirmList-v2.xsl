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
	
	<doc:reference xmlns="">
    	<referenceinfo>
			<releaseinfo role="meta">Version 2c</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Firm List Stylesheet - FirmList-v2.xsl</title>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Firm List in html format</para>
				<para>Added UCASE title Support PR56970 </para>
				<para>Removed Hearing Description Lookup in gcsUtils</para>
			</section>
		</partintro>
	</doc:reference>
		      
<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" />
<xsl:include href="gcsUtility.xsl" />


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'0c'" />
<xsl:variable name="stylesheet" select="'FirmList-v2.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-05-18'" />
<!-- End Version Information -->

<xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate" />
<xsl:variable name="endDate" select="//cs:ListHeader/cs:EndDate" />

<xsl:variable name="version"    select="//cs:ListHeader/cs:Version" />

<xsl:output method="html" indent="yes"/>

	<!-- **************************************** -->
	<!-- Root Template					-->
	<!-- **************************************** -->

	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate" />
			</head>
			<body>

				<!-- +++++++++ following template produces list header             +++++++++ -->
				<xsl:apply-templates select="cs:FirmList/cs:CrownCourt" /> 
				
				<!-- +++++++++ following template produces list body               +++++++++ -->			
				<xsl:apply-templates select="cs:FirmList/cs:CourtLists" /> 
		
				<!-- +++++++++ following template produces reserve list of hearings    +++++++++ -->			
				<xsl:apply-templates select="cs:FirmList/cs:ReserveList" /> 
				
				<!-- +++++++++ following template produces list footer             +++++++++ -->
				<xsl:call-template name="util:listFooter" >
					<xsl:with-param name="court" select="/cs:FirmList/cs:CrownCourt" />
				</xsl:call-template>
			
			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CrownCourt Template			-->
	<!-- **************************************** -->
	
	<doc:template match="/CrownCourt" xmlns="">
		<refpurpose>Creates the Report Header information.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:CrownCourt">
	<!-- processes the CrownCourt node - constructs the initial header information for the output -->
		<xsl:variable name="reporttype" select="'Criminal Firm List'" />
		<h1> The <xsl:value-of select="cs:CourtHouseType"/>
		<xsl:text> at </xsl:text>
		<xsl:value-of select="cs:CourtHouseName"/>
		</h1>
		<h2>
		<xsl:value-of select="$reporttype" />
		<br />
		<table width="100%">
			<tr>
				<td width="75%">
					<xsl:text> for the period </xsl:text>
					<xsl:call-template name="util:ukdate_fullMonth">
						<xsl:with-param name="inDate" select="$reportdate" />
					</xsl:call-template>
					<xsl:text> to </xsl:text>
					<xsl:call-template name="util:ukdate_fullMonth">
						<xsl:with-param name="inDate" select="$endDate" />
					</xsl:call-template>
				</td>
				<td width="25%">
					<xsl:value-of select="$version"/>
				</td>
			</tr>
		</table>
		</h2>
		<xsl:call-template name="util:publishDate"/>
	</xsl:template>
	
	<!-- For PR 56748 - only picking up the first day of the firm Lits -->
	<!--
	<xsl:key name="hearingByDate" 
	         match="cs:Hearing" 
			 use="//cs:Sittings/cs:Sitting/cs:Hearings/cs:Hearing/cs:HearingDetails/cs:HearingDate"/>
	-->

	<xsl:key name="hearingByDate" 
	         match="cs:Hearing" 
			 use="./cs:HearingDetails/cs:HearingDate"/>

	<!-- EN do f PR 56748 -->

	<xsl:key name="courtByName" 
	         match="//cs:CourtHouse/cs:CourtHouseName" 
			 use="//cs:CourtHouse/cs:CourtHouseName"/>		 		 
			 
	<!-- above two indexes are used to help with the control breaks on date and then court within date -->

	<!-- **************************************** -->
	<!-- CourtLists Template				-->
	<!-- **************************************** -->
	
	<doc:template match="/CourtLists" xmlns="">
		<refpurpose>Controls the creation of the report body that contains hearings organized by hearing date.</refpurpose>
		<refdescription>
			<para>Uses a key on HearingDate to iterate through the days covered by this list. Then for each date 
			        gets all the hearings on that date, sorted by SittingSequenceNumber within CourtRoomNumber. 
					At the start of each new sitting calls a template to display the judiciary 
					information, then calls a template to process each hearing.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Only display the court house name when it changes.
				</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template match="cs:CourtLists">
	<!-- processes the CourtList nodesets - constructs the body of the output -->

		<xsl:for-each select="./cs:CourtList/cs:Sittings/cs:Sitting/cs:Hearings/cs:Hearing
								[count(. | key('hearingByDate', ./cs:HearingDetails/cs:HearingDate)[1]) = 1]" >
			
		<xsl:variable name="dayListDate" select="./cs:HearingDetails/cs:HearingDate" />
		
		<hr />
		<center>
			<strong>
				<xsl:call-template name="date:format-date-time">
					<xsl:with-param name="year" select="substring($dayListDate,1,4)" />
					<xsl:with-param name="month" select="substring($dayListDate,6,2)" />
					<xsl:with-param name="day" select="substring($dayListDate,9,2)" />							
					<xsl:with-param name="format" select="'%A %d %B %Y'" />
				</xsl:call-template>
			</strong>	
		</center>
		<xsl:for-each select="//cs:CourtList[.//cs:HearingDetails/cs:HearingDate = $dayListDate]" >
			<xsl:sort select="cs:Sittings/cs:Sitting/cs:Hearings/cs:Hearing/cs:HearingDetails/cs:Date"/>
				<xsl:sort select="cs:Sittings/cs:Sitting/cs:CourtRoomNumber"/>
					<xsl:sort select="cs:Sittings/cs:Sitting/cs:SittingSequenceNo"/>	
			<xsl:variable name="sittingDate" >
				
			</xsl:variable>
			<strong>
			<xsl:if test="count(cs:CourtHouse/cs:CourtHouseName | key('courtByName', cs:CourtHouse/cs:CourtHouseName)[1]) = 1">
				<xsl:value-of select="cs:CourtHouse/cs:CourtHouseName"/>
			</xsl:if>
			</strong>
			<xsl:for-each select="cs:Sittings/cs:Sitting">
				<strong>
					<p>Court <xsl:value-of select="cs:CourtRoomNumber" /> 
					
					<xsl:variable name="sittingTime">
						<xsl:choose>
						<xsl:when test="cs:SittingAt">				
							<xsl:call-template name="date:format-date-time">
								<xsl:with-param name="hour" select="substring(cs:SittingAt,1,2)" />
								<xsl:with-param name="minute" select="substring(cs:SittingAt,4,2)" />						
								<xsl:with-param name="format" select="' - sitting at %I:%M %p'" />
							</xsl:call-template>
						</xsl:when>
						</xsl:choose>
					</xsl:variable>
	
					<!-- now display the date time combined -->
					<xsl:value-of select="$sittingTime"/>
					</p>
					<!-- +++++++++++  show the judge(s) for the sitting +++++++++++++ -->
					<xsl:call-template name="judiciary" >
						<xsl:with-param name="judiciary_NodeSet" select="./cs:Judiciary" />
					</xsl:call-template>
	
					<p />
				
					<xsl:value-of select="cs:SittingNote" /> 
					<br />
				</strong>
				<!-- +++++++++++  now process the hearings          +++++++++++++ -->
				<xsl:for-each select="cs:Hearings/cs:Hearing">
					<xsl:call-template name="hearing" />
				</xsl:for-each>
				
				<xsl:if test="not( position() = last())" >
					<hr />
				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
		</xsl:for-each> 
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- ReserveList Template			-->
	<!-- **************************************** -->
	
	<doc:template match="ReserveList"  xmlns="">
		<refpurpose>Controls the creation of the report body that contains hearings on the reserve list.</refpurpose>
		<refdescription>
			<para>Context node is ReserveList. Simply iterates through the hearings, calling a template to process each hearing.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template match="cs:ReserveList">
	
		<!-- processes the ReserveList which only consists of hearings -->
		<h4>
		<xsl:text>Reserve List</xsl:text>
		</h4>
		<xsl:for-each select="cs:Hearing">
			<xsl:call-template name="hearing" />
		</xsl:for-each>
		
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- judiciary Template				-->
	<!-- **************************************** -->	
	
	<doc:template name="judiciary" xmlns="">
		<refpurpose>Creates the list of Judges and / or Justices associated with a sitting.</refpurpose>
		<refdescription>
			<para>Firstly iterates through the Judge elements (if any) to list all the Judges, and then iterates through  
			        the Justice elements (if any) to list all the Justices.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Rather than use the individual elements making up a name, CitizenRequestedName is currently used to hold
				      all the parts of a Judge / Justice's name including Title.
				</para>
				</listitem>
			</itemizedlist>
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>judiciary_NodeSet</term>
					<listitem>
						<para>The Judiciary element within a sitting. </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Judges and Judiciary, centred on the page.</para>
		</refreturn>
	</doc:template>
	
	<xsl:template name="judiciary" >
	<!-- creates the list of judges for a particular sitting 
	     parameter - judiciary_NodeSet - parent node for judges in sitting
	-->
		<xsl:param name="judiciary_NodeSet" />
		<xsl:for-each select="$judiciary_NodeSet/cs:Judge" >
			<xsl:variable name="judge">
				<xsl:value-of select="apd:CitizenNameRequestedName" />  
			</xsl:variable>
			<center><strong><xsl:value-of select="$judge" /></strong></center>
		</xsl:for-each><xsl:variable name="justiceText">
			<xsl:choose>
			<xsl:when test="count($judiciary_NodeSet/cs:Justice) &gt; 1">
				<xsl:text>Justices:</xsl:text>
			</xsl:when>
			<xsl:when test="count($judiciary_NodeSet/cs:Justice) &gt; 0">
				<xsl:text>Justice:</xsl:text>
			</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:for-each select="$judiciary_NodeSet/cs:Justice" >
			<xsl:variable name="justice">
				<xsl:value-of select="apd:CitizenNameRequestedName" /> 
			</xsl:variable>
			<center>
				<strong>
				<xsl:if test="position()=1">
					<xsl:value-of select="$justiceText" />
				</xsl:if>
				<xsl:value-of select="$justice" />
				</strong>
			</center>
		</xsl:for-each>	
		<br />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- hearing Template				-->
	<!-- **************************************** -->
	
	<doc:template name="hearing" xmlns="">
		<refpurpose>Displays the information pertaining to a hearing.</refpurpose>
		<refdescription>
		  <para>Context is the Hearing Node. Prints all the preliminary information associated with a hearing eg hearing description, time marking 
		  		note then calls the template to print the defendant details on each line, and finally prints out the list note.</para>
		  <para>
		  	<emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>If the Hearing description is the same as that for the previous hearing then suppress its output.</para>
		  </listitem>
		  <listitem>
		  	<para>If the Prosecution organisation name contains 'Crown Prosecution Service' the prosecution reference is displayed
			      alongside each defendant, otherwise the name of the Prosecution Organisation is displayed.</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
		<refreturn>
			<para>The block of information for one hearing.</para>
		</refreturn>
	</doc:template>
	
	<xsl:template name="hearing" >
	<!-- creates all the detail associated with a hearing 
	     operates at the Hearing node level
	-->
		<strong>
		<xsl:if test="cs:TimeMarkingNote">
			<xsl:value-of select="cs:TimeMarkingNote"/>
			<br />
		</xsl:if>
		
			<xsl:choose>
			<xsl:when test="not (position()=1)" >
				<xsl:variable name="pos" select="position()" />
				<xsl:if test="not (cs:HearingDetails/@HearingType = ../cs:Hearing[position()=$pos - 1]/cs:HearingDetails/@HearingType)">
					<!-- KN 20005-05-10 Removed
					<xsl:call-template name="util:hearingDescription">
						<xsl:with-param name="code" select="cs:HearingDetails/@HearingType"/>
					</xsl:call-template>
					-->
					<!-- KN 20005-05-10 Added direct selection -->
					<xsl:value-of select="cs:HearingDetails/cs:HearingDescription" />
					<!-- KN 2005-05-10 End of Change -->
				</xsl:if>
			</xsl:when>
			
			<xsl:otherwise>
				<!-- KN 20005-05-10 Removed
				<xsl:call-template name="util:hearingDescription">
						<xsl:with-param name="code" select="cs:HearingDetails/@HearingType"/>
				</xsl:call-template>
				-->
				<!-- KN 20005-05-10 Added direct selection -->
				<xsl:value-of select="cs:HearingDetails/cs:HearingDescription" />
				<!-- KN 2005-05-10 End of Change -->
			</xsl:otherwise>
			</xsl:choose>
		</strong>
		<xsl:variable name="prosecutingref" >
			<xsl:choose>
			<xsl:when test="contains(cs:Prosecution/@ProsecutingAuthority,'Crown Prosecution Service')">
				<xsl:text>CPS Ref: </xsl:text>
				<xsl:value-of select="cs:Prosecution/cs:ProsecutingReference" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName" />
			</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="processdefendants">
			<xsl:with-param name="caseNumText" select="cs:CaseNumber" />
			<xsl:with-param name="committingText" select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
			<xsl:with-param name="prosecuteRefText" select="$prosecutingref" />
		</xsl:call-template>
		<table width="100%" class="detail">
			<tr>
				<td width="10%" />
				<td width="90%" /> <!-- empty row to set column widths for optional items below -->
			</tr>
			<xsl:choose>
			<!-- EDS are not populating the prosecuting organisation name or the respondent elements correctly
			     Everything is currently getting put in the prosecution advocate surname therefore need to do 
				 work rounds -->
			<xsl:when test="cs:Prosecution//cs:OrganisationName or 
			                cs:Prosecution/cs:Advocate/cs:PersonalDetails//apd:CitizenNameSurname">
				<xsl:variable name="prosecute">
					<xsl:choose>
					<xsl:when test="cs:Prosecution//cs:OrganisationName">
						<xsl:value-of select="cs:Prosecution//cs:OrganisationName"/>
					</xsl:when>
					<xsl:when test="cs:Prosecution/cs:Advocate/cs:PersonalDetails//apd:CitizenNameSurname">
						<xsl:value-of select="cs:Prosecution/cs:Advocate/cs:PersonalDetails//apd:CitizenNameSurname" />
					</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<xsl:if test="not(contains($prosecute,'Crown Prosecution Service'))">
					<tr>
						<td />
						<td>
							<xsl:choose>
							<xsl:when test="starts-with(cs:CaseNumber,'A')">
								<xsl:text>(Respondent: </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>(Prosecutor: </xsl:text>
							</xsl:otherwise>
							</xsl:choose>
							<xsl:call-template name="str:to-upper">
								<xsl:with-param name="text" select="$prosecute" />
							</xsl:call-template>
							<xsl:text>)</xsl:text>
						</td>
					</tr>
				</xsl:if>
			</xsl:when>
			<xsl:when test="cs:Respondent">
					<tr>
						<td />
						<td>
							<xsl:text>(Respondent: </xsl:text>
							<xsl:call-template name="str:to-upper">
								<xsl:with-param name="text" select="cs:Respondent" />
							</xsl:call-template>
							<xsl:text>)</xsl:text>
						</td>
					</tr>
			</xsl:when>
			</xsl:choose>
			
			
		</table>
		<xsl:if test="cs:ListNote">
			<strong>
			<xsl:value-of select="cs:ListNote"/>
			</strong>
			<br />
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- processdefendants Template	-->
	<!-- **************************************** -->
	
	<doc:template name="processdefendants" xmlns="">
		<refpurpose>Displays the detail associated with all defendants in a hearing.</refpurpose>
		<refdescription>
			<para>Context is a Hearing node. Iterates through the Defendant name elements (if any) to list the details 
			        of each defendant. Calls template util:solicitorDetails to show the solicitor details for each defendant.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Display the Defendant surname, first forename and the inital letter of the second forename.
				</para>
				</listitem>
			</itemizedlist>
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>caseNumText</term>
					<listitem>
						<para>The Case Number associated with a hearing. </para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>committingText</term>
					<listitem>
						<para>The Short Name of the committing court. </para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>prosecuteRefText</term>
					<listitem>
						<para>The prosecuting reference for the hearing or the prosecuting organisation name </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>All the defendants associated with a hearing.</para>
		</refreturn>
	</doc:template>
		
	<xsl:template name="processdefendants" >
	<!-- creates the details of defendants in the output in tabular form on the first row
	     also show the case details
	     parameters:
	     casenumText      - case number
	     committingText   - commiting court
	     prosecuteRefText - prosecution reference
	-->
		<xsl:param name="caseNumText"  />
		<xsl:param name="committingText" />
		<xsl:param name="prosecuteRefText" />
		<table  class="detail" width="100%">		
		<xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name" >
			<xsl:variable name="defendant">

				<!-- KN 2005-04-11 Update for PR56970 -->
				<!--
				<xsl:value-of select="apd:CitizenNameSurname"/>
				-->
				<xsl:choose>
					<xsl:when test="starts-with($caseNumText,'U')">
						<xsl:value-of select="concat(apd:CitizenNameSurname,apd:CitizenNameRequestedName)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="apd:CitizenNameSurname"/>
					</xsl:otherwise>
				</xsl:choose>
				<!-- KN 2005-04-11 End of Change for PR56970 -->
								
				<xsl:text> </xsl:text>
				<xsl:variable name="firstName" >
					<xsl:call-template name="util:stripCommas">
						<xsl:with-param name="name" select="apd:CitizenNameForename[position()=1]" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:value-of select="$firstName" />
				<xsl:if test="apd:CitizenNameForename[position()=2]" >
					<xsl:text> </xsl:text>
					<xsl:variable name="init" >
						<xsl:call-template name="util:getInitials">
							<xsl:with-param name="phrase" select="apd:CitizenNameForename[position()=2]" />
						</xsl:call-template>
					</xsl:variable>
					<xsl:call-template name="str:to-upper" >
						<xsl:with-param name="text" select="$init" />
					</xsl:call-template>
				</xsl:if>
			</xsl:variable>
			<xsl:choose>
			<xsl:when test="position()=1" >
				<tr>
				<td width="10%"><xsl:value-of select="$caseNumText" /></td>
				<td width="30%"><xsl:value-of select="$defendant" /></td>
				<td width="30%">
					<xsl:call-template name="util:solicitorDetails">
						<xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
					</xsl:call-template>
				</td>
				<td width="15%"><xsl:value-of select="$committingText" /></td>
				<td width="15%"><xsl:value-of select="../../cs:URN" /></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr>
				<td></td>
				<td><xsl:value-of select="$defendant" /></td>
				<td>
					<xsl:call-template name="util:solicitorDetails">
						<xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
					</xsl:call-template>
				</td>
				<td></td>
				<td><xsl:value-of select="../../cs:URN" /></td>
				</tr>
			</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		</table>		
	</xsl:template>
			
</xsl:stylesheet>
