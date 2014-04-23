package mainpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class GameClient {
	
	ServerSocket ssocket;
	int port = 10001;
	
	private static Socket socket;
	private static InputStream is;
	private static OutputStream os;
	private static DataInputStream in;
	private static DataOutputStream out;
	
	GameClient(String str)
	{
		switch(str)
		{
		case "Host":
			try{
				ssocket = new ServerSocket(port);
				
				socket = ssocket.accept();
				
				is = socket.getInputStream();
				os = socket.getOutputStream();
				
				in = new DataInputStream(is);
				out = new DataOutputStream(os);
				
				new gameInterface(true);
			}catch(Exception e){e.printStackTrace();}
			break;
		case "Join":
			try{
				InetAddress Connect = mainpackage.client.getAddress();
				String str1 = Connect.getHostAddress();
				System.out.println(str1);
				socket = new Socket(str1, port);
				
				is = socket.getInputStream();
				os = socket.getOutputStream();
				
				in = new DataInputStream(is);
				out = new DataOutputStream(os);
				
				new gameInterface(false);
			}catch(IOException e) {e.printStackTrace();}
		}
		
	}
	
	public static void sendWord(int x, int y, String ch, String str) throws IOException
	{
		out.writeInt(x);
		out.writeInt(y);
		out.writeUTF(ch);
		out.writeUTF(str);
		gameInterface.setYourPoints(gameInterface.getYourPoints() + str.length());
		gameInterface.getUsedWords().add(str);
		//out.flush();
	}
	public static void getWord() throws IOException
	{
		int x, y;
		x = in.readInt();
		y = in.readInt();
		String ch = in.readUTF();
		String str = in.readUTF();
		
		gameInterface.setEnemyPoints(gameInterface.getEnemyPoints() + str.length());
		gameInterface.getUsedWords().add(str);
		
		String result = gameInterface.getYourPoints() + "\t" + gameInterface.getEnemyPoints();
		gameInterface.getScore().setText(result);
		
		
		gameInterface.setNumberOfturns(gameInterface.getNumberOfturns() + 1);
		System.out.println(x + " " + y + " " + str);
		if (gameInterface.getNumberOfturns() >= 25)
		{
			JOptionPane.showConfirmDialog(gameInterface.getFrame(), "You lose");
		} else {
			gameInterface.getButtons()[x][y].setText(ch);
			gameInterface.setYourTurn(true);
			gameInterface.startNewTurn();
			gameInterface.refreshColor(true);
		}
	}
}

class gameInterface {
	private static boolean yourTurn;
	private static String start = "GLASS";
	static int numberOfturns = 5;
	private static int yourPoints = 0;
	private static int EnemyPoints = 0;
	private static LinkedList<String> usedWords;
	
	private static JButton[][] buttons;
	public static JLabel label;
	private static JLabel score;
	static JTextArea Word;
	static JTextArea wordArea;
	static JButton endTurn;
	private static JFrame frame;
	
	static int[][] Map= new int[5][5];
	static int choosenX, choosenY;
	static boolean choosenChar = false;
	static boolean newChar;
	static String result;
	
	gameInterface(boolean isHost) throws IOException
	{
		setYourTurn(isHost);
		makeGUI();
		if (!isHost) GameClient.getWord();
	}
	
