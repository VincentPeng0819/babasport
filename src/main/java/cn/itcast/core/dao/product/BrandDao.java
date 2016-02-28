package cn.itcast.core.dao.product;


import java.util.List;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.query.product.BrandQuery;

public interface BrandDao {
	public List<Brand> getBrandListWithPage(Brand brand);
	public int getBrandTotalCount(Brand brand);
	//添加品牌
	public void addBrand(Brand brand);
	public void deleteBrandByKey(Integer id);
	public void deleteBrandByKeys(Integer[] ids);
	public void updateBrandByKey(Brand brand);
	public Brand getBrandByKey(Integer id);
	//获取不带分页的品牌集合
	public List<Brand> getBrandList(BrandQuery brandQuery);
}
