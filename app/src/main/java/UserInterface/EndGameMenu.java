/**
 * MIT License
 *
 * Copyright (c) 2021 Michael Collier, Emily Elzinga, Benjamin Keninger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Gives the options for ending the game
 *
 * @author Michael Collier
 *
 * @since 1.0.0
 */

package UserInterface;

import AcquireProject.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EndGameMenu extends ActionMenu{

    String winnerMessage = "";

    public EndGameMenu(GameUI ui){
        super(ui);
    }

    @Override
    public VBox getMenu() {
        return menu;
    }

    @Override
    public void updateMenu(){
        menu = new VBox();
        menu.getStyleClass().add("actionMenu");

        Text title = new Text("Would you like to end the game?");
        title.getStyleClass().add("mediumText");
        menu.getChildren().add(title);

        Button endGameButton = new Button("End Game");
        Button continueButton = new Button("Continue Playing");

        endGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ui.getGame().endGame();
                winnerMessage = ui.getGame().getWinner();
                ui.updatePlayerInfo();
                ui.changeActionMenu(GameUI.END_GAME);
            }
        });

        continueButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ui.changeActionMenu(GameUI.BUY_STOCK);
            }
        });

        if(winnerMessage.equals("")) {
            menu.getChildren().add(endGameButton);
            menu.getChildren().add(continueButton);
        }else{
            Text message = new Text(winnerMessage);
            menu.getChildren().add(message);
        }

    }

}
