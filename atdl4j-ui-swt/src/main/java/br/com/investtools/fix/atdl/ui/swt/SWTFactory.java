package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
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
			c.setLayout(createLayout(panel.getOrientation()));
			
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
					parameterWidgets.put(parameter.getName(), pw);
				} else {
					// XXX: maybe parameter type is not supported
				}
			}

			return parameterWidgets;
		} else {
			// use group
			Group c = new Group(parent, style);
			c.setText(panel.getTitle());
			c.setLayout(createLayout(panel.getOrientation()));

			// build panels and parameters widgets recursively
			for (StrategyPanel p : panel.getStrategyPanelArray()) {
				parameterWidgets.putAll(create(c, p, style));
			}
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

	private Layout createLayout(PanelOrientationT.Enum orientation) {
		if (orientation == PanelOrientationT.HORIZONTAL) {
			RowLayout l = new RowLayout(SWT.HORIZONTAL);
			//FillLayout l = new FillLayout(SWT.HORIZONTAL);
			l.marginHeight = 3;
			l.marginWidth = 3;
			l.spacing = 3;
			return l;
		} else if (orientation == PanelOrientationT.VERTICAL) {
			RowLayout l = new RowLayout(SWT.VERTICAL);
			//FillLayout l = new FillLayout(SWT.VERTICAL);
			l.marginHeight = 3;
			l.marginWidth = 3;
			l.spacing = 3;
			return l;
		}
		return null;
	}
}
