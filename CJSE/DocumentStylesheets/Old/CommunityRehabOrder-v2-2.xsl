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
		<releaseinfo role="meta">Version 2.2</releaseinfo>
		<author>
			<surname>Cooke</surname>
			<firstname>Malcolm</firstname>
		</author>
	</referenceinfo>
	<title>Community Rehab Order Stylesheet</title>
	<para>File name : CommunityRehabOrder-v2.xsl</para>
	<partintro>
		<section>
			<title>Introduction</title>
			<para>This module produces the Community Rehab Order in html format</para>
		</section>
	</partintro>
</doc:reference>


<!-- Version Information -->
<xsl:variable name="majorVersion" select="'2'" />
<xsl:variable name="minorVersion" select="'2'" />
<xsl:variable name="stylesheet" select="'CommunityRehabOrder-v2-2.xsl'" />
<xsl:variable name="last-modified-date" select="'2004-07-26'" />
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

<xsl:variable name="conditionality">
	<xsl:choose>
	<xsl:when test="//cs:BailDecision='Conditional'">
		<xsl:text>conditionally</xsl:text>
	</xsl:when>
	<xsl:otherwise>
		<xsl:text>unconditionally</xsl:text>
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
			<xsl:apply-templates select="//cs:OrderHeader" /> 
			
			<!-- +++++++++ following template process the charges +++++++++ -->	
			<xsl:call-template name="charge" />
			<xsl:apply-templates select="//cs:CommittingCourt" />
			<xsl:apply-templates select="//cs:Breach" />
			<xsl:apply-templates select="//cs:OriginalOrderRevoked/cs:CurrentCourt" />
			<xsl:call-template name="rehabilitation" />

			<xsl:call-template name="orderConditions" /> 
			<xsl:call-template name="orderSignatory" />
			<xsl:call-template name="associatedCases" /> 
			<xsl:call-template name="footer" />
			
			
			</body>
		</html>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- OrderHeader Template			-->
	<!-- **************************************** -->

	<doc:template name="OrderHeader" xmlns="">
		<refpurpose>Creates the Order Header information; including the court details and a list of any associated cases.</refpurpose>
	</doc:template>

	<xsl:template match="cs:OrderHeader">
	<!-- processes the OrderHeader node - constructs the initial header information for the output -->
		<h2>
		<table WIDTH="100%">
			<tr>
				<td WIDTH="70%">
					<h1> In the <xsl:value-of select="cs:CourtHouse/cs:CourtHouseType"/>
						<xsl:text> at </xsl:text>
						<xsl:value-of select="cs:CourtHouse/cs:CourtHouseName"/>
					</h1>
				</td>
				<td WIDTH="15%"><xsl:text>Case No:</xsl:text></td>
				<td WIDTH="15%"><xsl:value-of select="cs:CaseNumber"/></td>
			</tr>
			<tr>
				<td></td>
				<td><xsl:text>Court Code:</xsl:text></td>
				<td><xsl:value-of select="cs:CourtHouse/cs:CourtHouseCode"/></td>
			</tr>
			<!-- Added URN for CR49  -->
			<tr>
				<td></td>
				<td><xsl:text>PTI URN</xsl:text></td>
				<td><xsl:value-of select="cs:Defendant/cs:URN"/></td>
			</tr>
		</table>
		</h2>
		
		<center>
			<h1>
			<strong>
				<xsl:value-of select="'Community Rehabilitation Order'"/>
			</strong>
			</h1>
		</center>
		<hr />
		<xsl:apply-templates select="cs:Defendant/cs:PersonalDetails" />
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
				<para>Note: Uses the routine util:personsFullName to format the name.	</para>
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
		<table WIDTH="100%" >
			<tr>
				<td WIDTH="70%">
					<xsl:text>The </xsl:text>
					<xsl:call-template name="str:to-lower">
						<xsl:with-param name="text" select="$subjectType"/>
					</xsl:call-template>
					<xsl:text> </xsl:text>
					<strong>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="cs:Name"/>
						</xsl:call-template>
					</strong>
				</td>
				<td WIDTH="30%">
					<xsl:text>Date of birth : </xsl:text>
					<strong>
						<xsl:call-template name="util:ukdate_mon">
							<xsl:with-param name="inDate" select="cs:DateOfBirth/apd:BirthDate"/>
						</xsl:call-template>
					</strong>
				</td>
			</tr>
			
			<tr>
				<td>
					<xsl:text>whose address is or will be </xsl:text>
					<strong>
					<xsl:call-template name="util:address_oneline" >
						<xsl:with-param name="personalDetails" select="." />
					</xsl:call-template>
					</strong>
				</td>
				<td />
			</tr>
			
			<xsl:if test="//cs:PettySessionalArea">
				<tr>
					<td colspan="2">
						<xsl:text>in the petty sessional area of </xsl:text>
						<xsl:value-of select="//cs:PettySessionalArea/cs:CourtHouseName"/>
					</td>
					<td />
				</tr>
			</xsl:if>
			
			<tr>
				<td>
					<xsl:text>has been convicted of</xsl:text>
				</td>
			</tr>		
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- chargeTemplate					-->
	<!-- **************************************** -->
	
	<doc:template name="charge" xmlns="">
		<refpurpose>Details the charges against the defendant.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: If the Charge element is present for a defendant then use that as the charge information otherwise use the information held in 
	            the AdditionalNotes element.</para>
			</listitem>
			<listitem>
				<para>Note: Only when using the Charge element - if there are multiple occurence of the same charge (identified by CJSOffenceCode) they 	should only be listed once with a multiplier
	            e.g. 3 x Dangerous Driving. Also if the Disposal element is available then the information should be shown alongside the charge.</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:key name="chargesByCode" 
	         match="//cs:OrderHeader/cs:Defendant/cs:Charges/cs:Charge" 
			 use="@CJSoffenceCode"/>
	
	<xsl:template name="charge" >
		<xsl:variable name="theCharges" select="//cs:OrderHeader/cs:Defendant/cs:Charges" />
		<xsl:choose>
		<xsl:when test="$theCharges">
			
			<xsl:for-each select="$theCharges/cs:Charge[count(. | key('chargesByCode', ./@CJSoffenceCode)[1]) = 1]">



				<xsl:variable name="num" select="count( key('chargesByCode', ./@CJSoffenceCode))"/> 
			
					<h4>
						<xsl:value-of select="./cs:OffenceStatement"/>
						<xsl:if test="$num &gt; 1">
							<xsl:text> x </xsl:text>
							<xsl:value-of select="$num"/>
						</xsl:if>
					</h4>
					<xsl:if test="./cs:Disposals" >
						<p>
							<xsl:for-each select="./cs:Disposals/cs:Disposal" >
								<xsl:value-of select="."/>
								<br />
							</xsl:for-each>
						</p>
					</xsl:if>
			</xsl:for-each>
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="//cs:Defendant/cs:AdditionalNotes">
				<h4>
					<xsl:value-of select="//cs:Defendant/cs:AdditionalNotes"/>
				</h4>
			</xsl:if>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- CommittingCourt Template		-->
	<!-- **************************************** -->

	<doc:template name="CommittingCourt" xmlns="">
		<refpurpose>Outputs the name of the Committing Court and the date the defendant was committed.</refpurpose>
	</doc:template>
	
	<xsl:template match="cs:CommittingCourt" >
		<xsl:text> at </xsl:text>
		<xsl:value-of select="./cs:CourtHouse/cs:CourtHouseName" />
		<xsl:if test="./cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
			<xsl:text> Crown Court</xsl:text>
		</xsl:if>
		<xsl:text> on </xsl:text>
		<xsl:call-template name="util:ukdate_mon">
			<xsl:with-param name="inDate" select="./cs:Date" />
		</xsl:call-template>
		<xsl:text> and was committed for sentence to the Crown Court. </xsl:text>
		<br />
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- Breach Template				-->
	<!-- **************************************** -->

	<doc:template name="Breach" xmlns="">
		<refpurpose>Outputs Outputs details where a defendant has been found to be in breach of a previous order.</refpurpose>
		<para>Shows the court where the defendant has been found to be in breach, the order which has been breached,
	and the court which originally issued the breached order, and if available the date that order was given.</para>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:getArticle to show 'a' or 'an' depending upon the type of Order breached.</para>
			</listitem>
			<listitem>
				<para>Note: If SentToCrownCourt = 'yes' show '...was sent to the Crown Court to be dealt with.',
	            otherwise show '...and was committed to this court to be dealt with.'.</para>
			</listitem>
			<listitem>
				<para>Note: If OriginalOrderRevoked/BreachCourt = 'yes' show:</para>
				<para>'[Name of Breach Court] was satisfied that it was in the interest of justic to revoke [original order type] ...' etc</para>
			</listitem>
		</itemizedlist>
	</doc:template>
	
	<xsl:template match="cs:Breach" >
		<xsl:text>has been found by the </xsl:text>
		<xsl:if test="cs:BreachCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
			<xsl:text> Crown Court at </xsl:text>
		</xsl:if>
		<xsl:value-of select="cs:BreachCourt/cs:CourtHouse/cs:CourtHouseName"/>
		
		<xsl:text> to be in breach of the requirements of </xsl:text>
		<xsl:call-template name="util:getArticle">
			<xsl:with-param name="inText" select="cs:OriginatingCourt/cs:OriginalOrderType" />
		</xsl:call-template>
		<xsl:text> Order made by the </xsl:text>
		<xsl:if test="cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
			<xsl:text> Crown Court at </xsl:text>
		</xsl:if>
		<xsl:value-of select="cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseName" />
		
		<xsl:if test="cs:OriginatingCourt/cs:Date">
			<xsl:text> on </xsl:text>
			<xsl:call-template name="util:ukdate_mon">
				<xsl:with-param name="inDate" select="cs:OriginatingCourt/cs:Date" />
			</xsl:call-template>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="cs:breachCourt/cs:SentToCrownCourt = 'yes'">
				<xsl:text> and was sent to the Crown Court to be dealt with. </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> and was committed to this Court to be dealt with. </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<br />
		<xsl:if test="../cs:OriginalOrderRevoked/cs:BreachCourt='yes'">
			<xsl:value-of select="cs:BreachCourt/cs:CourtHouse/cs:CourtHouseName"/>
			<xsl:if test="cs:BreachCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
				<xsl:text> Crown Court</xsl:text>
			</xsl:if>
			<xsl:text> was satisfied that it was in the interests of justice to revoke the </xsl:text>
			<xsl:value-of select="cs:OriginatingCourt/cs:OriginalOrderType" />
			<xsl:text> order and for </xsl:text>
			<xsl:if test="cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseType = 'Crown Court'">
				<xsl:text> Crown Court at </xsl:text>
			</xsl:if>
			<xsl:value-of select="cs:OriginatingCourt/cs:CourtHouse/cs:CourtHouseName" />
			<xsl:text> to deal with the defendant in some other manner for which it made the order</xsl:text>
			<xsl:if test="cs:OriginatingCourt/cs:Date">
				<xsl:text> on </xsl:text>
				<xsl:call-template name="util:ukdate_mon">
					<xsl:with-param name="inDate" select="cs:OriginatingCourt/cs:Date" />
				</xsl:call-template>
			</xsl:if>
			<xsl:text>. </xsl:text>
			<br />
		</xsl:if>

	</xsl:template>
	
	<!-- **************************************** -->
	<!-- OriginalOrderRevoked Template	-->
	<!-- **************************************** -->

	<doc:template name="OriginalOrderRevoked/CurrentCourt" xmlns="">
		<refpurpose>If this element is = 'yes' and the element FailedToComply not = 'yes' then outputs a variant of the failed to comply paragraph.
	            The template FailedToComply caters for the situation where the element FailedToComply = 'yes' </refpurpose>
	</doc:template>

	
	<xsl:template match="cs:OriginalOrderRevoked/cs:CurrentCourt">
		<xsl:if test=". = 'yes' and not(//cs:FailedToComply = 'yes')" > 
			<br />
			<xsl:text>This court was satisfied that the defendant has failed to comply with a requirement of the 
		          above order and was satisfied that it was in the interests of justice to revoke the order.</xsl:text>
			<br />
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- FailedToComply Template		-->
	<!-- **************************************** -->
	
	<doc:template name="FailedToComply" xmlns="">
		<refpurpose>If this element is = 'yes' shows a failed to comply paragraph </refpurpose>
		<para>Note: If either CurrentCourt or BreachCourt elements within OriginalOrderRevoked = 'yes' then show the clause 
	about revoking the order in the interest of justice.</para>
	</doc:template>
	
	<xsl:template match="cs:FailedToComply">
		<xsl:if test=". = 'yes' " > 
			<br />
			<xsl:text>This court was satisfied that the defendant has failed to comply with a requirement of the 
		          above order
			</xsl:text>
			<xsl:if test="//cs:OriginalOrderRevoked/cs:CurrentCourt = 'yes' or
						  //cs:OriginalOrderRevoked/cs:BreachCourt = 'yes' ">
				<xsl:text> and was satisfied that it was in the interests of justice to revoke the order</xsl:text>
			</xsl:if>
			<xsl:text> and </xsl:text>
			<br />
		</xsl:if>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- rehabilitation Template			-->
	<!-- **************************************** -->

	<doc:template name="rehabilitation" xmlns="">
		<refpurpose>Outputs warning about failure to comply with the order </refpurpose>
		<refdescription>
			<para>Shows the paragraph warning the defendant about failure to comply with this order.</para>
		</refdescription>
	</doc:template>

	<xsl:template name="rehabilitation">
			<br />
			<xsl:text>On </xsl:text>
			<xsl:call-template name="util:ukdate_fullMonth">
				<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:OrderDate"/>
			</xsl:call-template>
			<xsl:apply-templates select="//cs:FailedToComply" />
			<xsl:text> this Court, being of the opinion that it was appropriate to make a Community Rehabilitation Order,
					   explained to the defendant the effect of this order (including any additional requirements shown below), 
					   the consequences which may follow failure to comply with this order, and the right of the 
					   defendant or the supervising officer to apply for review of the order.
			</xsl:text>
			<br />
	</xsl:template>
 
 	<!-- **************************************** -->
	<!-- OrderConditions Template		-->
	<!-- **************************************** -->
  
	<doc:template name="OrderConditions" xmlns="">
		<refpurpose>Determines if any Rehabilitation Period has been ordered and shows the details if appropriate, 
	followed by any conditions that apply.</refpurpose>
		<para>
			<emphasis role="bold">Special Rules</emphasis>
		</para>
		<itemizedlist>
			<listitem>
				<para>Note: Uses the routine util:decodeDuration to format the period of rehabilitation ordered.</para>
			</listitem>
			<listitem>
				<para>Note: Uses the routine util:bulletRow to put the details of the Community Punishment as bulleted rows in a table.</para>
			</listitem>
			<listitem>
				<para>Note: Uses util:processCondition to process each of the Conditions/Condition elements</para>
			</listitem>
		</itemizedlist>
	</doc:template>
 
 	<xsl:template name="orderConditions">
		<xsl:if test="//cs:RehabilitationPeriod" >
			<br />
			<strong>
				<xsl:text>It is ordered</xsl:text>
			</strong>
			<xsl:text> that the defendant shall: </xsl:text>
			<br />
			<table width="100%" >
				<xsl:variable name="text" >
					<xsl:text>For a period of </xsl:text>
					<xsl:call-template name="util:decodeDuration">
						<xsl:with-param name="duration" select="//cs:RehabilitationPeriod"/>
					</xsl:call-template>
					<xsl:text> from the date of this order be under the supervision of a probation officer</xsl:text>
					<xsl:if test="//cs:PettySessionalArea">
						<xsl:text> for the petty sessional area of </xsl:text>
						<xsl:call-template name="str:to-upper">
							<xsl:with-param name="text" select="//cs:PettySessionalArea/cs:CourtHouseName" />
						</xsl:call-template>
					</xsl:if>
					<xsl:text>.</xsl:text>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text"/>
				</xsl:call-template>
				<xsl:variable name="text2" >
					<xsl:text>Keep in touch with the probation officer in accordance with such instructions as may from
							  time to time be given by that officer and notify the probation officer of any change of address.
					</xsl:text>
				</xsl:variable>
				<xsl:call-template name="util:bulletRow" >
					<xsl:with-param name="bulletText" select="$text2"/>
				</xsl:call-template>
				<xsl:for-each select="//cs:Conditions/cs:Condition">
					<xsl:call-template name="util:processCondition" >
						<xsl:with-param name="condition" select="."/>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>
	
	 <!-- **************************************** -->
	<!-- orderSignatory Template			-->
	<!-- **************************************** -->
 	
	<doc:template name="orderSignatory" xmlns="">
		<refpurpose>Outputs the signatory information for the order.</refpurpose>
	</doc:template>

	<xsl:template name="orderSignatory">
		<p />
		<p />
		<table width="100%">
			<tr>
				<td width="5%"> </td>
				<td width="55%">
					<xsl:text>Date: </xsl:text>
					<xsl:call-template name="util:ukdate_mon">
						<xsl:with-param name="inDate" select="//cs:OrderHeader/cs:SignedDate" />
					</xsl:call-template>
				</td>
				<td width="40%">
					<xsl:choose>
					<xsl:when test="//cs:OrderHeader/cs:SignedBy/cs:CourtOfficer">
						<xsl:text>An Officer of the Crown Court </xsl:text>
						<strong>
						<xsl:value-of select="//cs:SignedBy/cs:CourtOfficer/apd:CitizenNameTitle"/>
						<xsl:text> </xsl:text>
						<xsl:call-template name="util:personsFullName">
							<xsl:with-param name="name" select="//cs:SignedBy/cs:CourtOfficer"/>
						</xsl:call-template>
						</strong>
					</xsl:when>
					<xsl:otherwise>
						<strong>
						<xsl:call-template name="util:formalName">
							<xsl:with-param name="name" select="//cs:OrderHeader/cs:SignedBy/cs:Judge" />
						</xsl:call-template>
						</strong>
					</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- associatedCases Template		-->
	<!-- **************************************** -->

	<doc:template name="associatedCases" xmlns="">
		<refpurpose>Outputs the list of associated cases if any.</refpurpose>
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
		<xsl:call-template name="util:associatedCases">
			<xsl:with-param name="cases" select="//cs:AssociatedCases"/>
		</xsl:call-template>
	</xsl:template>
	
	<!-- **************************************** -->
	<!-- footerTemplate					-->
	<!-- **************************************** -->

	<doc:template name="footer" xmlns="">
		<refpurpose>Outputs the report footer.</refpurpose>
	</doc:template>
	
	<xsl:template name="footer">
		<small>
			<xsl:text>(For the use of the probation services only)  </xsl:text>
		</small>
		<xsl:text>This order has been explained to me and I have received a copy of it.</xsl:text>
		<table width="100%">
			<tr>
				<td width="20%" align="right">
					<strong>
						<xsl:text>Signed:</xsl:text>
					</strong>
				</td>
				<td width="40%" align="right">
					<strong>
						<xsl:text>Date:</xsl:text>
					</strong>
				</td>
				<td width="40%" align="right" />
			</tr>
		</table>
		<hr />
	</xsl:template>

</xsl:stylesheet>
