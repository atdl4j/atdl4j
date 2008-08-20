package br.com.investtools.fix.atdl.ui.swt;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument.Strategies;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;
import br.com.investtools.fix.atdl.valid.xmlbeans.StrategyEditDocument.StrategyEdit;

public class SWTBuilder {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("SWTBuilder <filename>");
			return;
		}

		String pathname = args[0];
		File file = new File(pathname);
		StrategiesDocument strategiesDocument;

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		SWTFactory factory = new SWTFactory();

		try {
			strategiesDocument = StrategiesDocument.Factory.parse(file);
			Strategies strategies = strategiesDocument.getStrategies();

			// create tab folder for all strategies
			TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);

			for (StrategyT strategy : strategies.getStrategyArray()) {

				// create tab item for strategy
				TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
				tabItem.setText(getText(strategy));
				Composite parent = new Composite(tabFolder, SWT.NONE);
				parent.setLayout(new FillLayout());

				Map<String, ParameterWidget> pwMap = new HashMap<String, ParameterWidget>();
				try {
					// build panels
					for (StrategyPanel panel : strategy.getStrategyLayout()
							.getStrategyPanelArray()) {
						pwMap.putAll(factory.create(parent, panel, SWT.NONE));
					}

					// build parameters
					for (ParameterT parameter : strategy.getParameterArray()) {
						pwMap.put(parameter.getName(), factory.create(parent,
								parameter, SWT.NONE));
					}

					parent.pack();
					tabItem.setControl(parent);
					
					// handles validation rules
					for (StrategyEdit se : strategy.getStrategyEditArray()) {
						Edit edit = se.getEdit();
						if (edit.getOperator() != null) {
							String field = edit.getField();
							ParameterWidget pw = pwMap.get(field);
							if (pw != null) {
								// TODO
							} else {
								// XXX: reference to a field not found
							}

						} else if (edit.getLogicOperator() != null) {
							// TODO
						} else {
							// XXX: invalid tag
						}
					}

					// handle flows
					// TODO

					// generate FIX message
					System.out.println(pwMap.toString());
					for (ParameterWidget pw : pwMap.values()) {
						System.out.println(pw.getFIXValue());
					}

				} catch (UnsupportedOperationException e) {
					// yahoo!
				}

			}

			tabFolder.pack();
			shell.pack();
			shell.open();

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getText(StrategyT strategy) {
		if (strategy.getUiRep() != null) {
			return strategy.getUiRep();
		}
		return strategy.getName();
	}
}
