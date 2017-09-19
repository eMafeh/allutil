package test;

public class imbdatest {
	private interface A<T> {
		public T valueA(String s);
		
	}

	private interface B {
		public int valueB(String s);
	}

//	public static void main(String[] args) {
//		A<Number> a = Long::valueOf;
//		B b = String::length;
//Function<String,Integer> f = String::length;
//f=String::hashCode;
//		// compiler error!
////		 b = a;
//
//		// ClassCastException at runtime!
////		 b = (B)a;
//
//		// works, but ugly (wraps in a new lambda)
////		b = (x) -> a.valueA(x);
//		System.out.println(b.valueB("abc"));
//		System.out.println(a.valueA("123"));
//		System.out.println(f.apply("a2a"));
//	}
	public void t(){
		String s1 = "a";
		System.out.println(s1.hashCode());
		String s4 = "ab";
		System.out.println(s4.hashCode());
		String s2 = s1 + "b";
		System.out.println(s2.hashCode());
		String s3 = "a" + "b";
		String s5 = new String("ab");
		System.out.println(s3.hashCode());
		System.out.println(s2 == "ab");
		System.out.println(s3 == "ab");
		System.out.println(s4 == s3);
	}
}