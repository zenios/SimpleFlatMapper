package org.sfm.csv.cell;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class DoubleCellValueReaderTest {

	DoubleCellValueReader reader = new DoubleCellValueReader();
	@Test
	public void testReadInt() throws UnsupportedEncodingException {
		testReadDouble(0);
		testReadDouble(12345.33);
		testReadDouble(-12345.33);
		testReadDouble(Double.MIN_VALUE);
		testReadDouble(Double.MAX_VALUE);
	}
	
	public void testInvalidDouble() throws UnsupportedEncodingException {
		final byte[] bytes = "ddd".getBytes("UTF-8");
		try {
			reader.read(bytes, 0, bytes.length);
			fail("Expect exception");
		} catch(ParsingException e){
			// expected
		}
	
	}

	private void testReadDouble(double i) throws UnsupportedEncodingException {
		final byte[] bytes = ("_" + Double.toString(i) + "_").getBytes("UTF-8");
		assertEquals(i, reader.read(bytes, 1, bytes.length-2).doubleValue(), 0);
	}

}
