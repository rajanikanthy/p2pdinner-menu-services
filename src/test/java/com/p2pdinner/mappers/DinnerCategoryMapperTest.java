package com.p2pdinner.mappers;

import com.p2pdinner.domain.DinnerCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.liquibase.enabled=false", "eureka.client.enabled=false"})
public class DinnerCategoryMapperTest {

    @Autowired
    private DinnerCategoryMapper dinnerCategoryMapper;

    @Test
    public void testFindAllCategories() {
        Collection<DinnerCategory> categories = dinnerCategoryMapper.findAllCategories();
        assertThat(categories).hasSize(11);
    }
}