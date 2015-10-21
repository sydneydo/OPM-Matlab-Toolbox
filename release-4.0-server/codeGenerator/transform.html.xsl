<xsl:stylesheet

    xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>

    <xsl:output method = 'xml'/>

    <!-- remove leading and trailing line breaks and spaces -->
    <xsl:strip-space elements="*" />

    <xsl:template match="SingleLineComment">
        <!-- convert into a single line -->
        // <xsl:value-of select = "normalize-space(.)" />
    </xsl:template>

    <xsl:template match="MultiLineComment">
        /* <xsl:value-of select = "."/> */
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy-of select = "."/>
    </xsl:template>

</xsl:stylesheet>