package cn.itcast.core.service.product;


import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;

public interface BrandService {
	public Pagination getBrandListWithPage(Brand brand);
}
