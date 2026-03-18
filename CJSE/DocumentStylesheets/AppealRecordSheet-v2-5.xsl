<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                                 +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 2-5</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Appeal Record Sheet Stylesheet</title>
		<para>File name : AppealRecordSheet-v2-5.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Appeal Record Sheet in html format</para>
			</section>
		</partintro>
	</doc:reference>
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'2'"/>
	<xsl:variable name="minorVersion" select="'4'"/>
	<xsl:variable name="stylesheet" select="'AppealRecordSheet-v2-5.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2007-10-18'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<!-- end Global Variables -->
	<xsl:output method="html" indent="yes"/>
	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
				<xsl:call-template name="header"/>
				<xsl:call-template name="appellant"/>
				<xsl:call-template name="respondent"/>
				<xsl:call-template name="courtReporting"/>
				<xsl:call-template name="judiciary"/>
				<xsl:call-template name="bailOrCustody"/>
				<xsl:call-template name="hearings"/>
				<!-- the following line added for PR5554 -insetion of page break - NOTE not all browser/printer will recognise this as it is an optional rule -->
				<div style="page-break-before:always">
					<span style="visibility: hidden">-</span>
				</div>
				<xsl:call-template name="counts"/>
				<xsl:call-template name="otherOrders"/>
				<xsl:call-template name="reasonsForDecision"/>
				<!-- KN 20050517 - CR27     -->
				<xsl:call-template name="util:copyrightText"/>
			</body>
		</html>
	</xsl:template>
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the Report Header information - including the Appellant and court details.</refpurpose>
	</doc:template>
	<xsl:template name="header">
		<!-- processes the Header information - appellant and court -->
		<xsl:variable name="personal" select="//cs:RecordSheetHeader/cs:Defendant/cs:PersonalDetails"/>
		<h2>
			<center>
				<xsl:value-of select="'Appeal Record Sheet'"/>
			</center>
		</h2>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="65%">
					<strong>
						<xsl:text>Appellant </xsl:text>
					</strong>
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
				</td>
				<td WIDTH="35%">
					<strong>
						<xsl:text>Appellant No.</xsl:text>
					</strong>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:CaseNumber"/>
					<xsl:text>-</xsl:text>
					<xsl:text>1/1</xsl:text>
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
			<tr/>
			<tr>
				<td>
					<strong>
						<xsl:text>Address</xsl:text>
					</strong>
				</td>
				<td>
					<!-- Added URN for CR49 -->
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
					<!-- Added URN for CR49 -->
					<xsl:text>PNC No: </xsl:text>
					<xsl:variable name="pnc" select="//cs:RecordSheetHeader/cs:Defendant/cs:PNCnumber"/>
					<xsl:value-of select="substring($pnc,3,10)"/>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:if test="$personal/cs:Nationality">
						<strong>
							<xsl:text>Nationality</xsl:text>
						</strong>
					</xsl:if>
				</td>
				<td>
					<!-- Added URN for CR49 -->
					<xsl:text>ASN No: </xsl:text>
					<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:ASNs/cs:ASN"/>
				</td>
			</tr>
			<xsl:if test="$personal/cs:Nationality">
				<tr>
					<td>
						<xsl:value-of select="$personal/cs:Nationality"/>
					</td>
				</tr>
			</xsl:if>
		</table>
		<!-- CCN400 START -->
		<hr/>
		<!-- now give out the court information -->
		<strong>
			<xsl:text>Before the </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseType"/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseName"/>
		</strong>
		<br/>
		<xsl:if test="//cs:RecordSheetHeader/cs:MagistratesCourt">
			<xsl:text>Appeal from a decision of </xsl:text>
			<xsl:value-of select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName"/>
			<xsl:if test="//cs:RecordSheetHeader/cs:DateOfInstigation">
				<xsl:text> on </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
				</xsl:call-template>
			</xsl:if>
			<br/>
		</xsl:if>
		<xsl:if test="//cs:OriginalSentenceOrOrderDate">
			<xsl:text>Date of original sentence/order: </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:OriginalSentenceOrOrderDate"/>
			</xsl:call-template>
			<br/>
		</xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:TransferInCourt">
			<xsl:variable name="transferCourtHouseName" select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseName"/>
			<xsl:text>Transferred from </xsl:text>
			<xsl:if test="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseType = 'Crown Court'">
				<xsl:text> the </xsl:text>
				<xsl:value-of select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseType"/>
				<xsl:text> at </xsl:text>
			</xsl:if>
			<xsl:call-template name="str:capitalise">
				<xsl:with-param name="text" select="$transferCourtHouseName"/>
			</xsl:call-template>
			<xsl:text> on </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:DateOfTransferIn"/>
			</xsl:call-template>
			<br/>
		</xsl:if>
		<xsl:if test="//cs:BenchWarrantDate">
			<xsl:text>Bench Warrant executed on </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:BenchWarrantDate"/>
			</xsl:call-template>
			<br/>
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
	<xsl:template name="appellant">
		<!-- this template outputs the defence team -->
		<xsl:variable name="appellantAdvocates" select="//cs:AppellantAdvocates"/>
		<xsl:variable name="appellantSolicitors" select="//cs:AppellantSolicitors"/>
		<h4>
			<xsl:text>Appellant</xsl:text>
		</h4>
		<!-- First do the advocates -->
		<strong>
			<xsl:text>Advocate</xsl:text>
		</strong>
		<br/>
		<xsl:if test="$appellantAdvocates/cs:AppellantAdvocate">
			<table width="90%">
				<!-- AppellantAdvocate Advocates using representativeDescriptionForAdvocate template -->
				<xsl:for-each select="$appellantAdvocates/cs:AppellantAdvocate/cs:Advocate">
					<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="30%">
							<xsl:variable name="cnrnFormalName">
								<xsl:call-template name="util:CNRNformalName">
									<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="$cnrnFormalName"/>
							</xsl:call-template>
						</td>
						<td width="10%">
							<xsl:call-template name="representativeDescriptionForAdvocate">
								<xsl:with-param name="qc" select="../@QC"/>
								<xsl:with-param name="advocateType" select="../@AdvocateType"/>
							</xsl:call-template>
						</td>
						<td width="10%">
							<xsl:text>(</xsl:text>
							<xsl:value-of select="../@HearingRole"/>
							<xsl:text>)</xsl:text>
						</td>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="./cs:StartDate"/>
								<xsl:with-param name="endDate" select="./cs:EndDate"/>
							</xsl:call-template>
							<xsl:if test="./cs:StartDate">
								<xsl:if test="not (./cs:EndDate)">
									<!--xsl:text> onwards </xsl:text-->
								</xsl:if>
							</xsl:if>
						</td>
						<td width="6%">
							<xsl:value-of select="../@RepresentationHearingType"/>
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
				<tr/>
				<!-- AppellantAdvocate Solicitors  using representativeDescriptionForAdvocate template-->
				<xsl:for-each select="$appellantAdvocates/cs:AppellantAdvocate/cs:Solicitor/cs:Party/cs:Person">
					<xsl:sort select="../../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../../cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="30%">
							<xsl:variable name="cnrnFormalName">
								<xsl:call-template name="util:CNRNformalName">
									<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="$cnrnFormalName"/>
							</xsl:call-template>
						</td>
						<td width="10%"/>
						<!-- Print 'Solicitor' text in the Hearing Role column -->
						<td width="10%">
							<xsl:text>(</xsl:text>
							<xsl:text>Solicitor</xsl:text>
							<xsl:text>)</xsl:text>
						</td>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="../../cs:StartDate"/>
								<xsl:with-param name="endDate" select="../../cs:EndDate"/>
							</xsl:call-template>
							<xsl:if test="../../cs:StartDate">
								<xsl:if test="not (../../cs:EndDate)">
									<!--xsl:text> onwards </xsl:text-->
								</xsl:if>
							</xsl:if>
						</td>
						<td width="6%">
							<xsl:value-of select="../../../@RepresentationHearingType"/>
						</td>
						<td width="4%">
							<xsl:if test="../../../@SubInst">
								<xsl:choose>
									<xsl:when test="../../../@SubInst = 'S'">
										<xsl:text>(S)</xsl:text>
									</xsl:when>
									<xsl:when test="../../../@SubInst = 'I'">
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
		<xsl:if test="not ($appellantAdvocates/cs:AppellantAdvocate)">
			<xsl:text> In Person</xsl:text>
			<br/>
		</xsl:if>
		<!-- APPROACH  FOR APPELLANT  SOLICITORS (NOT ADVOCATE SOLICITORS) MUST BE PARTY/ORGANISATION NOT PERSON-->
		<xsl:if test="$appellantSolicitors">
			<xsl:if test="$appellantSolicitors/cs:AppellantSolicitor/cs:Party/cs:Organisation">
				<strong>
					<xsl:text>Solicitors</xsl:text>
				</strong>
				<br/>
			</xsl:if>
			<table width="90%">
				<xsl:for-each select="$appellantSolicitors/cs:AppellantSolicitor/cs:Party">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<xsl:if test="./cs:Organisation">
						<tr>
							<td width="30%">
								<xsl:value-of select="./cs:Organisation/cs:OrganisationCode"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="./cs:Organisation/cs:OrganisationName"/>
							</td>
							<td width="10%"/>
							<td width="10%">
								<xsl:text>(</xsl:text>
								<xsl:value-of select="../@RepresentationType"/>
								<xsl:text>)</xsl:text>
							</td>
							<td width="25%">
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="../cs:StartDate"/>
									<xsl:with-param name="endDate" select="../cs:EndDate"/>
								</xsl:call-template>
								<xsl:if test="../cs:StartDate">
									<xsl:if test="not (../cs:EndDate)">
										<!--xsl:text> onwards </xsl:text-->
									</xsl:if>
								</xsl:if>
							</td>
							<td width="10%">
								<!-- nothing to go here i believe for appellant solicitors -->
							</td>
						</tr>
					</xsl:if>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
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
	<xsl:template name="respondent">
		<!-- this template outputs the prosecuting team -->
		<xsl:variable name="respondent" select="//cs:Respondents"/>
		<xsl:variable name="respondentadvocate" select="//cs:RespondentsAdvocates"/>
		<br/>
		<strong>
			<xsl:text>Respondent </xsl:text>
		</strong>
		<br/>
		<xsl:value-of select="$respondent/cs:ProsecutingOrganisation/cs:OrganisationName"/>
		<br/>
		<br/>
		<strong>
			<xsl:text>Advocate</xsl:text>
		</strong>
		<br/>
		<xsl:if test="$respondentadvocate/cs:RespondentsAdvocate">
			<table width="90%">
				<!-- Advocates -->
				<xsl:for-each select="//cs:RespondentsAdvocates/cs:RespondentsAdvocate/cs:Advocate">
					<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="30%">
							<xsl:variable name="cnrnFormalName">
								<xsl:call-template name="util:CNRNformalName">
									<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="$cnrnFormalName"/>
							</xsl:call-template>
						</td>
						<td width="10%"/>
						<td width="10%">
							<xsl:text>(Counsel)</xsl:text>
						</td>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="./cs:StartDate"/>
								<xsl:with-param name="endDate" select="./cs:EndDate"/>
							</xsl:call-template>
							<xsl:if test="./cs:StartDate">
								<xsl:if test="not (./cs:EndDate)">
									<!--xsl:text> onwards </xsl:text -->
								</xsl:if>
							</xsl:if>
						</td>
						<td width="10%">
							<!-- nothing to go here i believe for prosecuting advocates -->
						</td>
					</tr>
				</xsl:for-each>
				<!-- Respondent Advocate Solicitors - XSD currently incorrect (Solicitor has no structure) so this section displays just the text which will be a name. Remove when XSD fixed -->
				<xsl:for-each select="//cs:RespondentsAdvocates/cs:RespondentsAdvocate/cs:Solicitor">
					<tr>
						<td width="30%">
							<xsl:value-of select="."/>
						</td>
						<td width="10%"/>
						<td width="10%">
							<xsl:text>(Solicitor)</xsl:text>
						</td>
						<td width="25%">
                        </td>
						<td width="10%">
                        </td>
					</tr>
				</xsl:for-each>
				<!-- Respondent Advocate Solicitors - Uncomment this when the Respondent Advocate Solicitors have a SolicitorStructure in the Appeals XSD -->
				<!--
                <xsl:for-each select="//cs:RespondentsAdvocates/cs:RespondentsAdvocate/cs:Solicitor/cs:Party/cs:Person">
                    <xsl:sort select="../../cs:StartDate" data-type="text" order="ascending"/>
                    <xsl:sort select="../../cs:EndDate" data-type="text" order="ascending"/>
                    <tr>
                        <td width="30%">
                            <xsl:variable name="cnrnFormalName">
                                <xsl:call-template name="util:CNRNformalName">
                                    <xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>   
                                </xsl:call-template>   
                            </xsl:variable>
                            <xsl:call-template name="str:capitalise">
                               <xsl:with-param name="text" select="$cnrnFormalName"/>
                            </xsl:call-template>
                        </td>
                        <td width="10%"/>
                        <td width="10%">
                            <xsl:text>(Solicitor)</xsl:text>
                        </td>
                        <td width="25%">
                            <xsl:call-template name="trialDateRange">
                                <xsl:with-param name="startDate" select="../../cs:StartDate"/>
                                <xsl:with-param name="endDate" select="../../cs:EndDate"/>
                            </xsl:call-template>
                            <xsl:if test="../../cs:StartDate">
                              <xsl:if test="not (../../cs:EndDate)">
                                <xsl:text> onwards </xsl:text>
                              </xsl:if>
                            </xsl:if>
                          </td>
                        <td width="10%">
                        </td>
                    </tr>
                </xsl:for-each> 
                -->
				<tr/>
			</table>
		</xsl:if>
		<xsl:if test="not ($respondentadvocate/cs:RespondentsAdvocate)">
			<xsl:text> In Person</xsl:text>
			<br/>
		</xsl:if>
		<!--Respondent Solicitors - must be Organisations not persons -->
		<xsl:if test="$respondent/cs:RespondentsSolicitors">
			<xsl:if test="//cs:RespondentsSolicitors/cs:RespondentSolicitor/cs:Party/cs:Organisation">
				<strong>
					<xsl:text>Solicitors</xsl:text>
				</strong>
				<br/>
			</xsl:if>
			<table width="90%">
				<xsl:for-each select="//cs:RespondentsSolicitors/cs:RespondentSolicitor/cs:Party">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<xsl:if test="./cs:Organisation">
						<tr>
							<td width="30%">
								<xsl:value-of select="./cs:Organisation/cs:OrganisationCode"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="./cs:Organisation/cs:OrganisationName"/>
							</td>
							<td width="10%"/>
							<td width="10%">
								<xsl:text>(</xsl:text>
								<xsl:value-of select="../@RepresentationType"/>
								<xsl:text>)</xsl:text>
							</td>
							<td width="25%">
								<xsl:call-template name="trialDateRange">
									<xsl:with-param name="startDate" select="../cs:StartDate"/>
									<xsl:with-param name="endDate" select="../cs:EndDate"/>
								</xsl:call-template>
								<xsl:if test="../cs:StartDate">
									<xsl:if test="not (../cs:EndDate)">
										<!--xsl:text> onwards </xsl:text-->
									</xsl:if>
								</xsl:if>
							</td>
							<td width="10%">
								<!-- nothing to go here i believe for respondent solicitors -->
							</td>
						</tr>
					</xsl:if>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
		<hr/>
	</xsl:template>
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
		<xsl:if test="//cs:CourtReportingFirms">
			<h4>
				<xsl:text>Court Reporting Firm</xsl:text>
			</h4>
			<xsl:for-each select="//cs:CourtReportingFirms/cs:CourtReportingFirm">
				<xsl:text>(</xsl:text>
				<xsl:value-of select="./cs:Firm/cs:OrganisationCode"/>
				<xsl:text>) </xsl:text>
				<xsl:value-of select="./cs:Firm/cs:OrganisationName"/>
				<br/>
			</xsl:for-each>
			<!-- Print Court Reporter -->
			<h4>
				<xsl:text>Court Reporter</xsl:text>
			</h4>
			<xsl:for-each select="//cs:CourtReportingFirm">
				<xsl:if test="./cs:Reporter">
					<table width="95%">
						<xsl:for-each select="./cs:Reporter">
							<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
							<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
							<tr>
								<td width="35%">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="../cs:Firm/cs:OrganisationCode"/>
									<xsl:text>) </xsl:text>
									<xsl:variable name="cnrnFormalName">
										<xsl:call-template name="util:CNRNformalName">
											<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="$cnrnFormalName"/>
									</xsl:call-template>
								</td>
								<td width="10%">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="./cs:OperatorType"/>
									<xsl:text>)</xsl:text>
								</td>
								<td width="25%">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="./cs:StartDate"/>
										<xsl:with-param name="endDate" select="./cs:EndDate"/>
									</xsl:call-template>
								</td>
								<td width="10%"> </td>
							</tr>
						</xsl:for-each>
						<tr/>
					</table>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<doc:template name="judiciary" xmlns="">
		<refpurpose>Outputs the details of the Judges and Justices.</refpurpose>
		<refdescription>
			<para>Iterates through the Judges and any Justices there might be</para>
			<para>Uses the template util:judiciaryName to format the individuals name for display.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="judiciary">
		<!-- this template outputs the Judges and Justices -->
		<xsl:if test="//cs:Judiciary/cs:Judge">
			<h4>
				<xsl:text>Judge</xsl:text>
			</h4>
			<table width="90%">
				<xsl:for-each select="//cs:Judiciary/cs:Judge">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="30%">
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
						<td width="20%">
                    </td>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="../cs:StartDate"/>
								<xsl:with-param name="endDate" select="../cs:EndDate"/>
							</xsl:call-template>
						</td>
						<td width="10%">
                    </td>
					</tr>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
		<!-- now do the justices -->
		<xsl:if test="//cs:Judiciary/cs:Justice">
			<h4>
				<xsl:text>Justice</xsl:text>
			</h4>
			<table width="90%">
				<xsl:for-each select="//cs:Judiciary/cs:Justice">
					<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
					<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
					<tr>
						<td width="30%">
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
						<td width="20%">
                        </td>
						<td width="25%">
							<xsl:call-template name="trialDateRange">
								<xsl:with-param name="startDate" select="../cs:StartDate"/>
								<xsl:with-param name="endDate" select="../cs:EndDate"/>
							</xsl:call-template>
						</td>
						<td width="10%">
                    </td>
					</tr>
				</xsl:for-each>
				<tr/>
			</table>
		</xsl:if>
		<hr/>
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
	<xsl:template name="bailOrCustody">
		<h4>
			<xsl:text>Bail/Custody Status</xsl:text>
		</h4>
		<table width="100%">
			<tr>
				<td width="25%">
					<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusWhenAppealLodged">
						<xsl:text>When notice of appeal lodged: </xsl:text>
						<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusWhenAppealLodged"/>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusAtStartOfHearing">
						<xsl:text>At start of hearing: </xsl:text>
						<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusAtStartOfHearing"/>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusAfterBenchWarrantExecuted">
						<xsl:text>After Bench Warrant Executed: </xsl:text>
						<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusAfterBenchWarrantExecuted"/>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusPutBackforSentence">
						<xsl:text>Put back for sentence: </xsl:text>
						<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusPutBackforSentence"/>
					</xsl:if>
				</td>
			</tr>
			<xsl:for-each select="//cs:AppealRecordSheet/cs:DefendantBailApplications/cs:DefendantBailApplication">
				<xsl:sort select="./cs:ApplicationDate" data-type="text" order="ascending"/>
				<tr>
					<td>
						<xsl:text>Application for bail on </xsl:text>
						<xsl:value-of select="./cs:ApplicationDate"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="./cs:Result"/>
					</td>
				</tr>
			</xsl:for-each>
			<tr/>
		</table>
	</xsl:template>
	<doc:template name="hearings" xmlns="">
		<refpurpose>Outputs the hearing details along with key dates.</refpurpose>
		<refdescription>
			<para>Iterates through the hearing details showing the start date and end date(if available) for each.
                If the decision given date and/or the date of the sentence/order made dates are available then these
                are also shown.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="hearings">
		<!-- this template outputs the hearing information along with key dates -->
		<h4>
			<!-- xsl:text>Hearings and other important dates</xsl:text -->
		</h4>
		<xsl:for-each select="//cs:Hearings/cs:Hearing">
			<xsl:sort select="./cs:HearingDate" data-type="text" order="ascending"/>
			<xsl:sort select="./cs:HearingEndDate" data-type="text" order="ascending"/>
			<xsl:if test="./cs:HearingDate">
				<xsl:choose>
					<xsl:when test="starts-with(./@PreliminaryHearing, 'y')">
						<xsl:text>Preliminary appeal heard </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>Appeal heard </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text> on </xsl:text>
				<xsl:call-template name="trialDateRange">
					<xsl:with-param name="startDate" select="./cs:HearingDate"/>
					<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
				</xsl:call-template>
				<xsl:if test="not(position()=last())">
					<xsl:text> and </xsl:text>
				</xsl:if>
			</xsl:if>
			<!-- Assume same day hearing if the user enters an end date but not a start date, which is why the end date is in the start date for trialDateRange -->
			<xsl:if test="not(./cs:HearingDate)">
				<xsl:if test="./cs:HearingEndDate">
					<xsl:choose>
						<xsl:when test="starts-with(./@PreliminaryHearing, 'y')">
							<xsl:text>Preliminary appeal heard </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Appeal heard </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text> on </xsl:text>
					<xsl:call-template name="trialDateRange">
						<xsl:with-param name="startDate" select="./cs:HearingEndDate"/>
						<xsl:with-param name="endDate" select="./cs:HearingEndDate"/>
					</xsl:call-template>
					<xsl:if test="not(position()=last())">
						<xsl:text> and </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
		<table width="75%">
			<xsl:if test="/cs:AppealRecordSheet/cs:DecisionGivenDate">
				<tr>
					<td>
						<xsl:if test="/cs:AppealRecordSheet/cs:DecisionGivenDate">
							<xsl:text>Decision given on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:DecisionGivenDate"/>
							</xsl:call-template>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="/cs:AppealRecordSheet/cs:SentenceOrOrderDate">
				<tr>
					<td>
						<xsl:if test="/cs:AppealRecordSheet/cs:SentenceOrOrderDate">
							<xsl:text>Sentence/Order made on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentenceOrOrderDate"/>
							</xsl:call-template>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate">
				<tr>
					<td>
						<xsl:if test="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate">
							<xsl:text>Decision/Sentence postponed until </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate"/>
							</xsl:call-template>
							<xsl:if test="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentenceReports='yes'">
								<xsl:text> for reports </xsl:text>
							</xsl:if>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="/cs:AppealRecordSheet/cs:SentenceDeferredToDate">
				<tr>
					<td>
						<xsl:if test="/cs:AppealRecordSheet/cs:SentenceDeferredToDate">
							<xsl:text>Sentence deferred until </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentenceDeferredToDate"/>
							</xsl:call-template>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
		</table>
		<hr/>
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
				<td width="20%">
					<strong>
						<xsl:text>Offence</xsl:text>
					</strong>
				</td>
				<td width="10%">
					<strong>
						<xsl:text>Seq No</xsl:text>
					</strong>
				</td>
				<td width="20%">
					<strong>
						<xsl:text>Appeal Type and Result</xsl:text>
					</strong>
				</td>
				<td width="45%">
					<strong>
						<xsl:text>Original Sentence/Order (in italics) and</xsl:text>
						<br/>
						<xsl:text> Crown Court Decision (underlined)</xsl:text>
					</strong>
				</td>
			</tr>
			<xsl:for-each select="/cs:AppealRecordSheet/cs:Offences/cs:Offence">
				<xsl:variable name="offencenumber" select="./cs:OffenceNumber"/>
				<xsl:variable name="offenceCode" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/@CJSoffenceCode[../@IndictmentCountNumber = $offencenumber]"/>
				<xsl:variable name="offenceStatement" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStatement[../@IndictmentCountNumber = $offencenumber]"/>
				<xsl:variable name="offenceLocation" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceLocation[../@IndictmentCountNumber = $offencenumber]"/>
				<xsl:variable name="arrestingPoliceForceCode" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:ArrestingPoliceForceCode[../@IndictmentCountNumber = $offencenumber]"/>
				<xsl:variable name="offencestart" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStartDateTime[../@IndictmentCountNumber = $offencenumber]"/>
				<xsl:variable name="offenceend" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceEndDateTime[../@IndictmentCountNumber = $offencenumber]"/>
				<xsl:variable name="crn" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:CRN[../@IndictmentCountNumber = $offencenumber]"/>
				<tr>
					<td valign="top">
						<xsl:value-of select="$offencenumber"/>
					</td>
					<td valign="top">
						<xsl:value-of select="$offenceCode"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$offenceStatement"/>
					</td>
					<td valign="top">
						<xsl:value-of select="substring($crn,21,3)"/>
					</td>
					<td valign="top">
						<xsl:value-of select="./cs:AppealType"/>
						<br/>
						<p/>
						<xsl:value-of select="./cs:AppealResult"/>
					</td>
					<td valign="top">
						<xsl:if test="./cs:OffenceDisposals">
							<xsl:for-each select="./cs:OffenceDisposals">
								<i>
									<xsl:call-template name="replace">
										<xsl:with-param name="string" select="./cs:OriginalSentenceOrOrder"/>
									</xsl:call-template>
								</i>
								<br/>
								<br/>
								<u>
									<xsl:call-template name="replace">
										<xsl:with-param name="string" select="./cs:CrownCourtDecision"/>
									</xsl:call-template>
								</u>
								<br/>
								<br/>
							</xsl:for-each>
						</xsl:if>
					</td>
				</tr>
				<tr>
					<td colspan="1">  
                    </td>
					<td colspan="3">
						<table width="100%">
							<tr>
								<td width="40%" valign="top"/>
								<td width="60%"/>
							</tr>
							<tr>
								<td colspan="2">
									<br/>
									<strong>
										<xsl:text>Offence Location Address:</xsl:text>
									</strong>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<xsl:call-template name="buildOffenceLocationAddress">
										<xsl:with-param name="offenceLocation" select="$offenceLocation"/>
									</xsl:call-template>
									<!--                            <xsl:value-of select="$offenceLocation"/>-->
									<br/>
									<br/>
								</td>
							</tr>
							<tr>
								<td colspan="1">
									<xsl:text>Force Location Code:</xsl:text>
								</td>
								<td colspan="1">
									<xsl:value-of select="$arrestingPoliceForceCode"/>
								</td>
							</tr>
							<tr>
								<td colspan="1">
									<xsl:text>Offence Start Date:</xsl:text>
								</td>
								<td colspan="1">
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="$offencestart"/>
									</xsl:call-template>
								</td>
							</tr>
							<tr>
								<td colspan="1">
									<xsl:text>Offence Start Time:</xsl:text>
								</td>
								<td colspan="1">
									<xsl:choose>
										<xsl:when test="substring($offencestart,12,5) != '00:00' ">
											<xsl:value-of select="substring($offencestart,12,5)"/>
										</xsl:when>
									</xsl:choose>
								</td>
							</tr>
							<tr>
								<td colspan="1">
									<xsl:text>Offence End Date:</xsl:text>
								</td>
								<td colspan="1">
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="$offenceend"/>
									</xsl:call-template>
								</td>
							</tr>
							<tr>
								<td colspan="1">
									<xsl:text>Offence End Time:</xsl:text>
								</td>
								<td colspan="1">
									<xsl:choose>
										<xsl:when test="substring($offenceend,12,5) != '00:00' ">
											<xsl:value-of select="substring($offenceend,12,5)"/>
										</xsl:when>
									</xsl:choose>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<br/>
					</td>
				</tr>
			</xsl:for-each>
			<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders">
				<xsl:for-each select="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
					<xsl:if test="./cs:MagistratesOrder">
						<tr>
							<td colspan="3"/>
							<td valign="top" colspan="1">
								<xsl:if test="./cs:MagistratesOrder/cs:AppealResult">
									<xsl:value-of select="./cs:MagistratesOrder/cs:AppealResult"/>
								</xsl:if>
								<xsl:if test="not(./cs:MagistratesOrder/cs:AppealResult)">
									<xsl:text>Sentenced</xsl:text>
								</xsl:if>
							</td>
							<td colspan="1">
								<i>
									<xsl:call-template name="replace">
										<xsl:with-param name="string" select="./cs:MagistratesOrder/cs:OriginalOrder"/>
									</xsl:call-template>
								</i>
								<br/>
								<br/>
								<u>
									<xsl:call-template name="replace">
										<xsl:with-param name="string" select="./cs:MagistratesOrder/cs:CrownCourtDecision"/>
									</xsl:call-template>
								</u>
								<br/>
								<br/>
							</td>
						</tr>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
		</table>
		<hr/>
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
			<strong>
				<xsl:text>Total Sentence</xsl:text>
				<br/>
				<xsl:call-template name="util:decodeDuration">
					<xsl:with-param name="duration" select="/cs:AppealRecordSheet/cs:TotalSentence/cs:Term"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
				<xsl:call-template name="str:to-lower">
					<xsl:with-param name="text" select="/cs:AppealRecordSheet/cs:TotalSentence/cs:Term/@TermType"/>
				</xsl:call-template>
			</strong>
			<hr/>
		</xsl:if>
		<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders">
			<strong>
				<xsl:text>Other Orders</xsl:text>
				<br/>
			</strong>
			<xsl:for-each select="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
				<xsl:if test="./cs:CrownCourtOrder">
					<xsl:value-of select="./cs:CrownCourtOrder/cs:Order"/>
					<br/>
				</xsl:if>
			</xsl:for-each>
			<hr/>
		</xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason != ''">
			<xsl:call-template name="util:DeportationText"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="reasonsForDecision">
		<!-- outputs information about any other orders  -->
		<xsl:if test="/cs:AppealRecordSheet/cs:ReasonsForDecision">
			<strong>
				<xsl:text>Reasons For Decision</xsl:text>
				<br/>
			</strong>
			<xsl:value-of select="/cs:AppealRecordSheet/cs:ReasonsForDecision"/>
			<hr/>
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
		<xsl:param name="startDate"/>
		<xsl:param name="endDate"/>
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
	<xsl:template name="representativeDescriptionForAdvocate">
		<!-- Outputs QC, Sol or nothing depending on the parameters passed in -->
		<xsl:param name="qc"/>
		<xsl:param name="advocateType"/>
		<xsl:variable name="result">
			<xsl:choose>
				<xsl:when test="$qc='yes'">
					<xsl:text>QC</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$advocateType='yes'">
							<xsl:text>Sol</xsl:text>
						</xsl:when>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:copy-of select="$result"/>
	</xsl:template>
	<xsl:template name="replace">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="not($string)"/>
			<xsl:when test="contains($string, '~~~eol~~~')">
				<xsl:value-of select="substring-before($string, '~~~eol~~~')"/>
				<br/>
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
</xsl:stylesheet>
