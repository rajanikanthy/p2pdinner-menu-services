package com.p2pdinner.mappers;

import com.p2pdinner.domain.DinnerCategory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by rajaniy on 1/27/17.
 */
@Component
public class DinnerCategoryMapper {
    @Autowired
    private SqlSession sqlSession;

    public Collection<DinnerCategory> getCategoriesByMenuId(Integer profileId, Integer menuItemId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("profileId", profileId);
        return sqlSession.selectList("getCategoriesByMenuId", parameters);
    }

    public Optional<DinnerCategory> categoryByName(String name) {
        return Optional.of(sqlSession.selectOne("categoryByName", name));
    }

    @Transactional
    public void associateCategoryWithMenuItem(Integer menuItemId, Integer categoryId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("categoryId", categoryId);
        sqlSession.insert("associateCategoryWithMenuItem", parameters);
    }

    @Transactional
    public void disassociateCategoryWithMenuItem(Integer menuItemId, Integer dinnerCategoryId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("dinnerCategoryId", dinnerCategoryId);
        sqlSession.delete("disassociateCategoryWithMenuItem", parameters);
    }
}
