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
package plortz.ui.widget;

import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextArea;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.component.Widget;
import org.liquidengine.legui.event.KeyEvent;
import org.liquidengine.legui.listener.KeyEventListener;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;
import org.lwjgl.glfw.GLFW;
import plortz.ui.UserInterface;
import plortz.ui.command.Command;
import plortz.ui.command.CommandFactory;

/**
 * Widget containing command input and command history.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Console extends Panel {
    
    private final UserInterface  user_interface;
    private final CommandFactory command_factory;
    TextInput                    console_cmd;
    TextArea                     console_messages;

    public Console(Frame frame, UserInterface user_interface) {
        //super("Console", 10, 10, 220, 150);
        super(10, 10, 220, 150);
        this.user_interface  = user_interface;
        this.command_factory = CommandFactory.getInstance();
        this.user_interface.listenOnMessage(() -> this.onMessage());

        frame.getContainer().add(this);

        this.console_messages = new TextArea(10, 10, 200, 90);
        this.console_messages.setEditable(false);
        this.console_messages.setHorizontalScrollBarVisible(false);
        this.console_messages.setVerticalScrollBarVisible(true);
        this.add(this.console_messages);
        
        this.console_cmd = new TextInput(10, 100, 200, 20);
        this.console_cmd.getListenerMap().addListener(KeyEvent.class, (event) -> this.onCmdKey(event));
        this.add(this.console_cmd);
    }

    private void onCmdKey(KeyEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            switch (event.getKey()) {
                case GLFW.GLFW_KEY_UP:
                    this.goHistoryUp();
                    break;
                case GLFW.GLFW_KEY_DOWN:
                    this.goHistoryDown();
                    break;
            }
        } else if (event.getAction() == GLFW.GLFW_RELEASE) {
            switch (event.getKey()) {
                case GLFW.GLFW_KEY_ENTER:
                    this.processInput();
                    break;
            }
        }
    }

    private void processInput() {
        String input = this.console_cmd.getTextState().getText();
        this.user_interface.showMessage("> " + input);
        Command cmd = this.command_factory.create(input);
        if (cmd != null) {
            cmd.execute(this.user_interface);
        }
        this.user_interface.getCommandHistory().add(input);
        this.console_cmd.getTextState().setText("");
    }

    private void goHistoryUp() {
        String previous = this.user_interface.getCommandHistory().previous();
        if (previous != null) {
            this.console_cmd.getTextState().setText(previous);
            this.console_cmd.getTextState().setCaretPosition(previous.length());
        }
    }
    
    private void goHistoryDown() {
        String next = this.user_interface.getCommandHistory().next();
        if (next != null) {
            this.console_cmd.getTextState().setText(next);
            this.console_cmd.getTextState().setCaretPosition(next.length());
        } else {
            this.console_cmd.getTextState().setText("");
        }
    }

    private void onMessage() {
        var ts = this.console_messages.getTextAreaField().getTextState();
        StringBuilder text = new StringBuilder(ts.getText());
        text.append(this.user_interface.getMessage()).append("\n");
        ts.setText(text.toString());
        ts.setCaretPosition(text.length() - 1);
    }
}
