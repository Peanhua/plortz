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
package plortz.observer;

import java.util.List;
import plortz.util.MyArrayList;

/**
 * Observer pattern, the subject object.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Subject {
    private final List<Observer> observers;
    
    public Subject() {
        this.observers = new MyArrayList<>(Observer.class);
    }
    
    /**
     * Add a new observer to listen for state changes.
     * 
     * @param observer The observer to receive the notifications about state changes
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }
    
    /**
     * Stop observer from receiving notifications about state changes.
     * 
     * @param observer The observer to stop receiving notifications
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }
    
    /**
     * Notify all observers about a state change.
     */
    public void notifyObservers() {
        this.observers.forEach(observer -> observer.update());
    }
}
