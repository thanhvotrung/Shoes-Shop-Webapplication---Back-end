package com.ecom.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by vuhien96 on 29/10/2020 15:07
 */
@Getter
@Setter
@NoArgsConstructor
public class PageUtil {
    public int limit;
    public int page;

    public PageUtil(int limit, int page) {
        this.limit = limit;
        if (page < 1) {
            this.page = 1;
        } else {
            this.page = page;
        }
    }

    public int calculateOffset() {
        return (page - 1) * limit;
    }

    public int calculateTotalPage(int totalItems) {
        int totalPages;
        if (totalItems % limit == 0) {
            totalPages = totalItems / limit;
        } else {
            totalPages = totalItems / limit + 1;
        }
        return totalPages;
    }
}
