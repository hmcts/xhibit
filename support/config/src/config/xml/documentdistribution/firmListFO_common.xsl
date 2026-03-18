<?xml version="1.0" encoding="utf-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xml.apache.org/fop/extensions" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	<!-- Import data file which hold the hard coded text in the different languages -->
	<xsl:variable name="data" select="document('translation.xml')"/>
	<!-- Include the Translations Template -->
	<xsl:include href="translation.xsl"/>
	<!-- Default Language -->
	<xsl:variable name="DefaultLang">en</xsl:variable>
	<!-- set up global variables -->
	<xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate"/>
	<xsl:variable name="endDate" select="//cs:ListHeader/cs:EndDate"/>
	<xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime"/>
	<xsl:variable name="reporttype" select="'Criminal Firm List'"/>
	<xsl:variable name="initversion" select="//cs:ListHeader/cs:Version"/>
	<!--set up CREST 'version'-->
	<!-- set up keys -->
	<xsl:key name="hearingByDate" match="cs:Hearing" use="//cs:Sittings/cs:Sitting/cs:Hearings/cs:Hearing/cs:HearingDetails/cs:HearingDate"/>
	<xsl:key name="courtByName" match="//cs:CourtHouse/cs:CourtHouseName" use="//cs:CourtHouse/cs:CourtHouseName"/>
	<!-- Top level  Match -->
	<xsl:template name="PageSetUp">
		<fo:layout-master-set>
			<!-- set up page for first page -->
			<fo:simple-page-master master-name="first" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
				<fo:region-body margin-top="45mm" margin-bottom="20mm"/>
				<fo:region-before extent="50mm" region-name="header-first"/>
				<fo:region-after extent="20mm" region-name="footer-first"/>
			</fo:simple-page-master>
			<!-- set up page for rest of pages -->
			<fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
				<fo:region-body margin-top="15mm" margin-bottom="20mm"/>
				<fo:region-before extent="30mm" region-name="header-rest"/>
				<fo:region-after extent="20mm" region-name="footer-rest"/>
			</fo:simple-page-master>
			<fo:page-sequence-master master-name="document">
				<!-- set up page conditions -->
				<fo:repeatable-page-master-alternatives>
					<fo:conditional-page-master-reference page-position="first" master-reference="first"/>
					<fo:conditional-page-master-reference page-position="rest" master-reference="rest"/>
				</fo:repeatable-page-master-alternatives>
			</fo:page-sequence-master>
		</fo:layout-master-set>
	</xsl:template>
	<xsl:template name="DisplayFirmList">
		<xsl:param name="language"/>
		<fo:page-sequence master-reference="document" format="1" initial-page-number="1" force-page-count="no-force">
			<!-- Header Information for first page only-->
			<fo:static-content flow-name="header-first">
				<fo:block>
				<fo:table table-layout="fixed">
					<fo:table-column column-width="160mm"/>
					<fo:table-column column-width="25mm"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell text-align="right">
								<!-- display the page no -->
								<fo:block font-size="7pt">
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="'Page No'"/>
									</xsl:call-template>
									<xsl:text>: </xsl:text>
									<fo:page-number/>
									<xsl:text> </xsl:text>
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="'of'"/>
									</xsl:call-template>
									<xsl:text> </xsl:text>
									<fo:page-number-citation ref-id="{$language}"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				</fo:block>
				<fo:block>
					<xsl:call-template name="HeaderInfoFirstPage">
						<xsl:with-param name="language" select="$language"/>
					</xsl:call-template>
				</fo:block>
			</fo:static-content>
			<!--Header info all other pages-->
			<fo:static-content flow-name="header-rest">
				<fo:block>
					<xsl:call-template name="HeaderInfoOtherPages">
						<xsl:with-param name="language" select="$language"/>
					</xsl:call-template>
				</fo:block>
			</fo:static-content>
			<!-- Display footer info -->
			<fo:static-content flow-name="footer-first">
				<fo:block>
					<xsl:call-template name="ListFooterFirstPage">
						<xsl:with-param name="language" select="$language"/>
					</xsl:call-template>
				</fo:block>
			</fo:static-content>
			<!-- Footer Information Subsequent Pages -->
			<fo:static-content flow-name="footer-rest">
				<fo:block>
					<xsl:call-template name="ListFooterOtherPages">
						<xsl:with-param name="language" select="$language"/>
					</xsl:call-template>
				</fo:block>
			</fo:static-content>
			<!-- Main Body of Text -->
			<fo:flow flow-name="xsl-region-body">
				<!-- Display Court List details -->
				<xsl:apply-templates select="cs:FirmList/cs:CourtLists">
					<xsl:with-param name="language" select="$language"/>
				</xsl:apply-templates>
				<!-- Display Reserved List details -->
				<xsl:apply-templates select="cs:FirmList/cs:ReserveList">
					<xsl:with-param name="language" select="$language"/>
				</xsl:apply-templates>
				<fo:block id="{$language}"/>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
	<!-- 		
			**************************
			TEMPLATE MATCHES 
			*************************
	-->
	<!-- Display Court List details -->
	<xsl:template match="cs:CourtLists">
		<xsl:param name="language"/>
		<!-- for each court list -->
		<xsl:for-each select="cs:CourtList">
			<fo:block break-before="page"/>
			<xsl:variable name="sittingDate">
				<xsl:call-template name="displayDayDate">
					<xsl:with-param name="input" select="./@SittingDate"/>
					<xsl:with-param name="language" select="$language"/>
				</xsl:call-template>
			</xsl:variable>
			<fo:block>
			<fo:table table-layout="fixed" table-omit-header-at-break="false">
				<fo:table-column column-width="190mm"/>
				<!-- set up table header -->
				<fo:table-header>
					<fo:table-row>
						<fo:table-cell>
							<!-- only display if go onto next page -->
							<!-- New way of doing continued label pt 1 -->
							<fo:block font-size="10pt" font-weight="bold" margin-bottom="12pt">
								<fo:retrieve-table-marker retrieve-class-name="mc2" retrieve-position-within-table="first-starting" retrieve-boundary-within-table="table" />
							</fo:block>
							<!-- End of new continued label pt 1 -->
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
						
							<!-- New way of doing continued label pt 2 -->
							<fo:block>
								<fo:marker marker-class-name="mc2"></fo:marker>
							</fo:block>
							<fo:block font-size="10pt" font-weight="bold">
								<fo:marker marker-class-name="mc2">
									<xsl:value-of select="$sittingDate"/>
									<xsl:text> </xsl:text>
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="'continued'"/>
									</xsl:call-template>
								</fo:marker>
							</fo:block>
							<!-- End of new continued label pt 2 -->
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-size="11pt" font-weight="bold" text-align="center" space-after="10pt">
								<xsl:value-of select="$sittingDate"/>
							</fo:block>
							<!-- Only display if different from previous -->
							<xsl:choose>
								<xsl:when test="not (position()=1)">
									<xsl:variable name="pos" select="position()"/>
									<xsl:if test="not (./cs:CourtHouse/cs:CourtHouseName = ../cs:CourtList[position()=$pos - 1]/cs:CourtHouse/cs:CourtHouseName)">
										<!-- Display Court house name if different from previous-->
										<fo:block font-weight="bold" font-size="10pt" text-decoration="underline">
											<xsl:variable name="courthousename">
												<xsl:value-of select="./cs:CourtHouse/cs:CourtHouseName"/>
											</xsl:variable>
											<xsl:variable name="courthousename2">
												<xsl:call-template name="getValue">
													<xsl:with-param name="language" select="$language"/>
													<xsl:with-param name="key" select="$courthousename"/>
												</xsl:call-template>
											</xsl:variable>
											<xsl:value-of select="$courthousename2"/>
										</fo:block>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<!-- Display the court name if first on list-->
									<fo:block font-weight="bold" font-size="10pt" text-decoration="underline">
										<xsl:variable name="courthousename">
											<xsl:value-of select="./cs:CourtHouse/cs:CourtHouseName"/>
										</xsl:variable>
										<xsl:variable name="courthousename2">
											<xsl:call-template name="getValue">
												<xsl:with-param name="language" select="$language"/>
												<xsl:with-param name="key" select="$courthousename"/>
											</xsl:call-template>
										</xsl:variable>
											<xsl:value-of select="$courthousename2"/>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
							<!-- Display the sitting details -->
							<xsl:for-each select="cs:Sittings/cs:Sitting/cs:SittingPriority">
								<xsl:if test=". != 'F'">
									<xsl:call-template name="DisplaySittingInfo">
										<xsl:with-param name="language" select="$language"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
							<xsl:variable name="nonFloatCount">
								<xsl:value-of select="count(cs:Sittings/cs:Sitting/cs:SittingPriority[. != 'F'])"/>
							</xsl:variable>
							<xsl:for-each select="cs:Sittings/cs:Sitting/cs:SittingPriority">
								<xsl:if test=". = 'F'">
									<xsl:if test="$nonFloatCount>0">
										<fo:block break-after="page"/>
									</xsl:if>
									<xsl:call-template name="DisplaySittingInfo">
										<xsl:with-param name="language" select="$language"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="DisplaySittingInfo">
		<xsl:param name="language"/>
		<xsl:variable name="sittingTime">
			<xsl:choose>
				<xsl:when test="string-length(../cs:SittingAt)=8">
					<!-- call template to format the time -->
					<xsl:call-template name="FormatTime">
						<xsl:with-param name="input">
							<xsl:value-of select="substring(../cs:SittingAt,1,5)"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="string-length(.)=7">
					<!-- call template to format the time -->
					<xsl:call-template name="FormatTime">
						<xsl:with-param name="input">
							<xsl:value-of select="substring(../cs:SittingAt,1,4)"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="../cs:SittingAt"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- Display the courtnumber and sitting date and time-->
		<fo:block font-size="10pt" font-weight="bold">
			<xsl:if test="../cs:SittingPriority != 'F'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Court'"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
				<xsl:value-of select="../cs:CourtRoomNumber"/>
				<xsl:if test="../cs:SittingAt">
					<fo:inline font-weight="normal">
						<xsl:text> - </xsl:text>
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'sitting at'"/>
						</xsl:call-template>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$sittingTime"/>
					</fo:inline>
				</xsl:if>
			</xsl:if>
		</fo:block>
		<!-- Call template to display judge details, check sitting priority, only display if non sitting priority of floating -->
		<xsl:if test="../cs:SittingPriority !='F'">
			<fo:block space-after="5pt" font-size="10pt">
				<xsl:call-template name="judiciary">
					<xsl:with-param name="judiciary_NodeSet" select="../cs:Judiciary"/>
					<xsl:with-param name="language" select="$language"/>
				</xsl:call-template>
			</fo:block>
		</xsl:if>
		<!-- Display the sitting note -->
		<xsl:if test="../cs:SittingNote">
			<fo:block font-weight="bold" space-after="5pt" font-size="10pt">
				<xsl:choose>
					<xsl:when test="substring(../cs:SittingNote,1,25)=' These cases may be taken'">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="substring-after(../cs:SittingNote,' ')"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="../cs:SittingNote"/>
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</xsl:if>
		<!-- Display hearing details -->
		<xsl:for-each select="../cs:Hearings/cs:Hearing">
			<xsl:call-template name="hearing">
				<xsl:with-param name="language" select="$language"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:if test="not( position() = last())">
			<fo:block>
				<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<!-- Display reserved List hearing details -->
	<xsl:template match="cs:ReserveList">
		<xsl:param name="language"/>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block font-size="10pt" space-after="10pt">
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="'Reserve List'"/>
			</xsl:call-template>
		</fo:block>
		<fo:block space-after="10pt" font-size="10pt">
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="'The following cases are liable to be brought in at short notice to fill in any gaps arising in the main list.'"/>
			</xsl:call-template>
		</fo:block>
		<xsl:for-each select="cs:Hearing">
			<xsl:call-template name="hearing">
				<xsl:with-param name="language" select="$language"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<!-- 		
			**************************
			TEMPLATE NAMES
			*************************
	-->
	<!-- 
		from dt:date-time.xsl 
		Return number to represent the day of the week
			0	Sunday
			1	Monday
			2	Tuesday
			3	Wednesday
			4	Thursday
			5	Friday
			6	Saturday
	-->
	<xsl:template name="calculate-day-of-the-week">
		<xsl:param name="year"/>
		<xsl:param name="month"/>
		<xsl:param name="day"/>
		<xsl:variable name="a" select="floor((14 - $month) div 12)"/>
		<xsl:variable name="y" select="$year - $a"/>
		<xsl:variable name="m" select="$month + 12 * $a - 2"/>
		<xsl:value-of select="($day + $y + floor($y div 4) - floor($y div 100) + floor($y div 400) + floor((31 * $m) div 12)) mod 7"/>
	</xsl:template>
	<!-- Template to display the date in a specific format -->
	<xsl:template name="displayDate">
		<xsl:param name="input"/>
		<xsl:param name="language"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:value-of select="$day"/>
		<xsl:text> </xsl:text>
		<xsl:choose>
			<xsl:when test="$month='01'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'January'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='02'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'February'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='03'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'March'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='04'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'April'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='05'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'May'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='06'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'June'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='07'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'July'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='08'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'August'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='09'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'September'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='10'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'October'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='11'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'November'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='12'">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'December'"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>
	<!-- Common template to display date including the day -->
	<xsl:template name="displayDayDate">
		<xsl:param name="input"/>
		<xsl:param name="language"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:variable name="dayOfWeek">
			<xsl:call-template name="calculate-day-of-the-week">
				<xsl:with-param name="year" select="$year"/>
				<xsl:with-param name="month" select="$month"/>
				<xsl:with-param name="day" select="$day"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$dayOfWeek=0">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Sunday'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$dayOfWeek=1">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Monday'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$dayOfWeek=2">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Tuesday'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$dayOfWeek=3">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Wednesday'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$dayOfWeek=4">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Thursday'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$dayOfWeek=5">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Friday'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$dayOfWeek=6">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Saturday'"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:call-template name="displayDate">
			<xsl:with-param name="input" select="$input"/>
			<xsl:with-param name="language" select="$language"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Display the Name from the info provided -->
	<xsl:template name="DisplayFormalName">
		<!-- expects PersonalDetails/Name structure as inpit parameter -->
		<!-- return name in the following format title inits surname -->
		<!--  ie Mr FJ Bloggs -->
		<xsl:param name="name"/>
		<xsl:variable name="newname">
			<xsl:value-of select="$name/apd:CitizenNameTitle"/>
			<xsl:text> </xsl:text>
			<xsl:for-each select="$name/apd:CitizenNameForename">
				<xsl:value-of select="substring(.,1,1)"/>
			</xsl:for-each>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$name/apd:CitizenNameSurname"/>
			<xsl:for-each select="$name/apd:CitizenNameSuffix">
				<xsl:text> </xsl:text>
				<xsl:value-of select="."/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:copy-of select="$newname"/>
	</xsl:template>
	<!-- Template to display the solicitor details -->
	<xsl:template name="DisplaySolicitorDetails">
		<xsl:param name="party"/>
		<xsl:param name="language"/>
		<xsl:choose>
			<xsl:when test="$party/cs:Person">
				<!-- Call template to display Solicitor name -->
				<xsl:call-template name="DisplayFormalName">
					<xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$party/cs:Organisation">
				<xsl:choose>
					<xsl:when test="$party/cs:Organisation/cs:OrganisationName = 'NO REPRESENTATION RECORDED'">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'NO REPRESENTATION RECORDED'"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="$party/cs:Organisation/cs:OrganisationName = 'NOT REPRESENTED'">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'NOT REPRESENTED'"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="'Y'">
				<xsl:text>"</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'NO REPRESENTATION RECORDED'"/>
				</xsl:call-template>
			</xsl:otherwise>
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
	<!-- Template to display hearing details -->
	<xsl:template name="hearing">
		<xsl:param name="language"/>
		<fo:block font-weight="bold" font-size="10pt">
			<!-- Display TimeMarking notes if any -->
			<xsl:if test="cs:TimeMarkingNote">
				<xsl:choose>
					<xsl:when test="starts-with(cs:TimeMarkingNote,'SITTING AT')">
						<xsl:variable name="sittingAt">
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="'sitting at'"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:call-template name="toUpper">
							<xsl:with-param name="content" select="$sittingAt"/>
						</xsl:call-template>
						<xsl:text> </xsl:text>
						<xsl:value-of select="substring-after(cs:TimeMarkingNote,'SITTING AT')"/>
					</xsl:when>
					<xsl:when test="starts-with(cs:TimeMarkingNote,'NOT BEFORE')">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'NOT BEFORE'"/>
						</xsl:call-template>
						<xsl:text> </xsl:text>
						<xsl:value-of select="substring-after(cs:TimeMarkingNote,'NOT BEFORE')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="cs:TimeMarkingNote"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<fo:block space-after="5pt"/>
			<xsl:choose>
				<!-- Display hearing types -->
				<xsl:when test="not (position()=1)">
					<xsl:variable name="pos" select="position()"/>
					<xsl:if test="not (cs:HearingDetails/@HearingType = ../cs:Hearing[position()=$pos - 1]/cs:HearingDetails/@HearingType)">
						<!-- Display the hearing description -->
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="cs:HearingDetails/cs:HearingDescription"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<!-- Display the hearing description -->
					<xsl:call-template name="getValue">
						<xsl:with-param name="language" select="$language"/>
						<xsl:with-param name="key" select="cs:HearingDetails/cs:HearingDescription"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</fo:block>
		<!-- variable to hold proseuction details -->
		<xsl:variable name="prosecutingref">
			<xsl:choose>
				<!-- only set this when NOT CPS -->
				<xsl:when test="contains(cs:Prosecution/@ProsecutingAuthority,'Crown Prosecution Service') = false">
					<xsl:variable name="orgName">
						<xsl:choose>
							<xsl:when test="cs:Prosecution/cs:ProsecutingOrganisation">
								<xsl:variable name="prosOrg">
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:call-template name="toUpper">
									<xsl:with-param name="content" select="$prosOrg"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="cs:Prosecution/cs:Advocate">
								<xsl:variable name="prosOrg">
									<xsl:call-template name="getValue">
										<xsl:with-param name="language" select="$language"/>
										<xsl:with-param name="key" select="cs:Prosecution/cs:Advocate/cs:PersonalDetails/cs:Name/apd:CitizenNameRequestedName"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:call-template name="toUpper">
									<xsl:with-param name="content" select="$prosOrg"/>
								</xsl:call-template>
							</xsl:when>
						</xsl:choose>
					</xsl:variable>
					<xsl:if test="$orgName != ''">
						<xsl:choose>
							<!-- Display the organisation .or person name -->
							<xsl:when test="substring(cs:CaseNumber,1,1) = 'A'">
								<xsl:text>(</xsl:text>
								<xsl:call-template name="getValue">
									<xsl:with-param name="language" select="$language"/>
									<xsl:with-param name="key" select="'Respondent'"/>
								</xsl:call-template>
								<xsl:text> : </xsl:text>
								<xsl:value-of select="$orgName"/>
								<xsl:text>)</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>(</xsl:text>
								<xsl:call-template name="getValue">
									<xsl:with-param name="language" select="$language"/>
									<xsl:with-param name="key" select="'Prosecutor'"/>
								</xsl:call-template>
								<xsl:text> : </xsl:text>
								<xsl:value-of select="$orgName"/>
								<xsl:text>)</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<!-- call template to display defendant details -->
		<xsl:call-template name="processdefendants">
			<xsl:with-param name="caseNumText" select="cs:CaseNumber"/>
			<xsl:with-param name="committingText" select="cs:CommittingCourt/cs:CourtHouseCode/@CourtHouseShortName"/>
			<xsl:with-param name="prosecuteRefText" select="$prosecutingref"/>
			<xsl:with-param name="language" select="$language"/>
		</xsl:call-template>
		<!-- Display any list notes info -->
		<xsl:if test="cs:ListNote">
			<fo:block font-weight="bold" font-size="10pt">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="cs:ListNote"/>
				</xsl:call-template>
			</fo:block>
			<fo:block/>
		</xsl:if>
	</xsl:template>
	<!-- Template to display judge's name -->
	<xsl:template name="judiciary">
		<xsl:param name="judiciary_NodeSet"/>
		<xsl:param name="language"/>
		<xsl:for-each select="$judiciary_NodeSet/cs:Judge">
			<xsl:variable name="judge">
				<xsl:choose>
					<xsl:when test="apd:CitizenNameRequestedName != 'N/A'">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="apd:CitizenNameRequestedName"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="apd:CitizenNameSurname != 'N/A'">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="apd:CitizenNameSurname"/>
						</xsl:call-template>
					</xsl:when>
					<!-- If requested name and surname are N/A then display following text to indicate that there is no judge allocated-->
					<xsl:otherwise>
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'JUDGE NOT ALLOCATED'"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<!-- check to ensure there is a value for the judges name -->
			<xsl:choose>
				<xsl:when test="$judge != ''">
					<fo:block text-align="center" font-weight="bold">
						<xsl:call-template name="toUpper">
							<xsl:with-param name="content">
								<xsl:value-of select="$judge"/>
							</xsl:with-param>
						</xsl:call-template>
					</fo:block>
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
		<!-- see if any Justices -->
		<xsl:variable name="justiceText">
			<xsl:choose>
				<xsl:when test="count($judiciary_NodeSet/cs:Justice) &gt; 1">
					<fo:block>
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'Justices'"/>
						</xsl:call-template>
						<xsl:text>:</xsl:text>
					</fo:block>
				</xsl:when>
				<xsl:when test="count($judiciary_NodeSet/cs:Justice) &gt; 0">
					<fo:block>
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="'Justice'"/>
						</xsl:call-template>
						<xsl:text>:</xsl:text>
					</fo:block>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:for-each select="$judiciary_NodeSet/cs:Justice">
			<xsl:variable name="justice">
				<xsl:value-of select="apd:CitizenNameRequestedName"/>
			</xsl:variable>
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="20mm"/>
				<fo:table-column column-width="70mm"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								<xsl:if test="position()=1">
									<xsl:value-of select="$justiceText"/>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$justice"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:for-each>
	</xsl:template>
	<!-- Display the defendants information -->
	<xsl:template name="processdefendants">
		<xsl:param name="caseNumText"/>
		<xsl:param name="committingText"/>
		<xsl:param name="prosecuteRefText"/>
		<xsl:param name="language"/>
		<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="20mm"/>
				<fo:table-column column-width="70mm"/>
				<fo:table-column column-width="65mm"/>
				<fo:table-column column-width="15mm"/>
				<fo:table-column column-width="20mm"/>
				<fo:table-body font-size="9pt">
					<xsl:if test="not(cs:Defendants/cs:Defendant)">
						 <fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="$caseNumText"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell/>
							<fo:table-cell>
								<fo:block>
									NO DEFENDANTS
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
					<xsl:for-each select="cs:Defendants/cs:Defendant/cs:PersonalDetails/cs:Name">
						<!-- set up variable to hold defendant name -->
						<xsl:variable name="defendant">
							<xsl:value-of select="apd:CitizenNameSurname"/>
							<xsl:text> </xsl:text>
							<!-- display the defendant forename -->
							<xsl:value-of select="apd:CitizenNameForename[position()=1]"/>
							<!-- If there is a defendants middlename, the display the first character in upper case -->
							<xsl:if test="apd:CitizenNameForename[position()=2]">
								<xsl:text> </xsl:text>
								<xsl:call-template name="toUpper">
									<xsl:with-param name="content">
										<xsl:value-of select="substring(apd:CitizenNameForename[position()=2],1,1)"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:variable>
						<xsl:choose>
							<!-- Display the defendant information -->
							<xsl:when test="position()=1">
								<fo:table-row>
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="$caseNumText"/>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block hyphenate="true" language="en">
											<xsl:call-template name="format-text-for-wrapping">
												<xsl:with-param name="str" select="$defendant"/>
											</xsl:call-template>
											<!--<xsl:if test="(position() = last())">
                                                <fo:block/>
                                                <xsl:value-of select="$prosecuteRefText"/>
                                            </xsl:if> -->
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block margin-left="2px">
											<!-- If no Solicitor display default text -->
											<xsl:choose>
												<xsl:when test="../../cs:Counsel/cs:Solicitor/cs:Party">
													<!-- Call Template to display Solicitor details -->
													<xsl:call-template name="DisplaySolicitorDetails">
														<xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
														<xsl:with-param name="language" select="$language"/>
													</xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="getValue">
														<xsl:with-param name="language" select="$language"/>
														<xsl:with-param name="key" select="'NO REPRESENTATION RECORDED'"/>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="center">
											<xsl:value-of select="$committingText"/>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="right">
											<xsl:value-of select="../../cs:URN"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:when>
							<!-- Handling of rows after the first -->
							<xsl:otherwise>
								<fo:table-row>
									<fo:table-cell/>
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="$defendant"/>
											<!--<xsl:if test="(position() = last())">
                                                <fo:block/>
                                                <xsl:value-of select="$prosecuteRefText"/>
                                            </xsl:if> -->
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block>
											<xsl:variable name="pos" select="position()"/>
											<xsl:choose>
												<!-- check to see if there is a solicitor -->
												<xsl:when test="../../cs:Counsel/cs:Solicitor/cs:Party/cs:Organisation or ../../cs:Counsel/cs:Solicitor/cs:Party/cs:Person">
													<!-- if solicitor is same as previous solicitor then just display quotes-->
													<xsl:variable name="lastsolicitor" select="../../../cs:Defendant[position()=$pos - 1]/cs:Counsel/cs:Solicitor/cs:Party/cs:Organisation/cs:OrganisationName"/>
													<xsl:variable name="thissolicitor" select="../../cs:Counsel/cs:Solicitor/cs:Party/cs:Organisation/cs:OrganisationName"/>
													<xsl:choose>
														<xsl:when test="$lastsolicitor=$thissolicitor">
															<!--send a Y as the paramater to indicate duplicate solicitors-->
															<xsl:call-template name="DisplaySolicitorDetails">
																<xsl:with-param name="party" select="Y"/>
																<xsl:with-param name="language" select="$language"/>
															</xsl:call-template>
														</xsl:when>
														<xsl:otherwise>
															<!--send the solicitor structure as the parameter-->
															<xsl:call-template name="DisplaySolicitorDetails">
																<xsl:with-param name="party" select="../../cs:Counsel/cs:Solicitor/cs:Party"/>
																<xsl:with-param name="language" select="$language"/>
															</xsl:call-template>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:when>
												<!-- If no solicitor then check previous defendant for solicitor -->
												<xsl:otherwise>
													<xsl:choose>
														<!-- if previous has no solicitor either display quote -->
														<xsl:when test="not (../../../cs:Defendant[position()=$pos - 1]/cs:Counsel/cs:Solicitor/cs:Party/cs:Organisation or ../../../cs:Defendant[position()=$pos - 1]/cs:Counsel/cs:Solicitor/cs:Party/cs:Person)">
															<xsl:text>"</xsl:text>
														</xsl:when>
														<!-- if previous solicitor has solicitor then display standard text -->
														<xsl:otherwise>
															<xsl:call-template name="getValue">
																<xsl:with-param name="language" select="$language"/>
																<xsl:with-param name="key" select="'NO REPRESENTATION RECORDED'"/>
															</xsl:call-template>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell/>
									<fo:table-cell>
										<fo:block text-align="right">
											<xsl:value-of select="../../cs:URN"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</fo:block>
		<fo:block>
			<!-- Display the Prosecuting Reference -->
			<fo:table table-layout="fixed">
				<fo:table-column column-width="20mm"/>
				<fo:table-column column-width="170mm"/>
				<fo:table-body font-size="9pt">
					<fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$prosecuteRefText"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
	<!-- Template to convert the first character to upper case and the rest lower case -->
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
	<!-- Template to convert a string to upper case -->
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>
	<!-- Header information  on first page -->
	<xsl:template name="HeaderInfoFirstPage">
		<xsl:param name="language"/>
		<fo:block space-after="8pt">
			<fo:block text-align="left" font-size="18pt" font-weight="normal">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'The Crown Court'"/>
				</xsl:call-template>
			</fo:block>
			<!--Court name -->
			<fo:block font-size="15pt" font-weight="normal"/>
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="'at'"/>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:variable name="courthousename">
				<xsl:value-of select="cs:FirmList/cs:CrownCourt/cs:CourtHouseName"/>
			</xsl:variable>
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="$courthousename"/>
			</xsl:call-template>
		</fo:block>
		<fo:block text-align="center" font-size="15pt" font-weight="bold">
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="$reporttype"/>
			</xsl:call-template>
		</fo:block>
		<fo:block text-align="center" space-after="0pt">
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="'for the period'"/>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="displayDate">
				<xsl:with-param name="input" select="$reportdate"/>
				<xsl:with-param name="language" select="$language"/>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="getValue">
				<xsl:with-param name="language" select="$language"/>
				<xsl:with-param name="key" select="'to'"/>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="displayDate">
				<xsl:with-param name="input" select="$endDate"/>
				<xsl:with-param name="language" select="$language"/>
			</xsl:call-template>
		</fo:block>
		<fo:block text-align="center" font-size="9pt" line-height="18pt">
			<!-- remove the unnecessary character v in the version details -->
			<xsl:variable name="version">
				<xsl:choose>
					<xsl:when test="contains($initversion,' v')">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="substring-before($initversion,' v')"/>
						</xsl:call-template>
						<xsl:text> </xsl:text>
						<xsl:value-of select="substring-after($initversion,' v')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$initversion"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:value-of select="$version"/>
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" leader-length="100%"/>
		</fo:block>
	</xsl:template>
	<!-- Header information  on subsequent pages -->
	<xsl:template name="HeaderInfoOtherPages">
		<xsl:param name="language"/>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="100mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block text-align="left" font-size="8pt" font-weight="normal">
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="$reporttype"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="'for the period'"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="displayDate">
								<xsl:with-param name="input" select="$reportdate"/>
								<xsl:with-param name="language" select="$language"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="'to'"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="displayDate">
								<xsl:with-param name="input" select="$endDate"/>
								<xsl:with-param name="language" select="$language"/>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="center" font-size="8pt">
							<xsl:variable name="version">
								<xsl:choose>
									<xsl:when test="contains($initversion,' v')">
										<xsl:call-template name="getValue">
											<xsl:with-param name="language" select="$language"/>
											<xsl:with-param name="key" select="substring-before($initversion,' v')"/>
										</xsl:call-template>
										<xsl:text> </xsl:text>
										<xsl:value-of select="substring-after($initversion,' v')"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$initversion"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:value-of select="$version"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<!--page no-->
						<!-- display the page no -->
						<fo:block font-size="8pt" text-align="right">
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="'Page No'"/>
							</xsl:call-template>
							<xsl:text>: </xsl:text>
							<fo:page-number/>
							<xsl:text> </xsl:text>
							<xsl:call-template name="getValue">
								<xsl:with-param name="language" select="$language"/>
								<xsl:with-param name="key" select="'of'"/>
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<fo:page-number-citation ref-id="{$language}"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block text-align="left" font-size="8pt" font-weight="normal">
			<xsl:variable name="courthousename">
				<xsl:value-of select="cs:FirmList/cs:CrownCourt/cs:CourtHouseName"/>
			</xsl:variable>
			<xsl:variable name="courthousename2">
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="$courthousename"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:value-of select="$courthousename2"/>
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
	</xsl:template>
	<xsl:template name="CompanyAddressFooter">
		<xsl:param name="language"/>
		<fo:block>
			<xsl:if test="/cs:FirmList/cs:CrownCourt/cs:CourtHouseAddress">
				<!-- display address details -->
				<xsl:for-each select="/cs:FirmList/cs:CrownCourt/cs:CourtHouseAddress/apd:Line[not (.='-') and not (.=' ')]">
					<xsl:variable name="addLine">
						<xsl:call-template name="getValue">
							<xsl:with-param name="language" select="$language"/>
							<xsl:with-param name="key" select="."/>
						</xsl:call-template>
					</xsl:variable>
					
					<xsl:value-of select="$addLine"/>
					
					<xsl:if test="not (position() = last())">
						<xsl:if test="string-length() &gt; 0">
							<xsl:text>, </xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
				<xsl:text>, </xsl:text>
				<xsl:value-of select="/cs:FirmList/cs:CrownCourt/cs:CourtHouseAddress/apd:PostCode"/>
				<xsl:text>. </xsl:text>
			</xsl:if>
			<xsl:if test="/cs:FirmList/cs:CrownCourt/cs:CourtHouseDX">
				<xsl:value-of select="/cs:FirmList/cs:CrownCourt/cs:CourtHouseDX"/>
			</xsl:if>
			<xsl:if test="/cs:FirmList/cs:CrownCourt/cs:CourtHouseTelephone">
				<xsl:text> </xsl:text>
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Tel'"/>
				</xsl:call-template>
				<xsl:text>: </xsl:text>
				<xsl:value-of select="/cs:FirmList/cs:CrownCourt/cs:CourtHouseTelephone"/>
			</xsl:if>
			<xsl:if test="/cs:FirmList/cs:CrownCourt/cs:CourtHouseFax">
				<xsl:text> </xsl:text>
				<xsl:call-template name="getValue">
					<xsl:with-param name="language" select="$language"/>
					<xsl:with-param name="key" select="'Fax'"/>
				</xsl:call-template>
				<xsl:text>: </xsl:text>
				<xsl:value-of select="/cs:FirmList/cs:CrownCourt/cs:CourtHouseFax"/>
			</xsl:if>
		</fo:block>
	</xsl:template>
	<!--List Footer for first page-->
	<xsl:template name="ListFooterFirstPage">
		<xsl:param name="language"/>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="185mm"/>
			<fo:table-body>
				<xsl:for-each select="cs:FirmList/cs:DocumentID">
					<fo:table-row font-size="7pt">
						<fo:table-cell>
							<xsl:call-template name="CompanyAddressFooter">
								<xsl:with-param name="language" select="$language"/>
							</xsl:call-template>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row font-size="7pt">
						<fo:table-cell>
							<fo:table table-layout="fixed">
								<fo:table-column column-width="100mm"/>
								<fo:table-column column-width="85mm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<!-- display published details -->
											<xsl:if test="../cs:ListHeader/cs:PublishedTime">
												<fo:block>
													<xsl:choose>
														<!-- Test to see if the Published Date is the default -->
														<xsl:when test="substring(../cs:ListHeader/cs:PublishedTime,1,4)='1900'">
                                                            Unspecified Published Time
                                                        </xsl:when>
														<xsl:otherwise>
															<xsl:call-template name="getValue">
																<xsl:with-param name="language" select="$language"/>
																<xsl:with-param name="key" select="'Published'"/>
															</xsl:call-template>
															<xsl:text>: </xsl:text>
															<xsl:call-template name="displayDate">
																<xsl:with-param name="input" select="substring(../cs:ListHeader/cs:PublishedTime,1,10)"/>
																<xsl:with-param name="language" select="$language"/>
															</xsl:call-template>
															<xsl:text> </xsl:text>
															<xsl:call-template name="getValue">
																<xsl:with-param name="language" select="$language"/>
																<xsl:with-param name="key" select="'at'"/>
																<xsl:with-param name="context" select="'time'"/>
															</xsl:call-template>
															<xsl:text> </xsl:text>
															<xsl:value-of select="substring(../cs:ListHeader/cs:PublishedTime,12,5)"/>
														</xsl:otherwise>
													</xsl:choose>
												</fo:block>
											</xsl:if>
										</fo:table-cell>
										<!--CREST Print Ref-->
										<fo:table-cell text-align="right">
											<fo:block font-size="7pt">
												<xsl:value-of select="../cs:ListHeader/cs:CRESTprintRef"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:table-cell>
					</fo:table-row>
					<xsl:if test="$language != $DefaultLang">
						<fo:table-row font-size="7pt">
							<fo:table-cell number-columns-spanned="2">
								<fo:block>
									<xsl:call-template name="NOT_FOUND_FOOTER">
										<xsl:with-param name="language" select="$language"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block space-after="5pt"/>
		<fo:block font-size="10pt">
			<xsl:call-template name="displayCopyright">
				<xsl:with-param name="language" select="$language"/>
			</xsl:call-template>
		</fo:block>
	</xsl:template>
	<xsl:template name="ListFooterOtherPages">
		<xsl:param name="language"/>
		<xsl:call-template name="ListFooterFirstPage">
			<xsl:with-param name="language" select="$language"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Display Copyright Info -->
	<xsl:template name="displayCopyright">
		<xsl:param name="language"/>
		<xsl:text>&#x00A9; </xsl:text>
		<xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="'Crown copyright 2003. All rights reserved. Issued by HM Courts '"/>
        </xsl:call-template>
        <xsl:if test="$language = $DefaultLang">
            <xsl:text>&amp;</xsl:text>
        </xsl:if>
        <xsl:call-template name="getValue">
            <xsl:with-param name="language" select="$language"/>
            <xsl:with-param name="key" select="' Tribunals Service.'"/>
        </xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
