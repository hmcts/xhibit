<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooterLetters.xsl"/>
	<xsl:template name="NFIXDefendants">
		<xsl:param name="basedir"/>
		<xsl:for-each select="./nfix-fixture-values">
			<xsl:for-each select="./nfix-solicitor-values">
				<xsl:call-template name="Header">
					<xsl:with-param name="basedir" select="$basedir"/>
					<xsl:with-param name="path" select="../."/>
				</xsl:call-template>
				
				<fo:block space-after="5px">
					<fo:leader leader-pattern='rule' leader-length='100%'/>
				</fo:block>	
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
								<fo:block font-size="10pt" font-family="sans-serif" text-align="left">
									<xsl:value-of select="./defendant-solicitor-name"/>
								</fo:block>	
								<fo:block font-size="10pt" font-family="sans-serif" text-align="left" >
									<xsl:call-template name="formatAddressMultiLine">
										<xsl:with-param name="list" select="./defendant-solicitor-address"/>
										<xsl:with-param name="delimiter" select="'|'"/>
									</xsl:call-template>
								</fo:block>	
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<fo:block>
					<fo:leader leader-pattern='rule' leader-length='100%'/>
				</fo:block>	
				
				<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" space-before="20px" >
					<xsl:text>NOTIFICATION OF FIXTURE</xsl:text>
				</fo:block>	
				<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="10px" space-after="30px">
					<xsl:text>The following case has been given a fixed date for hearing. Please inform the Listing Officer immediately of any matters that could affect this hearing date.</xsl:text>
				</fo:block>
				
				<fo:table border="none" table-layout="fixed">
					<fo:table-column column-number="1" column-width="75px"/>
					<fo:table-column column-number="2" column-width="300px"/>
					<fo:table-column column-number="3" column-width="100px"/>
					<fo:table-body>
						<fo:table-row border="none">
							<fo:table-cell border-style="none">
								<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
									<xsl:text>Date:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" number-columns-spanned="2">
								<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
									<xsl:value-of select="../fixture-date"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row border="none">
							<fo:table-cell border-style="none">
								<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
									<xsl:text>At:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" number-columns-spanned="2">
								<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
									<xsl:value-of select="../hearing-address"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row border="none">
							<fo:table-cell border-style="none">
								<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
									<xsl:text>Listing:</xsl:text>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="none" number-columns-spanned="2">
								<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
									<xsl:value-of select="../hearing-code-description"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:variable name="DefendantsWithOtherSolicitors" select="preceding-sibling::nfix-solicitor-values//defendant-name | following-sibling::nfix-solicitor-values//defendant-name"/>
						<xsl:for-each select="./defendant">
							<fo:table-row border="none">
								<fo:table-cell border-style="none">
									<xsl:if test="position() = '1'">
										<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
											<xsl:value-of select="../../case-number"/><xsl:text>   </xsl:text>
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell border-style="none">
									<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
										<xsl:value-of select="./defendant-name"/>
									</fo:block>
									<xsl:if test="position() = last() and $DefendantsWithOtherSolicitors">
										<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
											<xsl:text>and </xsl:text><xsl:value-of select="count($DefendantsWithOtherSolicitors)"/><xsl:text> other(s)</xsl:text>
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell border-style="none">
									<xsl:if test="./solicitor-reference">
										<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="5px" >
											<xsl:text>Ref:  </xsl:text><xsl:value-of select="./solicitor-reference"/>
										</fo:block>
									</xsl:if>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
				
				<xsl:if test="../list-note or ../predefined-list-note">
					<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="20px" >
						<xsl:text>List note(s) from fixture:</xsl:text> <xsl:value-of select="concat(../predefined-list-note, '  ', ../list-note)"/>
					</fo:block>
				</xsl:if>
				<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="50px" >
					<xsl:text>An Officer of the Crown Court</xsl:text>
				</fo:block>
				<fo:block font-size="11pt" font-family="sans-serif" text-align="left" >
					<xsl:value-of select="//date-of-request"/>
				</fo:block>
				<xsl:if test="position()&lt;last()">
					<fo:block break-after="page"/>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="position()&lt;last()">
					<fo:block break-after="page"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="formatAddressMultiLine">
		<xsl:param name="list"/>
		<xsl:param name="delimiter"/>
		<xsl:choose>
			<xsl:when test="contains($list, $delimiter)">
				<fo:block font-size="10pt" font-family="sans-serif">
					<xsl:value-of select="substring-before($list,$delimiter)"/>
				</fo:block>
				<xsl:call-template name="formatAddressMultiLine">
					<xsl:with-param name="list" select="substring-after($list,$delimiter)"/>
					<xsl:with-param name="delimiter" select="$delimiter"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$list = ''">
						<xsl:text/>
					</xsl:when>
					<xsl:otherwise>
						<fo:block font-size="10pt" font-family="sans-serif">
							<xsl:value-of select="$list"/>
						</fo:block>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>