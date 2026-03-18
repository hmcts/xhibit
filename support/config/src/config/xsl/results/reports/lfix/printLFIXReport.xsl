<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/lfix/printLFIX.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooterLetters.xsl"/>
	<xsl:param name="basedir"/>
	<xsl:template match="LFIXReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="0.5cm" page-width="21.0cm" page-height="29.7cm" master-name="portrait">	
					<fo:region-body margin-top="3.4cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="3.4cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
		
			<fo:page-sequence master-reference="portrait">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="right" font-size="9pt" font-family="serif" >
						<xsl:text>Page </xsl:text>
							<fo:page-number/>
						<xsl:text> of </xsl:text>
						<fo:page-number-citation ref-id="theEnd"/>
					</fo:block>
					<xsl:call-template name="HeaderNoLogo">
						<xsl:with-param name="basedir" select="$basedir"/>
					</xsl:call-template>
					<fo:block font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>&#xD;&#xA;</xsl:text>
						<xsl:text>List of Fixed Dates (Criminal)</xsl:text>
					</fo:block>
					<fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
						<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
					</fo:block>
				</fo:static-content>
				
				<xsl:call-template name="Footer"/>
					
				<fo:flow flow-name="xsl-region-body">
					<fo:list-block>
						<fo:list-item>
						  <fo:list-item-label>
						    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						      <xsl:text>&#x25A0;</xsl:text>
						    </fo:block>
						  </fo:list-item-label>
						  <fo:list-item-body start-indent="10pt">
							<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							  <xsl:text>The undermentioned cases have been listed for hearing on the dates shown.</xsl:text>
						    </fo:block>
						  </fo:list-item-body>
						</fo:list-item>
						
						<fo:list-item>
						  <fo:list-item-label>
						    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						      <xsl:text>&#x25A0;</xsl:text>
						    </fo:block>
						  </fo:list-item-label>
						  <fo:list-item-body start-indent="10pt">
							<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							  <xsl:text>The Listing Officer must be informed immediately of any matters affecting the hearing.</xsl:text>
						    </fo:block>
						  </fo:list-item-body>
						</fo:list-item>
						
						<fo:list-item>
						  <fo:list-item-label>
						    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						      <xsl:text>&#x25A0; </xsl:text>
						    </fo:block>
						  </fo:list-item-label>
						  <fo:list-item-body start-indent="10pt">
							<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							  <xsl:text>The Prosecuting Solicitor is the Crown Prosecution Service unless otherwise stated.</xsl:text>
						    </fo:block>
						  </fo:list-item-body>
						</fo:list-item>
						
						<fo:list-item>
						  <fo:list-item-label>
						    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						      <xsl:text>* </xsl:text>
						    </fo:block>
						  </fo:list-item-label>
						  <fo:list-item-body start-indent="10pt">
							<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						      <xsl:text>Denotes a defendant in custody.</xsl:text>
							  <xsl:text>&#xD;&#xA;&#xD;&#xA;</xsl:text>
						    </fo:block>
						  </fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
					
					<fo:block font-size="10pt" font-family="sans-serif" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>&#xD;&#xA;</xsl:text>
					</fo:block>
					
					<xsl:call-template name="LFIX"/>
					<fo:block font-size="10pt" font-family="sans-serif" text-align="center" space-before.optimum="20pt">
						<xsl:text>End of Report</xsl:text>
					</fo:block>	
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>