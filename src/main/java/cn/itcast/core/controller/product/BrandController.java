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
		Brand brand = new Brand();
		if(StringUtils.isNotBlank(name)){
			brand.setName(name);
		}
		brand.setIsDisplay(isDisplay);
		
		//如果页号为空或者小于1则置为1
		
		//pageNo
		brand.setPageNo(Pagination.cpn(pageNo));
		//pageObject
		Pagination pagination = brandService.getBrandListWithPage(brand);
		model.addAttribute("pagination",pagination);
		return "brand/list";
	}

//	@InitBinder
//	public void initBinder(WebDataBinder binder, WebRequest request) {
//		// TODO Auto-generated method stub
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
//	}
}
