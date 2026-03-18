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
		<title>Warned List Stylesheet - warnedlist-v2.xsl</title>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Warned List in html format</para>
				<para>Added UCASE title Support PR56970 </para>
				<para>Added No later fix for 57106</para>
				<para>Removed Hearing Description Lookup in gcsUtils</para>
				<para>57210 - removed double initials</para>
				<para>57304- removed double initials</para>
			</section>
		</partintro>
	</doc:reference>

<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" />
<xsl:include href="gcsUtility.xsl" />


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'0d'" />
<xsl:variable name="stylesheet" select="'WarnedList-v2.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-07-02'" />
<!-- End Version Information -->

<xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate" />
<xsl:variable name="endDate" select="//cs:ListHeader/cs:EndDate" />
<xsl:variable name="version"  select="//cs:ListHeader/cs:Version" />

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
				<xsl:apply-templates select="cs:WarnedList/cs:CrownCourt" /> 

				<!-- +++++++++ following template produces list body for fixed hearings        +++++++++ -->			
				<xsl:apply-templates select="cs:WarnedList/cs:CourtLists" mode="fixed"/> 

				<!-- +++++++++ following template produces list of hearings not yet fixed   +++++++++ -->			
				<xsl:apply-templates select="cs:WarnedList/cs:CourtLists" mode="notFixed" /> 

				<!-- +++++++++ following template produces list footer             +++++++++ -->
				<xsl:call-template name="util:listFooter" >
					<xsl:with-param name="court" select="/cs:WarnedList/cs:CrownCourt" />
				</xsl:call-template>
				
			
			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CrownCourtTemplate			-->
	<!-- **************************************** -->
	<doc:template match="CrownCourt" xmlns="">
		<refpurpose>Creates the Report Header information, including listing instructions.</refpurpose>
		<refdescription>
			<para>Shows the court information and also any listing instructions present, followed by some hard coded listing information.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>If a listing instruction begins with the text 'No later than', then it is necessary to prepend some fixed text
				i.e 'Any representation about the listing of a case should be made to the Listing Officer' on the previous line.</para>
				</listitem>
				<listitem>
					<para>The template util:bulletRow is used to display each of the listing instructions.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:CrownCourt">
	<!-- processes the CrownCourt node - constructs the initial header information for the output -->
		<xsl:variable name="reporttype" select="'Criminal Warned List'" />
		<h1> The <xsl:value-of select="cs:CourtHouseType"/>
		<xsl:text> at </xsl:text>
		<xsl:value-of select="cs:CourtHouseName"/>
		</h1>
		<h2>
			<center>
				<xsl:value-of select="$reporttype" />
				<br />
				<small>
					<xsl:value-of select="$version" />
				</small>
			</center>

		</h2>
		<h4>
		<center>
			<xsl:text>The undermentioned cases are warned for hearing during the period  </xsl:text>
			<br />
			<xsl:call-template name="util:ukdate_fullMonth">
				<xsl:with-param name="inDate" select="$reportdate" />
			</xsl:call-template>
			<xsl:text> to </xsl:text>
			<xsl:call-template name="util:ukdate_fullMonth">
				<xsl:with-param name="inDate" select="$endDate" />
			</xsl:call-template>
		</center>
		</h4>
		<xsl:call-template name="util:publishDate" />
		<hr />
		<table width="100%">
			<xsl:for-each select="/cs:WarnedList/cs:ListingInstructions/cs:ListingInstruction" >
				<xsl:variable name="text">
						<xsl:choose>
							<xsl:when test="starts-with(. , 'No later than')" >
								<xsl:text>Any representation about the listing of a case should be made to the Listing Officer</xsl:text>
								<br />
								<strong>
									<xsl:value-of select="." />
								</strong>
							</xsl:when>
							<!-- KN 200505-04 - PR 57106 -->
							<xsl:when test="starts-with(. , 'no later than')" >
								<xsl:text>Any representation about the listing of a case should be made to the Listing Officer</xsl:text>
								<br />
								<strong>
									<xsl:value-of select="." />
								</strong>
							</xsl:when>
							<!-- KN 200505-04 - END-->
							<xsl:when test="contains(. , '---')" >
								<strong>
								<xsl:value-of select="substring-before(. , '---')" />
								</strong>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="." />
							</xsl:otherwise>
						</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text"/>
				</xsl:call-template>
			</xsl:for-each>
			<xsl:variable name="text2">
					<xsl:text>The prosecuting authority is the Crown Prosecution Service unless otherwise stated.</xsl:text>
			</xsl:variable>
			<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text2"/>
				</xsl:call-template>
			<tr>
				<td  >
					<table width="85%">
						<tr>
						<td  align="right" style="font-size: large; font-weight: bold;" >
						<xsl:text>* </xsl:text>
						</td>
						</tr>
					</table>
				</td>
				<td>
					<xsl:text>Denotes a defendant in custody</xsl:text>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	

	<xsl:key name="fixedHearingByType" 
	         match="/cs:WarnedList/cs:CourtLists/cs:CourtList/cs:WithFixedDate" 
			 use="@HearingType"/>		 		 
			 
	<xsl:key name="notFixedHearingByType" 
	         match="/cs:WarnedList/cs:CourtLists/cs:CourtList/cs:WithoutFixedDate" 
			 use="@HearingType"/>
			 
	<xsl:key name="courtByName" 
	         match="cs:CourtHouse/cs:CourtHouseName" 
			 use="cs:CourtHouse/cs:CourtHouseName"/>	
			 
	<!-- above indexes are used to help with the control breaks on date and then court within date -->
	
	<!-- **************************************** -->
	<!-- CourtLists Template				-->
	<!-- **************************************** -->
	<doc:template match="/CourtLists" mode="fixed" xmlns="">
		<refpurpose>Controls the creation of the report body that contains cases with fixed hearing dates.</refpurpose>
		<refdescription>
			<para>Iterates through Cases within WithFixedDate to create a list of all cases for each hearing type with a fixed hearing date.
			        The hearing type is held as an attribute on WithFixedDate</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>The hearing description is found from a look up table, by calling the template util:hearingDescription, with the hearing type
				      as the parameter passed across.	</para>
				</listitem>
				<listitem>
					<para>If the hearing type for a case is the same as the previous case processed then do not display the hearing description.</para>
				</listitem>
				<listitem>
					<para>Within each HearingType the cases are processed in ascending case number order.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template match="cs:CourtLists" mode="fixed">
	<!-- processes the CourtList nodesets - constructs the body of the output -->
		<xsl:for-each select="./cs:CourtList/cs:WithFixedDate 
									[count(. | key('fixedHearingByType', ./@HearingType)[1]) = 1]" > 
								
			<xsl:variable name="hearingType" select="./@HearingType" />
			<xsl:if test="position()=1" >
				<hr />			
				<strong>
					<xsl:text>Fixtures</xsl:text>
				</strong>
				<br />
			</xsl:if>
			<strong>
				<!-- KN 20005-05-10 Removed
				<xsl:call-template name="util:hearingDescription">
					<xsl:with-param name="code" select="$hearingType"/>
				</xsl:call-template>
				-->
				<!-- KN 20005-05-10 Added direct selection -->
				<xsl:value-of select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/cs:HearingDescription" />
				<!-- KN 2005-05-10 End of Change -->
			</strong>
	
			<xsl:for-each select="//cs:CourtList/cs:WithFixedDate/cs:Fixture/cs:Cases/cs:Case
									[../../../@HearingType = $hearingType]" >
				<xsl:sort select="./cs:CaseNumber"/>
					<!-- +++++++++++  now process the cases          +++++++++++++ -->
					<xsl:call-template name="case" />
			</xsl:for-each>
		</xsl:for-each> 
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CourtLists Template				-->
	<!-- **************************************** -->
	
	<doc:template match="/CourtLists" mode="notFixed" xmlns="">
		<refpurpose>Controls the creation of the report body that contains cases without a fixed hearing dates.</refpurpose>
		<refdescription>
			<para>Iterates through Cases within WithoutFixedDate to create a list of all cases for each hearing type without a fixed hearing date.
			        The hearing type is held as an attribute on WithoutFixedDate</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>The hearing description is found from a look up table, by calling the template util:hearingDescription, with the hearing type
				      as the parameter passed across.	</para>
				</listitem>
				<listitem>
					<para>If the hearing type for a case is the same as the previous case processed then do not display the hearing description.</para>
				</listitem>
				<listitem>
					<para>Within each HearingType the cases are processed in ascending case number order.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
		
	<xsl:template match="cs:CourtLists" mode="notFixed">
	<!-- processes the CourtList nodesets - constructs the body of the output -->
		<hr />
		<xsl:for-each select="./cs:CourtList/cs:WithoutFixedDate 
									[count(. | key('notFixedHearingByType', ./@HearingType)[1]) = 1]" > 
								
			<xsl:variable name="hearingType" select="./@HearingType" />
		
			<strong>
				<!-- KN 20005-05-10 Removed
				<xsl:call-template name="util:hearingDescription">
					<xsl:with-param name="code" select="$hearingType"/>
				</xsl:call-template>
				-->
				<!-- KN 20005-05-10 Added direct selection -->
				<!-- KN 2005-07-02 PR 57304 -->
				<xsl:value-of select="./cs:Fixture/cs:Cases/cs:Case/cs:Hearing/cs:HearingDescription" /> 
				<!-- KN 2005-07-02 End of Change -->
				<!-- KN 2005-05-10 End of Change -->
			</strong>
	
			<xsl:for-each select="//cs:CourtList/cs:WithoutFixedDate/cs:Fixture/cs:Cases/cs:Case
									[../../../@HearingType = $hearingType]" >
				<xsl:sort select="./cs:CaseNumber"/>
					<!-- +++++++++++  now process the cases          +++++++++++++ -->
					<xsl:call-template name="case" />
			</xsl:for-each>
		</xsl:for-each> 
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Case Template					-->
	<!-- **************************************** -->
		
	<doc:template name="Case" xmlns="">
		<refpurpose>Displays the information pertaining to a case.</refpurpose>
		<refdescription>
			<para>Context is a Case node. This template is called to display the information for a case, which may or may not have a 
			        hearing date fixed for it. Calls a template to output the defendant details, and then displays any other
					relevant case details.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>If the case is one with a fixed hearing date (ie child of WithFixedDate) then display the date.Note that if the date
				is '1900-01-01' then it shopuld not be displayed</para>
				</listitem>
				<listitem>
					<para>It is possible that the Notes element contains data to go over several lines of output, each line is delimited with the '|'
				      character. The routine util:lineSplitter is called to break the Notes element into discreet output lines.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="case" >
	<!-- creates all the detail associated with a case 
	     operates at the Case node level
	-->
		<br />
		
		<xsl:variable name="prosecutingref" >
			<xsl:choose>
			<xsl:when test="contains(cs:Prosecution//cs:OrganisationName,'Crown Prosecution Service')">
				<xsl:value-of select="cs:Prosecution/cs:ProsecutingReference" />
			</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="processdefendants">
			<xsl:with-param name="caseNumText" select="cs:CaseNumber" />
			<xsl:with-param name="committingText" select="cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
			<xsl:with-param name="prosecuteRefText" select="$prosecutingref" />
		</xsl:call-template>
		<table width="100%" class="detail">
			<tr>
				<td width="10%" />
				<td width="90%" /> <!-- empty row to set column widths for optional items below -->
			</tr>
			
			<xsl:choose>
			<xsl:when test="cs:Prosecution">
				<xsl:if test="not(contains(cs:Prosecution//cs:OrganisationName,'Crown Prosecution Service'))">
					<tr>
						<td />
						<td>
							<xsl:text>(</xsl:text>
							<xsl:value-of select="cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName" />
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
			<xsl:if test="../../cs:FixedDate[not(. = '1900-01-01')]">
				<tr>
					<td />
					<td>
						<strong>
						<xsl:variable name="fixDate" select="../../cs:FixedDate"/>
						<xsl:text>Fixed for </xsl:text>
						<xsl:call-template name="util:ukdate_fullMonth">
							<xsl:with-param name="inDate" select="$fixDate" />
						</xsl:call-template>
						</strong>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="../../cs:Notes">
				<tr>
					<td />
					<td>
						<strong>
						<xsl:call-template name="util:lineSplitter">
							<xsl:with-param name="text" select="../../cs:Notes" />
						</xsl:call-template>
						</strong>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="../../cs:LinkedCases" >
				<tr>
					<td>
						<xsl:text>(linked with: </xsl:text>
					</td>
					<td>
						<xsl:for-each select="../../cs:LinkedCases/cs:CaseNumber">
							<xsl:value-of select="."/>
							<xsl:if test="not(position()=last())">
								<xsl:text>, </xsl:text>
							</xsl:if>
						</xsl:for-each>
						<xsl:text>)</xsl:text>
					</td>
				</tr>
		</xsl:if>
			
		</table>
	
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- processdefendants Template	-->
	<!-- **************************************** -->
	
	<doc:template name="processdefendants" xmlns="">
		<refpurpose>Displays the detail associated with all defendants for a case.</refpurpose>
		<refdescription>
			<para>Context is a Case node. Iterates through the Defendant name elements (if any) to list the details of each defendant.
			        Calls the template util:solicitorDetails to show the solicitor information for each defendant - if there is
					no solicitor information then the default 'NO REPRESENTATION RECORDED' is to be shown instead.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>If the custody status contains 'On remand' or 'In custody', show an asterisk before the defendant details.</para>
				</listitem>
				<listitem>
					<para>Display the Defendant surname, first forename and the inital letter of the second forename.</para>
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
						<para>The prosecution reference for the case. </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>All the defendants associated with a case.</para>
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
		<table  width="100%" class="detail">		
		<xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name" >
			<xsl:variable name="asterisk">
				<xsl:if test="../../cs:CustodyStatus = 'In custody' or
				              ../../cs:CustodyStatus = 'On remand'">
					<strong>
						<xsl:text>*</xsl:text>
					</strong>
				</xsl:if>
			</xsl:variable>
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
					<!-- PR 57210 - 20050620 KN - Only one initial from the second name 				-->
					<!-- <xsl:variable name="init" >													-->
					<!-- 	<xsl:call-template name="util:getInitials">							-->
					<!-- 		<xsl:with-param name="phrase" select="apd:CitizenNameForename[position()=2]" />	-->
					<!-- 	</xsl:call-template>														-->
					<!-- </xsl:variable>																-->
					
					<xsl:value-of select="substring(substring-before(apd:CitizenNameForename[position()=2],' '),1,1)"/>
					<!-- PR 57210 - End of Change KN -->
					<!-- <xsl:call-template name="str:to-upper" >					-->
					<!-- 	<xsl:with-param name="text" select="$init" />		-->
					<!-- </xsl:call-template>											-->
				</xsl:if>
				<xsl:if test="../cs:Sex or ../cs:DateOfBirth or ../../cs:PrisonLocation">
					<br />
					<xsl:if test="../cs:Sex">
						<xsl:call-template name="util:getGender">
							<xsl:with-param name="sex" select="../cs:Sex" />
						</xsl:call-template>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:if test="../cs:DateOfBirth">
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="../cs:DateOfBirth/apd:BirthDate" />
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="../../cs:PrisonerID">
						<xsl:text> Prisoner Id: </xsl:text>
						<xsl:value-of select="../../cs:PrisonerID" />
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:if test="../../cs:PrisonLocation">
						<xsl:text>Prison: </xsl:text>
						<xsl:value-of select="../../cs:PrisonLocation/@PrisonID" />
						<xsl:text> / </xsl:text>
						<xsl:call-template name="str:capitalise" >
							<xsl:with-param name="text" select="../../cs:PrisonLocation/cs:Location" />
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</xsl:variable>
			<xsl:choose>
			<xsl:when test="position()=1" >
				<tr>
				<td width="8%" valign="top"><xsl:value-of select="$caseNumText" /></td>
				<td width="2%" valign="top"><xsl:value-of select="$asterisk" /></td>
				<td width="41%" valign="top"><xsl:copy-of select="$defendant" /></td>
				<td width="18%" valign="top">
					<xsl:call-template name="util:solicitorDetails">
						<xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
						<xsl:with-param name="nobody" select="'No Representation Recorded'" />
					</xsl:call-template>
				</td>
				<td width="10%" valign="top"><xsl:value-of select="../../cs:URN"/></td>
				<td width="13%" valign="top"><xsl:value-of select="$committingText" /></td>
				<td width="8%" valign="top"><xsl:value-of select="$prosecuteRefText" /></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr>
				<td></td>
				<td><xsl:value-of select="$asterisk" /></td>				
				<td><xsl:copy-of select="$defendant" /></td>
				<td>
					<xsl:call-template name="util:solicitorDetails">
						<xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
						<xsl:with-param name="nobody" select="'No Representation Recorded'" />
					</xsl:call-template>
				</td>
				<td><xsl:value-of select="../../cs:URN"/></td>
				<td></td>
				<td></td>
				</tr>
			</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		</table>		
	</xsl:template>
	
</xsl:stylesheet>
