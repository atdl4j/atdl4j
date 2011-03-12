package org.atdl4j.ui.swing.app.impl;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractAtdl4jTesterApp;
import org.atdl4j.ui.swing.config.SwingAtdl4jConfiguration;

/**
 * Represents the Swing-specific "TesterApp" with a main() line.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel
 *      ->AbstractAtdl4jCompositePanel layering structure. *
 * 
 */
public class SwingAtdl4jTesterApp extends AbstractAtdl4jTesterApp {
	public final Logger	logger	= Logger.getLogger(SwingAtdl4jTesterApp.class);
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
  	try {
	     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } 
    catch (Exception e) {
	     e.printStackTrace();
    }
		
		SwingAtdl4jTesterApp tempSwingAtdl4jTesterApp = new SwingAtdl4jTesterApp();
		try {
			tempSwingAtdl4jTesterApp.mainLine(args);
		}
		catch (Exception e) {
			if (Atdl4jConfig.getConfig() != null && Atdl4jConfig.getConfig().isCatchAllMainlineExceptions()) {
				tempSwingAtdl4jTesterApp.logger.warn("Fatal Exception in mainLine", e);
			}
			else {
				throw e;
			}
		}
	}
	
	public void mainLine(String[] args) throws Exception {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("atdl4j - The Open-Source Java Solution for FIXatdl (Swing)");
		
		// -- Delegate setup to AbstractAtdl4jTesterApp, construct a new
		// Swing-specific Atdl4jOptions --
		init(args, new SwingAtdl4jConfiguration(), new Atdl4jOptions(), frame);
		
		// -- Build the Swing panel from Atdl4jTesterPanel (** core GUI component **) --
		getAtdl4jTesterPanel().buildAtdl4jTesterPanel(frame, getAtdl4jOptions());
		
		frame.setSize(475, 500);
		
		try {
			frame.setVisible(true);
		}
		catch (Exception e) {
			if (Atdl4jConfig.getConfig() != null && Atdl4jConfig.getConfig().isCatchAllRuntimeExceptions()) {
				logger.warn("Fatal Exception encountered", e);
				if ((getAtdl4jTesterPanel() != null) && (getAtdl4jTesterPanel().getAtdl4jCompositePanel() != null)
						&& (getAtdl4jTesterPanel().getAtdl4jCompositePanel().getAtdl4jUserMessageHandler() != null)) {
					getAtdl4jTesterPanel().getAtdl4jCompositePanel().getAtdl4jUserMessageHandler()
							.displayException("Fatal Exception encountered", "", e);
				}
			}
			else {
				throw e;
			}
		}
	}
	
}
