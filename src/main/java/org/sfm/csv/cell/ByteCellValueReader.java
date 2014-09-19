package org.sfm.csv.cell;

import org.sfm.csv.CellValueReader;

public class ByteCellValueReader implements CellValueReader<Byte> {

	@Override
	public Byte read(byte[] bytes, int offset, int length) {
		return new Byte((byte)IntegerCellValueReader.parseInt(bytes, offset, length));
	}

}
