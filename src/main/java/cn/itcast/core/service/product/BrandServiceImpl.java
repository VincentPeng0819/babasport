package cn.itcast.core.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.dao.product.BrandDao;
import cn.itcast.core.query.product.BrandQuery;

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
		Pagination pagination = new Pagination(brand.getPageNo(), brand.getPageSize(),brandDao.getBrandTotalCount(brand));
		//Brand sets
		pagination.setList(brandDao.getBrandListWithPage(brand));
		return pagination;
	}


	@Override
	public void addBrand(Brand brand) {
		// TODO Auto-generated method stub
		brandDao.addBrand(brand);
	}


	@Override
	public void deleteBrandByKey(Integer id) {
		// TODO Auto-generated method stub
		brandDao.deleteBrandByKey(id);
	}


	@Override
	public void deleteBrandByKeys(Integer[] ids) {
		// TODO Auto-generated method stub
		brandDao.deleteBrandByKeys(ids);
	}


	@Override
	public void updateBrandByKey(Brand brand) {
		// TODO Auto-generated method stub
		brandDao.updateBrandByKey(brand);
	}


	@Override
	public Brand getBrandByKey(Integer id) {
		// TODO Auto-generated method stub
		return brandDao.getBrandByKey(id);
	}


	@Override
	public List<Brand> getBrandList(BrandQuery brandQuery) {
		// TODO Auto-generated method stub
		return brandDao.getBrandList(brandQuery);
	}

}
