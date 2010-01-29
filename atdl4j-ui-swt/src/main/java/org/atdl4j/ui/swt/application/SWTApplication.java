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

import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.swt.impl.SWTStrategiesUI;
import org.atdl4j.ui.swt.impl.SWTStrategiesUIFactory;
import org.atdl4j.ui.swt.impl.SWTStrategyUI;
import org.atdl4j.ui.swt.test.DebugMouseTrackListener;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SWTApplication {

	private static final Logger logger = LoggerFactory
			.getLogger(SWTApplication.class);

	private static Combo strategiesDropDown;
	private static Composite strategiesPanel;
	private static Shell shell;

	private static StrategiesT strategies;
	private static Map<StrategyT, StrategyUI> strategyUI;
	private static StrategyT selectedStrategy;

	private static Text outputFixMessageText;
	private static Text inputFixMessageText;
	private static Button cxlReplaceModeButton;

	public static void main(String[] args) {
		Display display = new Display();
		shell = new Shell(display);
		GridLayout shellLayout = new GridLayout(1, true);
		shell.setLayout(shellLayout);

		// header
		Composite headerComposite = new Composite(shell, SWT.NONE);
		GridLayout headerLayout = new GridLayout(3, false);
		headerComposite.setLayout(headerLayout);
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		final Text filepathText = new Text(headerComposite, SWT.BORDER);
		GridData filepathTextData = new GridData(SWT.FILL, SWT.CENTER, true,
				true);
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
						// e1.getMessage() is null if there is a JAXB parse
						// error
						String msg = "";
						if (e1.getMessage() != null)
							msg = e1.getMessage();
						else if (e1.getLinkedException() != null
								&& e1.getLinkedException().getMessage() != null) {
							messageBox.setText(e1.getLinkedException()
									.getClass().getSimpleName());
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
						messageBox.setMessage("NumberFormatExeception: "
								+ e1.getMessage());
						messageBox.open();
					}
				}
			}
		});

		/*
		 * Label tzLabel = new Label(headerComposite, SWT.NONE);
		 * tzLabel.setText("Timezone:"); // dropDownList Combo tzDropDown = new
		 * Combo(headerComposite, SWT.READ_ONLY | SWT.BORDER); GridData tzData =
		 * new GridData(SWT.FILL, SWT.CENTER, true, true); tzData.horizontalSpan
		 * = 2; tzDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
		 * true, false)); String tzs =""; for (String tz :
		 * TimeZone.getAvailableIDs()) { tzs += tz + "|" +
		 * TimeZone.getTimeZone(tz).getDisplayName() + "|" +
		 * TimeZone.getTimeZone(tz).getRawOffset() + "*\n"; }
		 * 
		 * for (String tz : TimeZone.getAvailableIDs()) { tzDropDown.add(tz); }
		 * 
		 * Text tzText = new Text(headerComposite, SWT.BORDER);
		 * tzText.setText(tzs);
		 */

		// Strategy selector dropdown
		Composite dropdownComposite = new Composite(shell, SWT.NONE);
		GridLayout dropdownLayout = new GridLayout(2, false);
		dropdownComposite.setLayout(dropdownLayout);
		dropdownComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		// label
		Label strategiesDropDownLabel = new Label(dropdownComposite, SWT.NONE);
		strategiesDropDownLabel.setText("Strategy");
		// dropDownList
		strategiesDropDown = new Combo(dropdownComposite, SWT.READ_ONLY
				| SWT.BORDER);
		strategiesDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		// tooltip
		strategiesDropDown.setToolTipText("Select a Strategy");
		// action listener
		strategiesDropDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int index = strategiesDropDown.getSelectionIndex();
				for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
					((GridData) strategiesPanel.getChildren()[i]
							.getLayoutData()).heightHint = (i != index) ? 0
							: -1;
					((GridData) strategiesPanel.getChildren()[i]
							.getLayoutData()).widthHint = (i != index) ? 0 : -1;
				}
				strategiesPanel.layout();
				shell.pack();
				if (strategies != null) {
					selectedStrategy = strategies.getStrategy().get(index);
				}
			}
		});

		// Main strategies panel
		strategiesPanel = new Composite(shell, SWT.NONE);
		GridLayout strategiesLayout = new GridLayout(1, false);
		strategiesLayout.verticalSpacing = 0;
		strategiesPanel.setLayout(strategiesLayout);
		strategiesPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		// Load in a file if passed into the app arguments
		if (args.length > 0) {
			try {
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
		validateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		validateButton.setText("Validate Output");
		validateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateStrategy();
			}
		});
		outputFixMessageText = new Text(footer, SWT.BORDER);
		outputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		// loader button
		Button loadMessageButton = new Button(footer, SWT.NONE);
		loadMessageButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false));
		loadMessageButton.setText("Load Message");
		loadMessageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadFixMessage();
			}
		});
		inputFixMessageText = new Text(footer, SWT.BORDER);
		inputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));

		// blank button used as spacer
		Button blankButton = new Button(footer, SWT.NONE);
		blankButton.setVisible(false);
		cxlReplaceModeButton = new Button(footer, SWT.CHECK);
		cxlReplaceModeButton.setText("Cxl Replace Mode");
		cxlReplaceModeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (strategyUI != null) {
					for (StrategyUI strategy : strategyUI.values()) {
						((SWTStrategyUI) strategy)
								.setCxlReplaceMode(cxlReplaceModeButton
										.getSelection());
					}
				}
			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	protected static void validateStrategy() {
		if (selectedStrategy == null) {
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
		if (selectedStrategy == null) {
			outputFixMessageText.setText("Please select a strategy");
			return;
		}
		logger.info("Loading FIX string " + inputFixMessageText.getText());
		try {
			StrategyUI ui = strategyUI.get(selectedStrategy);
			ui.setFIXMessage(inputFixMessageText.getText());
			outputFixMessageText.setText("FIX string loaded successfully!");

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
		for (Control control : strategiesPanel.getChildren())
			control.dispose();

		// parses the XML document and build an object model
		JAXBContext jc = JAXBContext.newInstance(StrategiesT.class.getPackage()
				.getName());
		Unmarshaller um = jc.createUnmarshaller();
		try {
			// try to parse as URL
			URL url = new URL(filepath);
			JAXBElement<?> element = (JAXBElement<?>) um.unmarshal(url);
			strategies = (StrategiesT) element.getValue();
		} catch (MalformedURLException e) {
			// try to parse as file
			File file = new File(filepath);
			JAXBElement<?> element = (JAXBElement<?>) um.unmarshal(file);
			strategies = (StrategiesT) element.getValue();
		}

		StrategiesUIFactory factory = new SWTStrategiesUIFactory();
		SWTStrategiesUI strategiesUI = (SWTStrategiesUI) factory
				.create(strategies);
		strategyUI = new HashMap<StrategyT, StrategyUI>();

		for (StrategyT strategy : strategies.getStrategy()) {

			// create composite
			Composite strategyParent = new Composite(strategiesPanel, SWT.NONE);
			strategyParent.setLayout(new FillLayout());
			SWTStrategyUI ui;

			// build strategy and catch strategy-specific errors
			try {
				ui = strategiesUI.createUI(strategy, strategyParent);
			} catch (JAXBException e1) {
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				// e1.getMessage() is null if there is a JAXB parse error
				String msg = "";
				if (e1.getMessage() != null) {
					messageBox.setText("Strategy Load Error");
					msg = e1.getMessage();
				} else if (e1.getLinkedException() != null
						&& e1.getLinkedException().getMessage() != null) {
					messageBox.setText(e1.getLinkedException().getClass()
							.getSimpleName());
					msg = e1.getLinkedException().getMessage();
				}
				messageBox.setMessage("Error in Strategy \""
						+ getStrategyName(strategy) + "\":\n\n" + msg);
				messageBox.open();

				// rollback changes
				strategyParent.dispose();

				// skip to next strategy
				continue;
			}

			// create dropdown item for strategy
			strategiesDropDown.add(getStrategyName(strategy));
			strategyUI.put(strategy, ui);
		}

		if (strategiesDropDown.getItem(0) != null)
			strategiesDropDown.select(0);

		// TODO: This flashes all parameters on the screen when we first load
		// There's got to be a better way...
		shell.pack();
		for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
			((GridData) strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != 0) ? 0
					: -1;
			((GridData) strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != 0) ? 0
					: -1;
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
		if (!(control.getClass().equals(Composite.class) || control.getClass()
				.equals(Group.class))) {
			control.addMouseTrackListener(new DebugMouseTrackListener(control));
		}
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				addDebugMouseTrackListener(child);
			}
		}
	}

}
