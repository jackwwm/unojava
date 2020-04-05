import java.util.ArrayList;

public class Player {
	private String playerName;
	private ArrayList<Card> handCards;
	private int cardsInDeck;

	public Player(String playerName) {
		setPlayerName(playerName);
		handCards = new ArrayList<Card>();
	}

	public ArrayList<Card> getCards(){
		return handCards;
	}

	public void removeCard(Card c) {
		handCards.remove(c);
		cardsInDeck --;
	}

	public void addCard(Card c) {
		handCards.add(c);
		cardsInDeck++;
		sortHandCards();
	}

	//TESTING METHOD
	public Card chooseCard(int cardNum) {

		return handCards.get(cardNum);
	}

	public boolean checkPlayable(Card discardC) {//discard pile's top card
		for(Card c:handCards) {
			if(c.getSymbol()==Card.Symbol.Wild || c.getSymbol()==Card.Symbol.Wild4){
				return true;
			}
			else if(c.getColor()==discardC.getColor() || c.getSymbol()==discardC.getSymbol() )
				return true;
			else return false;
		}
		return false;
	}

	//DEBUG (Can work)
	public boolean checkPlayable2(Card handsCard, Card dPile) {//discard pile's top card

		if(handsCard.getSymbol()==Card.Symbol.Wild || handsCard.getSymbol()==Card.Symbol.Wild4){
			return true;
		}
		else if(handsCard.getColor()==dPile.getColor() || handsCard.getSymbol()==dPile.getSymbol()||handsCard.getColor()==CardTest.wildColor)
			return true;
		else return false;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int checkCardNumber() {
		return cardsInDeck;
	}

	public void printHandCards() {
		for(Card c:handCards) {
			System.out.println(c.toString());
		}
	}

	//DEBUG (Can work) to let user choose card based on index number [indexnumber]
	public void printHandCards2() {
		for(int i = 0; i < handCards.size(); i++) {
			System.out.println("[" + i + "] " + handCards.get(i).toString());
		}
	}

	public void sortHandCards() {
		for(int i=0;i<handCards.size();i++)
		{
			for(int j=0;j<handCards.size() -1;j++)
			{
				if(handCards.get(j).compareTo(handCards.get(j+1)) > 0)
				{
					Card temp = this.handCards.get(j+1);
					//System.out.println("temp card is now "+temp.toString());

					handCards.set(j+1,this.handCards.get(j));
					//System.out.println("no "+(j+1)+" card is now "+handCards.get(j+1).toString());
					handCards.set(j, temp);
					//System.out.println("no "+j+" card is now "+handCards.get(j).toString());
				}
			}
		}
	}
}
