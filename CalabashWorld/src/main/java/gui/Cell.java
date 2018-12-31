package gui;

import being.Being;
import javafx.scene.control.*;
import javafx.scene.image.*;


public class Cell extends Label {
	
	final int HW=50;
	int row,col;

	public Cell(int row, int col){
		
		this.row=row;
		this.col=col;
		
		this.setPrefSize(HW, HW);
		this.setMaxSize(HW, HW);
		this.setMinSize(HW, HW);
		
		this.setOpacity(1);

	}
	
	public void setpic(ImageView view) {
		this.setGraphic(view);
	}
	
	public <T extends Being> void setbeing(T being) {
		if(being!=null) {
			ImageView view=being.getview();
			this.setGraphic(view);
		}
	}
	
	public void clear() {
		this.setGraphic(null);
	}

}