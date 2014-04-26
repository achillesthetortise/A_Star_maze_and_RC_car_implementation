public class Node implements Comparable{
	private int index;
	private Node parent;
	private int F;
	private int G;
	private boolean start;
	private boolean goal;
	private boolean barrier;
		
	public Node(int index, GridBoard grid){
		this.index = index;
		this.parent = null;
		this.F = Integer.MAX_VALUE;
		this.G = Integer.MAX_VALUE;
		barrier = grid.isBarrier(index);
		start = grid.getStartPosition() == index;
		goal = grid.getGoalPosition() == index;
	}
	public Node(int index){
		this.index = index;
		this.parent = null;
		this.F = Integer.MAX_VALUE;
		this.G = Integer.MAX_VALUE;
		this.barrier = true;
		this.start = false;
		this.goal = false;
	}
	public int getIndex(){
		return index;
	}
	public Node getParent(){
		return parent;
	}
	public void setParent(Node parent){
		this.parent = parent;
	}
	public int getF(){
		return F;
	}
	public void setF(int F){
		this.F = F;
	}
	public int getG(){
		return G;
	}
	public void setG(int G){
		this.G = G;
	}
	public boolean isStart(){
		return start;
	}
	public boolean isGoal(){
		return goal;
	}
	public boolean isBarrier(){
		return barrier;
	}
	public int compareTo(Object o){
		if(F < ((Node)o).getF()) return -1;
		if(F > ((Node)o).getF()) return 1;
		return 0;
	}
}
