import java.util.Random;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card implements Comparable<Card> {
	private Color color;
	private Symbol symbol;
	private String symbolCode; //Abbreviation of symbol (e.g. Zero = 0, Skip = s)
	private char colorCode; //Abbreviation of color (e.g. Red = r)
	
	//cardface image
	ImageView cardFace;
	ImageView cardBack = new ImageView(new Image("file:images/cardBack.png",80,120,true,true));
	DropShadow drop = new DropShadow(40, javafx.scene.paint.Color.YELLOW);

	//For aesthetic discard pile cards purposes (rotate angle and x y translate)
	Random random = new Random();
	private double upper = 5;
	private double lower = -5;
	private double upperAngle = 45;
	private double lowerAngle = -45;
	
	public enum Color
	{
		Red, Blue, Green, Yellow, Any;
		private static final Color[] colors = Color.values(); //set up an array


		public static Color getColor(int i) {
			return Color.colors[i];
		}

	}

	public enum Symbol
	{
		Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Skip, Draw2, Reverse, Wild, Wild4;
		private static final Symbol[] symbols = Symbol.values();

		public static Symbol getSymbol(int i) {
			return Symbol.symbols[i];
		}
	}



	public Card(final Color c, final Symbol s){
		color = c;
		symbol = s;

		//Generate symbolCode for file searching
		switch(symbol.ordinal()) {
		case 10:
			symbolCode = "s";
			break;
		case 11:
			symbolCode = "d";
			break;
		case 12:
			symbolCode = "r";
			break;
		case 13:
			symbolCode = "w";
			break;
		case 14:
			symbolCode = "4";
			break;
		default:
			symbolCode = Integer.toString(symbol.ordinal());
		}
		//Generate colorCode for file searching
		colorCode = color.toString().toLowerCase().charAt(0);

		setCardFace(symbolCode,colorCode);


	}

	//getter
	public Color getColor() {
		return color;
	}
	public Symbol getSymbol() {
		return symbol;
	}
	public ImageView getCardFace() {
		return cardFace;
	}
	public ImageView getCardBack() {
		return cardBack;
	}

	//setter
	public void applyInteractable(ImageView cardFace) {
		//Card Mouseover effects
		drop.setInput(new Glow(0.2)); //lights up card

		cardFace.setOnMouseEntered(e -> {
			cardFace.setEffect(drop);
			cardFace.setTranslateY(-10);
			cardFace.setTranslateX(-10);
		});
		cardFace.setOnMouseExited(e -> {
			cardFace.setEffect(null);
			cardFace.setTranslateY(0);
			cardFace.setTranslateX(0);
		});

		cardFace.setOnMouseClicked(e -> {
			if (CardTest.currentPlayer.checkPlayable2(this,CardTest.dTopCard)) {
			CardTest.playCard(this);
			action();
			//CardTest.Refresh(); //moved to action() cases
			}
		});
	}

	public void action() {
		if(this.colorCode == 'a') { //Color ANY
			CardTest.displayWildColors();
			if(this.symbolCode == "4") {
				for(int i = 0; i<4;i++) { //add 2 cards
					Card temp = CardTest.deck.drawCard();
					temp.applyInteractable(temp.getCardFace());
					CardTest.playerQueue.get(1).addCard(temp);
					
					}
			}
		}else if(this.symbolCode == "s") { //Skip
			CardTest.skipPlayerTurn();
		}else if(this.symbolCode == "d") { //Draw
			CardTest.changePlayer(); //change player
			
			for(int i = 0; i<2;i++) { //add 2 cards
			Card temp = CardTest.deck.drawCard();
			temp.applyInteractable(temp.getCardFace());
			CardTest.currentPlayer.addCard(temp);
			}
			
			CardTest.changePlayer(); //change player
			
		}else if(this.symbolCode =="r") {
			//TODO
			CardTest.reverseOrder();
		}
		else CardTest.changePlayer();
	}

	public void revokeInteractable(ImageView cardFace) {
		//Card Mouseover effects
		cardFace.setEffect(null);
		cardFace.setTranslateY(0);
		cardFace.setOnMouseEntered(null);		
		cardFace.setOnMouseExited(null);
		cardFace.setOnMouseClicked(null);
	}


	public void setCardFace(String sc, char cc) {
		cardFace = new ImageView(new Image("file:images/"+sc+cc+".png",80,120,true,true));
	}

	//tostring
	public String toString() {
		return color + "" + symbol;
	}

	public boolean compare(Card c) {
		if((c.color==this.color)|| c.symbol==this.symbol) {
			return true;
		}else return false;
	}

	
	@Override
	public int compareTo(Card c) {
		// TODO Auto-generated method stub
		if(this.color==c.getColor()) {
			//check symbol
			if(this.symbol==c.getSymbol())
				return 0;
			else if(this.symbol.ordinal() > c.getSymbol().ordinal())
				return 1;
			else return -1;
		}
		else if(this.color.ordinal()>c.getColor().ordinal())  
			return 1;  
		else  
			return -1;  
	} 
	
	
	
	public void setCardFaceTilt() {
		double randomX = Math.random() * (upper-lower)+lower;
		double randomY = Math.random() * (upper-lower)+lower;
		double randomAngle = Math.random() * (upperAngle-lowerAngle)+lowerAngle;

		cardFace.setTranslateX(randomX);
		cardFace.setTranslateY(randomY);
		cardFace.setRotate(randomAngle);
	}
}



