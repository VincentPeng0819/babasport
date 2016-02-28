package cn.itcast.core.query.product;



public class BrandQuery {
	private Integer id;
	private String name;
	private String description;
	private String imgUrl;
	private Integer sort;
	private Integer isDisplay;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name
				+ ", description=" + description + ", imgUrl=" + imgUrl
				+ ", sort=" + sort + ", isDisplay=" + isDisplay + "]";
	}
	/**************************新增fields字段，只查询id，name******************************************/
	private String fields;

	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	/**************************新增orderFields字段， 指定排序字段及顺序******************************************/
	public class OrderField{
		private String field;
		private String order;

		public OrderField(String field, String order) {
			super();
			this.field = field;
			this.order = order;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getOrder() {
			return order;
		}
		public void setOrder(String order) {
			this.order = order;
		}
	}
	private OrderField orderFields;

	public OrderField getOrderFields() {
		return orderFields;
	}
	public void setOrderFields(boolean isAsc) {
		OrderField orderField = new OrderField("id", isAsc?"asc":"desc");
		this.orderFields = orderField;
	}
	
	/**************************新增nameLike字段， 指定是否模糊查询******************************************/
	private boolean nameLike;

	public boolean isNameLike() {
		return nameLike;
	}
	public void setNameLike(boolean nameLike) {
		this.nameLike = nameLike;
	}

}
