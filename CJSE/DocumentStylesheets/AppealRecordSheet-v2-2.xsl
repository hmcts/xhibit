<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                                 +
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
				  
	<doc:reference xmlns="">
    	<referenceinfo>
			<releaseinfo role="meta">Version 2-2</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Appeal Record Sheet Stylesheet</title>
		<para>File name : AppealRecordSheet-v2-2.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Appeal Record Sheet in html format</para>
			</section>
		</partintro>
	</doc:reference>				  
<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" /> 
<xsl:include href="gcsUtility.xsl" />			      


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'2'" />
<xsl:variable name="stylesheet" select="'AppealRecordSheet-v2-2.xsl'" />
<xsl:variable name="last-modified-date" select="'2004-07-26'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="orderDate" >
	<xsl:call-template name="util:ukdate_mon">
		<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate" />
	</xsl:call-template>
</xsl:variable>
<!-- end Global Variables -->

	      
<xsl:output method="html" indent="yes"/>

	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate" />
			</head>
			<body>

				<!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
				<xsl:call-template name="header" /> 
				<xsl:call-template name="appellant" />
				<xsl:call-template name="respondent" /> 
				<xsl:call-template name="courtReporting" /> 
				<xsl:call-template name="judiciary" /> 
				<xsl:call-template name="bailOrCustody" />
				<xsl:call-template name="hearings" />
				<xsl:call-template name="counts" /> 
				<xsl:call-template name="otherOrders" /> 			
			
			</body>
		</html>
	</xsl:template>
	
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the Report Header information - including the Appellant and court details.</refpurpose>
	</doc:template>
	
	<xsl:template name="header">
	<!-- processes the Header information - appellant and court -->
	<xsl:variable name="personal" select="//cs:RecordSheetHeader/cs:Defendant/cs:PersonalDetails" />
		<h2>
			<center>
				<xsl:value-of select="'Appeal Record Sheet'" />
			</center>
		</h2>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="70%">
					<xsl:text>Appellant </xsl:text>
					<xsl:if test="$personal/cs:Sex">
						<xsl:text>(</xsl:text>
						<xsl:value-of select="$personal/cs:Sex"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
				</td>
				<td WIDTH="30%">
					<xsl:text>Appellant No.</xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:CaseNumber"/>
				</td>
			</tr>
			
			<tr>
				<td>
					<xsl:call-template name="util:surnameFirstUC">
						<xsl:with-param name="personalDetails" select="$personal"/>
					</xsl:call-template>
				</td>
				<td>
					<xsl:text>Date of Birth: </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="$personal/cs:DateOfBirth/apd:BirthDate"/>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:text>Address</xsl:text>
				</td>
				<td>
					<!-- Added URN for CR49 -->
					<xsl:text>PTI Unique Ref: </xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:URN" />
				</td>
			</tr>
			<tr>
				<td>
					<xsl:call-template name="util:address_oneline">
						<xsl:with-param name="personalDetails" select="$personal"/>
					</xsl:call-template>
				</td>
				<td>
				</td>
			</tr>
		</table>
		<hr />
	
		<!-- now give out the court information -->
		<xsl:text>Before the </xsl:text>
		<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseType"/>
		<xsl:text> at </xsl:text>
		<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseName"/>
		<br />
		<xsl:if test="//cs:RecordSheetHeader/cs:MagistratesCourt">
			<xsl:value-of select="//cs:RecordSheetHeader/cs:MethodOfInstigation"/>
			<xsl:text> from a decision of </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName"/>
			<br />
			<xsl:if test="//cs:RecordSheetHeader/cs:DateOfInstigation" >
				<xsl:text>on </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		<xsl:if test="//cs:OriginalSentenceOrOrderDate">
			<xsl:text>Date of original sentence/order : </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:OriginalSentenceOrOrderDate" />
			</xsl:call-template>
		</xsl:if>
		<hr />
	</xsl:template>

	<!-- create hearings index on hearing date -->
	<xsl:key name="hearings-by-date" match="cs:Hearing" use="cs:HearingDate" />	
	
	<doc:template name="appellant" xmlns="">
		<refpurpose>Outputs the details of the Appellant's defence team.</refpurpose>
		<refdescription>
		  <para>Firstly iterates through the Advocates (if any), and then iterates through  
		        any Solicitors there might be.</para>
		  <para>Uses the template util:formalName to format the individuals name for display.</para>
		  <para>
		  	<emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Note: The current version of schemas does not allow for Solicitor information to be 
			      present. It is planned that a future version (2.1 or later) of the schemas
			      will fix this by using Representative Structure
			</para>
		  </listitem>
		  <listitem>
		  	<para>Note: The current version of schemas does not allow for a status code to be associated 
			      with the advocate information. It is planned that a future version (2.1 or later) of the schemas will fix this
			</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="appellant" >
	<!-- this template outputs the defence team -->
		<xsl:variable name="appellant" select="//cs:AppellantAdvocates"/>
		<h4>
			<xsl:text>Appellant</xsl:text>
		</h4>
		
		<!-- First do the advocates -->
		<xsl:if test="$appellant/cs:AppellantAdvocate" >
			<strong>
				<xsl:text>Advocate</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="$appellant/cs:AppellantAdvocate">
				<tr>
					<td width="50%">
						<xsl:call-template name="util:formalName">
							<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name" />
						</xsl:call-template>
					</td>
					<td width="15%">
						<!-- A status code should go here representing barrister, junior aslone etc -->
						<!-- info not yet in schema -->
					</td>
					<td width="25%">
						<xsl:call-template name="trialDateRange">
							<xsl:with-param name="startDate" select ="./cs:StartDate"/>
							<xsl:with-param name="endDate" select ="./cs:EndDate"/>							
						</xsl:call-template>
					</td>
					<td width="10%">
						<xsl:value-of select="key('hearings-by-date', ./cs:StartDate)/@HearingType" />
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		
		<!-- R6.0 Kevin Nicholson -->
		<!-- Second lot of advocats -->
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:Counsel/cs:Advocate" >
			<strong>
				<xsl:text>Advocate</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:Counsel/cs:Advocate">
				<tr>
					<td width="50%">
						<xsl:call-template name="util:formalName">
							<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name" />
						</xsl:call-template>
					</td>
					<td width="15%">
						<!-- A status code should go here representing barrister, junior aslone etc -->
						<!-- info not yet in schema -->
					</td>
					<td width="25%">
						<xsl:call-template name="trialDateRange">
							<xsl:with-param name="startDate" select ="./cs:StartDate"/>
							<xsl:with-param name="endDate" select ="./cs:EndDate"/>							
						</xsl:call-template>
					</td>
					<td width="10%">
						<xsl:value-of select="key('hearings-by-date', ./cs:StartDate)/@HearingType" />
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		
		<!-- Now the solicitors 
		     This whole section of code needs to be reworked when V2.1 schemas become available.
		-->
		<!--
		<xsl:if test="$appellant/cs:AppellantAdvocate/cs:Solicitor" >
			<strong>
				<xsl:text>Solicitor</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:AppellantAdvocate/cs:Solicitor/cs:Party">
				<tr>
					<td width="50%">
						<xsl:choose>
						<xsl:when test="./cs:Person">
							<xsl:call-template name="util:formalName">
								<xsl:with-param name="name" select="./cs:Person/cs:PersonalDetails/cs:Name" />
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="./cs:Organisation">
							<xsl:value-of  select="./cs:Organisation/cs:OrganisationName" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>In Person</xsl:text>
						</xsl:otherwise>
						</xsl:choose>
					</td>
					<td width="15%">
						start comment // should be Rep Ord here ~ no info in schema for this // end comment required here
					</td>
					<td width="25%">
						<xsl:call-template name="trialDateRange">
							<xsl:with-param name="startDate" select ="../cs:StartDate"/>
							<xsl:with-param name="endDate" select ="../cs:EndDate"/>
						</xsl:call-template>
						<xsl:if test="not (../cs:EndDate)" >
							<xsl:text> onwards </xsl:text>
						</xsl:if>
					</td>
					<td width="10%">
						start comment // notsure if there should be anything here for solicitors // end comment required here
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		-->
	</xsl:template>
	
	<doc:template name="respondent" xmlns="">
		<refpurpose>Outputs the details of the Respondent's advocates.</refpurpose>
		<refdescription>
		  <para>Shows which prosecution organisation is acting as the respondent, and 
		        then iterates through the Advocates for the Respondent.</para>
		  <para>Uses the template util:formalName to format the individuals name for display.</para>				
		  <para>
		  <emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Note: This section will need to be changed when Version 2.1 of the schemas.
			      using Representative Structure, comes into force
			</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="respondent" >
	<!-- this template outputs the prosecuting team -->
		<xsl:variable name="respondent" select="//cs:Respondents"/>
		<h4>
			<xsl:text>Respondent : </xsl:text>
		<xsl:value-of select="$respondent/@ProsecutingAuthority"/>
		</h4>		
		<xsl:if test="$respondent/cs:Advocate" >
			<strong>
				<xsl:text>Advocate</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:Respondents/cs:Advocate">
				<tr>
					<td width="50%">
						<xsl:call-template name="util:formalName">
							<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name" />
						</xsl:call-template>
					</td>
					<td width="15%">
						<xsl:text>(</xsl:text>
						<xsl:text>Counsel</xsl:text>
						<xsl:text>)</xsl:text>
					</td>
					<td width="25%">
						<xsl:call-template name="trialDateRange">
							<xsl:with-param name="startDate" select ="./cs:StartDate"/>
							<xsl:with-param name="endDate" select ="./cs:EndDate"/>
						</xsl:call-template>
					</td>
					<td width="10%">
						<!-- nothing to go here i believe for prosecuting advocates -->
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		<hr />
	</xsl:template>
	
	<doc:template name="courtReporting" xmlns="">
		<refpurpose>Outputs the details of the court reporting firms and their associated partners.</refpurpose>
		<refdescription>
		  <para>Iterates through the court reporting firms showing for each one the name of the firm,
		        followed by the names of court reporters associated with that firm.</para>
		  <para>Uses the template util:formalName to format the individuals name for display.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="courtReporting" > 
	<!-- this template outputs the court reporting firms and their associated reporters -->
		<xsl:for-each select="//cs:CourtReportingFirm">
			<h4>
				<xsl:text>Court Reporting Firm</xsl:text>
			</h4>
			<xsl:value-of select="./cs:Firm/cs:OrganisationName" />
			<xsl:if test="./cs:Reporter">
				<h4>
					<xsl:text>Court Reporter</xsl:text>
				</h4>
				<table width="90%" >
					<xsl:for-each select="./cs:Reporter">
						<tr>
						<td width="50%">
							<xsl:call-template name="util:formalName">
								<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name" />
							</xsl:call-template>
						</td>
						<td width="15%">
							<xsl:text>(</xsl:text>
							<xsl:value-of select="./cs:OperatorType"/>
							<xsl:text>)</xsl:text>
						</td>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select ="./cs:StartDate"/>
								<xsl:with-param name="endDate" select ="./cs:EndDate"/>
							</xsl:call-template>
						</td>
						<td width="10%">
						</td>
					</tr>
					</xsl:for-each>
					<tr />
				</table>
			</xsl:if>
		</xsl:for-each>
		<hr />
	</xsl:template>
			
	
	<doc:template name="judiciary" xmlns="">
		<refpurpose>Outputs the details of the Judges and Justices.</refpurpose>
		<refdescription>
		  <para>Iterates through the Judges and any Justices there might be</para>
		  <para>Uses the template util:judiciaryName to format the individuals name for display.</para>
		</refdescription>
	</doc:template>	
		
	<xsl:template name="judiciary" >
	<!-- this template outputs the Judges and Justices -->
		<xsl:if test="//cs:Judiciary/cs:Judge">
			<h4>
				<xsl:text>Judge</xsl:text>
			</h4>
			<table width="90%" >
				<xsl:for-each select="//cs:Judiciary/cs:Judge">
					<tr>
					<td width="50%">
						<xsl:call-template name="util:judiciaryName">
							<xsl:with-param name="judge" select="." />
						</xsl:call-template>
					</td>
					<td width="15%">
					</td>
					<td width="25%">
						<xsl:call-template name="trialDateRange">
							<xsl:with-param name="startDate" select ="../cs:StartDate"/>
							<xsl:with-param name="endDate" select ="../cs:EndDate"/>
						</xsl:call-template>
					</td>
					<td width="10%">
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		
		<!-- now do the justices -->
		<xsl:if test="//cs:Judiciary/cs:Justice">
			<h4>
				<xsl:text>Justice</xsl:text>
			</h4>
			<table width="90%" >
				<xsl:for-each select="//cs:Judiciary/cs:Justice">
					<tr>
					<td width="50%">
						<xsl:call-template name="util:judiciaryName">
							<xsl:with-param name="judge" select="." />
						</xsl:call-template>
					</td>
					<td width="15%">
					</td>
					<td width="25%">
						<xsl:call-template name="trialDateRange">
							<xsl:with-param name="startDate" select ="../cs:StartDate"/>
							<xsl:with-param name="endDate" select ="../cs:EndDate"/>
						</xsl:call-template>
					</td>
					<td width="10%">
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		
		<hr />
	</xsl:template>
	
	<doc:template name="bailOrCustody" xmlns="">
		<refpurpose>Outputs the details of the bail or custody status.</refpurpose>
		<refdescription>
		  <para>Shows the bail or custody status at the various stages of the process i.e when appeal lodged,
		        at start of hearing, after bench warrant executed and put back for sentence.</para>
		<para>
		  <emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Note: This section may need to be changed if the BailStatusStructure, which was originally 
			      proposed for Version 2.x of the schemas, comes into force.
			</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="bailOrCustody" >
	<h4>
		<xsl:text>Bail/Custody Status</xsl:text>
	</h4>
	<table width="100%" >
		<tr>
			<td width="25%">
				<xsl:text>When appeal lodged : </xsl:text>
			</td>
			<td width="75%">
				<xsl:value-of  select="/cs:AppealRecordSheet/cs:BailStatusWhenAppealLodged" />
			</td>
		</tr>
		<tr>
			<td>
				<xsl:text>At start of hearing : </xsl:text>
			</td>
			<td>
				<xsl:value-of  select="/cs:AppealRecordSheet/cs:BailStatusAtStartOfHearing" />
			</td>
		</tr>		
		<tr>
			<td>
				<xsl:text>After Bench Warrant Executed : </xsl:text>
			</td>
			<td>
				<xsl:value-of  select="/cs:AppealRecordSheet/cs:BailStatusAfterBenchWarrantExecuted" />
			</td>
		</tr>
		<tr>
			<td >
				<xsl:text>Put back for sentence : </xsl:text>
			</td>
			<td>
				<xsl:value-of  select="/cs:AppealRecordSheet/cs:BailStatusPutBackForSentence" />
			</td>
		</tr>
		<tr />
	</table>
	<hr />
	</xsl:template>
	
	<doc:template name="hearings" xmlns="">
		<refpurpose>Outputs the hearing details along with key dates.</refpurpose>
		<refdescription>
		  <para>Iterates through the hearing details showing the start date and end date(if available) for each.
		        If the decision given date and/or the date of the sentence/order made dates are available then these
				are also shown.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="hearings" >
	<!-- this template outputs the hearing information along with key dates -->
		<table width="50%">
			<xsl:for-each select="//cs:Hearings/cs:Hearing">
				<tr>
					<td width="50%">
						<xsl:value-of select="./cs:HearingDescription"/>
						<xsl:text> on </xsl:text>
					</td>
					<td width="50%">
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="./cs:HearingDate"/>
						</xsl:call-template>
						<xsl:if test="./cs:HearingEndDate">
							<xsl:text> to </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="./cs:HearingEndDate"/>
							</xsl:call-template>
						</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
			<xsl:if test="/cs:AppealRecordSheet/cs:DecisionGivenDate">
				<tr>
					<td>
					<xsl:text>Decision given on </xsl:text>
					</td>
					<td>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:DecisionGivenDate"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
			
			<xsl:if test="/cs:AppealRecordSheet/cs:SentenceOrOrderDate">
				<tr>
					<td>
						<xsl:text>Sentence/Order made on </xsl:text>
					</td>
					<td>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentenceOrOrderDate"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
		</table>
		<hr />
	</xsl:template>
	
	
	<doc:template name="counts" xmlns="">
		<refpurpose>Outputs the information relating to charges, pleas etc.</refpurpose>
		<refdescription>
		  <para>Iterates through the Offences showing the appeal type and result,the original sentence and the 
		        decision of the Crown Court.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="counts">
	<!-- outputs information about the charges, pleas etc  -->
	<table width="100%">
		<tr>
			<td width="5%">
				<strong>
					<xsl:text>No</xsl:text>
				</strong>
			</td>
			<td width="25%">
				<strong>
					<xsl:text>Offence</xsl:text>
				</strong>
			</td>
			<td width="25%">
				<strong>
					<xsl:text>Appeal Type and Result</xsl:text>
				</strong>
			</td>
			<td width="45%">
				<strong>
					<xsl:text>Original Sentence/Order (italics) and</xsl:text>
					<br />
					<xsl:text> Crown Court Decision</xsl:text>
				</strong>
			</td>
		</tr>
		<xsl:for-each select="/cs:AppealRecordSheet/cs:Offences/cs:Offence">
			<xsl:variable name="offencenumber" select="./cs:OffenceNumber" />
			<tr>
				<td valign="top">
					<xsl:value-of select="$offencenumber" />
				</td>
				<td valign="top">
					<xsl:value-of select="./cs:Offence" />
					<xsl:variable name="crn"
								  select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:CRN[../@IndictmentCountNumber = $offencenumber]" />
					<xsl:if test="$crn" >
						<br />
						<xsl:text>CRN: </xsl:text>					
						<xsl:value-of select="$crn" />
					</xsl:if>
				</td>
				<td valign="top">
					<xsl:value-of select="./cs:AppealType" />
					<br />
					<xsl:value-of select="./cs:AppealResult" />
				</td>
				<td valign="top">
					<i>
						<xsl:value-of select="./cs:OriginalSentenceOrOrder" />
					</i>
					<br />
					<xsl:value-of select="./cs:CrownCourtDecision" />
				</td>
			</tr>
		</xsl:for-each>
		
	</table>
	<table width="100%">
		<tr>
			<td width="95%">
				<strong>
					<xsl:text>Other CRNs</xsl:text>
				</strong>
			</td>	
		</tr>
		<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:CRN[not(../@ChargeType = 'appeal')]" >
			<tr>
				<td>
					<xsl:text>CRN: </xsl:text>					
					<xsl:value-of select="." />
				</td>
			</tr>
			
		</xsl:for-each>
	</table>
	
	</xsl:template> 			

	<doc:template name="otherOrders" xmlns="">
		<refpurpose>Outputs info for any other orders.</refpurpose>
		<refdescription>
		  <para>Iterates through the TotalSentences/OtherOrders showing the orders found.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="otherOrders">
	<!-- outputs information about any other orders  -->
		<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:Term">
			<hr />
			<strong>
			<xsl:text>Total Sentence</xsl:text>
			<br />
			<xsl:call-template name="util:decodeDuration">
				<xsl:with-param name="duration" select="/cs:AppealRecordSheet/cs:TotalSentence/cs:Term" />
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="str:to-lower">
				<xsl:with-param name="text" select="/cs:AppealRecordSheet/cs:TotalSentence/cs:Term/@TermType" />
			</xsl:call-template>
			</strong>
			<hr />				
		</xsl:if>
		<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders">
			<strong>
			<xsl:text>Other Orders</xsl:text>
			<br />
			<xsl:for-each select="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
				<xsl:value-of select="." />
				<br />	
			</xsl:for-each>
			</strong>
			<hr />				
		</xsl:if>
	</xsl:template> 

<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~  called internal templates ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
	<doc:template name="trialDateRange" xmlns="">
		<refpurpose>Used internally to format the trial start and end dates.</refpurpose>
		<refdescription> 
		  <para>Called from a number of places to show the start and end dates which apply.</para>
		  <para>
		  	<emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Dates to be displayed are in the form dd-mon-yyyy i.e 27-Jun-2003</para>
		  </listitem>
		  <listitem>
		  	<para>Start date is always displayed</para>
		  </listitem>
		  <listitem>
		  	<para>End date is only displayed if it is different to the start date following the word 'to' i.e. to 28-Jun-2003</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
		
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>startDate</term>
					<listitem>
						<para>The start date for the date range </para>
					</listitem>
				</varlistentry>
				<varlistentry>
					<term>endDate</term>
					<listitem>
						<para>The end date for the date range </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Formatted date range</para>
		</refreturn>
	</doc:template>
			
	<xsl:template name="trialDateRange">
	<!-- Outputs the start date and the following : -->
	<!-- if end date same as start date then nothing -->
	<!-- if end date different to start date then the end date -->
	<!-- Dates are reformatted to dd-mon-yyyy format on output -->
	<!-- Params:                                               -->
	<!-- 1. Start Date  -->
	<!-- 2 End Date     -->
		<xsl:param name="startDate" />
		<xsl:param name="endDate"   />
		<xsl:variable name="result">
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="$startDate"/>
			</xsl:call-template>
			<xsl:text>  </xsl:text>
			<xsl:choose>
				<xsl:when test="$endDate">
					<xsl:if test="not( string($startDate) = string($endDate))">
						<xsl:text> to </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="$endDate"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:copy-of select="$result"/>
	</xsl:template>

	
</xsl:stylesheet>
