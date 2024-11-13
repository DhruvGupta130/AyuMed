package com.example.system.entity;

import com.example.system.dto.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Email
    private String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number")
    private String mobile;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Hospital hospital;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LoginUser loginUser = new LoginUser();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
