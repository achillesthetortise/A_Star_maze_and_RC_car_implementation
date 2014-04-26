import javax.swing.*;
import java.awt.*;

public class Setup extends JPanel {

    public Setup(int dim) {
	GridBoard grid = new GridBoard(dim);
	this.setSize(900,600);
	this.setLayout(new BorderLayout());
	this.add(new ControlPanel(grid),BorderLayout.WEST);
	this.add(grid,BorderLayout.CENTER);
	this.setVisible(true);
    }
    
    public Setup() {
	this(10);
    }


    public static void main(String[] args) {
	JFrame frame = new JFrame("A* Algorithm Simulation");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new GridBagLayout());
	JTextField dimField = new JTextField(5);
	JPanel setupPanel = new JPanel();
	setupPanel.add(new JLabel("Length : "));
	setupPanel.add(dimField);
	setupPanel.add(new JLabel("10-100 inclusive only"));
	int result = JOptionPane.showConfirmDialog(null, setupPanel, "Enter length value.",JOptionPane.OK_CANCEL_OPTION);
	if(result == JOptionPane.OK_OPTION) {
	    String dimtext = dimField.getText();
	    if(!dimtext.isEmpty()) {
		int dim = 10;
		try {
		    dim = Integer.parseInt(dimtext);
		} catch (NumberFormatException nfe) {
		    System.out.println("Please submit only digits 0-9, default value of 10 will be used");
		}
		if(dim >= 10 && dim <= 100) {
		    frame.add(new Setup(dim));
		} else {
		    System.out.println("Only lengths between 10 and 100 inclusive are valid");
		    frame.add(new Setup());
		}
	    } else {
		frame.add(new Setup());
	    }
	} else {
	    System.exit(0);
	}
	frame.setResizable(false);
	frame.setSize(900,600);
	frame.setVisible(true);
	frame.pack();
    }
}