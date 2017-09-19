package lab;

import bean.Cate;
import bean.FilePath;
import bean.Goods;
import bean.Price;
import bean.Production;
import bean.ShopCar;
import bean.user;

public class testclass  implements ContactLine{
	private  OneShip g = new OneShip(Goods.class,"fid", Cate.class, "id");
	private  OneShip d = new OneShip(Goods.class,"fid", FilePath.class, "id");
	private  OneShip e = new OneShip(Goods.class,"priid", Price.class, "id");
	private  OneShip f = new OneShip(Goods.class,"uid", user.class, "id");
	private  OneShip c = new OneShip(Production.class, "id", Goods.class, "proid");
	
private  OneShip h = new OneShip(ShopCar.class,"fid", Goods.class, "id");
private  OneShip i = new OneShip(ShopCar.class,"fid", user.class, "id");





}
