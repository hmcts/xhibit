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
			<releaseinfo role="meta">Version 1</releaseinfo>
			<author>
				<surname>Son</surname>
				<firstname>Loi</firstname>
			</author>
		</referenceinfo>
		<title>Suspended Sentence Order Stylesheet</title>
		<para>File name : SuspendedSentenceOrder-v1-11.xsl</para>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Suspended Sentence Order in html format</para>
			</section>
		</partintro>
	</doc:reference>
	<!-- Version Information -->
	<xsl:variable name="majorVersion" select="'1'"/>
	<xsl:variable name="minorVersion" select="'11'"/>
	<xsl:variable name="stylesheet" select="'SuspendedSentenceOrder-v1-11.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2020-06-04'"/>
	<!-- End Version Information -->
	<!-- Global Variables -->
	<xsl:variable name="orderDate">
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="Officer1">
		<xsl:value-of select="//cs:ResponsibleOfficer1"/>
	</xsl:variable>
	<xsl:variable name="Officer2">
		<xsl:value-of select="//cs:ResponsibleOfficer2"/>
	</xsl:variable>
	<xsl:variable name="OrderTITLE">
		<xsl:if test="//cs:OrderHeader/cs:RevisionDetails/cs:PreviousOrderDate">
			<xsl:text>Change of </xsl:text>
		</xsl:if>
		<xsl:text>Suspended Sentence Order</xsl:text>
	</xsl:variable>
	<!-- end Global Variables -->
	<xsl:output method="html" indent="yes"/>
	<!-- **************************************** -->
	<!-- Root Template						-->
	<!-- **************************************** -->
	<doc:template name="/" xmlns="">
		<refpurpose>Controls the sequence of elements to be created in the html page.</refpurpose>
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
				<xsl:call-template name="util:javascript"/>
				<xsl:call-template name="util:cssTemplate"/>
			</head>
			<body>
				<xsl:call-template name="util:showLogo"/>
				<!-- +++++++++ following template produces order header             +++++++++ -->
				<xsl:call-template name="util:UniversalOrderHeader">
					<xsl:with-param name="OrderTitle" select="$OrderTITLE"/>
					<xsl:with-param name="OrderHeaderRoot" select="/cs:SuspendedSentenceOrder /cs:OrderHeader"/>
				</xsl:call-template>
				<xsl:call-template name="SSO_Revision"/>
				<!-- +++++++++ following template processes the Personal Details+++++++++ -->
				<xsl:apply-templates select="/cs:SuspendedSentenceOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails"/>
				<xsl:call-template name="SSO_Order"/>
				<xsl:call-template name="SSO_Imprisonment"/>
				<xsl:apply-templates select="//cs:OrderRequirements"/>
				<xsl:call-template name="SSO_AdditionalNotes"/>
				<xsl:call-template name="SSO_Review"/>
				<xsl:call-template name="SSO_Warning"/>
				<xsl:call-template name="SSO_Note"/>
				<xsl:call-template name="util:orderSignatory"/>
				<xsl:call-template name="SSO_Offences"/>

				<xsl:call-template name="util:associatedCases">
					<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
					<xsl:with-param name="rulesRequired" select="'false'"/>
				</xsl:call-template>
				<br/>				
				
				<xsl:apply-templates select="//cs:Attachments"/>
				<!-- KN 20050517 - CR27 	-->
				
				
				<xsl:call-template name="util:copyOrderText"/>
				<xsl:call-template name="util:copyrightText"/>
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
				<para>Note: Uses the routine util:personsFullName to format the name.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:address_oneline to format the person's address all onto a single line</para>
			</listitem>
			<listitem>
				<para>Note: If there is data avalable for the Petty Sessional area then that is output also.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	<xsl:template match="cs:PersonalDetails">
		<table WIDTH="100%">
			<tr>
				<td WIDTH="70%">
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			<tr>
				<td WIDTH="70%">
					<strong>
						<xsl:call-template name="util:address_oneline">
							<xsl:with-param name="personalDetails" select="."/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			<tr>
				<td WIDTH="70%">
					<xsl:text>Date of birth : </xsl:text>
					<strong>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- Template to display Revision Text -->
	<xsl:template name="SSO_Revision">
		<!-- Check to see if this is a revised Community Order -->
		<xsl:if test="//cs:OrderHeader/cs:RevisionDetails/cs:PreviousOrderDate">
			<xsl:text>This order has been revised on </xsl:text>
			<xsl:value-of select="$orderDate"/>
			<xsl:text> and replaces the previous order made on  </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:RevisionDetails/cs:PreviousOrderDate"/>
			</xsl:call-template>
			<br/>
			<xsl:text>Revision number: </xsl:text>
			<xsl:value-of select="//cs:OrderHeader/cs:RevisionDetails/cs:RevisionNumber"/>
		</xsl:if>
	</xsl:template>
	
	<!-- Template to display Order Text -->
	<xsl:template name="SSO_Order">
		<h2>Order</h2>
		<xsl:text>You have been given a Suspended Sentence Order and it is set out in full below.</xsl:text>
		<br/>
		<br/>
		<xsl:variable name="total-requirements">
			<xsl:value-of select="count(/cs:SuspendedSentenceOrder/cs:OrderRequirements/*)"/>
		</xsl:variable>
		<xsl:variable name="display-total">
		<xsl:choose>
		    <xsl:when test="//cs:ElectronicMonitoringProvision='no'">
		        <xsl:value-of select="number($total-requirements) - 1"/>
		    </xsl:when>
		    <xsl:otherwise>
		        <xsl:value-of select="number($total-requirements)"/>
		    </xsl:otherwise>
           </xsl:choose>
           </xsl:variable>
		<xsl:text>There are </xsl:text>
		
		<xsl:choose>
			<xsl:when test="($display-total &gt; 0)">
				<xsl:value-of select="$display-total"/>		
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>no</xsl:text>
			</xsl:otherwise>
		</xsl:choose>		
		
		<xsl:text> requirement(s) to this order. </xsl:text>
	</xsl:template>
	
	<!-- Template to display Imprisonment text -->
	<xsl:template name="SSO_Imprisonment">
		<h2><xsl:value-of select="/cs:SuspendedSentenceOrder/cs:WarningDetentionType"/></h2>
		<xsl:text>You have been sentenced to a term of </xsl:text>
		<xsl:choose>
		<xsl:when test="/cs:SuspendedSentenceOrder/cs:WarningDetentionType='Imprisonment'">imprisonment</xsl:when>
		<xsl:when test="/cs:SuspendedSentenceOrder/cs:WarningDetentionType='Detention'">detention</xsl:when>
		</xsl:choose>
		<xsl:text> lasting </xsl:text>
		<xsl:call-template name="displayImprisonmentTerm">
			<xsl:with-param name="years" select="/cs:SuspendedSentenceOrder/cs:ImprisonmentPeriod/cs:Years"/>
			<xsl:with-param name="months" select="/cs:SuspendedSentenceOrder/cs:ImprisonmentPeriod/cs:Months"/>
			<xsl:with-param name="weeks" select="/cs:SuspendedSentenceOrder/cs:ImprisonmentPeriod/cs:Weeks"/>
			<xsl:with-param name="days" select="/cs:SuspendedSentenceOrder/cs:ImprisonmentPeriod/cs:Days"/>
		</xsl:call-template>
		<xsl:text>.</xsl:text>
		<br/>
		<br/>
		<xsl:text>If you</xsl:text>
		  <ul>
			  <xsl:if test="//cs:OrderRequirements/cs:UnpaidWorkRequirement or //cs:OrderRequirements/cs:ActivityRequirement or //cs:OrderRequirements/cs:ProgrammeRequirement or //cs:OrderRequirements/cs:ProhibitedActivityRequirement or //cs:OrderRequirements/cs:CurfewRequirement or //cs:OrderRequirements/cs:ExclusionRequirement or //cs:OrderRequirements/cs:ResidenceRequirement or //cs:OrderRequirements/cs:MentalHealthTreatmentRequirement or //cs:OrderRequirements/cs:DrugRehabilitationRequirement or //cs:OrderRequirements/cs:AlcoholTreatmentRequirement or //cs:OrderRequirements/cs:SupervisionRequirement or //cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement or //cs:OrderRequirements/cs:AttendanceCentreRequirement = 'yes' or //cs:OrderRequirements/cs:ElectronicMonitoringProvision= 'yes'">
				<li>do not comply with the <b>requirements</b> listed below during the supervision period; or </li>
			</xsl:if>
			<li>commit any other offence during the <b>operational period</b> of 
			<xsl:if test="/cs:SuspendedSentenceOrder/cs:OperationalPeriod='P0Y0M0D'">
			   <xsl:text> 0 month(s)</xsl:text>
			</xsl:if>
				  <xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="/cs:SuspendedSentenceOrder/cs:OperationalPeriod"/>
				  </xsl:call-template>				
				, you may be liable to serve the <b>custodial period</b> in 
				<xsl:choose>
					<xsl:when test="/cs:SuspendedSentenceOrder/cs:WarningDetentionType='Imprisonment'">prison.</xsl:when>
					<xsl:when test="/cs:SuspendedSentenceOrder/cs:WarningDetentionType='Detention'">a Young Offenders Institution.</xsl:when>
				</xsl:choose>
           </li>
       </ul>
	</xsl:template>
	<xsl:template name="displayImprisonmentTerm">
		<xsl:param name="years"/>
		<xsl:param name="months"/>
		<xsl:param name="weeks"/>
		<xsl:param name="days"/>
		<xsl:if test="$years != '' and  $years != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$years"/>
			<xsl:text> year(s)</xsl:text>
		</xsl:if>
		<xsl:if test="$months != '' and  $months != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$months"/>
			<xsl:text> month(s)</xsl:text>
		</xsl:if>
		<xsl:if test="$weeks != '' and  $weeks != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$weeks"/>
			<xsl:text> week(s)</xsl:text>
		</xsl:if>
		<xsl:if test="$days != '' and  $days != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$days"/>
			<xsl:text> day(s)</xsl:text>
		</xsl:if>	
	</xsl:template>	
	<!-- Template to display Requirements text -->
	<xsl:template match="//cs:OrderRequirements">
		<xsl:if test="//cs:OrderRequirements/cs:UnpaidWorkRequirement or //cs:OrderRequirements/cs:ActivityRequirement or //cs:OrderRequirements/cs:ProgrammeRequirement or //cs:OrderRequirements/cs:ProhibitedActivityRequirement or //cs:OrderRequirements/cs:CurfewRequirement or //cs:OrderRequirements/cs:ExclusionRequirement or //cs:OrderRequirements/cs:ResidenceRequirement or //cs:OrderRequirements/cs:MentalHealthTreatmentRequirement or //cs:OrderRequirements/cs:DrugRehabilitationRequirement or //cs:OrderRequirements/cs:AlcoholTreatmentRequirement or //cs:OrderRequirements/cs:SupervisionRequirement or //cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement or //cs:OrderRequirements/cs:RehabilitationActivityRequirement or //cs:OrderRequirements/cs:AttendanceCentreRequirement = 'yes' or //cs:OrderRequirements/cs:ElectronicMonitoringProvision= 'yes'">
		<h2>Requirements</h2>
		<xsl:text>For the supervision period which is the next </xsl:text>		
				<xsl:call-template name="util:decodeDuration">
					<xsl:with-param name="duration" select="//cs:SupervisionPeriod"/>
				</xsl:call-template>			
		<xsl:text>, you will be supervised by </xsl:text>				
		<xsl:value-of select="$Officer2"/>
		<xsl:text>.</xsl:text>
		<br/>
		   You <b>must</b>
		<xsl:call-template name="bullet">
			<xsl:with-param name="text">			
			<xsl:text>keep in touch with your </xsl:text>
			<xsl:value-of select="$Officer1"/>
			<xsl:text> as your </xsl:text>
            <xsl:value-of select="$Officer2"/>			
			<xsl:text> tells you</xsl:text>
			</xsl:with-param>	
		</xsl:call-template>		
		<xsl:call-template name="bullet">
			<xsl:with-param name="text">			
			<xsl:text>tell your </xsl:text>
			<xsl:value-of select="$Officer2"/>	
			<xsl:text> if you intend to change your address</xsl:text>			
			</xsl:with-param>	
		</xsl:call-template>	
		<xsl:call-template name="bullet">
			<xsl:with-param name="text">			
			<xsl:text>comply with the following requirements</xsl:text>			
			</xsl:with-param>	
		</xsl:call-template>				
		<xsl:text>You must:</xsl:text>
			
		<!-- Section A -->
		<xsl:if test="//cs:OrderRequirements/cs:UnpaidWorkRequirement">
		   <h4>Unpaid Work Requirement</h4>
					<xsl:text>carry out unpaid work for </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:choose>
						<xsl:when test="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:Concurrent='no'">
							<xsl:text> consecutive </xsl:text>
						</xsl:when>
						<xsl:when test="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:Concurrent='yes'">
							<xsl:text> concurrent </xsl:text>
						</xsl:when>
					</xsl:choose>
					<xsl:text>to </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:ConcurrentOrConsecutiveWorkDetails"/>
					<xsl:text> as you are told by </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:EndDate"/>
						</xsl:call-template>
					<xsl:text>. </xsl:text>
					<xsl:text>Your Probation officer will tell you who will be responsible for supervising work.</xsl:text>
										
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:UnpaidWorkRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section B -->
		<xsl:if test="//cs:OrderRequirements/cs:ActivityRequirement">
		<h4>Activity Requirement</h4>
					<!-- Option 1 -->
					<xsl:if test="//cs:OrderRequirements/cs:ActivityRequirement/cs:Contact">
						<xsl:text>present yourself to </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ActivityRequirement/cs:Contact"/>
						<xsl:text> at </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ActivityRequirement/cs:Location/cs:Site"/>
					</xsl:if>
					<!-- and -->
					<xsl:if test="//cs:OrderRequirements/cs:ActivityRequirement/cs:Contact and //cs:OrderRequirements/cs:ActivityRequirement/cs:Activity">
						<xsl:text> and </xsl:text>
					</xsl:if>
					<!-- Option 2 -->
					<xsl:if test="//cs:OrderRequirements/cs:ActivityRequirement/cs:Activity">
						<xsl:text>undertake </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ActivityRequirement/cs:Activity"/>
						<xsl:text> for </xsl:text>
						<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:ActivityRequirement/cs:Duration"/>
						</xsl:call-template>
						<xsl:text> in the way you are told by your </xsl:text>
						<xsl:value-of select="//cs:ResponsibleOfficer2"/>
						<xsl:text>. </xsl:text>
					</xsl:if>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:ActivityRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:ActivityRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section C -->
		<xsl:if test="//cs:OrderRequirements/cs:ProgrammeRequirement">
			<h4>Programme Requirement</h4>
					<xsl:text>participate in </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:ProgrammeRequirement/cs:Programme"/>
					<xsl:text> at </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:ProgrammeRequirement/cs:Location/cs:Site"/>
					<xsl:text> for </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:ProgrammeRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:ProgrammeRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:ProgrammeRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section D -->
		<xsl:if test="//cs:OrderRequirements/cs:ProhibitedActivityRequirement">
			<h4>Prohibited Activity Requirement</h4>
					<xsl:text>not take part in </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:ProhibitedActivityRequirement/cs:Activity"/>
					<xsl:text> for </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:ProhibitedActivityRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:ProhibitedActivityRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:ProhibitedActivityRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section E -->
		<xsl:if test="//cs:OrderRequirements/cs:CurfewRequirement">
			<h4>Curfew Requirement</h4>
					<xsl:text>remain in a place or places so specified during periods specified by the court. This curfew requirement last for </xsl:text>
					<xsl:choose>
						<xsl:when test="//cs:OrderRequirements/cs:CurfewRequirement/cs:Duration/@SpecifiedInWeeks ='yes'">
							<!-- As specified in weeks will need to divide number of days by 7 -->
							<xsl:variable name="StripP" select="substring-after(//cs:OrderRequirements/cs:CurfewRequirement/cs:Duration,'P')"/>
							<xsl:variable name="day" select="substring-before($StripP,'D')"/>
							<!-- KN 2005-11-17  Mercator provide the data as a Week number rather than a day  so remove div 7 -->
							<!-- <xsl:variable name="week" select="$day div 7" /> -->
							<xsl:variable name="week" select="$day"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$week"/>
							<xsl:text> weeks</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="util:decodeDuration">
								<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:CurfewRequirement/cs:Duration"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>. </xsl:text>
					<i > See separate Electronic Monitoring sheet for details.</i>
		</xsl:if>
		<!-- Section F -->
		<xsl:if test="//cs:OrderRequirements/cs:ExclusionRequirement">
			<h4>Exclusion Requirement</h4>
					<xsl:text>not enter </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:ExclusionRequirement/cs:Location/cs:Site"/>
					<xsl:if test="//cs:OrderRequirements/cs:ExclusionRequirement/cs:Between">
						<xsl:text> between </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ExclusionRequirement/cs:Between"/>
					</xsl:if>
					<xsl:text>.  </xsl:text>
					<xsl:text>This exclusion requirement lasts for </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:ExclusionRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:ExclusionRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:ExclusionRequirement/cs:AdditionalInformation"/>
					</xsl:if>
					<xsl:text>.</xsl:text> 
					<i>See separate Electronic Monitoring sheet for details.</i>
		</xsl:if>
		<!-- Section G -->
		<xsl:if test="//cs:OrderRequirements/cs:ResidenceRequirement">
			<h4>Residence Requirement</h4>
					<xsl:text>live at </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:ResidenceRequirement/cs:Location/cs:Site"/>
					<xsl:text> and obey any rules that apply there  for </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:ResidenceRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Another Place -->
					<xsl:if test="//cs:OrderRequirements/cs:ResidenceRequirement/cs:AlternativeLocation/cs:Site">
						<xsl:text>You may live at </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ResidenceRequirement/cs:AlternativeLocation/cs:Site"/>
						<xsl:text> with the prior approval of your </xsl:text>
						<xsl:value-of select="//cs:ResponsibleOfficer2"/>												
					</xsl:if>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:ResidenceRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:ResidenceRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section H -->
		<xsl:if test="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement">
			<h4>Mental Health Treatment Requirement</h4>
					<xsl:text>have mental health treatment by or under the direction of a </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:TreatmentDirector"/>
					<!-- Clinic -->
					<xsl:if test="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:TreatmentLocation">
						<xsl:text> at </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:TreatmentLocation/cs:Site"/>
						<xsl:text> as a </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:TreatmentLocation/@ResidencyStatus"/>
					</xsl:if>
					<xsl:text> patient for </xsl:text>
					<!-- Display duration details -->
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:MentalHealthTreatmentRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section I -->
		<xsl:if test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement">
			<h4>Drug Rehabilitation Requirement</h4>
					<xsl:text>have treatment for drug dependency by or under the direction of  </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:TreatmentDirector"/>
					<!-- Clinic -->
					<xsl:if test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:TreatmentLocation/cs:Site">
						<xsl:text> at </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:TreatmentLocation/cs:Site"/>
						<xsl:text> as a </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:TreatmentLocation/@ResidencyStatus"/>
					</xsl:if>
					<xsl:text> for </xsl:text>
					<!-- Display duration details -->
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<xsl:if test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement and //cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Samples='yes'">
					<xsl:text>To be sure that you do not have any illegal drug in your body, you must provide samples for testing at such times or in such circumstances as you Probation Officer or the person responsible for your treatment will tell you. The results of tests on the samples will be sent to your Probation Officer who will report the results to the court. Your Probation Officer will also tell the court how your order is progressing and the views of your treatment provider.</xsl:text>
		</xsl:if>
		<xsl:if test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement and //cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review">
					<xsl:text>The court will review this order </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:Periodicity"/>
					<xsl:text>.  The first review will be on </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:DateTime"/>
					</xsl:call-template>
					<xsl:text> at </xsl:text>
					<xsl:call-template name="util:time">
						<xsl:with-param name="inTime" select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:DateTime"/>
					</xsl:call-template>
					<xsl:text> at </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:Location/cs:CourtHouseName"/>
					<xsl:text>, </xsl:text>
					<xsl:call-template name="util:address_oneline_court">
						<xsl:with-param name="courtDetails" select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:Location"/>
					</xsl:call-template>
					<xsl:choose>
						<xsl:when test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:Location/cs:CourtHouseTelephone = '-'">
							<xsl:text>.  </xsl:text>
						</xsl:when>
						<xsl:when test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:Location/cs:CourtHouseTelephone">
							<xsl:text> (telephone </xsl:text>
							<xsl:value-of select="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:Location/cs:CourtHouseTelephone"/>
							<xsl:text>).  </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>.  </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>You</xsl:text>
					<xsl:choose>
						<xsl:when test="//cs:OrderRequirements/cs:DrugRehabilitationRequirement/cs:Review/cs:CompulsoryAttendance='yes'">
							<xsl:text> must attend the hearing.</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> need not attend the hearing.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
		</xsl:if>
		<!-- Section J -->
		<xsl:if test="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement">
			<h4>Alcohol Treatment Requirement</h4>
					<xsl:text>have treatment for alcohol dependency by or under the direction of </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:TreatmentDirector"/>
					<!-- Clinic -->
					<xsl:if test="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement and //cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:TreatmentLocation">
						<xsl:text> at </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:TreatmentLocation/cs:Site"/>
						<xsl:text> as a </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:TreatmentLocation/@ResidencyStatus"/>
					</xsl:if>
					<xsl:text> for </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:Duration"/>
					</xsl:call-template>
					<xsl:text>.  </xsl:text>
					<!-- Additional Req -->
					<xsl:if test="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:AlcoholTreatmentRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		<!-- Section K -->
		<xsl:if test="//cs:OrderRequirements/cs:SupervisionRequirement">
			<h4>Supervision Requirement</h4>
					<xsl:text>attend appointments with your </xsl:text>
					<xsl:value-of select="//cs:ResponsibleOfficer2"/>
					<xsl:text> or another person at the times and places your </xsl:text>
					<xsl:value-of select="//cs:ResponsibleOfficer2"/>
					<xsl:text> says. </xsl:text>
					<!-- Additional Req -->
					<xsl:if test=" //cs:OrderRequirements/cs:SupervisionRequirement/cs:AdditionalInformation">
						<xsl:value-of select="//cs:OrderRequirements/cs:SupervisionRequirement/cs:AdditionalInformation"/>
					</xsl:if>
		</xsl:if>
		
		<!-- Section L -->
		<xsl:if test="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement">
			<h4>Foreign Travel Prohibition Requirement</h4>
					<xsl:if test="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:Days">
						<xsl:text>on </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:Days"/>
						<xsl:if test=" //cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:FromDate">
							<xsl:text> or </xsl:text>
						</xsl:if>
					</xsl:if>
					<xsl:if test="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:FromDate">
						<xsl:text>from </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:FromDate"/>
						</xsl:call-template>
						<xsl:text> to </xsl:text>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:ToDate"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:text> you are not to travel to </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:ProhibitedFrom"/>
					<xsl:if test="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:Exception">
						<xsl:text> other than </xsl:text>
						<xsl:value-of select="//cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement/cs:Exception"/>
					</xsl:if>
			
		</xsl:if>		
		<!-- Section M -->
		<xsl:if test="//cs:OrderRequirements/cs:RehabilitationActivityRequirement">
			<h4>Rehabilitation Activity Requirement</h4>
					<xsl:text>Participate in Rehabilitation Activity Requirement(s) as instructed for a maximum of </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:RehabilitationActivityRequirement/cs:Duration"/>
					<xsl:text> days.</xsl:text>
		</xsl:if>		
		
		<!-- Section N -->
		<xsl:if test="//cs:OrderRequirements/cs:AttendanceCentreRequirement = 'yes'">
			<h4>Attendance Centre Requirement</h4>
					<xsl:text>attend an attendance centre - see separate sheet for details</xsl:text>
		</xsl:if>
		
		<!-- Section O -->
		<xsl:if test="//cs:OrderRequirements/cs:AlcoholAbstinenceAndMonitoringRequirement">
			<h4>Alcohol Abstinence and Monitoring Requirement</h4>
					<xsl:text>Abstain from alcohol and be monitored for </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:AlcoholAbstinenceAndMonitoringRequirement/cs:Days"/>
					<xsl:text> days.</xsl:text>
		</xsl:if>	
		
		<!-- Section P -->
		<xsl:if test="//cs:OrderRequirements/cs:ElectronicMonitoringProvision= 'yes'">
			<h4>Electronic Monitoring Provision</h4>
					<xsl:text>You will be electronically monitored so that the court can be sure you are complying with the requirements of this order.</xsl:text>
		</xsl:if>
		
		<!-- Section Q -->
		<xsl:if test="//cs:OrderRequirements/cs:TrailMonitoringRequirement">
			<h4>Trail Monitoring Requirement</h4>
					<xsl:text>You will be electronically Trail Monitored for </xsl:text>
					<xsl:value-of select="//cs:OrderRequirements/cs:TrailMonitoringRequirement/cs:Duration"/>
					<xsl:text> days so that the court can be sure you are complying with the requirements of this order.</xsl:text>
		</xsl:if>	
		</xsl:if>
	</xsl:template>
	
	<!-- Template to display SSO Additonal Notes text -->
	<xsl:template name="SSO_AdditionalNotes">
		<xsl:if test="//cs:SuspendedSentenceOrder/cs:AdditionalNotes">
			<h2>Additional Notes</h2>

			<xsl:value-of select="//cs:SuspendedSentenceOrder/cs:AdditionalNotes"/>
			
		</xsl:if>
		
	</xsl:template>

	
	<!-- Template to display Review text -->
	<xsl:template name="SSO_Review">
		<xsl:choose>	
			<xsl:when test="/cs:SuspendedSentenceOrder/cs:Review">
				<xsl:choose>
					<xsl:when test="//cs:OrderRequirements/cs:UnpaidWorkRequirement or //cs:OrderRequirements/cs:ActivityRequirement or //cs:OrderRequirements/cs:ProgrammeRequirement or //cs:OrderRequirements/cs:ProhibitedActivityRequirement or //cs:OrderRequirements/cs:CurfewRequirement or //cs:OrderRequirements/cs:ExclusionRequirement or //cs:OrderRequirements/cs:ResidenceRequirement or //cs:OrderRequirements/cs:MentalHealthTreatmentRequirement or //cs:OrderRequirements/cs:DrugRehabilitationRequirement or //cs:OrderRequirements/cs:AlcoholTreatmentRequirement or //cs:OrderRequirements/cs:SupervisionRequirement or //cs:OrderRequirements/cs:ForeignTravelProhibitionRequirement or //cs:OrderRequirements/cs:AttendanceCentreRequirement = 'yes' or //cs:OrderRequirements/cs:ElectronicMonitoringProvision= 'yes'">
						<h2>Review</h2>
					</xsl:when>
					<xsl:otherwise>
						<h2>2. Review</h2>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>The court will review this order </xsl:text>
				<xsl:value-of select="/cs:SuspendedSentenceOrder/cs:Review/cs:Periodicity"/>
				<xsl:text>.</xsl:text>
				<xsl:if test="/cs:SuspendedSentenceOrder/cs:Review/cs:DateTime">
					<xsl:text> The first review will be held on </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="/cs:SuspendedSentenceOrder/cs:Review/cs:DateTime"/>
					</xsl:call-template>
					<xsl:text> at </xsl:text>
					<xsl:call-template name="util:time">
						<xsl:with-param name="inTime" select="/cs:SuspendedSentenceOrder/cs:Review/cs:DateTime"/>
					</xsl:call-template>
					<xsl:text> at </xsl:text>
					<xsl:value-of select="/cs:SuspendedSentenceOrder/cs:Review/cs:Location/cs:CourtHouseName"/>
					<xsl:text>, </xsl:text>
					<xsl:call-template name="util:address_oneline_court">
						<xsl:with-param name="courtDetails" select="/cs:SuspendedSentenceOrder/cs:Review/cs:Location"/>
					</xsl:call-template>	
					<xsl:text>. You</xsl:text>
					<xsl:choose>
						<xsl:when test="/cs:SuspendedSentenceOrder/cs:Review/cs:CompulsoryAttendance='yes'">
							<xsl:text> must attend the hearing.</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> need not attend the hearing.</xsl:text>
						</xsl:otherwise>
					</xsl:choose>			
				</xsl:if>
				<!-- Petty Sessional Area -->
				<br/>
				<br/>
			</xsl:when>
			<xsl:otherwise>
					<br/><br/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>The Local Justice Area you live in is </xsl:text>
		<xsl:value-of select="//cs:LocalJusticeArea"/>
		<xsl:text>.</xsl:text>				
	</xsl:template>

	<!-- Template to display Warning text -->
	<xsl:template name="SSO_Warning">
		<h2>Warning</h2>
		<xsl:text>If you do not comply with this order, you will be brought back to court.  The court may then</xsl:text>
		<xsl:call-template name="bullet">
			<xsl:with-param name="text">
				<xsl:text>change the order by adding extra requirements</xsl:text>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="bullet">
			<xsl:with-param name="text">
				<xsl:text>pass a different sentence for the original offences; or</xsl:text>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="bullet">
			<xsl:with-param name="text">		
				 <xsl:text>you could be sent to </xsl:text>
				<xsl:choose>
					<xsl:when test="//cs:WarningDetentionType='Detention'">
						<xsl:text>a Young Offenders Institution.</xsl:text>
					</xsl:when>
					<xsl:when test="//cs:WarningDetentionType='Imprisonment'">
						<xsl:text>prison.</xsl:text>
					</xsl:when>
				</xsl:choose>		
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- Template to display  Notes text -->
	<xsl:template name="SSO_Note">
		<h2>Note</h2>
		<xsl:text>Either you or your </xsl:text>
		<xsl:value-of select="//cs:ResponsibleOfficer2"/>
		 <xsl:text> can ask the court to look again at this order and the court can then change it or cancel it if the court feels that is the right thing to do.  The court may also pass a different sentence for the original offence(s).  If you wish to ask the court to look at your order again you should get in touch with the court at the address above.</xsl:text>
	</xsl:template>
	<!-- Template to display Offences text -->
	<xsl:template name="SSO_Offences">
		<h2>Offences</h2>
		<xsl:choose>
			<xsl:when test="//cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStatement">
				<xsl:choose>
					<xsl:when test="count(//cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStatement)=1">
						<xsl:value-of select="//cs:OrderHeader/cs:CaseNumber"/>
						<xsl:text> / </xsl:text>
						<xsl:value-of select="//cs:Defendant/cs:Charges/cs:Charge/cs:OffenceStatement"/>
						<br/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="//cs:Defendant/cs:Charges/cs:Charge">
							<xsl:if test="cs:OffenceStatement != ''">
								<xsl:value-of select="//cs:OrderHeader/cs:CaseNumber"/>
								<xsl:text> / </xsl:text>
								<xsl:value-of select="cs:OffenceStatement"/>
							</xsl:if>
							<br/>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="//cs:Defendant/cs:AdditionalNotes">
					<xsl:value-of select="//cs:Defendant/cs:AdditionalNotes"/>
					<br/>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<!-- Do extended charges no matter what -->
		<xsl:for-each select="//cs:ExtendedInformation/any">
			<xsl:value-of select="."/>
			<br/>
		</xsl:for-each>
		<hr/>
	</xsl:template>
	<!-- Template to display Atatchments text -->
	<xsl:template match="cs:Attachments">	
		<xsl:text>Attached are:</xsl:text>
		<br/>
		<xsl:if test="//cs:Attachments/cs:Attachment='previousConvictionList'">
			<xsl:call-template name="bullet">
				<xsl:with-param name="text">	
					<xsl:text>List of previous convictions</xsl:text>
				</xsl:with-param>		
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="//cs:Attachments/cs:Attachment='preSentenceReport'">
			<xsl:call-template name="bullet">
				<xsl:with-param name="text">	
					<xsl:text>Pre-sentence report</xsl:text>
				</xsl:with-param>		
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="//cs:Attachments/cs:Attachment='medicalReport'">
			<xsl:call-template name="bullet">
				<xsl:with-param name="text">	
					<xsl:text>Medical report</xsl:text>
				</xsl:with-param>		
			</xsl:call-template>
		</xsl:if>		
	</xsl:template>
	
	<!-- Template to display a bullet point with text -->
	<xsl:template name="bullet">
		<xsl:param name="text"/>
		<ul>
			<li>
				<xsl:value-of select="$text"/>
			</li>
		</ul>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="no_bullet">
		<xsl:param name="text"/>
		<table width="100%">
			<td width="5%"/>
			<td width="95%">
				<xsl:value-of select="$text"/>
			</td>
		</table>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="FormatTime">
		<xsl:param name="text"/>
		<xsl:value-of select="$text"/>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="FormatDate">
		<xsl:param name="text"/>
		<xsl:value-of select="$text"/>
	</xsl:template>
</xsl:stylesheet>
