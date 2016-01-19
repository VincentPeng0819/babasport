package cn.itcast.core.service.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.dao.product.BrandDao;

@Service
@Transactional
public class BrandServiceImpl implements BrandService{

	@Resource
	private BrandDao brandDao;

	
	@Transactional(readOnly=true)
	public Pagination getBrandListWithPage(Brand brand) {
		// TODO Auto-generated method stub
		//1:start page startRow=(pageNo-1)*pageSize
		//2:per/page
		//3:total count
		Pagination pagination = new Pagination((brand.getPageNo()-1)*5,5,brandDao.getBrandTotalCount(brand));
		//Brand sets
		pagination.setList(brandDao.getBrandListWithPage(brand));
		return pagination;
	}

}
