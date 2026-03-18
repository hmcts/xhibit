<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

	<xsl:import href="/config/xml/PrintCourtLogEvents.xsl"/>

	<xsl:template match="court-log-controller-print-value">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="29.7cm" page-height="21.0cm" master-name="simple">
					<fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="end" font-size="9pt" font-family="serif" line-height="14pt">
						<xsl:text>XHIBIT 2 - Page </xsl:text>
						<fo:page-number/>
						<xsl:text> of </xsl:text>
						<fo:page-number-citation ref-id="theEnd"/>
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="./court-log-controller-print-composite-value"/>
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template match="court-log-controller-print-composite-value">
		<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="left" space-after.optimum="15pt" break-before="page">
			<xsl:text>Case Log</xsl:text>
		</fo:block>
        <xsl:choose>
            <!-- process related hearing values -->
            <xsl:when test="type='related'">
                <!-- Related Header -->
                <fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" text-align="left">
        			<xsl:text>Related court log entries</xsl:text>
        		</fo:block>
                <!-- Court Log Events -->
                <xsl:call-template name="EventsSubHeading"/>
                <xsl:call-template name="CourtLogEvents"/>
            </xsl:when>
            <xsl:otherwise>
                <!-- process normal hearing values -->
        		<xsl:choose>
        			<!-- Trial Header -->
        			<xsl:when test="@type-of-case=0 or @type-of-case=3 or @type-of-case=4 or @type-of-case=5 or @type-of-case=6 or @type-of-case=7">
        				<fo:block>
        				<fo:table inline-progression-dimension.minimum="100%" table-layout="fixed" language="en" hyphenate="true" hyphenation-remain-character-count="10">
        					<fo:table-column column-number="1"/>
        					<fo:table-column column-number="2"/>
        					<fo:table-column column-number="3"/>
        					<fo:table-column column-number="4"/>
        					<fo:table-column column-number="5"/>
        					<fo:table-column column-number="6"/>
        					<fo:table-column column-number="7"/>
        					<fo:table-column column-number="8"/>
        					<fo:table-column column-number="9"/>
                                                <fo:table-column column-number="10"/>
        					<fo:table-header>
        						<fo:table-row background-color="#EEEEEE">
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Case</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Defendant(s)</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Judge</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Prosecution Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Defence Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Hearing Type</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Time Listed</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Court Reporter</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Linked Cases</xsl:text>
        								</fo:block>
        							</fo:table-cell>
                                                                <fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Court Clerk</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        						</fo:table-row>
        					</fo:table-header>
        					<fo:table-body>
        						<fo:table-row>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./case-number"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./defendants"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./judge"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./pros-advocate"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./def-advocate"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./hearing-type"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./time-listed"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./court-reporter"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./linked-cases"/>
        								</fo:block>
        							</fo:table-cell>
                                                                <fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./court-clerk"/>
        								</fo:block>
        							</fo:table-cell>
        						</fo:table-row>
        					</fo:table-body>
        				</fo:table>
        				</fo:block>
        				<!-- Court Log Events -->
        				<xsl:call-template name="EventsSubHeading"/>
        				<xsl:call-template name="CourtLogEvents"/>
        			</xsl:when>
        			<!-- Criminal Appeal Header -->
        			<xsl:when test="@type-of-case=1">
        				<fo:block>
        				<fo:table inline-progression-dimension.minimum="100%" table-layout="fixed" language="en" hyphenate="true" hyphenation-remain-character-count="10">
        					<fo:table-column column-number="1"/>
        					<fo:table-column column-number="2"/>
        					<fo:table-column column-number="3"/>
        					<fo:table-column column-number="4"/>
        					<fo:table-column column-number="5"/>
        					<fo:table-column column-number="6"/>
        					<fo:table-column column-number="7"/>
        					<fo:table-column column-number="8"/>
        					<fo:table-column column-number="9"/>
        					<fo:table-column column-number="10"/>
        					<fo:table-column column-number="11"/>
        					<fo:table-column column-number="12"/>
                                                <fo:table-column column-number="13"/>
        					<fo:table-header>
        						<fo:table-row background-color="#EEEEEE">
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Case</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Appellant</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Judge</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 1</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 2</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 3</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 4</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Respondent Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Appellant Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Hearing Type</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Time Listed</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Court Reporter</xsl:text>
        								</fo:block>
        							</fo:table-cell>
                                                                <fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Court Clerk</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        						</fo:table-row>
        					</fo:table-header>
        					<fo:table-body>
        						<fo:table-row>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./case-number"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./defendants"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./judge"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice01"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice02"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice03"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice04"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./respondent"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./def-advocate"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./hearing-type"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./time-listed"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./court-reporter"/>
        								</fo:block>
        							</fo:table-cell>
                                                                <fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./court-clerk"/>
        								</fo:block>
        							</fo:table-cell>
        						</fo:table-row>
        					</fo:table-body>
        				</fo:table>
        				</fo:block>
        				<!-- Court Log Events -->
        				<xsl:call-template name="EventsSubHeading"/>
        				<xsl:call-template name="CourtLogEvents"/>
        			</xsl:when>
        			<!-- Misc. Appeal Header -->
        			<xsl:when test="@type-of-case=2">
        				<fo:block>
        				<fo:table inline-progression-dimension.minimum="100%" table-layout="fixed" language="en" hyphenate="true" hyphenation-remain-character-count="10">
        					<fo:table-column column-number="1"/>
        					<fo:table-column column-number="2"/>
        					<fo:table-column column-number="3"/>
        					<fo:table-column column-number="4"/>
        					<fo:table-column column-number="5"/>
        					<fo:table-column column-number="6"/>
        					<fo:table-column column-number="7"/>
        					<fo:table-column column-number="8"/>
        					<fo:table-column column-number="9"/>
        					<fo:table-column column-number="10"/>
        					<fo:table-column column-number="11"/>
        					<fo:table-column column-number="12"/>
        					<fo:table-column column-number="13"/>
                                                <fo:table-column column-number="14"/>
        					<fo:table-header>
        						<fo:table-row background-color="#EEEEEE">
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Case</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Appellant</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Judge</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 1</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 2</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 3</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Justice 4</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Respondent Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Objector Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Appellant Advocate</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Hearing Type</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Time Listed</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Court Reporter</xsl:text>
        								</fo:block>
        							</fo:table-cell>
                                                                <fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-weight="bold" font-size="9pt">
        									<xsl:text>Court Clerk</xsl:text>
        								</fo:block>
        							</fo:table-cell>
        						</fo:table-row>
        					</fo:table-header>
        					<fo:table-body>
        						<fo:table-row>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./case-number"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./defendants"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./judge"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice01"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice02"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice03"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./justice04"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./respondent"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./obj-advocate"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./def-advocate"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./hearing-type"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./time-listed"/>
        								</fo:block>
        							</fo:table-cell>
        							<fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./court-reporter"/>
        								</fo:block>
        							</fo:table-cell>
                                                                <fo:table-cell border-style="ridge" border-width="1px" padding="1mm">
        								<fo:block font-size="9pt">
        									<xsl:value-of select="./court-clerk"/>
        								</fo:block>
        							</fo:table-cell>
        						</fo:table-row>
        					</fo:table-body>
        				</fo:table>
        				</fo:block>
        				<!-- Court Log Events -->
        				<xsl:call-template name="EventsSubHeading"/>
        				<xsl:call-template name="CourtLogEvents"/>
        			</xsl:when>
        		</xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
	</xsl:template>

	<xsl:template name="EventsSubHeading">
		<fo:block font-size="12pt" font-family="sans-serif"  space-before.optimum="15pt" space-after.optimum="15pt" font-weight="bold" text-align="left" >
			<xsl:text>Sequence of events</xsl:text>
		</fo:block>
	</xsl:template>

</xsl:stylesheet>
