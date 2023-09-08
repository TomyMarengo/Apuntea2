package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Career;

import java.util.List;

public interface CareerDao {
    List<Career> getCareers();
}
