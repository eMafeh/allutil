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
	private String id;// ����id ����
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="����ʱ��")
	private Date currtime;// ����ʱ�� ѡ��(�Զ�����)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="����ʱ��")
	private Date offtime;// ɾ��ʱ�� ѡ��(�Զ�����)
	@Column(nullable = false)
	@HTMLjsoner(name="״̬",show=HTMLjsoner.ALTER)
	private Integer cansee;// �ɼ��� ѡ��(�Զ�����)

	@Column(nullable = false)
	@Searcher(clazz=Goods.class,field="name",likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="��Ʒ��",clazz=Goods.class,showfield="name",show=HTMLjsoner.ALTER)
	private String goods_id;// ����
	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="����",show=HTMLjsoner.ALTER)
	private String category;// ����
	// TODO ����

	


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