	static class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			if (isYourTurn())
			{
				switch (ae.getActionCommand())
				{
				case "OK":
					try {
						if (mainpackage.MainClass.sendRequest(result) && choosenChar && !isUsed(result) )
						{
							GameClient.sendWord(choosenX, choosenY, getButtons()[choosenX][choosenY].getText(), result);
							refreshColor(isYourTurn());
							setNumberOfturns(getNumberOfturns() + 1);
							if (getNumberOfturns() >= 25)
							{
								if (getYourPoints() > getEnemyPoints())
									JOptionPane.showConfirmDialog(getFrame(), "You won", "message",JOptionPane.INFORMATION_MESSAGE);
								else JOptionPane.showConfirmDialog(getFrame(), "You lose", "message", JOptionPane.INFORMATION_MESSAGE);
							}
							GameClient.getWord();
						} else
							{
								wordArea.setText("No word there");
								buttons[choosenX][choosenY].setText("");
								Map[choosenX][choosenY] = 1;
								startNewTurn();
							}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					break;
				default:
					System.out.println(ae.getSource());
					String[] tmp = ae.getSource().toString().split(",");
					int y = Integer.parseInt(tmp[1]) / 58;
					int x = Integer.parseInt(tmp[2]) / 43;
					
					switch(Map[x][y])
					{
					case 1:
						if (!Word.getText().isEmpty())
						{
							if (!result.isEmpty()) result = new String();
							Character ch = Word.getText().charAt(0);
							getButtons()[x][y].setText(ch.toString().toUpperCase());
							if (choosenX != -1)
							{
								Map[choosenX][choosenY] = 1;
								getButtons()[choosenX][choosenY].setText("");
							}
							Map[x][y] = 2;
							newChar = true;
							choosenX = x;
							choosenY = y;
							
						} else Word.setText("Error");
						break;
					case 2:
						Character ch = Word.getText().charAt(0);
						Character ch2 = getButtons()[x][y].getText().charAt(0);
						if (x == choosenX && y == choosenY && ch2.charValue() != ch.charValue() )
						{
							getButtons()[x][y].setText(ch.toString().toUpperCase());
						} else
						{
							if (newChar) createWord(x, y);
						}
						break;
					case 3:
						createWord(x, y);
						break;
					}
					break;
				case "Clear":
					if (choosenX != -1 && choosenY != -1)
					{
						getButtons()[choosenX][choosenY].setText("");
						startNewTurn();
					}
					break;
				}
			}	
		}
	}
	
	private static void makeGUI()
	{
		setFrame(new JFrame("Frame"));
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout());
		
		JPanel gamePanel = new JPanel();
		
		gamePanel.setLayout(new GridLayout(5,5));
		
		setButtons(new JButton[5][5]);
		
		result = new String();
		
		Listener list = new Listener();
		for (int i = 0; i < 5; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				getButtons()[i][j] = new JButton();
				getButtons()[i][j].addActionListener(list);
				getButtons()[i][j].setFont(new Font("TimesNewRoman", Font.PLAIN, 20));
				gamePanel.add(getButtons()[i][j]);
			}
		}
		
		for (int i = 0; i < 5; ++i)
		{
			Character ch = start.charAt(i);
			getButtons()[2][i].setText(ch.toString());
		}
		startNewTurn();
		
		mainPanel.add(gamePanel, BorderLayout.CENTER);
	
		
		Word = new JTextArea();
		
		gamePanel.setSize(new Dimension(300, 300));
		
		
		JPanel endTurnPanel = new JPanel(new GridLayout(1,3));
		wordArea = new JTextArea();
		endTurn = new JButton("OK");
		setScore(new JLabel());
		
		endTurnPanel.add(wordArea);
		endTurnPanel.add(getScore());
		endTurnPanel.add(endTurn);
		
		endTurn.addActionListener(list);
		
		JPanel areaPanel = new JPanel(new GridLayout(1, 3));
		
		label = new JLabel();
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(list);
		
		areaPanel.add(label);
		areaPanel.add(clear);
		areaPanel.add(Word);
		
		mainPanel.add(areaPanel, BorderLayout.NORTH);
		mainPanel.add(endTurnPanel, BorderLayout.SOUTH);
		mainPanel.setSize(gamePanel.getSize());
		getFrame().setContentPane(mainPanel);
		
		getFrame().setSize(mainPanel.getSize());
		getFrame().setVisible(true);
		getFrame().setResizable(false);
		
		
		setUsedWords(new LinkedList<String>());
		wordArea.setEditable(false);
		refreshColor(isYourTurn());
		game();
		
		
	}
	public static void startNewTurn() {
		choosenX = -1;
		choosenY = -1;
		choosenChar = false;
		newChar = false;
		for (int i = 0; i < 5; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				Map[i][j] = 0;
			}
		}
		for (int i = 0; i < 5; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				if (!getButtons()[i][j].getText().isEmpty()) Map[i][j] = 2;
			}
		}
		for (int i = 0; i < 5; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				if (Map[i][j] == 2)
				{
					if ( (i + 1) < 5)
					{
						if (Map[i+1][j] != 2) Map[i + 1][j] = 1;
					}
					if ( (i - 1) >= 0)
					{
						if (Map[i-1][j] != 2) Map[i - 1][j] = 1;
					}
					if ( (j + 1) < 5)
					{
						if (Map[i][j+1] != 2) Map[i][j+1] = 1;
					}
					if ( (j - 1) >= 0)
					{
						if (Map[i][j - 1] != 2) Map[i][j - 1] = 1;
					}
				}
			}
		}
		game();
		
	}
	public static void refreshColor(boolean turn)
	{
		if (turn) Word.setEditable(true); else Word.setEditable(false);
		if (turn) label.setText("Your Turn"); else label.setText("Enemy Turn");
	}
	
	public static void createWord(int x, int y)
	{
		if (x == choosenX && y == choosenY) choosenChar = true;
		result += getButtons()[x][y].getText();
		wordArea.setText(result);
		getButtons()[x][y].setBackground(Color.yellow);
		
		Map[x][y] = 4;
		
		for (int i = 0; i < 5; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				if (Map[i][j] == 3) Map[i][j] = 2;
			}
		}
		for (int j = 0; j < 5; ++j)
		{	
			for (int i = 0; i < 5; ++i)
			{
				if (Math.abs(j - x) == 1)
				{
					if (i == y && (Map[j][i] == 2)) Map[j][i] = 3;
				}
				else 
					if (Math.abs(i - y) == 1)
					{
						if (j == x && (Map[j][i] == 2)) Map[j][i] = 3;
					}
			}
		}
		for (int i= 0; i < 5; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				if (Map[i][j] != 3) getButtons()[i][j].setEnabled(false); else getButtons()[i][j].setEnabled(true);
			}
		}
	
	}
	public static void game()
	{
		for (int i= 0; i < 5; ++ i)
		{
			for (int j = 0; j < 5; ++j)
			{
				switch(Map[i][j])
				{
				case 0:
					getButtons()[i][j].setEnabled(false);
					getButtons()[i][j].setBackground(Color.LIGHT_GRAY);
					break;
				default:
					getButtons()[i][j].setEnabled(true);
					getButtons()[i][j].setBackground(Color.CYAN);
					break;
				}
			}
		}
	}
	public static boolean isUsed(String str)
	{
		for(String s: getUsedWords())
		{
			System.out.println(s);
			if (s.equals(str))
			{
				wordArea.setText("Word already used");
				return true;
			}
		}
		return false;
	}
	public static JButton[][] getButtons() {
		return buttons;
	}
	public static void setButtons(JButton[][] buttons) {
		gameInterface.buttons = buttons;
	}
	public static boolean isYourTurn() {
		return yourTurn;
	}
	public static void setYourTurn(boolean yourTurn) {
		gameInterface.yourTurn = yourTurn;
	}
	public static int getNumberOfturns() {
		return numberOfturns;
	}
	public static void setNumberOfturns(int numberOfturns) {
		gameInterface.numberOfturns = numberOfturns;
	}
	public static JFrame getFrame() {
		return frame;
	}
	public static void setFrame(JFrame frame) {
		gameInterface.frame = frame;
	}
	public static int getEnemyPoints() {
		return EnemyPoints;
	}
	public static void setEnemyPoints(int enemyPoints) {
		EnemyPoints = enemyPoints;
	}
	public static LinkedList<String> getUsedWords() {
		return usedWords;
	}
	public static void setUsedWords(LinkedList<String> usedWords) {
		gameInterface.usedWords = usedWords;
	}
	public static JLabel getScore() {
		return score;
	}
	public static void setScore(JLabel score) {
		gameInterface.score = score;
	}
	public static int getYourPoints() {
		return yourPoints;
	}
	public static void setYourPoints(int yourPoints) {
		gameInterface.yourPoints = yourPoints;
	}
}
