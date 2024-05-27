package Expense.Tracker.repository;

import Expense.Tracker.entity.Car;
import Expense.Tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findCarsByUser_ChatId(Long chatId);

    List<Car> findByUser(User user);
    @Query("SELECT c FROM Car c WHERE c.user = :user AND c.active = true")
    List<Car> findActiveCarsByUser(@Param("user") User user);

}
