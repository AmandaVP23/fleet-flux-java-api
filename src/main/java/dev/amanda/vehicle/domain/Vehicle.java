package dev.amanda.vehicle.domain;

import dev.amanda.organization.domain.Organization;
import dev.amanda.infrastructure.shared.domain.BaseEntity;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Vehicle extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "vehicle_brand_id")
    private VehicleBrand brand;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(length = 180)
    private String variant;

    @Column(length = 180)
    private String model;

    @Column(length = 20)
    private String plateNumber;

    @Column(length = 30)
    private String vin;

    @Column(length = 4)
    private int yearOfManufacture;

    @Column()
    @Enumerated(EnumType.STRING)
    private VehicleCategory category;

    @Column()
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Column()
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    private VehicleFuelType fuelType;

    @Column()
    private double fuelCapacityInLiters;

    @Column()
    private double avgConsumptionPer100km;

    @Column()
    private Instant lastServiceDate;

    @Column(length = 40)
    private String insurancePolicyNumber;

    @Column()
    private Instant insuranceExpiryDate;

    @Column(length = 180)
    private String insuranceCompany;

    @Column()
    private Instant inspectionDueDate;

    @Column()
    @Enumerated(EnumType.STRING)
    private VehicleOwnershipType ownershipType;
}


//        - odomoter (last know value)
//	- insurance_policy_number, insurance_expiry, insurance_company, inspection_due_date, ownership_type (owned, leased, rented)
//	- last_service_date
//	- next_service_due_date
//	- service_interval_km
//	- service_interval_days