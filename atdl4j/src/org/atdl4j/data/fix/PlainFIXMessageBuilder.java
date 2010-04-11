package org.atdl4j.data.fix;

/*import java.util.HashMap;
 import java.util.List;
 import java.util.Map;*/

import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.FIXMessageBuilder;

// TODO: Change to handle generic repeating groups
public class PlainFIXMessageBuilder
		implements FIXMessageBuilder
{

	private static char delimiter = '\001';

	private StringBuffer sb;
	private StringBuffer repeating;
	// private Map<Integer,List<List<StringBuffer>>> repeating;

	private int repeatingCount;

	public void onStart()
	{
		sb = new StringBuffer();
		repeating = new StringBuffer();
		// repeating = new HashMap<Integer,List<StringBuffer>>();
		repeatingCount = 0;
	}

	public void onField(int field, String value)
	{
		if ( field == Atdl4jConstants.TAG_STRATEGY_PARAMETER_NAME || field == Atdl4jConstants.TAG_STRATEGY_PARAMETER_TYPE
				|| field == Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE )
		{
			// if (repeating.get(Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS) ==
			// null)
			repeating.append( field ).append( '=' ).append( value ).append( delimiter );
			if ( field == Atdl4jConstants.TAG_STRATEGY_PARAMETER_NAME )
			{
				repeatingCount++;
			}
		}
		else
		{
			sb.append( field ).append( '=' ).append( value ).append( delimiter );
		}
	}

	public void onEnd()
	{
		// -- Avoid 957=0 --
		if ( repeatingCount > 0 )
		{
			// append repeating group count
			sb.append( Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS ).append( '=' ).append( repeatingCount ).append( delimiter );
			// append repeating group content
			sb.append( repeating );
		}
	}

	public String getMessage()
	{
		return sb.toString();
	}

}
