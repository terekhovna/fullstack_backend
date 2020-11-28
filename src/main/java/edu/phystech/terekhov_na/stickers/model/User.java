package edu.phystech.terekhov_na.stickers.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String login;
    @NotNull
    private String email;
    @NotNull
    private String password;

    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private UserData userData;

//    public void setUserData(UserData userData) {
//        if (userData == null) {
//            if (this.userData != null) {
//                this.userData.setUser(null);
//            }
//        }
//        else {
//            userData.setUser(this);
//        }
//        this.userData = userData;
//    }
}
