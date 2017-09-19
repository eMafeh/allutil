package reflect.testpojo;

import reflect.core.InitValue;
import reflect.core.ValueShip;

public class FuncUser {

	@Override
	public String toString() {
		return "FuncUser [funcId=" + funId + ", username=" + username + "]";
	}

	@InitValue
	private Integer funId;
	@InitValue(false)
	@ValueShip("nametest")
	private Long username;

	public FuncUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getFunId() {
		return funId;
	}

	public void setFunId(Integer funId) {
		this.funId = funId;
	}

	public Long getUsername() {
		return username;
	}

	public void setUsername(Long username) {
		this.username = username;
	}
	
	
}
