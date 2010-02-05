package org.atdl4j.ui.swt.application;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.apache.log4j.Logger;
import org.atdl4j.data.FIXMessageParser;
import org.atdl4j.data.InputAndFilterData;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.validation.AbstractOperatorValidationRule;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.swt.impl.SWTFactory;
import org.atdl4j.ui.swt.impl.SWTStrategiesUI;
import org.atdl4j.ui.swt.impl.SWTStrategiesUIFactory;
import org.atdl4j.ui.swt.impl.SWTStrategyUI;
import org.atdl4j.ui.swt.test.DebugMouseTrackListener;
import org.atdl4j.ui.swt.widget.RadioButtonListWidget;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;

public class SWTApplication {

	private static final Logger logger = Logger.getLogger(SWTApplication.class);
	
	private static Combo strategiesDropDown;
	private static Composite strategiesPanel;
	private static Shell shell;

	private static StrategiesT strategies;
	private static Map<StrategyT, StrategyUI> strategyUI;
	private static StrategyT selectedStrategy;
	
	private static Text outputFixMessageText;
	private static Text inputFixMessageText;
	private static Button cxlReplaceModeButton;
// 1/20/2010 Scott Atwell	
	private static Button debugModeButton;

// 1/17/2010 Scott Atwell Added 
	private static InputAndFilterData inputAndFilterData = new InputAndFilterData();	
	
	public static void main(String[] args) {
		Display display = new Display();
		shell = new Shell(display);
		GridLayout shellLayout = new GridLayout(1, true);
		shell.setLayout(shellLayout);
	
		// header
		Composite headerComposite = new Composite(shell, SWT.NONE);
		GridLayout headerLayout = new GridLayout(3, false);
		headerComposite.setLayout(headerLayout);
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		final Text filepathText = new Text(headerComposite, SWT.BORDER);
		GridData filepathTextData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		filepathTextData.horizontalSpan = 2;
		filepathText.setLayoutData(filepathTextData);
		Button browseButton = new Button(headerComposite, SWT.NONE);
		browseButton.setText("...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				String filepath = dialog.open();
				if (filepath != null) {
					filepathText.setText(filepath);
					try {
						parse(filepath);
					} catch (JAXBException e1) {
						MessageBox messageBox = new MessageBox(shell, SWT.OK
								| SWT.ICON_ERROR);
						// e1.getMessage() is null if there is a JAXB parse error 
						String msg = "";
						if (e1.getMessage() != null) msg = e1.getMessage();
						else if (e1.getLinkedException() != null && 
								e1.getLinkedException().getMessage() != null)
						{
							messageBox.setText(e1.getLinkedException().getClass().getSimpleName());
							msg = e1.getLinkedException().getMessage();
						}
						messageBox.setMessage(msg);
						messageBox.open();
					} catch (IOException e1) {
						MessageBox messageBox = new MessageBox(shell, SWT.OK
								| SWT.ICON_ERROR);
						messageBox.setMessage(e1.getMessage());
						messageBox.open();
					} catch (NumberFormatException e1) {
						MessageBox messageBox = new MessageBox(shell, SWT.OK
								| SWT.ICON_ERROR);
						messageBox.setMessage("NumberFormatExeception: " + e1.getMessage());
						messageBox.open();
					}
				}
			}
		});

		/*
		Label tzLabel = new Label(headerComposite, SWT.NONE);
		tzLabel.setText("Timezone:");
		// dropDownList
		Combo tzDropDown = new Combo(headerComposite, SWT.READ_ONLY | SWT.BORDER);
		GridData tzData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		tzData.horizontalSpan = 2;
		tzDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,	false));
		String tzs ="";
		for (String tz : TimeZone.getAvailableIDs())
		{
			tzs += tz + "|" + TimeZone.getTimeZone(tz).getDisplayName() + "|" + TimeZone.getTimeZone(tz).getRawOffset() + "*\n";
		}
		
		for (String tz : TimeZone.getAvailableIDs())
		{
			tzDropDown.add(tz);
		}
	
		Text tzText = new Text(headerComposite, SWT.BORDER);
		tzText.setText(tzs);*/
		
		
		
		// Strategy selector dropdown
		Composite dropdownComposite = new Composite(shell, SWT.NONE);
		GridLayout dropdownLayout = new GridLayout(2, false);
		dropdownComposite.setLayout(dropdownLayout);
		dropdownComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// label
		Label strategiesDropDownLabel = new Label(dropdownComposite, SWT.NONE);
		strategiesDropDownLabel.setText("Strategy");
		// dropDownList
		strategiesDropDown = new Combo(dropdownComposite, SWT.READ_ONLY | SWT.BORDER);
		strategiesDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,	false));
		// tooltip
		strategiesDropDown.setToolTipText("Select a Strategy");
		// action listener
		strategiesDropDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int index = strategiesDropDown.getSelectionIndex();
