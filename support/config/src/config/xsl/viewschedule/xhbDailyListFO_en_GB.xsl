<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:dt="http://xsltsl.org/date-time" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	<!-- 
		COMMENTS
		Transformer used by Xhibit for display of the Daily List, Tomorrow's List and MLD to transform the Daily List into PDF
	-->

	<!-- Global Variable to hold the Current House House Code -->
	<xsl:variable name="CurrentCourtCode">
		<xsl:value-of select="/DailyList/CourtLists/CourtList[1]/CourtHouse/CourtHouseCode"/>
	</xsl:variable>

	<!-- Global Variable to hold the CourtNames from the Court List -->
	<xsl:variable name="CourtNames">
		<!-- display the first court name -->
		<xsl:call-template name="TitleCase">
			<xsl:with-param name="text">
				<xsl:value-of select="/DailyList/CourtLists/CourtList[1]/CourtHouse/CourtHouseName"/>
			</xsl:with-param>
		</xsl:call-template>					

		<!-- loop rest of court lists and pick up those which are not of the same court code as the main court (first court list) -->
		<xsl:for-each select="/DailyList/CourtLists/CourtList[not(position() = 1)]/CourtHouse/CourtHouseCode[not(. = $CurrentCourtCode)]">
			<xsl:choose>
				<!-- if not last display comma and name -->
				<xsl:when test="position() != last()">
					<xsl:text>, </xsl:text>
					<xsl:call-template name="TitleCase">
						<xsl:with-param name="text">
							<xsl:value-of select="../CourtHouseName"/>
						</xsl:with-param>
					</xsl:call-template>					
				</xsl:when>
				<!-- when last display 'and ' name -->
				<xsl:otherwise>
					<xsl:text> and </xsl:text>
					<xsl:call-template name="TitleCase">
						<xsl:with-param name="text">
							<xsl:value-of select="../CourtHouseName"/>
						</xsl:with-param>
					</xsl:call-template>					
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:variable>
	
	<xsl:variable name="CourtNames2">
		<!-- display the first court name -->
		<xsl:value-of select="/DailyList/CourtLists/CourtList[1]/CourtHouse/CourtHouseName"/>

		<!-- loop rest of court lists and pick up those which are not of the same court code as the main court (first court list) -->
		<xsl:for-each select="/DailyList/CourtLists/CourtList[not(position() = 1)]/CourtHouse/CourtHouseCode[not(. = $CurrentCourtCode)]">
			<xsl:choose>
				<!-- if not last display comma and name -->
				<xsl:when test="position() != last()">
					<xsl:text>, </xsl:text>
					<xsl:value-of select="../CourtHouseName"/>
				</xsl:when>
				<!-- when last display 'and ' name -->
				<xsl:otherwise>
					<xsl:text> AND </xsl:text>
					<xsl:value-of select="../CourtHouseName"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:variable>

	<!-- Pick up the first court house type from the Court List -->
	<xsl:variable name="CourtType">
		<xsl:for-each select="DailyList/CourtLists/CourtList[1]/CourtHouse/CourtHouseType">
			<xsl:value-of select="."/>
		</xsl:for-each>
	</xsl:variable>
	
	<xsl:template match="DailyList">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<!-- set up page for first page -->
				<fo:simple-page-master master-name="first" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
					<fo:region-body margin-top="15mm" margin-bottom="10mm"/>
					<fo:region-before extent="30mm" region-name="header-first"/>
					<fo:region-after extent="15mm"/>
				</fo:simple-page-master>
				<!-- set up page for rest of pages -->
				<fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
					<fo:region-body margin-top="15mm" margin-bottom="10mm"/>
					<fo:region-before extent="25mm" region-name="header-rest"/>
					<fo:region-after extent="15mm"/>
				</fo:simple-page-master>
				<!-- set up page conditions -->
				<fo:page-sequence-master master-name="document">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference page-position="first" master-reference="first"/>
						<fo:conditional-page-master-reference page-position="rest" master-reference="rest"/>
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>
			<!-- set up main document -->
			<fo:page-sequence master-reference="document" format="1">
				<!-- Display info for header on first page -->
				<fo:static-content flow-name="header-first">
					<fo:block>
					<fo:table table-layout="fixed">
						<fo:table-column column-width="150mm"/>
						<fo:table-column column-width="30mm"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<!-- call template to display Crown Court details -->
									<xsl:call-template name="CourtDetails"/>
								</fo:table-cell>
								<fo:table-cell text-align="right">
									<!-- display the page no -->
									<fo:block font-size="9pt">
										Page No: <fo:page-number/>
										<xsl:text> of </xsl:text>
										<fo:page-number-citation ref-id="last-page"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					</fo:block>
				</fo:static-content>
				<!-- Display info for all headers apart from the first -->
				<fo:static-content flow-name="header-rest">
					<!-- call template to display header info -->
					<xsl:call-template name="displayHeaderRest"/>
				</fo:static-content>
				<!-- Footer Info -->
				<fo:static-content flow-name="xsl-region-after">
					<fo:block>
						<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
					</fo:block>
					<!-- call template to display Footer Info -->
					<xsl:call-template name="ListFooter"/>
				</fo:static-content>
				<!-- Main Body for document-->
				<fo:flow flow-name="xsl-region-body">
					<!-- call template to display the header info -->
					<xsl:call-template name="HeaderInfo"/>
					<fo:block space-after="8pt">
						<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
					</fo:block>
					<!-- display court list info -->
					<xsl:apply-templates select="CourtLists"/>
					<fo:block id="last-page"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<!-- 		
			**************************
			TEMPLATE MATCHES 
			*************************
	-->
	<!-- display Court List Info -->
	<xsl:template match="CourtLists">
		<xsl:for-each select="CourtList">
			<xsl:if test="position() != 1">
				<fo:block break-before="page"/>
			</xsl:if>
			<xsl:if test="count(./CourtHouse/CourtHouseName) = 1">
				<!-- display the court house name -->
				<fo:block font-weight="bold" space-after="10pt">
					<xsl:value-of select="./CourtHouse/CourtHouseName"/>
				</fo:block>
			</xsl:if>
			<xsl:for-each select="Sittings/Sitting">
			    <xsl:sort select="SittingPriority"  order="ascending"/>
			    <xsl:sort select="CourtRoomNumber" data-type="number"/>
				<!--
				'SittingPriority' logic used to determine if the case is floating
		   	true = floating
			-->
				<xsl:choose>
					<!-- not floating display court room info -->
					<xsl:when test="not(SittingPriority = 'true')">
						<fo:block font-size="10pt">
							<!-- pick out the Court Room number -->
							<xsl:choose>
								<xsl:when test="string-length(translate(CourtRoomNumber, translate(CourtRoomNumber,'-0123456789',''),''))>0">
									<xsl:text>Court </xsl:text>
									<xsl:value-of select="translate(CourtRoomNumber, translate(CourtRoomNumber,'-0123456789',''),'')"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="CourtRoomNumber"/>
								</xsl:otherwise>
							</xsl:choose>
							<!-- if hearings then display sitting at time otherwise display 'NOT SITTING' text-->
							<xsl:choose>
								<!--<xsl:when test="count(Hearings/Hearing/CaseNumber) &gt; 0">-->
								<xsl:when test="SittingAt">
								    <xsl:variable name="SittingAtTime">
								         <xsl:apply-templates select="SittingAt"/>
								    </xsl:variable>
                                                        <xsl:choose>
                                                            <!-- Check to ensure that there is a valid sitting at time -->
                                                            <xsl:when test="substring($SittingAtTime,1,5) != '00:00'">
                                                                <xsl:text> - sitting at </xsl:text>
                                                                <xsl:apply-templates select="SittingAt"/>
                                                            </xsl:when>
                                                        </xsl:choose>
								</xsl:when>
								<!--</xsl:when>-->
								<!--<xsl:otherwise>-->
								<!--<xsl:text> - NOT SITTING</xsl:text>-->
								<!--</xsl:otherwise>-->
							</xsl:choose>
						</fo:block>
					</xsl:when>
					<xsl:otherwise>
						<fo:block font-weight="bold" font-size="10pt">
							<!-- floating, display the following text -->
							<xsl:text>The following may be taken in any court.</xsl:text>
						</fo:block>
					</xsl:otherwise>
				</xsl:choose>
				<!-- display the judge information -->
				<xsl:call-template name="JudgeDetails">
					<xsl:with-param name="Judge" select="Judiciary"/>
				</xsl:call-template>
				<!-- display any sitting note information -->
				<fo:block font-weight="bold" space-after="8pt" font-size="10pt">
					<xsl:if test="SittingNote">
						<xsl:variable name="UPPERSittingNote">
							<xsl:call-template name="toUpper">
								<xsl:with-param name="content" select="SittingNote"/>
							</xsl:call-template>
						</xsl:variable>
						<!-- only display Sitting Note if it does not contain 'NOT SITTING' text as would have been displayed as 
							part of sitting information -->
						<!--<xsl:if test="not(contains($UPPERSittingNote,'NOT SITTING'))">-->
						<xsl:value-of select="SittingNote"/>
						<!--</xsl:if>-->
					</xsl:if>
				</fo:block>
				<!-- display information for each hearing -->
				<xsl:for-each select="Hearings/Hearing">
					<xsl:call-template name="hearing"/>
				</xsl:for-each>
				<fo:block space-after="8pt">
					<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
				</fo:block>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<!-- only display the first 5 character i.e. 10:30 -->
	<!-- Needs to be able to handle both SittingAt data from the Xhibit XML and the CS Xml -->
	<xsl:template match="SittingAt">
		<xsl:choose>
			<!-- Handle SittingAt data 10:00 -->
			<xsl:when test="string-length(.)=5">
				<!-- call template to postfix AM or PM -->
				<xsl:call-template name="FormatTime">
					<xsl:with-param name="input">
						<xsl:value-of select="substring(.,1,5)"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<!-- Handle SittingAt data 9:00 -->
			<xsl:when test="string-length(.)=4">
				<!-- call template to postfix AM or PM -->
				<xsl:call-template name="FormatTime">
					<xsl:with-param name="input">
						<xsl:value-of select="substring(.,1,4)"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<!-- 
				Handle other time formats
				2003-11-21T10:00:00.000Z
			-->
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="contains(.,'T')">
						<!-- variable to hold complete time format -->
						<xsl:variable name="Time">
							<xsl:value-of select="substring-after(.,'T')"/>
						</xsl:variable>
						<!-- extract the hours value from the time -->
						<xsl:variable name="Hours">
							<xsl:value-of select="substring-before($Time,':')"/>
						</xsl:variable>
						<!-- extract the mins value from the time -->
						<xsl:variable name="Mins">
							<xsl:variable name="TempTime">
								<xsl:value-of select="substring-after($Time,':')"/>
							</xsl:variable>
							<xsl:value-of select="substring-before($TempTime,':')"/>
						</xsl:variable>
						<!-- variable to hold complete time value -->
						<xsl:variable name="TimeToFormat">
							<xsl:value-of select="$Hours"/>
							<xsl:text>:</xsl:text>
							<xsl:value-of select="$Mins"/>
						</xsl:variable>
						<!-- call template to format the time -->
						<xsl:call-template name="FormatTime">
							<xsl:with-param name="input">
								<xsl:value-of select="$TimeToFormat"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="."/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 		
			***********************
			TEMPLATE NAMES 
			**********************
	-->
	<!-- from dt:date-time.xsl -->
	<!-- Return number to represent the day of the week
			0	Sunday
			1	Monday
			2	Tuesday
			3	Wednesday
			4	Thursday
			5	Friday
			6	Saturday
	-->
	<xsl:template name="dt:calculate-day-of-the-week">
		<xsl:param name="year"/>
		<xsl:param name="month"/>
		<xsl:param name="day"/>
		<xsl:variable name="a" select="floor((14 - $month) div 12)"/>
		<xsl:variable name="y" select="$year - $a"/>
		<xsl:variable name="m" select="$month + 12 * $a - 2"/>
		<xsl:value-of select="($day + $y + floor($y div 4) - floor($y div 100) + floor($y div 400) + floor((31 * $m) div 12)) mod 7"/>
	</xsl:template>
	<!-- Display Court Details from CourtList info -->
	<xsl:template name="CourtDetails">
		<fo:block font-size="16pt">
			<fo:inline font-weight="bold">
				The
				<!-- Court Type -->
				<xsl:call-template name="TitleCase">
					<xsl:with-param name="text">
						<xsl:value-of select="$CourtType"/>
					</xsl:with-param>
				</xsl:call-template>
			</fo:inline>
			<fo:block/>
			<!--	Court name -->
			<xsl:text>at </xsl:text>
			<xsl:value-of select="$CourtNames"/>
		</fo:block>
	</xsl:template>
	<!-- display the defendant details -->
	<xsl:template name="DefendantDetails">
		<xsl:param name="caseNumText"/>
		<xsl:param name="committingText"/>
		<xsl:param name="prosecuteRefText"/>
		<xsl:param name="appendText"/>
		<fo:block>
		<fo:table table-layout="fixed" font-size="9pt">
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="100mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-body>
				<xsl:for-each select="Defendants/Defendant/PersonalDetails/Name">
					<!-- get the formatted forename for the defendant : to handle long defendant forenames -->
					<xsl:variable name="FormattedForename">
						<xsl:call-template name="StringManipulation">
							<xsl:with-param name="InputString" select="CitizenNameForename[position()=1]"/>
							<xsl:with-param name="StringLength" select="45"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:variable name="defendant">
						<xsl:value-of select="CitizenNameSurname"/>
						<xsl:text> </xsl:text>
						<xsl:call-template name="TitleCase">
							<xsl:with-param name="text" select="$FormattedForename"/>
						</xsl:call-template>
						<!-- middle name, get initial only -->
						<!-- Court Service Stores Middle name using 2nd Forename element
							Xhibit stored the Middle name in the Suffix element -->
						<xsl:if test="CitizenNameForename[position()=2]">
							<xsl:text> </xsl:text>
							<xsl:value-of select="substring(CitizenNameForename[position()=2],1,1)"/>
						</xsl:if>
						<xsl:if test="CitizenNameSuffix">
							<xsl:text> </xsl:text>
							<xsl:value-of select="substring(CitizenNameSuffix,1,1)"/>
						</xsl:if>
						<xsl:value-of select="$appendText"/>
						<!-- only display if in prison, display the following details -->
						<xsl:if test="../../PrisonerID">
							<fo:block/>
							<xsl:if test="../Sex">
								<xsl:text> </xsl:text>
								<xsl:call-template name="toUpper">
									<xsl:with-param name="content" select="../Sex"/>
								</xsl:call-template>
								<xsl:text> </xsl:text>
							</xsl:if>
							<xsl:text> </xsl:text>
							<xsl:if test="../DateOfBirth">
								<xsl:text>DOB:</xsl:text>
								<xsl:call-template name="displayDate">
									<xsl:with-param name="input" select="../DateOfBirth/BirthDate"/>
								</xsl:call-template>
								<xsl:text> </xsl:text>
							</xsl:if>
							<xsl:if test="../../PrisonerID">
								<xsl:text>Prisoner Id: </xsl:text>
								<xsl:value-of select="../../PrisonerID"/>
								<xsl:text> </xsl:text>
							</xsl:if>
							<xsl:if test="../../PrisonLocation">
								<xsl:text>Prison: </xsl:text>
								<xsl:value-of select="../../PrisonLocation/@PrisonID"/>
								<xsl:text> / </xsl:text>
								<xsl:call-template name="toUpper">
									<xsl:with-param name="content" select="../../PrisonLocation/Location"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:variable>
					<xsl:choose>
						<!-- display all the details for the first time -->
						<xsl:when test="position()=1">
							<fo:table-row space-after="10pt">
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="$caseNumText"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:copy-of select="$defendant"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="$committingText"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="../../URN"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:call-template name="format-text-for-wrapping">
											<xsl:with-param name="str" select="$prosecuteRefText"/>
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:when>
						<!-- only display the defendant details -->
						<xsl:otherwise>
							<fo:table-row space-after="10pt">
								<fo:table-cell/>
								<fo:table-cell>
									<fo:block>
										<xsl:copy-of select="$defendant"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell/>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="../../URN"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell/>
							</fo:table-row>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block space-after="7pt"/>
	</xsl:template>
	<!-- template used to display the date -->
	<xsl:template name="displayDate">
		<xsl:param name="input"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:value-of select="$day"/>
		<xsl:text> </xsl:text>
		<xsl:choose>
			<xsl:when test="$month='01'">January</xsl:when>
			<xsl:when test="$month='02'">February</xsl:when>
			<xsl:when test="$month='03'">March</xsl:when>
			<xsl:when test="$month='04'">April</xsl:when>
			<xsl:when test="$month='05'">May</xsl:when>
			<xsl:when test="$month='06'">June</xsl:when>
			<xsl:when test="$month='07'">July</xsl:when>
			<xsl:when test="$month='08'">August</xsl:when>
			<xsl:when test="$month='09'">September</xsl:when>
			<xsl:when test="$month='10'">October</xsl:when>
			<xsl:when test="$month='11'">November</xsl:when>
			<xsl:when test="$month='12'">December</xsl:when>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>
	<!-- display the date including the Day -->
	<xsl:template name="displayDayDate">
		<xsl:param name="input"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:variable name="dayOfWeek">
			<xsl:call-template name="dt:calculate-day-of-the-week">
				<xsl:with-param name="year" select="$year"/>
				<xsl:with-param name="month" select="$month"/>
				<xsl:with-param name="day" select="$day"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$dayOfWeek=0">Sunday</xsl:when>
			<xsl:when test="$dayOfWeek=1">Monday</xsl:when>
			<xsl:when test="$dayOfWeek=2">Tuesday</xsl:when>
			<xsl:when test="$dayOfWeek=3">Wednesday</xsl:when>
			<xsl:when test="$dayOfWeek=4">Thursday</xsl:when>
			<xsl:when test="$dayOfWeek=5">Friday</xsl:when>
			<xsl:when test="$dayOfWeek=6">Saturday</xsl:when>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:call-template name="displayDate">
			<xsl:with-param name="input">
				<xsl:value-of select="$input"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- Display the header for pages other than the first -->
	<xsl:template name="displayHeaderRest">
		<fo:block>
		<fo:table table-layout="fixed" font-size="9pt">
			<fo:table-column column-width="110mm"/>
			<fo:table-column column-width="40mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
				             <xsl:value-of select="//DocumentName"/> for
							<!-- call template to display the date incl. the day -->
							<xsl:call-template name="displayDayDate">
								<xsl:with-param name="input">
									<xsl:value-of select="ListHeader/StartDate"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							Page No: <fo:page-number/>
							<xsl:text> of </xsl:text>
							<fo:page-number-citation ref-id="last-page"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							<!-- call template to display version details -->
							<xsl:call-template name="displayVersionDetails"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
							<!-- Display the parent / satellite court names -->
							<xsl:value-of select="$CourtNames2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block space-after="8pt">
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
	</xsl:template>
	<!-- Display the version details -->
	<xsl:template name="displayVersionDetails">
		<xsl:variable name="initversion">
			<!-- variable to hold CONSTANT null edition field value -->
			<xsl:variable name="NULL_EDITION">
				<xsl:value-of select="-1"/>
			</xsl:variable>
			<!-- variable to hold the length of Version text -->
			<xsl:variable name="vLength">
				<xsl:value-of select="string-length(/DailyList/ListHeader/Version)"/>
			</xsl:variable>
			<!-- variable to hold the length of null edition  text -->
			<xsl:variable name="edLength">
				<xsl:value-of select="string-length($NULL_EDITION)"/>
			</xsl:variable>
			<xsl:choose>
				<!-- Test to see if the Version ends with -1, which means that the Edition No is null -->
				<xsl:when test="substring(/DailyList/ListHeader/Version, $vLength - $edLength + 1) = $NULL_EDITION">
					<!-- display first part of version plus additional text at end indicating that the Edition No is null -->
					<xsl:value-of select="substring(/DailyList/ListHeader/Version, 1, $vLength - $edLength)"/>
					<xsl:text> </xsl:text> Unspecified Edition No
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="/DailyList/ListHeader/Version"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- strip out an unnecessary text in the version (v) -->
		<xsl:variable name="version">
			<xsl:choose>
				<xsl:when test="contains($initversion,'v')">
					<xsl:value-of select="substring-before($initversion,'v')"/>
					<xsl:value-of select="substring-after($initversion,'v')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$initversion"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- display the version value -->
		<xsl:value-of select="$version"/>
	</xsl:template>
	<!-- Display any optional header text -->
	<xsl:template name="displayOptionalHeaderText">
		<!-- Feb 2016 - requirement for 8.6.4 release to add in additional text where there is reporting restrictions -->
		<xsl:variable name="reportingRestriction">
			<xsl:for-each select="/DailyList/CourtLists/CourtList/Sittings/Sitting/Hearings/Hearing">
				<xsl:choose>
					<xsl:when test="substring(ListNote, 1, 10) = 'Order made'">
						Yes
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="contains($reportingRestriction, 'Yes')">
					<fo:block text-align="center" font-weight="bold" font-size="10pt">
						<xsl:text>Warning - There are cases within this court list with reporting restrictions. </xsl:text>
					</fo:block>
					<fo:block text-align="center" font-weight="bold" font-size="10pt">
						<xsl:text>Any breach of reporting restrictions is punishable by sanctions up to and including imprisonment.</xsl:text>
					</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- template used to format the time from 24hrs to the following format HH:MM [AM/PM]-->
	<!-- display the formatted time and then post fix with AM or PM -->
	<xsl:template name="FormatTime">
		<xsl:param name="input"/>
		<xsl:choose>
			<!-- if less than 12 postfix AM -->
			<xsl:when test="substring-before($input,':') &lt; 12">
				<xsl:value-of select="$input"/>
				<xsl:text> am</xsl:text>
			</xsl:when>
			<!-- otherwise format the hours element and postfix with PM -->
			<xsl:otherwise>
				<!-- get the hours element from the string -->
				<xsl:variable name="hrs" select="substring-before($input,':')"/>
				<xsl:choose>
					<!-- if 12 just display value as no formatting required -->
					<xsl:when test="$hrs = 12">
						<xsl:value-of select="$hrs"/>
					</xsl:when>
					<xsl:otherwise>
						<!-- subtract 12 from the 24 hours element -->
						<xsl:variable name="fmtHrs" select="$hrs - 12"/>
						<!-- if single element return prefix 0 to the hours -->
						<!-- i.e. 9 become 09 -->
						<xsl:if test="string-length($fmtHrs) = 1">
							<xsl:text>0</xsl:text>
						</xsl:if>
						<!-- display the value of the formatted hours -->
						<xsl:value-of select="$fmtHrs"/>
					</xsl:otherwise>
				</xsl:choose>
				<!-- display the rest of the time -->
				<xsl:text>:</xsl:text>
				<xsl:value-of select="substring-after($input,':')"/>
				<xsl:text> pm</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template to get the Not Before Time -->
	<xsl:template name="getNotBeforeTime">
		<xsl:if test="contains(TimeMarkingNote,':')">
			<xsl:variable name="UPPER_TMN">
				<xsl:call-template name="toUpper">
					<xsl:with-param name="content" select="TimeMarkingNote"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="contains($UPPER_TMN,'NOT BEFORE')">
					<xsl:call-template name="toLower">
						<xsl:with-param name="content" select="substring-after($UPPER_TMN,'NOT BEFORE ')"/>
					</xsl:call-template>
				</xsl:when>
				<!-- deal with 'SITTING AT' timemarking text -->
				<xsl:when test="contains($UPPER_TMN,'SITTING AT')">
					<xsl:call-template name="toLower">
						<xsl:with-param name="content" select="substring-after($UPPER_TMN,'SITTING AT ')"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="not(contains(TimeMarkingNote,'am') or contains(TimeMarkingNote,'pm') or contains(TimeMarkingNote,'AM') or contains(TimeMarkingNote,'PM')or contains(TimeMarkingNote,'Am') or contains(TimeMarkingNote,'Pm')or contains(TimeMarkingNote,'aM') or contains(TimeMarkingNote,'pM'))">
							<xsl:call-template name="FormatTime">
								<xsl:with-param name="input">
									<xsl:value-of select="TimeMarkingNote"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="toLower">
								<xsl:with-param name="content" select="TimeMarkingNote"/> 
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- Template to get the Sitting At Time -->
	<xsl:template name="getSittingTime">
		<xsl:apply-templates select="../../SittingAt"/>
	</xsl:template>
	<!-- display information for the header -->
	<xsl:template name="HeaderInfo">
		<fo:block space-after="8pt" text-align="center" font-size="15pt" font-weight="bold">
	             		<xsl:value-of select="//DocumentName"/> for
				<!-- call template to display the date incl. the day -->
			<xsl:call-template name="displayDayDate">
				<xsl:with-param name="input">
					<xsl:value-of select="ListHeader/StartDate"/>
				</xsl:with-param>
			</xsl:call-template>
		</fo:block>
		<fo:block text-align="center" font-size="9pt" line-height="18pt">
			<!-- call template to display version details -->
			<xsl:call-template name="displayVersionDetails"/>
		</fo:block>
		<fo:block text-align="center" font-size="9pt" line-height="18pt">
			<!-- display optional header text -->
			<xsl:call-template name="displayOptionalHeaderText"/>
		</fo:block>
	</xsl:template>
	<!-- display hearing details -->
	<xsl:template name="hearing">
		<xsl:variable name="hearingDescription">
			<xsl:choose>
				<xsl:when test="not (position()=1)">
					<xsl:variable name="pos" select="position()"/>
					<!-- check to ensure that the previous description is not the same, only display if different -->
					<xsl:if test="not (HearingDetails/HearingDescription = ../Hearing[position()=$pos - 1]/HearingDetails/HearingDescription)">
						<xsl:value-of select="HearingDetails/HearingDescription"/>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="HearingDetails/HearingDescription"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- set up bit to append to defendant if needed -->
		<xsl:variable name="appendage">
			<xsl:choose>
				<xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
					<xsl:value-of select="concat('-v-',Prosecution//OrganisationName)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="''"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- Get the Sitting At time -->
		<xsl:variable name="SittingAtValue">
			<xsl:call-template name="getSittingTime"/>
		</xsl:variable>
		<!-- Get the Not Before Time -->
		<xsl:variable name="NotBeforeTime">
			<xsl:call-template name="getNotBeforeTime"/>
		</xsl:variable>
		<!-- 	Needs to be able to handle both XHIBIT Internal TimeMarking Note where there is just a time value i.e. 10:00 which needs to be formatted
			and also the CS Tomorrow DailyList TimeMarking Note which contains the 'Not Before' text and also the time has been formatted i.e. Not Before 10:00 am -->
		<!-- display TimeMarkingNotes -->
		<fo:block font-weight="bold" font-size="10pt">
			<xsl:if test="TimeMarkingNote">
				<xsl:if test="not (TimeMarkingNote = ' ')">
					<xsl:variable name="upperMarkingNote">
						<xsl:call-template name="toUpper">
							<xsl:with-param name="content" select="TimeMarkingNote"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:variable name="NotBefore">
						<!--<xsl:if test="not(contains($upperMarkingNote,'NOT BEFORE')or contains($upperMarkingNote,'SITTING AT'))">-->
						<!--	<xsl:text>NOT BEFORE </xsl:text>-->
						<!--</xsl:if>-->
					</xsl:variable>
					<xsl:choose>
						<!-- could potentially be blank, so check that it contains a colon -->
						<xsl:when test="contains(TimeMarkingNote,':')">
							<!-- Only Display the Not Before Time if the value is different from the Sitting At Time -->
							<xsl:if test="$SittingAtValue != $NotBeforeTime">
								<xsl:choose>
									<!-- check to ensure that it does not already contain an AM or PM within the TimeMarkingNote -->
									<xsl:when test="not(contains(TimeMarkingNote,'am') or contains(TimeMarkingNote,'pm') or contains(TimeMarkingNote,'AM') or contains(TimeMarkingNote,'PM')or contains(TimeMarkingNote,'Am') or contains(TimeMarkingNote,'Pm')or contains(TimeMarkingNote,'aM') or contains(TimeMarkingNote,'pM'))">
										<!-- apply template to format the time -->
										<!-- Prefix the following text : Will only appear for Time Marking Notes that do not contain am or pm postfix -->
										<!--<xsl:text>NOT BEFORE </xsl:text>-->
										<xsl:call-template name="FormatTime">
											<xsl:with-param name="input">
												<xsl:value-of select="TimeMarkingNote"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<!-- if already contain AM or PM just display the value as it is -->
										<xsl:choose>
											<xsl:when test="$NotBefore=''">
												<!--display the 'NOT BEFORE', 'SITTING AT' as required-->
												<xsl:if test="contains($upperMarkingNote,'NOT BEFORE')">
													<xsl:text>NOT BEFORE </xsl:text>
													<!--make the am or pm lowercase-->
													<xsl:call-template name="toLower">
														<xsl:with-param name="content" select="substring-after($upperMarkingNote,'NOT BEFORE')"/>
													</xsl:call-template>
												</xsl:if>
												<xsl:if test="contains($upperMarkingNote,'SITTING AT')">
													<xsl:text>SITTING AT </xsl:text>
													<!--make the am or pm lowercase-->
													<xsl:call-template name="toLower">
														<xsl:with-param name="content" select="substring-after($upperMarkingNote,'SITTING AT')"/>
													</xsl:call-template>
												</xsl:if>
												<!--if no NOT BEFORE or SITTING AT then just display time-->
												<xsl:if test="not(contains($upperMarkingNote,'NOT BEFORE') or contains($upperMarkingNote,'SITTING AT'))">
        												<!-- The time marking note already contains an am or pm value (upper, lower case combinations) just convert to lower case -->
        												<xsl:value-of select="TimeMarkingNote"/>
        												<!--<xsl:call-template name="toLower">
        													<xsl:with-param name="content" select="TimeMarkingNote"/>
        												</xsl:call-template> -->
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$NotBefore"/>
												<xsl:call-template name="toLower">
													<xsl:with-param name="content" select="TimeMarkingNote"/>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<!--if Sitting At time and Not Before time are equal and it is not the first hearing for a courtroom then do not display Not before time - short ot itme before deadline hence - copy and paste approach - rework into template at some point-->
							<xsl:if test=" $SittingAtValue= $NotBeforeTime ">
								<xsl:if test="not(HearingSequenceNumber=1)">
									<!--<xsl:value-of select="HearingSequenceNumber[position()]"></xsl:value-of>-->
									<xsl:choose>
										<xsl:when test="$NotBefore=''">
											<!--display the 'NOT BEFORE', 'SITTING AT' as required-->
											<xsl:if test="contains($upperMarkingNote,'NOT BEFORE')">
												<xsl:text>NOT BEFORE </xsl:text>
												<!--make the am or pm lowercase-->
												<xsl:call-template name="toLower">
													<xsl:with-param name="content" select="substring-after($upperMarkingNote,'NOT BEFORE')"/>
												</xsl:call-template>
											</xsl:if>
											<xsl:if test="contains($upperMarkingNote,'SITTING AT')">
												<xsl:text>SITTING AT</xsl:text>
												<!--make the am or pm lowercase-->
												<xsl:call-template name="toLower">
													<xsl:with-param name="content" select="substring-after($upperMarkingNote,'SITTING AT')"/>
												</xsl:call-template>
											</xsl:if>
											<!--if no NOT BEFORE or SITTING AT then just display time-->
											<xsl:if test="not(contains($upperMarkingNote,'NOT BEFORE') or contains($upperMarkingNote,'SITTING AT'))">
												<xsl:value-of select="TimeMarkingNote"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$NotBefore"/>
											<xsl:call-template name="toLower">
												<xsl:with-param name="content" select="TimeMarkingNote"/>
											</xsl:call-template>
										</xsl:otherwise>
									</xsl:choose>
									<fo:block/>
								</xsl:if>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="TimeMarkingNote"/>
						</xsl:otherwise>
					</xsl:choose>
					<fo:block space-after="5pt"/>
				</xsl:if>
			</xsl:if>
			<!-- display hearing description exc. Miscellaneous text -->
			<xsl:choose>
				<xsl:when test="starts-with($hearingDescription,'Miscellaneous ')">
					<xsl:value-of select="substring-after($hearingDescription,'Miscellaneous ')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$hearingDescription"/>
				</xsl:otherwise>
			</xsl:choose>
		</fo:block>
		<!-- get the prosecuting organisation name -->
		<xsl:variable name="prosecutingref">
			<!-- Details held within Prosecuting Reference -->
			<!-- Pick up the Prosecuting Reference -->
			<xsl:value-of select="Prosecution/ProsecutingReference"/>
			<!--<xsl:choose>
				<xsl:when test="contains(Prosecution//OrganisationName,'Crown Prosecution Service')">
					<xsl:value-of select="Prosecution/ProsecutingReference"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="Prosecution//OrganisationName">
							<xsl:call-template name="TitleCase">
								<xsl:with-param name="text" select="Prosecution//OrganisationName"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="TitleCase">
								<xsl:with-param name="text" select="Prosecution/ProsecutingReference"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose> -->
		</xsl:variable>
		<!--<xsl:variable name="UpperProsRef">
			<xsl:call-template name="toUpper">
				<xsl:with-param name="content" select="$prosecutingref"/>
			</xsl:call-template>
		</xsl:variable> -->
		<!-- set up variable to hold the formatted prosecuting refererence details -->
		<xsl:variable name="Formattedprosecutingref">
			<xsl:call-template name="StringManipulation">
				<xsl:with-param name="InputString" select="$prosecutingref"/>
				<xsl:with-param name="StringLength" select="20"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<!-- Display of Defendant Details on the Daily List for U and B Type cases.  Display Case Title instead
				
				For the MLD Daily List the CaseTitle is placed in the CitizenNameSurname element of a defendant and is
				displayed as per a normal defendant

				For the Internal Xhibit Daily List display the Case Title is placed in the CaseTitle element, there are no
				defendants and is covered by the otherwise condition of the when clause below
			-->
			<xsl:when test="Defendants/Defendant">
				<!-- display the defendant detail -->
				<xsl:call-template name="DefendantDetails">
					<xsl:with-param name="caseNumText" select="CaseNumber"/>
					<xsl:with-param name="committingText" select="CommittingCourt/CourtHouseCode"/>
					<xsl:with-param name="prosecuteRefText" select="$Formattedprosecutingref"/>
					<xsl:with-param name="appendText" select="$appendage"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<fo:block>
				<fo:table table-layout="fixed" font-size="9pt">
					<fo:table-column column-width="25mm"/>
					<fo:table-column column-width="100mm"/>
					<fo:table-column column-width="20mm"/>
					<fo:table-column column-width="45mm"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="CaseNumber"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
									<!-- Ccapture scenario of internal Xhibit Daily List where there are no defendants for U and B type cases.
										Display the CaseTitle (only available in the internal Xhibit Daily List XML) -->
									<xsl:if test="substring(CaseNumber,1,1)='B' or substring(CaseNumber,1,1)='U'">
										<xsl:value-of select="CaseTitle"/>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="CommittingCourt/CourtHouseCode"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="$Formattedprosecutingref"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
		<!-- display any list notes that may exist -->
		<xsl:if test="ListNote">
			<fo:block font-weight="bold" font-size="10pt">
				<xsl:value-of select="ListNote"/>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<!-- template to display footer information -->
	<xsl:template name="ListFooter">
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="160mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-body>
				<xsl:for-each select="DocumentID">
					<fo:table-row>
						<!-- display published details -->
						<xsl:if test="../ListHeader/PublishedTime">
							<fo:table-cell>
								<fo:block font-size="9pt">
									<xsl:choose>
										<!-- Test to see if the Published Date is the default -->
										<xsl:when test="substring(../ListHeader/PublishedTime,1,4)='1900'">
											Unspecified Published Time
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>Published: </xsl:text>
											<xsl:call-template name="displayDate">
												<xsl:with-param name="input">
													<xsl:value-of select="substring(../ListHeader/PublishedTime,1,10)"/>
												</xsl:with-param>
											</xsl:call-template>
											at <xsl:value-of select="substring(../ListHeader/PublishedTime,12,5)"/>
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="9pt">
									<!--CREST Print Ref-->
									<xsl:value-of select="../ListHeader/CrestprintRef"/>
								</fo:block>
							</fo:table-cell>
						</xsl:if>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="9pt">
								<xsl:text>&#x00A9; Crown copyright 2025. All rights reserved. Issued by His Majesty's Court Service.</xsl:text>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>
	<!-- display the judge details -->
	<xsl:template name="JudgeDetails">
		<xsl:param name="Judge"/>
		<fo:block>
		<fo:table table-layout="fixed" font-size="10pt">
			<fo:table-column column-width="180mm"/>
			<fo:table-body>
				<xsl:for-each select="$Judge/Judge">
					<fo:table-row>
						<fo:table-cell>
							<fo:block space-after="5pt" space-before="5pt" text-align="center" font-weight="bold">
								<xsl:choose>
									<!-- test to see if there is a Full title for the judge -->
									<xsl:when test="CitizenNameFullTitle != ' '">
										<xsl:value-of select="CitizenNameFullTitle"/>
									</xsl:when>
									<!-- otherwise display the judge's surname -->
									<xsl:otherwise>
										<xsl:value-of select="CitizenNameSurname"/>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<!-- see if there is any justice information -->
		<xsl:variable name="justiceText">
			<xsl:choose>
				<xsl:when test="count($Judge/Justice) =  1">
					<xsl:text>Justice: </xsl:text>
				</xsl:when>
				<xsl:when test="count($Judge/Justice) &gt; 1">
					<xsl:text>Justices: </xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<fo:block>
		<fo:table table-layout="fixed" font-size="10pt">
			<fo:table-column column-width="40mm"/>
			<fo:table-column column-width="140mm"/>
			<fo:table-body>
				<xsl:for-each select="$Judge/Justice">
					<xsl:variable name="justice">
						<xsl:value-of select="CitizenNameTitle"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="CitizenNameSurname"/>
					</xsl:variable>
					<fo:table-row>
						<xsl:choose>
							<xsl:when test="position()=1">
								<fo:table-cell>
									<fo:block font-weight="bold" text-align="right">
										<xsl:value-of select="$justiceText"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" text-align="left">
										<xsl:value-of select="$justice"/>
									</fo:block>
								</fo:table-cell>
							</xsl:when>
							<xsl:otherwise>
								<fo:table-cell/>
								<fo:table-cell>
									<fo:block font-weight="bold" text-align="left">
										<xsl:value-of select="$justice"/>
									</fo:block>
								</fo:table-cell>
							</xsl:otherwise>
						</xsl:choose>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>
	<!-- 
	Template used to insert spaces into text if length is greater than provided parameter length 
		Parameters: 	InputString - The String to be checked 
						StringLength - After what Length should a space be inserted
	-->
	<xsl:template name="StringManipulation">
		<xsl:param name="InputString"/>
		<xsl:param name="StringLength"/>
		<!-- variable to hold the Input String Length -->
		<xsl:variable name="ISL" select="string-length($InputString)"/>
		<xsl:choose>
			<!-- check if the string length is greater than the provided paramater -->
			<xsl:when test="$ISL>$StringLength">
				<xsl:choose>
					<!-- check if the string contains any spaces -->
					<xsl:when test="contains($InputString,' ')">
						<!-- call template using string up to the space character -->
						<xsl:call-template name="StringManipulation">
							<xsl:with-param name="InputString">
								<xsl:value-of select="substring-before($InputString, ' ')"/>
							</xsl:with-param>
							<xsl:with-param name="StringLength" select="$StringLength"/>
						</xsl:call-template>
						<!-- insert a space character -->
						<xsl:text> </xsl:text>
						<!-- call template with the rest of the string i.e. after the space character-->
						<xsl:call-template name="StringManipulation">
							<xsl:with-param name="InputString">
								<xsl:value-of select="substring-after($InputString, ' ')"/>
							</xsl:with-param>
							<xsl:with-param name="StringLength" select="$StringLength"/>
						</xsl:call-template>
					</xsl:when>
					<!--	 if it does not contain spaces display the first chunk of text upto 
						the limit, insert a space and pass rest through the template again -->
					<xsl:otherwise>
						<xsl:value-of select="substring($InputString,1,$StringLength)"/>
						<xsl:text> </xsl:text>
						<xsl:call-template name="StringManipulation">
							<xsl:with-param name="InputString">
								<xsl:value-of select="substring($InputString,$StringLength+1,$ISL)"/>
							</xsl:with-param>
							<xsl:with-param name="StringLength" select="$StringLength"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- Text within String Lenght limit, so display as it is -->
			<xsl:otherwise>
				<xsl:value-of select="$InputString"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- template used to capitalise the first character of text and put the rest into lower case  -->
	<xsl:template name="TitleCase">
		<xsl:param name="text"/>
		<xsl:param name="lastletter" select="' '"/>
		<xsl:if test="$text">
			<xsl:variable name="thisletter" select="substring($text,1,1)"/>
			<xsl:choose>
				<xsl:when test="$lastletter=' '">
					<xsl:value-of select="translate($thisletter,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="translate($thisletter,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="TitleCase">
				<xsl:with-param name="text" select="substring($text,2)"/>
				<xsl:with-param name="lastletter" select="$thisletter"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- template used to convert a string to Lower Case -->
	<xsl:template name="toLower">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
	</xsl:template>
	<!-- template used to convert a string to upper case -->
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>
</xsl:stylesheet>
