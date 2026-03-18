<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

	<xsl:template name="CourtLogEvents">
	<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-number="1" column-width="30mm"/>
			<fo:table-column column-number="2" column-width="20mm"/>
			<fo:table-column column-number="3"/>
			<fo:table-header>
				<fo:table-row background-color="#EEEEEE">
					<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
						<fo:block font-weight="bold" font-size="9pt">
							<xsl:text>Date</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
						<fo:block font-weight="bold" font-size="9pt">
							<xsl:text>Time</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
						<fo:block font-weight="bold" font-size="9pt">
							<xsl:text>Events</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:apply-templates select="./charge-log-items"/>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>

	<xsl:template match="charge-log-items">
		<!-- variables for getting the log event header and the log event text -->
		<xsl:variable name="event_header_begin" select="'&lt;event_header&gt;'"/>
		<xsl:variable name="event_header_end" select="'&lt;/event_header&gt;'"/>
		<xsl:variable name="event_text_begin" select="'&lt;event_text&gt;'"/>
		<xsl:variable name="event_text_end" select="'&lt;/event_text&gt;'"/>
		<!-- keep-together.within-column="always" keeps row together for page breaks -->
		<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
			<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
				<!--  Date column -->
				<fo:block space-before="1pt" space-after="1pt">
					<!-- call template to display the date in the required format DD/MMM/YYYY -->
					<xsl:call-template name="displayDate">
						<xsl:with-param name="input">
							<xsl:value-of select="entry-date"/>
						</xsl:with-param>
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
				<!--  Time column -->
				<fo:block space-before="1pt" space-after="1pt">
					<xsl:value-of select="substring(entry-date, 12, 2)"/>
					<xsl:text>:</xsl:text>
					<xsl:value-of select="substring(entry-date, 15, 2)"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
				<xsl:variable name="entryDate" select="concat(substring(entry-date, 1, 4), substring(entry-date, 6, 2), substring(entry-date, 9, 2))"/>
				<xsl:variable name="updateDate" select="concat(substring(last-update-date, 1, 4), substring(last-update-date, 6, 2), substring(last-update-date, 9, 2))"/>
				<!--  Event column -->
				<xsl:choose>
					<!-- event created for the scheduled hearing -->
					<xsl:when test="$entryDate=$updateDate">
						<!-- event header -->
						<fo:block linefeed-treatment="preserve"
								  white-space-treatment='preserve'
								  white-space-collapse='false'
								  font-weight="bold"
								  space-before="1pt"
								  space-after="1pt">
							<xsl:variable name="s1" select="substring-after(log-entry, $event_header_begin)"/>
							<xsl:call-template name="replace-escape">
								<xsl:with-param name="text" select="substring-before($s1, $event_header_end)"/>
							</xsl:call-template>
						</fo:block>
						<!-- event text -->
						<fo:block linefeed-treatment="preserve"
								  white-space-treatment='preserve'
								  white-space-collapse='false'
								  hyphenate="true"
								  space-before="1pt"
								  space-after="1pt">
							<xsl:variable name="s2" select="substring-after(log-entry, $event_text_begin)"/>
							<xsl:call-template name="replace-escape">
								<xsl:with-param name="text" select="substring-before($s2, $event_text_end)"/>
							</xsl:call-template>
						</fo:block>
					</xsl:when>
					<!-- event created outside a scheduled hearing are displayed in italics -->
					<xsl:otherwise>
						<!-- event header -->
						<fo:block linefeed-treatment="preserve"
								  white-space-treatment='preserve'
								  white-space-collapse='false'
								  font-weight="bold"
								  font-style="italic"
								  space-before="1pt"
								  space-after="1pt">
							<xsl:variable name="s1" select="substring-after(log-entry, $event_header_begin)"/>
							<xsl:call-template name="replace-escape">
								<xsl:with-param name="text" select="substring-before($s1, $event_header_end)"/>
							</xsl:call-template>
						</fo:block>
						<!-- event text -->
						<fo:block font-style="italic"
								  linefeed-treatment="preserve"
								  white-space-treatment='preserve'
								  white-space-collapse='false'
								  hyphenate="true"
								  space-before="1pt"
								  space-after="1pt">
							<xsl:variable name="s2" select="substring-after(log-entry, $event_text_begin)"/>
							<xsl:call-template name="replace-escape">
								<xsl:with-param name="text" select="substring-before($s2, $event_text_end)"/>
							</xsl:call-template>
						</fo:block>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Display the date in the following format DD/MMM/YYYY -->
	<xsl:template name="displayDate">
		<xsl:param name="input"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:value-of select="$day"/>
		<xsl:text>/</xsl:text>
		<xsl:choose>
			<xsl:when test="$month='01'">Jan</xsl:when>
			<xsl:when test="$month='02'">Feb</xsl:when>
			<xsl:when test="$month='03'">Mar</xsl:when>
			<xsl:when test="$month='04'">Apr</xsl:when>
			<xsl:when test="$month='05'">May</xsl:when>
			<xsl:when test="$month='06'">Jun</xsl:when>
			<xsl:when test="$month='07'">Jul</xsl:when>
			<xsl:when test="$month='08'">Aug</xsl:when>
			<xsl:when test="$month='09'">Sep</xsl:when>
			<xsl:when test="$month='10'">Oct</xsl:when>
			<xsl:when test="$month='11'">Nov</xsl:when>
			<xsl:when test="$month='12'">Dec</xsl:when>
		</xsl:choose>
		<xsl:text>/</xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>

	<!-- A portion of the XML passed in has been XML character escaped twice.
		This template reverses the double expansion of the ampersand character.
	-->
	<xsl:template name="replace-escape">
		<xsl:param name="text"/>
		<xsl:call-template name="replace-string">
			<xsl:with-param name="text">
				<xsl:call-template name="replace-string">
					<xsl:with-param name="text">
						<xsl:call-template name="replace-string">
							<xsl:with-param name="text" select="$text"/>
							<xsl:with-param name="replace" select="'&amp;gt;'"/>
							<xsl:with-param name="with" select="'&gt;'"/>
						</xsl:call-template>
					</xsl:with-param>
					<xsl:with-param name="replace" select="'&amp;lt;'"/>
					<xsl:with-param name="with" select="'&lt;'"/>
				</xsl:call-template>
			</xsl:with-param>
			<xsl:with-param name="replace" select="'&amp;amp;'"/>
			<xsl:with-param name="with" select="'&amp;'"/>
		</xsl:call-template>
	</xsl:template>

	<!-- string replacement template -->
	<xsl:template name="replace-string">
		<xsl:param name="text"/>
		<xsl:param name="replace"/>
		<xsl:param name="with"/>
		<xsl:choose>
			<xsl:when test="contains($text,$replace)">
				<xsl:value-of select="substring-before($text,$replace)"/>
				<xsl:value-of select="$with"/>
				<xsl:call-template name="replace-string">
					<xsl:with-param name="text" select="substring-after($text,$replace)"/>
					<xsl:with-param name="replace" select="$replace"/>
					<xsl:with-param name="with" select="$with"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
