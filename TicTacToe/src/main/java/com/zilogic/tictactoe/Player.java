package application;

import java.util.Scanner;

public class Player {

	private String player1;
	private String player2;

	 public  Scanner scanner=new Scanner(System.in);
	
	 public Player()
	{
		player1=scanner.next();
		this.player1=player1;
		player2=scanner.next();
		this.player2=player2;
	}
	

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	@Override
	public String toString() {
		return "Model [Player1=" + player1 + ", player2=" + player2 + "]";
	}

}
