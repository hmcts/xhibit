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
		<para>File name : TrialRecordSheet-v2-10.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Trial Record Sheet in html format</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'2'"/>
	<xsl:variable name="minorVersion" select="'10'"/>
	<xsl:variable name="stylesheet" select="'trialrecordsheet-v2-10.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2016-02-22'"/>
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
	<!-- **************************************** -->
	<!-- Root Template                  -->
	<!-- **************************************** -->
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
				<xsl:call-template name="defence"/>
				<xsl:call-template name="prosecution"/>
				<xsl:call-template name="courtReporting"/>
				<xsl:call-template name="judiciary"/>
				<xsl:call-template name="bailOrCustody"/>
				<xsl:call-template name="hearings"/>
				<fo:block break-before="page">
				</fo:block>
				<xsl:call-template name="counts"/>
				<xsl:call-template name="summary"/>
				<xsl:call-template name="originalCharges"/>
				<xsl:call-template name="breach"/>
				<xsl:call-template name="totalSentence"/>
				<xsl:call-template name="otherOrders"/>
				<xsl:call-template name="takenIntoConsideration"/>
				<xsl:call-template name="specialCircumstances"/>
				<xsl:call-template name="decisionOfTheCourtOfAppeal"/>
				<xsl:call-template name="util:copyrightText"/>
				<fo:block id="theEnd"/>
			</fo:flow>
		</fo:page-sequence>
	</fo:root>
	</xsl:template>
	<!-- **************************************** -->
	<!-- lineBreak Template                    -->
	<!-- **************************************** -->
	<xsl:template name="lineBreak">
		<xsl:param name="text"/>
		<xsl:choose>
			<xsl:when test="contains($text, '&#xa;')">
				<xsl:value-of select="substring-before($text, '&#xa;')"/>
				<fo:block font-size="10pt"> </fo:block>
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
		<fo:block  space-after="18pt" keep-with-next="always"
			line-height="28pt" font-size="24pt" text-align="center">
			<xsl:value-of select="'Trial Record Sheet'"/>
		</fo:block>
		
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="340px"/>
			<fo:table-column column-number="2" column-width="190px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
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
									<xsl:text>):</xsl:text>
								</xsl:if>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Defendant No. </xsl:text>
							</fo:inline>
							<xsl:value-of select="//cs:RecordSheetHeader/cs:CaseNumber"/>
							<xsl:text>-</xsl:text>
							<xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:DefendantNumber"/>
							<xsl:text>/</xsl:text>
							<xsl:value-of select="//cs:RecordSheetHeader/cs:TotalDefendants"/>
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
							<fo:inline font-weight="bold">
								<xsl:text>Date of Birth: </xsl:text>
							</fo:inline>
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
								<xsl:text>Address: </xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>PTI Unique Ref: </xsl:text>
							</fo:inline>
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
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>PNC No: </xsl:text>
							</fo:inline>
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
									<xsl:text>Nationality: </xsl:text>
								</fo:inline>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>ASN No: </xsl:text>
							</fo:inline>
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
		
		<fo:block font-size="10pt">
			<fo:leader leader-pattern="rule" leader-length="100%"/>
		</fo:block>
		<fo:block font-size="10pt">
			<fo:inline font-weight="bold">
				<xsl:text>Before the </xsl:text>
				<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseType"/>
				<xsl:text> at </xsl:text>
				<xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseName"/>
			</fo:inline>
		</fo:block>
		<fo:block font-size="10pt"> </fo:block>
		<fo:block font-size="10pt">
			<xsl:call-template name="util:instigationText">
				<xsl:with-param name="code" select="//cs:RecordSheetHeader/cs:MethodOfInstigation"/>
			</xsl:call-template>
			<xsl:if test="//cs:RecordSheetHeader/cs:MethodOfInstigation = 'Committal' or
						  //cs:RecordSheetHeader/cs:MethodOfInstigation = 'Sending'">
				<xsl:value-of select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName"/>
			</xsl:if>
			<xsl:if test="//cs:RecordSheetHeader/cs:DateOfInstigation">
				<xsl:text> on </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
				</xsl:call-template>
			</xsl:if>
		</fo:block>
		<fo:block font-size="10pt"> </fo:block>
		<xsl:if test="//cs:RecordSheetHeader/cs:TransferInCourt">
			<fo:block font-size="10pt">
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
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="starts-with(//cs:TrialRecordSheet/cs:ReTrial, 'y')">
			<fo:block font-size="10pt">
				<xsl:text>Re-trial ordered by the Court of Appeal</xsl:text>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="//cs:TrialRecordSheet/cs:BenchWarrantDate">
			<fo:block font-size="10pt">
				<xsl:text>Bench Warrant executed on </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="//cs:TrialRecordSheet/cs:BenchWarrantDate"/>
				</xsl:call-template>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:if>
		<xsl:if test="//cs:TrialRecordSheet/cs:IndictmentHistory">
			<fo:block font-size="10pt">
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
			</fo:block>
		</xsl:if>
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
		<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
		
		<xsl:if test="$defenceAdvocates/cs:DefenceAdvocate or $defenceSolicitors/cs:DefenceSolicitor/cs:Party">
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Defence</xsl:text>
			</fo:block>
			
			<xsl:if test="$defenceAdvocates/cs:DefenceAdvocate">
				<!-- First do the advocates -->
				<fo:block font-size="10pt">
					<fo:inline font-weight="bold">
						<xsl:text>Advocates</xsl:text>
					</fo:inline>
				</fo:block>
				
				<fo:block font-size="10pt"> </fo:block>
			
				<fo:block>
				<fo:table table-layout="fixed">
				<fo:table-column column-number="1" column-width="140px"/>
				<fo:table-column column-number="2" column-width="50px"/>
				<fo:table-column column-number="3" column-width="55px"/>
				<fo:table-column column-number="4" column-width="105px"/>
				<fo:table-column column-number="5" column-width="85px"/>
				<fo:table-column column-number="6" column-width="10px"/>
				<fo:table-body>
					<xsl:for-each select="$defenceAdvocates/cs:DefenceAdvocate/cs:Advocate">
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
									<xsl:if test="../@HearingRole">
										<xsl:text>(</xsl:text>
										<xsl:value-of select="../@HearingRole"/>
										<xsl:text>)</xsl:text>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="trialDateRange">
										<xsl:with-param name="startDate" select="./cs:StartDate"/>
										<xsl:with-param name="endDate" select="./cs:EndDate"/>
									</xsl:call-template>
									<xsl:if test="./cs:StartDate">
										<xsl:if test="./cs:EndDate=''">
											<xsl:text> onwards </xsl:text>
										</xsl:if>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:if test="../@RepresentationHearingType">
										<xsl:text>(</xsl:text>
										<xsl:value-of select="../@RepresentationHearingType"/>
										<xsl:text>)</xsl:text>
									</xsl:if>
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
					<xsl:for-each select="$defenceAdvocates/cs:DefenceAdvocate/cs:Solicitor/cs:Party">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:variable name="inPerson">
										<xsl:call-template name="str:to-lower">
											<xsl:with-param name="text" select="./cs:Person/cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
										</xsl:call-template>
									</xsl:variable>
									<xsl:if test="$inPerson!='' and $inPerson!='in person'">
										<xsl:text>(Solicitor)</xsl:text>
									</xsl:if>
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
											<xsl:text> onwards </xsl:text>
										</xsl:if>
										<xsl:if test="../cs:EndDate=''">
											<xsl:text> onwards </xsl:text>
										</xsl:if>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:if test="../../@RepresentationHearingType">
										<xsl:text>(</xsl:text>
										<xsl:value-of select="../../@RepresentationHearingType"/>
										<xsl:text>)</xsl:text>		
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
				</fo:table>
				</fo:block>
			</xsl:if>
			<xsl:if test ="not ($defenceAdvocates/cs:DefenceAdvocate/cs:Advocate)">
				<xsl:if test ="$defenceAdvocates/cs:DefenceAdvocate">
					<xsl:if test ="not ($defenceAdvocates/cs:DefenceAdvocate/cs:Solicitor)">
						<fo:block font-size="10pt">
							<xsl:choose>
								<xsl:when test="$defenceAdvocates/cs:DefenceAdvocate/@NonAttendance = 'yes'">
									<xsl:text>Non Attendance</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>In Person</xsl:text>
								</xsl:otherwise>
							</xsl:choose>			
						</fo:block>
						<fo:block font-size="10pt"> </fo:block>
						<fo:block font-size="10pt"> </fo:block>
					</xsl:if>
				</xsl:if>
			</xsl:if>
			<!-- Now the solicitors -->
			<xsl:if test="$defenceSolicitors/cs:DefenceSolicitor/cs:Party">
				<fo:block font-size="10pt">
					<fo:inline font-weight="bold">
						<xsl:text>Solicitors</xsl:text>
					</fo:inline>
				</fo:block>
				<fo:block>
				<fo:table table-layout="fixed">
				<fo:table-column column-number="1" column-width="140px"/>
				<fo:table-column column-number="2" column-width="50px"/>
				<fo:table-column column-number="3" column-width="55px"/>
				<fo:table-column column-number="4" column-width="105px"/>
				<fo:table-column column-number="5" column-width="85px"/>
				<fo:table-column column-number="6" column-width="10px"/>
					<fo:table-body>
						<xsl:for-each select="//cs:DefenceSolicitor/cs:Party">
							<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
							<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:call-template name="str:capitalise">
											<xsl:with-param name="text" select="./cs:Organisation/cs:OrganisationName"/>
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:call-template name="representativeDescriptionForSolicitor">
											<xsl:with-param name="representativeType" select="../@RepresentationType"/>
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:if test="../cs:StartDate">
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="../cs:StartDate"/>
											</xsl:call-template>
											<xsl:choose>
												<xsl:when test="not (number(../cs:EndDate))">
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
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">							
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt"> 
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
				</fo:block>
			</xsl:if>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
		</xsl:if>
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
		<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
			<xsl:text>Prosecution </xsl:text>
			<xsl:if test="$prosecution/cs:ProsecutingOrganisation/cs:OrganisationName">
				<xsl:value-of select="$prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
			</xsl:if>
		</fo:block>
		<fo:block text-decoration="underline" font-size="10pt">
			<fo:inline font-weight="bold">
				<xsl:text>Advocate</xsl:text>
			</fo:inline>
		</fo:block>
		<fo:block font-size="10pt"> </fo:block>
		<xsl:choose>
			<xsl:when test="$prosecutionAdvocates/cs:ProsecutionAdvocate">
				<fo:block>
				<fo:table table-layout="fixed">
					<fo:table-column column-number="1" column-width="180px"/>
					<fo:table-column column-number="2" column-width="45px"/>
					<fo:table-column column-number="3" column-width="70px"/>
					<fo:table-column column-number="4" column-width="110px"/>
					<fo:table-column column-number="5" column-width="45px"/>
					<fo:table-body>
						<xsl:for-each select="$prosecutionAdvocates/cs:ProsecutionAdvocate/cs:Advocate">
							<xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
							<xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:if test="starts-with(./../@QC, 'y')">
											<xsl:text>(QC)</xsl:text>
										</xsl:if>
										<xsl:if test="not (starts-with(./../@QC, 'y'))">
											<xsl:text>(Counsel)</xsl:text>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:if test="./cs:StartDate">
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="./cs:StartDate"/>
											</xsl:call-template>
											<xsl:choose>
												<xsl:when test="not (number(./cs:EndDate))">
												</xsl:when>
												<xsl:otherwise>
													<xsl:text> to </xsl:text>
													<xsl:call-template name="util:ukdate_mon">
														<xsl:with-param name="inDate" select="./cs:EndDate"/>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
						<xsl:for-each select="$prosecutionAdvocates/cs:ProsecutionAdvocate/cs:Solicitor/cs:Party">
							<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
							<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="10pt">
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
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
										<xsl:variable name="inPerson">
											<xsl:call-template name="str:to-lower">
												<xsl:with-param name="text" select="./cs:Person/cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
											</xsl:call-template>
										</xsl:variable>
										<xsl:if test="$inPerson!='' and $inPerson!='in person'">
											<xsl:text>(Solicitor)</xsl:text>
										</xsl:if>
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
												<xsl:text> onwards </xsl:text>
											</xsl:if>
											<xsl:if test="../cs:EndDate=''">
												<xsl:text> onwards </xsl:text>
											</xsl:if>
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt">
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block font-size="10pt">
					<xsl:text>In Person</xsl:text>
				</fo:block>
				<fo:block font-size="10pt"> </fo:block>
				<fo:block font-size="10pt"> </fo:block>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="$prosecutionSolicitors/cs:ProsecutionSolicitor/cs:Party">
			<fo:block font-size="10pt">
				<fo:inline font-weight="bold">
					<xsl:text>Solicitors</xsl:text>
				</fo:inline>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="180px"/>
			<fo:table-column column-number="2" column-width="45px"/>
			<fo:table-column column-number="3" column-width="70px"/>
			<fo:table-column column-number="4" column-width="110px"/>
			<fo:table-column column-number="5" column-width="45px"/>
				<fo:table-body>
					<xsl:for-each select="$prosecutionSolicitors/cs:ProsecutionSolicitor/cs:Party">
						<xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
						<xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="str:capitalise">
										<xsl:with-param name="text" select="./cs:Organisation/cs:OrganisationName"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
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
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
				<fo:block  space-after="10pt" keep-with-next="always"
					line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
					<xsl:text>Court Reporting Firms</xsl:text>
				</fo:block>
			</xsl:if>
			<xsl:if test="./cs:Firm/cs:OrganisationCode">
				<fo:block font-size="10pt">
					<xsl:text>(</xsl:text>
					<xsl:value-of select="./cs:Firm/cs:OrganisationCode"/>
					<xsl:text>) </xsl:text>
				</fo:block>
			</xsl:if>
			<fo:block font-size="10pt">
				<xsl:value-of select="./cs:Firm/cs:OrganisationName"/>
			</fo:block>
			<fo:block font-size="10pt"> </fo:block>
		</xsl:for-each>
		<xsl:for-each select="//cs:CourtReportingFirm">
			<xsl:if test="./cs:Reporter">
				<xsl:if test="position()=1">
					<fo:block  space-after="10pt" keep-with-next="always"
						line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
						<xsl:text>Court Reporters</xsl:text>
					</fo:block>				
				</xsl:if>
				<fo:block>
				<fo:table table-layout="fixed">
				<fo:table-column column-number="1" column-width="180px"/>
				<fo:table-column column-number="2" column-width="45px"/>
				<fo:table-column column-number="3" column-width="70px"/>
				<fo:table-column column-number="4" column-width="110px"/>
				<fo:table-column column-number="5" column-width="45px"/>
				<fo:table-body>
					<xsl:for-each select="./cs:Reporter">
						<xsl:sort select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname" data-type="text" order="ascending"/>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:text>( </xsl:text>
									<xsl:value-of select="../cs:Firm/cs:OrganisationCode"/>
									<xsl:text> ) </xsl:text>
									<xsl:call-template name="util:personsFullName">
										<xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt"> </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:text>( </xsl:text>
									<xsl:value-of select="./cs:OperatorType"/>
									<xsl:text> )</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="10pt">
									<xsl:call-template name="util:ukdate_mon">
										<xsl:with-param name="inDate" select="./cs:StartDate"/>
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
		<xsl:if test="//cs:CourtReportingFirm">
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
		<xsl:if test="//cs:Judiciary/cs:Judge">
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Judge</xsl:text>
			</fo:block>
		
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="180px"/>
			<fo:table-column column-number="2" column-width="45px"/>
			<fo:table-column column-number="3" column-width="70px"/>
			<fo:table-column column-number="4" column-width="110px"/>
			<fo:table-column column-number="5" column-width="45px"/>
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
							<fo:block font-size="10pt">
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
							</fo:block>
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
							<fo:block font-size="10pt">
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
		</xsl:if>
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
		<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
			<xsl:text>Bail/Custody Status</xsl:text>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="225px"/>
		<fo:table-column column-number="2" column-width="70px"/>
		<fo:table-column column-number="3" column-width="160px"/>
			<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
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
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt">
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:for-each select="//cs:DefendantBailApplications/cs:DefendantBailApplication">
				<xsl:sort select="./cs:ApplicationDate" data-type="text" order="ascending"/>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:text>Application for bail on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="./cs:ApplicationDate"/>
							</xsl:call-template>
							<xsl:text>: </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="./cs:Result"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:for-each>
			<xsl:for-each select="//cs:Hearings/cs:Hearing/cs:HearingBailChanges">
				<xsl:sort select="./cs:BailChangeDate" data-type="text" order="ascending"/>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
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
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:for-each>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<xsl:if test="./cs:TrialRecordSheet/cs:BailStatusAtStartOfHearing">
							<xsl:text>At start of trial: </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="./cs:TrialRecordSheet/cs:BailStatusAtStartOfHearing"/>
							</xsl:call-template>
						</xsl:if>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt">
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:if test="./cs:TrialRecordSheet/cs:BailStatusPutBackforSentence">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:text>Put back for sentence: </xsl:text>
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="./cs:TrialRecordSheet/cs:BailStatusPutBackforSentence"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
		</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
		<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
			<xsl:variable name="iHearings1" select="count(//cs:Hearings/cs:Hearing/)"/>
			<xsl:if test="$iHearings1>0">
				<xsl:text>Hearings and other important dates</xsl:text>
			</xsl:if>
		</fo:block>	
		<!-- this template outputs the hearing information along with key dates -->
		<fo:block>
		<fo:table table-layout="fixed">			
		<fo:table-column column-number="1" column-width="450px"/>
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt" class="HearingBlock">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<!-- I am repeating this section because the preliminary and normal hearings were all being added to the same row making the details unreadable -->
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt" class="HearingBlock">
						<xsl:variable name="iHearings3" select="count(//cs:Hearings/cs:Hearing[starts-with(./@PreliminaryHearing, 'n')]/cs:HearingDate)"/>
						<xsl:if test="$iHearings3>0">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="450px"/>
		<fo:table-body>
			<!-- First Fixed Trail Date -->
			<xsl:if test="//cs:Hearings/cs:FirstFixedTrialDate != '' ">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt" class="HearingBlock">
							<xsl:text>1st</xsl:text>
							<xsl:text> Fixed on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="//cs:Hearings/cs:FirstFixedTrialDate"/>
							</xsl:call-template>
							<xsl:text> (</xsl:text>
							<xsl:value-of select="//cs:Hearings/cs:FirstHearingType"/>
							<xsl:text>)</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<!-- Arraigned On -->
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt" class="HearingBlock">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<!-- Tried On -->
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt" class="HearingBlock">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<!-- Convicted On -->
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt" class="HearingBlock">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<!-- END OF CR44 Changes -->
			<!-- Sentence or Order Made -->
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt" class="HearingBlock">
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:if test="//cs:SentenceDeferredToDate">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt" class="HearingBlock">
							<xsl:text>Sentence deferred until </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="//cs:SentenceDeferredToDate"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="/cs:TrialRecordSheet/cs:DateSentenceOrOrderMade">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt" class="HearingBlock">
							<xsl:text>Sentence/Order made on </xsl:text>
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="/cs:TrialRecordSheet/cs:DateSentenceOrOrderMade"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
			<xsl:if test="//cs:LinkedCases">
				<xsl:variable name="linkedCaseLimit">
					<xsl:value-of select="16"/>
				</xsl:variable>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
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
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:if>
		</fo:table-body>
		</fo:table>
		</fo:block>
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
		<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
			<xsl:text>Indictment Charges</xsl:text>
		</fo:block>	
		<fo:block>
		<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="45px"/>
		<fo:table-column column-number="2" column-width="230px"/>
		<fo:table-column column-number="3" column-width="45px"/>
		<fo:table-column column-number="4" column-width="80px"/>
		<fo:table-column column-number="5" column-width="125px"/>
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block font-size="10pt">
						<fo:inline font-weight="bold">
							<xsl:text>Count</xsl:text>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt" font-weight="bold">
						<fo:inline font-weight="bold">
							<xsl:text>Offences and Sentence/Order</xsl:text>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt" font-weight="bold">
						<fo:inline font-weight="bold">
							<xsl:text>Seq</xsl:text>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt" font-weight="bold">
						<fo:inline font-weight="bold">
							<xsl:text>Plea</xsl:text>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="10pt" font-weight="bold">
						<fo:inline font-weight="bold">
							<xsl:text>Verdict</xsl:text>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:for-each select="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'indictment']">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:value-of select="./@IndictmentNumber"/>
							<xsl:text>/</xsl:text>
							<xsl:value-of select="./@IndictmentCountNumber"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:value-of select="./@CJSoffenceCode"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="./cs:OffenceStatement"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:choose>
								<xsl:when test="string-length(./cs:CRN)=3">
									<xsl:value-of select="./cs:CRN"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="substring(./cs:CRN,21,3)"/>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:value-of select="./cs:Plea"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:value-of select="./cs:Verdict"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt"> </fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">	</fo:block>
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
						<fo:block font-size="10pt">	</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:call-template name="buildOffenceLocationAddress">
								<xsl:with-param name="offenceLocation" select="./cs:OffenceLocation"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">	</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:table table-layout="fixed">
							<fo:table-column column-number="1" column-width="140px"/>
							<fo:table-column column-number="2" column-width="90px"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>
												Force location Code:
											</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:value-of select="./cs:ArrestingPoliceForceCode"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>
												Offence Start Date:
											</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="substring(./cs:OffenceStartDateTime,1,10)"/>
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>
												Offence Start Time:
											</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="substring(./cs:OffenceStartDateTime,12,5) != '00:00' ">
													<xsl:value-of select="substring(./cs:OffenceStartDateTime,12,5)"/>
												</xsl:when>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>
												Offence End Date:
											</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:call-template name="util:ukdate_mon">
												<xsl:with-param name="inDate" select="substring(./cs:OffenceEndDateTime,1,10)"/>
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>
												Offence End Time:
											</xsl:text>
										</fo:block>									
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
													<xsl:value-of select="substring(./cs:OffenceEndDateTime,12,5)"/>
												</xsl:when>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:text>
												Offence committed on Bail:
											</xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<xsl:choose>
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
					<fo:table-cell number-columns-spanned="4">
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
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
											<fo:block font-size="10pt"> </fo:block>
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</fo:inline>
						</fo:block>
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
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:HateCrime">
			<xsl:call-template name="util:HateCrime"/>
		</xsl:if>
		
		<fo:block font-size="10pt" space-after="20pt">
			<xsl:text> </xsl:text>
		</fo:block>
		
		<xsl:if test="//cs:IndictmentComments">
			<fo:block  space-after="5pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Indictment Log</xsl:text>
			</fo:block>
		</xsl:if>
		<fo:block font-size="10pt">
			<xsl:value-of select="//cs:IndictmentComments"/>
		</fo:block>
		<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
	<!-- PR5458 - Tom Muir-Webb-->
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
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Other Orders</xsl:text>
			</fo:block>	
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="480px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<xsl:for-each select="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[not (contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' ')))]">
								<xsl:for-each select="cs:DisposalLine">
									<xsl:value-of select="cs:Data"/>
									<!-- PR5458 - Tom Muir-Webb
									<xsl:call-template name="replace">
										<xsl:with-param name="string" select="cs:Data"/>
									</xsl:call-template>-->
									<xsl:text> </xsl:text>
								</xsl:for-each>
								<fo:block font-size="10pt"> </fo:block>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
			<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Original Magistrate Offences</xsl:text>
			</fo:block>	
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="180px"/>
			<fo:table-column column-number="2" column-width="45px"/>
			<fo:table-column column-number="3" column-width="225px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>
									Charge Description
								</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>
									Seq
								</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>
									Disposed Details
								</xsl:text>
							</fo:inline>
						</fo:block>					
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="//cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:OriginalCharges/cs:OriginalCharge">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="./cs:OriginalChargeDescription"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="./cs:OriginalChargeSequenceNumber"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:for-each select="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
									<!--<xsl:value-of select="."/>-->
									<!-- PR5458 - Tom Muir-Webb-->
									<xsl:call-template name="replace">
										<xsl:with-param name="string" select="."/>
									</xsl:call-template>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
			<fo:block  space-after="10pt" keep-with-next="always"
				line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Offences under Section 51 (1) (b) Crime and Disorder Act 1998</xsl:text>
			</fo:block>	
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="30px"/>
			<fo:table-column column-number="2" column-width="340px"/>
			<fo:table-column column-number="3" column-width="30px"/>
			<fo:table-column column-number="4" column-width="120px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt" font-weight="bold">
							<fo:inline font-weight="bold">
								<xsl:text>No</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt" font-weight="bold">
							<fo:inline font-weight="bold">
								<xsl:text>Offences and Sentence/Order</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt" font-weight="bold">
							<fo:inline font-weight="bold">
								<xsl:text>Seq</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt" font-weight="bold">
							<fo:inline font-weight="bold">
								<xsl:text>Plea</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'summary']">
					<xsl:sort select="substring(./cs:CRN,21,3)" data-type="text" order="ascending"/>
					<xsl:sort select="./@IndictmentCountNumber" data-type="number" order="ascending"/>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="./@IndictmentCountNumber"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:if test="./@CJSoffenceCode">
									<xsl:value-of select="./@CJSoffenceCode"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="./cs:OffenceStatement"/>
								</xsl:if>
								<xsl:if test="not(./@CJSoffenceCode)">
									<xsl:value-of select="./cs:OffenceStatement"/>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="substring(./cs:CRN,21,3)"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="./cs:Plea"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
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
									<xsl:with-param name="offenceLocation" select="./cs:OffenceLocation"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
						<fo:table-cell>
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
											<xsl:value-of select="./cs:ArrestingPoliceForceCode"/>
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
												<xsl:with-param name="inDate" select="substring(./cs:OffenceStartDateTime,1,10)"/>
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
												<xsl:when test="substring(./cs:OffenceStartDateTime,12,5) != '00:00' ">
													<xsl:value-of select="substring(./cs:OffenceStartDateTime,12,5)"/>
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
												<xsl:with-param name="inDate" select="substring(./cs:OffenceEndDateTime,1,10)"/>
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>										
										<fo:block font-size="10pt">
											<xsl:choose>
												<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
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
												<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
													<xsl:value-of select="substring(./cs:OffenceEndDateTime,12,5)"/>
												</xsl:when>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt">
											<fo:inline font-weight="bold">
												<xsl:text>Offence committed on Bail?</xsl:text>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-size="10pt">
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
						<fo:table-cell>
							<fo:block font-size="10pt"> 
								<fo:inline font-weight="bold">
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
											<fo:block font-size="10pt"> </fo:block>
										</xsl:for-each>
									</xsl:if>
									<xsl:value-of select="./cs:TermType"/>
								</fo:inline>
							</fo:block>
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
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
			<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Breaches of Previous Orders</xsl:text>
			</fo:block>	
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="20px"/>
			<fo:table-column column-number="2" column-width="110px"/>
			<fo:table-column column-number="3" column-width="80px"/>
			<fo:table-column column-number="4" column-width="310px"/>
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
								<xsl:text>Made By</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Date</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Offence/Order Breached and New Sentence/Order</xsl:text>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="/cs:TrialRecordSheet/cs:Breaches/cs:Breach">
					<xsl:variable name="breachNo" select="./@CRESTbreachNumber"/>
					<xsl:variable name="breachType" select="./@BreachType"/>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="position()"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:if test="$breachType != 'F'">
									<xsl:if test="starts-with(./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType,'C')">
										<xsl:value-of select="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType"/>
										<xsl:text> at </xsl:text>
									</xsl:if>
									<xsl:value-of select="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseName"/>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="./cs:OriginatingCourt/cs:Date"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="10pt">
								<xsl:value-of select="./cs:OriginatingCourt/cs:OriginalOrderType"/>
							</fo:block>
							<fo:block font-size="10pt">
								<xsl:if test="./cs:OriginalSentence">
									<xsl:text>Original sentence </xsl:text>
									<xsl:value-of select="./cs:OriginalSentence"/>
								</xsl:if>
							</fo:block>
							<fo:block font-size="10pt">
								<xsl:text>Put and </xsl:text>
								<xsl:call-template name="breachAdmitted">
									<xsl:with-param name="admitted" select="./cs:Admitted"/>
								</xsl:call-template>
								<xsl:text> </xsl:text>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="./cs:DatePut"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<!-- Modified for Bichard PR 5352 - Tom Muir-Webb -->
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="3">
							<fo:block font-size="10pt"> </fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:table table-layout="fixed">
							<fo:table-column column-number="1" column-width="270px"/>
							<fo:table-column column-number="2" column-width="40px"/>
							<fo:table-body>
								<xsl:for-each select="/cs:TrialRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach' or ./@ChargeType = 'BAO']">
									<xsl:if test="./@IndictmentNumber = $breachNo">
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt" font-weight="bold">
													<xsl:text>Original Offences and new sentence/order:</xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt" font-weight="bold">
													<xsl:text>Seq</xsl:text>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:value-of select="./@CJSoffenceCode"/>
													<xsl:text> </xsl:text>
													<xsl:value-of select="./cs:OffenceStatement"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:if test="$breachType != 'F'">
														<xsl:value-of select="substring(./cs:CRN,21,3)"/>
													</xsl:if>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
												<fo:block font-size="10pt">
													<xsl:text>Offence Location Address: </xsl:text>
												</fo:block>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
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
												</fo:block>
												<fo:block font-size="10pt"> </fo:block>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:text>Force Location Code: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:value-of select="./cs:ArrestingPoliceForceCode"/>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:text>Offence Start Date: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:call-template name="util:ukdate_mon">
														<xsl:with-param name="inDate" select="./cs:OffenceStartDateTime"/>
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:text>Offence Start Time: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:choose>
														<xsl:when test="substring(./cs:OffenceStartDateTime,12,5) != '00:00' ">
															<xsl:value-of select="substring(./cs:OffenceStartDateTime,12,5)"/>
														</xsl:when>
													</xsl:choose>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:text>Offence End Date: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:call-template name="util:ukdate_mon">
														<xsl:with-param name="inDate" select="./cs:OffenceEndDateTime"/>
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:text>Offence End Time: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:choose>
														<xsl:when test="substring(./cs:OffenceEndDateTime,12,5) != '00:00' ">
															<xsl:value-of select="substring(./cs:OffenceEndDateTime,12,5)"/>
														</xsl:when>
													</xsl:choose>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:text>Offence Committed On Bail: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block font-size="10pt">
													<xsl:choose>
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
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block font-size="10pt"> </fo:block>
												<fo:block font-size="10pt"> 
													<fo:inline font-weight="bold">
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
																<fo:block font-size="10pt"> </fo:block>
															</xsl:for-each>
															<fo:block font-size="10pt"> </fo:block>
														</xsl:if>
													</fo:inline>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
					
									</xsl:if>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block padding="5pt"></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block padding="5pt"></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block padding="5pt"></fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block padding="5pt"></fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="500px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Offences admitted and taken into consideration: </xsl:text>
							</fo:inline>
							<xsl:value-of select="/cs:TrialRecordSheet/cs:NumberOfOffencesTIC"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
		<xsl:if test="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' '))]">">
			<fo:block  space-after="10pt" keep-with-next="always"
			line-height="12pt" font-weight="bold" font-size="10pt" text-decoration="underline" text-align="left">
				<xsl:text>Total Sentence</xsl:text>
			</fo:block>	
			<xsl:for-each select="/cs:TrialRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:CRESTOrderData[contains($totalSentenceTypesList, concat(' ', ./cs:DisposalCode, ' '))]">
				<fo:block font-size="10pt">
					<xsl:for-each select="./cs:DisposalLine">
						<xsl:value-of select="cs:Data"/>
						<!--<xsl:call-template name="replace">
							<xsl:with-param name="string" select="./cs:Data"/>
						</xsl:call-template>-->
						<xsl:text> </xsl:text>
					</xsl:for-each>
				</fo:block>
			</xsl:for-each>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-number="1" column-width="400px"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="10pt">	
								<fo:inline font-weight="bold">
									<xsl:text>The defendant was eligible for a mandatory sentence under </xsl:text>
									<xsl:text>The Crime and Sentence ACT 1997 </xsl:text>
									<xsl:text>but special circumstances were found and such a sentence was not imposed</xsl:text>
								</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
			<fo:block>
			<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="400px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-size="10pt">
							<fo:inline font-weight="bold">
								<xsl:text>Decision of the Court of Appeal (Criminal Division)</xsl:text>
							</fo:inline>
						</fo:block>
						<fo:block font-size="10pt"> </fo:block>
						<fo:block font-size="10pt"> </fo:block>
						<fo:block font-size="10pt">
							<xsl:value-of select="/cs:TrialRecordSheet/cs:CourtOfAppealResult"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
			</fo:table>
			</fo:block>
			<fo:block font-size="10pt"><fo:leader leader-pattern="rule" leader-length="100%"/></fo:block>
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
		<xsl:variable name="result">
        </xsl:variable>
		<xsl:choose>
			<xsl:when test="starts-with($qc, 'y')">
				<xsl:text>QC</xsl:text>
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
</xsl:stylesheet>
