package bean;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.SingleMainBean;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


@Entity
public class Production  implements SingleMainBean {
	@Id
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="供应商id")
	private String id;// 主键id 必填
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="创建时间")
	private Date currtime;// 创建时间 选填(自动生成)
	@HTMLjsoner(name="删除时间")
	private Date offtime;// 删除时间 选填(自动生成)
	@Column(nullable = false)
	@HTMLjsoner(name="状态",show=HTMLjsoner.ALTER)
	private Integer cansee;// 可见性 选填(自动生成)
	@Searcher()
	@HTMLjsoner(name="排序值",show=HTMLjsoner.ALTER)
	private Integer dsc;// 排序 选填

	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商家名称",show=HTMLjsoner.ALTER)
	private String name;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商家描述",show=HTMLjsoner.ALTER)
	private String description;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商家地址",show=HTMLjsoner.ALTER)
	private String address;
	@HTMLjsoner(name="商家暂存金钱")
	private BigDecimal money;
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
		return "Production [id=" + id + ", currtime=" + currtime + ", offtime=" + offtime + ", cansee=" + cansee
				+ ", dsc=" + dsc + ", name=" + name + ", description=" + description + ", address=" + address
				+ ", money=" + money + "]";
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	@Override
	public String getSelfId(BeanSQLBuffer sonbean) {
		if (sonbean != null)
			if (sonbean instanceof Goods)
				return ((Goods) sonbean).getPro_id();
		return null;
	}

	@Override
	public Class<? extends BeanSQLBuffer> getSonClass() {
		return Goods.class;
	}



}
