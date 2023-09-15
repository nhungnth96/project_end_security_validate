package md5.end.repository;

import md5.end.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTel(String tel);
    Optional<User> findByUsernameContainingIgnoreCase (String username);
    Optional<User> findByEmail (String email);
    Optional<User> findByTel (String tel);

    @Query("select u from User as u where u.name like concat('%',(?1) ,'%')")
    List<User> searchByName(String name);
}
