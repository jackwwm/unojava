
public class ActionCard extends Card {
	public ActionCard(Color c, Symbol s) {
		super(c, s);
		// TODO Auto-generated constructor stub
	}

	public void setColor(Color c) {
		//color = c;
	}
	
	public void drawTwo(Player p,Deck d) {
		
		p.addCard(d.drawCard());
		p.addCard(d.drawCard());
	}
	
	public void wildDrawFour(Player p,Deck d) {
		p.addCard(d.drawCard());
		p.addCard(d.drawCard());
		p.addCard(d.drawCard());
		p.addCard(d.drawCard());
	}
	
	public void actionSkip() {
		//current player +1
	}
	
	public void wild(Card card,Color c) {
		//current color change
		card.getColor();
	}
	
	public void Reverse() {
		//list of players reverse
	}
}
