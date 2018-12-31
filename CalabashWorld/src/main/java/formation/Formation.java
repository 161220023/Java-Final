package formation;

import java.util.ArrayList;

import annotation.Author;
import being.Creature;
import gui.Cell;

@Author(name="chy",number="161220023",email="161220023@smail.nju.edu.cn")
public abstract class Formation {
	FormationType type;
	public Formation(FormationType type){
		this.type=type;
	}
	
	FormationType getFormType() {
		return type;
	}
	
	String getFormName() {
		return type.name;
	}
	
	public abstract void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good);
	
	public void clear(Cell[][] cells, ArrayList<Creature> creature) {
		int len=creature.size();
		for(int i=0;i<len;i++) {
			Creature curcreature=creature.get(i);
			int row=curcreature.getrow();
			int col=curcreature.getcol();
			curcreature.moveto(-1, -1);
			if(row>=0&&row<=10&&col>=0&&col<=19)
				cells[row][col].clear();
		}
	}
}
