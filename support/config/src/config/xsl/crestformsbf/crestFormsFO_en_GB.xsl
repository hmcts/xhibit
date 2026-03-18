<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:crestformsbf="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="crestForm_common_en_GB.xsl"/>
	<xsl:import href="crestFormBFO_en_GB.xsl"/>
	<xsl:import href="crestFormCFO_en_GB.xsl"/>
	<xsl:import href="crestFormDFO_en_GB.xsl"/>
	<xsl:import href="crestFormEFO_en_GB.xsl"/>
	<xsl:import href="crestFormFFO_en_GB.xsl"/>
	<xsl:import href="crestFormFMiscFO_en_GB.xsl"/>
	<!-- Common Crest Forms B-F Table Column sizes used to display Crest Forms Title and Defendant Details -->	
	<xsl:variable name="Col1_Total">115mm</xsl:variable>
	<xsl:variable name="Col2_Total">75mm</xsl:variable>
	<xsl:variable name="Col1_Inner">113mm</xsl:variable>
	<xsl:variable name="Col2_Inner">73mm</xsl:variable>

	<xsl:template match="crestformsbf:CrestForms">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="all" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
					<fo:region-body margin-top="10mm" margin-bottom="10mm"/>
					<fo:region-before extent="10mm" region-name="header-all"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="all" format="1">
				<fo:static-content flow-name="header-all">
					<fo:block text-align="right" font-size="10pt">
						Page No: <fo:page-number/> of <fo:page-number-citation ref-id="last-page"/>
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates/>
					<fo:block id="last-page"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
