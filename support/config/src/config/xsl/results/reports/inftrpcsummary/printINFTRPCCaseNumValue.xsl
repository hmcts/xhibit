<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:template name="INFTRPCSummaryReport"> 
	<fo:block>
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="30px"/>
		<fo:table-column column-number="2" column-width="450px"/>
		<fo:table-column column-number="3" column-width="30px"/>
		<fo:table-header>
			<fo:table-row>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" number-columns-spanned="3">
					<fo:block space-before="5pt" font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="left">
						<xsl:text>REASONS FOR CRACKED TRIALS:</xsl:text>    
					</fo:block>
				</fo:table-cell>			
			</fo:table-row>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" number-columns-spanned="3">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text> </xsl:text>						
					</fo:block>
				</fo:table-cell>			
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./case-description-values[./trial-code-type = 'C']"> 
			<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:value-of select="./code"/>		
					</fo:block>
				</fo:table-cell>		
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal" >
						<xsl:value-of select="./description"/>
					</fo:block>				
				</fo:table-cell> 
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:value-of select="./total"/>	
					</fo:block>
				</fo:table-cell>
			</fo:table-row>		
			<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:text> </xsl:text>	
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt" number-columns-spanned="2">
					<fo:block space-before="1pt" space-after="1pt" font-size="9pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:for-each select="./casenumbers">
							<xsl:value-of select="./case-number"/>
							<xsl:if test="position() mod 7 = 0">
								<xsl:text>&#xD;&#xA;</xsl:text>
							</xsl:if>
							<xsl:if test="position() mod 7 != 0">
								<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</fo:block>		
				</fo:table-cell>				
			</fo:table-row>	
			</xsl:for-each>
		</fo:table-body>
	</fo:table>	
	</fo:block>
					
	<fo:block>
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="30px"/>
		<fo:table-column column-number="2" column-width="450px"/>
		<fo:table-column column-number="3" column-width="30px"/>
		<fo:table-header>
			<fo:table-row>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" number-columns-spanned="3">
					<fo:block space-before="5pt" font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="left">
						<xsl:text>REASONS FOR INEFFECTIVE TRIALS:</xsl:text>    
					</fo:block>
				</fo:table-cell>			
			</fo:table-row>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" number-columns-spanned="3">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text> </xsl:text>						
					</fo:block>
				</fo:table-cell>			
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./case-description-values[./trial-code-type = 'I']"> 
			<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:value-of select="./code"/>		
					</fo:block>
				</fo:table-cell>		
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal" >
						<xsl:value-of select="./description"/>
					</fo:block>				
				</fo:table-cell> 
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:value-of select="./total"/>	
					</fo:block>
				</fo:table-cell>
			</fo:table-row>		
			<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:text> </xsl:text>	
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt" number-columns-spanned="2">
					<fo:block space-before="1pt" space-after="1pt" font-size="9pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:for-each select="./casenumbers">
							<xsl:value-of select="./case-number"/>
							<xsl:if test="position() mod 7 = 0">
								<xsl:text>&#xD;&#xA;</xsl:text>
							</xsl:if>
							<xsl:if test="position() mod 7 != 0">
								<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</fo:block>		
				</fo:table-cell>				
			</fo:table-row>	
			</xsl:for-each>
		</fo:table-body>
	</fo:table>		
	</fo:block>
						
	<fo:block>
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="30px"/>
		<fo:table-column column-number="2" column-width="450px"/>
		<fo:table-column column-number="3" column-width="30px"/>
		<fo:table-header>
			<fo:table-row>
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" number-columns-spanned="3">
					<fo:block space-before="5pt" font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="left">
						<xsl:text>EFFECTIVE TRIALS:</xsl:text>    
					</fo:block>
				</fo:table-cell>			
			</fo:table-row>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" number-columns-spanned="3">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text> </xsl:text>						
					</fo:block>
				</fo:table-cell>			
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./case-description-values[./trial-code-type = 'E']"> 
			<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:value-of select="./code"/>		
					</fo:block>
				</fo:table-cell>		
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal" >
						<xsl:value-of select="./description"/>
					</fo:block>				
				</fo:table-cell> 
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:value-of select="./total"/>	
					</fo:block>
				</fo:table-cell>
			</fo:table-row>		
			<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
					<fo:block font-size="9pt" font-weight="normal">
						<xsl:text> </xsl:text>	
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt" number-columns-spanned="2">
					<fo:block space-before="1pt" space-after="1pt" font-size="9pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:for-each select="./casenumbers">
							<xsl:value-of select="./case-number"/>
							<xsl:if test="position() mod 7 = 0">
								<xsl:text>&#xD;&#xA;</xsl:text>
							</xsl:if>
							<xsl:if test="position() mod 7 != 0">
								<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</fo:block>		
				</fo:table-cell>				
			</fo:table-row>	
			</xsl:for-each>
		</fo:table-body>
	</fo:table>		
	</fo:block>
</xsl:template>
</xsl:stylesheet>