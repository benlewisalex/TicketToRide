package model;

import java.util.List;

public class ActiveGameList {
    private List<ActiveGame> activeGames;

    public ActiveGameList(List<ActiveGame> activeGames) {
        this.activeGames = activeGames;
    }

    public List<ActiveGame> getActiveGames() {
        return activeGames;
    }
}
