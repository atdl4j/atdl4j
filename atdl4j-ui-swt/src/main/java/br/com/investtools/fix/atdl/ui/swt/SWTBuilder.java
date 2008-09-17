package br.com.investtools.fix.atdl.ui.swt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.ui.StrategiesUIFactory;
import br.com.investtools.fix.atdl.ui.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.widget.DebugMouseTrackListener;

public class SWTBuilder {

	private static final Logger logger = LoggerFactory
			.getLogger(SWTBuilder.class);

	private static TabFolder tabFolder;
	private static Shell shell;

	private static StrategiesDocument strategiesDocument;
	private static Map<StrategyT, StrategyUI> strategyUI;
	private static StrategyT selectedStrategy;

	public static void main(String[] args) {
		Display display = new Display();
		shell = new Shell(display);
		GridLayout shellLayout = new GridLayout(1, true);
		shell.setLayout(shellLayout);

		// header
		Composite headerComposite = new Composite(shell, SWT.NONE);
		GridLayout headerLayout = new GridLayout(2, false);
		headerComposite.setLayout(headerLayout);
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		final Text filepathText = new Text(headerComposite, SWT.BORDER);
		GridData filepathTextData = new GridData(SWT.FILL, SWT.CENTER, true,
				true);
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
					} catch (XmlException e1) {
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
			}
		});

		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				int index = tabFolder.getSelectionIndex();
				if (strategiesDocument != null) {
					selectedStrategy = strategiesDocument.getStrategies()
							.getStrategyArray(index);
				}

			}
		});

		if (args.length > 0) {
			try {
				parse(args[0]);
			} catch (XmlException e1) {
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
		Composite footer = new Composite(shell, SWT.NONE);
		footer.setLayout(new GridLayout(1, true));
		footer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Button submit = new Button(footer, SWT.NONE);
		submit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		submit.setText("Validate!");
		submit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.info("Validating strategy \"{}\"", selectedStrategy
						.getName());
				try {
					StrategyUI ui = strategyUI.get(selectedStrategy);
					ui.validate();
				} catch (ValidationException ex) {
					MessageBox messageBox = new MessageBox(shell, SWT.OK
							| SWT.ICON_ERROR);
					messageBox.setMessage(ex.getMessage());
					messageBox.open();
				} catch (XmlException ex) {
					MessageBox messageBox = new MessageBox(shell, SWT.OK
							| SWT.ICON_ERROR);
					messageBox.setMessage(ex.getMessage());
					messageBox.open();
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

	protected static void parse(String filepath) throws XmlException,
			IOException {
		// remove all tabs
		for (TabItem tabs : tabFolder.getItems()) {
			tabs.dispose();
		}

		// parses the XML document and build an object model
		File file = new File(filepath);
		strategiesDocument = StrategiesDocument.Factory.parse(file);
		StrategiesUIFactory factory = new SWTStrategiesUIFactory();
		SWTStrategiesUI strategiesUI = (SWTStrategiesUI) factory
				.create(strategiesDocument);
		strategyUI = new HashMap<StrategyT, StrategyUI>();
		for (StrategyT strategy : strategiesDocument.getStrategies()
				.getStrategyArray()) {
			// create TabItem for strategy
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(getText(strategy));

			// create composite
			Composite parent = new Composite(tabFolder, SWT.NONE);
			item.setControl(parent);
			parent.setLayout(new FillLayout());

			strategyUI.put(strategy, strategiesUI.createUI(strategy, parent));
		}
		shell.pack();

		// XXX paint the town red debug
		addDebugMouseTrackListener(tabFolder);
	}

	private static String getText(StrategyT strategy) {
		if (strategy.getUiRep() != null) {
			return strategy.getUiRep();
		} else {
			return strategy.getName();
		}
	}

	private static void addDebugMouseTrackListener(Control control) {
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
