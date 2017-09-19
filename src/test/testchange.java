package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class testchange {

//	public static void main(String[] args) {
//		URL file = Thread.currentThread().getContextClassLoader().getResource("testmybatis");
//		System.out.println(file);
//		File f = new File(file.toString().substring(6));
//		System.out.println(f);
//		System.out.println(f.getParentFile().getParentFile().getParentFile());
//		String listFiles = f.getParentFile().getParentFile().getParent() + "\\src\\testmybatis\\user.java";
//		System.out.println(listFiles);
//		// chuan("C:\\Users\\Administrator\\Desktop\\user.java");
//		chuan(listFiles);
//	}

	public static void chuan(String old) {
		File file = new File(old);
		int res = 0;
		try (FileInputStream in = new FileInputStream(file);) {
			byte[] b = new byte[1024 * 1024];
			res = in.read(b);
			String txt = new String(b, 0, res);
			txt = txt.replaceAll(";*private ", "@FormValue\r\n\tprivate ");
			try (FileOutputStream out = new FileOutputStream(file);) {
				out.write(txt.getBytes());
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(file.getName());
	}

}
