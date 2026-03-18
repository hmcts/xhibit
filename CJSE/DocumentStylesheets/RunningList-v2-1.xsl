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
				extension-element-prefixes="date str doc util xsd n1 apd cs">	
	
	<doc:reference xmlns="">
    	<referenceinfo>
			<releaseinfo role="meta">Version 2-1</releaseinfo>
			<author>
				<surname>Cooke</surname>
				<firstname>Malcolm</firstname>
			</author>
		</referenceinfo>
		<title>Running List Stylesheet - RunningList-v2-1.xsl</title>
		<partintro>
			<section>
				<title>Introduction</title>
				<para>This module produces the Running List in html format.</para>
				<para>The report body consists of three sections 
						<itemizedlist>
							<listitem><para>Trial Cases</para></listitem>
							<listitem><para>Committal Cases</para></listitem>
							<listitem><para>Appeal Cases</para></listitem>
						</itemizedlist>
				</para>
			</section>
		</partintro>
	</doc:reference>

		      
<xsl:include href="date-time.xsl" />
<xsl:include href="string.xsl" />
<xsl:include href="gcsUtility.xsl" />


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'0'" />
<xsl:variable name="stylesheet" select="'RunningList-v2-1.xsl'" />
<xsl:variable name="last-modified-date" select="'2005-05-18'" />
<!-- End Version Information -->


<xsl:variable name="reportdate" >
	<xsl:choose>
	<xsl:when test="//cs:ListHeader/cs:EndDate">
		<xsl:value-of select="//cs:ListHeader/cs:EndDate" />
	</xsl:when>
	<xsl:otherwise>
		<xsl:value-of select="//cs:ListHeader/cs:StartDate" />
	</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
			      
