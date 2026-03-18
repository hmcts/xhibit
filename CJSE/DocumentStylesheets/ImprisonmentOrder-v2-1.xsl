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
		<releaseinfo role="meta">Version 2-1a</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Imprisonment Order Stylesheet</title>
	<para>File name : ImprisonmentOrder-v2-1.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Imprisonment Order in html format</para>
			<para>Addition of text for RFC1344</para>
			<para>CustodialSentence now CustodialTerm </para>		
			<para>Section44 is now an attribute</para>	
			<para>Section91 is now an attribute</para>	
			<para>ChapterIII is now RequiredCustodial</para>
			<para>Added CR27</para>
			<para>Added Pr57241 Fix</para>
			<para>Added Pr57259 Fix</para>
			<para>PR57317 - remove address lines</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'1b'" />
<xsl:variable name="stylesheet" select="'ImprisonmentOrder-v2-1.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-10-10'" />
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
							<xsl:text>Order for imprisonment</xsl:text>
						</p>
					</xsl:with-param>					
					<xsl:with-param name="OrderHeaderRoot" select="/cs:ImprisonmentOrder/cs:OrderHeader"/>
				</xsl:call-template>
				
				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:ImprisonmentOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<!--KN 2005-05-17 CustodialSentence changed to CustodialTerm --> 
				<xsl:apply-templates select="//cs:CustodialTerm" mode="custodial"/>
				<xsl:apply-templates select="//cs:RequiredCustodial" />

				<!-- RFC 1344 Added new section -->
				<xsl:call-template name="orderNotes" />

				<xsl:apply-templates select="//cs:CustodialTerm[./cs:ExtendedPeriod]" mode="extension"/>
				<xsl:apply-templates select="//cs:ReturnToImprisonment" />
				<xsl:apply-templates select="//cs:Section28" />
				
				<!-- RFC 1344 Added new section -->
				<xsl:call-template name="orderNotesPart2" />
				
				<xsl:apply-templates select="//cs:Section86" />
							
				<xsl:call-template name="util:AdditionalNotes" />
				<xsl:call-template name="util:orderSignatory" />
				
				<!--CCN400 -->
				<xsl:call-template name="util:Deportation" />
				
				<xsl:call-template name="util:associatedCases">
					<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
					<xsl:with-param name="rulesRequired" select="'false'" />
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
		<refdescription>Shows all personal information plus details of conviction date, court etc.</refdescription>
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
			<listitem>
				<para>Note: Uses the routine util:address_oneline to format the person's address all onto a single line</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:PersonalDetails">
	<!-- details of the defendant -->
		<xsl:variable name="conviction" select="//cs:Conviction" />
		<table width="100%" >
			<tr>
				<td width="20%" valign="top">
					<xsl:text>The </xsl:text>
					<xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
				</td>
				<td width="80%">
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
								<xsl:text>Date of birth : </xsl:text>
								
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
								</xsl:call-template>
								
							</td>
						</tr>
			
						<tr>
							<td>
								<xsl:text>was convicted of crime on </xsl:text>
								<xsl:variable name="convictDate">
									<xsl:choose>
									<xsl:when test="$conviction/cs:ConvictingCourt">
										<xsl:value-of select="$conviction/cs:ConvictingCourt/cs:Date" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$conviction/cs:ConvictionDate"/>
									</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="$convictDate"/>
								</xsl:call-template>
							</td>
							<td />
						</tr>
					</table>
				</td>
			</tr>
			<xsl:if test="$conviction/cs:ConvictingCourt">
				<tr>
					<td />
					<td>
						<xsl:text>at </xsl:text>
						<xsl:value-of select="$conviction/cs:ConvictingCourt/cs:CourtHouse/cs:CourtHouseName" />
						<xsl:if test="//cs:Conviction/cs:ConvictingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
							<xsl:text> Crown Court</xsl:text>
						</xsl:if>
						<xsl:text> and committed for sentence to the Crown Court.</xsl:text>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td />				
				<td>
					<xsl:text>Details of the conviction and sentence are on the court record.</xsl:text>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<hr />
				</td>
			</tr>
			<tr>
				<td width="20%" valign="top">
					<xsl:text>The Court ordered</xsl:text>
				</td>
				<td width="80%">
					<xsl:text>on </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
					</xsl:call-template>
					<xsl:text> that the defendant serve a period of imprisonment, details of which follow.</xsl:text>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CustodialSentence Template		-->
	<!-- **************************************** -->

	<doc:template name="CustodialTerm mode='custodial'" xmlns="">
		<refpurpose>Outputs the details of the custodial sentence.</refpurpose>
		<refdescription>Gives the details of the custodial sentence imposed on the defendant by the court.
	The custodial sentence can be either for life or for a specified period of time. Where it is for a specific period of time then 
	additional information (@TermType) may be present which indicates wether this sentence is to
	be concurrent, consecutive or before any other periods of imprisonmemnt which may have been ordered.  
	</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: template util:decodeDuration is used to format the period of imprisonmemnt</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:CustodialTerm" mode="custodial">
		<hr />
		<table width="100%" >
			<tr>
				<td width="20%" valign="top">
					<xsl:text>Custodial sentences </xsl:text>
				</td>
				<td width="80%">
					<xsl:text>The court ordered that the defendant be sentenced to  </xsl:text>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<strong>
					<xsl:choose>
					<xsl:when test="./cs:LifeSentence/@MandatoryLife">
						<xsl:text>imprisonment for life.</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="./cs:Term"/>
						</xsl:call-template>
						<xsl:text> imprisonment.</xsl:text>
					</xsl:otherwise>
					</xsl:choose>
					</strong>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<xsl:if test="not (./cs:LifeSentence/@MandatoryLife)and
					              ./cs:Term/@TermType" >
						<xsl:text>This sentence was ordered to be </xsl:text>
						<xsl:variable name="termType" >
							<xsl:call-template name="str:to-lower">
								<xsl:with-param name="text" select="./cs:Term/@TermType" />
							</xsl:call-template>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$termType = 'concurrent'">
								<xsl:text>concurrent to </xsl:text>
							</xsl:when>
							<xsl:when test="$termType = 'consecutive'">
								<xsl:text>consecutive to </xsl:text>
							</xsl:when>
							<xsl:when test="$termType='before'">
								<xsl:text>before</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$termType" />
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text> any other periods of imprisonment to which the</xsl:text>
						<xsl:text> defendant was subject prior to the making of this order.</xsl:text>
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CustodialSentence Template		-->
	<!-- **************************************** -->

	<doc:template name="CustodialTerm mode='extension'" xmlns="">
		<refpurpose>Outputs the details of the extended custodial sentence which may be applied for particular types of offence.</refpurpose>
		<refdescription>Gives the details of the extended custodial sentence imposed on the defendant by the court.
	The extended custodial sentence is shown as the total sentence, and its component parts - the custodial period and the extended period. 
	Additional information (@TermType) may be present which indicates wether this sentence is to
	be concurrent, consecutive or before any other periods of imprisonmemnt which may have been ordered.  
	</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: template util:decodeDuration is used to format the various sentence periods</para>
			</listitem>
			<listitem>
				<para>Note: CustodialTerm/Section44 = 'yes' then show the 'Section44' paragraph.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:CustodialTerm[./cs:ExtendedPeriod]" mode="extension">
		<hr />
		<table width="100%" >
			<tr>
				<td width="20%" valign="top">
					<xsl:text>Extended sentence</xsl:text>
					<br />
					<small>
						<xsl:text>(for sexual or violent offences)</xsl:text>
					</small>
				</td>
				<td width="80%">
					<xsl:text>The court ordered that the defendant be sentenced 
					          under section 85 of the Powers of Criminal Courts (Sentencing) Act 2000 to </xsl:text>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<xsl:text>an extended sentence of </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="./cs:TotalTerm"/>
					</xsl:call-template>
					<xsl:text> comprising</xsl:text>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<xsl:text>a custodial term of </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="./cs:Term"/>
					</xsl:call-template>
					<xsl:text> and</xsl:text>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<xsl:text>an extension period of </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="./cs:ExtendedPeriod"/>
					</xsl:call-template>
					<xsl:text>.</xsl:text>
				</td>
			</tr>
			<xsl:if test="./cs:ExtendedPeriod/@TermType">
				<tr>
					<td />
					<td>
						<xsl:if test="./cs:ExtendedPeriod/@TermType">
							<xsl:text>This sentence was ordered to be </xsl:text>
							<xsl:variable name="termType" >
								<xsl:call-template name="str:to-lower">
									<xsl:with-param name="text" select="./cs:ExtendedPeriod/@TermType" />
								</xsl:call-template>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$termType = 'concurrent'">
									<xsl:text>concurrent to </xsl:text>
								</xsl:when>
								<xsl:when test="$termType = 'consecutive'">
									<xsl:text>consecutive to </xsl:text>
								</xsl:when>
								<xsl:when test="$termType='before'">
									<xsl:text>before</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$termType" />
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text> any other periods of imprisonment to which the</xsl:text>
							<xsl:text> defendant was subject prior to the making of this order.</xsl:text>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="./cs:Section44 = 'yes'">
				<tr>
					<td />
					<td>
						<xsl:text>The provisions of section 44 of the Criminal Justice Act 1991, as substituted by section 59
						          of the Crime and Disorder Act 1998 (as amended by paragraph 141 of Schedule 9  to the
								  Powers of Criminal Courts (Sentencing) Act 2000) apply in this case. </xsl:text>
						
					</td>
				</tr>
			</xsl:if>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- ReturnToImprisonment Template	-->
	<!-- **************************************** -->
	
	<doc:template name="ReturnToImprisonment" xmlns="">
		<refpurpose>Outputs the Return to Imprisonment section of the Imprisonment Order if applicable.</refpurpose>
		<refdescription>This section is printed where a defendant has committed an offence whilst being on early release from impriosonment for an earlier 	offence.
	Gives the date of the new offence which has occasioned a return to prison, and details of the original order which resulted in the original imprisonment.
	Additional information (@TermType) may be present which indicates wether this sentence is to
	be concurrent, consecutive or before any other periods of imprisonmemnt which may have been ordered.  
	</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: template util:getDateQualifier is used to output the text 'not later than' rather than 'before'</para>
			</listitem>
			<listitem>
				<para>Note: The duration of the return to imprisonmemnt is shown unless  ReturnPeriod/Max116 is present 
	in which case the text relating to 'maximum specified by Section 116' is shown instead. </para>
			</listitem>
			<listitem>
				<para>Note: The paragraph relating to the total period of return is shown if  TotalPeriodOfReturn is present.</para>
			</listitem>
			<listitem>
				<para>Note: util:decodeDuration is used to format any periods of imprisonment.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:ReturnToImprisonment">
		<hr />
		<table width="100%" >
			<tr>
				<td width="20%" valign="top">
					<xsl:text>Return of defendants to prison</xsl:text>
				</td>
				<td width="80%">
					<xsl:text>The offence for which the defendant has been convicted by this court was committed </xsl:text>
					<xsl:call-template name="util:getDateQualifier" >
						<xsl:with-param name="text" select="./cs:OffenceDate/@Qualifier" />
					</xsl:call-template>
					<xsl:text> </xsl:text>
					<xsl:call-template name="util:ukdate_mon" >
						<xsl:with-param name="inDate" select="./cs:OffenceDate"/>
					</xsl:call-template>
					<xsl:text>.</xsl:text>

				</td>
			</tr>
			<tr>
				<td />
				<td>
					<xsl:text>This date was, or appeared to be, earlier than the date on which the defendant would, 
					          but for the defendant's release under Part II of the Criminal Justice Act 1991,
							  have completed serving the full sentence imposed by 
					</xsl:text>
					<xsl:if test="./cs:SentencingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
						<xsl:text> the Crown Court at </xsl:text>
					</xsl:if>
					<xsl:value-of select="./cs:SentencingCourt/cs:CourtHouse/cs:CourtHouseName" />					
					<xsl:text> on </xsl:text>
					<xsl:call-template name="util:ukdate_mon" >
						<xsl:with-param name="inDate" select="./cs:SentencingCourt/cs:Date"/>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td />
				<td>
					<xsl:text>The court applied its powers under section 116 of the Powers of Criminal Courts
					          (Sentencing) Act 2000 and ordered that the defendant be returned to prison for 
					</xsl:text>
					<xsl:choose>
						<xsl:when test="cs:ReturnPeriod/cs:Max116">
							<xsl:text> the maximum period specified by section 116</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="util:decodeDuration">
								<xsl:with-param name="duration" select="cs:ReturnPeriod/cs:Period"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>.</xsl:text>
				</td>
			</tr>
			<xsl:if test="cs:ReturnPeriod/@TermType">
				<tr>
					<td />
					<td>					
						<xsl:text>This period of imprisonment was ordered to be served </xsl:text>
						<xsl:choose>
							<xsl:when test="cs:ReturnPeriod/@TermType='Concurrent'">
								<xsl:text>concurrently with</xsl:text>
							</xsl:when>
							<xsl:when test="cs:ReturnPeriod/@TermType='Consecutive'">
								<xsl:text>consecutive to</xsl:text>
							</xsl:when>
							<xsl:when test="cs:ReturnPeriod/@TermType='Before'">
								<xsl:text>before</xsl:text>
							</xsl:when>
						</xsl:choose>
						<xsl:text> any other periods of imprisonment imposed by the court on the same occasion when this 
								   order was made.
						</xsl:text>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="cs:TotalPeriodOfReturn">
				<tr>
					<td />
					<td>
						<xsl:text>The total of the period of return </xsl:text>
						<xsl:if test="./cs:TotalPeriodOfReturn/@IncludesNewOffenceTerm = 'yes'" >
							<xsl:text>and of any custodial term for a new offence </xsl:text>
						</xsl:if>
						<xsl:text>is </xsl:text>
						<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="cs:TotalPeriodOfReturn"/>
						</xsl:call-template>
						<xsl:text>and because this total period of imprisonment is 12 months or less, Section 40A of the
						          Criminal Justice Act 1991, as substituted by section 116 of the Powers of Criminal
								  Courts (Sentencing) Act 2000, applies.
						</xsl:text>
					</td>
				</tr>
			</xsl:if>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- RequiredCustodial Template				-->
	<!-- **************************************** -->
	
	<doc:template name="RequiredCustodial" xmlns="">
		<refpurpose>Outputs ChapterIII paragraph if this element is present and = 'yes'.</refpurpose>
	</doc:template>

	<xsl:template match="cs:RequiredCustodial">
		<xsl:if test=". = 'yes'">
			<hr />
			<table width="100%" >
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Required custodial sentences for certain offences</xsl:text>
					</td>
					<td width="80%">
						<xsl:text>The court ordered that the provisions of Chapter III of Part V of the Powers of 
						Criminal Courts (Sentencing) Act 2000 should apply to the defendant.</xsl:text>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Section28 Template				-->
	<!-- **************************************** -->
	
	<doc:template name="Section28" xmlns="">
		<refpurpose>Outputs Section28 paragraph if this element is present.</refpurpose>
	</doc:template>

	<xsl:template match="cs:Section28">
			<hr />
			<table width="100%" >
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Release of discretionary life prisoners</xsl:text>
					</td>
					<td width="80%">
						<xsl:text>The court ordered that the provisions of section 28 of the Crime Sentences Act 1997,
								  as substituted by paragraph 182 of Schedule 9 to the Powers of Criminal Courts (Sentencing)
								  Act 2000, should apply to the defendant.
						</xsl:text>
					</td>
				</tr>
				<tr>
					<td />
					<td>
						<xsl:text>The court specified the 'relevant part' of the sentence to be </xsl:text>
						<xsl:call-template name="util:decodeDuration">
								<xsl:with-param name="duration" select="cs:DiscretionaryRelevantPart"/>
						</xsl:call-template>
					</td>
				</tr>
			</table>
	</xsl:template>

	<!-- **************************************** -->
	<!-- Section86 Template				-->
	<!-- **************************************** -->
	
	<doc:template name="Section86" xmlns="">
		<refpurpose>Outputs Section86 paragraph if this element is present and = 'yes'.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:Section86">
		<xsl:if test=". = 'yes'">
			<hr />
			<table width="100%" >
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Release of prisoners who have committed sexual offences before 30th September 1998</xsl:text>
					</td>
					<td width="80%">
						<xsl:text>The court ordered that the provisions of section 86 of the Powers of Criminal Courts (Sentencing)
								  Act 2000, should apply to the defendant.
						</xsl:text>
					</td>
				</tr>
				
			</table>
		</xsl:if>
	</xsl:template>	
	
	<!-- **************************************** -->
	<!-- OrderNotes Template			-->
	<!-- **************************************** -->

	<doc:template name="OrderNotes" xmlns="">
		<refpurpose>Creates the Order Notes information</refpurpose>
	</doc:template>
	
	<xsl:template name="orderNotes">

		<xsl:if test="//cs:CJA2003RequiredCustodial= 'yes'">
			<hr/>
			<table width="100%" >
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Second Serious Offence</xsl:text>		
					</td>				
					<td>				
							<xsl:text>The court ordered that a mandatory (automatic) life sentence for a second serious offence, pursuant to
						 section 109(1)(2)(4) of the Powers of the Criminal Courts (Sentencing) Act 2000, should apply to the defendant.</xsl:text>
					</td>
				</tr>	 
			</table>	
		</xsl:if>
	
		<xsl:if test="//cs:ClassAtrafficking= 'yes'">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Third Class A Drug Trafficking Offence</xsl:text>	
					</td>
					<td>
						<xsl:text>The court ordered that a minimum seven years sentence for a third Class A drug trafficking offence, pursuant to
						 		section 110(1)(2)(3) of the Powers of the Criminal Courts (Sentencing) Act 2000, should apply to the defendant.
					</xsl:text>
					</td>
				</tr>
			</table>
		</xsl:if>
		
		<xsl:if test="//cs:DomesticBurglary= 'yes'">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Third Domestic Burglary</xsl:text>	
					</td>
					<td>				
						<xsl:text>The court ordered that a minimum three years sentence for a third domestic burglary, pursuant to 
						section 111(1)(2)(5)(6) of the Powers of the Criminal Courts (Sentencing) Act 2000, should apply to the defendant.</xsl:text>
					</td>
				</tr>
			</table>
		</xsl:if>
		
	</xsl:template>
	
	
	<!-- **************************************** -->
	<!-- OrderNotes Template			-->
	<!-- **************************************** -->

	<doc:template name="OrderNotesPart2" xmlns="">
		<refpurpose>Creates the Order Notes information</refpurpose>
	</doc:template>
	
	<xsl:template name="orderNotesPart2">

		<xsl:if test="//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Minimum Sentence</xsl:text>	
					</td>
					<td>				
						<xsl:text>The court ordered that the defendant do serve a minimum of </xsl:text>
						<!-- Work out number of years -->
						<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm"/>
						</xsl:call-template>
						<xsl:text>imprisonment of </xsl:text>
						<xsl:call-template name="getPersonalGender"/>
						 <xsl:text> mandatory life sentence, pursuant to section 269 of the Criminal Justice Act 2003.</xsl:text>
					</td>
				</tr>
			</table>
		</xsl:if>
	
	</xsl:template>



	<!-- **************************************** -->
	<!-- PersonalGender					-->
	<!-- **************************************** -->

	<doc:template name="getPersonalGender" xmlns="">
		<refpurpose>If the sex of the defendant is known outputs 'him' or 'her' as appropriate.</refpurpose>
		<refdescription>If PersonalDetails/Sex = 'male' or 'female' show 'him' or 'her' otherwise show 'him/her'</refdescription>
	</doc:template>
	
	<xsl:template name="getPersonalGender" >
		<xsl:choose>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'male'">
				<xsl:text>his</xsl:text>
			</xsl:when>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'female'">
				<xsl:text>her</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>his / her</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	
</xsl:stylesheet>
