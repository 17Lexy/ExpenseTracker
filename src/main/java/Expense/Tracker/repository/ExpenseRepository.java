package Expense.Tracker.repository;

import Expense.Tracker.entity.Car;
import Expense.Tracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByCar(Car car);

    void deleteByCar(Car car);

    @Query("SELECT SUM(e.sum) FROM Expense e WHERE e.car.carId = :carId")
    Long getTotalExpenseByCarId(@Param("carId")Long carId);
}
