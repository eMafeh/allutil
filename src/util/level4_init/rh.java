package util.level4_init;

import javax.servlet.http.HttpServletRequest;

public class rh {
	private HttpServletRequest request;

	public rh sv(String name, Object value) {
		request.setAttribute(name, value);
		return this;
	}

	public rh(HttpServletRequest request) {
		this.request = request;
	}
}
