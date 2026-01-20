package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dimensions")
public class Dimension {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dimension_id")
    private UUID dimensionId;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @Column(name = "depth")
    private Double depth;
}
