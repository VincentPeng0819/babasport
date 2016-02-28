package cn.itcast.core.controller.product.front;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.BuyCart;
import cn.itcast.core.bean.BuyItem;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.query.user.AddrQuery;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.user.AddrService;
import cn.itcast.core.web.Constants;
import cn.itcast.core.web.session.SessionProvider;

@Controller
public class CartController {

	@RequestMapping(value="/shopping/buyCart.shtml")
	public String buyCart(HttpServletRequest request,ModelMap model,HttpServletResponse response,Integer skuId,Integer amount,Integer buyLimit,Integer productId){

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
			
		if(null == buyCart){
			//购物车
			buyCart = new BuyCart();
		}
		if(null != skuId ){
			Sku sku = new Sku();
			sku.setId(skuId);
			if(null != buyLimit){
				sku.setSkuUpperLimit(buyLimit);
			}
			//创建购物项
			BuyItem buyItem = new BuyItem();
			//sku
			buyItem.setSku(sku);
			//数量
			buyItem.setAmount(amount);
			buyCart.addItem(buyItem);
			//最后一款商品Id
			if(null != productId){
				buyCart.setProductId(productId);
			}
			

			StringWriter writer = new StringWriter();
			//对象转为JSon 字符串流
			try {
				objectMapper.writeValue(writer, buyCart);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, writer.toString());
			cookie.setMaxAge(24*60*60);
			cookie.setPath("/");
			//发送cookie
			response.addCookie(cookie);
		}	
		//装满购物车
		List<BuyItem> items = buyCart.getItems();
		for(BuyItem item : items){
			Sku s = skuService.getSkuByKey(item.getSku().getId());
			item.setSku(s);
		}
		model.addAttribute("buyCart",buyCart);
		return "product/cart";
	}
	
	///shopping/clearCart.shtml
	@RequestMapping(value="/shopping/clearCart.shtml")
	public String clearCart(HttpServletRequest request,HttpServletResponse response){
		//将购物车放入cookie value必须是字符串 如何转换为字符串
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);			
//		BuyCart buyCart = null;
//		//判断cookie是否有购物车
//		Cookie[] cookies = request.getCookies();
//		if(null != cookies && cookies.length > 0){
//			for(Cookie cookie : cookies){
//				if(Constants.BUYCART_COOKIE.equals(cookie.getName())){
//					String value = cookie.getValue();
//					try {
//						buyCart = objectMapper.readValue(value, BuyCart.class);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					break;
//				}
//			}
//		}
		Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:/shopping/buyCart.shtml";
	}
	
	//删除一个购物项
	@RequestMapping(value="shopping/deleteItem.shtml")
	public String deleteItem(HttpServletRequest request,Integer skuId,HttpServletResponse response){
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
		
		if(null != buyCart){
			Sku sku = new Sku();
			sku.setId(skuId);
			BuyItem buyItem = new BuyItem();
			buyItem.setSku(sku);
			buyCart.deleteItem(buyItem);
		}

		StringWriter writer = new StringWriter();
		//对象转为JSon 字符串流
		try {
			objectMapper.writeValue(writer, buyCart);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, writer.toString());
		cookie.setMaxAge(24*60*60);
		cookie.setPath("/");
		//发送cookie
		response.addCookie(cookie);
		return "redirect:/shopping/buyCart.shtml";
	}
	
	@RequestMapping(value="/shopping/trueBuy.shtml")
	public String trueBuy(ModelMap model,HttpServletRequest request,HttpServletResponse response){
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
		//是否有商品
		if(null != buyCart){
			//判断商品是否有库存
			List<BuyItem> items = buyCart.getItems();
			if(null != items && items.size() > 0){
				//清理前商品个数
				int i = items.size();
				
				for(BuyItem item : items){
					Sku sku = skuService.getSkuByKey(item.getSku().getId());
					if(sku.getStockInventory() < item.getAmount()){
						buyCart.deleteItem(item);
					}
					
				}
				//清理后商品个数
				int l = items.size();
				
				if(i > l){
					//修改购物车中商品数据 
					StringWriter writer = new StringWriter();
					//对象转为JSon 字符串流
					try {
						objectMapper.writeValue(writer, buyCart);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, writer.toString());
					cookie.setMaxAge(24*60*60);
					cookie.setPath("/");
					//发送cookie
					response.addCookie(cookie);
					return "redirect:/shopping/buyCart.shtml";
				}else {
					//加载收货地址 
					Buyer buyer = (Buyer)sessionProvider.getAttribute(request, Constants.BUYER_SESSION_STRING);
					AddrQuery addrQuery = new AddrQuery();
					addrQuery.setBuyerId(buyer.getUsername());
					addrQuery.setIsDef(1);
					List<Addr> addrs = addrService.getAddrList(addrQuery);
					model.addAttribute("addr", addrs.get(0));
					
					//装满购物车
					List<BuyItem> its = buyCart.getItems();
					for(BuyItem item : its){
						Sku s = skuService.getSkuByKey(item.getSku().getId());
						item.setSku(s);
					}
					model.addAttribute("buyCart",buyCart);

					//正常跳转  
					return "product/productOrder";
				}
			}else {
				return "redirect:/shopping/buyCart.shtml";
			}
		}else {
			return "redirect:/shopping/buyCart.shtml";
		}
			
	}
	@Autowired
	private SkuService skuService;
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private AddrService addrService;
}
