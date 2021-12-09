package openair.service.interfaces;

import openair.dto.RequestAbsenceDTO;
import openair.model.Absence;
import openair.model.enums.Status;

import java.util.List;

public interface IAbsenceService {
    List<Absence> getAllByUserId(Long id);
    Absence add(RequestAbsenceDTO requestAbsenceDTO, Long id);
    Absence review(Long id, Status status);
}
