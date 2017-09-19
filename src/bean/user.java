package bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import util.coreutil.BeanSQLBuffer;
import util.level2_bean.SingleMainBean;
import util.level3_annotation.HTMLjsoner;
import util.level3_annotation.Searcher;



@Entity(name = "user")
public class user implements  SingleMainBean {
	@Id
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="用户id")
	private String id;
	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="用户名")
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="真实姓名")
	private String realname;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="性别",show=HTMLjsoner.ALTER)
	private String sex;
	@Searcher()
	@HTMLjsoner(name="年龄",show=HTMLjsoner.ALTER)
	private Integer age;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="电话号码",show=HTMLjsoner.ALTER)
	private String mobile;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="地址",show=HTMLjsoner.ALTER)
	private String address;
	@HTMLjsoner(name="注册激活码")
	private String code;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="email地址",show=HTMLjsoner.ALTER)
	private String email;
	@Searcher(likeorbe = Searcher.LIKE)
	@HTMLjsoner(name="删除时间")
	private Date currtime;
	private Date deltime;
	private String icon;
	@HTMLjsoner(name="余额宝账户金额")
	private BigDecimal money;
	@HTMLjsoner(name="用户等级")
	private Integer cansee;



	@Override
	public String toString() {
		return "user [id=" + id + ", username=" + username + ", password=" + password + ", realname=" + realname
				+ ", sex=" + sex + ", age=" + age + ", mobile=" + mobile + ", address=" + address + ", code=" + code
				+ ", email=" + email + ", currtime=" + currtime + ", deltime=" + deltime + ", icon=" + icon + ", money="
				+ money + ", cansee=" + cansee + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCurrtime() {
		return currtime;
	}

	public void setCurrtime(Date currtime) {
		this.currtime = currtime;
	}

	public Date getDeltime() {
		return deltime;
	}

	public void setDeltime(Date deltime) {
		this.deltime = deltime;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getCansee() {
		return cansee;
	}

	public void setCansee(Integer cansee) {
		this.cansee = cansee;
	}

	@Override
	public void setOfftime(Date offtime) {

	}

	@Override
	public String getSelfId(BeanSQLBuffer sonbean) {
		if (sonbean != null)
			if (sonbean instanceof ShopCar)
				return ((ShopCar) sonbean).getUser_id();
		return null;
	}

	@Override
	public Class<? extends BeanSQLBuffer> getSonClass() {

		return ShopCar.class;
	}

}