package jp.haru_idea.springboot.ec_site.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserCreateForm extends UserCommonForm{    
    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?!.*[^0-9a-zA-Z]).{6,30}", message="数字1文字以上、英字1文字以上で6文字以上20文字以下になるように入力してください")
    // @Pattern(regexp = "^[0-9a-zA-Z]{6,20}$", message="6文字以上20文字以下の英数字を入力してください")
    private String password;
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
