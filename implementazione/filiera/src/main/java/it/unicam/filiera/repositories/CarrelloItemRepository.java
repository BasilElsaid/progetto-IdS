package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.CarrelloItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CarrelloItemRepository extends JpaRepository<CarrelloItem, Long> {
    List<CarrelloItem> findByScadeIlBefore(LocalDateTime time);
}
