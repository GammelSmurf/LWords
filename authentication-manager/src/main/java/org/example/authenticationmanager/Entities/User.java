package org.example.authenticationmanager.Entities;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "am_users")
public class User{
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private boolean enabled;
    private int progressLength;
    private int translationCount;
    private LocalDateTime date;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "am_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(){ }

    public User(String username, String password, LocalDateTime date){
        this.username = username;
        this.password = password;
        enabled = true;
        progressLength = 10;
        translationCount = 12;
        this.date = date;
    }

    public void deleteRole(Role role){
        this.roles.remove(role);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public int getProgressLength() {
        return progressLength;
    }

    public void setProgressLength(int progressLength) {
        this.progressLength = progressLength;
    }

    public int getTranslationCount() {
        return translationCount;
    }

    public void setTranslationCount(int translationCount) {
        this.translationCount = translationCount;
    }
}
