//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.10 at 11:33:39 AM CET 
//


package nl.b3p.topnl.top10nl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for InrichtingselementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InrichtingselementType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://register.geostandaarden.nl/gmlapplicatieschema/top10nl/1.2.0}_Top10nlObjectType">
 *       &lt;sequence>
 *         &lt;element name="typeInrichtingselement" type="{http://www.opengis.net/gml/3.2}CodeType"/>
 *         &lt;element name="hoogte" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="breedte" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="hoogteniveau" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="soortnaam" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="naam" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nummer" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="geometrie" type="{http://register.geostandaarden.nl/gmlapplicatieschema/brt-algemeen/1.2.0}BRTLijnOfPuntPropertyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InrichtingselementType", namespace = "http://register.geostandaarden.nl/gmlapplicatieschema/top10nl/1.2.0", propOrder = {
    "typeInrichtingselement",
    "hoogte",
    "breedte",
    "hoogteniveau",
    "soortnaam",
    "naam",
    "nummer",
    "geometrie"
})
public class InrichtingselementType
    extends Top10NlObjectType
{

    @XmlElement(required = true)
    protected CodeType typeInrichtingselement;
    protected Double hoogte;
    protected BigInteger breedte;
    @XmlElement(required = true)
    protected BigInteger hoogteniveau;
    protected String soortnaam;
    protected List<String> naam;
    protected List<String> nummer;
    @XmlAnyElement
    protected Element geometrie;

    /**
     * Gets the value of the typeInrichtingselement property.
     * 
     * @return
     *     possible object is
     *     {@link CodeType }
     *     
     */
    public CodeType getTypeInrichtingselement() {
        return typeInrichtingselement;
    }

    /**
     * Sets the value of the typeInrichtingselement property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeType }
     *     
     */
    public void setTypeInrichtingselement(CodeType value) {
        this.typeInrichtingselement = value;
    }

    /**
     * Gets the value of the hoogte property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHoogte() {
        return hoogte;
    }

    /**
     * Sets the value of the hoogte property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHoogte(Double value) {
        this.hoogte = value;
    }

    /**
     * Gets the value of the breedte property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBreedte() {
        return breedte;
    }

    /**
     * Sets the value of the breedte property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBreedte(BigInteger value) {
        this.breedte = value;
    }

    /**
     * Gets the value of the hoogteniveau property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHoogteniveau() {
        return hoogteniveau;
    }

    /**
     * Sets the value of the hoogteniveau property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHoogteniveau(BigInteger value) {
        this.hoogteniveau = value;
    }

    /**
     * Gets the value of the soortnaam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoortnaam() {
        return soortnaam;
    }

    /**
     * Sets the value of the soortnaam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoortnaam(String value) {
        this.soortnaam = value;
    }

    /**
     * Gets the value of the naam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the naam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNaam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNaam() {
        if (naam == null) {
            naam = new ArrayList<String>();
        }
        return this.naam;
    }

    /**
     * Gets the value of the nummer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nummer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNummer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNummer() {
        if (nummer == null) {
            nummer = new ArrayList<String>();
        }
        return this.nummer;
    }

    /**
     * Gets the value of the geometrie property.
     * 
     * @return
     *     possible object is
     *     {@link Element }
     *     
     */
    public Element getGeometrie() {
        return geometrie;
    }

    /**
     * Sets the value of the geometrie property.
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     
     */
    public void setGeometrie(Element value) {
        this.geometrie = value;
    }

}
