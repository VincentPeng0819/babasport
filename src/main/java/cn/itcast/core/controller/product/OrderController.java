package cn.itcast.core.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.query.order.DetailQuery;
import cn.itcast.core.query.order.OrderQuery;
import cn.itcast.core.query.user.AddrQuery;
import cn.itcast.core.service.order.DetailService;
import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.service.user.AddrService;

@Controller
public class OrderController {
	
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private DetailService detailService;
	@Autowired
	private AddrService addrService;
	@RequestMapping(value="/order/list.do")
	public String list(ModelMap model,Integer isPaiy,Integer state){
		
		
		OrderQuery orderQuery = new OrderQuery();
		//支付状态
		if(null != isPaiy){
			orderQuery.setIsPaiy(isPaiy);
		}
		if(null != state){
			orderQuery.setState(state);
		}
		List<Order> orders = orderService.getOrderList(orderQuery );
		model.addAttribute("orders", orders);
		return "order/list";
	}
	@RequestMapping(value="/order/view.do")
	public String view(ModelMap model,Integer orderId){
		//查询订单主表
		Order order = orderService.getOrderByKey(orderId);
		
		//查询订单详情
		DetailQuery detailQuery = new DetailQuery();
		detailQuery.setOrderId(orderId);
		List<Detail> details = detailService.getDetailList(detailQuery );
		
		//查询地址
		AddrQuery addrQuery = new AddrQuery();
		addrQuery.setBuyerId(order.getBuyerId());
		addrQuery.setIsDef(1);
		List<Addr> addrs = addrService.getAddrList(addrQuery );
		model.addAttribute("addr", addrs.get(0));
		model.addAttribute("order", order);
		model.addAttribute("details", details);
		return "order/view";
	}
}
