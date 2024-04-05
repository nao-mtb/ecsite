package jp.haru_idea.springboot.ec_site.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserMailForm{
    @NotBlank
    @Size(max=128)
    // @Email
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^\\/_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$", message = "メール形式で入力してください")
    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }    
}
