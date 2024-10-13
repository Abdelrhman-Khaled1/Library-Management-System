package com.maids.cc.Library.Management.System.Patrons;

import com.maids.cc.Library.Management.System.Patrons.model.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron,Long> {
}
