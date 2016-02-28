package cn.itcast.core.service.product;


import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.query.product.BrandQuery;

public interface BrandService {
	public Pagination getBrandListWithPage(Brand brand);
	public void addBrand(Brand brand);
	public void deleteBrandByKey(Integer id);
	public void deleteBrandByKeys(Integer[] ids);
	public void updateBrandByKey(Brand brand);
	public Brand getBrandByKey(Integer id);
	public List<Brand> getBrandList(BrandQuery brandQuery);

}
