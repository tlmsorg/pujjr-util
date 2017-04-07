package com.pujjr.ali.oss;

import java.io.File;

public interface IOssService {
	public void putObject(String endpoint,String accessKeyId,String accessKeySecret,String bucketName, String key, File file);
}
