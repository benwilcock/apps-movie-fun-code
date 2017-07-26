package org.superbiz.moviefun.albums;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class S3Store implements BlobStore{

    private final AmazonS3Client amazonS3Client;
    private final String s3BucketName;

    public S3Store(AmazonS3Client amazonS3Client, String s3BucketName) {
        this.amazonS3Client = amazonS3Client;
        this.s3BucketName = s3BucketName;
    }

    @Override
    public void put(Blob blob) throws IOException {
        byte[] bytes = getBytes(blob);
        ObjectMetadata md = new ObjectMetadata();
        md.setContentType(blob.contentType);
        md.setContentLength(bytes.length);
        amazonS3Client.putObject(new PutObjectRequest(s3BucketName, blob.name, new ByteArrayInputStream(bytes), md));
    }

    private byte[] getBytes(Blob blob) throws IOException {
        byte[] bytes = IOUtils.toByteArray(blob.inputStream);
        return bytes;
    }

    @Override
    public Optional<Blob> get(String key) throws IOException {
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(s3BucketName, key));
        InputStream objectData = object.getObjectContent();
        Blob blob = new Blob(key, objectData, object.getObjectMetadata().getContentType());
        return Optional.of(blob);
    }
}
