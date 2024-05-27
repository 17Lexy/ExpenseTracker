package Expense.Tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column(name = "id")
    private Long chatId;

    @Column(name = "name")
    String userName;

    @Enumerated(EnumType.STRING)
    BotState state;

    @OneToMany
    List<Car> car;

    @ManyToOne
    @JoinColumn(name = "selected_car_id")
    Car selectedCar;

}
