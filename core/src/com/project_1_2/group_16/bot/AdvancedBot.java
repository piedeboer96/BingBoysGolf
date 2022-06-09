package com.project_1_2.group_16.bot;

import com.project_1_2.group_16.gamelogic.Game;

import java.util.List;

public abstract class AdvancedBot {
    private float startX, startY;
    private Game game;

    public AdvancedBot(float startX, float startY, Game game, boolean useFloodFill){
        this.startX = startX;
        this.startY = startY;
        this.game = game;
        Game.useFloodFill = useFloodFill;
    }
    public abstract List<Float> runBot();
    public float getStartX(){
        return startX;
    }
    public float getStartY(){
        return startY;
    }
    public Game getGame(){
        return game;
    }

}
