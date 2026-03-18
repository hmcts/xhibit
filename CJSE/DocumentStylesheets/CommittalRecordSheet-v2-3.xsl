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
				  
	<doc:reference xmlns="">
    		<referenceinfo>
			<releaseinfo role="meta">Version 2-3</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Committal Record Sheet Stylesheet</title>
		<para>File name : xCommittalRecordSheet-v2-3.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Committal Record Sheet in html format</para>
			</section>
		</partintro>
	</doc:reference>
				  
<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" /> 
<xsl:include href="gcsUtility.xsl" />			      


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'3'" />
<xsl:variable name="stylesheet" select="'CommittalRecordSheet-v2-3.xsl'" />
<xsl:variable name="last-modified-date" select="'2004-10-18'" />
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

	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate" />
			</head>
			<body>

				<!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
				<xsl:call-template name="header" /> 
				<xsl:call-template name="defence" />
				<xsl:call-template name="prosecution" /> 
				<xsl:call-template name="courtReporting" /> 
				<xsl:call-template name="judiciary" /> 
				<xsl:call-template name="bailOrCustody" />
				<xsl:call-template name="hearings" />
				<xsl:call-template name="associatedCases" />
				<xsl:call-template name="counts" /> 
				<xsl:call-template name="otherOrders" /> 			
				<!-- kn 20041018 - CR77 - replaced BreachOrders with Breach Template from Trial Recordsheet 
				<xsl:call-template name="breachOrders" /> 					-->
				<xsl:call-template name="breach" /> 			

			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- header Template					-->
	<!-- **************************************** -->
	
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the Report Header information - including the Defendant and court details.</refpurpose>
	</doc:template>
	
	<xsl:template name="header">
		<!-- processes the Header information - defendant and court -->
		<xsl:variable name="personal" select="//cs:RecordSheetHeader/cs:Defendant/cs:PersonalDetails" />
		<h2>
			<center>
				<xsl:value-of select="'Committal Record Sheet'" />
			</center>
		</h2>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="70%">
					<xsl:text>Defendant </xsl:text>
					<xsl:if test="$personal/cs:Sex">
						<xsl:text>(</xsl:text>
						<xsl:value-of select="$personal/cs:Sex"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
				</td>
				<td WIDTH="30%">
					<xsl:text>Defendant No.</xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:CaseNumber"/>
					<xsl:text>-1</xsl:text>
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
			<xsl:text> for sentence to be dealt with from </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName"/>
			<br />
			<xsl:text>on </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
			</xsl:call-template>
		</xsl:if>
		<hr />
	</xsl:template>


	<!-- create hearings index on hearing date -->
	<xsl:key name="hearings-by-date" match="cs:Hearing" use="cs:HearingDate" />
	
	<!-- **************************************** -->
	<!-- defenceTemplate				-->
	<!-- **************************************** -->
	
	<doc:template name="defence" xmlns="">
		<refpurpose>Outputs the details of the Defendant's defence team.</refpurpose>
		<refdescription>
		  <para>Firstly iterates through the Advocates (if any), and then iterates through  
		        any Solicitors there might be.</para>
		  <para>Uses the template util:formalName to format the individuals name for display.</para>
		  <para>
		  	<emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Note: Version 2.1 of the schemas is currently planned to use Representative Structure, which will cover both Advocates and Solicitors.
			     Therefore this code will need to change with that release of the schemas.
			</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="defence" >
	<!-- this template outputs the defence team -->
		<xsl:variable name="defence" select="//cs:DefenceAdvocates"/>
		<h4>
			<xsl:text>Defence</xsl:text>
		</h4>
		
		<!-- First do the advocats -->
		<xsl:if test="$defence/cs:DefenceAdvocate/cs:Advocate" >
			<strong>
				<xsl:text>Advocate</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:DefenceAdvocate/cs:Advocate">
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
		<!-- Second lot of  advocats -->
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


		
		<!-- Now the solicitors -->
		<xsl:if test="$defence/cs:DefenceAdvocate/cs:Solicitor" >
			<strong>
				<xsl:text>Solicitor</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:DefenceAdvocate/cs:Solicitor/cs:Party">
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
						<!-- should be Rep Ord here ~ no info in schema for this -->
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
						<!-- notsure if there shouild be anything here for solicitors -->
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		
				<!-- Release 6.0 Kevin Nicholson -->
		<!-- BtoF Work - also need to check Defendant/Councel/Solicitor -->		
		<!-- Now the solicitors -->
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:Counsel/cs:Solicitor" >
			<strong>
				<xsl:text>Solicitors</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:Counsel/cs:Solicitor">
				<tr>
					<td width="50%">
						<xsl:choose>
						<xsl:when test="./cs:Party/cs:Person">
							<xsl:call-template name="util:formalName">
								<xsl:with-param name="name" select="./cs:Party/cs:Person/cs:PersonalDetails/cs:Name" />
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="./cs:Party/cs:Organisation">
							<xsl:value-of  select="./cs:Party/cs:Organisation/cs:OrganisationName" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>In Person</xsl:text>
						</xsl:otherwise>
						</xsl:choose>
					</td>
					<td width="15%">
						<!-- should be Rep Ord here ~ no info in schema for this -->
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
						<!-- notsure if there shouild be anything here for solicitors -->
					</td>
				</tr>
				</xsl:for-each>
				<tr />
			</table>
		</xsl:if>
		
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- prosecutionTemplate				-->
	<!-- **************************************** -->
		
	<doc:template name="prosecution" xmlns="">
		<refpurpose>Outputs the details of the Prosecuting team</refpurpose>
		<refdescription>
		  <para>Shows who the prosecuting authority is,
		        then iterates through the Advocates on the prosecuting team.</para>
		  <para>Uses the template util:formalName to format the individuals name for display.</para>				
		  <para>
		  <emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Note: This section will need to be changed when the version of the schemas,
			      using Representative Structure, comes into force (v2.1 ?)
			</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="prosecution" >
	<!-- this template outputs the prosecuting team -->
		<xsl:variable name="prosecution" select="//cs:Prosection"/>
		<h4>
			<xsl:text>Prosecution </xsl:text>
		<xsl:value-of select="$prosecution/@ProsecutingAuthority"/>
		</h4>		
		<xsl:if test="$prosecution/cs:Advocate" >
			<strong>
				<xsl:text>Advocate</xsl:text>
			</strong>
			<br />
			<table width="90%" >
				<xsl:for-each select="//cs:Prosection/cs:Advocate">
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
	
	<!-- **************************************** -->
	<!-- courtReportingTemplate			-->
	<!-- **************************************** -->
	
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
		<xsl:if test="//cs:CourtReportingFirm" >
			<hr />
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- judiciary Template				-->
	<!-- **************************************** -->
	
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
		<xsl:if test="//cs:Judiciary/cs:Justice or //cs:Judiciary/cs:Judge">
			<hr />
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- bailOrCustody Template			-->
	<!-- **************************************** -->	
	
	<doc:template name="bailOrCustody" xmlns="">
		<refpurpose>Outputs the details of the bail or custody status.</refpurpose>
		<refdescription>
		  <para>Shows the bail or custody status at the various stages of the process i.e on committal,
		        at start of hearing and put back for sentence.</para>
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
	<table width="90%" >
		<tr>
			<td width="50%">
				<xsl:text>On committal : </xsl:text>
				<xsl:value-of  select="//cs:BailStatusOnCommittal" />
			</td>
			<td width="15%">
			</td>
			<td width="35%">
			</td>
		</tr>
		<tr>
			<td width="50%">
				<xsl:text>At start of hearing : </xsl:text>
				<xsl:value-of  select="//cs:BailStatusAtStartOfHearing" />
			</td>
			<td width="15%">
			</td>
			<td width="35%">
			</td>
		</tr>
		<tr>
			<td width="50%">
				<xsl:text>Put back for sentence : </xsl:text>
				<xsl:value-of  select="//cs:BailStatusPutBackforSentence" />
			</td>
			<td width="15%">
			</td>
			<td width="35%">
			</td>
		</tr>
		<tr />
	</table>
	<hr />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- hearings Template				-->
	<!-- **************************************** -->
	
	<doc:template name="hearings" xmlns="">
		<refpurpose>Outputs the hearing details along with key dates.</refpurpose>
		<refdescription>
		  <para>Iterates through the hearing details showing the start date and end date(if available) for each.
		        If the sentenced postponed date and/or the date of the sentence/order made dates are available then these
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
			<xsl:if test="/cs:CommittalRecordSheet/cs:SentencePostponedDate">
				<tr>
					<td>
					<xsl:text>Sentence postponed until </xsl:text>
					</td>
					<td>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="/cs:CommittalRecordSheet/cs:SentencePostponedDate"/>
						</xsl:call-template>
						<xsl:text> for reports.</xsl:text>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="/cs:CommittalRecordSheet/cs:SentenceOrOrderDate">
				<tr>
					<td>
						<xsl:text>Sentence/Order made on </xsl:text>
					</td>
					<td>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="/cs:CommittalRecordSheet/cs:SentenceOrOrderDate"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
		</table>
		<hr />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- counts Template					-->
	<!-- **************************************** -->
		
	<doc:template name="counts" xmlns="">
		<refpurpose>Outputs the information relating to charges, pleas etc.</refpurpose>
		<refdescription>
		  <para>Iterates through the Offences showing the description and the 
		        decision of the Crown Court.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="counts">
	
		<!-- outputs information about the charges, pleas etc  -->
		<table width="100%">
			<tr>
				<td width="10%" valign="top">
					<strong>
						<xsl:text>No</xsl:text>
					</strong>
				</td>
				<td width="90%">
					<strong>
						<xsl:text>Offence and Sentence/Order</xsl:text>
					</strong>
				</td>
			</tr>
			
			<xsl:for-each select="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'committal']">
				<tr>
					<td valign="top">
						<xsl:value-of select="./@IndictmentCountNumber"/>
					</td>
					<td>
						<xsl:value-of select="./cs:OffenceStatement"/>
						<br/>
						<xsl:if test="./cs:Verdict">
							<xsl:value-of select="./cs:Verdict"/>
							<br/>
						</xsl:if>
						<xsl:if test="./cs:CRN">
							<xsl:text>CRN: </xsl:text>
							<xsl:value-of select="./cs:CRN"/>
							<br/>
						</xsl:if>
						<xsl:call-template name="util:chargeSentence">
							<xsl:with-param name="charge" select="."/>
						</xsl:call-template>

						<!-- KN20041018 - Addition for CR79 -->						
						<strong>
						<xsl:if test="./cs:Disposals">
							<xsl:for-each select="./cs:Disposals/cs:Disposal">
							<xsl:choose>
								<xsl:when test="starts-with(.,'Imprisonment')">
									<xsl:call-template name="str:to-lower" >
										<xsl:with-param name="text" select="." />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="." />
								</xsl:otherwise>
								</xsl:choose>
								<br />
							</xsl:for-each>
						</xsl:if>
						</strong>
						<!-- KN20041018 - End of Addition for CR79 -->
					</td>
				</tr>
			</xsl:for-each>
		</table>
		
		<table width="100%">
			<tr>
				<td width="90%">
					<strong>
						<xsl:text>Other CRNs</xsl:text>
					</strong>
				</td>
			</tr>
			
			<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:CRN[not (../@ChargeType = 'committal')]">
				<tr>
					<td>
						<xsl:text>CRN: </xsl:text>
						<xsl:value-of select="."/>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<hr/>
	</xsl:template>

	<!-- **************************************** -->
	<!-- associatedCases Template		-->
	<!-- **************************************** -->
	
	<doc:template name="associatedCases" xmlns="">
		<refpurpose>Outputs the case numbers of related cases.</refpurpose>
		<refdescription>
		  <para>Iterates through the Linked Cases showing the case number.</para>
		</refdescription>
	</doc:template>
		
	<xsl:template name="associatedCases">
	<!-- outputs information about any other orders  -->
		<xsl:if test="/cs:CommittalRecordSheet/cs:LinkedCases">
			<xsl:text>Associated Cases</xsl:text>
			<br />
			<xsl:for-each select="/cs:CommittalRecordSheet/cs:LinkedCases/cs:CaseNumber">
				<xsl:value-of select="." />
				<br />	
			</xsl:for-each>
			<hr />
		</xsl:if>
	</xsl:template> 

	<!-- **************************************** -->
	<!-- otherOrders Template			-->
	<!-- **************************************** -->
	
	<doc:template name="otherOrders" xmlns="">
		<refpurpose>Outputs info for any other orders.</refpurpose>
		<refdescription>
		  <para>Iterates through the TotalSentences/OtherOrders showing the orders found.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="otherOrders">
	<!-- outputs information about any other orders  -->
		<xsl:if test="/cs:CommittalRecordSheet/cs:TotalSentence/cs:Term">
			<strong>
			<xsl:text>Total Sentence</xsl:text>
			<br />
			<xsl:call-template name="util:decodeDuration">
				<xsl:with-param name="duration" select="/cs:CommittalRecordSheet/cs:TotalSentence/cs:Term" />
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="str:to-lower">
				<xsl:with-param name="text" select="/cs:CommittalRecordSheet/cs:TotalSentence/cs:Term/@TermType" />
			</xsl:call-template>
			</strong>
			<hr />				
		</xsl:if>
		<xsl:if test="/cs:CommittalRecordSheet/cs:TotalSentence/cs:OtherOrders">
			<strong>
			<xsl:text>Other Orders</xsl:text>
			<br />
			<xsl:for-each select="/cs:CommittalRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
				<xsl:value-of select="." />
				<br />	
			</xsl:for-each>
			</strong>
			<hr />				
		</xsl:if>
	</xsl:template> 
	
	<!-- **************************************** -->
	<!-- breachOrders Template			-->
	<!-- **************************************** -->
	
	<doc:template name="breachOrders" xmlns="">
		<refpurpose>Outputs info for any breached orders.</refpurpose>
		<refdescription>
		  <para>Iterates through the OffencesOrOrdersBreached showing the details of the original order 
		        ie what it was an where it originated, along with the new sentence or order which has been applied.</para>
		  <para> util:decodeDuration is used to decode the sentence term.</para>
		  <para>
		  	<emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>Current version of the schema does not allow for a new sentence or order to be directly associated with a breach. 
			      This may be fixed in a future release of the schemas, and this code will need to be amended accordingly.</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template name="breachOrders">
	<!-- outputs information about any breach orders  -->
		<xsl:if test="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach']">
			<xsl:text>Breaches of Previous Orders</xsl:text>
			<br />
			<table width="100%">
				<tr>
					<td width="10%">
						<strong>
							<xsl:text>No</xsl:text>
						</strong>
					</td>
					<td width="30%">
						<strong>
							<xsl:text>Made By</xsl:text>
						</strong>
					</td>
					<td width="15%">
						<strong>
							<xsl:text>Date</xsl:text>
						</strong>
					</td>
					<td width="45%">
						<strong>
							<xsl:text>Offence/Order Breached and New Sentence/Order</xsl:text>
						</strong>
					</td>
				</tr>
			<xsl:for-each select="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach']">
				<tr>
					<td valign="top">
						<xsl:value-of select="./cs:OffenceNumber" />
					</td>
					<td valign="top">
						<xsl:if test="./cs:OriginatingCourt">
							<xsl:value-of select="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseName" />
							<xsl:if test="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
								<xsl:text> Crown Court</xsl:text>
							</xsl:if>
						</xsl:if>
					</td>
					<td valign="top">
						<xsl:if test="./cs:OriginatingCourt/cs:Date">
							<xsl:call-template name="util:ukdate_mon" >
								<xsl:with-param name="inDate" select="./cs:OriginatingCourt/cs:Date" />
							</xsl:call-template>
						</xsl:if>
					</td>
					<td valign="top">
						<xsl:value-of select="./cs:Description" />
						<br />
						<xsl:text>Total sentence </xsl:text>
						<xsl:call-template name="util:decodeDuration" >
							<xsl:with-param name="duration" select="../cs:Term" />
						</xsl:call-template>
						<br />
						<xsl:text>Original offences and new sentence/order:</xsl:text>
						<br />
						
					</td>
				</tr>	
			</xsl:for-each>
			</table>
			<hr />				
		</xsl:if>
	</xsl:template> 
	
	<!-- KN20041018 - Addition of Breach template to cover CR79 for 6.4 -->

	<!-- **************************************** -->
	<!-- breach Template					-->
	<!-- **************************************** -->
	
	<doc:template name="breach" xmlns="">
		<refpurpose>Outputs details of breaches of previous orders..</refpurpose>
		<refdescription>
		 	<para>Shows the details of the original order - date, who made it, the offence and details
		        of the breach and any new sentence or order arising as a result of the breach.</para>
		</refdescription>
	</doc:template>
	
	<xsl:template name="breach">
		<xsl:if test="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach']">
			<table width="100%">
			<tr>
				<td colspan="4">
					<strong>
						<xsl:text>Breaches of Previous Orders</xsl:text>
					</strong>
				</td>
			</tr>
			<tr>
				<td width="10%">
					<strong>
						<xsl:text>No</xsl:text>
					</strong>
				</td>
				<td width="30%">
					<strong>
						<xsl:text>Made By</xsl:text>
					</strong>
				</td>
				<td width="15%">
					<strong>
						<xsl:text>Date</xsl:text>
					</strong>
				</td>
				<td width="45%">
					<strong>
						<xsl:text>Offence/Order Breached and New Sentence/Order</xsl:text>
					</strong>
				</td>
			</tr>
			<xsl:for-each select="/cs:CommittalRecordSheet /cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach']">
				<tr>
					<td valign="top">
						<xsl:value-of select="./@IndictmentCountNumber" />
					</td>
					<td>
						<xsl:value-of select="./cs:OffenceStatement" />
					</td>
					<td>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="./cs:ArraignmentDate" />
						</xsl:call-template>
					</td>
					<td>
						<xsl:value-of select="./cs:Verdict" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<xsl:text>CRN: </xsl:text>					
						<xsl:value-of select="./cs:CRN" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<strong>
							<xsl:call-template name="util:decodeDuration" >
								<xsl:with-param name="duration"  select="cs:SentenceTerm" />
							</xsl:call-template>
							
							<xsl:if test="./cs:Disposals">
								<xsl:for-each select="./cs:Disposals/cs:Disposal">
								<xsl:choose>
									<xsl:when test="starts-with(.,'Imprisonment')">
										<xsl:call-template name="str:to-lower" >
											<xsl:with-param name="text" select="." />
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="." />
									</xsl:otherwise>
									</xsl:choose>
									<br />
								</xsl:for-each>
							</xsl:if>
							<xsl:text> </xsl:text>
							<xsl:value-of select="./cs:TermType" />
						</strong>
					</td>
				</tr>
			</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>

	<!-- KN20041018 - End of Addition -->
		
	<!-- **************************************** -->
	<!-- trialDateRange Template			-->
	<!-- **************************************** -->

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
