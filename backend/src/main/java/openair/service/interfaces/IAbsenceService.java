package openair.service.interfaces;

import openair.model.Absence;
import openair.model.enums.Status;

import java.util.List;

public interface IAbsenceService {
    List<Absence> getAllByUserId(Long id);
    Absence add(Absence absence, Long id);
    Absence review(Long id, Status status);
}
