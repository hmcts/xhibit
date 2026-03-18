<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
    <!-- ========================= -->
    <!-- root element: data -->
    <!-- ========================= -->
    <xsl:template match="data">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="297mm" page-width="210mm" margin-top="20mm" margin-bottom="20mm" margin-left="10mm" margin-right="10mm">
                    <fo:region-body margin-top="40mm"/>
                    <fo:region-before extent="40mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">

            <fo:static-content flow-name="xsl-region-before">
                <fo:block text-align="start" font-size="10pt" font-family="serif" line-height="1em + 2pt">
                    <fo:block font-family="Helvetica" font-size="13pt">
                        <fo:block font-size="18pt" font-weight="bold">
                            The <xsl:value-of select="courtprefix"/>
                        </fo:block>
                        at
                        <xsl:value-of select="courtname"/>
                    </fo:block>
                </fo:block>
                <fo:block space-after="8pt" text-align="center" font-size="15pt" font-weight="bold">
                    Witness Details
                </fo:block>
            </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
					<fo:block>
                    <fo:table width="100%"  space-after="2mm" table-layout="fixed">
				<!-- Five columns of 20% each -->
                        <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="5" column-width="proportional-column-width(1)"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="court"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="defendantname"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="casenumber"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="policeofficer"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="cpscaseworker"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="courtname"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm" hyphenate="true" language="en">
                                        <xsl:value-of select="defendantsValue"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="caseValue"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="police"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border-style="solid" border-width=".1mm">
                                    <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                        <xsl:value-of select="caseworker"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                    <xsl:choose>
                        <xsl:when test="witness">
							<fo:block>
                            <fo:table width="100%" table-layout="fixed">
                                <fo:table-column column-width="33mm"/> <!-- Col 1 Trial Session Details -->
                                <fo:table-column column-width="47mm"/> <!-- Col 2 Witness Name -->
                                <fo:table-column column-width="25mm"/> <!-- Col 3 Witness Status -->
                                <fo:table-column column-width="14mm"/> <!-- Col 4 Witness Age -->
                                <fo:table-column column-width="15mm"/> <!-- Col 5 Witness Due At -->
                                <fo:table-column column-width="16mm"/> <!-- Col 6 Witness Arrived -->
                                <fo:table-column column-width="20mm"/> <!-- Col 7 Witness Release --> 
                                <fo:table-column column-width="20mm"/> <!-- Col 8 Witness Total Time -->
                                <fo:table-body>
                                    <fo:table-row>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="trialsession"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="name"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="status"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="age"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="dueat"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="arrived"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="released"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid" border-width=".1mm">
                                            <fo:block text-align="center" font-size="10pt" font-family="Verdana, Helvetica, Times" font-weight="bold" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                <xsl:value-of select="totaltime"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                    <xsl:for-each select="witness">
                                        <fo:table-row>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm" hyphenate="true" language="en">
                                                    <xsl:value-of select="dayno"/>
                                                    <xsl:text> - </xsl:text>
                                                    <xsl:value-of select="appearancedate"/>
                                                    <xsl:text> - </xsl:text>
                                                    <xsl:value-of select="sessiontype"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm" hyphenate="true" language="en">
                                                    <xsl:value-of select="name"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                    <xsl:value-of select="status"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                    <xsl:if test="age>0">
                                                        <xsl:value-of select="age"/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                    <!-- Not needed, only display time element part <xsl:value-of select="dueat"/> -->
                                                    <xsl:value-of select="dueattime"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                    <xsl:value-of select="arrived"/>
                                                    <xsl:value-of select="arrivedtime"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                    <xsl:value-of select="released"/>
                                                    <xsl:value-of select="releasedtime"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border-style="solid" border-width=".1mm">
                                                <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                                    <xsl:value-of select="totaltimehr"/>
                                                    <xsl:value-of select="../hr"/>
                                                    <xsl:value-of select="totaltimemin"/>
                                                    <xsl:value-of select="../min"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </fo:table-body>
                            </fo:table>
							</fo:block>
                        </xsl:when>
                        <xsl:otherwise>
                            <fo:block font-size="10pt" font-family="Verdana, Helvetica, Times" start-indent="1mm" end-indent="1mm" space-before="1mm">
                                <xsl:value-of select="nowitnesses"/>
                            </fo:block>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
