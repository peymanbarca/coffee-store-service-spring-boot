package com.demo.coffee_store_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by zevik on 6/1/23.
 */
@Entity
@Table(name = "drink")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drink {


    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="drink_id_seq")
    @SequenceGenerator(name="drink_id_seq", sequenceName="drink_id_seq", allocationSize=1)
    @Column(name = "id")
    private Long id;

    @NotNull @Column(name = "name") private String name;

    @NotNull @Column(name = "price") private Double price;

}
