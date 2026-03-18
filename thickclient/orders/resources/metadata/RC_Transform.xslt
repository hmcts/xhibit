<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ***************************** -->
    <!-- REMAND ORDER START -->
    <!-- *****************************  -->
    <!-- Remand title -->
    <xsl:template match="nar:RC_RemandTitle">
        <fo:inline>
			<xsl:choose>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='standard'">
                    <xsl:text>Remand Order</xsl:text>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='localauthority'">
                    <xsl:text>Remand Order -</xsl:text>
                    <xsl:text>Local Authority Accommodation</xsl:text>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='youthdetention'">
                    <xsl:text>Remand Order - </xsl:text>
                    <xsl:text>Youth Detention Accommodation</xsl:text>
                </xsl:when>
                <xsl:otherwise>
					<xsl:text>Remand Order</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- It was ordered -->
    <xsl:template match="nar:RC_ItWasOrdered">
        <fo:inline>
            <xsl:text>It was ordered</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Requirements of Court -->
    <xsl:template match="nar:RC_RequirementsOfCourt">
        <fo:inline>
            <xsl:text>Requirements of the Court on the Local Authority</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Content of Req of court on Local Authority -->
    <xsl:template match="nar:RC_ReqOnLocalAuthority">
        <xsl:value-of select="$baseAll/ord:RemandOrderType/ord:RequirementsOnLocalAuthority"/>
    </xsl:template>
    <!-- Conditions on Defendant -->
    <xsl:template match="nar:RC_ConditionsOnDefendant">
        <fo:inline>
            <xsl:text>Conditions on Defendant</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Content of Conditions on Defendant -->
    <xsl:template match="nar:RC_DefendantConditions">
        <xsl:value-of select="$baseAll/ord:RemandOrderType/ord:ConditionsOnDefendant"/>
    </xsl:template>
    <!-- That the defendant -->
    <xsl:template match="nar:RC_ThatTheDefendant">
        <fo:inline>
            <xsl:text>that the defendant </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Remanded -->
    <xsl:template match="nar:RC_Remanded">
        <fo:inline>
			<xsl:choose>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='standard'">
                    <xsl:text>be remanded in custody at </xsl:text>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='localauthority'">
                    <xsl:text>be remanded into the care of the Local Authority at </xsl:text>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='youthdetention'">
                    <xsl:text>be remanded into Youth Detention Accommodation at </xsl:text>
                </xsl:when>
            </xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Custody Location -->
    <xsl:template match="nar:RC_CustodyLocation">
        <xsl:value-of select="$baseAll/ord:CustodyLocation"/>
    </xsl:template>
    <!-- Template to display Report Indicator Details -->
    <xsl:template match="nar:RC_ReportIndicator">
        <xsl:if test="$baseAll/ord:ReportDetails[@selected='true']">
            for a report on 
            <xsl:call-template name="FormatTextArea">
                <xsl:with-param name="string" select="$baseAll/ord:ReportDetails"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    <!-- Template to display court text details -->
    <xsl:template match="nar:RC_CourtText">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:NextAppearance/ord:AppearanceCourt/ord:CourtHouseType=$CrownCourt">
                be brought before the <xsl:call-template name="CallableNextAppearanceCourtType"/> sitting at: <xsl:call-template name="CallableNextAppearanceCourtHouseName"/>
            </xsl:when>
            <xsl:otherwise>
                be brought before <xsl:call-template name="CallableNextAppearanceCourtHouseName"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Notified Text -->
    <xsl:template match="nar:RC_PlaceNotified">
        <fo:inline>
            <xsl:text>or any other place that may be notified.</xsl:text>
        </fo:inline>
    </xsl:template>
    <xsl:template match="nar:RC_DesignatedLA">
        <fo:inline>
            <xsl:choose>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='localauthority'">
                    <xsl:text>The designated Local Authority is </xsl:text>
                    <xsl:value-of select="$baseAll/ord:RemandOrderType/ord:LADesignatedLocalAuthority"/>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='youthdetention'">
                    <xsl:text>The designated Local Authority is </xsl:text>
                    <xsl:value-of select="$baseAll/ord:RemandOrderType/ord:YDDesignatedLocalAuthority"/>
                </xsl:when>
            </xsl:choose>
        </fo:inline>
    </xsl:template>
    <!-- Template to display commit sent details -->
    <xsl:template match="nar:RC_CommitSent">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:RemandReason/ord:CommitSent='committed'">
                committed
            </xsl:when>
            <xsl:when test="$baseAll/ord:RemandReason/ord:CommitSent='sent'">
                sent for trial
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- To Crown Court -->
    <xsl:template match="nar:RC_ToCrownCourt">
        <fo:inline>
            <xsl:text> to the Crown Court on </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display committed date -->
    <xsl:template match="nar:RC_CommittedDate">
        <xsl:call-template name="FormatDate">
            <xsl:with-param name="date" select="$baseAll/ord:RemandReason/ord:CrownCourt/ord:Date"/>
        </xsl:call-template>
    </xsl:template>
    <!-- Template to display Committing CourtHouse Name -->
    <xsl:template match="nar:RC_CommittingCourtHouseName">
        <xsl:value-of select="$baseAll/ord:RemandReason/ord:CrownCourt/ord:CourtHouse/ord:CourtHouseName"/>
    </xsl:template>
    <!-- Template to display Indicted Convicted details -->
    <xsl:template match="nar:RC_IndictedConvicted">
        and has been
        <xsl:choose>
            <xsl:when test="$baseAll/ord:RemandType='Indicted'">
                indicted for crime.
            </xsl:when>
            <xsl:when test="$baseAll/ord:RemandType='Convicted'">
                convicted of crime.
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Before the court -->
    <xsl:template match="nar:RC_BeforeTheCourt">
        <fo:inline>
            <xsl:text>is before the Court</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Voluntart Bill of Indictment Details -->
    <xsl:template match="nar:RC_VoluntaryBillOfIndictment">
        <xsl:for-each select="$baseAll/ord:RemandReason/ord:VoluntaryBillOfIndictment[@selected='true']">
            on a Voluntary Bill of Indictment dated 
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="."/>
            </xsl:call-template>
            <br/>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display Certificate of Transfer Details -->
    <xsl:template match="nar:RC_CertificateOfTransfer">
        <xsl:for-each select="$baseAll/ord:RemandReason/ord:CertificateOfTransfer[@selected='true']">
            on a Certificate of Transfer dated 
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="."/>
            </xsl:call-template>
            <br/>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display Appeal Against Bail Granted Details -->
    <xsl:template match="nar:RC_AppealAgainstBailGranted">
        <xsl:if test="$baseAll/ord:RemandReason/ord:AppealAgainstBailGranted[@selected='true']">
            as a result of an appeal by the Prosecution against the grant of bail<br/>
        </xsl:if>
    </xsl:template>
    <!-- BH 17/07/2014 New charged with section for LASBO changes -->
    <!-- Template to display charged with info details -->
    <xsl:template match="nar:RC_ChargedWith">
        <xsl:if test="$baseAll/ord:ChargedWith/@selected='true'">
            <fo:block>Charged With:</fo:block>
            <fo:block>
                <xsl:call-template name="FormatTextArea">
                    <xsl:with-param name="string" select="$baseAll/ord:ChargedWith"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- Template to display remand additional info details -->
    <xsl:template match="nar:RC_AdditionalInfo">
        <xsl:if test="$baseAll/ord:AdditionalInfo/@selected='true'">
            <fo:block>Additional Notes:</fo:block>
            <fo:block>
                <xsl:call-template name="FormatTextArea">
                    <xsl:with-param name="string" select="$baseAll/ord:AdditionalInfo"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
    </xsl:template>
    	<!-- BH 03/07/2014 - New addressee section added for LASBO changes -->
	<xsl:template match="nar:RC_Addressee">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='standard'">
                    <xsl:text>To Governor HMP</xsl:text>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='localauthority'">
                    <xsl:text>To Conveyor / Custodian &amp; Local Authority</xsl:text>
                </xsl:when>
				<xsl:when test="($baseAll/ord:RemandOrderType/ord:RemandOrdType)='youthdetention'">
                    <xsl:text>To Conveyor / Custodian &amp; Manager of Youth Detention Accommodation</xsl:text>
                </xsl:when>
            </xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Seriousness -->
	<xsl:template name="RC_Seriousness_Text">
		<fo:block>
			<xsl:text>Seriousness of offence:</xsl:text>
		</fo:block>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:SeriousnessForRemand/ord:SeriousnessOfOffence='violentOrSexualOffence'">
			<fo:block>
				<xsl:text>The offence must be violent or sexual offence.</xsl:text>
			</fo:block>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:SeriousnessForRemand/ord:SeriousnessOfOffence='lengthOfImprisonment'">
			<fo:block>	
				<xsl:text>An offence carrying 14 years' imprisionment or more in the case of an adult</xsl:text>
			</fo:block>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:SeriousnessForRemand/ord:LikelyConviction='true'">
			<fo:block>
				<xsl:text>and it is very likely that the offence would result in conviction and attract a custodial sentence.</xsl:text>
			</fo:block>
		</xsl:if>
		<fo:block/>
		<fo:block space-after="10pt"/>
    </xsl:template>
	<!-- History -->
	<xsl:template name="RC_History_Text">
		<fo:block>
			<xsl:text>History:</xsl:text>
		</fo:block>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:RemandHistory/ord:HistoryForRemand='historyOfAbsconding'">
			<fo:block>
				<xsl:text>The child has a recent and significant history of absconding while</xsl:text>
				<xsl:text> subject to a custodial remand and that history is relevant in all the </xsl:text>
				<xsl:text> circumstances of the case.</xsl:text>
			</fo:block>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:RemandHistory/ord:HistoryForRemand='historyOfOffencesOnBail'">
			<fo:block>
				<xsl:text>The child has recent and significant history of committing</xsl:text>
				<xsl:text> imprisonable offences while on bail or subject to a</xsl:text>
				<xsl:text> custodial remand and that history is relevant in all the circumstances of the case.</xsl:text>
			</fo:block>
		</xsl:if>	
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:RemandHistory/ord:LikelyCustodialSentence='true'">
			<fo:block>
				<xsl:text>and it is very likely that the offence would result in a conviction and attract a custodial sentence.</xsl:text>
			</fo:block>
		</xsl:if>
		<fo:block space-after="10pt"/>
    </xsl:template>
	<!-- Necessity -->
	<xsl:template name="RC_Necessity_Text">
		<fo:block>
			<xsl:text>Necessity condition:</xsl:text>
		</fo:block>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:NecessityCondition/ord:RiskOfHarm='true'">
			<fo:block>
				<xsl:text>The court has considered all other options but the child poses a risk</xsl:text>
				<xsl:text> of harm or offending AND the risk posed by the child</xsl:text>
				<fo:inline font-weight="bold">
					<xsl:text> cannot be managed safely in the community.</xsl:text>
				</fo:inline>	
			</fo:block>	
		</xsl:if>
		<fo:block space-after="10pt"/>
    </xsl:template>
	<!-- Other Reasons -->
	<xsl:template name="RC_OtherReasons_Text">
		<fo:block>
			<xsl:text>Other reasons:</xsl:text>
		</fo:block>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:WelfareReason/@selected='true'">
			<fo:block>
				<xsl:text>For welfare reasons. </xsl:text>
				<xsl:value-of select="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:WelfareReason"/>
			</fo:block>	
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:OwnProtectionReason/@selected='true'">
			<fo:block>
				<xsl:text>For own protection reasons. </xsl:text>
				<xsl:value-of select="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:OwnProtectionReason"/>
			</fo:block>	
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:LackOfPlacementReason/@selected='true'">
			<fo:block>
				<xsl:text>lack of suitable placement in the community (e.g. foster, local authority, relative etc). </xsl:text>
				<xsl:value-of select="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:LackOfPlacementReason"/>
			</fo:block>	
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:BailISSNotAvailableReason/@selected='true'">
			<fo:block>
				<xsl:text>Bail ISS not available. </xsl:text>
				<xsl:value-of select="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:BailISSNotAvailableReason"/>
			</fo:block>	
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:BailInadequateReason/@selected='true'">
			<fo:block>
				<xsl:text>bail package inadequate. </xsl:text>
				<xsl:value-of select="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/ord:BailInadequateReason"/>
			</fo:block>	
		</xsl:if>
		<fo:block/>
    </xsl:template>
	<!-- Remand Reasons -->
	<xsl:template match="nar:RC_RemandReasons_Label">
		<fo:block>
			<xsl:text>Reasons given in Court</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:text>for the remand to</xsl:text>
		</fo:block>
		<fo:block>	
			<xsl:text>youth detention</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:text>accomodation</xsl:text>
		</fo:block>	
		<fo:block/>
    </xsl:template>
	<xsl:template match="nar:RC_RemandReasons_Text">
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:SeriousnessForRemand/@selected='true'">
			<xsl:call-template name="RC_Seriousness_Text"/>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:RemandHistory/@selected='true'">
			<xsl:call-template name="RC_History_Text"/>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:NecessityCondition/@selected='true'">
			<xsl:call-template name="RC_Necessity_Text"/>
		</xsl:if>
		<xsl:if test="$baseAll/ord:ReasonsForRemand/ord:OtherReasonsForRemand/@selected='true'">
			<xsl:call-template name="RC_OtherReasons_Text"/>
		</xsl:if>
    </xsl:template>
    <!-- ***************************** -->
    <!-- REMAND ORDER END -->
    <!-- *****************************  -->
</xsl:stylesheet>
