package org.superbiz.moviefun.blobstore;

import javax.persistence.*;

@Entity
@Table
public class BlobEntity {

    @Id
    private String name;

    @Lob
    @Column(name="binaryData", length=100000)
    private byte[] blob;
    private String contentType;

    public BlobEntity(String name, byte[] blob, String contentType) {
        this.name = name;
        this.blob = blob;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public byte[] getBlob() {
        return blob;
    }

    public String getContentType() {
        return contentType;
    }

    public BlobEntity() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
