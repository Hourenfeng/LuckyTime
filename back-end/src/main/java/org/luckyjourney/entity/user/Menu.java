package org.luckyjourney.entity.user;

import lombok.Data;

import java.util.List;

@Data
public class Menu {

    private Long id;

    private Long pId;

    private String path;

    private String href;

    private String icon;

    private String title;

    private String target;

    // -1：目录、0：菜单、1：按钮
    private Integer isMenu;
    
    private List<Menu> child;
}