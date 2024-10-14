package com.maids.cc.Library.Management.System.Patrons.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.entity.BorrowingRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "patrons")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "contact_info")
    private String contactInfo;
    private String notes;

    @JsonIgnore
    @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL)
    private List<BorrowingRecord> borrowingRecords;
}
