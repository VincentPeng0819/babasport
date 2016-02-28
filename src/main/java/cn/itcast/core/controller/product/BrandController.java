package cn.itcast.core.controller.product;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.service.product.BrandService;

/**
 * 后台管理
 * 测试
 * @author lx
 *
 */
@Controller
public class BrandController{
	
	@Autowired
	private BrandService brandService;
	//品牌列表页面
	@RequestMapping(value="/brand/list.do")
	public String list(String name,Integer isDisplay,Integer pageNo,ModelMap model){
		StringBuilder params = new StringBuilder();
		Brand brand = new Brand();
		if(StringUtils.isNotBlank(name)){
			brand.setName(name);
			params.append("name=").append(name);
		}
		params.append("&");
		if(null != isDisplay){
			brand.setIsDisplay(isDisplay);
			params.append("isDisplay=").append(isDisplay);
		}else {
			brand.setIsDisplay(1);
			params.append("isDisplay=").append("1");			
		}
		brand.setPageSize(5);
		//如果页号为空或者小于1则置为1
		
		//pageNo
		brand.setPageNo(Pagination.cpn(pageNo));
		
		//pageObject
		Pagination pagination = brandService.getBrandListWithPage(brand);
		String url = "/brand/list.do";
		//设置分页
		pagination.pageView(url, params.toString());
		model.addAttribute("pagination",pagination);
		model.addAttribute("name",name);
		model.addAttribute("isDisplay",isDisplay);
		return "brand/list";
	}

//	@InitBinder
//	public void initBinder(WebDataBinder binder, WebRequest request) {
//		// TODO Auto-generated method stub
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
//	}
	//跳转品牌添加页面
	@RequestMapping(value="brand/toAdd.do")
	public String toAdd(){
		return "brand/add";
	}
	
	//添加品牌操作
	@RequestMapping(value="brand/add.do")
	public String add(Brand brand){
		brandService.addBrand(brand);
		return "redirect:/brand/list.do";
	}
	
	//删除单个品牌操作
	@RequestMapping(value="brand/delete.do")
	public String delete(Integer id,String name,Integer isDisplay,ModelMap model){
		
		brandService.deleteBrandByKey(id);
		if(StringUtils.isNotBlank(name)){
			model.addAttribute("name",name);
		}
		if(null != isDisplay){
			model.addAttribute("isDisplay",isDisplay);
		}
		
		
		return "redirect:/brand/list.do";
	}
	
	//删除多个品牌操作
	@RequestMapping(value="brand/deletes.do")
	public String deletes(Integer[] ids,String name,Integer isDisplay,ModelMap model){
		
		brandService.deleteBrandByKeys(ids);
		if(StringUtils.isNotBlank(name)){
			model.addAttribute("name",name);
		}
		if(null != isDisplay){
			model.addAttribute("isDisplay",isDisplay);
		}
		
		return "redirect:/brand/list.do";
	}
	
	//跳转品牌修改页面
	@RequestMapping(value="brand/toEdit.do")
	public String toEdit(Integer id,ModelMap model){
		Brand brandFromDB = brandService.getBrandByKey(id);
		model.addAttribute("brand",brandFromDB);
		return "brand/edit";
	}
	
	//修改品牌操作
	@RequestMapping(value="brand/edit.do")
	public String edit(Brand brand){
		brandService.updateBrandByKey(brand);		
		return "redirect:/brand/list.do";
	}
}
