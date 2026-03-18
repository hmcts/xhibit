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
		<releaseinfo role="meta">Version 2-4</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Indictment Stylesheet</title>
	<para>File name : indictment-v2-4.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Indictment in html format</para>
			<para>PR57292  - Re-did Title to stop Clipped C</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'4a'" />
<xsl:variable name="stylesheet" select="'indictment-v2-4.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-07-05'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="signedDate" >
	<xsl:call-template name="util:ukdate_mon">
		<xsl:with-param name="inDate" select="//cs:DateSigned" />
	</xsl:call-template>
</xsl:variable>

<!-- Change for CR71 - v2-3 -->
<!-- <xsl:variable name="outputType" select="'Indictment'" />  -->
<xsl:variable name="outputType" select="'Charges'" />
<xsl:variable name="court" select="//cs:IssuingCourtHouse" />
<xsl:variable name="caseNum" select="//cs:Cases/cs:CaseNumber" />
<xsl:variable name="theCrown" select="'THE QUEEN - V - '" />
<xsl:variable name="courtInfo" >
	<xsl:text>In the </xsl:text>
	<xsl:value-of  select="$court/cs:CourtHouseType"/>
	<xsl:text> at </xsl:text>
	<xsl:call-template name="str:to-upper">
		<xsl:with-param name="text" select="$court/cs:CourtHouseName"/>
	</xsl:call-template>
</xsl:variable>
<!-- End Global Variables -->
 	      
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
			
				<!--  ++++++++++  following template prepares the indictment header    ++++++++++++ -->			
				<xsl:apply-templates select="//cs:Defendants" />
				
				<!-- +++++++++ following template controls the processing of each count of the indictment  +++++++++ -->
				<!-- PRE00320 Sort Order of Charges not being carried out correctly -->
				<!--<xsl:apply-templates select="//cs:Charges"/> -->
				<xsl:call-template name="ChargesTemplate" />				
				<!-- End of PRE00320  Change -->
				<xsl:call-template name="orderSignatory" />
				
				<!-- KN 20050517 - CR27 	-->
				<xsl:call-template name="util:copyrightText" />
							
			</body>
		</html>
	</xsl:template>
		
	<!-- create index on offence number -->
	<xsl:key name="charges-by-number" match="cs:Charge" use="cs:CRESToffenceNumber" />
	
	<!-- create index on case number -->
	<xsl:key name="cases-by-number" match="cs:Charge" use="cs:CaseNumber" />
	
	<!-- **************************************** -->
	<!-- ChargesTemplate				-->
	<!-- **************************************** -->
	
	<doc:template name="ChargesTemplate" xmlns="">
		<refpurpose>Controls the output of the body of the indictment report.</refpurpose>
		<refdescription>
			<para>Uses an index on the charges based on the CRESToffenceNumber, in order to be able to extract
		a  list of unique charges. The list is sorted by CRESToffenceNumber. The sorted list is then iterated through to output the details of each count 
		in the indictment body.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Note: Uses the template defendantOnCharge to format 	the defendants against each count - passing in the charge number and 	CRN as parameters.</para>
					<param>rocessing altered for PRE00320 to run through all Charges at once rather than for each defendant.</param>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	
	<!-- PRE00320 Sort Order of Charges not being carried out correctly -->
	<!-- <xsl:template match="//cs:Charges">  -->

	<!--	<xsl:for-each select="cs:Charge[count(. | key('charges-by-number', cs:CRESToffenceNumber)[1]) = 1]"> -->
	
	<xsl:template name="ChargesTemplate">
		
		<xsl:for-each select="//cs:Defendant/cs:Charges/cs:Charge[count(.| key('charges-by-number', cs:CRESToffenceNumber)[1]) = 1]">
				
	<!-- End of PRE00320  Change -->

			<xsl:sort select="cs:CRESToffenceNumber" data-type="number" order="ascending"/>
			<!-- sorted in unique charge number sequence -->
			<!-- do the charge count number -->
			<h3>
				<xsl:text>Count </xsl:text>
				<!-- PRE00320 Sort Order of Charges not being carried out correctly -->
				<!--	<xsl:number count="cs:Charge" format="1" level="any"/>  -->
				<xsl:value-of  select="cs:CRESToffenceNumber"/>
				<!-- End of PRE00320  Change -->
			</h3>
			<xsl:variable name="charge" select="cs:CRESToffenceNumber" />
			<xsl:variable name="CRN" select="cs:CRN" />

			<table WIDTH="100%" BORDER="1">
				<tr>
					<td WIDTH="25%" VALIGN="TOP">
						<xsl:text>STATEMENT OF OFFENCE</xsl:text>
					</td>
					<td WIDTH="75%">
						<xsl:value-of select="key('charges-by-number',$charge)/cs:OffenceStatement"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>PARTICULARS OF OFFENCE</xsl:text>
					</td>
					<td>
						<!-- now call a template to obtain the defendants associated with each charge -->
						<xsl:call-template name="defendantsOnCharge">
							<xsl:with-param name="chargeNumber" select="$charge"/>
							<xsl:with-param name="crn" select="$CRN"/>							
						</xsl:call-template>
						<!-- offence particulars are not currently available. un comment the following two 
						     statements when they becom available -->
						<!--
						<BR />
						<xsl:value-of select="key('charges-by-number',$charge)/cs:OffenceParticulars"/>
						-->
					</td>
				</tr>
			</table>
			<br />
		</xsl:for-each>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- defendantsOnCharge Template	-->
	<!-- **************************************** -->
		
	<doc:template name="defendantsOnCharge" xmlns="">
		<refpurpose>Creates the defendant output for each count in the indictment report body.</refpurpose>
		<refdescription>
			<para>Uses the charge number passed in as a parameter to identify all the 
			  		defendants for that particular count, and output their details.
			  </para>
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>chargeNumber</term>
					<listitem>
						<para>Charge number for which we want the list of defendants.
								  This is matched against Charges/Charge/CRESToffenceNumber</para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>crn</term>
					<listitem>
						<para>CRN applicable to the count. </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
	</doc:template>

	<xsl:template name="defendantsOnCharge">
		<xsl:param name="chargeNumber" />
		<xsl:param name="crn" />
		<table width="100%">
		<xsl:for-each select="//cs:Defendant/cs:PersonalDetails[../cs:Charges/cs:Charge/cs:CRESToffenceNumber = $chargeNumber]">
			<tr>
				<td width="35%">
					<xsl:variable name="defendant">
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="./cs:Name"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:call-template name="str:to-upper" >
						<xsl:with-param name="text" select="$defendant"/>
					</xsl:call-template>
				</td>
				<td width="40%">
					<xsl:text>CRN: </xsl:text>
