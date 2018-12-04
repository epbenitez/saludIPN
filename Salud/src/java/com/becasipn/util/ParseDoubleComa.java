
package com.becasipn.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * 
 * @author Rafael Cardenas
 */
public class ParseDoubleComa extends CellProcessorAdaptor implements StringCellProcessor {

        public ParseDoubleComa() {
		super();
	}
	
	public ParseDoubleComa(final DoubleCellProcessor next) {
		super(next);
	}
	
        @Override
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		Double result=null;
		if( value instanceof Double ) {
			result = (Double) value;
		} else if( value instanceof String ) {
                    try {
                        NumberFormat format = NumberFormat.getInstance(new Locale("es","MX"));
                        Number number = format.parse((String) value);
                        result = number.doubleValue();
                    }
                    catch( NumberFormatException e) {
                            throw new SuperCsvCellProcessorException(String.format("'%s' could not be parsed as a Double", value),
                                    context, this, e);
                    } catch (ParseException ex) {
                        Logger.getLogger(ParseDoubleComa.class.getName()).log(Level.SEVERE, null, ex);
                    }
		} else {
			final String actualClassName = value.getClass().getName();
			throw new SuperCsvCellProcessorException(String.format(
				"the input value should be of type Double or String but is of type %s", actualClassName), context, this);
		}
		
		return next.execute(result, context);
	}

}

