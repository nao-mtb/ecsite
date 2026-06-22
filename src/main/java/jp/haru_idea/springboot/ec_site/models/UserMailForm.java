package jp.haru_idea.springboot.ec_site.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserMailForm{
    @NotBlank
    @Size(max=128)
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^\\/_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$", message = "メール形式で入力してください")
    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }    
}
