package com.ashu.banking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ashu.banking.domain.Appointment;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {

    List<Appointment> findAll();
}
