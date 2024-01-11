package com.project.zipkok.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DetailOption")
@Getter
@NoArgsConstructor
public class DetailOption {

    @Id
    @Column(name ="detail_option_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailOptionId;

    @Column(name ="name", nullable = false)
    private String name;

    @Column(name ="is_visible", nullable = false)
    private boolean isVisible;

    @OneToMany(mappedBy = "DetailOption", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CheckedDetailOption> checkedDetailOptions = new ArrayList<>();

    public DetailOption(String name, boolean isVisible){
        this.name =name;
        this.isVisible =isVisible;
    }
}
