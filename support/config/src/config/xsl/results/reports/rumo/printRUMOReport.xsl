<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="RUMOReports">
    <fo:block>
	<fo:table table-layout="fixed" padding-left="15px">
		<fo:table-column column-number="1" column-width="100px"/>
		<fo:table-column column-number="2" column-width="200px"/>
		<fo:table-column column-number="3" column-width="100px"/>
		<fo:table-column column-number="4" column-width="100px"/>
		<fo:table-header>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>CASE NO</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>DEFT/APPL/RESP NAME and DOB</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>DATE OF ORDER</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>ORDER TYPE/AMOUNT</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		
		<fo:table-body>
			<xsl:for-each select="./cases">
				<xsl:variable name="formattedDefName">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./defendant"/>
					</xsl:call-template>
				</xsl:variable>
			
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<xsl:if test="./case-number or not(./case-number = '')">
						<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<fo:block font-size="9pt" font-weight="normal">
								<xsl:value-of select="./case-number"/>
							</fo:block>
						</fo:table-cell>
					</xsl:if >
						<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<xsl:if test="./defendant or not(./defendant = '')">
								<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
									<xsl:value-of select="$formattedDefName"/><xsl:text>&#xa;</xsl:text>
								</fo:block>
							</xsl:if>
							<xsl:if test="./ptiurn or not(./ptiurn = '')">
								<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
									<xsl:value-of select="./ptiurn"/>
								</fo:block>					
							</xsl:if >
							<xsl:if test="./dob or not(./dob = '')">
								<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
									<xsl:value-of select="./dob"/>
								</fo:block>					
							</xsl:if >
						</fo:table-cell>
						<xsl:if test="./order-date or not(./order-date = '')">
							<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
								<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
									<xsl:value-of select="./order-date"/>
								</fo:block>
							</fo:table-cell>
						</xsl:if >
						<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<xsl:choose>
								<xsl:when test="(./fined= '') or not(./fined)">
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:text></xsl:text>
									</fo:block>	
								</xsl:when>
								<xsl:otherwise>
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:text>FINE: </xsl:text><xsl:value-of select="./fined"/>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:choose>
								<xsl:when test="(./costs= '') or not(./costs)">
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:text></xsl:text>
									</fo:block>	
								</xsl:when>
								<xsl:otherwise>
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:text>COSTS: </xsl:text><xsl:value-of select="./costs"/>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:choose>
								<xsl:when test="(./compensation= '') or not(./compensation)">
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:text></xsl:text>
									</fo:block>	
								</xsl:when>
								<xsl:otherwise>
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:text>COMPENSATION: </xsl:text><xsl:value-of select="./compensation"/>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-cell>
				</fo:table-row> 
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
	<fo:footnote>
		<fo:inline/>
		<fo:footnote-body>
			<fo:block text-align="left">
				<fo:block>
					<xsl:text>An Officer of the Crown Court</xsl:text>
				</fo:block>
				<fo:block>
					<xsl:value-of select="./today-date"/>
				</fo:block>
			</fo:block>
		</fo:footnote-body>
	</fo:footnote>
	</fo:block>
</xsl:template>
</xsl:stylesheet>
