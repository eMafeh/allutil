package bean;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.SonBean;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;



@Entity
public class ShopCar implements SonBean {
	@Id
	private String id;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="创建时间")
	private Date currtime;
	private Date offtime;
	@Column(nullable = false, columnDefinition = "INT default 0")
	@HTMLjsoner(name="账单状态")
	private Integer cansee;
	@Searcher()
	@HTMLjsoner(name="账单排序值")
	private Integer dsc;

	@Column(nullable = false)
	@Searcher(clazz=user.class,field="username",likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="用户名",clazz=user.class,showfield="username")
	private String user_id;
	@Searcher(clazz=Goods.class,field="name",likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商品名",clazz=Goods.class,showfield="name")
	@Column(nullable = false)
	private String goods_id;
	@Searcher()
	@HTMLjsoner(name="订单商品数量")
	@Column(nullable = false)
	private Integer goodsnum;

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
		return "ShopCar [id=" + id + ", currtime=" + currtime + ", offtime=" + offtime + ", cansee=" + cansee + ", dsc="
				+ dsc + ", user_id=" + user_id + ", goods_id=" + goods_id + ", goodsnum=" + goodsnum + "]";
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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(Integer goodsnum) {
		this.goodsnum = goodsnum;
	}

	@Override
	public String getMainId(Class<? extends BeanSQLBuffer> c) {
		System.out.println(c.getName());
		if (c != null)
			if (c.getName().equals(user.class.getName()))
				return user_id;
			else if (c.getName().equals(Goods.class.getName()))
				return goods_id;
		return null;
	}

}
