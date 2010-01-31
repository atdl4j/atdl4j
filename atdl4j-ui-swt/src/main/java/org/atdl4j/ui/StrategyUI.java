package org.atdl4j.ui;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.atdl.core.StrategyT;

public interface StrategyUI {

	public void validate() throws ValidationException, JAXBException;

	public StrategyT getStrategy();

	public String getFIXMessage() throws JAXBException;

	public void getFIXMessage(FIXMessageBuilder builder) throws JAXBException;

	public void setFIXMessage(String text) throws JAXBException;
	
//TODO Scott Atwell added 1/14/2010
	public Map<String, ControlUI<?>> getControls();
	
}