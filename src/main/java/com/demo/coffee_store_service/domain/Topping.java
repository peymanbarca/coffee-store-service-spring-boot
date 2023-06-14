package com.demo.coffee_store_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "topping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topping {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="topping_id_seq")
    @SequenceGenerator(name="topping_id_seq", sequenceName="topping_id_seq", allocationSize=1)
    @Column(name = "id")
    private Long id;

    @NotNull @Column(name = "name") private String name;

    @NotNull @Column(name = "price") private Double price;
}
