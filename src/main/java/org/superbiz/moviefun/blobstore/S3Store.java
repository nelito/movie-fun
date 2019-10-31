package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {
    private AmazonS3Client s3Client;
    private String photoStorageBucket;

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
        if (!this.s3Client.doesBucketExist(this.photoStorageBucket)) {
            this.s3Client.createBucket(this.photoStorageBucket);
        }
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.getContentType());
        s3Client.putObject(photoStorageBucket, blob.getName(), blob.getInputStream(), objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        Optional<Blob> optionalBlob;
        if (s3Client.doesObjectExist(photoStorageBucket, name)) {
            S3Object s3ClientObject = s3Client.getObject(photoStorageBucket, name);
            Blob blob = new Blob(name, s3ClientObject.getObjectContent(), s3ClientObject.getObjectMetadata().getContentType());
            optionalBlob = Optional.of(blob);
        } else {
            optionalBlob = Optional.empty();
        }
        return optionalBlob;
    }

    @Override
    public void deleteAll() {
        ObjectListing objectListing = s3Client.listObjects(photoStorageBucket);
        if (objectListing != null && objectListing.getObjectSummaries() != null) {
            objectListing.getObjectSummaries().forEach(obj -> s3Client.deleteObject(obj.getBucketName(), obj.getKey()));
        }
    }
}
