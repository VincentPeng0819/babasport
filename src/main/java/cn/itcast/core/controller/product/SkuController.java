package cn.itcast.core.controller.product;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.web.ResponseUtils;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.query.product.SkuQuery;
import cn.itcast.core.service.product.SkuService;

/**
 * 后台管理
 * 测试
 * @author lx
 *
 */
@Controller
public class SkuController{
	
	@Autowired
	private SkuService skuService;

	//跳转sku页面
	@RequestMapping(value="/sku/toList.do")
	public String toList(Integer productId,String pno,ModelMap model){
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.setProductId(productId);
		
		List<Sku> skus = skuService.getSkuList(skuQuery);
		
		model.addAttribute("skus", skus);
		model.addAttribute("pno",pno);
		return "sku/list";
	}
	
	//添加品牌操作
	@RequestMapping(value="/sku/edit.do")
	public void edit(Sku sku,HttpServletResponse response){
		skuService.updateSkuByKey(sku);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message","保存成功" );
		ResponseUtils.renderJson(response, jsonObject.toString());
	}

}
