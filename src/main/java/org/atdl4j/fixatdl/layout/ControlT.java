//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.24 at 10:55:05 AM CST 
//


package org.atdl4j.fixatdl.layout;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.atdl4j.fixatdl.flow.StateRuleT;


/**
 * <p>Java class for Control_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Control_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.fixprotocol.org/FIXatdl-1-1/Flow}StateRule" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="HelpText" type="{http://www.fixprotocol.org/FIXatdl-1-1/Layout}HelpText_t" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ID" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="[A-Za-z][A-za-z0-9_]{0,255}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="parameterRef">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="[A-Za-z][A-za-z0-9_]{0,255}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="initFixField" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="initPolicy">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="UseValue"/>
 *             &lt;enumeration value="UseFixField"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="disableForTemplate" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Control_t", propOrder = {
    "stateRule",
    "helpText"
})
@XmlSeeAlso({
    DoubleSpinnerT.class,
    MultiSelectListT.class,
    LabelT.class,
    CheckBoxListT.class,
    RadioButtonT.class,
    SingleSelectListT.class,
    CheckBoxT.class,
    HiddenFieldT.class,
    EditableDropDownListT.class,
    DropDownListT.class,
    RadioButtonListT.class,
    ClockT.class,
    SingleSpinnerT.class,
    TextFieldT.class,
    SliderT.class
})
public abstract class ControlT {

    @XmlElement(name = "StateRule", namespace = "http://www.fixprotocol.org/FIXatdl-1-1/Flow")
    protected List<StateRuleT> stateRule;
    @XmlElement(name = "HelpText")
    protected String helpText;
    @XmlAttribute(name = "ID", required = true)
    protected String id;
    @XmlAttribute
    protected String parameterRef;
    @XmlAttribute
    protected String label;
    @XmlAttribute
    protected String initFixField;
    @XmlAttribute
    protected String initPolicy;
    @XmlAttribute
    protected String tooltip;
    @XmlAttribute
    protected Boolean disableForTemplate;

    /**
     * Gets the value of the stateRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stateRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStateRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StateRuleT }
     * 
     * 
     */
    public List<StateRuleT> getStateRule()
    {
        if (stateRule == null)
        {
            stateRule = new ArrayList<>();
        }
        return this.stateRule;
    }

    /**
     * Gets the value of the helpText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHelpText() {
        return helpText;
    }

    /**
     * Sets the value of the helpText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHelpText(String value) {
        this.helpText = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the parameterRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterRef() {
        return parameterRef;
    }

    /**
     * Sets the value of the parameterRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterRef(String value) {
        this.parameterRef = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the initFixField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitFixField() {
        return initFixField;
    }

    /**
     * Sets the value of the initFixField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitFixField(String value) {
        this.initFixField = value;
    }

    /**
     * Gets the value of the initPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitPolicy() {
        return initPolicy;
    }

    /**
     * Sets the value of the initPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitPolicy(String value) {
        this.initPolicy = value;
    }

    /**
     * Gets the value of the tooltip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Sets the value of the tooltip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTooltip(String value) {
        this.tooltip = value;
    }

    /**
     * Gets the value of the disableForTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisableForTemplate() {
        return disableForTemplate;
    }

    /**
     * Sets the value of the disableForTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisableForTemplate(Boolean value) {
        this.disableForTemplate = value;
    }

}
