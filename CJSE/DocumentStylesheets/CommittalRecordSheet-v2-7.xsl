<?xml version="1.0" encoding="UTF-8"?>
<!--
     +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->

<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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
            <releaseinfo role="meta">Version 2-7</releaseinfo>
            <author>
                <surname>Cooke</surname>
                <firstname>Malcolm</firstname>
            </author>
        </referenceinfo>
        <title>Committal Record Sheet Stylesheet</title>
        <para>File name : CommittalRecordSheet-v2-7.xsl</para>
        <partintro>
            <section>
                <title>Introduction</title>
                <para>This module produces the Committal Record Sheet in html format</para>
                <para>CR44 - Minor Mod to Hearing Dates</para>
            </section>
        </partintro>
    </doc:reference>
                  

<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'7" />
<xsl:variable name="stylesheet" select="'CommittalRecordSheet-v2-7.xsl'" />
<xsl:variable name="last-modified-date" select="'2009-09-06'" />
<!-- End Version Information -->

<!-- Global Variables -->
<xsl:variable name="orderDate" >
    <xsl:call-template name="util:ukdate_mon">
        <xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate" />
    </xsl:call-template>
</xsl:variable>
<!-- end Global Variables -->

          
<xsl:output method="html" indent="yes"/>

    <!-- **************************************** -->
    <!-- Root Template                  -->
    <!-- **************************************** -->

    <xsl:template match="/">
        <html>
            <head>
                <xsl:call-template name="util:cssTemplate" />
            </head>
            <body>

                <!-- +++++++++ following templates produces each section of Trial Record Sheet  +++++++++ -->
                <xsl:call-template name="header" /> 
                <xsl:call-template name="defence" />
                <xsl:call-template name="prosecution" /> 
                <xsl:call-template name="courtReporting" /> 
                <xsl:call-template name="judiciary" /> 
                <xsl:call-template name="bailOrCustody" />
                <xsl:call-template name="hearings" />
                <!-- the following line added for PR5554 -insetion of page break - NOTE not all browser/printer will recognise this as it is an optional rule -->
				<div style="page-break-before:always"><span style="visibility: hidden">-</span></div>
                <xsl:call-template name="counts" >
                    <xsl:with-param name="offenceType"  select="'committal'" />
                </xsl:call-template>
                <xsl:call-template name="otherOrders" />
                <xsl:call-template name="breachOrders" >
                    <xsl:with-param name="breachType"  select="'C'" />
                </xsl:call-template>
				<!--PR 5352 - James Powell
                <xsl:call-template name="counts" >
                    <xsl:with-param name="offenceType"  select="'breach'" />
                </xsl:call-template>                -->
                <xsl:call-template name="breachOrders" >
                    <xsl:with-param name="breachType"  select="'B'" />
                </xsl:call-template>
                <xsl:call-template name="takenIntoConsideration" />
                <xsl:call-template name="totalSentence" />        
                <xsl:call-template name="decisionOfTheCourtOfAppeal"/>
                <!-- KN 20050517 - CR27     -->
                <xsl:call-template name="util:copyrightText" />         

            </body>
        </html>
    </xsl:template>
    
    <!-- **************************************** -->
    <!-- header Template                    -->
    <!-- **************************************** -->
    
    <doc:template name="header" xmlns="">
        <refpurpose>Creates the Report Header information - including the Defendant and court details.</refpurpose>
    </doc:template>
    
    <xsl:template name="header">
        <!-- processes the Header information - defendant and court -->
        <xsl:variable name="personal" select="//cs:RecordSheetHeader/cs:Defendant/cs:PersonalDetails" />
        <h2>
            <center>
                <xsl:value-of select="'Committal Record Sheet'" />
            </center>
        </h2>
        <table WIDTH="100%">
            <tr>
                <td WIDTH="65%">
                    <strong>
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
                        <xsl:text>:</xsl:text>
                    </xsl:if>
                    </strong>
                </td>
                <td WIDTH="35%">
                    <strong><xsl:text>Defendant No: </xsl:text></strong>
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
                <td><strong><xsl:text>Address:</xsl:text></strong></td>
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
                    <!-- Added PNC No: for 1745-->
                    <xsl:text>PNC No: </xsl:text>
                    <xsl:variable name="pnc" select="//cs:RecordSheetHeader/cs:Defendant/cs:PNCnumber"/>
                    <xsl:value-of select="substring($pnc,3,10)"/>              
                </td>
            </tr>
            <tr>
                <td><xsl:if test="$personal/cs:Nationality"><strong><xsl:text>Nationality</xsl:text></strong></xsl:if></td>
                <td>
                    <!-- Added ASN No: for 1745-->
                    <xsl:text>ASN No: </xsl:text>
                    <xsl:value-of select="//cs:RecordSheetHeader/cs:Defendant/cs:ASNs/cs:ASN" />                
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
		<hr/>
        <!-- now give out the court information -->
        <strong>
        <xsl:text>Before the </xsl:text>
        <xsl:value-of select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseType"/>
        <xsl:text> at </xsl:text>
        <xsl:variable name="courtHouseName" select="//cs:RecordSheetHeader/cs:CourtHouse/cs:CourtHouseName" />
        <xsl:call-template name="str:capitalise">
            <xsl:with-param name="text" select="$courtHouseName"/>
        </xsl:call-template>
        <br />
        </strong>
        <xsl:choose>
            <xsl:when test="//cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:OriginalCaseNumber">
				<!-- changes below edited out for PR5695 to match CREST RS  - BH 22-01-2009 -->
                <!--xsl:if test="not(//cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:OriginalCaseNumber= '')"-->
                    <!--xsl:text> Brought back to be dealt with, original number  </xsl:text-->
                    <!--xsl:value-of select="//cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:OriginalCaseNumber"/-->
                <!--/xsl:if-->
                <!--xsl:if test="//cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:OriginalCaseNumber= ''-->
                    <xsl:text>Committal for sentence/to be dealt with from </xsl:text>
                    <xsl:variable name="magistratesCourtHouseName" select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName" />
                    <xsl:call-template name="str:capitalise">
                        <xsl:with-param name="text" select="$magistratesCourtHouseName"/>
                    </xsl:call-template>
                    <xsl:text> on </xsl:text>
                    <xsl:call-template name="util:ukdate_mon">
                        <xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
                    </xsl:call-template>
                <!--/xsl:if-->                   
            </xsl:when>
            <xsl:otherwise>
                    <xsl:text>Committal for sentence/to be dealt with from </xsl:text>
                    <xsl:variable name="magistratesCourtHouseName" select="//cs:RecordSheetHeader/cs:MagistratesCourt/cs:CourtHouseName" />
                    <xsl:call-template name="str:capitalise">
                        <xsl:with-param name="text" select="$magistratesCourtHouseName"/>
                    </xsl:call-template>
                    <xsl:text> on </xsl:text>
                    <xsl:call-template name="util:ukdate_mon">
                        <xsl:with-param name="inDate" select="//cs:RecordSheetHeader/cs:DateOfInstigation"/>
                    </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
        <br />
        <xsl:if test="//cs:RecordSheetHeader/cs:TransferInCourt">
            <xsl:text>Transferred from </xsl:text>
            <xsl:variable name="transferCourtHouseName" select="//cs:RecordSheetHeader/cs:TransferInCourt/cs:CourtHouseName" />
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
            <br />
        </xsl:if>
        <xsl:if test="//cs:BenchWarrantDate">
            <xsl:text>Bench Warrant Executed on </xsl:text>
            <xsl:call-template name="util:ukdate_mon">
                <xsl:with-param name="inDate" select="//cs:BenchWarrantDate"/>
            </xsl:call-template><br />      
        </xsl:if>
    <hr />   
    
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
    <xsl:key name="hearings-by-date" match="cs:Hearing" use="cs:HearingDate" />
    
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
            <para>Note: Version 2.1 of the schemas is currently planned to use Representative Structure, which will cover both Advocates and Solicitors.
                 Therefore this code will need to change with that release of the schemas.
            </para>
          </listitem>
          </itemizedlist>
        </refdescription>
    </doc:template>    
    <xsl:template name="defence" >
        <!-- this template outputs the defence team -->
        <xsl:variable name="defence" select="//cs:CommittalRecordSheet/cs:DefenceAdvocates/cs:DefenceAdvocate"/>
        <h4>
            <xsl:text>Defence</xsl:text>
        </h4>    
        <!-- First do the advocates -->
        <xsl:if test="$defence/cs:Advocate" >
            <strong>
                <xsl:text>Advocate</xsl:text>
            </strong>
            <br />
            <table width="100%" >
                <xsl:for-each select="$defence/cs:Advocate">
                <xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
                <xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
                <tr>
                    <td width="40%">
