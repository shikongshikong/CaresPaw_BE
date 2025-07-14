package com.example.carespawbe.dto.Forum;

import lombok.Data;

import java.util.List;

@Data
public class ForumPageResponse {
    private List<ShortForumPost> popularPosts;
    private List<ShortForumPost> postList;
    private List<ShortForumPost> historyPosts;
//     + following
    private List<ShortForumPost> savePosts;
}
