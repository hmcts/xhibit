<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- Import Order Specific transformers START -->
	<xsl:import href="BC_Transform.xslt"/>
	<xsl:import href="BW_Transform.xslt"/>
	<xsl:import href="RC_Transform.xslt"/>
	<xsl:import href="IMP_Transform.xslt"/>
	<xsl:import href="IMP5035C_Transform.xslt"/>
	<xsl:import href="IMP5035C_Transform_pre_LASBO.xslt"/>
	<xsl:import href="CPO_Transform.xslt"/>
	<xsl:import href="CRO_Transform.xslt"/>
	<xsl:import href="CPRO_Transform.xslt"/>
	<xsl:import href="COMY_Transform.xslt"/>
	<xsl:import href="YO5044_Transform.xslt"/>
	<xsl:import href="YO5044_Transform_pre_LASBO.xslt"/>
	<xsl:import href="COMSENT_Transform.xslt"/>
	<xsl:import href="COMSENT_Transform_pre_LASBO.xslt"/>
	<xsl:import href="SUS_Transform.xslt"/>
	<xsl:import href="SUS_Transform_pre_LASBO.xslt"/>
	<xsl:import href="BW_AF_Transform.xslt"/>
	<xsl:import href="DTO_Transform.xslt"/>
	<xsl:import href="YRO_Transform.xslt"/>
	<xsl:import href="RPO_Transform.xslt"/>
	<xsl:import href="NAO_Transform.xslt"/>
	<!-- Import Order Specific transformers END -->
	<!-- Import Common transformers START -->
	<xsl:import href="OrdersCommonTransform.xslt"/>
	<xsl:import href="NarrativeTransform.xslt"/>
	<xsl:import href="IMP_COMY_CommonTransform.xslt"/>
	<xsl:import href="IMP_COMY_CommonTransform_pre_LASBO.xslt"/>
	<xsl:import href="CPO_CRO_CPRO_CommonTransform.xslt"/>
	<xsl:import href="MO_Transform.xslt"/>
	<xsl:import href="D20_Transform.xslt"/>
	<xsl:import href="NoticeOfDefermentSentence_Transform.xslt"/>
	<xsl:import href="BreachSuspendedSentence_Transform.xslt"/>
    <xsl:import href="ActionConditionalDischarge_Transform.xslt"/>
	<xsl:import href="NoticeBreachSuspendedSentence_Transform.xslt"/>
	<xsl:import href="BreachConditionalDischarge_Transform.xslt"/>

	<!-- Import Common transformers END -->
	<xsl:output method="xml" version="1.0" indent="yes"/>

	<!-- **************************** Global parameters START ****************************-->
	<xsl:param name="mode" select="'display'"/>
	<xsl:param name="resource" select="'file:///d:/projects/XHIBIT/thickclient/orders/resources'"/>
	<xsl:param name="base" select="/ord:Order/ord:OrderData"/>
	<xsl:param name="baseAll" select="$base/*"/>
	<xsl:param name="MagCourt">Magistrates Court</xsl:param>
	<xsl:param name="CrownCourt">Crown Court</xsl:param>
	<xsl:param name="YouthCourt">Youth Court</xsl:param>
	<xsl:param name="OrderDate" select="$baseAll/ord:OrderHeader/ord:OrderDate"/>
	<xsl:param name="CaseNo" select="$baseAll/ord:OrderHeader/ord:CaseNumber"/>
	<xsl:param name="DefDOB" select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:DateOfBirth/apd:BirthDate"/>
	<xsl:param name="DefAddress" select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address"/>
	<xsl:param name="SignedDate" select="$baseAll/ord:OrderHeader/ord:SignedDate"/>
	<xsl:param name="OriginalOrder" select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:OriginalOrderType"/>
	<!-- ******* basedir param added back in as this is set from the client ********-->
	<xsl:param name="basedir"/>
	<!-- **************************** Global parameters END ****************************-->
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- Call template to set up the page -->
			<xsl:call-template name="PageSetUp"/>
			<fo:page-sequence master-reference="A4">
				<fo:static-content flow-name="footer-first">
					<!-- S.Bachra 2/5/03 No longer use footer section - Signed section added to main body of the order -->
					<!-- SCR ref: 52874 / 52858 / 52875 -->
					<fo:block/>
				</fo:static-content>
				<fo:static-content flow-name="header-first">
					<!-- For D20 add an extra header section -->
					<xsl:if test="/ord:Order/ord:OrderData/ord:D20">
						<fo:table table-layout="fixed">
							<fo:table-column column-width="35mm"/>
							<fo:table-column column-width="135mm"/>
							<fo:table-column column-width="20mm"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell text-align="left">
										<fo:block text-align="right">
											<fo:inline>
												<fo:external-graphic>
													<xsl:attribute name="src"><xsl:value-of select="concat($basedir, 	'/images/DVLA_HisMajesty.jpg')"/></xsl:attribute>
													<xsl:attribute name="height">2.78cm</xsl:attribute>
													<xsl:attribute name="width">3.47cm</xsl:attribute>
													<xsl:attribute name="content-width">scale-to-fit</xsl:attribute>
												</fo:external-graphic>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" display-align="after" >
										<fo:block text-align="center" font-weight="bold" font-size="18pt">
											Notice by Court of Order for Endorsement
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="right" display-align="after" >
										<fo:block text-align="right" font-weight="bold" font-size="12pt">
											D20
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<!-- This fo:block is the section seperator -->
						<fo:block space-after="14pt" text-align="center">
							<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
						</fo:block>
					</xsl:if>
					<fo:table table-layout="fixed">
						<fo:table-column column-width="145mm"/>
						<fo:table-column column-width="45mm"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="left">
									<fo:block text-align="left" font-weight="500" font-size="18pt">
										<!-- S.Bachra 22/4/03 Change to standard text In the CourtType (Tracker 52712) -->
										<xsl:if test="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType=	$MagCourt">
		                                        In the Magistrates Court
		                                    </xsl:if>
										<xsl:if test="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType=	$CrownCourt">
		                                        In the Crown Court
		                                    </xsl:if>
									</fo:block>
									<fo:block space-after="12pt" font-size="16pt" font-weight="500">
		                                    at <xsl:value-of select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseName"/>
		                                    <xsl:if test="/ord:Order/ord:OrderData/ord:D20">
												 0<xsl:value-of select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseCode"/>
											</xsl:if>
										<!-- S.Bachra 22/4/03 Display the OrderDate/Warrant Date if Bench Warrant Order 	(Tracker 		52693) -->
										<xsl:if test="/ord:Order/ord:OrderData/ord:BenchWarrant">
											<fo:block space-after="12pt" font-size="16pt" font-weight="500">
		                                            on 
		                                            <xsl:call-template name="FormatDate">
													<xsl:with-param name="date" select="$OrderDate"/>
												</xsl:call-template>
											</fo:block>
										</xsl:if>
										<!-- New D20 order needs court house address; 31/05/2015 -->
										<xsl:if test="/ord:Order/ord:OrderData/ord:D20">
											<fo:block space-after="12pt" font-size="12pt" font-weight="500">
												<xsl:for-each select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseAddress">
													<xsl:call-template name="CallableBailAddress"/>
												</xsl:for-each>
												<fo:block padding="5mm">
													<fo:inline font-weight="bold">Case Number:</fo:inline> <xsl:value-of select="$CaseNo"/>
												</fo:block>
											</fo:block>
										</xsl:if>
			 						</fo:block>
									<xsl:if test="not(ord:Order/ord:OrderData/ord:D20)">
										<fo:block>
												Case No: <xsl:value-of select="$CaseNo"/>
										</fo:block>
										<fo:block>
												Court Code: <xsl:value-of select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseCode"/>
										</fo:block>
									</xsl:if>
									<!-- S.Bachra 23/5/3 Display PTI Urn If Imprisonment or YOI Order Tracker 53251 -->
									<xsl:if test="$base/ord:ImprisonmentOrder 
												or $base/ord:ImprisonmentOrder5035C
												or $base/ord:BenchWarrant
												or $base/ord:YOIOrder 
												or $base/ord:YoungOffendersOrder5044
												or $base/ord:SUSOrder
												or $base/ord:COOrder
												or $base/ord:YouthRehabilitationOrder
												or $base/ord:DetentionAndTrainingOrder
											    or $base/ord:BreachSuspendedSentenceOrder	
												or $base/ord:NoticeOfDefermentSentenceOrder
												or $base/ord:NoticeBreachSuspendedSentence
												or $base/ord:ActionConditionalDischargeOrder
 												or $base/ord:BreachConditionalDischargeOrder">
										<fo:block>
		                                        PTI URN: <xsl:value-of select="$baseAll/ord:OrderHeader/ord:Defendant/ord:URN"/>
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<xsl:choose>
									<xsl:when test="/ord:Order/ord:OrderData/ord:D20">
										<!--<fo:table-cell text-align="right" height="3.47cm">
											<fo:block text-align="right">
												<fo:inline>
													<fo:external-graphic>
														<xsl:attribute name="src"><xsl:value-of select="concat($basedir, 	'/images/DVLA_HisMajesty.jpg')"/></xsl:attribute>
														<xsl:attribute name="height">2.78cm</xsl:attribute>
														<xsl:attribute name="width">3.47cm</xsl:attribute>
													</fo:external-graphic>
												</fo:inline>
											</fo:block>
										</fo:table-cell>-->
									</xsl:when>
									<xsl:otherwise>
										<fo:table-cell text-align="right" height="4.5cm">
											<fo:block text-align="right">
												<fo:inline>
													<fo:external-graphic>
														<xsl:attribute name="src"><xsl:value-of select="concat($basedir, 	'/images/Crown_Logo_Updated_2025.gif')"/></xsl:attribute>
														<xsl:attribute name="height">3.5cm</xsl:attribute>
														<xsl:attribute name="width">4.5cm</xsl:attribute>
														<xsl:attribute name="content-width">scale-to-fit</xsl:attribute>
													</fo:external-graphic>
												</fo:inline>
											</fo:block>
										</fo:table-cell>
									</xsl:otherwise>
								</xsl:choose>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<!-- B.Hingston - Display addresee section if Remand Order - LASPO - L-R-4224-01 --> 
					<xsl:if test="$base/ord:RemandOrder">
						<fo:block>
							<xsl:choose>
								<xsl:when test="($base/ord:RemandOrder/ord:RemandOrderType/ord:RemandOrdType)='standard'">
									<br/>
									<br/>
									To Governor HMP
									<br/>
									<br/>
								</xsl:when>
								<xsl:when test="($base/ord:RemandOrder/ord:RemandOrderType/ord:RemandOrdType)='localauthority'">
									<br/>
									<br/>
									To Conveyor / Custodian &amp; Local Authority
									<br/>
									<br/>
								</xsl:when>
								<xsl:when test="($base/ord:RemandOrder/ord:RemandOrderType/ord:RemandOrdType)='youthdetention'">
									<br/>
									<br/>
									To Conveyor / Custodian &amp; Manager of Youth Detention Accomodation
									<br/>
									<br/>
								</xsl:when>
							</xsl:choose>
							<br/>
						</fo:block>
					</xsl:if>
					<!-- B.Hingston - Display addresee section if Release from Prison / Notice of Aqquittal - L-R-4574-01 --> 
					<xsl:if test="$base/ord:ReleaseFromPrisonOrder or $base/ord:NoticeOfAcquittalOrder">
						<fo:block>
							<br/>
							<br/>
							The Governor
							<br/>
							<xsl:value-of select="$baseAll/ord:Governor"/>
							<br/>
							<br/>
							<br/>
						</fo:block>
					</xsl:if>
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
</xsl:stylesheet>
