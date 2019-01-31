package com.p2pdinner.mappers;

import com.p2pdinner.domain.DinnerCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface DinnerCategoryMapper {
    Collection<DinnerCategory> findAllCategories();
    Collection<DinnerCategory> getCategoriesByMenuId(@Param("profileId") Integer profileId, @Param("menuItemId")String menuItemId);
    DinnerCategory categoryByName(@Param("categoryByName")String name);
    void associateCategoryWithMenuItem(@Param("menuItemId")String menuItemId, @Param("categoryId")Integer categoryId);
    void disassociateCategoryWithMenuItem(@Param("menuItemId")String menuItemId, @Param("dinnerCategoryId") Integer dinnerCategoryId);
}
