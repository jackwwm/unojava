import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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
	//vgap and hgap
	static double vgap = -50;
	static double hgap = -30; 
	
	static DropShadow drop = new DropShadow(30, Color.BLACK);
	static DropShadow glow = new DropShadow(80, Color.YELLOW);
	static DropShadow glow2 = new DropShadow(10, Color.WHITE);

	static FlowPane cardDisplayT = new FlowPane(hgap, vgap);
	static FlowPane cardDisplayR = new FlowPane(hgap, vgap);
	static FlowPane cardDisplayB = new FlowPane(hgap, vgap);
	static FlowPane cardDisplayL = new FlowPane(hgap, vgap);


	//GameController variables (to be moved)
	static Player currentPlayer;
	static Deck deck = new Deck(); //Card Deck
	static Deck dPile = new Deck(); //Discard or Play pool
	static Card dTopCard;
	static int order = 1; //clockwise order = 1, anti-clockwise order = -1
	static Card.Color wildColor;
	static Group arcs;

	//DrawCardDeckIcon and OrderArrows
	static ImageView drawingDeck = new ImageView(new Image("file:images/unoDeck.png",150,150,true,true));
	static ImageView arrows = new ImageView(new Image("file:images/arrows.png",300,300,true,true));



	public static ArrayList<Player> player = new ArrayList<Player>();
	public static ArrayList<Player> playerQueue = new ArrayList<Player>();

	public static void Refresh() {
		//reset pane
		pane.getChildren().remove(bp);

		//draw card image	   
		displayCards();
		drawDrawingDeck();

		//set dTopCard
		dTopCard = dPile.getTopCard();

		StackPane sp = new StackPane();
		sp.getChildren().addAll(cardDisplayT,drawingDeck);
		StackPane.setAlignment(drawingDeck, Pos.TOP_LEFT);
		sp.getChildren().get(1).setTranslateX(20); //move deckIcon to the right a bit

		//StackPane.setAlignment(cardDisplayT, Pos.CENTER);

		bp = new BorderPane();
		bp.setTop(sp);
		bp.setRight(cardDisplayR);//P1 cards
		bp.setLeft(cardDisplayL);
		bp.setBottom(cardDisplayB); 

		//align it to middle by changing preferred size
		bp.setPrefSize(1280, 700); //using 700 cuz 720 cause right side deck has imageview going out of bound
		bp.setTranslateX(10);

		//Center draw card and discarded pile
		//discardPane.getChildren().add(arrows);
		//animate arrows		
		animateArrows();
		bp.setCenter(discardPane);

		pane.getChildren().add(bp);
		//		System.out.println("Current player: "+currentPlayer.getPlayerName());
		//		System.out.println("Handcards: "+currentPlayer.getCards());
	}

	public static void drawDrawingDeck() {
		//draw drawingDeck

		drawingDeck.setOnMouseEntered(e -> {
			drawingDeck.setEffect(glow);
			drawingDeck.setTranslateY(-5);
		});
		drawingDeck.setOnMouseExited(e -> {
			drawingDeck.setEffect(drop);
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

	public void start(Stage primaryStage) {
		myStage = primaryStage;

		currentScene = getMenuScreen();

		myStage.sizeToScene();
		myStage.setResizable(false);
		myStage.setTitle("UNO Game");
		myStage.setScene(currentScene);
		// primaryStage.show();
		myStage.show();
	}

	public Scene getMenuScreen() {
		pane = new Pane();
		Background BG = new Background(
				new BackgroundImage(new Image("file:images/redgradient.png", 1280, 720, true, true), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)

				);
		pane.setBackground(BG);
		arrows.setOpacity(.6);

		Refresh();
		return new Scene(pane, 1280, 720);
	}

	public Scene getGameScreen() {

		return new Scene(pane, 700, 700);
	}


	//nextplayer
	public static void changePlayer() {
		playerQueue.add(playerQueue.size()-1,playerQueue.remove(0)); //next player
		currentPlayer = playerQueue.get(0);
		//if(currentPlayer.checkCardNumber()==0)
		//	changePlayer();
		//else
			Refresh();
	}

	public static void skipPlayerTurn() {

		for (Player x : playerQueue)
			System.out.println(x.getPlayerName());

		//have to put self at the bottom first, then put
		playerQueue.add(playerQueue.size()-1,playerQueue.remove(0)); // put self at bottom
		playerQueue.add(playerQueue.size()-1,playerQueue.remove(0)); // put next player below self
		currentPlayer = playerQueue.get(0);
		Refresh();
	}


	public static void reverseOrder() {
		Collections.reverse(playerQueue);
		order *= -1;
		currentPlayer = playerQueue.get(0);
		Refresh();
	}



	public static void addPlayer(Player p) {
		player.add(p);
	}

	public static void playCard(Card card) {
		card.revokeInteractable(card.cardFace);
		card.setCardFaceTilt();
		currentPlayer.removeCard(card);
		dPile.addCard(card);
		//changePlayer();
	}

	public static void main(String[] args){


		deck.resetDeck(); //Spawn cards
		deck.shuffleCards();



		//Add players
		Player p1 = new Player("P1");
		Player p2 = new Player("P2");
		Player p3 = new Player("P3");
		Player p4 = new Player("P4");

		addPlayer(p1);
		addPlayer(p2);
		addPlayer(p3);
		addPlayer(p4);
		playerQueue.add(p1);
		playerQueue.add(p2);
		playerQueue.add(p3);
		playerQueue.add(p4);

		currentPlayer = p1;

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

		dPile.addCard(deck.drawCard()); //First card to be taken from deck

		launch(args);
	}

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


	public static void displayCards() {
		//clean up first
		//discardPane.getChildren().clear();
		if(!discardPane.getChildren().contains(arrows))
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
		//		Randomly position them
		//		discardPane.getChildren().get(0).setTranslateX(40);
		//		discardPane.getChildren().get(1).setRotate(-30);
		//		discardPane.getChildren().get(2).setRotate(10);
		//		discardPane.getChildren().get(2).setTranslateX(10);


		//		Button btnDraw = new Button();
		//		btnDraw.setPrefSize(80, 120);

		//Clockwise from bottom 0 -> left 1 -> top 2 -> right 3
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