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
public class DinnerDeliveryMapper {
    @Autowired
    private SqlSession sqlSession;

    public Collection<DinnerCategory> getDeliveryByMenuId(Integer profileId, Integer menuItemId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("profileId", profileId);
        return sqlSession.selectList("getDeliveryByMenuId", parameters);
    }

    public Optional<DinnerCategory> deliveryByName(String name) {
        return Optional.of(sqlSession.selectOne("deliveryByName", name));
    }

    @Transactional
    public void associateSpecialNeedsWithMenuItem(Integer menuItemId, Integer dinnerDeliveryId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("dinnerDeliveryId", dinnerDeliveryId);
        sqlSession.insert("associateDeliveryWithMenuItem", parameters);
    }

    @Transactional
    public void disassociateSpecialNeedsWithMenuItem(Integer menuItemId, Integer dinnerDeliveryId) {
        Map<String,Object> parameters = new HashMap<>(2);
        parameters.put("menuItemId", menuItemId);
        parameters.put("dinnerDeliveryId", dinnerDeliveryId);
        sqlSession.delete("disassociateDeliveryWithMenuItem", parameters);
    }
}
