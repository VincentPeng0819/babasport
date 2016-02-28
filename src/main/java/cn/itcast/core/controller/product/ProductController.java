package cn.itcast.core.controller.product;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.Type;
import cn.itcast.core.query.product.BrandQuery;
import cn.itcast.core.query.product.ColorQuery;
import cn.itcast.core.query.product.FeatureQuery;
import cn.itcast.core.query.product.ProductQuery;
import cn.itcast.core.query.product.TypeQuery;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ColorService;
import cn.itcast.core.service.product.FeatureService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.product.TypeService;
import cn.itcast.core.service.staticpage.StaticPageService;

/**
 * 后台管理
 * 测试
 * @author lx
 *
 */
@Controller
public class ProductController{
	@Autowired
	BrandService brandService;
	@Autowired
	ProductService productService;
	@Autowired
	TypeService typeService;
	@Autowired
	FeatureService featureService;
	@Autowired
	ColorService colorService;
	
	
	@RequestMapping(value="/product/list.do")
	public String list(Integer pageNo,String name,Integer brandId,Integer isShow,ModelMap model){
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setFields("id,name");
		brandQuery.setIsDisplay(1);
		//分页参数
		StringBuilder paramsBuilder = new StringBuilder();
		//商品条件对象
		ProductQuery productQuery = new ProductQuery();
		
		productQuery.orderbyId(false);
		//1.判断条件是否为空
		if(StringUtils.isNotBlank(name)){
			productQuery.setName(name);
			productQuery.setNameLike(true);
			paramsBuilder.append("&name=" + name);
			model.addAttribute("name", name);
		}
		//判断品牌id
		if(null != brandId){
			productQuery.setBrandId(brandId);
			paramsBuilder.append("&brandId=" + brandId);
			model.addAttribute("brandId", brandId);

		}
		//判断上下架状态
		if(null != isShow){
			productQuery.setIsShow(isShow);
			paramsBuilder.append("&").append("isShow=").append(isShow);
			model.addAttribute("isShow", isShow);

		}else {
			productQuery.setIsShow(0);
			paramsBuilder.append("&").append("isShow=").append(0);
			model.addAttribute("isShow", 0);
		}
		productQuery.orderbyBrandId(false);
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(5);
		String url = "/product/list.do";
		Pagination pagination = productService.getProductListWithPage(productQuery);
		pagination.pageView(url, paramsBuilder.toString());
		List<Brand> brands = brandService.getBrandList(brandQuery);
		model.addAttribute("pagination", pagination);
		model.addAttribute("brands", brands);
		return "product/list";
	}
	
	//跳转添加商品页面
	@RequestMapping(value="/product/toAdd.do")
	public String toAdd(ModelMap model){
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

		//加载颜色
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.setParentId(0);
		List<Color> colors = colorService.getColorList(colorQuery);
		model.addAttribute("colors", colors);	
		return "product/add";
	}

	
	//商品添加
	@RequestMapping(value="/product/add.do")
	public String add(Product product,Img img){
	
		//1.商品表  图片表   sku表 （最小销售单元）
		product.setImg(img);
		productService.addProduct(product);
		return "redirect:/product/list.do";
	}
	
	
	//上架操作
	@RequestMapping(value="/product/isShow.do")
	public String isShow(Integer[] ids,ModelMap model,Integer pageNo,String name,Integer brandId,Integer isShow){
		

		if(ids != null && ids.length > 0){
			for(Integer id : ids ){
				
				//修改上架状态
				Map<String, Object> rootMap = new HashMap<>();
				//商品加载
				
				Product p = productService.getProductByKey(id);
				p.setIsShow(1);
				productService.updateProductByKey(p);
				Product product = productService.getProductByKey(id);
				rootMap.put("product", product);
				
//				SkuQuery skuQuery = new SkuQuery();
//				skuQuery.setProductId(productId);
				//sku加载
//				List<Sku> skus = skuService.getSkuList(skuQuery );
				List<Sku> skus = skuService.getStock(id);
				rootMap.put("skus", skus);
				
				//处理颜色
				List<Color> colors = new ArrayList<>();
				for(Sku sku : skus){ 
					if(!colors.contains(sku.getColor())){
						colors.add(sku.getColor());
					}
				}
				rootMap.put("colors", colors);
				staticPageService.productIndex(rootMap, id);
			}
		}
		if(null != pageNo){
			model.addAttribute("pageNo", pageNo);
		}
		if(StringUtils.isNotBlank(name)){
			model.addAttribute("name", name);
		}
		if(null != pageNo){
			model.addAttribute("brandId", brandId);
		}
		if(null != pageNo){
			model.addAttribute("isShow", isShow);
		}
		
		
		return "redirect:/product/list.do";
	}
	@Autowired
	private StaticPageService staticPageService;
	@Autowired
	private  SkuService skuService;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
