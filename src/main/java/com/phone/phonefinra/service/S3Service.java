package com.phone.phonefinra.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.phone.phonefinra.util.FileUtils;


import java.io.*;

public class S3Service {

    private static final String bucketName = "finra-phone";

    public static void getFileFromS3(String filePath, File destination) throws Exception{
        Regions clientRegion = Regions.US_EAST_1;
        String key = filePath;

        S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;

        try {

            AWSCredentials credentials = new BasicAWSCredentials(
                    System.getenv("AWS_ACCESS_KEY_ID"),
                    System.getenv("AWS_SECRET_ACCESS_KEY")
            );

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            // Get an object
            System.out.println("Downloading an object");
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: ");

            FileUtils.writeStream(fullObject.getObjectContent(),destination);

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } finally {
            if (fullObject != null) {
                fullObject.close();
            }
            if (objectPortion != null) {
                objectPortion.close();
            }
            if (headerOverrideObject != null) {
                headerOverrideObject.close();
            }
        }
    }

    public static void putFileToS3(File file, String whereToUpload) {
        Regions clientRegion = Regions.US_EAST_1;
        String fileObjKeyName = whereToUpload;


        try {

            AWSCredentials credentials = new BasicAWSCredentials(
                    System.getenv("AWS_ACCESS_KEY_ID"),
                    System.getenv("AWS_SECRET_ACCESS_KEY")
            );

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
            PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, file).withCannedAcl(CannedAccessControlList.PublicRead);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");

            metadata.addUserMetadata("title", "someTitle");


            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        File tmpStatus = new File("src/main/resources/tmp/status-in.txt");
        new S3Service().getFileFromS3("status/statusFile.txt", tmpStatus);
    }
}
