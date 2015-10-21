
/*
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 */

/*
 * @(#)$Id: StylesheetInserter.java,v 1.1 2004/06/16 11:03:44 eran Exp $
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of Sun Microsystems, Inc.
 * Use is subject to license terms.
 *
 */
package extensionTools.opl2;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Inserts &lt;?xml-stylesheet ... ?> PI.
 */
public class StylesheetInserter extends XMLFilterImpl {

    /**
     * @param styleSheetUri
     *      The value to put in the <code>href</code> pseudo-attribute
     *      of the PI. For example, "mystyle.xsl"
     * @param type
     *      The value to put in the <code>type</code> pseudo-attribute
     *      of the PI. For example, "text/xsl"
     */
    public StylesheetInserter( String styleSheetUri, String type ) {
        this.styleSheetUri = styleSheetUri;
        this.type = type;
    }

    private final String styleSheetUri,type;

    public void startDocument() throws SAXException {
        super.startDocument();

        // insert the PI at the beginning of the document.
        StringBuffer buf = new StringBuffer();
        buf.append("type='");
        buf.append(this.type);
        buf.append("' href='");
        buf.append(this.styleSheetUri);
        buf.append("'");

        super.processingInstruction("xml-stylesheet",buf.toString());
    }
}
