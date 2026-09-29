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
    VehicleBrand brand;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;

    @Column(length = 180)
    String variant;

    @Column(length = 180)
    String model;

    @Column(length = 20)
    String plateNumber;

    @Column(length = 30)
    String vin;

    @Column(length = 4)
    int yearOfManufacture;

    @Column()
    @Enumerated(EnumType.STRING)
    VehicleCategory category;

    @Column()
    @Enumerated(EnumType.STRING)
    VehicleStatus status;

    @Column()
    @Enumerated(EnumType.STRING)
    VehicleType type;

    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    VehicleFuelType fuelType;

    @Column()
    double fuelCapacityInLiters;

    @Column()
    double avgConsumptionPer100km;

    @Column()
    Instant lastServiceDate;

    @Column(length = 40)
    String insurancePolicyNumber;

    @Column()
    Instant insuranceExpiryDate;

    @Column(length = 180)
    String insuranceCompany;

    @Column()
    Instant inspectionDueDate;

    @Column()
    @Enumerated(EnumType.STRING)
    VehicleOwnershipType ownershipType;
}


//        - odomoter (last know value)
//	- insurance_policy_number, insurance_expiry, insurance_company, inspection_due_date, ownership_type (owned, leased, rented)
//	- last_service_date
//	- next_service_due_date
//	- service_interval_km
//	- service_interval_days