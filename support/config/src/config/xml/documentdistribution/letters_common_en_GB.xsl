<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" version="1.0">
    <!-- Setup templates that are used common across all the List Distributiuon Letters  -->
    <!-- Common template to display date in following formay 01 January 2004 -->
	<!-- Template to display the date in a specific format -->
	<xsl:template name="displayDate">
		<xsl:param name="input"/>
		<xsl:param name="language"/>
		<xsl:variable name="month" select="substring($input,6,2)"/>
		<xsl:variable name="day" select="substring($input,9,2)"/>
		<xsl:variable name="year" select="substring($input,1,4)"/>
		<xsl:value-of select="$day"/>
		<xsl:text> </xsl:text>
		<xsl:choose>
			<xsl:when test="$month='01'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'January'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='02'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'February'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='03'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'March'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='04'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'April'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='05'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'May'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='06'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'June'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='07'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'July'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='08'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'August'"/>
                        </xsl:call-template>
			</xsl:when>
			<xsl:when test="$month='09'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'September'"/>
                        </xsl:call-template>			
			</xsl:when>
			<xsl:when test="$month='10'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'October'"/>
                        </xsl:call-template>			
			</xsl:when>
			<xsl:when test="$month='11'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'November'"/>
                        </xsl:call-template>			
			</xsl:when>
			<xsl:when test="$month='12'">
                        <xsl:call-template name="getValue">
                            <xsl:with-param name="language" select="$language"/>
                            <xsl:with-param name="key" select="'December'"/>
                        </xsl:call-template>			
			</xsl:when>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>
    <!-- Common template to display start date (date to display on the letter) -->
    <xsl:template name="DisplayStartDate">
        <xsl:param name="date"/>
        <xsl:param name="language"/>
        <xsl:call-template name="displayDate">
            <xsl:with-param name="input" select="$date"/>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
    </xsl:template>
    <!-- Common template to display date including the day -->
    <xsl:template name="displayDayDate">
        <xsl:param name="input"/>
        <xsl:param name="language"/>
        <xsl:variable name="month" select="substring($input,6,2)"/>
        <xsl:variable name="day" select="substring($input,9,2)"/>
        <xsl:variable name="year" select="substring($input,1,4)"/>
        <xsl:variable name="dayOfWeek">
            <xsl:call-template name="calculate-day-of-the-week">
                <xsl:with-param name="year" select="$year"/>
                <xsl:with-param name="month" select="$month"/>
                <xsl:with-param name="day" select="$day"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$dayOfWeek=0">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Sunday'"/>
                </xsl:call-template>			
            </xsl:when>
            <xsl:when test="$dayOfWeek=1">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Monday'"/>
                </xsl:call-template>			
            </xsl:when>
            <xsl:when test="$dayOfWeek=2">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Tuesday'"/>
                </xsl:call-template>			
            </xsl:when>
            <xsl:when test="$dayOfWeek=3">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Wednesday'"/>
                </xsl:call-template>			
            </xsl:when>
            <xsl:when test="$dayOfWeek=4">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Thursday'"/>
                </xsl:call-template>			
            </xsl:when>
            <xsl:when test="$dayOfWeek=5">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Friday'"/>
                </xsl:call-template>			
            </xsl:when>
            <xsl:when test="$dayOfWeek=6">
                <xsl:call-template name="getValue">
                    <xsl:with-param name="language" select="$language"/>
                    <xsl:with-param name="key" select="'Saturday'"/>
                </xsl:call-template>			
            </xsl:when>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:call-template name="displayDate">
            <xsl:with-param name="input" select="$input"/>
            <xsl:with-param name="language" select="$language"/>
        </xsl:call-template>
    </xsl:template>
    <!-- Return number to represent the day of the week
			0	Sunday
			1	Monday
			2	Tuesday
			3	Wednesday
			4	Thursday
			5	Friday
			6	Saturday
	-->
    <xsl:template name="calculate-day-of-the-week">
        <xsl:param name="year"/>
        <xsl:param name="month"/>
        <xsl:param name="day"/>
        <xsl:variable name="a" select="floor((14 - $month) div 12)"/>
        <xsl:variable name="y" select="$year - $a"/>
        <xsl:variable name="m" select="$month + 12 * $a - 2"/>
        <xsl:value-of select="($day + $y + floor($y div 4) - floor($y div 100) + floor($y div 400) + floor((31 * $m) div 12)) mod 7"/>
    </xsl:template>
    <!-- Common template used to format the time from 24hrs to the following format HH:MM [AM/PM]-->
    <!-- Display the formatted time and then post fix with AM or PM -->
    <xsl:template name="FormatTime">
        <xsl:param name="input"/>
        <xsl:choose>
            <!-- if less than 12 postfix AM -->
            <xsl:when test="substring-before($input,':') &lt; 12">
                <xsl:value-of select="$input"/>
                <xsl:text> AM</xsl:text>
            </xsl:when>
            <!-- otherwise format the hours element and postfix with PM -->
            <xsl:otherwise>
                <!-- get the hours element from the string -->
                <xsl:variable name="hrs" select="substring-before($input,':')"/>
                <xsl:choose>
                    <!-- if 12 just display value as no formatting required -->
                    <xsl:when test="$hrs = 12">
                        <xsl:value-of select="$hrs"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- subtract 12 from the 24 hours element -->
                        <xsl:variable name="fmtHrs" select="$hrs - 12"/>
                        <!-- display the value of the formatted hours -->
                        <xsl:value-of select="$fmtHrs"/>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- display the rest of the time -->
                <xsl:text>:</xsl:text>
                <xsl:value-of select="substring-after($input,':')"/>
                <xsl:text> PM</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Fold line dimensions used for DLL FLL WLL -->
    <xsl:template name="FoldLine">
        <fo:block space-after="77mm"/>
        <fo:block>
            <fo:leader leader-length="70%" leader-pattern="rule"/>
        </fo:block>
    </xsl:template>
    <!-- Common template to set up the page [FO pages only] -->
    <xsl:template name="PageSetUp">
        <fo:layout-master-set>
        	<!-- set up page for first page -->
        	<fo:simple-page-master master-name="first" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
        		<fo:region-body margin-top="18mm" margin-bottom="10mm" margin-left="5mm"/>
        		<fo:region-before extent="20mm" region-name="header-first"/>
        		<fo:region-after extent="10mm"/>
        		<fo:region-start extent="5mm" region-name="page-fold"/>
        	</fo:simple-page-master>
        	<!-- set up page for rest of pages -->
        	<fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="15mm" margin-right="10mm">
        		<fo:region-body margin-top="18mm" margin-bottom="10mm"/>
        		<fo:region-before extent="20mm" region-name="header-rest"/>
        		<fo:region-after extent="10mm"/>
        	</fo:simple-page-master>
        	<fo:page-sequence-master master-name="document">
        		<!-- set up page conditions -->
        		<fo:repeatable-page-master-alternatives>
        			<fo:conditional-page-master-reference page-position="first" master-reference="first"/>
        			<fo:conditional-page-master-reference page-position="rest" master-reference="rest"/>
        		</fo:repeatable-page-master-alternatives>
        	</fo:page-sequence-master>
        </fo:layout-master-set>
    </xsl:template>
    <!-- Common template to capitalise first letter of each word -->
    <xsl:template name="TitleCase">
        <xsl:param name="text"/>
        <xsl:param name="lastletter" select="' '"/>
        <xsl:if test="$text">
            <xsl:variable name="thisletter" select="substring($text,1,1)"/>
            <xsl:choose>
                <xsl:when test="not(contains('0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',$lastletter))">
                    <xsl:value-of select="translate($thisletter,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="translate($thisletter,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="TitleCase">
                <xsl:with-param name="text" select="substring($text,2)"/>
                <xsl:with-param name="lastletter" select="$thisletter"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    <!-- Common template to convert text to upper case -->
    <xsl:template name="toUpper">
        <xsl:param name="content"/>
        <xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    </xsl:template>
</xsl:stylesheet>