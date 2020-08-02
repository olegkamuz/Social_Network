package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.ImageServiceOld;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

//@Controller
public class BlockingHomeController {
    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";
    private final BlockingImageService imageService;

    public BlockingHomeController(ImageServiceOld defaultImageService, BlockingImageService blockingImageService) {
        this.imageService = blockingImageService;
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.findAllImages());
        return Mono.just("index");
    }

    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> oneRawImage(@PathVariable String filename) {
        Resource resource = imageService.findOneImage(filename);
        try {
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .body(new InputStreamResource(
                            resource.getInputStream()));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Couldn't find " + filename +
                            " => " + e.getMessage());
        }
    }

    @PostMapping(value = BASE_PATH)
    public String createFile(@RequestPart(name = "file") List<FilePart> files) {
        imageService.createImage(files);
        return "redirect:/";
    }

    @DeleteMapping(BASE_PATH + "/" + FILENAME)
    public String deleteFile(@PathVariable String filename) {
        imageService.deleteImage(filename);
        return "redirect:/";
    }
}
