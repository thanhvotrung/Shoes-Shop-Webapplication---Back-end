package com.ecom.Service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class Image2Service {
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            // Initialize Firebase Storage
            InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            List<String> imageUrls = new ArrayList<>();

            // Upload each image to Firebase Storage and get their URLs
            for (MultipartFile file : files) {
                LocalDateTime date = LocalDateTime.now();
                int seconds = date.toLocalTime().toSecondOfDay();
                String imageName = seconds + "-" + file.getOriginalFilename();
                String bucketName = "shoesimagesapi.appspot.com";
                BlobId blobId = BlobId.of(bucketName, imageName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                Blob blob = storage.create(blobInfo, file.getBytes());
                String imageUrl = storage.get(bucketName, imageName).signUrl(365, TimeUnit.DAYS).toString();
                imageUrls.add(imageUrl);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(imageUrls);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