//TODO 1/16/2010 Scott Atwell moved to selectDropDownStrategy()		
//				for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
//					((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != index) ? 0 : -1;
//					((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != index) ? 0 : -1;
//				}
//				strategiesPanel.layout();
//				shell.pack();
//				if (strategies != null) {
//					selectedStrategy = strategies.getStrategy().get(index);
//				}
				selectDropDownStrategy( index );
			}
		});		
		
		// Main strategies panel
		strategiesPanel = new Composite(shell, SWT.NONE);
		GridLayout strategiesLayout = new GridLayout(1, false);
		strategiesLayout.verticalSpacing = 0;
		strategiesPanel.setLayout(strategiesLayout);
		strategiesPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Load in a file if passed into the app arguments
		if (args.length > 0) {
			try {
//TODO 1/18/2010 Scott Atwell added BELOW
				getInputAndFilterData().init();
				
				if (args.length >= 2)
				{
					// -- InputCxlReplaceMode = args[1] (eg "true" or "false")
					logger.info("args[1]: " + args[1] + " Boolean.parseBoolean() as inputCxlReplaceMode");
					getInputAndFilterData().setInputCxlReplaceMode( Boolean.parseBoolean( args[1] ) );
				}
				
				if ( args.length >= 3)
				{
					// -- InputHiddenFieldNameValueMap = args[2] (eg "FIX_OrderQty=10000|FIX_Side=1|FIX_OrdType=1") 
					String tempStringToParse = args[2];
					logger.info("args[2]: " + tempStringToParse + " parse as InputHiddenFieldNameValueMap (eg \"FIX_OrderQty=10000|FIX_Side=1|FIX_OrdType=1\")");
					String[] tempFieldAndValuesArray = tempStringToParse.split( "\\|" );
					if ( tempFieldAndValuesArray != null )
					{
						Map<String, String> tempInputHiddenFieldNameValueMap = new HashMap<String, String>();
						for (String tempFieldAndValue : tempFieldAndValuesArray )
						{
							String[] tempCombo = tempFieldAndValue.split( "=" );
							if ( ( tempCombo != null ) && ( tempCombo.length == 2 ) )
							{
								tempInputHiddenFieldNameValueMap.put( tempCombo[0], tempCombo[1] );
							}
						}
						
						logger.info("InputHiddenFieldNameValueMap: " + tempInputHiddenFieldNameValueMap);
						getInputAndFilterData().addMapToInputHiddenFieldNameValueMap( tempInputHiddenFieldNameValueMap );
					}
				}
//TODO 1/18/2010 Scott Atwell added ABOVE

				
				parse(args[0]);
			} catch (JAXBException e1) {
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				messageBox.setMessage(e1.getMessage());
				messageBox.open();
			} catch (IOException e1) {
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				messageBox.setMessage(e1.getMessage());
				messageBox.open();
			}
		}

		// footer
		Group footer = new Group(shell, SWT.NONE);
		footer.setText("Debug");
		footer.setLayout(new GridLayout(2, false));
		footer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// validator button
		Button validateButton = new Button(footer, SWT.NONE);
		validateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		validateButton.setText("Validate Output");
		validateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateStrategy();
			}
		});
		outputFixMessageText = new Text(footer, SWT.BORDER);
		outputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		// loader button
		Button loadMessageButton = new Button(footer, SWT.NONE);
		loadMessageButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		loadMessageButton.setText("Load Message");
		loadMessageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadFixMessage();
			}
		});
		inputFixMessageText = new Text(footer, SWT.BORDER);
		inputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// blank button used as spacer
		Button blankButton = new Button(footer, SWT.NONE);
		blankButton.setVisible(false);
		cxlReplaceModeButton = new Button(footer, SWT.CHECK);		
		cxlReplaceModeButton.setText("Cxl Replace Mode");
		
