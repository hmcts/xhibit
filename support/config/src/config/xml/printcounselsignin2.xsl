<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0" xmlns:dt="http://xsltsl.org/date-time">
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<!-- Set up the page -->
				<fo:simple-page-master margin-right="10mm" margin-left="10mm" margin-bottom="10mm" margin-top="10mm" page-width="210mm" page-height="297mm" master-name="simple">
					<fo:region-body margin-top="20mm" margin-bottom="15mm"/>
					<fo:region-before extent="20mm"/>
					<fo:region-after extent="15mm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<!-- Header Info -->
				<fo:static-content flow-name="xsl-region-before" font-size="10pt">
					<!-- Call template to display header info -->
					<xsl:call-template name="displayHeader">
						<xsl:with-param name="crtType">
							<xsl:call-template name="courtType">
								<xsl:with-param name="type">
									<xsl:value-of select="court-list/court-type"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:with-param>
						<xsl:with-param name="crtName">
							<xsl:call-template name="TitleCase">
								<xsl:with-param name="text">
									<xsl:value-of select="court-list/court-name"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:with-param>
						<xsl:with-param name="listDate">
							<!-- Call template to format the list date -->
							<xsl:call-template name="displayDayDate">
								<xsl:with-param name="input" select="substring(court-list/start-date,1,10)"/>
							</xsl:call-template>
						</xsl:with-param>
					</xsl:call-template>
				</fo:static-content>
				<!-- Footer Info -->
				<fo:static-content flow-name="xsl-region-after" font-size="10pt">
					<fo:block>
						<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
					</fo:block>
					<fo:block>
						<xsl:text>Published: </xsl:text>
						<xsl:call-template name="displayDate">
							<xsl:with-param name="input">
								<xsl:value-of select="substring(court-list/request-date,1,10)"/>
							</xsl:with-param>
						</xsl:call-template>
						at <xsl:value-of select="substring(court-list/request-date,12,5)"/>
					</fo:block>
					<!-- Display the page numbers -->
					<fo:block text-align="right">
						<xsl:text>Page </xsl:text>
						<fo:page-number/>
						<xsl:text> of </xsl:text>
						<fo:page-number-citation ref-id="theEnd"/>
					</fo:block>
				</fo:static-content>
				<!-- Main Body -->
				<fo:flow flow-name="xsl-region-body" font-size="10pt">
					<!-- Apply template to display report details -->
					<xsl:apply-templates select="court-list"/>
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- TOP LEVEL MATCH Display Information for the report -->
	<xsl:template match="court-list">
		<!-- Loop through each court room -->
		<xsl:for-each select="court-rooms">
			<!-- Insert page break for each  court room -->
			<xsl:if test="position() != 1">
				<fo:block break-before="page"/>
			</xsl:if>
			<xsl:variable name="courtDetails">
        			<!-- Pick up short name if one exists -->
        			<xsl:if test="court-site-short-name != 'null' and court-site-short-name != ''">
        				<xsl:value-of select="court-site-short-name"/>
        				<xsl:text> - </xsl:text>
        			</xsl:if>
				<xsl:choose>
					<!-- Check Court Room status -->
					<xsl:when test="@floating='false'">
						<xsl:value-of select="court-room-display-name"/>						
					</xsl:when>
					<xsl:when test="@floating='true'">
						<xsl:text>Unassigned</xsl:text>					
					</xsl:when>
				</xsl:choose>
			</xsl:variable>

        		<!-- Loop through each sitting -->
        		<xsl:for-each select="sittings">
        			<!-- Call template to display sitting details -->
        			<xsl:call-template name="displaySittingInfo">
        				<xsl:with-param name="court">
        				    <xsl:value-of select="$courtDetails"/>
        				</xsl:with-param>
        				<xsl:with-param name="sittingTime">
        					<xsl:call-template name="displayTimeHHMM">
        						<xsl:with-param name="input">
        							<xsl:value-of select="sitting-time"/>
        						</xsl:with-param>
        					</xsl:call-template>
        				</xsl:with-param>
        				<xsl:with-param name="judge">
        					<!-- Display the Judge name in upper case -->
        					<xsl:call-template name="toUpper">
        						<xsl:with-param name="content">
        							<xsl:value-of select="judge-name"/>
        						</xsl:with-param>
        					</xsl:call-template>
        				</xsl:with-param>
        			</xsl:call-template>
        			
        			<!-- Call template to display court clerk info -->
        			<xsl:call-template name="displayCourtClerkUsher"/>
        			
        			<!-- Call template to display table headers -->
        			<xsl:call-template name="displayTableHeadings"/>
        			
        			<!-- Display info for each scheduled hearing -->
        			<xsl:for-each select="scheduled-hearings">
        				<!-- Call template to display defendant details -->
        				<xsl:call-template name="displayDefendant">
        					<xsl:with-param name="notBeforeTime">
        						<xsl:call-template name="displayTimeHHMMPost">
        							<xsl:with-param name="input" select="not-before-time"/>
        						</xsl:call-template>
        					</xsl:with-param>
        					<xsl:with-param name="hrgType">
        						<xsl:value-of select="hearing-type"/>
        					</xsl:with-param>
        					<xsl:with-param name="caseNo">
        						<xsl:value-of select="case-type"/>
        						<xsl:value-of select="case-number"/>
        					</xsl:with-param>
        				</xsl:call-template>
        			</xsl:for-each> <!-- each scheduled hearing -->
        		</xsl:for-each> <!-- each sitting -->
		</xsl:for-each> <!-- each court room -->
	</xsl:template>

	<!-- ****************************************** TEMPLATES START HERE ******************************************-->

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

	<!-- Template to display Court Type -->
	<xsl:template name="courtType">
		<xsl:param name="type"/>
		<!--<xsl:choose>
			<xsl:when test="$type = 'CROWN'">Crown Court</xsl:when>
		</xsl:choose> -->
		<!-- Hardcoded to always return Crown Court -->
		<xsl:text>Crown Court</xsl:text>
	</xsl:template>

	<!-- Display Defendant Info -->
	<xsl:template name="defendant">
	<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
							<xsl:value-of select="surname"/>
							<xsl:text>, </xsl:text>
							<xsl:call-template name="TitleCase">
								<xsl:with-param name="text">
									<xsl:value-of select="first-name"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							<xsl:if test="position() = 1">
								<xsl:call-template name="prosAdvocate"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							<!-- Call template to display defence advocate info -->
							<xsl:call-template name="defAdvocate"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>

	<!-- Template to display Defendant Advocate -->
	<xsl:template name="defAdvocate">
		<xsl:for-each select="defendant-advocates">
			<xsl:value-of select="surname"/>
			<xsl:if test="surname and first-name">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:call-template name="TitleCase">
				<xsl:with-param name="text">
					<xsl:value-of select="first-name"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="TitleCase">
				<xsl:with-param name="text">
					<xsl:value-of select="title"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:if test="position() != last()">
				<fo:block/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- Display Court Clerk / Usher Info -->
	<xsl:template name="displayCourtClerkUsher">
	<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:text>Court Clerk(s):</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<!-- Handle long court clerk names -->
						<fo:block hyphenate="true" language="en">
							<!-- Display the Court Clerks, Sort alphabeticlly-->
							<xsl:for-each select="court-clerks">
								<xsl:sort select="."/>
								<xsl:value-of select="."/>
								<xsl:if test="position() != last()">
									<fo:block/>
								</xsl:if>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:text>Usher(s):</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<!-- Handle long Usher names -->
						<fo:block hyphenate="true" language="en">
							<!-- Display the Ushers, Sort alphabetically -->
							<xsl:for-each select="ushers">
								<xsl:sort select="."/>
								<xsl:value-of select="."/>
								<xsl:if test="position() != last()">
									<fo:block/>
								</xsl:if>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>
		
	<!-- Date Formatting Template -->
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

	<!-- Template to display Case and Defendant Details -->
	<xsl:template name="displayDefendant">
		<xsl:param name="notBeforeTime"/>
		<xsl:param name="hrgType"/>
		<xsl:param name="caseNo"/>
		<fo:block space-before="10pt">
			<xsl:text>Not Before </xsl:text>
			<xsl:value-of select="$notBeforeTime"/>
		</fo:block>
		<fo:block font-weight="bold">
			<xsl:value-of select="$hrgType"/>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="160mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:value-of select="$caseNo"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<!-- Handle long defendant names -->
						<!--	defence advocate / prosecution advocate names  also handled as displayed from defendants template-->
						<fo:block hyphenate="true" language="en">
							<!-- Display information for each defendant -->
							<xsl:choose>
								<xsl:when test="case-type='A' or case-type='S' or case-type='T'">
								<fo:block>
									<fo:table table-layout="fixed">
										<fo:table-column column-width="160mm"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block>
														<xsl:for-each select="defendants">
															<!-- Call defendant template -->
															<xsl:call-template name="defendant"/>
														</xsl:for-each> 
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
									</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<fo:block>
										<xsl:value-of select="case-title"/>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>

	<!-- Template to display header info -->
	<xsl:template name="displayHeader">
		<xsl:param name="crtType"/>
		<xsl:param name="crtName"/>
		<xsl:param name="listDate"/>
		<fo:block text-align="start" font-weight="bold">
			<xsl:text>The </xsl:text>
			<xsl:value-of select="$crtType"/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="$crtName"/>
		</fo:block>
		<fo:block text-align="start" space-before="10pt">
			<xsl:text>Court List for </xsl:text>
			<xsl:value-of select="$listDate"/>
			<xsl:text> at </xsl:text>
			<xsl:value-of select="$crtName"/>
		</fo:block>
	</xsl:template>

	<!-- Template to display the sitting information -->
	<xsl:template name="displaySittingInfo">
		<xsl:param name="court"/>
		<xsl:param name="sittingTime"/>
		<xsl:param name="judge"/>
		<fo:block>
		<fo:table table-layout="fixed" space-before="10pt">
			<fo:table-column column-width="45mm"/>
			<fo:table-column column-width="70mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:value-of select="$court"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							<xsl:text>- Sitting at </xsl:text>
							<xsl:value-of select="$sittingTime"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block font-weight="bold" text-align="center" space-before="10pt" space-after="10pt">
			<xsl:value-of select="$judge"/>
		</fo:block>
	</xsl:template>

	<!-- Display table header Info -->
	<xsl:template name="displayTableHeadings">
	<fo:block>
		<fo:table table-layout="fixed" space-before="10pt">
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:text>Defendants</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:text>Pros/Resp</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">
							<xsl:text>Def/Appllnt</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>

	<!-- Format the time -->
	<xsl:template name="displayTimeHHMM">
		<xsl:param name="input"/>
		<xsl:variable name="hour" select="substring($input,12,2)"/>
		<xsl:variable name="min" select="substring($input,15,2)"/>
		<xsl:value-of select="$hour"/>
		<xsl:text>:</xsl:text>
		<xsl:value-of select="$min"/>
	</xsl:template>
	
	<!-- Format the time with postfix-->
	<xsl:template name="displayTimeHHMMPost">
		<xsl:param name="input"/>
		<xsl:variable name="hour" select="substring($input,12,2)"/>
		<xsl:variable name="min" select="substring($input,15,2)"/>
		<xsl:call-template name="FormatTime">
			<xsl:with-param name="input">
				<xsl:value-of select="$hour"/>
				<xsl:text>:</xsl:text>
				<xsl:value-of select="$min"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	

	<!-- template used to format the time from 24hrs to the following format HH:MM [AM/PM]-->
	<!-- display the formatted time and then post fix with AM or PM -->
	<xsl:template name="FormatTime">
		<xsl:param name="input"/>
		<xsl:choose>
			<!-- if less than 12 postfix AM -->
			<xsl:when test="substring-before($input,':') &lt; 12">
				<xsl:value-of select="$input"/>
				<xsl:text> AM</xsl:text>
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
				<xsl:text> PM</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template to display prosecution advocate -->
	<xsl:template name="prosAdvocate">
		<!-- Display details for each prosecution advocate -->
		<xsl:for-each select="../prosecution-advocates">
			<xsl:value-of select="surname"/>
			<xsl:if test="surname and first-name">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:call-template name="TitleCase">
				<xsl:with-param name="text">
					<xsl:value-of select="first-name"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:call-template name="TitleCase">
				<xsl:with-param name="text">
					<xsl:value-of select="title"/>
				</xsl:with-param>
			</xsl:call-template>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>

	<!-- Template used to capitalise the first character of text and put the rest into lower case  -->
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

	<!-- Template used to convert a string to upper case -->
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>
	
	<!-- ****************************************** TEMPLATES END HERE ******************************************-->
</xsl:stylesheet>