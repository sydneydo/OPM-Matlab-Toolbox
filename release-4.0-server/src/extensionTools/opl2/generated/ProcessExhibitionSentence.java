//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.05.23 at 12:36:13 PM IDT 
//


package extensionTools.opl2.generated;


/**
 * Java content class for ProcessExhibitionSentence element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/raanan/jwsdp-1.6/jaxb/bin/opl.xml line 508)
 * <p>
 * <pre>
 * &lt;element name="ProcessExhibitionSentence">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ProcessName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element ref="{}Role" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}Operation" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}ExhibitedObject" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ProcessExhibitionSentence
    extends javax.xml.bind.Element, extensionTools.opl2.generated.ProcessExhibitionSentenceType
{


}
