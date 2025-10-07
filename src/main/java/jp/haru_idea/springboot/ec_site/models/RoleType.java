package jp.haru_idea.springboot.ec_site.models;

public enum RoleType{
    ROLE_OWNER("ROLE_OWNER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_ENDUSER("ROLE_ENDUSER"),
    ROLE_SYSTEM("ROLE_SYSTEM"),
    ROLE_SUPPORT("ROLE_SUPPORT"),
    ROLE_CONTENT("ROLE_CONTENT");

    private String roleName;

    private RoleType(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName(){
        return roleName;
    }
}
