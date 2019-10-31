package org.superbiz.moviefun.blobstore;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

public class DBStore implements BlobStore {

    @Autowired
    private BlobRepository blobRepository;

    @Override
    public void put(Blob blob) throws IOException {
        BlobEntity
        blobRepository.save(blob);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        BlobEntity blob = blobRepository.find(name);
        Optional<Blob> optionalBlob;
        if (blob != null) {
            optionalBlob = Optional.of(blob);
        } else {
            optionalBlob = Optional.empty();
        }

        return optionalBlob;
    }

    @Override
    public void deleteAll() {

    }
}
