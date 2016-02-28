package cn.itcast.core.service.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.BuyCart;
import cn.itcast.core.bean.BuyItem;
import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.query.order.OrderQuery;
import cn.itcast.core.web.Constants;
import cn.itcast.core.web.session.SessionProvider;
/**
 * 订单主
 * @author lixu
 * @Date [2014-3-27 下午03:31:57]
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Resource
	OrderDao orderDao;

	/**
	 * 插入数据库
	 * 
	 * @return
	 */
	public Integer addOrder(HttpServletRequest request,Order order,BuyCart buyCart) {
		//生成订单号
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String oid = dateFormat.format(new Date());
		order.setOid(oid);
		
		//设置运费
		order.setDeliverFee(buyCart.getFee());
		
		//应付金额
		order.setPayableFee(buyCart.getTotalPrice());
		
		//订单金额
		order.setTotalPrice(buyCart.getProductPrice());
		
		//支付状态
		if(order.getPaymentWay() == 0){
			order.setIsPaiy(0);
		}else {
			order.setIsPaiy(1);
		}
		
		//订单状态
		order.setState(0);
		
		//订单生成时间
		order.setCreateDate(new Date());
		
		//设置用户名
		Buyer buyer = (Buyer)sessionProvider.getAttribute(request, Constants.BUYER_SESSION_STRING);
		order.setBuyerId(buyer.getUsername());
		
		//保存订单
		Integer addOrder = orderDao.addOrder(order);
		
		//购物项集合
		List<BuyItem> items = buyCart.getItems();
		
		for(BuyItem item : items){
			Detail detail = new Detail();
			detail.setOrderId(order.getId());
			detail.setProductName(item.getSku().getProduct().getName());
			detail.setProductNo(item.getSku().getProduct().getNo());
			detail.setColor(item.getSku().getColor().getName());
			detail.setSize(item.getSku().getSize());
			detail.setSkuPrice(item.getSku().getSkuPrice());
			detail.setAmount(item.getAmount());
			detailService.addDetail(detail);
		}
		//保存订单详情
		return addOrder;
	}
	@Autowired
	private DetailService detailService;
	@Autowired
	private SessionProvider sessionProvider;

	/**
	 * 根据主键查找
	 */
	@Transactional(readOnly = true)
	public Order getOrderByKey(Integer id) {
		return orderDao.getOrderByKey(id);
	}
	
	@Transactional(readOnly = true)
	public List<Order> getOrdersByKeys(List<Integer> idList) {
		return orderDao.getOrdersByKeys(idList);
	}

	/**
	 * 根据主键删除
	 * 
	 * @return
	 */
	public Integer deleteByKey(Integer id) {
		return orderDao.deleteByKey(id);
	}

	public Integer deleteByKeys(List<Integer> idList) {
		return orderDao.deleteByKeys(idList);
	}

	/**
	 * 根据主键更新
	 * 
	 * @return
	 */
	public Integer updateOrderByKey(Order order) {
		return orderDao.updateOrderByKey(order);
	}
	
	@Transactional(readOnly = true)
	public Pagination getOrderListWithPage(OrderQuery orderQuery) {
		Pagination p = new Pagination(orderQuery.getPageNo(),orderQuery.getPageSize(),orderDao.getOrderListCount(orderQuery));
		p.setList(orderDao.getOrderListWithPage(orderQuery));
		return p;
	}
	
	@Transactional(readOnly = true)
	public List<Order> getOrderList(OrderQuery orderQuery) {
		return orderDao.getOrderList(orderQuery);
	}

}
