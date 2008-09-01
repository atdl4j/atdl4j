package br.com.investtools.fix.atdl.ui.swt;

import org.eclipse.swt.widgets.Composite;
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

	public Widget createWidget(Composite parent, ParameterT parameter, int style);

	public E getValue();

	public ParameterT getParameter();

	public E convertValue(String value);

	public String getFIXValue();
}
