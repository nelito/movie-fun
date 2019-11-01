package org.superbiz.moviefun.blobstore;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Component
@ConditionalOnProperty(value ="blobStore", havingValue = "DBStore")
public class DBStore implements BlobStore {

    @Autowired
    private BlobRepository blobRepository;

    @Override
    public void put(Blob blob) throws IOException {
        BlobEntity blobEntity = new BlobEntity(blob.getName(), IOUtils.toByteArray(blob.getInputStream()),blob.getContentType());
        blobRepository.save(blobEntity);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        BlobEntity blobEntity = blobRepository.find(name);
        Optional<Blob> optionalBlob;
        if (blobEntity != null) {
            Blob blob = new Blob(blobEntity.getName(), new ByteArrayInputStream(blobEntity.getBlob()),blobEntity.getContentType());
            optionalBlob = Optional.of(blob);
        } else {
            optionalBlob = Optional.empty();
        }

        return optionalBlob;
    }

    @Override
    public void deleteAll() {
        blobRepository.deleteAll();
    }
}
