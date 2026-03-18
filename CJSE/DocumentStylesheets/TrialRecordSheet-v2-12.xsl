<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 2-10</releaseinfo>
			<author>
				<surname>Tully</surname>
				<firstname>Stephen</firstname>
			</author>
		</referenceinfo>
		<title>Trial Record Sheet Stylesheet</title>
		<para>File name : TrialRecordSheet-v2-12.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Trial Record Sheet in html format</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'2'"/>
	<xsl:variable name="minorVersion" select="'12'"/>
	<xsl:variable name="stylesheet" select="'trialrecordsheet-v2-12.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2022-11-09'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="iCount" select="0"/>
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<!-- end Global Variables -->
	<xsl:key name="ArraignmentDate-by-IndCount" match="cs:Charge" use="cs:ArraignmentDate"/>
	<xsl:key name="ConvictionDate-by-IndCount" match="cs:Charge" use="cs:ConvictionDate"/>
	<xsl:output method="html" indent="yes"/>
	<!-- **************************************** -->
	<!-- Root Template                  -->
	<!-- **************************************** -->
	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
				<xsl:call-template name="header"/>
				<xsl:call-template name="defence"/>
				<xsl:call-template name="prosecution"/>
				<xsl:call-template name="courtReporting"/>
				<xsl:call-template name="judiciary"/>
				<xsl:call-template name="bailOrCustody"/>
				<xsl:call-template name="hearings"/>
				<!-- the following line added for PR5554 -insetion of page break - NOTE not all browser/printer will recognise this as it is an optional rule -->
				<div style="page-break-before:always">
					<span style="visibility: hidden">-</span>
				</div>
				<xsl:call-template name="counts"/>
				<xsl:call-template name="summary"/>
				<xsl:call-template name="totalSentence"/>
				<xsl:call-template name="otherOrders"/>
				<xsl:call-template name="originalCharges"/>
				<xsl:call-template name="breach"/>
				<xsl:call-template name="takenIntoConsideration"/>
				<xsl:call-template name="specialCircumstances"/>
				<xsl:call-template name="decisionOfTheCourtOfAppeal"/>
				<!-- KN 20050517 - CR27     -->
				<xsl:call-template name="util:copyrightText"/>
			</body>
		</html>
	</xsl:template>
	<!-- **************************************** -->
	<!-- lineBreak Template                    -->
	<!-- **************************************** -->
	<xsl:template name="lineBreak">
		<xsl:param name="text"/>
		<xsl:choose>
			<xsl:when test="contains($text, '&#xa;')">
				<xsl:value-of select="substring-before($text, '&#xa;')"/>
				<br/>
				<xsl:call-template name="lineBreak">
					<xsl:with-param name="text" select="substring-after($text, '&#xa;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- **************************************** -->
	<!-- header Template                    -->
	<!-- **************************************** -->
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the Report Header information - including the Defendant and court details.</refpurpose>
	</doc:template>
	<xsl:template name="header">
		<!-- processes the Header information - defendant and court -->
		<xsl:variable name="personal" select="//cs:RecordSheetHeader/cs:Defendant/cs:PersonalDetails"/>
		<h2>
			<center>
				<xsl:value-of select="'Trial Record Sheet'"/>
			</center>
		</h2>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="65%">
					<span class="emphasis">
						<xsl:text>Defendant </xsl:text>
						<xsl:if test="$personal/cs:Sex">
							<xsl:text>(</xsl:text>
							<xsl:choose>
								<xsl:when test="$personal/cs:Sex = 'unknown'">
									<xsl:text>Company</xsl:text>
								</xsl:when>
								<xsl:when test="$personal/cs:Sex != 'unknown'">
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="$personal/cs:Sex"/>
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</span>
				</td>
				<td WIDTH="35%">
					<xsl:text>Defendant No.</xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:CaseNumber"/>
					<xsl:text>-</xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:DefendantNumber"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:TotalDefendants"/>
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
					<span class="emphasis">
						<xsl:text>Address</xsl:text>
					</span>
				</td>
				<td>
					<xsl:text>PTI Unique Ref: </xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:URN"/>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:call-template name="util:address_oneline">
						<xsl:with-param name="personalDetails" select="$personal"/>
					</xsl:call-template>
				</td>
				<td>
					<xsl:text>PNC No: </xsl:text>
					<xsl:variable name="pnc" select="//cs:RecordSheetHeader/cs:Defendant/cs:PNCnumber"/>
					<xsl:value-of select="substring($pnc,3,10)"/>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:if test="$personal/cs:Nationality">					
						<span class="emphasis">
							<xsl:text>Nationality</xsl:text>
						</span>
					</xsl:if>
                </td>
				<td>
					<xsl:text>ASN No: </xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:ASNs/cs:ASN"/>
				</td>
			</tr>			
			<xsl:if test="$personal/cs:Nationality">			
				<tr>
					<td><xsl:value-of select="$personal/cs:Nationality"/></td>
				</tr>
			</xsl:if>			
		</table>
		<hr/>
		<strong>
			<!-- now give out the court information -->
			<xsl:text>Before the </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseType"/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseName"/>
		</strong>
		<br/>
		<xsl:call-template name="util:instigationText">
			<xsl:with-param name="code" select="//cs:RecordSheetHeader/cs:MethodOfInstigation"/>
		</xsl:call-template>
		<xsl:if test="//cs:RecordSheetHeader/cs:MethodOfInstigation = 'Committal' or
                      //cs:RecordSheetHeader/cs:MethodOfInstigation = 'Sending'">
			<xsl:value-of select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName"/>
			<xsl:text> on </xsl:text>
		</xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:DateOfInstigation">
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
			</xsl:call-template>
		</xsl:if>
		<br/>
		<xsl:if test="//cs:RecordSheetHeader/cs:TransferInCourt">
			<xsl:text>Transferred from </xsl:text>
			<xsl:variable name="transferCourtHouseName" select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseName"/>
			<xsl:choose>
				<xsl:when test="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseType='Crown Court'">
					<xsl:text> the </xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseType"/>
					<xsl:text> at </xsl:text>
					<xsl:call-template name="str:capitalise">
						<xsl:with-param name="text" select="$transferCourtHouseName"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="str:capitalise">
						<xsl:with-param name="text" select="$transferCourtHouseName"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> on </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:DateOfTransferIn"/>
			</xsl:call-template>
			<br/>
		</xsl:if>
		<xsl:if test="starts-with(//cs:TrialRecordSheet/cs:ReTrial, 'y')">
			<xsl:text>Re-trial ordered by the Court of Appeal</xsl:text>
			<br/>
		</xsl:if>
		<xsl:if test="//cs:TrialRecordSheet/cs:BenchWarrantDate">
			<xsl:text>Bench Warrant executed on </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:TrialRecordSheet/cs:BenchWarrantDate"/>
			</xsl:call-template>
			<br/>
		</xsl:if>
		<xsl:if test="//cs:TrialRecordSheet/cs:IndictmentHistory">
			<xsl:text>Date for preferment of indictment extended </xsl:text>
			<xsl:for-each select="//cs:TrialRecordSheet/cs:IndictmentHistory/cs:Extension">
				<xsl:sort select="./cs:ExtensionDate" data-type="text" order="ascending"/>
				<xsl:sort select="./cs:ExtendedToDate" data-type="text" order="ascending"/>
				<xsl:text> on </xsl:text>
				<xsl:call-template name="trialDateRange">
					<xsl:with-param name="startDate" select="./cs:ExtensionDate"/>
					<xsl:with-param name="endDate" select="./cs:ExtendedToDate"/>
				</xsl:call-template>
				<xsl:if test="not(position()=last())">
					<xsl:text> and </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
		<hr/>
		<!-- ***************************************-->
		<!--  Vulnerable Victim Indicator-->
		<!-- ***************************************-->
		<xsl:choose>
			<xsl:when test="//cs:RecordSheetHeader/cs:VulnerableVictimIndicator = 'Y'">
				<xsl:call-template name="util:VulnerableVictimIndicatorText"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="util:VulnerableVictimIndicatorDefaultText"/>
			</xsl:otherwise>
		</xsl:choose>
		<hr/>
	</xsl:template>
	<!-- create hearings index on hearing date -->
	<xsl:key name="hearings-by-date" match="cs:Hearing" use="cs:HearingDate"/>
	<!-- **************************************** -->
	<!-- defenceTemplate                -->
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
					<para>Note: The current version of schemas does not allow for a status code to be associated 
                      with the advocate information. It is planned that a future version (2.1 or later) of the schemas will fix this</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	<xsl:template name="defence">
		<!-- this template outputs the defence team -->
		<xsl:variable name="defenceAdvocates" select="//cs:DefenceAdvocates"/>
		<xsl:variable name="defenceSolicitors" select="//cs:DefenceSolicitors"/>
		<h4>
			<u>
				<xsl:text>Defence</xsl:text>
			</u>
		</h4>
		<!-- First do the advocats -->
		<strong>
			<u>
				<xsl:text>Advocate</xsl:text>
			</u>
		</strong>
		<br/>
		<!--		Modified by Tom Muir-Webb         -->
			<xsl:if test="$defenceAdvocates/cs:DefenceAdvocate">
				<table width="90%">
					<xsl:for-each select="$defenceAdvocates/cs:DefenceAdvocate/cs:Advocate">
						<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
						<tr>
							<td width="40%">
								<!--								<xsl:variable name="cnrnFormalName">
									<xsl:call-template name="util:formalName">

										<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:call-template name="str:capitalise">
									<xsl:with-param name="text" select="$cnrnFormalName"/>
								</xsl:call-template>
-->
								<!-- altered below to use the requested name as used in the CREST record sheet, uses Initials not long forenames where present-->
								<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
							</td>
							<td width="10%">
								<xsl:call-template name="representativeDescriptionForAdvocate">
									<xsl:with-param name="qc" select="../@QC"/>
									<xsl:with-param name="advocateType" select="../@AdvocateType"/>
									<xsl:with-param name="startDate" select="./cs:StartDate"/>
								</xsl:call-template>
							</td>
							<td width="15%">
								<xsl:if test="../@HearingRole">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="../@HearingRole"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
							</td>
							<td width="25%">
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="./cs:StartDate"/>
									<xsl:with-param name="endDate" select="./cs:EndDate"/>
								</xsl:call-template>
								<xsl:if test="./cs:StartDate">
									<xsl:if test="not (./cs:EndDate)">
										<!-- Bichard PR 5457 - Tom Muir-Webb  removed for PR5596 BJH-->
										<!--xsl:text> onwards </xsl:text-->
									</xsl:if>
									<xsl:if test="./cs:EndDate=''">
										<xsl:text> onwards </xsl:text>
									</xsl:if>
								</xsl:if>
							</td>
							<td width="6%">
								<xsl:if test="../@RepresentationHearingType">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="../@RepresentationHearingType"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
							</td>
							<td width="4%">
								<xsl:if test="../@SubInst">
									<xsl:choose>
										<xsl:when test="../@SubInst = 'S'">
											<xsl:text>(S)</xsl:text>
										</xsl:when>
										<xsl:when test="../@SubInst = 'I'">
											<xsl:text>(I)</xsl:text>
										</xsl:when>
									</xsl:choose>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
					<xsl:for-each select="$defenceAdvocates/cs:DefenceAdvocate/cs:Solicitor/cs:Party">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<tr>
							<td width="40%">
								<xsl:choose>
									<xsl:when test="./cs:Person">
										<xsl:variable name="cnrnFormalName">
											<xsl:call-template name="util:CNRNformalName">
												<xsl:with-param name="name" select="./cs:Person/cs:PersonalDetails/cs:Name"/>
											</xsl:call-template>
										</xsl:variable>
										<xsl:call-template name="str:capitalise">
											<xsl:with-param name="text" select="$cnrnFormalName"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="str:capitalise">
											<xsl:with-param name="text" select="./cs:Organisation/cs:OrganisationName"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%"/>
							<td width="15%">
								<xsl:variable name="inPerson">
									<xsl:call-template name="str:to-lower">
										<xsl:with-param name="text" select="./cs:Person/cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:if test="$inPerson!='' and $inPerson!='in person'">
									<xsl:text>(Solicitor)</xsl:text>
								</xsl:if>
							</td>
							<td width="25%">
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="../cs:StartDate"/>
									<xsl:with-param name="endDate" select="../cs:EndDate"/>
								</xsl:call-template>
								<xsl:if test="../cs:StartDate">
									<xsl:if test="not (../cs:EndDate)">
										<!-- Bichard PR 5457 - Tom Muir-Webb -->
										<xsl:text> onwards </xsl:text>
									</xsl:if>
									<xsl:if test="../cs:EndDate=''">
										<xsl:text> onwards </xsl:text>
									</xsl:if>
								</xsl:if>
							</td>
							<td width="6%">
								<xsl:if test="../../@RepresentationHearingType">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="../../@RepresentationHearingType"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
							</td>
							<td width="4%">
								<xsl:if test="../../@SubInst">
									<xsl:choose>
										<xsl:when test="../../@SubInst = 'S'">
											<xsl:text>(S)</xsl:text>
										</xsl:when>
										<xsl:when test="../../@SubInst = 'I'">
											<xsl:text>(I)</xsl:text>
										</xsl:when>
									</xsl:choose>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
					<tr/>
				</table>
			</xsl:if>
			<xsl:if test ="not ($defenceAdvocates/cs:DefenceAdvocate/cs:Advocate)">
				<xsl:if test ="$defenceAdvocates/cs:DefenceAdvocate">
					<xsl:if test ="not ($defenceAdvocates/cs:DefenceAdvocate/cs:Solicitor)">
						<xsl:choose>
							<xsl:when test="$defenceAdvocates/cs:DefenceAdvocate/@NonAttendance = 'yes'">
								<xsl:text>Non Attendance</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>In Person</xsl:text>
							</xsl:otherwise>
						</xsl:choose>						
						<br/>
						<br/>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		<!-- Now the solicitors -->
		<xsl:if test="$defenceSolicitors/cs:DefenceSolicitor/cs:Party">
			<strong>
				<xsl:text>Solicitors</xsl:text>
			</strong>
			<br/>
			<table width="90%">
				<xsl:for-each select="//cs:DefenceSolicitor/cs:Party">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="40%">
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="./cs:Organisation/cs:OrganisationName"/>
							</xsl:call-template>
						</td>
						<td width="10%"/>
						<td width="15%">
							<xsl:call-template name="representativeDescriptionForSolicitor">
								<xsl:with-param name="representativeType" select="../@RepresentationType"/>
							</xsl:call-template>
						</td>
						<td width="25%">
							<xsl:if test="../cs:StartDate">
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="../cs:StartDate"/>
								</xsl:call-template>
								<xsl:choose>
									<xsl:when test="not (number(../cs:EndDate))">
										<!-- Bichard PR 5457 - Tom Muir-Webb -->
										<xsl:text> onwards </xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text> to </xsl:text>
										<xsl:call-template name="util:ukdate_mon">
											<xsl:with-param name="inDate" select="../cs:EndDate"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</td>
						<td width="10%"/>
					</tr>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- prosecution Template           -->
	<!-- **************************************** -->
	<doc:template name="prosecution" xmlns="">
		<refpurpose>Outputs the details of the prosecution team.</refpurpose>
		<refdescription>
			<para>Iterates through the Advocates (if any).</para>
			<para>Uses the template util:formalName to format the individuals name for display.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="prosecution">
		<!-- this template outputs the prosecuting team -->
		<xsl:variable name="prosecution" select="//cs:Prosecution"/>
		<xsl:variable name="prosecutionAdvocates" select="//cs:Prosecution/cs:ProsecutionAdvocates"/>
		<xsl:variable name="prosecutionSolicitors" select="//cs:Prosecution/cs:ProsecutionSolicitors"/>
		<h4>
			<u>
				<xsl:text>Prosecution </xsl:text>
				<xsl:if test="$prosecution/cs:ProsecutingOrganisation/cs:OrganisationName">
					<!-- CREST record sheet does not have brackets -->
					<!--					<xsl:text>(</xsl:text>   -->
					<xsl:value-of select="$prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
					<!--					<xsl:text>)</xsl:text>   -->
				</xsl:if>
			</u>
		</h4>
		<strong>
			<u>
				<xsl:text>Advocate</xsl:text>
			</u>
		</strong>
		<br/>
		<!--		Modified by Tom Muir-Webb         -->
		<xsl:choose>
			<xsl:when test="$prosecutionAdvocates/cs:ProsecutionAdvocate">
				<table width="90%">
					<xsl:for-each select="$prosecutionAdvocates/cs:ProsecutionAdvocate/cs:Advocate">
						<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
						<tr>
							<td width="40%">
								<!--<xsl:variable name="cnrnFormalName">
									<xsl:call-template name="util:formalName">
										<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:call-template name="str:capitalise">
									<xsl:with-param name="text" select="$cnrnFormalName"/>
								</xsl:call-template>-->
								<!-- altered below to use the requested name as used in the CREST record sheet, uses Initials not long forenames where present-->
								<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
							</td>
							<td width="10%">
						</td>
							<td width="15%">
								<xsl:if test="starts-with(./../@QC, 'y')">
									<xsl:variable name="Date">
										<xsl:call-template name="dateToNumber">
											<xsl:with-param name="Date" select="./cs:StartDate"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:choose>
										<xsl:when test="$Date &lt; 20220909">
											<xsl:text>(QC)</xsl:text>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>(KC)</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
								<xsl:if test="not (starts-with(./../@QC, 'y'))">
									<xsl:text>(Counsel)</xsl:text>
								</xsl:if>
							</td>
							<td width="25%">
								<xsl:if test="./cs:StartDate">
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="./cs:StartDate"/>
									</xsl:call-template>
									<xsl:choose>
										<xsl:when test="not (number(./cs:EndDate))">
											<!-- Bichard PR 5457 - Tom Muir-Webb  - Removed for PR5596 BJH-->
											<!--xsl:text> onwards </xsl:text -->
										</xsl:when>
										<xsl:otherwise>
											<xsl:text> to </xsl:text>
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="./cs:EndDate"/>
											</xsl:call-template>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</td>
							<td width="10%"/>
						</tr>
					</xsl:for-each>
					<!-- Not handled previously BP 03092008 -->
					<xsl:for-each select="$prosecutionAdvocates/cs:ProsecutionAdvocate/cs:Solicitor/cs:Party">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<tr>
							<td width="40%">
								<xsl:choose>
									<xsl:when test="./cs:Person">
										<xsl:variable name="cnrnFormalName">
											<xsl:call-template name="util:CNRNformalName">
												<xsl:with-param name="name" select="./cs:Person/cs:PersonalDetails/cs:Name"/>
											</xsl:call-template>
										</xsl:variable>
										<xsl:call-template name="str:capitalise">
											<xsl:with-param name="text" select="$cnrnFormalName"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="str:capitalise">
											<xsl:with-param name="text" select="./cs:Organisation/cs:OrganisationName"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%"/>
							<td width="15%">
								<xsl:variable name="inPerson">
									<xsl:call-template name="str:to-lower">
										<xsl:with-param name="text" select="./cs:Person/cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:if test="$inPerson!='' and $inPerson!='in person'">
									<xsl:text>(Solicitor)</xsl:text>
								</xsl:if>
							</td>
							<td width="25%">
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="../cs:StartDate"/>
									<xsl:with-param name="endDate" select="../cs:EndDate"/>
								</xsl:call-template>
								<xsl:if test="../cs:StartDate">
									<xsl:if test="not (../cs:EndDate)">
										<!-- Bichard PR 5457 - Tom Muir-Webb -->
										<xsl:text> onwards </xsl:text>
									</xsl:if>
									<xsl:if test="../cs:EndDate=''">
										<!-- Bichard PR 5457 - Tom Muir-Webb -->
										<xsl:text> onwards </xsl:text>
									</xsl:if>
								</xsl:if>
							</td>
							<td width="10%">
								<!--Not required for prosecution-->
							</td>
						</tr>
					</xsl:for-each>
					<tr/>
				</table>
				<!--</xsl:if>-->
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>In Person</xsl:text>
				<br/>
				<br/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="$prosecutionSolicitors/cs:ProsecutionSolicitor/cs:Party">
			<strong>
				<xsl:text>Solicitors</xsl:text>
			</strong>
			<br/>
			<table width="90%">
				<xsl:for-each select="$prosecutionSolicitors/cs:ProsecutionSolicitor/cs:Party">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="40%">
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="./cs:Organisation/cs:OrganisationName"/>
							</xsl:call-template>
						</td>
						<td width="10%"/>
						<td width="15%"/>
						<td width="25%">
							<xsl:if test="../cs:StartDate">
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="../cs:StartDate"/>
								</xsl:call-template>
								<xsl:choose>
									<xsl:when test="not (number(../cs:EndDate))">
										<!-- Bichard PR 5457 - Tom Muir-Webb -->
										<xsl:text> onwards </xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text> to </xsl:text>
										<xsl:call-template name="util:ukdate_mon">
											<xsl:with-param name="inDate" select="../cs:EndDate"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</td>
						<td width="10%"/>
					</tr>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- courtReporting Template            -->
	<!-- **************************************** -->
	<doc:template name="courtReporting" xmlns="">
		<refpurpose>Outputs the details of the court reporting firms and their associated partners.</refpurpose>
		<refdescription>
			<para>Iterates through the court reporting firms showing for each one the name of the firm,
                followed by the names of court reporters associated with that firm.</para>
			<para>Uses the template util:formalName to format the individuals name for display.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="courtReporting">
		<!-- this template outputs the court reporting firms and their associated reporters -->
		<xsl:for-each select="//cs:CourtReportingFirm">
			<xsl:if test="position()=1">
				<h4>
					<u>
						<xsl:text>Court Reporting Firms</xsl:text>
					</u>
				</h4>
			</xsl:if>
			<xsl:if test="./cs:Firm/cs:OrganisationCode">
				<xsl:text>(</xsl:text>
				<xsl:value-of select="./cs:Firm/cs:OrganisationCode"/>
				<xsl:text>) </xsl:text>
			</xsl:if>
			<!-- UPPERCASE removed as per PR5349-->
			<!--			<font style="text-transform: uppercase;">-->
			<!-- amended for PR 59730 -->
			<xsl:value-of select="./cs:Firm/cs:OrganisationName"/>
			<!--			</font>-->
			<br/>
		</xsl:for-each>
		<xsl:for-each select="//cs:CourtReportingFirm">
			<xsl:if test="./cs:Reporter">
				<xsl:if test="position()=1">
					<h4>
						<u>
							<xsl:text>Court Reporters</xsl:text>
						</u>
					</h4>
				</xsl:if>
				<table width="90%">
					<xsl:for-each select="./cs:Reporter">
						<xsl:sort select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname" data-type="text" order="ascending"/>
						<!-- amended for PR 59728 -->
						<tr>
							<td width="40%">
								<xsl:text>( </xsl:text>
								<xsl:value-of select="../cs:Firm/cs:OrganisationCode"/>
								<xsl:text> ) </xsl:text>
								<!-- UPPERCASE removed as per PR5348-->
								<!--								<font style="text-transform: uppercase;">-->
								<!-- amended for PR 59729 -->
								<xsl:call-template name="util:personsFullName">
									<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
								</xsl:call-template>
								<!--								</font>-->
							</td>
							<td width="10%"/>
							<td width="15%">
								<xsl:text>( </xsl:text>
								<xsl:value-of select="./cs:OperatorType"/>
								<xsl:text> )</xsl:text>
							</td>
							<td width="25%">
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="./cs:StartDate"/>
								</xsl:call-template>
							</td>
							<td width="10%"/>
						</tr>
					</xsl:for-each>
					<tr/>
				</table>
			</xsl:if>
		</xsl:for-each>
		<xsl:if test="//cs:CourtReportingFirm">
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- judiciaryTemplate              -->
	<!-- **************************************** -->
	<doc:template name="judiciary" xmlns="">
		<refpurpose>Outputs the details of the Judges. There arn't any Justices for Committals.</refpurpose>
		<refdescription>
			<para>Iterates through the Judges there might be</para>
			<para>Uses the template util:judiciaryName to format the individuals name for display.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="judiciary">
		<!-- this template outputs the Judges and Justices -->
		<h4>
			<u>
				<xsl:text>Judge</xsl:text>
			</u>
		</h4>
		<xsl:if test="//cs:Judiciary/cs:Judge">
			<table width="90%">
				<xsl:for-each select="//cs:Judiciary/cs:Judge">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="40%">
							<xsl:choose>
								<xsl:when test="./apd:CitizenNameRequestedName">
									<xsl:value-of select="./apd:CitizenNameRequestedName"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="util:judiciaryName">
										<xsl:with-param name="judge" select="."/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="10%"/>
						<td width="15%"/>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="../cs:StartDate"/>
								<xsl:with-param name="endDate" select="../cs:EndDate"/>
							</xsl:call-template>
						</td>
						<td width="10%"/>
					</tr>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- bailOrCustody Template         -->
	<!-- **************************************** -->
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
                      proposed for Version 2.x of the schemas, comes into force.</para>
				</listitem>
				<listitem>
					<para>Note: Start of trial status is not available so for a workround have to 
                         use bail status at start of hearing instead.   </para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	<xsl:template name="bailOrCustody">
		<h4>
			<u>
				<xsl:text>Bail/Custody Status</xsl:text>
			</u>
		</h4>
		<table width="90%">
			<tr>
				<td width="50%">
					<xsl:choose>
						<xsl:when test="/cs:TrialRecordSheet/cs:BenchWarrantDate">
							<xsl:text>After Bench Warrant executed: </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="/cs:TrialRecordSheet/cs:BailStatusAfterBenchWarrantExecuted"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:MethodOfInstigation='Sending'">
							<xsl:text>On sending: </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="/cs:TrialRecordSheet/cs:BailStatusOnCommittal"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:MethodOfInstigation='Committal'">
							<xsl:text>On committal: </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="/cs:TrialRecordSheet/cs:BailStatusOnCommittal"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</td>
				<td width="15%"/>
				<td width="35%">
					<xsl:choose>
						<xsl:when test="/cs:TrialRecordSheet/cs:NumberOfProsecutionWitnesses">
							<xsl:value-of select="/cs:TrialRecordSheet/cs:NumberOfProsecutionWitnesses"/>
							<xsl:choose>
								<xsl:when test="/cs:TrialRecordSheet/cs:NumberOfProsecutionWitnesses &gt; 1">
									<xsl:text> Prosecution Witnesses</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text> Prosecution Witness</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>0 Prosecution Witnesses</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<xsl:for-each select="//cs:DefendantBailApplications/cs:DefendantBailApplication">
				<xsl:sort select="./cs:ApplicationDate" data-type="text" order="ascending"/>
				<tr>
					<td colspan="3">
						<xsl:text>Application for bail on </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="./cs:ApplicationDate"/>
						</xsl:call-template>
						<xsl:text>: </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="./cs:Result"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
			<xsl:for-each select="//cs:Hearings/cs:Hearing/cs:HearingBailChanges">
				<xsl:sort select="./cs:BailChangeDate" data-type="text" order="ascending"/>
				<tr>
					<td colspan="3">
						<xsl:choose>
							<xsl:when test="./cs:HearingBailResult">
								<xsl:text>Application for bail on </xsl:text>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="./cs:BailChangeDate"/>
								</xsl:call-template>
								<xsl:text>: </xsl:text>
								<xsl:value-of select="./cs:HearingBailResult"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>Change of status before verdict </xsl:text>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="./cs:BailChangeDate"/>
								</xsl:call-template>
								<xsl:text>: </xsl:text>
								<xsl:value-of select="./cs:NewBailStatus"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:for-each>
			<tr>
				<td width="50%">
					<xsl:if test="./cs:TrialRecordSheet/cs:BailStatusAtStartOfHearing">
						<!--Changed to match CREST "At start of trial-->
						<!--						<xsl:text>At start of hearing: </xsl:text>-->
						<xsl:text>At start of trial: </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="./cs:TrialRecordSheet/cs:BailStatusAtStartOfHearing"/>
						</xsl:call-template>
					</xsl:if>
				</td>
				<td width="15%"/>
				<td width="35%">
					<xsl:choose>
						<xsl:when test="./cs:TrialRecordSheet/cs:PagesOfEvidence">
							<xsl:value-of select="./cs:TrialRecordSheet/cs:PagesOfEvidence"/>
							<xsl:choose>
								<xsl:when test="./cs:TrialRecordSheet/cs:PagesOfEvidence &gt; 1">
									<xsl:text> Pages of Evidence</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text> Page of Evidence</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>0 Pages of Evidence</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<xsl:if test="./cs:TrialRecordSheet/cs:BailStatusPutBackforSentence">
				<tr>
					<td colspan="3">
						<xsl:text>Put back for sentence: </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="./cs:TrialRecordSheet/cs:BailStatusPutBackforSentence"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
			<tr/>
		</table>
	</xsl:template>
	<!-- **************************************** -->
	<!-- hearings Template              -->
	<!-- **************************************** -->
	<doc:template name="hearings" xmlns="">
		<refpurpose>Outputs the hearing details along with key dates.</refpurpose>
		<refdescription>
			<para>Iterates through the hearing details showing the start date and end date(if available) for each.
                If any of the following dates are available then they are also shown: Arraigned, Tried, Convicted, Sentence or Order made.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="hearings">
		<h4>
			<u>
				<xsl:variable name="iHearings1" select="count(//cs:Hearings/cs:Hearing/cs:HearingDate)"/>
				<xsl:if test="$iHearings1>0">
					<xsl:text>Hearings and other important dates</xsl:text>
				</xsl:if>
			</u>
		</h4>
		<!-- this template outputs the hearing information along with key dates -->
		<table>
			<tr>
				<td>
					<xsl:variable name="iHearings2" select="count(//cs:Hearings/cs:Hearing[starts-with(./@PreliminaryHearing, 'y')]/cs:HearingDate)"/>
					<xsl:if test="$iHearings2>0">
						<!-- with a double test (above) the position is 2 not 1-->
						<xsl:text>Preliminary hearing on </xsl:text>
					</xsl:if>
					<xsl:for-each select="//cs:Hearings/cs:Hearing[starts-with(./@PreliminaryHearing, 'y')]">
						<xsl:sort select="./cs:HearingDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:HearingEndDate" data-type="text" order="ascending"/>
						<xsl:if test="./cs:HearingDate">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="./cs:HearingDate"/>
								<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
							</xsl:call-template>
							<xsl:if test="not(position()=last())">
								<xsl:text> and on </xsl:text>
							</xsl:if>
						</xsl:if>
						<!-- Assume same day hearing if the user enters an end date but not a start date, which is why the end date is in the start date for trialDateRange -->
						<xsl:if test="not(./cs:HearingDate)">
							<xsl:if test="./cs:HearingEndDate">
								<xsl:if test="position()=1">
									<xsl:text>Preliminary hearing on </xsl:text>
								</xsl:if>
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="./cs:HearingEndDate"/>
									<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
								</xsl:call-template>
								<xsl:if test="not(position()=last())">
									<xsl:text> and on </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</td>
			</tr>
			<!-- I am repeating this section because the preliminary and normal hearings were all being added to the same row making the details unreadable -->
			<tr>
				<td>
					<xsl:variable name="iHearings3" select="count(//cs:Hearings/cs:Hearing[starts-with(./@PreliminaryHearing, 'n')]/cs:HearingDate)"/>
					<xsl:if test="$iHearings3>0">
						<!-- with a double test (above) the position is 2 not 1-->
						<xsl:text>Hearing on </xsl:text>
					</xsl:if>
					<xsl:for-each select="//cs:Hearings/cs:Hearing[starts-with(./@PreliminaryHearing, 'n')]">
						<xsl:sort select="./cs:HearingDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:HearingEndDate" data-type="text" order="ascending"/>
						<xsl:if test="./cs:HearingDate">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="./cs:HearingDate"/>
								<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
							</xsl:call-template>
							<xsl:if test="not(position()=last())">
								<xsl:text> and on </xsl:text>
							</xsl:if>
						</xsl:if>
						<!-- Assume same day hearing if the user enters an end date but not a start date, which is why the end date is in the start date for trialDateRange -->
						<xsl:if test="not(./cs:HearingDate)">
							<xsl:if test="./cs:HearingEndDate">
								<xsl:if test="position()=1">
									<xsl:text>Hearing on </xsl:text>
								</xsl:if>
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="./cs:HearingEndDate"/>
									<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
								</xsl:call-template>
								<xsl:if test="not(position()=last())">
									<xsl:text> and on </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</td>
			</tr>
		</table>
		<table>
			<!-- First Fixed Trail Date -->
			<xsl:if test="//cs:Hearings/cs:FirstFixedTrialDate != '' ">
				<tr>
					<td height="25px">
						<xsl:text>1</xsl:text>
						<sup>st</sup>
						<xsl:text> Fixed on </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="//cs:Hearings/cs:FirstFixedTrialDate"/>
						</xsl:call-template>
						<xsl:text> (</xsl:text>
						<xsl:value-of select="//cs:Hearings/cs:FirstHearingType"/>
						<xsl:text>)</xsl:text>
					</td>
				</tr>
			</xsl:if>
			<!-- Arraigned On -->
			<tr>
				<td>
					<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[count(.| key('ArraignmentDate-by-IndCount', cs:ArraignmentDate)[1]) = 1]">
						<xsl:sort select="cs:ArraignmentDate" data-type="text" order="ascending"/>
						<xsl:if test="cs:ArraignmentDate">
							<xsl:variable name="ArraignmentHold" select="cs:ArraignmentDate"/>
							<xsl:text>Arraigned on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="cs:ArraignmentDate"/>
							</xsl:call-template>
							<xsl:text> (Count(s) </xsl:text>
							<xsl:for-each select="key('ArraignmentDate-by-IndCount', $ArraignmentHold)">
								<xsl:if test="cs:ArraignmentDate = $ArraignmentHold">
									<xsl:value-of select="@IndictmentNumber"/>/<xsl:value-of select="@IndictmentCountNumber"/>
									<xsl:if test="position()!=last()">,</xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:text>)</xsl:text>
							<xsl:if test="not(position()=last())">
								<xsl:text> and </xsl:text>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</td>
			</tr>
			<!-- Tried On -->
			<tr>
				<td>
					<xsl:variable name="iHearings4" select="count(//cs:Hearings/cs:Hearing/cs:HearingDate)"/>
					<xsl:if test="$iHearings4>0">
						<!-- with a double test (above) the position is 2 not 1-->
						<xsl:if test="//cs:RecordSheetHeader/cs:MethodOfInstigation='Sending'">
							<xsl:text>Dealt with on </xsl:text>
						</xsl:if>
						<xsl:if test="not(//cs:RecordSheetHeader/cs:MethodOfInstigation='Sending')">
							<xsl:text>Tried on </xsl:text>
						</xsl:if>
					</xsl:if>
					<xsl:for-each select="//cs:Hearings/cs:Hearing">
						<xsl:sort select="./cs:HearingDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:HearingEndDate" data-type="text" order="ascending"/>
						<xsl:if test="./cs:HearingDate">
							<xsl:choose>
								<xsl:when test="//cs:RecordSheetHeader/cs:MethodOfInstigation='Sending'">
									<!-- not necessary-->
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="./cs:HearingDate"/>
										<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
									</xsl:call-template>
									<xsl:if test="./cs:RelevantHearingdDates">
										<xsl:text> with relevant hearing dates </xsl:text>
										<xsl:value-of select="./cs:RelevantHearingdDates"/>
									</xsl:if>
									<xsl:if test="not(position()=last())">
										<xsl:text> and on </xsl:text>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="./@PreliminaryHearing='no'">
										<xsl:if test="position()=1">
											<!--											<xsl:text> Tried on </xsl:text>-->
										</xsl:if>
										<xsl:call-template name="trialDateRange">
											<xsl:with-param name="startDate" select="./cs:HearingDate"/>
											<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
										</xsl:call-template>
										<xsl:if test="./cs:RelevantHearingdDates">
											<xsl:text> with relevant hearing dates </xsl:text>
											<xsl:value-of select="./cs:RelevantHearingdDates"/>
										</xsl:if>
										<xsl:if test="not(position()=last())">
											<xsl:text> and on </xsl:text>
										</xsl:if>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<!-- Assume same day hearing if the user enters an end date but not a start date, which is why the end date is in the start date for trialDateRange -->
						<xsl:if test="not(./cs:HearingDate)">
							<xsl:if test="./cs:HearingEndDate">
								<xsl:choose>
									<xsl:when test="//cs:RecordSheetHeader/cs:MethodOfInstigation='Sending'">
										<xsl:text>Dealt with on </xsl:text>
										<xsl:call-template name="trialDateRange">
											<xsl:with-param name="startDate" select="./cs:HearingEndDate"/>
											<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
										</xsl:call-template>
										<xsl:if test="./cs:RelevantHearingdDates">
											<xsl:text> with relevant hearing dates </xsl:text>
											<xsl:value-of select="./cs:RelevantHearingdDates"/>
										</xsl:if>
										<xsl:if test="not(position()=last())">
											<xsl:text> and </xsl:text>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="./@PreliminaryHearing='no'">
											<xsl:text> Tried on </xsl:text>
											<xsl:call-template name="trialDateRange">
												<xsl:with-param name="startDate" select="./cs:HearingEndDate"/>
												<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
											</xsl:call-template>
											<xsl:if test="./cs:RelevantHearingDates">
												<xsl:text> with relevant hearing dates </xsl:text>
												<xsl:value-of select="./cs:RelevantHearingdDates"/>
											</xsl:if>
											<xsl:if test="not(position()=last())">
												<xsl:text> and </xsl:text>
											</xsl:if>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</td>
			</tr>
			<!-- Convicted On -->
			<tr>
				<td>
					<xsl:for-each select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[count(.| key('ConvictionDate-by-IndCount', cs:ConvictionDate)[1]) = 1]">
						<xsl:sort select="cs:ConvictionDate" data-type="text" order="ascending"/>
						<xsl:if test="cs:ConvictionDate">
							<xsl:variable name="ConvictedHold" select="cs:ConvictionDate"/>
							<xsl:text>Convicted on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="cs:ConvictionDate"/>
							</xsl:call-template>
							<xsl:text> (Count(s) </xsl:text>
							<xsl:for-each select="key('ConvictionDate-by-IndCount', $ConvictedHold)">
								<xsl:if test="cs:ConvictionDate= $ConvictedHold">
									<xsl:value-of select="@IndictmentNumber"/>/<xsl:value-of select="@IndictmentCountNumber"/>
									<xsl:if test="position()!=last()">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:text>)</xsl:text>
							<xsl:if test="not(position()=last())">
								<xsl:text> and </xsl:text>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</td>
			</tr>
			<!-- END OF CR44 Changes -->
			<!-- Sentence or Order Made -->
			<tr>
				<td>
					<xsl:if test="cs:SentencePostponed">
						<xsl:text>Sentence postponed until </xsl:text>
						<xsl:choose>
							<xsl:when test="//cs:SentencePostponed/cs:SentencePostponedToDate">
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="//cs:SentencePostponed/cs:SentencePostponedToDate"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
                                a date to be fixed
                            </xsl:otherwise>
						</xsl:choose>
						<xsl:if test="starts-with(//cs:SentencePostponed/cs:SentenceReports, 'y')">
                            for reports
                        </xsl:if>
					</xsl:if>
				</td>
			</tr>
			<xsl:if test="//cs:SentenceDeferredToDate">
				<tr>
					<td>
						<xsl:text>Sentence deferred until </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="//cs:SentenceDeferredToDate"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="/cs:TrialRecordSheet/cs:DateSentenceOrOrderMade">
				<tr>
					<td>
						<xsl:text>Sentence/Order made on </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="/cs:TrialRecordSheet/cs:DateSentenceOrOrderMade"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="//cs:LinkedCases">
				<xsl:variable name="linkedCaseLimit">
					<xsl:value-of select="16"/>
				</xsl:variable>
				<tr>
					<td>
						<!-- RFC1745: Only print the first "$linkedCaseLimit" case numbers -->
						<xsl:for-each select="//cs:LinkedCases/cs:CaseNumber">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:text>See also </xsl:text>
									<xsl:value-of select="."/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="not(position() &gt; $linkedCaseLimit)">
										<xsl:text>, </xsl:text>
										<xsl:value-of select="."/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
						<xsl:if test="count(//cs:LinkedCases/cs:CaseNumber) > $linkedCaseLimit">
							<xsl:text> and others...</xsl:text>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
		</table>
		<hr/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- counts Template                    -->
	<!-- **************************************** -->
	<doc:template name="counts" xmlns="">
		<refpurpose>Outputs information about the charges, pleas etc.</refpurpose>
		<refdescription>
			<para>Shows the details for each count: the offence and any sentence or order, any plea and the verdict</para>
		</refdescription>
	</doc:template>
	<xsl:template name="counts">
		<!-- outputs information about the charges, pleas etc  -->
		<h4>
			<u>
				<xsl:text>Indictment Charges</xsl:text>
			</u>
		</h4>
		<table>
			<tr>
				<td width="10%">
					<strong>
						<xsl:text>Count</xsl:text>
					</strong>
				</td>
				<td width="40%">
					<strong>
						<xsl:text>Offences and Sentence/Order</xsl:text>
					</strong>
				</td>
				<td width="10%">
					<strong>
						<xsl:text>Seq</xsl:text>
					</strong>
				</td>
				<td width="15%">
					<strong>
						<xsl:text>Plea</xsl:text>
					</strong>
				</td>
				<td width="25%">
					<strong>
						<xsl:text>Verdict</xsl:text>
					</strong>
				</td>
			</tr>
			<xsl:for-each select="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'indictment']">
				<tr>
					<td valign="top">
						<!-- KN 2005-01-31 - Added for CR44 PR's -->
						<xsl:value-of select="./@IndictmentNumber"/>
						<xsl:text>/</xsl:text>
						<!-- KN 2005-01-31 - End of AdditionAdded for CR44 PR's -->
						<xsl:value-of select="./@IndictmentCountNumber"/>
					</td>
					<td valign="top">
						<xsl:value-of select="./@CJSoffenceCode"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="./cs:OffenceStatement"/>
					</td>
					<td valign="top">
						<!-- Altered to accomodate no ASN number in CREST and therefore there would be short CRN -->
						<xsl:choose>
							<xsl:when test="string-length(./cs:CRN)=3">
								<xsl:value-of select="./cs:CRN"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="substring(./cs:CRN,21,3)"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td valign="top">
						<xsl:value-of select="./cs:Plea"/>
					</td>
					<td valign="top">
						<xsl:value-of select="./cs:Verdict"/>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<br/>
					</td>
				</tr>
				<tr>
					<td/>
					<td colspan="3">
						<strong>Offence Location Address</strong>
					</td>
				</tr>
				<tr>
					<td/>
					<td colspan="1">
						<xsl:call-template name="buildOffenceLocationAddress">
							<xsl:with-param name="offenceLocation" select="./cs:OffenceLocation"/>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td/>
					<td colspan="3">
						<table>
							<tr>
								<td>Force Location Code:</td>
								<td align="right">
									<xsl:value-of select="./cs:ArrestingPoliceForceCode"/>
								</td>
							</tr>
							<tr>
								<td>Offence Start Date:</td>
								<td align="right">
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="substring(./cs:OffenceStartDateTime,1,10)"/>
									</xsl:call-template>
								</td>
							</tr>
							<tr>
								<td>Offence Start Time:</td>
								<td align="right">
									<xsl:choose>
										<xsl:when test="substring(./cs:OffenceStartDateTime,12,5) != '00:00' ">
											<xsl:value-of select="substring(./cs:OffenceStartDateTime,12,5)"/>
										</xsl:when>
									</xsl:choose>
								</td>
							</tr>
							<tr>
								<td>Offence End Date:</td>
								<td align="right">
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="substring(./cs:OffenceEndDateTime,1,10)"/>
									</xsl:call-template>
								</td>
							</tr>
							<tr>
								<td>Offence End Time:</td>
								<td align="right">
									<xsl:choose>
										<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
											<xsl:value-of select="substring(./cs:OffenceEndDateTime,12,5)"/>
										</xsl:when>
									</xsl:choose>
								</td>
							</tr>
							<tr>
								<td>Offence committed on Bail:</td>
								<td align="right">
									<xsl:choose>
										<!-- Modified for Bichard PR 59706 - Luis Valenzuela -->
										<xsl:when test="./cs:CommittedOnBail = 'yes' ">
											<xsl:text>Y</xsl:text>
										</xsl:when>
										<xsl:when test="./cs:CommittedOnBail = 'no' ">
											<xsl:text>N</xsl:text>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td/>
					<td colspan="4">
						<strong>
							<xsl:if test="./cs:Disposals">
								<xsl:for-each select="./cs:Disposals/cs:Disposal">
									<xsl:if test=". != ''">
										<xsl:choose>
											<xsl:when test="position() = last()">
												<xsl:call-template name="replace">
													<xsl:with-param name="string">
												<xsl:call-template name="lineBreak">
													<xsl:with-param name="text" select="."/>
												</xsl:call-template>
													</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="replace">
													<xsl:with-param name="string">
												<xsl:call-template name="lineBreak">
													<xsl:with-param name="text" select="."/>
												</xsl:call-template>
													</xsl:with-param>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
										<br/>
									</xsl:if>
								</xsl:for-each>
							</xsl:if>
						</strong>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<br/>
					</td>
				</tr>
			</xsl:for-each>
			<!-- amended for PR 59709 	 
             <tr>
                 <td/>
                 <td colspan="4"><strong><xsl:value-of select="//cs:IndictmentComments"/></strong></td>
             </tr>            
			 -->
		</table>
		<!-- RFS4224 Hate Crime -->
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:HateCrime">
			<xsl:call-template name="util:HateCrime"/>
		</xsl:if>
		<xsl:if test="//cs:IndictmentComments">
			<h4>
				<u>
					<xsl:text>Indictment Log</xsl:text>
				</u>
			</h4>
			<xsl:value-of select="//cs:IndictmentComments"/>
		</xsl:if>
		<hr/>
	</xsl:template>
	<!-- PR5459 & 5458 - fixed by Tom Muir-Webb -->
	<!-- **************************************** -->
	<!-- replace string template           -->
	<!-- **************************************** -->
	<xsl:template name="replace">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="not($string)"/>
			<xsl:when test="contains($string, '~~~eol~~~')">
				<xsl:value-of select="substring-before($string, '~~~eol~~~')"/>
				<br/>
				<dd/>
				<xsl:call-template name="replace">
					<xsl:with-param name="string" select="substring-after($string, '~~~eol~~~')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="replace1">
					<xsl:with-param name="string" select="$string"/>
				</xsl:call-template>
				<!--<xsl:value-of select="$string" />-->
				<br/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- PR5458 - Tom Muir-Webb-->
	<xsl:template name="replace1">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="not($string)"/>
			<xsl:when test="contains($string, 'This disposal has been amended')">
				<xsl:value-of select="substring-before($string, 'This disposal has been amended')"/>
				<br/>This disposal has been amended
      <xsl:call-template name="replace">
					<xsl:with-param name="string" select="substring-after($string, 'This disposal has been amended')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$string"/>
				<br/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="indictmentlog">
		<h4>
			<u>
				<xsl:text>Indictment Log</xsl:text>
			</u>
		</h4>
	</xsl:template>
	<!-- **************************************** -->
	<!-- otherOrders Template           -->
	<!-- **************************************** -->
	<doc:template name="otherOrders" xmlns="">
		<refpurpose>Outputs information about any other orders there might be.</refpurpose>
		<refdescription>
			<para>Show details of others orders that there might be</para>
		</refdescription>
	</doc:template>
	<xsl:template name="otherOrders">
		<!-- outputs information about any other orders  -->
		<xsl:variable name="totalSentenceTypesList" select="' TIMP TSUSP TORD TFINE TCOSTS TCOMP TPAY TPP TDISQ '"/>
		<xsl:if test="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[not (contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' ')))]">
			<h4>
				<u>
					<xsl:text>Other Orders</xsl:text>
				</u>
			</h4>
			<table>
				<tr>
					<td>
						<xsl:for-each select="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[not (contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' ')))]">
							<xsl:for-each select="cs:DisposalLine">
								<xsl:value-of select="cs:Data"/>
								<xsl:text> </xsl:text>
							</xsl:for-each>
							<br/>
						</xsl:for-each>
					</td>
				</tr>
			</table>
			<hr/>
		</xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason != ''">
			<xsl:call-template name="util:DeportationText"/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- originalCharges Template           -->
	<!-- **************************************** -->
	<doc:template name="originalCharges" xmlns="">
		<refpurpose>Outputs information about original charges.</refpurpose>
		<refdescription>
			<para>Show any original charges</para>
		</refdescription>
	</doc:template>
	<xsl:template name="originalCharges" xmlns="">
		<xsl:if test="//cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:OriginalCharges">
			<h4>
				<u>
					<!-- Change below, PR5575 "Original Charges" changes to "Original Magistrate Offences" BP 27082008-->
					<xsl:text>Original Magistrate Offences</xsl:text>
				</u>
			</h4>
			<table width="100%">
				<tr>
					<td width="40%">
						<!-- Change below, PR5575 Offence changes to Charge BP 27082008-->
						<strong>Charge Description</strong>
					</td>
					<td width="10%">
						<strong>Seq</strong>
					</td>
					<td width="50%">
						<strong>Disposed Details</strong>
					</td>
				</tr>
				<xsl:for-each select="//cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:OriginalCharges/cs:OriginalCharge">
					<tr>
						<td valign="top">
							<xsl:value-of select="./cs:OriginalChargeDescription"/>
						</td>
						<td valign="top">
							<xsl:value-of select="./cs:OriginalChargeSequenceNumber"/>
						</td>
						<td valign="top">
							<xsl:for-each select="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
								<!--<xsl:value-of select="."/>-->
								<!-- PR5458 - Tom Muir-Webb-->
								<xsl:call-template name="replace">
									<xsl:with-param name="string" select="."/>
								</xsl:call-template>
								<br/>
							</xsl:for-each>
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- summary Template               -->
	<!-- **************************************** -->
	<doc:template name="summary" xmlns="">
		<refpurpose>Outputs Summary offences section.</refpurpose>
		<refdescription>
			<para>Shows the details for each count: the offence and any sentence or order, any plea and the verdict</para>
		</refdescription>
	</doc:template>
	<xsl:template name="summary">
		<xsl:if test="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'summary']">
			<!-- RFC1745: Case number must be in the 7000 range, e.g. T20067001 -->
			<xsl:choose>
				<xsl:when test="substring(/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:CaseNumber,6,1) = '7'">
					<h4>
						<xsl:text>Offences under Section 41 (1) (b) Crime and Disorder Act 1998</xsl:text>
					</h4>
				</xsl:when>
				<xsl:otherwise>
					<h4>
						<xsl:text>Offences under Section 41 Criminal Justices Act 1988</xsl:text>
					</h4>
				</xsl:otherwise>
			</xsl:choose>
			<table width="100%">
				<tr>
					<td width="10%">
						<strong>
							<xsl:text>No</xsl:text>
						</strong>
					</td>
					<td width="40%">
						<strong>
							<xsl:text>Offences and Sentence/Order</xsl:text>
						</strong>
					</td>
					<td width="10%">
						<strong>
							<xsl:text>Seq</xsl:text>
						</strong>
					</td>
					<td width="40%">
						<strong>
							<xsl:text>Plea</xsl:text>
						</strong>
					</td>
				</tr>
				<xsl:for-each select="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'summary']">
					<xsl:sort select="substring(./cs:CRN,21,3)" data-type="text" order="ascending"/>
					<xsl:sort select="./@IndictmentCountNumber" data-type="number" order="ascending"/>
					<tr>
						<td valign="top">
							<xsl:value-of select="./@IndictmentCountNumber"/>
						</td>
						<td valign="top">
							<xsl:if test="./@CJSoffenceCode">
								<xsl:value-of select="./@CJSoffenceCode"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="./cs:OffenceStatement"/>
							</xsl:if>
							<xsl:if test="not(./@CJSoffenceCode)">
								<xsl:value-of select="./cs:OffenceStatement"/>
							</xsl:if>
						</td>
						<td>
							<xsl:value-of select="substring(./cs:CRN,21,3)"/>
						</td>
						<td valign="top">
							<xsl:value-of select="./cs:Plea"/>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<br/>
						</td>
					</tr>
					<tr>
						<td/>
						<td colspan="3">
							<strong>Offence Location Address</strong>
						</td>
					</tr>
					<tr>
						<td/>
						<td colspan="1">
							<xsl:call-template name="buildOffenceLocationAddress">
								<xsl:with-param name="offenceLocation" select="./cs:OffenceLocation"/>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td/>
						<td colspan="3">
							<table>
								<tr>
									<td>Force Location Code:</td>
									<td align="right">
										<xsl:value-of select="./cs:ArrestingPoliceForceCode"/>
									</td>
								</tr>
								<tr>
									<td>Offence Start Date:</td>
									<td align="right">
										<xsl:call-template name="util:ukdate_mon">
											<xsl:with-param name="inDate" select="substring(./cs:OffenceStartDateTime,1,10)"/>
										</xsl:call-template>
									</td>
								</tr>
								<tr>
									<td>Offence Start Time:</td>
									<td align="right">
										<xsl:choose>
											<xsl:when test="substring(./cs:OffenceStartDateTime,12,5) != '00:00' ">
												<xsl:value-of select="substring(./cs:OffenceStartDateTime,12,5)"/>
											</xsl:when>
										</xsl:choose>
									</td>
								</tr>
								<tr>
									<td>Offence End Date:</td>
									<td align="right">
										<xsl:call-template name="util:ukdate_mon">
											<xsl:with-param name="inDate" select="substring(./cs:OffenceEndDateTime,1,10)"/>
										</xsl:call-template>
									</td>
								</tr>
								<tr>
									<td>Offence End Time:</td>
									<td align="right">
										<xsl:choose>
											<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
												<xsl:value-of select="substring(./cs:OffenceEndDateTime,12,5)"/>
											</xsl:when>
										</xsl:choose>
									</td>
								</tr>
								<tr>
									<td>Offence committed on Bail?</td>
									<td align="right">
										<xsl:choose>
											<!-- Modified for Bichard PR 59706 - Luis Valenzuela -->
											<xsl:when test="./cs:CommittedOnBail = 'yes' ">
												<xsl:text>Y</xsl:text>
											</xsl:when>
											<xsl:when test="./cs:CommittedOnBail = 'no' ">
												<xsl:text>N</xsl:text>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td/>
						<td colspan="3">
							<strong>
								<xsl:call-template name="util:decodeDuration">
									<xsl:with-param name="duration" select="cs:SentenceTerm"/>
								</xsl:call-template>
								<xsl:if test="./cs:Disposals">
									<xsl:for-each select="./cs:Disposals/cs:Disposal">
										<xsl:choose>
											<xsl:when test="starts-with(.,'Imprisonment')">
												<xsl:call-template name="replace">
													<xsl:with-param name="string">
												<xsl:call-template name="str:to-lower">
													<xsl:with-param name="text" select="."/>
												</xsl:call-template>
													</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="replace">
													<xsl:with-param name="string">
												<xsl:value-of select="."/>
													</xsl:with-param>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
										<br/>
									</xsl:for-each>
								</xsl:if>
								<xsl:text> </xsl:text>
								<xsl:value-of select="./cs:TermType"/>
							</strong>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<br/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- breach Template                    -->
	<!-- **************************************** -->
	<doc:template name="breach" xmlns="">
		<refpurpose>Outputs details of breaches of previous orders..</refpurpose>
		<refdescription>
			<para>Shows the details of the original order - date, who made it, the offence and details
                of the breach and any new sentence or order arising as a result of the breach.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="breach">
		<xsl:if test="/cs:TrialRecordSheet/cs:Breaches">
			<h4>
				<u>
					<xsl:text>Breaches of Previous Orders</xsl:text>
				</u>
			</h4>
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
				<xsl:for-each select="/cs:TrialRecordSheet/cs:Breaches/cs:Breach">
					<xsl:variable name="breachNo" select="./@CRESTbreachNumber"/>
					<xsl:variable name="breachType" select="./@BreachType"/>
					<tr>
						<td valign="top">
							<xsl:value-of select="position()"/>
						</td>
						<td valign="top">
							<xsl:if test="$breachType != 'F'">
								<xsl:if test="starts-with(./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType,'C')">
									<xsl:value-of select="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType"/>
									<xsl:text> at </xsl:text>
								</xsl:if>
								<xsl:value-of select="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseName"/>
							</xsl:if>
						</td>
						<td valign="top">
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="./cs:OriginatingCourt/cs:Date"/>
							</xsl:call-template>
						</td>
						<td valign="top">
							<xsl:value-of select="./cs:OriginatingCourt/cs:OriginalOrderType"/>
							<xsl:text>, total sentence </xsl:text>
							<xsl:value-of select="./cs:OriginalSentence"/>
							<xsl:text>, put and </xsl:text>
							<xsl:call-template name="breachAdmitted">
								<xsl:with-param name="admitted" select="./cs:Admitted"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="./cs:DatePut"/>
							</xsl:call-template>
						</td>
					</tr>
					<!-- Modified for Bichard PR 5352 - Tom Muir-Webb -->
					<tr>
						<td valign="top" colspan="4"/>
					</tr>
					<tr>
						<td valign="top" colspan="3"/>
						<td valign="top">
							<!-- outputs information about the breach charges  -->
							<!-- Extra space  -->
							<h4>
								<xsl:text/>
							</h4>
							<table width="100%">
								<xsl:for-each select="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach' or ./@ChargeType = 'BAO']">
									<xsl:if test="./@IndictmentNumber = $breachNo">
										<tr>
											<td width="80%" colspan="1" valign="top">
												<xsl:text>Original Offences and new sentence/order:</xsl:text>
											</td>
											<td width="20%" colspan="1" valign="top" align="right">
												<xsl:text>Seq</xsl:text>
											</td>
										</tr>
										<tr>
											<td colspan="2" height="10"/>
										</tr>
										<tr>
											<td colspan="1" valign="top">
												<xsl:value-of select="./@CJSoffenceCode"/>
												<xsl:text> </xsl:text>
												<xsl:value-of select="./cs:OffenceStatement"/>
											</td>
											<td colspan="1" valign="top" align="right">
												<xsl:if test="$breachType != 'F'">
													<xsl:value-of select="substring(./cs:CRN,21,3)"/>
												</xsl:if>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<table width="100%">
													<tr>
														<td width="60%" valign="top"/>
														<td width="40%"/>
													</tr>
													<tr>
														<td colspan="2">
															<br/>
															<xsl:text>Offence Location Address: </xsl:text>
															<br/>
														</td>
													</tr>
													<tr>
														<td colspan="2" height="10"/>
													</tr>
													<tr>
														<td colspan="2">
															<xsl:for-each select="./cs:OffenceLocation/apd:Line">
																<xsl:value-of select="."/>
																<xsl:if test="not(position()=last())">
																	<xsl:text>, </xsl:text>
																</xsl:if>
															</xsl:for-each>
															<xsl:if test="./cs:OffenceLocation/apd:PostCode">
																<xsl:text>, </xsl:text>
																<xsl:value-of select="./cs:OffenceLocation/apd:PostCode"/>
															</xsl:if>
															<br/>
															<br/>
														</td>
													</tr>
													<tr>
														<td colspan="1">
															<xsl:text>Force Location Code: </xsl:text>
														</td>
														<td colspan="1">
															<xsl:value-of select="./cs:ArrestingPoliceForceCode"/>
														</td>
													</tr>
													<tr>
														<td colspan="1">
															<xsl:text>Offence Start Date: </xsl:text>
														</td>
														<td colspan="1">
															<xsl:call-template name="util:ukdate_mon">
																<xsl:with-param name="inDate" select="./cs:OffenceStartDateTime"/>
															</xsl:call-template>
														</td>
													</tr>
													<tr>
														<td colspan="1">
															<xsl:text>Offence Start Time: </xsl:text>
														</td>
														<td colspan="1">
															<xsl:choose>
																<xsl:when test="substring(./cs:OffenceStartDateTime,12,5) != '00:00' ">
																	<xsl:value-of select="substring(./cs:OffenceStartDateTime,12,5)"/>
																</xsl:when>
															</xsl:choose>
														</td>
													</tr>
													<tr>
														<td colspan="1">
															<xsl:text>Offence End Date: </xsl:text>
														</td>
														<td colspan="1">
															<xsl:call-template name="util:ukdate_mon">
																<xsl:with-param name="inDate" select="./cs:OffenceEndDateTime"/>
															</xsl:call-template>
														</td>
													</tr>
													<tr>
														<td colspan="1">
															<xsl:text>Offence End Time: </xsl:text>
														</td>
														<td colspan="1">
															<xsl:choose>
																<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
																	<xsl:value-of select="substring(./cs:OffenceEndDateTime,12,5)"/>
																</xsl:when>
															</xsl:choose>
														</td>
													</tr>
													<tr>
														<td colspan="2" height="20"/>
													</tr>
													<tr>
														<td colspan="1">
															<xsl:text>Offence Committed On Bail: </xsl:text>
														</td>
														<td colspan="1">
															<xsl:choose>
																<!-- Modified for Bichard PR 59706 - Luis Valenzuela -->
																<!-- Modified for Bichard Ben Payne-->
																<xsl:when test="./cs:CommittedOnBail = 'yes' ">
																	<xsl:text>Y</xsl:text>
																</xsl:when>
																<xsl:when test="./cs:CommittedOnBail = 'no' ">
																	<xsl:text>N</xsl:text>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:text/>
																</xsl:otherwise>
															</xsl:choose>
														</td>
													</tr>
													<tr>
														<td colspan="2">
															<br/>
															<strong>
																<xsl:if test="./cs:Disposals">
																	<xsl:for-each select="./cs:Disposals/cs:Disposal">
																		<xsl:choose>
																			<xsl:when test="starts-with(.,'Imprisonment')">
																				<xsl:call-template name="replace">
																					<xsl:with-param name="string">
																				<xsl:call-template name="str:to-lower">
																					<xsl:with-param name="text" select="."/>
																				</xsl:call-template>
																					</xsl:with-param>
																				</xsl:call-template>
																			</xsl:when>
																			<xsl:otherwise>
																				<xsl:call-template name="replace">
																					<xsl:with-param name="string">
																				<xsl:value-of select="."/>
																					</xsl:with-param>
																				</xsl:call-template>
																			</xsl:otherwise>
																		</xsl:choose>
																		<br/>
																	</xsl:for-each>
																	<br/>
																</xsl:if>
															</strong>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</xsl:if>
								</xsl:for-each>
							</table>
							<!-- / end modification for Bichard PR 5352 - Tom Muir-Webb -->
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="breachAdmitted">
		<!-- Outputs "admitted" or "not admitted" depending on parameter -->
		<xsl:param name="admitted"/>
		<xsl:variable name="result">
        </xsl:variable>
		<xsl:choose>
			<xsl:when test="$admitted='yes'">
				<xsl:text>admitted</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>not admitted</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:copy-of select="$result"/>
	</xsl:template>
	<!-- **************************************** -->
	<!-- takenIntoConsideration Template    -->
	<!-- **************************************** -->
	<doc:template name="takenIntoConsideration" xmlns="">
		<refpurpose>Outputs TIC information.</refpurpose>
		<refdescription>
			<para>Show the number of offences taken into consideration</para>
		</refdescription>
	</doc:template>
	<xsl:template name="takenIntoConsideration">
		<xsl:if test="/cs:TrialRecordSheet/cs:NumberOfOffencesTIC > '0'">
			<table>
				<tr>
					<td>
						<strong>
							<xsl:text>Offences admitted and taken into consideration: </xsl:text>
						</strong>
						<xsl:value-of select="/cs:TrialRecordSheet/cs:NumberOfOffencesTIC"/>
					</td>
				</tr>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- totalSentence Template             -->
	<!-- **************************************** -->
	<doc:template name="totalSentence" xmlns="">
		<refpurpose>Outputs information about the total sentence.</refpurpose>
		<refdescription>
			<para>Show the total sentence</para>
		</refdescription>
	</doc:template>
	<xsl:template name="totalSentence">
		<xsl:variable name="totalSentenceTypesList" select="' TIMP TSUSP TORD TFINE TCOSTS TCOMP TPAY TPP TDISQ '"/>
		<xsl:if test="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' '))]">
			<h4>
				<u>
					<xsl:text>Total Sentence</xsl:text>
				</u>
			</h4>
			 <table>
				<xsl:for-each select="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' '))]">
					<tr>
						<td>
							<xsl:for-each select="cs:DisposalLine">
								<xsl:value-of select="cs:Data"/>
								<xsl:text> </xsl:text>
							</xsl:for-each>
							<br/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- specialCircumstances Template      -->
	<!-- **************************************** -->
	<doc:template name="specialCircumstances" xmlns="">
		<refpurpose>Outputs information about special circumstances.</refpurpose>
		<refdescription>
			<para>Show any speacial circumstances</para>
		</refdescription>
	</doc:template>
	<xsl:template name="specialCircumstances">
		<xsl:if test="starts-with(/cs:TrialRecordSheet/cs:SpecialCircumstances, 'y')">
			<table>
				<tr>
					<td>
						<strong>
							<xsl:text>The defendant was eligible for a mandatory sentence under </xsl:text>
							<xsl:text>The Crime and Sentence ACT 1997 </xsl:text>
							<xsl:text>but special circumstances were found and such a sentence was not imposed</xsl:text>
						</strong>
					</td>
				</tr>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- decisionOfTheCourtOfAppeal Template    -->
	<!-- **************************************** -->
	<doc:template name="decisionOfTheCourtOfAppeal" xmlns="">
		<refpurpose>Outputs decision of the court of appeal.</refpurpose>
		<refdescription>
			<para>Show the decision of the court of appeal.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="decisionOfTheCourtOfAppeal">
		<xsl:if test="/cs:TrialRecordSheet/cs:CourtOfAppealResult">
			<table>
				<tr>
					<td>
						<strong>
							<xsl:text>Decision of the Court of Appeal (Criminal Division)</xsl:text>
						</strong>
						<br/>
						<br/>
						<xsl:value-of select="/cs:TrialRecordSheet/cs:CourtOfAppealResult"/>
					</td>
				</tr>
			</table>
			<hr/>
		</xsl:if>
	</xsl:template>
	<!-- **************************************** -->
	<!-- trialDateRange Template            -->
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
		<xsl:param name="startDate"/>
		<xsl:param name="endDate"/>
		<xsl:variable name="result">
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="$startDate"/>
			</xsl:call-template>
			<xsl:text>  </xsl:text>
			<!--			<xsl:if test="not($endDate='')">-->
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
			<!--			</xsl:if>-->
		</xsl:variable>
		<xsl:copy-of select="$result"/>
	</xsl:template>
	<xsl:template name="representativeDescriptionForAdvocate">
		<!-- Outputs QC, Sol-Adv, Sol-Ord or nothing depending on the parameters passed in -->
		<xsl:param name="qc"/>
		<xsl:param name="advocateType"/>
		<xsl:param name="startDate"/>
		<xsl:variable name="Date">
			<xsl:call-template name="dateToNumber">
				<xsl:with-param name="Date" select="$startDate"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="result">
        </xsl:variable>
		<xsl:choose>
			<xsl:when test="starts-with($qc, 'y')">
				<xsl:choose>
					<xsl:when test="$Date &lt; 20220909">
						<xsl:text>QC</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>KC</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="starts-with($advocateType, 'y')">
						<xsl:text>Sol-Adv</xsl:text>
					</xsl:when>
					<xsl:when test="starts-with($advocateType, 'n')">
						<xsl:text>Sol-Ord</xsl:text>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:copy-of select="$result"/>
	</xsl:template>
	<xsl:template name="representativeDescriptionForSolicitor">
		<!-- Outputs Rep Ord, Private or nothing depending on the parameters passed in -->
		<xsl:param name="representativeType"/>
		<xsl:variable name="result">
        </xsl:variable>
		<xsl:choose>
			<xsl:when test="$representativeType='Legal Aid'">
				<xsl:text>(Legal Aid)</xsl:text>
			</xsl:when>
			<xsl:when test="$representativeType='Private'">
				<xsl:text>(Private)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:copy-of select="$result"/>
	</xsl:template>
	<xsl:template name="buildOffenceLocationAddress">
		<!-- returns address and post code all on one line -->
		<xsl:param name="offenceLocation"/>
		<xsl:variable name="addr">
			<xsl:for-each select="$offenceLocation/apd:Line[not ( .='-') and not (. = ' ')]">
				<xsl:value-of select="."/>
				<xsl:if test="not (position() = last())">
					<xsl:choose>
						<xsl:when test="string-length() &gt; 0">
							<xsl:text>, </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="$offenceLocation/apd:PostCode">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="$offenceLocation/apd:PostCode"/>
			</xsl:if>
		</xsl:variable>
		<xsl:copy-of select="$addr"/>
	</xsl:template>
	<xsl:template name="dateToNumber">
		<!-- Returns a date as a 8 digit number -->
		<xsl:param name="Date"/>
		<xsl:variable name="Year">
			<xsl:value-of select="substring($Date, 1, 4)" />
		</xsl:variable>
		<xsl:variable name="Month">
			<xsl:value-of select="substring($Date, 6,2)" />
		</xsl:variable>
		<xsl:variable name="Day">
			<xsl:value-of select="substring($Date, 9,2)" />
		</xsl:variable>
		<xsl:variable name="DateNumber">
			<xsl:value-of select="$Year"/>
			<xsl:value-of select="$Month"/>
			<xsl:value-of select="$Day"/>
		</xsl:variable>
		<xsl:copy-of select="$DateNumber"/>
	</xsl:template>
</xsl:stylesheet>
