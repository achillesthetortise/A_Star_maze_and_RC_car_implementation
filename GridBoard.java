import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Random;
import java.util.*;

public class GridBoard extends JPanel {

    public final int screen_width;
    private int dim;
    private int width;
    private int[] xPositions;
    private int[] yPositions;
    private boolean[] barrierPositions;
    private boolean[] visitedPositions;
    private boolean[] pathPositions;
    private Node[] nodeMap;

    private int startPosition = -1;
    private int goalPosition = -1;
    private boolean barrierFlag = true;
    private boolean startFlag = false;
    private boolean goalFlag = false;

    public GridBoard(int dim) {
	this.dim = dim;
	this.width = 600/dim;
	this.barrierPositions = new boolean[dim*dim];
	this.visitedPositions = new boolean[dim*dim];
	this.pathPositions = new boolean[dim*dim];
	this.nodeMap = new Node[dim*dim];
	this.xPositions = getXPositions();
	this.yPositions = getYPositions();
	for(int i = 0; i < barrierPositions.length; i++) {
	    barrierPositions[i] = false;
	    visitedPositions[i] = false;
	    pathPositions[i] = false;
	}
	this.setVisible(true);
	this.setBackground(Color.WHITE);
	this.screen_width = width*dim + 2;
	this.setPreferredSize(new Dimension(screen_width, screen_width));
	this.setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 1;
	gbc.weighty = 1;
	this.addMouseListener(new BarrierClicker());
	this.addMouseMotionListener(new BarrierMotion());
    }

    public GridBoard() {
	this(10);
    }
  
    public int[] getXPositions() {
	xPositions = new int[dim];
	xPositions[0] = 0;
	for(int x = 1; x < dim; x++) {
	    xPositions[x] = xPositions[x-1] + width;
	}
	return xPositions;
    }
    
    public int[] getYPositions() {
	yPositions = new int[dim];
	yPositions[0] = 0;
	for(int y = 1; y < dim; y++) {
	    yPositions[y] = yPositions[y-1] + width;
	}
	return yPositions;
    }

    public void resetGrid() {
	barrierPositions = new boolean[dim*dim];
	visitedPositions = new boolean[dim*dim];
	pathPositions = new boolean[dim*dim];
	goalPosition = -1;
	startPosition = -1;
	repaint();
    }
    
    public void randomGrid() {
	Random rand = new Random();
/*	resetGrid();
	int b = rand.nextInt(dim*dim);
	barrierPositions = new boolean[dim*dim];
	for(int i = 0; i < b; i++) {
	    barrierPositions[rand.nextInt(barrierPositions.length)] = rand.nextBoolean();
	}
	repaint();
*/
	resetGrid();
	nodeMap = new Node[dim*dim];
	for(int i = 0; i < dim*dim; i++){
		barrierPositions[i] = true;
		nodeMap[i] = new Node(i);
	}
	Node root = new Node(rand.nextInt(dim*dim));
	barrierPositions[root.getIndex()] = false;
	setStart(root.getIndex());
	ArrayList<Node> list = new ArrayList<Node>();
	list = addWalls(root, list);
	while(!list.isEmpty()){
		Node cur = list.remove((int)(Math.random()*list.size()));
		Node opposite = getOpposite(cur);
		if(opposite != null){
			if(barrierPositions[cur.getIndex()] && barrierPositions[opposite.getIndex()]){
				barrierPositions[cur.getIndex()] = false;
				barrierPositions[cur.getParent().getIndex()] = false;
				barrierPositions[opposite.getIndex()] = false;
				list = addWalls(opposite, list);
			}
		}
		if(list.isEmpty()){
			if(!barrierPositions[opposite.getIndex()]){
				setGoal(opposite.getIndex());
			} else {
				GridModel grid = new GridModel(this);
				grid.needToPlaceGoal();
			}
		}		
	}
	repaint();
    }


    private Node getOpposite(Node n){
	int cur = n.getIndex();
	int par = n.getParent().getIndex();
	try{
		if(cur == par + dim && cur / dim < dim - 1) return nodeMap[cur+dim];
		if(cur == par - dim && cur / dim > 0) return nodeMap[cur-dim];
		if(cur == par + 1 && cur % dim < dim - 1) return nodeMap[cur+1];
		if(cur == par - 1 && cur % dim > 0) return nodeMap[cur-1];		
	}catch(Exception e){}
	return null;
    }