<!--                         <xsl:variable name="cnrnFormalName">
                           <xsl:call-template name="util:CNRNformalName">
                             <xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>   
                           </xsl:call-template>   
                         </xsl:variable>
                         <xsl:call-template name="str:capitalise">
                           <xsl:with-param name="text" select="$cnrnFormalName"/>
                         </xsl:call-template> -->
                         <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
                 </td>
                    <td width="10%">
                        <xsl:call-template name="representativeDescriptionForAdvocate">
                            <xsl:with-param name="qc" select="../@QC"/>
                            <xsl:with-param name="advocateType" select="../@AdvocateType"/>
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
                        <xsl:call-template name="trialDateRangeOnwardsAdvocate">
                            <xsl:with-param name="startDate" select ="./cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="./cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="6%">
						<xsl:if test="../@RepresentationHearingType">
							<!--xsl:text>(</xsl:text-->
							<xsl:value-of select="../@RepresentationHearingType"/>
							<!--xsl:text>)</xsl:text-->
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
                <tr />
            </table>
        </xsl:if>
        <!-- and then the solicitors of the advocates -->
        <xsl:if test="$defence/cs:Solicitor/cs:Party" >
            <table width="100%" >
                <xsl:for-each select="$defence/cs:Solicitor/cs:Party">
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
                    <td width="10%">
                         <xsl:if test="not(./cs:Person/cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName= 'In Person')"><xsl:text>(Solicitor)</xsl:text></xsl:if>
                    </td>
                    <td width="15%">
                        <xsl:call-template name="representativeDescriptionForSolicitor"><xsl:with-param name="representativeType" select="../@RepresentationType"/></xsl:call-template>
                    </td>                    
                    <td width="25%">
                        <xsl:call-template name="trialDateRangeOnwards">
                            <xsl:with-param name="startDate" select ="../cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="../cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="6%"><xsl:value-of select="../../@RepresentationHearingType"/></td>
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
                <tr />
            </table>                
        </xsl:if>
        <!-- and then the solicitors -->
        <xsl:variable name="defenceSolicitors" select="//cs:CommittalRecordSheet/cs:DefenceSolicitors"/>
        <xsl:if test="$defenceSolicitors/cs:DefenceSolicitor" >
            <strong>
                <xsl:text>Solicitor</xsl:text>
            </strong> 
            <table width="100%" >
                <xsl:for-each select="$defenceSolicitors/cs:DefenceSolicitor/cs:Party">
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
                    <td width="10%"><!-- Nothing --></td>
                    <td width="15%">
                        <xsl:call-template name="representativeDescriptionForSolicitor"><xsl:with-param name="representativeType" select="../@RepresentationType"/></xsl:call-template>
                    </td>
                    <td width="25%">
                        <xsl:call-template name="trialDateRangeOnwards">
                            <xsl:with-param name="startDate" select ="../cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="../cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="10%"><!-- Nothing --></td>
                </tr>
                </xsl:for-each>
                <tr />
            </table>                
        </xsl:if>
        <hr />
    </xsl:template>    
    <xsl:template name="representativeDescriptionForAdvocate">
        <!-- Outputs QC or Sol or nothing depending on the parameters passed in -->
        <xsl:param name="qc"/>
        <xsl:param name="advocateType"/>
        <xsl:variable name="result">
        </xsl:variable>
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
    
    
    
    <!-- **************************************** -->
    <!-- prosecutionTemplate                -->
    <!-- **************************************** -->
        
    <doc:template name="prosecution" xmlns="">
        <refpurpose>Outputs the details of the Prosecuting team</refpurpose>
        <refdescription>
          <para>Shows who the prosecuting authority is,
                then iterates through the Advocates on the prosecuting team.</para>
          <para>Uses the template util:formalName to format the individuals name for display.</para>                
          <para>
          <emphasis role="bold">Special Rules</emphasis>
          </para>
          <itemizedlist>
          <listitem>
            <para>Note: This section will need to be changed when the version of the schemas,
                  using Representative Structure, comes into force (v2.1 ?)
            </para>
          </listitem>
          </itemizedlist>
        </refdescription>
    </doc:template>
    
    <xsl:template name="prosecution" >
    <!-- this template outputs the prosecuting team -->
        <xsl:variable name="prosecution" select="//cs:CommittalRecordSheet/cs:Prosecution"/>
        <h4>
            <xsl:text>Prosecution </xsl:text>
            <xsl:value-of select="$prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
        </h4>       
        <xsl:if test="$prosecution/cs:ProsecutionAdvocates/cs:ProsecutionAdvocate/cs:Advocate" >
            <strong>
                <xsl:text>Advocate</xsl:text>
            </strong>
            <br />
            <table width="100%" >
                <xsl:for-each select="//cs:ProsecutionAdvocate/cs:Advocate">
                <xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
                <xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
                <tr>
                    <td width="50%">
