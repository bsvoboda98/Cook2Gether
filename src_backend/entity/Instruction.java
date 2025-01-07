package com.bee.cookwithfriends.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="instruction")
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, length = 1000)
    private String instruction;

    @Column(nullable = false)
    private int stepNumber;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
