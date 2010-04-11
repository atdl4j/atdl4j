package org.atdl4j.ui.swt.test;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

public class DebugMouseTrackListener implements MouseTrackListener {

	private Color defaultColor;

	private Color remarkColor;

	private Control control;

	public DebugMouseTrackListener(Control control) {
		this.control = control;
		defaultColor = control.getParent().getBackground();
		remarkColor = new Color(control.getDisplay(), 255, 0, 0);
	}

	public void mouseEnter(MouseEvent ev) {
		control.getParent().setBackground(remarkColor);
	}

	public void mouseExit(MouseEvent ev) {
		control.getParent().setBackground(defaultColor);
	}

	public void mouseHover(MouseEvent ev) {
	}
}
