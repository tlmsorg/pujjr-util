package com.pujjr.ali.oss.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Test2 {

	private static String endpoint = "oss-cn-shanghai.aliyuncs.com";
	private static String accessKeyId = "LTAIh1DJbbGvo9gN";
	private static String accessKeySecret = "tjqPBo4x4Ve24oG1tytPP4yeX2DD7k";
	private static String bucketName = "pjrp";
	private static String key = "test777/testttt777.xml";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new OssServiceImpl().putObject(bucketName, key, createSampleFile());
	}

	private static File createSampleFile() throws IOException {
        File file = File.createTempFile("oss-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("012345678901123489011234890112348901123489011234567890\n");
        writer.close();
        
        return file;
    }
}
