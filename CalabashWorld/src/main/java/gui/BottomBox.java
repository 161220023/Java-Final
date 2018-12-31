package gui;


import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class BottomBox extends VBox{
	
	int number;
	ArrayList<Node> labels;
	
	public BottomBox(int number){
		this.number=number;
		labels=new ArrayList<>();
	}
	
	public void add(Node label) {
		int len=labels.size();
		if(len==number)
			labels.remove(0);
		labels.add(label);
		this.getChildren().clear();
		this.getChildren().addAll(labels);
	}
	
	public void clear() {
		this.getChildren().clear();
		labels.clear();
	}
}
