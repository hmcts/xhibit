<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>
<xsl:template match="/">
<HTML>
<HEAD>
<TITLE>MSeries Source Code Zipfiles</TITLE>
<LINK REL ="stylesheet" TYPE="text/css" HREF="cw-stylesheet.css" TITLE="Style"/>
</HEAD>
<BODY>
<H2>MSeries Source Code Zipfiles</H2>
<P>
Choose the source version that you need, usually the latest and save it to a new dirctory, I suggest mseries. Unzip it using Winzip, GNUZIP (especially on Mac) or jar/tar, it will create a /source and /scripts directory. In the scripts directory you will find my ant build script, build.xml. You get get ant from <a href="http://jakarta.apache.org/ant">http://jakarta.apache.org/ant</a>. Get full details of the tasks from the script itself, the most important ones are:
</P>

<ul>
<li>comp - Compile the sources, produces a directory called classes</li>
<li>jar - Generated the jar file in a directory called jars</li>
<li>javadocs - Generated the javadocs in a directory called javadocs</li>
<li>run - Runs my demo application</li>
</ul>

<TABLE>
    <xsl:for-each select="source-zips/file-list/file">
    <xsl:sort select="name" order="descending" />
    <TR>
        <TD>
            <IMG src="compressed.gif" alt="WinZip File" />
        </TD>
        <TD>
            <xsl:element name="A">
                <xsl:attribute name="href">
                    <xsl:value-of select="name" />
                </xsl:attribute>
                <xsl:value-of select="name" />
            </xsl:element>
        </TD>

        <TD width="40px"/>
        <TD><xsl:value-of select="date" /></TD>
        <TD width="40px"/>
        <TD>
            WinZip File
        </TD>
    </TR>
    </xsl:for-each>
</TABLE>
<P/>
<HR/>
<P>Please refer to <a href="changes.txt">changes.txt</a> for the changes from one version to the next.</P>
</BODY>
</HTML>
</xsl:template>
</xsl:stylesheet>
