<?xml version="1.0"?>  
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="remand_details">
<HTML>
<BODY style="font-family: Times new Roman; font-size: 11pt;">
	<table width="100%">
	<tr><td style="font-family: Arial; font-size: 25pt;">In the Crown Court</td></tr>
	<tr><td>at <xsl:value-of select="court_location"/></td></tr>
	<tr><td align="right" height="30">Case No: <xsl:value-of select="case_no"/></td></tr>
	<tr><td align="right" height="30">Court Code: <xsl:value-of select="court_code"/></td></tr>
	</table>	
	<hr bordercolor="#000000" noshade="true" width="1200"/>	
	<table style="font-family: Arial; font-size: 18pt;" width="100%"><tr><td align="center" height="50">Remand Order</td></tr></table>	
	<table width="100%" border="0">
	<tr><td  colspan="3" height="30">It was ordered</td></tr>
	<tr><td colspan="3" height="30">on <xsl:value-of select="ordered_date"/></td></tr>
	<tr><td colspan="3" height="30">that the <xsl:value-of select="corespondent"/></td></tr>
	<tr>
		<td width="4%" height="30">&#32;</td>
		<td width="48%" height="30"><xsl:value-of select="name"/></td>
		<td width="48%" align="right" height="30">Date of birth: <xsl:value-of select="dob"/></td>
	</tr>
	<tr><td colspan="3">be remanded in custody at</td></tr>
	<tr>
		<td width="4%" height="30"></td>
		<td colspan="2" height="30"><xsl:value-of select="remanded_location"/></td>
	</tr>
	<xsl:apply-templates select="report"></xsl:apply-templates>
	<xsl:variable name="cdYes">and on: <xsl:value-of select="court_date"/>
	</xsl:variable>
	<xsl:variable name="cdNo">
		and on a date and at a time to be notified
	</xsl:variable>
	<xsl:call-template name="display">
		<xsl:with-param name="data" select="court_date"/>
		<xsl:with-param name="yes" select="$cdYes"/>
		<xsl:with-param name="no" select="$cdNo"/>
	</xsl:call-template>
	<tr><td colspan="3">be brought before the <xsl:value-of select="court"/> sitting at: <xsl:value-of select="court_location"/></td></tr>
	<tr><td colspan="3" height="30">or any other place that may be notified.</td></tr>
	<tr><td colspan="3" height="30">The <xsl:value-of select="corespondent"/></td></tr>
	<tr><td colspan="3" height="30">was <xsl:value-of select="action"/> to the Crown Court on <xsl:value-of select="pretrial_date"/></td></tr>
	
	<xsl:variable name="co"><xsl:value-of select="committed_options"/></xsl:variable>
	<xsl:variable name="cl"><xsl:value-of select="court_location"/></xsl:variable>
	<xsl:apply-templates select="action">
		<xsl:with-param name="coV" select="$co"/>
		<xsl:with-param name="clV" select="$cl"/>
	</xsl:apply-templates>
	
	<xsl:variable name="id"><xsl:value-of select="indictment_date"/></xsl:variable>
	<xsl:variable name="td"><xsl:value-of select="transfer_date"/></xsl:variable>
	<xsl:apply-templates select="offence">
		<xsl:with-param name="idV" select="$id"/>
		<xsl:with-param name="tdV" select="$td"/>
	</xsl:apply-templates>
	<tr><td colspan="3" align="right" height="30">&#32;</td></tr>
	<tr><td colspan="3" align="right" height="30"><xsl:value-of select="signature"/></td></tr>
	<tr><td colspan="3" align="right" height="30">An Officer of the Crown Court</td></tr>
	<tr ><td colspan="3" align="right" height="30">Dated: <xsl:value-of select="signature_date"/></td></tr>
	</table>
</BODY>
</HTML>

</xsl:template>
<xsl:template match="report">
	<tr>
		<td colspan="3" height="30">for a report on <xsl:value-of select="."/></td>
	</tr>
</xsl:template>

<xsl:template match="offence">
	<tr>
		<td colspan="3" height="30">is before the Court</td>
	</tr>
</xsl:template>

<xsl:template name="display">
	<xsl:param name="data" select="'Not Available'"/>
	<xsl:param name="yes" select="'Not Available'"/>
	<xsl:param name="no" select="'Not Available'"/>
	<xsl:choose>
	<xsl:when test="$data!='NaN'">
	<tr>
		<td colspan="3" height="30"><xsl:value-of select="$yes"/></td>
	</tr>
	</xsl:when>	
	<xsl:otherwise>
		<tr>
			<td colspan="3" height="30"><xsl:value-of select="$no"/></td>
		</tr>
	</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template match="offence">
	<xsl:param name="idV" select="'Not Available'"/>
	<xsl:param name="tdV" select="'Not Available'"/>
	<tr><td colspan="3" height="30">is before the Court</td></tr>
	<xsl:if test=".='indicted for'">
		<tr>
			<td width="4%" height="30">&#32;</td>
			<td colspan="2" height="30">on a Voluntary Bill of Indictment dated <xsl:value-of select="$idV"/></td>
		</tr>
	</xsl:if>
	<xsl:if test=".='convicted of'">
		<tr>
			<td width="4%" height="30">&#32;</td>
			<td colspan="2" height="30">on a Certificate of Transfer dated <xsl:value-of select="$tdV"/></td>
		</tr>
	</xsl:if>
	<xsl:if test=".='charged with'">
		<tr>
			<td width="4%" height="30">&#32;</td>
			<td colspan="2" height="30">as a result of an appeal by the Prosecution against the grant of bail</td>
		</tr>
	</xsl:if>
	<tr><td colspan="3" height="30">and has been <xsl:value-of select="."/> crime</td></tr>
</xsl:template>

<xsl:template match="action">
	<xsl:param name="coV" select="'Not Available'"/>
	<xsl:param name="clV" select="'Not Available'"/>
	<xsl:if test=".='committed'">
		<tr>
			<td colspan="3" height="30">by the <xsl:value-of select="$coV"/> Margistrates' Court</td>
		</tr>
	</xsl:if>
	<xsl:if test=".='sent'">
		<tr>
			<td colspan="3" height="30">by the <xsl:value-of select="$clV"/> Margistrates' Court</td>
		</tr>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>
















