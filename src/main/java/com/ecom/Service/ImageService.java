package com.ecom.Service;

import com.ecom.Entity.Image;
import com.ecom.Exception.BadRequestException;
import com.ecom.Exception.InternalServerException;
import com.ecom.Exception.NotFoundException;
import com.ecom.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    @Transactional(rollbackFor = InternalServerException.class)
    public void deleteImage(String uploadDir, String filename) {

        //Lấy đường dẫn file
        String link = "/media/static/" + filename;
        //Kiểm tra link
        Image image = imageRepository.findByLink(link);
        if (image == null) {
            throw new NotFoundException("File không tồn tại");
        }

        //Kiểm tra ảnh đã được dùng
        Integer inUse = imageRepository.checkImageInUse(link);
        if (inUse != null) {
            throw new BadRequestException("Ảnh đã được sử dụng không thể xóa!");
        }

        //Xóa file trong databases
        imageRepository.delete(image);

        //Kiểm tra file có tồn tại trong thư mục
        File file = new File(uploadDir + "/" + filename);
        if (file.exists()) {
            //Xóa file ở thư mục
            if (!file.delete()) {
                throw new InternalServerException("Lỗi khi xóa ảnh!");
            }
        }
    }
    public List<String> getListImageOfUser(long userId) {
        return imageRepository.getListImageOfUser(userId);
    }


}
