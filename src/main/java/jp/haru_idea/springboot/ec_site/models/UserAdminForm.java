package jp.haru_idea.springboot.ec_site.models;

import java.util.Collection;

import javax.persistence.OneToMany;

public class UserAdminForm extends UserCommonForm{
    private int id;

    private Collection<RoleUser> roleUsers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<RoleUser> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(Collection<RoleUser> roleUsers) {
        this.roleUsers = roleUsers;
    }

}
