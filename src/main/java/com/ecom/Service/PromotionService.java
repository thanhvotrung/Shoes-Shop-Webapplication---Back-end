package com.ecom.Service;

import com.ecom.Entity.Promotion;
import com.ecom.Exception.BadRequestException;
import com.ecom.Exception.InternalServerException;
import com.ecom.Exception.NotFoundException;
import com.ecom.Model.request.CreatePromotionRequest;
import com.ecom.Repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.ecom.config.Contant.DISCOUNT_PERCENT;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;


    public Page<Promotion> adminGetListPromotion(String code, String name, String publish, String active, int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        return promotionRepository.adminGetListPromotion(code, name, publish, active, pageable);
    }

    public Promotion getPromotionById(long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isEmpty()) {
            throw new NotFoundException("Khuyến mãi không tồn tại!");
        }
        return promotion.get();
    }


    public Promotion createPromotion(CreatePromotionRequest createPromotionRequest) {
        //Check mã đã tồn tại chưa
        Optional<Promotion> rs = promotionRepository.findByCouponCode(createPromotionRequest.getCode());
        if (rs.isPresent()) {
            throw new BadRequestException("Mã khuyến mại đã tồn tại!");
        }

        //Check validate
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (createPromotionRequest.getExpiredDate().before(now)) {
            throw new BadRequestException("Hạn khuyến mại không hợp lệ");
        }
        if (createPromotionRequest.getDiscountType() == DISCOUNT_PERCENT) {
            if (createPromotionRequest.getDiscountValue() < 1 || createPromotionRequest.getDiscountValue() > 100) {
                throw new BadRequestException("Mức giảm giá từ 1% - 100%");
            }
            if (createPromotionRequest.getMaxValue() < 1000) {
                throw new BadRequestException("Mức giảm giá tối đa phải lớn hơn 1000");
            }
        } else {
            if (createPromotionRequest.getDiscountValue() < 1000) {
                throw new BadRequestException("Mức giảm giá phải lớn hơn 1000");
            }
        }

        //Check có khuyến mại nào đang chạy hay chưa

        if (createPromotionRequest.isPublic() && createPromotionRequest.isActive()) {
            Promotion alreadyPromotion = promotionRepository.checkHasPublicPromotion();
            if (alreadyPromotion != null) {
                throw new BadRequestException("Chương trình khuyến mãi công khai \"" + alreadyPromotion.getCouponCode() + "\" đang chạy. Không thể tạo mới");
            }
        }

        Promotion promotion = new Promotion();
        promotion.setCouponCode(createPromotionRequest.getCode());
        promotion.setName(createPromotionRequest.getName());
        promotion.setActive(createPromotionRequest.isActive());
        promotion.setPublic(createPromotionRequest.isPublic());
        promotion.setExpiredAt(createPromotionRequest.getExpiredDate());
        promotion.setDiscountType(createPromotionRequest.getDiscountType());
        promotion.setDiscountValue(createPromotionRequest.getDiscountValue());
        if (createPromotionRequest.getDiscountType() == DISCOUNT_PERCENT) {
            promotion.setMaximumDiscountValue(createPromotionRequest.getMaxValue());
        } else {
            promotion.setMaximumDiscountValue(createPromotionRequest.getDiscountValue());
        }
        promotion.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        promotionRepository.save(promotion);
        return promotion;
    }


    public void updatePromotion(CreatePromotionRequest createPromotionRequest, long id) {
        Optional<Promotion> rs = promotionRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("Khuyến mại không tồn tại");
        }

        //check validate
        if (createPromotionRequest.getDiscountType() == DISCOUNT_PERCENT) {
            if (createPromotionRequest.getDiscountValue() < 1 || createPromotionRequest.getDiscountValue() > 100) {
                throw new BadRequestException("Mức giảm giá từ 1 - 100%");
            }
            if (createPromotionRequest.getMaxValue() < 1000) {
                throw new BadRequestException("Mức giảm giá tối đa phải lớn hơn 1000");
            }
        } else {
            if (createPromotionRequest.getDiscountValue() < 1000) {
                throw new BadRequestException("Mức giảm giá phải lớn hơn 1000");
            }
        }

        //Check có khuyến mại nào đang chạy hay không
        if (createPromotionRequest.isActive() && createPromotionRequest.isPublic()) {
            Promotion alreadyPromotion = promotionRepository.checkHasPublicPromotion();
            if (alreadyPromotion != null) {
                throw new BadRequestException("Chương trình khuyến mãi công khai \"" + alreadyPromotion.getCouponCode() + "\" đang chạy. Không thể tạo mới");
            }
        }

        //Kiểm tra mã code
        rs = promotionRepository.findByCouponCode(createPromotionRequest.getCode());
        if (rs.isPresent() && rs.get().getId() != id) {
            throw new BadRequestException("Mã code đã tồn tại trong hệ thống");
        }

        Promotion promotion = new Promotion();
        promotion.setCouponCode(createPromotionRequest.getCode());
        promotion.setName(createPromotionRequest.getName());
        promotion.setExpiredAt(createPromotionRequest.getExpiredDate());
        promotion.setDiscountType(createPromotionRequest.getDiscountType());
        promotion.setDiscountValue(createPromotionRequest.getDiscountValue());
        if (createPromotionRequest.getDiscountType() == DISCOUNT_PERCENT) {
            promotion.setMaximumDiscountValue(createPromotionRequest.getMaxValue());
        } else {
            promotion.setDiscountValue(createPromotionRequest.getDiscountValue());
        }
        promotion.setActive(createPromotionRequest.isActive());
        promotion.setPublic(createPromotionRequest.isPublic());
        promotion.setCreatedAt(rs.get().getCreatedAt());
        promotion.setId(id);

        try {
            promotionRepository.save(promotion);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi cập nhật khuyến mãi");
        }
    }

    public void deletePromotion(long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isEmpty()) {
            throw new BadRequestException("Khuyến mại không tồn tại");
        }

        //Check promotion used
//        int check = promotionRepository.checkPromotionUsed(promotion.get().getCouponCode());
//        if (check > 0) {
//            throw new BadRequestException("Khuyến mại đã được sử dụng không thể xóa");
//        }

        try {
            promotionRepository.deleteById(id);
        }catch (Exception e){
            throw new InternalServerException("Lỗi khi xóa khuyến mại");
        }
    }

    public Promotion findPromotionById(long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isEmpty()) {
            throw new NotFoundException("Khuyến mại không tồn tại");
        }
        return promotion.get();
    }


    public Promotion checkPublicPromotion() {
        return promotionRepository.checkHasPublicPromotion();
    }


    public long calculatePromotionPrice(long price, Promotion promotion) {
        long discountValue = promotion.getMaximumDiscountValue();
        long tmp = promotion.getDiscountValue();
        if (promotion.getDiscountType() == DISCOUNT_PERCENT) {
            tmp = price * promotion.getDiscountValue() / 100;
        }
        if (tmp < discountValue) {
            discountValue = tmp;
        }
        long promotionPrice = price - discountValue;
        if (promotionPrice < 0) {
            promotionPrice = 0;
        }
        return promotionPrice;
    }


    public Promotion checkPromotion(String code) {
        return promotionRepository.checkPromotion(code);
    }


    public List<Promotion> getAllValidPromotion() {
        return promotionRepository.getAllValidPromotion();
    }
}
