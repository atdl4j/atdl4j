package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.fixprotocol.atdl_1_1.core.EnumPairT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.layout.ListItemT;
import org.fixprotocol.atdl_1_1.layout.PanelOrientationT;
import org.fixprotocol.atdl_1_1.layout.RadioButtonGroupT;
import org.fixprotocol.atdl_1_1.layout.StrategyPanelT;

public class RadioButtonGroupWidget extends AbstractSWTWidget<String> {
	private static final Logger logger = Logger.getLogger(RadioButtonGroupWidget.class);

	private List<Button> radioButton = new ArrayList<Button>();
	private Label label;

// 1/20/2010 Scott Atwell added
	public static boolean disableVerticalLayoutHandling = false;
	
	public RadioButtonGroupWidget(RadioButtonGroupT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {

		// label
		Label l = new Label(parent, SWT.NONE);
// 1/20/2010 Scott Atwell avoid NPE as label is not required on Control		l.setText(control.getLabel());
		if ( control.getLabel() != null )
		{
			l.setText(control.getLabel());
		}
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		
//TODO Scott Atwell 1/20/2010		c.setLayout(new FillLayout());
//TODO Scott Atwell 1/20/2010		BELOW
		// -- original, default (Horizontal) --
		c.setLayout(new FillLayout());

logger.debug("LOG RadioButtonGroupWidget: " + control.getID() + " parent.getData(): " + parent.getData() + " parent.getLayout(): " + parent.getLayout() + " parent.getChildren().length: " + parent.getChildren().length + " parent.getChildren(): " + parent.getChildren() );
		// -- Special behavior to generate these vertically if parent StrategyPanel is in PanelOrientationT.VERTICAL and contains this component ONLY --
		// @see SWTFactory.createLayout() -- it has:  else if (orientation == PanelOrientationT.VERTICAL) { GridLayout l = new GridLayout(2, false); }
		if ( ( disableVerticalLayoutHandling == false ) &&
			  ( parent.getChildren().length <= 2 ) &&
			  ( parent.getData() instanceof StrategyPanelT ) &&
			  ( ((StrategyPanelT) parent.getData()).getOrientation() == PanelOrientationT.VERTICAL ) )
		{
			// -- consider it (orientation == PanelOrientationT.VERTICAL) --
logger.info("LOG RadioButtonGroupWidget: " + control.getID() + " Considering parent: " + parent + " with GridLayout: " + ((GridLayout) parent.getLayout()) + "to be PanelOrientationT.VERTICAL");
			c.setLayout(new GridLayout(1, false ));
		}
//TODO Scott Atwell 1/20/2010		ABOVE		
		
		
		
		

		// tooltip
		String tooltip = control.getTooltip();
		l.setToolTipText(tooltip);

		// radioButton		
		for (ListItemT listItem : ((RadioButtonGroupT)control).getListItem()) {
			
			Button radioElement = new Button(c, style | SWT.RADIO);
			radioElement.setText(listItem.getUiRep());
			if (parameter != null) {
				for (EnumPairT enumPair :  parameter.getEnumPair()) {
					if (enumPair.getEnumID() == listItem.getEnumID()) {
						radioElement.setToolTipText(enumPair.getDescription());
						break;
					}
				}
			} else radioElement.setToolTipText(tooltip);
			radioButton.add(radioElement);
		}
		
		// set initValue  (Note that this has to be the enumID, not the wireValue)
		if  (((RadioButtonGroupT)control).getInitValue() != null)
			setValue(((RadioButtonGroupT)control).getInitValue(), true);
		
		return c;
	}

	public String getControlValue() {
		for (int i = 0; i < this.radioButton.size(); i++) {
			Button b = radioButton.get(i);
//TODO 1/24/2010 Scott Atwell			if (b.getSelection()) {
			if ( (b.getSelection()) && (b.isVisible()) && (b.isEnabled()) )  {
				return ((RadioButtonGroupT)control).getListItem().get(i).getEnumID();
			}
		}
		return null;
	}
		
	public String getParameterValue() {
		return getParameterValueAsEnumWireValue();
	}
	
	public void setValue(String value)
	{
		this.setValue(value, false);
	}
	
	public void setValue(String value, boolean setValueAsControl) {
		for (int i = 0; i < radioButton.size(); i++) {
			Button b = radioButton.get(i);
			if (setValueAsControl || parameter == null)
			{
				b.setSelection(value.equals(getListItems().get(i).getEnumID()));
			}
			else
			{
				b.setSelection(value.equals(parameter.getEnumPair().get(i).getWireValue()));
			}
		}
	}
	
	public void generateStateRuleListener(Listener listener) {
		for (Button radioElement : radioButton) {
			radioElement.addListener(SWT.Selection, listener);
		}
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.addAll(radioButton);
		return widgets;
	}

	public void addListener(Listener listener) {
		// wrap around ParameterListener which raises a ParameterEvent
		Listener wrapper = new ParameterListenerWrapper(this, listener);
		for (Button b : radioButton) {
			b.addListener(SWT.Selection, wrapper);
		}
	}

	public void removeListener(Listener listener) {
		for (Button b : radioButton) {
			b.removeListener(SWT.Selection, listener);
			// Listener[] listeners = radio.getListeners(SWT.Selection);
			// for (int i = 0; i < listeners.length; i++) {
			// Listener l = listeners[i];
			// if (l instanceof ParameterListenerWrapper) {
			// ParameterListenerWrapper wrapper = (ParameterListenerWrapper) l;
			// if (wrapper.getDelegate() == listener) {
			// radio.removeListener(SWT.Selection, l);
			// return;
			// }
			// }
			// }
		}
	}

}
