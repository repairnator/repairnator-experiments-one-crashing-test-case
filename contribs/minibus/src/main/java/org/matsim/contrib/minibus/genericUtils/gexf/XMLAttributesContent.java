//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.19 at 03:18:45 PM MESZ 
//


package org.matsim.contrib.minibus.genericUtils.gexf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for attributes-content complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attributes-content">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.gexf.net/1.2draft}attribute" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="class" use="required" type="{http://www.gexf.net/1.2draft}class-type" />
 *       &lt;attribute name="mode" type="{http://www.gexf.net/1.2draft}mode-type" />
 *       &lt;attribute name="start" type="{http://www.gexf.net/1.2draft}time-type" />
 *       &lt;attribute name="startopen" type="{http://www.gexf.net/1.2draft}time-type" />
 *       &lt;attribute name="end" type="{http://www.gexf.net/1.2draft}time-type" />
 *       &lt;attribute name="endopen" type="{http://www.gexf.net/1.2draft}time-type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributes-content", propOrder = {
    "attribute"
})
public class XMLAttributesContent {

    private List<XMLAttributeContent> attribute;
    @XmlAttribute(name = "class", required = true)
    private XMLClassType clazz;
    @XmlAttribute
    private XMLModeType mode;
    @XmlAttribute
    private String start;
    @XmlAttribute
    private String startopen;
    @XmlAttribute
    private String end;
    @XmlAttribute
    private String endopen;

    /**
     * Gets the value of the attribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLAttributeContent }
     * 
     * 
     */
    public List<XMLAttributeContent> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<>();
        }
        return this.attribute;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link XMLClassType }
     *     
     */
    public XMLClassType getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLClassType }
     *     
     */
    public void setClazz(XMLClassType value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link XMLModeType }
     *     
     */
    public XMLModeType getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLModeType }
     *     
     */
    public void setMode(XMLModeType value) {
        this.mode = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStart(String value) {
        this.start = value;
    }

    /**
     * Gets the value of the startopen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartopen() {
        return startopen;
    }

    /**
     * Sets the value of the startopen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartopen(String value) {
        this.startopen = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnd(String value) {
        this.end = value;
    }

    /**
     * Gets the value of the endopen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndopen() {
        return endopen;
    }

    /**
     * Sets the value of the endopen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndopen(String value) {
        this.endopen = value;
    }

}
