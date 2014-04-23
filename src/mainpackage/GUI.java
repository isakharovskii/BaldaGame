package mainpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9108851024127595369L;

	GUI(String str)
	{
		super(str);
		makeGUI();
	}
	public void makeGUI()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		JPanel pane = new JPanel();
		pane.setSize(300, 200);
		
		GridLayout glay = new GridLayout(2, 3);
		pane.setLayout(glay);
		
		JPanel[][] panes = new JPanel[2][3];
		for (int i = 0; i < 2; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				panes[i][j] = new JPanel();
				panes[i][j].setBackground(Color.DARK_GRAY);
				pane.add(panes[i][j]);
			}
		}
		Listener listener = new Listener();
		JButton host = new JButton("Host");
		JButton join = new JButton("Join");
		
		host.addActionListener(listener);
			
		join.addActionListener(listener);
		
		panes[1][0].add(host);
		panes[1][2].add(join);
		
		JTextField field = new JTextField();
		
		field.setEnabled(false);
		field.setEditable(false);
		field.setFont(new Font("TimesNewRoman", Font.BOLD, 20));
		
		field.setAlignmentX(CENTER_ALIGNMENT);
		field.setAlignmentY(CENTER_ALIGNMENT);
		
		field.setBackground(Color.DARK_GRAY);
		field.setDisabledTextColor(Color.red);
		
		field.setBorder(null);
		field.setText("Á À Ë Ä À");
		
		panes[0][1].setLayout(new BorderLayout());
		
		panes[0][1].add(field, BorderLayout.CENTER);
		
		this.setSize(pane.getSize());
		this.setContentPane(pane);
		
		this.setVisible(true);
	}
	
	public void FrameNotVisible()
	{
		this.setVisible(false);
	}
	
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				new GUI("BALDA");
			}
		});
	}
}
class Listener implements ActionListener
{
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
		case "Host":
			mainpackage.client.host();
			break;
		case "Join":
			mainpackage.client.join();
			break;
		}
		new mainpackage.GameClient(ae.getActionCommand());
	}
}
