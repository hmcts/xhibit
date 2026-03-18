<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ********************************** -->
	<!-- SUSPENDED SENTENCE ORDER START -->
	<!-- ********************************** -->
	<xsl:variable name="Officer1">
		<xsl:value-of select="$baseAll/ord:ResponsibleOfficer1"/>
	</xsl:variable>
	<xsl:variable name="Officer2">
		<xsl:value-of select="$baseAll/ord:ResponsibleOfficer2"/>
	</xsl:variable>
	<!-- Template to display SUS Title -->
	<xsl:template match="nar:SUS_Title">
		<!-- Check to see if this is a revised Suspended Sentence Order -->
		<xsl:if test="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:RevisedOrder='true'">
			<xsl:text>Change of </xsl:text>
		</xsl:if>
		<xsl:text>Suspended Sentence Order</xsl:text>
	</xsl:template>
	<!-- Template to display SUS Revision Text -->
	<xsl:template match="nar:SUS_Revision">
		<!-- Check to see if this is a revised Suspended Sentence Order -->
		<xsl:if test="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:RevisedOrder='true'">
			<xsl:text>This order has been revised on </xsl:text>
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$OrderDate"/>
			</xsl:call-template>
			<xsl:text> and replaces the previous order made on  </xsl:text>
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:PreviousOrderDate"/>
			</xsl:call-template>
			<fo:block space-before="10pt">
				<xsl:text>Revision number: </xsl:text>
				<xsl:value-of select="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:RevisionNo"/>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<!-- Order title -->
	<xsl:template match="nar:SUS_OrderTitle">
		<fo:inline>
			<xsl:text>Order</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display SUS Order Text -->
	<xsl:template match="nar:SUS_Order">
		<xsl:text>You have been given a Suspended Sentence Order and it is set out in full below. </xsl:text>
		<fo:block space-after="10pt"/>
		<xsl:if test="not($baseAll/ord:OrderRequirements/@selected) or $baseAll/ord:OrderRequirements/@selected='true'">		
			<xsl:text>There </xsl:text>
			<xsl:call-template name="calculateNumRequirements"/>
			<xsl:text> to this order.</xsl:text>
			<fo:block space-after="10pt"/>
		</xsl:if>
	</xsl:template>
	<!-- Imprisonment Title -->
	<xsl:template match="nar:SUS_ImpTitle">
		<fo:inline>
			<xsl:value-of select="$baseAll/ord:WarningDetentionType"/>
		</fo:inline>
	</xsl:template>
	<!-- Template to display SUS order imprisonment section -->
	<xsl:template match="nar:SUS_Imp">
		<xsl:text>You have been sentenced to a term of </xsl:text> 
		<xsl:choose>
		<xsl:when test="$baseAll/ord:WarningDetentionType='Imprisonment'">imprisonment</xsl:when>
		<xsl:when test="$baseAll/ord:WarningDetentionType='Detention'">detention</xsl:when>
		</xsl:choose>
		<xsl:text> lasting </xsl:text>
