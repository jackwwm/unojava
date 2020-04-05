import java.util.ArrayList;
import java.util.Collections;

public class Deck{

	private ArrayList<Card> cards;
	private int cardsInDeck;

	public Deck() {
		cards = new ArrayList<Card>();
		cardsInDeck = cards.size();
	}
	
	public void resetDeck() {
		//Create number cards (76 cards)
		for(int col = 0; col < 4 ;col++) {
			//color values - 1 because the last color is "any" reserved for wildcards

			//1 number zero card for each color
			cards.add(new Card(Card.Color.getColor(col),Card.Symbol.getSymbol(0)));

			for (int sym = 1; sym <= 9; sym++) {
				//2 number 1-9 cards for each color
				cards.add(new Card(Card.Color.getColor(col),Card.Symbol.getSymbol(sym))); //zero card
				cards.add(new Card(Card.Color.getColor(col),Card.Symbol.getSymbol(sym))); //zero card
			}
			//action cards (24 cards)
			for(int act=10; act<=12; act++) { //10, 11, 12 are enum symbols for skip, draw2, reverse
				cards.add(new Card(Card.Color.getColor(col),Card.Symbol.getSymbol(act)));
				cards.add(new Card(Card.Color.getColor(col),Card.Symbol.getSymbol(act)));
			}
		}
		//create wild cards (8 cards, 4 of each)
		for(int i = 13; i<=14; i++) {
			for(int j = 0; j<=3; j++)
				cards.add(new Card(Card.Color.getColor(4),Card.Symbol.getSymbol(i)));
		}

		this.cardsInDeck = cards.size();
		//Collections.shuffle(cards);
	}
	
	
	public void shuffleCards() {
        Collections.shuffle(cards); //shuffle cards
	}
	public Card drawCard() {
		if(cardsInDeck == 0) {
			System.out.println("Deck is empty!");
			return null;
		}
		cardsInDeck--;
		return cards.remove(0);
	}

	public int getCardsInDeck() {
		return cardsInDeck;
	}

	public void addCard(Card c) {
		cards.add(c);
		cardsInDeck++;
	}
	
	public Card getCard(int i) {
		return cards.get(i);
	}
	
	public Card getTopCard() {
		return cards.get(cardsInDeck-1);
	}
}

	
/* experimental, using string arrays

public void reset() {
	Card.Color[] colors = Card.Color.values();
	cardsInDeck = 0;

	for(int i = 0; i<cards.length-1;i++) {
		Card.Color color = colors[i];

		cards[cardsInDeck++] = new Card(color,Card.Symbol.getSymbol(i)); //zero card

		for(int j = 0; j<10; j++) {
			cards[cardsInDeck++] = new Card(color,Card.Symbol.getSymbol(i)); //2 cards for 1 to 9cards
			cards[cardsInDeck++] = new Card(color,Card.Symbol.getSymbol(i)); 
		}


		Card.Symbol[] symbols = new Card.Symbol[] {Card.Symbol.Draw2,Card.Symbol.Reverse,Card.Symbol.Skip};

		for(Card.Symbol symbol : symbols)
		{
			cards[cardsInDeck++] = new Card(color,Card.Symbol.getSymbol(i)); //2 cards each power
			cards[cardsInDeck++] = new Card(color,Card.Symbol.getSymbol(i));
		}
		
		Card.Symbol[] symbols = new Card.Symbol[] {Card.Symbol.Wild,Card.Symbol.Wild4};

		for(Card.Symbol symbol : symbols)
		{
			cards[cardsInDeck++] = new Card(color,Card.Symbol.getSymbol(i)); //2 cards for 
		}
	}
}

*/