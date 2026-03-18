<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ************************************ -->
	<!-- COMMON NARRATIVE START -->
	<!-- ************************************ -->
	<!-- Common Template used to display Defendant Name -->
	<xsl:template match="nar:DefendantFullNameText">
		<fo:inline>
			<xsl:text>Defendants Name : </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- S.Bachra 17/4/03 Handling of no Title, Suffix (Tracker 52676) -->
	<xsl:template match="nar:DefendantFullName">
		<fo:inline>
			<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Name">
				<xsl:if test="count(apd:CitizenNameTitle) !=0">
					<xsl:value-of select="apd:CitizenNameTitle"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="apd:CitizenNameForename[1]">
					<xsl:value-of select="apd:CitizenNameForename[1]"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="apd:CitizenNameForename[2] and apd:CitizenNameForename[2] !=' '">
					<xsl:value-of select="apd:CitizenNameForename[2]"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:value-of select="apd:CitizenNameSurname"/>
				<xsl:text> </xsl:text>
				<xsl:if test="count(apd:CitizenNameSuffix) !=0">
					<xsl:value-of select="apd:CitizenNameSuffix"/>
					<xsl:text> </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<!-- Common Template used to display defendant address -->
	<xsl:template match="nar:DefendantAddressText">
		<fo:inline>
			<xsl:text>Defendants Address</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:DefendantAddress">
		<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
			<xsl:call-template name="CallableAddress"/>
		</xsl:for-each>
	</xsl:template>
	<!-- Date of birth text -->
	<xsl:template match="nar:DOBText">
		<fo:inline>
			<xsl:text>Date of birth</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Common Template used to display Defendant Date Of Birth -->
	<xsl:template match="nar:DefendantDOB">
		<fo:inline>
			<xsl:if test="$DefDOB">
				<xsl:call-template name="FormatDate">
					<xsl:with-param name="date" select="$DefDOB"/>
				</xsl:call-template>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Common Display Order Date -->
	<xsl:template match="nar:OrderDate">
		<fo:inline>
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$OrderDate"/>
			</xsl:call-template>
		</fo:inline>
	</xsl:template>
	<!-- Common Template to display next appearance details -->
	<xsl:template match="nar:NextAppearanceDate">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:NextAppearance/ord:AppearanceDateTime/@selected='true'">
                on: 
                <xsl:call-template name="FormatDate">
					<xsl:with-param name="date" select="$baseAll/ord:NextAppearance/ord:AppearanceDateTime/ord:AppearanceDate"/>
				</xsl:call-template>
                at 
                <xsl:call-template name="FormatTime">
					<xsl:with-param name="time" select="$baseAll/ord:NextAppearance/ord:AppearanceDateTime/ord:AppearanceTime"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="DateTimeNotified"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Common Template to display signed information -->
	<xsl:template match="nar:SignedInfo">
		<xsl:call-template name="SignedInfo"/>
	</xsl:template>
	<!-- Template to display Associated Case details -->
	<!-- Display 'None' if no Associated Cases have been selected -->
	<xsl:template match="nar:AssociatedCases">
		<fo:block>
			<fo:inline>Associated Cases:<xsl:text> </xsl:text>
			</fo:inline>
			<xsl:choose>
				<xsl:when test="count($baseAll/ord:AssociatedCases/ord:AssociatedCase[@selected='true'])='0'">
                    None
                </xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$baseAll/ord:AssociatedCases/ord:AssociatedCase[@selected='true']">
						<fo:block>
							<xsl:value-of select="."/>
						</fo:block>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</fo:block>
	</xsl:template>
	<!-- Display correct deportation text depend on which reason was seleted been selected -->
	<xsl:template match="nar:DeportationSection">
		<fo:block>
			<fo:inline>Deportation Reason:<xsl:text> </xsl:text>
			</fo:inline>
			<xsl:choose>
				<xsl:when test="($baseAll/ord:DeportationSection/ord:DeportationReasons/ord:DeportationReason)='custodial'">
                    Custodial
                </xsl:when>
				<xsl:when test="($baseAll/ord:DeportationSection/ord:DeportationReasons/ord:DeportationReason)='suspended'">
                    Suspended
                </xsl:when>
				<xsl:when test="($baseAll/ord:DeportationSection/ord:DeportationReasons/ord:DeportationReason)='seriousDrugOffence'">
                    Recommended fo Deportation
                </xsl:when>
				<xsl:when test="($baseAll/ord:DeportationSection/ord:DeportationReasons/ord:DeportationReason)='recommendedDeportation'">
                    Serious Drug Offence
                </xsl:when>
            </xsl:choose>
        </fo:block>
    </xsl:template>
    <!-- Of text -->
    <xsl:template match="nar:Of">
        <fo:inline>
            <xsl:text>of: </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template used to display next appearance court -->
    <xsl:template match="nar:NextAppearanceCourtHouseName">
        <xsl:call-template name="CallableNextAppearanceCourtHouseName"/>
    </xsl:template>
    <!-- Template used to display himself or herself depending on gender -->
    <xsl:template match="nar:HimHer">
        <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
            <xsl:call-template name="HimselfHerself"/>
        </xsl:for-each>
    </xsl:template>
    <!-- On text -->
    <xsl:template match="nar:On">
        <fo:inline>
            <xsl:text>on </xsl:text>
        </fo:inline>
    </xsl:template>
	<!-- At text -->
	<xsl:template match="nar:At">
        <fo:inline>
            <xsl:text>at</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- And text -->
    <xsl:template match="nar:And">
        <fo:inline>
            <xsl:text>and </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- The defendant -->
    <xsl:template match="nar:TheDefendant">
        <fo:inline>
            <xsl:text>The defendant</xsl:text>
        </fo:inline>
    </xsl:template>
	 <!-- The defendant -->
    <xsl:template match="nar:Warning">
        <fo:inline>
            <xsl:text>Warning</xsl:text>
        </fo:inline>
    </xsl:template>
	 <!-- The defendant -->
    <xsl:template match="nar:Notice">
        <fo:inline>
            <xsl:text>Notice</xsl:text>
		 </fo:inline>
    </xsl:template>
	<!-- The Summons -->
	<xsl:template match="nar:Summons">
        <fo:inline>
            <xsl:text>Summons</xsl:text>
        </fo:inline>
    </xsl:template>
	<!-- For Attention -->
	<xsl:template match="nar:ForAttention">
        <fo:inline>
            <xsl:text>For Attention</xsl:text>
        </fo:inline>
    </xsl:template>
	<!-- Order of Court-->
	<xsl:template match="nar:OrderofCourt">
        <fo:inline>
            <xsl:text>Order of this Court</xsl:text>
        </fo:inline>
    </xsl:template>
	<!-- OriginalSentence -->
	<xsl:template match="nar:OriginalSentence">
        <fo:inline>
            <xsl:text>Original Sentence</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Was text -->
    <xsl:template match="nar:Was">
        <fo:inline>
            <xsl:text>was</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- By the text -->
    <xsl:template match="nar:ByThe">
        <fo:inline>
            <xsl:text>by the </xsl:text>
        </fo:inline>
    </xsl:template>
		<!-- Colon text -->
	<xsl:template match="nar:colon">
		<fo:inline>
			<xsl:text> : </xsl:text>
		</fo:inline>
	</xsl:template>
    <!-- ******************************** -->
    <!-- COMMON NARRATIVE END -->
    <!-- ******************************** -->
    <!-- ************************* NARRATIVE START ************************* -->
    <!-- Conditional text display -->
    <xsl:template match="nar:If">
        <xsl:choose>
            <xsl:when test="nar:Condition='ConditionalBail' and $baseAll/ord:BailDecision = 'Conditional'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
			<xsl:when test="nar:Condition='TrailMonitoring' and $baseAll/ord:DTOrderRequirements/ord:TrailMonitoringRequirement/@selected = 'true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
       
            <xsl:when test="nar:Condition='ReportIndicator' and $baseAll/ord:ReportDetails[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='RemandIsBefore' and $baseAll/ord:RemandReason[@selected='true'] and $baseAll/ord:RemandReason/ord:IsBefore[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='RemandCommitted' and $baseAll/ord:RemandReason[@selected='true'] and $baseAll/ord:RemandReason/ord:IsBefore[@selected='false']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='ChapterIII' and $baseAll/ord:ChapterIII='yes'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <!-- Added for RFC 1344 START $$$$$$$$$-->
            <xsl:when test="nar:Condition='SeriousOffence' and $baseAll/ord:CJA2003RequiredCustodial/@selected='true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='DrugTrafficking' and $baseAll/ord:ClassAtrafficking/@selected='true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='DomesticBurglary' and $baseAll/ord:DomesticBurglary/@selected='true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='MinimumSentence' and $baseAll/ord:MinimumLifeTerm/@selected='true' and $baseAll/ord:CustodialSentence/ord:ImprisonmentType='life'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <!-- Added for RFC 1344 END $$$$$$$$$-->
            <xsl:when test="nar:Condition='ExtendedSentence' and $baseAll/ord:CustodialSentence/ord:ExtendedSentence/@selected='true' and $baseAll/ord:CustodialSentence/ord:ImprisonmentType='period'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='ExtendedSentence' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:ImprisonmentType='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType='section91term' and $baseAll/ord:CustodialSentence/ord:ExtendedSentence/@selected='true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='ReturnOfDefendant' and $baseAll/ord:ReturnToImprisonment[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='DiscretionaryLife' and $baseAll/ord:Section28[@selected='true'] and ($baseAll/ord:CustodialSentence/ord:ImprisonmentType='life' or       $baseAll/ord:CustodialSentence/ord:ImprisonmentType='hmpleasure' or ($baseAll/ord:CustodialSentence/ord:ImprisonmentType='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType='section91life'))">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='DiscretionaryLife' and $baseAll/ord:Section28[@selected='true'] and $baseAll/ord:CustodialSentence/ord:ImprisonmentType='section9394'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='Section86' and $baseAll/ord:Section86='yes'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='AdditionalNotes' and $baseAll/ord:AdditionalNotes[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <!--ADDED FOR CCN400 - START -->
            <xsl:when test="nar:Condition='DeportationSection' and $baseAll/ord:DeportationSection[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <!-- ADDED FOR CCN400 - END -->
            <xsl:when test="nar:Condition='RemandAdditionalInfo' and $baseAll/ord:AdditionalInfo/@selected='true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='CreditForRemand' and $baseAll/ord:CreditForRemand[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='CreditForBail' and $baseAll/ord:CreditForBail[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='IndeterminateSentence' and $baseAll/ord:IndeterminateSentence[@selected='true']">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='AutomaticDeportation' and $baseAll/ord:AutomaticDeportation='yes'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='RecommendedDeportation' and $baseAll/ord:RecommendedDeportation='yes'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
			<xsl:when test="nar:Condition='RemandRequirementsOfCourt' and $baseAll/ord:RemandOrderType/ord:RequirementsOnLocalAuthority/@selected='true' and $baseAll/ord:RemandOrderType/ord:RemandOrdType='localauthority'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='RemandConditionsOnDefendant' and $baseAll/ord:RemandOrderType/ord:ConditionsOnDefendant/@selected='true' and $baseAll/ord:RemandOrderType/ord:RemandOrdType='localauthority'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
            <xsl:when test="nar:Condition='RemandChargedWith' and $baseAll/ord:ChargedWith/@selected='true'">
                <xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
            </xsl:when>
			<xsl:when test="nar:Condition='RemandReasonsRequirements'">
				<xsl:if test="$baseAll/ord:ReasonsForRemand/@selected='true'">
					<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:SeriousnessForRemand/@selected='true' or $baseAll/ord:ReasonsForRemand/ord:RemandHistory/@selected='true' or $baseAll/ord:ReasonsForRemand/ord:NecessityCondition/@selected='true' or $baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/@selected='true'">
						<xsl:apply-templates select="nar:Body/* | nar:Body/text()"/>
					</xsl:if>	
				</xsl:if>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Template used to display court type -->
    <xsl:template name="PrecedingCourtHouseType">
        <xsl:choose>
            <xsl:when test="./ord:CourtHouseType=$CrownCourt"> Crown Court</xsl:when>
            <xsl:otherwise> unspecified court</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Common Template to display Term Details -->
    <xsl:template name="Term">
        <xsl:value-of select="./ord:Years"/> year(s) <xsl:value-of select="./ord:Months"/> month(s)
    </xsl:template>
    <!-- ************************* NARRATIVE END ************************* -->
    <!-- ************************* PAGE SET UP START ************************* -->
    <!-- Text Formatting -->
    <xsl:template match="nar:Body">
        <xsl:apply-templates select="nar:Section|nar:D20Section|nar:D20HeaderSection|nar:D20AppealSection|nar:D20FooterSection|nar:If|nar:Table"/>
	</xsl:template>
	<xsl:template name="col_one">
        <xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:Section/nar:Body/nar:DTable/nar:TR/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<!-- S.Bachra 28/4/03 Used for Header Section column two, DateOfBirth details (Tracker 52711) -->
	<xsl:template name="col_two">
        <xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:Section/nar:Body/nar:DTable/nar:TR/nar:TD[2]">
			<xsl:apply-templates select="*|text()"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="nar:H1">
		<!--xsl:copy-->
		<fo:block text-indent="0.0em" space-after="12pt" font-size="20pt">
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
		<!--/xsl:copy-->
	</xsl:template>
	<xsl:template match="nar:H2">
		<!--xsl:copy-->
		<fo:block text-indent="0.0em" space-after="12pt" font-size="15pt" font-weight="bold">
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
		<!--/xsl:copy-->
	</xsl:template>
	<xsl:template match="nar:H3">
		<!--xsl:copy-->
		<fo:block text-indent="0.0em" space-after="8pt" font-size="12pt" font-weight="bold">
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
			<fo:list-block provisional-distance-between-starts="5mm">
				<!-- 80 -->
				<fo:list-item space-after="1em">
					<fo:list-item-label>
						<fo:block font-weight="bold">
							<xsl:text>&#x2219;</xsl:text>
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
	<!-- S.Bachra 28/4/03 Used to display header information correctly -->
	<!-- Used by DTable to display text not required inside table (Tracker 52711) -->
	<xsl:template name="RestOfText">
		<fo:block>
            <xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:Section/nar:Body/nar:DTable/nar:TR/nar:TD[1]">
				<!-- only display lines other than 1 and 2 -->
				<xsl:for-each select="./nar:Line[3] | ./nar:Line[4] | ./nar:Line[5] | ./nar:Line[6] | ./nar:Line[7]">
					<xsl:apply-templates select="*|text()"/>
				</xsl:for-each>
			</xsl:for-each>
		</fo:block>
	</xsl:template>
	
    <xsl:template match="nar:Section/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:Section">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="proportional-column-width(1)"/>
			<fo:table-column column-width="proportional-column-width(2)"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-page="always">
					<fo:table-cell border="1mm">
						<fo:block>
							<fo:block font-weight="bold">
								<xsl:apply-templates select="nar:Label"/>
							</fo:block>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell start-indent="1mm" end-indent="3mm" border="1mm">
						<fo:block>
							<xsl:apply-templates select="nar:Body"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<fo:block space-after="14pt" text-align="right">
			<fo:leader leader-length="70%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>
    <xsl:template match="nar:Section/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>
	<!-- S.Bachra 28/4/03 Used for setting up table to display defendant details (Tracker 52711)-->
	<xsl:template match="nar:DTable">
		<fo:table table-layout="fixed">
			<!--165 and 70  -->
			<fo:table-column column-width="90mm"/>
			<fo:table-column column-width="40mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="col_one"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="col_two"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- Call template to display rest of the text in the first section -->
		<xsl:call-template name="RestOfText"/>
	</xsl:template>
	<!-- Risk or Vulnerability -->
	<xsl:template match="nar:RiskOrVulnerability">
		<fo:inline>
			<xsl:if test="$baseAll/ord:RiskVulnerabilityFactors/@selected = 'true'">
				<!-- xsl:text>Risk or Vulnerability factors if known to the court</xsl:text -->
				<fo:block>Risk or Vulnerability factors</fo:block>
				<fo:block>if known to the court</fo:block>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	
	<!-- ************************* PAGE SET UP END ************************* -->
</xsl:stylesheet>
