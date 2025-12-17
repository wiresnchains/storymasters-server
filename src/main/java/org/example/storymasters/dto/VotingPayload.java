package org.example.storymasters.dto;

import java.util.List;

public class VotingPayload {
    private List<UserStoryPayload> userStories;
    private boolean showResults;

    public VotingPayload() {}

    public VotingPayload(List<UserStoryPayload> userStories, boolean showResults) {
        this.userStories = userStories;
        this.showResults = showResults;
    }

    public List<UserStoryPayload> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<UserStoryPayload> userStories) {
        this.userStories = userStories;
    }

    public boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }
}
