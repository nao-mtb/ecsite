package jp.haru_idea.springboot.ec_site.models;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class UserCommonForm {
    @NotBlank
    @Pattern(regexp = "[^!\"#$%&'()*+,-./:;<=>?@[]^_`{]+$]{0,64}", message = "64文字以内の文字（記号を除く）を入力してください")
    private String lastName;
    
    @NotBlank
    @Pattern(regexp = "[^!\"#$%&'()*+,-./:;<=>?@[]^_`{]+$]{0,64}", message = "64文字以内の文字（記号を除く）を入力してください")
    private String firstName;
    
    //TODO ユニークキー登録時のエラーメッセージを作成
    //TODO メール正規表現整理
    @NotBlank
    @Size(max=128)
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^\\/_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$", message = "メール形式で入力してください")
    private String mail;

    //TODO バリデーションエラー時のリセット対処
    //"[yyyy-MM-dd][yyyy/MM/dd]"から[yyyy/MM/dd]を削除
    @NotNull
    @DateTimeFormat(pattern = "[yyyy-MM-dd]")
    private LocalDate birthDate;

    private Integer deleteFlag = 0;
        
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
        
}
