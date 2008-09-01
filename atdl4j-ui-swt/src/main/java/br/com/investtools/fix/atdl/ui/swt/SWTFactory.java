package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.iterators.StrategyPanelIterator;
import br.com.investtools.fix.atdl.layout.xmlbeans.PanelOrientationT;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;

public class SWTFactory implements WidgetFactory, PanelFactory {

	private ParameterWidgetFactory parameterWidgetFactory;

	public SWTFactory() {
		parameterWidgetFactory = new ParameterWidgetFactoryImpl();
	}

	@Override
	public ParameterWidget<?> create(Composite parent, ParameterT parameter,
			int style) throws XmlException {
		ParameterWidget<?> parameterWidget = null;

		try {
			parameterWidget = parameterWidgetFactory.create(parameter);
			parameterWidget.createWidget(parent, parameter, style);

		} catch (XmlException e) {
			throw e;
		}
		return parameterWidget;
	}

	@Override
	public Map<String, ParameterWidget<?>> create(Composite parent,
			StrategyPanel panel, int style) throws XmlException {
		Map<String, ParameterWidget<?>> parameterWidgets = new HashMap<String, ParameterWidget<?>>();

		if (panel.getTitle() == null || "".equals(panel.getTitle())) {
			// use simple composite
			Composite c = new Composite(parent, style);

			// keep StrategyPanel in data object
			c.setData(panel);

			// create layout
			c.setLayout(createLayout(panel));
			c.setLayoutData(createLayoutData(c));

			// XXX debug
			c.setToolTipText(panel.toString());

			Iterator<Object> it = new StrategyPanelIterator(panel);
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof ParameterT) {
					ParameterT parameter = (ParameterT) obj;
					ParameterWidget<?> pw;
					try {
						pw = create(c, parameter, style);
					} catch (XmlException e) {
						throw e;
					}

					parameterWidgets.put(parameter.getName(), pw);

				} else if (obj instanceof StrategyPanel) {
					StrategyPanel p = (StrategyPanel) obj;
					parameterWidgets.putAll(create(c, p, style));
				}
			}

			return parameterWidgets;
		} else {
			// use group
			Group c = new Group(parent, style);

			// keep StrategyPanel in data object
			c.setData(panel);

			c.setText(panel.getTitle());
			c.setLayout(createLayout(panel));
			c.setLayoutData(createLayoutData(c));

			// build panels widgets recursively
			for (StrategyPanel p : panel.getStrategyPanelArray()) {
				parameterWidgets.putAll(create(c, p, style));
			}

			// build parameters widgets recursively
			for (ParameterT parameter : panel.getParameterArray()) {
				ParameterWidget<?> pw = create(c, parameter, style);
				parameterWidgets.put(parameter.getName(), pw);
			}
			return parameterWidgets;
		}

	}

	private static Object createLayoutData(Composite c) {
		// if parent is a strategy panel
		if (c.getParent().getData() instanceof StrategyPanel) {
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
			c.setLayoutData(layoutData);

			StrategyPanel parentPanel = (StrategyPanel) c.getParent().getData();
			// if parent orientation is vertical, make this panel span 2 columns
			if (parentPanel.getOrientation() == PanelOrientationT.VERTICAL) {
				layoutData.horizontalSpan = 2;
			}
			return layoutData;
		}
		return null;
	}

	private Layout createLayout(StrategyPanel panel) {
		PanelOrientationT.Enum orientation = panel.getOrientation();
		if (orientation == PanelOrientationT.HORIZONTAL) {
			int parameterCount = panel.getParameterArray() != null ? panel
					.getParameterArray().length : 0;
			int panelCount = panel.getStrategyPanelArray() != null ? panel
					.getStrategyPanelArray().length : 0;

			// make 2 columns for each parameter and 1 column for each sub-panel
			GridLayout l = new GridLayout(parameterCount * 2 + panelCount,
					false);

			return l;
		} else if (orientation == PanelOrientationT.VERTICAL) {
			GridLayout l = new GridLayout(2, false);
			return l;
		}
		return null;
	}
}
