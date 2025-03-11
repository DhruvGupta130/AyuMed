package com.example.system.entity;

import com.example.system.dto.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "First name cannot be null")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Pattern(regexp = "^[+]?[0-9\\- ]{7,20}$", message = "Invalid phone number")
    @NotNull(message = "Mobile number cannot be null")
    private String mobile;

    @NotNull
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LoginUser loginUser = new LoginUser();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
