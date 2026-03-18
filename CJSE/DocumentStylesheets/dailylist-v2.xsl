<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="date str doc util xsd n1 apd cs">
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 2</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Daily List Stylesheet - dailylist-v2.xsl</title>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Daily List and Daily Prison List in html format</para>
				<para>RFC1354 - add DOC to all defendants</para>
			</section>
		</partintro>
	</doc:reference>
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'2'"/>
	<xsl:variable name="minorVersion" select="'0'"/>
	<xsl:variable name="stylesheet" select="'dailylist-v2.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2005-05-18'"/>
	<!-- End Version Information -->
	<xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate"/>
	<xsl:variable name="version" select="//cs:ListHeader/cs:Version"/>
	<xsl:output method="html" indent="yes"/>
	<!-- **************************************** -->
	<!-- Root Template					-->
	<!-- **************************************** -->
	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<!-- +++++++++ following template produces list header             +++++++++ -->
				<xsl:apply-templates select="cs:DailyList/cs:CrownCourt"/>
				<!-- +++++++++ following template produces list body               +++++++++ -->
				<xsl:apply-templates select="cs:DailyList/cs:CourtLists"/>
				<!-- +++++++++ following template produces list footer             +++++++++ -->
				<xsl:call-template name="util:listFooter">
					<xsl:with-param name="court" select="/cs:DailyList/cs:CrownCourt"/>
				</xsl:call-template>
			</body>
		</html>
	</xsl:template>
	<xsl:key name="courtByName" match="cs:CourtHouse/cs:CourtHouseName" use="cs:CourtHouse/cs:CourtHouseName"/>
	<!-- **************************************** -->
	<!-- CrownCourt Template			-->
	<!-- **************************************** -->
	<doc:template match="/CrownCourt" xmlns="">
		<refpurpose>Creates the Report Header information. If the DocumentType = 'DLP' then this is the Prison Daily List</refpurpose>
	</doc:template>
	<xsl:template match="cs:CrownCourt">
		<!-- processes the CrownCourt node - constructs the initial header information for the output -->
		<xsl:variable name="reporttype">
			<xsl:choose>
				<xsl:when test="//cs:DocumentID/cs:DocumentType = 'DLP'">
					<xsl:value-of select="'Daily Prison List'"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'Daily List'"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<h1> The <xsl:value-of select="cs:CourtHouseType"/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="cs:CourtHouseName"/>
		</h1>
		<h2>
			<table width="100%">
				<tr>
					<strong>
						<td width="75%">
							<big>
								<xsl:value-of select="$reporttype"/>
								<xsl:text> for </xsl:text>
								<xsl:call-template name="date:format-date-time">
									<xsl:with-param name="year" select="substring($reportdate,1,4)"/>
									<xsl:with-param name="month" select="substring($reportdate,6,2)"/>
									<xsl:with-param name="day" select="substring($reportdate,9,2)"/>
									<xsl:with-param name="format" select="'%A %D %B %Y'"/>
								</xsl:call-template>
							</big>
						</td>
						<td width="25%">
							<big>
								<xsl:value-of select="$version"/>
							</big>
						</td>
					</strong>
				</tr>
				<xsl:if test="//cs:DocumentID/cs:DocumentType = 'DLP'">
					<tr>
						<strong>
							<td>
								<big>
									<xsl:text>For Prison Use Only</xsl:text>
								</big>
							</td>
						</strong>
					</tr>
				</xsl:if>
			</table>
		</h2>
		<xsl:call-template name="util:publishDate"/>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- CourtLists Template				-->
	<!-- **************************************** -->
	<doc:template match="/CourtLists" xmlns="">
		<refpurpose>Controls the creation of the report body.</refpurpose>
		<refdescription>
			<para>Iterates through Hearings within Sitting within CourtList. If the court lists refer to different CourtHouseNames then the 
			        value is shown when it changes.  Normally the court house number and sitting time is shown (unless it is a 'floater' - see below), 
					followed by the judiciary information, the sitting note and then the hearing details.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>A Sitting which has a SittingPriority of 'F' indicates that the Hearings within that Sitting are 'floaters',
			  		and can be allocated to any court. When this is the case no court room number is displayed.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	<xsl:template match="cs:CourtLists">
		<!-- processes the CourtList nodesets - constructs the body of the output -->
		<xsl:for-each select="cs:CourtList">
			<xsl:if test="count(./cs:CourtHouse/cs:CourtHouseName | key('courtByName', ./cs:CourtHouse/cs:CourtHouseName)[1]) = 1">
				<strong>
					<xsl:value-of select="./cs:CourtHouse/cs:CourtHouseName"/>
				</strong>
			</xsl:if>
			<br/>
			<xsl:for-each select="cs:Sittings/cs:Sitting">
				<xsl:choose>
					<xsl:when test="not(cs:SittingPriority = 'F')">
						<p>Court <xsl:value-of select="cs:CourtRoomNumber"/>
							<xsl:if test="cs:SittingAt">
								<xsl:call-template name="date:format-date-time">
									<xsl:with-param name="hour" select="substring(cs:SittingAt,1,2)"/>
									<xsl:with-param name="minute" select="substring(cs:SittingAt,4,2)"/>
									<xsl:with-param name="format" select="' - sitting at %I:%M %p'"/>
								</xsl:call-template>
							</xsl:if>
						</p>
					</xsl:when>
					<xsl:otherwise>
						<strong>
							<xsl:text>The following may be taken in any court.</xsl:text>
						</strong>
					</xsl:otherwise>
				</xsl:choose>
				<!-- +++++++++++  show the judge(s) for the sitting +++++++++++++ -->
				<xsl:call-template name="judiciary">
					<xsl:with-param name="judiciary_NodeSet" select="./cs:Judiciary"/>
				</xsl:call-template>
				<strong>
					<xsl:value-of select="cs:SittingNote"/>
				</strong>
				<p/>
				<!-- +++++++++++  now process the hearings          +++++++++++++ -->
				<xsl:for-each select="cs:Hearings/cs:Hearing">
					<xsl:call-template name="hearing"/>
				</xsl:for-each>
				<hr/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<!-- **************************************** -->
	<!-- judiciary Template				-->
	<!-- **************************************** -->
	<doc:template name="judiciary" xmlns="">
		<refpurpose>Creates the list of Judges and / or Justices associated with a hearing.</refpurpose>
		<refdescription>
			<para>Firstly iterates through the Judge elements (if any) to list all the Judges, and then iterates through  
			        the Justice elements (if any) to list all the Justices.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Rather than use the individual elements making up a name, CitizenRequestedName is currently used to hold
				      all the parts of a Judge / Justice's name including Title.	</para>
					<para>If CitizenRequestedName = 'N/A' do not display.</para>
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
	<xsl:template name="judiciary">
		<!-- creates the list of judges for a particular sitting 
	     parameter - judiciary_NodeSet - parent node for judges in sitting
	-->
		<xsl:param name="judiciary_NodeSet"/>
		<table class="emphasis" width="100%">
			<xsl:for-each select="$judiciary_NodeSet/cs:Judge">
				<tr>
					<xsl:variable name="judge">
						<xsl:value-of select="apd:CitizenNameRequestedName"/>
					</xsl:variable>
					<td align="center">
						<xsl:if test="not ( $judge = 'N/A') ">
							<strong>
								<xsl:value-of select="$judge"/>
							</strong>
						</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<xsl:variable name="justiceText">
			<xsl:choose>
				<xsl:when test="count($judiciary_NodeSet/cs:Justice) &gt; 1">
					<xsl:text>Justices: </xsl:text>
				</xsl:when>
				<xsl:when test="count($judiciary_NodeSet/cs:Justice) &gt; 0">
					<xsl:text>Justice: </xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<table width="100%" class="emphasis">
			<xsl:for-each select="$judiciary_NodeSet/cs:Justice">
				<xsl:variable name="justice">
					<xsl:value-of select="apd:CitizenNameRequestedName"/>
				</xsl:variable>
				<tr>
					<xsl:choose>
						<xsl:when test="position()=1">
							<td width="20%" align="right">
								<strong>
									<large>
										<xsl:value-of select="$justiceText"/>
									</large>
								</strong>
							</td>
							<td width="80%" align="left">
								<strong>
									<large>
										<xsl:value-of select="$justice"/>
									</large>
								</strong>
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td/>
							<td>
								<strong>
									<large>
										<xsl:value-of select="$justice"/>
									</large>
								</strong>
							</td>
						</xsl:otherwise>
					</xsl:choose>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- **************************************** -->
	<!-- hearing Template				-->
	<!-- **************************************** -->
	<doc:template name="hearing" xmlns="">
		<refpurpose>Displays the information pertaining to a hearing.</refpurpose>
		<refdescription>
			<para>Context is the Hearing Node. Prints all the preliminary information associated with a hearing eg hearing description, time marking 
			  		note then calls the routine to print the defendant details on each line, and finally prints out the list note.
			  </para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>If the Hearing description is the same as that for the previous hearing then suppress its output.</para>
				</listitem>
				<listitem>
					<para>If the Hearing description begins with the word 'Miscellaneous ' need to remove the word Miscellaneous before display.
				      Also need to modify how the defendant details are displayed by appending '-v-' and the Prosecuting 
					  Organisation name to the defendant's name.	</para>
				</listitem>
				<listitem>
					<para>If the Prosecution organisation name contains 'Crown Prosecution Service' the prosecution reference is displayed
				      alongside each defendant, otherwise the name of the Prosecution Organisation is displayed.</para>
				</listitem>
				<listitem>
					<para>When displaying the name of the Prosecution Organisation it should be displayed in mixed case i.e Capital First Letter 
				      Rest Lower Case. However some elements of the text need to be left unchanged e.g. T.V. and of as in House of Lords, therefore the 	template 
					  util:transformCaseSpecial is used to do the case conversion on the prosecution organisation name.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
		<refreturn>
			<para>The block of information for one hearing.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="hearing">
		<!-- creates all the detail associated with a hearing 
	     operates at the Hearing node level
	-->
		<xsl:variable name="hearingDescription">
			<xsl:choose>
				<xsl:when test="not (position()=1)">
					<xsl:variable name="pos" select="position()"/>
					<xsl:if test="not (cs:HearingDetails/cs:HearingDescription = ../cs:Hearing[position()=$pos - 1]/cs:HearingDetails/cs:HearingDescription)">
						<xsl:value-of select="cs:HearingDetails/cs:HearingDescription"/>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="cs:HearingDetails/cs:HearingDescription"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- if hearing description contains Miscellaneous For Appeal, we need to modify removing the word
		     Miscellaneous. Also need to modify the display of defendant such that the name has '-v-' and the 
			 prosecuting organisation name appended to it.
		-->
		<!-- set up bit to append to defendant if needed -->
		<xsl:variable name="appendage">
			<xsl:choose>
				<xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
					<xsl:value-of select="concat('-v-',cs:Prosecution//cs:OrganisationName)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="''"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<strong>
			<xsl:if test="cs:TimeMarkingNote">
				<xsl:if test="not (cs:TimeMarkingNote = ' ')">
					<xsl:value-of select="cs:TimeMarkingNote"/>
					<br/>
				</xsl:if>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
					<xsl:value-of select="substring-after($hearingDescription,'Miscellaneous ')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$hearingDescription"/>
				</xsl:otherwise>
			</xsl:choose>
		</strong>
		<xsl:variable name="prosecutingref">
			<xsl:choose>
				<xsl:when test="contains(cs:Prosecution//cs:OrganisationName,'Crown Prosecution Service')">
					<xsl:value-of select="cs:Prosecution/cs:ProsecutingReference"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="util:transformCaseSpecial">
						<!-- keeps T.V. in upper case -->
						<xsl:with-param name="text" select="cs:Prosecution//cs:OrganisationName"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="cs:Defendants">
				<xsl:call-template name="processdefendants">
					<xsl:with-param name="caseNumText" select="cs:CaseNumber"/>
					<xsl:with-param name="committingText" select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
					<xsl:with-param name="prosecuteRefText" select="$prosecutingref"/>
					<xsl:with-param name="appendText" select="$appendage"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table width="100%">
					<tr>
						<td width="10%" valign="top">
							<xsl:value-of select="cs:CaseNumber"/>
						</td>
						<td width="55%" valign="top"/>
						<td width="5%" valign="top">
							<xsl:value-of select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
						</td>
						<td width="30%" valign="top">
							<xsl:value-of select="$prosecutingref"/>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="cs:ListNote">
			<strong>
				<xsl:value-of select="cs:ListNote"/>
			</strong>
		</xsl:if>
		<br/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- processdefendants Template	-->
	<!-- **************************************** -->
	<doc:template name="processdefendants" xmlns="">
		<refpurpose>Displays the detail associated with all defendants in a hearing.</refpurpose>
		<refdescription>
			<para>Context is a Hearing node. Iterates through the Defendant name elements (if any) to list the details of each defendant.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Display the Defendant surname, first forename and the inital letter of the second forename.</para>
				</listitem>
				<listitem>
					<para>The following should only be displayed if there is a prisoner id associated with a defendant: Sex, DOB,
				      Prisoner Id and Prison Id with location. The prisoner id is normally only present for a Prison Daily list.</para>
				</listitem>
				<listitem>
					<para>When the case number begins with the letter U, then the surname to be displayed is made up of the CitizenNameSurname
				      and CitizenNameRequestedName elements concatenated.</para>
				</listitem>
				<listitem>
					<para>Any commas in the first CitizenNameForname element are stripped out.</para>
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
				<varlistentry>
					<term>appendText</term>
					<listitem>
						<para>Used to pass the text to be appended to the defendant's name, if the hearing description is
							 	  'Miscellaneous For Appeal'</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>All the defendants associated with a hearing.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="processdefendants">
		<!-- creates the details of defendants in the output in tabular form on the first row
	     also show the case details
	     parameters:
	     casenumText      - case number
	     committingText   - commiting court
	     prosecuteRefText - prosecution reference
		 appendText       - text to append to defendant name if this is Miscellaneous For Appeal hearing
	-->
		<xsl:param name="caseNumText"/>
		<xsl:param name="committingText"/>
		<xsl:param name="prosecuteRefText"/>
		<xsl:param name="appendText"/>
		<table class="detail" width="100%">
			<xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name">
				<xsl:variable name="defendant">
					<xsl:choose>
						<xsl:when test="starts-with($caseNumText,'U')">
							<xsl:value-of select="concat(apd:CitizenNameSurname,apd:CitizenNameRequestedName)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="apd:CitizenNameSurname"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text> </xsl:text>
					<xsl:variable name="firstName">
						<xsl:call-template name="util:stripCommas">
							<xsl:with-param name="name" select="apd:CitizenNameForename[position()=1]"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:call-template name="str:capitalise">
						<xsl:with-param name="text" select="$firstName"/>
					</xsl:call-template>
					<xsl:if test="apd:CitizenNameForename[position()=2]">
						<xsl:text> </xsl:text>
						<xsl:variable name="init">
							<xsl:call-template name="util:getInitials">
								<xsl:with-param name="phrase" select="apd:CitizenNameForename[position()=2]"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:call-template name="str:to-upper">
							<xsl:with-param name="text" select="$init"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:value-of select="$appendText"/>
					<!-- KN 20050308 - For RFC1354 - Show Dateof Birth for All Defendants -->
					<xsl:if test="//cs:DocumentID/cs:DocumentType = 'DLP'">
						<!-- <xsl:if test="../../cs:PrisonerID">  -->
						<br/>
						<xsl:if test="../../cs:PrisonerID">
							<xsl:if test="../cs:Sex">
								<xsl:text> </xsl:text>
								<xsl:call-template name="str:capitalise">
									<xsl:with-param name="text" select="../cs:Sex"/>
								</xsl:call-template>
								<xsl:text> </xsl:text>
							</xsl:if>
						</xsl:if>
						<xsl:text> </xsl:text>
						<xsl:if test="../cs:DateOfBirth">
							<xsl:text>DOB:</xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="../cs:DateOfBirth/apd:BirthDate"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
						</xsl:if>
						<xsl:if test="../../cs:PrisonerID">
							<xsl:text>Prisoner Id: </xsl:text>
							<xsl:value-of select="../../cs:PrisonerID"/>
							<xsl:text> </xsl:text>
						</xsl:if>
						<xsl:if test="../../cs:PrisonLocation">
							<xsl:text>Prison: </xsl:text>
							<xsl:value-of select="../../cs:PrisonLocation/@PrisonID"/>
							<xsl:text> / </xsl:text>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="../../cs:PrisonLocation/cs:Location"/>
							</xsl:call-template>
						</xsl:if>
					</xsl:if>
					<!-- KN 20050308 - End of RFC1354 Change -->
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="position()=1">
						<tr>
							<td width="10%" valign="top">
								<xsl:value-of select="$caseNumText"/>
							</td>
							<td width="40%" valign="top">
								<xsl:copy-of select="$defendant"/>
							</td>
							<!-- Bichard Change - Add URN number - Tom Muir-Webb 130808 -->
							<td width="15%" valign="top">
								<xsl:value-of select="../../cs:URN"/>
							</td>
							<td width="10%" valign="top">
								<xsl:value-of select="$committingText"/>
							</td>
							<td width="25%" valign="top">
								<xsl:value-of select="$prosecuteRefText"/>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td/>
							<td>
								<xsl:copy-of select="$defendant"/>
							</td>
							<td>
								<xsl:value-of select="../../cs:URN"/>
							</td>
							<td/>
							<td/>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</table>
	</xsl:template>
</xsl:stylesheet>
