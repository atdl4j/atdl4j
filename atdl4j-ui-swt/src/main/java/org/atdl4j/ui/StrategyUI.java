package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.exception.ValidationException;

public interface StrategyUI {

	public void validate() throws ValidationException, JAXBException;

	public StrategyT getStrategy();

	public String getFIXMessage() throws JAXBException;

	public void getFIXMessage(FIXMessageBuilder builder) throws JAXBException;

	public void setFIXMessage(String text) throws JAXBException;

}