//TODO 1/18/2010 Scott Atwell added
		if ( getInputAndFilterData() != null )
		{
			cxlReplaceModeButton.setSelection( getInputAndFilterData().getInputCxlReplaceMode() );
		}
		
		cxlReplaceModeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (strategyUI != null)
				{
					for (StrategyUI strategy : strategyUI.values())
					{
						((SWTStrategyUI)strategy).setCxlReplaceMode(cxlReplaceModeButton.getSelection());
					}
				}
			}
		});
		
//TODO 1/20/2010 Scott Atwell BELOW	
		//blank button used as spacer
		Button blankButton2 = new Button(footer, SWT.NONE);
		blankButton2.setVisible(false);
		debugModeButton = new Button(footer, SWT.CHECK);		
		debugModeButton.setText("Debug Mode");

		debugModeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyLoggingLevel();
			}
		});
//TODO 1/20/2010 Scott Atwell ABOVE


		shell.pack();
		shell.open();

//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
// 2/3/2010 Scott Atwell added pop-up to display the Exception before the app crashes		
		while (!shell.isDisposed()) 
		{
			try
			{
				if (!display.readAndDispatch())
				{
					display.sleep();
				}	
			}
			catch (Exception e)
			{
				logger.warn( "Fatal Exception encountered", e );
				MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				if ( e.getMessage() != null )
				{
					messageBox.setMessage(e.getMessage());
				}
				else
				{
					messageBox.setMessage( e.toString() );
				}
				messageBox.open();
			}
		}
		
		display.dispose();
	}

	protected static void validateStrategy() {
		if (selectedStrategy == null)
		{
			outputFixMessageText.setText("Please select a strategy");
			return;
		}
		logger.info("Validating strategy " + selectedStrategy.getName());
		try {
			StrategyUI ui = strategyUI.get(selectedStrategy);
			ui.validate();
			outputFixMessageText.setText(ui.getFIXMessage());

		} catch (ValidationException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (JAXBException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (Exception ex) {
			outputFixMessageText.setText("");
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage("Generic exception occurred: "
					+ ex.toString());
			messageBox.open();
			ex.printStackTrace();
		}
	}
	
	protected static void loadFixMessage() {
		//TODO 1/16/2010 Scott Atwell replaced BELOW	
		logger.info("Loading FIX string " + inputFixMessageText.getText());
		try {
			if ( ( getInputAndFilterData() != null ) &&
				  ( getInputAndFilterData().getInputSelectStrategyName() != null ) )
			{
				logger.info("getInputAndFilterData().getInputSelectStrategyName(): " + getInputAndFilterData().getInputSelectStrategyName());			
				logger.info("Invoking selectDropDownStrategy: " + getInputAndFilterData().getInputSelectStrategyName() );							
				selectDropDownStrategy( getInputAndFilterData().getInputSelectStrategyName() );
			}
			else  // Match getWireValue() and then use getUiRep() if avail, otherwise getName()
			{
				if ( ( strategies != null ) && ( strategies.getStrategyIdentifierTag() != null ) )
				{
					String strategyWireValue = FIXMessageParser.extractFieldValueFromFIXMessage( inputFixMessageText.getText(), strategies.getStrategyIdentifierTag().intValue() );
					
	logger.info("strategyWireValue: " + strategyWireValue);			
					if ( strategyWireValue != null )
					{
						if ( strategies.getStrategy() != null )
						{
							for ( StrategyT tempStrategy : strategies.getStrategy() )
							{
								if ( strategyWireValue.equals( tempStrategy.getWireValue() ) )
								{
									if ( tempStrategy.getUiRep() != null )
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getUiRep(): " + tempStrategy.getUiRep() );							
										selectDropDownStrategy( tempStrategy.getUiRep() );
									}
									else
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getName(): " + tempStrategy.getName() );							
										selectDropDownStrategy( tempStrategy.getName() );
									}
									break;
								}
							}
						}
					}
				}
			}

			if (selectedStrategy == null)
			{
				outputFixMessageText.setText("Please select a strategy");
				return;
			}
// 1/16/2010 Scott Atwell		logger.info("Loading FIX string " + inputFixMessageText.getText());
// 1/16/2010 Scott Atwell		try {
			StrategyUI ui = strategyUI.get(selectedStrategy);
//TODO 1/19/2010 Scott Atwell BEFORE			ui.setFIXMessage(inputFixMessageText.getText());
//TODO 1/19/2010 Scott Atwell BEFORE			outputFixMessageText.setText("FIX string loaded successfully!");
//TODO 1/19/2010 Scott Atwell AFTER - BELOW
			// -- Note available strategies may be filtered due to SecurityTypes, Markets, or Region/Country rules --  
			if ( ui != null )
			{
				ui.setFIXMessage(inputFixMessageText.getText());
				outputFixMessageText.setText("FIX string loaded successfully!");
			}
			else
			{
				outputFixMessageText.setText( selectedStrategy.getName() + " is not available.");
			}
//TODO 1/19/2010 Scott Atwell AFTER - ABOVE			
		} catch (ValidationException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (JAXBException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (Exception ex) {
			outputFixMessageText.setText("");
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage("Generic exception occurred: "
					+ ex.toString());
			messageBox.open();
			ex.printStackTrace();
		}
	}
	
	protected static void parse(String filepath) throws JAXBException,
			IOException, NumberFormatException {
		
		// remove all dropdown items
		strategiesDropDown.removeAll();
		// remove all strategy panels
		for (Control control : strategiesPanel.getChildren()) control.dispose();
	
		// parses the XML document and build an object model
		JAXBContext jc = JAXBContext.newInstance(StrategiesT.class.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
		try {
			// try to parse as URL
			URL url = new URL(filepath);
			JAXBElement<?> element = (JAXBElement<?>)um.unmarshal(url);
			strategies = (StrategiesT)element.getValue();
		} catch (MalformedURLException e) {
			// try to parse as file
			File file = new File(filepath);		
			JAXBElement<?> element = (JAXBElement<?>)um.unmarshal(file);
			strategies = (StrategiesT)element.getValue();
		}
		
		StrategiesUIFactory factory = new SWTStrategiesUIFactory();
		SWTStrategiesUI strategiesUI = (SWTStrategiesUI) factory.create(strategies);
		strategyUI = new HashMap<StrategyT, StrategyUI>();
		
		for (StrategyT strategy : strategies.getStrategy()) {

//TODO 1/18/2010 Scott Atwell Added BELOW
			if ( getInputAndFilterData().isStrategySupported( strategy ) == false)
			{
				logger.info("Excluding strategy: " + strategy.getName() + " as inputAndFilterData.isStrategySupported() returned false." );
				continue; // skip it 
			}
//TODO 1/18/2010 Scott Atwell Added ABOVE
			
			// create composite
			Composite strategyParent = new Composite(strategiesPanel, SWT.NONE);
			strategyParent.setLayout(new FillLayout());
			SWTStrategyUI ui;
			
			// build strategy and catch strategy-specific errors
			try {
//TODO 1/17/2010 Scott Atwell				ui = strategiesUI.createUI(strategy, strategyParent);	
				ui = strategiesUI.createUI(strategy, strategyParent, inputAndFilterData.getInputHiddenFieldNameValueMap());	
			} catch (JAXBException e1) {
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				// e1.getMessage() is null if there is a JAXB parse error 
				String msg = "";
				if (e1.getMessage() != null)
				{
					messageBox.setText("Strategy Load Error");
					msg = e1.getMessage();
				}
				else if (e1.getLinkedException() != null && 
					     e1.getLinkedException().getMessage() != null)
				{
					messageBox.setText(e1.getLinkedException().getClass().getSimpleName());
					msg = e1.getLinkedException().getMessage();
				}
				messageBox.setMessage("Error in Strategy \"" + getStrategyName(strategy) + "\":\n\n" +msg);
				messageBox.open();
				
				// rollback changes
				strategyParent.dispose();
				
				// skip to next strategy
				continue;
			}
			
			// create dropdown item for strategy
			strategiesDropDown.add(getStrategyName(strategy));
			strategyUI.put(strategy, ui);
			
//TODO Scott Atwell 1/17/2010 Added BEGIN
			ui.setCxlReplaceMode( inputAndFilterData.getInputCxlReplaceMode() );
//TODO Scott Atwell 1/17/2010 Added END
		}

		
		
		
		
		if (strategiesDropDown.getItem(0) != null) strategiesDropDown.select(0);

		// TODO: This flashes all parameters on the screen when we first load
		// There's got to be a better way...
		shell.pack();
		for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != 0) ? 0 : -1;
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != 0) ? 0 : -1;
		}
		strategiesPanel.layout();
		if (strategies != null) {
			selectedStrategy = strategies.getStrategy().get(0);
		}
		shell.pack();
	}

	private static String getStrategyName(StrategyT strategy) {
		if (strategy.getUiRep() != null) {
			return strategy.getUiRep();
		} else {
			return strategy.getName();
		}
	}

	public static void addDebugMouseTrackListener(Control control) {
		if (!(control.getClass().equals(Composite.class) || 
		      control.getClass().equals(Group.class))) {
			control.addMouseTrackListener(new DebugMouseTrackListener(control));
		}
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				addDebugMouseTrackListener(child);
			}
		}
	}

	//TODO 1/16/2010 Scott Atwell added
	public static void selectDropDownStrategy(int index) {
		strategiesDropDown.select( index );
		
		// below moved from and called by strategiesDropDown.widgetSelected(SelectionEvent event)
		for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != index) ? 0 : -1;
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != index) ? 0 : -1;
		}
		strategiesPanel.layout();
		shell.pack();
		if (strategies != null) {
// 2/1/2010 Scott Atwell - CANNOT DO THIS as startegies.getStrategy() List contains ALL defined strategies (UNFILTERED) and thus NOT 1-for-1
//			selectedStrategy = strategies.getStrategy().get(index);
			String tempSelectedDropDownName = strategiesDropDown.getItem( index );
			selectedStrategy = null; 
			for ( StrategyT tempStrategy : strategies.getStrategy() )
			{
				if ( ( ( tempStrategy.getUiRep() != null ) && ( tempStrategy.getUiRep().equals( tempSelectedDropDownName ) ) ) ||
					  ( ( tempStrategy.getUiRep() == null ) && ( tempStrategy.getName().equals( tempSelectedDropDownName ) ) ) )
				{
					selectedStrategy = tempStrategy;
					break;
				}
			}
		}
	}

