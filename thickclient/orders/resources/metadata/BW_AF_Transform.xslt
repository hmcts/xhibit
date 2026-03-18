<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ***************************************** -->
	<!-- BENCH WARRANT ORDER START -->
	<!-- ***************************************** -->
	<!-- ###### Bench Warrant After failure to comply/attend title ###### -->
	<xsl:template match="nar:BW_AF_Title">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:WarrantType = 'failureToAttend' ">
				<fo:block text-align="center">Warrant for arrest after failure to </fo:block>
				<fo:block text-align="center">attend court after a breach of a </fo:block>
				<fo:block text-align="center">
					<xsl:call-template name="Callable_BW_AF_getAssociatedOrder"/>
				</fo:block>
			</xsl:when>
			<xsl:when test="$baseAll/ord:WarrantType = 'failureToComply' ">
				<fo:block text-align="center">Warrant for arrest after failure </fo:block>
				<fo:block text-align="center"> to comply with</fo:block>
				<fo:block text-align="center">
					<xsl:call-template name="Callable_BW_AF_getAssociatedOrder"/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ###### To all Constables ######  -->
	<xsl:template match="nar:BW_AF_ToAllConstables">
		<fo:inline>
			<xsl:text>TO ALL CONSTABLES</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- To all constables text passage -->
	<xsl:template match="nar:BW_AF_ToAllConstablesText">
		<fo:inline>
			<xsl:call-template name="CallableDefendantFullName"/>
			<xsl:text> (date of birth: </xsl:text>
			<xsl:call-template name="CallableDefendantDOB"/>
			<xsl:text> )</xsl:text>
		</fo:inline>
		<fo:block/>
		<fo:inline>
			<xsl:text>of </xsl:text>
			<xsl:call-template name="CallableDefendantAddress"/>
		</fo:inline>
		<fo:block space-after="10pt"/>
		<xsl:choose>
			<xsl:when test="$baseAll/ord:WarrantType = 'failureToAttend' ">
				<fo:inline>having been given adequate notice of the time and place of proceedings in relation to a breach of a </fo:inline>
				<xsl:call-template name="Callable_BW_AF_getAssociatedOrder"/>
				<fo:inline> imposed by this court, has failed to appear as required. </fo:inline>
			</xsl:when>
			<xsl:when test="$baseAll/ord:WarrantType = 'failureToComply' ">
				<fo:inline>has failed to comply with a </fo:inline>
				<xsl:call-template name="Callable_BW_AF_getAssociatedOrder"/>
				<fo:inline> imposed by this court.</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!--###### The court ordered title ######  -->
	<xsl:template match="nar:BW_AF_TheCourtOrderedTitle">
		<fo:inline> The Court ORDERED</fo:inline>
	</xsl:template>
	<!--###### The court ordered text ######  -->
	<xsl:template match="nar:BW_AF_TheCourtOrderedText">
		<fo:inline>that you are to arrest </fo:inline>
		<xsl:call-template name="CallableDefendantFullName"/>
		<fo:inline> and to </fo:inline>
		<xsl:choose>
			<!--Bring Forrthwith Option START-->
			<xsl:when test="$baseAll/ord:Release='Refused'">
				<fo:block>
					<fo:inline>bring </fo:inline>
					<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
						<xsl:call-template name="HimHer"/>
					</xsl:for-each>
					forthwith before the Crown Court at
					<xsl:value-of select="$baseAll/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseName"/>.
				</fo:block>
			</xsl:when>
			<!--Bring Forrthwith Option END -->
			<!-- Release on Bail Option START -->
			<xsl:when test="$baseAll/ord:Release='Conditional' ">
				<fo:block>
					<fo:inline>release </fo:inline>
					<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
						<xsl:call-template name="HimHer"/>
					</xsl:for-each>
					<fo:inline> on bail </fo:inline>
					<xsl:choose>
						<xsl:when test="$baseAll/ord:PreConditions[@selected='true'] or $baseAll/ord:PostConditions[@selected='true']">
							<fo:inline> subject to the following condition(s):</fo:inline>
						</xsl:when>
						<xsl:otherwise>
							<fo:inline> unconditionally.</fo:inline>
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</xsl:when>
		</xsl:choose>
		<fo:block space-after="10pt"/>
		<!-- Release on Bail Option End -->
		<!-- Bail Conditions Start-->
		<xsl:if test="$baseAll/ord:Release='Conditional'">
			<!-- Pre Conditions Start-->
			<xsl:if test="$baseAll/ord:PreConditions[@selected='true']">
				<fo:block/>
				(A). To be complied with <fo:inline font-weight="bold">BEFORE </fo:inline>release from custody: 
				<xsl:if test="$baseAll/ord:PreConditions/ord:Surety[@selected='true']">
					<fo:block>To provide
						<xsl:choose>
							<xsl:when test="$baseAll/ord:PreConditions/ord:Surety/ord:Plural = 'surety' ">
								<xsl:text> a surety </xsl:text>
							</xsl:when>
							<xsl:when test="$baseAll/ord:PreConditions/ord:Surety/ord:Plural = 'sureties' ">
								<xsl:text> sureties </xsl:text>
							</xsl:when>
						</xsl:choose>							
						in the sum of 
						<xsl:for-each select="$baseAll/ord:PreConditions/ord:Surety">
							<xsl:apply-templates select="ord:MonetaryValue"/>
						</xsl:for-each>
						to secure the surrender of 
						<xsl:call-template name="CallableDefendantFullName"/>
						to custody at the time and place directed (recognizance(s) of the
						<xsl:value-of select="$baseAll/ord:PreConditions/ord:Surety/ord:Plural"/>
						to be endorsed on form 5102D Bail: recognizance of a surety).
					</fo:block>
				</xsl:if>
				<fo:block space-after="10pt">
					<xsl:if test="$baseAll/ord:PreConditions/ord:PreConditionDetails[@selected='true']">
						<xsl:call-template name="FormatTextArea">
							<xsl:with-param name="string" select="$baseAll/ord:PreConditions/ord:PreConditionDetails"/>
						</xsl:call-template>
					</xsl:if>
				</fo:block>
			</xsl:if>
			<!-- Pre Conditions End -->
			<!-- Post Conditions start -->
			<xsl:if test="$baseAll/ord:PostConditions[@selected='true']">
				<fo:block space-after="10pt">
                    (B). To be complied with <fo:inline font-weight="bold">AFTER </fo:inline>release from custody:
                    <fo:block/>
					<xsl:call-template name="FormatTextArea">
						<xsl:with-param name="string" select="$baseAll/ord:PostConditions/ord:PostConditionDetails"/>
					</xsl:call-template>
				</fo:block>
			</xsl:if>
			<!-- Post Conditions end-->
		</xsl:if>
		<!-- Bail Conditions end -->
	</xsl:template>
	<!-- ###### Appear at Court ###### -->
	<xsl:template match="nar:BW_AF_AppearAtCourt">
		<xsl:if test="$baseAll/ord:Release='Conditional'">
			<fo:inline>
				<xsl:text>to appear at the Crown Court sitting at </xsl:text>
				<xsl:call-template name="CallableNextAppearanceCourtHouseName"/>
				<xsl:text> (or such other place as shall be notified)</xsl:text>
			</fo:inline>
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
					<xsl:text> on such day and at such time as the court may direct</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>, there to surrender </xsl:text>
			<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails">
				<xsl:call-template name="HimselfHerself"/>
			</xsl:for-each>
			<xsl:text> into custody.</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- ************************************** -->
	<!-- BENCH WARRANT ORDER END -->
	<!-- ************************************** -->
</xsl:stylesheet>
