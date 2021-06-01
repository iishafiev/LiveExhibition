package com.hse.cs.ce.LiveExhibition.Controllers;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.hse.cs.ce.LiveExhibition.Services.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/storage/")
public class BucketController {

    private AmazonClient amazonClient;

    @Value("${jsa.s3.bucketName}")
    private String bucket;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @GetMapping("/getFile/{fileName}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileName)
    {
        byte[] data = amazonClient.getFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok().contentLength(data.length)
                .header("Content-type", "Photo")
                .header("Content-description", "filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/deleteFile{fileName}")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }



}