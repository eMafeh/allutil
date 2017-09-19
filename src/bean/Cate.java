package bean;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.SingleSonBean;
import util.level3_annotation.FormValue;
import util.level3_annotation.FormValue.choose;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;
import util.level3_annotation.TableValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@TableValue(OfftimeField="offtime")
public class Cate implements SingleSonBean {
	@Id
	@FormValue(value="id",choose=choose.METHOD)
	private String id;// 主键id 必填
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="创建时间")
	private Date currtime;// 创建时间 选填(自动生成)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="冻结时间")
	private Date offtime;// 删除时间 选填(自动生成)
	@Column(nullable = false)
	@HTMLjsoner(name="状态",show=HTMLjsoner.ALTER)
	private Integer cansee;// 可见性 选填(自动生成)

	@Column(nullable = false)
	@Searcher(clazz=Goods.class,field="name",likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商品名",clazz=Goods.class,showfield="name",show=HTMLjsoner.ALTER)
	private String goods_id;// 必填
	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="分类",show=HTMLjsoner.ALTER)
	private String category;// 必填
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
		return "Cate [id=" + id + ", currtime=" + currtime + ", offtime=" + offtime + ", cansee=" + cansee
				+ ", goods_id=" + goods_id + ", category=" + category + "]";
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

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String getMainId() {
		return goods_id;
	}

	@Override
	public Class<? extends BeanSQLBuffer> getMainClass() {
		return Goods.class;
	}



}
