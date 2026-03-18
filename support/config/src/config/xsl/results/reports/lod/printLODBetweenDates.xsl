<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="LOD">
	
	<xsl:for-each select="./lod-diary-info">
	
	<fo:block space-before="1pt" space-after="1pt" font-weight="bold">
		<xsl:value-of select="./diary-date"/>
	</fo:block>
	
	<fo:table border="none" table-layout="fixed" font-size="11pt">
		<fo:table-column column-number="1" column-width="100px"/>
		<fo:table-column column-number="2" column-width="300px"/>
		<fo:table-column column-number="3" column-width="50px"/>
		
		<fo:table-body>
				<xsl:for-each select="./cases">
					<xsl:variable name="formattedCaseTitle">
						<xsl:call-template name="format-text-for-wrapping">
							<xsl:with-param name="str" select="./case-title"/>
						</xsl:call-template>
					</xsl:variable>
				
					<fo:table-row border="none" space-before.optimum="12pt">
						<xsl:if test="./case-number or not(./case-number = '')">
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" >
								<fo:block font-weight="normal">
									<xsl:value-of select="./case-number"/>
								</fo:block>
							</fo:table-cell>
						</xsl:if >
						<xsl:if test="./case-title or not(./case-title = '')">
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" >
								<fo:block  wrap-option="wrap" space-before="1pt" space-after="1pt" font-weight="bold">
									<xsl:value-of select="$formattedCaseTitle"/>
								</fo:block>
							</fo:table-cell>
						</xsl:if >
					</fo:table-row>
				
					<xsl:for-each select="./diaries">
						<xsl:variable name="formattedNoteText">
							<xsl:call-template name="format-text-for-wrapping">
								<xsl:with-param name="str" select="./diary-note-text"/>
							</xsl:call-template>
						</xsl:variable>
					
						<fo:table-row border="none" space-before.optimum="12pt">
							<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" >
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal"/>
							</fo:table-cell>
							<xsl:if test="./diary-note-text or not(./diary-note-text = '')">
								<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" >
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:value-of select="$formattedNoteText"/>
									</fo:block>
								</fo:table-cell>
							</xsl:if>
							<xsl:if test="./creation-date or not(./creation-date = '')">
								<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" >
									<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
										<xsl:value-of select="./creation-date"/>
									</fo:block>
								</fo:table-cell>
							</xsl:if>
						</fo:table-row>                    
					</xsl:for-each>
				</xsl:for-each>
		</fo:table-body>
	</fo:table>
	<fo:block break-after="page"/>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>