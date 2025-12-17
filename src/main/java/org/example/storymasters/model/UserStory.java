package org.example.storymasters.model;

public class UserStory {
    private String story;
    private Player owner;
    private Integer votes;

    public UserStory(String story, Player owner) {
        this.story = story;
        this.owner = owner;
        this.votes = 0;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
