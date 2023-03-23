package com.ucan.entity;

import java.io.Serializable;

/**
 * @Description:<用户-职位>实体类
 * @author liming.cen
 * @date 2023年3月3日 下午8:20:14
 */
public class UserPost implements Serializable {
    private static final long serialVersionUID = 8309140568914978557L;
    private String userId;
    private String postId;
    private User user;
    private Post post;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getPostId() {
	return postId;
    }

    public void setPostId(String postId) {
	this.postId = postId;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Post getPost() {
	return post;
    }

    public void setPost(Post post) {
	this.post = post;
    }

}
