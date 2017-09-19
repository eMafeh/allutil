package bean;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.MainBean;
import util.level2_bean.SingleSonBean;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Goods  implements MainBean,SingleSonBean {
	@Id
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商品id")
	private String id;// 主键
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="创建时间")
	private Date currtime;// 创建时间
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="下架时间")
	private Date offtime;// 下架时间
	@Column(nullable = false)
	@HTMLjsoner(name="状态",show=HTMLjsoner.ALTER)
	private Integer cansee;// 状态信息
	@Searcher()
	@HTMLjsoner(name="排序值",show=HTMLjsoner.ALTER)
	private Integer dsc;

	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商品名称",show=HTMLjsoner.ALTER)
	private String name;// 名称 必填
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商品描述",show=HTMLjsoner.ALTER)
	private String description;// 描述

	@Column(nullable = false)
	@Searcher(clazz=Production.class,field="name")
	@HTMLjsoner(name="供应商",clazz=Production.class,showfield="name")
	private String pro_id;// 供应商id 必填
	@Column(nullable = false)
	@Searcher()
	@HTMLjsoner(name="库存量",show=HTMLjsoner.ALTER)
	private Integer stock;// 库存 注意线程安全 必填
	@Searcher()
	@HTMLjsoner(name="已售数量")
	private Integer soldnum;// 已售数量
	@Searcher()
	@HTMLjsoner(name="点击数",show=HTMLjsoner.ALTER)
	private Long checknum;// 点击数量

	// TODO 属性


	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", currtime=" + currtime + ", offtime=" + offtime + ", cansee=" + cansee + ", dsc="
				+ dsc + ", name=" + name + ", description=" + description + ", pro_id=" + pro_id + ", stock=" + stock
				+ ", soldnum=" + soldnum + ", checknum=" + checknum + "]";
	}

	// TODO getset
	public Date getCurrtime() {
		return currtime;
	}

	public void setCurrtime(Date currtime) {
		this.currtime = currtime;
	}

	public Date getOfftime() {
		return offtime;
	}

	public void setOfftime(Date offtime) {
		this.offtime = offtime;
	}

	public Integer getCansee() {
		return cansee;
	}

	public void setCansee(Integer cansee) {
		this.cansee = cansee;
	}

	public Integer getDsc() {
		return dsc;
	}

	public void setDsc(Integer dsc) {
		this.dsc = dsc;
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

	public String getPro_id() {
		return pro_id;
	}

	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getSoldnum() {
		return soldnum;
	}

	public void setSoldnum(Integer soldnum) {
		this.soldnum = soldnum;
	}

	public Long getChecknum() {
		return checknum;
	}

	public void setChecknum(Long checknum) {
		this.checknum = checknum;
	}





	@Override
	public String getSelfId(BeanSQLBuffer sonbean) {
		if (sonbean != null)
			if (sonbean instanceof Cate)
				return ((Cate) sonbean).getGoods_id();
			else if (sonbean instanceof FilePath)
				return ((FilePath) sonbean).getGoods_id();
			else if (sonbean instanceof Price)
				return ((Price) sonbean).getGoods_id();
			else if (sonbean instanceof ShopCar)
				return ((ShopCar) sonbean).getGoods_id();
		return null;
	}

	@Override
	public String getMainId() {
		return pro_id;
	}

	@Override
	public Class<? extends BeanSQLBuffer> getMainClass() {
		return Production.class;
	}



	
}
