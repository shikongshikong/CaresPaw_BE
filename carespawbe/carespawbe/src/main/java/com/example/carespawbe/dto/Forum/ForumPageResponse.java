package com.example.carespawbe.dto.Forum;

import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import lombok.Data;

import java.util.List;

@Data
public class ForumPageResponse {
    private List<ShortForumPostResponse> popularPosts;
    private List<ShortForumPostResponse> postList;
    private List<ForumPostSideBarResponse> historyPosts;
//     + following
    private List<ForumPostSideBarResponse> savePosts;
}