<xsl:output method="html" indent="yes"/>

	<!-- **************************************** -->
	<!-- Root Template					-->
	<!-- **************************************** -->

	<xsl:template match="/">
		<html>
			<head>
				<xsl:call-template name="util:cssTemplate" />
			</head>
			<body>

				<!-- +++++++++ following template produces list header             +++++++++ -->
				<xsl:apply-templates select="cs:RunningList/cs:CrownCourt" /> 
				
				<!-- +++++++++ following template produces list body for trial cases              +++++++++ -->			
				<xsl:apply-templates select="cs:RunningList/cs:TrialCases" /> 
				
				<!-- +++++++++ following template produces list body for committal cases          +++++++++ -->			
				<xsl:apply-templates select="cs:RunningList/cs:CommitalCases" /> 
				
				<!-- +++++++++ following template produces list body for appeal cases             +++++++++ -->			
				<xsl:apply-templates select="cs:RunningList/cs:AppealCases" /> 
				
				<!-- +++++++++ following template produces list footer             +++++++++ -->
				<xsl:call-template name="util:listFooter" >
					<xsl:with-param name="court" select="/cs:RunningList/cs:CrownCourt" />
				</xsl:call-template>
								
			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CrownCourt Template			-->
	<!-- **************************************** -->
	
	<doc:template match="CrownCourt" xmlns="">
		<refpurpose>Creates the Report Header information.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:CrownCourt">
	<!-- processes the CrownCourt node - constructs the initial header information for the output -->
		<xsl:variable name="reporttype" select="'Running List'" />
		<h1> The <xsl:value-of select="cs:CourtHouseType"/>
		<xsl:text> at </xsl:text>
		<xsl:value-of select="cs:CourtHouseName"/>
		</h1>
		<h2>
		<xsl:value-of select="$reporttype" />
		
		<xsl:text> for period ending </xsl:text>
		<xsl:call-template name="date:format-date-time">
			<xsl:with-param name="year" select="substring($reportdate,1,4)" />
			<xsl:with-param name="month" select="substring($reportdate,6,2)" />
			<xsl:with-param name="day" select="substring($reportdate,9,2)" />			
			<xsl:with-param name="format" select="'%A %D %B %Y'" />
		</xsl:call-template>
		</h2>
		<xsl:call-template name="publishDate"/>
		<hr />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- TrialCases Template				-->
	<!-- **************************************** -->
	
	<doc:template match="TrialCases" xmlns="">
		<refpurpose>Creates the portion of the report body which consists of Trial Cases.</refpurpose>
		<refdescription>
			<para>Context node is TrialCases. Iterates through Cases and then within that Defendants, creating the detail 
			  		information about each case found.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>The charges faced by each defendant are actually held in the AdditionalNotes associated with each defendant.</para>
				</listitem>
				<listitem>
					<para>The routine util:transformCaseSpecial is used to process the prosecuting organisation name so that the proper case of letters is
				      is maintained for abbreviations eg T.V, House of Lords etc.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template match="cs:TrialCases">
	<!-- processes the Trial Cases nodesets  -->
		<h3>
		<xsl:text>Trial Cases</xsl:text>
		</h3>
		<br />
		<table class="detail" width="100%">
			<!-- table headers -->
			<tr>
				<strong>
					<td width="15%" valign="top">
						<xsl:text>CASE/DEFT.No:</xsl:text>
					</td>
					<td width="30%" valign="top">
						<xsl:text>DEFENDANT NAME/ PTIURN/ SEX/ DATE OF BIRTH/ </xsl:text>
						<br />
						<xsl:text>SOLICITORS/ CHARGES/</xsl:text>
						<br />
						<xsl:text>PDH/ PRELIMINARY HRG DATE</xsl:text>
					</td>
					<td width="10%" valign="top">
						<xsl:text>B/C STATUS:</xsl:text>
					</td>
					<td width="30%" valign="top">
						<xsl:text>COMMITTING / TRANSFERRING COURT: (PROSECUTOR)</xsl:text>
					</td>
					<td width="15%" valign="top">
						<xsl:text>DATE OF COMMITTAL / TC / SENT/ VB SIGNED / TRANSFER / EXEC/ RE-HEARING ORDERED</xsl:text>
					</td>
				</strong>
			</tr>
			<xsl:for-each select="./cs:Case" >
				<xsl:variable name="case" select="." />
				<xsl:variable name="caseNum" select="./cs:CaseNumber"/>
				<xsl:for-each select="./cs:Defendants/cs:Defendant">
					<tr>
						<td valign="top">
							<xsl:value-of select="$caseNum" />-<xsl:number value="position()" format="001"/>
						</td>
						<td valign="top">
							<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
							<br />
							<xsl:for-each select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename">
								<xsl:variable name="foreName" >
									<xsl:call-template name="util:stripCommas">
										<xsl:with-param name="name" select="." />
									</xsl:call-template>
								</xsl:variable>
								<xsl:value-of select="$foreName" />
								<xsl:text> </xsl:text>
							</xsl:for-each>
							<br />
							<xsl:if test="./cs:URN">
								<xsl:value-of select="./cs:URN"/>                    
								<br/>
                            </xsl:if>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
							</xsl:call-template>
							<xsl:text>   Date of Birth: </xsl:text>
							<xsl:call-template name="util:ukdate_mon" >
								<xsl:with-param name="inDate" select="./cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
							</xsl:call-template>
							<xsl:call-template name="solicitor">
								<xsl:with-param name="party" select="cs:Counsel/cs:Solicitor/cs:Party" />
							</xsl:call-template>
							<xsl:apply-templates select="./cs:AdditionalNotes"/>
							<br />
							<xsl:if test="$case/cs:Hearing/cs:HearingDescription" >
								<xsl:value-of select="$case/cs:Hearing/cs:HearingDescription"/>
								<xsl:text>:  </xsl:text>
								<xsl:call-template name="util:ukdate_mon">
									<xsl:with-param name="inDate" select="$case/cs:Hearing/cs:HearingDate"/>
								</xsl:call-template>
							</xsl:if>
						</td>
						<td  valign="top">
							<xsl:choose>
								<xsl:when test="cs:CustodyStatus">
									<xsl:value-of select="cs:CustodyStatus"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>N/A</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td valign="top">
							<xsl:if test="$case/cs:CaseArrivedFrom">
								<xsl:call-template name="util:transformCaseSpecial">
									<xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName" />
								</xsl:call-template>
								<xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
									<xsl:text> Crown Court</xsl:text>
								</xsl:if>
								<br />
							</xsl:if>
							
							<xsl:if test="$case/cs:Prosecution">
								<xsl:call-template name="util:transformCaseSpecial">
									<xsl:with-param name="text" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName" />
								</xsl:call-template>
								<xsl:if test="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress">
									<xsl:text> - </xsl:text>
									<xsl:call-template name="util:transformCaseSpecial">
										<xsl:with-param name="text"  select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress/apd:Line[position()=1]" />
									</xsl:call-template>
								</xsl:if>
								<br />
								<xsl:if test="$case/cs:Prosecution/cs:ProsecutingReference" >
									<xsl:text>PTI Unique Ref:</xsl:text>
									<xsl:value-of select="$case/cs:Prosecution/cs:ProsecutingReference" />
									<br />
								</xsl:if>
							</xsl:if>
							
						</td>
						<td valign="top">
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="$case/cs:DateOfInstigation"/>
							</xsl:call-template>
							<br />
							<xsl:text>Class </xsl:text>
							<xsl:value-of select="$case/cs:CaseClassNumber" />
						</td>
					
					</tr>
					<tr /> <!-- help split up the output -->
				</xsl:for-each>
			</xsl:for-each>
		</table>
		<hr />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CommittalCases Template		-->
	<!-- **************************************** -->
	
	<doc:template match="CommittalCases" xmlns="">
		<refpurpose>Creates the portion of the report body which consists of Committal Cases.</refpurpose>
		<refdescription>
		  <para>Context node is CommittalCases. Iterates through Cases and then within that Defendants, creating the detail 
		  		information about each case found.</para>
		  <para>
		  	<emphasis role="bold">Special Rules</emphasis>
		  </para>
		  <itemizedlist>
		  <listitem>
		  	<para>The charges faced by each defendant are actually held in the AdditionalNotes associated with each defendant.</para>
			<para>The routine util:transformCaseSpecial is used to process the prosecuting organisation name so that the proper case of letters is
			      is maintained for abbreviations eg T.V, House of Lords etc.</para>
		  </listitem>
		  </itemizedlist>
		</refdescription>
	</doc:template>
	
	<xsl:template match="cs:CommitalCases">
	<!-- processes the Committal Cases nodesets  -->
		<h3>
		<xsl:text>Committal For Sentence</xsl:text>
		</h3>
		<br />
		<table class="detail" width="100%">
			<!-- table headers -->
			<tr>
				<strong>
					<td width="15%" valign="top">
						<xsl:text>CASE/DEFT.No:</xsl:text>
					</td>
					<td width="30%" valign="top">
						<xsl:text>DEFENDANT NAME/ PTIURN/ SEX/ DATE OF BIRTH/ </xsl:text>
						<br />
						<xsl:text>SOLICITORS/ CHARGES:</xsl:text>
					</td>
					<td width="10%" valign="top">
						<xsl:text>B/C STATUS:</xsl:text>
					</td>
					<td width="30%" valign="top">
						<xsl:text>COMMITTING / TRANSFERRING COURT: (PROSECUTOR)</xsl:text>
					</td>
					<td width="15%" valign="top">
						<xsl:text>DATE OF COMMITTAL / EXEC / TRANSFER / BRING BACK:</xsl:text>
					</td>
				</strong>
			</tr>
			<xsl:for-each select="./cs:Case" >
				<xsl:variable name="case" select="." />
				<xsl:variable name="caseNum" select="./cs:CaseNumber"/>
				<xsl:for-each select="./cs:Defendants/cs:Defendant">
					<tr>
						<td valign="top">
							<xsl:value-of select="$caseNum" />-<xsl:number value="position()" format="001"/>
						</td>
						<td valign="top">
							<xsl:call-template name="str:to-upper">
								<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
							</xsl:call-template>
							<br />
							<xsl:for-each select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename">
								<xsl:variable name="foreName" >
									<xsl:call-template name="util:stripCommas">
										<xsl:with-param name="name" select="." />
									</xsl:call-template>
								</xsl:variable>
								<xsl:value-of select="$foreName" />
								<xsl:text> </xsl:text>
							</xsl:for-each>
							<br />
							<xsl:if test="./cs:URN">
								<xsl:value-of select="./cs:URN"/>                    
								<br/>
                            </xsl:if>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
							</xsl:call-template>
							<xsl:text>   Date of Birth: </xsl:text>
							<xsl:call-template name="util:ukdate_mon" >
								<xsl:with-param name="inDate" select="./cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
							</xsl:call-template>
							<xsl:call-template name="solicitor">
								<xsl:with-param name="party" select="cs:Counsel/cs:Solicitor/cs:Party" />
							</xsl:call-template>
							<xsl:apply-templates select="./cs:AdditionalNotes"/>

						</td>
						<td valign="top">
							<xsl:choose>
								<xsl:when test="cs:CustodyStatus">
									<xsl:value-of select="cs:CustodyStatus"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>N/A</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td valign="top">
							<xsl:if test="$case/cs:CaseArrivedFrom">
								<xsl:call-template name="util:transformCaseSpecial">
									<xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName" />
								</xsl:call-template>
								<xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
									<xsl:text> Crown Court</xsl:text>
								</xsl:if>
								<br />
							</xsl:if>
							
							<xsl:if test="$case/cs:Prosecution">
								<xsl:call-template name="util:transformCaseSpecial">
									<xsl:with-param name="text" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName" />
								</xsl:call-template>
								<xsl:if test="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress">
									<xsl:text> - </xsl:text>
									<xsl:call-template name="util:transformCaseSpecial">
										<xsl:with-param name="text"  select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress/apd:Line[position()=1]" />
									</xsl:call-template>									
								</xsl:if>
								<br />
							</xsl:if>
						</td>
						<td valign="top">
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="$case/cs:DateOfInstigation"/>
							</xsl:call-template>
						</td>
					
					</tr>
					<tr /> <!-- help split up the output -->
				</xsl:for-each>
			</xsl:for-each>
		</table>
		<hr />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- AppealCases Template			-->
	<!-- **************************************** -->
	
	<doc:template match="AppealCases" xmlns="">
		<refpurpose>Creates the portion of the report body which consists of Appeal Cases.</refpurpose>
		<refdescription>
			<para>Context node is AppealCases. Iterates through Cases and then within that Defendants, creating the detail 
			  		information about each case found.</para>
			<para>
				<emphasis role="bold">Special Rules</emphasis>
			</para>
			<itemizedlist>
				<listitem>
					<para>The routine util:transformCaseSpecial is used to process the prosecuting organisation name so that the proper case of letters is
				      is maintained for abbreviations eg T.V, House of Lords etc.</para>
				</listitem>
			</itemizedlist>
		</refdescription>
	</doc:template>

	<xsl:template match="cs:AppealCases">
	<!-- processes the Appeal Cases nodesets  -->
		<h3>
		<xsl:text>Appeals to the Crown Court</xsl:text>
		</h3>
		<br />
		<table class="detail" width="100%">
			<!-- table headers -->
			<tr>
				<strong>
					<td width="15%" valign="top">
						<xsl:text>CASE/DEFT.No:</xsl:text>
					</td>
					<td width="30%" valign="top">
						<xsl:text>APPELLANT NAME/ PTIURN/ SEX/ DATE OF BIRTH/ </xsl:text>
						<br />
						<xsl:text>SOLICITORS:</xsl:text>
					</td>
					<td width="10%" valign="top">
						<xsl:text>B/C STATUS:</xsl:text>
					</td>
					<td width="30%" valign="top">
						<xsl:text>MAGISTRATES / TRANSFERRING COURT/</xsl:text>
						<br />
						<xsl:text>APPEAL DESCRIPTION: (RESPONDENT)</xsl:text>
					</td>
					<td width="15%" valign="top">
						<xsl:text>DATE OF NOTICE OF APPEAL / TRANSFER / EXEC :</xsl:text>
					</td>
				</strong>
			</tr>
			<xsl:for-each select="./cs:Case" >
				<xsl:variable name="case" select="." />
				<xsl:variable name="caseNum" select="./cs:CaseNumber"/>
				<xsl:for-each select="./cs:Defendants/cs:Defendant">
					<tr>
						<td valign="top">
							<xsl:value-of select="$caseNum" />-<xsl:number value="position()" format="001"/>
						</td>
						<td valign="top">
							<xsl:call-template name="str:to-upper">
								<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
							</xsl:call-template>
							<br />
							<xsl:for-each select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename">
								<xsl:variable name="foreName" >
									<xsl:call-template name="util:stripCommas">
										<xsl:with-param name="name" select="." />
									</xsl:call-template>
								</xsl:variable>
								<xsl:value-of select="$foreName" />
								<xsl:text> </xsl:text>
							</xsl:for-each>
							<br />
							<xsl:if test="./cs:URN">
								<xsl:value-of select="./cs:URN"/>                    
								<br/>
                            </xsl:if>
							<xsl:call-template name="str:capitalise">
								<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
							</xsl:call-template>
							<xsl:text>   Date of Birth: </xsl:text>
							<xsl:call-template name="util:ukdate_mon" >
								<xsl:with-param name="inDate" select="./cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
							</xsl:call-template>
							<xsl:call-template name="solicitor">
								<xsl:with-param name="party" select="cs:Counsel/cs:Solicitor/cs:Party" />
							</xsl:call-template>

						</td>
						<td valign="top">
							<xsl:choose>
								<xsl:when test="cs:CustodyStatus">
									<xsl:value-of select="cs:CustodyStatus"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>N/A</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td valign="top">
							<xsl:if test="$case/cs:CaseArrivedFrom">
								<xsl:call-template name="util:transformCaseSpecial">
									<xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName" />
								</xsl:call-template>
								<xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
									<xsl:text> Crown Court</xsl:text>
								</xsl:if>
							<br />
							</xsl:if>
							
							<xsl:if test="$case/cs:AppealCaseDescription">
								<xsl:value-of select="$case/cs:AppealCaseDescription" />
								<br />
							</xsl:if>
							
							<xsl:if test="$case/cs:Prosecution">
								<xsl:call-template name="util:transformCaseSpecial">
									<xsl:with-param name="text" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName" />
								</xsl:call-template>
								<xsl:if test="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress">
									<xsl:text> - </xsl:text>
									<xsl:call-template name="util:transformCaseSpecial">
										<xsl:with-param name="text"  select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationAddress/apd:Line[position()=1]" />
									</xsl:call-template>								</xsl:if>
								<br />
							</xsl:if>
						</td>
						<td valign="top">
							<xsl:call-template name="util:ukdate_mon">
								<xsl:with-param name="inDate" select="$case/cs:DateOfInstigation"/>
							</xsl:call-template>
						</td>
					
					</tr>
					<tr /> <!-- help split up the output -->
				</xsl:for-each>
			</xsl:for-each>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- AdditionalNotesTemplate			-->
	<!-- **************************************** -->
	
	<xsl:template match="cs:AdditionalNotes">
		<br />
		<xsl:text>Charges: </xsl:text>
		<xsl:value-of select="."/>
	</xsl:template>
	
	<!-- **************************************** -->
	<!--solicitor Template					-->
	<!-- **************************************** -->
	
	<doc:template name="solicitor" xmlns="">
		<refpurpose>Formats the solicitor information.</refpurpose>
		<refdescription>
		  <para> Called by TrialCases and CommittalCases. </para>
		</refdescription>
		<refparameter>
			<variablelist>
				<varlistentry>
					<term>party</term>
					<listitem>
						<para>The Counsel/Solicitor/Party node belonging to a defendant. </para>
					</listitem>
				</varlistentry>
			</variablelist>
		</refparameter>
		<refreturn>
			<para>Solicitor organisation name and telephone number.</para>
		</refreturn>
	</doc:template>
	
	<xsl:template name="solicitor">
		<xsl:param name="party"  />
		<xsl:choose>
			<xsl:when test="$party/cs:Organisation or $party/cs:Person">
				<br />
				<xsl:text>Sols: </xsl:text>
				<xsl:choose>
					<xsl:when test="$party/cs:Organisation" >
						<xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
						<br />
						<xsl:text> Tel: </xsl:text>
						<xsl:value-of select="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="util:formalName">
							<xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name" />
						</xsl:call-template>
						<br />
						<xsl:text> Tel: </xsl:text>
						<xsl:value-of select="$party/cs:Person/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- publishDateTemplate				-->
	<!-- **************************************** -->
	
	<doc:template name="publishDate" xmlns="">
		<refpurpose>Display the List published date and time..</refpurpose>
		<refdescription>
		 	<para>The published date and time is displayed twice on the report, once below the list header,
		        and then again as part of the list footer</para>
		</refdescription>
	</doc:template>
		
	<xsl:template name="publishDate">
		<table width="100%">
		<tr>
			<td align="left">
			<xsl:text>Published: </xsl:text>
			<xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime" />
			<xsl:call-template name="date:format-date-time">
				<xsl:with-param name="year" select="substring($pubTime,1,4)" />
				<xsl:with-param name="month" select="substring($pubTime,6,2)" />
				<xsl:with-param name="day" select="substring($pubTime,9,2)" />
				<xsl:with-param name="hour" select="substring($pubTime,12,2)" />			
				<xsl:with-param name="minute" select="substring($pubTime,15,2)" />							
				<xsl:with-param name="format" select="'%D %B %Y at %H:%M'" />
			</xsl:call-template>
			</td>
			<td align="right">
			</td>
		</tr>
		</table>
	</xsl:template>
	
</xsl:stylesheet>
