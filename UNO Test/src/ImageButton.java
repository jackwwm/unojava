import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ImageButton implements EventHandler<MouseEvent> {
	DropShadow drop = new DropShadow(30, Color.BLACK);
	DropShadow glow = new DropShadow(80, Color.WHITE);

	@Override
	public void handle(MouseEvent me) {
		// TODO Auto-generated method stub

		ImageView i = (ImageView) me.getSource();
		i.setOnMouseEntered(e -> {
			i.setEffect(glow);
			i.setTranslateY(-5);
		});
		i.setOnMouseExited(e -> {
			i.setEffect(drop);
			i.setTranslateY(0);
			});
		i.setOnMouseClicked(e -> {
			CardTest.currentPlayer.addCard(CardTest.deck.drawCard());
			});
	}

}