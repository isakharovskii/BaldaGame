package mainpackage;

import java.util.LinkedList;

class Node
{
	private String str;
	private String wrd;
	LinkedList<Node> nextLevel;
	Node ()
	{
		str = "";
		wrd = "";
		nextLevel = new LinkedList<Node>();
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getWrd() {
		return wrd;
	}
	public void setWrd(String wrd) {
		this.wrd = wrd;
	}
	public Node next(char x)
	{
		for(int i = 0; i < nextLevel.size(); ++i)
			if (str.charAt(i) == x) return nextLevel.get(i);
		return null;
	}
	public void add(String s, String word)
	{
		if (s.length() == 0)
		{
			this.wrd = word;
			return;
		}
		if (str.indexOf(s.charAt(0)) != -1)
		{
			this.next(s.charAt(0)).add(s.substring(1), word);
		} else 
		{
			str += s.charAt(0);
			Node newLevel = new Node();
			if(s.length() != 0)
			{
				newLevel.add(s.substring(1), word);
			}
			nextLevel.add(newLevel);
		}
	}
	public void printAll()
	{
		if (this.wrd != "" )
			System.out.println(this.wrd);
		for (int i = 0 ; i < str.length(); ++i)
		{
			this.next(str.charAt(i)).printAll();
		}
	}
	public boolean find(String s)
	{
		if (s.length() == 0)
		{
			if (this.wrd != "") return true; else return false;
		}
		else
		{
			if (this.next(s.charAt(0)) == null) return false;
			else return this.next(s.charAt(0)).find(s.substring(1));
		}
	}
}