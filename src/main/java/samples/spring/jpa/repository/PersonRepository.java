package samples.spring.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samples.spring.jpa.domain.Person;

/**
 * Created by izeye on 14. 12. 23..
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
