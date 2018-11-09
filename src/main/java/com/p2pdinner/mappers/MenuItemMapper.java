package com.p2pdinner.mappers;

import com.p2pdinner.domain.MenuItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenuItemMapper {
	MenuItem findMenuItemById(@Param("profileId") int profileId, @Param("id") int id);
	void createMenuItem(MenuItem menuItem);
	int doesMenuItemExist(@Param("profileId") int profileId, @Param("id") int id);
	void updateMenuItem(MenuItem menuItem);
	int partialUpdateMenuItem(MenuItem menuItem);
}
