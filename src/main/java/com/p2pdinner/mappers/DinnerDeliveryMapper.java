package com.p2pdinner.mappers;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
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

/**
 * Created by rajaniy on 1/27/17.
 */
@Mapper
public interface DinnerDeliveryMapper {
    Collection<DinnerDelivery> findAllDeliveryTypes();
    Collection<DinnerDelivery> getDeliveryByMenuId(@Param("menuItemId")Integer profileId, @Param("profileId")Integer menuItemId);
    DinnerDelivery deliveryByName(@Param("deliveryByName")String name);
    void associateDeliveryWithMenuItem(@Param("menuItemId")Integer menuItemId, @Param("dinnerDeliveryId")Integer dinnerDeliveryId);
    void disassociateDeliveryWithMenuItem(@Param("menuItemId")Integer menuItemId, @Param("dinnerDeliveryId")Integer dinnerDeliveryId);
}
