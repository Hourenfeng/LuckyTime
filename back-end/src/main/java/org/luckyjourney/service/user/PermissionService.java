package org.luckyjourney.service.user;

import org.luckyjourney.entity.user.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface PermissionService extends IService<Permission> {

    Map<String, Object> initMenu(Long userId);


    List<Permission> treeSelect();


    void removeMenu(Long id);
}
