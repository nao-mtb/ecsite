package jp.haru_idea.springboot.ec_site.models;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;


public class UserChangePasswordForm{
    @Transient
    @NotBlank
    private String oldPassword;
    
    @Transient
    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?!.*[^0-9a-zA-Z]).{6,30}", message="数字1文字以上、英字1文字以上で6文字以上20文字以下になるように入力してください")

    private String password;

    @Transient
    @NotBlank
    private String confirmPassword;

    //TODO html上でバリデーション機能出力
    // @AssertTrue(message="新しいパスワードと一致しません")
    public boolean isNewPassword(){
        return password.equals(confirmPassword);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
