<?xml version="1.0" encoding="UTF-8"?>
<!--  $Revision: 1.21 $  $Author: xztnfq $ $Date: 2005/04/19 11:06:56 $-->
<xsl:stylesheet version="1.0" 
xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" 
xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" 
xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" 
xmlns:fo="http://www.w3.org/1999/XSL/Format"
xmlns:set="http://xml.apache.org/xslt">
	<xsl:output method="xml" version="1.0" indent="yes"/>
	<xsl:param name="mode" select="'display'"/>
	<!--xsl:param name="imageURL" select="file:///d:/projects/XHIBIT/thickclient/orders/images/lcd-logo-sm.gif"/-->
	<!-- ****************************************************************************** -->
	<!-- 05/03/03 Neil Entwistle Header and Footer need formatting            -->
	<!-- ****************************************************************************** -->
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master 
					master-name="print" 
					page-height="297mm" 
					page-width="210mm" 
					margin-top="20mm" 
					margin-bottom="15mm" 
					margin-left="1.0cm" 
					margin-right="0.5cm">
					<fo:region-body 
						margin-top="45mm" margin-bottom="15mm"/>
					<fo:region-before region-name="header-first"
						extent="45.5cm"/>
					<fo:region-after region-name="footer-first"
						extent="15mm"/>
				</fo:simple-page-master>
				<!-- S.Bachra 22/04/03 page-height adjusted to make display page longer -->
				<fo:simple-page-master 
					master-name="display" 
					page-height="650mm"  
					page-width="210mm" 
					margin-top="20mm" 
					margin-bottom="15mm" 
					margin-left="0.5cm" 
					margin-right="0.5cm">
					<fo:region-body 
						margin-top="55mm" margin-bottom="15mm"/>
					<fo:region-before region-name="header-first"
						extent="45.5cm"/>
					<fo:region-after region-name="footer-first"
						extent="1.5cm"/>
				</fo:simple-page-master>
				<fo:simple-page-master 
					master-name="rest" 
					page-height="297mm" 
					page-width="210mm" 
					margin-top="20mm" 
					margin-bottom="15mm" 
					margin-left="0.5cm" 
					margin-right="0.5cm">
					<fo:region-body 
						margin-top="15mm" margin-bottom="15mm"/>
					<fo:region-before region-name="header-rest"
						extent="1.5cm"/>
					<fo:region-after region-name="footer-rest"
						extent="7mm"/>
				</fo:simple-page-master>
    				<fo:page-sequence-master master-name="document">
     					 <fo:repeatable-page-master-alternatives>
         					<fo:conditional-page-master-reference page-position="first">
         						<xsl:attribute name="master-reference">
         							<xsl:value-of select="$mode"/>
         						</xsl:attribute>
         					</fo:conditional-page-master-reference>
         					 </fo:repeatable-page-master-alternatives>
    				</fo:page-sequence-master>
			</fo:layout-master-set>
			
			<fo:page-sequence master-reference="document">
  				 <fo:static-content flow-name="footer-first">
  				 	<fo:block>
  				 		<!-- S.Bachra 22/4/03 Signed Details: Display Judge of the Crown Court if Bench Warrant otherwise Officer of the Crown Court -->
  				 		<xsl:choose>
							<xsl:when test="/ord:Order/ord:OrderData/ord:BenchWarrant">
								Judge of the Crown Court
							</xsl:when>
							<xsl:otherwise>
								An Officer of the Crown Court 	
							</xsl:otherwise>
						</xsl:choose>
  				 	</fo:block>
  				 	<fo:block>
	 				 	 <fo:inline>
	  			 	               Signed: 
	  			 	               <fo:leader leader-pattern="space" leader-length="80mm"/>
	  			 	               Date:<xsl:call-template name="SignedDate"/>
	  				 	 </fo:inline> 
  				 	</fo:block>
    				</fo:static-content>
    				<fo:static-content flow-name="footer-rest">
 				 	<fo:block>
  				 		<fo:inline>
	  				 		<!-- S.Bachra 22/4/03 Signed Details: Display Judge of the Crown Court if Bench Warrant otherwise Officer of the Crown Court -->
	  				 		<xsl:choose>
								<xsl:when test="/ord:Order/ord:OrderData/ord:BenchWarrant">
									Judge of the Crown Court
								</xsl:when>
								<xsl:otherwise>
									An Officer of the Crown Court 	
								</xsl:otherwise>
							</xsl:choose>
  				 			<fo:leader leader-pattern="space" leader-length="80mm"/>
 				 			Date:<xsl:call-template name="SignedDate"/>
  				 		</fo:inline>
  				 	</fo:block>
    				</fo:static-content>
  				 <fo:static-content flow-name="header-first">
  				 <fo:list-block> 
  				 <fo:list-item>
                            <fo:list-item-label end-indent="label-end()">
                             <fo:block>
                              <fo:block text-align="left" font-weight="500" font-size="18pt">
	                              <!-- S.Bachra 22/4/03 Change to standard text In the CourtType -->
	                              <xsl:if test="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType='Magistrates Court'">
     	                         	In the Magistrates Court 
							</xsl:if>
							<xsl:if test="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType='Crown Court'">
                               		In the Crown Court 
							</xsl:if>
						</fo:block>

                                <fo:block space-after="12pt" font-size="16pt" font-weight="500">
                                    at <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseName"/>
                                    <!-- S.Bachra 22/4/03 Display the OrderDate/Warrant Date if Bench Warrant Order -->
                                    <xsl:if test="/ord:Order/ord:OrderData/ord:BenchWarrant">
								<fo:block space-after="12pt" font-size="16pt" font-weight="500">
	                                    	on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:OrderDate"/>
	                                  	</fo:block>
                                    </xsl:if>
                                </fo:block>
                                <fo:block>
                                    Case No: <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:CaseNumber"/>
                                </fo:block>
                                <fo:block>
                                    Court Code: <xsl:value-of select="/ord:Order/ord:OrderData//ord:OrderHeader/ord:CourtHouse/ord:CourtHouseCode"/>
                                </fo:block>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body>
                            <fo:list-item>
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block text-align="center"></fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block text-align="right"> 
                                        <fo:inline>
                                            <!--fo:external-graphic src="url(/images/lcd-logo-sm.gif)"/-->
                                        </fo:inline>
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
            </fo:list-item-body>
        </fo:list-item>
		</fo:list-block>
     			</fo:static-content>
    				<fo:static-content flow-name="header-rest">
      					<fo:block>Other page header.</fo:block>
    				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<xsl:apply-templates select="/ord:Order/nar:Narrative/nar:Body"/>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- ****************************** -->
	<!-- TEMPLATES START HERE-->
	<!-- ****************************** -->

	<xsl:template name="nar:AppellantDefendant">
		<fo:inline>
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader">
				<xsl:choose>
					<xsl:when test="substring(ord:CaseNumber,1,1)='A'">
				appellant
				</xsl:when>
					<xsl:otherwise>
				defendant
				</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	
	<xsl:template name="nar:BreachText">
		These hours of work to be 
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CommunityPunishment/ord:Hours/@TermType='Concurrent'">
				concurrent
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CommunityPunishment/ord:Hours/@TermType='Consecutive'">
				consecutive
			</xsl:when>
		</xsl:choose>
		those specified in a Community Punishment Order made by <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/> 
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
			<xsl:call-template name="nar:CourtHouseType"/>
		</xsl:for-each>
		on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/>
	</xsl:template>

	<xsl:template name="CallableAddress">
		<fo:inline>
			<xsl:for-each select="apd:Line">
				<xsl:if test=". !=' '">
					<xsl:value-of select="."/>,
				</xsl:if>
		</xsl:for-each>
			<xsl:value-of select="apd:PostCode"/>
		</fo:inline>
	</xsl:template>

	<xsl:template name="nar:CommunityPunishmentHours">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:CommunityPunishment/ord:Hours"/>
	</xsl:template>

	<xsl:template name="nar:CommunityBreachCourt">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:BreachCourt/ord:CourtHouse/ord:CourtHouseName"/>
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:BreachCourt/ord:CourtHouse">
			<xsl:call-template name="nar:CourtHouseType"/>
		</xsl:for-each>	
	</xsl:template>

	<xsl:template name="nar:CommunityCommittingCourt">
		<fo:inline>
			<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:CommittingCourt/ord:CourtHouse/ord:CourtHouseName"/>
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CommittingCourt/ord:CourtHouse">
				<xsl:call-template name="nar:CourtHouseType"/>
			</xsl:for-each>
			on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:CommittingCourt/ord:Date"/>
		</fo:inline> 
	</xsl:template>

	<xsl:template name="nar:CommunityOriginatingCourt">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/>
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
			<xsl:call-template name="nar:CourtHouseType"/>
		</xsl:for-each>
		on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/>	
	</xsl:template>

	<xsl:template name="nar:CommunityOriginatingCourtExcDate">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
			<xsl:call-template name="nar:CourtHouseType"/>
		</xsl:for-each>
		at <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/>
	</xsl:template>

	
	<xsl:template name="nar:CourtHouseType">
		<xsl:choose>
			<xsl:when test="./ord:CourtHouseType='Magistrates Court'"> Magistrates Court</xsl:when>
			<xsl:when test="./ord:CourtHouseType='Crown Court'"> Crown Court</xsl:when>
			<xsl:when test="./ord:CourtHouseType='Youth Court'"> Youth Court</xsl:when>
			<xsl:otherwise> unspecified court</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template used to format TextArea widgets to force a newline where appropriate -->
	<xsl:template name="FormatTextArea">
    	<xsl:param name="string"/>
    		<xsl:choose>
	        	<xsl:when test="contains($string,'&#10;')">
	            	<xsl:value-of select="substring-before($string,'&#10;')"/>
	            	<fo:block/>
	           	<xsl:call-template name="FormatTextArea">
	                	<xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
	            </xsl:call-template>
	        	</xsl:when>
	        	<xsl:otherwise>
	            <xsl:value-of select="$string"/>
	        	</xsl:otherwise>
    		</xsl:choose>
	</xsl:template>

	<xsl:template name="nar:HearingOption">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:AfterHearingFrom='yes'">
			and after hearing a probation officer or social worker of a local authority social services department
		</xsl:if>
	</xsl:template>


	<!-- ************************************************************* -->
	<!-- SB 17/04/03 Cover scenario when gender not set in XML -->
	<!-- ************************************************************* -->
	<xsl:template name="nar:HimselfHerself">
		<xsl:choose>
			<xsl:when test="./ord:Sex='male'">himself</xsl:when>
			<xsl:when test="./ord:Sex='female'">herself</xsl:when>
			<xsl:otherwise>himself/herself</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ************************************************************* -->
	<!-- SB 17/04/03 Cover scenario when gender not set in XML -->
	<!-- ************************************************************* -->
	<xsl:template name="nar:MaleFemale">
		<xsl:choose>
			<xsl:when test="./ord:Sex='male'">him</xsl:when>
			<xsl:when test="./ord:Sex='female'">her</xsl:when>
			<xsl:otherwise>him/her</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="nar:PettySessionalArea">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:PettySessionalArea/ord:CourtHouseName"/>
	</xsl:template>


	<!-- ***************************************************************** -->
	<!-- SB 17/04/03 Check to ensure that the value is not the default -->
	<!-- set-up in the blank schema 2000-01-01, if not select value     -->
	<!-- ***************************************************************** -->
	<xsl:template name="SignedDate">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:SignedDate != '2000-01-01'">
			<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:SignedDate"/>
		</xsl:if>
	</xsl:template>

	<!-- ********************************************************** -->
	<!-- Display only months value if the years value is set to 0 -->
	<!-- ********************************************************** -->
	<xsl:template name="nar:Term">
		<xsl:choose>
			<xsl:when test="./ord:Years='0'">
				<xsl:value-of select="./ord:Months"/> months
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="./ord:Years"/> years <xsl:value-of select="./ord:Months"/> months
			</xsl:otherwise>			
		</xsl:choose>
	</xsl:template>
	
	<!-- ************************************************************** -->
	<!-- Display month and day values if the years value is set to 0 -->
	<!-- ************************************************************** -->
	<xsl:template name="nar:TermIncDays">
		<xsl:choose>
			<xsl:when test="./ord:Years='0'">
				<xsl:value-of select="./ord:Months"/> months <xsl:value-of select="./ord:Days"/> days
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="./ord:Years"/> years <xsl:value-of select="./ord:Months"/> months <xsl:value-of select="./ord:Days"/> days
			</xsl:otherwise>			
		</xsl:choose>
	</xsl:template>

	<xsl:template name="nar:TermMonths">
		<xsl:value-of select="./ord:Months"/> months
	</xsl:template>

	<xsl:template name="nar:TermType">
		<xsl:if test=".='Concurrent'">
		concurrent
		</xsl:if>
		<xsl:if test=".='Consecutive'">
		consecutive
		</xsl:if>
	</xsl:template>

	<!-- ********************************* -->
	<!-- XSL MATCHES START HERE -->
	<!-- ********************************* -->
	
	<!-- used for addition of two dates -->
	<xsl:template match="nar:AdditionOfDates">
		<!-- years value -->
		<xsl:value-of select="(/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Years + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor(((/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Months + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> years

		<!-- months value -->
		<xsl:value-of select="(/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Months + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> months
	</xsl:template>
	
	<!-- S.Bachra 22-04-03 Used for YOI Order - also add up days -->
	<!-- used for addition of two dates (including Days) -->
	<xsl:template match="nar:AdditionOfDatesIncDays">
		<!-- years value -->
		<xsl:value-of select="(/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Years + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Years) + floor(((/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Months + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) div 12))"/> years

		<!-- months value -->
		<xsl:value-of select="(/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Months + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Months) mod 12"/> months

		<!-- days value -->
		<xsl:value-of select="(/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term/ord:Days + /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod/ord:Days)"/> days
	</xsl:template>


	<xsl:template match="nar:AdditionalNotes">
		<xsl:call-template name="FormatTextArea">
			<xsl:with-param name="string" select="/ord:Order/ord:OrderData/*/ord:AdditionalNotes"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="nar:RmdAdditionalInfo">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:AdditionalInfo/@selected='true'">
			<fo:block>Additional requirements:</fo:block>
			<fo:block space-after="12pt">
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="/ord:Order/ord:OrderData/*/ord:AdditionalInfo"/>
				</xsl:call-template>
			</fo:block>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="nar:AdditionalRequirements">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:Conditions/@selected='true'">
			<fo:block space-before="12pt">Additional requirements:</fo:block>
			<fo:block>
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="/ord:Order/ord:OrderData/*/ord:Conditions/ord:Condition/ord:Description"/>
				</xsl:call-template>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:Address">
		<xsl:call-template name="nar:CallableAddress"/>
	</xsl:template>
	
	<xsl:template match="nar:AddressOption">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:AddressOption='AddressIs'">
			is 
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
				<xsl:call-template name="CallableAddress"/>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:AddressOption='AddressWillBe'">
			will be
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
				<xsl:call-template name="CallableAddress"/>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:AppealAgainstBailGranted">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:AppealAgainstBailGranted[@selected='true']">
			as a result of an appeal by the Prosecution against the grant of bail<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:AppellantDefendant">
		<fo:inline>
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader">
				<xsl:choose>
					<xsl:when test="substring(ord:CaseNumber,1,1)='A'">
				appellant
				</xsl:when>
					<xsl:otherwise>
				defendant
				</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>

	<!-- Display 'None' if no Associated Cases have been selected -->
	<xsl:template match="nar:AssociatedCases">
		<fo:block>
			<fo:inline font-weight="bold"> Associated Cases: </fo:inline>
			<xsl:choose>
				<xsl:when test="count(/ord:Order/ord:OrderData/*/ord:AssociatedCases/ord:AssociatedCase[@selected='true'])='0'">
					None
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:AssociatedCases/ord:AssociatedCase[@selected='true']">
						<fo:block>
							<xsl:value-of select="."/>
						</fo:block>
					</xsl:for-each>	
					</xsl:otherwise>
			</xsl:choose>
		</fo:block>
	</xsl:template>

	<xsl:template match="nar:BailGrantedType">
		<fo:inline>
			<xsl:if test="/ord:Order/ord:OrderData/ord:BailOrder/ord:BailDecision='Conditional'">
				conditionally
			</xsl:if>
			<xsl:if test="/ord:Order/ord:OrderData/ord:BailOrder/ord:BailDecision='Unconditional'">
				unconditionally
			</xsl:if>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:CertificateOfTransfer">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:CertificateOfTransfer[@selected='true']">
			on a Certificate of Transfer dated <xsl:value-of select="."/><br/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:CommittingCourtHouseName">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:CrownCourt/ord:CourtHouse/ord:CourtHouseName"/> 
	</xsl:template>

	<xsl:template match="nar:CommittedDate">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:CrownCourt/ord:Date"/> 
	</xsl:template>

	<xsl:template match="nar:CommittedForSentence">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:CommittingCourt/@selected ='true'">
			<fo:inline>
				at <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:CommittingCourt/ord:CourtHouseName"/> Magistrates Court and committed for sentence to the Crown Court.
			</fo:inline>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:CommitSent">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:CommitSent='committed'">
			committed
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:CommitSent='sent'">
			sent for trial
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<!-- 	S. Bachra 20/3/03
			Transform used to display the enumeration of the Defendants convictions
			1. Get all the distinct Offence Statements i.e only select if not already selected
			2. Sort the Offence Statements Alphabetically
			3. Set up variable to hold current Offence Statement
			4. Display Offence Statement X count of all Offence Statements that match the variable
	-->

	<xsl:template match="nar:Conviction">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement[not(.=preceding::ord:OffenceStatement)]">
			<xsl:sort select="."/>
			<xsl:variable name="value" select="."/>
			<xsl:value-of select="."/><xsl:text> </xsl:text> X <xsl:value-of select="count(/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement[(.=$value)])"/><xsl:text> </xsl:text>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:ConvictionDate">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:ConvictionDate"/>
	</xsl:template>
	
	<xsl:template match="nar:CustodialTerm">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="nar:Term"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:CustodialTermIncDays">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term">
			<xsl:call-template name="nar:TermIncDays"/>
		</xsl:for-each>
	</xsl:template>


	<xsl:template match="nar:CustodyLocation">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:CustodyLocation"/>
	</xsl:template>
	
	<!-- used within the Community PUNISHMENT Order -->
	<xsl:template match="nar:CPOOrderedText">
		<fo:block space-before="12pt" space-after="12pt">
			<fo:inline font-weight="bold">It is ordered</fo:inline> that the defendant, during a period of
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CommunityPunishment/ord:Period">
				<xsl:call-template name="nar:Term"/>
			</xsl:for-each>
			from the date of this order, shall:
			<fo:block space-before="12pt">
			1. Keep in touch with the relevant officer in accordance with such instructions as may from time to time be given by that officer and notify the relevant officer of any change of address.				
			</fo:block>
			<fo:block space-before="12pt" space-after="12pt">
			2. Perform <xsl:call-template name="nar:CommunityPunishmentHours"/> hours of community punishment work at such time as that officer may instruct.  
			<xsl:if test="/ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
				<xsl:call-template name="nar:BreachText"/>	
			</xsl:if>
			</fo:block>
		</fo:block>
	</xsl:template>

	<!-- used within the Community PUNISHMENT Order -->
	<xsl:template match="nar:CPOTrialCommittedBreach">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CommittingCourt/@selected='true'">
				at <xsl:call-template name="nar:CommunityCommittingCourt"/> and was committed to this court to be dealt with.
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
				<fo:block>
					at <xsl:call-template name="nar:CommunityOriginatingCourt"/>
				</fo:block>
				has been found by <xsl:call-template name="nar:CommunityBreachCourt"/>
				to be in breach of the requirements of a <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/> Order made by the
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
					<xsl:call-template name="nar:CourtHouseType"/>
				</xsl:for-each>
				 at <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/> on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/> and was committed to this court to be dealt with.
				<fo:block space-before="12pt" space-after="12pt">
					The <xsl:call-template name="nar:CommunityBreachCourt"/>
					was satisfied that it was in the interests of justice
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:OriginalOrderRevoked/@selected='true'">
						to revoke the <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/> Order								</xsl:if>
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:Breach/ord:BreachCourt/@breachCourtOption='true'">
						and for the <xsl:call-template name="nar:CommunityOriginatingCourtExcDate"/>
					</xsl:if>
					to deal with the defendant in some other manner for the offences for which it made the order on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- used within the Community REHAB Order -->
	<xsl:template match="nar:CROOrderedText">
		<fo:block space-before="12pt" space-after="12pt">
			<fo:inline font-weight="bold">It is ordered</fo:inline> that the defendant shall:
			<fo:block space-before="12pt">
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block font-weight="bold">&#x2022;</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<fo:block>
								For a period of
								<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:RehabilitationPeriod">
									<xsl:call-template name="nar:Term"/>
								</xsl:for-each>
						                <!-- RFC 1356 - change Petty Sessional Area to Local Justice Area -->            
								from the date of this order be under the supervision of a probation officer for the Local Justice Area <xsl:call-template 	name="nar:PettySessionalArea"/>.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block font-weight="bold">&#x2022;</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<fo:block>
								Keep in touch with the probation officer in accordance with such instructions as may from time to time be given by that officer and notify 	the probation officer of any change of address.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</fo:block>	
		</fo:block>
	</xsl:template>
	
	<!-- used within the Community REHAB Order -->
	<xsl:template match="nar:CROTrialCommittedBreach">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CommittingCourt/@selected='true'">
				at <xsl:call-template name="nar:CommunityCommittingCourt"/> and was committed for sentence to the Crown Court.
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
				has been found by <xsl:call-template name="nar:CommunityBreachCourt"/>
				to be in breach of the requirements of a <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/> Order made by the
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
					<xsl:call-template name="nar:CourtHouseType"/>
				</xsl:for-each>
				 at <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/> on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/> and was committed to this court to be dealt with.
				<fo:block space-before="12pt" space-after="12pt">
					The <xsl:call-template name="nar:CommunityBreachCourt"/>
					was satisfied that it was in the interests of justice
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:OriginalOrderRevoked/@selected='true'">
						to revoke the <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/> Order								</xsl:if>
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:Breach/ord:BreachCourt/@breachCourtOption='true'">
						and for the <xsl:call-template name="nar:CommunityOriginatingCourtExcDate"/>
					</xsl:if>
					to deal with the defendant in some other manner for the offences for which it made the order on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<!-- used within the Community PUNISHMENT AND REHAB Order -->
	<xsl:template match="nar:CPROTrialCommittedBreach">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CommittingCourt/@selected='true'">
				at <xsl:call-template name="nar:CommunityCommittingCourt"/> and was committed for sentence to the Crown Court.
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
				<fo:block>
					at <xsl:call-template name="nar:CommunityOriginatingCourt"/>
				</fo:block>
				has been found by <xsl:call-template name="nar:CommunityBreachCourt"/>
				to be in breach of the requirements of a <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/> Order made by the
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
					<xsl:call-template name="nar:CourtHouseType"/>
				</xsl:for-each>
				 at <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/> on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/> and was committed to this court to be dealt with.
				<fo:block space-before="12pt" space-after="12pt">
					The <xsl:call-template name="nar:CommunityBreachCourt"/>
					was satisfied that it was in the interests of justice
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:OriginalOrderRevoked/@selected='true'">
						to revoke the <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/> Order								</xsl:if>
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:Breach/ord:BreachCourt/@breachCourtOption='true'">
						and for the <xsl:call-template name="nar:CommunityOriginatingCourtExcDate"/>
					</xsl:if>
					to deal with the defendant in some other manner for the offences for which it made the order on <xsl:value-of select="/ord:Order/ord:OrderData/*/ord:Breach/ord:OriginatingCourt/ord:Date"/>
				</fo:block>
				<fo:block space-after="12pt">
					This Court was satisfied that the defendant has failed to comply with a requirement of the above order
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:OriginalOrderRevoked/@selected='true'">
						and was satisfied that it was in the interests of justice to revoke the order.
					</xsl:if>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<!-- used within the Community PUNISHMENT AND REHAB Order -->
	<xsl:template match="nar:CPROOrderedText">
		<fo:block space-after="12pt" space-before="12pt">
			a. being of the opinion that it was appropriate to make a Community Rehabilitation Order, explained to the defendant the effect of this order (including any additional requirements shown below), the consequences which may follow failure to comply with the requirements of the order, and that the Court has the power to review this order on the application of either the defendant or the supervising officer.
		</fo:block>
		<fo:block space-after="12pt">
                        <!-- RFC 1356 - change Petty Sessional Area to Local Justice Area -->            
			b. after considering a report by a probation officer or social worker of a local authority social services department about the defendant and the defendants circumstances <xsl:call-template name="nar:HearingOption"/> was satisfied that the defendant is a suitable person to perform community punishment work and that provision can be made for the defendant to do so in the above Local Justice Area.
		</fo:block>
		<fo:block space-after="12pt">
			<fo:inline font-weight="bold">It is ordered</fo:inline> that the defendant shall:
			<fo:block space-before="12pt">
				<fo:list-block provisional-distance-between-starts="5mm">
					
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block font-weight="bold">&#x2022;</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<fo:block>
								For a period of
								<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:RehabilitationPeriod">
									<xsl:call-template name="nar:Term"/>
								</xsl:for-each>
            							<!-- RFC 1356 - change Petty Sessional Area to Local Justice Area -->            
								from the date of this order be under the supervision of a probation officer in the Local Justice Area of <xsl:call-template 	name="nar:PettySessionalArea"/>.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
					
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block font-weight="bold">&#x2022;</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<fo:block>
							Keep in touch with the probation officer in accordance with such instructions as may from time to time be given by that officer and notify 	the relevant officer of any change of address.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
					
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block font-weight="bold">&#x2022;</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()">
							<fo:block>During a period of 
								<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CommunityPunishment/ord:Period">
									<xsl:call-template name="nar:Term"/>
								</xsl:for-each>
								from the date of this order perform <xsl:call-template name="nar:CommunityPunishmentHours"/> hours of community punishment work at such times as the probation officer may instruct. 
								<xsl:if test="/ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
									<xsl:call-template name="nar:BreachText"/>	
								</xsl:if>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</fo:block>	
		</fo:block>
	</xsl:template>

	<xsl:template match="nar:DefendantDOB">
		<fo:inline>
			<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:DateOfBirth/apd:BirthDate"/>
		</fo:inline>
	</xsl:template>

	<xsl:template match="nar:DefendantAddress">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
			<xsl:call-template name="CallableAddress"/>
		</xsl:for-each>
	</xsl:template>

	<!-- S.Bachra 17/4/03 Handling of no Title, Suffix -->
	<xsl:template match="nar:DefendantFullName">
		<fo:inline>
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Name">
				<xsl:if test="count(apd:CitizenNameTitle) !=0">
					<xsl:value-of select="apd:CitizenNameTitle"/> &#160;
				</xsl:if>
				<xsl:value-of select="apd:CitizenNameForename"/>&#160;
				<xsl:value-of select="apd:CitizenNameSurname"/>&#160;
				<xsl:if test="count(apd:CitizenNameSuffix) !=0">
					<xsl:value-of select="apd:CitizenNameSuffix"/> &#160;
				</xsl:if>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>

	<xsl:template match="nar:DetentionOrImprisonment">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:DetentionOrImprisonment">
			<xsl:choose>
				<xsl:when test=".='Detention'">
				detention
				</xsl:when>
				<xsl:when test=".='Imprisonment'">
				imprisonment
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:ExtendedPeriod">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="nar:Term"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:ExtendedPeriodIncDays">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:ExtensionPeriod">
			<xsl:call-template name="nar:TermIncDays"/>
		</xsl:for-each>
	</xsl:template>

	
	<xsl:template match="nar:ExtendedSentenceType">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:SentenceType">
			<xsl:call-template name="nar:TermType"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:FailedToComply">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:FailedToComply='yes' and /ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
			this Court was satisfied that the defendant has failed to comply with a requirement of the above order and
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="nar:Forthwith">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:Release='Unconditional'">
			bring
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
				<xsl:call-template name="nar:MaleFemale"/>
			</xsl:for-each>
			forthwith before the Crown Court
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:HearingOption">
		<xsl:call-template name="nar:HearingOption"/>
	</xsl:template>
	
	<xsl:template match="nar:HimHer">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
			<xsl:call-template name="nar:HimselfHerself"/>
		</xsl:for-each> 
	</xsl:template>

	<xsl:template match="nar:If">
		<xsl:choose>
			<xsl:when test="nar:Condition='ConditionalBail' and /ord:Order/ord:OrderData/*/ord:BailDecision = 'Conditional'">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='ReportIndicator' and /ord:Order/ord:OrderData/*/ord:ReportDetails[@selected='true']">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='RemandCommitted' and /ord:Order/ord:OrderData/*/ord:RemandReason[@selected='true'] and /ord:Order/ord:OrderData/*/ord:RemandReason/ord:IsBefore[@selected='false']">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='RemandIsBefore' and /ord:Order/ord:OrderData/*/ord:RemandReason[@selected='true'] and /ord:Order/ord:OrderData/*/ord:RemandReason/ord:IsBefore[@selected='true']">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='ChapterIII' and /ord:Order/ord:OrderData/*/ord:ChapterIII='yes'">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='ExtendedSentence' and /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/@selected='true' and /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType='period'">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='ReturnOfDefendant' and /ord:Order/ord:OrderData/*/ord:ReturnToImprisonment[@selected='true']">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='DiscretionaryLife' and /ord:Order/ord:OrderData/*/ord:Section28[@selected='true'] and /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType='life'">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>

			<xsl:when test="nar:Condition='DiscretionaryLife' and /ord:Order/ord:OrderData/*/ord:Section28[@selected='true'] and /ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType='section9394'">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			
			<xsl:when test="nar:Condition='Section86' and /ord:Order/ord:OrderData/*/ord:Section86='yes'">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
			
			<xsl:when test="nar:Condition='AdditionalNotes' and /ord:Order/ord:OrderData/*/ord:AdditionalNotes[@selected='true']">
				<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>		

	<!-- used for Imprisonment Order -->
	<xsl:template match="nar:ImprisonLife">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='life'">
				imprisonment for life.		
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='period'">
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term">
					<xsl:call-template name="nar:Term"/> imprisonment
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- display only if imprisonment selected within the YOI screen -->
	<xsl:template match="nar:ImprisonOption">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:DetentionOrImprisonment='Imprisonment'">
			<fo:block space-after="12pt"/>
			<fo:block>
			The Crown Court had, or would have had but for the statutory restrictions upon the imprisonment of young offenders, power to impose imprisonment on the defendant.
			</fo:block>
		</xsl:if>
	</xsl:template>

	<!-- used for YOI Order -->
	<xsl:template match="nar:ImprisonmentType">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='life'">
				for life.		
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='section9394'">
				under the life sentence provisions for young offenders of sections 93 and 94 of the Powers of Criminal Courts (Sentencing) Act of 2000
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='hmpleasure'">
				during His Majesty's pleasure
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='section91'">
				under the long sentence provisions of section 91 of the Powers of Criminal Courts (Sentencing) Act 2000
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ImprisonmentType ='period'">
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:DetentionOrImprisonment">
					<xsl:choose>
					<xsl:when test=".='Detention'">
						in a young offender institution
					</xsl:when>
				</xsl:choose>
				for
				</xsl:for-each>
					<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:Term">
						<xsl:call-template name="nar:TermIncDays"/>
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="nar:IndictedConvicted">
			and has been
			<xsl:choose>
				<xsl:when test="/ord:Order/ord:OrderData/*/ord:RemandType='Indicted'">
				indicted for crime.<br/>
				</xsl:when>
				<xsl:when test="/ord:Order/ord:OrderData/*/ord:RemandType='Convicted'">
				convicted for crime.<br/>
				</xsl:when>
			</xsl:choose>			
	</xsl:template>

	<xsl:template match="nar:LifePrisonerPeriod">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:Section28/ord:DiscretionaryRelevantPart">
			<xsl:call-template name="nar:Term"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:MonetaryValue">
		<xsl:choose>
			<xsl:when test="ord:Currency='GBP'">&#163;</xsl:when>
			<xsl:when test="ord:Currency='EURO'">&#8364;</xsl:when>
			<xsl:otherwise><xsl:value-of select="ord:Currency"/></xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="ord:Amount"/>
	</xsl:template>

	<xsl:template match="nar:NextAppearanceCourtType">
		<fo:inline>
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:NextAppearance/ord:AppearanceCourt">
				<xsl:call-template name="nar:CourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:NextAppearanceDate">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:NextAppearance/ord:AppearanceDateTime/@selected='true'">
				<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:NextAppearance/ord:AppearanceDateTime/ord:AppearanceDate"/> at 
				<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:NextAppearance/ord:AppearanceDateTime/ord:AppearanceTime"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="text()"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="nar:NextAppearanceCourtHouseName">
			<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:NextAppearance/ord:AppearanceCourt/ord:CourtHouseName"/>
	</xsl:template>

	<xsl:template match="nar:OffenceDate">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:OffenceDate"/>
	</xsl:template>

	<xsl:template match="nar:OrderDate">
		<fo:inline>
			<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:OrderDate"/>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:PettySessionalArea">
		<xsl:call-template name="nar:PettySessionalArea"/>
	</xsl:template>
	
	<xsl:template match="nar:PreConditions">
		<!-- Bail Order Pre Conditions -->
		<xsl:if test="/ord:Order/ord:OrderData/ord:BailOrder">
			<!-- Surety -->
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:PreConditions[@selected='true']">
				<fo:block space-after="12pt">
				(A) To be complied with <fo:inline font-weight="bold">before</fo:inline> release from custody
				</fo:block>
				<xsl:for-each select="ord:Surety[@selected='true']">
					<fo:block>
						To provide 
						<xsl:value-of select="ord:Plural"/>
						in the sum of 
						<xsl:apply-templates select="ord:MonetaryValue"/>
						to secure the surrender of the <xsl:call-template name="nar:AppellantDefendant"/>
 to custody at the time and place 
						directed (recognisance(s) of 
						<xsl:value-of select="ord:Plural"/>
						endorsed on Form 5102D: 'Bail: recognisance of a surety')
					</fo:block>
				</xsl:for-each>
				<xsl:for-each select="ord:Passport[@selected='true' and (ord:Surrendered='true' or ord:Retained='true')]">
					<fo:block>
					Passport to be
					<xsl:if test="ord:Surrendered='true'"> surrendered to </xsl:if>
						<xsl:if test="ord:Retained = 'true'">
							<xsl:if test="ord:Surrendered='true'"> and </xsl:if>retained by </xsl:if>
					the police
				</fo:block>
				</xsl:for-each>
				<xsl:for-each select="ord:Security[@selected='true']">
					<fo:block>
					To provide a security in the sum of
					<xsl:apply-templates select="ord:MonetaryValue"/>
					to be deposited with the court.
				</fo:block>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="nar:PostConditions">
		<!-- Bail Order Post Conditions -->
		<xsl:if test="/ord:Order/ord:OrderData/ord:BailOrder">
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:PostConditions[@selected='true']">
				<fo:block space-after="12pt">
				(B) To be complied with <fo:inline font-weight="bold">after</fo:inline> release from custody
			</fo:block>
				<xsl:for-each select="ord:LiveSleep[@selected='true']">
					<fo:block>
					To live and sleep each night
				<xsl:choose>
							<xsl:when test="ord:AtFollowingAddress='false'">
					at above address</xsl:when>
							<xsl:otherwise>
						at: 
						<xsl:for-each select="./ord:Address">
							<xsl:call-template name="CallableAddress"/>
						</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>.
				</fo:block>
				</xsl:for-each>
				<xsl:if test="ord:NotifyPolice/@selected='true'">To notify police of any change of address.	</xsl:if>
				<xsl:for-each select="ord:Curfew[@selected='true']">
					<fo:block>
					To observe the curfew between the hours of 
					<xsl:value-of select="ord:From"/> and <xsl:value-of select="ord:To"/>
					</fo:block>
				</xsl:for-each>
				<xsl:for-each select="ord:PoliceReport[@selected='true']">
					<fo:block>
					To report to: 
					<xsl:value-of select="ord:Station"/>
					Police Station each: 
					<xsl:value-of select="ord:Period"/>
					between 
					<xsl:value-of select="ord:From"/> and <xsl:value-of select="ord:To"/>.
				</fo:block>
				</xsl:for-each>
				<fo:block>
					<xsl:if test="ord:Available/@selected='true'">To be available as and when required to enable 	enquiries or reports to be made.</xsl:if>
				</fo:block>
				<fo:block>
					<xsl:if test="ord:Contact/@selected='true'">Not to contact directly or indirectly: <xsl:value-of select="ord:Contact"/>.</xsl:if>
				</fo:block>
				<xsl:for-each select="ord:Distance[@selected='true']">
					<fo:block>
					Not to come within <xsl:value-of select="ord:TheDistance"/> of <xsl:value-of select="ord:Location"/> except to see a solicitor by prior written appointment.
				</fo:block>
				</xsl:for-each>
				<fo:block>
					<xsl:if test="ord:Other[@selected='true']">Other <fo:inline font-style="italic">(please say here):</fo:inline>
						<xsl:value-of select="ord:Other"/>.</xsl:if>
				</fo:block>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:Qualifier">
		<xsl:choose>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:OffenceDate/@Qualifier='on'">
				on
			</xsl:when>
			<xsl:when test="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:OffenceDate/@Qualifier='on or before'">
				no later than
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="nar:Reasons">
    		<xsl:call-template name="FormatTextArea">
        		<xsl:with-param name="string" select="/ord:Order/ord:OrderData/*/ord:Reasons"/>
    		</xsl:call-template>
	</xsl:template>

	<xsl:template match="nar:ReleaseOnBail">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:Release='Conditional'">
				release
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
					<xsl:call-template name="nar:MaleFemale"/>
				</xsl:for-each> 
				on bail 
				<xsl:choose>
					<xsl:when test="/ord:Order/ord:OrderData/*/ord:PreConditions[@selected='true'] or /ord:Order/ord:OrderData/*/ord:PostConditions[@selected='true']">
						subject to the following condition(s)	
					</xsl:when>
					<xsl:otherwise>
						unconditionally		
					</xsl:otherwise>
				</xsl:choose>
				<fo:block>
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:PreConditions[@selected='true']">
						A. To be complied with <fo:inline font-weight="bold">before</fo:inline> release on bail: to provide
						<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:PreConditions/ord:Surety/ord:Plural"/>
						in the sum of 
						<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:PreConditions/ord:Surety">
							<xsl:apply-templates select="ord:MonetaryValue"/>
						</xsl:for-each>
						to secure the surrender of the defendant to custody at the time and place directed<br/>
					</xsl:if>
				</fo:block>
				
				<xsl:if test="/ord:Order/ord:OrderData/*/ord:PreConditions[@selected='true'] and /ord:Order/ord:OrderData/*/ord:PostConditions[@selected='true']">and <br/></xsl:if>
				
				<fo:block>
					<xsl:if test="/ord:Order/ord:OrderData/*/ord:PostConditions[@selected='true']">
						B. To be complied with <fo:inline font-weight="bold">after</fo:inline> release on bail:
						<fo:block/>
						<xsl:call-template name="FormatTextArea">
        						<xsl:with-param name="string" select="/ord:Order/ord:OrderData/*/ord:PostConditions/ord:PostConditionDetails"/>
    						</xsl:call-template>
					</xsl:if>
				</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:ReportIndicator">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:ReportDetails[@selected='true']">
			for a report on <fo:block/>
			<xsl:call-template name="FormatTextArea">
				<xsl:with-param name="string" select="/ord:Order/ord:OrderData/*/ord:ReportDetails"/>
    			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:ReturnPeriod">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:ReturnPeriod/ord:Max116='yes'">
		the maximum period specified by section 116.
		</xsl:if>
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:ReturnPeriod/ord:Max116='no'">
			<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:ReturnPeriod/ord:PeriodInMonths"/> months.
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:RevokeOption">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:OriginalOrderRevoked/@selected='true' and /ord:Order/ord:OrderData/*/ord:Breach/@selected='true'">
			this court was satisfied that it was in the interests of justice to revoke the order and
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:RtnYOIPrison">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment">
			<xsl:choose>
				<xsl:when test="ord:ReturnPeriod/@DetentionType='Detention'">
				a young offender institution
				</xsl:when>
				<xsl:when test="ord:ReturnPeriod/@DetentionType='Imprisonment'">
				prison
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:Section44">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:ExtendedSentence/ord:Section44/@selected='true'">
			<fo:block>
				The provisions of section 44 of the Criminal Justice Act 1991, as substituted by section 59 of the Crime and Disorder Act 1998 (as amended by paragraph 141 of Schedule 9 to the Powers of Criminal Courts (Sentencing) Act 2000) apply in this case.
			</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:SentenceOption">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:TermType/@selected='true'">
			<fo:block>
				This sentence was ordered to be
				<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:TermType">
					<xsl:call-template name="nar:TermType"/>
				</xsl:for-each>
				 to any other periods of imprisonment to which the defendant was subject prior to making of this order.
			</fo:block>				 
		</xsl:if>
	</xsl:template>

	<xsl:template match="nar:SentencingDate">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:SentencingCourt/ord:Date"/>
	</xsl:template>
	
	<xsl:template match="nar:SentencingCourtName">
		<xsl:value-of select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:SentencingCourt/ord:CourtHouse/ord:CourtHouseName"/>
	</xsl:template>
	
	<xsl:template match="nar:SentencingCourtType">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:SentencingCourt/ord:CourtHouse">
			<xsl:call-template name="nar:CourtHouseType"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:ServedPeriod">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType='Before'">
			before
		</xsl:if>	
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType='Concurrent'">
			concurrently with
		</xsl:if>	
	</xsl:template>
	
	<xsl:template match="nar:TotalPeriodOfReturn">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:ReturnToImprisonment">
			<xsl:if test="ord:TotalPeriodOfReturn/@selected='true'">
			<fo:block>
			The total of the period of return
			<xsl:if test="ord:TotalPeriodOfReturn/@IncludesNewOffenceTerm='yes'">
				and of any custodial term for a new offence
			</xsl:if>
			is <xsl:value-of select="ord:TotalPeriodOfReturn"/> months and because this total period of imprisonment is 12 months or less, Section 40A of the Criminal Justice Act 1991, as substituted by section 116 of the Powers of Criminal Courts (Sentencing) Act 2000, applies.
			</fo:block>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:VoluntaryBillOfIndictment">
		<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:RemandReason/ord:VoluntaryBillOfIndictment[@selected='true']">
			on a Voluntary Bill of Indictment dated <xsl:value-of select="."/><br/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:YOISentenceOption">
		<xsl:if test="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:TermType/@selected='true'">
			<fo:block space-after="12pt"/>
			This sentence was ordered to be
			<xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:TermType">
				<xsl:call-template name="nar:TermType"/>
			</xsl:for-each>
			 to any other periods of
			 <xsl:for-each select="/ord:Order/ord:OrderData/*/ord:CustodialSentence/ord:DetentionOrImprisonment">
				<xsl:choose>
					<xsl:when test=".='Detention'">
					detention
					</xsl:when>
					<xsl:when test=".='Imprisonment'">
					imprisonment
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
			 the defendant was subject prior to making of this order.
		</xsl:if>
	</xsl:template>

