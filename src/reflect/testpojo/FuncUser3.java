package reflect.testpojo;

import reflect.core.InitValue;
import reflect.core.ValueShip;

public class FuncUser3 {

	
	@Override
	public String toString() {
		return "FuncUser3 [abc=" + abc + ", cba=" + cba + "]";
	}

	@ValueShip(sourceName="abc")
	private String abc;

	@ValueShip(sourceName="cba")
	private Integer cba;

	@InitValue(value = false,ignore = true)
	private Long test;

	@InitValue
	private FuncUser funcUser;

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public Integer getCba() {
		return cba;
	}

	public void setCba(Integer cba) {
		this.cba = cba;
	}

	public Long getTest() {
		return test;
	}

	public void setTest(Long test) {
		this.test = test;
	}

	public FuncUser getFuncUser() {
		return funcUser;
	}

	public void setFuncUser(FuncUser funcUser) {
		this.funcUser = funcUser;
	}

	public String getFunId() {
		return abc;
	}
	public void setFunId(String funId) {
		this.abc = funId;
	}
	public Integer getUsername() {
		return cba;
	}
	public void setUsername(Integer username) {
		this.cba = username;
	}

}
