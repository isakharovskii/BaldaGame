package mainpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -9108851024127595369L;
	private JTextField IParea;
	private int n = 5;
	Listener listener;
	private String str = "127.0.0.1";
	private String [] num = {"5", "6", "7"};
	private JComboBox<String> box;
	JFrame frame = null;
	private JCheckBox chBox;
	GUI(String str)
	{
		super(str);
		
		makeGUI();
	}
	public void makeGUI()
	{
		this.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				GameClient.sendCloseOption();
				GameClient.closeAllsockets();
			}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		chBox = new JCheckBox("vsBot");
		box = new JComboBox<String>(num);
		IParea = new JTextField(15);
		IParea.setText(str);
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
		listener = new Listener();
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
		
		
		JMenu menu = new JMenu("Main");
		JMenuItem settings = new JMenuItem("Settings");
		
		settings.addActionListener(new menuListener());
		menu.add(settings);
		JMenuBar bar = new JMenuBar();
		bar.add(menu);
		
		this.setJMenuBar(bar);
		
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
	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			boolean vsBot = chBox.isSelected();
			//System.out.println("Text^ " + IParea.getText());
			if (!IParea.getText().isEmpty()) str = IParea.getText();
			mainpackage.client.setIp(str);
			//System.out.println("Str^ " + str);
			n = box.getSelectedIndex() + 5;
			//System.out.println(n);
			switch(ae.getActionCommand())
			{
			case "Host":
				if (!vsBot)
				{
					mainpackage.client.host(n);
				}
				new mainpackage.GameClient(ae.getActionCommand(), n, vsBot);
				break;
			case "Join":
				if (!vsBot)
				{
					mainpackage.client.join(n);
				}
				new mainpackage.GameClient(ae.getActionCommand(), n, vsBot);
				break;
			case "Save":
				if (!IParea.getText().isEmpty()) str = IParea.getText();
				mainpackage.client.setIp(str);
				n = box.getSelectedIndex() + 5;
				frame.setVisible(false);
				break;
			}
			
			//new mainpackage.GameClient(ae.getActionCommand(), n, vsBot);
		}
	}
	
	class menuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			if (frame == null)
			{
				frame = new JFrame("Settings");
				 
				
				frame.setLayout(new BorderLayout());
				
				JPanel pane = new JPanel(new FlowLayout());
				
				pane.add(new JLabel("Set Server IP: "));
				pane.add(IParea);
							
				
				JPanel numPane = new JPanel(new FlowLayout());
				
				numPane.add(new JLabel("Choose game field"));
				numPane.add(box);
				
				JPanel botPane = new JPanel(new FlowLayout());
				botPane.add(new JLabel("Choose this if you want play vs bot"));
				botPane.add(chBox);
				pane.add(numPane);
				pane.add(botPane);
				JButton save = new JButton("Save");
				save.addActionListener(listener);
				
				pane.add(save);
				frame.add(pane, BorderLayout.CENTER);
				frame.setSize(300, 200);
				frame.setVisible(true);
			}
			else frame.setVisible(true);
		}
	}
}

