<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="RELCJReportValues"> 
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="60px"/>
		<fo:table-column column-number="2" column-width="130px"/>
		<fo:table-column column-number="3" column-width="35px"/>
		<fo:table-column column-number="4" column-width="35px"/>
		<fo:table-column column-number="5" column-width="35px"/>
		<fo:table-column column-number="6" column-width="180px"/>
		<fo:table-column column-number="7" column-width="60px"/>
		<fo:table-header>	
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Case No</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Case Title</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Class</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Hrg Type</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Time Est</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Offences</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Required Judge</xsl:text>						
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./relcj-values"> 
			
				<xsl:variable name="formattedCaseTitle">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./case-title"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="formattedJudge">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./required-judge"/>
					</xsl:call-template>
				</xsl:variable>
			
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./case-number"/>	
						</fo:block>
					</fo:table-cell>	
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="$formattedCaseTitle"/>	
					</fo:block>
					</fo:table-cell>					
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./class-code"/>			
					</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./hearing-type"/>			
						</fo:block>				
					</fo:table-cell>					
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./est"/>			
						</fo:block>				
					</fo:table-cell>
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./charges-info"/>			
						</fo:block>				
					</fo:table-cell> 
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="$formattedJudge"/>			
						</fo:block>				
					</fo:table-cell> 
				</fo:table-row>	
			</xsl:for-each>
	</fo:table-body>
</fo:table>	
</xsl:template>
</xsl:stylesheet>	