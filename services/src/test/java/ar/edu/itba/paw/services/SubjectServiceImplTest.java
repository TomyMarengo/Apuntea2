package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.SubjectDao;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceImplTest {
    @Mock
    private SubjectDao subjectDao;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    public void testGetSubjectsGroupByYear() {
        Mockito.when(subjectDao.getSubjectsByCareerId(Mockito.any())).thenReturn(Stream.of(
                new Subject(UUID.randomUUID(), "Subject 1a", UUID.randomUUID(), 1),
                new Subject(UUID.randomUUID(), "Subject 1b", UUID.randomUUID(), 1),
                new Subject(UUID.randomUUID(), "Subject 1c", UUID.randomUUID(), 1),
                new Subject(UUID.randomUUID(), "Subject 2", UUID.randomUUID(), 2),
                new Subject(UUID.randomUUID(), "Subject 3a", UUID.randomUUID(), 3),
                new Subject(UUID.randomUUID(), "Subject 3b", UUID.randomUUID(), 3)
        ).collect(Collectors.toList()));

        Map<Integer, List<Subject>> subjectsMap =  subjectService.getSubjectsByCareerGroupByYear(UUID.randomUUID());

        assertEquals(3, subjectsMap.size());
        assertEquals(3, subjectsMap.get(1).size());
        assertEquals(1, subjectsMap.get(2).size());
        assertEquals(2, subjectsMap.get(3).size());
        assertTrue(subjectsMap.keySet()
                              .stream()
                              .allMatch(year -> subjectsMap.get(year)
                                                           .stream()
                                                           .allMatch(subject -> subject.getYear()
                                                                                       .equals(year))));
    }

}
