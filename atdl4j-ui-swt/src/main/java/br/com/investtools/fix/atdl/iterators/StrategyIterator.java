package br.com.investtools.fix.atdl.iterators;

import java.util.Iterator;

import org.w3c.dom.Node;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;

/**
 * A StrategyT node can have ParameterT's and StrategyLayout as children. Using
 * the XmlBeans parser it's not possible to iterate over the elements following
 * the XML order. This iterator allows the user to do this.
 * 
 * <code>
 * Iterator<Object> it = new StrategyIterator(strategy);
 * while (it.hasNext()) {
 * 	Object obj = it.next();
 * 	if (obj instanceof ParameterT) {
 * 		// handle that
 * 	} else if (obj instanceof StrategyLayout) {
 * 		// handle that
 * 	}
 * }
 * </code>
 * 
 * @author tuler
 * 
 */
public class StrategyIterator implements Iterator<Object> {

	private StrategyT s;

	private int i;

	private int paramIndex;

	public StrategyIterator(StrategyT s) {
		this.s = s;
		i = 0;
		paramIndex = 0;
	}

	@Override
	public boolean hasNext() {
		int length = s.getDomNode().getChildNodes().getLength();
		return i < length;
	}

	/**
	 * Returns the current element. The user must check if it's an instanceof
	 * ParameterT or StrategyLayout.
	 */
	@Override
	public Object next() {
		Object ret = null;
		int length = s.getDomNode().getChildNodes().getLength();
		while (i < length && ret == null) {
			// get i-child
			Node node = s.getDomNode().getChildNodes().item(i);

			// se if it matches with current parameter node
			if (s.getParameterArray().length > paramIndex) {
				Node parameterNode = s.getParameterArray(paramIndex)
						.getDomNode();
				if (parameterNode != null && parameterNode.equals(node)) {
					// return it and goes to the next parameter
					ret = s.getParameterArray(paramIndex);
					paramIndex++;
				}
			}

			// se if it matches with layout node
			Node layoutNode = s.getStrategyLayout().getDomNode();
			if (layoutNode != null && layoutNode.equals(node)) {
				// return it
				ret = s.getStrategyLayout();
			}

			i++;
		}
		return ret;
	}

	@Override
	public void remove() {
		throw new RuntimeException("operation not supported");
	}

}