<!--		<xsl:for-each select="$baseAll/ord:ImprisonmentTerm/ord:Term">
			
			<xsl:call-template name="TermDuration"/>
		</xsl:for-each>.-->
		<xsl:call-template name="displayImprisonmentTerm">
			<xsl:with-param name="years" select="$baseAll/ord:ImprisonmentPeriod/ord:Years"/>
			<xsl:with-param name="months" select="$baseAll/ord:ImprisonmentPeriod/ord:Months"/>
			<xsl:with-param name="weeks" select="$baseAll/ord:ImprisonmentPeriod/ord:Weeks"/>
			<xsl:with-param name="days" select="$baseAll/ord:ImprisonmentPeriod/ord:Days"/>
		</xsl:call-template><xsl:text>.</xsl:text>
		<fo:block space-after="10pt"/>
		<xsl:text>If you </xsl:text>
		<fo:block/>
		<fo:list-block>
		<xsl:if test="not($baseAll/ord:OrderRequirements/@selected) or $baseAll/ord:OrderRequirements/@selected='true'">
			<fo:list-item space-after="6pt">
				<fo:list-item-label>
					<fo:block>
						<xsl:text>&#x2219;</xsl:text>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()" end-indent="5mm">
					<fo:block>
						do not comply with the <fo:inline font-weight="bold">requirements</fo:inline> listed below during the supervision period; or
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
		</xsl:if>
		<fo:list-item space-after="6pt">
			<fo:list-item-label>
				<fo:block>
					<xsl:text>&#x2219;</xsl:text>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()" end-indent="5mm">
				<fo:block>
					commit any other offence during the  <fo:inline font-weight="bold"> operational period</fo:inline> of
					<xsl:value-of select="$baseAll/ord:OperationalPeriod/ord:Months"/> month(s),
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
		</fo:list-block>
		<fo:block>
			you may be liable to serve the <fo:inline font-weight="bold"> custodial period</fo:inline> in
			<xsl:choose>
				<xsl:when test="$baseAll/ord:WarningDetentionType='Imprisonment'">prison.</xsl:when>
				<xsl:when test="$baseAll/ord:WarningDetentionType='Detention'">a Young Offenders Institution.</xsl:when>
			</xsl:choose>
		</fo:block>
	</xsl:template>
	<xsl:template name="displayImprisonmentTerm">
		<xsl:param name="years"/>
		<xsl:param name="months"/>
		<xsl:param name="weeks"/>
		<xsl:param name="days"/>
		<xsl:if test="$years != '' and  $years != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$years"/>
			<xsl:text> year(s)</xsl:text>
		</xsl:if>
		<xsl:if test="$months != '' and  $months != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$months"/>
			<xsl:text> month(s)</xsl:text>
		</xsl:if>
		<xsl:if test="$weeks != '' and  $weeks != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$weeks"/>
			<xsl:text> week(s)</xsl:text>
		</xsl:if>
		<xsl:if test="$days != '' and  $days != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$days"/>
			<xsl:text> day(s)</xsl:text>
		</xsl:if>	
	</xsl:template>
	<!-- Requirements title -->
	<xsl:template match="nar:SUS_RequirementTitle">
		<xsl:if test="not($baseAll/ord:OrderRequirements/@selected) or $baseAll/ord:OrderRequirements/@selected='true'">
			<fo:inline>
				<xsl:text>Requirements</xsl:text>
			</fo:inline>
		</xsl:if>
	</xsl:template>
	<!-- Calculate the number of  Requirements  -->
	<xsl:template name="calculateNumRequirements">
		<xsl:if test="not($baseAll/ord:OrderRequirements/@selected) or $baseAll/ord:OrderRequirements/@selected='true'">
			<xsl:variable name="reqs" select="$baseAll/ord:OrderRequirements/*/@selected = 'true'"/>
			<xsl:variable name="reqNum" select=" count($baseAll/ord:OrderRequirements/*[@selected = 'true'] ) "/>
			<!-- add one if electronic req selected -->
			<xsl:variable name="ECSum">
				<xsl:if test="$baseAll/ord:ElectronicMonitoringRequirement/@selected = 'true'">
					<xsl:value-of select="$reqNum + 1"/>
				</xsl:if>
				<xsl:if test="$baseAll/ord:ElectronicMonitoringRequirement/@selected = 'false'">
					<xsl:value-of select="$reqNum"/>
				</xsl:if>
			</xsl:variable>
			<!-- add one if Curfew option selected -->
			<xsl:variable name="CurfSum">
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewOption = 'yes'">
					<xsl:value-of select="$ECSum + 1"/>
				</xsl:if>
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewOption != 'yes'">
					<xsl:value-of select="$ECSum"/>
				</xsl:if>
			</xsl:variable>
