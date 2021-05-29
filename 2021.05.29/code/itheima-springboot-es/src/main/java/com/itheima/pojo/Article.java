package com.itheima.pojo;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/17 11:38
 * @description 标题
 * @package com.itheima.pojo
 */
public class Article {
    private Long id;
    private String title;
    private String content;

    public Article() {
    }

    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
