package com.concungshop.service;

import com.concungshop.dto.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends GeneralService<CategoryDto>{
    Iterable<String> getNameAlLCategory();
}
