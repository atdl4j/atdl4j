//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.24 at 10:55:05 AM CST 
//


package org.atdl4j.fixatdl.layout;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.atdl4j.fixatdl.layout package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _StrategyLayout_QNAME = new QName("http://www.fixprotocol.org/FIXatdl-1-1/Layout", "StrategyLayout");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.atdl4j.fixatdl.layout
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClockT }
     * 
     */
    public ClockT createClockT() {
        return new ClockT();
    }

    /**
     * Create an instance of {@link StrategyLayoutT }
     * 
     */
    public StrategyLayoutT createStrategyLayoutT() {
        return new StrategyLayoutT();
    }

    /**
     * Create an instance of {@link SliderT }
     * 
     */
    public SliderT createSliderT() {
        return new SliderT();
    }

    /**
     * Create an instance of {@link CheckBoxT }
     * 
     */
    public CheckBoxT createCheckBoxT() {
        return new CheckBoxT();
    }

    /**
     * Create an instance of {@link StrategyPanelT }
     * 
     */
    public StrategyPanelT createStrategyPanelT() {
        return new StrategyPanelT();
    }

    /**
     * Create an instance of {@link DropDownListT }
     * 
     */
    public DropDownListT createDropDownListT() {
        return new DropDownListT();
    }

    /**
     * Create an instance of {@link RadioButtonListT }
     * 
     */
    public RadioButtonListT createRadioButtonListT() {
        return new RadioButtonListT();
    }

    /**
     * Create an instance of {@link RadioButtonT }
     * 
     */
    public RadioButtonT createRadioButtonT() {
        return new RadioButtonT();
    }

    /**
     * Create an instance of {@link MultiSelectListT }
     * 
     */
    public MultiSelectListT createMultiSelectListT() {
        return new MultiSelectListT();
    }

    /**
     * Create an instance of {@link CheckBoxListT }
     * 
     */
    public CheckBoxListT createCheckBoxListT() {
        return new CheckBoxListT();
    }

    /**
     * Create an instance of {@link HiddenFieldT }
     * 
     */
    public HiddenFieldT createHiddenFieldT() {
        return new HiddenFieldT();
    }

    /**
     * Create an instance of {@link SingleSpinnerT }
     * 
     */
    public SingleSpinnerT createSingleSpinnerT() {
        return new SingleSpinnerT();
    }

    /**
     * Create an instance of {@link SingleSelectListT }
     * 
     */
    public SingleSelectListT createSingleSelectListT() {
        return new SingleSelectListT();
    }

    /**
     * Create an instance of {@link EditableDropDownListT }
     * 
     */
    public EditableDropDownListT createEditableDropDownListT() {
        return new EditableDropDownListT();
    }

    /**
     * Create an instance of {@link ListItemT }
     * 
     */
    public ListItemT createListItemT() {
        return new ListItemT();
    }

    /**
     * Create an instance of {@link TextFieldT }
     * 
     */
    public TextFieldT createTextFieldT() {
        return new TextFieldT();
    }

    /**
     * Create an instance of {@link DoubleSpinnerT }
     * 
     */
    public DoubleSpinnerT createDoubleSpinnerT() {
        return new DoubleSpinnerT();
    }

    /**
     * Create an instance of {@link LabelT }
     * 
     */
    public LabelT createLabelT() {
        return new LabelT();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StrategyLayoutT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.fixprotocol.org/FIXatdl-1-1/Layout", name = "StrategyLayout")
    public JAXBElement<StrategyLayoutT> createStrategyLayout(StrategyLayoutT value) {
        return new JAXBElement<>(_StrategyLayout_QNAME, StrategyLayoutT.class, null, value);
    }

}
