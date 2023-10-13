package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.SearchDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceImplTest {

    @Mock
    private SearchDao searchDao;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Test
    public void testSearchSuccess() {
        int PAGE_SIZE = 10;
        int PAGE = 2;
        int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(securityService.getCurrentUser()).thenReturn(Optional.of(new User(UUID.randomUUID(), "mail")));
        Mockito.when(searchDao.countSearchResults(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(searchDao.search(Mockito.any())).thenReturn(new ArrayList<>());

        Page<Searchable> results = searchService.search(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "other", null, "date", true, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(PAGE, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testSearchUnderPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = -5;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(securityService.getCurrentUser()).thenReturn(Optional.of(new User(UUID.randomUUID(), "mail")));
        Mockito.when(searchDao.countSearchResults(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(searchDao.search(Mockito.any())).thenReturn(new ArrayList<>());

        Page<Searchable> results = searchService.search(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "other", null, SearchArguments.SortBy.DATE.toString(), true, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testSearchOverPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = 6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(securityService.getCurrentUser()).thenReturn(Optional.of(new User(UUID.randomUUID(), "mail")));
        Mockito.when(searchDao.countSearchResults(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(searchDao.search(Mockito.any())).thenReturn(new ArrayList<>());

        Page<Searchable> results = searchService.search(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "other", null, SearchArguments.SortBy.DATE.toString(), true, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(results.getTotalPages(), results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

}
