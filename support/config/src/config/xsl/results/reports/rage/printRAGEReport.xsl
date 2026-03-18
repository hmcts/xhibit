<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/rage/printRAGE.xsl"/>
		<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>
	<xsl:template match="RAGEReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21cm" page-height="29.7cm" master-name="portrait">	
					<fo:region-body margin-top="4.5cm" margin-bottom="1.2cm"/>
					<fo:region-before extent="4.5cm"/>
					<fo:region-after extent="1.0cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="portrait">
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="Header"/>
					<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>&#xD;&#xA;</xsl:text>
						<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court-name"/>
					</fo:block>
					<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>Cases Over 'N' Weeks Old With Listing History</xsl:text>
						<xsl:text>&#xD;&#xA;</xsl:text>
					</fo:block>
					<xsl:choose>
						<xsl:when test="./week-to != ''">
					       <fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>Trial Cases between </xsl:text><xsl:value-of select="./week-from"/>
								<xsl:text>&#x00A0;and </xsl:text><xsl:value-of select="./week-to"/>
								<xsl:text>&#x00A0;weeks old</xsl:text>    				
						   </fo:block>	
						</xsl:when>
						<xsl:otherwise>
							<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>&#xD;&#xA;</xsl:text>
								<xsl:text>Trial Cases at least </xsl:text><xsl:value-of select="./week-from"/>
								<xsl:text>&#x00A0;week(s) old</xsl:text>    				
						   </fo:block>	
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="./sitecommited != ''">
						<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>Committed/Sent to </xsl:text><xsl:value-of select="./sitecommited"/>   				
						</fo:block>
					</xsl:if>
					<xsl:if test="./caseclasses != ''">
						<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>Class </xsl:text><xsl:value-of select="./caseclasses"/>   				
						</fo:block>
					</xsl:if>
				</fo:static-content>
				
				<xsl:call-template name="Footer"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="RAGE"/>
					
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>