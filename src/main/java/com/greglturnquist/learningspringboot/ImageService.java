package com.greglturnquist.learningspringboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ImageService {
    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;

    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Flux<Image> findAllImagesOldFix() {
        try {
            DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(UPLOAD_ROOT));
            Iterator<Path> iterator = ds.iterator();
            List<Image> list = new ArrayList<>();
            while (iterator.hasNext()) {
                Path path = iterator.next();
                list.add(new Image(String.valueOf(path.hashCode()), path.getFileName().toString()));
            }
            return Flux.fromIterable(list);
        } catch (IOException e) {
            return Flux.empty();
        }
    }

    public Flux<Image> findAllImages() {
        try {
            return Flux.fromStream(
                    StreamSupport.stream(Files.newDirectoryStream(Paths.get(UPLOAD_ROOT)).spliterator(), true)
                            .map(path -> new Image(String.valueOf(path.hashCode()), path.getFileName().toString())));
        } catch (IOException e) {
            return Flux.empty();
        }
    }

    public Mono<Resource> findOneImage(String filename) {
        Resource resource = resourceLoader.getResource(
                "file:" + UPLOAD_ROOT + "/" + filename);
        Mono<Resource> mono = Mono.fromSupplier(() -> resource);
        int t =4 ;
        return mono;
    }

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .flatMap(file -> file.transferTo(
                        Paths.get(UPLOAD_ROOT, file.filename()).toFile()))
                .then();
    }

    public Mono<Void> deleteImage(String filename) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Pre-load some fake images
     *
     * @return Spring Boot {@link CommandLineRunner} automatically run after app context is loaded.
     */
//    @Bean
    CommandLineRunner setUp() throws IOException {

        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file", new FileWriter(UPLOAD_ROOT + "/test1.jpg"));

            FileCopyUtils.copy("Test file2", new FileWriter(UPLOAD_ROOT + "/test2.jpg"));

            FileCopyUtils.copy("Test file3", new FileWriter(UPLOAD_ROOT + "/test3.jpg"));
        };

    }

}