<!--					<xsl:value-of select="$crn" />-->
<!-- The the above has been replaced by the below, it is assumed that the first CRN only is noted, if this is not the case then the whole structure of this will need to be altered to run through each CRN -->
<!--					<xsl:value-of select="../cs:Charges/cs:Charge/cs:CRN"/>-->
					<table>
						<xsl:for-each select="../cs:Charges/cs:Charge">
						<tr>
							<td width="100%">
								<xsl:value-of select="./cs:CRN"/>
							</td>
						</tr>
						</xsl:for-each>
					</table>
				</td>
				<!-- Addition of PTI URN for CR49 -->
				<td width="25%">
					<xsl:text>PTI URN: </xsl:text>
					<xsl:value-of select="../cs:URN" />
				</td>
			</tr>
		</xsl:for-each>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Defendants Template			-->
	<!-- **************************************** -->

	<doc:template name="Defendants" xmlns="">
		<refpurpose>Prepares the the data for the Indictment document header</refpurpose>
		<refdescription>Prepares the document header including 
	 a list of all defendants indicted, the case numbers and any 
	 joinder information.</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the template 'header' to actually output the header details.
				Three parameters are passed across: a list of defendants, a joinder flag and a list of cases.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:personsFullName to format the defendant names.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the index created on case numbers ('cases-by-numbers')to create a list 
	      of uniques case numbers.</para>
			</listitem>
			<listitem>
				<para>Note: Indictment/@IsJoinderIndictment='yes'indicates that there is joinder information to be shown.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:Defendants">
	<!-- prepares the indictment header info from the Defendants -->
		<xsl:variable name="allDefendants"> 
			<xsl:for-each select="./cs:Defendant/cs:PersonalDetails">
				<xsl:call-template name="util:personsFullName">
					<xsl:with-param name="name" select="./cs:Name"/>
				</xsl:call-template>
				<xsl:if test="not(position() = last())" >
					<xsl:text> &amp; </xsl:text> 
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		
		
		<xsl:variable name="allCasenums"> 
			<xsl:for-each select="//cs:Charges/cs:Charge[count(. | key('cases-by-number', cs:CaseNumber)[1]) = 1]">
				<xsl:sort select="cs:CaseNumber" data-type="text" order="ascending"/>
					<xsl:value-of select="cs:CaseNumber"/>
					<br />
			</xsl:for-each>
		</xsl:variable>
		
		<xsl:variable name="joinder">
			<xsl:choose>
				<xsl:when test="/cs:Indictment/@IsJoinderIndictment='yes'">
					<xsl:choose>	
						<xsl:when test="count(//cs:Charges/cs:Charge[count(. | key('cases-by-number', cs:CaseNumber)[1]) = 1]) &gt; 1">
							<xsl:text>Leave to join indictments granted by </xsl:text>
							<xsl:call-template name="util:judiciaryName">
								<xsl:with-param name="judge" select="//cs:JoinderIndictment/cs:PreferedByJudge/cs:Judge" />
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="util:ukdate_fullMonth">
								<xsl:with-param name="inDate" select="//cs:JoinderIndictment/cs:DateJoined" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Indictments stayed by </xsl:text>
							<xsl:call-template name="util:judiciaryName">
								<xsl:with-param name="judge" select="//cs:JoinderIndictment/cs:PreferedByJudge/cs:Judge" />
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="util:ukdate_fullMonth">
								<xsl:with-param name="inDate" select="//cs:JoinderIndictment/cs:DateJoined" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>no</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="not(string-length($allCasenums) =0)">
				<xsl:call-template name="header">
					<xsl:with-param name="defendant" select="$allDefendants"/>
					<xsl:with-param name="joined" select="$joinder"/>
					<xsl:with-param name="caseNum" select="$allCasenums"/> 
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="header">
					<xsl:with-param name="defendant" select="$allDefendants"/>
					<xsl:with-param name="joined" select="$joinder"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- header Template					-->
	<!-- **************************************** -->
	
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the header output for Indictment Report.</refpurpose>
		<refdescription>
			<para>If joinder information is avalable then outputs this first before the standard header information
			  of Court details, case number list and the list of defendants being indicted/</para>
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>defendant</term>
					<listitem>
						<para>List of defendants being indicted. </para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>joinded</term>
					<listitem>
						<para>joinder flag - set to 'no' if joinder does not apply, otherwise text of joinder. </para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>caseNum</term>
					<listitem>
						<para>List of case numbers from the charges (default is the case number from the overall indictment). </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Indictment report header.</para>
		</refreturn>
	</doc:template>

	<xsl:template name="header">
	<!-- processes the header information - constructs the initial header information for the 
	     indictment of each defendant.
		 Parameter 1 : defendant name(s)
		 Parameter 2 : joinder flag - set to 'no' if joinder does not apply, otherwise text of joinder
		 Parameter 3 : case numbers from the charges (default is the case number from the overall indictment
		 -->
		<xsl:param name="defendant"/>
		<xsl:param name="joined" />
		<xsl:param name="caseNum" select="/cs:Indictment/cs:Cases/cs:CaseNumber"/>
		<xsl:variable name="ucDefendant">
			<xsl:call-template name="str:to-upper">
				<xsl:with-param name="text" select="$defendant"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:if test="not ($joined = 'no')" >
			<h3>
				<xsl:copy-of select="$joined"/>
			</h3>
		</xsl:if>
		<center><h1><xsl:value-of select="$outputType" /></h1></center>
		<table class="emphasis" WIDTH="100%">
			<tr>
				<td WIDTH="100%" ALIGN="RIGHT">
					<xsl:text>Case No: </xsl:text>
					<xsl:copy-of select="$caseNum"/>
				</td>
			</tr>
		</table>
		<h2>
			<xsl:value-of select="$courtInfo" />
		</h2>
		<h4>
			<xsl:value-of select="$theCrown" />
			<xsl:value-of select="$ucDefendant"/>
		</h4>
		<h2>		
			<xsl:choose>
				<xsl:when test="count(//cs:Defendant)&gt;1">
					<xsl:text> are </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> is </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>charged as follows:</xsl:text>
		</h2>		
	</xsl:template>

	<!-- **************************************** -->
	<!-- orderSignatory Template			-->
	<!-- **************************************** -->
	
	<doc:template name="orderSignatory" xmlns="">
		<refpurpose>Outputs the signatory information for the order.</refpurpose>
	</doc:template>
	
	<xsl:template name="orderSignatory">
		<xsl:if test="//cs:Signed = 'yes'">
			<table width="100%">
				<tr>
					<td width="20%" />
					<td width="80%">
						<xsl:text>An Officer of the Crown Court </xsl:text>
	 					<br/>							
						<xsl:text>Signed: </xsl:text>
						<strong>
							<xsl:value-of select="//cs:SigningOfficer/cs:Name/apd:CitizenNameTitle"/>
							<xsl:text> </xsl:text>
							<xsl:call-template name="util:personsFullName">
								<xsl:with-param name="name" select="//cs:SigningOfficer/cs:Name"/>
							</xsl:call-template>
						</strong>
					</td>
				</tr>
				<tr>
					<td> </td>
					<td >
						<xsl:text>Date: </xsl:text>
						<strong>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="//cs:DateSigned" />
							</xsl:call-template>
						</strong>
					</td>
					<td>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	
	
</xsl:stylesheet>
