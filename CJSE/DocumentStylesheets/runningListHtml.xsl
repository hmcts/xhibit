<?xml version="1.0" encoding="UTF-8"?>
<!--
	+       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" version="1.1">
	<xsl:output method="html" indent="yes"/>
	<!-- set up global variable to hold the report date -->
	   	<!-- Version and name Informaiton -->
	<xsl:variable name="majorVersion" select="'2'" />
	<xsl:variable name="minorVersion" select="'0'" />
	<xsl:variable name="stylesheet" select="'runninglist-v2.xsl'" />
	<xsl:variable name="last-modified-date" select="'2005-09-13'" />
	<!-- End Version and name Informaiton -->
	<xsl:variable name="reportdate">
		<xsl:choose>
			<xsl:when test="//cs:ListHeader/cs:EndDate">
				<xsl:value-of select="//cs:ListHeader/cs:EndDate"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="//cs:ListHeader/cs:StartDate"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- top level match -->
	<xsl:template match="/">
		<html>
			<meta  content="text/html;  charset=UTF-8"  http-equiv="Content-Type"/>
			<!-- Add Comments to HTML to help future maintenance -->
			<xsl:comment>
				<xsl:text>Produced by : </xsl:text>
				<xsl:value-of select="$stylesheet" />
				<xsl:text> Version : </xsl:text>
				<xsl:value-of select="$majorVersion"/><xsl:text>.</xsl:text><xsl:value-of select="$minorVersion"/>
				<xsl:text>  last modified : </xsl:text>
				<xsl:value-of select="$last-modified-date"/>
			</xsl:comment>
			<xsl:comment>&#x00A9; Crown copyright 2003. All rights reserved.</xsl:comment>
			<xsl:comment>
				<xsl:text>Document Unique Id : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:UniqueID" />
				<xsl:text> Document version : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:Version" />
				<xsl:text> Document timestamp : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:TimeStamp" />
				<xsl:text> Document stylesheet URL : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:XSLstylesheetURL" />
			</xsl:comment>
			<!-- End of Add Comments to HTML to help future maintenance -->	
			<body>
				<!-- Display Crown Court Details -->
				<xsl:apply-templates select="cs:RunningList/cs:CrownCourt"/>
				<!-- Display Trial Case Info -->
				<xsl:apply-templates select="cs:RunningList/cs:TrialCases"/>
				<!-- Display Commital Case Info -->
				<xsl:apply-templates select="cs:RunningList/cs:CommitalCases"/>
				<!-- Display Appeal Case Info -->
				<xsl:apply-templates select="cs:RunningList/cs:AppealCases"/>
				<center>
					<b>
						<xsl:text>End of Report</xsl:text>
					</b>
				</center>
				<!-- Display footer info -->
				<xsl:call-template name="listfooter"/>
				<!-- Finish with Copyright notice -->
		    	<br/>
				<xsl:text>&#x00A9; Crown copyright </xsl:text>	
				<xsl:value-of select="substring(//cs:DocumentID/cs:TimeStamp, 1, 4)"/>
				<xsl:text>. All rights reserved. Issued by HM Courts &amp; Tribunals Service.</xsl:text>	
				<!-- End Finish with Copyright notice -->
			</body>
		</html>
	</xsl:template>
	<!-- 		
			**************************
			TEMPLATE MATCHES 
			*************************
	-->
	<!-- Display Appeal Case Information ******************************************************************************************************************************************** -->
	<xsl:template match="cs:AppealCases">
		<br/>
		<font size="1">
			
				<xsl:text>APPEALS TO THE CROWN COURT</xsl:text>
			
		</font>
		<br/>
		<table class="detail" width="92%">
			<!-- Display header info -->
			<tr>
				
					<td width="10%" valign="top">
						<font size="1">
						
								<xsl:text>CASE/DEFT.No:</xsl:text>
							
						</font>
					</td>
					<td width="30%" valign="top">
						<font size="1">
							
								<xsl:text>APPELLANT NAME/ PTIURN/ SEX/ DATE OF BIRTH/ </xsl:text>
								<br/>
								<xsl:text>SOLICITORS:</xsl:text>
							
						</font>
					</td>
					<td width="10%" valign="top">
						<font size="1">
							
								<xsl:text>B/C STATUS:</xsl:text>
							
						</font>
					</td>
					<td width="30%" valign="top">
						<font size="1">
						
								<xsl:text>MAGISTRATES / TRANSFERRING COURT/</xsl:text>
								<br/>
								<xsl:text>APPEAL DESCRIPTION: (RESPONDENT)</xsl:text>
							
						</font>
					</td>
					<td width="15%" valign="top">
						<font size="1">
							
								<xsl:text>DATE OF NOTICE OF APPEAL / TRANSFER / EXEC :</xsl:text>
							
						</font>
					</td>
				
			</tr>
			<tr>
				<td colspan="5">
					<hr/>
				</td>
			</tr>
			<!-- Display Case and defendant info -->
			<xsl:for-each select="./cs:Case">
				<xsl:variable name="case" select="."/>
				<xsl:variable name="caseNum" select="./cs:CaseNumber"/>
				<xsl:for-each select="./cs:Defendants/cs:Defendant">
					<tr>
						<td valign="top">
							<!-- Just display the case number if it is the first defendant -->
							<font size="1">
								<xsl:value-of select="$caseNum"/>
							</font>
							<!--
							<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
								<font size="1">
									<xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
								</font>
							</xsl:if>
							<xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
								<font size="1">
									<xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
								</font>
							</xsl:if>
							-->
						</td>
						<td valign="top">
							<font size="1">
								<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
									<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
									</xsl:call-template>
									<br/>
									<!-- Display defendants Forename if exists -->
									<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
										<br/>
									</xsl:if>
									<!-- Check to see if there is a defendants Middle Name and display -->
									<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
										<br/>
									</xsl:if>
								</xsl:if>
								<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
									<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
									<br/>
								</xsl:if>
								<xsl:if test="./cs:URN">
									<xsl:value-of select="./cs:URN"/>                    
									<br/>
                                </xsl:if>
                                                    <!-- check to see if sex or DOB -->
								<xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown' or cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                        <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                                            <tr>
                                                                <td width="25%" valign="top">
                                                                    <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                                                        <font size="1">
                    									<xsl:call-template name="TitleCase">
                    										<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                    									</xsl:call-template>
                                                                        </font>
                                                                    </xsl:if>
                                                                </td>
                                                                <td width="75%" valign="top">
                								<xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                                        <font size="1">
                    									<xsl:text>Date of Birth: </xsl:text>
                    									<xsl:call-template name="FormatDate">
                    										<xsl:with-param name="input">
                    											<xsl:value-of select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                    										</xsl:with-param>
                    									</xsl:call-template>
                                                                        </font>
                								</xsl:if>
                                                                </td>
                                                            </tr>
                                                        </table>
								</xsl:if>
								<!-- Display solicitor details -->
								<xsl:for-each select="cs:Counsel/cs:Solicitor">
									<xsl:call-template name="solicitor">
										<xsl:with-param name="party" select="cs:Party"/>
									</xsl:call-template>
								</xsl:for-each>
							</font>
						</td>
						<td valign="top">
							<!-- Display Custody Status -->
							<font size="1">
                                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
        								<xsl:if test="not(cs:CustodyStatus)">N/A</xsl:if>
        								<xsl:if test="(cs:CustodyStatus)">
        									<xsl:choose>
        										<xsl:when test="cs:CustodyStatus='Not applicable'">N/A</xsl:when>
        										<xsl:when test="not (contains(cs:CustodyStatus,'Not applicable'))">
                        									<xsl:call-template name="TitleCase">
                    										<xsl:with-param name="text" select="cs:CustodyStatus"/>
                    									</xsl:call-template>
        										</xsl:when>
        										<xsl:otherwise>
        											<xsl:text>N/A</xsl:text>
        										</xsl:otherwise>
        									</xsl:choose>
        								</xsl:if>
                                                    </xsl:if>
							</font>
						</td>
						<td valign="top">
							<font size="1">
								<!--Display info depending on method of instigation -->
								<xsl:if test="$case/cs:MethodOfInstigation">
									<xsl:call-template name="DispMethInst">
										<xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
										<xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
										<xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
										<xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
									</xsl:call-template>
									<br/>
								</xsl:if>
								<!--what if no method of instigation? - show committing court stuff or appeal case description-->
								<xsl:if test="not($case/cs:MethodOfInstigation)">
									<!-- Display originating court details -->
									<xsl:if test="$case/cs:CaseArrivedFrom">
										<xsl:call-template name="do-replace">
											<xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
										</xsl:call-template>
										<xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
											<xsl:text> CC</xsl:text>
										</xsl:if>										
										<br/>
									</xsl:if>
									<xsl:if test="$case/cs:AppealCaseDescription">
										<br/>
										<xsl:value-of select="$case/cs:AppealCaseDescription"/>
										<br/>
									</xsl:if>
								</xsl:if>
							</font>
							<!-- Display prosecution info -->
							<xsl:choose>
								<xsl:when test="$case/cs:Prosecution">
									<font size="1">
										<xsl:call-template name="toUpper">
											<xsl:with-param name="content" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
										</xsl:call-template>
									</font>
								</xsl:when>
								<xsl:otherwise>
									<font size="1">
										<xsl:text>** NO RESPONDENT **</xsl:text>
									</font>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td valign="top">
							<xsl:if test="$case/cs:DateOfInstigation">
								<font size="1">
									<xsl:call-template name="displayDate">
										<xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
									</xsl:call-template>
								</font>
							</xsl:if>
						</td>
					</tr>
					<tr/>
					<!--No deft/appllt? then display fixed text-->
					<tr>
						<td colspan="5" align="center">
							<font size="1">
								<xsl:call-template name="noDeftText"/>
							</font>
						</td>
					</tr>
				</xsl:for-each>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- Display Committal Case Info ************************************************************************************************************************************************** -->
	<xsl:template match="cs:CommitalCases">
		<br/>
		<font size="1">
		
				<xsl:text>COMMITTALS FOR SENTENCE</xsl:text>
			
		</font>
		<br/>
		<table class="detail" width="92%">
			<!-- Dispay Header information -->
			<tr>
				
					<td width="10%" valign="top">
						<font size="1">
						
								<xsl:text>CASE/DEFT.No:</xsl:text>
							
						</font>
					</td>
					<td width="30%" valign="top">
						<font size="1">
							
								<xsl:text>DEFENDANT NAME/ PTIURN/ SEX/ DATE OF BIRTH/ </xsl:text>
								<br/>
								<xsl:text>SOLICITORS/ CHARGES:</xsl:text>
							
						</font>
					</td>
					<td width="10%" valign="top">
						<font size="1">
							
								<xsl:text>B/C STATUS:</xsl:text>
							
						</font>
					</td>
					<td width="30%" valign="top">
						<font size="1">
							
								<xsl:text>COMMITTING / TRANSFERRING COURT: (PROSECUTOR)</xsl:text>
							
						</font>
					</td>
					<td width="15%" valign="top">
						<font size="1">
							
								<xsl:text>DATE OF COMMITTAL / EXEC / TRANSFER / BRING BACK:</xsl:text>
							
						</font>
					</td>
				
			</tr>
			<tr>
				<td colspan="5">
					<hr/>
				</td>
			</tr>
			<!-- Display case and defendant info -->
			<xsl:for-each select="./cs:Case">
				<xsl:variable name="case" select="."/>
				<xsl:variable name="caseNum" select="./cs:CaseNumber"/>
				<xsl:for-each select="./cs:Defendants/cs:Defendant">
					<tr>
						<td valign="top" align="right">
							<!--Defendant numbering-->
							<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
								<xsl:if test="position()=1">
									<font size="1">
										<xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
									</font>
								</xsl:if>
								<xsl:if test="position()!=1">
                                                        <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                                            <tr valign="top">
                                                                <td width="100%" align="right" valign="top">
                                                                    <font size="1">
                                                                        -<xsl:number value="position()" format="001"/>
                                                                    </font>
                                                                </td>
                                                            </tr>
                                                        </table>
								</xsl:if>
							</xsl:if>
							<xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
								<font size="1">
									<xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
								</font>
							</xsl:if>
						</td>
						<td valign="top">
							<font size="1">
								<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
									<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
									</xsl:call-template>
									<br/>
									<!-- Display defendants Forename if exists -->
									<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
										<br/>
									</xsl:if>
									<!-- Check to see if there is a defendants Middle Name and display -->
									<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
										<br/>
									</xsl:if>
								</xsl:if>
								<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
									<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
									<br/>
								</xsl:if>
								<xsl:if test="./cs:URN">
									<xsl:value-of select="./cs:URN"/>                    
									<br/>
                                </xsl:if>								
                                                    <!-- check to see if sex or DOB -->
								<xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown' or cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                        <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                                            <tr>
                                                                <td width="25%" valign="top">
                                                                    <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                                                        <font size="1">
                    									<xsl:call-template name="TitleCase">
                    										<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                    									</xsl:call-template>
                                                                        </font>
                                                                    </xsl:if>
                                                                </td>
                                                                <td width="75%" valign="top">
                								<xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                                        <font size="1">
                    									<xsl:text>Date of Birth: </xsl:text>
                    									<xsl:call-template name="FormatDate">
                    										<xsl:with-param name="input">
                    											<xsl:value-of select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                    										</xsl:with-param>
                    									</xsl:call-template>
                                                                        </font>
                								</xsl:if>
                                                                </td>
                                                            </tr>
                                                        </table>
								</xsl:if>
								<!-- Display solicitor details -->
								<xsl:for-each select="cs:Counsel/cs:Solicitor">
									<xsl:call-template name="solicitor">
										<xsl:with-param name="party" select="cs:Party"/>
									</xsl:call-template>
								</xsl:for-each>
								<!-- Display any charge information -->
								<!--if defendant is the first defendant then display all the charge info-->
								<xsl:if test="position()=1">
									<xsl:if test="cs:AdditionalNotes">
										<br/>
										<xsl:text>Charges:  </xsl:text>
									</xsl:if>
									<xsl:value-of select="cs:AdditionalNotes"/>
								</xsl:if>
							</font>
						</td>
						<td valign="top">
							<!-- Display custody if exists -->
							<font size="1">
                                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
        								<xsl:if test="not(cs:CustodyStatus)">N/A</xsl:if>
        								<xsl:if test="(cs:CustodyStatus)">
        									<xsl:choose>
        										<xsl:when test="cs:CustodyStatus='Not applicable'">N/A</xsl:when>
        										<xsl:when test="not (contains(cs:CustodyStatus,'Not applicable'))">
                        									<xsl:call-template name="TitleCase">
                    										<xsl:with-param name="text" select="cs:CustodyStatus"/>
                    									</xsl:call-template>
        										</xsl:when>
        										<xsl:otherwise>
        											<xsl:text>N/A</xsl:text>
        										</xsl:otherwise>
        									</xsl:choose>
        								</xsl:if>
                                                    </xsl:if>        								
							</font>
						</td>
						<td valign="top">
							<font size="1">
								<!--Display info depending on method of instigation -->
								<xsl:if test="$case/cs:MethodOfInstigation">
									<xsl:call-template name="DispMethInst">
										<xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
										<xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
										<xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
										<xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
									</xsl:call-template>
									<br/>
								</xsl:if>
								<!--what if no method of instigation? - show committing court stuff or appeal case description-->
								<xsl:if test="not($case/cs:MethodOfInstigation)">
									<!-- Display originating court details -->
									<xsl:if test="$case/cs:CaseArrivedFrom">
										<xsl:call-template name="do-replace">
											<xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
										</xsl:call-template>
										<xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
											<xsl:text> CC</xsl:text>
										</xsl:if>										
										<br/>
									</xsl:if>
									<xsl:if test="$case/cs:AppealCaseDescription">
										<br/>
										<xsl:value-of select="$case/cs:AppealCaseDescription"/>
										<br/>
									</xsl:if>
								</xsl:if>
								<!-- Display prosecution details -->
								<xsl:if test="$case/cs:Prosecution">
									<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
									</xsl:call-template>
									<br/>
									<xsl:if test="$case/cs:Prosecution/cs:ProsecutingReference">
										<xsl:if test="$case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service'">
											<xsl:text>PTI Unique Ref:</xsl:text>
											<xsl:value-of select="$case/cs:Prosecution/cs:ProsecutingReference"/>
										</xsl:if>
										<xsl:if test="not($case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service')">
											<xsl:text>Pros Ref:</xsl:text>
											<xsl:value-of select="$case/cs:Prosecution/cs:ProsecutingReference"/>
										</xsl:if>
										<br/>
									</xsl:if>
								</xsl:if>
							</font>
						</td>
						<td valign="top">
							<xsl:if test="$case/cs:DateOfInstigation">
								<font size="1">
									<xsl:call-template name="displayDate">
										<xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
									</xsl:call-template>
								</font>
							</xsl:if>
						</td>
					</tr>
					<tr/>
					<!--No deft/appllt? then display fixed text-->
					<tr>
						<td colspan="5" align="center">
							<font size="1">
								<xsl:call-template name="noDeftText"/>
							</font>
						</td>
					</tr>
				</xsl:for-each>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- Display Crown Court Details -->
	<xsl:template match="cs:CrownCourt">
		<xsl:variable name="reporttype" select="'Running List'"/>
		<font size="5" style="font-family: Helvetica;">The <xsl:value-of select="cs:CourtHouseType"/>
		</font>
		<br/>
		<xsl:text> at </xsl:text>
		<xsl:call-template name="TitleCase">
			<xsl:with-param name="text" select="cs:CourtHouseName"/>
		</xsl:call-template>
		<br/>
		<font size="3" style="font-family:Ariel">
			<xsl:text>  </xsl:text>
			<xsl:value-of select="$reporttype"/>
			<xsl:text> for period ending </xsl:text>
			<xsl:call-template name="displayDate">
				<xsl:with-param name="input">
					<xsl:value-of select="$reportdate"/>
				</xsl:with-param>
			</xsl:call-template>
		</font>
		<br/>
		<br/>
	</xsl:template>
	<!-- Display Trial Case Information                                       								***************************************************************************************************************************************-->
	<xsl:template match="cs:TrialCases">
		<font size="1">
			
				<xsl:text>TRIAL CASES</xsl:text>
			
		</font>
		<br/>
		<table class="detail" width="92%">
			<!-- Display header information -->
			<tr>
			
					<td width="10%" valign="top">
						<font size="1">
						
								<xsl:text>CASE/DEFT.No:</xsl:text>
							
						</font>
					</td>
					<td width="30%" valign="top">
						<font size="1">
							
								<xsl:text>DEFENDANT NAME/ PTIURN/ SEX/ DATE OF BIRTH/ </xsl:text>
								<br/>
								<xsl:text>SOLICITORS/ CHARGES/</xsl:text>
								<br/>
								<xsl:text>PDH/ PRELIMINARY HRG DATE</xsl:text>
							
						</font>
					</td>
					<td width="10%" valign="top">
						<font size="1">
							
								<xsl:text>B/C STATUS:</xsl:text>
							
						</font>
					</td>
					<td width="30%" valign="top">
						<font size="1">
							
								<xsl:text>COMMITTING / TRANSFERRING COURT: (PROSECUTOR)</xsl:text>
						
						</font>
					</td>
					<td width="15%" valign="top">
						<font size="1">
							
								<xsl:text>DATE OF COMMITTAL / TC / SENT/ VB SIGNED / TRANSFER / EXEC/ RE-HEARING ORDERED</xsl:text>
							
						</font>
					</td>
				
			</tr>
			<tr>
				<td colspan="5">
					<hr/>
				</td>
			</tr>
			<!-- Display Case and Defendant Information -->
			<xsl:for-each select="./cs:Case">
				<xsl:variable name="case" select="."/>
				<xsl:variable name="caseNum" select="./cs:CaseNumber"/>
				<xsl:for-each select="./cs:Defendants/cs:Defendant">
					<tr>
						<td valign="top" align="right">
							<!--Defendant numbering-->
							<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
								<xsl:if test="position()=1">
									<font size="1">
										<xsl:value-of select="$caseNum"/>-<xsl:number value="position()" format="001"/>
									</font>
								</xsl:if>
								<xsl:if test="position()!=1">
                                                        <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                                            <tr valign="top">
                                                                <td width="100%" align="right" valign="top">
                                                                    <font size="1">
                                                                        -<xsl:number value="position()" format="001"/>
                                                                    </font>
                                                                </td>
                                                            </tr>
                                                        </table>
								</xsl:if>
							</xsl:if>
							<xsl:if test="contains(./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname,'**No Deft/Applt**')">
								<font size="1">
									<xsl:value-of select="$caseNum"/>-<xsl:text>***</xsl:text>
								</font>
							</xsl:if>
						</td>
						<td valign="top">
							<font size="1">
								<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
									<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
									</xsl:call-template>
									<br/>
									<!-- Display defendants Forename if exists -->
									<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[1]"/>
										<br/>
									</xsl:if>
									<!-- Check to see if there is a defendants Middle Name and display -->
									<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]">
										<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameForename[2]"/>
										<br/>
									</xsl:if>
								</xsl:if>
								<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
									<xsl:value-of select="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname"/>
									<br/>
								</xsl:if>
								<xsl:if test="./cs:URN">
									<xsl:value-of select="./cs:URN"/>                    
									<br/>
                                </xsl:if>								
								<!-- check to see if sex or DOB -->
								<xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown' or cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                        <table width="100%" cellpadding="0mm" cellspacing="0mm">
                                                            <tr>
                                                                <td width="25%" valign="top">
                                                                    <xsl:if test="./cs:PersonalDetails/cs:Sex != 'unknown'">
                                                                        <font size="1">
                    									<xsl:call-template name="TitleCase">
                    										<xsl:with-param name="text" select="./cs:PersonalDetails/cs:Sex"/>
                    									</xsl:call-template>
                                                                        </font>
                                                                    </xsl:if>
                                                                </td>
                                                                <td width="75%" valign="top">
                								<xsl:if test="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate">
                                                                        <font size="1">
                    									<xsl:text>Date of Birth: </xsl:text>
                    									<xsl:call-template name="FormatDate">
                    										<xsl:with-param name="input">
                    											<xsl:value-of select="cs:PersonalDetails/cs:DateOfBirth/apd:BirthDate"/>
                    										</xsl:with-param>
                    									</xsl:call-template>
                                                                        </font>
                								</xsl:if>
                                                                </td>
                                                            </tr>
                                                        </table>
								</xsl:if>
								<!-- Display hearing info if exists -->
								<xsl:if test="$case/cs:Hearing/cs:HearingDescription">
									<xsl:value-of select="$case/cs:Hearing/cs:HearingDescription"/>
									<xsl:text>:  </xsl:text>
									<xsl:call-template name="FormatDate">
										<xsl:with-param name="input">
											<xsl:value-of select="$case/cs:Hearing/cs:HearingDate"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<!-- Display solicitor details -->
								<xsl:for-each select="cs:Counsel/cs:Solicitor">
									<br/>
									<xsl:call-template name="solicitor">
										<xsl:with-param name="party" select="cs:Party"/>
									</xsl:call-template>
								</xsl:for-each>
								<!-- Display any charge information -->
								<!--if defendant is the first defendant then display all the charge info-->
								<xsl:if test="position()=1">
									<xsl:if test="cs:AdditionalNotes">
										<br/>
										<xsl:text>Charges:  </xsl:text>
									</xsl:if>
									<xsl:value-of select="cs:AdditionalNotes"/>
								</xsl:if>
							</font>
						</td>
						<td valign="top">
							<!-- Display custody information if exists -->
							<font size="1">
                                                    <xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname!='**No Deft/Applt**'">
        								<xsl:if test="not(cs:CustodyStatus)">N/A</xsl:if>
        								<xsl:if test="(cs:CustodyStatus)">
        									<xsl:choose>
        										<xsl:when test="cs:CustodyStatus='Not applicable'">N/A</xsl:when>
        										<xsl:when test="not (contains(cs:CustodyStatus,'Not applicable'))">
                        									<xsl:call-template name="TitleCase">
                    										<xsl:with-param name="text" select="cs:CustodyStatus"/>
                    									</xsl:call-template>
        										</xsl:when>
        										<xsl:otherwise>
        											<xsl:text>N/A</xsl:text>
        										</xsl:otherwise>
        									</xsl:choose>
        								</xsl:if>
                                                    </xsl:if>
							</font>
						</td>
						<td valign="top">
							<font size="1">
								<!--Display info depending on method of instigation -->
								<xsl:if test="$case/cs:MethodOfInstigation">
									<xsl:call-template name="DispMethInst">
										<xsl:with-param name="methInst" select="$case/cs:MethodOfInstigation"/>
										<xsl:with-param name="court" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
										<xsl:with-param name="courtType" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType"/>
										<xsl:with-param name="caseType" select="substring($case/cs:CaseNumber,1,1)"/>
									</xsl:call-template>
									<br/>
								</xsl:if>
								<!--what if no method of instigation? - show committing court stuff or appeal case description-->
								<xsl:if test="not($case/cs:MethodOfInstigation)">
									<!-- Display originating court details -->
									<xsl:if test="$case/cs:CaseArrivedFrom">
    										<xsl:call-template name="do-replace">
    											<xsl:with-param name="text" select="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseName"/>
    										</xsl:call-template>
										<xsl:if test="$case/cs:CaseArrivedFrom/cs:OriginatingCourt/cs:CourtHouseType = 'Crown Court'">
											<xsl:text> CC</xsl:text>
										</xsl:if>										
										<br/>
									</xsl:if>
									<xsl:if test="$case/cs:AppealCaseDescription">
										<xsl:value-of select="$case/cs:AppealCaseDescription"/>
										<br/>
									</xsl:if>
								</xsl:if>
								<!-- Display prosecuting organisation details -->
								<xsl:if test="$case/cs:Prosecution">
									<xsl:call-template name="toUpper">
										<xsl:with-param name="content" select="$case/cs:Prosecution/cs:ProsecutingOrganisation/cs:OrganisationName"/>
									</xsl:call-template>
									<br/>
									<xsl:if test="$case/cs:Prosecution/cs:ProsecutingReference">
										<xsl:if test="$case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service'">
											<xsl:text>PTI Unique Ref:</xsl:text>
											<xsl:value-of select="$case/cs:Prosecution/cs:ProsecutingReference"/>
										</xsl:if>
										<xsl:if test="not($case/cs:Prosecution/@ProsecutingAuthority='Crown Prosecution Service')">
											<xsl:text>Pros Ref:</xsl:text>
											<xsl:value-of select="$case/cs:Prosecution/cs:ProsecutingReference"/>
										</xsl:if>
										<br/>
									</xsl:if>
								</xsl:if>
							</font>
						</td>
						<td valign="top">
							<font size="1">
								<xsl:if test="$case/cs:DateOfInstigation">
									<xsl:call-template name="displayDate">
										<xsl:with-param name="input" select="$case/cs:DateOfInstigation"/>
									</xsl:call-template>
								</xsl:if>
								<br/>
								<!--Display class of case for first defendant on each case only-->
								<xsl:if test="position()=1">
									<xsl:if test="$case/cs:CaseClassNumber">
										<xsl:text>Class </xsl:text>
										<xsl:value-of select="$case/cs:CaseClassNumber"/>
									</xsl:if>
								</xsl:if>
							</font>
						</td>
					</tr>
					<tr/>
					<!--No deft/appllt? then display fixed text-->
					<tr>
						<td colspan="5" align="center">
							<font size="1">
								<xsl:call-template name="noDeftText"/>
							</font>
						</td>
					</tr>
				</xsl:for-each>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- 		
			************************
			TEMPLATE NAMES
			***********************
	-->
	<!-- Display the date in a specified format -->
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
	<!-- Display the date in a specific format -->
	<xsl:template name="FormatDate">
		<xsl:param name="input"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:value-of select="$day"/>
		<xsl:text>-</xsl:text>
		<xsl:choose>
			<xsl:when test="$month='01'">JAN</xsl:when>
			<xsl:when test="$month='02'">FEB</xsl:when>
			<xsl:when test="$month='03'">MAR</xsl:when>
			<xsl:when test="$month='04'">APR</xsl:when>
			<xsl:when test="$month='05'">MAY</xsl:when>
			<xsl:when test="$month='06'">JUN</xsl:when>
			<xsl:when test="$month='07'">JUL</xsl:when>
			<xsl:when test="$month='08'">AUG</xsl:when>
			<xsl:when test="$month='09'">SEP</xsl:when>
			<xsl:when test="$month='10'">OCT</xsl:when>
			<xsl:when test="$month='11'">NOV</xsl:when>
			<xsl:when test="$month='12'">DEC</xsl:when>
		</xsl:choose>
		<xsl:text>-</xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>
	<!-- Template to format the name to a specific format -->
	<xsl:template name="FormalName">
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
	<!-- display the footer information -->
	<xsl:template name="listfooter">
		<!-- call template to display details -->
		<xsl:call-template name="listFooterDisplay">
			<xsl:with-param name="court" select="/cs:RunningList/cs:CrownCourt"/>
		</xsl:call-template>
	</xsl:template>
	<!-- template to construct footer information -->
	<xsl:template name="listFooterDisplay">
		<!-- creates the footer in the output -->
		<xsl:param name="court"/>
		<hr/>
		<table class="detail" width="92%">
			<tr>
				<td align="left">
					<font size="1">
						<!-- Display court address details -->
						<xsl:if test="$court/cs:CourtHouseAddress">
							<xsl:for-each select="$court/cs:CourtHouseAddress/apd:Line[position() &lt; 5 and (not (position()=2 and .='-'))and not (. = ' ')]">
								<xsl:call-template name="TitleCase">
									<xsl:with-param name="text" select="."/>
								</xsl:call-template>
								<xsl:if test="not (position() = last())">
									<xsl:if test="string-length() &gt; 0">
										<xsl:text>, </xsl:text>
									</xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$court/cs:CourtHouseAddress/apd:PostCode"/>
							<xsl:text>. </xsl:text>
						</xsl:if>
						<xsl:if test="$court/cs:CourtHouseDX">
							<xsl:value-of select="$court/cs:CourtHouseDX"/>
						</xsl:if>
						<xsl:if test="$court/cs:CourtHouseTelephone">
							<xsl:text> Tel: </xsl:text>
							<xsl:value-of select="$court/cs:CourtHouseTelephone"/>
						</xsl:if>
					</font>
				</td>
			</tr>
			<tr>
				<td align="right">
					<font size="1">
						<!-- display print reference -->
						Ref: <xsl:value-of select="cs:RunningList /cs:ListHeader/cs:CRESTprintRef"/>
					</font>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- Display the published Details -->
	<xsl:template name="publishDate">
		<table width="100%">
			<tr>
				<td align="left">
					<xsl:text>Published: </xsl:text>
					<xsl:variable name="pubTime" select="//cs:ListHeader/cs:PublishedTime"/>
					<xsl:call-template name="displayDate">
						<xsl:with-param name="input">
							<xsl:value-of select="substring($pubTime,1,10)"/>
						</xsl:with-param>
					</xsl:call-template>
				at <xsl:value-of select="substring($pubTime,12,5)"/>
				</td>
				<td align="right">
			</td>
			</tr>
		</table>
	</xsl:template>
	<!-- Display solicitor details -->
	<xsl:template name="solicitor">
		<xsl:param name="party"/>
		<!-- Put in condition to not display the Solicitor details if the cs:EndDate is populated which means 
		that the solicitor is no longer representing the defendant -->
		<xsl:if test="string-length(cs:EndDate) != 10 and string-length(cs:EndDate) != 9">
			<xsl:choose>
				<xsl:when test="$party/cs:Organisation or $party/cs:Person">
					<xsl:text>Sols: </xsl:text>
					<xsl:choose>
						<xsl:when test="$party/cs:Organisation">
							<xsl:value-of select="$party/cs:Organisation/cs:OrganisationName"/>
							<br/>
							<xsl:if test="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber">
								<xsl:text> Tel: </xsl:text>
								<xsl:value-of select="$party/cs:Organisation/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<!-- Format the name -->
							<xsl:call-template name="FormalName">
								<xsl:with-param name="name" select="$party/cs:Person/cs:PersonalDetails/cs:Name"/>
							</xsl:call-template>
							<br/>
							<xsl:text> Tel: </xsl:text>
							<xsl:value-of select="$party/cs:Person/cs:ContactDetails/apd:Telephone/apd:TelNationalNumber"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- Template to make the first character a capital and rest in lower case -->
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
	<!-- template used to convert a string to upper case -->
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>
	<!-- Replace Instances of 'Magistrates Court with MC' in originating court names-->
	<!-- Replace Instances of 'Youth Court with YC' in originating court names-->
	<!-- Replace Instances of 'Crown Court with CC' in originating court names-->
	<xsl:param name="MCreplace" select='"MAGISTRATES&apos; COURT"'/>
	<xsl:param name="MCby" select="' MC'"/>
	<xsl:param name="YCreplace" select='"YOUTH COURT"'/>
	<xsl:param name="YCby" select="' YC'"/>
	<xsl:param name="CCreplace" select='"CROWN COURT"'/>
	<xsl:param name="CCby" select="' CC'"/>
	<xsl:template name="do-replace">
		<xsl:param name="text"/>
		<xsl:choose>
			<xsl:when test="contains($text, $MCreplace)">
				<xsl:call-template name="TitleCase">
					<xsl:with-param name="text" select="substring-before($text, $MCreplace)"/>
				</xsl:call-template>
				<xsl:value-of select="$MCby"/>
				<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="substring-after($text, $MCreplace)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="contains($text, $YCreplace)">
				<xsl:call-template name="TitleCase">
					<xsl:with-param name="text" select="substring-before($text, $YCreplace)"/>
				</xsl:call-template>
				<xsl:value-of select="$YCby"/>
				<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="substring-after($text, $YCreplace)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="contains($text, $CCreplace)">
				<xsl:call-template name="TitleCase">
					<xsl:with-param name="text" select="substring-before($text, $CCreplace)"/>
				</xsl:call-template>
				<xsl:value-of select="$CCby"/>
				<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="substring-after($text, $CCreplace)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="DispMethInst">
		<xsl:param name="methInst"/>
		<xsl:param name="court"/>
		<xsl:param name="courtType"/>
		<xsl:param name="caseType"/>
		<xsl:variable name="upperCourtType">
			<xsl:call-template name="toUpper">
				<xsl:with-param name="content" select="$courtType"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="newCourt">
			<xsl:choose>
				<xsl:when test="$courtType = 'Crown Court' and not(contains($court,$CCreplace))">
					<xsl:value-of select="$court"/><xsl:text> </xsl:text><xsl:value-of select="$upperCourtType"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$court"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$methInst='Committal'">
				<!-- Display originating court details -->
				<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="$newCourt"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$methInst='Voluntary bill'">
				<xsl:text>Voluntary Bill</xsl:text>
				<!-- Display originating court details -->
				<!--<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="$newCourt"/>
				</xsl:call-template> -->
			</xsl:when>
			<xsl:when test="$methInst='Sending'">
			</xsl:when>
			<xsl:when test="$methInst='Execution'">
				<xsl:text>Bench Warrant Executed</xsl:text>
			</xsl:when>
			<xsl:when test="$methInst='Transfer'">
				<xsl:text>TI</xsl:text>
				<br/>
				<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="$newCourt"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$methInst='Transfer certificated'">
				<xsl:text>Transfer Certificate Case</xsl:text>
			</xsl:when>
			<xsl:when test="$methInst='Rehearing ordered'">
				<!-- if case type = A display Re-hearing all else display Re-trial -->
				<xsl:choose>
					<xsl:when test="$caseType = 'A'">
						<xsl:text>Re-hearing</xsl:text>					
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>Re-trial</xsl:text>					
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<!--covers 'CMC' and empty string methods of instigation  -->
				<!-- Display originating court details -->
				<xsl:call-template name="do-replace">
					<xsl:with-param name="text" select="$newCourt"/>
				</xsl:call-template>
				<!--	<xsl:if test="$case/cs:AppealCaseDescription">-->
				<!--	<xsl:value-of select="$case/cs:AppealCaseDescription"/>-->
				<!--	</xsl:if>-->
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="noDeftText">
		<xsl:if test="./cs:PersonalDetails/cs:Name/apd:CitizenNameSurname='**No Deft/Applt**'">
			<xsl:text>**********Warning: Case party details are incomplete in the above case*********</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
