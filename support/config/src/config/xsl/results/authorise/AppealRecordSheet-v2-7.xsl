<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                                 +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 2-7</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Appeal Record Sheet Stylesheet</title>
		<para>File name : AppealRecordSheet-v2-7.xsl</para>
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
	<xsl:variable name="minorVersion" select="'7'"/>
	<xsl:variable name="stylesheet" select="'AppealRecordSheet-v2-7.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2016-02-22'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<!-- end Global Variables -->
	
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">
					<fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="20cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="end" font-size="9pt" font-family="serif" line-height="14pt">
						<xsl:text>Page </xsl:text>
						<fo:page-number/>
						<xsl:text> of </xsl:text>
						<fo:page-number-citation ref-id="theEnd"/>
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="header"/>
					<xsl:call-template name="appellant"/>
					<xsl:call-template name="respondent"/>
					<xsl:call-template name="courtReporting"/>
					<xsl:call-template name="judiciary"/>
					<xsl:call-template name="bailOrCustody"/>
					<xsl:call-template name="hearings"/>
					<fo:block break-before="page">
					</fo:block>
					<xsl:call-template name="counts"/>
					<xsl:call-template name="otherOrders"/>
					<xsl:call-template name="reasonsForDecision"/>
					<xsl:call-template name="util:copyrightText"/>		
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the Report Header information - including the Appellant and court details.</refpurpose>
	</doc:template>
	<xsl:template name="header">
		<!-- processes the Header information - appellant and court -->
		<xsl:variable name="personal" select="//cs:RecordSheetHeader/cs:Defendant/cs:PersonalDetails"/>
		<fo:block  space-after="18pt" keep-with-next="always"
			line-height="28pt" font-size="24pt" text-align="center">
			<xsl:value-of select="'Appeal Record Sheet'"/>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="250px"/>
			<fo:table-column column-number="2" column-width="300px"/>
			<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<fo:inline font-weight="bold">
							<xsl:text>Appellant </xsl:text>
						</fo:inline>
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
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<fo:inline font-weight="bold">
							<xsl:text>Appellant No.</xsl:text>
						</fo:inline>
						<xsl:value-of select="//cs:RecordSheetHeader/cs:CaseNumber"/>
						<xsl:text>-</xsl:text>
						<xsl:text>1/1</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<xsl:call-template name="util:surnameFirstUC">
							<xsl:with-param name="personalDetails" select="$personal"/>
						</xsl:call-template>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<xsl:text>Date of Birth: </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="$personal/cs:DateOfBirth/apd:BirthDate"/>
						</xsl:call-template>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<fo:inline font-weight="bold">
							<xsl:text>Address</xsl:text>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<!-- Added URN for CR49 -->
					<fo:block font-size="10pt">
						<xsl:text>PTI Unique Ref: </xsl:text>
						<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:URN"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<xsl:call-template name="util:address_oneline">
							<xsl:with-param name="personalDetails" select="$personal"/>
						</xsl:call-template>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<!-- Added URN for CR49 -->
					<fo:block font-size="10pt">
						<xsl:text>PNC No: </xsl:text>
						<xsl:variable name="pnc" select="//cs:RecordSheetHeader/cs:Defendant/cs:PNCnumber"/>
						<xsl:value-of select="substring($pnc,3,10)"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<xsl:if test="$personal/cs:Nationality">
							<fo:inline font-weight="bold">
								<xsl:text>Nationality</xsl:text>
							</fo:inline>
						</xsl:if>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<!-- Added URN for CR49 -->
					<fo:block font-size="10pt">
						<xsl:text>ASN No: </xsl:text>
						<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:ASNs/cs:ASN"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:if test="$personal/cs:Nationality">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:value-of select="$personal/cs:Nationality"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<!-- CCN400 START -->
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
		<!-- now give out the court information -->
		<fo:block font-size="10pt">
			<fo:inline font-weight="bold">
				<xsl:text>Before the </xsl:text>
				<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseType"/>
				<xsl:text> at </xsl:text>
				<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseName"/>
			</fo:inline>
		</fo:block>
		<fo:block font-size="10pt"> </fo:block>
		<xsl:if test="//cs:RecordSheetHeader/cs:MagistratesCourt">
			<fo:block font-size="10pt">
				<xsl:text>Appeal from a decision of </xsl:text>
				<xsl:value-of select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName"/>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="//cs:OriginalSentenceOrOrderDate">
			<fo:block font-size="10pt">
				<xsl:text>Date of original sentence/order: </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:OriginalSentenceOrOrderDate"/>
				</xsl:call-template>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:TransferInCourt">
			<xsl:variable name="transferCourtHouseName" select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseName"/>
			<fo:block font-size="10pt">
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
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="//cs:BenchWarrantDate">
			<fo:block font-size="10pt">
				<xsl:text>Bench Warrant executed on </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:BenchWarrantDate"/>
				</xsl:call-template>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
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
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
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
		<fo:block font-size="10pt">
			<fo:inline font-weight="bold">
				<xsl:text>Appellant</xsl:text>
			</fo:inline>
		</fo:block>
		<xsl:if test="$appellantAdvocates/cs:AppellantAdvocate">
			<!-- First do the advocates -->
			<fo:block font-size="10pt">
				<fo:inline font-weight="bold">
					<xsl:text>Advocate</xsl:text>
				</fo:inline>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="180px"/>
			<fo:table-column column-number="2" column-width="45px"/>
			<fo:table-column column-number="3" column-width="70px"/>
			<fo:table-column column-number="4" column-width="150px"/>
			<fo:table-column column-number="5" column-width="45px"/>
			<fo:table-column column-number="6" column-width="10px"/>
				<fo:table-body>
					<!-- AppellantAdvocate Advocates using representativeDescriptionForAdvocate template -->
					<xsl:for-each select="$appellantAdvocates/cs:AppellantAdvocate/cs:Advocate">
						<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:variable name="cnrnFormalName">
										<xsl:call-template name="util:CNRNformalName">
											<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="$cnrnFormalName"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="representativeDescriptionForAdvocate">
										<xsl:with-param name="qc" select="../@QC"/>
										<xsl:with-param name="advocateType" select="../@AdvocateType"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:text>(</xsl:text>
									<xsl:value-of select="../@HearingRole"/>
									<xsl:text>)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="./cs:StartDate"/>
										<xsl:with-param name="endDate" select="./cs:EndDate"/>
									</xsl:call-template>
									<xsl:if test="./cs:StartDate">
										<xsl:if test="not (./cs:EndDate)">
											<!--xsl:text> onwards </xsl:text-->
										</xsl:if>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
								<xsl:value-of select="../@RepresentationHearingType"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					<!-- AppellantAdvocate Solicitors  using representativeDescriptionForAdvocate template-->
					<xsl:for-each select="$appellantAdvocates/cs:AppellantAdvocate/cs:Solicitor/cs:Party/cs:Person">
						<xsl:sort select="../../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../../cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:variable name="cnrnFormalName">
										<xsl:call-template name="util:CNRNformalName">
											<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="$cnrnFormalName"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<!-- Print 'Solicitor' text in the Hearing Role column -->
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:text>(</xsl:text>
									<xsl:text>Solicitor</xsl:text>
									<xsl:text>)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="../../cs:StartDate"/>
										<xsl:with-param name="endDate" select="../../cs:EndDate"/>
									</xsl:call-template>
									<xsl:if test="../../cs:StartDate">
										<xsl:if test="not (../../cs:EndDate)">
											<!--xsl:text> onwards </xsl:text-->
										</xsl:if>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:value-of select="../../../@RepresentationHearingType"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<xsl:if test="not ($appellantAdvocates/cs:AppellantAdvocate/cs:Advocate)">
			<xsl:if test="$appellantAdvocates/cs:AppellantAdvocate">
				<xsl:if test="not ($appellantAdvocates/cs:AppellantAdvocate/cs:Solicitor)">
					<xsl:if test="not ($appellantSolicitors)">
						<fo:block font-size="10pt">
							<xsl:choose>
								<xsl:when test="$appellantAdvocates/cs:AppellantAdvocate/@NonAttendance = 'yes'">
									<xsl:text>Non Attendance</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text> In Person</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
						<fo:block font-size="10pt"> </fo:block>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:if>
		<!-- APPROACH  FOR APPELLANT  SOLICITORS (NOT ADVOCATE SOLICITORS) MUST BE PARTY/ORGANISATION NOT PERSON-->
		<xsl:if test="$appellantSolicitors">
			<xsl:if test="$appellantSolicitors/cs:AppellantSolicitor/cs:Party/cs:Organisation">
				<fo:block font-size="10pt">
					<fo:inline font-weight="bold">
						<xsl:text>Solicitors</xsl:text>
					</fo:inline>
				</fo:block>
				<fo:block font-size="10pt"> </fo:block>
			</xsl:if>
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="180px"/>
			<fo:table-column column-number="2" column-width="45px"/>
			<fo:table-column column-number="3" column-width="70px"/>
			<fo:table-column column-number="4" column-width="150px"/>
			<fo:table-column column-number="5" column-width="45px"/>
				<fo:table-body>
					<xsl:for-each select="$appellantSolicitors/cs:AppellantSolicitor/cs:Party">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<xsl:if test="./cs:Organisation">
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:value-of select="./cs:Organisation/cs:OrganisationCode"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="./cs:Organisation/cs:OrganisationName"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt"> </fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:text>(</xsl:text>
										<xsl:value-of select="../@RepresentationType"/>
										<xsl:text>)</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:call-template name="trialDateRange">
											<xsl:with-param name="startDate" select="../cs:StartDate"/>
											<xsl:with-param name="endDate" select="../cs:EndDate"/>
										</xsl:call-template>
										<xsl:if test="../cs:StartDate">
											<xsl:if test="not (../cs:EndDate)">
												<!--xsl:text> onwards </xsl:text-->
											</xsl:if>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt"> </fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<fo:block font-size="10pt" white-space="pre">
			<xsl:text>&#160;</xsl:text>
		</fo:block>
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
		<fo:block font-size="10pt"> </fo:block>
		<xsl:if test="$respondent/cs:ProsecutingOrganisation/cs:OrganisationName">
			<fo:block font-size="10pt">
				<fo:inline font-weight="bold">
					<xsl:text>Respondent </xsl:text>
				</fo:inline>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
			<xsl:value-of select="$respondent/cs:ProsecutingOrganisation/cs:OrganisationName"/>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="$respondentadvocate/cs:RespondentsAdvocate">
			<fo:block font-size="10pt">
				<fo:inline font-weight="bold">
					<xsl:text>Advocate</xsl:text>
				</fo:inline>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-number="1" column-width="180px"/>
				<fo:table-column column-number="2" column-width="45px"/>
				<fo:table-column column-number="3" column-width="70px"/>
				<fo:table-column column-number="4" column-width="150px"/>
				<fo:table-column column-number="5" column-width="45px"/>
				<fo:table-body>
					<!-- Advocates -->
					<xsl:for-each select="//cs:RespondentsAdvocates/cs:RespondentsAdvocate/cs:Advocate">
						<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:variable name="cnrnFormalName">
										<xsl:call-template name="util:CNRNformalName">
											<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="$cnrnFormalName"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:text>(Counsel)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="./cs:StartDate"/>
										<xsl:with-param name="endDate" select="./cs:EndDate"/>
									</xsl:call-template>
									<xsl:if test="./cs:StartDate">
										<xsl:if test="not (./cs:EndDate)">
											<!--xsl:text> onwards </xsl:text -->
										</xsl:if>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<!-- nothing to go here i believe for prosecuting advocates -->
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					<!-- Respondent Advocate Solicitors - XSD currently incorrect (Solicitor has no structure) so this section displays just the text which will be a name. Remove when XSD fixed -->
					<xsl:for-each select="//cs:RespondentsAdvocates/cs:RespondentsAdvocate/cs:Solicitor/cs:Party/cs:Person">
						<xsl:sort select="../../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../../cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:variable name="cnrnFormalName">
										<xsl:call-template name="util:CNRNformalName">
											<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="$cnrnFormalName"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:text>(Solicitor)</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="../../cs:StartDate"/>
										<xsl:with-param name="endDate" select="../../cs:EndDate"/>
									</xsl:call-template>
									<xsl:if test="../../cs:StartDate">
										<xsl:if test="not (../../cs:EndDate)">
											<!--<xsl:text> onwards </xsl:text>-->
										</xsl:if>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<!-- nothing to go here i believe for prosecuting solicitors -->
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<xsl:if test="not ($respondentadvocate/cs:RespondentsAdvocate)">
			<fo:block font-size="10pt">
				<xsl:text> In Person</xsl:text>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<!--Respondent Solicitors - must be Organisations not persons -->
		<xsl:if test="$respondent/cs:RespondentsSolicitors">
			<xsl:if test="//cs:RespondentsSolicitors/cs:RespondentSolicitor/cs:Party/cs:Organisation">
				<fo:block font-size="10pt">
					<fo:inline font-weight="bold">
						<xsl:text>Solicitors</xsl:text>
					</fo:inline>
				</fo:block>
				<fo:block font-size="10pt"> </fo:block>
			</xsl:if>
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="180px"/>
			<fo:table-column column-number="2" column-width="45px"/>
			<fo:table-column column-number="3" column-width="70px"/>
			<fo:table-column column-number="4" column-width="150px"/>
			<fo:table-column column-number="5" column-width="45px"/>
				<fo:table-body>
					<xsl:for-each select="//cs:RespondentsSolicitors/cs:RespondentSolicitor/cs:Party">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<xsl:if test="./cs:Organisation">
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:value-of select="./cs:Organisation/cs:OrganisationCode"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="./cs:Organisation/cs:OrganisationName"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt"> </fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:text>(</xsl:text>
										<xsl:value-of select="../@RepresentationType"/>
										<xsl:text>)</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:call-template name="trialDateRange">
											<xsl:with-param name="startDate" select="../cs:StartDate"/>
											<xsl:with-param name="endDate" select="../cs:EndDate"/>
										</xsl:call-template>
										<xsl:if test="../cs:StartDate">
											<xsl:if test="not (../cs:EndDate)">
												<!--xsl:text> onwards </xsl:text-->
											</xsl:if>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<!-- nothing to go here i believe for respondent solicitors -->
									<fo:block font-size="10pt"> </fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
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
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Court Reporting Firm</xsl:text>
			</fo:block>
			<xsl:for-each select="//cs:CourtReportingFirms/cs:CourtReportingFirm">
				<fo:block font-size="10pt">
					<xsl:text>(</xsl:text>
					<xsl:value-of select="./cs:Firm/cs:OrganisationCode"/>
					<xsl:text>) </xsl:text>
					<xsl:value-of select="./cs:Firm/cs:OrganisationName"/>
				</fo:block>
				<fo:block font-size="10pt"> </fo:block>
			</xsl:for-each>
			<!-- Print Court Reporter -->
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Court Reporter</xsl:text>
			</fo:block>
			<xsl:for-each select="//cs:CourtReportingFirm">
				<xsl:if test="./cs:Reporter">
					<fo:block>
					<fo:table table-layout="fixed">
					<fo:table-column column-number="1" column-width="180px"/>
					<fo:table-column column-number="2" column-width="70px"/>
					<fo:table-column column-number="3" column-width="110px"/>
					<fo:table-column column-number="4" column-width="45px"/>
						<fo:table-body>
							<xsl:for-each select="./cs:Reporter">
								<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
								<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
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
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>(</xsl:text>
											<xsl:value-of select="./cs:OperatorType"/>
											<xsl:text>)</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:call-template name="trialDateRange">
												<xsl:with-param name="startDate" select="./cs:StartDate"/>
												<xsl:with-param name="endDate" select="./cs:EndDate"/>
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt"> </fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
					</fo:block>
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
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Judge</xsl:text>
			</fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-number="1" column-width="180px"/>
				<fo:table-column column-number="2" column-width="70px"/>
				<fo:table-column column-number="3" column-width="115px"/>
				<fo:table-column column-number="4" column-width="45px"/>
				<fo:table-body>
					<xsl:for-each select="//cs:Judiciary/cs:Judge">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="../cs:StartDate"/>
										<xsl:with-param name="endDate" select="../cs:EndDate"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<!-- now do the justices -->
		<xsl:if test="//cs:Judiciary/cs:Justice">
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Justice</xsl:text>
			</fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="135px"/>
			<fo:table-column column-number="2" column-width="70px"/>
			<fo:table-column column-number="3" column-width="115px"/>
			<fo:table-column column-number="4" column-width="45px"/>
				<fo:table-body>
					<xsl:for-each select="//cs:Judiciary/cs:Justice">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="../cs:StartDate"/>
										<xsl:with-param name="endDate" select="../cs:EndDate"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
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
		<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
			<xsl:text>Bail/Custody Status</xsl:text>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="350px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusWhenAppealLodged">
								<xsl:text>When notice of appeal lodged: </xsl:text>
								<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusWhenAppealLodged"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusAtStartOfHearing">
								<xsl:text>At start of hearing: </xsl:text>
								<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusAtStartOfHearing"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusAfterBenchWarrantExecuted">
								<xsl:text>After Bench Warrant Executed: </xsl:text>
								<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusAfterBenchWarrantExecuted"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:if test="/cs:AppealRecordSheet/cs:BailStatusPutBackforSentence">
								<xsl:text>Put back for sentence: </xsl:text>
								<xsl:value-of select="/cs:AppealRecordSheet/cs:BailStatusPutBackforSentence"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="//cs:AppealRecordSheet/cs:DefendantBailApplications/cs:DefendantBailApplication">
					<xsl:sort select="./cs:ApplicationDate" data-type="text" order="ascending"/>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:text>Application for bail on </xsl:text>
								<xsl:value-of select="./cs:ApplicationDate"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="./cs:Result"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
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
		<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
			<xsl:text>Hearings and other important dates</xsl:text>
		</fo:block>
		<xsl:for-each select="//cs:Hearings/cs:Hearing">
			<xsl:sort select="./cs:HearingDate" data-type="text" order="ascending"/>
			<xsl:sort select="./cs:HearingEndDate" data-type="text" order="ascending"/>
			<fo:block font-size="10pt" class="HearingBlock">
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
			</fo:block>
		</xsl:for-each>
		<fo:block>
		<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="350px"/>
			<fo:table-body>
				<xsl:if test="/cs:AppealRecordSheet/cs:DecisionGivenDate">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"  class="HearingBlock">
								<xsl:if test="/cs:AppealRecordSheet/cs:DecisionGivenDate">
									<xsl:text>Decision given on </xsl:text>
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:DecisionGivenDate"/>
									</xsl:call-template>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="/cs:AppealRecordSheet/cs:SentenceOrOrderDate">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"  class="HearingBlock">
								<xsl:if test="/cs:AppealRecordSheet/cs:SentenceOrOrderDate">
									<xsl:text>Sentence/Order made on </xsl:text>
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentenceOrOrderDate"/>
									</xsl:call-template>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt" class="HearingBlock">
								<xsl:if test="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate">
									<xsl:text>Decision/Sentence postponed until </xsl:text>
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate"/>
									</xsl:call-template>
									<xsl:if test="/cs:AppealRecordSheet/cs:SentencePostponed/cs:SentenceReports='yes'">
										<xsl:text> for reports </xsl:text>
									</xsl:if>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:if test="/cs:AppealRecordSheet/cs:SentenceDeferredToDate">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt" class="HearingBlock">
								<xsl:if test="/cs:AppealRecordSheet/cs:SentenceDeferredToDate">
									<xsl:text>Sentence deferred until </xsl:text>
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="/cs:AppealRecordSheet/cs:SentenceDeferredToDate"/>
									</xsl:call-template>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
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
		<fo:block>
		<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="25px"/>
		<fo:table-column column-number="2" column-width="90px"/>
		<fo:table-column column-number="3" column-width="45px"/>
		<fo:table-column column-number="4" column-width="90px"/>
		<fo:table-column column-number="5" column-width="240px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>No</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Offence</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Seq No</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Appeal Type and Result</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Original Sentence/Order (in italics) and</xsl:text>
								<xsl:text> Crown Court Decision (underlined)</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="/cs:AppealRecordSheet/cs:Offences/cs:Offence">
					<xsl:variable name="offencenumber" select="./cs:OffenceNumber"/>
					<xsl:variable name="offenceCode" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/@CJSoffenceCode[../@IndictmentCountNumber = $offencenumber]"/>
					<xsl:variable name="offenceStatement" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStatement[../@IndictmentCountNumber = $offencenumber]"/>
					<xsl:variable name="offenceLocation" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceLocation[../@IndictmentCountNumber = $offencenumber]"/>
					<xsl:variable name="arrestingPoliceForceCode" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:ArrestingPoliceForceCode[../@IndictmentCountNumber = $offencenumber]"/>
					<xsl:variable name="offencestart" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStartDateTime[../@IndictmentCountNumber = $offencenumber]"/>
					<xsl:variable name="offenceend" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:OffenceEndDateTime[../@IndictmentCountNumber = $offencenumber]"/>
					<xsl:variable name="crn" select="//cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge/cs:CRN[../@IndictmentCountNumber = $offencenumber]"/>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="$offencenumber"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="$offenceCode"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="$offenceStatement"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="substring($crn,21,3)"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="./cs:AppealType"/>
								<fo:block font-size="10pt"> </fo:block>
								<xsl:value-of select="./cs:AppealResult"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<xsl:if test="./cs:OffenceDisposals">
								<xsl:for-each select="./cs:OffenceDisposals">
									<fo:block font-size="10pt">
										<fo:inline font-style="italic">
											<xsl:call-template name="replace">
												<xsl:with-param name="string" select="./cs:OriginalSentenceOrOrder"/>
											</xsl:call-template>
										</fo:inline>
									</fo:block>
									<fo:block font-size="10pt"> </fo:block>
									<fo:block font-size="10pt"> </fo:block>
									<fo:block font-size="10pt">
										<fo:inline text-decoration="underline">
											<xsl:call-template name="replace">
												<xsl:with-param name="string" select="./cs:CrownCourtDecision"/>
											</xsl:call-template>
										</fo:inline>
									</fo:block>
									<fo:block font-size="10pt"> </fo:block>
									<fo:block font-size="10pt"> </fo:block>
								</xsl:for-each>
							</xsl:if>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<fo:inline font-weight="bold">
									<xsl:text>Offence Location Address</xsl:text>
								</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:call-template name="buildOffenceLocationAddress">
									<xsl:with-param name="offenceLocation" select="$offenceLocation"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="4">
							<fo:table table-layout="fixed">
							<fo:table-column column-number="1" column-width="170px"/>
							<fo:table-column column-number="2" column-width="130px"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<fo:inline font-weight="bold">
												<xsl:text>Force Location Code:</xsl:text>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:value-of select="$arrestingPoliceForceCode"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<fo:inline font-weight="bold">
												<xsl:text>Offence Start Date:</xsl:text>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="$offencestart"/>
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<fo:inline font-weight="bold">
												<xsl:text>Offence Start Time:</xsl:text>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="substring($offencestart,12,5) != '00:00' ">
													<xsl:value-of select="substring($offencestart,12,5)"/>
												</xsl:when>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<fo:inline font-weight="bold">
												<xsl:text>Offence End Date:</xsl:text>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="$offenceend"/>
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>										
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="substring($offenceend,12,5) != '00:00' ">
													<fo:inline font-weight="bold">
														<xsl:text>Offence End Time:</xsl:text>
													</fo:inline>
												</xsl:when>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="substring($offenceend,12,5) != '00:00' ">
													<xsl:value-of select="substring($offenceend,12,5)"/>
												</xsl:when>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
							</fo:table>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
	</xsl:template>
	<doc:template name="otherOrders" xmlns="">
		<refpurpose>Outputs info for any other orders.</refpurpose>
		<refdescription>
			<para>Iterates through the TotalSentences/OtherOrders showing the orders found.</para>
		</refdescription>
	</doc:template>
	<xsl:template name="otherOrders">
		<xsl:variable name="totalSentenceTypesList" select="' TIMP TSUSP TORD TFINE TCOSTS TCOMP TPAY TPP TDISQ '"/>
		<!-- outputs information about any other orders  -->
		<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order/cs:CrownCourtOrder[contains($totalSentenceTypesList, concat(' ', ./cs:CrownCourtOrderData/cs:DisposalCode, ' '))]/cs:Order">
			<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Total Sentence</xsl:text>
			</fo:block>	
			<xsl:for-each select="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order/cs:CrownCourtOrder[contains($totalSentenceTypesList, concat(' ', ./cs:CrownCourtOrderData/cs:DisposalCode, ' '))]/cs:Order">
				<fo:block font-size="10pt">
					<xsl:call-template name="replace">
						<xsl:with-param name="string" select="."/>
					</xsl:call-template>
				</fo:block>
			</xsl:for-each>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
		</xsl:if>
		<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order/cs:CrownCourtOrder[not(contains($totalSentenceTypesList, concat(' ', ./cs:CrownCourtOrderData/cs:DisposalCode, ' ')))]/cs:Order
				or /cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order/cs:MagistratesOrder">
			<fo:block font-size="10pt">
				<fo:inline font-weight="bold">
					<xsl:text>Other Orders</xsl:text>
				</fo:inline>
			</fo:block>
			<xsl:for-each select="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order/cs:CrownCourtOrder[not(contains($totalSentenceTypesList, concat(' ', ./cs:CrownCourtOrderData/cs:DisposalCode, ' ')))]/cs:Order">
				<fo:block font-size="10pt">
					<fo:inline text-decoration="underline">
						<xsl:call-template name="replace">
							<xsl:with-param name="string" select="."/>
						</xsl:call-template>
					</fo:inline>
				</fo:block>
			</xsl:for-each>
			<xsl:if test="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders">
				<xsl:for-each select="/cs:AppealRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
					<xsl:if test="./cs:MagistratesOrder">
						<fo:block font-size="10pt">
							<xsl:if test="./cs:MagistratesOrder/cs:AppealResult">
								<xsl:value-of select="./cs:MagistratesOrder/cs:AppealResult"/>
							</xsl:if>
							<xsl:if test="not(./cs:MagistratesOrder/cs:AppealResult)">
								<xsl:text>Sentenced</xsl:text>
							</xsl:if>
						</fo:block>
						<fo:block font-size="10pt">
							<fo:inline font-style="italic">
								<xsl:call-template name="replace">
									<xsl:with-param name="string" select="./cs:MagistratesOrder/cs:OriginalOrder"/>
								</xsl:call-template>
							</fo:inline>
						</fo:block>
						<fo:block font-size="10pt"> </fo:block>
						<fo:block font-size="10pt">
							<fo:inline text-decoration="underline">
								<xsl:call-template name="replace">
									<xsl:with-param name="string" select="./cs:MagistratesOrder/cs:CrownCourtDecision"/>
								</xsl:call-template>
							</fo:inline>
						</fo:block>
						<fo:block font-size="10pt"> </fo:block>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
			<fo:block font-size="10pt">
				<fo:leader leader-pattern="rule" leader-length="100%"/>
			</fo:block>
		</xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason != ''">
			<xsl:call-template name="util:DeportationText"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="reasonsForDecision">
		<!-- outputs information about any other orders  -->
		<xsl:if test="/cs:AppealRecordSheet/cs:ReasonsForDecision">
			<fo:block font-size="10pt">
				<fo:inline font-weight="bold">
					<xsl:text>Reasons For Decision</xsl:text>
				</fo:inline>
			</fo:block>
			<fo:block font-size="10pt">
				<xsl:value-of select="/cs:AppealRecordSheet/cs:ReasonsForDecision"/>
			 </fo:block>
			<fo:block font-size="10pt">
				<fo:leader leader-pattern="rule" leader-length="100%"/>
			</fo:block>
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
				<fo:block font-size="10pt"> </fo:block>
				<xsl:call-template name="replace">
					<xsl:with-param name="string" select="substring-after($string, '~~~eol~~~')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="replace1">
					<xsl:with-param name="string" select="$string"/>
				</xsl:call-template>
				<!--<xsl:value-of select="$string" />-->
				<fo:block font-size="10pt"> </fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="replace1">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="not($string)"/>
			<xsl:when test="contains($string, 'This disposal has been amended')">
				<xsl:value-of select="substring-before($string, 'This disposal has been amended')"/>
				<fo:block font-size="10pt"> </fo:block>This disposal has been amended
      <xsl:call-template name="replace">
					<xsl:with-param name="string" select="substring-after($string, 'This disposal has been amended')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$string"/>
				<fo:block font-size="10pt"> </fo:block>
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
