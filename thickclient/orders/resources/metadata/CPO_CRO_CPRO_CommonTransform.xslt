<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ************************************************************************-->
    <!-- COMMUNITY ORDERS COMMON (CPO, CPRO, CRO) START -->
    <!-- ************************************************************************-->
    <!-- whose address -->
    <xsl:template match="nar:Comm_WhoseAddress">
        <fo:inline>
            <xsl:text>whose address </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display address details -->
    <xsl:template match="nar:Comm_AddressOption">
        <xsl:if test="$baseAll/ord:AddressOption='AddressIs'">
            is 
            <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
                <xsl:call-template name="CallableAddress"/>
            </xsl:for-each>
        </xsl:if>
        <xsl:if test="$baseAll/ord:AddressOption='AddressWillBe'">
            will be
            <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
                <xsl:call-template name="CallableAddress"/>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <!-- convicted of text -->
    <xsl:template match="nar:Comm_ConvictedOf">
        <fo:inline>
            <xsl:text> has been convicted of </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template used to display conviction details -->
    <!-- 	S. Bachra 20/3/03
    Transform used to display the enumeration of the Defendants convictions
    1. Get all the distinct Offence Statements i.e only select if not already selected
    2. Sort the Offence Statements Alphabetically
    3. Set up variable to hold current Offence Statement
    4. Display Offence Statement X count of all Offence Statements that match the variable
    
    S.Bachra 07/05/03 (Tracker 52740)
    By default there will be one charge structure in place which will be used if there are no charges retieved from the database
    This is distriguisted from the other charges by having a default CaseNumber of 'A00000000' -->
    <xsl:template match="nar:Comm_Conviction">
        <!-- Check to see if any charges are present other than default, ignoring the first charge as this is user enterable -->
        <fo:block hyphenate="true" language="en_GB">
            <xsl:choose>
                <xsl:when test="count($baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge) >1">
                    <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge[position()!=1]/ord:OffenceStatement[not(.=preceding::ord:OffenceStatement)]">
                        <xsl:sort select="."/>
                        <xsl:variable name="value" select="."/>
                        <xsl:value-of select="."/>
                        <xsl:choose>
                            <xsl:when test="count($baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement[(.=$value)]) > 1">
                                <xsl:text> </xsl:text> X <xsl:value-of select="count($baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement[(.=$value)])"/>
                            </xsl:when>
                        </xsl:choose>
                        <!-- add a semicolon after each conviction except the last -->
                        <xsl:if test="position() != last()">; </xsl:if>
                        <xsl:text> </xsl:text>
                        <fo:block/>
                    </xsl:for-each>
                </xsl:when>
                <!-- If there is not charge information display the text within the default charge -->
                <xsl:otherwise>
                    <xsl:value-of select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge[1]/ord:OffenceStatement"/>
                    <xsl:text> </xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </xsl:template>
    <!-- Common Template to display Petty Sessional Area -->
    <xsl:template name="PettySessionalArea">
        <xsl:value-of select="$baseAll/ord:PettySessionalArea/ord:CourtHouseName"/>
    </xsl:template>
    <!--Common Community Order Current specific court text -->
    <xsl:template name="COMMCourtTextCurrent">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType=$CrownCourt">
                <xsl:text> the Crown Court at </xsl:text>
                <xsl:value-of select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseName"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseName"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Common Template to display Court and date details -->
    <xsl:template name="CommunityCommittingCourt">
        <fo:inline>
            <xsl:value-of select="$baseAll/ord:CommittingCourt/ord:CourtHouse/ord:CourtHouseName"/>
            <xsl:for-each select="$baseAll/ord:CommittingCourt/ord:CourtHouse">
                <xsl:call-template name="CourtHouseType"/>
            </xsl:for-each>
            on 
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:CommittingCourt/ord:Date"/>
            </xsl:call-template>
        </fo:inline>
    </xsl:template>
    <!-- Common Template to display Breach Court details inc date -->
    <xsl:template name="CommunityConvictingCourt">
        <fo:inline>
            <xsl:value-of select="$baseAll/ord:Breach/ord:ConvictingCourt/ord:CourtHouse/ord:CourtHouseName"/>
            <xsl:for-each select="$baseAll/ord:Breach/ord:ConvictingCourt/ord:CourtHouse">
                <xsl:call-template name="CourtHouseType"/>
            </xsl:for-each>
            on 
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:Breach/ord:ConvictingCourt/ord:Date"/>
            </xsl:call-template>
        </fo:inline>
    </xsl:template>
    <!-- Common Template to display Breach Court details inc date -->
    <xsl:template name="CommunityBreachCourt">
        <xsl:value-of select="$baseAll/ord:Breach/ord:BreachCourt/ord:CourtHouse/ord:CourtHouseName"/>
        <xsl:for-each select="$baseAll/ord:Breach/ord:BreachCourt/ord:CourtHouse">
            <xsl:call-template name="CourtHouseType"/>
        </xsl:for-each>
    </xsl:template>
    <!--Common Community Order Breach specific court text -->
    <xsl:template name="COMMCourtTextBreach">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseType=$CrownCourt">
                <xsl:call-template name="CallableCommunityCourtHouseType"/>
                <xsl:value-of select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- the court -->
    <xsl:template match="nar:Comm_TheCourt">
        <fo:inline>
            <xsl:text>The Court</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- on -->
    <xsl:template match="nar:Comm_On">
        <fo:inline>
            <xsl:text>On </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Common Template to display failed to comply details -->
    <xsl:template match="nar:Comm_FailedToComply">
        <xsl:if test="$baseAll/ord:FailedToComply='yes' and $baseAll/ord:Breach/@selected='true'">
            this Court was satisfied that the defendant has failed to comply with a requirement of the above order and
            <fo:block/>
        </xsl:if>
    </xsl:template>
    <!-- Common Template to display Revoke details -->
    <xsl:template match="nar:Comm_RevokeOption">
        <xsl:if test="$baseAll/ord:OriginalOrderRevoked/@selected='true' and $baseAll/ord:Breach/@selected='true' and $baseAll/ord:OriginalOrderRevoked/ord:BreachCourt='no'">
            this Court was satisfied that it was in the interests of justice to revoke the order and
        </xsl:if>
    </xsl:template>
    <!-- Common Template to display Petty Sessional Area -->
    <xsl:template match="nar:Comm_PettySessionalArea">
        <xsl:call-template name="PettySessionalArea"/>
    </xsl:template>
    <!-- Common Template to display additional requirements -->
    <xsl:template match="nar:Comm_AdditionalRequirements">
        <xsl:if test="$baseAll/ord:Conditions/@selected='true'">
            <fo:block space-before="12pt">Additional requirements:</fo:block>
            <fo:block>
                <xsl:call-template name="FormatTextArea">
                    <xsl:with-param name="string" select="$baseAll/ord:Conditions/ord:Condition/ord:Description"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- Common Template to display Signed Info -->
    <xsl:template match="nar:Comm_SignedInfoCommunity">
        <xsl:call-template name="SignedInfoCommunity"/>
    </xsl:template>
    <!-- *********************************************************************-->
    <!-- COMMUNITY ORDERS COMMON (CPO, CPRO, CRO) END -->
    <!-- *********************************************************************-->
</xsl:stylesheet>