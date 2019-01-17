package com.p2pdinner.mappers;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerSpecialNeeds;
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
public interface DinnerSpecialNeedsMapper {
    Collection<DinnerSpecialNeeds> findAllSpecialNeeds();
    Collection<DinnerSpecialNeeds> getSpecialNeedsByMenuId(@Param("menuItemId") Integer profileId, @Param("profileId") Integer menuItemId);

    DinnerSpecialNeeds specialNeedsByName(@Param("name") String name);

    void associateSpecialNeedsWithMenuItem(@Param("menuItemId") Integer menuItemId, @Param("dinnerSpecialNeedsId") Integer dinnerSpecialNeedsId);

    void disassociateSpecialNeedsWithMenuItem(@Param("menuItemId") Integer menuItemId, @Param("dinnerSpecialNeedsId") Integer dinnerSpecialNeedsId);
}
