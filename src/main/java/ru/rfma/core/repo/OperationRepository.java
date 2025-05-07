package ru.rfma.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.rfma.core.entities.Operation;
import ru.rfma.core.enums.OperationType;

import java.util.List;
@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {

    List<Operation> getOperationsByCategoryIdAndOperationTypeAndUserId(int id, OperationType operationType, Integer userId);

    List<Operation> getOperationsByOperationTypeAndUserId(OperationType operationType, Integer userId);

    List<Operation> findOperationsByUserId(final Integer userId);

    Operation getOperationByUserIdAndId(Integer userId, int id);
}
