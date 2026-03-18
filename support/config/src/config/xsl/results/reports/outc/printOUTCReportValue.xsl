<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="OUTCReportValues">
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="60px"/>
		<fo:table-column column-number="2" column-width="10px"/>
		<fo:table-column column-number="3" column-width="110px"/>
		<fo:table-column column-number="4" column-width="80px"/>
		<fo:table-column column-number="5" column-width="40px"/>
		<fo:table-column column-number="6" column-width="50px"/>
		<fo:table-column column-number="7" column-width="50px"/>
		<fo:table-column column-number="8" column-width="40px"/>
		<fo:table-column column-number="9" column-width="60px"/>
		<fo:table-column column-number="10" column-width="40px"/>
		<fo:table-header>	
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell number-columns-spanned="2" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt" text-align="left">
						<xsl:text>Case No</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt" text-align="left">
						<xsl:text>Case Title</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt" text-align="left">
						<xsl:text>Committed/Sent</xsl:text>
					</fo:block>	
				</fo:table-cell>	
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt" text-align="left">
						<xsl:text>Class</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt" text-align="left">
						<xsl:text>Hrg Type</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>L.O.Est</xsl:text>
					</fo:block>
				</fo:table-cell>	
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Group</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>FirstNAD</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Listed?</xsl:text>						
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			 <xsl:for-each select="./outstanding-cases-values">
			 
				<xsl:variable name="formattedCaseTitle">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./case-title"/>
					</xsl:call-template>
				</xsl:variable>
			 
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal" text-align="left">
							<xsl:value-of select="./case-number"/>	
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal" text-align="left">
							<xsl:value-of select="./custody-case"/>	
						</fo:block>
					</fo:table-cell>					
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block wrap-option="wrap" font-size="9pt" font-weight="normal" text-align="left" >
							<xsl:value-of select="$formattedCaseTitle"/>		
						</fo:block>
						<fo:block space-before="1pt" space-after="1pt" font-size="9pt" font-weight="normal" text-align="left" >
							<xsl:choose>
								<xsl:when test="normalize-space(./juvenile) != '' and normalize-space(./monitoring-category-code) != ''">
									<xsl:value-of select="./juvenile"/> <xsl:text>;</xsl:text> <xsl:value-of select="./monitoring-category-code"/>
								</xsl:when>
								<xsl:when test="normalize-space(./juvenile) != '' and normalize-space(./monitoring-category-code) = ''">
									<xsl:value-of select="./juvenile"/>
								</xsl:when>
								<xsl:when test="normalize-space(./juvenile) = '' and normalize-space(./monitoring-category-code) != ''">
									<xsl:value-of select="./monitoring-category-code"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text> </xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>		
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal" >
							<xsl:value-of select="./commited-sent"/>			
					</fo:block>
					</fo:table-cell>			
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal" text-align="left">
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
							<xsl:value-of select="./loest"/>					
						</fo:block>
					</fo:table-cell>					
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
						    <xsl:value-of select="./case-group-number"/>							
						</fo:block>
					</fo:table-cell>	
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./first-nad"/>					
					</fo:block>
					</fo:table-cell>		
					<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./listed"/>			
						</fo:block>				
					</fo:table-cell>
				</fo:table-row>	
				
				<xsl:for-each select="./outstandingcasenotes">
				
					<xsl:variable name="formattedNoteText">
						<xsl:call-template name="format-text-for-wrapping">
							<xsl:with-param name="str" select="./diary-note-text"/>
						</xsl:call-template>
					</xsl:variable>
				
					<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
						<fo:table-cell number-columns-spanned="2" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<fo:block font-size="9pt" font-weight="normal">
								<xsl:text> </xsl:text>	
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="8" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
							<fo:block space-before="1pt" space-after="1pt" font-size="9pt" font-weight="normal" text-align="left" >
								<xsl:value-of select="./note-prefix"/>  <xsl:text> </xsl:text> 
								<xsl:value-of select="./note-date"/>  <xsl:text> </xsl:text> 
								<xsl:value-of select="$formattedNoteText"/>
								<xsl:if test="./diary-date">
									<xsl:text> [</xsl:text> <xsl:value-of select="./diary-date"/>  <xsl:text>]</xsl:text>
								</xsl:if>
							</fo:block>		
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
	
			</xsl:for-each>				
		</fo:table-body>					
	</fo:table>			
</xsl:template>
</xsl:stylesheet>