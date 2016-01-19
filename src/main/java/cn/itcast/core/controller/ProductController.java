package cn.itcast.core.controller;


import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理
 * 测试
 * @author lx
 *
 */
@Controller
@RequestMapping(value="/control/")
public class ProductController{
	
	@RequestMapping(value="test/springtest.do")
	public String test(String name,Date birthday){
		
		System.out.println(birthday);
		return name;
		
	}

//	@InitBinder
//	public void initBinder(WebDataBinder binder, WebRequest request) {
//		// TODO Auto-generated method stub
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
//	}
}
