package md5.end.service;


import md5.end.model.entity.Role;
import md5.end.model.entity.RoleName;

import javax.security.auth.login.LoginException;

public interface IRoleService {
    Role findByRoleName(RoleName roleName) throws LoginException;
}
