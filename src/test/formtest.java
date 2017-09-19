package test;

import org.junit.Test;

public class formtest {
//public static void main(String[] args) {
//	a.aa();
//	b b = new cc();
//	b.a();
//}
	public void a(){
//		Form form = new Form();
		a.aa();
		b b = new cc();
		b.a();
//		Response putResponse = new Client(Protocol.HTTP)
}
	interface a{void a();
	
	static void aa(){
		System.out.println("aa");
	}
	}
	interface b extends a{
		
		@Override@Test
		default void a() {
			System.out.println("a");		
		}
		
	}
	static class cc implements b{}
}