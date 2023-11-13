package com.ecom.Controller.admin;
import com.ecom.Service.Image2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/image")
public class Image2Controller {
    @Autowired
    Image2Service imgService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        return imgService.uploadImages(files);
    }
}
