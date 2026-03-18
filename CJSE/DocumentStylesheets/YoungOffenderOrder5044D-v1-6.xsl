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
		<releaseinfo role="meta">Version 1-7</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Young Offender Order Stylesheet</title>
	<para>File name : YoungOffenderOrder5044D-v1-7.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Young Offender Order 5044D in html format</para>
			<para>v1.5. Updates for L-R-4410-01 to cover S236A</para>
			<para>v1.6. Updates for L-R-4851-01 to cover S235A updates</para>
			<para>v1.7 Updates for Prisoner Voting Rights (SR0638)</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'1'" />
<xsl:variable name="minorVersion" select="'7'" />
<xsl:variable name="stylesheet" select="'YoungoffenderOrder5044D-v1-7.xsl'" />
<xsl:variable name="last-modified-date" select="'2018-07-18'" />
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

<xsl:variable name="detentionType" >
	<xsl:choose>
		<xsl:when test="//cs:CustodialSentence/cs:Term/@DetentionType">
			<xsl:call-template name="str:to-lower">
				<xsl:with-param name="text" select="//cs:CustodialSentence/cs:Term/@DetentionType" />
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>detention</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
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
							<xsl:text>Custodial order for persons who are 18 or older but under 21 years old</xsl:text>
						</p>
					</xsl:with-param>					
					<xsl:with-param name="OrderHeaderRoot" select="/cs:CustodialOrder/cs:OrderHeader"/>
				</xsl:call-template>
				
				<!-- +++++++++ following template produces Personal Details            +++++++++ -->
				<xsl:apply-templates select="/cs:CustodialOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<!-- KN 20050517 - CustodialSentence now CustodialTerm -->
				<xsl:apply-templates select="/cs:CustodialOrder/cs:CustodialTerm" mode="custodial"/>
				<xsl:apply-templates select="/cs:CustodialOrder/cs:CustodialTerm[./cs:ExtendedPeriod]" mode="extension"/>
				<xsl:apply-templates select="//cs:CreditForRemand"/>
				<xsl:apply-templates select="//cs:CreditForBail"/>					
				<xsl:apply-templates select="//cs:RequiredCustodial" />
				
				
				<xsl:apply-templates select="//cs:AutomaticDeportation"/>
				<xsl:apply-templates select="//cs:RecommendedDeportation"/>
				
				<!-- RFC 1344 Added new section -->
				<xsl:call-template name="OrderNotes" />								
				<xsl:apply-templates select="//cs:ReturnToImprisonment" />
				<xsl:apply-templates select="//cs:Section28" />					
				<xsl:call-template name="util:AdditionalNotes" />	
				<xsl:call-template name="PrisonerVotingRights" />
				<xsl:call-template name="Footer" />	
				<xsl:call-template name="util:orderSignatory"/>
				<xsl:call-template name="associatedCases" />
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
				<para>Note: Uses the routine util:personsFullName to format the name.</para>
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
					<xsl:text>The defendant</xsl:text>
				</td>		
				<td width="80%">
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
					<xsl:text> (date of birth: </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
					</xsl:call-template>								
					<xsl:text>)</xsl:text>
				</td>
			</tr>			
			<tr>
				<td/>
				<td width="80%">
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
			</tr>
				
			
			<xsl:if test="$conviction/cs:ConvictingCourt">
				<tr>			
					<td/>
					<td width="80%">																	
						<xsl:text>at </xsl:text>
						<xsl:value-of select="$conviction/cs:ConvictingCourt/cs:CourtHouse/cs:CourtHouseName" />
						<xsl:if test="$conviction/cs:ConvictingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
							<xsl:text> Crown Court</xsl:text>
						</xsl:if>
						<xsl:text> and committed for sentence to the Crown Court.</xsl:text>					
					</td>
				</tr>
			</xsl:if>
			<tr>				
				<td/>
				<td width="80%">
					<xsl:text>Details of the conviction and sentence are on the enclosed court record.</xsl:text>
				</td>
			</tr>
			<tr>
				<td colspan="100%">
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
					<xsl:text> that the defendant serve a period of detention in a young offender institution, details of which follow.</xsl:text>
				</td>
			</tr>
			<tr>		
				<td/>
				<td width="80%">					
					<xsl:text>The Crown Court had, or would have had but for the statutory 
							  restrictions upon the imprisonment of young offenders, power to 
							  impose imprisonment on the defendant.
					</xsl:text>					
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
				<para>Note: Section91 = 'yes' - display 'long sentence provisions of Section 91..' paragraph.</para>
			</listitem>
			<listitem>
				<para>Note: HMPleasure = 'yes' - display paragraph relating to Her Majesty's pleasure.	</para>
			</listitem>
			<listitem>
				<para>Note: element Section9394 present - display 'life provisions for young offenders..' paragraph.</para>
			</listitem>
			<listitem>
				<para>Note: template util:decodeDuration is used to format the period of imprisonmemnt	</para>
			</listitem>
		</itemizedlist>
	</doc:template>

	<xsl:template match="cs:CustodialOrder/cs:CustodialTerm" mode="custodial">
	   <xsl:choose>
			<xsl:when test="//cs:CustodialTerm/@IndeterminateSentence = 'yes'">
				<xsl:apply-templates select="//cs:CustodialTerm" mode="indeterminate" />
			</xsl:when>
			<xsl:when test="//cs:CustodialTerm/cs:LifeSentence/@MandatoryLife = 'yes' or //cs:CustodialTerm/cs:LifeSentence/@AutomaticLife = 'yes' or //cs:CustodialTerm/cs:LifeSentence/@DiscretionaryLife = 'yes'">
				<xsl:apply-templates select="//cs:CustodialTerm/cs:LifeSentence" /> 
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="//cs:CustodialOrder/cs:CustodialTerm" mode="detention"/>
			</xsl:otherwise>
	   </xsl:choose>
	</xsl:template>
	
	<xsl:template match="cs:CustodialOrder/cs:CustodialTerm" mode="detention">
		<xsl:choose>		
			<xsl:when test="./cs:ExtendedPeriod">
			</xsl:when>
			<xsl:otherwise>
				<hr/>
				<table width="100%" >
					<tr>				
						<td width="20%" valign="top">
							<xsl:text>Custodial sentences</xsl:text>
						</td>
						<td width="80%">
							<xsl:text>The court ordered that the defendant be sentenced</xsl:text>
						</td>
					</tr>
				</table>
				<table width="100%" >		
					<!--Detention-->		
					<tr>									
						<td/>
						<td width="80%">					
							<xsl:if test="./cs:Term">
								<xsl:text> to </xsl:text>											
								<xsl:call-template name="util:decodeModifiedDuration">
									<xsl:with-param name="duration" select="./cs:Term"/>
								</xsl:call-template>
								<xsl:text> detention</xsl:text>
								<xsl:choose>
									<xsl:when test="./cs:Term/@Concurrent = 'yes'">
										<xsl:text> (concurrent to S235A sentence below)</xsl:text>
									</xsl:when>
									<xsl:when test="./cs:Term/@Consecutive = 'yes'">
										<xsl:text> (consecutive to S235A sentence below)</xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="./cs:s235-236"/>
									<xsl:otherwise>. </xsl:otherwise>
								</xsl:choose>
							</xsl:if>																																										
							<xsl:if test="./cs:s235-236">
								<xsl:choose>
									<xsl:when test="./cs:Term">
										<xsl:text> and under Section 235A Criminal Justice Act 2003</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>under Section 235A Criminal Justice Act 2003 to</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text> serve a Special Custodial Sentence of </xsl:text>
								<xsl:call-template name="util:decodeModifiedDuration">
									<xsl:with-param name="duration" select="./cs:TotalTerm"/>
								</xsl:call-template>
								<xsl:text> comprising a custodial term of </xsl:text>
								<xsl:call-template name="util:decodeModifiedDuration">
									<xsl:with-param name="duration" select="./cs:s235-236/cs:CustodialTerm"/>
								</xsl:call-template>
								<xsl:text>and an extended licence period of </xsl:text>
								<xsl:call-template name="util:decodeModifiedDuration">
									<xsl:with-param name="duration" select="./cs:s235-236/cs:ExtensionPeriod"/>
								</xsl:call-template>
								<xsl:text> </xsl:text>
								<xsl:choose>
									<xsl:when test="./cs:s235-236/@Concurrent = 'yes'">
										<xsl:text> (concurrent to detention above)</xsl:text>
									</xsl:when>
									<xsl:when test="./cs:s235-236/@Consecutive = 'yes'">
										<xsl:text> (consecutive to detention above)</xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:text>. </xsl:text>
							</xsl:if>
						</td>
					</tr>
				</table>
				<table width="100%" >
					<tr>
						<td/>
						<td width="80%">	
							<xsl:choose>
								<xsl:when test="./cs:Term/@TermType" >
									<xsl:text>This sentence was ordered to be </xsl:text>
									<xsl:call-template name="str:to-lower">
										<xsl:with-param name="text" select="./cs:Term/@TermType"/>
									</xsl:call-template>
									<xsl:text> to any other periods of detention to which the defendant was subject prior to the making of this order.</xsl:text>
								</xsl:when>
								<xsl:when test="./cs:s235-236/cs:CustodialTerm/@TermType" >
									<xsl:text>This sentence was ordered to be </xsl:text>
									<xsl:call-template name="str:to-lower">
										<xsl:with-param name="text" select="./cs:s235-236/cs:CustodialTerm/@TermType"/>
									</xsl:call-template>
									<xsl:text> to any other periods of detention to which the defendant was subject prior to the making of this order.</xsl:text>
								</xsl:when>
							</xsl:choose>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	

	<xsl:template match="cs:CustodialOrder/cs:CustodialTerm/cs:LifeSentence">		
		<hr/>
		<table width="100%" >
			<tr>				
				<td width="20%" valign="top">
					<xsl:text>Custodial sentences</xsl:text>
				</td>
				<td width="80%">
					<xsl:text>The court</xsl:text>
					<xsl:choose>
						<xsl:when test="//cs:CustodialTerm/@SectionType = 's224a'">
							<xsl:text>, under section 224A Criminal Justice Act 2003,</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:CustodialTerm/@SectionType = 's225'">
							<xsl:text>, under section 225 Criminal Justice Act 2003,</xsl:text>
						</xsl:when>
					</xsl:choose>
					<xsl:text> ordered that the defendant be sentenced to detention for life.</xsl:text>
				</td>
			</tr>
		</table>
		<table width="100%" >	
			<tr>								
				<td/>
				<td width="80%">		
				<xsl:choose>		
					<xsl:when test="//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm = 'No Term Specified'">
						<xsl:text>The court did not specify a minimum term.</xsl:text>
					</xsl:when>
					<xsl:when test ="not (//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm = 'P0Y0M0W0D')">
						<xsl:text>The court specified that the defendant must serve a minimum term of </xsl:text>
						<xsl:call-template name="util:decodeModifiedDuration">
							<xsl:with-param name="duration" select="//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm"/>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
				</td>
			</tr>	
		</table>
		<table width="100%" >
			<tr>
				<td/>
					<td width="80%">	
					<xsl:if test="//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm/@TermType  " >
						<xsl:text>This sentence was ordered to be </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="//cs:CustodialTerm/cs:LifeSentence/cs:MinimumLifeTerm/@TermType"/>
						</xsl:call-template>
						<xsl:text> to any other periods of detention to which the defendant was subject prior to the making of this order.</xsl:text>
					</xsl:if>
				</td>
			</tr>
		</table>
		<table width="100%" >
			<tr>
				<td/>
					<td width="80%">	
					<xsl:text> The sentence is </xsl:text>
					<xsl:choose>
						<xsl:when test="//cs:CustodialTerm/cs:LifeSentence/@AutomaticLife">
							<xsl:text>Automatic Life.</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:CustodialTerm/cs:LifeSentence/@MandatoryLife">
							<xsl:text>Mandatory Life.</xsl:text>
						</xsl:when>
						<xsl:when test="//cs:CustodialTerm/cs:LifeSentence/@DiscretionaryLife">
							<xsl:text>Discretionary Life.</xsl:text>
						</xsl:when>
					</xsl:choose>
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
	Additional information (@TermType) may be present which indicates whether this sentence is to
	be concurrent, consecutive or before any other periods of imprisonmemnt which may have been ordered.  
	</refdescription>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: template util:decodeDuration is used to format the various sentence periods</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:CustodialOrder/cs:CustodialTerm[./cs:ExtendedPeriod]" mode="extension">
		<hr />
		<table width="100%" >
			<tr>
				<td width="20%" valign="top">
					<xsl:text>Extended sentence (for sexual or violent offences)</xsl:text>
				</td>
				<td width="80%">
					<xsl:text>The court ordered that the defendant be sentenced, 
					          under section </xsl:text>
					<xsl:choose>
						<xsl:when test="./@SectionType='s226a'">
							<xsl:text>226A</xsl:text>
						</xsl:when>
						<xsl:when test="./@SectionType='s227'">
							<xsl:text>227</xsl:text>
						</xsl:when>
					</xsl:choose>
					<xsl:text> of the Criminal Justice Act 2003, to </xsl:text>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="cs:TotalTerm"/>
					</xsl:call-template>
					<xsl:text> comprising a custodial term of </xsl:text>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="cs:Term"/>
					</xsl:call-template>
					<xsl:text> and an extension period of </xsl:text>
					<xsl:call-template name="util:decodeModifiedDuration">
						<xsl:with-param name="duration" select="cs:ExtendedPeriod"/>
					</xsl:call-template>
					<xsl:text>.</xsl:text>
				</td>
			</tr>					
		</table>
		<table width="100%" >
			<tr>
				<td/>
					<td width="80%">	
					<xsl:if test="cs:Term/@TermType" >
						<xsl:text>This sentence was ordered to be </xsl:text>
						<xsl:call-template name="str:to-lower">
							<xsl:with-param name="text" select="cs:Term/@TermType"/>
						</xsl:call-template>
						<xsl:text> to any other periods of detention to which the defendant was subject prior to the making of this order.</xsl:text>
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Credit for time served on remand Template	-->
	<!-- **************************************** -->	
	<doc:template name="CreditForRemand" xmlns="">
		<refpurpose>Outputs Credit for Time Served on Remand paragraph if this element is present. Since Legal Aid Sentecing this now displays time spent in custody while awaiting extradition</refpurpose>
	</doc:template>	
	<xsl:template match="cs:CreditForRemand">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Time spent in custody in a foreign jurisdiction</xsl:text>
					</td>		
					<td width="80%">
						<xsl:text>The court specifies that the defendant served </xsl:text>
							<xsl:value-of select="//cs:CreditForRemand"/>
						<xsl:text> day(s) in custody while awaiting extradition.</xsl:text>
					</td>
				</tr>
			</table>	
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Credit for time served on bail Template	-->
	<!-- **************************************** -->	
	<doc:template name="CreditForBail" xmlns="">
		<refpurpose>Outputs Credit for Time Served on Bail paragraph if this element is present</refpurpose>
	</doc:template>	
	<xsl:template match="cs:CreditForBail">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Credit for time spent on bail with an electronically monitored curfew condition</xsl:text>
					</td>				
					<td width="80%">
						<xsl:text>Under section 240A of the Criminal Justice Act 2003, the court directs that </xsl:text>
							<xsl:value-of select="//cs:CreditForBail"/>
						<xsl:text> days will count towards the sentence.</xsl:text>
					</td>
				</tr>
			</table>	
	</xsl:template>
	
