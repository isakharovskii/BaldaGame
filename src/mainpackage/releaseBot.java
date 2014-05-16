package mainpackage;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;


public class releaseBot {
	private	static int chooseX, chooseY;
	private static char ch;
	private static Node toFind;
	private static String maxString = "";
	public static void findWord(Node dictionary, String [][] gameField)
	{
		toFind = dictionary;
		int n = gameField.length;
		int [][] Map = new int[n][n];
		for (int i = 0; i <n; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (gameField[i][j].isEmpty()) Map[i][j] = 0;
				else Map[i][j] = 2;
			}
		}
		for (int i = 0; i < n; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (Map[i][j] == 2)
				{
					if ( (i + 1) < n)
					{
						if (Map[i+1][j] != 2) Map[i + 1][j] = 1;
					}
					if ( (i - 1) >= 0)
					{
						if (Map[i-1][j] != 2) Map[i - 1][j] = 1;
					}
					if ( (j + 1) < n)
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
		for (int i = 0; i <n; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (Map[i][j] == 1)
				{
					for (int k = 0; k < 33; ++k)
					{
						char begin = (char) ('À' + k);
						LinkedList<String> words = makeAllwords(n, i, j, Character.toString(begin), Map, gameField);
						findMax(i, j, words);
					}
				}
			}
		}
	}
	private static void findMax(int i, int j, LinkedList<String> words)
	{
		for (int k = 0; k < words.size(); ++k)
		{
			String thisString = words.get(k);
			//System.out.println(thisString);
			//System.out.println();
			if (thisString.length() > maxString.length() && !gameInterface.isUsed(thisString))
			{
				maxString = thisString;
				chooseX = i;
				chooseY = j;
				ch = maxString.charAt(0);
			}
		}
	}
	private static LinkedList<String> makeAllwords(int n, int i, int j, String begin, int[][] Map, String[][] gameField)
	{
		LinkedList<String> words = new LinkedList<String>();
		String next = findNewWord(begin, gameField, n, i, j, new int[n][n], 0);
		if (next != null) words.add(next);
		return words;
	}
	private static String findNewWord(String begin, String[][] gameField, int n, int i, int j, int [][] Map, int last)
	{
		//System.out.println(begin);
		Map[i][j] = 3;
		if (toFind.find(begin)) return begin;
		if ( (i + 1) < n   &&  !gameField[i+1][j].isEmpty() && Map[i+1][j] != 3 && last != 1)
		{
			return findNewWord(begin + gameField[i+1][j], gameField, n, i + 1, j, Map, 3);
		}
		if ( (i - 1) >= 0 && !gameField[i-1][j].isEmpty() && Map[i-1][j] != 3 && last != 3)
		{
			return findNewWord(begin + gameField[i-1][j], gameField, n, i - 1, j, Map, 1);
		}
		if ( (j + 1) < n && !gameField[i][j+1].isEmpty() && Map[i][j+1] != 3 && last != 4)
		{
			return findNewWord(begin + gameField[i][j+1], gameField, n, i, j + 1, Map, 2);
		}
		if ( (j - 1) >= 0 && !gameField[i][j-1].isEmpty() && Map[i][j-1] != 3 && last != 2)
		{
			return findNewWord(begin + gameField[i][j-1], gameField, n, i, j-1, Map, 4);
		}
		
		
		return null;
	}
	public int getX()
	{
		return chooseX;
	}
	public int getY()
	{
		return chooseY;
	}
	public String getWord()
	{
		return maxString;
	}
	public static void toNewStep() {
		mainpackage.gameInterface.setButton(chooseX, chooseY,maxString.substring(0, 1));
		mainpackage.gameInterface.addUsedWord(maxString);
		mainpackage.gameInterface.increaseEnemyPoint(maxString.length());
		System.out.println("String is: " + maxString);
		maxString = "";
	}
}