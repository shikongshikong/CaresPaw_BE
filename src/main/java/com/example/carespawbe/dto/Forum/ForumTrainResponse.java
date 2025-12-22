package com.example.carespawbe.dto.Forum;

import com.example.carespawbe.dto.History.ForumHistoryTrainDTO;
import com.example.carespawbe.dto.Like.LikeTrainDTO;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import com.example.carespawbe.entity.Forum.ForumPostLikeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumTrainResponse {
    private List<ForumPostTrainingDTO>  postDb;
    private List<ForumHistoryTrainDTO> postHistoryDb;
    private List<LikeTrainDTO>  postLikeDb;
}
