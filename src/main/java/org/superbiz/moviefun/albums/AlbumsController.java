package org.superbiz.moviefun.albums;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    private final AlbumsBean albumsBean;

    private final BlobStore fileStore;

    @Value("${our.image.path}")
    private String imagePath;

    public AlbumsController(AlbumsBean albumsBean, BlobStore fileStore) {
        this.albumsBean = albumsBean;
        this.fileStore = fileStore;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {

        Blob blob = new Blob(getCoverFile(albumId), uploadedFile.getInputStream(), uploadedFile.getContentType());
        fileStore.put(blob);

        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {

        byte[] imageBytes;
        HttpHeaders headers = new HttpHeaders();
        Optional<Blob> optionalBlob = fileStore.get(getCoverFile(albumId));
        String contentType;
        if (optionalBlob.isPresent()) {
            Blob blob = optionalBlob.get();
            imageBytes = IOUtils.toByteArray(blob.getInputStream());
            contentType = blob.getContentType();
        } else {
            Resource resource = new ClassPathResource("default-cover.jpg");
            Path coverFilePath = resource.getFile().toPath();
            imageBytes = readAllBytes(coverFilePath);
            contentType = new Tika().detect(coverFilePath);
        }
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(imageBytes.length);
        return new HttpEntity<>(imageBytes, headers);
    }

    @GetMapping("/delete")
    public String deleteAll(Map<String, Object> model) {
        fileStore.deleteAll();
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    private String getCoverFile(long albumId) {
        return format("%s/%d", imagePath, albumId);

    }

}
