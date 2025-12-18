package org.example.storymasters.dto;

import org.example.storymasters.model.UserStory;

public class UserStoryPayload {
    private Integer index;
    private String story;
    private PlayerPayload owner;
    private Integer votes;

    public UserStoryPayload() {}

    public UserStoryPayload(Integer index, String story, PlayerPayload owner, Integer votes) {
        this.index = index;
        this.story = story;
        this.owner = owner;
        this.votes = votes;
    }

    public UserStoryPayload(UserStory userStory, Integer index) {
        this.index = index;
        this.story = userStory.getStory();
        this.owner = new PlayerPayload(userStory.getOwner());
        this.votes = userStory.getVotes();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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
