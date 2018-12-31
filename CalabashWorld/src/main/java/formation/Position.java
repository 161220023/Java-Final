package formation;

import java.util.concurrent.locks.*;

import being.Being;


public class Position {
	
	Being being;
	int row,col;
	Lock lock;

	public Position(int row, int col){
		this.row=row;
		this.col=col;
		this.lock=new ReentrantLock();
	}
	
	public boolean setbeing(Being being) {
		lock.lock();
		try {
			if(this.being!=null) {//正在被其他being占领
				return false;
			}
			this.being=being;
			being.setrow(row);
			being.setcol(col);
			return true;
		}finally {
			lock.unlock();
		}
	}
	
	public void clear() {
		lock.lock();
		try {
			this.being=null;
		}finally {
			lock.unlock();
		}
	}
	
	public Being getbeing() {
		return this.being;
	}
	
	public boolean nobeing() {
		return true;
	}
	
	public int getrow() {
		return row;
	}
	
	public int getcol() {
		return col;
	}
}
