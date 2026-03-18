<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ***************************** -->
    <!-- REMAND ORDER START -->
    <!-- *****************************  -->
    <!-- Remand title -->
    <xsl:template match="nar:RC_RemandTitle">
        <fo:inline>
            <xsl:text>Remand Order</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- It was ordered -->
    <xsl:template match="nar:RC_ItWasOrdered">
        <fo:inline>
            <xsl:text>It was ordered</xsl:text>
        </fo:inline>
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
            <xsl:text>be remanded in custody at </xsl:text>
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
    <!-- ***************************** -->
    <!-- REMAND ORDER END -->
    <!-- *****************************  -->
</xsl:stylesheet>
