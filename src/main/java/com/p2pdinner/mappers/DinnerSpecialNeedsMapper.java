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
public class DinnerSpecialNeedsMapper {
    @Autowired
    private SqlSession sqlSession;

    public Collection<DinnerCategory> getCategoriesByMenuId(Integer profileId, Integer menuItemId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("profileId", profileId);
        return sqlSession.selectList("getSpeicalNeedsByMenuId", parameters);
    }

    public Optional<DinnerCategory> categoryByName(String name) {
        return Optional.of(sqlSession.selectOne("specialNeedsByName", name));
    }

    @Transactional
    public void associateSpecialNeedsWithMenuItem(Integer menuItemId, Integer dinnerSpecialNeedsId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("dinnerSpecialNeedsId", dinnerSpecialNeedsId);
        sqlSession.insert("associateSpecialNeedsWithMenuItem", parameters);
    }

    @Transactional
    public void disassociateSpecialNeedsWithMenuItem(Integer menuItemId, Integer dinnerSpecialNeedsId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("dinnerSpecialNeedsId", dinnerSpecialNeedsId);
        sqlSession.delete("disassociateSpecialNeedsWithMenuItem", parameters);
    }
}
