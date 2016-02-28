package cn.itcast;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.common.junit.SpringJunitTest;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.query.product.BrandQuery;
import cn.itcast.core.service.product.BrandService;

/**
 * 测试
 * @author lx
 *
 */

public class TestGetBrandList extends SpringJunitTest{

	@Autowired
	private BrandService brandService;
	@Test
	public void testList() throws Exception {
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setFields("id,name");
		brandQuery.setOrderFields(false);
		brandQuery.setName("莲");
		brandQuery.setNameLike(true);
		List<Brand> brands = brandService.getBrandList(brandQuery);
		for(Brand brand : brands){
			System.out.println(brand);
		}
	}
}