//TODO 1/16/2010 Scott Atwell added
//TODO !!!! may have issue with "either or" logic for uiRep vs. name attributes (and fact dropdown will list uiRep) @see getStrategyName()
	// public static void selectDropDownStrategy(StrategyT strategy) {
	public static void selectDropDownStrategy(String strategyName) {
		for (int i = 0; i < strategiesDropDown.getItemCount(); i++) {
			if ( strategyName.equals( strategiesDropDown.getItem( i ) ) ) {
				selectDropDownStrategy( i );
				return;
			}
		}
	}

//TODO 1/20/2010 Scott Atwell added	
	private static void applyLoggingLevel()
	{
		org.apache.log4j.Level tempLevel = org.apache.log4j.Level.INFO; 
		if ( debugModeButton.getSelection() )
		{
			tempLevel = org.apache.log4j.Level.DEBUG;
//			tempLevel = org.apache.log4j.Level.TRACE;
		}
		
/***		
		org.apache.log4j.Logger.getLogger( SWTApplication.class ).setLevel( tempLevel );
		
		org.apache.log4j.Logger.getLogger( AbstractOperatorValidationRule.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( InputAndFilterData.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( SWTFactory.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( AbstractStrategyUI.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( RadioButtonListWidget.class ).setLevel( tempLevel );
***/
		org.apache.log4j.Logger.getLogger( "org.atdl4j" ).setLevel( tempLevel );

	}

	/**
	 * @return the inputAndFilterData
	 */
	public static InputAndFilterData getInputAndFilterData()
	{
		return inputAndFilterData;
	}

	/**
	 * @param aInputAndFilterData the inputAndFilterData to set
	 */
	public static void setInputAndFilterData(InputAndFilterData aInputAndFilterData)
	{
		inputAndFilterData = aInputAndFilterData;
	}
	
}