<!-- **************************************** -->
	<!-- Automatic deportation Template	-->
	<!-- **************************************** -->	
	<doc:template name="AutomaticDeportation" xmlns="">
		<refpurpose>Outputs Automatic Deportation statement if element is present</refpurpose>
	</doc:template>	
	<xsl:template match="cs:AutomaticDeportation">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Automatic deportation</xsl:text>
					</td>				
					<td width="80%">
						<xsl:text>As a consequence of the sentence, the provisions of section 32 UK Borders Act 2007 will apply in this case </xsl:text>
					</td>
				</tr>
			</table>	
	</xsl:template>	
	
	<!-- **************************************** -->
	<!-- ReturnToImprisonment Template		-->
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
					<xsl:text>Return of defendants to a young offender institution</xsl:text>
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
					<br>
					<xsl:text>This date was, or appeared to be, earlier than the date on which the defendant would, 
					          but for the defendant's release under Part II of the Criminal Justice Act 1991,
							  have completed serving the full sentence imposed by
					</xsl:text>
					</br>
					<xsl:if test="./cs:SentencingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
						<xsl:text> the Crown Court at </xsl:text>
					</xsl:if>
					<xsl:if test="./cs:SentencingCourt/cs:CourtHouse/cs:CourtHouseType = 'Magistrates Court'">
						<xsl:text> the magistrates' court at </xsl:text>
					</xsl:if>					
					
					<xsl:value-of select="./cs:SentencingCourt/cs:CourtHouse/cs:CourtHouseName" />
					<xsl:text> on </xsl:text>
					<xsl:call-template name="util:ukdate_mon" >
						<xsl:with-param name="inDate" select="./cs:SentencingCourt/cs:Date"/>
					</xsl:call-template>
					<br>
					<xsl:text>The court applied its powers under section 116 of the Powers of Criminal Courts
					          (Sentencing) Act 2000 and ordered that the defendant be returned to a young offender institution for:</xsl:text>
					<xsl:choose>
					<xsl:when test="cs:ReturnPeriod/cs:Max116 = 'yes'">
						<xsl:text> the maximum period specified by section 116</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="cs:ReturnPeriod/cs:Period"/>
						</xsl:call-template>
					</xsl:otherwise>
					</xsl:choose>
					<xsl:text>.</xsl:text>
					</br>
			       <xsl:if test="cs:ReturnPeriod/@TermType">
			            <br>
						<xsl:text>This period of detention was ordered to be served </xsl:text>
						<xsl:choose>
							<xsl:when test="cs:ReturnPeriod/@TermType='Concurrent'">
								<xsl:text>concurrently with</xsl:text>
							</xsl:when>
							<xsl:when test="cs:ReturnPeriod/@TermType='Before'">
								<xsl:text>before</xsl:text>
							</xsl:when>
						</xsl:choose>
						<xsl:text> any other periods of detention imposed by the court on the same occasion when this order was made.</xsl:text>
					   </br>
			        </xsl:if>
			        <xsl:if test="cs:TotalPeriodOfReturn/@IncludesNewOffenceTerm='yes'">
			            <br>
						<xsl:text>The total of the period of return (and any custodial term for a new offence) is</xsl:text>
						<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="//cs:TotalPeriodOfReturn"/>
						</xsl:call-template>
						<xsl:text> and because this total period of imprisonment is 12 months or less, Section 40A of the Criminal Justice Act 1991,</xsl:text>
						<xsl:text> as substituted by section 116 of the Powers of Criminal Courts (Sentencing) Act 2000, applies</xsl:text>
					   </br>
			        </xsl:if>
			</td>
		   </tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- RequiredCustodial Template		-->
	<!-- **************************************** -->
	
	<doc:template name="RequiredCustodial" xmlns="">
		<refpurpose>Outputs Chapter 5 paragraph if this element is present and = 'yes'.</refpurpose>
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
						<xsl:text>The court ordered that the provisions of Chapter 5 of Part 12 of the Criminal Justice Act 2003 should apply to the defendant.</xsl:text>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Indeterminate Sentence Template		-->
	<!-- **************************************** -->
	
	<doc:template name="cs:CustodialTerm" xmlns="" mode="indeterminate">
		<refpurpose>Outputs Indeterminate Sentence paragraph if this element is present and = 'yes'.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:CustodialOrder/cs:CustodialTerm" mode="indeterminate">		
		<hr />
		<table width="100%" >
			<tr>
				<td width="20%" valign="top">
					<xsl:text>Indeterminate sentence for public protection</xsl:text>
				</td>
				<td width="80%">
					<xsl:text>The court ordered that the provisions of section 225 of the Criminal Justice Act 2003 should apply to the defendant. The court specified the defendant must serve a minimum term of </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="./cs:Term"/>
					</xsl:call-template>
					<xsl:text>.</xsl:text>
				</td>
			</tr>
		</table>
	
	</xsl:template>	
	
	<!-- **************************************** -->
	<!-- Automatic Deportation Template	-->
	<!-- **************************************** -->	
	<doc:template name="AutomaticDeportation" xmlns="">
		<refpurpose>Outputs Automatic Deportation paragraph if this element is present</refpurpose>
	</doc:template>	
	<xsl:template match="cs:AutomaticDeportation">	
		<xsl:if test=". = 'yes'">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Automatic Deportation</xsl:text>
					</td>
					<td width="80%">
						<xsl:text>As a consequence of the sentence, the provisions of section 32 UK Borders Act 2007 will apply in this case.</xsl:text>
					</td>
				</tr>
			</table>	
			</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Recommended Deportation Template	-->
	<!-- **************************************** -->	
	<doc:template name="RecommendedDeportation" xmlns="">
		<refpurpose>Outputs Recommended Deportation paragraph if this element is present</refpurpose>
	</doc:template>	
	<xsl:template match="cs:RecommendedDeportation">	
		<xsl:if test=". = 'yes'">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Recommendation for Deportation</xsl:text>
					</td>
					<td width="80%">
						<xsl:text>The court, being satisfied that the defendant has been given such notice as is required under section 6(2) of the Immigration Act 1971 at least 7 days before the date of this order, recommended that a deportation order be made in this case.</xsl:text>
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
				<td/>
				<td width="80%">
					<xsl:text>The court specified the 'relevant part' of the sentence to be </xsl:text>
					<xsl:call-template name="util:decodeDuration">
							<xsl:with-param name="duration" select="cs:DiscretionaryRelevantPart"/>
					</xsl:call-template>
					<xsl:text>.</xsl:text>
				</td>
			</tr>
		</table>
	</xsl:template>

    <!-- **************************************** -->
	<!-- associatedCasesTemplate		-->
	<!-- **************************************** -->
	 
	<doc:template name="associatedCases" xmlns="">
		<refpurpose>Outputs the list of associated cases if any..</refpurpose>
		<refdescription>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<para>Note: template util:associatedCases is called to display the other cases if any associated with this order.</para>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template name="associatedCases" >
		<hr />
		<table width="100%">
		<tr>
			<td width="20%" />
			<td>
				<xsl:call-template name="util:associatedCases">
					<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
					<xsl:with-param name="rulesRequired" select="'false'" />
				</xsl:call-template>
			</td>
		</tr>
		</table>
		<hr />
	</xsl:template>

	<!-- **************************************** -->
	<!-- OrderNotes Template			-->
	<!-- **************************************** -->

	<doc:template name="OrderNotes" xmlns="">
		<refpurpose>Creates the Order Notes information</refpurpose>
	</doc:template>
	
	<xsl:template name="OrderNotes">
		<xsl:if test="//cs:DomesticBurglary= 'yes'">
			<hr/>
			<table width="100%">
				<tr>
					<td width="20%" valign="top">
						<xsl:text>Domestic Burglary</xsl:text>
					</td>
					<td width="80%">
						<xsl:text>The court was satisfied that section 111 of the Powers of Criminal Courts (Sentencing) Act 2000 applied in this case.</xsl:text>
					</td>
				</tr>
			</table>
		</xsl:if>
	
	
	<!-- **************************************** -->
	<!-- PrisonerVotingRights Template			-->
	<!-- **************************************** -->
	<doc:template name="PrisonerVotingRights" xmlns="">
		<refpurpose>Creates the Prisoner Voting Rights text</refpurpose>
	</doc:template>
	<xsl:template name="PrisonerVotingRights">
		<br/>
		<xsl:text>Convicted offenders sentenced to imprisonment lose the right to vote while they are detained in custody.</xsl:text>
		<br/>
	</xsl:template>
	
	
	</xsl:template>
	<!-- **************************************** -->
	<!-- Footer Template			-->
	<!-- **************************************** -->

	<doc:template name="Footer" xmlns="">
		<refpurpose>Creates the Footer</refpurpose>
	</doc:template>
	
	<xsl:template name="Footer">
		<hr/>
		<table width="100%">
			<br/>
			<br/>
			<tr>
				<td width="100%" valign="top" colspan="100%"></td>
			</tr>
		</table>
						
	</xsl:template>

</xsl:stylesheet>
