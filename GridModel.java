import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;

public class GridModel{

	private GridBoard grid;
	private PriorityQueue<Node> openList;
	private HashSet<Node> closedList;
	private Node[] nodeMap;
	
	
	public GridModel(GridBoard grid){
		this.grid = grid;
	}

	public void performAStar(){
		if(grid.getStartPosition() == -1) needToPlaceStart();
		if(grid.getGoalPosition() == -1) needToPlaceGoal();

		nodeMap = new Node[grid.getDim()*grid.getDim()];		
		closedList = new HashSet<Node>();
		openList = new PriorityQueue<Node>();
		
		for(int i = 0; i < nodeMap.length; i++) nodeMap[i] = new Node(i,grid);

		Node cur = nodeMap[grid.getStartPosition()];
		cur.setG(0);
		cur.setF(cur.getG()+costEstimate(cur.getIndex()));
		openList.add(cur);

		while(!openList.isEmpty()){
			cur = openList.poll();
			grid.visitNode(cur.getIndex());
			if(cur.isGoal()){
				pathReconstruct();
				return;
			}
			closedList.add(cur);
			Node[] neighbors = getNeighbors(cur.getIndex());
			for(int i = 0; i < neighbors.length; i++){
				if(!neighbors[i].isBarrier()){
					int gScore = cur.getG()+1;
					int fScore = gScore + costEstimate(neighbors[i].getIndex());
					if(closedList.contains(neighbors[i]) && fScore >= neighbors[i].getF()) continue;
					if(!openList.contains(neighbors[i]) || fScore <= neighbors[i].getF()){
						neighbors[i].setParent(cur);
						neighbors[i].setG(gScore);
						neighbors[i].setF(fScore);
						if(!openList.contains(neighbors[i])) openList.add(neighbors[i]);
					}
				}
			}
		}
		pathNotFound();
	}

	public void needToPlaceStart(){
		int i = 0;
		while(grid.isBarrier(i)) i++;
		grid.setStart(i);
	}

	public void needToPlaceGoal(){
		int i = (grid.getDim()*grid.getDim())-1;
		while(grid.isBarrier(i)) i--;
		grid.setGoal(i);
	}

	private void pathReconstruct(){
		Stack<Integer> directions = new Stack<Integer>();
		Node cur = nodeMap[grid.getGoalPosition()];
		while(cur.getParent().getIndex() != grid.getStartPosition()){
			if(cur.getParent().getIndex() == cur.getIndex()+grid.getDim()){
				directions.push(1);
			} else if(cur.getParent().getIndex() == cur.getIndex()-1){
				directions.push(2);
			} else if(cur.getParent().getIndex() == cur.getIndex()-grid.getDim()){
				directions.push(3);
			} else {
				directions.push(4);
			}
			cur = cur.getParent();
			grid.setPathNode(cur.getIndex());
		}
		Graphics g = grid.getGraphics();
		if(g != null) grid.paintComponent(g);
		else grid.repaint();
		//Subprocess sentToArduino = new Subprocess(directions);
		//while(!directions.isEmpty()) System.out.println(directions.pop());
	}

	private void pathNotFound(){
		grid.repaint();
		JOptionPane.showMessageDialog(null, "Path not found!", "Alert!", JOptionPane.ERROR_MESSAGE);
	}

	private Node[] getNeighbors(int index){
		boolean top = false;
		boolean bottom = false;
		boolean left = false;
		boolean right = false;
		int count = 0;
		if(index >= grid.getDim()){
			top = true;
			count++;
		}
		if(index % grid.getDim() < grid.getDim()-1){
			right = true;
			count++;
		}
		if(index + grid.getDim() < nodeMap.length){
			bottom = true;
			count++;
		}
		if(index % grid.getDim() > 0){
			left = true;
			count++;
		}
		Node[] neighbors = new Node[count];
		count = 0;
		if(top) neighbors[count++] = nodeMap[index-grid.getDim()];
		if(right) neighbors[count++] = nodeMap[index+1];
		if(bottom) neighbors[count++] = nodeMap[index+grid.getDim()];
		if(left) neighbors[count++] = nodeMap[index-1];
		return neighbors;
	}

	private int costEstimate(int index){
		int r = index / grid.getDim();
		int c = index % grid.getDim();
		int rH = Math.abs((grid.getGoalPosition() / grid.getDim()) - r);
		int cH = Math.abs((grid.getGoalPosition() % grid.getDim()) - c);
		return rH + cH;
	}
}
