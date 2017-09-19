package reflect.testpojo;

import reflect.core.ValueShip;

import java.math.BigDecimal;

public class FuncUser2 extends FuncUser3{

	@Override
	public String toString() {
		return "FuncUser2 [funcId=" + funId + ", username=" + username + "]";
	}

	@ValueShip(sourceName="username",targetName="abc")
	private BigDecimal funId;
	@ValueShip(sourceName="funId",targetName="cba")
	private short username;

}
