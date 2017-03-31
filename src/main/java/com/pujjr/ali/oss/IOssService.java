package com.pujjr.ali.oss;

import java.io.File;

public interface IOssService {
	public void putObject(String bucketName,String ossKey,File file);
}
