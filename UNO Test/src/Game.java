import java.util.ArrayList;
import java.util.Collections;

public class Game {
	ArrayList<Player> player = new ArrayList<Player>(4);
	Player p1 = new Player("P1");
	Player p2 = new Player("P2");
	Player p3 = new Player("P3");
	
	public void changePlayer() {
		player.add(player.size()-1,player.remove(0)); //next player
	}
	
	public void reverseOrder() {
		Collections.reverse(player);
		CardTest.arrows.setScaleX(-1);
	}
	
	public void skipPlayerTurn() {
		player.add(player.size()-1,player.remove(0)); // put self at bottom
		player.add(player.size()-1,player.remove(0)); // put next player below self
	}
	

	
}
