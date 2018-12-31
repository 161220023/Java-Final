package formation;

public enum FormationType {
	Arrow("锋矢阵"),CraneWing("鹤翼阵"),CrescentMoon("偃月阵"),CrossBar("衡轭阵"),FishScale("鱼鳞阵"),SerpentArray("长蛇阵"),SquareCircle("方圆阵"),WildGooseRow("雁行阵");
	
	String name;
	
	FormationType(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
}
