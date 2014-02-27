package org.atdl4j.ui.swing.app.impl;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractStrategyDescriptionPanel;

/**
 * Represents the Swing-specific Strategy Description GUI component.
 * @author Scott Atwell
 * @version October 5, 2010
 */
public class SwingStrategyDescriptionPanel
    extends AbstractStrategyDescriptionPanel
{
  // JPanel as we need TitledBorder private Container container;
  private JPanel container;

  private JTextPane strategyDescription;

  public Object buildStrategyDescriptionPanel(Object parentOrShell,
                                              Atdl4jOptions atdl4jOptions)
  {
    return buildStrategyDescriptionPanel((Window) parentOrShell, atdl4jOptions);
  }

  public JPanel buildStrategyDescriptionPanel(Window aParentContainer,
                                              Atdl4jOptions atdl4jOptions)
  {
    setAtdl4jOptions(atdl4jOptions);

    container = new JPanel(new BorderLayout());
    container.setBorder(new TitledBorder("Strategy Description"));

    strategyDescription = new JTextPane();
    strategyDescription.setContentType("text/html");
    strategyDescription.setFont(new JLabel().getFont());
    strategyDescription.setEditable(false);
    strategyDescription.setFont(new JLabel().getFont());

    JScrollPane tempScrollPane = new JScrollPane(strategyDescription);
    container.add(tempScrollPane, BorderLayout.CENTER);

    return container;
  }

  /*
   * (non-Javadoc)
   * @see
   * org.atdl4j.ui.app.AbstractStrategyDescriptionPanel#setStrategyDescriptionText
   * (java.lang.String)
   */
  protected void setStrategyDescriptionText(String aText) {
    if (strategyDescription != null) {
      String newText = aText.replaceAll("&apos;", "&#39;");
      String htmlText = "<html>" + newText + "</html>";
      strategyDescription.setText(htmlText);
      strategyDescription.setSelectionStart(0);
      strategyDescription.setSelectionEnd(0);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.atdl4j.ui.app.StrategyDescriptionPanel#setVisible(boolean)
   */
  @Override
  public void setVisible(boolean aVisible) {
    if (container != null) {
      container.setVisible(aVisible);
    }
  }
}
