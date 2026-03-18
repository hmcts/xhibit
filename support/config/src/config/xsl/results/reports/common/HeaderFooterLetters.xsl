<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template name="Header">
		<xsl:param name="basedir"/>
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
	</xsl:template>
	<xsl:template name="HeaderNoLogo">
		<xsl:param name="basedir"/>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="15.0cm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-body>
				<fo:table-row font-size="7pt">
					<fo:table-cell>
						<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="left" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>The Crown Court</xsl:text>
							<xsl:text>&#xD;&#xA;</xsl:text>
							<xsl:text>at </xsl:text><xsl:value-of select=".//court-name"/>
							<xsl:text>   </xsl:text>                     
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<xsl:value-of select="./basedir"/>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>	
	</xsl:template>
	<xsl:template name="Footer">
		<fo:static-content flow-name="xsl-region-after">
			<xsl:call-template name="FooterContents"/>
		</fo:static-content>
	</xsl:template>	

	<xsl:template name="FooterContents">
		<fo:block font-size="9pt" font-family="sans-serif" font-weight="bold">
			<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
		</fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="350px"/>
			<fo:table-column column-width="185px"/>
			<fo:table-body>
				<fo:table-row font-size="7pt">
					<fo:table-cell>
						<fo:block font-size="9pt" font-family="sans-serif" font-weight="normal" text-align="left">
							<xsl:value-of select=".//court-address"/>
							<xsl:text>   </xsl:text>
							<xsl:text>Tel: </xsl:text>
							<xsl:value-of select=".//court-telephone"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-size="9pt" font-family="sans-serif" font-weight="normal" text-align="right" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>Ref: </xsl:text>
							<xsl:value-of select=".//user-name"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>				
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>