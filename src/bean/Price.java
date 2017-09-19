package bean;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.SingleSonBean;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


@Entity
public class Price  implements SingleSonBean {
	@Id
	private String id;// ����
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="�޸�ʱ��")
	private Date currtime;// ����ʱ��
	private Date offtime;// �¼�ʱ��
	@Column(nullable = false)
	@HTMLjsoner(name="״̬",show=HTMLjsoner.ALTER)
	private Integer cansee;// ״̬��Ϣ
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="����ֵ",show=HTMLjsoner.ALTER)
	private Integer dsc;

	@Column(nullable = false)
	@Searcher(clazz=Goods.class,field="name",likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="��Ʒ��",clazz=Goods.class,showfield="name")
	private String goods_id;// ��Ӧ��goods
	@Searcher()
	@HTMLjsoner(name="�ۿ�",show=HTMLjsoner.ALTER)
	private String sale;// �ۿ� Ĭ��Ϊ���ۿ�

	@Column(nullable = false)
	@Searcher()
	@HTMLjsoner(name="����",show=HTMLjsoner.ALTER)
	private BigDecimal price;// ����
	@Searcher()
	@HTMLjsoner(name="��ʱ��ʱ��",show=HTMLjsoner.ALTER)
	private Date timelimit;// ��ʱ�����Ż�����
	@Searcher()
	@HTMLjsoner(name="��ʱ������",show=HTMLjsoner.ALTER)
	private BigDecimal pricelimit;// ��ʱ��������
	// TODO ����



	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	// TODO getset

	@Override
	public String toString() {
		return "Price [id=" + id + ", currtime=" + currtime + ", offtime=" + offtime + ", cansee=" + cansee + ", dsc="
				+ dsc + ", goods_id=" + goods_id + ", sale=" + sale + ", price=" + price + ", timelimit=" + timelimit
				+ ", pricelimit=" + pricelimit + "]";
	}

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

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	
	public Date getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(Date timelimit) {
		this.timelimit = timelimit;
	}


	

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPricelimit() {
		return pricelimit;
	}

	public void setPricelimit(BigDecimal pricelimit) {
		this.pricelimit = pricelimit;
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
