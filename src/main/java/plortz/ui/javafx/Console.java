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

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import plortz.ui.UserInterface;
import plortz.ui.command.Command;
import plortz.ui.command.CommandFactory;

/**
 * A widget containing command input and command history.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Console extends Widget {
    
    private final UserInterface  user_interface;
    private final CommandFactory command_factory;
    TextField                    console_cmd;
    VBox                         console_messages;

    public Console(UserInterface user_interface) {
        this.user_interface  = user_interface;
        this.command_factory = CommandFactory.getInstance();
        this.user_interface.listenOnMessage(() -> this.onMessage());
    }
    
    @Override
    public Node createUserInterface() {
        Pane pane = new VBox();
        
        ScrollPane console_message_pane = new ScrollPane();
        pane.getChildren().add(console_message_pane);
        console_message_pane.setPrefHeight(200);
        this.console_messages = new VBox();
        console_message_pane.setContent(this.console_messages);
        console_message_pane.vvalueProperty().bind(this.console_messages.heightProperty());
        
        console_cmd = new TextField();
        pane.getChildren().add(console_cmd);
        console_cmd.addEventHandler(KeyEvent.KEY_PRESSED,  (event) -> this.onCmdKeyPressed(event));
        console_cmd.addEventHandler(KeyEvent.KEY_RELEASED, (event) -> this.onCmdKeyReleased(event));
        
        return pane;
    }
    
    private void onCmdKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                this.goHistoryUp();
                break;
            case DOWN:
                this.goHistoryDown();
                break;
        }
    }

    private void onCmdKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                this.processInput();
                break;
        }
    }
    
    private void processInput() {
        String input = this.console_cmd.getText();
        this.user_interface.showMessage("> " + input);
        Command cmd = this.command_factory.create(input);
        if (cmd != null) {
            cmd.execute(this.user_interface);
        }
        this.user_interface.getCommandHistory().add(input);
        this.console_cmd.clear();
    }
    
    private void goHistoryUp() {
        String previous = this.user_interface.getCommandHistory().previous();
        if (previous != null) {
            this.console_cmd.setText(previous);
            Platform.runLater(() -> console_cmd.positionCaret(previous.length()));
        }
    }
    
    private void goHistoryDown() {
        String next = this.user_interface.getCommandHistory().next();
        if (next != null) {
            this.console_cmd.setText(next);
            Platform.runLater(() -> console_cmd.positionCaret(next.length()));
        } else {
            this.console_cmd.clear();
        }
    }

    @Override
    public void refresh() {
        
    }
    
    private void onMessage() {
        this.console_messages.getChildren().add(new Text(this.user_interface.getMessage()));
    }
}
