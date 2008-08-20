package br.com.investtools.fix.atdl.ui.swt.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import br.com.investtools.fix.atdl.core.xmlbeans.EnumPairT;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument.Strategies;
import br.com.investtools.fix.atdl.layout.xmlbeans.ComponentT;
import br.com.investtools.fix.atdl.layout.xmlbeans.PanelOrientationT;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyLayoutDocument.StrategyLayout;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;


public class Main 
{
    public static void main( String[] args )
    {
    
    	File file = new File("C:\\teste.txt");
    	StrategiesDocument strategiesDocument;
    	
    	Display display = new Display ();
    	Shell shell = new Shell(display);
    	
    	FillLayout shellLayout = initializeFillLayout();
    	shell.setLayout (shellLayout);

    	
    	try {
    		strategiesDocument = StrategiesDocument.Factory.parse(file);
    		Strategies strategies = strategiesDocument.getStrategies();
    		StrategyT[] strategyTArray = strategies.getStrategyArray();
    		
    		for (int i = 0; i < strategyTArray.length; i++) {
    		
    			StrategyLayout strategyLayout = strategyTArray[i].getStrategyLayout();
    			StrategyPanel[] strategyPanelArray = strategyLayout.getStrategyPanelArray();
    			
    			expandStrategyPanel(shell, strategyPanelArray);
    		    			
    			shell.pack();
    	    	shell.open();
    	    	
    	    	while (!shell.isDisposed ()) {
    	    		if (!display.readAndDispatch ()) display.sleep ();
    	    	}
    	    	display.dispose ();

    		
			}
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }

	private static void expandStrategyPanel(Composite parent, StrategyPanel[] strategyPanelArray) {
		
		for (int i = 0; i < strategyPanelArray.length; i++) {
			
			// Monto o strategy panel e pego seus parameters
			StrategyPanel strategyPanel = strategyPanelArray[i];
			
			ParameterT[] parameterArray = strategyPanel.getParameterArray();
			
			String strategyPanelTitle = strategyPanel.getTitle();
			Composite group;
			
			if ( strategyPanelTitle == null || "".equals(strategyPanelTitle.trim()) ) {
				group = new Composite(parent, SWT.NONE);
			} else {
				group = new Group(parent, SWT.NONE);
				((Group) group).setText(strategyPanelTitle);
			}
						
	    	FillLayout groupLayout = initializeFillLayout();
	    	
	    	PanelOrientationT.Enum strategyPanelOrientation = strategyPanel.getOrientation();
	    	
	    	if (strategyPanelOrientation.equals(PanelOrientationT.HORIZONTAL)) {
	    		groupLayout.type = SWT.HORIZONTAL;
	    	} else if (strategyPanelOrientation.equals(PanelOrientationT.VERTICAL)) {
	    		groupLayout.type = SWT.VERTICAL;
	    	}
	    	
	    	group.setLayout (groupLayout);
			
			for (int j = 0; j < parameterArray.length; j++) {
				
				ParameterT parameter = parameterArray[j];
				
				// Composite dos parameter, que conterá label + controle
			   	Composite parameterGroup = new Composite(group, SWT.NONE); 
		    	FillLayout parameterGroupLayout = initializeFillLayout();
		    	parameterGroupLayout.type = SWT.HORIZONTAL;
		    	parameterGroup.setLayout(parameterGroupLayout);
		    	//

		    	// Label do parameter
		    	Label parameterLabel = new Label(parameterGroup, SWT.HORIZONTAL);
		    	parameterLabel.setText(parameter.getName());
				//
				
		    	
		    	// Controle do parameter
				ComponentT.Enum component = parameter.getControlType();
					
				if (component.equals(ComponentT.CHECK_BOX)) {
					
				} else if (component.equals(ComponentT.COMBO_BOX)) {
					// Falta amarrar o display value ao value de fato
			    	Combo combo = new Combo (parameterGroup, SWT.READ_ONLY);
			    	
			    	EnumPairT[] enumPairArray = parameter.getEnumPairArray();
			    	List<String> comboItens = new ArrayList<String>();
			    	
			    	for (int k = 0; k < enumPairArray.length; k++) {
			    		comboItens.add(enumPairArray[k].getUiRep());
					}
			    	
					combo.setItems(comboItens.toArray(new String[0]));
					
				} else if (component.equals(ComponentT.RADIO_BUTTON)) {
					
				} else if (component.equals(ComponentT.LABEL)) {
					
				} else if (component.equals(ComponentT.TEXT_FIELD)) {
					
				} else if (component.equals(ComponentT.SLIDER)) {
					
				} else if (component.equals(ComponentT.MULTI_CHECK_BOX)) {
				
				} else if (component.equals(ComponentT.CLOCK)) {

					DateTime clock = new DateTime (parameterGroup, SWT.TIME);
			    	clock.addSelectionListener (new SelectionAdapter () {
			    		public void widgetSelected (SelectionEvent e) {
			    			System.out.println ("start time changed");
			    		}
			    	});
				
				} else if (component.equals(ComponentT.SINGLE_SPINNER)) {
					new Spinner (parameterGroup, SWT.BORDER);					
				} else if (component.equals(ComponentT.DUAL_SPINNER)) {
					
				}
				//
			}
			
			StrategyPanel[] innerStrategyPanelArray = strategyPanel.getStrategyPanelArray();
			
			if (innerStrategyPanelArray.length > 0 ) {
				expandStrategyPanel(group, innerStrategyPanelArray);
			}
			
		}
		
	}

	private static FillLayout initializeFillLayout() {
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 3;
		fillLayout.marginHeight  = 3;
		fillLayout.marginWidth = 3;

		return fillLayout;
	}
    
}
