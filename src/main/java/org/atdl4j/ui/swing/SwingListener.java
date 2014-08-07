package org.atdl4j.ui.swing;

import java.awt.event.ActionListener;

import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;

import org.atdl4j.data.ValidationRule;

public interface SwingListener extends ActionListener, ChangeListener, ListSelectionListener, DocumentListener {
    
    public void handleEvent();
    
    public SwingWidget<?> getAffectedWidget();

    public ValidationRule getRule();

    public void setCxlReplaceMode(boolean cxlReplaceMode);

    public void handleLoadFixMessageEvent();
    
}