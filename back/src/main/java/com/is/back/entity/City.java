package com.is.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(nullable = false)
    private Integer area;

    @Column(nullable = false)
    private Integer population;

    @Column(name = "establishment_date")
    private java.time.LocalDateTime establishmentDate;

    @Column(nullable = false)
    private boolean capital;

    @Column(name = "meters_above_sea_level")
    private long metersAboveSeaLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Climate climate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Government government;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandardOfLiving standardOfLiving;

    @ManyToOne
    @JoinColumn(name = "governor_id")
    private Human governor;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Getters and Setters
}
