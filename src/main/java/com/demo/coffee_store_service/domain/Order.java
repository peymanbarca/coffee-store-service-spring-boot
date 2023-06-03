package com.demo.coffee_store_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by zevik on 6/2/23.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="orders_id_seq")
    @SequenceGenerator(name="orders_id_seq", sequenceName="orders_id_seq", allocationSize=1)
    @Column(name = "id")
    private Long id;

    @NotNull @Column(name = "create_date") private Date createDate;

    @NotNull @Column(name = "original_amount") private Double originalAmount;

    @NotNull @Column(name = "discount_amount") private Double discountAmount;

}
