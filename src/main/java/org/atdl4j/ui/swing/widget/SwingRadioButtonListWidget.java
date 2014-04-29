package org.atdl4j.ui.swing.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.swing.SwingListener;

public class SwingRadioButtonListWidget
		extends AbstractSwingWidget<String>
{
	private List<JRadioButton> buttons = new ArrayList<JRadioButton>();
	private ButtonGroup group = new ButtonGroup();	
	private JLabel label;


	public String getControlValueRaw()
	{
		for ( int i = 0; i < this.buttons.size(); i++ )
		{
			JRadioButton b = buttons.get( i );
			if ( b.isSelected() )
			{
				return ( (RadioButtonListT) control ).getListItem().get( i ).getEnumID();
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
		this.setValue( value, false );
	}

	public void setValue(String value, boolean setValueAsControl)
	{
		for (int i = 0; i < buttons.size(); i++) {
			JRadioButton b = buttons.get(i);
			if (setValueAsControl || parameter == null) {
				b.setSelected(value.equals(getListItems().get(i).getEnumID()));
			} else {
				b.setSelected(value.equals(parameter.getEnumPair().get(i)
						.getWireValue()));
			}
		}
	}
	
	public List<Component> getComponents()
	{
		List<Component> widgets = new ArrayList<Component>();
		if (label != null) widgets.add( label );
		widgets.addAll( buttons );
		return widgets;
	}

	public List<Component> getComponentsExcludingLabel() {
		List<Component> widgets = new ArrayList<Component>();
		widgets.addAll( buttons );
		return widgets;
	}	
	
	public void addListener(SwingListener listener) {
		for ( JRadioButton b : buttons )
		{
			b.addActionListener(listener);
		}
	}

	public void removeListener(SwingListener listener) {
		for ( JRadioButton b : buttons )
		{
			b.removeActionListener(listener);
		}
	}	
	
	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( aControlInitValue != null )
		{
			// -- apply initValue if one has been specified --
			setValue( (String) aControlInitValue, true );
		}
		else
		{
			// -- reset each when no initValue exists --
			for ( JRadioButton tempButton : buttons )
			{
				if ( ( tempButton != null ) )
				{
					tempButton.setSelected( false );
				}
			}
		}
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
	
	@Override
	protected List< ? extends Component> createBrickComponents() {
	  List<Component> components = new ArrayList<Component>();
	  
	  JPanel wrapper = new JPanel();
      String tooltip = getTooltip();
                  
      // label
      if ( control.getLabel() != null ) {
          label = new JLabel();
          label.setName(getName()+"/label");
          label.setText( control.getLabel() );
          if ( tooltip != null ) label.setToolTipText( tooltip );
          components.add(label);
      }   
      
      /*
       //TODO: implement horiz/vert orientation for Swing
      if ( ((RadioButtonListT) control).getOrientation() != null &&
           PanelOrientationT.VERTICAL.equals( ((RadioButtonListT) control).getOrientation() ) )
      {
          c.setLayout( new GridLayout( 1, false ) );
      } else {
          RowLayout rl = new RowLayout();
          rl.wrap = false;
          c.setLayout( rl );
      }
       */
      
      // radioButton
      for ( ListItemT listItem : ( (RadioButtonListT) control ).getListItem() )
      {
          JRadioButton radioElement = new JRadioButton();
          radioElement.setName(getName()+"/button/"+listItem.getEnumID());
          radioElement.setText( listItem.getUiRep() );
          if ( parameter != null )
          {
              for ( EnumPairT enumPair : parameter.getEnumPair() )
              {
                  if ( enumPair.getEnumID() == listItem.getEnumID() )
                  {
                      radioElement.setToolTipText( enumPair.getDescription() );
                      break;
                  }
              }
          }
          else
          {
              radioElement.setToolTipText( tooltip );
          }
          group.add( radioElement );
          buttons.add( radioElement );
          wrapper.add( radioElement );
      }

      // set initValue (Note that this has to be the enumID, not the
      // wireValue)
      // set initValue
      if ( ControlHelper.getInitValue( control, getAtdl4jOptions() ) != null )
          setValue( (String) ControlHelper.getInitValue( control, getAtdl4jOptions() ), true );
      
      components.add(wrapper);
      return components;
	}
}