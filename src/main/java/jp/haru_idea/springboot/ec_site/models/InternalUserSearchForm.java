package jp.haru_idea.springboot.ec_site.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class InternalUserSearchForm {
    private int roleId;

    @NotBlank
    @Pattern(regexp = "[^!\"#$%&'()*+,-./:;<=>?@[]^_`{]+$]{0,64}", message = "64文字以内の文字（記号を除く）を入力してください")
    private String lastName;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
