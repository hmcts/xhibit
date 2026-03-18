<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ****************************************************************** -->
    <!-- COMMITMENT TO YOUNG OFFENDERS ORDER START -->
    <!-- *****************************************************************  -->
    <!-- COMY title -->
    <xsl:template match="nar:COMY_Title">
        <fo:inline>
            <xsl:text>Order for commitment to a young offender institution</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Committed for Sentence Details -->
    <xsl:template match="nar:COMY_CommittedForSentence">
        <xsl:if test="$baseAll/ord:CommittingCourt/@selected ='true'">
            <fo:block>
                at <xsl:value-of select="$baseAll/ord:CommittingCourt/ord:CourtHouseName"/>
                <!-- magistrates court--> and committed for sentence to the Crown Court.
            </fo:block>
        </xsl:if>
        <fo:block space-before="12pt">
            Details of the conviction and sentence are on the enclosed court record.
        </fo:block>
    </xsl:template>
    <!-- Period of -->
    <xsl:template match="nar:COMY_PeriodOf">
        <fo:inline>
            <xsl:text> that the defendant serve a period of detention, details of which follow.</xsl:text>
        </fo:inline>
        <fo:block space-after="12pt"/>
        <fo:block>
            <xsl:text>The Crown Court had, or would have had but for the statutory restrictions upon the imprisonment of young offenders, power to impose imprisonment on the defendant.</xsl:text>
        </fo:block>
    </xsl:template>
    <!-- Defendant be detained -->
    <xsl:template match="nar:COMY_DefendantBeDetained">
        <fo:inline>
            <xsl:text>The court ordered that the defendant be detained</xsl:text>
        </fo:inline>
    </xsl:template>    
    <!-- Template used to display Imprisonment details -->
    <xsl:template match="nar:COMY_ImprisonmentType">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='life'">
                <xsl:call-template name="callableLifeText"/>
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section9394'">
                under the life sentence provisions for young offenders of sections 93 and 94 of the Powers of Criminal Courts (Sentencing) Act 2000
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='hmpleasure'">
                during His Majesty's pleasure
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='false'">
                <xsl:call-template name="callableSection91Text"/>
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
                <xsl:call-template name="callableDetentionText"/>
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section91TermType ='section91life'">
                <xsl:call-template name="callableSection91Text"/>
                <xsl:call-template name="callableLifeText"/>
            </xsl:when>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section91TermType ='section91term'">
                <xsl:call-template name="callableSection91Text"/>
                <xsl:call-template name="callableDetentionText"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Template to display Life Details -->
    <xsl:template name="callableLifeText">
        for life.	
    </xsl:template>
    <!-- Template to display Section 91 text -->
    <xsl:template name="callableSection91Text">
        under the long sentence provisions of section 91 of the Powers of Criminal Courts (Sentencing) Act 2000
    </xsl:template>
    <!-- Template to display Detention or Imprisonment details -->
    <xsl:template name="callableDetentionText">
        <xsl:text>in a young offender institution for </xsl:text>
        <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:Term">
            <!-- S.Bachra 22/4/03 Display days value (Tracker 52680) -->
            <xsl:call-template name="TermIncDays"/>
        </xsl:for-each>
        <xsl:text> </xsl:text>
    </xsl:template>
    <!--Template to display Custodial Sentence Details -->
    <xsl:template match="nar:COMY_CustodialSentenceOption">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:TermType!='Not Applicable'">
                <xsl:call-template name="YOICallableSentenceOption"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Template to display Sentence text Details -->
    <xsl:template name="YOICallableSentenceOptionText">
        <fo:block space-after="12pt"/>
            <xsl:text>This sentence was ordered to be </xsl:text>
            <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:TermType">
                <xsl:call-template name="TermType"/>
            </xsl:for-each>
        <xsl:text> any other periods of detention to which the defendant was subject prior to the making of this order.</xsl:text>
    </xsl:template>
    <!-- Template to display Extended Term details -->
    <xsl:template name="IMPExtendedSentenceType">
        <xsl:for-each select="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SentenceType">
            <xsl:call-template name="TermType"/>
        </xsl:for-each>
    </xsl:template>
    <!--Template to display extended sentence currency -->
    <xsl:template match="nar:COMY_ExSentence">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:CustodialSentence/ord:ExtendedSentence/ord:SentenceType!='Not Applicable'">
                <xsl:text>This sentence was ordered to be </xsl:text>
                <xsl:call-template name="IMPExtendedSentenceType"/>
                <xsl:text>any other periods of detention to which the defendant was subject prior to the making of this order.</xsl:text>
                <xsl:call-template name="callableP"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Return Defendant title 1 -->
    <xsl:template match="nar:COMY_ReturnDefendant_Title1">
        <fo:inline>
            <xsl:text>Return of defendants</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Return Defendant title 2 -->
    <xsl:template match="nar:COMY_ReturnDefendant_Title2">
        <fo:inline>
            <xsl:text>to a young offender</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Return Defendant title 3 -->
    <xsl:template match="nar:COMY_ReturnDefendant_Title3">
        <fo:inline>
            <xsl:text>institution</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Detention Prison details -->
    <xsl:template match="nar:COMY_YOIPrison">
        <xsl:for-each select="$baseAll/ord:ReturnToImprisonment">
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
    <!-- Template to display custodial sentence currency -->
    <xsl:template match="nar:COMY_Return">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:ReturnToImprisonment/ord:ReturnPeriod/@TermType!='Not Applicable'">
                <xsl:text>This period of detention was ordered to be served </xsl:text>
                <xsl:call-template name="IMPServedPeriod"/>
                <xsl:text> any other periods of detention imposed by the court on the same occasion when this order was made. </xsl:text>
               <xsl:call-template name="callableP"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- *************************************************************** -->
    <!-- COMMITMENT TO YOUNG OFFENDERS ORDER END -->
    <!-- **************************************************************  -->
</xsl:stylesheet>
