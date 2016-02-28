package cn.itcast.core.controller.product.front;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.Type;
import cn.itcast.core.query.product.BrandQuery;
import cn.itcast.core.query.product.FeatureQuery;
import cn.itcast.core.query.product.ProductQuery;
import cn.itcast.core.query.product.TypeQuery;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ColorService;
import cn.itcast.core.service.product.FeatureService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.product.TypeService;

/**
 * 后台管理
 * 测试
 * @author lx
 *
 */
@Controller
public class FrontProductController{
	@Autowired
	BrandService brandService;
	@Autowired
	ProductService productService;
	@Autowired
	TypeService typeService;
	@Autowired
	FeatureService featureService;
	
	
	@RequestMapping(value="/product/display/list.shtml")
	public String list(Integer typeId,String typeName,String brandName,Integer brandId,Integer pageNo,ModelMap model){
		//分页查询时需要传递的参数包括筛选条件内的参数
		StringBuilder params = new StringBuilder();
		
		//加载商品类型
		TypeQuery typeQuery = new TypeQuery();
		typeQuery.setFields("id,name");
		typeQuery.setIsDisplay(1);
		typeQuery.setParentId(0);
		List<Type> types = typeService.getTypeList(typeQuery);
		model.addAttribute("types", types);

		
		//加载商品品牌
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setFields("id,name");
		brandQuery.setIsDisplay(1);
		List<Brand> brands = brandService.getBrandList(brandQuery);
		model.addAttribute("brands", brands);

		
		//加载商品属性
		FeatureQuery featureQuery = new FeatureQuery();
		List<Feature> features = featureService.getFeatureList(featureQuery);
		model.addAttribute("features", features);
		
		//设置页号
		ProductQuery productQuery = new ProductQuery();
		pageNo = Pagination.cpn(pageNo);
		productQuery.setPageNo(pageNo);
		
		//设置每页数
		productQuery.setPageSize(Product.FRONT_PAGE_SIZE);
		
		//返回商品倒叙
		productQuery.orderbyId(false);
		
		//隐藏页面晒选条件
		boolean flag;
		flag = false;
		
		//前台传来brandId
		if(null != brandId){
			productQuery.setBrandId(brandId);
			flag = true;
			model.addAttribute("brandId", brandId);
			model.addAttribute("brandName", brandName);
			model.addAttribute("flag", flag);
			params.append("&branId=" + brandId).append("&brandName=" + brandName);
		}
		
		if(null != typeId){
			productQuery.setTypeId(typeId);
			flag = true;
			model.addAttribute("typeId", typeId);
			model.addAttribute("typeName", typeName);
			model.addAttribute("flag", flag);
			params.append("&typeId=" + typeId).append("&typeName=" + typeName);
		}
		params.append("&flag=" + flag);
		//获取分页对象
		Pagination pagination = productService.getProductListWithPage(productQuery);
		
		//分页页面显示
		String url = "/product/display/list.shtml";
		pagination.pageView(url, params.toString());
		//回显对象
		model.addAttribute("pagination", pagination);
		model.addAttribute("flag", flag);		
		return "product/product";
	}
	
	
	//跳转商品详情页面
	@RequestMapping(value="/product/display/detail.shtml")
	public String detail(Integer productId,ModelMap model){
		
		//商品加载
		Product product = productService.getProductByKey(productId);
		
		model.addAttribute("product", product);
		
//		SkuQuery skuQuery = new SkuQuery();
//		skuQuery.setProductId(productId);
		//sku加载
//		List<Sku> skus = skuService.getSkuList(skuQuery );
		List<Sku> skus = skuService.getStock(productId);
		model.addAttribute("skus", skus);
		
		//处理颜色
		List<Color> colors = new ArrayList<>();
		for(Sku sku : skus){ 
			if(!colors.contains(sku.getColor())){
				colors.add(sku.getColor());
			}
		}
		model.addAttribute("colors", colors);
		
		return "product/productDetail";
		
		//加载商品（带图片）
		
/*		
		Product product = productService.getProductByKey(productId);
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.setProductId(productId);
		List<Sku> skus = skuService.getSkuList(skuQuery);
		List<Color> colors = new ArrayList<>();
//		List<Integer> colorIds = new ArrayList<>();
//		for(Sku sku : skus){
//			if(!(colorIds.contains(sku.getColorId()))){
//				colorIds.add(sku.getColorId());
//				colors.add(colorService.getColorByKey(sku.getColorId()));
//			}
//		}
		
		for(Sku sku : skus){
			if(!colors.contains(sku.getColor())){
				colors.add(sku.getColor());
			}
		}
		System.out.println(colors);
		*/
	
	}
	
	@Autowired
	private SkuService skuService;
	@Autowired
	private ColorService colorService;
	
	
	
	
}
