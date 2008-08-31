package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.layout.xmlbeans.PanelOrientationT;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;

public class SWTFactory implements WidgetFactory, PanelFactory {

	private ParameterWidgetFactory parameterWidgetFactory;

	public SWTFactory() {
		parameterWidgetFactory = new ParameterWidgetFactoryImpl();
	}

	@Override
	public ParameterWidget<?> create(Composite parent, ParameterT parameter,
			int style) {
		ParameterWidget<?> parameterWidget = parameterWidgetFactory
				.create(parameter);
		if (parameterWidget != null) {
			parameterWidget.createWidget(parent, parameter, style);

		} else {
			// throw new UnsupportedOperationException("ControlType not
			// supported");
			return null;
		}
		return parameterWidget;
	}

	@Override
	public Map<String, ParameterWidget<?>> create(Composite parent,
			StrategyPanel panel, int style) {
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

			// build panels widgets recursively
			for (StrategyPanel p : panel.getStrategyPanelArray()) {
				parameterWidgets.putAll(create(c, p, style));
			}

			// build parameters widgets recursively
			for (ParameterT parameter : panel.getParameterArray()) {
				ParameterWidget<?> pw = create(c, parameter, style);
				if (pw != null) {
					// keep parameter in Map, identified by its name
					parameterWidgets.put(parameter.getName(), pw);
				} else {
					// XXX: maybe parameter type is not supported
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
				if (pw != null) {
					parameterWidgets.put(parameter.getName(), pw);
				} else {
					// XXX: maybe parameter type is not supported
				}
			}

			// build parameters widgets recursively
			return parameterWidgets;
		}

	}

	private static Object createLayoutData(Composite c) {
		// if parent is a strategy panel
		if (c.getParent().getData() instanceof StrategyPanel) {
			StrategyPanel parentPanel = (StrategyPanel) c.getParent().getData();
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
			c.setLayoutData(layoutData);

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

			// RowLayout l = new RowLayout(SWT.HORIZONTAL);
			// FillLayout l = new FillLayout(SWT.HORIZONTAL);
			// l.marginHeight = 3;
			// l.marginWidth = 3;
			// l.spacing = 3;
			return l;
		} else if (orientation == PanelOrientationT.VERTICAL) {
			GridLayout l = new GridLayout(2, false);
			// RowLayout l = new RowLayout(SWT.VERTICAL);
			// FillLayout l = new FillLayout(SWT.VERTICAL);
			// l.marginHeight = 3;
			// l.marginWidth = 3;
			// l.spacing = 3;
			return l;
		}
		return null;
	}
}
