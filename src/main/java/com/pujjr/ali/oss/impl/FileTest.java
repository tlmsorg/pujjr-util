package com.pujjr.ali.oss.impl;

import java.io.File;
import java.io.IOException;

public class FileTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = null;
		try {
			file = File.createTempFile("催收temp", ".xlsx");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.deleteOnExit();
	}

}
