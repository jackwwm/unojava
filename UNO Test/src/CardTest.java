import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CardTest extends Application {

	Scene currentScene;
	// Pane currentPane;
	static BorderPane bp;
	static Stage myStage;
	static Pane pane;

	//Displays variables
	static StackPane discardPane = new StackPane();
	//vgap and hgap between cards
	static double vgap = -50;
	static double hgap = -30; 
	
	static DropShadow drop = new DropShadow(30, Color.BLACK);
	static DropShadow glow = new DropShadow(80, Color.YELLOW);
	static DropShadow glow2 = new DropShadow(10, Color.WHITE);

	static FlowPane cardDisplayT = new FlowPane(hgap, vgap);
	static FlowPane cardDisplayR = new FlowPane(hgap, vgap);
	static FlowPane cardDisplayB = new FlowPane(hgap, vgap);
	static FlowPane cardDisplayL = new FlowPane(hgap, vgap);


	//GameController variables
	static int playerAmt; //amount of players
	static Player currentPlayer;
	static Deck deck = new Deck(); //Card Deck
	static Deck dPile = new Deck(); //Discard or Play pool
	static Card dTopCard; //discard pile top card
	static int order = 1; //clockwise order = 1, anti-clockwise order = -1
	static Card.Color wildColor;
	static Group arcs;

	//DrawCardDeckIcon and OrderArrows
	static ImageView drawingDeck = new ImageView(new Image("file:images/unoDeck.png",150,150,true,true));
	static ImageView arrows = new ImageView(new Image("file:images/arrows.png",300,300,true,true));

	public static ArrayList<Player> player = new ArrayList<Player>();
	public static ArrayList<Player> playerQueue = new ArrayList<Player>();
	
//SET UP SCENE
	public void start(Stage primaryStage) {
		myStage = primaryStage;

		currentScene = getMenuScreen();

		myStage.sizeToScene();
		myStage.setResizable(false);
		myStage.setTitle("UNO Game");
		myStage.setScene(currentScene);
		myStage.show();
	}
	
	//Main Menu
	public static Scene getMenuScreen() {
		BorderPane pane = new BorderPane();
		Background BG = new Background(
				new BackgroundImage(new Image("file:images/menuBg.jpg", 1280, 700, false, false), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
		Button btnPlay = new Button("Play");

		pane.setBackground(BG);
		btnPlay.setOnMouseClicked(e -> {
			int promptPlayerNum = Integer.parseInt(JOptionPane.showInputDialog("Please enter number of Players:"));
			if(promptPlayerNum > 1 && promptPlayerNum < 5) {
				myStage.setScene(getGameScreen(promptPlayerNum));
			}
			else {
				JOptionPane.showMessageDialog(null,"Invalid input. Please enter a number between 2 - 4.");
			}
			
			
		});
		// pane.getChildren().add(btnPlay);
		HBox hb = new HBox();
		hb.getChildren().add(btnPlay);
		hb.setAlignment(Pos.CENTER);
		
		pane.setPadding(new Insets(150, 150, 150, 150));
		pane.setBottom(hb);
		

		return new Scene(pane, 1280, 700);
	}

	//Game screen
	public static Scene getGameScreen(int playerAmt) {
		setupGame(playerAmt);
		//set player number
		CardTest.playerAmt= playerAmt; 
		
		pane = new Pane();
		Background BG = new Background(
				new BackgroundImage(new Image("file:images/redgradient.jpg", 1280, 720, true, true), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)

				);
		pane.setBackground(BG);
		arrows.setOpacity(.6);

		Refresh();
		return new Scene(pane, 1280, 720);
	}
	
	
	//Main method
	public static void main(String[] args){
		launch(args);
	}
	
//GAME ENGINE
	//Check winning condition
	public static void checkWin() {
		for(Player p : player) {
			if(p.checkCardNumber()==0) {
				JOptionPane.showMessageDialog(null,p.getPlayerName()+ " won!!!" + 
						"\n\nThanks for playing. Press OK to return to Main Menu.");
				myStage.setScene(getMenuScreen());

			}
		}
	}
	
	//Change to next player
	public static void changePlayer() {
		playerQueue.add(playerQueue.size()-1,playerQueue.remove(0)); //next player
		currentPlayer = playerQueue.get(0);
			Refresh();
	}

	public static void skipPlayerTurn() {

		//have to put self at the bottom first, then only next player (playing with ArrayList functions)
		playerQueue.add(playerQueue.size()-1,playerQueue.remove(0)); // put self at bottom
		playerQueue.add(playerQueue.size()-1,playerQueue.remove(0)); // put next player below self
		currentPlayer = playerQueue.get(0);
		Refresh();
	}


	public static void reverseOrder() {
		Collections.reverse(playerQueue);
		order *= -1;
		//Reverse order, and then change player to the first player of the ArrayList
		currentPlayer = playerQueue.get(0);
		Refresh();
	}

	public static void playCard(Card card) {
		card.revokeInteractable(card.cardFace);
		card.setCardFaceTilt();
		currentPlayer.removeCard(card);
		dPile.addCard(card);	
		
	}
	
	public static void setupGame(int playerAmt) {
		discardPane.getChildren().clear();
		playerQueue.clear();
		player.clear();
		deck.resetDeck(); //Spawn cards
		deck.shuffleCards();

		//Add players
		for(int x = 1; x<= playerAmt; x++) {
			Player p = new Player("P"+x);
			player.add(p);
			playerQueue.add(p);
		}
		
		currentPlayer = playerQueue.get(0);

		//DRAWCARDS
		for(int i = 0; i<7; i++) { //1 card at a time, 7 times
			for(Player p : playerQueue) {
				Card temp = deck.drawCard();
				temp.applyInteractable(temp.getCardFace());
				p.addCard(temp);

				//TESTING POWERCARDS
				//				Card banyakPower = new Card(Card.Color.Blue,Card.Symbol.Skip);
				//				temp.applyInteractable(banyakPower.getCardFace());
				//				p.addCard(banyakPower);
			}
		}

		//First card to be taken from deck
		dPile.addCard(deck.drawCard()); 
		while(dPile.getTopCard().getColor()==Card.Color.Any) {
			dPile.addCard(deck.drawCard());
		} 
	}
		
//GRAPHIC METHODS BELOW
	//Refresh GUI after every action
	public static void Refresh() {
		//checkWin();
		//reset pane
		pane.getChildren().remove(bp);

		//draw card image	   
		displayCards();
		drawDrawingDeck(glow,drop);

		//set dTopCard
		dTopCard = dPile.getTopCard();

		StackPane sp = new StackPane();
		Button btnBack = new Button("Main Menu");
		StackPane.setAlignment(drawingDeck, Pos.TOP_LEFT);
		StackPane.setAlignment(btnBack, Pos.TOP_RIGHT);
		btnBack.setOnMouseClicked(e -> {
		myStage.setScene(getMenuScreen());
		});
				
		sp.getChildren().addAll(cardDisplayT,drawingDeck,btnBack);
		sp.getChildren().get(1).setTranslateX(20); //move deckIcon to the right a bit
		sp.getChildren().get(2).setTranslateX(-20); //move btn to the left a bit
		sp.getChildren().get(2).setTranslateY(20); //move btn lower a bit

		bp = new BorderPane();
		bp.setTop(sp);
		bp.setRight(cardDisplayR);//P1 cards
		bp.setLeft(cardDisplayL);
		bp.setBottom(cardDisplayB); 

		//align it to middle by changing preferred size
		bp.setPrefSize(1280, 700); //using 700 cuz 720 will cause right side deck to go out of bound
		bp.setTranslateX(10);

		//animate arrows		
		animateArrows();
		bp.setCenter(discardPane);

		pane.getChildren().add(bp);		
	}

	
	//Animate turn direction arrows
	public static void animateArrows() {
		//test animation

		//Creating a rotate transition    
		RotateTransition rotateTransition = new RotateTransition(); 

		//Setting the duration for the transition 
		rotateTransition.setDuration(Duration.millis(4000)); 

		//Setting the node for the transition 
		rotateTransition.setNode(arrows);       

		//Setting the angle of the rotation

		rotateTransition.setByAngle(360*order);
		arrows.setScaleX(1*order);

		//Setting the cycle count for the transition 
		rotateTransition.setCycleCount(Animation.INDEFINITE); 

		//Setting auto reverse value to false 
		rotateTransition.setAutoReverse(false); 

		//Playing the animation 
		rotateTransition.play(); 

	}
	//Below are codes used for color choosing arcs
	public static void setArcMouseOver(Arc arc, Card.Color col) {
		glow2.setInput(new Glow()); //lights up card

		arc.setOnMouseEntered(e -> {
			arc.setEffect(glow2);
			arc.setScaleX(1.2);
			arc.setScaleY(1.2);
		});
		arc.setOnMouseExited(e -> {
			arc.setEffect(null);
			arc.setScaleX(1);
			arc.setScaleY(1);
		});
		arc.setOnMouseClicked(e -> {
			wildColor=col;
			System.out.println("Wildcard chosen color is "+wildColor.toString());
			//allow self put card first? or straight away next player?
			//if next player, do next player code here instead of refresh.
			canUseHandcards(true);
			discardPane.getChildren().remove(arcs);
			
			
			if(dPile.getTopCard().getSymbol()==Card.Symbol.Wild) {
			changePlayer();
			}else if(dPile.getTopCard().getSymbol()==Card.Symbol.Wild4) {
				changePlayer();
				changePlayer();
			}
			
			Refresh();
		});
	}

	//Deactivate the hardcards when doing wildcard color selection
	public static void canUseHandcards(boolean x) {
		if(!x)
			for(Card c : currentPlayer.getCards()) {
				c.revokeInteractable(c.cardFace);
			}
		else
			for(Card c : currentPlayer.getCards()) {
				c.applyInteractable(c.cardFace);
			}
		Refresh();

	}

	//WildColor chooser arcs
	public static void displayWildColors() {
		canUseHandcards(false);
		Arc arcB = new Arc();
		arcB.setTranslateX(+8);
		arcB.setTranslateY(-8);
		arcB.setRadiusX(150.0f);
		arcB.setRadiusY(150.0f);
		arcB.setFill(Color.BLUE);

		//setAngle
		arcB.setStartAngle(0);
		arcB.setLength(90);

		arcB.setType(ArcType.ROUND);

		//Red
		Arc arcR = new Arc();
		arcR.setTranslateX(-8);
		arcR.setTranslateY(-8);
		arcR.setRadiusX(150.0f);
		arcR.setRadiusY(150.0f);
		arcR.setFill(Color.RED);

		//setAngle
		arcR.setStartAngle(90);
		arcR.setLength(90);

		arcR.setType(ArcType.ROUND);

		//Yellow
		Arc arcY = new Arc();
		arcY.setTranslateX(-8);
		arcY.setTranslateY(+8);
		arcY.setRadiusX(150.0f);
		arcY.setRadiusY(150.0f);
		arcY.setFill(Color.YELLOW);

		//setAngle
		arcY.setStartAngle(180);
		arcY.setLength(90);

		arcY.setType(ArcType.ROUND);

		Arc arcG = new Arc();
		arcG.setTranslateX(8);
		arcG.setTranslateY(+8);
		arcG.setRadiusX(150.0f);
		arcG.setRadiusY(150.0f);
		arcG.setFill(Color.GREEN);

		//setAngle
		arcG.setStartAngle(270);
		arcG.setLength(90);

		arcG.setType(ArcType.ROUND);

		arcs = new Group(arcB,arcR,arcY,arcG);
		arcs.setScaleX(.8);
		arcs.setScaleY(.8);
		arcs.setEffect(drop);

		//Mouse event
		setArcMouseOver(arcB,Card.Color.Blue);
		setArcMouseOver(arcR,Card.Color.Red);
		setArcMouseOver(arcY,Card.Color.Yellow);
		setArcMouseOver(arcG,Card.Color.Green);
		
		discardPane.getChildren().add(arcs);
	}


	public static void drawDrawingDeck(DropShadow enter, DropShadow exit) {
		//draw drawingDeck
		drawingDeck.setOnMouseEntered(e -> {
			drawingDeck.setEffect(enter);
			drawingDeck.setTranslateY(-5);
		});
		drawingDeck.setOnMouseExited(e -> {
			drawingDeck.setEffect(exit);
			drawingDeck.setTranslateY(0);
		});
		drawingDeck.setOnMouseClicked(e -> {
			Card temp = deck.drawCard();
			if(temp!=null) {
				temp.applyInteractable(temp.cardFace);
				currentPlayer.addCard(temp);
			}
			Refresh();
		});
	}
	
	public static void displayCards() {
		
		//clean up first
		if(!discardPane.getChildren().contains(arrows)) //Reset arrow if it gets removed
		discardPane.getChildren().add(arrows);
		
		cardDisplayT.getChildren().clear();
		cardDisplayR.getChildren().clear();
		cardDisplayB.getChildren().clear();
		cardDisplayL.getChildren().clear();
		//==CARDS==
		//try to display top card (implement random rotate angle and x y position)
		if(!discardPane.getChildren().contains(dPile.getTopCard().getCardFace()))
		discardPane.getChildren().add(dPile.getTopCard().getCardFace());

		StackPane.setAlignment(discardPane, Pos.CENTER);

		if(playerAmt>3) {
			//Right
			cardDisplayR.setAlignment(Pos.BASELINE_LEFT);
			cardDisplayR.setOrientation(Orientation.VERTICAL);
			//loop through player handcards
			for(Card c : player.get(3).getCards()) {
				if(currentPlayer != player.get(3))
					cardDisplayR.getChildren().add(c.getCardBack());
				else
					cardDisplayR.getChildren().add(c.getCardFace());
			}
			cardDisplayR.setTranslateX(-30);
		}
		if(playerAmt>2) {
			//Top
			cardDisplayT.setAlignment(Pos.CENTER);
			cardDisplayT.setOrientation(Orientation.HORIZONTAL);
			//loop through player handcards
			for(Card c : player.get(2).getCards()) {
				if(currentPlayer != player.get(2))
					cardDisplayT.getChildren().add(c.getCardBack());
				else
					cardDisplayT.getChildren().add(c.getCardFace());
			}
			cardDisplayT.setTranslateY(10);
		}
		

		//Bottom
		cardDisplayB.setAlignment(Pos.CENTER);
		cardDisplayB.setOrientation(Orientation.HORIZONTAL);
		//loop through player handcards
		for(Card c : player.get(0).getCards()) {
			if(currentPlayer != player.get(0))
				cardDisplayB.getChildren().add(c.getCardBack());
			else
				cardDisplayB.getChildren().add(c.getCardFace());

		}

		//Left
		cardDisplayL.setAlignment(Pos.CENTER);
		cardDisplayL.setOrientation(Orientation.VERTICAL);
		//loop through player handcards
		for(Card c : player.get(1).getCards()) {
			if(currentPlayer != player.get(1))
				cardDisplayL.getChildren().add(c.getCardBack());
			else
				cardDisplayL.getChildren().add(c.getCardFace());
			cardDisplayL.setTranslateX(30);

		}
	}
}