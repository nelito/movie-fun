package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@ConditionalOnProperty(value ="useS3", havingValue = "false")
public class FileStore implements BlobStore {

    @Value("${our.image.path}")
    private String imagePath;

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.getName());
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[blob.getInputStream().available()];
            blob.getInputStream().read(buffer);
            outputStream.write(buffer);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        File coverFile = new File(name);
        Optional<Blob> optional;

        if (coverFile.exists()) {
            String contentType = new Tika().detect(coverFile.toPath());
            FileInputStream inputStream = new FileInputStream(coverFile);
            Blob blob = new Blob(name, inputStream, contentType);
            optional = Optional.of(blob);
        } else {
            optional = Optional.empty();
        }

        return optional;
    }

    @Override
    public void deleteAll() {
        File targetFile = new File(imagePath);
        Arrays.stream(targetFile.listFiles()).forEach(f -> f.delete());
    }
}