<!--                         <xsl:variable name="cnrnFormalName">
                           <xsl:call-template name="util:CNRNformalName">
                               <xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>   
                           </xsl:call-template>   
                         </xsl:variable>
                         <xsl:call-template name="str:capitalise">
                           <xsl:with-param name="text" select="$cnrnFormalName"/>
                         </xsl:call-template>-->
                         <xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
                    </td>
                    <td width="15%">
                        <xsl:text>(</xsl:text>
                        <xsl:if test="../@QC='yes'"><xsl:text>QC</xsl:text></xsl:if>
                        <xsl:if test="../@QC='no'"><xsl:text>Counsel</xsl:text></xsl:if>
                        <xsl:text>)</xsl:text>
                    </td>
                    <td width="25%">
                        <xsl:call-template name="trialDateRangeOnwardsAdvocate">
                            <xsl:with-param name="startDate" select ="./cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="./cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="10%">
                        <!-- nothing to go here i believe for prosecuting advocates -->
                    </td>
                </tr>
                </xsl:for-each>
                <tr />
            </table>
        </xsl:if>
        <xsl:if test="$prosecution/cs:ProsecutionAdvocates/cs:ProsecutionAdvocate/cs:Solicitor/cs:Party">
            <table width="90%" >
                <xsl:for-each select="$prosecution/cs:ProsecutionAdvocates/cs:ProsecutionAdvocate/cs:Solicitor/cs:Party">
                <xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
                <xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
                <tr>
                    <td width="50%">
                        <xsl:variable name="cnrnFormalName">
                            <xsl:call-template name="util:CNRNformalName">
                                <xsl:with-param name="name" select="./cs:Person/cs:PersonalDetails/cs:Name"/>   
                            </xsl:call-template>   
                        </xsl:variable>
                        <xsl:call-template name="str:capitalise">
                            <xsl:with-param name="text" select="$cnrnFormalName"/>
                        </xsl:call-template>
                    </td>
                    <td width="15%"><xsl:text>(Solicitor)</xsl:text></td>
                    <td width="25%">
                        <xsl:call-template name="trialDateRangeOnwards">
                            <xsl:with-param name="startDate" select ="../cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="../cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="10%">
                        <!-- nothing to go here i believe for prosecuting solicitors -->
                    </td>
                </tr>
                </xsl:for-each>
                <tr />
            </table>                
        </xsl:if>
        <xsl:if test="$prosecution/cs:ProsecutionSolicitors/cs:ProsecutionSolicitor/cs:Party" >
            <strong>
                <xsl:text>Solicitor</xsl:text>
            </strong>    
            <table width="90%" >
                <xsl:for-each select="$prosecution/cs:ProsecutionSolicitors/cs:ProsecutionSolicitor/cs:Party">
                <xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
                <xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
                <tr>
                    <td width="50%">
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
                    <td width="15%">
                        <!-- Nothing -->
                    </td>                    
                    <td width="25%">
                        <xsl:call-template name="trialDateRangeOnwards">
                            <xsl:with-param name="startDate" select ="../cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="../cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="10%"><!-- Nothing --></td>
                </tr>
                </xsl:for-each>
                <tr />
            </table>                
        </xsl:if> 
        <hr />
    </xsl:template>
    
    <!-- **************************************** -->
    <!-- courtReportingTemplate         -->
    <!-- **************************************** -->
    
    <doc:template name="courtReporting" xmlns="">
        <refpurpose>Outputs the details of the court reporting firms and their associated partners.</refpurpose>
        <refdescription>
          <para>Iterates through the court reporting firms showing for each one the name of the firm,
                followed by the names of court reporters associated with that firm.</para>
          <para>Uses the template util:formalName to format the individuals name for display.</para>
        </refdescription>
    </doc:template>
    
    <xsl:template name="courtReporting" > 
        <!-- this template outputs the court reporting firms and their associated reporters -->        
        <xsl:for-each select="//cs:CourtReportingFirm">
            <xsl:if test="position()=1">
                <h4>
                    <xsl:text>Court Reporting Firms</xsl:text>
                </h4>
            </xsl:if>
            <xsl:text>(</xsl:text><xsl:value-of select="./cs:Firm/cs:OrganisationCode" /><xsl:text>) </xsl:text>
            <xsl:value-of select="./cs:Firm/cs:OrganisationName" />
            <br/>
        </xsl:for-each>
        <xsl:for-each select="//cs:CourtReportingFirm">
            <xsl:if test="./cs:Reporter">
                <xsl:if test="position()=1">
                    <h4>
                        <xsl:text>Court Reporters</xsl:text>
                    </h4>
                </xsl:if>
                <table width="100%" >
                    <xsl:for-each select="./cs:Reporter">
                      <xsl:sort select="./cs:StartDate" data-type="text" order="ascending"/>
                      <xsl:sort select="./cs:EndDate" data-type="text" order="ascending"/>
                        <tr>
                        <td width="50%">
                            <xsl:text>(</xsl:text><xsl:value-of select="../cs:Firm/cs:OrganisationCode" /><xsl:text>) </xsl:text>
                            <xsl:variable name="cnrnFormalName">
                              <xsl:call-template name="util:CNRNformalName">
                               <xsl:with-param name="name" select="./cs:PersonalDetails/cs:Name"/>
                              </xsl:call-template>
                            </xsl:variable>
                            <xsl:call-template name="str:capitalise">
                              <xsl:with-param name="text" select="$cnrnFormalName"/>
                            </xsl:call-template> 
                        </td>
                        <td width="15%">
                            <xsl:text>(</xsl:text>
                            <xsl:value-of select="./cs:OperatorType"/>
                            <xsl:text>)</xsl:text>
                        </td>
                        <td width="25%">
                            <xsl:call-template name="trialDateRange">
                                <xsl:with-param name="startDate" select ="./cs:StartDate"/>
                                <xsl:with-param name="endDate" select ="./cs:EndDate"/>
                            </xsl:call-template>
                        </td>
                        <td width="10%"></td>
                    </tr>
                    </xsl:for-each>
                    <tr />
                </table>
            </xsl:if>
        </xsl:for-each>        
        <xsl:if test="//cs:CourtReportingFirm" ><hr /></xsl:if>
    </xsl:template>
    
    <!-- **************************************** -->
    <!-- judiciary Template             -->
    <!-- **************************************** -->
    
    <doc:template name="judiciary" xmlns="">
        <refpurpose>Outputs the details of the Judges and Justices.</refpurpose>
        <refdescription>
          <para>Iterates through the Judges and any Justices there might be</para>
          <para>Uses the template util:judiciaryName to format the individuals name for display.</para>
        </refdescription>
    </doc:template>     
            
    <xsl:template name="judiciary" >
    <!-- this template outputs the Judges and Justices -->
        <xsl:if test="//cs:Judiciary/cs:Judge">
            <h4><xsl:text>Judge</xsl:text></h4>
            <table width="100%" >
                <xsl:for-each select="//cs:Judiciary/cs:Judge">
                  <xsl:sort select="../cs:StartDate" data-type="text" order="ascending"/>
                  <xsl:sort select="../cs:EndDate" data-type="text" order="ascending"/>
                    <tr>
                    <td width="50%">
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
                    <td width="15%"></td>
                    <td width="25%">
                        <xsl:call-template name="trialDateRange">
                            <xsl:with-param name="startDate" select ="../cs:StartDate"/>
                            <xsl:with-param name="endDate" select ="../cs:EndDate"/>
                        </xsl:call-template>
                    </td>
                    <td width="10%"></td>
                </tr>
                </xsl:for-each>
                <tr />
            </table>
        </xsl:if>
        <xsl:if test="//cs:Judiciary/cs:Justice or //cs:Judiciary/cs:Judge"><hr /></xsl:if>
    </xsl:template>
    
    <!-- **************************************** -->
    <!-- bailOrCustody Template         -->
    <!-- **************************************** -->   
    
    <doc:template name="bailOrCustody" xmlns="">
        <refpurpose>Outputs the details of the bail or custody status.</refpurpose>
        <refdescription>
          <para>Shows the bail or custody status at the various stages of the process i.e on committal,
                at start of hearing and put back for sentence.</para>
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
    
    <xsl:template name="bailOrCustody" >
    <h4>
        <xsl:text>Bail/Custody Status</xsl:text>
    </h4>
    <table width="90%" >
        <tr>
            <td width="50%">
                <xsl:choose>
                     <xsl:when test="//cs:BenchWarrantDate">
                        <xsl:text>After Bench Warrant executed: </xsl:text>
                        <xsl:value-of  select="//cs:BailStatusAfterBenchWarrantExecuted" />
                     </xsl:when>
                     <xsl:when test="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:MethodOfInstigation='Sending'">
                        <xsl:text>On sending: </xsl:text>
                <xsl:call-template name="str:to-lower">
                    <xsl:with-param name="text" select="//cs:BailStatusOnCommittal"/>
                </xsl:call-template>
                     </xsl:when>
                     <xsl:when test="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:MethodOfInstigation='Committal'">
                        <xsl:text>On committal: </xsl:text>
                <xsl:call-template name="str:to-lower">
                    <xsl:with-param name="text" select="//cs:BailStatusOnCommittal"/>
                </xsl:call-template>
                     </xsl:when>
                </xsl:choose>
            </td>
            <td width="15%"></td>
            <td width="35%"></td>
        </tr>
        <xsl:if test="//cs:DefendantBailApplications/cs:DefendantBailApplication">
            <xsl:for-each select="//cs:DefendantBailApplications/cs:DefendantBailApplication">
              <xsl:sort select="./cs:ApplicationDate" data-type="text" order="ascending"/>
                <tr>
                    <td width="50%">
                       <xsl:text>Application for bail on </xsl:text>
                       <xsl:value-of select="./cs:ApplicationDate" /> 
                       <xsl:text>: </xsl:text>
                       <xsl:value-of select="./cs:Result" />         
                    </td>
                    <td width="15%"></td>
                    <td width="35%"></td>
                </tr>
            </xsl:for-each>                   
        </xsl:if>
        <tr>
            <td width="50%">
                <xsl:if test="/cs:CommittalRecordSheet/cs:Hearings/cs:Hearing/@PreliminaryHearing='yes'">
                    <xsl:text>Application for bail on </xsl:text>
                    <xsl:value-of  select="//cs:CommittalRecordSheet/cs:Hearings/cs:Hearing/cs:HearingBailChanges/cs:BailChangeDate" />
                    <xsl:text>: </xsl:text>
                    <xsl:value-of  select="//cs:CommittalRecordSheet/cs:Hearings/cs:Hearing/cs:HearingBailChanges/cs:HearingBailResult" /> 
                </xsl:if>
            </td>
            <td width="15%"></td>
            <td width="35%"></td>
        </tr>
        <xsl:if test="//cs:BailStatusAtStartOfHearing">
            <tr>
                <td width="50%">
                    <xsl:text>At start of hearing: </xsl:text>
                <xsl:call-template name="str:to-lower">
                    <xsl:with-param name="text" select="//cs:BailStatusAtStartOfHearing" />
                </xsl:call-template>
                </td>
                <td width="15%"></td>
                <td width="35%"></td>
            </tr>
        </xsl:if>
        <xsl:if test="//cs:BailStatusPutBackforSentence">
            <tr>
                <td width="50%">
                    <xsl:text>Put back for sentence: </xsl:text>
                    <xsl:value-of  select="//cs:BailStatusPutBackforSentence" />
                </td>
                <td width="15%"></td>
                <td width="35%"></td>
            </tr>
        </xsl:if>
       
        <tr />
    </table>
    </xsl:template>
    <!-- **************************************** -->
    <!-- hearings Template              -->
    <!-- **************************************** -->
    
    <doc:template name="hearings" xmlns="">
        <refpurpose>Outputs the hearing details along with key dates.</refpurpose>
        <refdescription>
          <para>Iterates through the hearing details showing the start date and end date(if available) for each.
                If the sentenced postponed date and/or the date of the sentence/order made dates are available then these
                are also shown.</para>
        </refdescription>
    </doc:template>
    
    <xsl:template name="hearings" >
        <!-- this template outputs the hearing information along with key dates --> 
        <h4>
            <!-- xsl:text>Hearings and other important dates</xsl:text -->
        </h4>
        <xsl:text> Appeared for sentence/to be dealt with </xsl:text>
        <xsl:for-each select="//cs:Hearings/cs:Hearing">
          <xsl:sort select="./cs:HearingDate" data-type="text" order="ascending"/>
          <xsl:sort select="./cs:HearingEndDate" data-type="text" order="ascending"/>
          <xsl:if test="./cs:HearingDate">
            <xsl:if test="not(position()=1)">
                <xsl:text> and </xsl:text>
            </xsl:if>
            <xsl:text> on </xsl:text>
            <xsl:call-template name="trialDateRange">
                <xsl:with-param name="startDate" select ="./cs:HearingDate"/>
                <xsl:with-param name="endDate" select ="./cs:HearingEndDate"/>
            </xsl:call-template>
          </xsl:if>
          <!-- Assume same day hearing if the user enters an end date but not a start date, which is why the end date is in the start date for trialDateRange -->
          <xsl:if test="not(./cs:HearingDate)"> 
           <xsl:if test="./cs:HearingEndDate">
            <xsl:if test="not(position()=1)">
                <xsl:text> and </xsl:text>
            </xsl:if>
            <xsl:text> on </xsl:text>
            <xsl:call-template name="trialDateRange">
                <xsl:with-param name="startDate" select ="./cs:HearingEndDate"/>
                <xsl:with-param name="endDate" select ="./cs:HearingEndDate"/>
            </xsl:call-template>
           </xsl:if>
          </xsl:if>
        </xsl:for-each> 
        <br/>
     <xsl:if test="cs:SentencePostponed">              
            <xsl:text>Sentence postponed until </xsl:text>
            <xsl:choose>
                <xsl:when test="/cs:CommittalRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate">
                    <xsl:call-template name="util:ukdate_mon">
                        <xsl:with-param name="inDate" select="//cs:CommittalRecordSheet/cs:SentencePostponed/cs:SentencePostponedToDate"/>
                    </xsl:call-template>         
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text> a date to be fixed</xsl:text>                                  
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="/cs:CommittalRecordSheet/cs:SentencePostponed/cs:SentenceReports='yes'">        
               <xsl:text> for reports.</xsl:text>
            </xsl:if>        
        </xsl:if>  
        <br/>
        <xsl:if test="/cs:CommittalRecordSheet/cs:SentenceDeferredToDate">
            <xsl:text>Sentence deferred until </xsl:text>
            <xsl:call-template name="util:ukdate_mon">
                <xsl:with-param name="inDate" select="/cs:CommittalRecordSheet/cs:SentenceDeferredToDate"/>
            </xsl:call-template>
            <br/>
        </xsl:if>
        <xsl:if test="/cs:CommittalRecordSheet/cs:SentenceOrOrderDate">
            <xsl:text>Sentence/Order made on </xsl:text>
            <xsl:call-template name="util:ukdate_mon">
                <xsl:with-param name="inDate" select="/cs:CommittalRecordSheet/cs:SentenceOrOrderDate"/>
            </xsl:call-template>
            <br/>
        </xsl:if>
        <xsl:if test="//cs:CommittalRecordSheet/cs:LinkedCases/cs:CaseNumber">
          <xsl:text>See also No. </xsl:text>
          <xsl:for-each select="//cs:CommittalRecordSheet/cs:LinkedCases/cs:CaseNumber">
            <xsl:if test="position() &lt; 17">
                <xsl:value-of select="." />
                <xsl:if test="(position()!=last()-1 and position()!=last())">
                    <xsl:if test="position() &lt; 16">
                        <xsl:text>, </xsl:text>
                    </xsl:if>    
                </xsl:if>
                <xsl:if test="position()=last()-1">
                    <xsl:text> and </xsl:text>
                </xsl:if>
            </xsl:if>
            <xsl:if test="position()=17">
                <xsl:text> and others ... </xsl:text>
            </xsl:if>
          </xsl:for-each>
        </xsl:if>
        <hr />
    </xsl:template>
    
    <!-- **************************************** -->
    <!-- counts Template                    -->
    <!-- **************************************** -->
        
    <doc:template name="counts" xmlns="">
        <refpurpose>Outputs the information relating to charges, pleas etc.</refpurpose>
        <refdescription>
          <para>Iterates through the Offences showing the description and the 
                decision of the Crown Court.</para>
        </refdescription>
    </doc:template>
    
    <xsl:template name="counts">
      <xsl:param name="offenceType" />
        
        <!-- Extra space  -->
        <h4>
            <xsl:text></xsl:text>
        </h4>    
    
        <table width="100%">
        
          <xsl:for-each select="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = $offenceType]">

            <xsl:if test="position()=1">
              
              <!-- Setup columns  -->   
              <tr>
                <td width="10%" valign="top"><xsl:text></xsl:text></td>
                <td width="50%" valign="top"><xsl:text></xsl:text></td>
                <td width="40%" valign="top"><xsl:text></xsl:text></td>
              </tr>
              
              <!-- outputs header  -->
              <tr>
                <td colspan="3">
                     <xsl:if test="$offenceType='committal'">
                         <h4>
                            <xsl:text>Committal Charges</xsl:text>
                         </h4>
                     </xsl:if>    
                     <xsl:if test="$offenceType='breach'">
                         <h4>
                             <xsl:text>Breach Charges</xsl:text>
                         </h4>
                     </xsl:if>
                 </td>
              </tr>
              
              <!-- outputs column headers  -->
              <tr>
                <td colspan="1"><strong><xsl:text>No</xsl:text></strong></td>
                <td colspan="1">
                    <strong>
                    <xsl:call-template name="offenceHeader" >
                            <xsl:with-param name="offenceType"  select="$offenceType" />
                    </xsl:call-template>
                    </strong>
                </td>
                <td colspan="1"><strong><xsl:text>Seq No</xsl:text></strong></td>
              </tr>  
            
            </xsl:if>
            
              <tr>
                  <td colspan="1" valign="top">
                      <xsl:value-of select="./@IndictmentCountNumber"/>
                  </td>
                  <td colspan="1" valign="top">
                      <xsl:value-of select="./@CJSoffenceCode"/>
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="./cs:OffenceStatement"/>
                  </td>
                  <td colspan="1">
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
              </tr>
              <tr>
                  <td colspan="1">  
                  </td>
                  <td colspan="2">
                      <table width="100%">
                          <tr>
                              <td width="20%" valign="top"></td>
                              <td width="80%"></td>
                          </tr>  
                          <tr>
                              <td colspan="1">
                                  <br />
                                      <strong><xsl:text>Offence Location Address </xsl:text></strong>
                                  <br />
                              </td>
                          </tr>
                          <tr>
                              <td colspan="2">
								<xsl:call-template name="buildOffenceLocationAddress">
									<xsl:with-param name="offenceLocation" select="./cs:OffenceLocation"/>
								</xsl:call-template>                              
                              
