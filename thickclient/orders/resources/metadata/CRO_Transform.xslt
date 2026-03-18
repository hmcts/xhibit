<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ****************************************************-->
    <!-- COMMUNITY REHAB ORDER (CRO) START -->
    <!-- ****************************************************-->
    <!-- CRO Title -->
    <xsl:template match="nar:CRO_Title">
        <fo:inline>
            <xsl:text>Community Rehabilitation Order</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- used within the Community REHAB Order -->
    <xsl:template match="nar:CRO_Text1">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:Trial/@selected='true'">
                at <xsl:call-template name="COMMCourtTextCurrent"/> on 
                <xsl:call-template name="FormatDate">
                    <xsl:with-param name="date" select="$baseAll/ord:Trial/ord:ConvictionDate"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$baseAll/ord:CommittingCourt/@selected='true'">
                <fo:block>
                    at <xsl:call-template name="CommunityCommittingCourt"/>
                    and was committed for sentence to the Crown Court.
                </fo:block>
            </xsl:when>
            <xsl:when test="$baseAll/ord:Breach/@selected='true'">
                <fo:block>
                    at <xsl:call-template name="CommunityConvictingCourt"/>
                </fo:block>
                <fo:block/>
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
                        <xsl:call-template name="CommunityBreachCourt"/>
                        was satisfied that it was in the interests of justice
                        <xsl:if test="$baseAll/ord:OriginalOrderRevoked/@selected='true'">
                            to revoke the order
                        </xsl:if>
                        and for <xsl:call-template name="COMMCourtTextBreach"/>
                        to deal with the defendant in some other manner for the offences for which it made the order on 
                        <xsl:call-template name="FormatDate">
                            <xsl:with-param name="date" select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:Date"/>
                        </xsl:call-template>
                    </xsl:if>
                </fo:block>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- CRO text2 -->
    <xsl:template match="nar:CRO_Text2">
        <fo:inline>
            <xsl:text> this Court, being of the opinion that it was appropriate to make a Community Rehabilitation Order, explained to the defendant the effect of this order (including any additional requirements shown below), the consequences which may follow failure to comply with this order, and the right of the defendant or the supervising officer to apply for the review of the order.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- used within the Community REHAB Order -->
    <xsl:template match="nar:CRO_Text3">
        <fo:block space-before="12pt" space-after="12pt">
            <fo:inline font-weight="bold">It is ordered</fo:inline> that the defendant shall:
                <fo:block space-before="12pt">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block font-weight="bold">&#x2219;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                For a period of
                                <xsl:for-each select="$baseAll/ord:RehabilitationPeriod">
                                    <xsl:call-template name="Term"/>
                                </xsl:for-each>
                                from the date of this order be under the supervision of a probation officer for the local justice area of <xsl:call-template name="PettySessionalArea"/><xsl:text>.</xsl:text>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block font-weight="bold">&#x2219;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                Keep in touch with the probation officer in accordance with such instructions as may from time to time be given by that officer and notify the probation officer of any change of address.
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </fo:block>
    </xsl:template>
    <!-- ************************************************-->
    <!-- COMMUNITY REHAB ORDER (CRO) END -->
    <!-- ************************************************-->
</xsl:stylesheet>
