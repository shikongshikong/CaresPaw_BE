package com.example.carespawbe.dto.Forum;

import com.example.carespawbe.dto.History.ForumPostHistoryTagResponse;
import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ForumPageResponse {
    private List<ShortForumPostResponse> popularPosts;
//    private List<ShortForumPostResponse> postList;
    Page<ShortForumPostResponse> forumPosts;
    private List<ForumPostHistoryTagResponse> historyPosts;
//     + following
    private List<ForumPostSideBarResponse> savePosts;
}
