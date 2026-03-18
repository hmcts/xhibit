<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooterLetters.xsl"/>
	<xsl:template name="NHACases">
		<xsl:param name="basedir"/>
		<xsl:for-each select="./NHACaseValues">
				
				<!-- Copy for Clerk to Justice if there is a Magistrates Court set -->
				<xsl:if test="(./magistrate-name != '') and string-length(./magistrate-name) > 0">
					<xsl:call-template name="NHACommonHeader"/>
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell>
									<fo:block>
										<xsl:text> </xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="3.5cm">
									<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:value-of select="./clerk-to-justice-name"/><xsl:text>&#xA;</xsl:text>
										<xsl:value-of select="./magistrate-address"/>
									</fo:block>	
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<xsl:call-template name="NHACommonBody"/>
					<fo:block break-after="page"/>
				</xsl:if>
				
				<!-- Copy for Defendant/Appellant -->
				<xsl:call-template name="NHACommonHeader"/>	
				<fo:table border="none" table-layout="fixed">
					<fo:table-column column-number="1" column-width="1.0cm"/>
					<fo:table-column column-number="2" column-width="10.0cm"/>
					<fo:table-body>
						<fo:table-row border="none">
							<fo:table-cell>
								<fo:block>
									<xsl:text> </xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell height="3.5cm">
								<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
									<xsl:value-of select="./appellant-name"/><xsl:text>&#xA;</xsl:text>
									<xsl:value-of select="./defendant-address"/>
								</fo:block>	
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<xsl:call-template name="NHACommonBody"/>
				<fo:block break-after="page"/>
				
				<!-- Copy for Defendant/Appellant's Solicitor if present -->
				<xsl:if test="normalize-space(./solicitor-firm-name) != ''">
					<xsl:call-template name="NHACommonHeader"/>	
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell>
									<fo:block>
										<xsl:text> </xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="3.5cm">
									<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:value-of select="./solicitor-firm-name"/><xsl:text>&#xA;</xsl:text>
										<xsl:value-of select="./solicitor-address"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<xsl:call-template name="NHACommonBody"/>
					<fo:block break-after="page"/>
				</xsl:if>
				
				<!-- Copy for the Governor if the Appellant is in custody -->
				<xsl:if test="./in-custody='Y'">
					<xsl:call-template name="NHACommonHeader"/>
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell>
									<fo:block>
										<xsl:text> </xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="3.5cm">
									<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" space-after="3.0cm">
										<xsl:text>The Governor</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<xsl:call-template name="NHACommonBody"/>
					<fo:block break-after="page"/>
				</xsl:if>
				
				<!-- Copy for the Respondent -->
				<xsl:call-template name="NHACommonHeader"/>
				<fo:table border="none" table-layout="fixed">
					<fo:table-column column-number="1" column-width="1.0cm"/>
					<fo:table-column column-number="2" column-width="10.0cm"/>
					<fo:table-body>
						<fo:table-row border="none">
							<fo:table-cell>
								<fo:block>
									<xsl:text> </xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell height="3.5cm">
								<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
									<xsl:value-of select="./respondent-name"/><xsl:text>&#xA;</xsl:text>
									<xsl:value-of select="./respondent-address"/>
								</fo:block>	
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<xsl:call-template name="NHACommonBody"/>
				
				<!-- Copy for Respondent's Solicitor if present -->
				<xsl:if test="normalize-space(./resp-sol-name) != ''">
					<fo:block break-after="page"/>
					<xsl:call-template name="NHACommonHeader"/>	
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell>
									<fo:block>
										<xsl:text> </xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="3.5cm">
									<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:value-of select="./resp-sol-name"/><xsl:text>&#xA;</xsl:text>
										<xsl:value-of select="./resp-sol-address"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					
					<xsl:call-template name="NHACommonBody"/>
				</xsl:if>
				
				<!-- Copy for each objector on the case -->
				<xsl:for-each select="./objectors">
					<fo:block break-after="page"/>
					<xsl:call-template name="NHACommonHeader">
						<xsl:with-param name="path" select=".."/>
					</xsl:call-template>
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell>
									<fo:block>
										<xsl:text> </xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="3.5cm">
									<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
										<xsl:value-of select="./objector-name"/><xsl:text>&#xA;</xsl:text>
										<xsl:value-of select="./objector-address"/>
									</fo:block>	
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<xsl:call-template name="NHACommonBody">
						<xsl:with-param name="path" select=".."/>
					</xsl:call-template>
					
					<!-- Copy for Objector's Solicitor if present -->
					<xsl:if test="normalize-space(./solicitor-name) != ''">
						<fo:block break-after="page"/>
						<xsl:call-template name="NHACommonHeader">
							<xsl:with-param name="path" select=".."/>
						</xsl:call-template>
						<fo:table border="none" table-layout="fixed">
							<fo:table-column column-number="1" column-width="1.0cm"/>
							<fo:table-column column-number="2" column-width="10.0cm"/>
							<fo:table-body>
								<fo:table-row border="none">
									<fo:table-cell>
										<fo:block>
											<xsl:text> </xsl:text>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell height="3.5cm">
										<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
											<xsl:value-of select="./solicitor-name"/><xsl:text>&#xA;</xsl:text>
											<xsl:value-of select="./solicitor-address"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<xsl:call-template name="NHACommonBody">
							<xsl:with-param name="path" select=".."/>
						</xsl:call-template>
					</xsl:if>
					
				</xsl:for-each>
				
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="NHACommonHeader">
		<xsl:param name="path" select="."/>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="15.0cm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body>
				<fo:table-row font-size="7pt">
					<fo:table-cell>
						<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="left" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>The Crown Court</xsl:text>
							<xsl:text>&#xD;&#xA;</xsl:text>
							<xsl:text>at </xsl:text><xsl:value-of select="$path//court-name"/>
							<xsl:text>   </xsl:text>                     
						</fo:block>
						<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="15px" >
							<fo:inline font-weight="bold">Case No: </fo:inline>
							<xsl:value-of select="$path/case-number"/>
						</fo:block>	
						<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="3px" >
							<fo:inline font-weight="bold">Court Code: </fo:inline>
							<xsl:value-of select="$path/court-code"/>
						</fo:block>	
					</fo:table-cell>
					<fo:table-cell>
					    <fo:block>
							<xsl:value-of select="./basedir"/>
							<fo:external-graphic>
								<xsl:attribute name="src"><xsl:value-of select="concat($basedir, 	'/images/Crown_Logo_Updated_2025.gif')"/></xsl:attribute>
								<xsl:attribute name="height">3.5cm</xsl:attribute>
								<xsl:attribute name="width">4.5cm</xsl:attribute>
								<xsl:attribute name="content-width">scale-to-fit</xsl:attribute>
							</fo:external-graphic>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>			
		
		
		<fo:block space-before="1px">
			<fo:leader leader-pattern='rule' leader-length='100%'/>
		</fo:block>		
	</xsl:template>
	
	<xsl:template name="NHACommonBody">
		<xsl:param name="path" select="."/>
		<xsl:variable name="appealText">
			<xsl:choose>
				<xsl:when test="$path/case-sub-type = 'O'">
					<xsl:text>against a decision</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$path/appeal-type"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="byName">
			<xsl:choose>
				<xsl:when test="($path/magistrate-name != '') and string-length($path/magistrate-name) > 0">
					<xsl:value-of select="$path/magistrate-name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$path/respondent-surname"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="onDate">
			<xsl:choose>
				<xsl:when test="$path/case-sub-type = 'O'">
					<xsl:value-of select="$path/orig-body-decision-date"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$path/mag-court-conviction-date"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
	
		<fo:block space-before="1px">
			<fo:leader leader-pattern='rule' leader-length='100%'/>
		</fo:block>	
		<fo:block font-size="10pt" font-family="sans-serif" text-align="center" space-before="3px" >
			<xsl:text>NOTICE OF HEARING OF APPEAL</xsl:text>
		</fo:block>
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="3px" >
			<fo:inline>The appeal of </fo:inline>
			<xsl:value-of select="$path/appellant-name"/>
		</fo:block>	
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" >
			<xsl:value-of select="$appealText"/>
		</fo:block>
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="3px" >
			<fo:inline>By </fo:inline>
			<xsl:value-of select="$byName"/>
			<fo:inline> on </fo:inline>
			<xsl:value-of select="$onDate"/>
		</fo:block>	
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="4px" >
			<fo:inline>Will be heard at the Crown Court at </fo:inline>
			<xsl:value-of select="$path/hearing-venue"/><xsl:text> </xsl:text><xsl:value-of select="$path/hearing-address"/>
		</fo:block>	
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="4px" >
			<fo:inline>On the </fo:inline>
			<xsl:value-of select="$path/date-of-fixture"/>
			<fo:inline> at </fo:inline>
			<xsl:value-of select="$path/time-of-listing"/>
		</fo:block>
		<xsl:if test="normalize-space($path/list-note-text) != '' or normalize-space($path/pre-defined-list-note) != ''">
			<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left" space-before="4px" >
				<xsl:value-of select="$path/pre-defined-list-note"/>
				<xsl:if test="normalize-space($path/list-note-text) != '' and normalize-space($path/pre-defined-list-note) != ''">
					<xsl:text>, </xsl:text>
				</xsl:if>
				<xsl:value-of select="$path/list-note-text"/>
			</fo:block>
		</xsl:if>
		<fo:block>
			<fo:leader leader-pattern='rule' leader-length='100%' font-weight="bold"/>
		</fo:block>	
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left">
			<xsl:text>Important Information:</xsl:text>
			<fo:list-block>
			
			<fo:list-item>
				<fo:list-item-label>
				<fo:block space-before="2px">
					<xsl:text>&#8226; </xsl:text>
				</fo:block>
				</fo:list-item-label>
				<fo:list-item-body>
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
					<xsl:text>If you are not legally represented, you are advised to telephone the court on </xsl:text> <xsl:value-of select="$path/court-phone-number"/>
				</fo:block>
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm" space-after="1.5px">
					<xsl:text>during the afternoon before the hearing of the appeal for the confirmation of the time your case will be heard</xsl:text>
				</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			
			<fo:list-item>
				<fo:list-item-label>
				<fo:block space-before="2px">
					<xsl:text>&#8226; </xsl:text>
				</fo:block>
				</fo:list-item-label>
				<fo:list-item-body>
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm" space-after="1.5px">
					<xsl:text>If your appeal is likely to last LONGER THAN 1 HOUR please tell us IMMEDIATELY</xsl:text>
				</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			
			<fo:list-item>
				<fo:list-item-label>
				<fo:block space-before="2px">
					<xsl:text>&#8226; </xsl:text>
				</fo:block>
				</fo:list-item-label>
				<fo:list-item-body>
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
					<xsl:text>To abandon your appeal:</xsl:text>
				</fo:block>
				
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
				   <xsl:text>If you are appealing against the decision of a magistrates' court or licensing justices you must</xsl:text>
				</fo:block>
			
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
					<xsl:text>give a written notice of your wish to abandon the appeal to the Clerk to Justices; a copy of the</xsl:text> 
				</fo:block>
			
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
				   <xsl:text>notice to the Crown Court; a copy to every other party to the appeal.</xsl:text> 
				</fo:block>
				
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
				   <xsl:text>If you are appealing against any other decision you must give a written notice to the Crown Court</xsl:text> 
				</fo:block>
				
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
				   <xsl:text>and a copy to every other party to the appeal.</xsl:text> 
				</fo:block>
			
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
				   <xsl:text>Notice must be given at least 3 clear days (not counting Saturdays, Sundays and Bank</xsl:text>
				</fo:block>
			
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
				   <xsl:text>Holidays) before the date of hearing. If you do not, or if you do not appear at the hearing,</xsl:text>
				</fo:block>
			
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm" space-after="1.5px">
				   <xsl:text>you may have to pay costs.</xsl:text>
				</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			
			</fo:list-block>
			
			<fo:list-block> 
			
			<fo:list-item>
				<fo:list-item-label>
				<fo:block space-before="2px">
					<xsl:text>&#8226; </xsl:text>
				</fo:block>
				</fo:list-item-label>
				<fo:list-item-body>
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
					<xsl:text>If you are not successful in your appeal to the Crown Court you may be ordered to pay the</xsl:text>
				</fo:block>
				<fo:block font-size="10pt" font-family="sans-serif" start-indent="1cm">
					<xsl:text>costs of the Respondent or at least a part of them. If you are successful you may be awarded costs</xsl:text>
				</fo:block>
				</fo:list-item-body>
			</fo:list-item>
			
			</fo:list-block>
			
						
		</fo:block>	
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="4px" >
		<fo:block font-style="italic" space-before="3px">
			<xsl:text>Copy to:</xsl:text>
		</fo:block>
		<fo:block font-style="italic" space-before="3px">
			<xsl:text>Appellant</xsl:text>
		</fo:block>
		<fo:block font-style="italic" space-before="3px">
			<xsl:text>Respondent</xsl:text>
		</fo:block>
		<fo:block font-style="italic" space-before="3px">
			<xsl:text>Clerk to the Justices </xsl:text>
		</fo:block>
		</fo:block>
		<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="16px" >
		<fo:block>
			<xsl:text>An Officer of the Crown Court</xsl:text>
		</fo:block>
		<fo:block space-before="3px">
			<xsl:value-of select="$path/todays-date"/>
		</fo:block>
		</fo:block>	
		<fo:block break-after="page"/>
	</xsl:template>
</xsl:stylesheet>