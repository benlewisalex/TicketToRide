package model;

import java.util.Arrays;
import java.util.List;

public class GameList {
    private List<Game> games;

    public GameList(List<Game> games) {
//        this.games = (Game[]) games.toArray();
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

//    public void setGames(List<Game> games) {
////        this.games = (Game[]) games.toArray();
//        this.games = new Game[games.size()];
//        for(int i = 0; i < games.size(); i++) {
//            this.games.set(i, games.get(i));
//        }
//    }
}
