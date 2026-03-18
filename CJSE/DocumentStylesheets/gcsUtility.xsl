<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2008 sp1 (http://www.altova.com) by LCMG (LogicaCMG) -->
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<!-- Amended - 29th May 2008 - S H Phillips
     Rel 8.2 (Bichard) - Problem Reports 59709, 59710
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:csdata="http://www.courtservice.gov.uk/transforms/courtservice/csdata" extension-element-prefixes="date str doc xsd n1 apd cs util csdata">
	<xsl:variable name="last-util-modified-date" select="'2016-10-12'"/>
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 2-7</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Utility Stylesheet</title>
		<para>File name : gcsUtility.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This styleshhet is included in each of the various document producing
		stylesheets. It contains common routines for formatting dates, names etc and provides code lookups 
		and standard report components that are common eg signatory section of orders.</para>
				<para>
	 	One method for doing code lookups was to use the document() function,
		to refer to the lookup table within this stylesheet itself. Unfortunately
		the current version of BITS does not support the document function without modifying 
		some of the security permissions currently in place, therefore alternative
		routines have been provided which work on the basis of massive IF THEN ELSE statement.
		It is these alternative routines which are currently active. The original document() based routines
		are still present but commented out.</para>
				<para> Updated 3/12/04 for TRN hearing type -- RFC1270 / Rel 3.19</para>
				<para> Updated 15/4/05 for PCMH hearing type -- RFC1380</para>
				<para> Updated 10/5/05 To remove dependance on Hearing Types - util:hearingDescription has been removed. Firm LIst and Warned LIst both impacted by the change</para>
				<para> Updated 23/5/05 To Add Signed By</para>
				<para> Updated 29/6/05 SingleAddress Line function - for PR57273</para>
				<para>PR 57295 - Donot displau additional notes if not present</para>
				<para>PR 57442 - Added in removed functions to support old documents</para>
				<para>				- Added: util:hearingDescription and util:altOrderSignatory</para>
				<para>Altered util:address_oneline_court to remove -'s from all lines</para>
				<para>util:address_oneline - ermoved , before Postcode</para>
				<para>PR 57818 - JUdges initials</para>
				<para>RFC1376 - PSR Addition - Copyright</para>
				<para>CCN400 - Updated to include deportation reason text.</para>
				<para>CCN1263 - Updated to include the vulnerable victim indicatior text.</para>
				<para>L-R-4224-01 -Updated to include new OrderAddressee parameter in OrderHeader template. LASBO change</para>
				<para>Updated to change copyrigth text to display current year</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<!-- name="majorVersion" select="'2'" /> -->
	<!-- name="minorVersion" select="'5'" /> -->
	<!-- name="stylesheet" select="'gcsUtility.xsl'" /> -->
	<!-- name="last-modified-date" select="'2006-07-09'" /> -->
	<!-- End Version Information -->
	<!-- lookup data required where information not readily available in incoming xml message -->
	<!--	<csdata:InstigationTypes xml:space="preserve" xmlns="">
         <InstigationType InstigationValue="Committal" >Committal for Trial from </InstigationType>
         <InstigationType InstigationValue="Sending" >Sent for Trial </InstigationType>
         <InstigationType InstigationValue="Execution" >Execution</InstigationType>
         <InstigationType InstigationValue="Transfer" >Transferred from the Crown Court </InstigationType>
         <InstigationType InstigationValue="Transfer certificated" >Certificate of Transfer dated </InstigationType>
         <InstigationType InstigationValue="Voluntary bill" >Voluntary Bill dated </InstigationType>
         <InstigationType InstigationValue="Rehearing ordered" >Re-trial ordered by the Court of Appeal </InstigationType>
         
	</csdata:InstigationTypes>	      

-->
	<!-- version of routine which uses document function. When BITS is modified use this version 
	     instead of alternative below 
	<xsl:template name="util:instigationText" >
	<xsl:param name="code"/>
		<xsl:value-of select="document('')/xsl:stylesheet/csdata:InstigationTypes/InstigationType[@InstigationValue = $code]" />
	</xsl:template>	
	-->
	<!-- **************************************** -->
	<!-- instigationText Template			-->
	<!-- **************************************** -->
	<doc:template name="util:instigationText" xmlns="">
		<refpurpose>Looks up the instigation text for a particular code.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>code</term>
					<listitem>
						<para>Instigation code</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Instigation text.</para>
		</refreturn>
	</doc:template>
	<!-- when BITS is fixed to use document function use the above version instead of this one -->
	<xsl:template name="util:instigationText">
		<xsl:param name="code"/>
		<xsl:choose>
			<xsl:when test=" $code = 'Committal' ">Committal for Trial from </xsl:when>
			<xsl:when test=" $code = 'Sending' ">Sent for Trial from </xsl:when>
			<xsl:when test=" $code = 'Execution' ">Execution</xsl:when>
			<xsl:when test=" $code = 'Transfer' ">Transferred from the Crown Court </xsl:when>
			<xsl:when test=" $code = 'Transfer certificated' ">Certificate of Transfer dated </xsl:when>
			<xsl:when test=" $code = 'Voluntary bill' ">Voluntary Bill of Indictment dated </xsl:when>
			<xsl:when test=" $code = 'Rehearing ordered' ">Re-trial ordered by the Court of Appeal </xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- transformCaseSpecial Template	-->
	<!-- **************************************** -->
	<doc:template name="util:transformCaseSpecial" xmlns="">
		<refpurpose>Converts the source string to mixed case except for various reserved words
		            such as T.V., of etc</refpurpose>
		<refdescription>First capitalises the input string and then performs a series
		of substitutions to reset reserved words back to what they should be.
		To add more reserved words need to add more substitution 'sections' at the end of the chain,
		each one of which uses as its input the out from the previous one.
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>text</term>
					<listitem>
						<para>Text to be converted</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Source text converted to mixed case except for 'reserved words'.</para>
		</refreturn>
	</doc:template>
	<!-- Once BITS is modified to allow use of document() function - switch to above alternative instead of this one -->
	<xsl:template name="util:transformCaseSpecial">
		<!-- converts source string to mixed case except for specific things which have to be upper / lower case -->
		<xsl:param name="text"/>
		<!-- first do the required transformation to mixed case -->
		<!-- then do substitution for T.v. to T.V. -->
		<xsl:variable name="transformed">
			<xsl:call-template name="str:capitalise">
				<xsl:with-param name="text" select="$text"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="pass1">
			<xsl:call-template name="str:subst">
				<xsl:with-param name="text" select="$transformed"/>
				<xsl:with-param name="replace" select="'T.v.'"/>
				<xsl:with-param name="with" select="'T.V.'"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="pass2">
			<xsl:call-template name="str:subst">
				<xsl:with-param name="text" select="$pass1"/>
				<xsl:with-param name="replace" select="' And '"/>
				<xsl:with-param name="with" select="' and '"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="pass3">
			<xsl:call-template name="str:subst">
				<xsl:with-param name="text" select="$pass2"/>
				<xsl:with-param name="replace" select="'Cps'"/>
				<xsl:with-param name="with" select="'CPS'"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="pass4">
			<xsl:call-template name="str:subst">
				<xsl:with-param name="text" select="$pass3"/>
				<xsl:with-param name="replace" select="'Dpp'"/>
				<xsl:with-param name="with" select="'DPP'"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="result">
			<xsl:call-template name="str:subst">
				<xsl:with-param name="text" select="$pass4"/>
				<xsl:with-param name="replace" select="' Of '"/>
				<xsl:with-param name="with" select="' of '"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:value-of select="$result"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- ukdate Template					-->
	<!-- **************************************** -->
	<doc:template name="util:ukdate" xmlns="">
		<refpurpose>Formats the input date to dd-mm-yyyy ie 3-11-2004 format</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>inDate</term>
					<listitem>
						<para>Date to be formatted (standard schema date time format)</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted date</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:ukdate">
		<!-- generates date in dd-mm-yyyy format -->
		<xsl:param name="inDate"/>
		<xsl:if test="$inDate">
			<xsl:call-template name="date:format-date-time">
				<xsl:with-param name="year" select="substring($inDate,1,4)"/>
				<xsl:with-param name="month" select="substring($inDate,6,2)"/>
				<xsl:with-param name="day" select="substring($inDate,9,2)"/>
				<xsl:with-param name="format" select="'%d-%m-%Y'"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- ukdate_mon Template			-->
	<!-- **************************************** -->
	<doc:template name="util:ukdate_mon" xmlns="">
		<refpurpose>Formats the input date to dd-mon-yyyy ie 3-NOV-2004 format</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>inDate</term>
					<listitem>
						<para>Date to be formatted (standard schema date time format)</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted date</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:ukdate_mon">
		<!-- generates date in dd-mon-yyyy format -->
		<xsl:param name="inDate"/>
		<xsl:if test="$inDate">
			<xsl:variable name="date_lower_case">
				<xsl:call-template name="date:format-date-time">
					<xsl:with-param name="year" select="substring($inDate,1,4)"/>
					<xsl:with-param name="month" select="substring($inDate,6,2)"/>
					<xsl:with-param name="day" select="substring($inDate,9,2)"/>
					<xsl:with-param name="format" select="'%d-%b-%Y'"/>
				</xsl:call-template>
			</xsl:variable>
			<!-- convert date into upper case (Bichard PR:59730 19052008 LV) -->
			<xsl:call-template name="str:to-upper">
				<xsl:with-param name="text" select="$date_lower_case"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- ukdate_fullMonth Template		-->
	<!-- **************************************** -->
	<doc:template name="util:ukdate_fullMonth" xmlns="">
		<refpurpose>Formats the input date to dd month yyyy ie 3 November 2004 format</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>inDate</term>
					<listitem>
						<para>Date to be formatted (standard schema date time format)</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted date</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:ukdate_fullMonth">
		<!-- generates date in dd month yyyy format -->
		<xsl:param name="inDate"/>
		<xsl:if test="$inDate">
			<xsl:call-template name="date:format-date-time">
				<xsl:with-param name="year" select="substring($inDate,1,4)"/>
				<xsl:with-param name="month" select="substring($inDate,6,2)"/>
				<xsl:with-param name="day" select="substring($inDate,9,2)"/>
				<xsl:with-param name="format" select="'%D %B %Y'"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- time Template					-->
	<!-- **************************************** -->
	<doc:template name="util:time" xmlns="">
		<refpurpose>Formats the input time to hh:mm ie 6:20 format</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>inDate</term>
					<listitem>
						<para>Time to be formatted (standard schema date time format)</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted date</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:time">
		<!-- generates time in hh:mm format -->
		<xsl:param name="inTime"/>
		<xsl:if test="$inTime">
			<xsl:call-template name="date:format-date-time">
				<xsl:with-param name="hour" select="substring($inTime,12,2)"/>
				<xsl:with-param name="minute" select="substring($inTime,15,2)"/>
				<xsl:with-param name="format" select="'%H:%M'"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- lineSplitterTemplate				-->
	<!-- **************************************** -->
	<doc:template name="util:lineSplitter" xmlns="">
		<refpurpose>Replaces any '|' characters in the input text with html <br/> strings</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>text</term>
					<listitem>
						<para>Text to be formatted </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted text</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:lineSplitter">
		<xsl:param name="text"/>
		<xsl:choose>
			<xsl:when test="contains($text,'|')">
				<xsl:value-of select="substring-before($text,'|')"/>
				<br/>
				<xsl:call-template name="util:lineSplitter">
					<xsl:with-param name="text" select="substring-after($text,'|')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- surnameFirstUC Template		-->
	<!-- **************************************** -->
	<doc:template name="util:surnameFirstUC" xmlns="">
		<refpurpose>Formats a persons name as follows SURNAME forename1 forename2</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>personalDetails</term>
					<listitem>
						<para>node containing personalDetails structure </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted name</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: util:stripCommas is used to remove commas that may be present with forenames.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:surnameFirstUC">
		<!-- expects personal details structure to be passed in -->
		<!-- returns name in format SURNAME forename -->
		<xsl:param name="personalDetails"/>
		<xsl:variable name="person">
			<xsl:call-template name="str:to-upper">
				<xsl:with-param name="text" select="$personalDetails//apd:CitizenNameSurname"/>
			</xsl:call-template>
			<xsl:for-each select="$personalDetails//apd:CitizenNameForename">
				<xsl:text> </xsl:text>
				<xsl:call-template name="util:stripCommas">
					<xsl:with-param name="name" select="."/>
				</xsl:call-template>
			</xsl:for-each>
			<xsl:text> </xsl:text>
		</xsl:variable>
		<xsl:value-of select="$person"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- judiciaryName Template			-->
	<!-- **************************************** -->
	<doc:template name="util:judiciaryName" xmlns="">
		<refpurpose>Formats the name of a member of judiciary as follows:
			<para> title forename surname suffix i.e HHJ Ben Franklin OBE</para>
		</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>judge</term>
					<listitem>
						<para>node containing CitizenNameStructure  </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted name</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: util:personsFullName is used to format the forename and surname.
						</para>
			</listitem>
			<listitem>
				<para>Note: If the title consists of more than one word then first letter of
			            each word is used ie His Honour Judge becomes HHJ</para>
			</listitem>
			<listitem>
				<para>Note: util:getInitials formats multi-word titles to initials.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:judiciaryName">
		<!-- expects an apd:CitizenNameStructure to be passed in -->
		<!-- returns the name formatted as follows -->
		<!-- title forename surname suffix -->
		<!-- NB if the title is more than word, then use first letter of each word -->
		<!-- so His Honour Judge becomes HHJ -->
		<xsl:param name="judge"/>
		<xsl:variable name="newName">
			<!-- do Title -->
			<xsl:variable name="titleInits">
				<xsl:call-template name="util:getInitials">
					<xsl:with-param name="phrase" select="$judge/apd:CitizenNameTitle"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="substring-after($judge/apd:CitizenNameTitle,' ')">
					<!-- indicates that title made up of multiple words so use the initials instead -->
					<xsl:copy-of select="$titleInits"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$judge/apd:CitizenNameTitle"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> </xsl:text>
			<!-- now do forename and surname -->
			<xsl:call-template name="util:personsFullName">
				<xsl:with-param name="name" select="$judge"/>
				<xsl:with-param name="nonDefendant" select="'true'"/>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$judge/apd:CitizenNameSuffix"/>
		</xsl:variable>
		<xsl:copy-of select="$newName"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- getInitials Template				-->
	<!-- **************************************** -->
	<doc:template name="util:getInitials" xmlns="">
		<refpurpose>Returns the initial letter of each word in the input string</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>phrase</term>
					<listitem>
						<para>source string</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Initial letter of each word</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Calls itself recursively to process each word in the phrase</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:getInitials">
		<!-- returns the initail letter of each word passed in the input parameter -->
		<!-- parameter 1 = phrase    -->
		<!--               contains the string of words from which the initial letters will be extracted -->
		<xsl:param name="phrase"/>
		<xsl:variable name="result">
			<!-- <xsl:element name="token"> -->
			<xsl:choose>
				<xsl:when test="substring-before($phrase,' ')">
					<xsl:value-of select="substring(substring-before($phrase,' '),1,1)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="substring($phrase,1,1)"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- </xsl:element> -->
			<xsl:if test="substring-after($phrase,' ')">
				<xsl:call-template name="util:getInitials">
					<xsl:with-param name="phrase" select="substring-after($phrase,' ')"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:variable>
		<xsl:copy-of select="$result"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- personsFullName Template		-->
	<!-- **************************************** -->
	<doc:template name="util:personsFullName" xmlns="">
		<refpurpose>Formats a persons name as follows:
		<para> forename1 forename surname</para>
		</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>name</term>
					<listitem>
						<para>node containing CitizenNameStructure</para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>nonDefendant</term>
					<listitem>
						<para>indicates when name is not for a defendant</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted name</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: if the input name is for a defendant then util:stripCommas 
		is used to remove any commas associated with forename values.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:personsFullName">
		<!-- expects Citizen name structure to be passed in as param.
	     returns full name as follows 'forename1 forename2 [...] surname'
	-->
		<xsl:param name="name"/>
		<xsl:param name="nonDefendant"/>
		<xsl:for-each select="$name/apd:CitizenNameForename">
			<xsl:choose>
				<xsl:when test="$nonDefendant">
					<xsl:value-of select="."/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="util:stripCommas">
						<xsl:with-param name="name" select="."/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> </xsl:text>
		</xsl:for-each>
		<xsl:value-of select="$name/apd:CitizenNameSurname"/>
		<xsl:text> </xsl:text>
	</xsl:template>
	<!-- **************************************** -->
	<!-- stripCommas Template			-->
	<!-- **************************************** -->
	<doc:template name="util:stripCommas" xmlns="">
		<refpurpose>Replaces commas in a string with spaces</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>name</term>
					<listitem>
						<para>Source string to be modified</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>String without commas</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:stripCommas">
		<xsl:param name="name"/>
		<xsl:call-template name="str:subst">
			<xsl:with-param name="text" select="$name"/>
			<xsl:with-param name="replace" select="','"/>
			<xsl:with-param name="with" select="' '"/>
		</xsl:call-template>
	</xsl:template>
	<!-- **************************************** -->
	<!-- solicitorDetails Template			-->
	<!-- **************************************** -->
	<doc:template name="util:solicitorDetails" xmlns="">
		<refpurpose>Returns either the formatted solicitor details or 
		the text 'In Person' if there are no solicitor details.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>party</term>
					<listitem>
						<para>Solicitor/Party nodeset</para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>nobody</term>
					<listitem>
						<para>Text to be returned if no soloicitor details present. 
						Default value is 'In Person'</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>If a company of solicitors then the name of the company, or the name of
			and individual solicitor, or the text when no solicitor details present (nobody parameter value)</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: if an individual (Person node present) use util:formalName to format the name.</para>
			</listitem>
			<listitem>
				<para>Note: A company is indicated by the presence of the Organisation node.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:solicitorDetails">
		<!-- expects cs:Solicitor/cs:Party to be passed in as param
		 return either the personal name if an idividual, the company name or the text in the second param
		 (default = 'In Person') if no details present.
	-->
		<xsl:param name="party"/>
		<xsl:param name="nobody" select="'In person'"/>
		<xsl:choose>
			<xsl:when test="$party/cs:Person">
				<xsl:call-template name="util:formalName">
					<xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$party/cs:Organisation">
				<xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$nobody"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- addressTemplate				-->
	<!-- **************************************** -->
	<doc:template name="util:address" xmlns="">
		<refpurpose>Returns an address and post code formatted with <br/> separating the lines.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>personalDetails</term>
					<listitem>
						<para>PersonalDetails nodeset structure</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted address.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: The schema specifies a minimum of 2 address lines but the data sometimes only contains one,
			in this case the second line is given a value of '-' which should not be included in the
			formatted address</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:address">
		<!-- expects personal details structure to be passed -->
		<!-- returns address and post code with <br /.> separating each line -->
		<xsl:param name="personalDetails"/>
		<xsl:variable name="addr">
			<xsl:for-each select="$personalDetails/cs:Address/apd:Line[not (position()=2 and .='-')]">
				<xsl:call-template name="str:capitalise">
					<xsl:with-param name="text" select="."/>
				</xsl:call-template>
				<xsl:if test="not (position() = last())">
					<br/>
				</xsl:if>
			</xsl:for-each>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="$personalDetails/cs:Address/apd:PostCode"/>
		</xsl:variable>
		<xsl:copy-of select="$addr"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- address_oneline Template		-->
	<!-- **************************************** -->
	<doc:template name="util:address_oneline" xmlns="">
		<refpurpose>Returns an address and post code formatted on a single line, with comma separators.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>personalDetails</term>
					<listitem>
						<para>PersonalDetails nodeset structure</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted address.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: The schema specifies a minimum of 2 address lines but the data sometimes only contains one,
				in this case the second line is given a value of '-' which should not be included in the
				formatted address</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:address_oneline">
		<!-- expects personal details structure to be passed -->
		<!-- returns address and post code all on one line -->
		<xsl:param name="personalDetails"/>
		<xsl:variable name="addr">
			<xsl:for-each select="$personalDetails/cs:Address/apd:Line[not ( .='-') and not (. = ' ')]">
				<xsl:call-template name="str:capitalise">
					<xsl:with-param name="text" select="."/>
				</xsl:call-template>
				<xsl:if test="not (position() = last())">
					<xsl:if test="string-length() &gt; 0">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="$personalDetails/cs:Address/apd:PostCode">
				<xsl:text>  </xsl:text>
				<xsl:value-of select="$personalDetails/cs:Address/apd:PostCode"/>
			</xsl:if>
		</xsl:variable>
		<xsl:copy-of select="$addr"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- address_oneline_court Template	 -->
	<!-- **************************************** -->
	<doc:template name="util:address_oneline_court" xmlns="">
		<refpurpose>Returns an address and post code formatted on a single line, with comma separators.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>courtDetails</term>
					<listitem>
						<para>CourtHouseStructure nodeset structure</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted address.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: The schema specifies a minimum of 2 address lines but the data sometimes only contains one,
				in this case the second line is given a value of '-' which should not be included in the
				formatted address</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:address_oneline_court">
		<!-- expects personal details structure to be passed -->
		<!-- returns address and post code all on one line -->
		<xsl:param name="courtDetails"/>
		<xsl:variable name="addr">
			<xsl:for-each select="$courtDetails/cs:CourtHouseAddress/apd:Line[not ( .='-') and not (. = ' ')]">
				<xsl:call-template name="str:capitalise">
					<xsl:with-param name="text" select="."/>
				</xsl:call-template>
				<xsl:if test="not (position() = last())">
					<xsl:if test="string-length() &gt; 0">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="$courtDetails/cs:CourtHouseAddress/apd:PostCode"/>
		</xsl:variable>
		<xsl:copy-of select="$addr"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- formalName Template			-->
	<!-- **************************************** -->
	<doc:template name="util:formalName" xmlns="">
		<refpurpose>Returns an name in the following format: 
		            title initials surname suffix e.g Mr HG Wells MBE.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>name</term>
					<listitem>
						<para>PersonalDetails nodeset structure</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted name.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:formalName">
		<!-- expects PersonalDetails/Name structure as inpit parameter -->
		<!-- return name in the following format title inits surname -->
		<!--  ie Mr FJ Bloggs -->
		<xsl:param name="name"/>
		<xsl:variable name="newname">
			<xsl:value-of select="$name/apd:CitizenNameTitle"/>
			<xsl:text> </xsl:text>
			<xsl:for-each select="$name/apd:CitizenNameForename">
				<xsl:value-of select="."/>
				<xsl:text> </xsl:text>
				<!-- amended for PR 59709 and PR 59710 -->
			</xsl:for-each>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$name/apd:CitizenNameSurname"/>
			<xsl:for-each select="$name/apd:CitizenNameSuffix">
				<xsl:text> </xsl:text>
				<xsl:value-of select="."/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:copy-of select="$newname"/>
	</xsl:template>
	<!-- *************************************************************** -->
	<!-- CitizenNameRequestedName / formalName Template    -->
	<!-- *************************************************************** -->
	<xsl:template name="util:CNRNformalName">
		<!-- expects PersonalDetails/Name structure as inpit parameter -->
		<!-- return name in the following format title inits surname -->
		<!--  ie Mr FJ Bloggs -->
		<xsl:param name="name"/>
		<xsl:variable name="newname">
			<xsl:choose>
				<xsl:when test="$name/apd:CitizenNameRequestedName">
					<xsl:value-of select="$name/apd:CitizenNameRequestedName"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$name/apd:CitizenNameTitle"/>
					<xsl:text> </xsl:text>
					<xsl:for-each select="$name/apd:CitizenNameForename">
						<xsl:value-of select="."/>
					</xsl:for-each>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$name/apd:CitizenNameSurname"/>
					<xsl:for-each select="$name/apd:CitizenNameSuffix">
						<xsl:text> </xsl:text>
						<xsl:value-of select="."/>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:copy-of select="$newname"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- getGender Template				-->
	<!-- **************************************** -->
	<doc:template name="util:getGender" xmlns="">
		<refpurpose>Returns 'M' for male, 'F' for female, or ' '</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>sex</term>
					<listitem>
						<para>string (from personalDetails)</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Single character for sex.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:getGender">
		<xsl:param name="sex"/>
		<xsl:choose>
			<xsl:when test="$sex = 'male'">
				<xsl:text>M </xsl:text>
			</xsl:when>
			<xsl:when test="$sex = 'female'">
				<xsl:text>F </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- getDateQualifier Template		-->
	<!-- **************************************** -->
	<doc:template name="util:getDateQualifier" xmlns="">
		<refpurpose>If the input phrase contains 'on or before' or 'before', return 'no later than'</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>text</term>
					<listitem>
						<para>Source string</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>'no later than' if appropriate or source string unchanged.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:getDateQualifier">
		<xsl:param name="text"/>
		<xsl:choose>
			<xsl:when test="($text = 'on or before') or 
			                ($text = 'before')">
				<xsl:text>no later than</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- getSubjectTypeTemplate			-->
	<!-- **************************************** -->
	<doc:template name="util:getSubjectType" xmlns="">
		<refpurpose>Based on the first character of case number 
		returns text 'Appellant' (1st char = 'A') or 'Defendant' (any other value)</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>caseNum</term>
					<listitem>
						<para>Case number</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>String value : Appellant or Defendant</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:getSubjectType">
		<!-- based on the first letter of the case number returns the subject type 
		     where case number begins with A - return Appellant
			 anything else return Defendant (the default hence the default caseNum of X00000)
		-->
		<xsl:param name="caseNum" select="'X00000'"/>
		<xsl:choose>
			<xsl:when test="starts-with($caseNum,'A')">
				<xsl:text>Appellant</xsl:text>
			</xsl:when>
			<xsl:when test="starts-with($caseNum,'a')">
				<xsl:text>Appellant</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Defendant</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- orderSignatory template			-->
	<!-- **************************************** -->
	<doc:template name="util:orderSignatory" xmlns="">
		<refpurpose>Creates the standard order signatory output which appears at the bottom of various orders</refpurpose>
		<refreturn>
			<para>Order signatory html</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:orderSignatory">
		<br/>
		<br/>
		<table width="100%">
			<tr>
				<td width="20%"/>
				<xsl:choose>
					<xsl:when test="//cs:OrderHeader/cs:SignedBy/cs:CourtOfficer">
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
					</xsl:when>
					<xsl:otherwise>
						<td width="80%">
							<xsl:text>Judge of the Crown Court </xsl:text>
							<br/>
							<strong>
								<xsl:text>Signed: </xsl:text>
								<xsl:call-template name="util:formalName">
									<xsl:with-param name="name" select="//cs:OrderHeader/cs:SignedBy/cs:Judiciary/cs:Judge"/>
								</xsl:call-template>
							</strong>
						</td>
					</xsl:otherwise>
				</xsl:choose>
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
	<!-- **************************************** -->
	<!-- publishDate Template			-->
	<!-- **************************************** -->
	<doc:template name="publishDate" xmlns="">
		<refpurpose>Display the List published date and time..</refpurpose>
		<refdescription>
			<para>The published date and time is displayed twice on the report, once below the list header,
		        and then again as part of the list footer
		  </para>
		</refdescription>
	</doc:template>
	<xsl:template name="util:publishDate">
		<table width="100%">
			<tr>
				<td align="left">
					<xsl:text>Published: </xsl:text>
					<xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime"/>
					<xsl:call-template name="date:format-date-time">
						<xsl:with-param name="year" select="substring($pubTime,1,4)"/>
						<xsl:with-param name="month" select="substring($pubTime,6,2)"/>
						<xsl:with-param name="day" select="substring($pubTime,9,2)"/>
						<xsl:with-param name="hour" select="substring($pubTime,12,2)"/>
						<xsl:with-param name="minute" select="substring($pubTime,15,2)"/>
						<xsl:with-param name="format" select="'%D %B %Y '"/>
					</xsl:call-template>
				</td>
				<td align="right">
			</td>
			</tr>
		</table>
	</xsl:template>
	<!-- **************************************** -->
	<!-- listFooter template				-->
	<!-- **************************************** -->
	<doc:template name="util:listFooter" xmlns="">
		<refpurpose>Creates the standard report footer html which appears at the bottom of list documents.</refpurpose>
		<refreturn>
			<para>List footer html</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:listFooter">
		<!-- creates the footer in the output -->
		<xsl:param name="court"/>
		<hr/>
		<table class="detail" width="100%">
			<tr>
				<td align="left">
					<small>
						<xsl:if test="$court/cs:CourtHouseAddress">
							<!-- <xsl:for-each select="$court/cs:CourtHouseAddress/apd:Line[position() &lt; 5 and (not (position()=2 and .='-'))and not (. = ' ')]"> -->
							<xsl:for-each select="$court/cs:CourtHouseAddress/apd:Line[position() &lt; 5 and (not ( .='-'))and not (. = ' ')]">
								<xsl:call-template name="str:capitalise">
									<xsl:with-param name="text" select="."/>
								</xsl:call-template>
								<xsl:text> </xsl:text>
							</xsl:for-each>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$court/cs:CourtHouseAddress/apd:PostCode"/>
						</xsl:if>
						<xsl:if test="$court/cs:CourtHouseDX">
							<xsl:text> </xsl:text>
							<xsl:value-of select="$court/cs:CourtHouseDX"/>
						</xsl:if>
						<xsl:if test="$court/cs:CourtHouseTelephone">
							<xsl:text> Tel: </xsl:text>
							<xsl:value-of select="$court/cs:CourtHouseTelephone"/>
						</xsl:if>
						<xsl:if test="$court/cs:CourtHouseFax">
							<xsl:text> Fax: </xsl:text>
							<xsl:value-of select="$court/cs:CourtHouseFax"/>
						</xsl:if>
					</small>
				</td>
				<td align="right">
					<xsl:text>Ref: </xsl:text>
					<xsl:value-of select="//cs:ListHeader/cs:CRESTprintRef"/>
				</td>
			</tr>
		</table>
		<!-- Now do Published Date -->
		<xsl:call-template name="util:publishDate"/>
		<!-- KN 20050517 - CR27 	-->
		<xsl:call-template name="util:copyrightText"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- associatedCases template		-->
	<!-- **************************************** -->
	<doc:template name="util:associatedCases" xmlns="">
		<refpurpose>Returns the html for the Associated Cases information that comes
		at the bottom of several documents.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>cases</term>
					<listitem>
						<para>AssociatedCases nodeset</para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>rulesRequired</term>
					<listitem>
						<para>boolean indicating wether a <hr/> html tag should be added to end of html or not. Default is true.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>fragment of html for Associated Cases section.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:associatedCases">
		<xsl:param name="cases"/>
		<xsl:param name="rulesRequired" select="'true'"/>
		<xsl:if test="$rulesRequired = 'true'">
			<hr/>
		</xsl:if>
		<table width="100%">
			<tr>
				<td width="20%" valign="top">
					<xsl:text>Associated Cases:</xsl:text>
				</td>
				<td width="80%">
					<xsl:choose>
						<xsl:when test="$cases">
							<xsl:for-each select="$cases/cs:AssociatedCase">
								<xsl:value-of select="."/>
								<xsl:text> </xsl:text>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>None</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
		<xsl:if test="$rulesRequired = 'true'">
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- getArticle template				-->
	<!-- **************************************** -->
	<doc:template name="util:getArticle" xmlns="">
		<refpurpose>Returns the html for the Associated Cases information that comes
		at the bottom of several documents.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>inText</term>
					<listitem>
						<para>Source text.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>prefixes the source text with the correct form of the indefinite article (a or an)
			depending on what letter the source text begins with.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:getArticle">
		<xsl:param name="inText"/>
		<xsl:variable name="lcText">
			<xsl:call-template name="str:to-lower">
				<xsl:with-param name="text" select="$inText"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="starts-with($lcText,'a') or
							starts-with($lcText,'e') or
							starts-with($lcText,'i') or
							starts-with($lcText,'o') or
							starts-with($lcText,'u') ">
				<xsl:text> an </xsl:text>
				<xsl:value-of select="$inText"/>
				<xsl:text> </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> a </xsl:text>
				<xsl:value-of select="$inText"/>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- processCondition template		-->
	<!-- **************************************** -->
	<doc:template name="util:processCondition" xmlns="">
		<refpurpose>Parses a condition from a community order and expands surety, security, period etc .</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>condition</term>
					<listitem>
						<para>Pre or post Condition nodeset from an order.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Reformatted condition as a bulleted table row.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Use util:decodeDuration to format any period values.</para>
			</listitem>
			<listitem>
				<para>Note: Use util:ukdate to format date values.</para>
			</listitem>
			<listitem>
				<para>Note: Surety and Security are formatted as monetary values</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:processCondition">
		<!-- passed a condition from a community order 
				parses the condition for period, security etc and reformats as a bullet row
		-->
		<xsl:param name="condition"/>
		<xsl:variable name="output">
			<xsl:value-of select="$condition/cs:Description"/>
			<xsl:text> </xsl:text>
			<xsl:if test="$condition/cs:Period">
				<xsl:call-template name="util:decodeDuration">
					<xsl:with-param name="duration" select="$condition/cs:Period"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
			</xsl:if>
			<xsl:if test="$condition/cs:Date">
				<xsl:text> from </xsl:text>
				<xsl:call-template name="util:ukdate">
					<xsl:with-param name="inDate" select="$condition/cs:Date"/>
				</xsl:call-template>
				<xsl:text>. </xsl:text>
			</xsl:if>
			<xsl:if test="$condition/cs:Surety">
				<xsl:text>To provide surety </xsl:text>
				<xsl:text> in the sum of &#x00A3;</xsl:text>
				<xsl:value-of select="format-number($condition/cs:Surety,'##,###,##0.00')"/>
				<xsl:text>. </xsl:text>
			</xsl:if>
			<xsl:if test="$condition/cs:Security">
				<xsl:text>To provide a security in the sum of &#x00A3;</xsl:text>
				<xsl:value-of select="format-number($condition/cs:Security,'##,###,##0.00')"/>
				<xsl:text> to be deposited with the court. </xsl:text>
			</xsl:if>
		</xsl:variable>
		<xsl:call-template name="util:bulletRow">
			<xsl:with-param name="bulletText" select="$output"/>
		</xsl:call-template>
	</xsl:template>
	<!-- **************************************** -->
	<!-- bulletRow Template				-->
	<!-- **************************************** -->
	<doc:template name="util:bulletRow" xmlns="">
		<refpurpose>Formats input text into an html fragment for a bulleted item in a table row.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>bulletText</term>
					<listitem>
						<para>Text to be changed into html fragment.</para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>bulletWidth</term>
					<listitem>
						<para>Width of table cell holding bullet character. 
						Expressed as percentage of table width - default = 5.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>html fragment for bulleted table row containg source text as bullet point.</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:bulletRow">
		<xsl:param name="bulletText"/>
		<xsl:param name="bulletWidth" select="5"/>
		<tr>
			<xsl:element name="td">
				<xsl:attribute name="width"><xsl:value-of select="concat($bulletWidth,'%')"/></xsl:attribute>
				<xsl:attribute name="align">right</xsl:attribute>
				<xsl:attribute name="valign">top</xsl:attribute>
				<li/>
			</xsl:element>
			<xsl:variable name="remainder" select="100-$bulletWidth"/>
			<xsl:element name="td">
				<xsl:attribute name="width"><xsl:value-of select="concat($remainder,'%')"/></xsl:attribute>
				<xsl:copy-of select="$bulletText"/>
			</xsl:element>
		</tr>
	</xsl:template>
	<!-- not specific to particular elements -->
	<!--  +++++++++++++++++ logo image requirements ++++++++++++ -->
	<!-- logo is held in two places ~ inside and outside gsi
	     Use the url of one as the default location for the logo, if the load fails
		 then use the alternative url by calling javascript function. 
		 
		 Please change the two following URLs to reflect the live situation
	-->
	<!-- **************************************** -->
	<!-- javascript Template				-->
	<!-- **************************************** -->
	<doc:template name="util:javascript" xmlns="">
		<refpurpose>Javascript routine which is included in any documents which need	to display the Crown logo. </refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Basically there are three URLs where the logo can be found ie
	inside firewall, outside firewall etc. If the browser does not have access to the default 
	logo location an error will occur and the javascript function catches the error and changes
	the logo location url to one of the other values, and tries again. </para>
				<para>Now removed CJSE is no longer on the GSI and the logo is available on internet</para>
				<para>Leave empty template as place marker</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:javascript">
	</xsl:template>
	<!-- **************************************** -->
	<!-- showLogo Template				-->
	<!-- **************************************** -->
	<doc:template name="util:showLogo" xmlns="">
		<refpurpose>Outputs the html link for the Crown logo</refpurpose>
	</doc:template>
	<xsl:template name="util:showLogo">
		<center>
			<xsl:element name="img">
				<xsl:attribute name="src">https://pportal.cjsonline.gov.uk/secure/CourtService/lcd.gif</xsl:attribute>
				<xsl:attribute name="alt">Image: crown logo</xsl:attribute>
				<xsl:attribute name="name">logoImage</xsl:attribute>
				<xsl:attribute name="height">150</xsl:attribute>
				<xsl:attribute name="width">186</xsl:attribute>
			</xsl:element>
		</center>
	</xsl:template>
	<!-- **************************************** -->
	<!-- chargeSentence Template		-->
	<!-- **************************************** -->
	<doc:template name="util:chargeSentence" xmlns="">
		<refpurpose>As required formats the sentence period and/or outputs 'Life'.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>charge</term>
					<listitem>
						<para>Charge nodeset.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted charge sentence.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Use util:decodeDuration to format any period values.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:chargeSentence">
		<xsl:param name="charge"/>
		<xsl:if test="$charge/cs:SentenceTerm">
			<xsl:call-template name="util:decodeDuration">
				<xsl:with-param name="duration" select="$charge/cs:SentenceTerm"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="$charge/cs:ForLife='yes'">
			<xsl:text>Life </xsl:text>
		</xsl:if>
		<br/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- decode Template		-->
	<!-- **************************************** -->
	<doc:template name="util:decodeDuration" xmlns="">
		<refpurpose>Formats an xsd:duration value into words and numbers eg 3 years 6 months.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>duration</term>
					<listitem>
						<para>a standard xsd:duration PnYnMnDTnHnMnS.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Duration formatted as a text string eg 1 year 3 months.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Zero values are not returned eg 1Y0M3D is 1 year 3 days</para>
			</listitem>
			<listitem>
				<para>Plural values are used as appropriate</para>
			</listitem>
			<listitem>
				<para>Called recursively to handle each element of the duration</para>
			</listitem>
			<listitem>
				<para>'M' values could be months or minutes therefore use the 'T' separator as a guide to which is being dealt with.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:decodeDuration">
		<!-- a standard xsd:duration PnYnMnDTnHnMnS is passed in as a parameter 
		 Assumed to be a valid format.
	     returns the duration as a text string
		 NB zero values are not printed ie P1Y0M0D is printed as 1 year not 1 year 0 months 0 days
		 -->
		<xsl:param name="duration"/>
		<xsl:if test="$duration">
			<xsl:choose>
				<xsl:when test="contains($duration,'P')">
					<xsl:text> </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'P')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'Y')">
					<xsl:variable name="year" select="substring-before($duration,'Y')"/>
					<xsl:if test="$year = 1 ">
						<xsl:value-of select="$year"/>
						<xsl:text> year </xsl:text>
					</xsl:if>
					<xsl:if test="$year &gt; 1 ">
						<xsl:value-of select="$year"/>
						<xsl:text> years </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'Y')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'M')
							and not (contains(substring-before($duration,'M'),'D') or
						              contains(substring-before($duration,'M'),'H'))">
					<!-- could be minutes or months, for minutes will carry the T delimiter through
				 before the M delimiter
			-->
					<xsl:choose>
						<xsl:when test="not (contains(substring-before($duration,'M'),'T'))">
							<xsl:variable name="month" select="substring-before($duration,'M')"/>
							<xsl:if test="$month = 1">
								<xsl:value-of select="$month"/>
								<xsl:text> month </xsl:text>
							</xsl:if>
							<xsl:if test="$month &gt; 1">
								<xsl:value-of select="$month"/>
								<xsl:text> months </xsl:text>
							</xsl:if>
							<xsl:call-template name="util:decodeDuration">
								<xsl:with-param name="duration" select="substring-after($duration,'M')"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(substring-before($duration,'M'),'T')">
							<!-- only process minutes if already processed any hours or days -->
							<xsl:variable name="minute" select="substring-after(substring-before($duration,'M'),'T')"/>
							<xsl:if test="$minute = 1">
								<xsl:value-of select="$minute"/>
								<xsl:text> minute </xsl:text>
							</xsl:if>
							<xsl:if test="$minute &gt; 1">
								<xsl:value-of select="$minute"/>
								<xsl:text> minutes </xsl:text>
							</xsl:if>
							<xsl:call-template name="util:decodeDuration">
								<xsl:with-param name="duration" select="concat('T',substring-after($duration,'M'))"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="contains($duration,'D')">
					<xsl:variable name="day" select="substring-before($duration,'D')"/>
					<xsl:if test="$day = 1 ">
						<xsl:value-of select="$day"/>
						<xsl:text> day </xsl:text>
					</xsl:if>
					<xsl:if test="$day &gt; 1 ">
						<xsl:value-of select="$day"/>
						<xsl:text> days </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'D')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'H')">
					<xsl:variable name="hour" select="substring-after(substring-before($duration,'H'),'T')"/>
					<xsl:if test="$hour = 1 ">
						<xsl:value-of select="$hour"/>
						<xsl:text> hour </xsl:text>
					</xsl:if>
					<xsl:if test="$hour &gt; 1 ">
						<xsl:value-of select="$hour"/>
						<xsl:text> hours </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="concat('T',substring-after($duration,'H'))"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'S')">
					<xsl:variable name="second" select="substring-after(substring-before($duration,'S'),'T')"/>
					<xsl:if test="$second = 1 ">
						<xsl:value-of select="$second"/>
						<xsl:text> second </xsl:text>
					</xsl:if>
					<xsl:if test="$second &gt; 1 ">
						<xsl:value-of select="$second"/>
						<xsl:text> seconds </xsl:text>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- decode Template		-->
	<!-- **************************************** -->
	<doc:template name="util:decodeModifiedDuration" xmlns="">
		<refpurpose>Formats an xsd:duration based value (which now includes weeks) into words and numbers eg 3 years 6 months.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>duration</term>
					<listitem>
						<para>a modified xsd:duration PnYnMnWnDTnHnMnS.</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Duration formatted as a text string eg 1 year 3 months.</para>
		</refreturn>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Zero values are not returned eg 1Y0M3D is 1 year 3 days</para>
			</listitem>
			<listitem>
				<para>Plural values are used as appropriate</para>
			</listitem>
			<listitem>
				<para>Called recursively to handle each element of the duration</para>
			</listitem>
			<listitem>
				<para>'M' values could be months or minutes therefore use the 'T' separator as a guide to which is being dealt with.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template name="util:decodeModifiedDuration">
		<!-- a modified xsd:duration (contains week option) PnYnMnWnDTnHnMnS is passed in as a parameter 
		 Assumed to be a valid format.
	     returns the duration as a text string
		 NB zero values are not printed ie P1Y0M0D is printed as 1 year not 1 year 0 months 0 days
		 -->
		<xsl:param name="duration"/>
		<xsl:if test="$duration">
			<xsl:choose>
				<xsl:when test="contains($duration,'P')">
					<xsl:text> </xsl:text>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'P')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'Y')">
					<xsl:variable name="year" select="substring-before($duration,'Y')"/>
					<xsl:if test="$year = 1 ">
						<xsl:value-of select="$year"/>
						<xsl:text> year </xsl:text>
					</xsl:if>
					<xsl:if test="$year &gt; 1 ">
						<xsl:value-of select="$year"/>
						<xsl:text> years </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'Y')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'M')
							and not (contains(substring-before($duration,'M'),'D') or
						              contains(substring-before($duration,'M'),'H'))">
					<!-- could be minutes or months, for minutes will carry the T delimiter through
				 before the M delimiter
			-->
					<xsl:choose>
						<xsl:when test="not (contains(substring-before($duration,'M'),'T'))">
							<xsl:variable name="month" select="substring-before($duration,'M')"/>
							<xsl:if test="$month = 1">
								<xsl:value-of select="$month"/>
								<xsl:text> month </xsl:text>
							</xsl:if>
							<xsl:if test="$month &gt; 1">
								<xsl:value-of select="$month"/>
								<xsl:text> months </xsl:text>
							</xsl:if>
							<xsl:call-template name="util:decodeModifiedDuration">
								<xsl:with-param name="duration" select="substring-after($duration,'M')"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(substring-before($duration,'M'),'T')">
							<!-- only process minutes if already processed any hours or days -->
							<xsl:variable name="minute" select="substring-after(substring-before($duration,'M'),'T')"/>
							<xsl:if test="$minute = 1">
								<xsl:value-of select="$minute"/>
								<xsl:text> minute </xsl:text>
							</xsl:if>
							<xsl:if test="$minute &gt; 1">
								<xsl:value-of select="$minute"/>
								<xsl:text> minutes </xsl:text>
							</xsl:if>
							<xsl:call-template name="util:decodeModifiedDuration">
								<xsl:with-param name="duration" select="concat('T',substring-after($duration,'M'))"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="contains($duration,'W')">
					<xsl:variable name="week" select="substring-before($duration,'W')"/>
					<xsl:if test="$week = 1 ">
						<xsl:value-of select="$week"/>
						<xsl:text> week </xsl:text>
					</xsl:if>
					<xsl:if test="$week &gt; 1 ">
						<xsl:value-of select="$week"/>
						<xsl:text> weeks </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'W')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'D')">
					<xsl:variable name="day" select="substring-before($duration,'D')"/>
					<xsl:if test="$day = 1 ">
						<xsl:value-of select="$day"/>
						<xsl:text> day </xsl:text>
					</xsl:if>
					<xsl:if test="$day &gt; 1 ">
						<xsl:value-of select="$day"/>
						<xsl:text> days </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="substring-after($duration,'D')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'H')">
					<xsl:variable name="hour" select="substring-after(substring-before($duration,'H'),'T')"/>
					<xsl:if test="$hour = 1 ">
						<xsl:value-of select="$hour"/>
						<xsl:text> hour </xsl:text>
					</xsl:if>
					<xsl:if test="$hour &gt; 1 ">
						<xsl:value-of select="$hour"/>
						<xsl:text> hours </xsl:text>
					</xsl:if>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="concat('T',substring-after($duration,'H'))"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="contains($duration,'S')">
					<xsl:variable name="second" select="substring-after(substring-before($duration,'S'),'T')"/>
					<xsl:if test="$second = 1 ">
						<xsl:value-of select="$second"/>
						<xsl:text> second </xsl:text>
					</xsl:if>
					<xsl:if test="$second &gt; 1 ">
						<xsl:value-of select="$second"/>
						<xsl:text> seconds </xsl:text>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- getHourText Template			-->
	<!-- **************************************** -->
	<doc:template name="util:getHourText" xmlns="">
		<refpurpose>Returns 'hour' or 'hours' depending on input value.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>hours</term>
					<listitem>
						<para>number</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>'hour' or 'hours'</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:getHourText">
		<xsl:param name="hours"/>
		<xsl:choose>
			<xsl:when test="$hours &gt; 1">
				<xsl:text> hours</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> hour</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- cssTemplate Template			-->
	<!-- **************************************** -->
	<doc:template name="util:cssTemplate" xmlns="">
		<refpurpose>Included in every document stylesheet. Puts CSS stylesheet in output document.</refpurpose>
		<refdescription>Adds copyright info to every html document produced,plus defines styles for tables etc.</refdescription>
	</doc:template>
	<xsl:template name="util:cssTemplate">
		<xsl:comment>
			<xsl:text>Produced by : </xsl:text>
			<xsl:value-of select="$stylesheet"/>
			<xsl:text> version </xsl:text>
			<xsl:value-of select="$majorVersion"/>
			<xsl:text>.</xsl:text>
			<xsl:value-of select="$minorVersion"/>
			<xsl:text>  last modified : </xsl:text>
			<xsl:call-template name="util:ukdate_fullMonth">
				<xsl:with-param name="inDate" select="$last-modified-date"/>
			</xsl:call-template>
		</xsl:comment>
		<xsl:comment>&#x00A9; Crown copyright 2003. All rights reserved.</xsl:comment>
		<xsl:comment>
			<xsl:text>Document Unique Id : </xsl:text>
			<xsl:value-of select="//cs:DocumentID/cs:UniqueID"/>
			<xsl:text>Document version : </xsl:text>
			<xsl:value-of select="//cs:DocumentID/cs:Version"/>
			<xsl:text>Document timestamp : </xsl:text>
			<xsl:value-of select="//cs:DocumentID/cs:TimeStamp"/>
			<xsl:text>Document stylesheet URL : </xsl:text>
			<xsl:value-of select="//cs:DocumentID/cs:XSLstylesheetURL"/>
		</xsl:comment>
		<style type="text/css">
			table.detail 	{font-family: Arial Narrow, Verdana, Arial, Helvetica, sans-serif ;
							 padding-top: 0;
							 padding-bottom: 0;}
							 
			table.emphasis {font-family: Arial Narrow, Verdana, Arial, Helvetica, sans-serif ;
							font-size: larger}
							padding-top: 0;
							padding-bottom: 0;}
			h2 tr           {line-height: normal;}
			tr				{line-height: 1em;}		
			
			span.emphasis	{font-family: Arial Narrow, Verdana, Arial, Helvetica, sans-serif ;
			       			 font-weight: bold;
							 text-decoration: underline;}
							 
			*				{font-family: Arial Narrow, Verdana, Arial, Helvetica, sans-serif ;}
			
			strong 			{font-family: Arial Narrow, Verdana, Arial, Helvetica, sans-serif ;
			       			 font-weight: bold;}
			tr strong td	{font-weight: bolder; }
			
		</style>
	</xsl:template>
	<!-- **************************************** -->
	<!-- copyrightText Template			-->
	<!-- **************************************** -->
	<doc:template name="util:copyrightText" xmlns="">
		<refpurpose>Outputs Copyright Name at bottom of HTML.</refpurpose>
	</doc:template>
	<!-- Display Copyright -->
	<!-- Now modified to always display current year-->
	<xsl:template name="util:copyrightText">
		<br/>
		<xsl:text>&#x00A9; Crown copyright </xsl:text>
		<xsl:value-of select="substring(//cs:DocumentID/cs:TimeStamp, 1, 4)"/>
		<xsl:text>. All rights reserved. Issued by His Majesty’s Court Service.</xsl:text>
	</xsl:template>
	<!-- **************************************** -->
	<!-- copyrightText 2006 Template RFC1376-->
	<!-- Now modified to always display current year-->
	<!-- **************************************** -->
	<doc:template name="util:copyrightText2006" xmlns="">
		<refpurpose>Outputs Copyright Name at bottom of HTML. 2006 Version for RFC1376</refpurpose>
	</doc:template>
	<!-- Display Copyright -->
	<xsl:template name="util:copyrightText2006">
		<br/>
		<xsl:text>&#x00A9; Crown copyright </xsl:text>
		<xsl:value-of select="substring(//cs:DocumentID/cs:TimeStamp, 1, 4)"/>
		<xsl:text>. All rights reserved. Issued by His Majesty’s Court Service.</xsl:text>
	</xsl:template>
	<!-- **************************************** -->
	<!-- copyOriginalOrder Template			-->
	<!-- **************************************** -->
	<doc:template name="util:copyOrderText" xmlns="">
		<refpurpose>Outputs This is a Copy at bottom of Orders HTML.</refpurpose>
	</doc:template>
	<!-- Display Copyright -->
	<xsl:template name="util:copyOrderText">
		<br/>
		<xsl:text>THIS DOCUMENT IS A COPY OF AN ORIGINAL ORDER OF THE CROWN COURT</xsl:text>
		<br/>
	</xsl:template>
	<!-- *********************************************************************** -->
	<!-- UniversalOrderHeader Template - Moved from Each Order XSL-->
	<!-- *********************************************************************** -->
	<doc:template name="util:UniversalOrderHeader" xmlns="">
		<refpurpose>Creates the Order Header information; including the court details and a list of any associated cases.</refpurpose>
	</doc:template>
	<xsl:template name="util:UniversalOrderHeader">
		<!-- processes the OrderHeader node - constructs the initial header information for the output -->
		<xsl:param name="OrderTitle"/>
		<xsl:param name="OrderHeaderRoot"/>
		<xsl:param name="OrderSubHeading"/>		
		<xsl:param name="OrderTitleDate"/>
		<xsl:param name="DisplayASN"/>
		<!-- new parameter added for remand order addressee - LASBO change -->
		<xsl:param name="OrderAddressee"/>
		
		<xsl:variable name="caseNums">
			<xsl:value-of select="$OrderHeaderRoot/cs:CaseNumber"/>
		</xsl:variable>
		<xsl:variable name="ASNums">
			<xsl:for-each select="//cs:ASNs/cs:ASN">
				<xsl:value-of select="."/>
				<br/>
			</xsl:for-each>
		</xsl:variable>
		<h1>
			<xsl:text>In the </xsl:text>
			<xsl:value-of select="$OrderHeaderRoot/cs:CourtHouse/cs:CourtHouseType"/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="$OrderHeaderRoot/cs:CourtHouse/cs:CourtHouseName"/>
		</h1>

		<xsl:if test="$OrderTitleDate">
			<table WIDTH="100%">
				<tr>
					<td>
						<xsl:value-of select="$OrderTitleDate"/>
					</td>
				</tr>
			</table>
		</xsl:if>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="15%" valign="top">
					<xsl:text>Case No:</xsl:text>
				</td>
				<td WIDTH="85%">
					<xsl:copy-of select="$caseNums"/>
				</td>
			</tr>
			<tr>
				<td WIDTH="15%">
					<xsl:text>Court Code:</xsl:text>
				</td>
				<td WIDTH="85%">
					<xsl:value-of select="$OrderHeaderRoot/cs:CourtHouse/cs:CourtHouseCode"/>
				</td>
			</tr>
			<tr>
				<td WIDTH="15%">
					<xsl:text>PTI URN:</xsl:text>
				</td>
				<td WIDTH="85%">
					<xsl:value-of select="$OrderHeaderRoot/cs:Defendant/cs:URN"/>
				</td>
			</tr>
			<xsl:if test="$DisplayASN='YES'">
				<tr>
					<td WIDTH="15%" valign="top">
						<xsl:text>ASN:</xsl:text>
					</td>
					<td WIDTH="85%">
						<xsl:copy-of select="$ASNums"/>
					</td>
				</tr>
			</xsl:if>
		</table>
		
		<xsl:if test="$OrderAddressee">	
			<br/>
			<br/>
			<xsl:value-of select="$OrderAddressee"/>
		</xsl:if>
		
		<center>
			<h1>
				<strong>
					<xsl:value-of select="$OrderTitle"/>
				</strong>
			</h1>
		</center>
		<xsl:if test="$OrderSubHeading">
			<table WIDTH="100%">
				<tr>
					<td>
						<center><i><xsl:value-of select="$OrderSubHeading"/></i></center>
					</td>
				</tr>
			</table>
		</xsl:if>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- AdditionalNotes	Template		-->
	<!-- **************************************** -->
	<doc:template name="util:AdditionalNotes" xmlns="">
		<refpurpose>Outputs the details of any Additional Notes for this order.</refpurpose>
	</doc:template>
	<xsl:template name="util:AdditionalNotes">
		<xsl:if test="//cs:AdditionalNotes">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Additional Notes:</xsl:text>
					</td>
					<td width="80%">
						<xsl:value-of select="//cs:AdditionalNotes"/>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- ************************************************************************* -->
	<!-- PR 57442	- The following functions were removed in 7.5		-->
	<!-- util:hearingDescription																-->
	<!-- util:altOrderSignatory																-->
	<!-- ************************************************************************* -->
	<!-- version of routine which uses document function. When BITS is modified use this version 
	     instead of alternative below 
	<xsl:template name="util:hearingDescription" >
	<xsl:param name="code"/>
		<xsl:value-of select="document('')/xsl:stylesheet/csdata:HearingTypes/HearingType[@HearingCode = $code]" />
	</xsl:template>
	-->
	<doc:template name="util:hearingDescription" xmlns="">
		<refpurpose>Looks up the hearing description for a particular hearing code.</refpurpose>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>code</term>
					<listitem>
						<para>Hearing code</para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Hearing description.</para>
		</refreturn>
	</doc:template>
	<!-- when BITS is fixed to use document function use the above version instead -->
	<xsl:template name="util:hearingDescription">
		<xsl:param name="code"/>
		<xsl:choose>
			<xsl:when test="$code ='ABG' ">For Appeal under the Betting, Gaming and Lotteries Act 1963</xsl:when>
			<xsl:when test="$code ='ACF' ">For Appeal in respect of Firearms Certificate</xsl:when>
			<xsl:when test="$code ='ACN' ">For Appeal against Conviction</xsl:when>
			<xsl:when test="$code ='ACO' ">For Appeal against Compensation Order</xsl:when>
			<xsl:when test="$code ='ACS' ">For Appeal against Conviction and Sentence</xsl:when>
			<xsl:when test="$code ='ACY' ">For Appeal under the Children and Young Persons Act 1969</xsl:when>
			<xsl:when test="$code ='ADJ' ">For Appeal against District Judge's Order</xsl:when>
			<xsl:when test="$code ='AEH' ">Admissibility of Evidence - Half day</xsl:when>
			<xsl:when test="$code ='AEW' ">Admissibility of Evidence - full day</xsl:when>
			<xsl:when test="$code ='AGA' ">For Appeal under the Gaming Act 1968</xsl:when>
			<xsl:when test="$code ='AHA' ">For Appeal under the Housing Act 1957</xsl:when>
			<xsl:when test="$code ='AHC' ">For Appeal in respect of Hackney Carriage Licence</xsl:when>
			<xsl:when test="$code ='ALA' ">For Appeal under the Lotteries and Amusements Act 1976</xsl:when>
			<xsl:when test="$code ='ALD' ">For Appeal under the Land Drainage Act 1976</xsl:when>
			<xsl:when test="$code ='ALJ' ">For Appeal against Licensing Justices Decision</xsl:when>
			<xsl:when test="$code ='APH' ">Appeal (Part Heard)</xsl:when>
			<xsl:when test="$code ='APL' ">For Appeal</xsl:when>
			<xsl:when test="$code ='APN' ">For Application</xsl:when>
			<xsl:when test="$code ='ASE' ">For Appeal against Sentence</xsl:when>
			<xsl:when test="$code ='BAC' ">Breach of Attendance Centre Order</xsl:when>
			<xsl:when test="$code ='BBO' ">Breach of Bind Over</xsl:when>
			<xsl:when test="$code ='BCD' ">Breach of Conditional Discharge</xsl:when>
			<xsl:when test="$code ='BCF' ">Breach of Requirements of Curfew Order</xsl:when>
			<xsl:when test="$code ='BCP' ">Breach of Requirements of both Community Service Order &amp; Probation Order</xsl:when>
			<xsl:when test="$code ='BCS' ">Breach of Requirements of Community Service Order</xsl:when>
			<xsl:when test="$code ='BEX' ">Breach of Exclusion Order</xsl:when>
			<xsl:when test="$code ='BFA' ">For Application to Break Fixture</xsl:when>
			<xsl:when test="$code ='BLA' ">For Application for Bail</xsl:when>
			<xsl:when test="$code ='BOO' ">Breach of Order</xsl:when>
			<xsl:when test="$code ='BPS' ">Breach of Partly Suspended Sentence</xsl:when>
			<xsl:when test="$code ='BRG' ">For Application to Rescind Grant of Bail</xsl:when>
			<xsl:when test="$code ='BRP' ">Breach of Requirements of Probation Order</xsl:when>
			<xsl:when test="$code ='BSS' ">Breach of Suspended Sentence</xsl:when>
			<xsl:when test="$code ='BVA' ">For Application to Vary Bail Conditions</xsl:when>
			<xsl:when test="$code ='CBR' ">Committal for Breaches</xsl:when>
			<xsl:when test="$code ='CCT' ">Contempt of Court Proceedings</xsl:when>
			<xsl:when test="$code ='CMA' ">For Application to Commit</xsl:when>
			<xsl:when test="$code ='CON' ">Continued</xsl:when>
			<xsl:when test="$code ='CPT' ">For Contempt</xsl:when>
			<xsl:when test="$code ='CSE' ">Committal for Sentence</xsl:when>
			<xsl:when test="$code ='CSP' ">Committal for Sentence (Part Heard)</xsl:when>
			<xsl:when test="$code ='CSS' ">Committals for Sentence</xsl:when>
			<xsl:when test="$code ='CTA' ">For Application to Extend Custody Time Limit</xsl:when>
			<xsl:when test="$code ='CTL' ">Custody Time Limit application</xsl:when>
			<xsl:when test="$code ='DCA' ">For Application for Dismissal of Charges</xsl:when>
			<xsl:when test="$code ='DIR' ">For Directions</xsl:when>
			<xsl:when test="$code ='DPR' ">For Deferred Sentence (Prosecution Released)</xsl:when>
			<xsl:when test="$code ='DRA' ">For Application under the Drug Trafficking Act 1994</xsl:when>
			<xsl:when test="$code ='DRR' ">For Deferred Sentence (Respondent Released)</xsl:when>
			<xsl:when test="$code ='DRS' ">For Application under the Drug Trafficking Act 1994, and Sentence</xsl:when>
			<xsl:when test="$code ='DSE' ">For Deferred Sentence</xsl:when>
			<xsl:when test="$code ='DTA' ">For Application under the Drug Trafficking Offences Act 1986</xsl:when>
			<xsl:when test="$code ='DTS' ">For Application under Drug Trafficking Offences Act 1986, and Sentence</xsl:when>
			<xsl:when test="$code ='EBW' ">For Execution of Bench Warrant</xsl:when>
			<xsl:when test="$code ='FHA' ">For Application to Fix Hearing Date</xsl:when>
			<xsl:when test="$code ='HRG' ">For Hearing</xsl:when>
			<xsl:when test="$code ='INJ' ">For Application for Injunction</xsl:when>
			<xsl:when test="$code ='JGT' ">For Judgment</xsl:when>
			<xsl:when test="$code ='LAA' ">For Application to Add to List</xsl:when>
			<xsl:when test="$code ='MAA' ">For Mention (Appellant to Attend)</xsl:when>
			<xsl:when test="$code ='MAP' ">For Mention (All Parties to Attend)</xsl:when>
			<xsl:when test="$code ='MBO' ">For Motion By Order</xsl:when>
			<xsl:when test="$code ='MDA' ">For Mention (Defendant to Attend)</xsl:when>
			<xsl:when test="$code ='MEF' ">For Mention and to Fix</xsl:when>
			<xsl:when test="$code ='MEN' ">For Mention</xsl:when>
			<xsl:when test="$code ='MNW' ">For Mention (No Witnesses Required)</xsl:when>
			<xsl:when test="$code ='MOA' ">For Mention (Officer to Attend)</xsl:when>
			<xsl:when test="$code ='PAD' ">For Plea and Directions</xsl:when>
			<xsl:when test="$code ='PAF' ">For Plea and to Fix</xsl:when>
			<xsl:when test="$code ='PCA' ">For Application under the Police and Criminal Evidence Act</xsl:when>
			<!-- Added by KN 15/4/05 -  to support RFC1380 -->
			<xsl:when test="$code ='PCM' ">For Plea and Case Management</xsl:when>
			<!-- End of Addition -->
			<xsl:when test="$code ='PHD' ">Part Heard</xsl:when>
			<xsl:when test="$code ='PLE' ">For Plea</xsl:when>
			<xsl:when test="$code ='PLY' ">For Preliminary Hearing</xsl:when>
			<xsl:when test="$code ='PPH' ">Plea (Part Heard)</xsl:when>
			<xsl:when test="$code ='PPT' ">For Plea/Pre-Trial Review</xsl:when>
			<xsl:when test="$code ='PRA' ">For Application under the Proceeds of Crime Act 1995</xsl:when>
			<xsl:when test="$code ='PRY' ">For Preparatory Hearing</xsl:when>
			<xsl:when test="$code ='PTN' ">For Plea to be Taken</xsl:when>
			<xsl:when test="$code ='PTR' ">For Pre-Trial Review</xsl:when>
			<xsl:when test="$code ='RBA' ">For Application to Revoke both Community Service Order &amp; Probation Order</xsl:when>
			<xsl:when test="$code ='RCA' ">For Application to Revoke Community Service Order</xsl:when>
			<xsl:when test="$code ='RCF' ">For Application to Revoke Curfew Order</xsl:when>
			<xsl:when test="$code ='RDA' ">For Application to Remove Driving Disqualification</xsl:when>
			<xsl:when test="$code ='REP' ">For Plea and Reparation</xsl:when>
			<xsl:when test="$code ='REV' ">For Review</xsl:when>
			<xsl:when test="$code ='RIN' ">For Return Injunction</xsl:when>
			<xsl:when test="$code ='RJT' ">For Reserved Judgment</xsl:when>
			<xsl:when test="$code ='RLA' ">For Application to Remove from List</xsl:when>
			<xsl:when test="$code ='RPA' ">For Application to Revoke Probation Order</xsl:when>
			<xsl:when test="$code ='RVA' ">For Application for Review</xsl:when>
			<xsl:when test="$code ='SAC' ">For Sentence (at another Court)</xsl:when>
			<xsl:when test="$code ='SCE' ">To Show Cause</xsl:when>
			<xsl:when test="$code ='SCJ' ">For Juror to Show Cause</xsl:when>
			<xsl:when test="$code ='SCS' ">For Solicitor to Show Cause</xsl:when>
			<xsl:when test="$code ='SCW' ">For Witness to Show Cause</xsl:when>
			<xsl:when test="$code ='SCY' ">For Surety to Show Cause</xsl:when>
			<xsl:when test="$code ='SDA' ">For Application to Suspend Driving Disqualification</xsl:when>
			<xsl:when test="$code ='SEN' ">For Sentence</xsl:when>
			<xsl:when test="$code ='SOA' ">For Sentence (Officer to Attend)</xsl:when>
			<xsl:when test="$code ='SPA' ">For Sentence (Prosecution to Attend)</xsl:when>
			<xsl:when test="$code ='SPO' ">For Sentence (Prosecution and Officer to Attend)</xsl:when>
			<xsl:when test="$code ='SPR' ">For Sentence (Prosecution Released)</xsl:when>
			<xsl:when test="$code ='SUB' ">For Surrender to Bail</xsl:when>
			<xsl:when test="$code ='TBK' ">For Trial (Backer)</xsl:when>
			<xsl:when test="$code ='TFL' ">For Trial (Floater)</xsl:when>
			<xsl:when test="$code ='TFW' ">For Trial (First Warning)</xsl:when>
			<xsl:when test="$code ='TIS' ">For Trial of Issue</xsl:when>
			<xsl:when test="$code ='TNW' ">For Trial (No Witnesses)</xsl:when>
			<xsl:when test="$code ='TPH' ">Trial (Part Heard)</xsl:when>
			<xsl:when test="$code ='TPI' ">For Trial of Preliminary Issue</xsl:when>
			<xsl:when test="$code ='TPR' ">For Trial (Priority)</xsl:when>
			<xsl:when test="$code ='TPW' ">For Trial (Previously Warned)</xsl:when>
			<xsl:when test="$code ='TRA' ">For Application to Terminate Restriction Order</xsl:when>
			<xsl:when test="$code ='TRE' ">For Trial (Reserve)</xsl:when>
			<xsl:when test="$code ='TRL' ">For Trial</xsl:when>
			<!-- Added by Kn 3/12/04 -  to support RFC1270 / Rel 3.19 -->
			<xsl:when test="$code ='TRN' ">For Trial</xsl:when>
			<!-- End of Addition -->
			<xsl:when test="$code ='TWC' ">For Trial (Fixed for this Week)</xsl:when>
			<xsl:when test="$code ='XPA' ">For Application (Ex Parte)</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- altOrderSignatory template		-->
	<!-- **************************************** -->
	<doc:template name="util:altOrderSignatory" xmlns="">
		<refpurpose>Creates a slightly different version of order signatory output which appears on Imprisonment, Remand and Young Offender orders</refpurpose>
		<refreturn>
			<para>Order signatory html</para>
		</refreturn>
	</doc:template>
	<xsl:template name="util:altOrderSignatory">
		<hr/>
		<table width="100%">
			<tr>
				<td width="20%"> </td>
				<td width="60%">
					<xsl:choose>
						<xsl:when test="//cs:OrderHeader/cs:SignedBy/cs:CourtOfficer">
							<xsl:text>An Officer of the Crown Court </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>An Judge of the Crown Court </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td width="20%"/>
			</tr>
			<tr>
				<td width="20%"> </td>
				<td width="60%">
					<xsl:text>Signed: </xsl:text>
					<xsl:choose>
						<xsl:when test="//cs:OrderHeader/cs:SignedBy/cs:CourtOfficer">
							<strong>
								<xsl:value-of select="//cs:SignedBy/cs:CourtOfficer/apd:CitizenNameTitle"/>
								<xsl:text> </xsl:text>
								<xsl:call-template name="util:personsFullName">
									<xsl:with-param name="name" select="//cs:SignedBy/cs:CourtOfficer"/>
								</xsl:call-template>
							</strong>
						</xsl:when>
						<xsl:otherwise>
							<strong>
								<xsl:call-template name="util:formalName">
									<xsl:with-param name="name" select="//cs:OrderHeader/cs:SignedBy/cs:Judge"/>
								</xsl:call-template>
							</strong>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td width="20%">
					<xsl:text>Date: </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:SignedDate"/>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ************************************ -->
	<!-- CCN1263 VulnerableVictim Indicator Template  -->
	<!-- ************************************ -->
	<xsl:template name="util:VulnerableVictimIndicatorDefaultText">
		<table>
			<tr width="100%">
				<td>
					<strong>
						<xsl:text>
							Vulnerable Victim Indicator
						</xsl:text>
					</strong>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:text>
						The Victim on this case has not been identified as vulnerable.
					</xsl:text>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="util:VulnerableVictimIndicatorText">
		<table>
			<tr width="100%">
				<td>
					<strong>
						<xsl:text>
							Vulnerable Victim Indicator
						</xsl:text>
					</strong>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:text>
						The Victim on this case has been identified as vulnerable.
					</xsl:text>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ************************************ -->
	<!-- CCN400 Deportation Template  -->
	<!-- ************************************ -->
	<xsl:template name="util:DeportationText">
		<xsl:choose>
			<xsl:when test="//cs:DocumentID/cs:DocumentType != 'AR'">
				<xsl:choose>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'custodial'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This defendant is liable to deportation because the defendant is a foreign national and has received a custodial sentence of 12 months or more.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'suspended'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This defendant meets the requirements for automatic deportation because the defendant has breached a suspended sentence of 12 months or more.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'recommendedDeportation'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This defendant is liable to deportation because the Court recommended deportation regardless of offence or sentence length.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'seriousDrugOffence'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This defendant is liable to deportation because the defendant is a foreign national and has been sentenced to a period of imprisonment for a serious drugs offence.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="//cs:DocumentID/cs:DocumentType = 'AR'">
				<xsl:choose>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'custodial'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This appellant is liable to deportation because the appellant is a foreign national and has received a custodial sentence of 12 months or more.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'suspended'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This appellant meets the requirements for automatic deportation because the appellant has breached a suspended sentence of 12 months or more.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'recommendedDeportation'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This appellant is liable to deportation because the Court recommended deportation regardless of offence or sentence length.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason = 'seriousDrugOffence'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
								Deportation Reason
							</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
								This appellant is liable to deportation because the appellant is a foreign national and has been sentenced to a period of imprisonment for a serious drugs offence.
							</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="util:Deportation">
		<xsl:choose>
			<xsl:when test="//cs:DocumentID/cs:DocumentType = 'AR'">
				<xsl:choose>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'custodial'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This appellant is liable to deportation because the appellant is a foreign national and has received a custodial sentence of 12 months or more.									
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'suspended'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This appellant meets the requirements for automatic deportation because the appellant has breached a suspended sentence of 12 months or more.
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'recommendedDeportation'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This appellant is liable to deportation because the Court recommended deportation regardless of offence or sentence length.
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'seriousDrugOffence'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This appellant is liable to deportation because the appellant is a foreign national and has been sentenced to a period of imprisonment for a serious drugs offence.
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="//cs:DocumentID/cs:DocumentType != 'AR'">
				<xsl:choose>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'custodial'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This defendant is liable to deportation because the defendant is a foreign national and has received a custodial sentence of 12 months or more.							
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'suspended'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This defendant meets the requirements for automatic deportation because the defendant has breached a suspended sentence of 12 months or more.
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'recommendedDeportation'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This defendant is liable to deportation because the Court recommended deportation regardless of offence or sentence length.
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
					<xsl:when test="//cs:DeportationSection/cs:DeportationReasons/cs:Reason = 'seriousDrugOffence'">
						<table>
							<tr width="100%">
								<td>
									<strong>
										<xsl:text>
									Deportation Reason
								</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:text>
									This defendant is liable to deportation because the defendant is a foreign national and has been sentenced to a period of imprisonment for a serious drugs offence.
								</xsl:text>
								</td>
							</tr>
						</table>
						<hr/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ************************************ -->
	<!-- RFS4224 Hate Crime Template -->
	<!-- ************************************ -->
	<xsl:template name="util:HateCrime">
		<h4>
			<u>
				<xsl:text>Hate Crime (enhanced sentence passed under s145 and s146 of the CJA2003)</xsl:text>
			</u>
		</h4>
		<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:HateCrime/cs:Reason">
			<xsl:choose>
				<xsl:when test=".='DISG'">
					<xsl:text>The Court finds that the offence was aggravated because of disability in general</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'DISV'">
					<xsl:text>The Court finds that the offence was aggravated because of disability of the victim.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'RA'">
					<xsl:text>The Court finds that the offence was racially aggravated.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'RARE'">
					<xsl:text>The Court finds that the offence to have been both racially and religiously aggravated.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'RE'">
					<xsl:text>The Court finds that the offence was religiously aggravated.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'SEXO'">
					<xsl:text>The Court finds that the offence was aggravated due to sexual orientation in general.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'SEXOV'">
					<xsl:text>The Court finds that the offence was aggravated due to sexual orientation of the victim.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'TGG'">
					<xsl:text>The Court finds that the offence was aggravated due to transgender issues in general.</xsl:text>
					<br/>
				</xsl:when>
				<xsl:when test=". = 'TGV'">
					<xsl:text>The Court finds that the offence was aggravated due to transgender, or presumed transgender, of the victim.</xsl:text>
					<br/>
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
		<br/>
		<br/>
	</xsl:template>
</xsl:stylesheet>
