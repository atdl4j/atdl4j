# Examples

## Swing

```java
// Embed the strategy panel in your own JFrame/JDialog
JFrame frame = new JFrame();
frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
frame.setTitle("atdl4j - The Open-Source Java Solution for FIXatdl (Swing)");

Atdl4jConfiguration config = new SwingAtdl4jConfiguration();                
Atdl4jConfig.setConfig( config );

// Set the required options
Atdl4jOptions options = new Atdl4jOptions();

SwingAtdl4jCompositePanel atdlPanel = new SwingAtdl4jCompositePanel();
JPanel strategyPanel = (JPanel) atdlPanel.buildAtdl4jCompositePanel(frame, options);

String file = "path_to_atdl_file.xml";
atdlPanel.parseFixatdlFile(file);
atdlPanel.loadScreenWithFilteredStrategies();

frame.add(strategyPanel);
frame.setSize(475, 500);
frame.setVisible(true);
```

## SWT

