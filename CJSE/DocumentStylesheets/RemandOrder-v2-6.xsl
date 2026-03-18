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
		<releaseinfo role="meta">Version 2.6</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Remand Order Stylesheet</title>
	<para>File name : RemandOrder-v2.6xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Remand Order in html format</para>
			<para>PR58310 - Add extra space to be brought</para>
			<para>v2.3 updated for LASBO</para>
			<para>v2.6 updated for PCSC Bill</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'6'" />
<xsl:variable name="stylesheet" select="'RemandOrder-v2-6xsl'" />
<xsl:variable name="last-modified-date" select="'2022-05-11'" />
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
				<xsl:call-template name="util:UniversalOrderHeader"> 
					<xsl:with-param name="OrderTitle">
						<p>
							<xsl:choose>
								<xsl:when test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'LocalAuthority'">
									<xsl:text>Remand Order - Local Authority Accommodation</xsl:text>
								</xsl:when>
								<xsl:when test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'YouthDetention'">
									<xsl:text>Remand Order - Youth Detention Accommodation</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>Remand Order</xsl:text>
								</xsl:otherwise>
							</xsl:choose>							
						</p>
					</xsl:with-param>					
					<xsl:with-param name="OrderHeaderRoot" select="/cs:RemandOrder/cs:OrderHeader"/>
					<xsl:with-param name="OrderAddressee">
						<p>
							<xsl:choose>
								<xsl:when test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'LocalAuthority'">
									<xsl:text>To conveyor / custodian &amp; Local Authority</xsl:text>
								</xsl:when>
								<xsl:when test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'YouthDetention'">
									<xsl:text>To conveyor / custodian &amp; Manager of Youth Detention Accommodation</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>To Governor HMP</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</p>
					</xsl:with-param>
				</xsl:call-template>
			
				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:RemandOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<xsl:call-template name="remandDetails" />
			
				<xsl:apply-templates select="/cs:RemandOrder/cs:NextAppearance" />
				
				<xsl:apply-templates select="/cs:RemandOrder/cs:RemandOrderType/cs:RequirementOnLocalAuthority" />
				
				<xsl:apply-templates select="/cs:RemandOrder/cs:RemandOrderType/cs:ConditionsOnDefendant" />
				
				<xsl:apply-templates select="/cs:RemandOrder/cs:ReasonsForRemand" />
				
				<xsl:apply-templates select="/cs:RemandOrder/cs:RemandReason" />
								
				<xsl:call-template name="chargedWith" />
				
				<xsl:call-template name="util:AdditionalNotes" />
				
				<xsl:call-template name="util:orderSignatory" />
				
				<xsl:call-template name="util:associatedCases">
					<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
				</xsl:call-template>
								
				<!-- KN 20050517 - CR27 	-->
				<xsl:call-template name="util:copyOrderText" />
				<xsl:call-template name="util:copyrightText" />
			</body>
		</html>
	</xsl:template>
	
	
	<!-- **************************************** -->
	<!-- PersonalDetails Template			-->
	<!-- **************************************** -->

	<doc:template name="PersonalDetails" xmlns="">
		<refpurpose>Shows the personal information eg name, birth date, address etc.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:personsFullName to format the name.	</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:PersonalDetails">
	
		<h3>
			<xsl:text>It was ordered</xsl:text>
		</h3>
		<xsl:text>on </xsl:text>
		<strong>
			<xsl:value-of select="$orderDate"/>
		</strong>
		<xsl:text> that the </xsl:text>
		<xsl:call-template name="str:to-lower">
			<xsl:with-param name="text" select="$subjectType"/>
		</xsl:call-template>
		
		<br />

		<!-- details of the defendant -->
		<table width="100%">
			<tr>
				<td width="65%">
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
				<td width="35%">
					<xsl:text>Date of birth: </xsl:text>
					<strong>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
					</xsl:call-template>
					</strong>
				</td>
			</tr>			
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- remandDetails	Template		-->
	<!-- **************************************** -->
	
	<doc:template name="remandDetails" xmlns="">
		<refpurpose>Shows the remand location and wether a report is required or not.</refpurpose>
	</doc:template>
	
	<xsl:template name="remandDetails" >
		<br />
		<xsl:choose>
			<xsl:when test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'LocalAuthority'">
				<xsl:text>be remanded into the care of the Local Authority at </xsl:text>
			</xsl:when>
			<xsl:when test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'YouthDetention'">
				<xsl:text>be remanded into Youth Detention Accommodation at </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>be remanded in custody at </xsl:text>
			</xsl:otherwise>
		</xsl:choose>		
		<strong>
			<xsl:value-of select="/cs:RemandOrder/cs:CustodyLocation"/>
		</strong>
		<xsl:if test="/cs:RemandOrder/cs:ForReportOn and string-length(/cs:RemandOrder/cs:ForReportOn) &gt; 0">
			<xsl:text> for a report on </xsl:text>
			<br/>
			<xsl:value-of select="/cs:RemandOrder/cs:ForReportOn"/>
		</xsl:if>
	</xsl:template>
		
	<!-- **************************************** -->
	<!-- NextAppearance Template		-->
	<!-- **************************************** -->

	<doc:template name="NextAppearance" xmlns="">
		<refpurpose>Outputs the details of the next court appearance.</refpurpose>
		<refdescription>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<para>Note: If AppearanceDateTime is present then it is used to show the date and optionally the time of the next
			              appearance, otherwise the text 'on a date and time to be notified' is used.</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: util:ukdate_mon is used to show the date portion of AppearanceDateTime.</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: util:time is used to show the time portion of AppearanceDateTime (signified by the presence of the 'T' separator in
			  AppearanceDateTime).</para>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:NextAppearance" >
		<br />
		<xsl:text>and on </xsl:text>
		<xsl:choose>
			<xsl:when test="cs:AppearanceDateTime">
				<strong>	
					<xsl:call-template name="util:ukdate_mon" >
						<xsl:with-param name="inDate" select="cs:AppearanceDateTime"/>
					</xsl:call-template>
					<xsl:if test="contains(cs:AppearanceDateTime, 'T')">
						<xsl:text> at </xsl:text>
						<xsl:call-template name="util:time" >
						<xsl:with-param name="inTime" select="cs:AppearanceDateTime"/>
					</xsl:call-template>
					</xsl:if>
				</strong>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>a date and at a time to be notified</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text> be brought before</xsl:text>
		<xsl:choose>
			<xsl:when test="cs:AppearanceCourt/cs:CourtHouseType = 'Crown Court'">
				<xsl:text> the Crown Court sitting at: </xsl:text>
				<strong>
					<xsl:value-of select="cs:AppearanceCourt/cs:CourtHouseName" />
				</strong>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>: </xsl:text>
				<strong>
					<xsl:value-of select="cs:AppearanceCourt/cs:CourtHouseName" />
				</strong>
			</xsl:otherwise>
		</xsl:choose>
		<br />
		<xsl:text> or any other place that may be notified.</xsl:text>
		<xsl:if test="/cs:RemandOrder/cs:RemandOrderType/@RemandType = 'LocalAuthority' or /cs:RemandOrder/cs:RemandOrderType/@RemandType = 'YouthDetention'">
			<br/>
			<xsl:text>The designated Local Authority is </xsl:text>
			<xsl:value-of select="/cs:RemandOrder/cs:RemandOrderType/cs:DesignatedLocalAuthority" />
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- PersonalDetails	Template		-->
	<!-- **************************************** -->

	<doc:template name="RemandReason" xmlns="">
		<refpurpose>Output the reason(s) for which the defendant has been remanded.</refpurpose>
		<refdescription>
			<para>A variety of elements are used to flag the various reasons why the defendant has been remanded.
	For each that is present (or in some cases having a value of 'yes') then the appropriate reasons are output</para>
		</refdescription>
	</doc:template>
	
	<xsl:template match="cs:ReasonsForRemand" >
		<xsl:choose>
			<xsl:when test="//cs:RemandOrder/cs:ReasonsForRemand = ''"></xsl:when>
			<xsl:otherwise>
				<br />
				<table width="100%">
					<tr>
						<td >
							<h3>
								<xsl:text>Reasons given in Court for the remand to youth detention accomodation</xsl:text>
							</h3>
							<xsl:if test="cs:SeriousnessOfOffence">
								<xsl:text>Seriousness of Offence:</xsl:text>
								<br/>
								<xsl:value-of select="cs:SeriousnessOfOffence" />
								<br/>
								<br/>
							</xsl:if>	
							<xsl:if test="cs:History">
								<xsl:text>History:</xsl:text>
								<br/>
								<xsl:value-of select="cs:History" />
								<br/>
								<br/>
							</xsl:if>	
							<xsl:if test="cs:Necessity">
								<xsl:text>Necessity condition:</xsl:text>
								<br/>
								<xsl:text>The court has considered all other options but the child poses a risk of harm or offending AND the risk posed by the child</xsl:text>
								<strong>
									<xsl:text> cannot be managed safely in the community.</xsl:text>
								</strong>
								<br/>
								<br/>
							</xsl:if>	
							<xsl:if test="cs:OtherReasons">
								<xsl:text>Other reasons:</xsl:text>
								<br/>
								<xsl:if test="cs:OtherReasons/cs:Welfare">
									<xsl:text>For welfare reasons. </xsl:text>
									<xsl:value-of select="cs:OtherReasons/cs:Welfare" />
									<br/>
								</xsl:if>
								<xsl:if test="cs:OtherReasons/cs:OwnProtection">
									<xsl:text>For own protection reasons. </xsl:text>
									<xsl:value-of select="cs:OtherReasons/cs:OwnProtection" />
									<br/>
								</xsl:if>
								<xsl:if test="cs:OtherReasons/cs:LackOfPlacement">
									<xsl:text>lack of suitable placement in the community (e.g. foster, local authority, relative etc). </xsl:text>
									<xsl:value-of select="cs:OtherReasons/cs:LackOfPlacement" />
									<br/>
								</xsl:if>
								<xsl:if test="cs:OtherReasons/cs:BailISSNotAvailable">
									<xsl:text>Bail ISS not available. </xsl:text>
									<xsl:value-of select="cs:OtherReasons/cs:BailISSNotAvailable" />
									<br/>
								</xsl:if>
								<xsl:if test="cs:OtherReasons/cs:BailInadequate">
									<xsl:text>bail package inadequate. </xsl:text>
									<xsl:value-of select="cs:OtherReasons/cs:BailInadequate" />
									<br/>
								</xsl:if>
							</xsl:if>							
						</td>
					</tr>
					<br />
			
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
			
	
	<xsl:template match="cs:RemandReason" >
		<br />
		<table width="100%">
			<xsl:if test="cs:SentforTrial">
				<tr>
					<td >
						<h3>
							<xsl:text>The </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="$subjectType"/>
							</xsl:call-template>
						</h3>
						<xsl:text> was sent for trial to the Crown Court on </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="cs:SentforTrial/cs:Date"/>
						</xsl:call-template>
						<xsl:text> by the </xsl:text>
						<xsl:choose>
							<xsl:when test="cs:SentforTrial/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
								<xsl:value-of select="cs:SentforTrial/cs:CourtHouse/cs:CourtHouseName"/>
								<xsl:text> Crown Court </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="cs:SentforTrial/cs:CourtHouse/cs:CourtHouseName"/>
								<xsl:text> </xsl:text>	
							</xsl:otherwise>
						</xsl:choose>						
					</td>
				</tr>
			</xsl:if>
			
			<xsl:if test="cs:CrownCourtCommittal or
						  cs:VoluntaryBillOfIndictment or
						  cs:CertificateOfTransfer or
						  cs:AppealAgainstBailGranted = 'yes'   ">
				<tr>
					<td >
						<h3>
							<xsl:text>The </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="$subjectType"/>
							</xsl:call-template>
						</h3>
					</td>
				</tr>
			</xsl:if>
			
			<xsl:if test="cs:CrownCourtCommittal">
				<tr>
					<td>
						<xsl:text>was committed to the Crown Court on </xsl:text>
						<strong>			
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="cs:CrownCourtCommittal/cs:Date"/>
							</xsl:call-template>
						</strong>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>by the </xsl:text>
						<xsl:value-of select="cs:CrownCourtCommittal/cs:CourtHouse/cs:CourtHouseName" />
						<xsl:if test="cs:CrownCourtCommittal/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
							<xsl:text> Crown Court</xsl:text>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			
			<xsl:if test="cs:VoluntaryBillOfIndictment or
						  cs:CertificateOfTransfer or
						  cs:AppealAgainstBailGranted = 'yes'   ">
				<tr>
					<td>
						<xsl:text>is before the Court</xsl:text>
					</td>
				</tr>
				<xsl:choose>
					<xsl:when test="cs:VoluntaryBillOfIndictment">
						<tr>
							<td>
								<xsl:text>on a Voluntary Bill of Indictment dated </xsl:text>
								<strong>			
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="cs:VoluntaryBillOfIndictment"/>
									</xsl:call-template>
								</strong>
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="cs:CertificateOfTransfer">
						<tr>
							<td>
								<xsl:text>on a Certificate of Transfer dated </xsl:text>
								<strong>			
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="cs:CertificateOfTransfer"/>
									</xsl:call-template>
								</strong>
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="cs:AppealAgainstBailGranted = 'yes'">
						<tr>
							<td>
								<xsl:text>as a result of an appeal by the Prosecution against the grant of bail </xsl:text>
							</td>
						</tr>
					</xsl:when>
				</xsl:choose>
			</xsl:if>
			<tr>
				<td>
					<xsl:text>and has been </xsl:text>
					<xsl:variable name="remandType" >
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="../cs:RemandType" />
						</xsl:call-template>
					</xsl:variable>
					<xsl:value-of select="$remandType" />
					<xsl:choose>
						<xsl:when test="$remandType = 'indicted'"> 
							<xsl:text> for crime.</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> of crime.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<br />
			
		</table>
		
	</xsl:template>
	
	<xsl:template match="cs:RequirementOnLocalAuthority" >
		<tr>
			<td >
				<h3>
					<xsl:text>Requirements of the court on the Local Authority</xsl:text>
				</h3>
				<xsl:value-of select="/cs:RemandOrder/cs:RemandOrderType/cs:RequirementOnLocalAuthority" />
			</td>
		</tr>
	</xsl:template>
	
	
	<xsl:template match="cs:ConditionsOnDefendant" >
		<tr>
			<td >
				<h3>
					<xsl:text>Conditions on defendant</xsl:text>
				</h3>
				<xsl:value-of select="/cs:RemandOrder/cs:RemandOrderType/cs:ConditionsOnDefendant" />
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="chargedWith">
		<xsl:if test="//cs:ChargedWith">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Charged With:</xsl:text>
					</td>
					<td width="80%">
						<xsl:value-of select="//cs:ChargedWith"/>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
