<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ***********************************************************-->
    <!-- COMMUNITY PUNISHMENT ORDER (CPO) START -->
    <!-- ***********************************************************-->
    <!-- CPO Title -->
    <xsl:template match="nar:CPO_Title">
        <fo:inline>
            <xsl:text>Community Punishment Order</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Trial Committed Breach Details -->
    <xsl:template match="nar:CPO_Text1">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:Trial/@selected='true'">
                at <xsl:call-template name="COMMCourtTextCurrent"/> on 
                <xsl:call-template name="FormatDate">
                    <xsl:with-param name="date" select="$baseAll/ord:Trial/ord:ConvictionDate"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$baseAll/ord:CommittingCourt/@selected='true'">
                <fo:block>
                    at <xsl:call-template name="CommunityCommittingCourt"/> and was committed to this Court to be dealt with.
                </fo:block>
            </xsl:when>
            <xsl:when test="$baseAll/ord:Breach/@selected='true'">
                <fo:block>
                    at <xsl:call-template name="CommunityConvictingCourt"/>
                </fo:block>
                    has been found by <xsl:call-template name="CommunityBreachCourt"/>
                    to be in breach of the requirements of 
                    <xsl:call-template name="LetterDisplay">
                        <xsl:with-param name="text" select="$OriginalOrder"/>
                    </xsl:call-template>
                    Order made by <xsl:call-template name="COMMCourtTextBreach"/> on 
                    <xsl:call-template name="FormatDate">
                        <xsl:with-param name="date" select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:Date"/>
                    </xsl:call-template>
                and was committed to this Court to be dealt with.
                <fo:block space-before="12pt" space-after="12pt">
                    <xsl:if test="$baseAll/ord:OriginalOrderRevoked/@selected='true' and $baseAll/ord:OriginalOrderRevoked/ord:BreachCourt='yes'">
	 		   The 
                        <xsl:call-template name="CommunityBreachCourt"/>
                        was satisfied that it was in the interests of justice
                        <xsl:if test="$baseAll/ord:OriginalOrderRevoked/@selected='true'">
                            to revoke the <xsl:value-of select="$OriginalOrder"/> order								
                        </xsl:if>
                        <!-- If both options selected include the word 'and' -->
                        <xsl:if test="$baseAll/ord:OriginalOrderRevoked/@selected='true'">
                            and 
                        </xsl:if>
                        for <xsl:call-template name="COMMCourtTextBreach"/>
                        to deal with the defendant in some other manner for the offences for which it made the order on 
                        <xsl:call-template name="FormatDate">
                            <xsl:with-param name="date" select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:Date"/>
                        </xsl:call-template>
                    </xsl:if>
                </fo:block>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- CPO text 2-->
    <xsl:template match="nar:CPO_Text2">
        <fo:inline>
            <xsl:text> the Court, after considering a report by a probation officer or social worker of a local authority social services department about the defendant and the defendant's circumstances </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Display hearing option -->
    <xsl:template match="nar:CPO_HearingOption">
        <xsl:call-template name="HearingOption"/>
    </xsl:template>
    <!-- Template to display hearing details -->
    <xsl:template name="HearingOption">
        <xsl:if test="$baseAll/ord:AfterHearingFrom='yes'">
            <fo:block>
                and after hearing a probation officer or social worker of a local authority social services department
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- CPO text 3-->
    <xsl:template match="nar:CPO_Text3">
        <fo:inline>
            <xsl:text> was satisfied that the defendant is a suitable person to perform community punishment work and that provisions can be made for the defendant to do so in the local justice area of </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- used within the Community PUNISHMENT Order -->
    <xsl:template match="nar:CPO_Text4">
        <fo:block space-before="12pt">
            <fo:inline font-weight="bold">It is ordered</fo:inline> that the defendant, during a period of
            <xsl:for-each select="$baseAll/ord:CommunityPunishment/ord:Period">
                <xsl:call-template name="Term"/>
            </xsl:for-each>
            from the date of this order, shall:
            <fo:block space-before="12pt">
                1. Keep in touch with the relevant officer in accordance with such instructions as may from time to time be given by that officer and notify the relevant officer of any change of address.	
            </fo:block>
            <fo:block space-before="12pt">
                2. Perform <xsl:call-template name="CommunityPunishmentHours"/> hour(s) of community punishment work at such time as that officer may instruct.
                <xsl:if test="$baseAll/ord:Breach/@selected='true'">
                    <xsl:call-template name="BreachText"/>
                </xsl:if>
            </fo:block>
        </fo:block>
    </xsl:template>
    <!-- *******************************************************-->
    <!-- COMMUNITY PUNISHMENT ORDER (CPO) END -->
    <!-- *******************************************************-->
</xsl:stylesheet>
