package com.example.demo.repositories;

import com.example.demo.model.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "address", path = "address")
public interface AddressesRepository extends JpaRepository<Addresses, Integer> {

    // GET /api/v1/address                    → findAll() — inherited, no override needed

    // GET /api/v1/address/{address_id}       → findById() — inherited, no override needed

    // GET /api/v1/address/add                → save() — inherited (POST), no override needed

    // GET /api/v1/address/update/{address_id}→ save() — inherited (PUT), no override needed

    // Exposed as: GET /api/v1/address/search/findByCity?city=Mumbai
    @RestResource(path = "findByCity", rel = "findByCity")
    List<Addresses> findByCity(@Param("city") String city);

    // Exposed as: GET /api/v1/address/search/findByState?state=Maharashtra
    @RestResource(path = "findByState", rel = "findByState")
    List<Addresses> findByState(@Param("state") String state);

    // Exposed as: GET /api/v1/address/search/findByCountry?country=India
    @RestResource(path = "findByCountry", rel = "findByCountry")
    List<Addresses> findByCountry(@Param("country") String country);

    // Exposed as: GET /api/v1/address/search/findByPostalCode?postalCode=400001
    @RestResource(path = "findByPostalCode", rel = "findByPostalCode")
     List<Addresses> findByPostalCode(@Param("postalCode") String postalCode);

    // Exposed as: GET /api/v1/address/search/findByStreetContaining?street=Main
    @RestResource(path = "findByStreetContaining", rel = "findByStreetContaining")
    List<Addresses> findByStreetContaining(@Param("street") String street);
}