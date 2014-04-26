import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Random;

public class ControlPanel extends JPanel {

    private GridBoard grid;
    private GridModel model;
    private Timer timer;

    @SuppressWarnings("unchecked")
    public ControlPanel(GridBoard grid) {

	this.grid = grid;
	this.model = new GridModel(grid);
	JButton startButton = new JButton("Start");
	JButton resetButton = new JButton("Reset");
	JButton randomButton = new JButton("Randomize");
//	String[] boxes = {"Barrier","Start","Goal"};
//	JComboBox options = new JComboBox(boxes);
	JRadioButton barrier = new JRadioButton("Barrier");
	JRadioButton start = new JRadioButton("Start");
	JRadioButton goal = new JRadioButton("Goal");
	barrier.setSelected(true);
	barrier.addActionListener(new OptionListener());
	start.addActionListener(new OptionListener());
	goal.addActionListener(new OptionListener());
	ButtonGroup group = new ButtonGroup();
	group.add(barrier);
	group.add(start);
	group.add(goal);
	JPanel menu = new JPanel(new GridLayout(0,1));
	menu.add(barrier);
	menu.add(start);
	menu.add(goal);
//	options.setSelectedIndex(0);
//	options.addActionListener(new OptionListener());
	startButton.addActionListener(new StartListener());
	resetButton.addActionListener(new ResetListener());
	randomButton.addActionListener(new RandomListener());
	this.setLayout(new GridLayout(0,1));
//	this.add(options);
	this.add(menu);
	this.add(startButton);
	this.add(resetButton);
	this.add(randomButton);
	this.setPreferredSize(new Dimension(300,0));
	this.setVisible(true);
    }
    
    private class OptionListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
	    String option = ((JRadioButton)ae.getSource()).getText();
		System.out.println(option);
	    if(option.equals("Barrier")) {
		ControlPanel.this.grid.setBarrierFlag(true);
	    } else if(option.equals("Start")) {
		ControlPanel.this.grid.setStartFlag(true);
	    } else if(option.equals("Goal")) {
		ControlPanel.this.grid.setGoalFlag(true);
	    }
	}
    }

    private class StartListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
	    ControlPanel.this.model.performAStar();
	}
    }
    private class ResetListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
	    ControlPanel.this.grid.resetGrid();
	}
    }
    private class RandomListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
	    ControlPanel.this.grid.randomGrid();
	}
    }
}
