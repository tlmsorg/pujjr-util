package com.pujjr.ali.oss.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.ObjectAcl;
import com.aliyun.oss.model.PutObjectRequest;
import com.pujjr.ali.oss.IOssService;

@Service
public class OssServiceImpl implements IOssService {

	/*private static String endpoint = "oss-cn-shanghai.aliyuncs.com";
	private static String accessKeyId = "LTAIh1DJbbGvo9gN";
	private static String accessKeySecret = "tjqPBo4x4Ve24oG1tytPP4yeX2DD7k";
	private static String bucketName = "pjrp";
	private static String key = "tang/test/ddd/eee";*/
	@Value("${endpoint}")
	private String endpoint;
	@Value("${accessKeyId}")
	private String accessKeyId;
	@Value("${accessKeySecret}")
	private String accessKeySecret;

	@Override
	public void putObject(String endpoint,String accessKeyId,String accessKeySecret
			,String bucketName,String key, File file) {
		/*
		 * Constructs a client instance with your account for accessing OSS
		 */
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		System.out.println("Getting Started with OSS SDK for Java\n");

		try {

			/*
			 * Determine whether the bucket exists
			 */
			if (!ossClient.doesBucketExist(bucketName)) {
				/*
				 * Create a new OSS bucket
				 */
				System.out.println("Creating bucket " + bucketName + "\n");
				ossClient.createBucket(bucketName);
				CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
				createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
				ossClient.createBucket(createBucketRequest);
			}

			/*
			 * List the buckets in your account
			 */
			System.out.println("Listing buckets");

			ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
			listBucketsRequest.setMaxKeys(500);

			for (Bucket bucket : ossClient.listBuckets()) {
				System.out.println(" - " + bucket.getName());
			}
			System.out.println();

			/*
			 * Upload an object to your bucket
			 */
			System.out.println("Uploading a new object to OSS from a file\n");
			System.out.println("file.getAbsolutePath():"+file.getAbsolutePath());
			ossClient.putObject(new PutObjectRequest(bucketName, key, file));

			/*
			 * Determine whether an object residents in your bucket
			 */
			boolean exists = ossClient.doesObjectExist(bucketName, key);
			System.out.println("Does object " + bucketName + " exist? " + exists + "\n");

			ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
			ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.Default);

			ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, key);
			System.out.println("ACL:" + objectAcl.getPermission().toString());

			/*
			 * Download an object from your bucket
			 */
			/*
			 * System.out.println("Downloading an object"); OSSObject object =
			 * ossClient.getObject(bucketName, key); System.out.println(
			 * "Content-Type: " + object.getObjectMetadata().getContentType());
			 * displayTextInputStream(object.getObjectContent());
			 */
			/*
			 * List objects in your bucket by prefix
			 */
			/*
			 * System.out.println("Listing objects"); ObjectListing
			 * objectListing = ossClient.listObjects(bucketName, "My"); for
			 * (OSSObjectSummary objectSummary :
			 * objectListing.getObjectSummaries()) { System.out.println(" - " +
			 * objectSummary.getKey() + "  " + "(size = " +
			 * objectSummary.getSize() + ")"); } System.out.println();
			 */
			/*
			 * Delete an object
			 */
			/*
			 * System.out.println("Deleting an object\n");
			 * ossClient.deleteObject(bucketName, key);
			 */

		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message: " + oe.getErrorCode());
			System.out.println("Error Code:       " + oe.getErrorCode());
			System.out.println("Request ID:      " + oe.getRequestId());
			System.out.println("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ce.getMessage());
		} finally {
			/*
			 * Do not forget to shut down the client finally to release all
			 * allocated resources.
			 */
			ossClient.shutdown();
		}
	}

}
