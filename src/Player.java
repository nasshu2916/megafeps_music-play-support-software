

import javafx.scene.layout.VBox;


public class Player extends PlayerUi {
	
	Player(Main main, int playerNumber) {
		this.main = main;
		this.playerNumber = playerNumber;
	}
	
	Player(Main main) {
		this.main = main;
		createPlayerWindow();
	}
	
	public VBox getPanel() {
		return panel;
	}
}
