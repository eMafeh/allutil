package util.level2_bean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import util.coreutil.BeanSQLBuffer;

public class PageBean<E extends BeanSQLBuffer> {
	private static final int MAXPAGE = 99;
	private static final int MAXITEM = 20;

	private int page = 1;
	private int size;
	private int item = 2;
	private int maxpage;
	private int begin;
	private int end;

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	static {
		// 从数据库配置文件读取最大值
	}

	private PageBean() {
	}

	/**
	 * 默认每页五条
	 * 
	 * 返回第一页
	 * 
	 * @param E_extends_BeanSQLBuffer
	 * @return PageBean<E>
	 */
	public static <E extends BeanSQLBuffer> PageBean<E> getPageBean(List<E> list) {
		return getPageBean(list, null);
	}

	/**
	 * 自选每页条数:item
	 * 
	 * 返回第page页
	 * 
	 * @param E_extends_BeanSQLBuffer
	 * @return PageBean<E>
	 * 
	 */

	public static <E extends BeanSQLBuffer> PageBean<E> getPageBean(List<E> list, HttpServletRequest req) {
		String Page;
		String Item;
		HttpSession session = null;
		if (req == null) {
			Page = "1";
			Item =MAXITEM+"";
		} else {

			Page = req.getParameter("Page");
			Item = req.getParameter("Item");
			session = req.getSession();
			if (Page == null && session.getAttribute("Page") != null)
				Page = session.getAttribute("Page").toString();
			if (Item == null && session.getAttribute("Item") != null)
				Item = session.getAttribute("Item").toString();
		}
		System.out.println(Page + ":" + Item);
		PageBean<E> pagebean = new PageBean<E>();
		pagebean.size = list.size();
		pagebean.item = validityItem(Item);
		pagebean.maxpage = (pagebean.size + pagebean.item - 1) / pagebean.item;
		pagebean.page = validityPage(Page, pagebean.maxpage);
		pagebean.begin = (pagebean.page - 1) * pagebean.item;
		pagebean.end = pagebean.begin + pagebean.item;
		if (session != null) {
			session.setAttribute("Page", pagebean.page);
			session.setAttribute("Item", pagebean.item);
		}
		return pagebean;
	}

	private static int validityPage(String page, int maxpage) {
		if (page == null)
			return 1;

		Integer i = 1;
		try {
			i = Integer.valueOf(page);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (i == null)
			return 1;
		if (i < 1)
			i = 1;
		if (i > MAXPAGE)
			i = MAXPAGE;
		if (i > maxpage)
			i = maxpage;
		return i;
	}

	private static int validityItem(String item) {
		if (item == null)
			return 2;
		Integer i = 1;
		try {
			i = Integer.valueOf(item);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (i == null)
			return 2;
		if (i < 1)
			i = 2;
		if (i > MAXITEM)
			i = MAXITEM;
		return i;
	}

	/**
	 * 当前第几页
	 * 
	 * @return int
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 总共多少条数据
	 * 
	 * @return int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 每页多少条数据
	 * 
	 * @return int
	 */
	public int getItem() {
		return item;
	}

	/**
	 * 在当前每页条数下
	 * 
	 * 最多能显示多少页
	 * 
	 * @return int
	 */
	public int getMaxpage() {
		return maxpage;
	}

	@Override
	public String toString() {
		return "PageBean [page=" + page + ", size=" + size + ", item=" + item + ", maxpage=" + maxpage + ", begin="
				+ begin + ", end=" + end + "]";
	}

}
