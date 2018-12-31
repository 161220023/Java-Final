package formation;

import java.util.ArrayList;

import being.Creature;
import gui.Cell;

public class SquareCircle extends Formation{

	public SquareCircle() {
		super(FormationType.SquareCircle);
	}

	@Override
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		if(good) {
			int i;
			for(i=0;i<2;i++) {
				Creature cala=creature.get(i);
				cala.moveto(4+i*2, 3);
				cells[4+i*2][3].setbeing(cala);
			}
			for(;i<5;i++) {
				Creature cala=creature.get(i);
				cala.moveto(i*2-1, 4);
				cells[i*2-1][4].setbeing(cala);
			}
			for(;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(i*2-6, 5);
				cells[i*2-6][5].setbeing(cala);
			}
		}
		else {
			int i;
			for(i=0;i<2;i++) {
				Creature cala=creature.get(i);
				cala.moveto(4+i*2, 16);
				cells[4+i*2][16].setbeing(cala);
			}
			for(;i<5;i++) {
				Creature cala=creature.get(i);
				cala.moveto(i*2-1, 15);
				cells[i*2-1][15].setbeing(cala);
			}
			for(;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(i*2-6, 14);
				cells[i*2-6][14].setbeing(cala);
			}
		}
	}

}
