package ru.rfma.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.rfma.core.entities.Operation;
import ru.rfma.core.enums.OperationType;

import java.util.List;
@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {

    List<Operation> getOperationsByCategoryIdAndOperationType(int id, OperationType operationType);

    List<Operation> getOperationsByOperationType(OperationType operationType);
}
