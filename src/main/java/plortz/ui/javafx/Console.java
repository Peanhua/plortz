/*
 * Copyright (C) 2020 Joni Yrjana {@literal <joniyrjana@gmail.com>}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package plortz.ui.javafx;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import plortz.terrain.Terrain;
import plortz.ui.UserInterface;
import plortz.ui.command.Command;
import plortz.ui.command.CommandFactory;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Console extends Widget {
    
    private final UserInterface  user_interface;
    private final CommandFactory command_factory;
    TextField                    console_cmd;

    public Console(UserInterface user_interface) {
        this.user_interface  = user_interface;
        this.command_factory = CommandFactory.getInstance();
    }
    
    @Override
    public Node createUserInterface() {
        Pane pane = new VBox();
        
        Text console_history = new Text();
        pane.getChildren().add(console_history);
        
        console_cmd = new TextField();
        pane.getChildren().add(console_cmd);

        console_cmd.addEventHandler(KeyEvent.KEY_RELEASED, event -> { this.onCmdKeyReleased(event); });
        
        return pane;
    }
    
    private void onCmdKeyReleased(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) {
            return;
        }
        String input = this.console_cmd.getText();
        Command cmd = this.command_factory.create(input);
        if (cmd != null) {
            cmd.execute(this.user_interface);
        } else if (input != null && input.length() > 0) {
            this.user_interface.showError("Unknown command: " + input);
        }
        this.console_cmd.clear();
    }

    @Override
    public void refresh() {
        
    }
    
}
