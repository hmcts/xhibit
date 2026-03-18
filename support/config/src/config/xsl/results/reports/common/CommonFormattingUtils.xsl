<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

	<xsl:variable name="whitespace" select="'&#09;&#10;&#13; '" />

	<xsl:template name="format-text-for-wrapping">
		<xsl:param name="str" select="."/>
		<xsl:param name="delim" select="' '"/>
		<xsl:param name="maxlen" select="15"/>
		
		<xsl:variable name="formattedtext">
			<xsl:call-template name="split">
				<xsl:with-param name="pText" select="$str"/>
				<xsl:with-param name="pDelimiter" select="$delim"/>
				<xsl:with-param name="pMaxlen" select="$maxlen"/>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:value-of select="normalize-space($formattedtext)"/>
		
	</xsl:template>
	
	<xsl:template name="format-text-for-wrapping2">
		<xsl:param name="str" select="."/>
		<xsl:param name="delim" select="' '"/>
		<xsl:param name="maxlen" select="15"/>
		
		<xsl:variable name="formattedtext">
			<xsl:call-template name="split">
				<xsl:with-param name="pText" select="$str"/>
				<xsl:with-param name="pDelimiter" select="$delim"/>
				<xsl:with-param name="pMaxlen" select="$maxlen"/>
			</xsl:call-template>
		</xsl:variable>
		
		<!-- Remove trailing and leading white-space.  Cannot use normalize-space as it strips out
			 line breaks in the middle of the string -->
		<xsl:call-template name="string-trim">
			<xsl:with-param name="string" select="$formattedtext"/>
		</xsl:call-template>
		
	</xsl:template>

	<xsl:template match="text()" name="split">
		<xsl:param name="pText" select="."/>
		<xsl:param name="pDelimiter" select="' '"/>
		<xsl:param name="pMaxlen" select="15"/>
		<xsl:if test="string-length($pText)">
			<xsl:variable name="token" select="substring-before(concat($pText,$pDelimiter),$pDelimiter)"/>
			<xsl:choose>
				<xsl:when test="string-length($token) &gt; $pMaxlen">
					<xsl:call-template name="intersperse-with-zero-spaces">
						<xsl:with-param name="str" select="$token"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$token"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text> </xsl:text>
			
			<xsl:call-template name="split">
				<xsl:with-param name="pText" select="substring-after($pText, $pDelimiter)"/>
				<xsl:with-param name="pDelimiter" select="$pDelimiter"/>
				<xsl:with-param name="pMaxlen" select="$pMaxlen"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="intersperse-with-zero-spaces">
		<xsl:param name="str"/>
		<xsl:variable name="spacechars">
			&#x9;&#xA;
			&#x2000;&#x2001;&#x2002;&#x2003;&#x2004;&#x2005;
			&#x2006;&#x2007;&#x2008;&#x2009;&#x200A;&#x200B;
		</xsl:variable>

		<xsl:if test="string-length($str) &gt; 0">
			<xsl:variable name="c1" select="substring($str, 1, 1)"/>
			<xsl:variable name="c2" select="substring($str, 2, 1)"/>

			<xsl:value-of select="$c1"/>
			<xsl:if test="$c2 != '' and not(contains($spacechars, $c1) or $c1 = ' ' or contains($spacechars, $c2) or $c2 = ' ' )">
				<xsl:text>&#x200B;</xsl:text>
			</xsl:if>

			<xsl:call-template name="intersperse-with-zero-spaces">
				<xsl:with-param name="str" select="substring($str, 2)"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>	

	<!-- Strips trailing whitespace characters from 'string' -->
	<xsl:template name="string-rtrim">
		<xsl:param name="string" />
		<xsl:param name="trim" select="$whitespace" />

		<xsl:variable name="length" select="string-length($string)" />

		<xsl:if test="$length &gt; 0">
			<xsl:choose>
				<xsl:when test="contains($trim, substring($string, $length, 1))">
					<xsl:call-template name="string-rtrim">
						<xsl:with-param name="string" select="substring($string, 1, $length - 1)" />
						<xsl:with-param name="trim"   select="$trim" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$string" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- Strips leading whitespace characters from 'string' -->
	<xsl:template name="string-ltrim">
		<xsl:param name="string" />
		<xsl:param name="trim" select="$whitespace" />

		<xsl:if test="string-length($string) &gt; 0">
			<xsl:choose>
				<xsl:when test="contains($trim, substring($string, 1, 1))">
					<xsl:call-template name="string-ltrim">
						<xsl:with-param name="string" select="substring($string, 2)" />
						<xsl:with-param name="trim"   select="$trim" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$string" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- Strips leading and trailing whitespace characters from 'string' -->
	<xsl:template name="string-trim">
		<xsl:param name="string" />
		<xsl:param name="trim" select="$whitespace" />
		<xsl:call-template name="string-rtrim">
			<xsl:with-param name="string">
				<xsl:call-template name="string-ltrim">
					<xsl:with-param name="string" select="$string" />
					<xsl:with-param name="trim"   select="$trim" />
				</xsl:call-template>
			</xsl:with-param>
			<xsl:with-param name="trim"   select="$trim" />
		</xsl:call-template>
	</xsl:template>

</xsl:stylesheet>	