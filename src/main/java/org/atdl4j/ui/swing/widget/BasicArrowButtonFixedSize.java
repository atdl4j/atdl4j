package org.atdl4j.ui.swing.widget;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.plaf.basic.BasicArrowButton;


public class BasicArrowButtonFixedSize extends BasicArrowButton
{
  
  private static final long serialVersionUID = 20140226L;

  public BasicArrowButtonFixedSize(int direction, Color background, Color shadow,
                           Color darkShadow, Color highlight)
  {
    super(direction, background, shadow, darkShadow, highlight);
  }

  public BasicArrowButtonFixedSize(int direction) {
    super(direction);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(16,9);
  }
}
