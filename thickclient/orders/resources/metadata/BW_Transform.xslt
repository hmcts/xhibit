<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ***************************************** -->
    <!-- BENCH WARRANT ORDER START -->
    <!-- ***************************************** -->
    <!-- All Constables -->
    <xsl:template match="nar:BW_AllConstables">
        <fo:inline>
            <xsl:text>ALL CONSTABLES ARE ORDERED</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- To arrest -->
    <xsl:template match="nar:BW_ToArrest">
        <fo:inline>
            <xsl:text>To arrest</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- The -->
    <xsl:template match="nar:BW_The">
        <fo:inline>
            <xsl:text>The</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Defendant -->
    <xsl:template match="nar:BW_Defendant">
        <fo:inline>
            <xsl:text>Defendant</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Release standard text -->
    <xsl:template match="nar:BW_ReleaseText">
        <fo:inline>
            <xsl:text>who, having been released on bail subject to a duty to surrender to the custody of the Crown Court, has failed to surrender as required and </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- BW Release On Bail -->
    <!-- S.Bachra 22/4/03 Change of text, remove magistrates court ref (Tracker 52697) -->
    <xsl:template match="nar:BW_Forthwith">
        <xsl:if test="$baseAll/ord:Release='Refused'">
            <fo:block>
                bring
                <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
                    <xsl:call-template name="MaleFemale"/>
                </xsl:for-each>
                forthwith before the Crown Court
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- S.Bachra 22/4/03 Correct spelling of condition(s) Tracker 52724 -->
    <xsl:template match="nar:BW_ReleaseOnBail">
        <xsl:if test="$baseAll/ord:Release='Conditional'">
            <fo:block>
                release
                <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
                    <xsl:call-template name="MaleFemale"/>
                </xsl:for-each> 
                on bail 
                <xsl:choose>
                    <xsl:when test="$baseAll/ord:PreConditions[@selected='true'] or $baseAll/ord:PostConditions[@selected='true']">
                        subject to the following condition(s):-
                    </xsl:when>
                    <xsl:otherwise>
                        unconditionally
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
            <xsl:if test="$baseAll/ord:PreConditions[@selected='true']">
                <fo:block/>
                    A. To be complied with <fo:inline font-weight="bold">BEFORE </fo:inline>release on bail: 
                <fo:block>
                    to provide
                    <xsl:value-of select="$baseAll/ord:PreConditions/ord:Surety/ord:Plural"/>
                    in the sum of 
                    <xsl:for-each select="$baseAll/ord:PreConditions/ord:Surety">
                        <xsl:apply-templates select="ord:MonetaryValue"/>
                    </xsl:for-each>
                </fo:block>
                <fo:block>
                    to secure the surrender of the defendant to custody at the time and place directed
                </fo:block>
            </xsl:if>
            <xsl:if test="$baseAll/ord:PreConditions[@selected='true'] and $baseAll/ord:PostConditions[@selected='true']">AND<fo:block/>
            </xsl:if>
            <xsl:if test="$baseAll/ord:PostConditions[@selected='true']">
                <fo:block>
                    B. To be complied with <fo:inline font-weight="bold">AFTER </fo:inline>release on bail:
                    <fo:block/>
                    <xsl:call-template name="FormatTextArea">
                        <xsl:with-param name="string" select="$baseAll/ord:PostConditions/ord:PostConditionDetails"/>
                    </xsl:call-template>
                </fo:block>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    <!-- Appear at Court -->
    <xsl:template match="nar:BW_AppearAtCourt">
        <fo:inline>
            <xsl:text>to appear at the Crown Court at </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Other Place -->
    <xsl:template match="nar:BW_OtherPlace">
        <fo:inline>
            <xsl:text>(or such other place as shall be notified)</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Common Template to display next appearance details -->
    <xsl:template match="nar:BW_NextAppearanceDate">
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
                <xsl:call-template name="BW_DateTimeNotified"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Date and time notified -->
    <xsl:template name="BW_DateTimeNotified">
        <fo:inline>
            <xsl:text> on such day and at such time as the court may direct</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Surrender -->
    <xsl:template match="nar:BW_Surrender">
        <fo:inline>
            <xsl:text>THERE to surrender </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Custody -->
    <xsl:template match="nar:BW_Custody">
        <fo:inline>
            <xsl:text> into custody.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- ************************************** -->
    <!-- BENCH WARRANT ORDER END -->
    <!-- ************************************** -->
</xsl:stylesheet>
