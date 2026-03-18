<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
    <xsl:text>Jury Sworn In:
    </xsl:text>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror1!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror1, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo1)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror2!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror2, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo2)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror3!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror3, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo3)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror4!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror4, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo4)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror5!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror5, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo5)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror6!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror6, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo6)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror7!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror7, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo7)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror8!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror8, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo8)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror9!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror9, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo9)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror10!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror10, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo10)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror11!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror11, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo11)"/>.
    </xsl:if>
    <xsl:if test="event/E20902_Jury_Sworn_In/E20902_JSIO_juror12!=''"><xsl:value-of select="concat('- ', event/E20902_Jury_Sworn_In/E20902_JSIO_juror12, ' ', event/E20902_Jury_Sworn_In/E20902_JSIO_jurorNo12)"/>.
    </xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
