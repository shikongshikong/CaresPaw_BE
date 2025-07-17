package com.example.carespawbe.dto.Forum;

import com.example.carespawbe.dto.History.PostSideBarResponse;
import lombok.Data;

import java.util.List;

@Data
public class ForumPageResponse {
    private List<ShortForumPost> popularPosts;
    private List<ShortForumPost> postList;
    private List<PostSideBarResponse> historyPosts;
//     + following
    private List<PostSideBarResponse> savePosts;
}
