<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

    <xsl:template name="DefOnCaseAuthorisationFailures">
      <xsl:if test="@def-on-case-failure='true'">
          <xsl:for-each select="./defendant-on-case-print-values">
            <fo:block font-size="12pt" font-family="sans-serif"  space-before.optimum="15pt" space-after.optimum="15pt" font-weight="bold" text-align="left">
                <xsl:text>Defendant Name: </xsl:text><xsl:value-of select="./defendant-full-name"/>
            </fo:block>
			<fo:block>
            <fo:table table-layout="fixed">
                <fo:table-column column-number="1" column-width="50mm"/>
                <fo:table-column column-number="2"/>
                <fo:table-header>
                    <fo:table-row background-color="#EEEEEE">
                        <fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
                            <fo:block font-weight="bold" font-size="9pt">
                                <xsl:text>Reason for Failure</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
                            <fo:block font-weight="bold" font-size="9pt">
                                <xsl:text>Details</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <xsl:for-each select="./defendant-failure-reasons">
                        <fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
                            <fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
                              <!-- failure reason column -->
                              <fo:block font-size="9pt" font-weight="normal">
                                  <xsl:value-of select="./column01"/>
                              </fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
                            <!--  failure detail column -->
                            <fo:block space-before="1pt" space-after="1pt" font-weight="normal">
                                <xsl:value-of select="./column02"/>
                            </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                    
                    </xsl:for-each>
                </fo:table-body>
            </fo:table>
			</fo:block>
          </xsl:for-each>
      </xsl:if>
    </xsl:template>
</xsl:stylesheet>
