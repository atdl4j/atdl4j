package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.EnumPairT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.atdl.layout.RadioButtonListT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.atdl4j.atdl.layout.PanelOrientationT;
import org.atdl4j.atdl.layout.StrategyPanelT;

public class RadioButtonListWidget extends AbstractSWTWidget<String>
{
    private static final Logger logger = Logger
	    .getLogger(RadioButtonListWidget.class);

    private List<Button> buttons = new ArrayList<Button>();
    private Label label;

    // 1/20/2010 Scott Atwell added
    public static boolean disableVerticalLayoutHandling = false;

    public RadioButtonListWidget(RadioButtonListT control, ParameterT parameter)
	    throws JAXBException
    {
	this.control = control;
	this.parameter = parameter;
	init();
    }

    public Widget createWidget(Composite parent, int style)
    {

	// label
	label = new Label(parent, SWT.NONE);
	if (control.getLabel() != null)
	    label.setText(control.getLabel());

	Composite c = new Composite(parent, SWT.NONE);

	// Scott Atwell 1/20/2010
	// -- original, default (Horizontal) --
	c.setLayout(new FillLayout());

	logger.debug("LOG RadioButtonGroupWidget: " + control.getID()
		+ " parent.getData(): " + parent.getData()
		+ " parent.getLayout(): " + parent.getLayout()
		+ " parent.getChildren().length: "
		+ parent.getChildren().length + " parent.getChildren(): "
		+ parent.getChildren());
	// -- Special behavior to generate these vertically if parent
	// StrategyPanel is in PanelOrientationT.VERTICAL and contains this
	// component ONLY --
	// @see SWTFactory.createLayout() -- it has: else if (orientation ==
	// PanelOrientationT.VERTICAL) { GridLayout l = new GridLayout(2,
	// false); }
	if ((disableVerticalLayoutHandling == false)
		&& (parent.getChildren().length <= 2)
		&& (parent.getData() instanceof StrategyPanelT)
		&& (((StrategyPanelT) parent.getData()).getOrientation() == PanelOrientationT.VERTICAL))
	{
	    // -- consider it (orientation == PanelOrientationT.VERTICAL) --
	    logger.info("LOG RadioButtonGroupWidget: " + control.getID()
		    + " Considering parent: " + parent + " with GridLayout: "
		    + ((GridLayout) parent.getLayout())
		    + "to be PanelOrientationT.VERTICAL");
	    c.setLayout(new GridLayout(1, false));
	}
	// TODO Scott Atwell 1/20/2010 ABOVE

	// tooltip
	String tooltip = getTooltip();
	if (tooltip != null)
	    label.setToolTipText(tooltip);

	// radioButton
	for (ListItemT listItem : ((RadioButtonListT) control).getListItem())
	{

	    Button radioElement = new Button(c, style | SWT.RADIO);
	    radioElement.setText(listItem.getUiRep());
	    if (parameter != null)
	    {
		for (EnumPairT enumPair : parameter.getEnumPair())
		{
		    if (enumPair.getEnumID() == listItem.getEnumID())
		    {
			radioElement.setToolTipText(enumPair.getDescription());
			break;
		    }
		}
	    } else
		radioElement.setToolTipText(tooltip);
	    buttons.add(radioElement);
	}

	// set initValue (Note that this has to be the enumID, not the
	// wireValue)
	// set initValue
	if (((RadioButtonListT) control).getInitValue() != null)
	    setValue(((RadioButtonListT) control).getInitValue(), true);

	return c;
    }

    public String getControlValue()
    {
	for (int i = 0; i < this.buttons.size(); i++)
	{
	    Button b = buttons.get(i);
	    // TODO 1/24/2010 Scott Atwell if (b.getSelection()) {
	    if ((b.getSelection()) && (b.isVisible()) && (b.isEnabled()))
	    {
		return ((RadioButtonListT) control).getListItem().get(i)
			.getEnumID();
	    }
	}
	return null;
    }

    public String getParameterValue()
    {
	return getParameterValueAsEnumWireValue();
    }

    public void setValue(String value)
    {
	this.setValue(value, false);
    }

    public void setValue(String value, boolean setValueAsControl)
    {
	for (int i = 0; i < buttons.size(); i++)
	{
	    Button b = buttons.get(i);
	    if (setValueAsControl || parameter == null)
	    {
		b.setSelection(value.equals(getListItems().get(i).getEnumID()));
	    } else
	    {
		b.setSelection(value.equals(parameter.getEnumPair().get(i)
			.getWireValue()));
	    }
	}
    }
    
    public List<Control> getControls()
    {
	List<Control> widgets = new ArrayList<Control>();
	widgets.add(label);
	widgets.addAll(buttons);
	return widgets;
    }

    public void addListener(Listener listener)
    {
	for (Button b : buttons)
	{
	    b.addListener(SWT.Selection, listener);
	}
    }

    public void removeListener(Listener listener)
    {
	for (Button b : buttons)
	{
	    b.removeListener(SWT.Selection, listener);
	}
    }

}