<!-- Text Formatting -->

	<xsl:template match="nar:Body">
			<xsl:apply-templates select="nar:Section|nar:If"/>
	</xsl:template>

	<xsl:template match="nar:H1">
		<!--xsl:copy-->
		<fo:block text-indent="0.0em" space-after="12pt" font-size="20pt">
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
		<!--/xsl:copy-->
	</xsl:template>
	
	<xsl:template match="nar:Line">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>

	<xsl:template match="nar:List">
		<xsl:for-each select="nar:ListItem">
			<fo:list-block start-indent="80mm" provisional-distance-between-starts="5mm">
				<fo:list-item space-after="1em">
					<fo:list-item-label>
						<fo:block font-weight="bold">
					&#x2022;
				</fo:block>
					</fo:list-item-label>
					<fo:list-item-body start-indent="body-start()" end-indent="5mm">
						<fo:block>
							<xsl:apply-templates select="*|text()"/>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
			</fo:list-block>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="nar:P">
		<!--xsl:copy-->
		<fo:block space-after="12pt">
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
		<!--/xsl:copy-->
	</xsl:template>
	
	<xsl:template match="nar:Section">
		<fo:list-block start-indent="3mm" provisional-distance-between-starts="60mm">
			<fo:list-item space-after="1em">
				<fo:list-item-label>
					<fo:block font-weight="bold">
						<xsl:apply-templates select="nar:Label"/>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()" end-indent="3mm">
					<fo:block>
						<xsl:apply-templates select="nar:Body"/>
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
		</fo:list-block>
		<fo:block space-after="14pt" text-align="right">
			<fo:leader leader-length="70%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>

	<xsl:template match="nar:Section/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
			<!--hr class="section-body-hrule"/-->
		</fo:block>
	</xsl:template>
</xsl:stylesheet>
