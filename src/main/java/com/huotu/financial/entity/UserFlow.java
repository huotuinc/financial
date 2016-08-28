package com.huotu.financial.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/8/28.
 */
@Getter
@Setter
@Entity
@Table()
public class UserFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
