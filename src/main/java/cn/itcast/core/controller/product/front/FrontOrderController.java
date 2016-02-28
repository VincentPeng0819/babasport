package cn.itcast.core.controller.product.front;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.BuyCart;
import cn.itcast.core.bean.BuyItem;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.web.Constants;

@Controller
public class FrontOrderController {

	@Autowired
	private OrderService orderService;
	@RequestMapping(value = "/buyer/confirmOrder.shtml")
	public String confirmOrder(HttpServletResponse response,Order order,HttpServletRequest request) {
		//接受前台传来的参数
		//保存订单及订单详情两张表
		//将购物车放入cookie value必须是字符串 如何转换为字符串
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);			
		BuyCart buyCart = null;
		//判断cookie是否有购物车
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length > 0){
			for(Cookie cookie : cookies){
				if(Constants.BUYCART_COOKIE.equals(cookie.getName())){
					String value = cookie.getValue();
					try {
						buyCart = objectMapper.readValue(value, BuyCart.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		
		//装满购物车
		List<BuyItem> its = buyCart.getItems();
		for(BuyItem item : its){
			Sku s = skuService.getSkuByKey(item.getSku().getId());
			item.setSku(s);
		}
		orderService.addOrder(request,order,buyCart);
		
		Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		
		response.addCookie(cookie);
		return "product/confirmOrder";
	}
	@Autowired
	private SkuService skuService;
}
