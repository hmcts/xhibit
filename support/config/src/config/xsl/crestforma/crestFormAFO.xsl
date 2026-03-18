<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="all" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
					<fo:region-body margin-top="0mm" margin-bottom="10mm"/>
					<fo:region-before extent="10mm"/>
					<fo:region-after extent="10mm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="all" format="1">
				<fo:flow flow-name="xsl-region-body">
					<!-- start of hearing record value(s) -->
					<xsl:for-each select="linked-hearing-record-values/hearing-record-values">
						<xsl:if test="position() > 1">
							<fo:block break-before="page" line-height="1pt"> </fo:block>
						</xsl:if>
						<fo:block>
						<fo:table table-layout="fixed">
							<fo:table-column column-width="105mm"/>
							<fo:table-column column-width="85mm"/>
							<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
								<fo:table-row>
									<fo:table-cell>
										<fo:block> 
										<fo:table table-layout="fixed">
											<fo:table-column column-width="103mm"/>
											<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
												<fo:table-row>
													<fo:table-cell>
														<fo:block font-size="18pt" font-weight="bold" space-before="6pt">
															Court Clerk's Hearing Record (A)
														 </fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell>
														<fo:block> 
														<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="24.5mm">
															<fo:table-column column-width="103mm"/>
															<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
																			<xsl:if test="hearing-record-display-value/hr-case-value/case-type != 'A'">
																				Defendant's name:
																			</xsl:if>
																			<xsl:if test="hearing-record-display-value/hr-case-value/case-type = 'A'">
																			 	Appellant's name:
																			 </xsl:if>
																		</fo:block>
																		<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt" hyphenate="true" language="en">
																			<xsl:value-of select="hearing-record-display-value/hr-defendant-value/first-name"/>
																			<xsl:text> </xsl:text>
																			<xsl:value-of select="hearing-record-display-value/hr-defendant-value/middle-name"/>
																			<xsl:text> </xsl:text>
																			<xsl:value-of select="hearing-record-display-value/hr-defendant-value/surname"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-width=".6mm">
										<fo:block>
										<fo:table table-layout="fixed">
											<fo:table-column column-width="83mm"/>
											<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
												<fo:table-row>
													<fo:table-cell>
														<fo:block>
														<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="15mm">
															<fo:table-column column-width="81mm"/>
															<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block margin-left="3pt" font-family="Times" font-size="11pt">
																			<fo:block line-height="20pt">
																			 	Court Clerk:
																			 </fo:block>
																			<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt" hyphenate="true" language="en">
																				<xsl:value-of select="court-clerk-exporter"/>
																			</fo:block>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell>
														<fo:block>
														<fo:table table-layout="fixed" border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="18mm">
															<fo:table-column column-width="81mm"/>
															<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
																			<xsl:text>Case Number: </xsl:text>
																			<!-- <xsl:if test="hearing-record-display-value/hr-case-value/case-type != 'A'">
																			 Case and Defendant No.:
																			 </xsl:if>
																			 <xsl:if test="hearing-record-display-value/hr-case-value/case-type = 'A'">
																			 Case and Appellant No.:
																			</xsl:if> -->
																		</fo:block>
																		<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt">
																			<xsl:value-of select="hearing-record-display-value/hr-case-value/case-type"/>
																			<xsl:value-of select="hearing-record-display-value/hr-case-value/case-number"/>
																			<!-- <xsl:text>-</xsl:text>
 																			<xsl:value-of select="hearing-record-display-value/hr-defendant-value/defendant-iD"/>-->
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:block>
						<xsl:apply-templates select="."/>
						<!--xsl:call-template name="formA"/-->
						<fo:block space-before="10pt"> </fo:block>
					</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="hearing-record-values">
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold" space-after="8pt">
			Hearing Details
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="65mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="55mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Hearing Type:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/mp-hearing-type='M'">Main</xsl:when>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/mp-hearing-type='P'">Preliminary</xsl:when>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Start Date
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:call-template name="displayDateDDMMMYYYY">
								<xsl:with-param name="input">
									<xsl:value-of select="substring(hearing-record-update-value/def-hearing-record-value/hearing-start-date,1,10)"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="15mm"/>
			<fo:table-column column-width="15mm"/>
			<fo:table-column column-width="15mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="55mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Duration:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Hours
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!--xsl:value-of select="../indictmentNo"/-->
							<xsl:if test="hearing-record-update-value/def-hearing-record-value/last-calculated-duration != 'null' and  hearing-record-update-value/def-hearing-record-value/last-calculated-duration != ' ' ">
								<xsl:value-of select="floor(hearing-record-update-value/def-hearing-record-value/last-calculated-duration div 1000 div 60 div 60)"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Minutes
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!--xsl:value-of select="../indictmentNo"/-->
							<xsl:if test="hearing-record-update-value/def-hearing-record-value/last-calculated-duration != 'null' and  hearing-record-update-value/def-hearing-record-value/last-calculated-duration != ' ' ">
								<xsl:value-of select="floor(hearing-record-update-value/def-hearing-record-value/last-calculated-duration div 1000 div 60 mod 60)"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							End Date
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:call-template name="displayDateDDMMMYYYY">
								<xsl:with-param name="input">
									<xsl:value-of select="substring(hearing-record-update-value/def-hearing-record-value/hearing-end-date,1,10)"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="75mm"/>
			<fo:table-column column-width="85mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
				<fo:table-row>
					<fo:table-cell>
						<!--fo:block line-height="10pt">&#160;</fo:block-->
					</fo:table-cell>
					<fo:table-cell>
					</fo:table-cell>
					<fo:table-cell number-rows-spanned="2">
						<xsl:if test="hearing-record-update-value/hr-sHJudge-value/deputy-hCJ='Y'">
							<fo:block font-family="Times" font-size="11pt" text-align="center">Sitting as a Deputy HCJ - Yes</fo:block>
						</xsl:if>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Hearing Judge <fo:inline font-style="italic">(if different from listed Judge)</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!--              <xsl:if test="hearing-record-display-value/hr-hearing-display-value/hr-judge-value/ref-judge-iD = hearing-record-update-value/hr-sHJudge-value/ref-judge-iD">-->
							<xsl:for-each select="hearing-record-display-value/hr-hearing-display-value/hr-judge-value">
								<xsl:choose>
									<xsl:when test="judge-full-list-title1 != 'null' and judge-full-list-title1 != ''">
										<xsl:value-of select="judge-full-list-title1"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="judge-surname != 'null' and judge-surname != ''">
												<xsl:value-of select="judge-surname"/>
											</xsl:when>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
							<!--              </xsl:if>-->
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="65mm"/>
			<fo:table-column column-width="120mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							JP's <fo:inline font-style="italic">(if different from listed magistrates)</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:for-each select="hearing-record-display-value/hr-hearing-display-value/hr-justice-values">
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="justice-name"/>
							</fo:block>
						</xsl:for-each>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<!--<xsl:apply-templates select="hearing-record-display-value/hr-counsel-value" mode="prosecution"/>-->
		<xsl:apply-templates select="." mode="prosecution"/>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" space-after="8pt">
			<fo:inline font-weight="bold">Concurrent Cases</fo:inline>
			- List any other cases that were
			<fo:inline font-weight="bold">heard concurrently</fo:inline>
			for Graduated Fee purposes
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="185mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block>
							<xsl:for-each select="hearing-record-display-value/hr-linked-case-list-value">
								<fo:inline font-family="Courier" font-size="15pt">
									<xsl:value-of select="case-type"/>
									<xsl:value-of select="case-number"/>
								</fo:inline>
								<!--fo:inline white-space-collapse="false"><xsl:text>. . . . . . . . </xsl:text></fo:inline-->
								<fo:inline white-space-collapse="false">
									<xsl:text>                </xsl:text>
								</fo:inline>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="90mm"/>
			<fo:table-column column-width="65mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
							<xsl:if test="hearing-record-display-value/hr-case-value/case-type != 'A'">
								Defendant Details
							</xsl:if>
							<xsl:if test="hearing-record-display-value/hr-case-value/case-type = 'A'">
								Appellant Details
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Bail status at <fo:inline font-weight="bold">end</fo:inline> of hearing
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/end-bail-status='B'">Bail</xsl:when>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/end-bail-status='C'">Custody</xsl:when>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/end-bail-status='J'">In Care</xsl:when>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/end-bail-status='N'">Not Applicable</xsl:when>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-style="italic" space-after="8pt">
			Only complete the remainder of this block at an effective sentence hearing
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="85mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Date of last conviction
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:call-template name="displayDateDDMMMYYYY">
								<xsl:with-param name="input">
									<xsl:value-of select="substring(hearing-record-display-value/hr-defendant-value/last-conviction-date,1,10)"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-style="italic" space-after="8pt">
			The following item <fo:inline font-weight="bold">must</fo:inline> be completed whenever there is a driving conviction
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="70mm"/>
			<fo:table-column column-width="90mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt">
							Under the 'totting up' procedure
						</fo:block>
						<fo:block font-family="Times" font-size="11pt">
							(S35 RTOA 88) was the defendant
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="25pt">
							<xsl:choose>
								<xsl:when test="hearing-record-display-value/hr-defendant-value/final-driving-license-status='1'">1. Not liable/Not disqualified</xsl:when>
								<xsl:when test="hearing-record-display-value/hr-defendant-value/final-driving-license-status='2'">2. Disqualified</xsl:when>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-column column-width="80mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Number of TICs
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="hearing-record-display-value/hr-defendant-value/no-of-tICs"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Collecting magistrates' court
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="hearing-record-display-value/hr-defendant-value/collect-magistrate-court-name"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="27mm"/>
			<fo:table-column column-width="85mm"/>
			<fo:table-column column-width="27mm"/>
			<fo:table-column column-width="45mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Driver Number
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<!--xsl:value-of select="../indictmentNo"/-->
							<xsl:value-of select="hearing-record-display-value/hr-defendant-value/driver-number"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							CRO Number
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="hearing-record-display-value/hr-defendant-value/cro-number"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<xsl:apply-templates select="hearing-record-display-value/hr-counsel-value" mode="defendantRepresentation"/>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="90mm"/>
			<fo:table-column column-width="80mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Bail/Custody Status at the beginning of hearing:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<xsl:variable name="startBail">
								<xsl:value-of select="hearing-record-update-value/def-hearing-record-value/start-bail-status"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$startBail='C'">Custody</xsl:when>
								<xsl:when test="$startBail='B'">Bail</xsl:when>
								<xsl:when test="$startBail='J'">In Care</xsl:when>
								<xsl:when test="$startBail='N'">Not Applicable</xsl:when>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="25mm"/>
			<fo:table-column column-width="45mm"/>
			<fo:table-column column-width="90mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Hearing Type
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="hearing-record-update-value/def-hearing-record-value/ref-def-hearing-type-desc"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<!--<xsl:if test="hearing-record-update-value/def-hearing-record-value/is-hra-application='Y'">-->
								HRA Application
							<!--</xsl:if>-->
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/is-hra-application='Y'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!--<fo:table-row>
					<fo:table-cell>
					<fo:block font-family="Times" font-size="11pt" line-height="20pt">
					Oral evidence when a S37 Hospital Order considered
					</fo:block>
					</fo:table-cell>
					<fo:table-cell>
					<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
					<xsl:choose>
					<xsl:when test="hearing-record-update-value/def-hearing-record-value/oral-evidence='Y'">Yes</xsl:when>
					<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
					</fo:block>
					</fo:table-cell>
				</fo:table-row>-->
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="120mm"/>
			<fo:table-column column-width="25mm"/>
			<!--<fo:table-column column-width="45mm"/>
			<fo:table-column column-width="90mm"/>-->
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Oral evidence when a S37 Hospital Order considered
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/oral-evidence='Y'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="40mm"/>
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Estimated length of trial at PDH
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="hearing-record-update-value/directions-for-case-value/directions-for-case-basic-value/trial-time-estimate"/>
							<xsl:text> </xsl:text>
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/directions-for-case-value/directions-for-case-basic-value/trial-time-unit='2'">Day(s) </xsl:when>
								<xsl:when test="hearing-record-update-value/directions-for-case-value/directions-for-case-basic-value/trial-time-unit='3'">Week(s) </xsl:when>
								<xsl:when test="hearing-record-update-value/directions-for-case-value/directions-for-case-basic-value/trial-time-unit='4'">Month(s) </xsl:when>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Substantive Bail Application
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/subst-bail-application='Y'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before="8pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
			Bail/ Custody Applications
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="100mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							If a <fo:inline font-weight="bold">preliminary hearing</fo:inline> and an application for bail made
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Date of application
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:call-template name="displayDateDDMMMYYYY">
								<xsl:with-param name="input">
									<xsl:value-of select="substring(hearing-record-update-value/def-hearing-record-value/date-bail-application,1,10)"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="120mm"/>
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="40mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Result:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							<xsl:if test="hearing-record-update-value/def-hearing-record-value/result-bail-application='G'">
								Granted
							</xsl:if>
							<xsl:if test="hearing-record-update-value/def-hearing-record-value/result-bail-application='R'">
								Refused
							</xsl:if>
							<!--<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/result-bail-application='G'">Granted</xsl:when>
								<xsl:otherwise>Refused</xsl:otherwise>
							</xsl:choose>-->
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt">
			If a <fo:inline font-weight="bold">main hearing</fo:inline>
			and the status changes, show the new status:
		</fo:block>
		<xsl:if test="hearing-record-update-value/hearing-basic-value/mp-hearing-type='M'">
			<fo:block>
			<fo:table table-layout="fixed" space-after="8pt">
				<fo:table-column column-width="90mm"/>
				<fo:table-column column-width="40mm"/>
				<fo:table-column column-width="50mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
								<xsl:choose>
									<xsl:when test="hearing-record-update-value/def-hearing-record-value/new-bail-status='B'">Bail</xsl:when>
									<xsl:when test="hearing-record-update-value/def-hearing-record-value/new-bail-status='C'">Custody</xsl:when>
									<xsl:when test="hearing-record-update-value/def-hearing-record-value/new-bail-status='J'">In Care</xsl:when>
									<xsl:when test="hearing-record-update-value/def-hearing-record-value/new-bail-status='N'">Not Applicable</xsl:when>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
								Date new status started
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:call-template name="displayDateDDMMMYYYY">
									<xsl:with-param name="input">
										<xsl:value-of select="substring(hearing-record-update-value/def-hearing-record-value/start-date-new-bail-status,1,10)"/>
									</xsl:with-param>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
			Adjournment
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="85mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="15mm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Was the case adjourned other than overnight?
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:choose>
								<xsl:when test="hearing-record-update-value/def-hearing-record-value/is-adjourned='Y'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<xsl:if test="hearing-record-update-value/def-hearing-record-value/is-adjourned='Y'">
						<fo:table-cell>
							<fo:block font-family="Times" font-size="11pt" line-height="20pt">
								To date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:call-template name="displayDateDDMMMYYYY">
									<xsl:with-param name="input">
										<xsl:value-of select="substring(hearing-record-update-value/def-hearing-record-value/adjourned-date,1,10)"/>
									</xsl:with-param>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed" space-after="8pt">
			<fo:table-column column-width="20mm"/>
			<fo:table-column column-width="165mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt">
							Because
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
							<xsl:value-of select="hearing-record-update-value/def-hearing-record-value/ref-adjournment-desc"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
			Hearing Dates
			<fo:inline font-weight="normal">- Enter a breakdown of an individual count's hearing dates if different</fo:inline>
		</fo:block>
		<xsl:for-each select="hearing-record-update-value/def-hearing-record-value/hearing-date-freetext1">
			<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
				<xsl:value-of select="."/>
			</fo:block>
		</xsl:for-each>
		<xsl:for-each select="hearing-record-update-value/def-hearing-record-value/hearing-date-freetext2">
			<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
				<xsl:value-of select="."/>
			</fo:block>
		</xsl:for-each>
		<xsl:for-each select="hearing-record-update-value/def-hearing-record-value/hearing-date-freetext3">
			<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
				<xsl:value-of select="."/>
			</fo:block>
		</xsl:for-each>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
			Court Reporters
			<fo:inline font-weight="normal">- Enter each reporter that worked during the case</fo:inline>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="75mm"/>
			<fo:table-column column-width="55mm"/>
			<fo:table-column column-width="55mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt" font-weight="bold">
							Name
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt" font-weight="bold">
							Start Date
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="Times" font-size="11pt" line-height="20pt" font-weight="bold">
							End Date
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:variable name="startDate">
					<xsl:value-of select="substring(hearing-record-display-value/hr-hearing-display-value/start-date,1,10)"/>
				</xsl:variable>
				<xsl:variable name="endDate">
					<xsl:value-of select="substring(hearing-record-display-value/hr-hearing-display-value/end-date,1,10)"/>
				</xsl:variable>
				<xsl:for-each select="hearing-record-display-value/hr-hearing-display-value/hr-scheduled-hearing-values">
					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="hr-court-reporter/surname"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="hr-court-reporter/initials"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="hr-court-reporter/first-name"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="hr-court-reporter/middle-name"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:if test="hr-court-reporter/surname != ''">
									<xsl:call-template name="displayDateDDMMMYYYY">
										<xsl:with-param name="input">
											<xsl:value-of select="original-time"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:if test="hr-court-reporter/surname != ''">
									<xsl:call-template name="displayDateDDMMMYYYY">
										<xsl:with-param name="input">
											<xsl:value-of select="original-time"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="rule" space-before="12pt" space-after.optimum="12pt" leader-length="100%"/>
		</fo:block>
		<fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="5mm"/>
			<fo:table-column column-width="90mm"/>
			<fo:table-column column-width="90mm"/>
			<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
				<fo:table-row>
					<fo:table-cell>
					</fo:table-cell>
					<fo:table-cell border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="80mm">
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt" line-height="20pt" font-weight="bold" space-before="8pt">
							Other Information
						</fo:block>
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt" space-before="8pt">
								Was the indictment severed? <fo:inline white-space-collapse="false">
								<xsl:text> </xsl:text>
							</fo:inline>
							<fo:inline font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:choose>
									<!--<xsl:when test="hearing-record-display-value/hr-case-value/severed-ind='Y'">Yes</xsl:when>-->
									<xsl:when test="hearing-record-display-value/hr-case-value/crest-severed-ind='Y'">Yes</xsl:when>
									<xsl:otherwise>No</xsl:otherwise>
								</xsl:choose>
							</fo:inline>
						</fo:block>
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt" space-before="8pt">
							Use the remaining space to continue from any previous part of the form (eg Court Reporters) or to record
							information not asked for elsewhere (eg change in the defendant's details).
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-collapse="separate" border-color="#000000" border-style="solid" border-width=".2mm" height="80mm">
						<fo:block margin-left="3pt" font-family="Times" font-size="10pt" font-weight="bold" text-decoration="underline" space-before="8pt">
							TO BE COMPLETED AT THE CONCLUSION OF THE CASE
						</fo:block>
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt" space-before="8pt">
								No of prosecution witnesses: <fo:inline white-space-collapse="false">
								<xsl:text> </xsl:text>
							</fo:inline>
							<fo:inline font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="hearing-record-display-value/hr-case-value/no-pros-witness"/>
							</fo:inline>
						</fo:block>
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt" space-before="8pt">
							No of pages of prosecution evidence: <fo:inline white-space-collapse="false">
							<xsl:text> </xsl:text>
							</fo:inline>
							<fo:inline font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="hearing-record-display-value/hr-case-value/no-page-pros-evidence"/>
							</fo:inline>
						</fo:block>
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt">
							(to include documentary exhibits &amp; NFE - this
							<fo:inline text-decoration="underline">does not</fo:inline>
							include unused material)
						</fo:block>
						<fo:block margin-left="3pt" font-family="Times" font-size="11pt" space-before="8pt">
							Length of video evidence/tape recorded interview (in minutes): <fo:inline white-space-collapse="false">
							<xsl:text> </xsl:text>
							</fo:inline>
							<fo:inline font-family="Courier" text-decoration="underline" font-size="15pt">
								<xsl:value-of select="hearing-record-display-value/hr-case-value/length-tape"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template>
	<xsl:template match="hr-counsel-value" mode="defendantRepresentation">
		<xsl:if test="legal-role='D' or legal-role='A'">
			<fo:block>
				<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
			</fo:block>
			<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
				Representation
			</fo:block>
			<xsl:choose>
				<!-- if the defendant is representing themselves we dont need all the info -->
				<xsl:when test="sol-firm-or-ref-legal-rep='I'">
					<fo:block font-family="Times" font-size="11pt" line-height="20pt">
						Defendant In Person
					</fo:block>
				</xsl:when>
				<xsl:otherwise>
					<fo:block>
					<fo:table table-layout="fixed" space-after="8pt">
						<fo:table-column column-width="40mm"/>
						<fo:table-column column-width="110mm"/>
						<fo:table-column column-width="17mm"/>
						<fo:table-column column-width="10mm"/>
						<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-family="Times" font-size="11pt" line-height="20pt">
										The advocate was a:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-family="Times" font-size="11pt" line-height="20pt">
										<xsl:if test="legal-rep-type='A'">
											Barrister/Solicitor Advocate
										</xsl:if>
										<xsl:if test="legal-rep-type='S'">
											Solicitor
										</xsl:if>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-family="Times" font-size="11pt" line-height="20pt">
										Category
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					</fo:block>
					<fo:block>
					<fo:table table-layout="fixed" space-after="8pt">
						<fo:table-column column-width="150mm"/>
						<fo:table-column column-width="38mm"/>
						<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
							<fo:table-row>
								<fo:table-cell>
									<fo:block>
									<fo:table table-layout="fixed">
										<fo:table-column column-width="37mm"/>
										<fo:table-column column-width="63mm"/>
										<fo:table-column column-width="22mm"/>
										<fo:table-column column-width="25mm"/>
										<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
											<fo:table-row>
												<fo:table-cell>
													<fo:block font-family="Times" font-size="11pt" line-height="20pt">
														Representative's name
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
														<xsl:value-of select="surname"/>,
														<xsl:value-of select="first-name"/>
														<xsl:text> </xsl:text>
														<xsl:value-of select="middle-name"/>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block font-family="Times" font-size="11pt" line-height="20pt">
														ID Number
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
														<xsl:value-of select="bar-number"/>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
									</fo:block>
									<fo:block font-family="Times" font-size="8pt" font-style="italic">
										(include initials)
									</fo:block>
									<fo:block>
									<fo:table table-layout="fixed" space-after="8pt">
										<fo:table-column column-width="35mm"/>
										<fo:table-column column-width="110mm"/>
										<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
											<fo:table-row>
												<fo:table-cell>
													<fo:block font-family="Times" font-size="11pt" line-height="20pt">
														Address (1st Line)
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
														<xsl:value-of select="address-basic-value/address1"/>
														<xsl:if test="address-basic-value/address1 != '' and  address-basic-value/town != ''">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:value-of select="address-basic-value/town"/>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
									</fo:block>
									<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-style="italic" space-after="8pt">
										(Enter below any change of advocate during the proceedings - stand ins etc.)
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
										<xsl:call-template name="getLegalRepCategory">
											<xsl:with-param name="repID">
												<xsl:value-of select="ref-legal-rep-iD"/>
											</xsl:with-param>
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					</fo:block>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<xsl:template name="getLegalRepCategory">
		<xsl:param name="repID"/>
		<xsl:for-each select="/linked-hearing-record-values/hearing-record-values/hearing-record-update-value/hr-sHLeg-rep-values">
			<xsl:variable name="alreadyDone">
				<xsl:text>FALSE</xsl:text>
				<xsl:for-each select="preceding-sibling::*/ref-leg-rep-iD">
					<xsl:if test=".=$repID">TRUE</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			<xsl:if test="$repID=ref-leg-rep-iD and $alreadyDone='FALSE'">
				<xsl:value-of select="ref-defence-category-desc"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!--<xsl:template match="hr-counsel-value" mode="prosecution">-->
	<xsl:template match="hearing-record-display-value" mode="prosecution">
		<xsl:for-each select="hr-counsel-value">
			<xsl:if test="legal-role='P' or legal-role='R'">
				<fo:block>
					<fo:leader leader-pattern="rule" space-before.optimum="12pt" space-after.optimum="12pt" leader-length="100%"/>
				</fo:block>
				<fo:block line-height="20pt" font-family="Times" font-size="11pt" font-weight="bold">
					<xsl:if test="legal-role='P'">
						Prosecutor Details
					</xsl:if>
					<xsl:if test="legal-role!='P'">
						Respondant Details
					</xsl:if>
				</fo:block>
				<fo:block>
				<fo:table table-layout="fixed" space-after="8pt">
					<fo:table-column column-width="32mm"/>
					<fo:table-column column-width="108mm"/>
					<fo:table-column column-width="45mm"/>
					<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-family="Times" font-size="11pt" line-height="20pt">
									<xsl:if test="legal-role='P'">
										Prosecutor's name
									</xsl:if>
									<xsl:if test="legal-role!='P'">
										Respondant's name
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-family="Courier" text-decoration="underline" font-size="15pt" hyphenate="true" language="en">
									<xsl:value-of select="surname"/>,
									<xsl:value-of select="first-name"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="middle-name"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-family="Times" font-size="11pt" line-height="20pt">
									<xsl:if test="legal-rep-type='A'">
										Barrister/Solicitor Advocate
									</xsl:if>
									<xsl:if test="legal-rep-type='S'">
										Solicitor
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				</fo:block>
				<fo:block>
				<fo:table table-layout="fixed">
					<fo:table-column column-width="17mm"/>
					<fo:table-column column-width="168mm"/>
					<fo:table-body font-family="Times" font-weight="normal" font-size="11pt">
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-family="Times" font-size="11pt" line-height="20pt">
									Address
								</fo:block>
								<fo:block font-family="Times" font-size="11pt" font-style="italic">
									(1st Line)
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
									<xsl:value-of select="address-basic-value/address1"/>
									<xsl:if test="address-basic-value/address1 != '' and  address-basic-value/town != ''">
										<xsl:text>, </xsl:text>
									</xsl:if>
									<xsl:value-of select="address-basic-value/town"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				</fo:block>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
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
	</xsl:template>
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>
</xsl:stylesheet>
