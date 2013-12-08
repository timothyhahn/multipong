package net.timothyhahn.multipong.screens;

import net.timothyhahn.multipong.MultiPongGame;

public abstract class Screen {
	MultiPongGame game;
    
    public Screen(MultiPongGame game){
            this.game = game;
    }
    
    public abstract void update();
    
    public abstract void present();
    
    public abstract void pause();
    
    public abstract void resume();
    
    public abstract void dispose();
}
