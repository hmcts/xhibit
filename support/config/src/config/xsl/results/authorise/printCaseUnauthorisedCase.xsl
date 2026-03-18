<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:template name="UnauthorisedCases">
	<xsl:for-each select="unauthorised-cases-court-values">
	<fo:block font-size="13pt" font-family="sans-serif"  space-before.optimum="25pt" space-after.optimum="1pt" font-weight="bold" text-align="left">
		<xsl:text>Court Centre Name: </xsl:text><xsl:value-of select="./court-name"/>
    </fo:block>
    <fo:block font-size="13pt" font-family="sans-serif"  space-before.optimum="1pt" space-after.optimum="3pt" font-weight="bold" text-align="left">
		<xsl:text>Court Centre ID: </xsl:text><xsl:value-of select="./court-id"/>
    </fo:block>
	<fo:block>
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1"/>
		<fo:table-column column-number="2"/>
		<fo:table-column column-number="3"/>
		<fo:table-column column-number="4" column-width="70mm"/>
		<fo:table-column column-number="5"/>
		<fo:table-column column-number="6"/>
		<fo:table-column column-number="7"/>
		<fo:table-header>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Court Room</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Case Number</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Conclusion Date</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Defendant Name</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Authorisation Status</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>No. days between final hearing and export date</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Date Exported</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./unauthorised-cases">
			<xsl:for-each select="./defendants">
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!-- failure reason column -->
						<fo:block font-size="9pt" font-weight="normal">
						<xsl:if test="position()=1" >
						  <xsl:value-of select="./../court-room"/>
						  </xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!--  failure detail column -->
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">							
						<xsl:if test="position()=1" >
							<xsl:value-of select="./../case-type-and-number"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!--  failure detail column -->
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
						<xsl:if test="position()=1" >
							<xsl:value-of select="./../conclusion-date"/>
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!--  failure detail column -->
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:value-of select="./deft-name"/>
						</fo:block>
					</fo:table-cell>										
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!--  failure detail column -->
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:choose>
								<xsl:when test="contains(./deft-results-exported,'E')">
										<xsl:text>Export Successful</xsl:text>
								</xsl:when>
								<xsl:when test="contains(./deft-results-exported,'R')">
										<xsl:text>Ready for Export</xsl:text>
								</xsl:when>
								<xsl:when test="contains(./deft-results-exported,'U')">
										<xsl:text>Exporting</xsl:text>
								</xsl:when>
								<xsl:when test="contains(./deft-results-exported,'F')">
										<xsl:text>Export Failed</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>Not Authorised</xsl:text>
								</xsl:otherwise>
							</xsl:choose>							
						</fo:block>
					</fo:table-cell>					
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!--  failure detail column -->
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:choose>
								<xsl:when test="contains(./deft-results-exported,'E')">
										<xsl:value-of select="./date-diff"/>
								</xsl:when>
							</xsl:choose>							
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<!--  failure detail column -->
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:choose>
								<xsl:when test="contains(./deft-results-exported,'E')">
										<xsl:value-of select="./date-exported"/>
								</xsl:when>
							</xsl:choose>							
						</fo:block>
					</fo:table-cell>					
				</fo:table-row>                    
			</xsl:for-each>
			</xsl:for-each>
<!--			<xsl:apply-templates select="./case-failure-reasons"/>-->
		</fo:table-body>
	</fo:table>
	</fo:block>
	</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
