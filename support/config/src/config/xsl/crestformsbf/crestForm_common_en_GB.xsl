<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:crestformsbf="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

	<!-- Setup templates that are used common across all the CrestForms B-F -->

	<!-- Display Disposal Details For Crest Form F-->
	<xsl:template match="crestformsbf:Disposal" mode="FormF">
		<fo:block>
			<xsl:choose>
				<xsl:when test="crestformsbf:IsMagistrateCourt = 'Yes'">
					<xsl:text>Magistrate: </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Variation: </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<!-- call template to display disposal details -->
			<xsl:call-template name="DisposalDetails"/>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
	</xsl:template>
	
	<!-- Display Disposal Details -->
	<xsl:template match="crestformsbf:Disposal">
		<fo:block>
			<!-- call template to display disposal details -->
			<xsl:call-template name="DisposalDetails"/>
		</fo:block>
		<fo:block line-height="8pt" space-after="8pt"> </fo:block>
	</xsl:template>
	
	<!-- Display UnrelatedDisposal Details -->
	 <xsl:template name="UnrelatedDisposal">
		<!-- Display details for Crown Court Disposals -->
       	<xsl:for-each select="crestformsbf:UnrelatedDisposals/crestformsbf:Disposal/crestformsbf:IsMagistrateCourt[.='No']">
			<fo:block>
                    	<fo:table table-layout="fixed">
					<fo:table-column column-width="35mm"/>
					<fo:table-column column-width="150mm"/>
					<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:if test="position()=1">
										<xsl:text>Crown Court</xsl:text>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
									<xsl:for-each select="..">
										<xsl:call-template name="DisposalDetails"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</xsl:for-each>
		<!-- Display details for Magistrates Court Disposals -->
		<xsl:for-each select="crestformsbf:UnrelatedDisposals/crestformsbf:Disposal/crestformsbf:IsMagistrateCourt[.='Yes']">
			<fo:block>
				<fo:table table-layout="fixed">
					<fo:table-column column-width="35mm"/>
					<fo:table-column column-width="150mm"/>
					<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:if test="position()=1">
										<xsl:text>Magistrates Court</xsl:text>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-family="Courier" text-decoration="underline" font-size="15pt">
									<xsl:for-each select="..">
										<xsl:call-template name="DisposalDetails"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</xsl:for-each>
	</xsl:template>

	<!-- Appeal Case Form F and FMisc, display Judges Comments -->
	<!-- Display judges reason for the appeal -->
	<xsl:template match="crestformsbf:AppealCase">
		<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-width="20mm"/>
				<fo:table-column column-width="165mm"/>
				<fo:table-body font-family="Times" font-weight="normal" font-size="11pt" line-height="20pt">
					<fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block font-family="Courier" font-size="15pt">
								<xsl:value-of select="crestformsbf:ReasonForAppeal"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>


	<!-- template used to display disposal details, all details as part of FormBFText element -->
	<xsl:template name="DisposalDetails">
		<xsl:if test="crestformsbf:FormBFText">
			<xsl:value-of select="crestformsbf:FormBFText"/>
		</xsl:if>
		<xsl:if test="crestformsbf:VerdictCode">
        		<fo:block>
        			<xsl:text>Result: </xsl:text>
        			<xsl:value-of select="crestformsbf:VerdictCode"/>
        			<xsl:text> : </xsl:text>
        			<xsl:value-of select="crestformsbf:VerdictDesc"/>
        		</fo:block>
		</xsl:if>
            <!-- display variation details, only applies to CrestForm F (unrelated disposals)-->
            <xsl:if test="count(crestformsbf:Variations/crestformsbf:Variation) > 0">
                <fo:block space-after="10pt">
                    <xsl:for-each select="crestformsbf:Variations/crestformsbf:Variation">
                        <fo:block>
                            <xsl:text>Variation: </xsl:text>
                            <xsl:value-of select="crestformsbf:FormBFText"/>
                        </fo:block>
                    </xsl:for-each>
                </fo:block>
            </xsl:if>
	</xsl:template>
		
	<!-- OLD DISPOSAL TEMPLATE -->
	<!-- template used to display the disposal details -->
	<xsl:template name="DisposalDetails_old">
		<xsl:if test="crestformsbf:DisposalCode!=''">
			<xsl:value-of select="crestformsbf:DisposalCode"/>
			<xsl:text> - </xsl:text>
		</xsl:if>
		<xsl:value-of select="crestformsbf:DisposalDesc"/>
		<!-- If Disposal for Life -->
		<xsl:if test="crestformsbf:DisposalDuration='Life'">
			<xsl:text> - </xsl:text>
			Life
		</xsl:if>
		<!-- If disposal for a period -->
		<xsl:if test="crestformsbf:DisposalDuration='Period'">
			<xsl:if test="crestformsbf:Years!='0'">
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:Years"/>
				<xsl:text> year(s)</xsl:text>
			</xsl:if>
			<xsl:if test="crestformsbf:Months!='0'">
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:Months"/>
				<xsl:text> month(s)</xsl:text>
			</xsl:if>
			<xsl:if test="crestformsbf:Days!='0'">
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:Days"/>
				<xsl:text> day(s)</xsl:text>
			</xsl:if>
			<xsl:if test="crestformsbf:Hours!='0'">
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:Hours"/>
				<xsl:text> hours(s)</xsl:text>
			</xsl:if>
			<xsl:if test="crestformsbf:Amount!='0'">
				<!-- set up variable to hold the upper case of the Disposal Description -->
				<xsl:variable name="UPPERDispDesc">
					<xsl:call-template name="toUpper">
						<xsl:with-param name="content" select="crestformsbf:DisposalDesc"/>
					</xsl:call-template>
				</xsl:variable>
				<!-- only display 'Fine' text if it is not present within the Disposal Description already -->
				 <xsl:if test="not(contains($UPPERDispDesc,'FINE'))">
					<xsl:text> Fine </xsl:text>
				 </xsl:if>
				<xsl:text> £</xsl:text>
				<xsl:value-of select="crestformsbf:Amount"/>
			</xsl:if>
			<xsl:if test="crestformsbf:DisposalTerm!=''">
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:DisposalTerm"/>
			</xsl:if>
		</xsl:if>
		<!-- Display disposal text regardless of Disposal Duration -->
		<xsl:if test="crestformsbf:DisposalText!=''">
			<xsl:text> </xsl:text>
			<xsl:value-of select="crestformsbf:DisposalText"/>
		</xsl:if>
	</xsl:template>

	<!-- Appeal Case Details -->
	<!-- Used by Crest Forms F, FMisc -->
	<xsl:template name="AppealCaseNoDetails">
		<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt">
			<xsl:value-of select="crestformsbf:AppealCase/crestformsbf:CaseReference"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantNumber"/>
		</fo:block>
	</xsl:template>

	<!-- Common template to display Case Reference and Defendant details -->
	<xsl:template name="CaseNoDetails">
		<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
             		Case and Defendant No.:
			<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt">
				<xsl:value-of select="crestformsbf:Case/crestformsbf:CaseReference"/>
				<xsl:text>-</xsl:text>
				<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantNumber"/>
			</fo:block>
		</fo:block>
	</xsl:template>

	<!-- Display CaseNo and defendant details excluding Title -->
	<!-- Used by Crest Forms E -->
	<xsl:template name="CaseNoDetailsExcTitle">
		<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt">
			<xsl:value-of select="crestformsbf:Case/crestformsbf:CaseReference"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantNumber"/>
		</fo:block>
	</xsl:template>

	<!-- Common template to display court clerk details -->
	<xsl:template name="CourtClerkDetails">
		<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
			Court Clerk:
			<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt" hyphenate="true" language="en">
				<xsl:value-of select="crestformsbf:CourtClerk/crestformsbf:ClerkName"/>
			</fo:block>
		</fo:block>
	</xsl:template>

	<!-- Common template to display defendant details -->
	<xsl:template name="DefendantDetails">
		<fo:block line-height="20pt" margin-left="3pt" font-family="Times" font-size="11pt">
			Defendant's name:
			<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt" hyphenate="true" language="en">
				<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantName/crestformsbf:Forename"/>
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantName/crestformsbf:MiddleName"/>
				<xsl:text> </xsl:text>
				<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantName/crestformsbf:Surname"/>
			</fo:block>
		</fo:block>
	</xsl:template>

	<!-- Display defendant details excluding title -->
	<!-- Used by CrestForms E, F, FMisc  -->
	<xsl:template name="DefendantDetailsExcTitle">
		<fo:block line-height="30pt" margin-left="3pt" margin-right="3pt" text-align="center" font-family="Courier" text-decoration="underline" font-size="15pt" hyphenate="true" language="en">
			<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantName/crestformsbf:Forename"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantName/crestformsbf:MiddleName"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="crestformsbf:Defendant/crestformsbf:DefendantName/crestformsbf:Surname"/>
		</fo:block>
	</xsl:template>

	<!-- Display the date in the following format DD-MMM-YYYY -->
	<xsl:template name="displayDate">
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
	
	<!-- Display the date in the following format DD-MMM-YYYY -->
	<xsl:template name="displayDate2">
		<xsl:param name="input"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:value-of select="$day"/>
		<xsl:text>-</xsl:text>
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
		<xsl:text>-</xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>
	

	<!-- Convert a string to Initialise the first character and lower case the rest -->
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

	<!-- Convert text to UPPER CASE -->
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>

</xsl:stylesheet>
