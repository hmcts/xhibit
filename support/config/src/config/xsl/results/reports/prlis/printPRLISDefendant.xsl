<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	<xsl:template name="TrialCases">
		<fo:block font-weight="bold" font-size="12pt">
			<xsl:text>TRIAL CASES</xsl:text>
		</fo:block>
		<xsl:choose>
			<xsl:when test="count(./prlis-case-values[case-type='T']) > 0">
				<fo:table table-layout="fixed" hyphenate="true" language="en">
					<xsl:call-template name="SetColumnWidths"/>
					<fo:table-header>
						<fo:table-row background-color="#EEEEEE">
							<fo:table-cell number-columns-spanned="2" border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>CASE/DEFT. NO:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>DEFENDANT NAME/  PTIURN/ SEX/ DATE OF BIRTH/ SOLICITORS/ CHARGES/ PDH /PRELIMINARY HRG DATE</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>B\C STATUS:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>COMMITTING/ TRANSFERRING COURT: (PROSECUTOR)</xsl:text>						
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>DATE OF COMMITTAL/TC /SENT /VB SIGNED /TRANSFER /EXEC /RE-HEARING ORDERED</xsl:text>						
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<xsl:for-each select="./prlis-case-values[case-type='T']">
							<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
								<xsl:call-template name="PrintCaseNumber"/>
								<fo:table-cell padding="1mm" font-size="9pt">
									<fo:block font-size="9pt" font-weight="normal">
										<xsl:value-of select="./defendant-number-text"/>
									</fo:block>
								</fo:table-cell>
								<xsl:call-template name="PrintDefendantDetails"/>
								<xsl:call-template name="PrintBCStatus"/>
								<fo:table-cell padding="1mm" font-size="9pt">
									<xsl:choose>
										<xsl:when test="./magistrates-transferred-court-name != '' and ./magistrates-transferred-court-name">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:text>TI</xsl:text>
											</fo:block>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./magistrates-transferred-court-name"/>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./magistrates-court-name"/>
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:choose>
											<xsl:when test="normalize-space(./prosecutor-name) != ''">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./prosecutor-name"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												**NO PROSECUTOR**
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>	
								<fo:table-cell padding="1mm" font-size="9pt">
									<xsl:choose>
										<xsl:when test="./magistrates-transferred-court-name != '' and ./magistrates-transferred-court-name">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./transferred-date"/>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./sent-for-trial-date"/>
												<xsl:value-of select="./committal-date"/>
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:choose>
										<xsl:when test="./first-def-no = ./current-def-no">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												Class  <xsl:value-of select="./class-code"/>
											</fo:block>
										</xsl:when>
									</xsl:choose>
								</fo:table-cell>	
							</fo:table-row>
							<xsl:call-template name="PrintIncompleteCaseWarning"/>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
			</xsl:when>
			<xsl:when test="count(./prlis-case-values[case-type='T']) = 0">
				<fo:block font-size="10pt" font-family="sans-serif" text-align="center" space-before.optimum="20pt">
					<xsl:text>NIL REPORT</xsl:text>
				</fo:block>
			</xsl:when>
		</xsl:choose>	
	</xsl:template>
	<xsl:template name="CommittalCases">
		<fo:block font-weight="bold" font-size="12pt" break-before="page">
			<xsl:text>COMMITTALS FOR SENTENCE </xsl:text>
		</fo:block>
		<xsl:choose>
			<xsl:when test="count(./prlis-case-values[case-type='S']) > 0">
				<fo:table table-layout="fixed">
					<xsl:call-template name="SetColumnWidths"/>
					<fo:table-header>
						<fo:table-row background-color="#EEEEEE">
							<fo:table-cell number-columns-spanned="2" border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>CASE/DEFT NO.:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>DEFENDANT NAME/ SEX/ DATE OF BIRTH/ SOLICITORS/ CHARGES</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>B\C STATUS:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>COMMITTING/ TRANSFERRING COURT: (PROSECUTOR)</xsl:text>						
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>DATE OF COMMITTAL/ EXEC/ TRANSFER/ BRING BACK</xsl:text>						
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<xsl:for-each select="./prlis-case-values[case-type='S']">
							<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
								<xsl:call-template name="PrintCaseNumber"/>
								<fo:table-cell padding="1mm" font-size="9pt">
									<fo:block font-size="9pt" font-weight="normal">
										<xsl:value-of select="./defendant-number-text"/>
									</fo:block>
								</fo:table-cell>
								<xsl:call-template name="PrintDefendantDetails"/>
								<xsl:call-template name="PrintBCStatus"/>
								<fo:table-cell padding="1mm" font-size="9pt">
									   <xsl:choose>
										<xsl:when test="./magistrates-transferred-court-name != '' and ./magistrates-transferred-court-name">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:text>TI</xsl:text>
											</fo:block>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./magistrates-transferred-court-name"/>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./magistrates-court-name"/>
											</fo:block>
										</xsl:otherwise>
									   </xsl:choose>
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:choose>
											<xsl:when test="normalize-space(./prosecutor-name) != ''">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./prosecutor-name"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												**NO PROSECUTOR**
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>	
								<fo:table-cell padding="1mm" font-size="9pt">
									<xsl:choose>
										<xsl:when test="./magistrates-transferred-court-name != '' and ./magistrates-transferred-court-name">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./transferred-date"/>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./committal-date"/>
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-cell>	
							</fo:table-row>  
							<xsl:call-template name="PrintIncompleteCaseWarning"/>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
			</xsl:when>
			<xsl:when test="count(./prlis-case-values[case-type='C']) = 0">
				<fo:block font-size="10pt" font-family="sans-serif" text-align="center" space-before.optimum="20pt">
					<xsl:text>NIL REPORT</xsl:text>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="AppealsCases">
		<fo:block font-weight="bold" font-size="12pt" break-before="page">
			<xsl:text>APPEALS TO THE CROWN COURT </xsl:text>
		</fo:block>
		<xsl:choose>
			<xsl:when test="count(./prlis-case-values[case-type='A']) > 0">
				<fo:table table-layout="fixed">
					<xsl:call-template name="SetColumnWidths"/>
					<fo:table-header>
						<fo:table-row background-color="#EEEEEE">
							<fo:table-cell number-columns-spanned="2" border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>CASE/DEFT NO.:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>APPELLANT NAME/ SEX/ DATE OF BIRTH/ SOLICITORS:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>B/C STATUS:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>MAGISTRATES/ TRANSFERRING COURT/ APPEAL DESCRIPTION: (RESPONDENT)</xsl:text>						
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
								<fo:block font-weight="bold" font-size="9pt">
									<xsl:text>DATE OF NOTICE OF APPEAL/ TRANSFER/ EXEC</xsl:text>						
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<xsl:for-each select="./prlis-case-values[case-type='A']">
							<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
								<xsl:call-template name="PrintCaseNumber"/>
								<fo:table-cell padding="1mm" font-size="9pt">
									<fo:block font-size="9pt" font-weight="normal">
										<xsl:value-of select="./defendant-number-text"/>
									</fo:block>
								</fo:table-cell>
								<xsl:call-template name="PrintDefendantDetails"/>
								<xsl:call-template name="PrintBCStatus"/>
								<fo:table-cell padding="1mm" font-size="9pt">
									   <xsl:choose>	
										<xsl:when test="./magistrates-transferred-court-name != '' and ./magistrates-transferred-court-name">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:text>TI</xsl:text>
											</fo:block>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./magistrates-transferred-court-name"/>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./magistrates-court-name"/>
											</fo:block>
										</xsl:otherwise>
									   </xsl:choose>
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:choose>
											<xsl:when test="normalize-space(./prosecutor-name) != ''">
												<xsl:call-template name="format-text-for-wrapping">
													<xsl:with-param name="str" select="./prosecutor-name"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												**NO RESPONDENT**
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>	
								<fo:table-cell padding="1mm" font-size="9pt">
									<xsl:choose>
										<xsl:when test="./magistrates-transferred-court-name != '' and ./magistrates-transferred-court-name">
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./transferred-date"/>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
												<xsl:value-of select="./appeal-lodged-date"/>
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-cell>	
							</fo:table-row>   
							<xsl:call-template name="PrintIncompleteCaseWarning"/>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
			</xsl:when>
			<xsl:when test="count(./prlis-case-values[case-type='A']) = 0">
				<fo:block font-size="10pt" font-family="sans-serif" text-align="center" space-before.optimum="20pt">
					<xsl:text>NIL REPORT</xsl:text>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="SetColumnWidths">
		<fo:table-column column-number="1" column-width="60px"/>
		<fo:table-column column-number="2" column-width="30px"/>
		<fo:table-column column-number="3" column-width="150px"/>
		<fo:table-column column-number="4" column-width="50px"/>
		<fo:table-column column-number="5" column-width="150px"/>
		<fo:table-column column-number="6" column-width="100px"/>
		<fo:table-column column-number="7" column-width="10px"/>
		<fo:table-column column-number="8" column-width="10px"/>
	</xsl:template>
	<xsl:template name="PrintCaseNumber">
		<fo:table-cell padding="1mm" font-size="9pt">
			<fo:block font-size="9pt" font-weight="normal">
				<xsl:choose>
					<xsl:when test="./first-def-no = ./current-def-no">
						<xsl:value-of select="./case-number"/>
					</xsl:when>
				</xsl:choose>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template name="PrintDefendantDetails">
		<xsl:variable name="formattedSolName">
			<xsl:call-template name="format-text-for-wrapping">
				<xsl:with-param name="str" select="./solicitor-name"/>
			</xsl:call-template>
		</xsl:variable>
		<fo:table-cell padding="1mm" font-size="9pt">
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">							
				<xsl:choose>
					<xsl:when test="normalize-space(./defandant-surname) != ''">
						<xsl:call-template name="format-text-for-wrapping">
							<xsl:with-param name="str" select="./defandant-surname"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						**NO DEFT/APPLT**
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal" >
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="str" select="./defendant-first-name"/>
				</xsl:call-template>
			</fo:block>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="str" select="./defendant-middle-name"/>
				</xsl:call-template>
			</fo:block>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
				<xsl:value-of select="./ptiurn"/>
			</fo:block>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">	
				<xsl:value-of select="./defendant-gender"/>      Date of Birth: <xsl:value-of select="./defendant-date-of-birth"/>
			</fo:block>
			<xsl:if test="case-type='T'">
				<fo:block space-before="1pt" space-after="1pt" font-weight="normal">	
					Preliminary Hearing: <xsl:value-of select="./date-of-hearing"/>
				</fo:block>
			</xsl:if>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">	
				<xsl:text>Sols:  </xsl:text><xsl:value-of select="$formattedSolName"/>
			</fo:block>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">	
				<xsl:text>Tel: </xsl:text><xsl:value-of select="./solicitor-phone-number"/>
			</fo:block>
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal" linefeed-treatment="preserve" white-space-treatment="preserve">
				<xsl:text>Charges: </xsl:text><xsl:value-of select="./charges"/>
			</fo:block>	
		</fo:table-cell>
	</xsl:template>
	<xsl:template name="PrintBCStatus">
		<fo:table-cell padding="1mm" font-size="9pt">
			<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
				<xsl:choose>
					<xsl:when test="normalize-space(./defandant-surname) != ''">
						<xsl:value-of select="./bc-status"/>
					</xsl:when>
				</xsl:choose>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	
	<xsl:template name="PrintIncompleteCaseWarning">
		<xsl:choose>
			<xsl:when test="not(normalize-space(./defandant-surname) != '') and not(normalize-space(./prosecutor-name) != '')">
				<fo:table-row>
					<fo:table-cell number-columns-spanned="7" padding="1mm" font-size="9pt">
						<fo:block font-weight="bold" text-align="center">
							****Warning: Case Party details are incomplete in the above case****
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
