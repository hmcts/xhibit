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
		<releaseinfo role="meta">Version 1.1</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Warrant After Failure Stylesheet</title>
	<para>File name : WarrantAfterFailure5061A-v1-1.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Bench Warrant in html format</para>
			<para>Warrant for arrest after failure to attend court after breach of a Community/Suspended Sentence Order</para>
		</section>
	</partintro>
</doc:reference>

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'1'" />
<xsl:variable name="minorVersion" select="'1'" />
<xsl:variable name="stylesheet" select="'WarrantAfterFailure5061A-v1-1.xsl'" />
<xsl:variable name="last-modified-date" select="'2010-05-15'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="orderDate" select="//cs:OrderHeader/cs:OrderDate" />
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
				<para>Note: Uses the routine util:javascript to embed the javascript functions (used to load the Crown Logo) in the generated html.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:cssTemplate to embed the CSS stylesheet in the generated html.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:showLogo to embed the link to the Crown logo in the generated html
				</para>
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
				<xsl:with-param name="OrderHeaderRoot" select="/cs:WarrantAfterFailureOrder/cs:OrderHeader"/>
					<xsl:with-param name="OrderTitle">
						<xsl:text>Warrant for arrest after failure to attend court </xsl:text>
						<xsl:text>after a breach of a  </xsl:text>
						<xsl:if test="//cs:WarrantAfterFailureOrder/cs:AssociatedOrderType='communityOrder'">
							<xsl:text>
							    Community Order
							  </xsl:text>
						</xsl:if>
						<xsl:if test="//cs:WarrantAfterFailureOrder/cs:AssociatedOrderType='suspendedSentenceOrder'">
							<xsl:text>
							       Suspended Sentence Order
							 </xsl:text>
						</xsl:if>												
					</xsl:with-param>					
					<xsl:with-param name="OrderTitleDate">					
						<xsl:text>on </xsl:text>
						<xsl:call-template name="date:format-date-time">
							<xsl:with-param name="year" select="substring($orderDate,1,4)" />
							<xsl:with-param name="month" select="substring($orderDate,6,2)" />
							<xsl:with-param name="day" select="substring($orderDate,9,2)" />			
							<xsl:with-param name="format" select="'%D %B %Y'" />
						</xsl:call-template>
					</xsl:with-param>
				</xsl:call-template>
						
				<!-- +++++++++ following template processes the Personal Details+++++++++ -->	
				
	   			<xsl:apply-templates select="/cs:WarrantAfterFailureOrder/cs:OrderHeader/cs:Defendant/cs:PersonalDetails" />
				
				<!-- +++++++++ following template process each section of the Bench Warrant +++++++++ -->	
				<xsl:call-template name="CourtOrder" />				
				<xsl:apply-templates select="cs:OrderAfterFailureOrder/cs:NextAppearance" /> 
				<xsl:call-template name="util:orderSignatory" />
				<xsl:call-template name="util:copyOrderText" />
				<xsl:call-template name="util:copyrightText" />		
			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- PersonalDetails					-->
	<!-- **************************************** -->

	<doc:template name="PersonalDetails" xmlns="">
		<refpurpose>Shows the personal information eg name, birth date, address etc.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:personsFullName to format the name.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:ukdate_mon to format the Date of Birth.
				</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:address_oneline to format the person's address all onto a single line
				</para>
			</listitem>
		</itemizedlist>
	</doc:template>

	<xsl:template match="cs:PersonalDetails">
		<table width="100%" >
			<tr>
				<td width="100%" colspan="100%">				
					<strong>
					TO ALL CONSTABLES						
					</strong>
				</td>
			</tr>
			<tr>
				<td width="100%" colspan="100%">					
					<xsl:call-template name="util:personsFullName">
						<xsl:with-param name="name" select="cs:Name"/>
					</xsl:call-template>
						
					<!-- DOB -->
					<xsl:if test="cs:DateOfBirth">
						
						<xsl:text> (date of birth: </xsl:text>						
							
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
								</xsl:call-template>
							
						<xsl:text>)</xsl:text>						
					</xsl:if>						
				</td>
			</tr>

			<xsl:if test="cs:Address">
				<tr>
					<td valign="top" colspan="100%">
						<xsl:text>of </xsl:text>
						
						<xsl:call-template name="util:address_oneline" >
							<xsl:with-param name="personalDetails" select="." />
						</xsl:call-template>						
					</td>
				</tr>	
			</xsl:if>
			
			<tr>				
				<td colspan="100%">				
					<xsl:text>having been given adequate notice of the time and place of proceedings in relation to a breach of a  </xsl:text>
						<xsl:if test="//cs:WarrantAfterFailureOrder/cs:AssociatedOrderType='communityOrder'">
							<xsl:text>
							    Community Order
							  </xsl:text>
						</xsl:if>
                    <xsl:if test="//cs:WarrantAfterFailureOrder/cs:AssociatedOrderType='suspendedSentenceOrder'">
							<xsl:text>
							       Suspended Sentence Order
							 </xsl:text>
						</xsl:if>	
					<xsl:text> imposed by this court, has failed to appear as required.</xsl:text>
				</td>
			</tr>			
		</table>
	</xsl:template>
	<!-- **************************************** -->
	<!-- Court Order			-->
	<!-- **************************************** -->
	<doc:template name="CourtOrder" xmlns="">
		<refpurpose>Outputs the court order details</refpurpose>
		<refdescription>
			<para>Shows wether bail was granted uncondtionally or dependent on conditions.</para>			
		</refdescription>
	</doc:template>
	
	<xsl:template name="CourtOrder" >
		<br/>
		<table width="100%">
			<tr>
				<td width="100%" colspan="100%">				
					<strong>
						<xsl:text> The Court ORDERED</xsl:text>
					</strong>					
				</td>
			</tr>
			<tr>
				<td width="100%" colspan="100%">
					<xsl:text>that you are to arrest </xsl:text>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="//cs:PersonalDetails/cs:Name"/>
						</xsl:call-template>
					<xsl:text>, and to</xsl:text>
				</td>
			</tr>		
			<tr>
				<td width="100%" colspan="100%"/>
			</tr>
			<tr>				
				<td colspan="100%">	
					<xsl:variable name="conditionality" select="/cs:WarrantAfterFailureOrder/cs:Release" />
					<xsl:choose>
						<xsl:when test="$conditionality = 'Refused'">					
		
									<xsl:text>bring </xsl:text>
									<xsl:call-template name="getPersonalGender"/>
										<xsl:text> forthwith before the Crown Court at </xsl:text>
										<xsl:value-of select="//cs:CourtHouseName"/>					
			
						</xsl:when>				
						<!-- Unconditional -->
						<xsl:when test="$conditionality = 'Conditional' and not (cs:OrderAfterFailureOrder/cs:PreConditions | cs:WarrantAfterFailureOrder/cs:PostConditions) ">
								<xsl:text>release </xsl:text>
								<xsl:call-template name="getPersonalGender"/>
								<xsl:text> on bail unconditionally.</xsl:text>
							</xsl:when>
							
							<!-- Conditional -->
							<xsl:otherwise>
								<!-- Must be conditional -->
								<xsl:text>release </xsl:text>
								<xsl:call-template name="getPersonalGender"/>
								<xsl:text> on bail subject to the following conditions:</xsl:text>
								<!-- apply the conditions if any -->
								<xsl:apply-templates select="//cs:WarrantAfterFailureOrder/cs:PreConditions | //cs:WarrantAfterFailureOrder/cs:PostConditions" />
							</xsl:otherwise>	
							
						</xsl:choose>			
				</td>
			</tr>		
		</table>
	</xsl:template>			
	
	<!-- **************************************** -->
	<!-- preConditions					-->
	<!-- **************************************** -->

	<doc:template name="preConditions" xmlns="">
		<refpurpose>Outputs any pre-conditions associated with the bail order.</refpurpose>
		<refdescription>
			<para>Iterates through the list of pre-conditions displaying them.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Note: Show as section A if present
				</para>
				</listitem>
				<listitem>
					<para>Note: If Surety or Security is present the the value should be displayed as formatted currency eg currenct symbol,
				            thousand separator, 2 decimal places, leading zeros suppressed.
				</para>
				</listitem>
				<listitem>
					<para>Note: The currency symbol at the moment is hard coded to GB pound symbol, this would need to be changed if switch to euros. 
				</para>
				</listitem>
				<listitem>
					<para>Note: The template util:bulletRow is called to display each condition as a bulleted item in a row in a table.
				</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:PreConditions" >	
		<table width="100%" >
			<tr>
				<td colspan="100%"></td>
			</tr>
			<tr>			
				<td width="5%"></td>
				<td width="95%">
					<xsl:text>(A) To be complied with </xsl:text>
					<b>
						<xsl:text>BEFORE</xsl:text>
					</b>
					<xsl:text> release from custody:</xsl:text>
				</td>
			</tr>
		</table>
		<table width="100%" >
			<tr>
				<td colspan="100%"></td>
			</tr>
			<tr>			
				<td width="10%"></td>
				<td width="90%">
					<xsl:for-each select="cs:Condition">
						<xsl:variable name="text" >
							<xsl:if test="not (cs:Surety | cs:Security)" >
								<xsl:value-of select="normalize-space(cs:Description)"/>
							</xsl:if>
							<xsl:if test="cs:Surety">
								<xsl:text>To provide </xsl:text>
								<xsl:if test="normalize-space(cs:Description)='surety'">
							          <xsl:text>a </xsl:text>
						       </xsl:if>
								<xsl:value-of select="normalize-space(cs:Description)"/>
								<xsl:text> in the sum of &#x00A3;</xsl:text>
								<xsl:value-of select="format-number(cs:Surety,'##,###,##0.00')"/>
								<xsl:text> to secure the surrender of </xsl:text>
								<xsl:call-template name="util:personsFullName">
									<xsl:with-param name="name" select="//cs:PersonalDetails/cs:Name"/>
								</xsl:call-template>
								<xsl:text> to custody at the time and place directed (recognizance(s) of the </xsl:text>
								<xsl:value-of select="normalize-space(cs:Description)"/>
								<xsl:text> to be endorsed on form 5102D Bail: recognizance of a surety).</xsl:text>
							</xsl:if>
						</xsl:variable>
							<xsl:value-of select="$text"/>
					</xsl:for-each>
				</td>
			</tr>

			<xsl:for-each select="cs:OtherCondition">
				<xsl:variable name="text" >
					<xsl:value-of select="."/>
				</xsl:variable>
					<xsl:value-of select="$text"/>						
			</xsl:for-each>
			
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- postConditions					-->
	<!-- **************************************** -->
	
	 <doc:template name="PostConditions" xmlns="">
		<refpurpose>Outputs any post-conditions associated with the bail order.</refpurpose>
		<refdescription>
			<para>Iterates through the list of post-conditions displaying them.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>Note: Show as section B if present
				</para>
				</listitem>
				<listitem>
					<para>Note: If Surety or Security is present the the value should be displayed as formatted currency eg currenct symbol,
				            thousand separator, 2 decimal places, leading zeros suppressed.
				</para>
				</listitem>
				<listitem>
					<para>Note: The currency symbol at the moment is hard coded to GB pound symbol, this would need to be changed if switch to euros. 
				</para>
				</listitem>
				<para>Note: The template util:bulletRow is called to display each condition as a bulleted item in a row in a table.
				</para>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:PostConditions" >
		<table width="100%" >
			<tr>
				<td colspan="100%"></td>
			</tr>
			<tr>			
				<td width="5%"></td>
				<td width="95%">
					<xsl:text>(B) To be complied with </xsl:text>
					<b>
						<xsl:text>AFTER</xsl:text>
					</b>
					<xsl:text> release from custody:</xsl:text>
				</td>
			</tr>		
		</table>
		<table width="100%" >
			<tr>
				<td colspan="100%"></td>
			</tr>
			<tr>			
				<td width="10%"></td>
				<td width="90%">		
							<xsl:value-of select="//cs:PersonalDetails/cs:Name"/>
				</td>
			</tr>
		</table>				
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- NextAppearance				-->
	<!-- **************************************** -->

	 <doc:template name="NextAppearance" xmlns="">
		<refpurpose>Outputs the details of the next court appearance, 
			and then calls the routine to show the order signatories.</refpurpose>
		<refdescription>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<para>Note: If AppearanceDateTime is present then it is used to show the date and optionally the time of the next
			              appearance, otherwise the text 'on a date and time to be notified' is used. 
				</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: util:ukdate_mon is used to show the date portion of AppearanceDateTime. 
				</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: util:time is used to show the time portion of AppearanceDateTime (signified by the presence of the 'T' separator in
			  AppearanceDateTime). 
				</para>
			</itemizedlist>
			<itemizedlist>
				<para>Note: template util:orderSignatory is used to show the signatories for the order. 
				</para>
			</itemizedlist>
		</refdescription>
	</doc:template>	
	
	<xsl:template match="cs:NextAppearance">
		<table width="100%" >
			<tr>
				<td colspan="100%"></td>
			</tr>
			<tr>			
				<td width="5%"></td>
				<td width="95%">
					<xsl:text>to appear at the Crown Court sitting at </xsl:text>
					<strong>
						<xsl:value-of select="//cs:AppearanceCourt/cs:CourtHouseName"/>			
					</strong>					
					<xsl:text> (or such other places as shall be notified) </xsl:text>					
					<xsl:choose>
						<xsl:when test="//cs:AppearanceDateTime">
							<xsl:variable name="appearance" select="//cs:AppearanceDateTime" />
							<br/>
							<xsl:text> on </xsl:text>														
							<xsl:choose>												
								<xsl:when test="contains($appearance,'T')">
									<strong>
										<xsl:call-template name="util:ukdate_mon">
											<xsl:with-param name="inDate" select="//cs:AppearanceDateTime" />
										</xsl:call-template>
										<xsl:text> at </xsl:text>
										<xsl:call-template name="util:time">
											<xsl:with-param name="inTime" select="//cs:AppearanceDateTime" />
										</xsl:call-template>
									</strong>
								</xsl:when>
								<xsl:otherwise>
									<strong>
										<xsl:call-template name="util:ukdate_mon">
											<xsl:with-param name="inDate" select="//cs:AppearanceDateTime" />
										</xsl:call-template>
									</strong>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>, or </xsl:text>		
						</xsl:when>						
					</xsl:choose>
					<br/>
					<xsl:text>on such day and at such time as the Court may direct, there to surrender </xsl:text>					
					<xsl:call-template name="getPersonalGenderWithSelf"/>					
					<xsl:text> into custody.</xsl:text>						
				</td>
			</tr>			
		</table>
		<table width="100%" >
			<br/>
			<br/>
			<br/>
			<tr>
				<td colspan="100%">Judge of the Crown Court</td>
			</tr>				
		</table>
		<table width="100%" >
			<br/>
			<tr>
			<td colspan="100%"></td>		
			</tr>
		</table>
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
				<xsl:text>him</xsl:text>
			</xsl:when>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'female'">
				<xsl:text>her</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>him / her</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- PersonalGender with Self					-->
	<!-- **************************************** -->

	<doc:template name="getPersonalGenderWithSelf" xmlns="">
		<refpurpose>If the sex of the defendant is known outputs 'him' or 'her' as appropriate.</refpurpose>
		<refdescription>If PersonalDetails/Sex = 'male' or 'female' show 'him' or 'her' otherwise show 'him/her'</refdescription>
	</doc:template>
	
	<xsl:template name="getPersonalGenderWithSelf" >
		<xsl:choose>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'male'">
				<xsl:text>himself</xsl:text>
			</xsl:when>
			<xsl:when test="//cs:OrderHeader/cs:Defendant/cs:PersonalDetails/cs:Sex = 'female'">
				<xsl:text>herself</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>himself / herself</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	 
</xsl:stylesheet>
