package Expense.Tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "expense")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(name = "description")
    private String description;

    @Column(name = "sum")
    private Long sum;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "category")
    private String category;

    @ManyToOne
    @JoinColumn(name = "selected_car_id")
    private Car car;

}
