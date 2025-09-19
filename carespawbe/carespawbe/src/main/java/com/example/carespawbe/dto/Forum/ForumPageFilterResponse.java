package com.example.carespawbe.dto.Forum;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ForumPageFilterResponse {
    Page<ShortForumPostResponse> postList;
}