    private ArrayList<Node> addWalls(Node cell, ArrayList<Node> list){
	boolean top = false;
	boolean bottom = false;
	boolean left = false;
	boolean right = false;
	int index = cell.getIndex();
	if(cell.getIndex() >= dim){
		top = true;
	}
	if(cell.getIndex() % dim < dim-1){
		right = true;
	}
	if(cell.getIndex() + dim < dim*dim){
		bottom = true;
	}
	if(cell.getIndex() % dim > 0){
		left = true;
	}
	if(top && barrierPositions[index-dim]){
		nodeMap[index-dim].setParent(cell);
		list.add(nodeMap[index-dim]);
	}
	if(right && barrierPositions[index+1]){
		nodeMap[index+1].setParent(cell);
		list.add(nodeMap[index+1]);
	}
	if(bottom && barrierPositions[index+dim]){
		nodeMap[index+dim].setParent(cell);
		list.add(nodeMap[index+dim]);
	}
	if(left && barrierPositions[index-1]){
		nodeMap[index-1].setParent(cell);
		list.add(nodeMap[index-1]);
	}
	return list;
    }
    
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(Color.BLACK);
	for(int c = 0; c < dim; c++) {
	    for(int r = 0; r < dim; r++) {
		if(isBarrier(c,r)){
		    g.setColor(Color.RED);
		    g.fillRect(xPositions[c]+1, yPositions[r]+1,width,width);
		    g.setColor(Color.BLACK);
		} else if( ((r*dim) + c) == startPosition) {
		    g.setColor(Color.BLUE);
		    g.fillRect(xPositions[c]+1, yPositions[r]+1,width,width);
		    g.setColor(Color.BLACK);
		} else if( ((r*dim) + c) == goalPosition) {
		    g.setColor(Color.GREEN);
		    g.fillRect(xPositions[c]+1, yPositions[r]+1,width,width);
		    g.setColor(Color.BLACK);
		} else if(isPath(c,r)) {
		    g.setColor(Color.CYAN);
		    g.fillRect(xPositions[c]+1, yPositions[r]+1,width,width);
		    g.setColor(Color.BLACK);
		} else if(isVisited(c,r)) {
		    g.setColor(Color.YELLOW);
		    g.fillRect(xPositions[c]+1, yPositions[r]+1,width,width);
		    g.setColor(Color.BLACK);
		} 
		g.drawRect(xPositions[c]+1, yPositions[r]+1,width,width);
	    }
	}
    }

    public boolean isPath(int x, int y) { return pathPositions[(y*dim) + x%dim]; }
    
    public void setPathNode(int index) { pathPositions[index] = true; }

    public void visitNode(int index) { visitedPositions[index] = true; }
    
    public boolean isVisited(int x, int y) { return visitedPositions[(y*dim) + x%dim]; }

    private boolean isBarrier(int x, int y) { return barrierPositions[(y*dim) + x%dim]; }

    public boolean isBarrier(int index) { return barrierPositions[index]; }

    public int getPosition(int xPos, int yPos) {
	int r = 0;
	int c = 0;
	if(xPos < 0 || yPos < 0 || xPos > screen_width || yPos > screen_width) return -1;
	if(xPositions[dim-1] < xPos) {
	    c = dim-1;
	}
	if(yPositions[dim-1] < yPos) {
	    r = dim-1;
	}
	while(c < dim-1) {
	    if(xPositions[c] <= xPos && xPositions[c+1] >= xPos) break;
	    c++;
	}
	while(r < dim-1) {
	    if(yPositions[r] <= yPos && yPositions[r+1] >= yPos) break;
	    r++;
	}
	return (r*dim) + c;
    }

    public void setBarrierPosition(int xPos, int yPos, boolean state) {
	int position = getPosition(xPos,yPos);
	if(position != startPosition && position != goalPosition) {
	barrierPositions[position] = state;
	}
	this.repaint();
    }

   public boolean getBarrierState(int xPos, int yPos){
	return barrierPositions[getPosition(xPos,yPos)];
   }
    
    public void setStartPosition(int xPos, int yPos) {
	int position = getPosition(xPos, yPos);
	if(!barrierPositions[position] && goalPosition != position) startPosition = position;
	repaint();
    }
    public int getStartPosition() { return this.startPosition; }
    
    public void setStart(int i){ startPosition = i; }
    public void setGoal(int i){ goalPosition = i; }

    public void setGoalPosition(int xPos, int yPos) {
	int position = getPosition(xPos,yPos);
	if(!barrierPositions[position] && startPosition != position) goalPosition = position;
	repaint();
    }
    public int getGoalPosition() { return this.goalPosition; }

    public void setBarrierFlag(boolean state) {
	if(state) {
	    this.startFlag = false;
	    this.goalFlag = false;
	}
	this.barrierFlag = state;
    }
    
    public void setStartFlag(boolean state) {
	if(state) {
	    this.barrierFlag = false;
	    this.goalFlag = false;
	}
	this.startFlag = state;
    }
    
    public void setGoalFlag(boolean state) {
	if(state) {
	    this.barrierFlag = false;
	    this.startFlag = false;
	}
	this.goalFlag = state;
    }

    public int getDim() {
	return this.dim;
    }
    
private class BarrierClicker implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		Point clickPoint = e.getPoint();
		if(barrierFlag) {
			if(getBarrierState(clickPoint.x,clickPoint.y)){
				GridBoard.this.setBarrierPosition(clickPoint.x,clickPoint.y,false);
			} else {
		    		GridBoard.this.setBarrierPosition(clickPoint.x,clickPoint.y,true);
			}
		} else if(goalFlag) {
			if(getPosition(clickPoint.x,clickPoint.y) == goalPosition){
				goalPosition = -1;
				repaint();
			} else {
		    		GridBoard.this.setGoalPosition(clickPoint.x,clickPoint.y);
			}
		} else if(startFlag){
			if(getPosition(clickPoint.x,clickPoint.y) == startPosition){
		    		startPosition = -1;
				repaint();
			} else {
				GridBoard.this.setStartPosition(clickPoint.x,clickPoint.y);
			}
		}
	}
	
	public void mouseEntered(MouseEvent e)  { 	 }
	public void mouseExited(MouseEvent e)   {   	 }
	public void mousePressed(MouseEvent e)  {     	 } 
	public void mouseReleased(MouseEvent e) {	 }
    }

    private class BarrierMotion implements MouseMotionListener {

	public void mouseDragged(MouseEvent e){
	    Point clickPoint = e.getPoint();
	    if(barrierFlag && clickPoint.x >= 0 && clickPoint.y >=0
	       && clickPoint.x <= screen_width && clickPoint.y <= screen_width) {
		if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == 0) {
		    GridBoard.this.setBarrierPosition(clickPoint.x,clickPoint.y,true);
		} else {
		    GridBoard.this.setBarrierPosition(clickPoint.x,clickPoint.y,false);
		}
	    }
	}
	public void mouseMoved(MouseEvent e){

	}
    }
}
