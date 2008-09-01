package br.com.investtools.fix.atdl.ui.swt;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
import br.com.investtools.fix.atdl.ui.StrategiesUI;
import br.com.investtools.fix.atdl.ui.StrategiesUIFactory;
import br.com.investtools.fix.atdl.ui.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.widget.DebugMouseTrackListener;

public class SWTBuilder {

	private static final Logger logger = LoggerFactory
			.getLogger(SWTBuilder.class);
	private static TabFolder tabFolder;
	private static Shell shell;

	private static StrategiesDocument strategiesDocument;
	private static StrategiesUI strategiesUI;
	private static StrategyT selectedStrategy;

	public static void main(String[] args) {
		Display display = new Display();
		shell = new Shell(display);
		RowLayout shellLayout = new RowLayout(SWT.VERTICAL);
		shellLayout.fill = true;
		shell.setLayout(shellLayout);

		// header
		Composite headerComposite = new Composite(shell, SWT.NONE);
		GridLayout headerLayout = new GridLayout(2, false);
		headerComposite.setLayout(headerLayout);
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
						strategiesUI = parse(filepath);
					} catch (XmlException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		tabFolder = new TabFolder(shell, SWT.NONE);
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
				strategiesUI = parse(args[0]);
			} catch (XmlException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// footer
		Composite footer = new Composite(shell, SWT.NONE);
		footer.setLayout(new GridLayout(1, true));
		Button submit = new Button(footer, SWT.NONE);
		submit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		submit.setText("Validate!");
		submit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.info("Validating strategy \"{}\"", selectedStrategy
						.getName());
				try {
					StrategyUI strategyUI = strategiesUI.getStrategyUI(selectedStrategy);
					strategyUI.validate();
				} catch (ValidationException ex) {
					MessageBox messageBox = new MessageBox(shell, SWT.OK
							| SWT.ICON_ERROR);
					messageBox.setMessage(ex.getMessage());
					messageBox.open();
				}
			}
		});
		// submit.addSelectionListener(new StrategySubmitListener(pwMap,
		// strategy.getStrategyEditArray()));

		tabFolder.pack();
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	protected static StrategiesUI parse(String filepath) throws XmlException,
			IOException {
		// remove all tabs
		for (TabItem tabs : tabFolder.getItems()) {
			tabs.dispose();
		}

		// parses the XML document and build an object model
		File file = new File(filepath);
		strategiesDocument = StrategiesDocument.Factory.parse(file);
		StrategiesUIFactory factory = new SWTStrategiesUIFactory(tabFolder);
		StrategiesUI strategiesUI = factory.create(strategiesDocument);
		shell.pack();

		// XXX debug
		addDebugMouseTrackListener(tabFolder);

		return strategiesUI;
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
