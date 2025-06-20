package cn.iocoder.forum.model;

import java.util.Date;

public class ArticleReply {
    // 编号
    private Long id;

    // 帖子Id, 关联Article
    private Long articleId;

    // 回复的用户编号
    private Long postUserId;

    // 忽略，需求中楼中楼功能
    private Long replyId;

    // 忽略，需求中楼中楼功能
    private Long replyUserId;

    // 回复的正文
    private String content;

    // 忽略，需求中点赞功能
    private Integer likeCount;

    // 状态 0正常  1 禁用
    private Byte state;

    // 状态 0正常  1 删除
    private Byte deleteState;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;

    // 关联对象 - 回复的发布者
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(Long postUserId) {
        this.postUserId = postUserId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Byte deleteState) {
        this.deleteState = deleteState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}