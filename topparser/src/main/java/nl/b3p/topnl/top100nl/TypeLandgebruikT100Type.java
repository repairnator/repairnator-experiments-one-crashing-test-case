//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.07 at 02:40:39 PM CET 
//


package nl.b3p.topnl.top100nl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TypeLandgebruikT100Type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TypeLandgebruikT100Type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="aanlegsteiger"/>
 *     &lt;enumeration value="akkerland"/>
 *     &lt;enumeration value="basaltblokken, steenglooiing"/>
 *     &lt;enumeration value="bebouwd gebied"/>
 *     &lt;enumeration value="boomgaard"/>
 *     &lt;enumeration value="boomkwekerij"/>
 *     &lt;enumeration value="bos: gemengd bos"/>
 *     &lt;enumeration value="bos: griend"/>
 *     &lt;enumeration value="bos: loofbos"/>
 *     &lt;enumeration value="bos: naaldbos"/>
 *     &lt;enumeration value="dodenakker"/>
 *     &lt;enumeration value="fruitkwekerij"/>
 *     &lt;enumeration value="grasland"/>
 *     &lt;enumeration value="heide"/>
 *     &lt;enumeration value="kassengebied"/>
 *     &lt;enumeration value="laadperron"/>
 *     &lt;enumeration value="populieren"/>
 *     &lt;enumeration value="spoorbaanlichaam"/>
 *     &lt;enumeration value="zand"/>
 *     &lt;enumeration value="overig"/>
 *     &lt;enumeration value="ongedefinieerd"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TypeLandgebruikT100Type", namespace = "http://register.geostandaarden.nl/gmlapplicatieschema/top100nl/1.1.0")
@XmlEnum
public enum TypeLandgebruikT100Type {

    @XmlEnumValue("aanlegsteiger")
    AANLEGSTEIGER("aanlegsteiger"),
    @XmlEnumValue("akkerland")
    AKKERLAND("akkerland"),
    @XmlEnumValue("basaltblokken, steenglooiing")
    BASALTBLOKKEN_STEENGLOOIING("basaltblokken, steenglooiing"),
    @XmlEnumValue("bebouwd gebied")
    BEBOUWD_GEBIED("bebouwd gebied"),
    @XmlEnumValue("boomgaard")
    BOOMGAARD("boomgaard"),
    @XmlEnumValue("boomkwekerij")
    BOOMKWEKERIJ("boomkwekerij"),
    @XmlEnumValue("bos: gemengd bos")
    BOS_GEMENGD_BOS("bos: gemengd bos"),
    @XmlEnumValue("bos: griend")
    BOS_GRIEND("bos: griend"),
    @XmlEnumValue("bos: loofbos")
    BOS_LOOFBOS("bos: loofbos"),
    @XmlEnumValue("bos: naaldbos")
    BOS_NAALDBOS("bos: naaldbos"),
    @XmlEnumValue("dodenakker")
    DODENAKKER("dodenakker"),
    @XmlEnumValue("fruitkwekerij")
    FRUITKWEKERIJ("fruitkwekerij"),
    @XmlEnumValue("grasland")
    GRASLAND("grasland"),
    @XmlEnumValue("heide")
    HEIDE("heide"),
    @XmlEnumValue("kassengebied")
    KASSENGEBIED("kassengebied"),
    @XmlEnumValue("laadperron")
    LAADPERRON("laadperron"),
    @XmlEnumValue("populieren")
    POPULIEREN("populieren"),
    @XmlEnumValue("spoorbaanlichaam")
    SPOORBAANLICHAAM("spoorbaanlichaam"),
    @XmlEnumValue("zand")
    ZAND("zand"),
    @XmlEnumValue("overig")
    OVERIG("overig"),

    /**
     * ruimte tussen 2 weglijnen van direct naast elkaar gelegen wegen
     * 
     */
    @XmlEnumValue("ongedefinieerd")
    ONGEDEFINIEERD("ongedefinieerd");
    private final String value;

    TypeLandgebruikT100Type(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TypeLandgebruikT100Type fromValue(String v) {
        for (TypeLandgebruikT100Type c: TypeLandgebruikT100Type.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
