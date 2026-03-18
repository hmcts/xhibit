<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit">
    <xsl:template match="xhibit:XTable">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="29.7cm" page-height="21.0cm" master-name="simple">
                    <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
                    <fo:region-before extent="1cm"/>
                    <fo:region-after extent="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block text-align="end" font-size="9pt" font-family="serif" line-height="14pt">
                        <xsl:text> XHIBIT 2 - Page</xsl:text>
                        <fo:page-number/>
                        <xsl:text> of </xsl:text>
                        <fo:page-number-citation ref-id="theEnd"/>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="12pt" font-family="sans-serif" space-after.optimum="15pt" font-weight="bold" text-align="left">
                        <xsl:value-of select="./xhibit:XTableTitle"/>
                    </fo:block>
                    <fo:block>
                    <fo:table inline-progression-dimension.minimum="100%" table-layout="fixed">
                        <xsl:apply-templates select="./xhibit:XTableHeader"/>
                        <xsl:apply-templates select="./xhibit:XTableBody"/>
                    </fo:table>
                    </fo:block>
                    <fo:block id="theEnd"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="xhibit:XTableHeader">
        <xsl:for-each select="./xhibit:XTableRow/xhibit:XTableCell">
            <xsl:element name="fo:table-column">
                <xsl:attribute name="column-number">
                     <xsl:value-of select="position()"/>
                </xsl:attribute>
            </xsl:element>
        </xsl:for-each>
        <fo:table-header>
            <xsl:for-each select="./xhibit:XTableRow">
                <fo:table-row background-color="#EEEEEE">
                    <xsl:for-each select="./xhibit:XTableCell">
                        <fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
                            <fo:block font-weight="bold" font-size="9pt">
                                <xsl:value-of select="."/>
                            </fo:block>
                        </fo:table-cell>
                    </xsl:for-each>
                </fo:table-row>
            </xsl:for-each>
        </fo:table-header>
    </xsl:template>
    <xsl:template match="xhibit:XTableBody">
        <fo:table-body>
            <xsl:for-each select="./xhibit:XTableRow">
                <fo:table-row>
                    <xsl:for-each select="./xhibit:XTableCell">
                        <fo:table-cell border-style="ridge" border-width="0.1mm" padding="1mm">
                            <fo:block font-size="9pt">
                                <xsl:value-of select="."/>
                            </fo:block>
                        </fo:table-cell>
                    </xsl:for-each>
                </fo:table-row>
            </xsl:for-each>
        </fo:table-body>
    </xsl:template>
</xsl:stylesheet>
