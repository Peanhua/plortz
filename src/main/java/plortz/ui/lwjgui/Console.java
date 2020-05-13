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
package plortz.ui.lwjgui;

import lwjgui.LWJGUI;
import lwjgui.event.KeyEvent;
import lwjgui.scene.Node;
import lwjgui.scene.control.Label;
import lwjgui.scene.control.ScrollPane;
import lwjgui.scene.control.TextField;
import lwjgui.scene.layout.Pane;
import lwjgui.scene.layout.VBox;
import org.lwjgl.glfw.GLFW;
import plortz.ui.UserInterface;
import plortz.ui.command.Command;
import plortz.ui.command.CommandFactory;

/**
 * Widget containing command input and command history.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Console extends Widget {
    
    private final UserInterface  user_interface;
    private final CommandFactory command_factory;
    TextField                    console_cmd;
    ScrollPane                   console_messages_pane;
    VBox                         console_messages;

    public Console(UserInterface user_interface) {
        super();
        this.user_interface  = user_interface;
        this.command_factory = CommandFactory.getInstance();
        this.user_interface.listenOnMessage(() -> this.onMessage());
    }
    
    @Override
    protected Node createUserInterface() {
        Pane pane = new VBox();
        
        this.console_messages_pane = new ScrollPane();
        pane.getChildren().add(this.console_messages_pane);
        this.console_messages_pane.setPrefHeight(200);
        this.console_messages = new VBox();
        this.console_messages_pane.setContent(this.console_messages);
        //console_message_pane.vvalueProperty().bind(this.console_messages.heightProperty());
        
        this.console_cmd = new TextField();
        pane.getChildren().add(this.console_cmd);
        /*
        var f = new TextField();
        f.setOnKeyPressed((event) -> 
                        System.out.println("f.onKeyPressed(" + event.getKeyName() + "): consumed=" 
                                + event.isConsumed() + ", event=" + event)
        );

        pane.getChildren().add(f);
        */
        this.console_cmd.setOnKeyPressed((event) -> this.onCmdKeyPressed(event));
        this.console_cmd.setOnKeyReleased((event) -> this.onCmdKeyReleased(event));
        
        return pane;
    }
    
    private void onCmdKeyPressed(KeyEvent event) {
        /*
        if (!this.console_cmd.isEditing()) {
            return;
        }
*/
        System.out.println("Console.onKeyPressed(" + event.getKeyName() + "): consumed=" + event.isConsumed() + ", event=" + event);
        if (event.isConsumed()) {
            return;
        }
        switch (event.key) {
            case GLFW.GLFW_KEY_UP:
                this.goHistoryUp();
                break;
            case GLFW.GLFW_KEY_DOWN:
                this.goHistoryDown();
                break;
        }
        event.consume();
    }

    private void onCmdKeyReleased(KeyEvent event) {
        if (event.isConsumed()) {
            return;
        }
        switch (event.key) {
            case GLFW.GLFW_KEY_ENTER:
                this.processInput();
                break;
        }
        event.consume();
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
            console_cmd.setCaretPosition(previous.length());
        }
    }
    
    private void goHistoryDown() {
        String next = this.user_interface.getCommandHistory().next();
        if (next != null) {
            this.console_cmd.setText(next);
            console_cmd.setCaretPosition(next.length());
        } else {
            this.console_cmd.clear();
        }
    }

    @Override
    public void refresh() {
    }
    
    private void onMessage() {
        this.console_messages.getChildren().add(new Label(this.user_interface.getMessage()));
        LWJGUI.runLater(() -> {
            this.console_messages_pane.setVvalue(1);
        });
    }
}
