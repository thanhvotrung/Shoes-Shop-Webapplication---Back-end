package com.ecom.Controller.admin;

import com.ecom.Entity.Image;
import com.ecom.Exception.BadRequestException;
import com.ecom.Exception.InternalServerException;
import com.ecom.Exception.NotFoundException;
import com.ecom.Security.CustomUserDetails;
import com.ecom.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.UUID;

@RestController
public class ImageController {
    private static String UPLOAD_DIR = System.getProperty("user.home") + "/media/upload";

    @Autowired
    private ImageService imageService;

    @PostMapping("/api/upload/files")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        //Tạo thư mục chứa ảnh nếu không tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        //Lấy tên file và đuôi mở rộng của file
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (originalFilename.length() > 0) {

            //Kiểm tra xem file có đúng định dạng không
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("gif") && !extension.equals("svg") && !extension.equals("jpeg")) {
                throw new BadRequestException("Không hỗ trợ định dạng file này!");
            }
            try {
                Image image = new Image();
                image.setId(UUID.randomUUID().toString());
                image.setName(file.getName());
                image.setSize(file.getSize());
                image.setType(extension);
                String link = "/media/static/" + image.getId() + "." + extension;
                image.setLink(link);
                image.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                image.setCreatedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());

                //Tạo file
                File serveFile = new File(UPLOAD_DIR + "/" + image.getId() + "." + extension);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(serveFile));
                bos.write(file.getBytes());
                bos.close();

                imageService.saveImage(image);
                return ResponseEntity.ok(link);

            } catch (Exception e) {
                throw new InternalServerException("Có lỗi trong quá trình upload file!");
            }
        }
        throw new BadRequestException("File không hợp lệ!");
    }

    @GetMapping("/media/static/{filename:.+}")
    public ResponseEntity<Object> downloadFile(@PathVariable String filename) {
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()) {
            throw new NotFoundException("File không tồn tại!");
        }

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException ex) {
            throw new NotFoundException("File không tồn tại!");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/api/delete-image/{filename:.+}")
    public ResponseEntity<Object> deleteImage(@PathVariable String filename){
        imageService.deleteImage(UPLOAD_DIR,filename);
        return ResponseEntity.ok("Xóa file thành công!");
    }
}
