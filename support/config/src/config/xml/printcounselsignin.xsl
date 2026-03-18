<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1.5cm" margin-left="1.5cm" margin-bottom="1cm" margin-top="1cm"
												page-width="29.7cm" page-height="21.0cm" master-name="simple">
					<fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<!-- Display the heading. Just get the first date and display that. -->
					<fo:block text-align="start" font-size="10pt" font-family="serif" line-height="14pt" font-weight="bold">
						<xsl:text>Counsel Sign In</xsl:text>
						<xsl:if test="print-party-on-case-value-list/party-on-case-value-list/time-listed != '' ">
							<xsl:text> - </xsl:text>
							<xsl:call-template name="displayDateDDMMMYYYY">
	                					<xsl:with-param name="input">
	                				 		<xsl:value-of select="print-party-on-case-value-list/party-on-case-value-list/time-listed"/>
					                	</xsl:with-param>
				           	   </xsl:call-template>
						</xsl:if>
					</fo:block>
					<!-- Display the page numbers -->
					<fo:block text-align="right" font-size="10pt" font-family="serif" line-height="14pt" font-weight="bold">
						<xsl:text>XHIBIT 2 - Page </xsl:text>
						<fo:page-number/>
						<xsl:text> of </xsl:text>
						<fo:page-number-citation ref-id="theEnd"/>
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="print-party-on-case-value-list"/>
 					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template match="print-party-on-case-value-list">
	<fo:block>
 	 	<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid">
			<fo:table-column column-width="22mm" column-number="1" />
			<fo:table-column column-width="24mm" column-number="2"/>
			<fo:table-column column-width="24mm" column-number="3"/>
			<fo:table-column column-width="16mm" column-number="4"/>
			<fo:table-column column-width="34mm" column-number="5"/>
			<fo:table-column column-width="10mm" column-number="6"/>
			<fo:table-column column-width="53mm" column-number="7"/>
			<fo:table-column column-width="17mm" column-number="8"/>
			<fo:table-column column-width="65mm" column-number="9"/>
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Court Room</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Court clerk</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Usher</xsl:text>
						 </fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Case Number</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Hearing Type</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Time Listed</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Defendant</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Legal Role</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
						<fo:block font-weight="bold" font-size="8pt">
							<xsl:text>Counsel</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
			<xsl:for-each select="party-on-case-value-list">
				<fo:table-row>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
                            <xsl:if test="court-site-short-name != 'null' and court-site-short-name != ''">
                                <xsl:value-of select="court-site-short-name"/>
                                <xsl:text> - </xsl:text>
                            </xsl:if>
							<xsl:if test="is-floating = 1">Unassigned</xsl:if>
							<xsl:if test="is-floating != 1"><xsl:value-of select="court-room-display-name"/></xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
						    	<xsl:for-each select="court-clerk">
								<fo:block font-size="8pt">
									<xsl:if test="name != '' ">
										<xsl:value-of select="name"/>
									</xsl:if>
								</fo:block>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
							<xsl:for-each select="usher">
								<fo:block font-size="8pt">
									<xsl:if test="name != '' ">
										<xsl:value-of select="name"/>
									</xsl:if>
								</fo:block>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
							<xsl:value-of select="case-type"/><xsl:value-of select="case-number"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
							<xsl:value-of select="hearing-type-desc"/>
						</fo:block>
					</fo:table-cell>

					<!-- Time listed -->
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
						<xsl:call-template name="displayTimeHHMM">
                					<xsl:with-param name="input">
				                  		<xsl:value-of select="time-listed"/>
				                	</xsl:with-param>
				              </xsl:call-template>
						</fo:block>
					</fo:table-cell>

					<!-- Defendant names -->
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
                            <xsl:choose>
                                <xsl:when test="case-type='A' or case-type='S' or case-type='T'">
                                    <xsl:for-each select="defendants">
                                        <fo:block font-size="8pt">
                                            <xsl:if test="sur-name != '' ">
                                                <xsl:value-of select="sur-name"/>
                                            </xsl:if>
                                            <xsl:if test=" sur-name  != '' and first-name != '' ">
                                                <xsl:text>, </xsl:text>
                                                <xsl:value-of select="first-name"/>
                                            </xsl:if>
                                            <xsl:if test=" sur-name != '' ">
                                                <xsl:text> </xsl:text>
                                                <xsl:value-of select="initials"/>
                                            </xsl:if>
                                        </fo:block>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise>
                                    <fo:block font-size="8pt">
                                        <xsl:value-of select="case-title"/>
                                    </fo:block>
                                </xsl:otherwise>
                            </xsl:choose>
						</fo:block>
					</fo:table-cell>

					<!-- Party Roles -->
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
							<xsl:if test="party-role = 'P'">
								<xsl:text>Prosecution</xsl:text>
							</xsl:if>
							<xsl:if test="party-role = 'A'">
								<xsl:text>Appellant</xsl:text>
							</xsl:if>
							<xsl:if test="party-role = 'R'">
								<xsl:text>Respondent</xsl:text>
							</xsl:if>
							<xsl:if test="party-role = 'O'">
								<xsl:text>Objector</xsl:text>
							</xsl:if>
							<xsl:if test="party-role = 'D'">
								<xsl:text>Defendant</xsl:text>
							</xsl:if>
							<xsl:if test="party-role != 'D' and party-role != 'O' and party-role != 'R' and party-role != 'A' and party-role != 'P'">
								<xsl:text>party-role</xsl:text>
							</xsl:if>
						</fo:block>
					</fo:table-cell>

					<!-- Legal Representatives names and addresses -->
					<fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm" >
						<fo:block font-size="8pt">
							<!-- Display the representatives from the collection -->
							<xsl:for-each select="representatives">

								<fo:block font-size="8pt">
									<!-- If the defendant or appellant is representing themselves display this -->
									<xsl:if test="sol-firm-or-ref-legal-rep='I'">
										<xsl:text>In Person</xsl:text>
									</xsl:if>
									<xsl:if test="legal-rep-type = 'A'">
										<xsl:text>Advocate: </xsl:text>
									</xsl:if>
									<xsl:if test="legal-rep-type = 'S'">
										<xsl:text>Solicitor: </xsl:text>
									</xsl:if>
												<xsl:value-of select="leg-rep-title"/>
												<xsl:text> </xsl:text>
												<xsl:value-of select="leg-rep-first-name"/>
												<xsl:text> </xsl:text>
									<xsl:value-of select="leg-rep-surname"/>
								</fo:block>
								<fo:block font-size="8pt">
									<xsl:if test="ref-solicitor-firm-id > 0 and solicitor-firm-name != '' ">
										<xsl:text>Firm: </xsl:text><xsl:value-of select="solicitor-firm-name"/>
									</xsl:if>
									<xsl:if test="ref-chamber-id > 0 and chamber-firm-name != '' ">
										<xsl:text>Chamber: </xsl:text><xsl:value-of select="chamber-firm-name"/>
									</xsl:if>
								</fo:block>
								<fo:block font-size="8pt">
									<xsl:value-of select="address1"/>
									<xsl:value-of select="address2"/>
								</fo:block>
								<fo:block font-size="8pt">
									<xsl:value-of select="address3"/>
									<xsl:value-of select="address4"/>
								</fo:block>
								<fo:block font-size="8pt">
									<xsl:value-of select="town"/>
								</fo:block>
								<fo:block font-size="8pt">
									<xsl:value-of select="postcode"/>
								</fo:block>
								<fo:block>
									<xsl:text>

									</xsl:text>
								</fo:block>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>

				</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</fo:block>
	</xsl:template>

	<!-- Format the date -->
	<xsl:template name="displayDateDDMMMYYYY">
		<xsl:param name="input"/>

		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>

		<xsl:value-of select="$day"/>
		<xsl:text> </xsl:text>

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
		<xsl:text> </xsl:text>

		<xsl:value-of select="$year"/>
		<xsl:text> </xsl:text>
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


</xsl:stylesheet>
