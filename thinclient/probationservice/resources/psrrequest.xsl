<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
    <!-- ========================= -->
    <!-- root element: projectteam -->
    <!-- ========================= -->
    <xsl:template match="data">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1.5cm" margin-bottom="1.5cm" margin-left="1.5cm" margin-right="1.5cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="12pt" font-family="sans-serif" text-align="center" font-weight="bold" space-after="1mm">
                        <xsl:value-of select="print.title"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block>
                    <fo:table width="100%" table-layout="fixed">
                        <!-- Four columns at 25% each -->
                        <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-after="1mm">
                                        <xsl:value-of select="print.seniorcrowncourtliasonofficer"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-after="1mm">
                                        <xsl:value-of select="print.probationliasondepartment"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="telephone"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationtelephone"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="officename"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="fax"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationfax"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationaddress1"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="email"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-rows-spanned="6" >
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" hyphenate="true" language="en_GB">
                                        <xsl:value-of select="probationemail"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2" column-number="3">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationaddress2"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>                                
                                <fo:table-cell number-columns-spanned="2" column-number="3">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationaddress3"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>                                
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationaddress4"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                 <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationaddresstown"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>                                
                                 <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationcounty"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                 <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="probationpostcode"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.madeby"/>
                        <xsl:value-of select="judgetitle"/>
                        <xsl:value-of select="print.on"/>
                        <xsl:value-of select="creationdate"/>
                        <xsl:value-of select="print.at"/>
                        <xsl:value-of select="courtname"/>
                        <xsl:value-of select="print.crowncourt"/>
                        <xsl:value-of select="print.forhearingon"/>
                        <xsl:value-of select="hearingdate"/>
                    </fo:block>
					<fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
						<xsl:value-of select="print.to"/>
						<xsl:value-of select="recipientoffice"/>
						<xsl:value-of select="print.address"/>
						<xsl:value-of select="recipientaddress"/>
						<xsl:value-of select="fax"/>
						<xsl:value-of select="recipientfax"/>
						<xsl:value-of select="email"/>
						<xsl:value-of select="recipientemail"/>
					</fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" hyphenate="true" language="en_GB">
                        <xsl:value-of select="print.remandedto"/>
                        <xsl:value-of select="defendantlocation"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block>
                    <fo:table width="100%" table-layout="fixed">
                        <!-- Four columns at 25% each -->
                        <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
                        <fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.surname"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="defendantsurname"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.solicitors"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="solicitorname"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.forenames"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="defendantforenames"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="telephone"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="solicitortelephone"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.dob"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="defendantdob"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.probationcontact"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" hyphenate="true" language="en_GB">
                                        <xsl:value-of select="probationcontact"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.age"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:if test="defendantage > 0">
                                            <xsl:value-of select="defendantage"/>
                                        </xsl:if>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="print.homeaddress"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                        <xsl:value-of select="defendantaddress"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" >
                        <xsl:value-of select="print.antecedents"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" hyphenate="true" language="en_GB">
                        <xsl:value-of select="antecedents"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.cps"/>
                        <xsl:value-of select="cpsoffice"/>
                        <xsl:value-of select="print.officeforwarded"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.offences"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="offences"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.codefendants"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="codefendants"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.circumstances"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" hyphenate="true" language="en_GB">
                        <xsl:value-of select="circumstances"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.comments"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" hyphenate="true" language="en_GB">
                        <xsl:value-of select="comments"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.interview"/><xsl:value-of select="available"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.send6copies"/><xsl:value-of select="probationaddress"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.nolaterthan"/><xsl:value-of select="nolaterthan"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="print.letusknow"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-before="10pt" space-after="10pt">
                        <xsl:value-of select="print.signed"/><xsl:value-of select="print.officersname"/><xsl:value-of select="username"/>
                    </fo:block>
                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold">
                        <xsl:value-of select="date"/><xsl:value-of select="currentdate"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader
                            leader-length="100%"
                            leader-pattern="rule"
                            rule-style="solid"
                            rule-thickness="0.1mm"
                            color="black"/>
                    </fo:block>
                    <fo:block>
                    <fo:table width="100%" table-layout="fixed">
				<!-- Two columns at 50% width each -->
				<fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
				<fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" >
                                        <xsl:value-of select="print.to"/>
                                        <xsl:value-of select="recipientoffice"/>
                                        <xsl:value-of select="recipientaddress"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-before="10pt" space-after="10pt">
                                        <xsl:value-of select="print.re"/>
                                        <xsl:value-of select="defendantforenames"/>
                                        <xsl:value-of select="defendantsurname"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-before="10pt" space-after="10pt">
                                        <xsl:value-of select="print.dateline"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-before="10pt" space-after="10pt">
                                        <xsl:value-of select="print.willsupply"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" >
                                        <xsl:value-of select="print.defendantknown"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-before="10pt" space-after="10pt">
                                        <xsl:value-of select="print.signedline"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="10pt" font-family="sans-serif" font-weight="bold" space-before="10pt" space-after="10pt">
                                        <xsl:value-of select="print.officeline"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
