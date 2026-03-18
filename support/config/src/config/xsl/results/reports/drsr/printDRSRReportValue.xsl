<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:template name="DRSRReportValues"> 
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="80px"/>
		<fo:table-column column-number="2" column-width="80px"/>
		<fo:table-column column-number="3" column-width="240px"/>
		<fo:table-header>	
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>List Date</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Case No</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Details of Missing Codes</xsl:text>
					</fo:block>	
				</fo:table-cell>			
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./drsr-report-values"> 
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./listing-date"/>	
						</fo:block>
					</fo:table-cell>	
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./case-number"/>		
					</fo:block>
					</fo:table-cell>		
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:text>Trial </xsl:text> 
							<xsl:value-of select="./htyp"/>	
							<xsl:text>, Sitting </xsl:text> 
							<xsl:value-of select="./sitting-sequence-no"/> 
							<xsl:text>, Room </xsl:text>	
							<xsl:value-of select="./court-room-name"/> 
							<xsl:text>, Site </xsl:text> 
							<xsl:value-of select="./site"/>	
							<xsl:text> has a missing cracked/effective code</xsl:text>
						</fo:block>
					</fo:table-cell>																			
				</fo:table-row>		
			</xsl:for-each>
	</fo:table-body>
	</fo:table>	
</xsl:template>
</xsl:stylesheet>	