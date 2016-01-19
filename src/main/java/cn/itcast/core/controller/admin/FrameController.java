package cn.itcast.core.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理
 * 测试
 * @author lx
 *
 */
@Controller
@RequestMapping(value="/control/frame/")
public class FrameController{

	@RequestMapping(value="product_main")
	public String product_main(){
		
		return "frame/product_main";
	}

	@RequestMapping(value="product_left")
	public String product_left(){
		
		return "frame/product_left";
	}
	
	@RequestMapping(value="order_main")
	public String order_main(){
		
		return "frame/order_main";
	}

	@RequestMapping(value="order_left")
	public String order_left(){
		
		return "frame/order_left";
	}
}
