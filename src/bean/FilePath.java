package bean;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.SingleSonBean;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;



@Entity
public class FilePath implements SingleSonBean {
	@Id
	private String id;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="上传时间")
	private Date currtime;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="删除时间")
	private Date offtime;
	@Column(nullable = false)
	@HTMLjsoner(name="状态",show=HTMLjsoner.ALTER)
	private Integer cansee;
	@Searcher()
	@HTMLjsoner(name="排序值",show=HTMLjsoner.ALTER)
	private Integer dsc;

	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="文件当前路径")
	private String filepath;
	@Column(nullable = false)
	@Searcher(clazz=Goods.class,field="name",likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="商品名",clazz=Goods.class,showfield="name")
	private String goods_id;
	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="文件名",show=HTMLjsoner.ALTER)
	private String filename;

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
		return "FilePath [id=" + id + ", currtime=" + currtime + ", offtime=" + offtime + ", cansee=" + cansee
				+ ", dsc=" + dsc + ", filepath=" + filepath + ", goods_id=" + goods_id + ", filename=" + filename + "]";
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

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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
