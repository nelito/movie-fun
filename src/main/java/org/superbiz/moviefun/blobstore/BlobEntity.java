package org.superbiz.moviefun.blobstore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.InputStream;
import java.sql.Blob;

@Entity
@Table(name = "blob")
public class BlobEntity {

    @Id
    private final String name;
    private final Blob blob;
    private final String contentType;

    public BlobEntity(String name, Blob blob, String contentType) {
        this.name = name;
        this.blob = blob;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public Blob getBlob() {
        return blob;
    }

    public String getContentType() {
        return contentType;
    }
}
