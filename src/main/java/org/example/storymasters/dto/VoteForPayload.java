package org.example.storymasters.dto;

public class VoteForPayload {
    private Integer userStoryIndex;

    public VoteForPayload() {}

    public Integer getUserStoryIndex() {
        return userStoryIndex;
    }

    public void setUserStoryIndex(Integer userStoryIndex) {
        this.userStoryIndex = userStoryIndex;
    }
}
