<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- *************************************-->
	<!-- DETENTION & TRAINING ORDER START -->
	<!-- *************************************-->
	<!-- DTO title -->
	<xsl:template match="nar:DTO_Title">
		<fo:inline>
			<xsl:text>Detention and Training Order</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- DTO Sub title -->
	<xsl:template match="nar:DTO_SubTitle">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:OrderPeriod/ord:Section105/@selected ='true'">
					Offences committed during the currency of this order
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:OrderMadeUnder='Section100'">
							<xsl:text>Made under Section 100 Powers of Criminal Courts (Sentencing) Act 2000</xsl:text>
						</xsl:when>
						<xsl:when test="$baseAll/ord:OrderMadeUnder='Section233'">
							<xsl:text>Made under Section 233 of the Sentencing Act 2020</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Made under Section 100 Powers of Criminal Courts (Sentencing) Act 2000</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Defendant Details -->
	<xsl:template match="nar:DTO_DefendantDetails">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
	</xsl:template>
	<!-- Template used to display Committed for Sentence Details -->
	<xsl:template match="nar:DTO_CommittedForSentence">
		<xsl:if test="$baseAll/ord:CommittingCourt/@selected ='true'">
			<fo:block>
                at <xsl:value-of select="$baseAll/ord:CommittingCourt/ord:CourtHouseName"/>
				<!-- magistrates court--> and committed for sentence to the Crown Court.
            </fo:block>
		</xsl:if>
		<fo:block space-before="12pt">
            Details of the conviction and sentence are on the court record.
        </fo:block>
	</xsl:template>
	<!-- Template used to show Risk or Vulnerability details -->
	<xsl:template match="nar:DTO_RiskOrVulnerability">
		<fo:inline>
			<xsl:if test="$baseAll/ord:RiskVulnerabilityFactors/@selected = 'true'">
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="$baseAll/ord:RiskVulnerabilityFactors/ord:RiskText"/>
				</xsl:call-template>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- The court ordered ending text -->
	<xsl:template match="nar:DTO_PeriodOfOrder">
		<fo:inline>
			<xsl:text> that for a term of </xsl:text>
			<xsl:for-each select="$baseAll/ord:OrderPeriod/ord:Period">
				<xsl:call-template name="TermDuration"/>
			</xsl:for-each>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:OrderPeriod/ord:Section105/@selected ='true'">
					<xsl:text> the defendant was made subject to a Detention and Training Order.</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> the defendant is subject to a period of detention and training followed by a period of supervision.</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Sentence information  -->
	<xsl:template match="nar:DTO_SentenceText">
		<fo:inline>
			<xsl:choose>
				<!-- Section 105 option -->
				<xsl:when test="$baseAll/ord:OrderPeriod/ord:Section105/@selected ='true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:OrderMadeUnder='Section100'">
							<xsl:text>The Court ordered that under the provisions of section 105 of the Powers of Criminal Courts (Sentencing) Act 2000 the defendant be detained in secure accommodation for a further period of  </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>The Court ordered that under the provisions of section 243 of the Sentencing Act 2020 the defendant be detained in secure accommodation for a further period of </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:for-each select="$baseAll/ord:OrderPeriod/ord:Section105/ord:Period">
						<xsl:call-template name="TermDuration"/>
						<xsl:text>.</xsl:text>
					</xsl:for-each>
					<xsl:text> The period of detention to be served in such secure accommodation as may be determined by the Secretary of State or by such other person as authorised by him for that purpose.</xsl:text>
				</xsl:when>
				<!-- Section 105 Option end -->
				<!-- Non Section 105 start-->
				<xsl:otherwise>
					<xsl:text>The first period, of detention and training, is to be served in youth detention accomodation as determined by the Secretary of State. The second period, of supervision, is to be served in the community.</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Non Section 105 end-->
		</fo:inline>
	</xsl:template>
	<!-- Credit for Bail Section -->
	<xsl:template match="nar:COMYIMP_CreditForBail">
		<fo:inline>
			<xsl:text>Under section </xsl:text>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:CreditForBail/ord:CreditForTimeOnBailSection='section240a'">
					<xsl:text>240A Criminal Justice Act 2003, the court directs that </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>325 of the Sentencing Act 2020, the court directs that </xsl:text>
				</xsl:otherwise>
			</xsl:choose> 
			<xsl:for-each select="$baseAll/ord:CreditForBail/ord:Term">
				<xsl:call-template name="TermOnlyDays"/>
				<xsl:text> will count towards the sentence.</xsl:text>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
		<!-- Requirements title -->
	<xsl:template match="nar:DTO_RequirementTitle">
		<xsl:if test="$baseAll/ord:DTOrderRequirements/ord:TrailMonitoringRequirement/@selected='true'">
			<fo:inline>
				<xsl:text>Requirements</xsl:text>
			</fo:inline>
		</xsl:if>
	</xsl:template>
	<!-- Template to display DTO Requirements text -->
	<xsl:template match="nar:DTO_Requirement">
		<!-- Trail Monitoring Requirement -->
		<xsl:if test="$baseAll/ord:DTOrderRequirements/ord:TrailMonitoringRequirement/@selected='true'">
			<xsl:text>You will be electronically trail monitored for </xsl:text>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:DTOrderRequirements/ord:TrailMonitoringRequirement/ord:Duration != ''">
					<xsl:value-of select="$baseAll/ord:DTOrderRequirements/ord:TrailMonitoringRequirement/ord:Duration"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>0</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> days after release from custody so that the court can be sure you are complying with the requirements of this order.</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- ***********************************-->
	<!-- DETENTION & TRAINING ORDER END -->
	<!-- ***********************************-->
</xsl:stylesheet>
