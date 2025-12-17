package org.example.storymasters.dto;

import org.example.storymasters.model.UserStory;

public class UserStoryPayload {
    private String story;
    private PlayerPayload owner;
    private Integer votes;

    public UserStoryPayload() {}

    public UserStoryPayload(String story, PlayerPayload owner, Integer votes) {
        this.story = story;
        this.owner = owner;
        this.votes = votes;
    }

    public UserStoryPayload(UserStory userStory) {
        this.story = userStory.getStory();
        this.owner = new PlayerPayload(userStory.getOwner());
        this.votes = userStory.getVotes();
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public PlayerPayload getOwner() {
        return owner;
    }

    public void setOwner(PlayerPayload owner) {
        this.owner = owner;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
