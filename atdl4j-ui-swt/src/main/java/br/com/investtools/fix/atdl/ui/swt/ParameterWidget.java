package br.com.investtools.fix.atdl.ui.swt;

import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;

/**
 * Interface que representa um parametro de um algoritmo FIXatdl com sua
 * representacao em forma de Widget SWT.
 * 
 * @author renato.gallart
 * 
 */
public interface ParameterWidget<E extends Comparable<E>> {

	public Widget createWidget(Composite parent, ParameterT parameter, int style) throws XmlException;

	public E getValue();
	
	public void setValue(E value);

	public ParameterT getParameter();
	
	public List<Control> getControls();

	public E convertValue(String value) throws XmlException;

	public String getFIXValue();
	
	public void generateStateRuleListener(Listener listener);
	
}
