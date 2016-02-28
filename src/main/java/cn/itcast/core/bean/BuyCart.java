package cn.itcast.core.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class BuyCart {

	//购物项集合
	List<BuyItem> items = new ArrayList<>();
	//继续购物 最后一款
	private Integer productId;
	public void addItem(BuyItem buyItem){
		
		if(items.contains(buyItem)){
			for (BuyItem it : items) {
				if(it.equals(buyItem)){
					int result = it.getAmount() + buyItem.getAmount();
					if(it.getSku().getSkuUpperLimit() >= result){
						it.setAmount(result);
					}else {
						it.setAmount(it.getSku().getSkuUpperLimit());
					}
				}
			}
		}else {
			items.add(buyItem);
		}
	}
	//小计

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public List<BuyItem> getItems() {
		return items;
	}

	public void setItems(List<BuyItem> items) {
		this.items = items;
	}
	
	//小计 
	//商品数量
	@JsonIgnore
	public int getProductAmount(){
		int result = 0;
		for(BuyItem item : items){
			result += item.getAmount();
		}
		return result;
	}
	
	
	@JsonIgnore
	//商品总金额
	public double getProductPrice(){
		double result = 0.00;
		for(BuyItem item : items){
			result += item.getSku().getSkuPrice()*item.getAmount();
		}
		return result;
	}
	
	//商品运费
	@JsonIgnore
	public double getFee(){
		double result = 0.00;
			if(getProductPrice() <= 39){
				result = 10.00;
		}
		return result;
	}
	
	//应付金额
	@JsonIgnore
	public double getTotalPrice(){
		double result = 0.00;
		result = getProductPrice() + getFee();
		return result;
	}
	
	//删除一个商品
	public void deleteItem(BuyItem item){
		items.remove(item);
	}
}
