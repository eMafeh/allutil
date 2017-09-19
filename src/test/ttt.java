package test;

public class ttt {
int[] ia = new int[1];
boolean b;
int i;
Object o;
//public static void main(String[] args) {
//	ttt t = new ttt();
//	cc c = t.new cc();
//c.doit();
//}
public void p(){
	System.out.println(ia[0]+""+b+i+o);
}
class a{void doit(){System.out.println("1");}}
class b extends a{

	@Override
	void doit() {
		System.out.println("2");
	} }
class cc extends b{@Override
	void doit() {
	super.doit();
} }
}