<!-- add one if alcohol abstinence option selected -->
			<xsl:variable name="AlcSum">
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholAbstinenceAndMonitoringRequirement/@selected = 'true'">
					<xsl:value-of select="$CurfSum + 1"/>
				</xsl:if>
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholAbstinenceAndMonitoringRequirement/@selected = 'false'">
					<xsl:value-of select="$CurfSum"/>
				</xsl:if>
			</xsl:variable>
			<!-- add one if Attendance option selected -->
			<xsl:variable name="finSum">
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:AttendanceCentreRequirement/ord:AttendanceOption = 'yes'">
					<xsl:value-of select="$AlcSum + 1"/>
				</xsl:if>
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:AttendanceCentreRequirement/ord:AttendanceOption != 'yes'">
					<xsl:value-of select="$AlcSum"/>
				</xsl:if>
			</xsl:variable>
			<xsl:if test="$finSum &gt; 1">
				<xsl:text>are </xsl:text>
				<xsl:value-of select="$finSum"/>
				<xsl:text> requirements</xsl:text>
			</xsl:if>
			<xsl:if test="$finSum &lt; 2">
				<xsl:text>is </xsl:text>
				<xsl:value-of select="$finSum"/>
				<xsl:text> requirement</xsl:text>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<!-- Template to display SUS Requirements text -->
	<xsl:template match="nar:SUS_Requirement">
		<xsl:if test="not($baseAll/ord:OrderRequirements/@selected) or $baseAll/ord:OrderRequirements/@selected='true'">		
			<fo:block space-after="10pt">
				For the supervision period which is the next
				<xsl:value-of select="$baseAll/ord:SupervisionPeriod/ord:Months"/> month(s), you will be supervised by
				<xsl:value-of select="$Officer2"/><xsl:text>.</xsl:text>
			</fo:block>
			<fo:block>
				You <fo:inline font-weight="bold">must</fo:inline>
			</fo:block>
			<fo:block space-after="10pt"/>
			<fo:list-block>
			<fo:list-item space-after="6pt">
				<fo:list-item-label>
					<fo:block>
						<xsl:text>&#x2219;</xsl:text>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()" end-indent="5mm">
					<fo:block>
						<xsl:text>keep in touch with your </xsl:text>
						<xsl:value-of select="$Officer1"/>
						<xsl:text> as your </xsl:text>
						<xsl:value-of select="$Officer2"/>
						<xsl:text> tells you.</xsl:text>
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			<fo:list-item space-after="6pt">
				<fo:list-item-label>
					<fo:block>
						<xsl:text>&#x2219;</xsl:text>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()" end-indent="5mm">
					<fo:block>
						<xsl:text>tell your </xsl:text>
						<xsl:value-of select="$Officer2"/>
						<xsl:text> if you intend to change your address.</xsl:text>
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			<fo:list-item space-after="6pt">
				<fo:list-item-label>
					<fo:block>
						<xsl:text>&#x2219;</xsl:text>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()" end-indent="5mm">
					<fo:block>
						comply with the following requirements
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			</fo:list-block>
			<fo:block>
				You must
			</fo:block>
			<fo:block space-after="10pt"/>
			<!--  Unpaid Work Requirement-->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Unpaid Work Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>carry out unpaid work for </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Hours"/>
								<xsl:text> hours </xsl:text>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent='no'">
										<xsl:text> consecutive to </xsl:text>
									</xsl:when>
									<xsl:when test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent='yes'">
										<xsl:text> concurrent to </xsl:text>
									</xsl:when>
									<xsl:when test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent='none'">
										<xsl:text> </xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent != 'none'">
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:WorkDetails"/>
								</xsl:if>
								<xsl:text> as you are told by </xsl:text>
								<xsl:call-template name="FormatDate">
									<xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:WorkEndDate"/>
								</xsl:call-template>
								<xsl:text>. Your Probation Officer will tell you who will be responsible for supervising work.</xsl:text>
							</fo:block>
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:AdditonalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:AdditonalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Activity Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/@selected='true'">
				<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true' or $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true' or $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:AdditonalRequirements/@selected='true'">
					<fo:block font-weight="bold">
						<xsl:text>Activity Requirement</xsl:text>
					</fo:block>
					<fo:list-block provisional-distance-between-starts="5mm">
						<fo:list-item space-after="10pt">
							<fo:list-item-label>
								<fo:block font-weight="bold">
									<xsl:text> </xsl:text>
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="body-start()" end-indent="5mm">
								<fo:block space-before="15pt">
									<!-- Option 1 -->
									<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true'">
										<xsl:text>present yourself to </xsl:text>
										<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/ord:Person"/>
										<xsl:text> at </xsl:text>
										<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/ord:Place/ord:Site"/>
									</xsl:if>
									<!-- and -->
									<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true' and $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true'">
										<xsl:text> and </xsl:text>
									</xsl:if>
									<!-- Option 2 -->
									<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true'">
										<xsl:text>undertake </xsl:text>
										<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/ord:Activity"/>
										<xsl:text> for </xsl:text>
										<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/ord:ActivityPeriod">
											<!-- Display duration details -->
											<xsl:call-template name="TermMonthsDays"/>
										</xsl:for-each>
										<xsl:text> in the way you are told by your </xsl:text>
										<xsl:value-of select="$Officer2"/>
										<xsl:text>.</xsl:text>
									</xsl:if>
								</fo:block>
								<!-- Additional Req -->
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:AdditonalRequirements/@selected='true'">
									<xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true' or $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true'">
										<xsl:text>.  </xsl:text>
									</xsl:if>
									<xsl:call-template name="FormatBulletTextArea">
										<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:AdditonalRequirements/ord:AdditionalInformation"/>
									</xsl:call-template>
								</xsl:if>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
				</xsl:if>
			</xsl:if>
			<!-- Programme Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Programme Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>participate in </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:Programme"/>
								<xsl:text> at </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:Place/ord:Site"/>
								<xsl:text> for </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:Days"/>
								<xsl:text> days.  </xsl:text>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Prohibited Activity Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Prohibited Activity Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>not take part in </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:Activity"/>
								<xsl:text> for </xsl:text>
								<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:ActivityPeriod">
									<!-- Display duration details -->
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
								<xsl:text>.  </xsl:text>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Curfew Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewOption='yes'">
				<fo:block font-weight="bold">
					<xsl:text>Curfew Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>remain in a place or places so specified during periods specified by the court. This curfew requirement lasts for </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewDuration/ord:Duration"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewDuration/ord:Period"/>
								<fo:inline font-style="italic">. See separate Electronic Monitoring sheet for details</fo:inline>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Exclusion Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Exclusion Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>not enter </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:Place/ord:Site"/>
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:BetweenPeriod/@selected='true'">
									<xsl:text> between </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:BetweenPeriod/ord:ApplicablePeriod"/>
								</xsl:if>
								<xsl:text>.  </xsl:text>
								<xsl:text>This exclusion requirement lasts for </xsl:text>
								<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:ExclusionPeriod">
									<!-- Display duration details -->
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each><xsl:text>.</xsl:text>
								<!-- Additional Req -->
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:AdditionalRequirements/@selected='true'">
									<xsl:call-template name="FormatBulletTextArea">
										<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
									</xsl:call-template>
								</xsl:if>
								<fo:inline font-style="italic"> See separate Electronic Monitoring sheet for details</fo:inline>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Residence Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Residence Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>live at </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:Hostel/ord:Site"/>
								<xsl:text> and obey any rules that apply there for </xsl:text>
								<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:ObeyRules">
									<!-- Display duration details -->
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
								<xsl:text>.  </xsl:text>
								<!-- Another Place -->
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:PlaceOption/@selected='true'">
									<xsl:text>You may live at </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:PlaceOption/ord:Place/ord:Site"/>
									<xsl:text> with the prior approval of your </xsl:text>
									<xsl:value-of select="$Officer2"/>
									<xsl:text>.  </xsl:text>
								</xsl:if>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Mental Health Treatment Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Mental Health Treatment Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>have mental health treatment by or under the direction of a </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentDirector"/>
								<!-- Clinic -->
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentOption/@selected='true'">
									<xsl:text> at </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentOption/ord:TreatmentLocation/ord:Site"/>
									<xsl:text> as a </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentOption/ord:ResidenceStatus"/>
									<xsl:text> patient </xsl:text>
								</xsl:if>
								<xsl:text> for </xsl:text>
								<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentPeriod">
									<!-- Display duration details -->
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
								<xsl:text>.</xsl:text>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Drug Rehabilitation Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Drug Rehabilitation Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>have treatment for drug dependency by or under the direction of  </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentDirector"/>
								<!-- Clinic -->
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentOption/@selected='true'">
									<xsl:text> at </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentOption/ord:TreatmentLocation/ord:Site"/>
									<xsl:text> as a </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentOption/ord:ResidenceStatus"/>
								</xsl:if>
								<xsl:text> for </xsl:text>
								<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentPeriod">
									<!-- Display duration details -->
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
								<xsl:text>.  </xsl:text>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:SampleOption/@selected='true'">
				<fo:list-block provisional-distance-between-starts="5mm">
					<xsl:call-template name="no_bullet">
						<xsl:with-param name="text">
							<xsl:text>To be sure that you do not have any illegal drug in your body, you must provide samples for testing 
						at such times or in such circumstances as your </xsl:text>
							<xsl:value-of select="$Officer2"/>
							<xsl:text> or the person responsible for your treatment will tell you.  The results of tests on the samples will be sent to your </xsl:text>
							<xsl:value-of select="$Officer2"/>
							<xsl:text> who will report the results to the court.  Your </xsl:text>
							<xsl:value-of select="$Officer2"/>
							<xsl:text> will also tell the court how your order is progressing and the views of your treatment provider.</xsl:text>
						</xsl:with-param>
					</xsl:call-template>
				</fo:list-block>
			</xsl:if>
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/@selected='true'">
				<fo:list-block provisional-distance-between-starts="5mm">
					<xsl:call-template name="no_bullet">
						<xsl:with-param name="text">
							<xsl:text>The court will review this order </xsl:text>
							<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewDetails"/>
							<xsl:text>.  The first review will be on </xsl:text>
							<xsl:call-template name="FormatDate">
								<xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewDate"/>
							</xsl:call-template>
							<xsl:text> at </xsl:text>
							<xsl:call-template name="FormatTime">
								<xsl:with-param name="time" select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewTime"/>
							</xsl:call-template>
							<xsl:text> at </xsl:text>
							<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseName"/>
							<xsl:text>, </xsl:text>
							<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseAddress">
								<xsl:call-template name="CallableAddress_Comm_Order"/>
							</xsl:for-each>
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:TelephoneOption/@selected='true'">
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseTelephone != '-'">
									<xsl:text> (telephone </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseTelephone"/>
									<xsl:text>).  </xsl:text>
								</xsl:if>
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseTelephone = '-'">
									<xsl:text>).  </xsl:text>
								</xsl:if>
							</xsl:if>
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:TelephoneOption/@selected='false'">
								<xsl:text>.  </xsl:text>
							</xsl:if>
							<xsl:text>You</xsl:text>
							<xsl:choose>
								<xsl:when test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:Attendance = 'yes'">
									<xsl:text> must attend the hearing.</xsl:text>
								</xsl:when>
								<xsl:when test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:Attendance = 'no'">
									<xsl:text> need not attend the hearing.</xsl:text>
								</xsl:when>
							</xsl:choose>
						</xsl:with-param>
					</xsl:call-template>
				</fo:list-block>
			</xsl:if>
			<!-- Alcohol Treatment Requirement-->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Alcohol Treatment Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>have treatment for alcohol dependency by or under the direction of </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentDirector"/>
								<!-- Clinic -->
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentOption/@selected='true'">
									<xsl:text> at </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentOption/ord:TreatmentLocation/ord:Site"/>
									<xsl:text> as a </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentOption/ord:ResidenceStatus"/>
								</xsl:if>
								<xsl:text> for </xsl:text>
								<xsl:for-each select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentPeriod">
									<!-- Display duration details -->
									<xsl:call-template name="TermDuration"/>
								</xsl:for-each>
								<xsl:text>.  </xsl:text>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Supervision Treatment Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:SupervisionRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Supervision Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>attend appointments with your </xsl:text>
								<xsl:value-of select="$Officer2"/>
								<xsl:text> or another person at the times and places your </xsl:text>
								<xsl:value-of select="$Officer2"/>
								<xsl:text> says.  </xsl:text>
							</fo:block>
							<!-- Additional Req -->
							<xsl:if test="$baseAll/ord:OrderRequirements/ord:SupervisionRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:SupervisionRequirement/ord:AdditionalRequirements/@selected='true'">
								<xsl:call-template name="FormatBulletTextArea">
									<xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:SupervisionRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
								</xsl:call-template>
							</xsl:if>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Attendance Centre Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:AttendanceCentreRequirement/ord:AttendanceOption='yes'">
				<fo:block font-weight="bold">
					<xsl:text>Attendance Centre Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<fo:block>attend an attendance centre - see separate sheet for details</fo:block>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Foreign Travel Prohibition Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Foreign Travel Prohibition Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="1em">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:Days!=''">
									<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:Days!=' '">
										<xsl:text> on </xsl:text>
										<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:Days"/>
										<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/@selected='true'">
											<xsl:text> or</xsl:text>
										</xsl:if>
									</xsl:if>
								</xsl:if>
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/@selected='true'">
									<xsl:text> from </xsl:text>
									<xsl:call-template name="FormatDate">
										<xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/ord:FromDate"/>
									</xsl:call-template>
									<xsl:text> to </xsl:text>
									<xsl:call-template name="FormatDate">
										<xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/ord:ToDate"/>
									</xsl:call-template>
								</xsl:if>
								<xsl:text> you are not to travel to </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:ProhibitedFrom"/>
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:ExceptionOption/@selected='true'">
									<xsl:text> other than </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:ExceptionOption/ord:Exception"/>
								</xsl:if>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Rehabilitation Activity Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:RehabilitationActivityRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Rehabilitation Activity Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="1em">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text> Participate in Rehabilitation Activity Requirement(s) as instructed for a maximum of </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:RehabilitationActivityRequirement/ord:Days"/>
								<xsl:text> days.</xsl:text>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Alcohol Abstinence Monitoring Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholAbstinenceAndMonitoringRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Alcohol Abstinence and Monitoring Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="1em">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text> Abstain from alcohol and be monitored for </xsl:text>
								<xsl:variable name="alcDays">
										<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholAbstinenceAndMonitoringRequirement/ord:Days"/>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="$alcDays != ''">
										<xsl:value-of select="$alcDays"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>0</xsl:text>
									</xsl:otherwise>
								</xsl:choose>									
								<xsl:text> days.</xsl:text>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Electronic Monitoring Provision -->
			<xsl:if test="$baseAll/ord:ElectronicMonitoringRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Electronic Monitoring Provision</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<fo:block>You will be electronically monitored so that the court can be sure you are complying with the requirements of your order.</fo:block>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
			<!-- Trail Monitoring Requirement -->
			<xsl:if test="$baseAll/ord:OrderRequirements/ord:TrailMonitoringRequirement/@selected='true'">
				<fo:block font-weight="bold">
					<xsl:text>Trail Monitoring Requirement</xsl:text>
				</fo:block>
				<fo:list-block provisional-distance-between-starts="5mm">
					<fo:list-item space-after="10pt">
						<fo:list-item-label>
							<fo:block font-weight="bold">
								<xsl:text> </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="body-start()" end-indent="5mm">
							<fo:block space-before="15pt">
								<xsl:text>You will be electronically Trail monitored for </xsl:text>
								<xsl:choose>
									<xsl:when test="$baseAll/ord:OrderRequirements/ord:TrailMonitoringRequirement/ord:Duration != ''">
										<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:TrailMonitoringRequirement/ord:Duration"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>0</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text> days so that the court can be sure you are complying with the requirements of this order.</xsl:text>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<!-- Review Title -->
	<xsl:template match="nar:SUS_ReviewTitle">
		<fo:inline>
			<xsl:if test="$baseAll/ord:ReviewType/@selected='true'">
				<xsl:if test="not($baseAll/ord:OrderRequirements/@selected) or $baseAll/ord:OrderRequirements/@selected='true'">
					<xsl:text>Review</xsl:text>
				</xsl:if>
				<xsl:if test="$baseAll/ord:OrderRequirements/@selected='false'">
					<xsl:text>Review</xsl:text>
				</xsl:if>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!--  SUS Review text -->
	<xsl:template match="nar:SUS_Review">
		<xsl:if test="$baseAll/ord:ReviewType/@selected='true'">
			<xsl:text>The court will review this order </xsl:text>
			<xsl:value-of select="$baseAll/ord:ReviewType/ord:ReviewDetails"/>
			<xsl:if test="$baseAll/ord:ReviewType/ord:FirstReview/@selected='true'">
				<xsl:text>.  The first review will be on </xsl:text>
				<xsl:call-template name="FormatDate">
					<xsl:with-param name="date" select="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewDate"/>
				</xsl:call-template>
				<xsl:text> at </xsl:text>
				<xsl:call-template name="FormatTime">
					<xsl:with-param name="time" select="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewTime"/>
				</xsl:call-template>
				<xsl:text> at </xsl:text>
				<xsl:value-of select="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewCourt/ord:CourtHouseName"/>
				<xsl:text>, </xsl:text>
				<xsl:for-each select="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewCourt/ord:CourtHouseAddress">
					<xsl:call-template name="CallableAddress_Comm_Order"/>
				</xsl:for-each>
				<xsl:if test="$baseAll/ord:ReviewType/ord:FirstReview/ord:TelephoneOption/@selected='true'">
					<xsl:if test="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewCourt/ord:CourtHouseTelephone != '-'">
						<xsl:text> (telephone </xsl:text>
						<xsl:value-of select="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewCourt/ord:CourtHouseTelephone"/>
						<xsl:text>).  </xsl:text>
					</xsl:if>
					<xsl:if test="$baseAll/ord:ReviewType/ord:FirstReview/ord:ReviewCourt/ord:CourtHouseTelephone = '-'">
						<xsl:text>).  </xsl:text>
					</xsl:if>
				</xsl:if>
				<xsl:if test="$baseAll/ord:ReviewType/ord:FirstReview/ord:TelephoneOption/@selected='false'">
					<xsl:text>.  </xsl:text>
				</xsl:if>
				<xsl:text>You</xsl:text>
				<xsl:choose>
					<xsl:when test="$baseAll/ord:ReviewType/ord:FirstReview/ord:Attendance = 'yes'">
						<xsl:text> must attend the hearing.</xsl:text>
					</xsl:when>
					<xsl:when test="$baseAll/ord:ReviewType/ord:FirstReview/ord:Attendance = 'no'">
						<xsl:text> need not attend the hearing.</xsl:text>
					</xsl:when>
				</xsl:choose>
			</xsl:if>
			<xsl:if test="$baseAll/ord:ReviewType/ord:FirstReview/@selected='false'">
				<xsl:text>.</xsl:text>
			</xsl:if>
		</xsl:if>
		<!-- Petty Sessional Area -->
		<fo:block space-after="10pt">
			<xsl:text>The Local Justice Area you live in is </xsl:text>
			<xsl:value-of select="$baseAll/ord:PettySessionalArea/ord:CourtHouseName"/>
			<xsl:text>.</xsl:text>
		</fo:block>
	</xsl:template>
	<!-- Warning Title -->
	<xsl:template match="nar:SUS_WarningTitle">
		<fo:inline>
			<xsl:text>Warning</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display SUS Warning text -->
	<xsl:template match="nar:SUS_Warning">
		<fo:block>
			<xsl:text>If you do not comply with this order, you will be brought back to court.  The court may then</xsl:text>
			<fo:list-block provisional-distance-between-starts="5mm">
				<xsl:call-template name="bullet">
					<xsl:with-param name="text">
						<xsl:text>change the order by adding extra requirements</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
			</fo:list-block>
			<fo:list-block provisional-distance-between-starts="5mm">
				<xsl:call-template name="bullet">
					<xsl:with-param name="text">
						<xsl:text>pass a different sentence for the original offences; or</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
			</fo:list-block>
			<fo:list-block provisional-distance-between-starts="5mm">
				<xsl:call-template name="bullet">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="$baseAll/ord:WarningDetentionType='Detention'">
								<xsl:text>you could be sent to a Young Offenders Institution.</xsl:text>
							</xsl:when>
							<xsl:when test="$baseAll/ord:WarningDetentionType='Imprisonment'">
								<xsl:text>you could be sent to prison.</xsl:text>
							</xsl:when>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</fo:list-block>
		</fo:block>
	</xsl:template>
	<!-- Note title -->
	<xsl:template match="nar:SUS_NoteTitle">
		<fo:inline>
			<xsl:text>Note</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display SUS Notes text -->
	<xsl:template match="nar:SUS_Note">
		<xsl:text>Either you or your </xsl:text>
		<xsl:value-of select="$Officer2"/>
		<xsl:text> can ask the court to look again at this order and the court can then change it or cancel it if it feels that is the right thing to do.  
		The court may also pass a different sentence for the original offence(s).  If you wish to ask the court to look at your order again
		you should get in touch with the court at the address above.</xsl:text>
	</xsl:template>
	<!-- Template to display SUS Signed text -->
	<xsl:template match="nar:SUS_Signed">
		<!-- call template to display signed info -->
		<xsl:call-template name="SignedInfo"/>
	</xsl:template>
	<!-- Offence title -->
	<xsl:template match="nar:SUS_OffencesTitle">
		<fo:inline>
			<xsl:text>Offences</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display SUS Offences text -->
	<xsl:template match="nar:SUS_Offences">
		<xsl:choose>
			<xsl:when test="count($baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement)=1">
				<xsl:call-template name="FormatBulletTextArea">
					<xsl:with-param name="string" select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge[1]/ord:OffenceStatement"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge">
					<xsl:if test="position()!=1">
						<xsl:if test="ord:OffenceStatement != ''">
							<xsl:value-of select="ord:CaseNumber"/>
							<xsl:text> / </xsl:text>
							<xsl:value-of select="ord:OffenceStatement"/>
							<fo:block/>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
		<fo:block/>
		<!-- Check to if there are any linked offences for the same defendant to display -->
		<xsl:if test="$baseAll/ord:LinkedOffences">
			<fo:block>
				<xsl:for-each select="$baseAll/ord:LinkedOffences/ord:LinkedOffence">
					<xsl:value-of select="ord:LinkedCaseNumber"/>
					<xsl:text> / </xsl:text>
					<xsl:value-of select="ord:LinkedOffenceStatement"/>
				</xsl:for-each>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<xsl:template match="nar:SUS_AttachementsTitle">
		<fo:inline>
			<xsl:text>Attached are:</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:SUS_Attachements">
		<fo:inline>
			<fo:block space-after="5pt"/>
			<xsl:if test="$baseAll/ord:Attachments/ord:previousConvictionList/@selected = 'true' ">
				<fo:list-block provisional-distance-between-starts="5mm">
				<xsl:call-template name="bullet">
					<xsl:with-param name="text">
						<xsl:text>List of previous convictions</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
				</fo:list-block>
			</xsl:if>
			<xsl:if test="$baseAll/ord:Attachments/ord:preSentenceReport/@selected = 'true' ">
				<fo:list-block provisional-distance-between-starts="5mm">
				<xsl:call-template name="bullet">
					<xsl:with-param name="text">
						<xsl:text>Pre-sentence report</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
				</fo:list-block>
			</xsl:if>
			<xsl:if test="$baseAll/ord:Attachments/ord:medicalReport/@selected = 'true'">
				<fo:list-block provisional-distance-between-starts="5mm">
				<xsl:call-template name="bullet">
					<xsl:with-param name="text">
						<xsl:text>Medical Report</xsl:text>
					</xsl:with-param>
				</xsl:call-template>
				</fo:list-block>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Additional Req title-->
	<xsl:template match="nar:SUS_AddReq_Title">
		<fo:inline>
			<xsl:text>Additional Notes</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Additional Notes details -->
	<xsl:template match="nar:SUS_AdditionalNotes">
		<xsl:call-template name="FormatTextArea">
			<xsl:with-param name="string" select="$baseAll/ord:AdditionalNotes"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ********************************** -->
	<!-- SUSPENDED SENTENCE ORDER END -->
	<!-- ********************************** -->
</xsl:stylesheet>
