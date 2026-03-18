<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" version="1.0">
    <!-- Import the common List Distribution Letters templates -->
    <xsl:import href="letters_common_en_GB.xsl"/>
    <xsl:include href="firmListLetterHtml_common.xsl"/>
    <!-- Set the output method -->
    <xsl:output method="html" indent="yes"/>
    <!-- Parameter passed in to hold the current date -->
    <xsl:param name="java-date"/>
    <!-- Handle multiple letters -->
    <xsl:template match="xhibit:FirmListLetters">
        <html>
            <body>
                <!-- DISPLAY ENGLISH LETTERS -->
                <!-- Display Solicitor letters in alphabetical order -->
                <xsl:for-each select="xhibit:FirmListLetter/xhibit:Recipient/xhibit:Solicitor">
                    <xsl:sort select="translate(xhibit:FirmName,' ','.')"/>
                    <xsl:if test="position() !=1">
                        <xsl:if test="count(../../../xhibit:FirmListLetter/xhibit:Recipient/xhibit:Solicitor) > 1">
                            <br/>
                            <br/>
                        </xsl:if>
                    </xsl:if>
                    <!-- Call template to set up Page Sequence and display the Firm List Letter Info-->
                    <xsl:for-each select="../..">
                        <xsl:call-template name="DisplayFirmListLetter">
                            <xsl:with-param name="language">en</xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:for-each>
                <br/>
                <br/>
                <!-- Display Prosecution letters in alphabetical order -->
                <xsl:for-each select="xhibit:FirmListLetter/xhibit:Recipient/xhibit:Prosecution">
                    <xsl:sort select="translate(xhibit:ProsecutingOrganisation/xhibit:OrganisationName,' ','.')"/>
                    <xsl:if test="position() !=1">
                        <xsl:if test="count(../../../xhibit:FirmListLetter/xhibit:Recipient/xhibit:Prosecution) > 1">
                            <br/>
                            <br/>
                        </xsl:if>
                    </xsl:if>
                    <!-- Call template to set up Page Sequence and display the Firm List Letter Info-->
                    <xsl:for-each select="../..">
                        <xsl:call-template name="DisplayFirmListLetter">
                            <xsl:with-param name="language">en</xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:for-each>
                <br/>
                <br/>
                <!-- Display Defendant letters in alphabetical order -->
                <xsl:for-each select="xhibit:FirmListLetter/xhibit:Recipient/xhibit:Defendant">
                    <xsl:sort select="translate(xhibit:PersonalDetails/xhibit:Name/xhibit:CitizenNameSurname,' ','.')"/>
                    <xsl:if test="position() !=1">
                        <xsl:if test="count(../../../xhibit:FirmListLetter/xhibit:Recipient/xhibit:Defendant) > 1">
                            <br/>
                            <br/>
                        </xsl:if>
                    </xsl:if>
                    <!-- Call template to set up Page Sequence and display the Firm List Letter Info-->
                    <xsl:for-each select="../..">
                        <xsl:call-template name="DisplayFirmListLetter">
                            <xsl:with-param name="language">en</xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="xhibit:FirmListLetter">
        <html>
            <body>
                <xsl:call-template name="DisplayFirmListLetter">
                    <xsl:with-param name="language">en</xsl:with-param>
                </xsl:call-template>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>