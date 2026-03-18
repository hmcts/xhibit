<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:n1="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:util="http://www.courtservice.gov.uk/transforms/courtservice/gcsUtility" xmlns:date="http://xsltsl.org/date-time" xmlns:str="http://xsltsl.org/string" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xso="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="fo" extension-element-prefixes="util date str doc xsd n1 apd cs">
	<xsl:include href="date-time.xsl"/>
	<xsl:include href="string.xsl"/>
	<xsl:include href="gcsUtility.xsl"/>
	<doc:reference xmlns="">
		<referenceinfo>
			<releaseinfo role="meta">Version 1-6</releaseinfo>
			<author>
				<surname>Hingston</surname>
				<firstname>Brian</firstname>
			</author>
		</referenceinfo>
		<title>DVLA D20 form Stylesheet</title>
		<para>File name : DVLAD20-v1-6.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the DVLA D20 form in html format</para>
				<para>New document added as part of L-R-4410-01</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'1'"/>
	<xsl:variable name="minorVersion" select="'6'"/>
	<xsl:variable name="stylesheet" select="'DVLAD20-v1-6.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2021-04-26'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="subjectType">
		<xsl:call-template name="util:getSubjectType">
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
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
				<xsl:call-template name="header"/>
				<xsl:call-template name="offences"/>
				<xsl:call-template name="footer"/>
				<xsl:call-template name="form"/>
				<xsl:call-template name="util:copyrightText"/>
			</body>
		</html>
	</xsl:template>
	<doc:template name="header" xmlns="">
		<refpurpose>Creates the Report Header information - including the Defendant and court details.</refpurpose>
	</doc:template>
	<xsl:template name="header">
		<!-- processes the Header information - defendant and court -->
		<h2>
			<center>
				<xsl:value-of select="'Notice by Court of Order for Endorsement'"/>
			</center>
		</h2>
		<hr/>
		<table WIDTH="100%">
			<tr>
				<td>
					<strong>
						<xsl:text>In the </xsl:text>
						<xsl:value-of select="//cs:CourtHouse/cs:CourtHouseType"/>
					</strong>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:text>at </xsl:text>
					<xsl:value-of select="//cs:CourtHouse/cs:CourtHouseName"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="//cs:CourtHouse/cs:CourtHouseCode"/>
				</td>
			</tr>
			<tr/>
			<tr>
				<td>
					<strong>
						<xsl:text>Case Number: </xsl:text>
					</strong>
					<xsl:value-of select="//cs:CaseNumber"/>
				</td>
			</tr>
		</table>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="33%">
					<span class="emphasis">
						<xsl:text>Defendant Name </xsl:text>
					</span>
				</td>
				<td WIDTH="33%">
					<span class="emphasis">
						<xsl:text>Defendant Address</xsl:text>
					</span>
				</td>
				<td WIDTH="33%">
					<span class="emphasis">
						<xsl:text>Date of Birth </xsl:text>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:PersonName/cs:PersonGivenName3"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:PersonName/cs:PersonGivenName1"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:PersonName/cs:PersonGivenName2"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:PersonName/cs:PersonFamilyName"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:Gender"/>
					<xsl:text>)</xsl:text>
				</td>
				<td>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine1"/>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine2">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine2"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine3">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine3"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine4">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine4"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine5">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:AddressLine5"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:PostCode">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:UnstructuredAddress/cs:PostCode"/>
					</xsl:if>
				</td>
				<td>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:Birthdate"/>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>DVLA use only </xsl:text>
					</span>
				</td>
				<td>
					<span class="emphasis">
						<xsl:text>Convicting Court</xsl:text>
					</span>
				</td>
				<td>
					<span class="emphasis">
						<xsl:text>Date of Conviction </xsl:text>
					</span>
				</td>
			</tr>
			<tr>
				<td/>
				<td>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement/cs:BasicEndorsementDetails/cs:ConvictingCourt"/>
				</td>
				<td>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement/cs:ConvictionDate"/>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Driver Number</xsl:text>
					</span>
				</td>
				<td>
					<span class="emphasis">
						<xsl:text>Licence Produced in Court</xsl:text>
					</span>
				</td>
				<td>
					<span class="emphasis">
						<xsl:text>Licence Issue No </xsl:text>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:BasicDriverDetails/cs:DriverNumber"/>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:LicenceRecordType = '0'">
							<xsl:text>0 - Never held a DVLA Licence</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:LicenceRecordType = '1'">
							<xsl:text>1 - DVLA Provisional</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:LicenceRecordType = '2'">
							<xsl:text>2 - DVLA Full</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:LicenceRecordType = '3'">
							<xsl:text>3 - Non UK</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:LicenceRecordType = '5'">
							<xsl:text>5 - Has been or is a DVLA Licence holder</xsl:text>
						</xsl:when>
					</xsl:choose>
				</td>
				<td>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:DriverLicenceIssue"/>
				</td>
			</tr>
		</table>
		<hr/>
	</xsl:template>
	<doc:template name="offences" xmlns="">
		<refpurpose>Creates the Offences.</refpurpose>
	</doc:template>
	<xsl:template name="offences">
		<!-- processes the offences information -->
		<table WIDTH="100%">
			<tr>
				<td WIDTH="32%"/>
			</tr>
			<tr>
				<td WIDTH="17%"/>
			</tr>
			<tr>
				<td WIDTH="17%"/>
			</tr>
			<tr>
				<td WIDTH="17%"/>
			</tr>
			<tr>
				<td WIDTH="17%"/>
			</tr>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Offence Code </xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:DVLAoffenceCode"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Offence Date </xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:call-template name="util:ukdate">
							<xsl:with-param name="inDate" select="cs:BasicEndorsementDetails/cs:OffenceDate"/>
						</xsl:call-template>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Fine (££££pp) </xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:if test="cs:BasicEndorsementDetails/cs:Fine != '0.00'">
							<xsl:value-of select="cs:BasicEndorsementDetails/cs:Fine"/>
						</xsl:if>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Penalty Points </xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:if test="cs:BasicEndorsementDetails/cs:PenaltyPoints !='0'">
							<xsl:value-of select="cs:BasicEndorsementDetails/cs:PenaltyPoints"/>
						</xsl:if>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Alcohol Level / Drug Level</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:AlcoholLevelAmount"/>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:AlcoholLevelMethod"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Disqualified Period (YYMMDD)</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:DisqualificationPeriod"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Other Sentence</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:SecondarySentenceQualifier"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>PSS / CO</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:PrimarySentenceQualifier"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text> </xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:PrimarySentencePeriod"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>DTTP/DTETP</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:DisqualificationUntilTestPassed"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Interim/Final</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:InterimFinal"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Sentencing Court</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:SentencingCourt"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Date of Sentence (if different)</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:call-template name="util:ukdate">
							<xsl:with-param name="inDate" select="cs:BasicEndorsementDetails/cs:DateOfSentence"/>
						</xsl:call-template>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Date from which disq. removed</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:call-template name="util:ukdate">
							<xsl:with-param name="inDate" select="cs:BasicEndorsementDetails/cs:DateDisqualificationRemoved"/>
						</xsl:call-template>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Date disq suspended pending appeal / further sentence</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:call-template name="util:ukdate">
							<xsl:with-param name="inDate" select="cs:BasicEndorsementDetails/cs:DateDisqualificationSuspended"/>
						</xsl:call-template>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Date disq reimposed pending appeal / further sentence</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:call-template name="util:ukdate">
							<xsl:with-param name="inDate" select="cs:BasicEndorsementDetails/cs:DateDisqualificationReimposed"/>
						</xsl:call-template>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Mitigating Circumstances</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:NoDisqualificationMitigatingCircumstances"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Special Reasons</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:NoDisqualificationSpecialReasons"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Notification of disability</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:NotificationOfDisability"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>			
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Court</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:AppealCourt"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Date</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:call-template name="util:ukdate">
							<xsl:with-param name="inDate" select="cs:BasicEndorsementDetails/cs:AppealDate"/>
						</xsl:call-template>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Was Against Conviction</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:AppealAgainstConviction"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Against Sentence Only</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:AppealAgainstSentenceOnly"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Was Allowed</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:AppealAllowed"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Dismissed</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:AppealDismissed"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Was Abandoned</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:AppealAbandoned"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Was Varied</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:SentenceVaried"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Appeal Was Remitted</xsl:text>
					</span>
				</td>
				<xsl:for-each select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Endorsement">
					<td>
						<xsl:value-of select="cs:BasicEndorsementDetails/cs:Appeals/cs:AppealRemitted"/>
					</td>
				</xsl:for-each>
			</tr>
			<tr/>
		</table>	
		<hr/>	
	</xsl:template>
	<doc:template name="footer" xmlns="">
		<refpurpose>Creates the footer information.</refpurpose>
	</doc:template>
	<xsl:template name="footer">
		<!-- processes the offences information -->
		<table WIDTH="100%">
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Notification Previously Sent </xsl:text>
					</span>
				</td>
				<td>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:EndorsementsNotified"/>
				</td>
			</tr>
			<tr/>
			<tr/>
			<tr/>
			<tr>
				<td>
					<span class="emphasis">
						<xsl:text>Name and/or address on licence if different from above: </xsl:text>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherName !=' '">
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherName"/>
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine1"/>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine2 !=' '">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine2"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine3 !=' '">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine3"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine4 !=' '">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine4"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine5 != ' '">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:AddressLine5"/>
					</xsl:if>
					<xsl:if test="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:PostCode">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="//cs:D20NotificationMessage/cs:DriversWithEndorsements/cs:Driver/cs:OtherAddress/cs:PostCode"/>
					</xsl:if>
				</td>
			</tr>
			<tr/>
			<tr/>
		</table>
		<hr/>
	</xsl:template>
	<doc:template name="form" xmlns="">
		<refpurpose>Creates the form information.</refpurpose>
	</doc:template>
	<xsl:template name="form">
		<strong>
			<xsl:text>Form:  </xsl:text>
		</strong>
			<xsl:value-of select="//cs:Form/cs:FormNumber"/>
			<xsl:text> of </xsl:text>
			<xsl:value-of select="//cs:Form/cs:TotalForms"/>
	</xsl:template>
</xsl:stylesheet>
