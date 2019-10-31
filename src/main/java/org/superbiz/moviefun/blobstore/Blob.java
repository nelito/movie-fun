package org.superbiz.moviefun.blobstore;

import javax.persistence.*;
import java.io.InputStream;

@Entity
@Table(name = "blob")
public class Blob {
    @Id
    private final String name;
    private final InputStream inputStream;
    private final String contentType;

    public Blob(String name, InputStream inputStream, String contentType) {
        this.name = name;
        this.inputStream = inputStream;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }
}