<!--       REPLACED WITH TRIALRECORDSHEET VERSION BP 24092008
								   <xsl:for-each select="./cs:OffenceLocation/apd:Line">
                                      <xsl:value-of select="."/>
                                      <xsl:if test="not(position()=last())">
										  <xsl:text>, </xsl:text>
                                      </xsl:if>
                                  </xsl:for-each>
                                  <xsl:if test="./cs:OffenceLocation/apd:PostCode">
                                      <xsl:text>, </xsl:text>
                                      <xsl:value-of select="./cs:OffenceLocation/apd:PostCode"/>
                                  </xsl:if>-->
                                  <br />
                                  <br />
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
                              <td colspan="1">
                                  <xsl:if test="starts-with(./cs:CommittedOnBail, 'y')">
									  <xsl:text>Offence Committed On Bail?</xsl:text>
                                  </xsl:if>
                              </td>
                              <td colspan="1">
                                  <xsl:choose>
                                      <xsl:when test="starts-with(./cs:CommittedOnBail, 'y')">
                                          <xsl:text>yes</xsl:text>
                                      </xsl:when>
                                  </xsl:choose>
                              </td>
                          </tr>
                          <tr> 
                              <td colspan="2">
                                  <br />
                                  <strong>
                                  <xsl:if test="./cs:Disposals">
                                      <xsl:for-each select="./cs:Disposals/cs:Disposal">
                                          <xsl:choose>
                                              <xsl:when test="starts-with(.,'Imprisonment')">
                                                  <xsl:call-template name="str:to-lower" >
                                                      <xsl:with-param name="text" select="." />
                                                  </xsl:call-template>
                                              </xsl:when>
                                              <xsl:otherwise>
                                                  <xsl:value-of select="." />
                                              </xsl:otherwise>
                                          </xsl:choose>
                                          <br />
                                      </xsl:for-each>
                                      <br />
                                  </xsl:if>
                                  </strong>                               
                              </td>
                          </tr>
                      </table>
                  </td>                    
              </tr>
              <tr>
                  <td colspan="3">
                      <xsl:if test="position()=last()">
                          <hr/>
                      </xsl:if>
                  </td>
              </tr>
            </xsl:for-each>
        </table>
    </xsl:template>    

    <xsl:template name="offenceHeader">
        <!-- Outputs the appropriate offence column header depending on parameter -->
        <xsl:param name="offenceType"/>
        <xsl:variable name="result">
        </xsl:variable>
            <xsl:choose>
                <xsl:when test="$offenceType='breach'">
                    <xsl:text>Original Offences and New Sentence/Order</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Offence and Sentence/Order</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        <xsl:copy-of select="$result"/>
    </xsl:template>
    
    <!-- **************************************** -->
    <!-- otherOrders Template           -->
    <!-- **************************************** -->
    
    <doc:template name="otherOrders" xmlns="">
        <refpurpose>Outputs info for any other orders.</refpurpose>
        <refdescription>
          <para>Iterates through the TotalSentences/OtherOrders showing the orders found.</para>
        </refdescription>
    </doc:template>
    
    <xsl:template name="otherOrders">
        <!-- outputs information about any other orders  -->
        <xsl:if test="/cs:CommittalRecordSheet/cs:TotalSentence/cs:OtherOrders">
            <h4>
                <xsl:text>Other Orders</xsl:text>
            </h4>
            <strong>
            <xsl:for-each select="/cs:CommittalRecordSheet/cs:TotalSentence/cs:OtherOrders/cs:Order">
                <xsl:value-of select="." />
                <br />  
            </xsl:for-each>
            </strong>
            <hr />              
        </xsl:if>
		<xsl:if test="//cs:RecordSheetHeader/cs:Defendant/cs:DeportationReason != ''">
			<xsl:call-template name="util:DeportationText"/>
        </xsl:if>
    </xsl:template> 
    
    <!-- **************************************** -->
    <!-- breachOrders Template          -->
    <!-- **************************************** -->
    
    <doc:template name="breachOrders" xmlns="">
        <refpurpose>Outputs info for any breached orders.</refpurpose>
        <refdescription>
          <para>Iterates through the OffencesOrOrdersBreached showing the details of the original order 
                ie what it was an where it originated, along with the new sentence or order which has been applied.</para>
          <para> util:decodeDuration is used to decode the sentence term.</para>
          <para>
            <emphasis role="bold">Special Rules</emphasis>
          </para>
          <itemizedlist>
          <listitem>
            <para>Current version of the schema does not allow for a new sentence or order to be directly associated with a breach. 
                  This may be fixed in a future release of the schemas, and this code will need to be amended accordingly.</para>
          </listitem>
          </itemizedlist>
        </refdescription>
    </doc:template>


    <xsl:template name="breachOrders">
      <xsl:param name="breachType"/>
        <!-- outputs information about any breach orders  -->

      <xsl:if test="/cs:CommittalRecordSheet/cs:Breaches/cs:Breach">
	  <xsl:variable name="alternateType">
		  <xsl:choose>
			  <xsl:when test="$breachType='B'">
				  <xsl:value-of select="'F'"/>
			  </xsl:when>
			  <xsl:otherwise>
				  <xsl:value-of select="$breachType"/>
			  </xsl:otherwise>
		  </xsl:choose>
	   </xsl:variable>
        <!-- Extra space  -->
        <h4>
            <xsl:text></xsl:text>
        </h4>                         
        
        <table width="100%">          
          <xsl:for-each select="/cs:CommittalRecordSheet/cs:Breaches/cs:Breach[./@BreachType = $breachType or ./@BreachType = $alternateType]">
			<xsl:variable name="breachNo" select="./@CRESTbreachNumber"/>			
			<xsl:variable name="thisBreachType" select="./@BreachType"/>
              <xsl:if test="position()=1">
              
                <!-- Setup columns  -->   
                <tr>
                    <td width="10%" valign="top"><xsl:text></xsl:text></td>
                    <td width="30%" valign="top"><xsl:text></xsl:text></td>
                    <td width="15%" valign="top"><xsl:text></xsl:text></td>
                    <td width="45%" valign="top"><xsl:text></xsl:text></td>
                </tr>

                <!-- outputs header  -->
                <tr>
                    <td colspan="4"> 
                        <xsl:if test="$breachType='B'">
                            <h4> 
                                <xsl:text>Breaches of Previous Orders</xsl:text>
                            </h4>
                        </xsl:if>
                        <xsl:if test="$breachType='C'">
                            <h4> 
                                <!--<xsl:text>Breaches of Committals</xsl:text>-->
                            </h4>
                        </xsl:if>
                    </td>
                </tr>
              
                <!-- outputs column header  -->
                <tr>
                    <td colspan="1">
                        <strong>
                            <xsl:text>No</xsl:text>
                        </strong>
                    </td>
                    <td colspan="1">
                        <strong>
                            <xsl:text>Made By</xsl:text>
                        </strong>
                    </td>
                    <td colspan="1">
                        <strong>
                            <xsl:text>Date</xsl:text>
                        </strong>
                    </td>
                    <td colspan="1">
                        <strong>
                            <xsl:text>Offence/Order Breached and New Sentence/Order</xsl:text>
                        </strong>
                    </td>
                </tr>       
              </xsl:if>
              
              <!-- outputs breaches detail  -->
              <tr>
                  <td colspan="4">
                      <xsl:call-template name="breachDetail" ></xsl:call-template>
                  </td>
              </tr>
			<!--Beginning of fix for 5352 - James Powell-->
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
								<tr>
									<td width="80%" colspan="1" valign="top">
										<xsl:text>Original Offences and new sentence/order:</xsl:text>
									</td>
									<td width="20%" colspan="1" valign="top" align="right">
										<xsl:text>Seq</xsl:text>
									</td>
								</tr>
								<xsl:for-each select="/cs:CommittalRecordSheet/cs:RecordSheetHeader/cs:Defendant/cs:Charges/cs:Charge[./@ChargeType = 'breach' or ./@ChargeType = 'BAO']">
									<xsl:if test="./@IndictmentNumber = $breachNo">									

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
											<xsl:if test="$thisBreachType != 'F'">
													<xsl:choose>
														<xsl:when test="string-length(./cs:CRN)=3">
															<xsl:value-of select="./cs:CRN"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="substring(./cs:CRN,21,3)"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
										</td>							 
									</tr>
              <tr>
                  <td colspan="2">
                      <table width="100%">
                          <tr>
                              <td width="60%" valign="top"></td>
                              <td width="40%"></td>
                          </tr>  
                          <tr>
                              <td colspan="2">
                                  <br />
                                      <xsl:text>Offence Location Address </xsl:text>
                                  <br />
                              </td>
                          </tr>
                          <tr>
							<td colspan="2" height="10"/>
						</tr>
						<tr>
							<td colspan="2">
								<xsl:for-each select="./cs:OffenceLocation/apd:Line[not ( .='-') and not (. = ' ')]">
									<xsl:value-of select="."/>
									<xsl:if test="not(position()=last())">
									<xsl:if test="string-length() &gt; 0">
										<xsl:text>, </xsl:text>
									</xsl:if>
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
                              <td colspan="1">
                                  <xsl:if test="starts-with(./cs:CommittedOnBail, 'y')">
									  <xsl:text>Offence Committed On Bail?</xsl:text>
                                  </xsl:if>
                              </td>
                              <td colspan="1">
                                  <xsl:choose>
                                      <xsl:when test="starts-with(./cs:CommittedOnBail, 'y')">
                                          <xsl:text>yes</xsl:text>
                                      </xsl:when>
                                  </xsl:choose>
                              </td>
                          </tr>
                          <tr> 
                              <td colspan="2">
                                  <br />
                                  <strong>
                                  <xsl:if test="./cs:Disposals">
                                      <xsl:for-each select="./cs:Disposals/cs:Disposal">
                                          <xsl:choose>
                                              <xsl:when test="starts-with(.,'Imprisonment')">
                                                  <xsl:call-template name="str:to-lower" >
                                                      <xsl:with-param name="text" select="." />
                                                  </xsl:call-template>
                                              </xsl:when>
                                              <xsl:otherwise>
                                                  <xsl:value-of select="." />
                                              </xsl:otherwise>
                                          </xsl:choose>
                                          <br />
                                      </xsl:for-each>
                                      <br />
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
			</td>
			</tr>							
			<!--End of fix for PR 5352 - James Powell-->								
              <tr>
                  <td colspan="4">
                      <xsl:if test="position()=last()">
                          <hr/>
                      </xsl:if>
                  </td>
              </tr>              
          </xsl:for-each>
        </table>
      </xsl:if>  
    </xsl:template>
    
    <xsl:template name="breachDetail">
        <!-- outputs information about any breach orders  -->
        <xsl:variable name="thisBreachType" select="./@BreachType"/>
           <table width="100%">
                <tr>
                    <td width="10%" valign="top">
                        <xsl:value-of select="position()" />
                    </td>
                    <td width="30%" valign="top">                    
						<xsl:if test="$thisBreachType != 'F'">
							<xsl:if test="./cs:OriginatingCourt">
								<xsl:if test="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
									<xsl:text> The Crown Court at:</xsl:text>                                
								</xsl:if>
								<xsl:value-of select="./cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseName" />
							 </xsl:if>
                         </xsl:if>
                    </td>
                    <td width="15%" valign="top">
                        <xsl:if test="./cs:OriginatingCourt/cs:Date">
                            <xsl:call-template name="util:ukdate_mon" >
                                <xsl:with-param name="inDate" select="./cs:OriginatingCourt/cs:Date" />
                            </xsl:call-template>
                        </xsl:if>
                    </td> 
                    <td width="45%" valign="top">
                        <xsl:value-of select="./cs:OriginatingCourt/cs:OriginalOrderType"/>
                        <xsl:text>, total sentence </xsl:text>
                        <xsl:value-of select="./cs:OriginalSentence" />
                        <xsl:if test="./cs:Admitted">
							<xsl:text>, put and </xsl:text>
							<xsl:call-template name="breachAdmitted" >
								<xsl:with-param name="admitted"  select="./cs:Admitted" />
							</xsl:call-template>
                        </xsl:if>
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="util:ukdate_mon">
                            <xsl:with-param name="inDate" select="./cs:DatePut"/>
                        </xsl:call-template>
                        <br />
                    </td>
                </tr>
            </table>
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
        <xsl:if test="/cs:CommittalRecordSheet/cs:NumberOfOffencesTIC > '0'">
            <table>
                <tr>
                    <td>
                        <strong><xsl:text>Offences admitted and taken into consideration: </xsl:text></strong>
                        <xsl:value-of select="/cs:CommittalRecordSheet/cs:NumberOfOffencesTIC"/>
                    </td>
                </tr>
            </table>
            <hr/>
        </xsl:if>
    </xsl:template>
    
     <!-- **************************************** -->
     <!-- totalSentence Template           -->
     <!-- **************************************** -->
     
     <doc:template name="totalSentence" xmlns="">
         <refpurpose>Outputs info for total sentence.</refpurpose>
         <refdescription>
           <para>cs:CommittalRecordSheet/cs:TotalSentence/cs:Term</para>
         </refdescription>
     </doc:template>
     
     <xsl:template name="totalSentence">
     <!-- outputs information about any other orders  -->
         <xsl:if test="/cs:CommittalRecordSheet/cs:TotalSentence/cs:Term">
             <h4>
                 <xsl:text>Total Sentence</xsl:text>
             </h4>
             <strong>
             <xsl:call-template name="util:decodeDuration">
                 <xsl:with-param name="duration" select="/cs:CommittalRecordSheet/cs:TotalSentence/cs:Term" />
             </xsl:call-template>
             <xsl:text> </xsl:text>
             <xsl:call-template name="str:to-lower">
                 <xsl:with-param name="text" select="/cs:CommittalRecordSheet/cs:TotalSentence/cs:Term/@TermType" />
             </xsl:call-template>
             </strong>
             <hr />              
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
        <xsl:if test="/cs:CommittalRecordSheet/cs:CourtOfAppealResult">
            <table>
                <tr>
                    <td>
                        <strong><xsl:text>Decision of the Court of Appeal (Criminal Division)</xsl:text></strong><br/><br/>
                        <xsl:value-of select="/cs:CommittalRecordSheet/cs:CourtOfAppealResult"/>
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
        <xsl:param name="startDate" />
        <xsl:param name="endDate"   />
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
    
    <xsl:template name="trialDateRangeOnwards">
    <!-- Outputs the start date and the following : -->
    <!-- if end date same as start date then nothing -->
    <!-- if end date different to start date then the end date -->
    <!-- Dates are reformatted to dd-mon-yyyy format on output -->
    <!-- Params:                                               -->
    <!-- 1. Start Date  -->
    <!-- 2 End Date - if empty will print out onwards instead     -->
        <xsl:param name="startDate" />
        <xsl:param name="endDate"   />
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
                <xsl:otherwise>
                    <xsl:if test="$startDate">
                        <xsl:text> onwards</xsl:text>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:copy-of select="$result"/>
    </xsl:template>  
        <xsl:template name="trialDateRangeOnwardsAdvocate">
    <!-- Outputs the start date and the following : -->
    <!-- if end date same as start date then nothing -->
    <!-- if end date different to start date then the end date -->
    <!-- Dates are reformatted to dd-mon-yyyy format on output -->
    <!-- Params:                                               -->
    <!-- 1. Start Date  -->
    <!-- 2 End Date - if empty will print out onwards instead     -->
        <xsl:param name="startDate" />
        <xsl:param name="endDate"   />
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
                <xsl:otherwise>
                    <xsl:if test="$startDate">
                        <!--xsl:text> onwards</xsl:text-->
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:copy-of select="$result"/>
    </xsl:template>
    <xsl:template name="buildOffenceLocationAddress">
		<!-- returns address and post code all on one line -->
		<xsl:param name="offenceLocation"/>
		<xsl:variable name="addr">
			<xsl:for-each select="$offenceLocation/apd:Line[not ( .='-') and not (. = ' ')]">
				<xsl:value-of select="."/>
				<xsl:if test="not (position() = last())">
					<xsl:if test="string-length() &gt; 0">
						<xsl:text>, </xsl:text>
					</xsl:if>
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
